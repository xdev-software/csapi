package xdev.ui.charts;

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
import java.util.Map;

import xdev.ui.charts.model.Chart3DOrientationStrategy;


/**
 * Indicator for XY Charts.
 * 
 * @param <VT>
 *            the chart data value type.
 * 
 * @param <CT>
 *            the chart data category type.
 * 
 * @author XDEV Software jwill
 * @since 4.0
 */
public interface OrientationalCategoryValueChart<VT, CT>
{
	/**
	 * Initializes the category axis.
	 * 
	 * @param categoryValues
	 *            the category values wrapped in a {@link ChartCategoryValue} to
	 *            store the category value and its paint by default.
	 */
	<T extends ChartCategoryValue<CT>> void processCategoryAxis(List<T> categoryValues);
	
	
	/**
	 * Initializes the value axis.
	 * 
	 * @param values
	 *            the value axis values.
	 * 
	 */
	void processValueAxis(List<VT> values);
	
	
	/**
	 * Creates the model for this chart.
	 * 
	 * @param dataSet
	 *            a {@link Map} the chart data set.
	 */
	public void setModel(Map<ChartCategoryValue<CT>, VT> dataSet);
	
	
	/**
	 * Creates the model for this chart.
	 * 
	 * @param dataSet
	 *            a {@link Map} the chart data set.
	 * @param depthValue
	 *            the z coordinate column index for this chart.
	 */
	public <DT extends Number> void setModel(Map<ChartCategoryValue<CT>, VT> dataSet, DT depthValue);
	
	
	/**
	 * Sets this charts {@link Chart3DOrientationStrategy}. The bars can be
	 * vertical or horizontal depending on which axis is a category axis.
	 * 
	 * @param chartOrientation
	 *            the orientation strategy to set.
	 */
	public void setChartOrientation(Chart3DOrientationStrategy chartOrientation);
	
	
	/**
	 * Returns this charts {@link Chart3DOrientationStrategy}.
	 * 
	 * @return the orientation strategy (horizontal or vertical)
	 */
	public Chart3DOrientationStrategy getChartOrientation();
}
