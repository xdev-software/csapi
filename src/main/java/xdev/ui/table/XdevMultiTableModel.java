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
package xdev.ui.table;


import xdev.util.IntList;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTableModel;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.EditorContext;
import com.jidesoft.grid.MultiTableModel;
import com.jidesoft.grid.TableScrollPane;
import com.jidesoft.grid.TableSplitPane;


/**
 * This class exposes an {@link VirtualTable} as a {@link MultiTableModel}.
 * <p>
 * By using a model of this class, a {@code VirtualTable} can be used within a
 * {@link XdevTableScrollPane} or a {@link TableSplitPane}.
 * </p>
 * 
 * @author XDEV Software
 * 
 */
@SuppressWarnings("unchecked")
public class XdevMultiTableModel extends VirtualTableModel implements MultiTableModel
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	/**
	 * a list of header column indices.
	 */
	private IntList				headerColumnIndices	= new IntList();
	/**
	 * a list of footer column indices.
	 */
	private IntList				footerColumnIndices	= new IntList();
	

	/**
	 * Instantiates a new instance of a {@code XdevMultiTableModel}.
	 * 
	 * @param virtualTable
	 *            the {@link VirtualTable} to be used for this instance.
	 */
	public XdevMultiTableModel(VirtualTable virtualTable)
	{
		this(virtualTable,new int[]{},new int[]{});
	}
	

	/**
	 * Instantiates a new instance of a {@code XdevMultiTableModel}.
	 * 
	 * @param virtualTable
	 *            the {@link VirtualTable} to be used for this instance.
	 * @param columnIndices
	 *            an array of column indices used in this
	 *            {@link XdevMultiTableModel}
	 */
	public XdevMultiTableModel(VirtualTable virtualTable, int[] columnIndices)
	{
		this(virtualTable,columnIndices,new int[]{},new int[]{});
	}
	

	/**
	 * Instantiates a new instance of a {@code XdevMultiTableModel}.
	 * 
	 * @param virtualTable
	 *            the {@link VirtualTable} to be used for this instance.
	 * @param headerColumnsIndices
	 *            an array of header column indices used in this
	 *            {@link XdevMultiTableModel}
	 * @param footerColumnIndices
	 *            an array of footer column indices used in this
	 *            {@link XdevMultiTableModel}
	 */
	public XdevMultiTableModel(VirtualTable virtualTable, int[] headerColumnsIndices,
			int[] footerColumnIndices)
	{
		this(virtualTable,new int[]{},headerColumnsIndices,footerColumnIndices);
	}
	

	/**
	 * Instantiates a new instance of a {@code XdevMultiTableModel}.
	 * 
	 * @param virtualTable
	 *            the {@link VirtualTable} to be used for this instance.
	 * @param columnIndices
	 *            an array of column indices used in this
	 *            {@link XdevMultiTableModel}
	 * @param headerColumnIndices
	 *            an array of header column indices used in this
	 *            {@link XdevMultiTableModel}
	 * @param footerColumnIndices
	 *            an array of footer column indices used in this
	 *            {@link XdevMultiTableModel}
	 */
	public XdevMultiTableModel(VirtualTable virtualTable, int[] columnIndices,
			int[] headerColumnIndices, int[] footerColumnIndices)
	{
		super(virtualTable,columnIndices);
		
		if(headerColumnIndices != null)
		{
			setHeaderColumns(headerColumnIndices);
		}
		
		if(footerColumnIndices != null)
		{
			setFooterColumns(footerColumnIndices);
		}
	}
	

	/**
	 * Sets a new list of header column indices.
	 * 
	 * @param headerColumns
	 *            a number of column indices.
	 */
	public void setHeaderColumns(int... headerColumns)
	{
		this.headerColumnIndices.clear();
		this.headerColumnIndices.addAll(new IntList(headerColumns));
	}
	

	/**
	 * Sets a new list of footer column indices.
	 * 
	 * @param footerColumns
	 *            a number of column indices.
	 */
	public void setFooterColumns(int... footerColumns)
	{
		this.footerColumnIndices.clear();
		this.footerColumnIndices.addAll(new IntList(footerColumns));
	}
	

	/**
	 * Returns the columnType for a specified columnIndex.
	 * <p>
	 * This controls how a particular column is displayed in a
	 * {@link TableScrollPane}. The columnType can be either regular, header or
	 * footer. Header columns are displayed non-scrollable at the left side.
	 * Footer column are displayed non-scrollable at the right side. Regular
	 * columns are display scrollable in the middle.
	 * </p>
	 * This default implementation returns REGULAR_COLUMN always.
	 * 
	 * @param columnIndex
	 *            the columnIndex to get the columnType for.
	 * @return the columnType
	 */
	@Override
	public int getColumnType(int columnIndex)
	{
		int index = getModelColumnIndices()[columnIndex];
		if(headerColumnIndices.contains(index))
		{
			return MultiTableModel.HEADER_COLUMN;
		}
		else if(footerColumnIndices.contains(index))
		{
			return MultiTableModel.FOOTER_COLUMN;
		}
		
		return MultiTableModel.REGULAR_COLUMN;
	}
	

	/**
	 * Returns the index of the split pane to display a specified column in.
	 * Used for {@link TableSplitPane}.
	 * <p>
	 * This controls in which pane of {@link TableSplitPane} a column gets
	 * displayed. The index is the number of the pane counted from left to
	 * right, starting with index 0.
	 * </p>
	 * This default implementation returns 0 for every columnIndex.
	 * 
	 * @param columnIndex
	 *            the columnIndex to return the tableIndex for
	 * @return the table index
	 */
	@Override
	public int getTableIndex(int columnIndex)
	{
		return 0;
	}
	

	/**
	 * @param rowIndex
	 *            the row index
	 * @param columnIndex
	 *            the column index
	 * @return {@code null}
	 */
	@Override
	public ConverterContext getConverterContextAt(int rowIndex, int columnIndex)
	{
		return null;
	}
	

	/**
	 * @param rowIndex
	 *            the row index
	 * @param columnIndex
	 *            the column index
	 * @return {@code null}
	 */
	@Override
	public EditorContext getEditorContextAt(int rowIndex, int columnIndex)
	{
		return null;
	}
	

	/**
	 * @param rowIndex
	 *            the row index
	 * @param columnIndex
	 *            the column index
	 * @return {@code null}
	 */
	@Override
	public Class<?> getCellClassAt(int rowIndex, int columnIndex)
	{
		return getColumnClass(columnIndex);
	}
}
