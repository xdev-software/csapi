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

import xdev.db.DataType;


/**
 * DBMS return untyped values so type checks must be done to identify which
 * color parsing algorithm has to be used.
 * 
 * 
 * @author XDEV Software jwill
 * @since 4.0
 */
public final class ColorParserUtils
{
	
	/**
	 * Returns the {@link Color} representation for the given colorValue type.
	 * 
	 * @param colorValue
	 *            the colorValue (untyped)
	 * @param includeAlpha
	 *            the flag that determines if alpha should be considered on
	 *            computation.
	 * @return the {@link Color} representation for the given colorValue type.
	 */
	public static Color getRGBAColorFrom(Object colorValue, boolean includeAlpha)
	{
		if(colorValue instanceof Integer)
		{
			return new NumberRGBAColorParser(includeAlpha).getColorFromValue((Integer)colorValue);
		}
		else if(colorValue instanceof String)
		{
			return new StringRGBAColorParser(includeAlpha).getColorFromValue((String)colorValue);
		}
		throw new IllegalArgumentException("No suitable ColorParser found for " + colorValue);
	}
	
	
	/**
	 * Returns the value representation for the given {@link Color}.
	 * 
	 * @param color
	 *            the {@link Color}
	 * @param type
	 *            the column representation {@link DataType}.
	 * @param includeAlpha
	 *            the flag that determines if alpha should be considered on
	 *            computation.
	 * @return the value representation for the given {@link Color}.
	 */
	public static Object getRGBAValueFromColor(Color color, DataType type, boolean includeAlpha)
	{
		if(type.isNumeric())
		{
			return new NumberRGBAColorParser(includeAlpha).getValueFromColor(color);
		}
		else if(type.isString())
		{
			return new StringRGBAColorParser(includeAlpha).getValueFromColor(color);
		}
		throw new IllegalArgumentException("No suitable ColorParser found ");
	}
	
	
	/**
	 * Returns the {@link Color} representation for the given colorValue type.
	 * 
	 * @param colorValue
	 *            the colorValue (untyped)
	 * @param includeAlpha
	 *            the flag that determines if alpha should be considered on
	 *            computation.
	 * @return the {@link Color} representation for the given colorValue type.
	 */
	public static Color getHexColorFrom(Object colorValue, boolean includeAlpha)
	{
		if(colorValue instanceof String)
		{
			return new HexColorParser(includeAlpha).getColorFromValue((String)colorValue);
		}
		throw new IllegalArgumentException("No suitable ColorParser found for " + colorValue);
	}
	
	
	/**
	 * Returns the value representation for the given {@link Color}.
	 * 
	 * @param color
	 *            the {@link Color}
	 * @param type
	 *            the column representation {@link DataType}.
	 * @param includeAlpha
	 *            the flag that determines if alpha should be considered on
	 *            computation.
	 * @return the value representation for the given {@link Color}.
	 */
	public static Object getHexValueFromColor(Color color, DataType type, boolean includeAlpha)
	{
		if(type.isString())
		{
			return new HexColorParser(includeAlpha).getValueFromColor(color);
		}
		throw new IllegalArgumentException("No suitable ColorParser found");
	}
}
