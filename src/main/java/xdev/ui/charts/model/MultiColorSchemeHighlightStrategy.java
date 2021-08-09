package xdev.ui.charts.model;

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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import xdev.ui.charts.AbstractRelationalStructureChart;
import xdev.ui.charts.utils.HSLColor;
import xdev.util.NamedColors;

import com.jidesoft.chart.model.ChartModel;
import com.jidesoft.chart.model.DefaultChartModel;
import com.jidesoft.chart.model.Highlight;
import com.jidesoft.chart.model.Highlightable;


/**
 * Defines how chart points will be colorized with multiple colors.
 * 
 * @author jwill
 * @since 4.0
 */
public class MultiColorSchemeHighlightStrategy implements ChartColorSchemeHighlightStrategy
{
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addPointColorSchemeHighlights(AbstractRelationalStructureChart<?, ?> chart,
			ChartModel model, Color color)
	{
		Color[] commonColors = NamedColors.getCommonColors();
		int delimiterCount = 0;
		int colorIterator = 0;
		for(int i = 0; i < model.getPointCount(); i++)
		{
			if(i / commonColors.length > delimiterCount)
			{
				delimiterCount++;
				commonColors = getNextCommonColors(commonColors);
				colorIterator = 0;
			}
			colorizePoint(chart,model,i,commonColors[colorIterator]);
			colorIterator++;
		}
	}
	
	
	private Color[] getNextCommonColors(Color[] colors)
	{
		Color[] nextColors = new Color[colors.length];
		
		for(int i = 0; i < colors.length; i++)
		{
			HSLColor base = new HSLColor(colors[i]);
			
			nextColors[i] = base.adjustTone(10);
		}
		
		return nextColors;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends DefaultChartModel> List<Color> getColorScheme(Collection<T> models,
			ChartColorScheme colorScheme)
	{
		List<Color> schemeValueList = new ArrayList<Color>();
		Color[] commonColors = NamedColors.getCommonColors();
		int colorIterator = 0;
		int delimiterCount = 0;
		
		for(int i = 0; i < models.size(); i++)
		{
			if(i / commonColors.length > delimiterCount)
			{
				delimiterCount++;
				commonColors = getNextCommonColors(commonColors);
				colorIterator = 0;
			}
			schemeValueList.add(commonColors[colorIterator]);
			colorIterator++;
		}
		
		return schemeValueList;
	}
	
	
	private void colorizePoint(AbstractRelationalStructureChart<?, ?> chart, ChartModel model,
			int index, Color color) throws IllegalAccessError
	{
		if(model.getPoint(index) instanceof Highlightable)
		{
			Highlight pointHighlight = new Highlight(color.toString() + index);
			Highlightable point = (Highlightable)model.getPoint(index);
			point.setHighlight(pointHighlight);
			chart.setHighlightStyle(pointHighlight,chart.createStyle(color));
		}
		else
		{
			throw new IllegalAccessError(
					"There must be highlightable chartPoints for PieChart segment coloring");
		}
	}
}
