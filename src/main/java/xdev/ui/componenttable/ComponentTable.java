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


import java.awt.Component;
import java.awt.Container;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.UIManager;
import javax.swing.table.TableModel;

import xdev.lang.OperationCanceledException;
import xdev.ui.ComponentTreeVisitor;
import xdev.ui.UIUtils;

import com.jidesoft.grid.SortableTable;


/**
 * 
 * 
 * @author XDEV Software (RHHF)
 * @see ComponentTable
 * @since 4.0
 */
public class ComponentTable extends SortableTable
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 8586220934573329052L;
	Class<? extends ComponentTableTemplate<?>>	templateClass;
	
	{
		/*
		 * Reordering destroys the span (because the span
		 * is set to the first column). Therefore we turn it off.
		 */
		this.getTableHeader().setReorderingAllowed(false);
	}
	
	
	public Class<? extends ComponentTableTemplate<?>> getTemplateClass()
	{
		return templateClass;
	}
	
	
	public void setTemplateClass(Class<? extends ComponentTableTemplate<?>> templateClass)
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
	
	
	@Override
	public void setModel(TableModel paramTableModel)
	{
		
		// ComponentTableRenderer renderer = new
		// ComponentTableRenderer(templateClass);
		//
		// ComponentTableEditor editor = new
		// ComponentTableEditor(templateClass);
		//
		// this.putClientProperty("terminateEditOnFocusLost",true);
		//
		// ComponentTableModel model = new ComponentTableModel(paramTableModel);
		// super.setModel(model);
		// TableColumn col = this.getColumnModel().getColumn(0);
		//
		// col.setCellEditor(editor);
		// col.setCellRenderer(renderer);
		//
		// this.setRowHeight(template.getPreferredSize().height);
		//
		// }else{
		super.setModel(paramTableModel);
		// }
	}
	
	
	public void decorateTemplate(final Component template, int row, int column,
			final boolean isSelected)
	{
		template.setBackground(isSelected ? getSelectionBackground() : getBackground());
		
		if(template instanceof Container)
		{
			UIUtils.lookupComponentTree((Container)template,
					new ComponentTreeVisitor<Object, Component>()
					{
						@Override
						public Object visit(Component c) throws OperationCanceledException
						{
							if(c instanceof JLabel)
							{
								c.setForeground(UIManager
										.getColor(isSelected ? "Table.selectionForeground"
												: "Label.foreground"));
							}
							else if(c instanceof JCheckBox)
							{
								c.setForeground(UIManager
										.getColor(isSelected ? "Table.selectionForeground"
												: "CheckBox.foreground"));
							}
							else if(c instanceof JRadioButton)
							{
								c.setForeground(UIManager
										.getColor(isSelected ? "List.selectionForeground"
												: "RadioButton.foreground"));
							}
							
							return null;
						}
					});
		}
	}
}
