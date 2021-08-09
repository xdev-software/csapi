package xdev.ui.duallistbox;

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


import xdev.lang.OperationCanceledException;
import xdev.ui.ItemList.Entry;
import xdev.ui.XdevDualListBox;


/**
 * A default implementation for {@link XdevDualListBoxHandler}.
 * <p>
 * Can be overwritten and set in {@link XdevDualListBox} to customize the
 * behavior of it.
 * </p>
 * 
 * @author XDEV Software
 * 
 */
public class XdevDualListBoxDefaultHandler implements XdevDualListBoxHandler
{
	/**
	 * Default Constructor.
	 */
	public XdevDualListBoxDefaultHandler()
	{
	}
	
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	

	/**
	 * Default implementation for mapping available entries to selected entries.
	 * 
	 * @param availableEntry
	 *            an entry being selected
	 * @return the reference to the availableEntry
	 * @throws OperationCanceledException
	 *             if user cancels the operation to add an entry
	 */
	@Override
	public Entry onAddToSelected(Entry availableEntry) throws OperationCanceledException
	{
		Entry selectedEntry = availableEntry.clone();
		selectedEntry.setEnabled(true);
		return selectedEntry;
	}
	

	/**
	 * Default implementation for mapping available entries to selected entries.
	 * 
	 * @param selectedEntry
	 *            the entry to be removed
	 * @return the reference to the selectedEntry
	 */
	@Override
	public Entry onRemoveFromSelected(Entry selectedEntry)
	{
		return selectedEntry.clone();
	}
}
