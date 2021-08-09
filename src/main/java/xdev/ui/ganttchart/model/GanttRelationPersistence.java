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
import com.jidesoft.gantt.GanttEntryRelation;


/**
 * This persistence is responsible for transmitting all changes within the
 * {@link GanttChart} or implicitly its {@link GanttEntryRelation}s to a data
 * source.
 * 
 * @param <S>
 *            the customized {@link GanttEntry} type.
 * 
 * @author XDEV Software jwill
 * @since 4.0
 */
public interface GanttRelationPersistence<S extends UpdateableGanttEntry<?>>
{
	/**
	 * Adds the given relation the the data source.
	 * 
	 * @param relation
	 *            the {@link GanttEntryRelation} to add.
	 */
	public void addRelation(GanttEntryRelation<S> relation);
	
	
	/**
	 * Removes the given relation from the data source.
	 * 
	 * @param relation
	 *            the relation to remove.
	 */
	public void removeRelation(GanttEntryRelation<S> relation);
}
