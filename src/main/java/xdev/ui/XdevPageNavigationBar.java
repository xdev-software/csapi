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


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import xdev.ui.table.ExtendedTable;

import com.jidesoft.grid.TableModelWrapperUtils;
import com.jidesoft.list.ListModelWrapperUtils;
import com.jidesoft.paging.PageNavigationBar;
import com.jidesoft.paging.PageNavigationSupport;


/**
 * {@code XdevPageNavigationBar} is a graphical component, that supplements a
 * specified component of type {@link JList} or type {@link JTable} with paging
 * functionality.
 * <p>
 * One of the {@code setNavigationBarFor} methods of this class hat to be called
 * after instantiation with the component to be paged. Within the {@code init}
 * method you can further specify, if the navigation bar is displayed at the
 * bottom (default) or top and if the number of rows per page should be fixed.
 * </p>
 * 
 * @author XDEV Software
 * @see PageNavigationBar
 */
public class XdevPageNavigationBar extends JPanel implements XdevFocusCycleComponent
{
	/**
	 * serialVersionUID.
	 */
	private static final long	serialVersionUID			= 1L;
	
	/**
	 * the identifier for the related {@link PropertyChangeEvent}.
	 */
	private static final String	MODEL_CHANGED_IDENTIFIER	= "model";
	
	/**
	 * Support class for providing paging functionality.
	 */
	private NavigationBarHelper	navigationBarHelper;
	
	/**
	 * a wrapped instance of {@link PageNavigationBar}.
	 */
	// its ensured navigationBar is not null because default construktur
	// initialized it with table
	private PageNavigationBar	navigationBar;
	
	/**
	 * the component to be wrapped - required because of propertyChange issues.
	 */
	private static Component	navigationComponent;
	
	/**
	 * the font of the {@link PageNavigationBar#getNavigationField()}.
	 */
	private Font				navigationFieldFont;
	
	/**
	 * the foreground of the {@link PageNavigationBar#getNavigationField()}.
	 */
	private Color				navigationFieldFontColor;
	
	
	/**
	 * Instantiates a new {@link XdevPageNavigationBar}.
	 * <p>
	 * If this constructor is used, a call to setNavigationBar is required.
	 * </p>
	 */
	public XdevPageNavigationBar()
	{
		this.setLayout(new BorderLayout());
		
		// default constructor used a empty dummy table
		XdevTable table = new XdevTable();
		setNavigationBarFor(table);
		navigationComponent = table;
	}
	
	
	/**
	 * Instantiates a new {@link XdevPageNavigationBar} with a source component
	 * and its scrollpane.
	 * 
	 * @param list
	 *            the component to be supported with paging.
	 * @param scrollPane
	 *            the scrollPanel surrounding the list
	 */
	public XdevPageNavigationBar(JList list, JScrollPane scrollPane)
	{
		this.setLayout(new BorderLayout());
		this.setNavigationBarFor(list,scrollPane);
		navigationComponent = list;
	}
	
	
	/**
	 * Instantiates a new {@link XdevPageNavigationBar} with a source component
	 * and its scrollpane.
	 * 
	 * @param table
	 *            the component to be supported with paging.
	 * @param scrollPane
	 *            the scrollPanel surrounding the table
	 */
	public XdevPageNavigationBar(JTable table, JScrollPane scrollPane)
	{
		
		this.setLayout(new BorderLayout());
		this.setNavigationBarFor(table,scrollPane);
		navigationComponent = table;
	}
	
	
	/**
	 * Instantiates a new {@link XdevPageNavigationBar} with a source component.
	 * (displays fixed number of rows)
	 * 
	 * @param list
	 *            the component to be paged.
	 */
	public XdevPageNavigationBar(JList list)
	{
		this.setLayout(new BorderLayout());
		this.setNavigationBarFor(list);
		navigationComponent = list;
	}
	
	
	/**
	 * Instantiates a new {@link XdevPageNavigationBar} with a source component
	 * (displays fixed number of rows).
	 * 
	 * @param table
	 *            the component to be paged.
	 */
	public XdevPageNavigationBar(ExtendedTable table)
	{
		this.setLayout(new BorderLayout());
		this.setNavigationBarFor(table);
		navigationComponent = (JTable)table;
	}
	
	
	/**
	 * Sets the source component for this instance of
	 * {@link XdevPageNavigationBar}.
	 * <p>
	 * <strong> Note: The navigation bar is re-initialized every time you call
	 * this method and all properties set prior to this method will be
	 * obsolete.</strong>
	 * </p>
	 * 
	 * @param list
	 *            the list to be supported with paging.
	 * @param scrollPane
	 *            the scrollPanel surrounding the list
	 */
	public void setNavigationBarFor(JList list, final JScrollPane scrollPane)
	{
		
		setNavigationBarForImpl(list,scrollPane);
		navigationComponent = list;
	}
	
	
	/**
	 * Sets the source component for this instance of
	 * {@link XdevPageNavigationBar}.
	 * <p>
	 * <strong> Note: The navigation bar is re-initialized every time you call
	 * this method and all properties set prior to this method will be
	 * obsolete.</strong>
	 * </p>
	 * 
	 * @param table
	 *            the table to be supported with paging.
	 * @param scrollPane
	 *            the scrollPanel surrounding the table
	 */
	public void setNavigationBarFor(JTable table, final JScrollPane scrollPane)
	{
		
		setNavigationBarForImpl(table,scrollPane);
		navigationComponent = table;
	}
	
	
	/**
	 * Connects a {@link XdevPageNavigationBar} to be used with an instance of
	 * type {@link JList} or {@link JTable}.
	 * <p>
	 * This method takes a scrollpane as a second parameter to allow for a
	 * flexible number of records per page.
	 * </p>
	 * <p>
	 * <strong> Note: The navigation bar is re-initialized every time you call
	 * this method and all properties set prior to this method will be
	 * obsolete.</strong>
	 * </p>
	 * 
	 * @param component
	 *            a component of type {@link JList} or {@link JTable}.
	 * @param scrollPane
	 *            the scrollpane surrounding the component.
	 */
	private void setNavigationBarForImpl(JComponent component, final JScrollPane scrollPane)
	{
		initNavigationBarHelper(component);
		
		navigationBarHelper.initComponent();
		
		scrollPane.addComponentListener(new ComponentAdapter()
		{
			@Override
			public void componentResized(ComponentEvent e)
			{
				configurePageSize(scrollPane);
			}
		});
		
		// ensure that componentResized event is triggered at least once (fixes
		// #12194)
		scrollPane.getViewport().addChangeListener(new ChangeListener()
		{
			@Override
			public void stateChanged(ChangeEvent e)
			{
				configurePageSize(scrollPane);
				((JViewport)e.getSource()).removeChangeListener(this);
			}
		});
		
		displayNavigationBar();
	}
	
	
	/**
	 * Configures the current number of rows.
	 * 
	 * @param scrollPane
	 *            a {@link JScrollPane} used to determine the viewport height.
	 */
	private void configurePageSize(final JScrollPane scrollPane)
	{
		int rowCount = scrollPane.getViewport().getHeight() / navigationBarHelper.getRowHeight();
		
		PageNavigationSupport pageNavigationSupport = navigationBarHelper
				.getPageNavigationSupport();
		if(pageNavigationSupport != null)
		{
			pageNavigationSupport.setPageSize(rowCount);
		}
	}
	
	
	/**
	 * Sets the source component for this instance of
	 * {@link XdevPageNavigationBar}. (displays fixed number of rows)
	 * <p>
	 * <strong> Note: The navigation bar is re-initialized every time you call
	 * this method and all properties set prior to this method will be
	 * obsolete.</strong>
	 * </p>
	 * 
	 * @param list
	 *            the component to be paged.
	 */
	
	public void setNavigationBarFor(JList list)
	{
		setNavigationBarForImpl(list,list.getVisibleRowCount());
	}
	
	
	/**
	 * Sets the source component for this instance of
	 * {@link XdevPageNavigationBar}. (displays fixed number of rows)
	 * <p>
	 * <strong> Note: The navigation bar is re-initialized every time you call
	 * this method and all properties set prior to this method will be
	 * obsolete.</strong>
	 * </p>
	 * 
	 * @param table
	 *            the component to be paged.
	 */
	
	public void setNavigationBarFor(ExtendedTable table)
	{
		setNavigationBarForImpl((JTable)table,table.getVisibleRowCount());
	}
	
	
	/**
	 * Connects a {@link XdevPageNavigationBar} to be used with an instance of
	 * type {@link JList} or {@link JTable}.
	 * <p>
	 * This method takes an int as a second parameter to set a fixed number of
	 * records per page.
	 * </p>
	 * <p>
	 * <strong> Note: The navigation bar is re-initialized every time you call
	 * this method and all properties set prior to this method will be
	 * obsolete.</strong>
	 * </p>
	 * 
	 * @param component
	 *            a component of type {@link JList} or {@link JTable}.
	 * @param numberOfRows
	 *            the number of rows per page
	 */
	private void setNavigationBarForImpl(JComponent component, int numberOfRows)
	{
		initNavigationBarHelper(component);
		
		navigationBarHelper.initComponent();
		
		navigationBarHelper.setVisibleRows(numberOfRows);
		
		displayNavigationBar();
	}
	
	
	/**
	 * Initializes the {@link NavigationBarHelper}.
	 * <p>
	 * Dependent on the specified parameter a {@link NavigationBarHelperForList}
	 * or a {@link NavigationBarHelperForTable} is created.
	 * </p>
	 * 
	 * @param component
	 *            the source component, either of type {@link JList} or
	 *            {@link JTable}.
	 */
	private void initNavigationBarHelper(JComponent component)
	{
		if(component instanceof JTable)
		{
			this.navigationBarHelper = new NavigationBarHelperForTable((JTable)component);
		}
		else if(component instanceof JList)
		{
			this.navigationBarHelper = new NavigationBarHelperForList((JList)component);
		}
		else
		{
			throw new IllegalArgumentException("component has to be of type JList or JTable");
		}
	}
	
	
	/**
	 * re inits properties with dependencies.
	 */
	private void initPropertyIndependency()
	{
		this.setFont(this.navigationFieldFont);
		this.setForeground(this.navigationFieldFontColor);
	}
	
	
	/**
	 * Creates a wrapped instance of PageNavigationBar and adds it as the sole
	 * component to the layout.
	 */
	private void displayNavigationBar()
	{
		this.navigationBar = navigationBarHelper.createPageNavigationBar();
		this.navigationBar.setEnabled(super.isEnabled());
		this.initPropertyIndependency();
		this.add(this.navigationBar,BorderLayout.CENTER);
	}
	
	
	/**
	 * Sets whether or not this component is enabled. A component that is
	 * enabled may respond to user input, while a component that is not enabled
	 * cannot respond to user input. Some components may alter their visual
	 * representation when they are disabled in order to provide feedback to the
	 * user that they cannot take input.
	 * 
	 * Note: Disabling a component does not disable its children.
	 * 
	 * Note: Disabling a lightweight component does not prevent it from
	 * receiving MouseEvents.
	 * 
	 * @param state
	 *            enabled {@code true} if this component should be enabled,
	 *            {@code false} otherwise
	 */
	public void setEnabled(boolean state)
	{
		super.setEnabled(state);
		this.navigationBar.setEnabled(state);
	}
	
	
	/**
	 * Determines whether this component is enabled. An enabled component can
	 * respond to user input and generate events. Components are enabled
	 * initially by default. A component may be enabled or disabled by calling
	 * its setEnabled method.
	 * 
	 * @return true if the component is enabled, false otherwise
	 */
	public boolean isEnabled()
	{
		return this.navigationBar.isEnabled();
	}
	
	
	
	/**
	 * Interface {@code NavigationBarsupport} that contains common navigation
	 * bar functionality for Lists and Tables. Specific Implementations for
	 * Lists and Tables are {@link NavigationBarHelperForList} and
	 * {@link NavigationBarHelperForTable}
	 * 
	 * @author XDEV Software
	 */
	private interface NavigationBarHelper extends Serializable
	{
		/**
		 * Initializes the contained component.
		 */
		public void initComponent();
		
		
		/**
		 * Sets the number rows that are visible at the same time.
		 * 
		 * @param numberOfRows
		 *            a number
		 */
		public void setVisibleRows(int numberOfRows);
		
		
		/**
		 * Gets the height of the row.
		 * 
		 * @return the height of the row in pixels
		 */
		public int getRowHeight();
		
		
		/**
		 * Returns the {@link PageNavigationSupport} for the contained
		 * component.
		 * 
		 * @return PageNavigationSupport for either a {@code List} or a
		 *         {@code Table}.
		 */
		public PageNavigationSupport getPageNavigationSupport();
		
		
		/**
		 * Create a new instance of {@link PageNavigationBar} for the contained
		 * component.
		 * 
		 * @return a {@link PageNavigationBar}
		 */
		public PageNavigationBar createPageNavigationBar();
	}
	
	
	
	/**
	 * Implementation of {@link NavigationBarHelper} for elements of type
	 * {@link JTable}.
	 * 
	 * @author XDEV Software
	 * 
	 */
	private static class NavigationBarHelperForTable implements NavigationBarHelper
	{
		/**
		 * serialVersionUID.
		 */
		private static final long		serialVersionUID	= 1L;
		
		/**
		 * The table to be provided with a {@link PageNavigationBar}.
		 */
		JTable							table;
		PageNavigationBar				navigationBar;
		private PropertyChangeListener	tableModelChangeListener;
		
		
		/**
		 * Creates a new instance of {@link NavigationBarHelper}.
		 * 
		 * @param newTable
		 *            the underlying table
		 */
		public NavigationBarHelperForTable(final JTable newTable)
		{
			
			JTable oldTable = null;
			Component comp = getNavigationComponent();
			
			if(comp instanceof JTable)
			{
				oldTable = (JTable)comp;
			}
			
			// refresh table
			this.table = newTable;
			
			if(oldTable != newTable)
			{
				if(tableModelChangeListener == null)
				{
					tableModelChangeListener = new PropertyChangeListener()
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
									
									// after initialization
									if(navigationBar != null)
									{
										JTable table = (JTable)evt.getSource();
										// cant validate because navigationbar
										// does
										// not provide any getModel - methods.
										navigationBar.setTableModel(table.getModel());
									}
								}
								finally
								{
									fireChanged = true;
								}
							}
						}
					};
				}
				
				if(oldTable != null)
				{
					oldTable.removePropertyChangeListener(MODEL_CHANGED_IDENTIFIER,
							tableModelChangeListener);
				}
				
				this.table.addPropertyChangeListener(MODEL_CHANGED_IDENTIFIER,
						tableModelChangeListener);
			}
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void initComponent()
		{
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public int getRowHeight()
		{
			return table.getRowHeight();
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public PageNavigationSupport getPageNavigationSupport()
		{
			return (PageNavigationSupport)TableModelWrapperUtils.getActualTableModel(
					table.getModel(),PageNavigationSupport.class);
		}
		
		
		/**
		 * creates a new pavenavigation bar.
		 * 
		 * @return returns the created pagenavigation bar
		 */
		@Override
		public PageNavigationBar createPageNavigationBar()
		{
			// fix aufgrund von Issue (# 13068) reverted.
			// see issue for information.
			
			// if(!(table instanceof XdevTable))
			// {
			// if a default VirtualTableWrapper model is created here - the row
			// convertion works as default and returns the wrong rows
			
			this.navigationBar = new PageNavigationBar(table);
			return this.navigationBar;
			
			// }
			// else
			// {
			// // XdevTable needs a special implementation of the TableModel to
			// // work as expected (#12193)
			// return new PageNavigationBar(table)
			// {
			//
			// private static final long serialVersionUID = 1L;
			//
			//
			// @Override
			// protected PageNavigationSupport createPageTableModel(TableModel
			// tablemodel,
			// int i)
			// {
			// return new XdevDefaultPageTableModel(tablemodel,i);
			// }
			// };
			// }
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setVisibleRows(int numberOfRows)
		{
			int height = 0;
			for(int row = 0; row < numberOfRows; row++)
			{
				height += table.getRowHeight(row);
			}
			// #11976 Sonderbehandlung f�r ExtendedTables
			if(table instanceof ExtendedTable)
			{
				ExtendedTable extendedTable = (ExtendedTable)table;
				extendedTable.setVisibleRowCount(numberOfRows);
			}
			else
			{
				table.setPreferredScrollableViewportSize(new Dimension(table
						.getPreferredScrollableViewportSize().width,height));
			}
		}
	}
	
	
	
	/**
	 * Implementation of {@link NavigationBarHelper} for elements of type
	 * {@link JList}.
	 * 
	 * @author XDEV Software
	 * 
	 */
	private static class NavigationBarHelperForList implements NavigationBarHelper
	{
		
		/**
		 * 
		 */
		private static final long	serialVersionUID	= 1L;
		
		/**
		 * The list to be provided with a {@link PageNavigationBar}.
		 */
		JList						list;
		PageNavigationBar			navigationBar;
		
		
		/**
		 * Creates a new instance of {@link NavigationBarHelper}.
		 * 
		 * @param list
		 *            the underlying list
		 */
		public NavigationBarHelperForList(final JList list)
		{
			this.list = list;
			
			// flag handling to avoid infinite prop change loop
			this.list.addPropertyChangeListener(MODEL_CHANGED_IDENTIFIER,
					new PropertyChangeListener()
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
									
									if(navigationBar != null)
									{
										navigationBar.setListModel(list.getModel());
									}
								}
								finally
								{
									fireChanged = true;
								}
							}
							
						}
					});
		}
		
		
		@Override
		public void initComponent()
		{
		}
		
		
		@Override
		public int getRowHeight()
		{
			return list.getCellBounds(0,0).height;
		}
		
		
		@Override
		public PageNavigationSupport getPageNavigationSupport()
		{
			return (PageNavigationSupport)ListModelWrapperUtils.getActualListModel(list.getModel(),
					PageNavigationSupport.class);
		}
		
		
		@Override
		public PageNavigationBar createPageNavigationBar()
		{
			this.navigationBar = new PageNavigationBar(this.list);
			return this.navigationBar;
		}
		
		
		@Override
		public void setVisibleRows(int numberOfRows)
		{
			list.setVisibleRowCount(numberOfRows);
		}
		
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
	
	// /**
	// * Special implementation for usage of {@link XdevPageNavigationBar} in
	// * combination with {@link XdevTable}.
	// */
	// private static class XdevDefaultPageTableModel extends
	// DefaultPageTableModel implements
	// VirtualTableWrapper
	// {
	// /**
	// *
	// */
	// private static final long serialVersionUID = 1L;
	//
	// /**
	// * A {@link VirtualTableModel} used as a base for this table model.
	// */
	// private VirtualTableModel virtualTableModel = null;
	//
	//
	// /**
	// * Creates a new instance.
	// *
	// * @param tablemodel
	// * a outer table model.
	// */
	// public XdevDefaultPageTableModel(TableModel tablemodel)
	// {
	// super(tablemodel);
	// virtualTableModel =
	// (VirtualTableModel)TableModelWrapperUtils.getActualTableModel(
	// tablemodel,VirtualTableModel.class);
	//
	// }
	//
	//
	// /**
	// * Creates a new instance.
	// *
	// * @param tablemodel
	// * a outer table model.
	// * @param pageSize
	// * the page size
	// */
	// public XdevDefaultPageTableModel(TableModel tablemodel, int pageSize)
	// {
	// super(tablemodel,pageSize);
	// virtualTableModel =
	// (VirtualTableModel)TableModelWrapperUtils.getActualTableModel(
	// tablemodel,VirtualTableModel.class);
	// }
	//
	//
	// @Override
	// public VirtualTable getVirtualTable()
	// {
	// if(virtualTableModel != null)
	// {
	// return virtualTableModel.getVirtualTable();
	// }
	// else
	// {
	// return null;
	// }
	// }
	//
	//
	// @Override
	// public int viewToModelColumn(int col)
	// {
	// if(virtualTableModel != null)
	// {
	// return virtualTableModel.viewToModelColumn(col);
	// }
	// else
	// {
	// return -1;
	// }
	// }
	//
	//
	// @Override
	// public int[] getModelColumnIndices()
	// {
	// if(virtualTableModel != null)
	// {
	// return virtualTableModel.getModelColumnIndices();
	// }
	// else
	// {
	// return new int[]{};
	// }
	// }
	//
	//
	// @Override
	// public VirtualTableRow getVirtualTableRow(int modelRow)
	// {
	// if(virtualTableModel != null)
	// {
	// return virtualTableModel.getVirtualTableRow(modelRow);
	// }
	// else
	// {
	// return null;
	// }
	// }
	// }
	
	/**
	 * Returns the component to be wrapped - required because of propertyChange
	 * issues.
	 * 
	 * @return the wrapped component.
	 */
	private static Component getNavigationComponent()
	{
		return navigationComponent;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setFont(Font paramFont)
	{
		this.navigationFieldFont = paramFont;
		
		if(this.navigationBar != null)
		{
			this.navigationBar.getNavigationField().getTextField().setFont(paramFont);
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setForeground(Color paramColor)
	{
		this.navigationFieldFontColor = paramColor;
		
		if(this.navigationBar != null)
		{
			this.navigationBar.getNavigationField().getTextField().setForeground(paramColor);
		}
	}
}
