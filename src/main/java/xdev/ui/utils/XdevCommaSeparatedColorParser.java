package xdev.ui.utils;

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
 * Parses color values, for example RGB, HEX-<code>String</code>s or color
 * <code>Integer</code> color values to {@link Color} objects.
 * 
 * @author XDEV Software jwill
 * @since 4.0
 */
public class XdevCommaSeparatedColorParser implements ColorParser
{
	private String	converterContext;
	
	
	/**
	 * @return the converterContext
	 */
	public String getConverterContext()
	{
		if(this.converterContext == null)
		{
			//use default context
			return ColorUtils.DEFAULT_CONVERTER_CONTEXT;
		}
		return converterContext;
	}
	
	
	/**
	 * @param converterContext
	 *            the converterContext to set
	 */
	public void setConverterContext(String converterContext)
	{
		this.converterContext = converterContext;
	}
	
	
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
	@Override
	public Color convertFromRGBString(String rgbColorValue)
	{
		return ColorUtils.getColorFromRGBColorString(rgbColorValue,this.getConverterContext());
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
	@Override
	public Color convertFromHexString(String hexColorValue)
	{
		return ColorUtils.getColorFromHexColorString(hexColorValue,this.getConverterContext());
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Color convertFromInteger(Integer integerColorvalue)
	{
		return new Color(integerColorvalue);
	}
	
}
