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
package xdev.ui.ganttchart.action;


import xdev.ui.ganttchart.UpdateableGanttEntry;

import com.jidesoft.gantt.GanttChart;
import com.jidesoft.gantt.GanttEntryRelation;
import com.jidesoft.gantt.GanttModel;


/**
 * 
 * 
 * 
 * @param <T>
 * @param <S>
 * 
 * @author XDEV Software jwill
 * @since 4.0
 */
public abstract class AbstractRelationManagementStrategy<T, S extends UpdateableGanttEntry<T>>
		implements GanttRelationManagementStrategy<T, S>
{
	/**
	 * Initially validates the existing relations within the {@link GanttChart}.
	 * 
	 * @param model
	 *            the <code>Chart</code> to validate.
	 */
	public AbstractRelationManagementStrategy(GanttModel<T, S> model)
	{
		// validate relations
		for(int i = 0; i < model.getEntryCount(); i++)
		{
			for(GanttEntryRelation<S> relation : model.getGanttEntryRelationModel()
					.getEntryRelations(model.getEntryAt(i)))
			{
				this.manageRelation(relation);
			}
		}
	}
	
	
	public AbstractRelationManagementStrategy()
	{
		super();
	}
}
