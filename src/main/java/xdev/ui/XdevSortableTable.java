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
import xdev.ui.paging.LazyLoadable;
import xdev.ui.paging.PageControl;
import xdev.ui.paging.Pageable;
import xdev.ui.persistence.Persistable;
import xdev.ui.table.ExtendedTable;
import xdev.ui.table.ExtendedTableSupport;
import xdev.ui.table.XdevTableModelWrapper;
import xdev.ui.table.XdevTreeTableModel;
import xdev.util.ObjectUtils;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTable.VirtualTableRow;
import xdev.vt.VirtualTableModel;
import xdev.vt.VirtualTableWrapper;

import com.jidesoft.grid.SortListener;
import com.jidesoft.grid.SortableTable;


/**
 * This is a extended Version of Jide's {@link SortableTable}.
 * <p>
 * It provides all features of the {@code SortableTable} and provides support
 * for XDev's {@code VirtualTable}.
 * </p>
 * 
 * @author XDEV Software
 * 
 */
public class XdevSortableTable extends SortableTable implements ExtendedTable<XdevSortableTable>,
		XdevFocusCycleComponent, VirtualTableEditor, Persistable, PopupRowSelectionHandler,
		Pageable, LazyLoadable
{
	
	/**
	 * serialVersionUID.
	 */
	private static final long								serialVersionUID	= 1L;
	
	/**
	 * Provides functionality common to all CS table types.
	 */
	protected final ExtendedTableSupport<XdevSortableTable>	support				= new ExtendedTableSupport<XdevSortableTable>(
																						this);
	
	/**
	 * This class provides functionality common to all CS table types.
	 */
	protected XdevTableModelWrapper							modelWrapper;
	
	/**
	 * flag for configuring, if table is editable.
	 */
	private boolean											editable			= false;
	
	/**
	 * an integer specifying the preferred number of rows to display without
	 * requiring scrolling.
	 */
	private int												visibleRows			= 8;
	
	/**
	 * Color of table rows with even index.
	 */
	private Color											evenBackground		= null;
	/**
	 * Color of table rows with odd index.
	 */
	private Color											oddBackground		= null;
	
	/**
	 * Should the gui state be persisted? Defaults to {@code true}.
	 */
	private boolean											persistenceEnabled	= true;
	
	/**
	 * flag to set the vt-cell style.
	 */
	private boolean											cellStyle			= false;
	
	
	/**
	 * @see SortableTable
	 */
	public XdevSortableTable()
	{
		super();
		init();
	}
	
	
	/**
	 * 
	 * 
	 * @param numRows
	 *            the number of rows the table holds
	 * @param numColumns
	 *            the number of columns the table holds
	 * @see JTable#JTable(int, int)
	 * 
	 */
	public XdevSortableTable(int numRows, int numColumns)
	{
		super(numRows,numColumns);
		init();
	}
	
	
	/**
	 * @param rowData
	 *            the data for the new table
	 * @param columnNames
	 *            names of each column
	 * 
	 * @see JTable#JTable(Object[][], Object[])
	 */
	public XdevSortableTable(Object[][] rowData, Object[] columnNames)
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
	 */
	public XdevSortableTable(TableModel dm, TableColumnModel cm, ListSelectionModel sm)
	{
		super(dm,cm,sm);
		init();
	}
	
	
	/**
	 * @param dm
	 *            the data model for the table
	 * @param cm
	 *            the column model for the table
	 * @see JTable#JTable(TableModel, TableColumnModel)
	 */
	public XdevSortableTable(TableModel dm, TableColumnModel cm)
	{
		super(dm,cm);
		init();
	}
	
	
	/**
	 * @param model
	 *            any table model.
	 * @see JTable#JTable(TableModel)
	 */
	public XdevSortableTable(TableModel model)
	{
		super(model);
		init();
	}
	
	
	/**
	 * @param rowData
	 *            the data for the new table
	 * @param columnNames
	 *            names of each column
	 * @see JTable#JTable(Vector, Vector)
	 */
	public XdevSortableTable(Vector<?> rowData, Vector<?> columnNames)
	{
		// (19.10.2020 TB)FIXME: 
		super((Vector<? extends Vector>)rowData,columnNames);
		init();
	}
	
	
	private void init()
	{
		support.initRenderersAndEditors();
		setFillsViewportHeight(true);
		setFillsBottom(true);
		setFillsRight(true);
		setFillsSelection(true);
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
	}
	
	
	/**
	 * re calls the methods which have dependences on the set model.
	 */
	private void initPropertyIndependency()
	{
		this.setCellStyleOn(this.cellStyle);
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
	 * Adds the specified listener to receive SortEvents from this
	 * SortableTable.
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
	 * SortEvents from this SortableTable.
	 * 
	 * @param l
	 *            a {@link SortListener}
	 */
	public void removeSortListener(SortListener l)
	{
		getSortableTableModel().removeSortListener(l);
	}
	
	
	/**
	 * Configures, if cell style is enabled.
	 * 
	 * @param isCellStyleOn
	 *            if set to {@code true}, cell style is enables
	 */
	public void setCellStyleOn(boolean isCellStyleOn)
	{
		this.cellStyle = isCellStyleOn;
		if(modelWrapper != null)
		{
			modelWrapper.setCellStyleOn(isCellStyleOn);
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
	 * @throws IllegalStateException
	 *             if the supported table is not based on a VirtualTableModel or
	 *             if a new VirtualTable could not be created.
	 */
	public VirtualTable createSubsettedVirtualTable() throws IllegalStateException
	{
		return support.createSubsettedVirtualTable();
	}
	
	
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
	 * 
	 * @see #savePersistentState()
	 */
	@Override
	public void loadPersistentState(String persistentState)
	{
		this.support.loadPersistentState(persistentState);
	}
	
	
	/**
	 * {@inheritDoc}.
	 * <p>
	 * Persisted Properties:
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
		return this.support.savePersistentState();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPersistentId()
	{
		return this.support.getPersistentId();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setModel(VirtualTable vt)
	{
		support.setModel(vt);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setModel(VirtualTable vt, String columns, boolean queryData)
	{
		support.setModel(vt,columns,queryData);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setModel(final VirtualTable vt, final String columns, final boolean queryData,
			final boolean selectiveQuery)
	{
		this.support.setModel(vt,columns,queryData,selectiveQuery);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setModel(VirtualTable vt, String columns, SELECT select, Object... params)
	{
		support.setModel(vt,columns,select,params);
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 4.0
	 */
	@Override
	public void setPagingEnabled(boolean b)
	{
		support.setPagingEnabled(b);
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 4.0
	 */
	@Override
	public boolean isPagingEnabled()
	{
		return support.isPagingEnabled();
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 4.0
	 */
	@Override
	public PageControl getPageControl()
	{
		return support.getPageControl();
	}
	
	
	/**
	 * Enabled or disables the lazy loading feature.
	 * <p>
	 * If lazy loading is enabled only the displayed rows are loaded.
	 * 
	 * @since 4.0
	 */
	public void setLazyLoadingEnabled(boolean lazyLoadingEnabled)
	{
		support.setLazyLoadingEnabled(lazyLoadingEnabled);
	}
	
	
	/**
	 * @return if the lazy loading feature is enabled
	 * 
	 * @since 4.0
	 */
	public boolean isLazyLoadingEnabled()
	{
		return support.isLazyLoadingEnabled();
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
	public XdevSortableTable sort(String columnName, boolean ascending)
	{
		support.sort(columnName,ascending);
		return this;
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
