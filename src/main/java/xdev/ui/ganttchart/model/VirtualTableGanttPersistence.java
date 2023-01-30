/*
 * XDEV Component Suite - XDEV Component Suite
 * Copyright © 2011 XDEV Software (https://xdev.software)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package xdev.ui.ganttchart.model;


import java.util.HashMap;
import java.util.Map;

import xdev.db.DBException;
import xdev.db.DBUtils;
import xdev.ui.ganttchart.UpdateableGanttEntry;
import xdev.ui.ganttchart.utils.VirtualTableGanttPersistenceUtils;
import xdev.ui.ganttchart.utils.XdevGanttPersistenceCalculationUtils;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTable.VirtualTableRow;
import xdev.vt.VirtualTableException;

import com.jidesoft.gantt.GanttChart;
import com.jidesoft.gantt.GanttEntry;
import com.jidesoft.range.Range;


/**
 * The {@link VirtualTable} implementation for {@link GanttRelationPersistence}.
 * <p>
 * This persistence is responsible to transmit all changes within the
 * {@link GanttChart} or implicitly its {@link GanttEntry}s to the data
 * provider, which is in this case a {@link VirtualTable}
 * </p>
 * 
 * @param <T>
 *            the gantt data type, for example <code>Date</code> or
 *            <code>Integer</code>.
 * 
 * @author XDEV Software jwill
 * @since 4.0
 */
public class VirtualTableGanttPersistence<T, S extends UpdateableGanttEntry<T>> implements
		GanttPersistence<T, S>
{
	/**
	 * the {@link VirtualTable} which is the data provider and connector for
	 * this concrete persistence layer.
	 */
	private XdevGanttEntryVTMappings<T>	dataContainer;
	private boolean						dbSync;
	private Object						rootIdentifier;
	
	
	/**
	 * 
	 * @param dataInformation
	 *            the {@link GanttDataInformation} which provides connection and
	 *            mapping information about the {@link GanttEntry}s.
	 */
	public VirtualTableGanttPersistence(XdevGanttEntryVTMappings<T> dataContainer, boolean dbSync,
			Object rootIdentifier)
	{
		this.dataContainer = dataContainer;
		this.dbSync = dbSync;
		this.rootIdentifier = rootIdentifier;
		
		// maybe create seperate property - loadDataAtStart
		if(dbSync)
		{
			VirtualTableGanttPersistenceUtils.fillData(dataContainer.getVirtualTable(),
					dataContainer.getId());
		}
	}
	
	
	/**
	 * Triggers the synchronization of the VirtualTable with the database, if
	 * {@code synchronizeWithDB} is {@code true}.
	 */
	private void syncWithDB()
	{
		if(this.dbSync)
		{
			try
			{
				dataContainer.getVirtualTable().synchronizeChangedRows();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * 
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public void removeEntry(S entry)
	{
		
	}
	
	
	/**
	 * 
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public void updateEntry(S entry)
	{
		for(int rowIndex = 0; rowIndex < this.dataContainer.getVirtualTable().getRowCount(); rowIndex++)
		{
			Object currentId = this.dataContainer.getVirtualTable().getValueAt(rowIndex,
					this.dataContainer.getId());
			if(currentId.equals(entry.getId()))
			{
				Map<String, Object> rowUpdateData = new HashMap<String, Object>();
				rowUpdateData.put(this.dataContainer.getStart().getName(),entry.getRange().lower());
				rowUpdateData.put(this.dataContainer.getEnd().getName(),entry.getRange().upper());
				
				if(this.dataContainer.getDescription() != null)
				{
					rowUpdateData
							.put(this.dataContainer.getDescription().getName(),entry.getName());
				}
				if(this.dataContainer.getRoot() != null)
				{
					rowUpdateData.put(this.dataContainer.getRoot().getName(),entry.getRoot());
				}
				if(this.dataContainer.getCompletion() != null)
				{
					this.modifyParentCompletionValue(entry);
					rowUpdateData.put(this.dataContainer.getCompletion().getName(),
							entry.getCompletion());
				}
				
				try
				{
					this.dataContainer.getVirtualTable().updateRow(rowUpdateData,rowIndex,
							this.dbSync);
				}
				catch(VirtualTableException e)
				{
					e.printStackTrace();
				}
				catch(DBException e)
				{
					e.printStackTrace();
				}
				// unique id filter
				break;
			}
		}
		this.syncWithDB();
	}
	
	
	/**
	 * Sets the completion value of the parent related to its child completion
	 * values.
	 * 
	 * @param parentEntry
	 *            the parentEntry to modify.
	 */
	private void modifyParentCompletionValue(UpdateableGanttEntry<T> parentEntry)
	{
		if(parentEntry.getChildrenCount() > 0)
		{
			double completion = XdevGanttPersistenceCalculationUtils
					.getParentCompletionValue(parentEntry);
			
			if(parentEntry.getCompletion() != completion)
			{
				parentEntry.setCompletion(completion);
			}
		}
	}
	
	
	/**
	 * 
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public void addEntry(S entry)
	{
		if(!this.idExists(this.dataContainer.getVirtualTable(),entry.getId()))
		{
			VirtualTableRow newVTRow = this.dataContainer.getVirtualTable().createRow();
			this.transmitEntryValues(entry,newVTRow);
			
			try
			{
				newVTRow = this.dataContainer.getVirtualTable().addRow(newVTRow,this.dbSync);
			}
			catch(VirtualTableException e)
			{
				e.printStackTrace();
			}
			catch(DBException e)
			{
				e.printStackTrace();
			}
			
			// // syncronize data generated fields // //
			this.syncronizeID(entry,newVTRow);
		}
	}
	
	
	/**
	 * Transmits the relations data to its {@link VirtualTableRow}
	 * representation.
	 * 
	 * @param entry
	 *            the current {@link GanttEntry} to transmit.
	 * @param vtRow
	 *            the {@link VirtualTableRow} to fill.
	 * 
	 */
	private void transmitEntryValues(UpdateableGanttEntry<T> entry, VirtualTableRow vtRow)
	{
		Range<T> range = entry.getRange();
		
		if(entry.getId() == null && !this.dataContainer.getId().isAutoIncrement())
		{
			try
			{
				vtRow.set(
						this.dataContainer.getVirtualTable().getColumnIndex(
								this.dataContainer.getId().getName()),DBUtils.getMaxValue(
								this.dataContainer.getVirtualTable().getName(),this.dataContainer
										.getId().getName()));
			}
			catch(VirtualTableException e)
			{
				e.printStackTrace();
			}
			catch(DBException e)
			{
				e.printStackTrace();
			}
		}
		// else vt#addRow gets id autoincrement logic from db
		
		if(entry.getName() != null && this.dataContainer.getDescription() != null)
		{
			vtRow.set(
					this.dataContainer.getVirtualTable().getColumnIndex(
							this.dataContainer.getDescription().getName()),entry.getName());
		}
		
		// mandatory
		if(range != null)
		{
			vtRow.set(
					this.dataContainer.getVirtualTable().getColumnIndex(
							this.dataContainer.getStart().getName()),range.lower());
			
			vtRow.set(
					this.dataContainer.getVirtualTable().getColumnIndex(
							this.dataContainer.getEnd().getName()),range.upper());
		}
		
		if(new Double(entry.getCompletion()) != null && this.dataContainer.getCompletion() != null)
		{
			vtRow.set(
					this.dataContainer.getVirtualTable().getColumnIndex(
							this.dataContainer.getCompletion().getName()),entry.getCompletion());
		}
		
		if(entry.getParent() != null)
		{
			// jide wraps his treetable nodes in a invisible expandable tree
			// node which has level -1
			if(entry.getParent().getLevel() == -1)
			{
				// set rootidentifier to new root entry
				vtRow.set(
						this.dataContainer.getVirtualTable().getColumnIndex(
								this.dataContainer.getRoot().getName()),this.rootIdentifier);
			}
			else
			{
				// there is no other way to reach the id
				// cast ok here because this is a VT implementation and all
				// entries must have this interface base type
				@SuppressWarnings("unchecked")
				UpdateableGanttEntry<T> parentEntry = (UpdateableGanttEntry<T>)entry.getParent();
				
				vtRow.set(
						this.dataContainer.getVirtualTable().getColumnIndex(
								this.dataContainer.getRoot().getName()),parentEntry.getId());
			}
		}
	}
	
	
	/**
	 * Syncronizes the VT-Id with the entry.
	 * 
	 * @param entry
	 *            the {@link GanttEntry} to syncronize the id with.
	 * @param row
	 *            the row to get the previoussly set id from.
	 */
	private void syncronizeID(UpdateableGanttEntry<T> entry, VirtualTableRow row)
	{
		if(entry.getId() == null)
		{
			entry.setId(row.get(this.dataContainer.getId()));
		}
	}
	
	
	private boolean idExists(VirtualTable vt, Object id)
	{
		boolean idExists = false;
		
		for(int i = 0; i < vt.getRowCount(); i++)
		{
			if(vt.getRow(i).get(dataContainer.getId()).equals(id))
			{
				idExists = true;
				break;
			}
		}
		
		return idExists;
	}
}
