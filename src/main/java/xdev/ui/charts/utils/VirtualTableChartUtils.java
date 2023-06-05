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
package xdev.ui.charts.utils;


import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xdev.ui.charts.AbstractRelationalStructureChart;
import xdev.ui.charts.model.Chart3DOrientationStrategy;
import xdev.ui.charts.model.ChartColorScheme;
import xdev.ui.charts.model.VerticalChartOrientationStrategy;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTable.VirtualTableRow;
import xdev.vt.VirtualTableColumn;

import com.jidesoft.chart.Chart;
import com.jidesoft.chart.ChartType;
import com.jidesoft.chart.model.ChartModel;
import com.jidesoft.chart.model.DefaultChartModel;
import com.jidesoft.chart.style.ChartStyle;


/**
 * Provides utilities dealing with chart data associated with
 * {@link VirtualTable}s.
 * 
 * @author XDEV Software jwill
 * 
 * @since 4.0
 */
public class VirtualTableChartUtils
{
	private static final int	SINGLE_SERIES_MODEL	= 1;
	
	
	private static <T extends DefaultChartModel> void addSingleSeriesModel(
			AbstractRelationalStructureChart<?, ?> chart, ChartModel model, ChartColorScheme scheme)
	{
		scheme.getHightlightStrategy().addPointColorSchemeHighlights(chart,model,
				scheme.getBaseColor());
		
		// use "empty" style
		chart.addModel(model,chart.createStyle());
	}
	
	
	/**
	 * Resets all models from a particular {@link Chart}.
	 * 
	 * @param chart
	 *            to reset.
	 */
	public static void resetChartModels(final AbstractRelationalStructureChart<?, ?> chart)
	{
		if(chart.getModels() != null)
		{
			// reset chart models
			chart.getModels().removeAll(chart.getModels());
		}
	}
	
	
	/**
	 * Adds the given chart models to a certain {@link Chart}.
	 * 
	 * @param chart
	 *            the chart to add the models to.
	 * @param orientation
	 *            the plotting orientation.
	 * @param models
	 *            the models to add.
	 * @param colorScheme
	 *            the colorization basis.
	 * @param groupCount
	 *            the group indicator.
	 */
	public static <T extends DefaultChartModel> void addChartModels(
			final AbstractRelationalStructureChart<?, ?> chart,
			final Chart3DOrientationStrategy orientation, Collection<T> models,
			ChartColorScheme colorScheme, int groupCount)
	{
		resetChartModels(chart);
		final List<Color> scheme = colorScheme.getHightlightStrategy().getColorScheme(models,
				colorScheme);
		
		int modelCount = 0;
		for(final T model : models)
		{
			if(model.getPointCount() > 0)
			{
				if(chart.getChartType().equals(ChartType.PIE)
						|| (groupCount == 0 && models.size() == SINGLE_SERIES_MODEL))
				{
					addSingleSeriesModel(chart,model,colorScheme);
				}
				else
				{
					ChartStyle style = chart.createStyle(scheme.get(modelCount));
					orientation.setGraphOrientation(style);
					chart.addModel(model,style);
				}
			}
			modelCount++;
		}
	}
	
	
	/**
	 * Creates the seriesDataContext-{@link Map} which is the mapping between a
	 * concrete {@link ChartModel} and its seriesData.
	 * 
	 * @param container
	 * @param seriesColumnIndices
	 * @return the mapping between chart and group data.
	 */
	public static Map<List<Object>, DefaultChartModel> createChartSeriesDataContext(
			VirtualTable container, VirtualTableColumn<?>... seriesColumnIndices)
	{
		Map<List<Object>, DefaultChartModel> seriesContextMap = new HashMap<List<Object>, DefaultChartModel>();
		if(seriesColumnIndices.length > 0)
		{
			List<List<Object>> seriesRowDataList = createChartSeriesData(container,
					seriesColumnIndices);
			
			for(int i = 0; i < seriesRowDataList.size(); i++)
			{
				DefaultChartModel model = new DefaultChartModel(
						createUniqueSeriesName(seriesRowDataList.get(i)));
				seriesContextMap.put(seriesRowDataList.get(i),model);
			}
		}
		return seriesContextMap;
	}
	
	
	/**
	 * Returns all unique (grouped) chart series data sets from the given
	 * {@link VirtualTable}
	 * 
	 * @param container
	 *            the data container.
	 * @param seriesColumnIndices
	 *            the groupColumns.
	 * @return all unique groups from the given {@link VirtualTable}.
	 */
	public static List<List<Object>> createChartSeriesData(VirtualTable container,
			VirtualTableColumn<?>... seriesColumnIndices)
	{
		List<List<Object>> seriesDataList = new ArrayList<List<Object>>();
		
		for(int row = 0; row < container.getRowCount(); row++)
		{
			List<Object> currentSeriesData = getRowGroupData(container.getRow(row),
					seriesColumnIndices);
			
			// only add unique group combinations
			if(!seriesDataList.contains(currentSeriesData))
			{
				seriesDataList.add(currentSeriesData);
			}
		}
		return seriesDataList;
	}
	
	
	/**
	 * Returns the group data of the given row.
	 * 
	 * @param row
	 *            the {@link VirtualTableRow} containing the groups.
	 * @param groupColumns
	 *            the group columns.
	 * @return the group data of a particular {@link VirtualTableRow}.
	 */
	public static List<Object> getRowGroupData(VirtualTableRow row,
			VirtualTableColumn<?>... groupColumns)
	{
		List<Object> currentSeriesData = new ArrayList<Object>();
		for(int seriesColumnIndex = 0; seriesColumnIndex < groupColumns.length; seriesColumnIndex++)
		{
			currentSeriesData.add(row.get(groupColumns[seriesColumnIndex]));
		}
		
		return currentSeriesData;
	}
	
	
	/**
	 * Returns a certain {@link ChartModel} matching the given parameters from
	 * the mapped group data.
	 * 
	 * @param singleGroupChartModel
	 *            the chart model.
	 * @param groupContextMap
	 *            the unique chart data groups.
	 * @param groupData
	 *            the group data.
	 * @return
	 */
	public static DefaultChartModel getChartModel(DefaultChartModel singleGroupChartModel,
			Map<List<Object>, DefaultChartModel> groupContextMap, List<Object> groupData)
	{
		DefaultChartModel model = singleGroupChartModel;
		
		if(groupData.size() > 0)
		{
			// multi series
			return groupContextMap.get(groupData);
		}
		
		// add singleSeries model to chart data context.
		groupContextMap.put(null,model);
		// use single series model
		return model;
	}
	
	
	private static String createUniqueSeriesName(List<Object> seriesData)
	{
		String seriesName = "";
		
		for(Object object : seriesData)
		{
			seriesName += object.toString();
		}
		
		return seriesName;
	}
	
	
	/**
	 * Checks whether the given data is empty or not.
	 * 
	 * @param data
	 *            the data to check.
	 * @return the indicator whether the given data is empty or not.
	 */
	public static boolean checkVTData(VirtualTable data)
	{
		if(data.getRowCount() > 0)
		{
			return true;
		}
		return false;
	}
	
	
	public static Chart3DOrientationStrategy getOrientation(Chart3DOrientationStrategy orientation)
	{
		if(orientation != null)
		{
			return orientation;
		}
		else
		{
			// default orientation
			return new VerticalChartOrientationStrategy();
		}
	}
}
