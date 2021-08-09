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
import java.awt.Font;
import java.util.StringTokenizer;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;

import xdev.db.sql.SELECT;
import xdev.ui.BeanProperty;
import xdev.ui.DefaultBeanCategories;
import xdev.ui.TableSupport;
import xdev.util.IntList;
import xdev.util.logging.LoggerFactory;
import xdev.util.logging.XdevLogger;
import xdev.vt.VirtualTable;

import com.jidesoft.grid.MultiTableModel;
import com.jidesoft.grid.SortableTable;
import com.jidesoft.grid.TableScrollPane;


/**
 * A {@code XdevTableScrollPane} provides a specialized display for tabular data
 * within three separared vertical regions.
 * <p>
 * These three regions are:
 * <ul>
 * <li>Header Columns: Those columns are displayed on the left side and are non
 * scrollable</li>
 * <li>Regular Columns: Those columns are displayed in the center and can be
 * scrolled independently of the other columns</li>
 * <li>Footer Columns: Those columns are displayed on the right side and are non
 * scrollable</li>
 * </ul>
 * </p>
 * <p>
 * {@link XdevTableScrollPane} uses a special type of model, called
 * {@link MultiTableModel}. {@code MultiTableModel} specifies the method
 * {@link MultiTableModel#getColumnType(int)}. Overwritten implementations of
 * this method can be used to map the columns of the model on the three
 * different regions of {@code XdevTableScrollPane}.
 * </p>
 * <p>
 * It supports an optional second {@code MultiTableModel} In addition to its
 * main {@code MultiTableModel}. This second model is then used to display a
 * footer column for the regular region. Therefore the number of columns in the
 * footer table model has to match the number of column in the regular region in
 * order for this to work.
 * </p>
 * <p>
 * {@link XdevMultiTableModel} can be used to adapt {@link VirtualTable} objects
 * for this component.
 * </p>
 * 
 * @author XDEV Software
 * @see XdevMultiTableModel
 */
public class XdevTableScrollPane extends TableScrollPane
{
	
	/**
	 * Logger instance for this class.
	 */
	private static final XdevLogger	log					= LoggerFactory
			.getLogger(XdevTableScrollPane.class);
	
	/**
	 * serialVersionUID.
	 */
	private static final long		serialVersionUID	= 1L;
	
	/**
	 * flag for setting the editability of the contained tables.
	 */
	private boolean					editable;
	
	/**
	 * the wrapped {@link JTable}s font.
	 */
	private Font					wrappedTableFont;
	
	/**
	 * the wrapped {@link JTable}s font color.
	 */
	private Color					wrappedTableFontColor;
	
	/**
	 * the whole components tool tip text.
	 */
	private String					toolTipText;
	
	
	/**
	 * Create a new instance of {@link XdevTableScrollPane}.
	 */
	public XdevTableScrollPane()
	{
		super();
	}
	
	
	/**
	 * Create a new instance of {@link XdevTableScrollPane}.
	 * 
	 * @param tableModel
	 *            main table model for this instance.
	 * @param sortable
	 *            flag indicating if sorting is enabled
	 */
	public XdevTableScrollPane(MultiTableModel tableModel, boolean sortable)
	{
		super(tableModel,sortable);
	}
	
	
	/**
	 * Create a new instance of {@link XdevTableScrollPane}.
	 * 
	 * @param tableModel
	 *            main table model for this instance.
	 * @param footerTableModel
	 *            the footer table Model for this instance
	 * @param sortable
	 *            flag indicating if sorting is enabled
	 * @param sync
	 *            will table by synced (usually set to true)
	 */
	public XdevTableScrollPane(MultiTableModel tableModel, MultiTableModel footerTableModel,
			boolean sortable, boolean sync)
	{
		super(tableModel,footerTableModel,sortable,sync);
	}
	
	
	/**
	 * Create a new instance of {@link XdevTableScrollPane}.
	 * 
	 * @param tableModel
	 *            main table model for this instance.
	 * @param footerTableModel
	 *            the footer table Model for this instance
	 * @param sortable
	 *            flag indicating if sorting is enabled
	 */
	public XdevTableScrollPane(MultiTableModel tableModel, MultiTableModel footerTableModel,
			boolean sortable)
	{
		super(tableModel,footerTableModel,sortable);
	}
	
	
	/**
	 * Create a new instance of {@link XdevTableScrollPane}.
	 * 
	 * @param tableModel
	 *            main table model for this instance.
	 * @param footerTableModel
	 *            the footer table Model for this instance
	 */
	public XdevTableScrollPane(MultiTableModel tableModel, MultiTableModel footerTableModel)
	{
		super(tableModel,footerTableModel);
	}
	
	
	/**
	 * Create a new instance of {@link XdevTableScrollPane}.
	 * 
	 * @param tableModel
	 *            main table model for this instance.
	 */
	public XdevTableScrollPane(MultiTableModel tableModel)
	{
		super(tableModel);
	}
	
	
	/**
	 * Sets a model for this {@link XdevTableScrollPane} based on a
	 * {@link VirtualTable} and some additional configuration.
	 * 
	 * @param vt
	 *            the {@link VirtualTable} to base the model on.
	 * @param headerColumns
	 *            a comma separated list of column names for configuring header
	 *            columns
	 * @param footerColumns
	 *            a comma separated list of column names for configuring footer
	 *            columns
	 * @param queryData
	 *            if {@code true} performs a query on the
	 */
	public void setModel(VirtualTable vt, String headerColumns, String footerColumns,
			boolean queryData)
	{
		setModel(vt,"*",headerColumns,footerColumns,queryData);
	}
	
	
	/**
	 * Sets a model for this {@link XdevTableScrollPane} based on a
	 * {@link VirtualTable} and some additional configuration.
	 * 
	 * @param vt
	 *            the {@link VirtualTable} to base the model on.
	 * @param columns
	 *            a comma separated list of column names for configuring visible
	 *            columns in the model
	 * @param headerColumns
	 *            a comma separated list of column names for configuring header
	 *            columns
	 * @param footerColumns
	 *            a comma separated list of column names for configuring footer
	 *            columns
	 * @param queryData
	 *            if {@code true} performs a query on the
	 */
	public void setModel(VirtualTable vt, String columns, String headerColumns,
			String footerColumns, boolean queryData)
	{
		
	}
	
	
	/**
	 * Sets a model for this {@link XdevTableScrollPane} based on a
	 * {@link VirtualTable} and some additional configuration.
	 * 
	 * @param vt
	 *            the {@link VirtualTable} to base the model on.
	 * @param columns
	 *            a comma separated list of column names for configuring visible
	 *            columns in the model
	 * @param headerColumns
	 *            a comma separated list of column names for configuring header
	 *            columns
	 * @param footerColumns
	 *            a comma separated list of column names for configuring footer
	 *            columns
	 * @param queryData
	 *            if {@code true} performs a query on the
	 * @param selectiveQuery
	 *            if {@code true}, only the display <code>columns</code> are
	 *            queried
	 * @since 5.0
	 */
	public void setModel(VirtualTable vt, String columns, String headerColumns,
			String footerColumns, boolean queryData, final boolean selectiveQuery)
	{
		SELECT select = null;
		if(queryData)
		{
			select = TableSupport.createSelect(vt,selectiveQuery,columns,headerColumns,
					footerColumns);
		}
		
		setModel(vt,columns,headerColumns,footerColumns,select);
	}
	
	
	/**
	 * Sets a model for this {@link XdevTableScrollPane} based on a
	 * {@link VirtualTable} and some additional configuration.
	 * 
	 * @param vt
	 *            the {@link VirtualTable} to base the model on.
	 * @param columns
	 *            a comma separated list of column names for configuring visible
	 *            columns in the model
	 * @param headerColumns
	 *            a comma separated list of column names for configuring header
	 *            columns
	 * @param footerColumns
	 *            a comma separated list of column names for configuring footer
	 *            columns
	 * @param select
	 *            a custom {@link SELECT} used for querying the
	 *            {@link VirtualTable}
	 * @param params
	 *            a optional number of parameters for the custom select
	 */
	public void setModel(VirtualTable vt, String columns, String headerColumns,
			String footerColumns, SELECT select, Object... params)
	{
		if(select != null)
		{
			try
			{
				vt.queryAndFill(select,params);
			}
			catch(Exception e)
			{
				log.error(e);
			}
		}
		
		setModel(new XdevMultiTableModel(vt,getColumnIndices(vt,columns),
				getColumnIndices(vt,headerColumns),getColumnIndices(vt,footerColumns)));
		
		// refresh enabled state of child tables
		
		this.initPropertyIndependency();
	}
	
	
	/**
	 * Retrieves column indices of a specified {@link VirtualTable} and a list
	 * of column names.
	 * 
	 * @param vt
	 *            a {@link VirtualTable}
	 * @param columns
	 *            a comma separated list of column names of the {@code vt}.
	 * @return an array of column indices
	 */
	private int[] getColumnIndices(VirtualTable vt, String columns)
	{
		if(columns == null)
		{
			return null;
		}
		
		columns = columns.trim();
		
		if(columns.length() == 0)
		{
			return null;
		}
		
		if(columns.equals("*"))
		{
			return vt.getVisibleColumnIndices();
		}
		
		IntList indices = new IntList();
		
		StringTokenizer st = new StringTokenizer(columns,",");
		while(st.hasMoreTokens())
		{
			int index = vt.getColumnIndex(st.nextToken().trim());
			if(index >= 0)
			{
				indices.add(index);
			}
		}
		
		return indices.toArray();
	}
	
	
	/**
	 * Initializes a {@code XdevTableScrollPane} with a {@code MultiTableModel}.
	 * 
	 * @param tableModel
	 *            the main table model to set
	 */
	public void setModel(MultiTableModel tableModel)
	{
		this.setTableModel(tableModel);
	}
	
	
	/**
	 * * Initializes a {@code XdevTableScrollPane} with a
	 * {@code MultiTableModel}s for main and footer area.
	 * 
	 * @param tableModel
	 *            the main table model to set
	 * @param footerTableModel
	 *            the footer table model to set
	 */
	public void setModel(MultiTableModel tableModel, MultiTableModel footerTableModel)
	{
		this.setTableModel(tableModel);
		this.setFooterTableModel(footerTableModel);
	}
	
	
	/**
	 * Add a {@link ListSelectionListener} to the main table that's notified
	 * each time a change to the selection occurs.
	 * 
	 * @param listener
	 *            the ListSelectionListener
	 * 
	 * @see #removeListSelectionListener(ListSelectionListener)
	 */
	public void addListSelectionListener(ListSelectionListener listener)
	{
		this.getMainTable().getSelectionModel().addListSelectionListener(listener);
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
		this.getMainTable().getSelectionModel().addListSelectionListener(listener);
	}
	
	
	/**
	 * Are the tables in the {@link XdevTableScrollPane} editable?
	 * 
	 * @return {@code true} if tables are editable.
	 */
	public boolean isEditable()
	{
		return editable;
	}
	
	
	/**
	 * Sets the editability of the tables in {@link XdevTableScrollPane}
	 * (defaults to {@code false}).
	 * 
	 * @param editable
	 *            if set to {@code true} tables are editable
	 */
	public void setEditable(boolean editable)
	{
		this.editable = editable;
	}
	
	
	/**
	 * Overridden method to allow setting the editability via property.
	 * 
	 * @param model
	 *            the TableModel to set for the table to create
	 * @param sortable
	 *            if {@code true} the created table will be sortable
	 * @return a newly created table.
	 */
	@Override
	protected JTable createTable(TableModel model, final boolean sortable)
	{
		@SuppressWarnings("serial")
		SortableTable table = new SortableTable(model)
		{
			@Override
			public boolean isCellEditable(int rowIndex, int vColIndex)
			{
				return XdevTableScrollPane.this.isEditable()
						&& super.isCellEditable(rowIndex,vColIndex);
			}
		};
		table.setSortable(sortable);
		
		return table;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setEnabled(boolean enabled)
	{
		super.setEnabled(enabled);
		JTable[] tables = this.getAllChildTables();
		for(int i = 0; i < tables.length; i++)
		{
			tables[i].setEnabled(enabled);
		}
		
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setFont(Font paramFont)
	{
		this.wrappedTableFont = paramFont;
		for(int i = 0; i < this.getAllChildTables().length; i++)
		{
			this.getAllChildTables()[i].setFont(paramFont);
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setForeground(Color fontColor)
	{
		this.wrappedTableFontColor = fontColor;
		for(int i = 0; i < this.getAllChildTables().length; i++)
		{
			this.getAllChildTables()[i].setForeground(fontColor);
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setToolTipText(String text)
	{
		this.toolTipText = text;
		super.setToolTipText(text);
		for(int i = 0; i < this.getAllChildTables().length; i++)
		{
			this.getAllChildTables()[i].setToolTipText(text);
		}
	}
	
	
	/**
	 * re init dependencies.
	 */
	private void initPropertyIndependency()
	{
		this.setToolTipText(this.toolTipText);
		this.setForeground(this.wrappedTableFontColor);
		this.setFont(this.wrappedTableFont);
		this.setEnabled(isEnabled());
	}
	
	
	// ////////////////////////////////////////////////////////////
	// / Following properties are hidden on development time ///
	// ///////////////////////////////////////////////////////////
	
	/**
	 * {@inheritDoc}
	 * <p>
	 * <b>This property does alter the layout on JComponent level so nothing is
	 * going to be updated on this component level.</b>
	 * </p>
	 */
	@BeanProperty(category = DefaultBeanCategories.MISC)
	@Override
	public void setBackground(Color bg)
	{
		super.setBackground(bg);
	}
	
	
	/**
	 * {@inheritDoc}
	 * <p>
	 * <b>This property does alter the layout on JComponent level so nothing is
	 * going to be updated on this component level.</b>
	 * </p>
	 */
	@BeanProperty(category = DefaultBeanCategories.MISC)
	@Override
	public void setOpaque(boolean arg0)
	{
		super.setOpaque(arg0);
	}
}
