package xdev.ui.table;

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


import xdev.ui.XdevTreeTable;
import xdev.vt.VirtualTable.VirtualTableRow;

import com.jidesoft.grid.DefaultExpandableRow;
import com.jidesoft.grid.Row;
import com.jidesoft.grid.TreeTableModel;


/**
 * This class adapts XDEV's {@link VirtualTableRow} to Jide's
 * {@link DefaultExpandableRow}.
 * <p>
 * Jide's {@link TreeTableModel} is organized as a {@code List} of type
 * {@link Row}. To make {@link VirtualTableRow} compatible to this approach, it
 * has to be wrapped in an instance of this class.
 * </p>
 * 
 * @author XDEV Software
 * @see XdevTreeTableModel
 * @see XdevTreeTable
 */
public class XdevTreeTableExpandableRow extends DefaultExpandableRow
{
	/**
	 * A wrapped {@link VirtualTableRow}.
	 */
	private VirtualTableRow	virtualTableRow;
	

	/**
	 * Initializes a new {@link XdevTreeTableExpandableRow}.
	 * 
	 * @param virtualTableRow
	 *            the {@link VirtualTableRow} to wrap.
	 */
	public XdevTreeTableExpandableRow(VirtualTableRow virtualTableRow)
	{
		this.virtualTableRow = virtualTableRow;
	}
	

/**
	 * Returns a value of the underlying {@link VirtualTableRow}.
	 * 
	 * @param columnIndex the column index.
	 * @return a value at {@code columnIndex}
	 */
	@Override
	public Object getValueAt(int columnIndex)
	{
		return virtualTableRow.get(columnIndex);
	}
	

	/**
	 * Sets a value in the underlying {@link VirtualTableRow}.
	 * 
	 * @param value
	 *            a value to set at position {@code columnIndex}
	 * @param columnIndex
	 *            the index of the column to set the value for.
	 */
	@Override
	public void setValueAt(Object value, int columnIndex)
	{
		virtualTableRow.set(columnIndex,value);
	}
	

	/**
	 * Returns the wrapped {@link VirtualTableRow}.
	 * 
	 * @return the wrapped {@link VirtualTableRow}
	 */
	public VirtualTableRow getVirtualTableRow()
	{
		return virtualTableRow;
	}
}
