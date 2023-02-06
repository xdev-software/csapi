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


import javax.swing.JTable;

import xdev.ui.XdevValueChooserTextField;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTable.VirtualTableRow;


/**
 * A {@link XdevValueChooser} is a special {@link ValueChooser} optimized for
 * XDEV IDE features and usage with a {@link JTable}.
 * 
 * 
 * 
 * @param <T>
 *            The value's type
 * 
 * @since CS 1.0
 * @author XDEV Software (RHHF)
 * 
 * @see XdevValueChooserTextField
 * 
 */
public interface XdevValueChooser<T> extends ValueChooser<T>
{
	/**
	 * Sets the visibility of the {@link ValueChooser}.
	 * 
	 * @param visible
	 *            if <code>true</code>, makes the {@link ValueChooser} visible,
	 *            otherwise hides the {@link ValueChooser}.
	 */
	public void setVisible(boolean visible);
	
	
	/**
	 * Returns the {@link JTable} of the {@link XdevValueChooser}.
	 * 
	 * @return the {@link JTable} of the {@link XdevValueChooser}.
	 */
	public JTable getTable();
	
	
	/**
	 * Sets the size of value chooser.
	 * 
	 * @param width
	 *            the width in pixel
	 * @param height
	 *            the height in pixel
	 */
	public void setChooserSize(int width, int height);
	
	
	/**
	 * Sets the chosen {@link VirtualTableRow} to the specified value.
	 * 
	 * @param selectedValue
	 *            a {@link VirtualTableRow} to select
	 */
	public void setSelectValue(VirtualTableRow selectedValue);
	
	
	/**
	 * Sets the VirtualTable for this value chooser dialog.
	 * <p>
	 * This method is meant to be called by XDEVs code generation only. It is
	 * recommended to use {@link PopupVirtualTableValueChooser}
	 * for creating this class and initializing it with a virtual table.
	 * </p>
	 * 
	 * @param virtualTable
	 *            {@link VirtualTable} containing the valid values for this
	 *            instance.
	 */
	public void setVirtualTable(VirtualTable virtualTable);
	
}
