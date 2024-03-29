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
 * 
 * Validates Finish to Finish GanttEntryRelationships.
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
 * @author XDEV Software JWill
 * @since 4.0
 */
public class FtFRelationManagementController<T extends Comparable<T>, S extends UpdateableGanttEntry<T>>
		implements GanttRelationManagementController<T, S>
{
	
	@Override
	public boolean validateEntryRelation(S precessor, S sucessor)
	{
		// sucessor finish must be after or equals the precessor finish
		if(sucessor.getRange().upper().compareTo(precessor.getRange().upper()) >= 0)
		{
			return true;
		}
		
		return false;
	}
	
}
