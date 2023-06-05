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


import static com.jidesoft.swing.ShadowFactory.KEY_BLUR_QUALITY;
import static com.jidesoft.swing.ShadowFactory.VALUE_BLUR_QUALITY_HIGH;

import java.awt.Color;

import xdev.ui.charts.model.ChartColorScheme;

import com.jidesoft.chart.style.ChartStyle;


/**
 * 
 * Point Chart connects the data as points in a series. Point charts are usually
 * used to view data trends over time or category.
 * 
 * @author XDEV Software jwill
 * @since 4.0
 * 
 */
public class XdevPointChart extends VirtualTableAbstractCategoryChart
{
	private static final long						serialVersionUID	= 4858737858525147288L;
	private final com.jidesoft.swing.ShadowFactory	shadowRenderer		= new com.jidesoft.swing.ShadowFactory();
	private static final int						DEFAULT_PONT_SIZE	= 12;
	
	
	/**
	 * Initializes an empty valued numeric XY-chart.
	 */
	public XdevPointChart()
	{
		super();
		this.initRendering();
	}
	
	
	/**
	 * Initializes an empty valued numeric XY-chart.
	 */
	public XdevPointChart(ChartColorScheme colorScheme)
	{
		super(colorScheme);
		this.initRendering();
	}
	
	
	protected void initRendering()
	{
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
			style.withPoints();
			style.setPointColor(defaultColor);
			style.setPointSize(DEFAULT_PONT_SIZE);
			return style;
		}
		else
		{
			style = new ChartStyle(defaultColor);
			style.withPoints();
			style.setPointSize(DEFAULT_PONT_SIZE);
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
			style.withPoints();
			style.setPointSize(DEFAULT_PONT_SIZE);
		}
		else
		{
			style = new ChartStyle().withPoints();
			style.setPointSize(DEFAULT_PONT_SIZE);
		}
		return style;
	}
}
