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

import com.jidesoft.chart.model.ChartModel;
import com.jidesoft.chart.model.DefaultChartModel;
import com.jidesoft.chart.model.Highlight;
import com.jidesoft.chart.model.Highlightable;


/**
 * Defines how chart points will be colorized based on a single color.
 * 
 * @author jwill
 * @since 4.0
 */
public class SingleColorSchemeHightlightStrategy implements ChartColorSchemeHighlightStrategy
{
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addPointColorSchemeHighlights(AbstractRelationalStructureChart<?, ?> chart,
			ChartModel model, Color color)
	{
		createShadedColorization(chart,model,color);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends DefaultChartModel> List<Color> getColorScheme(Collection<T> models,
			ChartColorScheme colorScheme)
	{
		List<Color> schemeValueList = new ArrayList<Color>();
		schemeValueList.add(colorScheme.getBaseColor());
		
		for(int i = 1; i < models.size(); i++)
		{
			HSLColor base = new HSLColor(schemeValueList.get(i - 1));
			schemeValueList.add(base.adjustShade(100 / models.size()));
		}
		
		return schemeValueList;
	}
	
	
	private void createShadedColorization(AbstractRelationalStructureChart<?, ?> chart,
			ChartModel model, Color color)
	{
		Color shaded = new Color(color.getRGB());
		
		for(int i = 0; i < model.getPointCount(); i++)
		{
			HSLColor base = new HSLColor(shaded);
			
			shaded = base.adjustShade(100 / model.getPointCount());
			colorizePoint(chart,model,i,shaded);
		}
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
