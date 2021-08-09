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


import javax.swing.JTable;

import xdev.vt.VirtualTable;
import xdev.vt.VirtualTableWrapper;

import com.jidesoft.grid.TableModelWrapperUtils;


/**
 * Specifies the contract between a Table which modifies its columns and a
 * {@link VirtualTable} used for TableModel purpose.
 * <p>
 * This interface guarantees that the modified data is handled properly
 * independent of the filtered model
 * </p>
 * 
 * <p>
 * For Example if the Table modifies it�s column count -
 * {@link TableColumnConverter#viewToModel(JTable, int)} can be used to identify
 * non model columns.
 * </p>
 * 
 * @author XDEV Software (JW)
 * @since 3.1
 */
public class CSTableColumnConverter extends DefaultTableColumnConverter
{
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int viewToModel(JTable table, int column)
	{
		column = super.viewToModel(table,column);
		
		if(TableModelWrapperUtils.getActualTableModel(table.getModel(),
				VirtualTableWrapper.class) != null)
		{
			column = TableModelWrapperUtils.getActualColumnAt(table.getModel(),column,
					VirtualTableWrapper.class);
		}
		
		return column;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int modelToView(JTable table, int column)
	{
		column = super.modelToView(table,column);
		
		if(TableModelWrapperUtils.getActualTableModel(table.getModel(),
				VirtualTableWrapper.class) != null)
		{
			column = TableModelWrapperUtils.getColumnAt(table.getModel(),TableModelWrapperUtils
					.getActualTableModel(table.getModel(),VirtualTableWrapper.class),column);
		}
		
		return column;
	}
	
}
