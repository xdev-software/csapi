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


import xdev.db.DBException;
import xdev.db.DBUtils;
import xdev.ui.ganttchart.UpdateableGanttEntry;
import xdev.ui.ganttchart.utils.VirtualTableGanttPersistenceUtils;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTable.VirtualTableRow;
import xdev.vt.VirtualTableException;

import com.jidesoft.gantt.GanttChart;
import com.jidesoft.gantt.GanttEntry;
import com.jidesoft.gantt.GanttEntryRelation;


/**
 * The {@link VirtualTable} implementation for {@link GanttRelationPersistence}.
 * <p>
 * This persistence is responsible to transmit all changes within the
 * {@link GanttChart} or implicitly its {@link GanttEntryRelation}s to the data
 * provider, which is in this case a {@link VirtualTable}
 * </p>
 * 
 * @param <T>
 *            the gantt data type, for example <code>Date</code> or
 *            <code>Integer</code>.
 * @param <S>
 *            the custom {@link GanttEntry} type.
 * 
 * @author XDEV Software jwill
 * @since 4.0
 */
public class VirtualTableGanttRelationPersistence<T, S extends UpdateableGanttEntry<T>> implements
		GanttRelationPersistence<S>
{
	/**
	 * the {@link VirtualTable} which is the data provider and connector for
	 * this concrete persistence layer.
	 */
	private VirtualTable				relationGanttVt;
	
	private boolean						dbSync;
	
	private XdevGanttRelationVTWrapper	relationContainer;
	
	
	/**
	 * 
	 * @param dataInformation
	 *            the {@link GanttDataInformation} which provides connection and
	 *            mapping information about the {@link GanttEntry}s.
	 */
	public VirtualTableGanttRelationPersistence(XdevGanttRelationVTWrapper relationContainer,
			boolean dbSync)
	{
		this.relationGanttVt = relationContainer.getVirtualTable();
		this.dbSync = dbSync;
		this.relationContainer = relationContainer;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addRelation(GanttEntryRelation<S> relation)
	{
		// create row does not sync with db
		VirtualTableRow newVTRow = this.relationGanttVt.createRow();
		
		this.transmitEntryValues(relation,newVTRow);
		
		// persist row
		try
		{
			newVTRow = this.relationGanttVt.addRow(newVTRow,dbSync);
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
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeRelation(GanttEntryRelation<S> relation)
	{
		try
		{
			VirtualTableGanttPersistenceUtils
					.getEntryRelationRowAt(relation,this.relationContainer).delete(this.dbSync);
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
	
	
	/**
	 * Transmits the relations data to its {@link VirtualTableRow}
	 * representation.
	 * 
	 * @param relation
	 *            the current {@link GanttEntryRelation} to transmit.
	 * @param vtRow
	 *            the {@link VirtualTableRow} to fill.
	 */
	private void transmitEntryValues(GanttEntryRelation<S> relation, VirtualTableRow vtRow)
	{
		if(relation.getPredecessorEntry() != null)
		{
			S pre = relation.getPredecessorEntry();
			
			vtRow.set(
					this.relationGanttVt.getColumnIndex(this.relationContainer.getRelationRoot()),
					pre.getId());
			
			S suc = relation.getSuccessorEntry();
			vtRow.set(this.relationGanttVt.getColumnIndex(this.relationContainer.getEntryId()),
					suc.getId());
			
			vtRow.set(
					this.relationGanttVt.getColumnIndex(this.relationContainer.getRelationType()),
					relation.getRelationType());
			
			// id fallback
			if(!this.relationContainer.getId().isAutoIncrement())
			{
				try
				{
					vtRow.set(this.relationGanttVt.getColumnIndex(this.relationContainer.getId()),
							DBUtils.getMaxValue(this.relationGanttVt.getName(),
									this.relationContainer.getId().getName()));
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
		}
	}
}
