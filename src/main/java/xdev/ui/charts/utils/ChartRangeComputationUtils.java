package xdev.ui.charts.utils;

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


import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import xdev.vt.VirtualTable;
import xdev.vt.VirtualTableColumn;

import com.jidesoft.chart.model.ChartCategory;
import com.jidesoft.range.Category;
import com.jidesoft.range.CategoryRange;
import com.jidesoft.range.NumericRange;
import com.jidesoft.range.Range;
import com.jidesoft.range.TimeRange;


/**
 * 
 * @author XDEV Software jwill
 * @since 4.0
 */
public class ChartRangeComputationUtils
{
	
	/**
	 * Computes a numeric range from given values.
	 * 
	 * @param container
	 *            the data container, for example a {@link VirtualTable}.
	 * @param categoryColumnIndex
	 *            the value column index.
	 * 
	 * @return returns the computed {@link NumericRange}.
	 */
	public static CategoryRange<String> computeStringRange(VirtualTable container,
			Integer categoryColumnIndex)
	{
		CategoryRange<String> categoryRange = new CategoryRange<String>();
		
		if(container.getRowCount() > 0)
		{
			for(int row = 0; row < container.getRowCount(); row++)
			{
				String category = (String)container.getValueAt(row,
						container.getColumnAt(categoryColumnIndex));
				// ensure grouping
				if(!categoryRange.contains(category))
				{
					categoryRange.add(new ChartCategory<String>(category));
				}
			}
		}
		else
		{
			throw new IllegalArgumentException(
					"Chart axis range cannot be computed because VirtualTable is empty");
		}
		
		return categoryRange;
	}
	
	
	/**
	 * Computes a numeric range from given values.
	 * 
	 * @param container
	 *            the data container, for example a {@link VirtualTable}.
	 * @param valueColumnIndices
	 *            the value column index.
	 * 
	 * @return returns the computed {@link NumericRange}.
	 */
	public static Range<Double> computeNumericValueRange(VirtualTable container,
			int... valueColumnIndices)
	{
		Double minValue = null;
		Double maxValue = null;
		List<Double> possibleValues = new ArrayList<Double>();
		
		if(container.getRowCount() > 0)
		{
			for(int i = 0; i < valueColumnIndices.length; i++)
			{
				int valueColumnIndex = valueColumnIndices[i];
				
				VirtualTableColumn<? extends Number> valueColumn = VirtualTableColumnTypeUtils
						.isNumericColumn(container.getColumnAt(valueColumnIndex));
				
				for(int row = 0; row < container.getRowCount(); row++)
				{
					Number value = container.getValueAt(row,valueColumn);
					
					// ensure grouping
					if(!possibleValues.contains(value.doubleValue()))
					{
						possibleValues.add(value.doubleValue());
					}
				}
				
				// store values
				if(minValue == null || Collections.min(possibleValues) < minValue)
				{
					minValue = Collections.min(possibleValues);
				}
				
				if(maxValue == null || Collections.max(possibleValues) > maxValue)
				{
					maxValue = Collections.max(possibleValues);
					// calculate trailing margin proportions
					maxValue += (maxValue * 0.1);
				}
			}
			
			// 0 start
			NumericRange numericRange = new NumericRange(minValue - minValue,maxValue);
			
			return numericRange;
		}
		else
		{
			throw new IllegalArgumentException(
					"Chart axis range cannot be computed because VirtualTable is empty");
		}
	}
	
	
	/**
	 * Computes a numeric range from given values.
	 * 
	 * @param container
	 *            the data container, for example a {@link VirtualTable}.
	 * @param valueColumnIndices
	 *            the value column index.
	 * 
	 * @return returns the computed {@link NumericRange}.
	 */
	public static Range<Double> computeNumericCategoryRange(VirtualTable container,
			int... valueColumnIndices)
	{
		Double minValue = null;
		Double maxValue = null;
		List<Double> possibleValues = new ArrayList<Double>();
		
		if(container.getRowCount() > 0)
		{
			for(int i = 0; i < valueColumnIndices.length; i++)
			{
				int valueColumnIndex = valueColumnIndices[i];
				
				VirtualTableColumn<? extends Number> valueColumn = VirtualTableColumnTypeUtils
						.isNumericColumn(container.getColumnAt(valueColumnIndex));
				
				for(int row = 0; row < container.getRowCount(); row++)
				{
					Number value = container.getValueAt(row,valueColumn);
					
					// ensure grouping
					if(!possibleValues.contains(value.doubleValue()))
					{
						possibleValues.add(value.doubleValue());
					}
				}
				
				// store values
				if(minValue == null || Collections.min(possibleValues) < minValue)
				{
					minValue = Collections.min(possibleValues);
				}
				
				if(maxValue == null || Collections.max(possibleValues) > maxValue)
				{
					maxValue = Collections.max(possibleValues) + minValue;
				}
			}
			
			// 0 start
			NumericRange numericRange = new NumericRange(minValue - minValue,maxValue);
			
			return numericRange;
		}
		else
		{
			throw new IllegalArgumentException(
					"Chart axis range cannot be computed because VirtualTable is empty");
		}
	}
	
	
	/**
	 * Computes a numeric range from given values.
	 * 
	 * @param container
	 *            the data container, for example a {@link VirtualTable}.
	 * @param valueColumnIndices
	 *            the value column index.
	 * 
	 * @return returns the computed {@link NumericRange}.
	 */
	public static TimeRange computeTimeValueRange(VirtualTable container, int... valueColumnIndices)
	{
		Long minValue = null;
		Long maxValue = null;
		List<Long> possibleValues = new ArrayList<Long>();
		
		if(container.getRowCount() > 0)
		{
			for(int i = 0; i < valueColumnIndices.length; i++)
			{
				int valueColumnIndex = valueColumnIndices[i];
				
				VirtualTableColumn<? extends Date> valueColumn = VirtualTableColumnTypeUtils
						.isDateColumn(container.getColumnAt(valueColumnIndex));
				
				for(int row = 0; row < container.getRowCount(); row++)
				{
					Date value = container.getValueAt(row,valueColumn);
					
					// ensure grouping
					if(!possibleValues.contains(value.getTime()))
					{
						possibleValues.add(value.getTime());
					}
				}
				
				// store values
				if(minValue == null || Collections.min(possibleValues) < minValue)
				{
					minValue = Collections.min(possibleValues);
				}
				
				if(maxValue == null || Collections.max(possibleValues) > maxValue)
				{
					maxValue = Collections.max(possibleValues);
					// calculate trailing margin proportions
					maxValue += (maxValue / 100 * 10);
				}
			}
			// 0 start
			TimeRange timeRange = new TimeRange(minValue - minValue,maxValue);
			
			return timeRange;
		}
		else
		{
			throw new IllegalArgumentException(
					"Chart axis range cannot be computed because VirtualTable is empty");
		}
	}
	
	
	/**
	 * Computes a numeric range from given values.
	 * 
	 * @param container
	 *            the data container, for example a {@link VirtualTable}.
	 * @param valueColumnIndices
	 *            the value column index.
	 * 
	 * @return returns the computed {@link NumericRange}.
	 */
	public static TimeRange computeTimeCategoryRange(VirtualTable container,
			int... valueColumnIndices)
	{
		Long minValue = null;
		Long maxValue = null;
		List<Long> possibleValues = new ArrayList<Long>();
		
		if(container.getRowCount() > 0)
		{
			for(int i = 0; i < valueColumnIndices.length; i++)
			{
				int valueColumnIndex = valueColumnIndices[i];
				
				VirtualTableColumn<? extends Date> valueColumn = VirtualTableColumnTypeUtils
						.isDateColumn(container.getColumnAt(valueColumnIndex));
				
				for(int row = 0; row < container.getRowCount(); row++)
				{
					Date value = container.getValueAt(row,valueColumn);
					
					// ensure grouping
					if(!possibleValues.contains(value.getTime()))
					{
						possibleValues.add(value.getTime());
					}
				}
				
				// store values
				if(minValue == null || Collections.min(possibleValues) < minValue)
				{
					minValue = Collections.min(possibleValues);
				}
				
				if(maxValue == null || Collections.max(possibleValues) > maxValue)
				{
					maxValue = Collections.max(possibleValues) + minValue;
				}
				
			}
			// 0 start
			TimeRange timeRange = new TimeRange(minValue - minValue,maxValue);
			
			return timeRange;
		}
		else
		{
			throw new IllegalArgumentException(
					"Chart axis range cannot be computed because VirtualTable is empty");
		}
	}
	
	

	public static <T> Category<T> getCategory(CategoryRange<T> range, T value)
	{
		for(Category<T> category : range.getCategoryValues())
		{
			if(category.getValue().equals(value))
			{
				return category;
			}
		}
		throw new RuntimeException("No category value found");
	}
	
	

	public static <T> T getRangeValue(Range<T> range, T value)
	{
		for(int i = 0; i < range.size(); i++)
		{
			if(range.contains(value))
			{
				return value;
			}
		}
		throw new RuntimeException("No category value found");
	}
}
