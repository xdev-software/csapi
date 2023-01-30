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


import java.awt.Color;

import xdev.ui.charts.model.ChartColorScheme;

import com.jidesoft.chart.style.ChartStyle;


/**
 * 
 * Area Chart plots a area connecting the data points in a series. Area charts
 * are usually used to view data trends over time or category.
 * 
 * @author XDEV Software jwill
 * @since 4.0
 * 
 */
public class XdevAreaChart extends VirtualTableAbstractCategoryChart
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -3874961833878033953L;
	private static final int	DEFAULT_POINT_SIZE	= 8;
	private static final int	DEFAULT_LINE_WIDTH	= 3;
	private static final int	DEFAULT_FILL_ALPHA	= 120;
	
	
	/**
	 * Initializes an empty valued numeric XY-chart.
	 */
	public XdevAreaChart()
	{
		super();
		this.initializeRendering();
	}
	
	
	/**
	 * Initializes an empty valued numeric XY-chart.
	 */
	public XdevAreaChart(ChartColorScheme colorScheme)
	{
		super(colorScheme);
		this.initializeRendering();
	}
	
	
	protected void initializeRendering()
	{
		this.setVerticalGridLinesVisible(false);
		this.setShadowVisible(true);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ChartStyle createStyle(Color defaultColor)
	{
		ChartStyle style = null;
		if(defaultStyle != null)
		{
			style = new ChartStyle(defaultStyle);
			style.withPointsAndLines();
			style.setLineWidth(DEFAULT_LINE_WIDTH);
			style.setPointSize(DEFAULT_POINT_SIZE);
			
			style.setLineFill(new Color(defaultColor.getRed(),defaultColor.getGreen(),defaultColor
					.getBlue(),DEFAULT_FILL_ALPHA));
			
			return style;
		}
		else
		{
			style = new ChartStyle(defaultColor).withPointsAndLines();
			style.setLineFill(new Color(defaultColor.getRed(),defaultColor.getGreen(),defaultColor
					.getBlue(),DEFAULT_FILL_ALPHA));
			style.setLineWidth(DEFAULT_LINE_WIDTH);
			style.setPointSize(DEFAULT_POINT_SIZE);
			return style;
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ChartStyle createStyle()
	{
		ChartStyle style = null;
		if(defaultStyle != null)
		{
			style = new ChartStyle(defaultStyle);
			style.withPointsAndLines();
			style.setLineWidth(DEFAULT_LINE_WIDTH);
			style.setPointSize(DEFAULT_POINT_SIZE);
		}
		else
		{
			style = new ChartStyle().withPointsAndLines();
			style.setLineWidth(DEFAULT_LINE_WIDTH);
			style.setPointSize(DEFAULT_POINT_SIZE);
		}
		return style;
	}
	
}
