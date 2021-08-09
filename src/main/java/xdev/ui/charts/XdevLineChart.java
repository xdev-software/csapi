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


import static com.jidesoft.swing.ShadowFactory.KEY_BLUR_QUALITY;
import static com.jidesoft.swing.ShadowFactory.VALUE_BLUR_QUALITY_HIGH;

import java.awt.Color;

import xdev.ui.charts.model.ChartColorScheme;

import com.jidesoft.chart.style.ChartStyle;


/**
 * 
 * Line Chart plots a line connecting the data points in a series. Line charts
 * are usually used to view data trends over time or category.
 * 
 * @author XDEV Software jwill
 * @since 4.0
 * 
 */
public class XdevLineChart extends VirtualTableAbstractCategoryChart
{
	/**
	 * 
	 */
	private static final long						serialVersionUID	= -3874961833878033953L;
	private final com.jidesoft.swing.ShadowFactory	shadowRenderer		= new com.jidesoft.swing.ShadowFactory();
	private static final int						DEFAULT_POINT_SIZE	= 12;
	private static final int						DEFAULT_LINE_WIDTH	= 7;
	
	
	/**
	 * Initializes an empty valued numeric XY-chart.
	 */
	public XdevLineChart()
	{
		super();
		this.initRendering();
	}
	
	
	/**
	 * Initializes an empty valued numeric XY-chart.
	 */
	public XdevLineChart(ChartColorScheme colorScheme)
	{
		super(colorScheme);
		this.initRendering();
	}
	
	
	protected void initRendering()
	{
		this.setVerticalGridLinesVisible(false);
		
		shadowRenderer.setRenderingHint(KEY_BLUR_QUALITY,VALUE_BLUR_QUALITY_HIGH);
		shadowRenderer.setSize(DEFAULT_SHADOW_SIZE);
		shadowRenderer.setColor(Color.DARK_GRAY);
		shadowRenderer.setOpacity(0.40f);
		this.setShadowRenderer(shadowRenderer);
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
			style.setPointColor(defaultColor);
			style.setPointSize(DEFAULT_POINT_SIZE);
			
			return style;
		}
		else
		{
			style = new ChartStyle(defaultColor).withPointsAndLines();
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
