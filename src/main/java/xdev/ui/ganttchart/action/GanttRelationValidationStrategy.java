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
 * Notifies the user whether the relation validation failed or succeeded.
 * <p>
 * This is the position where the user can determine his own relation rules or
 * adapt existing relation rules.
 * </p>
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
 * @see StSRelationManagementController
 * @see StFRelationManagementController
 * @see FtFRelationManagementController
 * @see FtSRelationManagementController
 */
public interface GanttRelationValidationStrategy<T, S extends UpdateableGanttEntry<T>> extends
		GanttEntryRelationManager<T, S>
{
	/**
	 * Validates each upcoming relation case.
	 * 
	 * @param relation
	 *            the {@link GanttEntryRelation} to validate.
	 * @return whether the validation was successfull.
	 */
	public boolean validateRelation(GanttEntryRelation<S> relation);
}
