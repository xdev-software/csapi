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
 * This concrete implementation of
 * {@link SpecialRelationalUpdatableEntryCreator} creates and returns
 * {@link XdevVirtualTableGanttEntry} instance, filled with the provided values.
 * 
 * 
 * @param <T>
 *            the gantt data type, for example <code>Date</code> or
 *            <code>Integer</code>.
 *            <p>
 *            Used for entry scaling via {@link GanttEntry#getRange()}
 *            </p>
 * 
 * @author XDEV Software jwill
 * @since 4.0
 */
public class XdevVirtualTableGanttEntryCreator<T> implements
		SpecialRelationalUpdatableEntryCreator<UpdateableGanttEntry<T>, T>
{
	/**
	 * {@inheritDoc}
	 */
	@Override
	public UpdateableGanttEntry<T> createRelationalUpdatableEntry(String description,
			Range<T> range, XdevGanttEntryVTMappings<T> dataInfo)
	{
		return new XdevVirtualTableGanttEntry<T>(description,range,dataInfo,
				dataInfo.getVirtualTable());
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public UpdateableGanttEntry<T> createRelationalUpdatableEntry(String description,
			Double completion, Range<T> range, XdevGanttEntryVTMappings<T> dataInfo)
	{
		return new XdevVirtualTableGanttEntry<T>(description,range,completion,dataInfo,
				dataInfo.getVirtualTable());
	}
	
}
