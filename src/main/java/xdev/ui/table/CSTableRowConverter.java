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
import javax.swing.table.TableModel;

import xdev.vt.VirtualTable;
import xdev.vt.VirtualTableModel;
import xdev.vt.VirtualTableWrapper;

import com.jidesoft.grid.JideTable;
import com.jidesoft.grid.TableModelWrapperUtils;


/**
 * Component Suite implementation of {@link TableRowConverter}.
 * <p>
 * For handling of view modified data for example by filtering or sorting.
 * </p>
 * <p>
 * Gets initialized at Component Suite loadtime.
 * <p>
 * Extends {@link TableRowConverter} behavior to the ability of reconverting
 * view updates in updated models. For example the ComponentSuite
 * {@link ExtendedTable}s sorting algorithms modify the model-data on model
 * level.
 * </p>
 * </p>
 * 
 * @author XDEV Software
 * 
 */
public class CSTableRowConverter extends DefaultTableRowConverter
{
	
	/**
	 * Calculates the data representation of a given view-model row. For example
	 * for saving filtered or sorted data independent to its view index.
	 * 
	 * <p>
	 * <b> Use only if the component holds a {@link VirtualTable} as model. </b>
	 * </p>
	 * 
	 * @param table
	 *            the table to calculate model modifications from.
	 * @param row
	 *            the view row to get the model representation from.
	 * 
	 * @return the representation of the given row in the datamodel
	 * 
	 * @see VirtualTableModel
	 */
	@Override
	public int viewToModel(JTable table, int row)
	{
		boolean defiltered = false;
		
		if((table.getModel() instanceof VirtualTableWrapper))
		{
			return super.viewToModel(table,row);
		}
		
		// jtable filters not on model (mvc) -> getActualRowAt would take the
		// wrong index
		if(!(table instanceof JideTable))
		{
			row = super.viewToModel(table,row);
			defiltered = true;
		}
		
		if(row != -1)
		{
			// use Jide functionality to convert from a virtual table model to
			// view index
			
			int subModelRow = TableModelWrapperUtils.getActualRowAt(table.getModel(),row,
					VirtualTableWrapper.class);
			
			if(subModelRow != -1)
			{
				row = subModelRow;
			}
			// without this if, the defiltered value which is not the actual
			// value would be converted again -> wrong value
			else if(!defiltered)
			{
				row = super.viewToModel(table,row);
			}
		}
		
		return row;
	}
	
	
	/**
	 * Calculates the view representation of a given data-model row. For example
	 * for setting dataindex to to a sorted or filtered component.
	 * 
	 * <p>
	 * <b> Use only if the component holds a {@link VirtualTable} as model. </b>
	 * </p>
	 * 
	 * @param table
	 *            the table to calculate model modifications from.
	 * @param row
	 *            the model row to get the view representation from.
	 * 
	 * @return the representation of the given row in the view layer.
	 * 
	 * @see VirtualTableModel
	 */
	@Override
	public int modelToView(JTable table, int row)
	{
		
		if((table.getModel() instanceof VirtualTableWrapper))
		{
			return super.modelToView(table,row);
		}
		
		if(row != -1)
		{
			TableModel outerModel = table.getModel();
			TableModel wrapper = TableModelWrapperUtils.getActualTableModel(outerModel,
					VirtualTableWrapper.class);
			if(wrapper != null && wrapper != outerModel)
			{
				row = TableModelWrapperUtils.getRowAt(outerModel,wrapper,row);
				
				// jide�s jidetable#getRowAt can�t filter view sort
				if(!(table instanceof JideTable))
				{
					row = super.modelToView(table,row);
				}
			}
			else
			{
				// use super method cause this handles first level models and
				// view
				if(!(table instanceof JideTable))
				{
					// filtered?
					if(row <= table.getModel().getRowCount())
					{
						return super.modelToView(table,row);
					}
				}
				else
				{
					return TableModelWrapperUtils.getRowAt(outerModel,row);
				}
			}
		}
		return row;
	}
}
