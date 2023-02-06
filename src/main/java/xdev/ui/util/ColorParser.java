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


/**
 * ColorParser is able to convert a {@link Color} in its data object value.
 * <p>
 * For example {@link Color#red} would be 255,0,0.
 * </p>
 * 
 * @author XDEV Software jwill
 * 
 * @param <CT>
 *            the data object value type, for example {@link String} or
 *            {@link Number}.
 * @since 4.0
 */
public interface ColorParser<CT>
{
	/**
	 * Returns the {@link Color} representation of the given value.
	 * 
	 * @param colorValue
	 *            the color value to convert.
	 * @return the converted {@link Color}.
	 */
	Color getColorFromValue(CT colorValue);
	
	
	/**
	 * Returns the color value representation of the given {@link Color}.
	 * 
	 * @param color
	 *            the {@link Color} to convert.
	 * @return the converted color value.
	 */
	CT getValueFromColor(Color color);
	
	
	/**
	 * Determines whether alpha should be considered in computation.
	 * 
	 * @return the alpha computation state.
	 */
	boolean isAlphaIncluded();
	
	
	/**
	 * Determines whether alpha should be considered in computation.
	 * 
	 * @param alphaIncluded
	 *            the alpha computation state.
	 */
	void setAlphaIncluded(boolean alphaIncluded);
}
