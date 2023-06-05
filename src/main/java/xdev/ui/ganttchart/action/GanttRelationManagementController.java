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
import xdev.ui.ganttchart.model.GanttRelationPersistence;

import com.jidesoft.gantt.GanttEntry;


/**
 * This is the controller layer between {@link GanttRelationPersistence} and the
 * GanttChart relation view.
 * 
 * <p>
 * If the upcoming GanttRelations aren�t legal, special implementation details
 * notify the user about the mistake.
 * </p>
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
 * @author XDEV Software jwill
 * @since 4.0
 * 
 * @see GanttRelationValidationStrategy
 * 
 */
public interface GanttRelationManagementController<T extends Comparable<T>, S extends UpdateableGanttEntry<T>>
{
	/**
	 * Validates the upcomming entry relationship.
	 * <p>
	 * Concrete implementations like {@link StSRelationManagementController}
	 * verify the entries start and end
	 * </p>
	 * 
	 * @param precessor
	 *            the precessor entry of the relationship.
	 * @param sucessor
	 *            the sucessor entry of the relationship.
	 * @return returns the validation boolean.
	 * 
	 * @see FtFRelationManagementController
	 * @see StFRelationManagementController
	 */
	public boolean validateEntryRelation(S precessor, S sucessor);
}
