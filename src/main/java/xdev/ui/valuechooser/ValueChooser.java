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


import xdev.lang.NotNull;
import xdev.lang.Nullable;


/**
 * A {@link ValueChooser} is a component that enables a user to select a single
 * value from a list of possible values.
 * 
 * <p>
 * A {@link ValueChooser} is typically, but not necessarily, implemented as
 * popup or dialog.
 * </p>
 * 
 * <p>
 * {@link ValueChooser} is always complemented with a ValueChooserField for
 * displaying the selected values.
 * </p>
 * 
 * @param <T>
 *            The value's type
 * 
 * @since CS 1.0
 * @author XDEV Software (RHHF)
 * 
 * @see ValueChooserField
 * 
 */
public interface ValueChooser<T>
{
	
	/**
	 * Makes the {@link ValueChooser} visible.
	 * 
	 * @param owner
	 *            {@link ValueChooserField} that called the method
	 */
	public void show(final @NotNull ValueChooserField owner);
	
	
	/**
	 * Returns the chosen value.
	 * 
	 * @return the chosen value.
	 */
	@Nullable
	public T getSelectValue();
	
	
	/**
	 * returns either true if the {@link ValueChooser} was successfully
	 * initialized or false if not.
	 * 
	 * For example returns true after the table model has been set.
	 * 
	 * @return the initialization state.
	 */
	public boolean getInitState();
	
	
	/**
	 * Resets the {@link ValueChooser}.
	 */
	public void reset();
	
}
