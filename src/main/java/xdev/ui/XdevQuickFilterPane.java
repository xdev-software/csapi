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
package xdev.ui;


import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.Scrollable;
import javax.swing.table.TableModel;

import xdev.ui.table.ExtendedTableSupport;

import com.jidesoft.grid.QuickFilterPane;
import com.jidesoft.plaf.UIDefaultsLookup;


/**
 * {@code XdevQuickFilterPane} can be used to dynamically filter the rows of a
 * table.
 * <p>
 * It takes any {@link TableModel} as input.
 * </p>
 * <p>
 * A {@code XdevQuickFilterPane} consists of a {@link JList} for the columns of
 * the modes, that are used to filter the table.
 * </p>
 * 
 * @author XDEV Software
 * @see QuickFilterPane
 */
public class XdevQuickFilterPane extends QuickFilterPane implements XdevFocusCycleComponent,
		Scrollable
{
	/**
	 * serialVersionUID.
	 */
	private static final long	serialVersionUID	= 1L;
	
	
	/**
	 * @see QuickFilterPane#QuickFilterPane()
	 */
	public XdevQuickFilterPane()
	{
		super();
	}
	
	
	/**
	 * @param tableModel
	 *            the table model. It could be any table model. We will
	 *            automatically wrap it in FilterableTableModel.
	 * @see QuickFilterPane#QuickFilterPane(TableModel)
	 */
	public XdevQuickFilterPane(TableModel tableModel)
	{
		super(tableModel);
	}
	
	
	/**
	 * 
	 * @param tableModel
	 *            the table model. It could be any table model. We will
	 *            automatically wrap it in FilterableTableModel.
	 * @param columnIndices
	 *            the indices to the columns to be filtered. You need to make
	 *            sure all values in the index array are within the range.
	 *            Otherwise, it will throw ArrayIndexOutOfBoundsException. The
	 *            order also matters as it will determine the filter order. In
	 *            the other words, it will apply filter to the first column
	 *            index, then the second and so on.
	 * @see QuickFilterPane#QuickFilterPane(TableModel, int[])
	 */
	public XdevQuickFilterPane(TableModel tableModel, int[] columnIndices)
	{
		super(tableModel,columnIndices);
	}
	
	
	/**
	 * @param tableModel
	 *            the table model. It could be any table model. We will
	 *            automatically wrap it in FilterableTableModel.
	 * @param columnIndices
	 *            the indices to the columns to be filtered. You need to make
	 *            sure all values in the index array are within the range.
	 *            Otherwise, it will throw ArrayIndexOutOfBoundsException. The
	 *            order also matters as it will determine the filter order. In
	 *            the other words, it will apply filter to the first column
	 *            index, then the second and so on.
	 * @param displayNames
	 *            the names used to label each list. If you don't specify this
	 *            parameter, it will use column names in tableModel instead. In
	 *            most cases, it should be fine.
	 * @see QuickFilterPane#QuickFilterPane(TableModel, int[], String[])
	 */
	public XdevQuickFilterPane(TableModel tableModel, int[] columnIndices, String[] displayNames)
	{
		super(tableModel,columnIndices,displayNames);
	}
	
	
	/**
	 * Connects the filter pane with a table, meaning it always uses the table's
	 * model.
	 * 
	 * @param table
	 *            the table to connect with.
	 * @param columns
	 *            the filtered columns, leave empty to use all columns of the
	 *            model
	 */
	public void setFilterFor(final JTable table, String... columns)
	{
		setTable(table);
		
		if(columns != null && columns.length > 0)
		{
			setModel(table.getModel(),ExtendedTableSupport.getColumnIndices(table,columns));
		}
		else
		{
			setModel(table.getModel());
		}
		
		table.addPropertyChangeListener("model",new PropertyChangeListener()
		{
			boolean	fireChanged	= true;
			
			
			@Override
			public void propertyChange(PropertyChangeEvent evt)
			{
				if(fireChanged)
				{
					try
					{
						fireChanged = false;
						
						setTableModel(table.getModel());
					}
					finally
					{
						fireChanged = true;
					}
				}
			}
		});
	}
	
	
	/**
	 * Updates the {@code XdevQuickFilterPane} instance.
	 * <p>
	 * Should be called, if the default constructor was used to create the
	 * instance.
	 * </p>
	 * 
	 * @param tableModel
	 *            the model object of the table to filter
	 */
	public void setModel(TableModel tableModel)
	{
		this.setModel(tableModel,(int[])null);
	}
	
	
	/**
	 * Updates the {@code XdevQuickFilterPane} instance.
	 * <p>
	 * Should be called, if the default constructor was used to create the
	 * instance.
	 * </p>
	 * 
	 * @param tableModel
	 *            the model object of the table to filter
	 * @param columnIndices
	 *            an array of column indexes. for each specified column a
	 *            separate filter list will be created.
	 */
	public void setModel(TableModel tableModel, int[] columnIndices)
	{
		this.setModel(tableModel,columnIndices,null);
	}
	
	
	/**
	 * Updates the {@code XdevQuickTableFilterField} instance.
	 * 
	 * @param tableModel
	 *            the model object of the table to filter
	 * @param columnNames
	 *            an array of column names
	 * @param displayNames
	 *            an array of column display names
	 * @throws IllegalArgumentException
	 *             if column name does not exist in filtered table
	 * @see #setModel(TableModel, int[])
	 */
	public void setModel(TableModel tableModel, String[] columnNames, String[] displayNames)
			throws IllegalArgumentException
	{
		int[] columnIndices = ExtendedTableSupport.getColumnIndices(tableModel,columnNames);
		this.setModel(tableModel,columnIndices,displayNames);
	}
	
	
	/**
	 * Updates the {@code XdevQuickTableFilterField} instance.
	 * 
	 * @param tableModel
	 *            the model object of the table to filter
	 * @param columnNames
	 *            an array of column names
	 * @throws IllegalArgumentException
	 *             if column name does not exist in filtered table
	 * @see #setModel(TableModel, int[])
	 */
	public void setModel(TableModel tableModel, String[] columnNames)
			throws IllegalArgumentException
	{
		int[] columnIndices = ExtendedTableSupport.getColumnIndices(tableModel,columnNames);
		this.setModel(tableModel,columnIndices,columnNames);
	}
	
	
	/**
	 * Updates the {@code XdevQuickFilterPane} instance.
	 * <p>
	 * Should be called, if the default constructor was used to create the
	 * instance.
	 * </p>
	 * 
	 * @param tableModel
	 *            the model object of the table to filter
	 * @param columnIndices
	 *            an array of column indexes. for each specified column a
	 *            separate filter list will be created.
	 * @param displayNames
	 *            an array of String for the titles of the column filter lists
	 */
	public void setModel(TableModel tableModel, int[] columnIndices, String[] displayNames)
	{
		this.setTableModel(tableModel);
		this.setColumnIndices(columnIndices);
		this.setDisplayNames(displayNames);
	}
	
	//
	// XdevFocusCycleComponent impl - start
	//
	
	/**
	 * the initial tabIndex.
	 */
	private int	tabIndex	= -1;
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getTabIndex()
	{
		return tabIndex;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTabIndex(int tabIndex)
	{
		if(this.tabIndex != tabIndex)
		{
			int oldValue = this.tabIndex;
			this.tabIndex = tabIndex;
			firePropertyChange(TAB_INDEX_PROPERTY,oldValue,tabIndex);
		}
	}
	
	
	//
	// XdevFocusCycleComponent impl - end
	//
	
	//
	// Scrollable impl - start
	//
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Dimension getPreferredScrollableViewportSize()
	{
		return getPreferredSize();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction)
	{
		return getScrollableBlockIncrement(visibleRect,orientation,direction);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction)
	{
		int min = getWidth();
		int cols = getColumnCount();
		int dividerSize = UIDefaultsLookup.getInt("JideSplitPane.dividerSize");
		for(int i = 0; i < cols; i++)
		{
			JList list = getList(i);
			if(list != null)
			{
				Component scrl = list.getParent().getParent();
				min = Math.min(min,scrl.getWidth() + dividerSize);
			}
		}
		return min;
	}
	
	
	/**
	 * Returns the number of displayed columns.
	 * 
	 * @return the number of displayed columns
	 */
	private int getColumnCount()
	{
		int[] indices = getColumnIndices();
		if(indices != null)
		{
			return indices.length;
		}
		TableModel model = getDisplayTableModel();
		if(model != null)
		{
			return model.getColumnCount();
		}
		return 0;
	}
	
	
	@Override
	public boolean getScrollableTracksViewportHeight()
	{
		return false;
	}
	
	
	@Override
	public boolean getScrollableTracksViewportWidth()
	{
		return false;
	}
	
	
	//
	// Scrollable impl - end
	//
	
	// ////////////////////////////////////////////////////////////
	// / Following properties are now shown on development time///
	// ///////////////////////////////////////////////////////////
	
	/**
	 * {@inheritDoc}
	 */
	@BeanProperty(category = DefaultBeanCategories.MISC)
	@Override
	public void setFont(Font paramFont)
	{
		super.setFont(paramFont);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@BeanProperty(category = DefaultBeanCategories.MISC)
	@Override
	public void setForeground(Color paramColor)
	{
		super.setForeground(paramColor);
	}
}
