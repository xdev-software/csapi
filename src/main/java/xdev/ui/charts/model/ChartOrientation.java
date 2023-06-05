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


/**
 * <code>ChartOrientation</code> mode.
 * 
 * @author XDEV Software jwill
 * @since 4.0
 * 
 */
public enum ChartOrientation
{
	/**
	 * Horizontal chart alignment.
	 */
	HORIZONTAL(new HorizontalChartOrientationStrategy()),
	
	/**
	 * Vertical chart alignment.
	 */
	VERTICAL(new VerticalChartOrientationStrategy());
	
	private final Chart3DOrientationStrategy	orientation;
	
	
	/**
	 * @return the orientation
	 */
	public Chart3DOrientationStrategy getOrientation()
	{
		return orientation;
	}
	
	
	ChartOrientation(Chart3DOrientationStrategy orientation)
	{
		this.orientation = orientation;
	}
}
