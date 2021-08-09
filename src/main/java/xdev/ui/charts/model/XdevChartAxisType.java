package xdev.ui.charts.model;

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


import java.util.List;

import xdev.db.DataType;
import xdev.ui.charts.AbstractRelationalStructureChart;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTableColumn;

import com.jidesoft.chart.axis.CategoryAxis;
import com.jidesoft.chart.axis.NumericAxis;
import com.jidesoft.chart.axis.TimeAxis;


/**
 * Chart initializer mapping.
 * <p>
 * The type names are always constructed like
 * %CategoryAxisType%_%ValueAxisType%_Axis.
 * </p>
 * 
 * @author XDEV Software jwill
 * 
 * @since 4.0
 */
public enum XdevChartAxisType
{
	/**
	 * // * {@link TimeAxis} initialization strategy. //
	 */
	TIME_NUMERIC_AXIS(DataType.getDateTypes(), DataType.getNumericTypes(),
			new VTTimeNumericChartDataInitializer()),
	/**
	 * {@link NumericAxis} initialization strategy.
	 */
	NUMERIC_NUMERIC_AXIS(DataType.getNumericTypes(), DataType.getNumericTypes(),
			new VTNumericNumericChartDataInitializer()),
	/**
	 * {@link CategoryAxis} initialization strategy.
	 */
	STRING_NUMERIC_AXIS(DataType.getStringTypes(), DataType.getNumericTypes(),
			new VTStringNumericChartDataInitializer()),
	/**
	 * {@link TimeAxis} initialization strategy.
	 */
	STRING_TIME_AXIS(DataType.getStringTypes(), DataType.getDateTypes(),
			new VTStringTimeChartDataInitializer()),
	
	/**
	 * {@link TimeAxis} initialization strategy.
	 */
	NUMERIC_TIME_AXIS(DataType.getNumericTypes(), DataType.getDateTypes(),
			new VTStringTimeChartDataInitializer());
	
	/**
	 * the {@link ChartDataInitializer} to set up chart axes.
	 */
	private final ChartDataInitializer<VirtualTable, VirtualTableColumn<?>>	dataInitializer;
	
	private final List<DataType>											categoryTypeList;
	
	private final List<DataType>											valueTypeList;
	
	
	/**
	 * Chart initializer mapping.
	 * 
	 * @param description
	 *            the axis description
	 * @param categoryTypeList
	 *            for values which have a matching category {@link DataType}.
	 * 
	 * @param valueTypeList
	 *            for values which have a matching value {@link DataType}
	 * 
	 * @param dataInitializer
	 *            the {@link ChartDataInitializer} to set up chart axes.
	 */
	private XdevChartAxisType(List<DataType> categoryTypeList, List<DataType> valueTypeList,
			ChartDataInitializer<VirtualTable, VirtualTableColumn<?>> dataInitializer)
	{
		this.dataInitializer = dataInitializer;
		this.categoryTypeList = categoryTypeList;
		this.valueTypeList = valueTypeList;
	}
	
	
	/**
	 * Returns the appropriate axis/data initializer for a given category and
	 * value column.
	 * 
	 * @param categoryColumn
	 *            the categoryColumn.
	 * @param valueColumn
	 *            the valueColumn.
	 * @return the appropriate axis/data initializer for a given category and
	 *         value column.
	 * 
	 * @see ChartDataInitializer
	 */
	public static XdevChartAxisType getAxis(VirtualTableColumn<?> categoryColumn,
			VirtualTableColumn<?> valueColumn)
	{
		for(int i = 0; i < values().length; i++)
		{
			if(values()[i].getCategoryTypeList().contains(categoryColumn.getType())
					&& values()[i].getValueTypeList().contains(valueColumn.getType()))
			{
				return values()[i];
			}
		}
		
		throw new RuntimeException("No matching axis/data initializer type was found");
	}
	
	
	/**
	 * Return the axis {@link ChartDataInitializer}.
	 * 
	 * @return the dataInitializer
	 */
	public ChartDataInitializer<VirtualTable, VirtualTableColumn<?>> getDataInitializer(
			AbstractRelationalStructureChart<VirtualTable, VirtualTableColumn<?>> chart)
	{
		this.dataInitializer.setChart(chart);
		return this.dataInitializer;
	}
	
	
	/**
	 * @return the categoryTypeList
	 */
	public List<DataType> getCategoryTypeList()
	{
		return categoryTypeList;
	}
	
	
	/**
	 * @return the valueTypeList
	 */
	public List<DataType> getValueTypeList()
	{
		return valueTypeList;
	}
	
	
	/**
	 * Returns the XdevChartAxisType instance with the given name.
	 * 
	 * @param typeName
	 *            the {@link XdevChartAxisType#name()} equivalent
	 * @return true if the name is equals the {@link XdevChartAxisType}.
	 */
	public boolean getType(String typeName)
	{
		if(typeName.equals(name()))
		{
			return true;
		}
		return false;
	}
}
