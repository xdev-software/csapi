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


import java.util.Date;
import java.util.List;
import java.util.Map;

import xdev.ui.charts.AbstractRelationalStructureChart;
import xdev.ui.charts.utils.ChartRangeComputationUtils;
import xdev.ui.charts.utils.VirtualTableChartUtils;
import xdev.ui.charts.utils.VirtualTableColumnTypeUtils;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTableAdapter;
import xdev.vt.VirtualTableColumn;
import xdev.vt.VirtualTableEvent;

import com.jidesoft.chart.axis.CategoryAxis;
import com.jidesoft.chart.axis.NumericAxis;
import com.jidesoft.chart.model.DefaultChartModel;
import com.jidesoft.chart.model.RealPosition;
import com.jidesoft.chart.model.TimePosition;
import com.jidesoft.range.CategoryRange;
import com.jidesoft.range.Range;
import com.jidesoft.range.TimeRange;


/**
 * 
 * @author XDEV Software jwill
 * @since 4.0
 */
public class VTStringTimeChartDataInitializer extends VirtualTableAdapter implements
		ChartDataInitializer<VirtualTable, VirtualTableColumn<?>>
{
	
	/**
	 * the values stored in a {@link TimeRange}.
	 */
	private Range<Date>																timeValueRange;
	
	/**
	 * the category values stored in a {@link CategoryAxis}.
	 */
	private CategoryRange<String>													categoryRange;
	
	/**
	 * the chart to initialze.
	 */
	private AbstractRelationalStructureChart<VirtualTable, VirtualTableColumn<?>>	chart;
	
	private ChartColorScheme														colorScheme;
	
	private Chart3DOrientationStrategy												orientation;
	
	// concrete data
	private VirtualTable															modelVT;
	private VirtualTableColumn<? extends String>									categoryColumn;
	private VirtualTableColumn<? extends Date>										valueColumn;
	private VirtualTableColumn<? extends Number>									depthColumn;
	private VirtualTableColumn<?>[]													seriesColumns;
	
	
	/**
	 * Creates an empty {@link OrientationalChartDataInitializer}.
	 * <p>
	 * The chart orientation can be set via
	 * {@link OrientationalChartDataInitializer#setChartOrientation(Chart3DOrientationStrategy)}
	 * .
	 * </p>
	 */
	public VTStringTimeChartDataInitializer()
	{
		super();
	}
	
	
	public VTStringTimeChartDataInitializer(
			AbstractRelationalStructureChart<VirtualTable, VirtualTableColumn<?>> chart)
	{
		super();
		this.setChart(chart);
	}
	
	
	/**
	 * @return the chart
	 */
	@Override
	public AbstractRelationalStructureChart<VirtualTable, VirtualTableColumn<?>> getChart()
	{
		if(this.chart == null)
		{
			throw new NullPointerException("A chart must be set to initialize data");
		}
		else
		{
			return chart;
		}
	}
	
	
	@Override
	public void setChart(AbstractRelationalStructureChart<VirtualTable, VirtualTableColumn<?>> chart)
	{
		this.chart = chart;
	}
	
	
	private void initializeCategoryAxis(VirtualTableColumn<?> categoryColumnIndex,
			VirtualTable container, Chart3DOrientationStrategy orientation)
	{
		this.categoryRange = ChartRangeComputationUtils.computeStringRange(container,
				container.getColumnIndex(categoryColumnIndex));
		
		orientation.setCategoryAxis(this.getChart(),new CategoryAxis<String>(this.categoryRange,
				categoryColumnIndex.getName()));
	}
	
	
	// single value column data
	private void initializeValueAxis(VirtualTableColumn<?> valueColumnIndex,
			VirtualTable container, Chart3DOrientationStrategy orientation)
	{
		this.timeValueRange = ChartRangeComputationUtils.computeTimeValueRange(container,
				container.getColumnIndex(valueColumnIndex));
		
		orientation.setValueAxis(this.getChart(),new NumericAxis(this.timeValueRange));
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initializeChartModel(VirtualTable container,
			VirtualTableColumn<?> categoryColumnIndex, VirtualTableColumn<?> valueColumnIndex,
			Chart3DOrientationStrategy orientation, VirtualTableColumn<?>... seriesColumnIndices)
	{
		this.modelVT = container;
		this.modelVT.addVirtualTableListener(this);
		this.categoryColumn = VirtualTableColumnTypeUtils.isStringColumn(categoryColumnIndex);
		this.valueColumn = VirtualTableColumnTypeUtils.isDateColumn(valueColumnIndex);
		this.seriesColumns = seriesColumnIndices;
		this.orientation = orientation;
		
		if(VirtualTableChartUtils.checkVTData(this.modelVT))
		{
			this.initializeCategoryAxis(categoryColumnIndex,container,orientation);
			this.initializeValueAxis(valueColumnIndex,container,orientation);
			Map<List<Object>, DefaultChartModel> seriesContextMap = VirtualTableChartUtils
					.createChartSeriesDataContext(container,seriesColumnIndices);
			
			DefaultChartModel singleSeriesModel = new DefaultChartModel(valueColumn.getName());
			DefaultChartModel currentSeriesModel = null;
			for(int row = 0; row < container.getRowCount(); row++)
			{ // select model
				currentSeriesModel = VirtualTableChartUtils.getChartModel(singleSeriesModel,
						seriesContextMap,VirtualTableChartUtils.getRowGroupData(
								container.getRow(row),seriesColumnIndices));
				
				Date value = container.getValueAt(row,valueColumn);
				currentSeriesModel.addPoint(orientation.getOrientedPoint(ChartRangeComputationUtils
						.getCategory(this.categoryRange,
								container.getValueAt(row,this.categoryColumn)),new TimePosition(
						value.getTime())));
			}
			VirtualTableChartUtils.addChartModels(this.getChart(),orientation,
					seriesContextMap.values(),this.getChartColorScheme(),this.seriesColumns.length);
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initializeChartModel(VirtualTable container,
			VirtualTableColumn<?> categoryColumnIndex, VirtualTableColumn<?> valueColumnIndex,
			VirtualTableColumn<?> depthColumnIndex, Chart3DOrientationStrategy orientation,
			VirtualTableColumn<?>... seriesColumnIndices)
	{
		this.modelVT = container;
		this.modelVT.addVirtualTableListener(this);
		this.categoryColumn = VirtualTableColumnTypeUtils.isStringColumn(categoryColumnIndex);
		this.valueColumn = VirtualTableColumnTypeUtils.isDateColumn(valueColumnIndex);
		this.depthColumn = VirtualTableColumnTypeUtils.isNumericColumn(depthColumnIndex);
		this.seriesColumns = seriesColumnIndices;
		this.orientation = orientation;
		
		if(VirtualTableChartUtils.checkVTData(this.modelVT))
		{
			this.initializeCategoryAxis(categoryColumnIndex,container,orientation);
			this.initializeValueAxis(valueColumnIndex,container,orientation);
			Map<List<Object>, DefaultChartModel> seriesContextMap = VirtualTableChartUtils
					.createChartSeriesDataContext(container,seriesColumnIndices);
			
			DefaultChartModel singleSeriesModel = new DefaultChartModel(valueColumn.getName());
			DefaultChartModel currentSeriesModel = null;
			for(int row = 0; row < container.getRowCount(); row++)
			{
				currentSeriesModel = VirtualTableChartUtils.getChartModel(singleSeriesModel,
						seriesContextMap,VirtualTableChartUtils.getRowGroupData(
								container.getRow(row),seriesColumnIndices));
				Date value = container.getValueAt(row,valueColumn);
				Number depthValue = container.getValueAt(row,depthColumn);
				
				currentSeriesModel.addPoint(orientation.getOrientedPoint(ChartRangeComputationUtils
						.getCategory(this.categoryRange,
								container.getValueAt(row,this.categoryColumn)),new TimePosition(
						value.getTime()),new RealPosition(depthValue.doubleValue())));
			}
			VirtualTableChartUtils.addChartModels(this.getChart(),orientation,
					seriesContextMap.values(),this.getChartColorScheme(),this.seriesColumns.length);
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setChartColorScheme(ChartColorScheme colorScheme)
	{
		this.colorScheme = colorScheme;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ChartColorScheme getChartColorScheme()
	{
		if(this.colorScheme != null)
		{
			return this.colorScheme;
		}
		else
		{
			return ChartDataInitializer.DEFAULT_CHART_COLORSCHEME;
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void virtualTableDataChanged(VirtualTableEvent event)
	{
		// re/initialize model
		if(this.depthColumn != null)
		{
			this.initializeChartModel(this.modelVT,this.categoryColumn,this.valueColumn,
					this.depthColumn,this.orientation,this.seriesColumns);
		}
		else
		{
			this.initializeChartModel(this.modelVT,this.categoryColumn,this.valueColumn,
					this.orientation,this.seriesColumns);
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void virtualTableRowInserted(VirtualTableEvent event)
	{
		// re/initialize model
		if(this.depthColumn != null)
		{
			this.initializeChartModel(this.modelVT,this.categoryColumn,this.valueColumn,
					this.depthColumn,this.orientation,this.seriesColumns);
		}
		else
		{
			this.initializeChartModel(this.modelVT,this.categoryColumn,this.valueColumn,
					this.orientation,this.seriesColumns);
		}
	}
}
