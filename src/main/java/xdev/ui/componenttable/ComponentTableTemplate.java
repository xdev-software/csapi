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
package xdev.ui.componenttable;


import java.awt.Dimension;
import java.awt.LayoutManager;


/**
 * 
 * @author XDEV Software (RHHF)
 * @see ComponentTable
 * @since 4.0
 */
public interface ComponentTableTemplate<T>
{
	
	/**
	 * Gets the preferred size of this component.
	 * 
	 * @return a dimension object indicating this component's preferred size
	 * @see LayoutManager
	 */
	public Dimension getPreferredSize();
	
	
	/**
	 * Called as part of the editor lifecycle, when the editor is closed.
	 */
	public void onEditorClose();
	
	
	/**
	 * 
	 * @param o
	 */
	public void setValue(T o);
}
