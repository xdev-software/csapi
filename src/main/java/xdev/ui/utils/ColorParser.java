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


/**
 * Parses color values, for example RGB, HEX-<code>String</code>s or color
 * <code>Integer</code> color values to {@link Color} objects.
 * 
 * @author XDEV Software jwill
 * @since 4.0
 */
public interface ColorParser
{
	/**
	 * Parses RGB color <code>Strings</code> into their {@link Color}
	 * representation.
	 * 
	 * @param rgbColorValue
	 *            the rgbColorValue
	 * 
	 * @return the {@link Color} representation of the given color
	 *         <code>String</code> or <code>null</code> if the given value is
	 *         not valid.
	 */
	public Color convertFromRGBString(String rgbColorValue);
	
	
	/**
	 * Parses HEX color <code>Strings</code> into their {@link Color}
	 * representation.
	 * 
	 * @param hexColorValue
	 *            the hexColorValue.
	 * 
	 * @return the {@link Color} representation of the given color
	 *         <code>String</code> or <code>null</code> if the given value is
	 *         not valid.
	 */
	public Color convertFromHexString(String hexColorValue);
	
	
	/**
	 * Parses <code>Integer</code> colors into their {@link Color}
	 * representation.
	 * 
	 * @param integerColorvalue
	 *            the integerColorvalue.
	 * 
	 * @return the {@link Color} representation of the given color
	 *         <code>Integer</code> or <code>null</code> if the given value is
	 *         not valid.
	 */
	public Color convertFromInteger(Integer integerColorvalue);
}
