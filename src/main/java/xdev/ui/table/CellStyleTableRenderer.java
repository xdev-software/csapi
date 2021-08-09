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


import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;


/**
 * This implementation of {@link TableCellRenderer} is meant to provide visual
 * cell styling of individual table cells based on the value and/or properties
 * of a field from a Virtual Table instance.
 * <p>
 * This class decorates other TableCellRenderers and modifies the cell component
 * after those other renderer's, building a renderer chain.
 * </p>
 * 
 * @author XDEV Software
 * 
 */
public class CellStyleTableRenderer implements TableCellRenderer
{
	/**
	 * The previous renderer responsible for doing the initial rendering.
	 */
	private TableCellRenderer	previousRenderer;
	

	/**
	 * Creates a new instance.
	 * 
	 * @param previousRenderer
	 *            the previous TableCellRenderer.
	 */
	public CellStyleTableRenderer(TableCellRenderer previousRenderer)
	{
		this.previousRenderer = previousRenderer;
	}
	

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
			boolean hasFocus, int row, int column)
	{
		Component component = previousRenderer.getTableCellRendererComponent(table,value,
				isSelected,hasFocus,row,column);
		
		/*
		 * TODO: Currently this renderer doesn't modify the appearance of the
		 * cell. Implementation of vt cell specific modifications should be done
		 * here...
		 */
		// VirtualTableModel vtm =
		// (VirtualTableModel)TableModelWrapperUtils.getActualTableModel(table.getModel(),
		// VirtualTableModel.class);
		
		return component;
	}
	

	/**
	 * @return the previous {@link TableCellRenderer}.
	 */
	public TableCellRenderer getPreviousRenderer()
	{
		return previousRenderer;
	}
	
}
