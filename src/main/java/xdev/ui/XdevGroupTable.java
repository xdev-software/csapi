package xdev.ui;

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
import java.awt.Dimension;
import java.awt.Point;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import xdev.db.Operator;
import xdev.db.sql.Condition;
import xdev.db.sql.SELECT;
import xdev.ui.persistence.Persistable;
import xdev.ui.table.ExtendedTable;
import xdev.ui.table.ExtendedTableSupport;
import xdev.ui.table.XdevTableModelWrapper;
import xdev.util.ObjectUtils;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTable.VirtualTableRow;
import xdev.vt.VirtualTableModel;
import xdev.vt.VirtualTableWrapper;

import com.jidesoft.grid.AutoFilterTableHeader;
import com.jidesoft.grid.DefaultGroupTableModel;
import com.jidesoft.grid.FilterableTableModel;
import com.jidesoft.grid.GroupTable;
import com.jidesoft.grid.GroupTableHeader;
import com.jidesoft.grid.GroupTablePopupMenuCustomizer;
import com.jidesoft.grid.SortListener;
import com.jidesoft.grid.TableHeaderPopupMenuInstaller;
import com.jidesoft.grid.TableModelWrapperUtils;
import com.jidesoft.grid.TableUtils;


/**
 * This is a extended Version of Jide's {@link GroupTable}.
 * <p>
 * It provides all features of the {@code GroupTable} and provides integration
 * support for XDEV's {@code VirtualTable}.
 * <p>
 * Integration works by wrapping a {@link VirtualTableModel} within an instance
 * of {@link DefaultGroupTableModel}.
 * </p>
 * </p>
 * 
 * @author XDEV Software
 * @see GroupTable
 */
public class XdevGroupTable extends GroupTable implements ExtendedTable<XdevGroupTable>,
		XdevFocusCycleComponent, VirtualTableEditor, Persistable, PopupRowSelectionHandler
{
	/**
	 * serialVersionUID.
	 */
	private static final long	serialVersionUID	= 1L;
	
	
	
	/**
	 * A specialized support class for {@link XdevGroupTable} instances.
	 * 
	 * @author XDEV Software
	 * 
	 */
	private static class Support extends ExtendedTableSupport<XdevGroupTable>
	{
		/**
		 * Creates a new instance.
		 * 
		 * @param table
		 *            the table to support.
		 * @param mdc
		 *            the master detail component for this support class.
		 */
		Support(XdevGroupTable table)
		{
			super(table);
			
			setShowGroupHeaders(true);
		}
		
		
		/**
		 * {@inheritDoc}
		 * <p>
		 * This override returns a {@link GroupTableHeader}.
		 * </p>
		 */
		@Override
		protected AutoFilterTableHeader createTableHeader()
		{
			GroupTableHeader header = new GroupTableHeader(table);
			header.setGroupHeaderEnabled(false);
			return header;
		}
		
		
		/**
		 * Displays / hides group headers.
		 * 
		 * @param showGroupHeaders
		 *            if {@code true} group headers are displayed.
		 */
		void setShowGroupHeaders(boolean showGroupHeaders)
		{
			((GroupTableHeader)tableHeader).setGroupHeaderEnabled(showGroupHeaders);
		}
		
		
		/**
		 * Are group headers displayed?
		 * 
		 * @return if {@code true} group headers are displayed
		 */
		boolean isShowGroupHeaders()
		{
			return ((GroupTableHeader)tableHeader).isGroupHeaderEnabled();
		}
		
		
		/**
		 * {@inheritDoc}
		 * <p>
		 * This override add a {@link GroupTablePopupMenuCustomizer} to the
		 * popup header menu.
		 * </p>
		 */
		@Override
		protected void customizeTableHeaderPopupMenuInstaller(
				TableHeaderPopupMenuInstaller installer)
		{
			super.customizeTableHeaderPopupMenuInstaller(installer);
			installer.addTableHeaderPopupMenuCustomizer(new GroupTablePopupMenuCustomizer());
		}
	}
	
	/**
	 * A supporting instance for performing group table specific operations.
	 */
	protected Support				support;
	
	/**
	 * This class provides functionality common to all CS table types .
	 */
	protected XdevTableModelWrapper	modelWrapper;
	/**
	 * flag for single level grouping.
	 */
	private boolean					singleLevel				= false;
	/**
	 * flag for showing columns for group count.
	 */
	private boolean					showGroupColumns		= false;
	/**
	 * flag for showing count columns.
	 */
	private boolean					showCountColumns		= false;
	/**
	 * flag for keeping the column order.
	 */
	private boolean					keepColumnOrder			= false;
	/**
	 * flag for showing a separate grouping column.
	 */
	private boolean					showSeparateGroupColumn	= false;
	
	/**
	 * flag for configuring, if table is editable.
	 */
	private boolean					editable				= false;
	
	/**
	 * an integer specifying the preferred number of rows to display without
	 * requiring scrolling.
	 */
	private int						visibleRows				= 8;
	
	/**
	 * Color of table rows with even index.
	 */
	private Color					evenBackground			= null;
	/**
	 * Color of table rows with odd index.
	 */
	private Color					oddBackground			= null;
	
	/**
	 * Should the gui state be persisted? Defaults to {@code true}.
	 */
	private boolean					persistenceEnabled		= true;
	
	
	/**
	 * @see JTable#JTable()
	 */
	public XdevGroupTable()
	{
		super();
		init();
	}
	
	
	/**
	 * 
	 * @param numRows
	 *            the number of rows the table holds
	 * @param numColumns
	 *            the number of columns the table holds
	 * @see JTable#JTable(int, int)
	 */
	public XdevGroupTable(int numRows, int numColumns)
	{
		super(numRows,numColumns);
		init();
	}
	
	
	/**
	 * @param rowData
	 *            the data for the new table
	 * @param columnNames
	 *            names of each column
	 * @see JTable#JTable(Object[][], Object[])
	 * 
	 */
	public XdevGroupTable(Object[][] rowData, Object[] columnNames)
	{
		super(rowData,columnNames);
		init();
	}
	
	
	/**
	 * 
	 * @param dm
	 *            the data model for the table
	 * @param cm
	 *            the column model for the table
	 * @param sm
	 *            the row selection model for the table
	 * @see JTable#JTable(TableModel, TableColumnModel, ListSelectionModel)
	 * 
	 */
	public XdevGroupTable(TableModel dm, TableColumnModel cm, ListSelectionModel sm)
	{
		super(dm,cm,sm);
		init();
	}
	
	
	/**
	 * 
	 * @param dm
	 *            the data model for the table
	 * @param cm
	 *            the column model for the table
	 * @see JTable#JTable(TableModel, TableColumnModel)
	 * 
	 */
	public XdevGroupTable(TableModel dm, TableColumnModel cm)
	{
		super(dm,cm);
		init();
	}
	
	
	/**
	 * @param dm
	 *            the data model for the table
	 * @see JTable#JTable(TableModel)
	 * 
	 */
	public XdevGroupTable(TableModel dm)
	{
		setModel(dm);
		init();
	}
	
	
	/**
	 * @param rowData
	 *            the data for the new table
	 * @param columnNames
	 *            names of each column
	 * @see JTable#JTable(Vector, Vector)
	 * 
	 */
	@SuppressWarnings("rawtypes")
	// inherited untyped Vector from super class
	public XdevGroupTable(Vector rowData, Vector columnNames)
	{
		super(rowData,columnNames);
		init();
	}
	
	
	/**
	 * This Constructor initializes a new XdevGroupTable with a
	 * {@link VirtualTableModel} and number of column indices.
	 * 
	 * @param virtualTableModel
	 *            the {@link VirtualTableModel} to use
	 * @param groupColumnIndices
	 *            the column indices used for grouping
	 */
	public XdevGroupTable(VirtualTableModel virtualTableModel, int... groupColumnIndices)
	{
		super(wrapVirtualTableRows(virtualTableModel,groupColumnIndices));
		init();
	}
	
	
	/**
	 * This Constructor initializes a new XdevGroupTable with a
	 * {@link VirtualTableModel} and a number of column names.
	 * 
	 * @param virtualTableModel
	 *            the {@link VirtualTableModel} to use
	 * @param groupColumnNames
	 *            the column indices used for grouping
	 * @throws IllegalArgumentException
	 *             if a specified column name does not exist in the
	 *             {@link VirtualTableModel}.
	 */
	public XdevGroupTable(VirtualTableModel virtualTableModel, String... groupColumnNames)
			throws IllegalArgumentException
	{
		super(wrapVirtualTableRows(virtualTableModel,groupColumnNames));
		init();
	}
	
	
	private void init()
	{
		// #12158 a DefaultGroupTableModel has to be attached to the table,
		// prior to initializing the support class
		this.setModel(new DefaultGroupTableModel(this.getModel()));
		support = new Support(this);
		
		support.initRenderersAndEditors();
		setFillsViewportHeight(true);
		setFillsBottom(true);
		setFillsRight(true);
		setFillsSelection(true);
	}
	
	
	/**
	 * re inits properties with dependencies.
	 */
	private void initPropertyIndependency()
	{
		this.enableShowGroupColumns(this.showGroupColumns);
		this.enableShowCountColumns(this.showCountColumns);
		this.enableShowSeparateGroupColumn(this.showSeparateGroupColumn);
		this.enableKeepColumnOrder(this.keepColumnOrder);
		this.enableSingleLevel(this.singleLevel);
	}
	
	
	/**
	 * Wraps a {@code VirtualTableModel} within a {@code DefaultGroupTableModel}
	 * .
	 * 
	 * @param virtualTableModel
	 *            the {@code VirtualTableModel} to wrap
	 * @param groupColumnIndices
	 *            the column indices used for grouping
	 * @return a {@code DefaultGroupTableModel}
	 */
	private static DefaultGroupTableModel wrapVirtualTableRows(VirtualTableModel virtualTableModel,
			int... groupColumnIndices)
	{
		FilterableTableModel filterableTableModel = new FilterableTableModel(virtualTableModel);
		DefaultGroupTableModel model = new DefaultGroupTableModel(filterableTableModel);
		for(int i : groupColumnIndices)
		{
			model.addGroupColumn(i);
		}
		model.groupAndRefresh();
		return model;
	}
	
	
	/**
	 * Wraps a {@code VirtualTableModel} within a {@code DefaultGroupTableModel}
	 * .
	 * 
	 * @param virtualTableModel
	 *            the {@code VirtualTableModel} to wrap
	 * @param groupColumnNames
	 *            the column names used for grouping
	 * @return a {@code DefaultGroupTableModel}
	 */
	private static DefaultGroupTableModel wrapVirtualTableRows(VirtualTableModel virtualTableModel,
			String[] groupColumnNames)
	{
		FilterableTableModel filterableTableModel = new FilterableTableModel(virtualTableModel);
		DefaultGroupTableModel groupTableModel = new DefaultGroupTableModel(filterableTableModel);
		for(String columnName : groupColumnNames)
		{
			
			int columnIndex = virtualTableModel.getVT().getColumnIndex(columnName);
			if(columnIndex < 0)
			{
				throw new IllegalArgumentException("Column name: " + columnName
						+ " does not exist.");
			}
			int outerColumnIndex = TableModelWrapperUtils.getColumnAt(groupTableModel,
					virtualTableModel,columnIndex);
			
			groupTableModel.addGroupColumn(outerColumnIndex);
		}
		groupTableModel.groupAndRefresh();
		return groupTableModel;
	}
	
	
	/**
	 * Gets the DefaultGroupTableModel for this table.
	 * 
	 * @return the DefaultGroupTableModel
	 */
	public DefaultGroupTableModel getGroupTableModel()
	{
		return (DefaultGroupTableModel)TableModelWrapperUtils.getActualTableModel(getModel(),
				DefaultGroupTableModel.class);
	}
	
	
	/**
	 * Sets the {@link TableModel} for this table.
	 * <p>
	 * The passed {@code model} gets wrapped into a new
	 * {@link XdevTableModelWrapper}, if necessary.
	 * </p>
	 * 
	 * @param model
	 *            the {@link TableModel} to set.
	 */
	@Override
	public void setModel(TableModel model)
	{
		if(model instanceof XdevTableModelWrapper)
		{
			modelWrapper = (XdevTableModelWrapper)model;
		}
		else if(model instanceof VirtualTableModel)
		{
			DefaultGroupTableModel groupTableModel = wrapVirtualTableRows((VirtualTableModel)model,
					new int[]{});
			modelWrapper = createXdevTableModelWrapper(groupTableModel);
		}
		else
		{
			modelWrapper = createXdevTableModelWrapper(model);
		}
		
		super.setModel(modelWrapper);
		
		this.initPropertyIndependency();
		
		if(support != null)
		{
			support.adjustColumnWidths();
		}
		
		// refresh
		// enableKeepColumnOrder(keepColumnOrder);
	}
	
	
	/**
	 * Sets the {@link TableModel} for this table.
	 * <p>
	 * The passed {@code model} gets wrapped into a new
	 * {@link XdevTableModelWrapper}, if necessary.
	 * </p>
	 * 
	 * @param model
	 *            the {@link TableModel} to set.
	 * @param groupColumnIndices
	 *            the column indices used for grouping
	 */
	public void setModel(VirtualTableModel model, int... groupColumnIndices)
	{
		this.setModel(wrapVirtualTableRows(model,groupColumnIndices));
	}
	
	
	/**
	 * Sets the {@link TableModel} for this table.
	 * <p>
	 * The passed {@code model} gets wrapped into a new
	 * {@link XdevTableModelWrapper}, if necessary.
	 * </p>
	 * 
	 * @param model
	 *            the {@link TableModel} to set.
	 * @param groupColumnNames
	 *            the column indices used for grouping
	 * @throws IllegalArgumentException
	 *             if a specified column name does not exist in the
	 *             {@link VirtualTableModel}.
	 */
	public void setModel(VirtualTableModel model, String... groupColumnNames)
			throws IllegalArgumentException
	{
		this.setModel(wrapVirtualTableRows(model,groupColumnNames));
	}
	
	
	/**
	 * Creates a wrapped version of the passed {@code model}.
	 * 
	 * @param model
	 *            the {@link TableModel} to wrap.
	 * @return a wrapped {@link TableModel} of type
	 *         {@link XdevTableModelWrapper}.
	 */
	protected XdevTableModelWrapper createXdevTableModelWrapper(TableModel model)
	{
		return new XdevTableModelWrapper(model);
	}
	
	
	/**
	 * Returns, if group headers are shown.
	 * 
	 * @return {@code true}, if group headers are shown.
	 * @see #setShowGroupHeaders(boolean)
	 */
	public boolean isShowGroupHeaders()
	{
		return support.isShowGroupHeaders();
	}
	
	
	/**
	 * Sets the display of group table headers.
	 * <p>
	 * Creates a label for each grouped column. Those labels can be moved per
	 * Drag & Drop to customize the grouping order.
	 * </p>
	 * 
	 * @param showGroupHeaders
	 *            {@code true}, if group headers should be displayed
	 */
	public void setShowGroupHeaders(boolean showGroupHeaders)
	{
		support.setShowGroupHeaders(showGroupHeaders);
	}
	
	
	/**
	 * Returns, if group columns are shown.
	 * 
	 * @return {@code true}, if group columns are shown.
	 */
	public boolean isShowGroupColumns()
	{
		return showGroupColumns;
	}
	
	
	/**
	 * Sets the display of group columns.
	 * 
	 * @param showGroupColumns
	 *            {@code true}, if grouped columns are to be shown.
	 */
	public void setShowGroupColumns(boolean showGroupColumns)
	{
		this.showGroupColumns = showGroupColumns;
		enableShowGroupColumns(showGroupColumns);
	}
	
	
	/**
	 * Enables or disabled the display of group columns.
	 * 
	 * @param isShown
	 *            if {@code true}, group columns are shown
	 */
	private void enableShowGroupColumns(boolean isShown)
	{
		TableModel model = TableModelWrapperUtils.getActualTableModel(this.getModel(),
				DefaultGroupTableModel.class);
		if(model instanceof DefaultGroupTableModel)
		{
			DefaultGroupTableModel defaultGroupTableModel = (DefaultGroupTableModel)model;
			defaultGroupTableModel.setDisplayGroupColumns(isShown);
			defaultGroupTableModel.groupAndRefresh();
			defaultGroupTableModel.fireTableStructureChanged();
			this.expandAll();
		}
	}
	
	
	/**
	 * Returns, if count columns are shown.
	 * 
	 * @return {@code true}, if count columns are shown.
	 */
	public boolean isShowCountColumns()
	{
		return showCountColumns;
	}
	
	
	/**
	 * Sets the display of group columns.
	 * <p>
	 * Works only if {@link #showGroupColumns} is set to {@code true}.
	 * </p>
	 * 
	 * @param showCountColumns
	 *            {@code true}, if count columns are to be shown.
	 */
	public void setShowCountColumns(boolean showCountColumns)
	{
		this.showCountColumns = showCountColumns;
		enableShowCountColumns(showCountColumns);
	}
	
	
	/**
	 * Enables or disabled the display of count columns.
	 * 
	 * @param isShown
	 *            if {@code true}, count columns are shown
	 */
	private void enableShowCountColumns(boolean isShown)
	{
		TableModel model = TableModelWrapperUtils.getActualTableModel(this.getModel(),
				DefaultGroupTableModel.class);
		if(model instanceof DefaultGroupTableModel)
		{
			DefaultGroupTableModel defaultGroupTableModel = (DefaultGroupTableModel)model;
			defaultGroupTableModel.setDisplayCountColumn(isShown);
			defaultGroupTableModel.groupAndRefresh();
			defaultGroupTableModel.fireTableStructureChanged();
			TableUtils.saveColumnOrders(this,false);
			this.expandAll();
		}
	}
	
	
	/**
	 * Returns, if grouping should be restricted to one column.
	 * 
	 * @return {@code true}, if grouping is restricted to one column.
	 */
	public boolean isSingleLevel()
	{
		return singleLevel;
	}
	
	
	/**
	 * Sets the single level grouping restriction.
	 * 
	 * @param singleLevel
	 *            {@code true}, if grouping is restricted to one column.
	 */
	public void setSingleLevel(boolean singleLevel)
	{
		this.singleLevel = singleLevel;
		enableSingleLevel(singleLevel);
	}
	
	
	/**
	 * Enables or disabled the single level grouping restriction.
	 * 
	 * @param singleLevel
	 *            if {@code true}, count columns are shown
	 */
	private void enableSingleLevel(boolean singleLevel)
	{
		TableModel model = TableModelWrapperUtils.getActualTableModel(this.getModel(),
				DefaultGroupTableModel.class);
		if(model instanceof DefaultGroupTableModel)
		{
			DefaultGroupTableModel defaultGroupTableModel = (DefaultGroupTableModel)model;
			defaultGroupTableModel.setSingleLevelGrouping(singleLevel);
			defaultGroupTableModel.groupAndRefresh();
			defaultGroupTableModel.fireTableStructureChanged();
			this.expandAll();
		}
	}
	
	
	/**
	 * Returns {@code true}, if column order is kept.
	 * 
	 * @return {@code true}, if column order should be kept
	 */
	public boolean isKeepColumnOrder()
	{
		return keepColumnOrder;
	}
	
	
	/**
	 * Sets the keeping of the column order.
	 * 
	 * @param keepColumnOrder
	 *            {@code true}, if column order should be kept
	 */
	public void setKeepColumnOrder(boolean keepColumnOrder)
	{
		this.keepColumnOrder = keepColumnOrder;
		enableKeepColumnOrder(keepColumnOrder);
	}
	
	
	/**
	 * Enables or disabled the column order keeping.
	 * 
	 * @param keepColumnOrder
	 *            if {@code true}, count columns are shown
	 */
	private void enableKeepColumnOrder(boolean keepColumnOrder)
	{
		TableModel model = TableModelWrapperUtils.getActualTableModel(this.getModel(),
				DefaultGroupTableModel.class);
		if(model instanceof DefaultGroupTableModel)
		{
			DefaultGroupTableModel defaultGroupTableModel = (DefaultGroupTableModel)model;
			String preference = TableUtils.getTablePreferenceByName(this);
			defaultGroupTableModel.setKeepColumnOrder(keepColumnOrder);
			defaultGroupTableModel.groupAndRefresh();
			defaultGroupTableModel.fireTableStructureChanged();
			TableUtils.setTablePreferenceByName(this,preference);
			this.expandAll();
		}
	}
	
	
	/**
	 * Returns, if a separate column is displayed for grouped values.
	 * 
	 * @return {@code true}, if separate group is displayed
	 */
	public boolean isShowSeparateGroupColumn()
	{
		return showSeparateGroupColumn;
	}
	
	
	/**
	 * Sets the display of a separate group column.
	 * 
	 * @param showSeparateGroupColumn
	 *            {@code true}, if separate group column should be displayed.
	 */
	public void setShowSeparateGroupColumn(boolean showSeparateGroupColumn)
	{
		this.showSeparateGroupColumn = showSeparateGroupColumn;
		enableShowSeparateGroupColumn(showSeparateGroupColumn);
	}
	
	
	/**
	 * Enables or disabled the column order keeping.
	 * 
	 * @param showSeparateGroupColumn
	 *            if {@code true}, separate group columns are shown.
	 */
	private void enableShowSeparateGroupColumn(boolean showSeparateGroupColumn)
	{
		TableModel model = TableModelWrapperUtils.getActualTableModel(this.getModel(),
				DefaultGroupTableModel.class);
		if(model instanceof DefaultGroupTableModel)
		{
			DefaultGroupTableModel defaultGroupTableModel = (DefaultGroupTableModel)model;
			defaultGroupTableModel.setDisplaySeparateGroupColumn(showSeparateGroupColumn);
			defaultGroupTableModel.groupAndRefresh();
			defaultGroupTableModel.fireTableStructureChanged();
			TableUtils.saveColumnOrders(this,false);
			this.expandAll();
		}
	}
	
	
	/**
	 * Gets the autoFilterEnabled flag.
	 * 
	 * @return {@code true}, if auto filtering headers are enabled
	 */
	public boolean isAutoFilterEnabled()
	{
		return support.isAutoFilterEnabled();
	}
	
	
	/**
	 * Sets the autoFilterEnabled flag.
	 * <p>
	 * When autofiltering gets enabled, every column of the table gets a
	 * additional header with a combobox, that can be used to filter the values
	 * of the table.
	 * </p>
	 * 
	 * @param autoFilterEnabled
	 *            if {@code true}, auto filtering headers get enabled
	 */
	public void setAutoFilterEnabled(boolean autoFilterEnabled)
	{
		support.setAutoFilterEnabled(autoFilterEnabled);
		if(autoFilterEnabled)
		{
			support.getAutoFilterTableHeader().setUseNativeHeaderRenderer(true);
		}
	}
	
	
	/**
	 * Gets the tableHeaderPopupMenuEnabled flag.
	 * 
	 * @return {@code true}, if table popup menu headers are enabled
	 */
	public boolean isTableHeaderPopupMenuEnabled()
	{
		return support.isTableHeaderPopupMenuEnabled();
	}
	
	
	/**
	 * Sets the tableHeaderPopupMenuEnabled flag.
	 * <p>
	 * Header popup menus are displayed, whenever the user right-clicks on a
	 * table column of the table.
	 * </p>
	 * 
	 * @param tablePopupMenuEnabled
	 *            if {@code true}, table header popup menus are enabled
	 */
	public void setTableHeaderPopupMenuEnabled(boolean tablePopupMenuEnabled)
	{
		support.setTableHeaderPopupMenuEnabled(tablePopupMenuEnabled);
	}
	
	
	//
	// MasterDetailComponent implementations - Start
	//
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getFormularName()
	{
		return support.getFormularName();
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.1
	 */
	@Override
	public void setDataField(String dataField)
	{
		support.setDataField(dataField);
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.1
	 */
	@Override
	public String getDataField()
	{
		return support.getDataField();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Deprecated
	public final void setFormularValue(VirtualTable vt, int col, Object value)
	{
		support.setFormularValue(vt,col,value);
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.2
	 */
	@Override
	public void setFormularValue(VirtualTable vt, Map<String, Object> record)
	{
		support.setFormularValue(vt,record);
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.2
	 */
	@Override
	public void setMasterValue(VirtualTable vt, Map<String, Object> record)
	{
		support.setValue(vt,record);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getFormularValue()
	{
		return support.getFormularValue();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void saveState()
	{
		support.saveState();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void restoreState()
	{
		support.restoreState();
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.1
	 */
	@Override
	public boolean hasStateChanged()
	{
		return support.hasStateChanged();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isMultiSelect()
	{
		return support.isMultiSelect();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean verify()
	{
		return support.verify();
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.1
	 */
	@Override
	public void addValidator(Validator validator)
	{
		support.addValidator(validator);
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.1
	 */
	@Override
	public void removeValidator(Validator validator)
	{
		support.removeValidator(validator);
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.1
	 */
	@Override
	public Validator[] getValidators()
	{
		return support.getValidators();
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.1
	 */
	@Override
	public void validateState() throws ValidationException
	{
		support.validateState();
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.1
	 */
	@Override
	public void validateState(Validation validation) throws ValidationException
	{
		support.validateState(validation);
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.1
	 */
	@Override
	public void setFilterOperator(Operator filterOperator)
	{
		support.setFilterOperator(filterOperator);
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.1
	 */
	@Override
	public Operator getFilterOperator()
	{
		return support.getFilterOperator();
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.2
	 */
	@Override
	public void setReadOnly(boolean readOnly)
	{
		support.setReadOnly(readOnly);
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.2
	 */
	@Override
	public boolean isReadOnly()
	{
		return support.isReadOnly();
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 4.0
	 */
	@Override
	public void setVirtualTable(VirtualTable vt)
	{
		setModel(vt);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public VirtualTable getVirtualTable()
	{
		return support.getVirtualTable();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public VirtualTableWrapper getVirtualTableWrapper()
	{
		return support.getVirtualTableWrapper();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void refresh()
	{
		support.refresh();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateModel(Condition condition, Object... params)
	{
		support.updateModel(condition,params);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clearModel()
	{
		support.clearModel();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addValueChangeListener(xdev.ui.MasterDetailComponent.ValueChangeListener l)
	{
		support.addValueChangeListener(l);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDetailHandler(xdev.ui.MasterDetailComponent.DetailHandler detailHandler)
	{
		support.setDetailHandler(detailHandler);
	}
	
	//
	// MasterDetailComponent implementations - End
	//
	
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
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEditable()
	{
		return editable;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setEditable(boolean editable)
	{
		if(this.editable != editable)
		{
			boolean oldValue = this.editable;
			this.editable = editable;
			firePropertyChange(EDITABLE_PROPERTY,oldValue,editable);
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isCellEditable(int row, int column)
	{
		
		return isEditable() && super.isCellEditable(row,column);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setModel(VirtualTable vt)
	{
		support.setModel(vt);
		// this.initPropertyIndependency();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setModel(VirtualTable vt, String columns, boolean queryData)
	{
		support.setModel(vt,columns,queryData);
		// this.initPropertyIndependency();
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setModel(final VirtualTable vt, final String columns, final boolean queryData,
			final boolean selectiveQuery)
	{
		this.support.setModel(vt,columns,queryData,selectiveQuery);
		// this.initPropertyIndependency();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setModel(VirtualTable vt, String columns, SELECT select, Object... params)
	{
		support.setModel(vt,columns,select,params);
		// this.initPropertyIndependency();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setVisibleRowCount(int visibleRows)
	{
		if(this.visibleRows != visibleRows)
		{
			int oldValue = this.visibleRows;
			this.visibleRows = visibleRows;
			firePropertyChange(XdevTable.VISIBLE_ROWS_PROPERTY,oldValue,visibleRows);
			revalidate();
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getVisibleRowCount()
	{
		return visibleRows;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Dimension getPreferredScrollableViewportSize()
	{
		Dimension d = super.getPreferredScrollableViewportSize();
		if(visibleRows > 0)
		{
			d.height = visibleRows * (getRowHeight() + getRowMargin());
		}
		
		return d;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setEvenBackground(Color evenBackground)
	{
		if(!ObjectUtils.equals(this.evenBackground,evenBackground))
		{
			Object oldValue = this.evenBackground;
			this.evenBackground = evenBackground;
			support.setAlternatingBackgroundColors();
			firePropertyChange(XdevTable.EVEN_BACKGROUND_PROPERTY,oldValue,evenBackground);
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Color getEvenBackground()
	{
		return evenBackground;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setOddBackground(Color oddBackground)
	{
		if(!ObjectUtils.equals(this.oddBackground,oddBackground))
		{
			Object oldValue = this.oddBackground;
			this.oddBackground = oddBackground;
			support.setAlternatingBackgroundColors();
			firePropertyChange(XdevTable.ODD_BACKGROUND_PROPERTY,oldValue,oddBackground);
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Color getOddBackground()
	{
		return oddBackground;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void exportCSV(Writer writer) throws IOException
	{
		support.exportCSV(writer);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void exportCSV(Writer writer, char delimiter) throws IOException
	{
		support.exportCSV(writer,delimiter);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void exportCSV(Writer writer, char delimiter, boolean withColumnNames)
			throws IOException
	{
		support.exportCSV(writer,delimiter,withColumnNames);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clear()
	{
		super.setModel(new DefaultTableModel());
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDefaultRenderer(TableCellRenderer renderer)
	{
		defaultRenderersByColumnClass.clear();
		setDefaultCellRenderer(renderer);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDefaultEditor(TableCellEditor editor)
	{
		defaultEditorsByColumnClass.clear();
		setDefaultEditor(Object.class,editor);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSomethingSelected()
	{
		return support.isSomethingSelected();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getSelectedModelRow() throws IndexOutOfBoundsException
	{
		return support.getSelectedModelRow();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int[] getSelectedModelRows() throws IndexOutOfBoundsException
	{
		return support.getSelectedModelRows();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public VirtualTableRow getSelectedVirtualTableRow()
	{
		return support.getSelectedVirtualTableRow();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public VirtualTableRow[] getSelectedVirtualTableRows()
	{
		return support.getSelectedVirtualTableRows();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSelectedVirtualTableRow(VirtualTableRow row)
	{
		support.setSelectedVirtualTableRow(row);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSelectedVirtualTableRows(VirtualTableRow[] rows)
	{
		support.setSelectedVirtualTableRows(rows);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSelectedModelRow(int row) throws IndexOutOfBoundsException,
			IllegalArgumentException
	{
		support.setSelectedModelRow(row);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSelectedModelRows(int[] rows) throws IndexOutOfBoundsException,
			IllegalArgumentException
	{
		support.setSelectedModelRows(rows);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSelectedModelRows(int start, int end)
	{
		support.setSelectedModelRows(start,end);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSelectedRows(int[] indices)
	{
		support.setSelectedRows(indices);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getRowAtPoint(int x, int y) throws IndexOutOfBoundsException
	{
		return support.getRowAtPoint(x,y);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getRowAtPoint(Point location) throws IndexOutOfBoundsException
	{
		return support.getRowAtPoint(location);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getColumnAtPoint(int x, int y)
	{
		return support.getColumnAtPoint(x,y);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getColumnAtPoint(Point location)
	{
		return support.getColumnAtPoint(location);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setColumnWidth(int index, int width)
	{
		support.setColumnWidth(index,width);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setColumnTitle(int index, String title)
	{
		support.setColumnTitle(index,title);
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see #savePersistentState()
	 */
	@Override
	public void loadPersistentState(String persistentState)
	{
		support.loadPersistentState(persistentState);
	}
	
	
	/**
	 * {@inheritDoc}.
	 * <p>
	 * Persisted properties:
	 * <ul>
	 * <li>column visibility</li>
	 * <li>column ordering</li>
	 * <li>column widths</li>
	 * <li>sorting order</li>
	 * <li>sorting direction</li>
	 * </ul>
	 * </p>
	 */
	@Override
	public String savePersistentState()
	{
		return support.savePersistentState();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPersistentId()
	{
		return support.getPersistentId();
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @return {@code true} if the GUI state should be persisted
	 */
	@Override
	public boolean isPersistenceEnabled()
	{
		return persistenceEnabled;
	}
	
	
	/**
	 * Sets the persistenceEnabled flag.
	 * 
	 * @param persistenceEnabled
	 *            the state for this instance
	 */
	public void setPersistenceEnabled(boolean persistenceEnabled)
	{
		this.persistenceEnabled = persistenceEnabled;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void ensureCellIsVisible(int rowIndex, int columnIndex)
	{
		this.support.ensureCellIsVisible(rowIndex,columnIndex);
		
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void ensureRowIsVisible(int rowIndex)
	{
		this.support.ensureRowIsVisible(rowIndex);
		
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void ensureColumnIsVisible(int columnIndex)
	{
		this.support.ensureColumnIsVisible(columnIndex);
		
	}
	
	
	/**
	 * En- or disables the display of the filter name in the column header.
	 * <p>
	 * Has only effect if autofiltering is enabled
	 * </p>
	 * 
	 * @param showFilterName
	 *            {@code true} for enabling
	 */
	public void setShowFilterName(boolean showFilterName)
	{
		support.setShowFilterName(showFilterName);
	}
	
	
	/**
	 * Is filter name currently displayed?
	 * 
	 * @return {@code} true for displayed
	 */
	public boolean isShowFilterName()
	{
		return support.isShowFilterName();
	}
	
	
	/**
	 * En- or disables the display of the filter name as tooltip.
	 * <p>
	 * Has only effect if autofiltering is enabled
	 * </p>
	 * 
	 * @param showFilterNameAsToolTip
	 *            {@code true} for enabling
	 */
	public void setShowFilterNameAsToolTip(boolean showFilterNameAsToolTip)
	{
		support.setShowFilterNameAsToolTip(showFilterNameAsToolTip);
	}
	
	
	/**
	 * Is filter name currently displayed as tooltip?
	 * 
	 * @return {@code} true for displayed
	 */
	public boolean isShowFilterNameAsToolTip()
	{
		return support.isShowFilterNameAsToolTip();
	}
	
	
	/**
	 * Sort column specified by columnName.
	 * 
	 * @param columnName
	 *            name of column to be sorted.
	 * @param ascending
	 *            whether the sort order is ascending or descending.
	 * @return this
	 */
	public XdevGroupTable sort(String columnName, boolean ascending)
	{
		support.sort(columnName,ascending);
		return this;
	}
	
	
	/**
	 * Groups columns.
	 * 
	 * @param columnNames
	 *            names of columns to group
	 * @return this
	 */
	// group can only be invoked after setting the model
	public XdevGroupTable group(String... columnNames)
	{
		
		DefaultGroupTableModel groupModel = getGroupTableModel();
		if(groupModel != null)
		{
			TableModel model = TableModelWrapperUtils.getActualTableModel(getModel(),
					VirtualTableWrapper.class);
			VirtualTableWrapper wrapper = null;
			if(model instanceof VirtualTableWrapper)
			{
				wrapper = (VirtualTableWrapper)model;
			}
			
			for(String columnName : columnNames)
			{
				boolean groupDefault = true;
				
				if(wrapper != null)
				{
					int[] indices = wrapper.getModelColumnIndices();
					for(int i = 0; i < indices.length; i++)
					{
						if(wrapper.getVirtualTable().getColumnAt(indices[i]).getName()
								.equals(columnName))
						{
							groupModel.addGroupColumn(i);
							groupDefault = false;
							break;
						}
					}
				}
				
				if(groupDefault)
				{
					try
					{
						groupModel.addGroupColumn(getColumnModel().getColumnIndex(columnName));
					}
					catch(IllegalArgumentException e)
					{
						// column not found
					}
				}
			}
			
			groupModel.groupAndRefresh();
		}
		
		return this;
	}
	
	
	/**
	 * Ungroup's all grouped columns.
	 */
	public void ungroup()
	{
		DefaultGroupTableModel groupModel = getGroupTableModel();
		if(groupModel != null)
		{
			groupModel.clearGroupColumns();
			groupModel.groupAndRefresh();
		}
	}
	
	
	/**
	 * Adds the specified listener to receive SortEvents from this GroupTable.
	 * 
	 * @param l
	 *            a {@link SortListener}
	 */
	public void addSortListener(SortListener l)
	{
		getSortableTableModel().addSortListener(l);
	}
	
	
	/**
	 * Removes the specified SortListener so that it no longer receives
	 * SortEvents from this GroupTable.
	 * 
	 * @param l
	 *            a {@link SortListener}
	 */
	public void removeSortListener(SortListener l)
	{
		getSortableTableModel().removeSortListener(l);
	}
	
	
	/**
	 * Add a {@link ListSelectionListener} to the list that's notified each time
	 * a change to the selection occurs.
	 * 
	 * @param listener
	 *            the ListSelectionListener
	 * 
	 * @see #removeListSelectionListener(ListSelectionListener)
	 */
	public void addListSelectionListener(ListSelectionListener listener)
	{
		getSelectionModel().addListSelectionListener(listener);
	}
	
	
	/**
	 * Remove a listener from the {@link ListSelectionModel} that's notified
	 * each time a change to the selection occurs.
	 * 
	 * @param listener
	 *            the {@link ListSelectionListener}
	 * 
	 * @see #addListSelectionListener(ListSelectionListener)
	 */
	public void removeListSelectionListener(ListSelectionListener listener)
	{
		getSelectionModel().removeListSelectionListener(listener);
	}
	
	
	/**
	 * Returns the current selection mode.
	 * 
	 * @return the current selection mode
	 * @see #setSelectionMode(int)
	 */
	public int getSelectionMode()
	{
		return getSelectionModel().getSelectionMode();
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.2
	 */
	@Override
	public void handlePopupRowSelection(int x, int y)
	{
		int row = this.getRowAtPoint(x,y);
		if(!this.getSelectionModel().isSelectedIndex(row) && row >= 0)
		{
			support.getRowSelectionHandler().selectRow(row);
		}
	}
	
}
