package xdev.ui.util;

/*-
 * #%L
 * XDEV Component Suite
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
 * Converts from RGBA {@link Number} value to {@link Color} Object.
 * 
 * 
 * @author XDEV Software jwill
 * @since 4.0
 */
public class NumberRGBAColorParser implements ColorParser<Integer>
{
	private boolean	alphaIncluded	= false;
	
	
	public NumberRGBAColorParser()
	{
		super();
	}
	
	
	public NumberRGBAColorParser(boolean includeAlpha)
	{
		this.setAlphaIncluded(includeAlpha);
	}
	
	
	/**
	 * If alpha value is not included, converts Color to/from "xxx, xxx, xxx"
	 * format. For example "0, 0, 0" is Color(0, 0, 0) and "255, 0, 255" is
	 * Color(255, 0, 255).
	 * 
	 * <p>
	 * If alpha value is included, converts Color to/from "xxx, xxx, xxx, xxx"
	 * format. For example "0, 0, 0, 255" is Color(0, 0, 0, 255) and
	 * "255, 0, 255, 100" is Color(255, 0, 255, 100).
	 * </p>
	 * 
	 * @param rgbColorValue
	 *            the {@link Integer} color value.
	 * 
	 * 
	 * @return the {@link Color} representation of the given string value.
	 */
	@Override
	public Color getColorFromValue(Integer rgbColorValue)
	{
		if(this.isAlphaIncluded())
		{
			return new Color(rgbColorValue,this.isAlphaIncluded());
		}
		else
		{
			return new Color(rgbColorValue);
		}
	}
	
	
	/**
	 * If alpha value is not included, converts Color to/from "xxx, xxx, xxx"
	 * format. For example "0, 0, 0" is Color(0, 0, 0) and "255, 0, 255" is
	 * Color(255, 0, 255).
	 * 
	 * <p>
	 * If alpha value is included, converts Color to/from "xxx, xxx, xxx, xxx"
	 * format. For example "0, 0, 0, 255" is Color(0, 0, 0, 255) and
	 * "255, 0, 255, 100" is Color(255, 0, 255, 100).
	 * </p>
	 * 
	 * @param color
	 *            the {@link Color} object.
	 * 
	 * 
	 * @return the {@link String} representation of the given color value.
	 */
	@Override
	public Integer getValueFromColor(Color color)
	{
		return color.getRGB();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isAlphaIncluded()
	{
		return this.alphaIncluded;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAlphaIncluded(boolean alphaIncluded)
	{
		this.alphaIncluded = alphaIncluded;
	}
}
