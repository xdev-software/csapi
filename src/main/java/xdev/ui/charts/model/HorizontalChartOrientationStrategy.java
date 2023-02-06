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


import com.jidesoft.chart.Chart;
import com.jidesoft.chart.Orientation;
import com.jidesoft.chart.axis.Axis;
import com.jidesoft.chart.model.ChartPoint;
import com.jidesoft.chart.model.ChartPoint3D;
import com.jidesoft.chart.model.Highlight;
import com.jidesoft.chart.style.ChartStyle;
import com.jidesoft.range.Positionable;


/**
 * 
 * @author XDEV Software jwill
 * @since 4.0
 */
public class HorizontalChartOrientationStrategy implements Chart3DOrientationStrategy
{
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCategoryAxis(Chart chart, Axis categoryAxis)
	{
		chart.setYAxis(categoryAxis);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setValueAxis(Chart chart, Axis valueAxis)
	{
		chart.setXAxis(valueAxis);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setGraphOrientation(ChartStyle chartStyle)
	{
		chartStyle.setBarOrientation(Orientation.horizontal);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ChartPoint getOrientedPoint(Positionable category, Positionable value)
	{
		return new ChartPoint(value,category);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ChartPoint getOrientedPoint(Positionable category, Positionable value,
			Highlight highlight)
	{
		return new ChartPoint(value,category,highlight);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ChartPoint getOrientedPoint(Positionable category, Positionable value,
			Positionable depthValue)
	{
		ChartPoint3D point3D = new ChartPoint3D();
		point3D.setX(value);
		point3D.setY(category);
		point3D.setZ(depthValue);
		
		return point3D;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ChartPoint getOrientedPoint(Positionable category, Positionable value,
			Positionable depthValue, Highlight highlight)
	{
		ChartPoint3D point3D = new ChartPoint3D();
		point3D.setX(value);
		point3D.setY(category);
		point3D.setZ(depthValue);
		
		point3D.setHighlight(highlight);
		
		return point3D;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Axis getCategoryAxis(Chart chart)
	{
		return chart.getYAxis();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Axis getValueAxis(Chart chart)
	{
		return chart.getXAxis();
	}
}
