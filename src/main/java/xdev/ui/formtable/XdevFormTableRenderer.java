package xdev.ui.formtable;

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

import xdev.ui.TableSupport;
import xdev.ui.componenttable.ComponentTableRenderer;
import xdev.ui.componenttable.ComponentTableTemplate;
import xdev.vt.VirtualTable.VirtualTableRow;
import xdev.vt.VirtualTableWrapper;


/**
 * 
 * @author XDEV Software (RHHF)
 * @author XDEV Software (FHAE)
 * @since 4.0
 */
public class XdevFormTableRenderer extends ComponentTableRenderer<VirtualTableRow>
{
	/**
	 * @param templateClass
	 */
	public XdevFormTableRenderer(
			Class<? extends ComponentTableTemplate<VirtualTableRow>> templateClass)
	{
		super(templateClass);
	}
	
	
	@Override
	protected VirtualTableRow getRowValues(JTable table, Object value, boolean isSelected,
			int rowIndex, int columnIndex, int actualRowIndex)
	{
		
		VirtualTableWrapper wrap = (VirtualTableWrapper)TableSupport.getTableModelWrapperLookup()
				.lookupTableModel(table.getModel(),VirtualTableWrapper.class);
		if(wrap == null)
		{
			throw new IllegalStateException(
					"Tablemodel / Tablemodelhierarchy does not wrap a VirtualTable.");
		}
		
		try
		{
			return wrap.getVirtualTableRow(actualRowIndex);
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			// row not loaded yet (lazy loading)
			return null;
		}
	}
	
	
	@Override
	protected void decorateTemplate(Component template, JTable table, int row, int column,
			boolean isSelected)
	{
		if(table instanceof XdevFormTable)
		{
			((XdevFormTable)table).decorateTemplate(template,row,column,isSelected);
		}
	}
}
