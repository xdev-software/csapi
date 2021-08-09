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
import xdev.ui.charts.model.ChartColorScheme;
import xdev.ui.charts.model.HorizontalChartOrientationStrategy;
import xdev.ui.charts.model.VerticalChartOrientationStrategy;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTableColumn;

import com.jidesoft.chart.DefaultAutoRanger;
import com.jidesoft.chart.render.DefaultBarRenderer;
import com.jidesoft.chart.style.ChartStyle;


/**
 * A chart that plots bars indicating data values for a category.
 * <p>
 * The bars can be vertical or horizontal depending on which axis is a category
 * axis.
 * </p>
 * 
 * @see XdevBarChart#setChartOrientation(xdev.ui.charts.model.Chart3DOrientationStrategy)
 * 
 * @author XDEV Software jwill
 * @since 4.0
 */
public class XdevBarChart extends VirtualTableAbstractCategoryChart implements GroupableChart
{
	/**
	 * the serialization id.
	 */
	private static final long	serialVersionUID	= 1L;
	
	
	/**
	 * Initializes an empty valued numeric XY-chart.
	 */
	public XdevBarChart()
	{
		super();
		this.initRendering();
	}
	
	
	/**
	 * Initializes an empty valued numeric XY-chart.
	 */
	public XdevBarChart(ChartColorScheme colorScheme)
	{
		super(colorScheme);
		this.initRendering();
	}
	
	
	/**
	 * Initializes the bar outline rendering.
	 */
	private void initRendering()
	{
		// cast because of strange return type behavior.
		DefaultBarRenderer renderer = (DefaultBarRenderer)this.getBarRenderer();
		renderer.setOutlineColor(Color.darkGray);
		renderer.setOutlineWidth(0.5f);
		renderer.setAlwaysShowOutlines(true);
		this.setShadowVisible(true);
		this.setBarsGrouped(true);
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
			style.setBarWidthProportion(DEFAULT_BAR_GAP_PROPORTION);
			style.setBarColor(color);
			style.setLinesVisible(false);
			style.setPointsVisible(false);
			style.setBarsVisible(true);
		}
		else
		{
			style = new ChartStyle(color);
			style.setBarWidthProportion(DEFAULT_BAR_GAP_PROPORTION);
			style.setBarColor(color);
			style.setLinesVisible(false);
			style.setPointsVisible(false);
			style.setBarsVisible(true);
		}
		
		return style;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setBarsGrouped(boolean barsGrouped)
	{
		super.setBarsGrouped(barsGrouped);
		this.setBarGroupGapProportion(DEFAULT_BAR_GROUP_GAP_PROPORTION);
		
		if(!barsGrouped)
		{
			this.initStackedBarsAutoRanging(getChartOrientation());
		}
		else
		{
			this.setAutoRanging(!barsGrouped);
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAutoRanging(boolean autoRanging)
	{
		if(!this.isBarsGrouped())
		{
			super.setAutoRanging(autoRanging);
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
			style.setBarWidthProportion(DEFAULT_BAR_GAP_PROPORTION);
			style.setLinesVisible(false);
			style.setPointsVisible(false);
			style.setBarsVisible(true);
		}
		else
		{
			style = new ChartStyle();
			style.setBarWidthProportion(DEFAULT_BAR_GAP_PROPORTION);
			style.setLinesVisible(false);
			style.setPointsVisible(false);
			style.setBarsVisible(true);
			
		}
		return style;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setModel(VirtualTableColumn<?> categoryColumn,
			VirtualTableColumn<?> valueColumn, VirtualTable vt,
			VirtualTableColumn<?>... groupColumns)
	{
		super.setModel(categoryColumn,valueColumn,vt,groupColumns);
		this.initStackedBarsAutoRanging(this.getChartOrientation());
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setModel(VirtualTableColumn<?> categoryColumn,
			VirtualTableColumn<?> valueColumn, VirtualTableColumn<?> depthColumn,
			VirtualTable vt, VirtualTableColumn<?>... groupColumns)
	{
		super.setModel(categoryColumn,valueColumn,depthColumn,vt,groupColumns);
		this.initStackedBarsAutoRanging(this.getChartOrientation());
	}
	
	
	protected void initStackedBarsAutoRanging(Chart3DOrientationStrategy orientation)
	{
		if(!this.isBarsGrouped())
		{
			if(this.getXAxis() != null && this.getYAxis() != null)
			{
				if(orientation instanceof VerticalChartOrientationStrategy)
				{
					// zero alignment + adapting value range for stacked
					// BarCharts
					// (which is the default behavior for multi series
					// BarCharts)
					this.setAutoRanging(true);
					this.setAutoRanger(new DefaultAutoRanger(0.0,0.0,this.getXAxis().getRange()
							.maximum(),null));
					// refresh axis manually...
					if(this.getAutoRanger().getRanges(this).getSecond() != null)
					{
						this.getYAxis().setRange(this.getAutoRanger().getRanges(this).getSecond());
					}
				}
				else if(orientation instanceof HorizontalChartOrientationStrategy)
				{
					// zero alignment + adapting value range for stacked
					// BarCharts
					// (which is the default behavior for multi series
					// BarCharts)
					this.setAutoRanging(true);
					this.setAutoRanger(new DefaultAutoRanger(0.0,0.0,null,this.getYAxis()
							.getRange().maximum()));
					// refresh axis manually...
					if(this.getAutoRanger().getRanges(this).getFirst() != null)
					{
						this.getXAxis().setRange(this.getAutoRanger().getRanges(this).getFirst());
					}
				}
			}
		}
	}
}
