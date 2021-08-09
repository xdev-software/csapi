package xdev.ui.ganttchart.model;

/*-
 * #%L
 * XDEV BI Suite
 * %%
 * Copyright (C) 2011 - 2021 XDEV Software
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */


import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;

import xdev.db.DataType;
import xdev.ui.ganttchart.UpdateableGanttEntry;
import xdev.ui.ganttchart.template.XdevGanttTemplate;
import xdev.ui.ganttchart.utils.VirtualTableGanttPersistenceUtils;
import xdev.vt.VirtualTable.VirtualTableRow;
import xdev.vt.VirtualTableAdapter;
import xdev.vt.VirtualTableColumn;
import xdev.vt.VirtualTableEvent;

import com.jidesoft.gantt.DefaultGanttModel;
import com.jidesoft.range.TimeRange;


/**
 * 
 * @author XDEV Software jwill
 * @since 4.0
 * @param <EntryType>
 */
public final class DateGanttDataVTSyncronizer<EntryType extends UpdateableGanttEntry<Date>> extends
		VirtualTableAdapter implements PropertyChangeListener
{
	private final EntryVTMapper<EntryType, Date>		mapper;
	private final DefaultGanttModel<Date, EntryType>	model;
	private final Object								rootIdentifier;
	private boolean										dbSync	= false;
	
	
	/**
	 * 
	 * @param model
	 * @param mapper
	 * @param rootIdentifier
	 */
	public DateGanttDataVTSyncronizer(DefaultGanttModel<Date, EntryType> model,
			EntryVTMapper<EntryType, Date> mapper, Object rootIdentifier)
	{
		this.mapper = mapper;
		this.model = model;
		this.rootIdentifier = rootIdentifier;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void virtualTableRowInserted(VirtualTableEvent event)
	{
		VirtualTableRow row = event.getRow();
		EntryType entry = mapper.dataToGanttEntry(row);
		
		if(!this.dbSync)
		{
			// generate a unique id in this case, as otherwise -1 (= default id
			// value) would get
			// reused again and again
			this.generateSetIDFor(entry,row);
		}
		
		for(int i = 0; i < model.getEntryCount(); i++)
		{
			if(model.getEntryAt(i).getId().equals(entry.getId()))
			{
				return; // avoid endless chaining and keep parent integrity
			}
		}
		
		if(entry.getRoot() != this.rootIdentifier)
		{
			for(int i = 0; i < model.getEntryCount(); i++)
			{
				if(model.getEntryAt(i).getId().equals(entry.getRoot()))
				{
					model.getEntryAt(i).addChild(entry);
				}
			}
		}
		else
		{
			model.addGanttEntry(entry);
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void virtualTableRowUpdated(VirtualTableEvent event)
	{
		VirtualTableRow row = event.getRow();
		EntryType entry = mapper.dataToGanttEntry(row);
		EntryType existingEntry = null;
		
		for(int i = 0; i < model.getEntryCount(); i++)
		{
			if(model.getEntryAt(i).getId().equals(entry.getId()))
			{
				existingEntry = model.getEntryAt(i);
				break;
			}
		}
		if(existingEntry != null)
		{
			if(!existingEntry.isAdjusting())
			{
				if(!equalsUpdateableValues(entry,existingEntry))
				{
					syncronizeChangedFields(entry,existingEntry);
				}
			}
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void virtualTableRowDeleted(VirtualTableEvent event)
	{
		VirtualTableRow row = event.getRow();
		EntryType entry = mapper.dataToGanttEntry(row);
		EntryType existingEntry = null;
		
		for(int i = 0; i < model.getEntryCount(); i++)
		{
			if(model.getEntryAt(i).getId().equals(entry.getId()))
			{
				existingEntry = model.getEntryAt(i);
				break;
			}
		}
		if(existingEntry != null)
		{
			model.removeGanttEntry(existingEntry);
		}
	}
	
	
	private void generateSetIDFor(EntryType entry, VirtualTableRow row)
	{
		VirtualTableColumn<?> idColumn = this.mapper.getDataContainer().getId();
		
		int transientUID = (int)System.nanoTime();
		
		// updates the virtualTableRow connected to this activity
		if(idColumn.getType() == DataType.INTEGER || idColumn.getType() == DataType.BIGINT)
		{
			// uses the lower bytes of nanoTime to get (relatively safe)
			// unique
			// ids
			entry.setId(transientUID);
			row.set(idColumn.getName(),transientUID);
		}
		if(idColumn.getType() == DataType.VARCHAR || idColumn.getType() == DataType.CHAR)
		{
			String transientStringId = "" + transientUID;
			entry.setId(transientStringId);
			row.set(idColumn.getName(),transientStringId);
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt)
	{
		if(evt.getPropertyName().equals(XdevGanttTemplate.GANTT_PERSISTENCE_STATE))
		{
			this.dbSync = (Boolean)evt.getNewValue();
		}
	}
	
	
	private boolean equalsUpdateableValues(UpdateableGanttEntry<Date> entry1,
			UpdateableGanttEntry<Date> entry2)
	{
		if(entry2 == null)
		{
			return false;
		}
		else
		{
			if(entry1.getName().equals(entry2.getName()))
			{
				if(VirtualTableGanttPersistenceUtils.compareTimeRange(entry1.getRange(),
						entry2.getRange()))
				{
					if(entry1.getCompletion() == entry2.getCompletion())
					{
						return true;
					}
					else
					{
						return false;
					}
				}
				else
				{
					return false;
				}
			}
			else
			{
				return false;
			}
		}
	}
	
	
	private void syncronizeChangedFields(UpdateableGanttEntry<Date> updatedEntry,
			UpdateableGanttEntry<Date> originalEntry)
	{
		if(!updatedEntry.getName().equals(originalEntry.getName()))
		{
			originalEntry.setName(updatedEntry.getName());
		}
		else if(updatedEntry.getCompletion() != originalEntry.getCompletion())
		{
			originalEntry.setCompletion(updatedEntry.getCompletion());
		}
		else if(!updatedEntry.getRange().lower().equals(originalEntry.getRange().lower())
				|| !updatedEntry.getRange().upper().equals(originalEntry.getRange().upper()))
		{
			originalEntry.setRange(new TimeRange(updatedEntry.getRange().lower(),updatedEntry
					.getRange().upper()));
		}
	}
}
