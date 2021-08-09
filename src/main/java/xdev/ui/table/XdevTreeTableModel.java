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


import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import xdev.vt.VirtualTable;
import xdev.vt.VirtualTable.VirtualTableRow;
import xdev.vt.VirtualTableWrapper;

import com.jidesoft.grid.Row;
import com.jidesoft.grid.TreeTableModel;


/**
 * This is a custom implementation of Jide's TreeTableModel.
 * <p>
 * It provides an adaption of the two dimensional structure of XDEV's
 * {@link VirtualTable} to the hierarchical row structure of a {@code TreeTable}.
 * <p>
 * The adaption is achieved by treating the {@code virtualTable} as a
 * {@code List} of rows. One column has to be specified as a unique identifier
 * for these rows. Another column is specified to relate child rows with their
 * parent rows.
 * <p>
 * Example:
 * <table border='1'>
 * <tr>
 * <th>id</th>
 * <th>parent_id</th>
 * <th>value</th>
 * </tr>
 * <tr>
 * <td>1</td>
 * <td>null</td>
 * <td>rootRow</td>
 * </tr>
 * <tr>
 * <td>2</td>
 * <td>1</td>
 * <td>childRow</td>
 * </tr>
 * </table>
 * </p>
 * The example above would be interpreted as a TreeTable with one root row
 * (parent_id null) and one child of it (parent_id of child equals id of root).
 * </p>
 * <p>
 * <b>Note:</b> The current implementation expects the VirtualTable to be
 * ordered in a way, that parent rows appear before child rows.
 * </p>
 * </p>
 * 
 * @author XDEV Software
 * 
 * @param <T>
 *            the type of rows contained within the Model.
 * @see TreeTableModel
 */
public class XdevTreeTableModel<T extends Row> extends TreeTableModel<T> implements
		VirtualTableWrapper
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	/**
	 * The underlying Virtual Table.
	 */
	private VirtualTable		virtualTable;
	
	/**
	 * The column indices of the Virtual Table to be used in the model.
	 */
	private int[]				columnIndices;
	
	
	/**
	 * Initializes a new instance of {@link XdevTreeTableModel}.
	 * <p>
	 * This constructor uses all visible columns of the virtual table within the
	 * model.
	 * </p>
	 * 
	 * @param virtualTable
	 *            the {@link VirtualTable} to adapt.
	 * @param columnIndexId
	 *            the index of the column used as a unique identifier.
	 * @param columnIndexParent
	 *            the index of the column used to relate child entities to their
	 *            parents.
	 */
	// super class uses untyped list
	public XdevTreeTableModel(VirtualTable virtualTable, int columnIndexId, int columnIndexParent)
	{
		this(virtualTable,columnIndexId,columnIndexParent,virtualTable.getVisibleColumnIndices());
	}
	
	
	/**
	 * Initializes a new instance of {@link XdevTreeTableModel}.
	 * 
	 * @param virtualTable
	 *            the {@link VirtualTable} to adapt.
	 * @param columnIndexId
	 *            the index of the column used as a unique identifier.
	 * @param columnIndexParent
	 *            the index of the column used to relate child entities to their
	 *            parents.
	 * @param visibleColumnIndices
	 *            the column indices of the virtual table to be used in the
	 *            visual model
	 */
	// super class uses untyped list
	public XdevTreeTableModel(VirtualTable virtualTable, int columnIndexId, int columnIndexParent,
			int[] visibleColumnIndices)
	{
		this(virtualTable,columnIndexId,columnIndexParent,visibleColumnIndices,"");
	}
	
	
	/**
	 * Initializes a new instance of {@link XdevTreeTableModel}.
	 * 
	 * @param virtualTable
	 *            the {@link VirtualTable} to adapt.
	 * @param columnIndexId
	 *            the index of the column used as a unique identifier.
	 * @param columnIndexParent
	 *            the index of the column used to relate child entities to their
	 *            parents.
	 * @param visibleColumnIndices
	 *            the column indices of the virtual table to be used in the
	 *            visual model
	 * @param rootIdentifier
	 *            an Object representing the parent id for determining root
	 *            level columns (defaults to empty String)
	 */
	@SuppressWarnings("unchecked")
	// super class uses untyped list
	public XdevTreeTableModel(VirtualTable virtualTable, int columnIndexId, int columnIndexParent,
			int[] visibleColumnIndices, Object rootIdentifier)
	{
		super(wrapVirtualTableRows(virtualTable,columnIndexId,columnIndexParent,rootIdentifier));
		this.virtualTable = virtualTable;
		this.columnIndices = visibleColumnIndices;
	}
	
	
/**
	 * Converts the contents of the virtual table to be useable as input for a
	 * TreeTable.
	 * 
	 * @param virtualTable
	 *            the virtual table to be represented as a tree.
	 * @param columnIndexId
	 *            the column to be used as an id for building the tree.
	 * @param columnIndexParent
	 *            the column to be used as an parent_id for building the tree.
	 * @param rootIdentifier
	 *            an Object representing the parent id for determining root
	 *            level columns (defaults to empty String)
	 * @return a List of {@link XdevTreeTableExpandableRow) instances
	 *         representing the virtual table a a tree.
	 * 
	 * @see XdevTreeTableExpandableRow XdevTreeTableExpandableRows are used as data container.
	 */
	// super class uses untyped list
	@SuppressWarnings("rawtypes")
	private static List wrapVirtualTableRows(VirtualTable virtualTable, int columnIndexId,
			int columnIndexParent, Object rootIdentifier)
	{
		Map<Object, XdevTreeTableExpandableRow> rootIdMap = fillUpRowMap(virtualTable,
				columnIndexId,columnIndexParent,rootIdentifier);
		
		Iterator<Entry<Object, XdevTreeTableExpandableRow>> it = rootIdMap.entrySet().iterator();
		
		while(it.hasNext())
		{
			XdevTreeTableExpandableRow currentRow = it.next().getValue();
			
			Object parentId = currentRow.getVirtualTableRow().get(columnIndexParent);
			
			// / child
			if(isChild(parentId,rootIdentifier))
			{
				XdevTreeTableExpandableRow rootRow = rootIdMap.get(parentId);
				if(rootRow != null)
				{
					rootRow.addChild(currentRow);
				}
				else
				{
					throw new IllegalStateException("no parent row found with id = " + parentId);
				}
			}
		}
		
		return getRootRows(rootIdMap,columnIndexParent,rootIdentifier);
	}
	
	
	/**
	 * Returns a {@link List} of Root - {@link XdevTreeTableExpandableRow}s.
	 * 
	 * @param rootIdMap
	 *            the map returned from
	 *            {@link XdevTreeTableModel#fillUpRowMap(VirtualTable, int, int, Object)}
	 *            .
	 * @param columnIndexParent
	 *            the parent id index.
	 * @param rootIdentifier
	 *            the value for identifying the root rows e.g. <code>null</code>
	 *            .
	 * @return return a {@link List} of Root -
	 *         {@link XdevTreeTableExpandableRow}s.
	 * 
	 * @see XdevTreeTableModel#fillUpRowMap(VirtualTable, int, int, Object).
	 */
	private static List<XdevTreeTableExpandableRow> getRootRows(
			Map<Object, XdevTreeTableExpandableRow> rootIdMap, int columnIndexParent,
			Object rootIdentifier)
	{
		List<XdevTreeTableExpandableRow> rootRows = new ArrayList<XdevTreeTableExpandableRow>();
		
		Iterator<Entry<Object, XdevTreeTableExpandableRow>> it = rootIdMap.entrySet().iterator();
		
		while(it.hasNext())
		{
			XdevTreeTableExpandableRow actualRow = it.next().getValue();
			
			// parent
			if(isParent(actualRow.getVirtualTableRow().get(columnIndexParent),rootIdentifier))
			{
				rootRows.add(actualRow);
			}
			
			// avoids a possible ConcurrentModificationException
			it.remove();
		}
		return rootRows;
	}
	
	
	// consider null as a must?
	/**
	 * Returns true if the row with the corresponding id is a child.
	 * 
	 * @param parentId
	 *            the id which is going to be checked.
	 * @param rootIdentifier
	 *            the identifier which is used to validate.
	 */
	private static boolean isChild(Object parentId, Object rootIdentifier)
	{
		return parentId != null && parentId.equals(rootIdentifier) == false;
	}
	
	
	/**
	 * Returns true if the row with the corresponding id is a parent.
	 * 
	 * @param parentId
	 *            the id which is going to be checked.
	 * @param rootIdentifier
	 *            the identifier which is used to validate.
	 */
	private static boolean isParent(Object parentId, Object rootIdentifier)
	{
		return parentId == null || parentId.equals(rootIdentifier);
	}
	
	
	/**
	 * Fills up a {@link Map} which contains all potential root or child rows.
	 * 
	 * @param virtualTable
	 *            the {@link VirtualTable} to fill up the {@link Map} from.
	 * @param columnIndexId
	 *            the ids index.
	 * @param columnIndexParent
	 *            the parent id index.
	 * @param rootIdentifier
	 *            the value for identifying the root rows e.g. <code>null</code>
	 *            .
	 * 
	 * @return a {@link Map} containing {@link XdevTreeTableExpandableRow}s from
	 *         the given {@link VirtualTable}.
	 */
	private static Map<Object, XdevTreeTableExpandableRow> fillUpRowMap(VirtualTable virtualTable,
			int columnIndexId, int columnIndexParent, Object rootIdentifier)
	{
		Map<Object, XdevTreeTableExpandableRow> rootIdMap = new LinkedHashMap<Object, XdevTreeTableExpandableRow>();
		
		for(int i = 0; i < virtualTable.getRowCount(); i++)
		{
			VirtualTableRow vtr = virtualTable.getRow(i);
			Object id = vtr.get(columnIndexId);
			XdevTreeTableExpandableRow currentRow = new XdevTreeTableExpandableRow(vtr);
			
			if(rootIdMap.containsKey(id))
			{
				throw new IllegalStateException("multiple occurences for value " + id
						+ ", that has to be unique.");
			}
			rootIdMap.put(id,currentRow);
		}
		
		return rootIdMap;
	}
	
	
	/**
	 * Returns the number of columns for the wrapped TableModel.
	 * 
	 * @return the number of columns
	 */
	@Override
	public int getColumnCount()
	{
		return columnIndices.length;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public VirtualTable getVirtualTable()
	{
		return virtualTable;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int viewToModelColumn(int col)
	{
		return columnIndices[col];
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int[] getModelColumnIndices()
	{
		return columnIndices;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public VirtualTableRow getVirtualTableRow(int modelRow)
	{
		Row row = getRowAt(modelRow);
		if(row instanceof XdevTreeTableExpandableRow)
		{
			return ((XdevTreeTableExpandableRow)row).getVirtualTableRow();
		}
		
		return virtualTable.getRow(modelRow);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getColumnName(int column)
	{
		return virtualTable.getColumnCaption(columnIndices[column]);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		return super.getValueAt(rowIndex,columnIndices[columnIndex]);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex)
	{
		super.setValueAt(aValue,rowIndex,columnIndices[columnIndex]);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		return super.isCellEditable(rowIndex,columnIndices[columnIndex]);
	}
}
