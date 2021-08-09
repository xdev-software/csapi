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


import javax.swing.table.TableModel;

import com.jidesoft.grid.CellStyle;
import com.jidesoft.grid.CellStyleProvider;
import com.jidesoft.grid.SortableTreeTableModel;


/**
 * This class wraps {@link TableModel} instances to extend the functionality of
 * the original table models.
 * <p>
 * All table types of the Component Suite wrap their original table models into
 * a wrapper of this type.
 * </P>
 * 
 * @author XDEV Software
 * 
 */
// TODO: ge�ndert von DefaultTableModelWrapper auf SortableTreeTableModel wegen
// collapsable icons (checken, ob ok)
@SuppressWarnings("rawtypes")
public class XdevTableModelWrapper extends SortableTreeTableModel
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	/**
	 * Flag indicating if cell style is on.
	 */
	private boolean				isCellStyleOn		= false;
	
	/**
	 * A cell style provider for this {@link XdevTableModelWrapper}.
	 */
	private CellStyleProvider	cellStyleProvider	= null;
	

	/**
	 * Creates a new instance of {@link XdevTableModelWrapper}.
	 * 
	 * @param tableModel
	 *            the {@link TableModel} to be wrapped.
	 */
	public XdevTableModelWrapper(TableModel tableModel)
	{
		super(tableModel);
	}
	

	@Override
	public boolean isCellStyleOn()
	{
		return isCellStyleOn;
	}
	

	/**
	 * En- or disabled the use of cell style.
	 * 
	 * @param isCellStyleOn
	 *            if {@code true} cell style gets enabled.
	 */
	public void setCellStyleOn(boolean isCellStyleOn)
	{
		this.isCellStyleOn = isCellStyleOn;
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CellStyle getCellStyleAt(int row, int column)
	{
		if(isCellStyleOn && cellStyleProvider != null)
		{
			return cellStyleProvider.getCellStyleAt(this,row,column);
		}
		return null;
	}
	

	@Override
	public String getColumnName(int column)
	{
		String columnName = null;
		try
		{
			columnName = super.getColumnName(column);
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			// XdevAggregateTable in combination with autofilter throws a
			// strange exception (#12105)
			// Ignoring this exception here as a workaround seems to fix the
			// problem
		}
		return columnName;
	}
	

	@Override
	public Object getColumnIdentifier(int column)
	{
		Object columnIdentifier = null;
		try
		{
			columnIdentifier = super.getColumnIdentifier(column);
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			// XdevAggregateTable in combination with autofilter throws a
			// strange exception (#12105)
			// Ignoring this exception here as a workaround seems to fix the
			// problem
		}
		return columnIdentifier;
	}
	
}
