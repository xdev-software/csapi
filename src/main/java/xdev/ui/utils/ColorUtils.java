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
package xdev.ui.utils;


import java.awt.Color;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.converter.HexColorConverter;
import com.jidesoft.converter.RgbColorConverter;


/**
 * <p>
 * The <code>ColorUtils</code> class provides utility methods for {@link Color}
 * handling for example parsing etc.
 * </p>
 * 
 * @author XDEV Software JWill
 * 
 * @since 4.0
 */
public final class ColorUtils
{
	public static final String	RGB_CONVERTER_CONTEXT		= "rgbConverterContext";
	public static final String	HEX_CONVERTER_CONTEXT		= "hexConverterContext";
	public static final String	DEFAULT_CONVERTER_CONTEXT	= "converterContext";
	
	
	/**
	 * Parses RGB color <code>Strings</code> into their {@link Color}
	 * representation.
	 * 
	 * @param rgbColorValue
	 *            the rgbColorValue, comma separated.
	 * 
	 * @return the {@link Color} representation of the given color
	 *         <code>String</code> or <code>null</code> if the given value is
	 *         not valid.
	 */
	public static Color getColorFromRGBColorString(String rgbColorValue, String context)
	{
		RgbColorConverter converter = new RgbColorConverter();
		
		Object concreteRGBColorValue = converter.fromString(rgbColorValue,new ConverterContext(
				context));
		
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
	 * Parses hex color <code>Strings</code> into their {@link Color}
	 * representation.
	 * 
	 * @param hexColorValue
	 *            the hexColorValue, comma separated.
	 * 
	 * @return the {@link Color} representation of the given color
	 *         <code>String</code> or <code>null</code> if the given value is
	 *         not valid.
	 */
	public static Color getColorFromHexColorString(String hexColorValue, String context)
	{
		HexColorConverter hexConverter = new HexColorConverter();
		Object concreteHexColorValue = hexConverter.fromString(hexColorValue.toString(),
				new ConverterContext(context));
		
		// cast/instance of because converter returns Object value
		if(concreteHexColorValue != null && concreteHexColorValue instanceof Color)
		{
			return (Color)concreteHexColorValue;
		}
		else
		{
			return null;
		}
	}
}
