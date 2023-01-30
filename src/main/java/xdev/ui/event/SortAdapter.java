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
package xdev.ui.event;


import com.jidesoft.grid.SortEvent;
import com.jidesoft.grid.SortListener;
import com.jidesoft.grid.SortableTableModel;


/**
 * Event adapter for the {@link SortListener} interface.
 * 
 * @author XDEV Software
 */
public abstract class SortAdapter implements SortListener
{
	/**
	 * Called whenever the sorting index of {@link SortableTableModel} changed.
	 * 
	 * @param event
	 *            a {@link SortEvent}
	 */
	@Override
	public void sortChanged(SortEvent event)
	{
	}
	

	/**
	 * Called whenever the sorting index of {@link SortableTableModel} is about
	 * to change.
	 * 
	 * @param event
	 *            a {@link SortEvent}
	 */
	@Override
	public void sortChanging(SortEvent event)
	{
	}
}
