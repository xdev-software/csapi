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


/**
 * 
 * @author XDEV Software jwill
 * @since 4.0
 */
public enum ChartColorScheme
{
	// HSB Values
	// 1,115,127
	RED(new Color(236,216,216), new SingleColorSchemeHightlightStrategy()),
	// 53, 101,130
	GREEN(new Color(227,235,217), new SingleColorSchemeHightlightStrategy()),
	// 142,109,126
	BLUE(new Color(241,244,250), new SingleColorSchemeHightlightStrategy()),
	// 178,60,123
	MAGENTA(new Color(222,218,229), new SingleColorSchemeHightlightStrategy()),
	// 160,0,75
	GREY(new Color(255,255,255), new SingleColorSchemeHightlightStrategy()),
	COMBINATION(new Color(0,0,0), new MultiColorSchemeHighlightStrategy());
	
	public ChartColorSchemeHighlightStrategy getHightlightStrategy()
	{
		return hightlightStrategy;
	}
	
	private Color								baseColor;
	private ChartColorSchemeHighlightStrategy	hightlightStrategy;
	
	
	/**
	 * @return the baseColor
	 */
	public Color getBaseColor()
	{
		return baseColor;
	}
	
	
	private ChartColorScheme(Color baseColor, ChartColorSchemeHighlightStrategy hightlightStrategy)
	{
		this.baseColor = baseColor;
		this.hightlightStrategy = hightlightStrategy;
	}
}
