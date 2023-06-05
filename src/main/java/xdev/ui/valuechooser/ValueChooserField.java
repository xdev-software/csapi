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
package xdev.ui.valuechooser;


import java.awt.Component;


/**
 * A {@link ValueChooserField} is a component that displays a single selected
 * value. To chose a value from a list of possible values a {@link ValueChooser}
 * is displayed
 * 
 * 
 * @since CS 1.0
 * @author XDEV Software (RHHF)
 * 
 * @see ValueChooser
 * 
 */
public interface ValueChooserField
{
	/**
	 * Is called by the chooser after it has closed.
	 */
	public void chooserClosed();
	

	/**
	 * Returns the {@link ValueChooserField} component.
	 * 
	 * @return the {@link ValueChooserField} component.
	 */
	public Component getComponent();
}
