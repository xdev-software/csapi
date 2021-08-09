package xdev.ui.ganttchart.model;

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

import com.jidesoft.gantt.GanttChart;
import com.jidesoft.gantt.GanttEntry;


/**
 * This is the {@link GanttChart}s persistence layer, which is responsible for
 * all changes made to its {@link GanttEntry}s.
 * <p>
 * Changes like add, update and delte are committed to a datasource.
 * </p>
 * 
 * 
 * @param <T>
 *            data type, for example <code>Date</code> or <code>Integer</code>.
 * @param <S>
 *            the custom {@link GanttEntry} type.
 * 
 * @author XDEV Software jwill
 * @since 4.0
 */
public interface GanttPersistence<T, S extends UpdateableGanttEntry<T>>
{
	/**
	 * Updates the {@link GanttEntry}s changes in a data source.
	 * 
	 * @param entry
	 *            the enty to update.
	 */
	public void updateEntry(S entry);
	
	
	/**
	 * Adds the new {@link GanttEntry} to a data source.
	 * 
	 * @param entry
	 *            the entry to add.
	 */
	public void addEntry(S entry);
	
	
	/**
	 * Removes the {@link GanttEntry} from a data source.
	 * 
	 * @param entry
	 *            the entry to remove.
	 */
	public void removeEntry(S entry);
}
