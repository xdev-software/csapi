package xdev.ui.ganttchart.model;

/*-
 * #%L
 * XDEV BI Suite
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


import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import xdev.vt.VirtualTable;

import com.jidesoft.gantt.DefaultGanttEntry;
import com.jidesoft.gantt.GanttChartPane;
import com.jidesoft.grid.CellStyle;
import com.jidesoft.grid.ExpandableRow;
import com.jidesoft.grid.Row;
import com.jidesoft.grid.StyleModel;
import com.jidesoft.grid.TreeTable;
import com.jidesoft.grid.TreeTableModel;


/**
 * A Specific {@link TreeTableModel} to display custom data correctly in the
 * {@link GanttChartPane}s {@link TreeTable}.
 * 
 * @author XDEV Software jwill
 * @since 4.0
 * 
 * @param <T>
 *            data type, either date or number format.
 */
public class XdevVirtualTableGanttTreeTableModel<T extends Comparable<T>> extends
		TreeTableModel<DefaultGanttEntry<T>> implements StyleModel, PropertyChangeListener
{
	
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -9190597511684868419L;
	
	/**
	 * the {@link VirtualTable} which provides the data for this model.
	 */
	private VirtualTable		vt;
	
	/**
	 * the flag which indicates the cell style usage.
	 */
	private boolean				cellStyleState		= false;
	
	private boolean				cellEditableState	= false;
	
	/**
	 * the currently used {@link CellStyle} which provides customized styles for
	 * table cells.
	 */
	private CellStyle			cellStyle			= new CellStyle();
	
	
	/**
	 * Customizes {@link TreeTableModel} from the given {@link VirtualTable}.
	 * 
	 * @param vt
	 *            - the {@link VirtualTable} which contains the data.
	 */
	public XdevVirtualTableGanttTreeTableModel(VirtualTable vt)
	{
		this(vt,false,null);
	}
	
	
	/**
	 * Customizes {@link TreeTableModel} from the given {@link VirtualTable}.
	 * 
	 * @param vt
	 *            - the {@link VirtualTable} which contains the data.
	 * @param cellStyle
	 *            - enable customized cell style
	 * @param font
	 *            - custom {@link Font} for e.g. header rows.
	 */
	public XdevVirtualTableGanttTreeTableModel(VirtualTable vt, boolean cellStyle, Font font)
	{
		this.vt = vt;
		this.cellStyleState = cellStyle;
		
		// replace with default look and feel font ?
		if(font != null)
		{
			this.cellStyle.setFont(font);
		}
	}
	
	
	@Override
	public int getColumnCount()
	{
		return vt.getVisibleColumnIndices().length;
	}
	
	
	@Override
	public Class<?> getCellClassAt(int rowIndex, int columnIndex)
	{
		return getColumnClass(columnIndex);
	}
	
	
	@Override
	public Class<?> getColumnClass(int columnIndex)
	{
		return vt.getColumnAt(vt.getVisibleColumnIndices()[columnIndex]).getType().getJavaClass();
	}
	
	
	@Override
	public String getColumnName(int column)
	{
		if(vt.getColumnCaption(vt.getVisibleColumnIndices()[column]) != null)
		{
			return vt.getColumnCaption(vt.getVisibleColumnIndices()[column]);
		}
		else
		{
			return vt.getColumnName(vt.getVisibleColumnIndices()[column]);
		}
		
	}
	
	
	@Override
	public CellStyle getCellStyleAt(int rowIndex, int columnIndex)
	{
		// logic - user determined
		Row row = getRowAt(rowIndex);
		if((row instanceof ExpandableRow && ((ExpandableRow)row).hasChildren()))
		{
			return cellStyle;
		}
		return null;
	}
	
	
	@Override
	public boolean isCellStyleOn()
	{
		return cellStyleState;
	}
	
	
	@Override
	public boolean isCellEditable(int row, int column)
	{
		return this.cellEditableState;
	}
	
	
	@Override
	public void propertyChange(PropertyChangeEvent evt)
	{
		// must assume that the getValue is boolean typed,
		// because prop change
		// listener supports no generics
		this.cellEditableState = ((Boolean)evt.getNewValue());
	}
	
}
