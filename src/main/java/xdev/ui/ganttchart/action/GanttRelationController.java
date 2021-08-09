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
import com.jidesoft.gantt.GanttEntryRelationModel;


/**
 * Relation controller either for simple connection without logic, or with
 * connection compatibility check.
 * 
 * 
 * @param <T>
 *            the gantt data type, for example <code>Date</code> or
 *            <code>Integer</code>.
 *            <p>
 *            Used for entry scaling via {@link GanttEntry#getRange()}
 *            </p>
 * 
 * 
 * @param <S>
 *            the customized {@link GanttEntry} type.
 * 
 * @author XDEV Software jwill
 * @since 4.0
 */
public interface GanttRelationController<T, S extends UpdateableGanttEntry<T>>
{
	
	/**
	 * Handles the propper deletion of the given {@link GanttEntry}.
	 * 
	 * @param entry
	 *            the {@link GanttEntry} to delete.
	 */
	public void removeEntryRelation(GanttEntryRelation<S> relation);
	
	
	/**
	 * Handles the propper insertion of the given {@link GanttEntry}.
	 * 
	 * @param entry
	 *            the {@link GanttEntry} to insert.
	 */
	public void insertEntryRelation(GanttEntryRelation<S> relation);
	
	
	/**
	 * Set the {@link GanttRelationValidationStrategy} to use for validation
	 * error notification.
	 * 
	 * @param strategy
	 *            the strategy to use to notify if the validation of the
	 *            upcoming relation failed.
	 */
	public void setRelationValidationStrategy(GanttRelationValidationStrategy<T, S> strategy);
	
	
	/**
	 * Returns the {@link GanttRelationValidationStrategy} used for validation
	 * error notifying.
	 * <p>
	 * The default strategy is {@link UIRelationManagementStrategy}.
	 * </p>
	 * 
	 * @return the previously set {@link GanttRelationValidationStrategy}.
	 */
	public GanttRelationValidationStrategy<T, S> getRelationValidationStrategy();
	
	
	
	public class DefaultGanttRelationController<T, S extends UpdateableGanttEntry<T>> implements
			GanttRelationController<T, S>
	{
		private final RelationalGanttTemplate<T, S>	template;
		
		
		/**
		 * 
		 * @param model
		 *            the {@link GanttEntryRelationModel} to register the
		 *            upcoming relations.
		 */
		public DefaultGanttRelationController(RelationalGanttTemplate<T, S> template)
		{
			this.template = template;
		}
		
		
		@Override
		public void setRelationValidationStrategy(
				GanttRelationValidationStrategy<T, S> relationValidationStrategy)
		{
		}
		
		
		@Override
		public GanttRelationValidationStrategy<T, S> getRelationValidationStrategy()
		{
			return null;
		}
		
		
		@Override
		public void removeEntryRelation(GanttEntryRelation<S> relation)
		{
			if(this.template.getGanttRelationPersistence() != null)
			{
				this.template.getGanttRelationPersistence().removeRelation(relation);
			}
		}
		
		
		@Override
		public void insertEntryRelation(GanttEntryRelation<S> relation)
		{
			if(this.template.getGanttRelationPersistence() != null)
			{
				this.template.getGanttRelationPersistence().addRelation(relation);
			}
		}
	}
}
