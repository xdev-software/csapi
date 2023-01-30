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


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.UIManager;

import xdev.db.DBException;
import xdev.ui.BeanProperty;
import xdev.ui.DefaultBeanCategories;
import xdev.ui.XComponent;
import xdev.ui.charts.model.Chart3DOrientationStrategy;
import xdev.ui.charts.model.ChartColorScheme;
import xdev.ui.charts.model.ChartDataInitializer;
import xdev.ui.charts.model.XdevChartAxisType;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTableColumn;
import xdev.vt.VirtualTableException;

import com.jidesoft.chart.Chart;
import com.jidesoft.chart.Legend;


/**
 * Chart basis class for all two to three axis charts. It is responsible for
 * drawing the axes and the plot content. Also supports z coordinates to specify
 * the diameter of a point.
 * <p>
 * Usually set the z coordinate to a value in proportion to the square root of
 * the quantity that you wish the point size to show.
 * </p>
 * <p>
 * Implementations of <code>XdevAbstractCategoryChart</code> can add their
 * specific chart style.
 * </p>
 * 
 * <p>
 * <b>Automatic value and category ranging is enabled by default</b>
 * </p>
 * 
 * @see XdevBarChart
 * @see XdevPieChart
 * @see XdevLineChart
 * @see XdevPointChart
 * @see XdevAreaChart
 * 
 * @author XDEV Software jwill
 * @since 4.0
 */
public abstract class VirtualTableAbstractCategoryChart extends
		AbstractRelationalStructureChart<VirtualTable, VirtualTableColumn<?>>
{
	/**
	 * the serialization id.
	 */
	private static final long	serialVersionUID			= 1L;
	
	protected static final int	DEFAULT_SHADOW_SIZE			= 2;
	
	private static final int	VERTICAL_LEGEND_ALIGNMENT	= 1;
	
	private ChartColorScheme	colorScheme					= null;
	
	private boolean				autoQueryData				= false;
	
	
	/**
	 * Indicates whether the set data should be auto queried or not.
	 * 
	 * @return the indicator whether the set data should be auto queried or not.
	 */
	public boolean isAutoQueryData()
	{
		return autoQueryData;
	}
	
	
	/**
	 * Indicates whether the set data should be auto queried or not.
	 * 
	 * @param autoQueryData
	 *            the auto query indicator.
	 */
	@BeanProperty(category = DefaultBeanCategories.DATA)
	public void setAutoQueryData(boolean autoQueryData)
	{
		this.autoQueryData = autoQueryData;
	}
	
	
	/**
	 * Initializes an empty valued numeric XY-chart.
	 */
	public VirtualTableAbstractCategoryChart()
	{
		super();
		this.initStyle();
		this.setAutoRanging(false);
	}
	
	
	/**
	 * Initializes an empty valued numeric XY-chart.
	 */
	public VirtualTableAbstractCategoryChart(ChartColorScheme colorScheme)
	{
		super();
		this.initStyle();
		this.setAutoRanging(false);
		this.colorScheme = colorScheme;
	}
	
	
	protected void initStyle()
	{
		this.setBackground(Color.WHITE);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setModel(VirtualTableColumn<?> categoryColumnIndex,
			VirtualTableColumn<?> valueColumnIndex, VirtualTable vt,
			VirtualTableColumn<?>... groupColumnIndices)
	{
		this.checkAutoQueryVT(vt);
		this.setModel(vt,categoryColumnIndex,valueColumnIndex,this.getChartOrientation(),
				groupColumnIndices);
	}
	
	
	private void setModel(VirtualTable vt, VirtualTableColumn<?> categoryColumnIndex,
			VirtualTableColumn<?> valueColumnIndex, Chart3DOrientationStrategy orientation,
			VirtualTableColumn<?>... groupColumnIndices)
	{
		
		ChartDataInitializer<VirtualTable, VirtualTableColumn<?>> initializer = XdevChartAxisType
				.getAxis(categoryColumnIndex,valueColumnIndex).getDataInitializer(this);
		
		initializer.setChartColorScheme(this.getColorScheme());
		initializer.initializeChartModel(vt,categoryColumnIndex,valueColumnIndex,orientation,
				groupColumnIndices);
	}
	
	
	private void setModel(VirtualTable vt, VirtualTableColumn<?> categoryColumnIndex,
			VirtualTableColumn<?> valueColumnIndex, VirtualTableColumn<?> depthColumnIndex,
			Chart3DOrientationStrategy orientation, VirtualTableColumn<?>... groupColumnIndices)
	{
		
		ChartDataInitializer<VirtualTable, VirtualTableColumn<?>> initializer = XdevChartAxisType
				.getAxis(categoryColumnIndex,valueColumnIndex).getDataInitializer(this);
		
		initializer.setChartColorScheme(this.getColorScheme());
		initializer.initializeChartModel(vt,categoryColumnIndex,valueColumnIndex,depthColumnIndex,
				orientation,groupColumnIndices);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setModel(VirtualTableColumn<?> categoryColumnIndex,
			VirtualTableColumn<?> valueColumnIndex, VirtualTableColumn<?> depthColumnIndex,
			VirtualTable vt, VirtualTableColumn<?>... groupColumnIndices)
	{
		this.checkAutoQueryVT(vt);
		this.setModel(vt,categoryColumnIndex,valueColumnIndex,depthColumnIndex,
				this.getChartOrientation(),groupColumnIndices);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Chart clearHighlights()
	{
		return super.clearHighlights();
	}
	
	
	protected void initAxisFont()
	{
		this.getXAxis().setTickFont(UIManager.getFont("Label.font"));
		this.getYAxis().setTickFont(UIManager.getFont("Label.font"));
	}
	
	
	/**
	 * Creates the panel which contains this {@link VirtualTableChart} and
	 * related {@link Legend}<br>
	 * This is a convenience method to build layout constructs.
	 * 
	 * @return a panel containing this {@link VirtualTableChart} and the related
	 *         {@link Legend}.
	 */
	public XComponent createLegend(Object legendConstraints)
	{
		if(this.getModels() != null)
		{
			JPanel legendPanel = new JPanel();
			Legend legend = new Legend(this,this.getModels().size());
			if(legendConstraints.equals(BorderLayout.WEST)
					|| legendConstraints.equals(BorderLayout.EAST))
			{
				legend.setColumns(VERTICAL_LEGEND_ALIGNMENT);
			}
			legend.setBackground(this.getBackground());
			legendPanel.setBackground(this.getBackground());
			legendPanel.add(legend);
			
			XComponent pnl = new XComponent(new BorderLayout());
			pnl.add(this,BorderLayout.CENTER);
			pnl.add(legendPanel,legendConstraints);
			
			return pnl;
		}
		throw new RuntimeException(
				"Chart must be filled with at least one model to create a legend");
	}
	
	
	/**
	 * Returns the currently considered color scheme.
	 * 
	 * @return the colorScheme
	 */
	public ChartColorScheme getColorScheme()
	{
		return colorScheme;
	}
	
	
	/**
	 * Sets a color scheme which is considered when computing the chart model.
	 * 
	 * @param colorScheme
	 *            the colorScheme to set
	 */
	public void setColorScheme(ChartColorScheme colorScheme)
	{
		this.colorScheme = colorScheme;
	}
	
	
	private void checkAutoQueryVT(VirtualTable vt)
	{
		if(this.isAutoQueryData())
		{
			try
			{
				vt.queryAndFill();
			}
			catch(VirtualTableException e)
			{
				e.printStackTrace();
			}
			catch(DBException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	
	// IDE PROPERTIES
	
	/**
	 * {@inheritDoc}
	 */
	@BeanProperty(category = DefaultBeanCategories.DESIGN)
	@Override
	public void setLabelColor(Color labelColor)
	{
		super.setLabelColor(labelColor);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@BeanProperty(category = DefaultBeanCategories.DESIGN, owner = "selectionEnabled")
	@Override
	public void setSelectionShowsOutline(boolean selectionShowsOutline)
	{
		super.setSelectionShowsOutline(selectionShowsOutline);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@BeanProperty(category = DefaultBeanCategories.DESIGN)
	@Override
	public void setSelectionEnabled(boolean selectionEnabled)
	{
		super.setSelectionEnabled(selectionEnabled);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@BeanProperty(category = DefaultBeanCategories.DESIGN)
	@Override
	public void setRolloverEnabled(boolean rolloverEnabled)
	{
		super.setRolloverEnabled(rolloverEnabled);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@BeanProperty(category = DefaultBeanCategories.DESIGN)
	@Override
	public void setShadowVisible(boolean shadowVisible)
	{
		super.setShadowVisible(shadowVisible);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@BeanProperty(category = DefaultBeanCategories.DESIGN)
	@Override
	public void setTickFont(Font tickFont)
	{
		super.setTickFont(tickFont);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@BeanProperty(category = DefaultBeanCategories.DESIGN)
	@Override
	public void setTickColor(Color tickColor)
	{
		super.setTickColor(tickColor);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@BeanProperty(category = DefaultBeanCategories.DESIGN)
	@Override
	public void setGridColor(Color gridColor)
	{
		super.setGridColor(gridColor);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@BeanProperty(category = DefaultBeanCategories.DESIGN)
	@Override
	public void setAnimateOnShow(boolean arg0)
	{
		super.setAnimateOnShow(arg0);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@BeanProperty(category = DefaultBeanCategories.MISC)
	@Override
	public void setFont(Font font)
	{
		super.setFont(font);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@BeanProperty(category = DefaultBeanCategories.MISC)
	@Override
	public void setForeground(Color fg)
	{
		super.setForeground(fg);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Dimension getPreferredSize()
	{
		if(super.getPreferredSize().getHeight() <= 1 && super.getPreferredSize().getWidth() <= 1)
		{
			// default preview size
			return new Dimension(450,200);
		}
		return super.getPreferredSize();
	}
}
