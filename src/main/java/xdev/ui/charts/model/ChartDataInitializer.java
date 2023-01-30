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


import xdev.ui.charts.AbstractRelationalStructureChart;


/**
 * Initializes a charts axis and data.
 * 
 * @param <ContainerType>
 *            the container type which provides the model data.
 * 
 * @author XDEV Software jwill
 * @since 4.0
 */
public interface ChartDataInitializer<ContainerType, ValueType>
{
	
	static final Chart3DOrientationStrategy	DEFAULT_ORIENTATION_STRATEGY	= new VerticalChartOrientationStrategy();
	static final ChartColorScheme			DEFAULT_CHART_COLORSCHEME		= ChartColorScheme.BLUE;
	
	
	public void initializeChartModel(ContainerType container, ValueType categoryColumnIndex,
			ValueType valueColumnIndex, Chart3DOrientationStrategy orientation,
			ValueType... seriesColumnIndices);
	
	
	public void initializeChartModel(ContainerType container, ValueType categoryColumnIndex,
			ValueType valueColumnIndex, ValueType depthColumnIndex,
			Chart3DOrientationStrategy orientation, ValueType... seriesColumnIndices);

	
	/**
	 * Returns the initialized chart.
	 * 
	 * @return the initialized chart.
	 */
	AbstractRelationalStructureChart<ContainerType, ValueType> getChart();
	
	
	/**
	 * Sets the chart to initialize.
	 * 
	 * @param chart
	 *            the chart to initialize.
	 */
	void setChart(AbstractRelationalStructureChart<ContainerType, ValueType> chart);
	
	
	/**
	 * 
	 * @param colorScheme
	 */
	void setChartColorScheme(ChartColorScheme colorScheme);
	
	
	/**
	 * 
	 * @return
	 */
	ChartColorScheme getChartColorScheme();
}
