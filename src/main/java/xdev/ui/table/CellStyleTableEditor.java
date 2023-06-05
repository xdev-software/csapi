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


import java.awt.Component;
import java.util.EventObject;

import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;


/**
 * This implementation of {@link TableCellEditor} is meant to provide visual
 * cell styling of individual table cells based on the value and/or properties
 * of a field from a Virtual Table instance.
 * <p>
 * This class delegates all calls to its configured previous editor instance.
 * The method
 * {@link #getTableCellEditorComponent(JTable, Object, boolean, int, int)} is
 * used to modify the appearance of the editor cell component.
 * </p>
 * 
 * @author XDEV Software
 * 
 */
public class CellStyleTableEditor implements TableCellEditor
{
	/**
	 * The previous renderer responsible for doing the initial rendering.
	 */
	private TableCellEditor	previousEditor;
	

	/**
	 * Creates a new instance.
	 * 
	 * @param previousEditor
	 *            the previous TableCellEditor.
	 */
	public CellStyleTableEditor(TableCellEditor previousEditor)
	{
		this.previousEditor = previousEditor;
	}
	

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
			int row, int column)
	{
		Component component = previousEditor.getTableCellEditorComponent(table,value,isSelected,
				row,column);
		
		/*
		 * TODO: Currently this editor doesn't modify the appearance of the
		 * cell. Implementation of vt cell specific modifications should be done
		 * here...
		 */

		return component;
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getCellEditorValue()
	{
		return previousEditor.getCellEditorValue();
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isCellEditable(EventObject anEvent)
	{
		return previousEditor.isCellEditable(anEvent);
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean shouldSelectCell(EventObject anEvent)
	{
		return previousEditor.shouldSelectCell(anEvent);
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean stopCellEditing()
	{
		return previousEditor.stopCellEditing();
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void cancelCellEditing()
	{
		previousEditor.cancelCellEditing();
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addCellEditorListener(CellEditorListener l)
	{
		previousEditor.addCellEditorListener(l);
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeCellEditorListener(CellEditorListener l)
	{
		previousEditor.removeCellEditorListener(l);
	}
	
}
