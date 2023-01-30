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
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTableColumn;

import com.jidesoft.gantt.DefaultGanttEntry;
import com.jidesoft.gantt.GanttEntry;


/**
 * Static mapper class for {@link GanttEntry} mandatory columns.
 * <p>
 * Also handles {@link VirtualTableColumn} invisibility.
 * </p>
 * 
 * @see GanttEntry
 * @author XDEV Software jwill
 * @since 4.0
 */
public class GanttEntryVirtualTableColumnConverter
{
	
	/**
	 * 
	 * @param column
	 *            the mandatory {@link GanttEntry} column to calculate.
	 * @param dataInfo
	 *            the {@link XdevGanttEntryVTMappings} which provides mapping
	 *            information about {@link GanttEntry} columns.
	 * @param vt
	 *            the {@link VirtualTable} which represents the data.
	 * 
	 * @param <T>
	 *            the current <code>Gantt</code> data type.
	 * 
	 * @return the actual colum index in the {@link VirtualTable}
	 *         representation.
	 */
	public static <T> int getActualMandatoryColumnIndex(int column,
			XdevGanttEntryVTMappings<T> dataInfo, VirtualTable vt)
	{
		switch(column)
		{
			case DefaultGanttEntry.COLUMN_NAME:
			{
				return subtractRelevantInvisibleFields(
						vt.getColumnIndex(dataInfo.getDescription()),vt);
			}
			case DefaultGanttEntry.COLUMN_COMPLETION:
			{
				return subtractRelevantInvisibleFields(vt.getColumnIndex(dataInfo.getCompletion()),
						vt);
			}
			case DefaultGanttEntry.COLUMN_RANGE_START:
			{
				return subtractRelevantInvisibleFields(vt.getColumnIndex(dataInfo.getStart()),vt);
			}
			case DefaultGanttEntry.COLUMN_RANGE_END:
			{
				
				return subtractRelevantInvisibleFields(vt.getColumnIndex(dataInfo.getEnd()),vt);
			}
		}
		return column;
	}
	
	
	/**
	 * handles vt invisable columns and returns the valid column index.
	 * 
	 * @param columnIndex
	 *            the columnIndex to calculate the visibleColumnIndex from.
	 * @param vt
	 *            the {@link VirtualTable} as data provider.
	 * 
	 * @return the visible calculated index.
	 */
	public static int subtractRelevantInvisibleFields(int columnIndex, VirtualTable vt)
	{
		int subtrahend = 0;
		
		for(int i = 0; i < vt.getColumnCount(); i++)
		{
			if(!vt.getColumnAt(i).isVisible() && i < columnIndex)
			{
				subtrahend++;
			}
		}
		
		return columnIndex - subtrahend;
	}
	
}
