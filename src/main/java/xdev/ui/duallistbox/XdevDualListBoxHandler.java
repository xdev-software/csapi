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


import java.io.Serializable;

import xdev.lang.OperationCanceledException;
import xdev.ui.ItemList.Entry;
import xdev.ui.XdevDualListBox;


/**
 * This interface contains the methods that are used to control the component
 * {@link XdevDualListBox}.
 * 
 * @author XDEV Software
 * 
 */
public interface XdevDualListBoxHandler extends Serializable
{
	/**
	 * Called, when an available entry is moved to selected.
	 * 
	 * @param availableEntry
	 *            an {@link Entry} from the available list to be mapped as
	 *            selected entry
	 * @return an {@link Entry} representing the passed available {@link Entry}
	 *         as selected.
	 * @throws OperationCanceledException
	 *             if user cancels the operation to add an entry
	 */
	public Entry onAddToSelected(Entry availableEntry) throws OperationCanceledException;
	

	/**
	 * Called, when an selected entry is removed and if the
	 * {@code selectionmode} of the containing {@link XdevDualListBox} is
	 * {@code REMOVE_SELECTED}.
	 * <p>
	 * The counterpart to {@link #onAddToSelected(Entry)} method.
	 * </p>
	 * 
	 * @param selectedEntry
	 *            an {@link Entry} from the selected list to be mapped as
	 *            available entry
	 * @return an {@link Entry} representing the passed selected {@link Entry}
	 *         as available.
	 */
	public Entry onRemoveFromSelected(Entry selectedEntry);
}
