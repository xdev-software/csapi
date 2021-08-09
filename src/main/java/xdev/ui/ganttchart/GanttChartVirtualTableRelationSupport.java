package xdev.ui.ganttchart;

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


import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import xdev.ui.ganttchart.action.GanttRelationManagementStrategy;
import xdev.ui.ganttchart.model.GanttPersistence;
import xdev.ui.ganttchart.model.XdevGanttRelationVTWrapper;
import xdev.ui.ganttchart.utils.GanttRelationColumnType;
import xdev.ui.ganttchart.utils.VTGanttRelationMappingConverter;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTable.VirtualTableRow;
import xdev.vt.VirtualTableColumn;

import com.jidesoft.gantt.DefaultGanttEntryRelation;
import com.jidesoft.gantt.GanttChart;
import com.jidesoft.gantt.GanttEntry;
import com.jidesoft.gantt.GanttEntryRelation;
import com.jidesoft.gantt.GanttModel;


/**
 * Support class for relational {@link GanttChart}s.
 * 
 * @param <T>
 * @param <S>
 * @author XDEV Software jwill
 * @since 4.0
 */
public class GanttChartVirtualTableRelationSupport<T, S extends UpdateableGanttEntry<T>>
{
	/**
	 * 
	 * @param chart
	 * @param relationMappings
	 * @param relationVT
	 */
	public void addRelations(GanttChart<T, S> chart,
			Map<GanttRelationColumnType, VirtualTableColumn<?>> relationMappings,
			VirtualTable relationVT)
	{
		for(int i = 0; i < chart.getModel().getEntryCount(); i++)
		{
			for(GanttEntryRelation<S> relation : getEntryRelations(chart.getModel(),chart
					.getModel().getEntryAt(i),
					VTGanttRelationMappingConverter.getEntryRelationColumnMappings(
							relationMappings,relationVT)))
			{
				chart.getModel().getGanttEntryRelationModel()
						.addEntryRelation((GanttEntryRelation<S>)relation);
			}
		}
	}
	
	
	/**
	 * 
	 * @param manager
	 * @param model
	 * @param entryPersistence
	 */
	public void validateRelationsInitially(GanttModel<T, S> model,
			GanttRelationManagementStrategy<T, S> relationManagementStrategy,
			GanttPersistence<T, S> entryPersistence)
	{
		for(int i = 0; i < model.getEntryCount(); i++)
		{
			S entry = model.getEntryAt(i);
			for(GanttEntryRelation<S> relation : model.getGanttEntryRelationModel()
					.getEntryRelations(entry))
			{
				// only persist after successful validation or update.
				if(relationManagementStrategy.manageRelation(relation))
				{
					// both relation entries could be updated.
					if(entryPersistence != null)
					{
						entryPersistence.updateEntry(relation.getPredecessorEntry());
						entryPersistence.updateEntry(relation.getSuccessorEntry());
					}
				}
			}
		}
	}
	
	
	/**
	 * Finds all {@link GanttEntryRelation}s referred to the given
	 * {@link GanttEntry}.
	 * 
	 * @param successorEntry
	 *            the identifier to for searching gantt relations.
	 * @return the collection including all collections refered to the given
	 *         entry.
	 */
	private Collection<GanttEntryRelation<S>> getEntryRelations(GanttModel<T, S> model,
			S successorEntry, XdevGanttRelationVTWrapper wrapper)
	{
		Collection<GanttEntryRelation<S>> relations = new ArrayList<GanttEntryRelation<S>>();
		
		for(int i = 0; i < wrapper.getVirtualTable().getRowCount(); i++)
		{
			VirtualTableRow row = wrapper.getVirtualTable().getRow(i);
			
			if(row.get(wrapper.getEntryId()).equals(successorEntry.getId()))
			{
				S predecessorEntry = getEntryAt(model,row.get(wrapper.getRelationRoot()));
				
				GanttEntryRelation<S> relation = new DefaultGanttEntryRelation<S>(predecessorEntry,
						successorEntry,row.get(wrapper.getRelationType()));
				relations.add(relation);
			}
		}
		return relations;
	}
	
	
	/**
	 * Returns this models {@link GanttEntry} with the given id.
	 * 
	 * @param id
	 *            the entries id.
	 * @return the entry with the given id.
	 * @throws NullPointerException
	 *             if no entry with the given id was found.
	 */
	public S getEntryAt(GanttModel<T, S> model, Object id) throws NullPointerException
	{
		for(int i = 0; i < model.getEntryCount(); i++)
		{
			if(model.getEntryAt(i).getId().equals(id))
			{
				return model.getEntryAt(i);
			}
		}
		
		throw new NullPointerException("No Entry found with id " + id);
	}
}
