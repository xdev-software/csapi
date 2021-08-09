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


import xdev.ui.charts.XdevBarChart;

import com.jidesoft.chart.model.ChartPoint;
import com.jidesoft.chart.model.Highlight;
import com.jidesoft.range.Positionable;


/**
 * Computes the chart orientation inclusive z-axis value.
 * <p>
 * An example could be a {@link XdevBarChart} with horizontal bars
 * </p>
 * 
 * @author XDEV Software jwill
 * @since 4.0
 */
public interface Chart3DOrientationStrategy extends ChartOrientationStrategy
{
	/**
	 * Computes the point values depending on the set chart orientation.
	 * 
	 * @param category
	 *            the category axis value.
	 * @param value
	 *            the value axis value.
	 * @param depthValue
	 *            the z-axis value.
	 * 
	 * @return the computed point values.
	 */
	public ChartPoint getOrientedPoint(Positionable category, Positionable value,
			Positionable depthValue);
	
	
	/**
	 * Computes the point values depending on the set chart orientation.
	 * 
	 * @param category
	 *            the category axis value.
	 * @param value
	 *            the value axis value.
	 * @param depthValue
	 *            the z-axis value.
	 * @param highlight
	 *            the point {@link Highlight} (e.g. color value).
	 * 
	 * @return the computed point values.
	 */
	public ChartPoint getOrientedPoint(Positionable category, Positionable value,
			Positionable depthValue, Highlight highlight);
	
}
