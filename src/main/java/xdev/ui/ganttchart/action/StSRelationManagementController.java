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


/**
 * Validates Start to Start GanttEntryRelationships.
 * 
 * @param <T>
 *            the gantt data type, for example <code>Date</code> or
 *            <code>Integer</code>. Has to extend {@link Comparable} for
 *            validation purpose.
 *            <p>
 *            Used for entry scaling via {@link GanttEntry#getRange()}.
 *            </p>
 * 
 * @param <S>
 *            the customized {@link GanttEntry} type.
 * 
 * @author XDEV Software jwill
 * @since 4.0
 */
public class StSRelationManagementController<T extends Comparable<T>, S extends UpdateableGanttEntry<T>>
		implements GanttRelationManagementController<T, S>
{
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean validateEntryRelation(S precessor, S sucessor)
	{
		// precessor must be equals or greater then the sucessor start
		if(precessor.getRange().lower().compareTo(sucessor.getRange().lower()) <= 0)
		{
			return true;
		}
		return false;
	}
	
}
