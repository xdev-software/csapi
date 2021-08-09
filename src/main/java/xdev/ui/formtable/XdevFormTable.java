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
import java.awt.Container;
import java.beans.Beans;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.UIManager;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import xdev.lang.OperationCanceledException;
import xdev.ui.BeanProperty;
import xdev.ui.BeanSettings;
import xdev.ui.ComponentTreeVisitor;
import xdev.ui.DefaultBeanCategories;
import xdev.ui.UIUtils;
import xdev.ui.XdevComponent;
import xdev.ui.XdevSortableTable;
import xdev.ui.XdevStyle;
import xdev.ui.componenttable.ComponentTableModel;
import xdev.ui.componenttable.ComponentTableTemplate;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTable.VirtualTableRow;


/**
 * {@link XdevFormTable} is UI Component that displays rows of
 * {@link VirtualTable} using {@link XdevFormTableTemplate}. The content of the
 * {@link VirtualTable} is not only displayed, but also editable by a
 * {@link XdevFormTableTemplate}. From a technical point of view is the
 * {@link XdevFormTable} a one-component renderer / editor for one
 * {@link VirtualTableRow}.
 * 
 * 
 * @author XDEV Software (RHHF)
 * @see XdevFormTable
 * @since 4.0
 */

@BeanSettings(acceptChildren = false, useXdevCustomizer = true)
public class XdevFormTable extends XdevSortableTable
{
	
	/**
	 * 
	 */
	private static final long											serialVersionUID	= -4026493599327339224L;
	
	private Class<? extends ComponentTableTemplate<VirtualTableRow>>	templateClass;
	
	private XdevFormTableRenderer										renderer;
	
	private XdevFormTableEditor											editor;
	
	
	/**
	 * Create a new {@link XdevFormTable} with no table header. The
	 * {@link XdevFormTable} is editable by default. The persistence option is
	 * turned off.
	 * 
	 */
	public XdevFormTable()
	{
		// no header needed
		setTableHeader(null);
		
		// editable by default
		setEditable(true);
		
		// persistence by default
		setPersistenceEnabled(false);
	}
	
	
	/**
	 * Returns the current {@link ComponentTableTemplate} that can handle a
	 * {@link VirtualTableRow}.
	 * 
	 * @return the current {@link XdevFormTableTemplate}
	 */
	public Class<? extends ComponentTableTemplate<VirtualTableRow>> getTemplateClass()
	{
		return templateClass;
	}
	
	
	/**
	 * Set the new {@link ComponentTableTemplate} that can handle a
	 * {@link VirtualTableRow}. If the {@code templateClass} is
	 * <code>null</code> or is not assignable from a {@link Component} a
	 * {@link IllegalArgumentException} is thrown.
	 * 
	 * @param templateClass
	 *            the new ComponentTableTemplate
	 * 
	 * @see XdevFormTableTemplate
	 */
	@BeanProperty(category = DefaultBeanCategories.OBJECT)
	public void setTemplateClass(
			final Class<? extends ComponentTableTemplate<VirtualTableRow>> templateClass)
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
		
		this.templateClass = templateClass;
	}
	
	
	/**
	 * Sets a {@link TableModel} for this table.
	 * <p>
	 * The passed {@code model} gets wrapped into a new
	 * {@link ComponentTableModel}, if necessary.
	 * </p>
	 * 
	 * Set a new {@link XdevFormTableRenderer} and {@link XdevFormTableEditor}
	 * for this {@link XdevFormTable}.
	 * 
	 * @param model
	 *            the {@link TableModel} to set.
	 * 
	 * @see XdevFormTableEditor
	 * @see XdevFormTableRenderer
	 * @see XdevFormTableTemplate
	 */
	@Override
	public void setModel(final TableModel model)
	{
		if(Beans.isDesignTime() || model == null || this.templateClass == null)
		{
			super.setModel(model);
		}
		else
		{
			renderer = new XdevFormTableRenderer(templateClass);
			
			editor = new XdevFormTableEditor(templateClass);
			
			super.setModel(new ComponentTableModel(model));
			
			putClientProperty("terminateEditOnFocusLost",true);
			
			// Ensure jide uses the renderer
			setDefaultCellRenderer(renderer);
			setDefaultEditor(editor);
			
			final TableColumn col = getColumnModel().getColumn(0);
			col.setCellEditor(editor);
			col.setCellRenderer(renderer);
			
			final Component templateInstance = renderer.getTemplateInstance();
			int height = templateInstance.getPreferredSize().height;
			if(height <= 0)
			{
				height = templateInstance.getHeight();
			}
			if(height > 0)
			{
				this.setRowHeight(height);
			}
		}
	}
	
	
	/**
	 * The property`s background, foreground be transferred from the table to
	 * the template
	 * 
	 * @param template
	 *            the {@link Component} to decorate
	 * @param row
	 *            the row index of the cell being drawn.
	 * @param column
	 *            the column index of the cell being drawn
	 * @param isSelected
	 *            <code>true</code> if the cell is to be rendered with the
	 *            selection highlighted; otherwise <code>false</code>
	 * 
	 */
	public void decorateTemplate(final Component template, final int row, final int column,
			final boolean isSelected)
	{
		if(template instanceof XdevComponent
				&& ((XdevComponent)template).getBackgroundType() != XdevStyle.COLOR)
		{
			return;
		}
		
		template.setBackground(isSelected ? getSelectionBackground()
				: (row % 2 == 0 ? getEvenBackground() : getOddBackground()));
		
		if(template instanceof Container)
		{
			UIUtils.lookupComponentTree((Container)template,
					new ComponentTreeVisitor<Object, Component>()
					{
						@Override
						public Object visit(final Component c) throws OperationCanceledException
						{
							decorateComponent(c,row,column,isSelected);
							
							return null;
						}
					});
		}
	}
	
	
	protected void decorateComponent(final Component c, final int row, final int column,
			final boolean isSelected)
	{
		if(c instanceof JLabel)
		{
			c.setForeground(UIManager.getColor(isSelected ? "Table.selectionForeground"
					: "Label.foreground"));
		}
		else if(c instanceof JCheckBox)
		{
			c.setForeground(UIManager.getColor(isSelected ? "Table.selectionForeground"
					: "CheckBox.foreground"));
		}
		else if(c instanceof JRadioButton)
		{
			c.setForeground(UIManager.getColor(isSelected ? "List.selectionForeground"
					: "RadioButton.foreground"));
		}
	}
}
