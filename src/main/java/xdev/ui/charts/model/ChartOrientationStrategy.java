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
package xdev.ui.charts.model;


import xdev.ui.charts.XdevBarChart;

import com.jidesoft.chart.Chart;
import com.jidesoft.chart.axis.Axis;
import com.jidesoft.chart.model.ChartPoint;
import com.jidesoft.chart.model.Highlight;
import com.jidesoft.chart.style.ChartStyle;
import com.jidesoft.range.Positionable;


/**
 * Computes the chart orientation.
 * <p>
 * An example could be a {@link XdevBarChart} with horizontal bars
 * </p>
 * 
 * @author XDEV Software jwill
 * @since 4.0
 */
public interface ChartOrientationStrategy
{
	/**
	 * Initializes the category axis.
	 * 
	 * @param chart
	 *            the chart to initialize.
	 * @param categoryAxis
	 *            the axis to initialize.
	 */
	public void setCategoryAxis(Chart chart, Axis categoryAxis);
	
	
	public Axis getCategoryAxis(Chart chart);
	
	
	/**
	 * Initializes the value axis.
	 * 
	 * @param chart
	 *            the chart to initialze.
	 * @param valueAxis
	 *            the axis to initialize.
	 */
	public void setValueAxis(Chart chart, Axis valueAxis);
	
	
	public Axis getValueAxis(Chart chart);
	
	
	/**
	 * Sets the chart slice orientation. (Horizontal/Vertical)
	 * 
	 * @param chartStyle
	 *            the {@link ChartStyle} to modify.
	 */
	public void setGraphOrientation(ChartStyle chartStyle);
	
	
	/**
	 * Computes the point values depending on the set chart orientation.
	 * 
	 * @param category
	 *            the category axis value.
	 * @param value
	 *            the value axis value.
	 * @return the computed point values.
	 */
	public ChartPoint getOrientedPoint(Positionable category, Positionable value);
	
	
	/**
	 * Computes the point values depending on the set chart orientation.
	 * 
	 * @param category
	 *            the category axis value.
	 * @param value
	 *            the value axis value.
	 * 
	 * @param highlight
	 *            the point {@link Highlight} (e.g. color value).
	 * @return the computed point values.
	 */
	public ChartPoint getOrientedPoint(Positionable category, Positionable value,
			Highlight highlight);
}
