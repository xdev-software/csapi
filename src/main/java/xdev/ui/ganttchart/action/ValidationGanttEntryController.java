package xdev.ui.ganttchart.action;

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


import xdev.ui.ganttchart.UpdateableGanttEntry;
import xdev.ui.ganttchart.template.RelationalGanttTemplate;

import com.jidesoft.gantt.GanttEntry;
import com.jidesoft.gantt.GanttEntryRelation;


/**
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
public class ValidationGanttEntryController<T extends Comparable<T>, S extends UpdateableGanttEntry<T>>
		implements GanttEntryController<T, S>
{
	
	/**
	 * the currently used {@link GanttRelationValidationStrategy} to validate
	 * {@link GanttEntryRelation} restrictions.
	 */
	private GanttRelationValidationStrategy<T, S>	relationValidationStrategy;
	private final RelationalGanttTemplate<T, S>		template;
	
	
	/**
	 * 
	 * 
	 * @param model
	 *            the <code>GanttChart</code>s model to relate the changed
	 *            entries which are to persist.
	 * 
	 * @param persistence
	 *            the <code>GanttChart</code>s persistence.
	 * 
	 */
	public ValidationGanttEntryController(RelationalGanttTemplate<T, S> template)
	{
		this.template = template;
	}
	
	
	/**
	 * 
	 * 
	 * @param model
	 *            the <code>GanttChart</code>s model to relate the changed
	 *            entries which are to persist.
	 * 
	 * @param persistence
	 *            the <code>GanttChart</code>s persistence.
	 * 
	 */
	public ValidationGanttEntryController(RelationalGanttTemplate<T, S> template,
			GanttRelationManagementStrategy<T, S> strategy)
	{
		this.template = template;
		
		this.getRelationValidationStrategy().setGanttEntryRelationManagementStrategy(strategy);
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
			if(this.getRelationValidationStrategy() != null)
			{
				for(GanttEntryRelation<S> relation : this.template.getGanttChart().getModel()
						.getGanttEntryRelationModel().getEntryRelations(entry))
				{
					// only persist after successful validation or update.
					if(this.getRelationValidationStrategy().validateRelation(relation))
					{
						// both relation entries could be updated.
						if(template.getGanttPersistence() != null)
						{
							template.getGanttPersistence().updateEntry(
									relation.getPredecessorEntry());
							template.getGanttPersistence()
									.updateEntry(relation.getSuccessorEntry());
						}
					}
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
		if(template.getGanttPersistence() != null) // no persistence is be valid
		{
			template.getGanttPersistence().removeEntry(entry);
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void insertEntry(S entry)
	{
		if(template.getGanttPersistence() != null) // no persistence is be valid
		{
			template.getGanttPersistence().addEntry(entry);
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
		if(this.relationValidationStrategy == null)
		{
			this.relationValidationStrategy = new DefaultRelationUpdateValidationStrategy<T, S>();
			return this.relationValidationStrategy;
		}
		else
		{
			return this.relationValidationStrategy;
		}
	}
	
}
