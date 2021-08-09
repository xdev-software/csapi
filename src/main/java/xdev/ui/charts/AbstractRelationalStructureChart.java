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


import java.awt.Color;

import xdev.ui.charts.model.Chart3DOrientationStrategy;
import xdev.ui.charts.model.ChartOrientation;
import xdev.ui.charts.utils.VirtualTableChartUtils;

import com.jidesoft.chart.Chart;
import com.jidesoft.chart.style.ChartStyle;


/**
 * Chart base class for all two axis charts. It is responsible for drawing the two
 * axes and the plot content.
 * <p>
 * The chart is also capable to change the orientation of its points, for
 * example swap x and y.
 * </p>
 * 
 * @author XDEV Software jwill
 * 
 * @param <ContainerType>
 *            the container type which provides the model data.
 * 
 * @since 4.0
 */
public abstract class AbstractRelationalStructureChart<ContainerType, ValueType> extends Chart
		implements RelationalStructureChartDataInitializer<ContainerType, ValueType>
{
	/**
	 * the serialization id.
	 */
	private static final long			serialVersionUID	= -8240177212656219144L;
	
	/**
	 * Default {@link ChartStyle} instance.
	 */
	protected ChartStyle				defaultStyle;
	private Chart3DOrientationStrategy	orientation;
	
	
	public Chart3DOrientationStrategy getChartOrientation()
	{
		return VirtualTableChartUtils.getOrientation(this.orientation);
	}
	
	
	/**
	 * Sets the {@link Chart3DOrientationStrategy} for considering the
	 * {@link ChartOrientation} during plotting.
	 * 
	 * @param orientation
	 *            the {@link Chart3DOrientationStrategy}.
	 */
	public void setChartOrientation(Chart3DOrientationStrategy orientation)
	{
		this.orientation = orientation;
	}
	
	
	/**
	 * Sets the charts {@link ChartOrientation} either horizontal or vertical.
	 * 
	 * @param orientation
	 *            the {@link ChartOrientation}
	 */
	public void setChartOrientation(ChartOrientation orientation)
	{
		this.orientation = orientation.getOrientation();
	}
	
	
	/**
	 * Initializes an empty valued numeric XY-chart.
	 */
	public AbstractRelationalStructureChart()
	{
		super();
	}
	
	
	/**
	 * Returns this default {@link ChartStyle} instance.
	 * 
	 * @return the default chart style.
	 */
	public ChartStyle getDefaultStyle()
	{
		return defaultStyle;
	}
	
	
	/**
	 * Sets this default {@link ChartStyle} instance.
	 * 
	 * @param defaultStyle
	 *            the default chart style to set.
	 */
	public void setDefaultStyle(ChartStyle defaultStyle)
	{
		this.defaultStyle = defaultStyle;
	}
	
	
	/**
	 * Creates the {@link ChartStyle} for this chart.
	 * 
	 * @param defaultColor
	 *            a default color which is going to be used in the
	 *            {@link ChartStyle}.
	 * @return the created style.
	 */
	public abstract ChartStyle createStyle(Color defaultColor);
	
	
	/**
	 * Creates the {@link ChartStyle} for this chart.
	 * 
	 * @return the created style.
	 */
	public abstract ChartStyle createStyle();
	
}
