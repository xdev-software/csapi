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
package xdev.ui.util;


import java.awt.Color;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.converter.RgbColorConverter;


/**
 * Converts from RGBA {@link String} value to {@link Color} Object.
 * 
 * 
 * @author XDEV Software jwill
 * @since 4.0
 */
public class StringRGBAColorParser implements ColorParser<String>
{
	private static final String	RGB_CONVERTER_CONTEXT	= "rgbContext";
	private boolean				alphaIncluded			= false;
	
	
	public StringRGBAColorParser()
	{
		super();
	}
	
	
	public StringRGBAColorParser(boolean includeAlpha, String... alphaValue)
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
	 * @param colorValue
	 *            the {@link String} color value.
	 * 
	 * 
	 * @return the {@link Color} representation of the given string value.
	 */
	@Override
	public Color getColorFromValue(String colorValue)
	{
		RgbColorConverter converter = new RgbColorConverter(this.isAlphaIncluded());
		
		Object concreteRGBColorValue = converter.fromString(colorValue,new ConverterContext(
				RGB_CONVERTER_CONTEXT));
		
		// cast/instance of because converter returns Object value
		if(concreteRGBColorValue != null && concreteRGBColorValue instanceof Color)
		{
			return (Color)concreteRGBColorValue;
		}
		else
		{
			return null;
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
	public String getValueFromColor(Color color)
	{
		RgbColorConverter converter = new RgbColorConverter(this.isAlphaIncluded());
		
		String concreteRGBColorValue = converter.toString(color,new ConverterContext(
				RGB_CONVERTER_CONTEXT));
		
		return concreteRGBColorValue;
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
