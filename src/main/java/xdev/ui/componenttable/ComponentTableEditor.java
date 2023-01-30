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
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellEditor;

import xdev.lang.NotNull;

import com.jidesoft.grid.TableModelWrapperUtils;


/**
 * 
 * @author XDEV Software (RHHF)
 * @author XDEV Software (FHAE)
 * @see ComponentTable
 * @since 4.0
 */
public abstract class ComponentTableEditor<T> extends AbstractCellEditor implements TableCellEditor
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -748861326694980986L;
	private final Class<? extends ComponentTableTemplate<T>>	templateClass;
	private ComponentTableTemplate<T>							template;
	private MouseEvent											lastMouseEvent;
	
	
	public ComponentTableEditor(
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
		
		this.templateClass = templateClass;
		
		this.createTemplateInstance();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getCellEditorValue()
	{
		/*
		 * Saving the values of the template right away may
		 * cause (gui)lags. Keeping the template in memory and saving the values
		 * later on the dispatching theard avoids those lags.
		 * 
		 * For that to work "getTableCellEditorComponent" must always return a
		 * new instance!
		 */
		final ComponentTableTemplate<?> templateReference = this.template;
		
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				templateReference.onEditorClose();
			}
		});
		
		return null;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
			int row, int column)
	{
		this.createTemplateInstance();
		
		int actualRowIndex = TableModelWrapperUtils.getActualRowAt(table.getModel(),row);
		
		this.template.setValue(this.getRowValues(table,value,isSelected,row,column,actualRowIndex));
		
		final Component component = (Component)this.template;
		
		decorateTemplate(component,table,row,column);
		
		if(lastMouseEvent != null)
		{
			SwingUtilities.invokeLater(new Runnable()
			{
				public void run()
				{
					Point p = component.getLocation();
					final Point mouse = lastMouseEvent.getPoint();
					mouse.x -= p.x;
					mouse.y -= p.y;
					final Component toFocus = SwingUtilities.getDeepestComponentAt(component,
							mouse.x,mouse.y);
					if(toFocus != null)
					{
						SwingUtilities.invokeLater(new Runnable()
						{
							public void run()
							{
								toFocus.requestFocus();
								
								// SwingUtilities.invokeLater(new Runnable()
								// {
								// public void run()
								// {
								// Point click =
								// SwingUtilities.convertPoint(comp,mouse,
								// toFocus);
								// MouseEvent e = new
								// MouseEvent(toFocus,lastMouseEvent
								// .getID(),System.currentTimeMillis(),0,click.x,
								// click.y,1,false);
								// toFocus.dispatchEvent(e);
								// }
								// });
							}
						});
					}
				}
			});
		}
		
		return (Component)this.template;
	}
	
	
	protected void decorateTemplate(Component template, JTable table, int row, int column)
	{
		if(table instanceof ComponentTable)
		{
			((ComponentTable)table).decorateTemplate(template,row,column,true);
		}
	}
	
	
	protected void createTemplateInstance()
	{
		try
		{
			this.template = this.templateClass.newInstance();
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
	
	
	protected void setTemplate(ComponentTableTemplate<T> template)
	{
		this.template = template;
	}
	
	
	protected Class<? extends ComponentTableTemplate<T>> getTemplateClass()
	{
		return this.templateClass;
	}
	
	
	protected abstract T getRowValues(JTable table, Object value, boolean isSelected, int rowIndex,
			int columnIndex, int actualRowIndex);
	
	
	protected ComponentTableTemplate<T> getTemplate()
	{
		return template;
	}
	
	
	@Override
	public boolean isCellEditable(EventObject anEvent)
	{
		lastMouseEvent = null;
		if(anEvent instanceof MouseEvent)
		{
			lastMouseEvent = (MouseEvent)anEvent;
			// lastMouseEvent.consume();
		}
		
		return true;
	}
}
