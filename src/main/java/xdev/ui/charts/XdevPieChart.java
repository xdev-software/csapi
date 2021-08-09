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
import java.awt.GradientPaint;
import java.awt.Paint;

import xdev.ui.charts.model.ChartColorScheme;
import xdev.vt.VirtualTable;

import com.jidesoft.chart.ChartType;
import com.jidesoft.chart.render.AbstractRenderer;
import com.jidesoft.chart.render.PieSegmentRenderer;
import com.jidesoft.chart.style.ChartStyle;


/**
 * 
 * Displays a PieChart.
 * <p>
 * The chart content is populated by pie slices based on data set on the
 * PieChart.
 * </p>
 * <p>
 * To display the slices in different colors either
 * {@link XdevPieChart#setModel(VirtualTable, java.util.Map)} or
 * {@link XdevPieChart#setModel(VirtualTable, int, int...)} can be used.
 * </p>
 * 
 * @author XDEV Software jwill
 * @since 4.0
 */
public class XdevPieChart extends VirtualTableAbstractCategoryChart
{
	/**
	 * the serialization id.
	 */
	private static final long	serialVersionUID	= 1L;
	
	
	/**
	 * Initializes an empty valued numeric XY-chart.
	 */
	public XdevPieChart()
	{
		super();
		this.initType();
		this.initOutlineRendering();
	}
	
	
	/**
	 * Initializes an empty valued numeric XY-chart.
	 */
	public XdevPieChart(ChartColorScheme colorScheme)
	{
		super(colorScheme);
		this.initType();
		this.initOutlineRendering();
	}
	
	
	/**
	 * Initializes the concrete type of this chart.
	 */
	private void initType()
	{
		this.setChartType(ChartType.PIE);
	}
	
	
	/**
	 * Initializes the bar outline rendering.
	 */
	private void initOutlineRendering()
	{
		// cast because of #setAlwaysShowOutlines requirement
		AbstractRenderer renderer = (AbstractRenderer)this.getPieSegmentRenderer();
		renderer.setAlwaysShowOutlines(false);
		this.setShadowVisible(true);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ChartStyle createStyle(Color color)
	{
		ChartStyle style = null;
		if(defaultStyle != null)
		{
			style = new ChartStyle(defaultStyle);
			style.withBars();
			style.setBarColor(color);
		}
		else
		{
			style = new ChartStyle();
			style.withBars();
			style.setBarPaint(this.getGradientPaint(color));
		}
		
		return style;
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
			style.withBars();
		}
		else
		{
			style = new ChartStyle().withBars();
		}
		return style;
	}
	
	
	// ///////// TRIAL STATE - move to utils //////
	protected Paint getGradientPaint(Color color)
	{
		Paint gradientPaint = new GradientPaint(0,0,this.enlightColor(color,0.5f),200,200,color,
				false);
		
		return gradientPaint;
	}
	
	
	private Color enlightColor(Color originalRGBColor, float amount)
	{
		float[] hsv = new float[3];
		Color.RGBtoHSB(originalRGBColor.getRed(),originalRGBColor.getGreen(),
				originalRGBColor.getBlue(),hsv);
		hsv[1] = Math.min(1.0f,amount * hsv[1]);
		Color hsvColor = new Color(Color.HSBtoRGB(hsv[0],hsv[1],hsv[2]));
		return hsvColor;
	}
	
	
	/**
	 * 
	 * @see com.jidesoft.chart.Chart#setPieSegmentRenderer(com.jidesoft.chart.render
	 *      .PieSegmentRenderer)
	 */
	@Override
	public void setPieSegmentRenderer(PieSegmentRenderer renderer)
	{
		super.setPieSegmentRenderer(renderer);
		this.initOutlineRendering();
	}
	
}
