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


import xdev.ui.ganttchart.model.XdevGanttEntryVTMappings;

import com.jidesoft.gantt.GanttEntry;
import com.jidesoft.range.Range;


/**
 * Generic factory for extended {@link GanttEntry}s.
 * <p>
 * Any extension of {@link GanttEntry} requires specific mappings for its
 * mandatory fields. That is the reason there is a {@link GanttDataInformation}
 * parameter which wraps the data-mappings.
 * </p>
 * 
 * 
 * 
 * @param <S>
 *            the {@link GanttEntry} type to create.
 * @param <T>
 *            the <code>GanttEntry</code> data type, for example
 *            <code>Date</code> or <code>Number</code>
 * 
 * @author Xdev Software jwill
 * @since 4.0
 */
public interface SpecialRelationalUpdatableEntryCreator<S extends UpdateableGanttEntry<T>, T>
{
	
	/**
	 * The default completion value, used for entry creation.
	 */
	public static final double	DEFAULT_COMPLETION	= 0.0;
	
	/**
	 * The default description value, used for entry creation.
	 */
	public static final String	DEFAULT_DESCRIPTION	= GanttResourceBundle
															.getString("entrycreator.defaulttext");
	
	
	/**
	 * Provides a {@link GanttEntry} <code>S</code> typed. (see class
	 * description), filled with the given values.
	 * 
	 * @param description
	 *            the description which is going to be set by default.
	 * @param range
	 *            the range which is going to be set by default.
	 * @param dataInfo
	 *            the data info which needs to be set to customized entries, to
	 *            map the mandatory columns.
	 * @return a pre filled {@link GanttEntry} with type <code>S</code> (see
	 *         class description).
	 */
	public S createRelationalUpdatableEntry(String description, Range<T> range,
			XdevGanttEntryVTMappings<T> dataInfo);
	
	
	/**
	 * Provides a {@link GanttEntry} <code>S</code> typed. (see class
	 * description), filled with the given values.
	 * 
	 * @param description
	 *            the description as <code>String</code> which is going to be
	 *            set by default.
	 * @param range
	 *            the range as {@link Range} which is going to be set by
	 *            default.
	 * @param completion
	 *            the completion as <code>Double</code> which is going to be set
	 *            by default.
	 * 
	 * @param dataInfo
	 *            the data info which needs to be set to customized entries, to
	 *            map the mandatory columns.
	 * @return a pre filled {@link GanttEntry} with type <code>S</code> (see
	 *         class description).
	 */
	public S createRelationalUpdatableEntry(String description, Double completion, Range<T> range,
			XdevGanttEntryVTMappings<T> dataInfo);
}
