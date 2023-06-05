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
package xdev.ui.ganttchart.model;


import xdev.ui.ganttchart.DateRangeProvider;
import xdev.ui.ganttchart.IntegerRangeProvider;
import xdev.ui.ganttchart.RangeProvider;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTable.VirtualTableRow;

import com.jidesoft.gantt.GanttEntry;
import com.jidesoft.gantt.GanttModel;


/**
 * Maps a {@link VirtualTableRow} to a {@link GanttEntry}.
 * <p>
 * Used to create a {@link GanttModel} from an existing {@link VirtualTable}.
 * </p>
 * 
 * 
 * @param <T>
 *            the mapping source type for example {@link VirtualTableRow}
 * @param <S>
 *            the {@link GanttEntry} type, also can be a customized
 *            implementation.
 * 
 * @param <GDT>
 *            the gantt charts data type, for example <code>Date</code> or
 *            <code>Integer</code>.
 * 
 * @author XDEV Software jwill
 * @since 4.0
 */
public interface GanttEntryMapper<T, S, GDT>
{
	
	/**
	 * Maps a target object of type {@code T} to an {@link GanttEntry}.
	 * 
	 * @param input
	 *            the object to create an {@link GanttEntry} from.
	 * @return an {@link Activity}
	 */
	public S dataToGanttEntry(T input);
	
	
	/**
	 * Retruns a concrete {@link RangeProvider} related to the GanttChart data
	 * type.
	 * 
	 * @return the concrete <code>RangeProvider</code>.
	 * 
	 * @see DateRangeProvider
	 * @see IntegerRangeProvider
	 */
	public RangeProvider<GDT> getRangeProvider();
	
}
