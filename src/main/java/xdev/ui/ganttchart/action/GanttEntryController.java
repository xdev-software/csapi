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

import com.jidesoft.gantt.GanttEntry;
import com.jidesoft.gantt.GanttEntryRelation;
import com.jidesoft.gantt.GanttModelEvent;


/**
 * Delegates {@link GanttModelEvent} actions.
 * 
 * 
 * @param <T>
 *            the gantt data type, for example <code>Date</code> or
 *            <code>Integer</code>.
 *            <p>
 *            Used for entry scaling via {@link GanttEntry#getRange()}
 *            </p>
 * 
 * @param <S>
 *            the customized {@link GanttEntry} type.
 * 
 * @author XDEV Software JWill
 * @since 4.0
 */
public interface GanttEntryController<T, S extends UpdateableGanttEntry<T>>
{
	/**
	 * Handles the proper update of the given {@link GanttEntry}.
	 * <p>
	 * Also takes care of the {@link GanttEntry} range restrictions through
	 * {@link GanttEntryRelation}s, if a {@link GanttRelationValidationStrategy}
	 * is set.
	 * </p>
	 * 
	 * 
	 * @param entry
	 *            the {@link GanttEntry} to update.
	 */
	public void updateEntry(S entry);
	
	
	/**
	 * Handles the propper deletion of the given {@link GanttEntry}.
	 * 
	 * @param entry
	 *            the {@link GanttEntry} to delete.
	 */
	public void removeEntry(S entry);
	
	
	/**
	 * Handles the propper insertion of the given {@link GanttEntry}.
	 * 
	 * @param entry
	 *            the {@link GanttEntry} to insert.
	 */
	public void insertEntry(S entry);
	
	
	/**
	 * @param relationValidationStrategy
	 *            the {@link GanttRelationValidationStrategy} to set.
	 */
	public void setRelationValidationStrategy(
			GanttRelationValidationStrategy<T, S> relationValidationStrategy);
	
	
	/**
	 * @return the {@link GanttRelationValidationStrategy}.
	 */
	public GanttRelationValidationStrategy<T, S> getRelationValidationStrategy();
	
}
