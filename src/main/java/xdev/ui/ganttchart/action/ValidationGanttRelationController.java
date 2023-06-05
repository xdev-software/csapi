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
import xdev.ui.ganttchart.template.RelationalGanttTemplate;

import com.jidesoft.gantt.GanttEntry;
import com.jidesoft.gantt.GanttEntryRelation;
import com.jidesoft.gantt.GanttEntryRelationModel;


/**
 * Manages the dis/-connection betweeen {@link GanttEntry}s.
 * <p>
 * This concrete implementation validates the {@link GanttEntryRelation}.
 * </p>
 * 
 * 
 * 
 * @param <T>
 *            the gantt data type, for example <code>Date</code> or
 *            <code>Integer</code>. Has to extend {@link Comparable} for
 *            validation purpose.
 *            <p>
 *            Used for entry scaling via {@link GanttEntry#getRange()}
 *            </p>
 * @param <S>
 *            the customized {@link GanttEntry} type.
 * 
 * @author XDEV Software jwill
 * @since 4.0
 * 
 */
public class ValidationGanttRelationController<T extends Comparable<T>, S extends UpdateableGanttEntry<T>>
		implements GanttRelationController<T, S>
{
	
	/**
	 * the specific implementation of {@link GanttRelationValidationStrategy}
	 * which informs if the validation failed.
	 */
	private GanttRelationValidationStrategy<T, S>	strategy;
	
	private final RelationalGanttTemplate<T, S>		template;
	
	
	/**
	 * 
	 * @param model
	 *            the {@link GanttEntryRelationModel} to register the upcoming
	 *            relations.
	 */
	public ValidationGanttRelationController(RelationalGanttTemplate<T, S> template)
	{
		this.template = template;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setRelationValidationStrategy(GanttRelationValidationStrategy<T, S> strategy)
	{
		this.strategy = strategy;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public GanttRelationValidationStrategy<T, S> getRelationValidationStrategy()
	{
		if(this.strategy == null)
		{
			return new DefaultRelationValidationStrategy<T, S>();
		}
		else
		{
			return this.strategy;
		}
	}
	
	
	@Override
	public void removeEntryRelation(GanttEntryRelation<S> relation)
	{
		//no persistence is valid
		if(this.template.getGanttRelationPersistence() != null)
		{
			this.template.getGanttRelationPersistence().removeRelation(relation);
		}
	}
	
	
	@Override
	public void insertEntryRelation(GanttEntryRelation<S> relation)
	{
		if(this.getRelationValidationStrategy() != null)
		{
			if(this.getRelationValidationStrategy().validateRelation(relation))
			{
				if(this.template.getGanttRelationPersistence() != null) // no
				// persistence
				// is
				// valid
				{
					this.template.getGanttRelationPersistence().addRelation(relation);
				}
			}
		}
	}
}
