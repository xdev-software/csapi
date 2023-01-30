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
package xdev.ui.formtable;


import java.awt.Component;

import javax.swing.JTable;

import xdev.ui.TableSupport;
import xdev.ui.componenttable.ComponentTableEditor;
import xdev.ui.componenttable.ComponentTableTemplate;
import xdev.vt.VirtualTable.VirtualTableRow;
import xdev.vt.VirtualTableWrapper;


/**
 * 
 * @author XDEV Software (RHHF)
 * @author XDEV Software (FHAE)
 * @see TableForm
 * @since 4.0
 */
public class XdevFormTableEditor extends ComponentTableEditor<VirtualTableRow>
{
	
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 7511883250487882405L;

	
	public XdevFormTableEditor(
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
		
		return wrap.getVirtualTableRow(actualRowIndex);
	}
	
	
	@Override
	protected void createTemplateInstance()
	{
		try
		{
			// Create new instance
			this.setTemplate(this.getTemplateClass().newInstance());
		}
		catch(InstantiationException e)
		{
			throw new IllegalArgumentException("provided templateClass could not be instantiated",e);
		}
		catch(IllegalAccessException e)
		{
			throw new IllegalArgumentException("provided templateClass could not be instantiated",e);
		}
	}
	
	
	
	@Override
	protected void decorateTemplate(Component template, JTable table, int row, int column)
	{
		if(table instanceof XdevFormTable)
		{
			((XdevFormTable)table).decorateTemplate(template,row,column,true);
		}
	}
}
