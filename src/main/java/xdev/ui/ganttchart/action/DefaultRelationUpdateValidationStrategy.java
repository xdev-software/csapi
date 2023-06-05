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

import com.jidesoft.gantt.GanttEntry;
import com.jidesoft.gantt.GanttEntryRelation;


/**
 * The concrete implementation of {@link GanttRelationValidationStrategy} which
 * is just an empty default wrapper.
 * 
 * 
 * 
 * @param <S>
 *            the customized {@link GanttEntry} type.
 * 
 * @param <T>
 *            the gantt data type, for example <code>Date</code> or
 *            <code>Integer</code>. Has to extend {@link Comparable} for
 *            validation purpose.
 *            <p>
 *            Used for entry scaling via {@link GanttEntry#getRange()}
 *            </p>
 * 
 * @author XDEV Software jwill
 * @since 4.0
 */
public class DefaultRelationUpdateValidationStrategy<T extends Comparable<T>, S extends UpdateableGanttEntry<T>>
		implements GanttRelationValidationStrategy<T, S>
{
	private GanttRelationManagementStrategy<T, S>	relationManagementStrategy;
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean validateRelation(GanttEntryRelation<S> relation)
	{
		// default implementation uses default rules
		return this.getGanttEntryRelationManagementStrategy().manageRelation(relation);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setGanttEntryRelationManagementStrategy(
			GanttRelationManagementStrategy<T, S> strategy)
	{
		this.relationManagementStrategy = strategy;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public GanttRelationManagementStrategy<T, S> getGanttEntryRelationManagementStrategy()
	{
		if(this.relationManagementStrategy == null)
		{
			return new UIRelationManagementStrategy<T, S>();
		}
		else
		{
			return this.relationManagementStrategy;
		}
	}
}
