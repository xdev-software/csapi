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
package xdev.ui.ganttchart;


import javax.annotation.processing.Completion;

import com.jidesoft.gantt.GanttEntry;
import com.jidesoft.range.Range;


/**
 * Extension layer for {@link GanttEntry} to gain the ability to update the
 * entries values.
 * 
 * 
 * 
 * @param <T>
 *            the gantt data type, for example <code>Date</code> or
 *            <code>Integer</code>. Has to extend {@link Comparable} for
 *            validation purpose.
 *            <p>
 *            Used for entry scaling via {@link GanttEntry#getRange()}
 *            </p>
 * 
 * @author XDEV Software jwill
 * @since 4.0
 */
public interface UpdateableGanttEntry<T> extends GanttEntry<T>
{
	
	/**
	 * 
	 * sets a unique identifier.
	 * 
	 * @param id
	 *            the id to set.
	 * 
	 */
	public void setId(Object id);
	
	
	/**
	 * 
	 * @return a unique identifier.
	 */
	public Object getId();
	
	
	/**
	 * Sets the {@link GanttEntry}s root Entry id.
	 * <p>
	 * Used for hierarchical tree structure.
	 * </p>
	 * 
	 * @param root
	 *            the root Entry id
	 */
	public void setRoot(Object root);
	
	
	/**
	 * Returns the {@link GanttEntry}s root Entry id.
	 * <p>
	 * Used for hierarchical tree structure.
	 * </p>
	 * 
	 * @return {@link GanttEntry}s root entry.
	 */
	public Object getRoot();
	
	
	/**
	 * Sets the internal {@link GanttEntry} description.
	 * 
	 * @param name
	 *            the description value to set.
	 */
	public void setName(String name);
	
	
	/**
	 * Sets the internal {@link GanttEntry} {@link Completion}.
	 * <p>
	 * Can be used to create completion independencies
	 * </p>
	 * 
	 * @param completion
	 *            the completion value to set.
	 */
	public void setCompletion(double completion);
	
	
	/**
	 * Sets the internal {@link GanttEntry} {@link Completion}.
	 * <p>
	 * Is used to create relational start/end dependencies.
	 * </p>
	 * 
	 * @param range
	 *            the range value to set, dependend on the data type
	 *            <code>T</code> (see class description).
	 */
	public void setRange(Range<T> range);
}
