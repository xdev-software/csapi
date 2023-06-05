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
import com.jidesoft.converter.HexColorConverter;


/**
 * Converts from hex- {@link String} value to {@link Color} Object.
 * 
 * @author XDEV Software jwill
 * @since 4.0
 */
public class HexColorParser implements ColorParser<String>
{
	private static final String	HEX_CONVERTER_CONTEXT	= "hexContext";
	private boolean				alphaIncluded			= false;
	
	
	public HexColorParser()
	{
		super();
	}
	
	
	public HexColorParser(boolean includeAlpha)
	{
		this.setAlphaIncluded(includeAlpha);
	}
	
	
	/**
	 * Converts Color to/from "#xxxxxx" format. For example "#000000" is
	 * {@link Color#black} and #FF0000 is {@link Color#red}.
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
		HexColorConverter hexConverter = new HexColorConverter(this.isAlphaIncluded());
		Object concreteHexColorValue = hexConverter.fromString(colorValue,new ConverterContext(
				HEX_CONVERTER_CONTEXT));
		
		if(concreteHexColorValue != null && concreteHexColorValue instanceof Color)
		{
			return (Color)concreteHexColorValue;
		}
		else
		{
			return null;
		}
	}
	
	
	/**
	 * Converts Color to/from "#xxxxxx" format. For example "#000000" is
	 * {@link Color#black} and #FF0000 is {@link Color#red}.
	 * 
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
		HexColorConverter hexConverter = new HexColorConverter(this.isAlphaIncluded());
		String concreteHexColorValue = hexConverter.toString(color,new ConverterContext(
				HEX_CONVERTER_CONTEXT));
		
		return concreteHexColorValue;
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
