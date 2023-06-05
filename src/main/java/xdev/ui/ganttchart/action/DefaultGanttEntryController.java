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
import xdev.ui.ganttchart.template.GanttTemplate;

import com.jidesoft.gantt.GanttEntry;
import com.jidesoft.gantt.GanttEntryRelation;


/**
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
public class DefaultGanttEntryController<T extends Comparable<T>, S extends UpdateableGanttEntry<T>>
		implements GanttEntryController<T, S>
{
	
	private final GanttTemplate<T, S>				template;
	/**
	 * the currently used {@link GanttRelationValidationStrategy} to validate
	 * {@link GanttEntryRelation} restrictions.
	 */
	private GanttRelationValidationStrategy<T, S>	relationValidationStrategy	= new DefaultRelationValidationStrategy<T, S>();
	
	
	/**
	 * 
	 * 
	 * @param template
	 *            the <code>GanttChart</code>s model to relate the changed
	 *            entrys which are to persist.
	 * 
	 */
	public DefaultGanttEntryController(GanttTemplate<T, S> template)
	{
		this.template = template;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateEntry(S entry)
	{
		// persist only if user action is finished
		if(!entry.isAdjusting())
		{
			for(GanttEntryRelation<S> relation : this.template.getGanttChart().getModel()
					.getGanttEntryRelationModel().getEntryRelations(entry))
			{
				// both relation entries could be updated.
				if(this.template.getGanttPersistence() != null)
				{
					this.template.getGanttPersistence().updateEntry(relation.getPredecessorEntry());
					this.template.getGanttPersistence().updateEntry(relation.getSuccessorEntry());
				}
			}
			if(template.getGanttChart().getModel().getGanttEntryRelationModel()
					.getEntryRelations(entry).size() <= 0)
			{
				// update the related entries without ganttRelations
				if(template.getGanttPersistence() != null)
				{
					template.getGanttPersistence().updateEntry(entry);
				}
			}
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeEntry(S entry)
	{
		if(this.template.getGanttPersistence() != null)
		{
			this.template.getGanttPersistence().removeEntry(entry);
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void insertEntry(S entry)
	{
		if(this.template.getGanttPersistence() != null)
		{
			this.template.getGanttPersistence().addEntry(entry);
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setRelationValidationStrategy(
			GanttRelationValidationStrategy<T, S> relationManagementStrategy)
	{
		this.relationValidationStrategy = relationManagementStrategy;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public GanttRelationValidationStrategy<T, S> getRelationValidationStrategy()
	{
		return this.relationValidationStrategy;
	}
}
