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


import java.awt.Color;
import java.util.Collection;
import java.util.List;

import com.jidesoft.chart.model.ChartModel;
import com.jidesoft.chart.model.DefaultChartModel;

import xdev.ui.charts.AbstractRelationalStructureChart;


/**
 * Defines how chart points will be colorized.
 * 
 * @author jwill
 * @since 4.0
 */
public interface ChartColorSchemeHighlightStrategy
{
	/**
	 * Colorizes each chart point.
	 * 
	 * @param chart
	 *            the chart to colorize.
	 * @param modelToAdd
	 *            the chart model to add.
	 * @param color
	 *            the basis color.
	 */
	void addPointColorSchemeHighlights(AbstractRelationalStructureChart<?, ?> chart,
			ChartModel modelToAdd, Color color);
	
	
	/**
	 * Returns a collection of colors starting from the given
	 * {@link ChartColorScheme#getBaseColor()} fitting the given chart models.
	 * 
	 * @param modelsToAdd
	 *            the concerned {@link ChartModel}s.
	 * @param colorScheme 
	 *            the defined colorScheme the basis color.
	 * @return a color scheme fitting the given chart models.
	 */
	<T extends DefaultChartModel> List<Color> getColorScheme(Collection<T> modelsToAdd,
			ChartColorScheme colorScheme);
}
