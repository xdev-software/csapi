package xdev.ui.componenttable;

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

import xdev.lang.NotNull;

import com.jidesoft.grid.TableModelWrapperUtils;


/**
 * 
 * @author XDEV Software (RHHF)
 * @author XDEV Software (FHAE)
 * @see ComponentTable
 * @since 4.0
 */
public abstract class ComponentTableRenderer<T> implements TableCellRenderer
{
	
	@SuppressWarnings("unused")
	// to fit the setter / getter pattern
	private final Class<? extends ComponentTableTemplate<T>>	templateClass;
	
	/**
	 * Holds the instance used for rendering
	 */
	private final ComponentTableTemplate<T>						template;
	
	
	/**
	 * Creates a new TableFormRenderer based on the provided template.
	 * 
	 * @param templateClass
	 *            {@link ComponentTableTemplate} that will be used for
	 *            rendering.
	 */
	public ComponentTableRenderer(
			@NotNull final Class<? extends ComponentTableTemplate<T>> templateClass)
	{
		if(templateClass == null)
		{
			throw new IllegalArgumentException("templateClass must not be null");
		}
		else if(templateClass.getClass().isAssignableFrom(Component.class))
		{
			throw new IllegalArgumentException(
					"templateClass must be a subclass of java.awt.component");
		}
		
		try
		{
			this.template = templateClass.newInstance();
			
			/*
			 * Should only be assigned if an instance of
			 * the class could be created.
			 */
			this.templateClass = templateClass;
			
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
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Component getTableCellRendererComponent(final JTable table, Object value,
			final boolean isSelected, boolean hasFocus, int row, int column)
	{
		int actualRowIndex = TableModelWrapperUtils.getActualRowAt(table.getModel(),row);
		
		this.template.setValue(this.getRowValues(table,value,isSelected,row,column,actualRowIndex));
		
		Component component = (Component)this.template;
		
		decorateTemplate(component,table,row,column,isSelected);
		
		return component;
	}
	
	
	protected void decorateTemplate(Component template, JTable table, int row, int column,
			boolean isSelected)
	{
		if(table instanceof ComponentTable)
		{
			((ComponentTable)table).decorateTemplate(template,row,column,isSelected);
		}
	}
	
	
	protected abstract T getRowValues(JTable table, Object value, boolean isSelected, int rowIndex,
			int columnIndex, int actualRowIndex);
	
	
	public Component getTemplateInstance()
	{
		return (Component)this.template;
	}
}
