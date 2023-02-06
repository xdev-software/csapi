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


import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import com.jidesoft.grid.TableModelWrapper;


/**
 * 
 * @author XDEV Software (RHHF)
 * @see ComponentTable
 * @since 4.0
 */

public class ComponentTableModel implements TableModel, TableModelWrapper
{
	private TableModel	model;
	
	
	public ComponentTableModel(TableModel model)
	{
		this.model = model;
	}
	
	
	@Override
	public TableModel getActualModel()
	{
		return model;
	}
	
	
	@Override
	public int getColumnCount()
	{
		return 1;
	}
	
	
	@Override
	public int getRowCount()
	{
		return model.getRowCount();
	}
	
	
	@Override
	public String getColumnName(int columnIndex)
	{
		return model.getColumnName(columnIndex);
	}
	
	
	@Override
	public Class<?> getColumnClass(int columnIndex)
	{
		return model.getColumnClass(columnIndex);
	}
	
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		return true;
	}
	
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		return model.getValueAt(rowIndex,columnIndex);
	}
	
	
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex)
	{
		model.setValueAt(aValue,rowIndex,columnIndex);
	}
	
	
	@Override
	public void addTableModelListener(TableModelListener l)
	{
		model.addTableModelListener(l);
	}
	
	
	@Override
	public void removeTableModelListener(TableModelListener l)
	{
		model.removeTableModelListener(l);
	}
}
