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
package xdev.ui.charts;


import java.sql.ResultSet;

import xdev.vt.VirtualTable;


/**
 * Indicator for XY Charts with a table structure based data set, for example
 * {@link VirtualTable} or {@link ResultSet}.
 * 
 * <p>
 * If the Chart dataSet should be ObjectGraph based, the API foundation would be
 * {@link OrientationalCategoryValueChart}.
 * </p>
 * 
 * @param <ContainerType>
 *            the container type which provides the model data.
 * 
 * @author XDEV Software jwill
 * @since 4.0
 */
public interface RelationalStructureChartDataInitializer<ContainerType, ValueType>
{
	/**
	 * Create the chart content from the given data container, for example a
	 * {@link VirtualTable}.
	 * 
	 * @param categoryColumn
	 *            the category column
	 * @param valueColumn
	 *            the value column
	 * @param container
	 *            the data container, for example {@link VirtualTable}.
	 * @param groupColumns
	 *            the optional group columns.
	 */
	public void setModel(ValueType categoryColumn, ValueType valueColumn, ContainerType container,
			ValueType... groupColumns);
	
	
	/**
	 * Create the chart content from the given data container, for example a
	 * {@link VirtualTable}.
	 * 
	 * @param categoryColumn
	 *            the category column
	 * @param valueColumn
	 *            the value column
	 * @param depthColumn
	 *            for including z-axis values.
	 * @param container
	 *            the data container.
	 * @param groupColumns
	 *            the optional group columns.
	 */
	public void setModel(ValueType categoryColumn, ValueType valueColumn, ValueType depthColumn,
			ContainerType container, ValueType... groupColumns);
}
