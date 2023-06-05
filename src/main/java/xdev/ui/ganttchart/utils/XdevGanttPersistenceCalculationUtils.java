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
package xdev.ui.ganttchart.utils;


import com.jidesoft.gantt.GanttEntry;


/**
 * Utility class to calculate values for comprehensive {@link GanttEntry}s.
 * 
 * @author XDEV Software jwill
 * @since 4.0
 */
public class XdevGanttPersistenceCalculationUtils
{
	
	/**
	 * 
	 * @param parentEntry
	 *            the parent entry to calculate the subentries completion from.
	 * 
	 * @param <T>
	 *            the gantt data type, for example <code>Date</code> or
	 *            <code>Integer</code>.
	 *            <p>
	 *            Used for entry scaling via {@link GanttEntry#getRange()}
	 *            </p>
	 *            .
	 * @param <S>
	 *            the customized {@link GanttEntry} type.
	 * 
	 * @return the average completion of all subentries,
	 */
	// have to type class based because jide base interface does not provide
	// #getSubEntries.
	public static <S extends GanttEntry<?>> Double getParentCompletionValue(S parentEntry)
	{
		Double completion = 0.0;
		
		for(Object childEntry : parentEntry.getChildren())
		{
			// cant avoid cast because of JIDE API. (getParent returnVal !=
			// Generic
			// Type)
			GanttEntry<?> calcEntry = (GanttEntry<?>)childEntry;
			completion += calcEntry.getCompletion();
		}
		
		return completion / parentEntry.getChildren().size();
	}
	
}
