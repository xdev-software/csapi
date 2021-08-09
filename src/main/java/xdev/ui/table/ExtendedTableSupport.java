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


import java.awt.Color;
import java.beans.Beans;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.CellEditor;
import javax.swing.JTable;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import xdev.db.sql.SELECT;
import xdev.ui.MasterDetailComponent;
import xdev.ui.TableSupport;
import xdev.ui.persistence.Persistable;
import xdev.util.IntList;
import xdev.util.XdevDate;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTableColumn;
import xdev.vt.VirtualTableModel;
import xdev.vt.VirtualTableWrapper;
import xdev.vt.XdevBlob;
import xdev.vt.XdevClob;

import com.jidesoft.grid.AutoFilterTableHeader;
import com.jidesoft.grid.AutoResizePopupMenuCustomizer;
import com.jidesoft.grid.CellEditorFactory;
import com.jidesoft.grid.CellEditorManager;
import com.jidesoft.grid.ISortableTableModel;
import com.jidesoft.grid.RowStripeTableStyleProvider;
import com.jidesoft.grid.SelectTablePopupMenuCustomizer;
import com.jidesoft.grid.SortableTable;
import com.jidesoft.grid.TableColumnChooserPopupMenuCustomizer;
import com.jidesoft.grid.TableHeaderPopupMenuInstaller;
import com.jidesoft.grid.TableModelWrapperUtils;
import com.jidesoft.grid.TableStyleProvider;


/**
 * This class provides common functionality used by all table types of the
 * Component Suite.
 * <p>
 * This class extends {@link TableSupport} with functionality specific to the
 * Jide components.
 * </p>
 * 
 * @param <T>
 *            type table
 * @author XDEV Software
 */
public class ExtendedTableSupport<T extends SortableTable & MasterDetailComponent<T>> extends
		TableSupport<T> implements Persistable
{
	/**
	 * the table to support.
	 */
	protected T													table;
	
	/**
	 * the {@link AutoFilterTableHeader} used for the supported table.
	 */
	protected AutoFilterTableHeader								tableHeader;
	
	/**
	 * Are table header popup's enabled for the supported table?
	 */
	private boolean												tableHeaderPopupMenuEnabled	= false;
	
	/**
	 * Separates different persistent parts of tables.
	 */
	private static final String									PERSISTABLE_SEPARATOR		= "~";
	
	/**
	 * An array of ClassTypes to register the {@link XdevTableEditor} on.
	 */
	private static Class<?>[]									editorClasstypes			= {
			Byte.class,byte.class,Short.class,short.class,Integer.class,int.class,Long.class,
			long.class,Float.class,float.class,Double.class,double.class,Boolean.class,
			boolean.class,String.class,XdevClob.class,XdevBlob.class,byte[].class,Date.class,
			XdevDate.class																	};
	
	private SortableTablePageControl<T>							pageControl					= null;
	private VirtualTableSortableTableLazyLoadingTableModel<T>	lazyLoadingTableModel		= null;
	
	static
	{
		if(!Beans.isDesignTime())
		{
			// Configures TableCellEditors used by all the cs tables
			CellEditorFactory cellEditorFactory = new CellEditorFactory()
			{
				@Override
				public CellEditor create()
				{
					return new CellStyleTableEditor(new XdevTableEditor());
				}
			};
			for(int i = 0; i < editorClasstypes.length; i++)
			{
				CellEditorManager.registerEditor(editorClasstypes[i],cellEditorFactory);
			}
		}
	}
	
	
	/**
	 * Creates a new instance of {@link ExtendedTableSupport}.
	 * 
	 * @param table
	 *            a {@link SortableTable} instance to be supported by this
	 *            class. A master detail component.
	 */
	public ExtendedTableSupport(T table)
	{
		super(table);
		
		this.table = table;
		
		tableHeader = createTableHeader();
		tableHeader.setAutoFilterEnabled(false);
		table.setTableHeader(tableHeader);
		
		setTableHeaderPopupMenuEnabled(true);
	}
	
	
	/**
	 * Factory method for creating a {@link AutoFilterTableHeader}.
	 * 
	 * @return a new instance of {@link AutoFilterTableHeader}.
	 */
	protected AutoFilterTableHeader createTableHeader()
	{
		AutoFilterTableHeader autoFilterTableHeader = new AutoFilterTableHeader(table);
		// #11958 - enable display of filter names
		autoFilterTableHeader.setShowFilterName(true);
		autoFilterTableHeader.setShowFilterNameAsToolTip(true);
		return autoFilterTableHeader;
	}
	
	
	//
	// Feature Auto Filter Header
	//
	
	/**
	 * Returns the AutoFilterTableHeader of the supported component.
	 * 
	 * @return the {@link AutoFilterTableHeader}.
	 */
	public AutoFilterTableHeader getAutoFilterTableHeader()
	{
		return tableHeader;
	}
	
	
	/**
	 * Enables auto filtering headers for the supported {@code table}.
	 * <p>
	 * This method adds filtering headers for all columns of the {@code table}.
	 * </p>
	 * 
	 * @param b
	 *            if {@code true} enable auto filter
	 */
	public void setAutoFilterEnabled(boolean b)
	{
		tableHeader.setAutoFilterEnabled(b);
	}
	
	
	/**
	 * Is auto filtering currently enabled?
	 * 
	 * @return {@code true} if enabled
	 */
	public boolean isAutoFilterEnabled()
	{
		return tableHeader.isAutoFilterEnabled();
	}
	
	
	private void registerLazyLoadingSortListener(ISortableTableModel model)
	{
		SortableTablePageControl<T> sortablePageControl = this.getPageControl();
		sortablePageControl.addPageingTableSortListener(model);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public SortableTablePageControl<T> getPageControl()
	{
		if(pageControl == null)
		{
			pageControl = new SortableTablePageControl<T>(component);
		}
		
		return pageControl;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected VirtualTableSortableTableLazyLoadingTableModel<T> getLazyLoadingTableModel()
	{
		if(lazyLoadingTableModel == null)
		{
			lazyLoadingTableModel = new VirtualTableSortableTableLazyLoadingTableModel<T>(component);
		}
		
		return lazyLoadingTableModel;
	}
	
	
	/**
	 * En- or disables the display of the filter name in the column header.
	 * 
	 * @param showFilterName
	 *            {@code true} for enabling
	 */
	public void setShowFilterName(boolean showFilterName)
	{
		tableHeader.setShowFilterName(showFilterName);
	}
	
	
	/**
	 * Is filter name currently displayed?
	 * 
	 * @return {@code} true for displayed
	 */
	public boolean isShowFilterName()
	{
		return tableHeader.isShowFilterName();
	}
	
	
	/**
	 * En- or disables the display of the filter name as tooltip.
	 * 
	 * @param showFilterNameAsToolTip
	 *            {@code true} for enabling
	 */
	public void setShowFilterNameAsToolTip(boolean showFilterNameAsToolTip)
	{
		tableHeader.setShowFilterNameAsToolTip(showFilterNameAsToolTip);
	}
	
	
	/**
	 * Is filter name currently displayed as tooltip?
	 * 
	 * @return {@code} true for displayed
	 */
	public boolean isShowFilterNameAsToolTip()
	{
		return tableHeader.isShowFilterNameAsToolTip();
	}
	
	
	/**
	 * Gets the tableHeaderPopupMenuEnabled flag.
	 * 
	 * @return {@code true}, if table popup menu headers are enabled
	 */
	public boolean isTableHeaderPopupMenuEnabled()
	{
		return tableHeaderPopupMenuEnabled;
	}
	
	
	/**
	 * Sets the tableHeaderPopupMenuEnabled flag.
	 * <p>
	 * Header Popup menus are displayed, whenever the user right-clicks on a
	 * table column of the table.
	 * </p>
	 * 
	 * @param tablePopupMenuEnabled
	 *            if {@code true}, table header popup menus are enabled
	 */
	public void setTableHeaderPopupMenuEnabled(boolean tablePopupMenuEnabled)
	{
		tableHeaderPopupMenuEnabled = tablePopupMenuEnabled;
		
		if(tablePopupMenuEnabled)
		{
			installTableHeaderPopupMenu();
		}
		else
		{
			uninstallTableHeaderPopupMenu();
		}
	}
	
	
	/**
	 * Installs a table header popup menu on a table.
	 */
	protected void installTableHeaderPopupMenu()
	{
		TableHeaderPopupMenuInstaller installer = new TableHeaderPopupMenuInstaller(this.table);
		customizeTableHeaderPopupMenuInstaller(installer);
	}
	
	
	/**
	 * Customizes the header popup menu.
	 * 
	 * @param installer
	 *            a {@link TableHeaderPopupMenuInstaller} used for customizing.
	 */
	protected void customizeTableHeaderPopupMenuInstaller(TableHeaderPopupMenuInstaller installer)
	{
		installer.addTableHeaderPopupMenuCustomizer(new AutoResizePopupMenuCustomizer());
		installer.addTableHeaderPopupMenuCustomizer(new TableColumnChooserPopupMenuCustomizer());
		installer.addTableHeaderPopupMenuCustomizer(new SelectTablePopupMenuCustomizer());
	}
	
	
	/**
	 * Uninstalls a table header popup menu from a table.
	 */
	protected void uninstallTableHeaderPopupMenu()
	{
		TableHeaderPopupMenuInstaller installer = TableHeaderPopupMenuInstaller
				.getTableHeaderPopupMenuInstaller(table);
		if(installer != null)
		{
			installer.uninstallListeners();
		}
	}
	
	
	/**
	 * Creates a Virtual Table from the currently displayed values of this
	 * supported table.
	 * <p>
	 * This allows to get sorted or filtered VirtualTable instances for further
	 * processing.
	 * </p>
	 * <p>
	 * The supported table must have a referenced VirtualTable (either through a
	 * {@link VirtualTableModel} or a {@link XdevTreeTableModel}) for this
	 * method to work)
	 * </p>
	 * 
	 * @return a {@link VirtualTable} representing the currently displayed
	 *         results of the table
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public VirtualTable createSubsettedVirtualTable()
	{
		TableModel outerTableModel = this.table.getModel();
		
		VirtualTableWrapper wrapper = (VirtualTableWrapper)TableModelWrapperUtils
				.getActualTableModel(outerTableModel,VirtualTableWrapper.class);
		
		XdevTreeTableModel xdevTreeTableModel = (XdevTreeTableModel)TableModelWrapperUtils
				.getActualTableModel(outerTableModel,XdevTreeTableModel.class);
		
		if(wrapper == null && xdevTreeTableModel == null)
		{
			throw new IllegalStateException("There is no wrapped VirtualTable for this table.");
		}
		
		final VirtualTable modelVT = wrapper != null ? wrapper.getVirtualTable()
				: xdevTreeTableModel.getVirtualTable();
		
		VirtualTable subsettedVt = createSubsettedVT(modelVT);
		
		fillSubsettedVT(modelVT,subsettedVt);
		
		return subsettedVt;
		
	}
	
	
	/**
	 * Creates a subsetted Virtual Table.
	 * <p>
	 * A new Virtual Table is created, that contains only those columns that are
	 * currently visible in the table.
	 * </p>
	 * 
	 * @param modelVT
	 *            the Virtual Table representing the model for reading column
	 *            information
	 * @return the resulting {@link VirtualTable} after subsetting.
	 */
	@SuppressWarnings("rawtypes")
	private VirtualTable createSubsettedVT(final VirtualTable modelVT)
	{
		final int columnCount = table.getColumnCount();
		
		final VirtualTableColumn[] virtualTableColumns = new VirtualTableColumn[columnCount];
		
		List<String> visibibleColumnNames = getVisibleColumnNames();
		
		for(int i = 0; i < columnCount; i++)
		{
			VirtualTableColumn modelColumn = null;
			for(VirtualTableColumn column : modelVT)
			{
				String modelColumnName = null;
				if(column.getCaption() != null && !column.getCaption().equals(""))
				{
					modelColumnName = column.getCaption();
				}
				else
				{
					modelColumnName = column.getName();
				}
				String visibleColumnName = visibibleColumnNames.get(i);
				if(modelColumnName.equals(visibleColumnName))
				{
					modelColumn = column;
					break;
				}
			}
			virtualTableColumns[i] = modelColumn.clone();
		}
		
		String name = modelVT.getName();
		String alias = modelVT.getDatabaseAlias();
		VirtualTable vt = new VirtualTable(name,alias,virtualTableColumns);
		
		return vt;
	}
	
	
	/**
	 * Fills the values of the subsetted Virtual Table with values from the
	 * models Virtual Table.
	 * <p>
	 * Only values are filled in that are currently visible in the table. The
	 * order of rows and columns equals the displayed order.
	 * </p>
	 * 
	 * @param modelVT
	 *            the underlying Virtual Table representing the model for
	 *            reading the values
	 * @param subsettedVT
	 *            the subsetted Virtual Table representing the view for writing
	 *            the values
	 */
	private void fillSubsettedVT(final VirtualTable modelVT, VirtualTable subsettedVT)
	{
		TableModel outerTableModel = table.getModel();
		int columnCount = table.getColumnCount();
		List<String> visibibleColumnNames = getVisibleColumnNames();
		
		for(int row = 0; row < outerTableModel.getRowCount(); row++)
		{
			List<Object> values = new ArrayList<Object>();
			for(int col = 0; col < columnCount; col++)
			{
				int outerCol = -1;
				for(int colModel = 0; colModel <= outerTableModel.getColumnCount(); colModel++)
				{
					if(outerTableModel.getColumnName(colModel)
							.equals(visibibleColumnNames.get(col)))
					{
						outerCol = colModel;
						break;
					}
				}
				
				int innerCol = TableModelWrapperUtils.getActualColumnAt(outerTableModel,outerCol,
						VirtualTableWrapper.class);
				int innerRow = TableModelWrapperUtils.getActualRowAt(outerTableModel,row,
						VirtualTableWrapper.class);
				
				Object value = modelVT.getValueAt(innerRow,innerCol);
				values.add(value);
			}
			try
			{
				subsettedVT.addRow(values,false);
			}
			catch(Exception e)
			{
				throw new IllegalStateException("Creation of VirtualTable failed.",e);
			}
		}
	}
	
	
	@Override
	public void loadPersistentState(String persistentState)
	{
		String[] storedPrefs = persistentState.split(PERSISTABLE_SEPARATOR);
		if(storedPrefs.length > 0)
		{
			com.jidesoft.grid.TableUtils.setTablePreferenceByName(table,storedPrefs[0]);
			if(storedPrefs.length > 1)
			{
				com.jidesoft.grid.TableUtils.setSortableTablePreference(table,storedPrefs[1]);
			}
		}
	}
	
	
	@Override
	public String savePersistentState()
	{
		String prefs = com.jidesoft.grid.TableUtils.getTablePreferenceByName(table);
		String sortPrefs = com.jidesoft.grid.TableUtils.getSortableTablePreference(table);
		String storedPrefs = prefs + PERSISTABLE_SEPARATOR + sortPrefs;
		return storedPrefs;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPersistentId()
	{
		return (table.getName() != null) ? table.getName() : table.getClass().getSimpleName();
	}
	
	
	/**
	 * The enabling of tables should be implemented by the tables itself. It is
	 * therefore not recommended to call this method.
	 * 
	 * @return always {@code true}
	 */
	@Override
	public boolean isPersistenceEnabled()
	{
		return true;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setModel(VirtualTable vt, String columns, SELECT select, Object... params)
	{
		super.setModel(vt,columns,select,params);
		TableModel vtModel = table.getModel();
		XdevTableModelWrapper wrapper = new XdevTableModelWrapper(vtModel);
		
		// forced to use flags induced by lazy loading API - see XDEVAPI-189
		if(pagingEnabled && !lazyLoadingEnabled)
		{
			// see TableSupport#getRowSorter for the JTable sorting algorithm
			this.registerLazyLoadingSortListener(wrapper);
		}
		
		table.setModel(wrapper);
		
		// forced to use flags induced by lazy loading API - see XDEVAPI-189
		if(lazyLoadingEnabled && !pagingEnabled)
		{
			ISortableTableModel sortableModel = (ISortableTableModel)TableModelWrapperUtils
					.getActualTableModel(component.getModel(),ISortableTableModel.class);
			this.getLazyLoadingTableModel().setConcreteModel(sortableModel);
		}
	}
	
	
	/**
	 * Configures a {@link TableStyleProvider} for supporting alternating colors
	 * for table rows.
	 */
	public void setAlternatingBackgroundColors()
	{
		if(table instanceof ExtendedTable)
		{
			final ExtendedTable extendedTable = (ExtendedTable)table;
			Color odd = extendedTable.getOddBackground() != null ? extendedTable.getOddBackground()
					: Color.WHITE;
			Color even = extendedTable.getEvenBackground() != null ? extendedTable
					.getEvenBackground() : Color.WHITE;
			RowStripeTableStyleProvider styleProvider = new RowStripeTableStyleProvider();
			styleProvider.setAlternativeBackgroundColors(new Color[]{even,odd});
			table.setTableStyleProvider(styleProvider);
		}
	}
	
	
	/**
	 * Common code for initialization of CellRenderers and CellEditors for the
	 * supported table.
	 */
	public void initRenderersAndEditors()
	{
		if(table instanceof ExtendedTable)
		{
			// use renderers and editors from XAPI (#12404)
			this.table.setDefaultCellRenderer(new CellStyleTableRenderer(new XdevTableRenderer()));
			/*
			 * registration of editors is done globally for all table instances
			 * in a static initializer block of this class
			 */
		}
	}
	
	
	/**
	 * Sort column specified by columnName.
	 * 
	 * @param columnName
	 *            name of column to be sorted.
	 * @param ascending
	 *            whether the sort order is ascending or descending.
	 */
	public void sort(String columnName, boolean ascending)
	{
		int index = getColumnIndex(table,columnName);
		if(index != -1)
		{
			table.sortColumn(index,false,ascending);
		}
	}
	
	
	/**
	 * Retrieves column indices of a specified {@link VirtualTable} and a list
	 * of column names.
	 * 
	 * @param table
	 *            a {@link VirtualTable}
	 * @param columnNames
	 *            a comma separated list of column names of the {@code vt}.
	 * @return an array of column indices
	 */
	public static int[] getColumnIndices(JTable table, String... columnNames)
	{
		IntList list = new IntList();
		for(String columnName : columnNames)
		{
			list.add(getColumnIndex(table,columnName));
		}
		return list.toArray();
	}
	
	
	/**
	 * Retrieves a column index of a specified {@link JTable} and a column name.
	 * 
	 * @param table
	 *            a JTable
	 * @param columnName
	 *            the name of the column
	 * @return a column index
	 */
	public static int getColumnIndex(JTable table, String columnName)
	{
		int index = getModelColumnIndex(table,columnName); // getColumnIndexNoException(
		// table.getModel(),columnName);
		if(index != TableColumnConverter.NOT_IN_ORIGINAL_MODEL)
		{
			return index;
		}
		
		TableColumnModel columnModel = table.getColumnModel();
		if(columnModel != null)
		{
			try
			{
				return table.getColumnModel().getColumnIndex(columnName);
			}
			catch(IllegalArgumentException e)
			{
				// column not found
			}
		}
		
		throw columnNodeFound(columnName);
	}
	
	
	/**
	 * Retrieves column indices for a specified TableModel and a list of column
	 * names.
	 * 
	 * @param model
	 *            a {@link TableModel}.
	 * @param columnNames
	 *            a number of column names.
	 * @return an integer array containing column indices.
	 */
	public static int[] getColumnIndices(TableModel model, String... columnNames)
	{
		IntList list = new IntList();
		for(String columnName : columnNames)
		{
			list.add(getColumnIndex(model,columnName));
		}
		return list.toArray();
	}
	
	
	/**
	 * Retrieves the column index for a given table model and a column name.
	 * 
	 * @param model
	 *            a {@link TableModel}.
	 * @param columnName
	 *            a column name.
	 * @return a column index.
	 */
	public static int getColumnIndex(TableModel model, String columnName)
	{
		int index = getColumnIndexNoException(model,columnName);
		if(index != -1)
		{
			return index;
		}
		
		throw columnNodeFound(columnName);
	}
	
	
	/**
	 * Retrieves the index for a specified {@link TableModel} and a column name.
	 * 
	 * @param model
	 *            a {@link TableModel}
	 * @param columnName
	 *            a column name.
	 * @return a column index, or -1 if no column index could be found.
	 */
	private static int getColumnIndexNoException(TableModel model, String columnName)
	{
		model = TableModelWrapperUtils.getActualTableModel(model,VirtualTableWrapper.class);
		if(model instanceof VirtualTableWrapper)
		{
			VirtualTableWrapper wrapper = (VirtualTableWrapper)model;
			int[] indices = wrapper.getModelColumnIndices();
			for(int i = 0; i < indices.length; i++)
			{
				if(wrapper.getVirtualTable().getColumnAt(indices[i]).getName().equals(columnName))
				{
					return i;
				}
			}
		}
		
		return -1;
	}
	
	
	/**
	 * Returns the column index of the given name from the given table.
	 * 
	 * @param table
	 *            the table to calculate the colum index from.
	 * @param columnName
	 *            the column name to determine its index.
	 * @return the column index of the given name from the given table.
	 */
	private static int getModelColumnIndex(JTable table, String columnName)
	{
		TableModel model = TableModelWrapperUtils.getActualTableModel(table.getModel(),
				VirtualTableWrapper.class);
		
		if(model instanceof VirtualTableWrapper)
		{
			VirtualTableWrapper wrapper = (VirtualTableWrapper)model;
			
			int[] visibleIndices = wrapper.getModelColumnIndices();
			for(int i = 0; i < visibleIndices.length; i++)
			{
				VirtualTableColumn<?> modelColumn = wrapper.getVirtualTable().getColumnAt(
						visibleIndices[i]);
				if(modelColumn.getName().equals(columnName))
				{
					return i;
				}
			}
		}
		
		return TableColumnConverter.NOT_IN_ORIGINAL_MODEL;
	}
	
	
	/**
	 * Creates a exception, if column could not be found.
	 * 
	 * @param columnName
	 *            the name of column.
	 * @return a {@link IllegalArgumentException}
	 */
	private static IllegalArgumentException columnNodeFound(String columnName)
	{
		return new IllegalArgumentException("Column '" + columnName + "' not found");
	}
}
