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

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import xdev.db.Operator;
import xdev.db.sql.Condition;
import xdev.db.sql.SELECT;
import xdev.ui.persistence.Persistable;
import xdev.ui.table.ExtendedTable;
import xdev.ui.table.ExtendedTableSupport;
import xdev.ui.table.TableRowSelectionHandler;
import xdev.ui.table.XdevTableModelWrapper;
import xdev.util.ArrayUtils;
import xdev.util.ObjectUtils;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTable.VirtualTableRow;
import xdev.vt.VirtualTableModel;
import xdev.vt.VirtualTableWrapper;

import com.jidesoft.grid.SortListener;
import com.jidesoft.grid.TableHeaderPopupMenuInstaller;
import com.jidesoft.pivot.AggregateTable;
import com.jidesoft.pivot.AggregateTablePopupMenuCustomizer;
import com.jidesoft.pivot.IPivotDataModel;
import com.jidesoft.pivot.PivotConstants;
import com.jidesoft.pivot.PivotDataModel;
import com.jidesoft.pivot.PivotField;


/**
 * This is a extended Version of Jide's {@link AggregateTable}.
 * <p>
 * It provides all features of the {@code AggregateTable} and provides
 * integration support for XDEV's {@code VirtualTable}.
 * </p>
 * 
 * @author XDEV Software
 * 
 */
public class XdevAggregateTable extends AggregateTable implements
		ExtendedTable<XdevAggregateTable>, XdevFocusCycleComponent, VirtualTableEditor,
		Persistable, PivotConstants, PopupRowSelectionHandler
{
	/**
	 * serialVersionUID.
	 */
	private static final long	serialVersionUID	= 1L;
	
	
	
	/**
	 * A specialized support class for {@link XdevAggregateTable} instances.
	 * 
	 * @author XDEV Software
	 * 
	 */
	private static class Support extends ExtendedTableSupport<XdevAggregateTable>
	{
		/**
		 * Creates a new instance.
		 * 
		 * @param table
		 *            the table to support.
		 * @param mdc
		 *            the master detail component for this support class.
		 */
		Support(XdevAggregateTable table)
		{
			super(table);
		}
		
		
		/**
		 * {@inheritDoc}
		 * <p>
		 * This override add a {@link AggregateTablePopupMenuCustomizer} to the
		 * popup header menu.
		 * </p>
		 */
		@Override
		protected void customizeTableHeaderPopupMenuInstaller(
				TableHeaderPopupMenuInstaller installer)
		{
			super.customizeTableHeaderPopupMenuInstaller(installer);
			installer.addTableHeaderPopupMenuCustomizer(new AggregateTablePopupMenuCustomizer());
		}
	}
	
	/**
	 * Provides functionality common to all CS table types.
	 */
	protected final Support			support				= new Support(this);
	
	/**
	 * This class provides functionality common to all CS table types.
	 */
	protected XdevTableModelWrapper	modelWrapper;
	
	/**
	 * flag for configuring, if table is editable.
	 */
	private boolean					editable			= false;
	
	/**
	 * an integer specifying the preferred number of rows to display without
	 * requiring scrolling.
	 */
	private int						visibleRows			= 8;
	
	/**
	 * Color of table rows with even index.
	 */
	private Color					evenBackground		= null;
	/**
	 * Color of table rows with odd index.
	 */
	private Color					oddBackground		= null;
	
	/**
	 * Should the gui state be persisted? Defaults to {@code true}.
	 */
	private boolean					persistenceEnabled	= true;
	
	/**
	 * flag for showing a grand total row.
	 */
	private boolean					showGrandTotal		= false;
	
	/**
	 * flag for showing summary rows.
	 */
	private boolean					showSummary			= false;
	
	/**
	 * flag for activating summary mode.
	 */
	private boolean					summaryMode			= false;
	
	/**
	 * flag for showing summary rows only.
	 */
	private boolean					summaryOnly			= false;
	
	/**
	 * 
	 */
	private boolean					sortAutomatically	= false;
	
	
	/**
	 * @see JTable#JTable()
	 */
	public XdevAggregateTable()
	{
		super();
		init();
	}
	
	
	/**
	 * 
	 * @param tableModel
	 *            the table model.
	 * @see JTable#JTable(TableModel)
	 */
	public XdevAggregateTable(TableModel tableModel)
	{
		super(tableModel);
		init();
	}
	
	
	/**
	 * Creates an XdevAggregateTable from the specified
	 * {@code VirtualTableModel}.
	 * 
	 * @param virtualTableModel
	 *            the {@code TableModel} to use for creating the
	 *            {@code XdevAggregateTable}
	 * @param aggregateColumnIndices
	 *            the column indices used for aggregation (can be empty)
	 */
	public XdevAggregateTable(VirtualTableModel virtualTableModel, int... aggregateColumnIndices)
	{
		this(virtualTableModel);
		this.aggregate(aggregateColumnIndices);
		init();
	}
	
	
	/**
	 * Creates an XdevAggregateTable from the specified
	 * {@code VirtualTableModel}.
	 * 
	 * @param virtualTableModel
	 *            the {@code TableModel} to use for creating the
	 *            {@code XdevAggregateTable}
	 * @param aggregateColumnNames
	 *            the column names used for aggregation (can be empty)
	 */
	public XdevAggregateTable(VirtualTableModel virtualTableModel, String... aggregateColumnNames)
	{
		this(virtualTableModel);
		this.aggregate(aggregateColumnNames);
		init();
	}
	
	
	private void init()
	{
		support.initRenderersAndEditors();
		
		// reordering of aggregated columns doesn't work
		this.getTableHeader().setReorderingAllowed(false);
		
		setFillsViewportHeight(true);
		setFillsBottom(true);
		setFillsRight(true);
		setFillsSelection(true);
		
		setNonContiguousCellSelection(false);
		
		// disable aggregate table row/column selection behavior
		setCellSelectionEnabled(false);
		
		// use default table row selection behavior
		setRowSelectionAllowed(true);
		
		support.setRowSelectionHandler(new AggregateTableRowSelectionHandler(this));
	}
	
	
	
	private static class AggregateTableRowSelectionHandler extends TableRowSelectionHandler
	{
		AggregateTableRowSelectionHandler(JTable table)
		{
			super(table);
		}
		
		
		@Override
		public void selectRow(int row)
		{
			super.selectRow(row);
			selectAllColumns();
		}
		
		
		@Override
		public void selectRowInterval(int row0, int row1)
		{
			super.selectRowInterval(row0,row1);
			selectAllColumns();
		}
		
		
		@Override
		public void selectRows(int... rows)
		{
			super.selectRows(rows);
			selectAllColumns();
		}
		
		
		void selectAllColumns()
		{
			JTable table = getTable();
			table.getColumnModel().getSelectionModel()
					.setSelectionInterval(0,table.getColumnCount() - 1);
		}
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
		
		initPropertyIndependency();
		
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
		this.setShowGrandTotal(this.showGrandTotal);
		this.setShowSummary(this.showSummary);
		this.setShowSummaryOnly(this.summaryOnly);
		this.setSummaryMode(this.summaryMode);
		this.setSortAggregatedColumns(this.sortAutomatically);
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
	
	
	//
	// Auto Filtering
	//
	
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
	
	
	//
	// Delegations to AggregatedTableModel
	//
	
	/**
	 * Gets the showSummary flag.
	 * 
	 * @return true if summary is shown. Otherwise false.
	 */
	public boolean isShowGrandTotal()
	{
		return getAggregateTableModel().isShowGrandTotal();
	}
	
	
	/**
	 * Sets showGrandTotal flag. If true, the last row will be the grand total
	 * row.
	 * 
	 * @param show
	 *            true or false.
	 */
	public void setShowGrandTotal(boolean show)
	{
		this.showGrandTotal = show;
		getAggregateTableModel().setShowGrandTotal(show);
	}
	
	
	/**
	 * Gets the showSummary flag.
	 * 
	 * @return true if summary is shown. Otherwise false.
	 */
	public boolean isShowSummary()
	{
		return getAggregateTableModel().isShowSummary();
	}
	
	
	/**
	 * Sets showSummary flag. If true, there will be a summary row before the
	 * aggregated rows. The value of this summary rows are depending on the
	 * corresponding PivotField's getSummaryType.
	 * 
	 * @param show
	 *            true or false.
	 */
	public void setShowSummary(boolean show)
	{
		this.showSummary = show;
		getAggregateTableModel().setShowSummary(show);
	}
	
	
	/**
	 * Gets the summaryMode flag.
	 * 
	 * @return true if summary is shown when the row is collapsed. Otherwise the
	 *         first row will be displayed.
	 */
	public boolean isSummaryMode()
	{
		return getAggregateTableModel().isSummaryMode();
	}
	
	
	/**
	 * Sets summaryMode flag. If false, only the first row will be displayed
	 * when the value is collapsed. If true, the collapsed row will display the
	 * summary of all the rows. This method basically calls to
	 * {@link PivotDataModel#setSummaryMode(boolean)}.
	 * 
	 * @param show
	 *            true or false.
	 */
	public void setSummaryMode(boolean show)
	{
		this.summaryMode = show;
		getAggregateTableModel().setSummaryMode(show);
	}
	
	
	/**
	 * Sets showSummaryOnly flag. If true, the last aggregated column will not
	 * be expandable.
	 * 
	 * @param showSummaryOnly
	 *            true or false.
	 */
	public void setShowSummaryOnly(boolean showSummaryOnly)
	{
		this.summaryOnly = showSummaryOnly;
		getAggregateTableModel().setShowSummaryOnly(showSummaryOnly);
	}
	
	
	/**
	 * Gets the flag, for showing the summary only.
	 * 
	 * @return true, of show summary only is active, otherwise false.
	 */
	public boolean isShowSummaryOnly()
	{
		return getAggregateTableModel().isShowSummaryOnly();
	}
	
	
	/**
	 * Sets the flag if the aggregated columns should be sorted automatically.
	 * If true, the aggregated columns will be sorted automatically.
	 * 
	 * @param sorted
	 *            true or false.
	 */
	public void setSortAggregatedColumns(boolean sorted)
	{
		this.sortAutomatically = sorted;
		getAggregateTableModel().setSortAggregatedColumns(sorted);
	}
	
	
	/**
	 * Gets the flag if the aggregated columns should be sorted automatically.
	 * 
	 * @return true if the aggregated columns is sorted automatically. Otherwise
	 *         false.
	 */
	public boolean isSortAggregatedColumns()
	{
		return getAggregateTableModel().isSortAggregatedColumns();
	}
	
	
	/**
	 * Checks if the table has been aggregated.
	 * 
	 * @return {@code true} or {@code false}
	 */
	public boolean isAggregated()
	{
		return getAggregateTableModel().isAggregated();
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
	
	
	@Override
	/**
	 * {@inheritDoc}
	 */
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
	public XdevAggregateTable sort(String columnName, boolean ascending)
	{
		support.sort(columnName,ascending);
		return this;
	}
	
	
	/**
	 * Aggregates the specified column and adds the specified sum rows.
	 * <p>
	 * <b> Clears the row selection through a structure change. </b>
	 * </p>
	 * 
	 * @param columnName
	 *            the column to aggregate
	 * @param summaries
	 *            the summaries to add
	 * @return this
	 */
	public XdevAggregateTable aggregate(String columnName, int... summaries)
	{
		int index = ExtendedTableSupport.getColumnIndex(this,columnName);
		if(index != -1)
		{
			if(summaries != null && summaries.length > 0)
			{
				PivotField field = getAggregateTableModel().getField(index);
				field.setSubtotalType(PivotConstants.SUBTOTAL_CUSTOM);
				field.setCustomSubtotals(summaries);
			}
			int[] indices = getAggregateTableModel().getAggregatedColumns();
			if(!ArrayUtils.contains(indices,index))
			{
				aggregate(ArrayUtils.concat(indices,index));
			}
		}
		
		return this;
	}
	
	
	/**
	 * Adds a grand total row with the specified summary.
	 * 
	 * @param summary
	 *            the summary for the grand total row
	 */
	public void aggregateGrandTotal(int summary)
	{
		IPivotDataModel pivotModel = getAggregateTableModel().getPivotDataModel();
		pivotModel.setShowGrandTotalForRow(true);
		for(PivotField field : pivotModel.getDataFields())
		{
			field.setGrandTotalSummaryType(summary);
		}
		aggregate();
	}
	
	
	/**
	 * Adds the specified listener to receive SortEvents from this
	 * AggregateTable.
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
	 * SortEvents from this AggregateTable.
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
	 */
	@Override
	public void setSelectionMode(int selectionMode)
	{
		this.setNonContiguousCellSelection(false);
		this.getSelectionModel().setSelectionMode(selectionMode);
		this.getColumnModel().getSelectionModel().setSelectionMode(selectionMode);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handlePopupRowSelection(int x, int y)
	{
		int row = this.getRowAtPoint(x,y);
		if(!this.getSelectionModel().isSelectedIndex(row) && row >= 0)
		{
			this.support.getRowSelectionHandler().selectRow(row);
		}
	}
	
}
