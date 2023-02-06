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
package xdev.ui.ganttchart.template;


import java.awt.BorderLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import xdev.db.DBException;
import xdev.ui.XdevMenu;
import xdev.ui.ganttchart.DateGanttVirtualTableSupport;
import xdev.ui.ganttchart.GanttChartVirtualTableRelationSupport;
import xdev.ui.ganttchart.GanttModelUIInformation;
import xdev.ui.ganttchart.UpdateableGanttEntry;
import xdev.ui.ganttchart.action.GanttDateScaleAreaUpdater;
import xdev.ui.ganttchart.action.GanttRelationManagementStrategy;
import xdev.ui.ganttchart.action.GanttTemplateRemoveKeyAdapter;
import xdev.ui.ganttchart.action.UpdateDateRelationManagementStrategy;
import xdev.ui.ganttchart.action.VirtualTableUpdateDateGanttChartPopupMenuCustomizer;
import xdev.ui.ganttchart.action.XdevGanttEntryRelationListener;
import xdev.ui.ganttchart.action.XdevRelationGanttChartPopupMenuCustomizer;
import xdev.ui.ganttchart.editor.XdevGanttChartPopupMenuInstaller;
import xdev.ui.ganttchart.editor.XdevGanttTemplateToolBar;
import xdev.ui.ganttchart.model.GanttPersistence;
import xdev.ui.ganttchart.model.VirtualTableGanttPersistence;
import xdev.ui.ganttchart.model.VirtualTableGanttRelationPersistence;
import xdev.ui.ganttchart.model.XdevGanttEntryVTMappings;
import xdev.ui.ganttchart.utils.DateGanttEntryColumnType;
import xdev.ui.ganttchart.utils.GanttRelationColumnType;
import xdev.ui.ganttchart.utils.VTDateGanttMappingConverter;
import xdev.ui.ganttchart.utils.VTGanttRelationMappingConverter;
import xdev.ui.ganttchart.utils.XdevGanttUtils;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTable.VirtualTableRow;
import xdev.vt.VirtualTableColumn;
import xdev.vt.VirtualTableException;

import com.jidesoft.gantt.DateGanttChartPane;
import com.jidesoft.gantt.DefaultGanttModel;
import com.jidesoft.gantt.GanttChart;
import com.jidesoft.gantt.GanttChartPane;
import com.jidesoft.gantt.GanttChartPopupMenuCustomizer;
import com.jidesoft.gantt.GanttEntryRelationModel;
import com.jidesoft.gantt.GanttModel;
import com.jidesoft.grid.AutoFilterTableHeader;
import com.jidesoft.grid.TableUtils;
import com.jidesoft.grid.TreeTable;
import com.jidesoft.scale.DatePeriod;
import com.jidesoft.scale.DateScaleModel;
import com.jidesoft.scale.ScaleModel;


/**
 * The {@link XdevGanttTemplate} which is a concrete implementation of
 * {@link RelationalGanttTemplate} provides a UI representation for project
 * data.
 * 
 * <p>
 * Use {@link AbstractGanttTemplate} for a fully customizable {@link GanttChart}
 * .
 * </p>
 * 
 * <p>
 * The data is displayed in a {@link GanttChart} on the right hand side, and a
 * {@link TreeTable} for more detailed information, at the left hand side.
 * </p>
 * 
 * <p>
 * Several interaction interfaces allow to update the data on the UI layer at
 * runtime.
 * 
 * It is also possible to disable the <code>GanttChartTemplates</code>
 * components at runtime, a common use case would be to disable the
 * <code>TreeTable</code> for a better general view.
 * </p>
 * 
 * 
 * @see GanttEntryEditor
 * @see GanttChartTemplateTablePopup
 * @see ComponentSplitHandler
 * 
 * @author XDEV Software jwill
 * @since 4.0
 */
public class XdevGanttTemplate extends AbstractGanttTemplate<Date, UpdateableGanttEntry<Date>>
{
	
	public static final String																		GANTT_PERSISTENCE_STATE			= "GANTT_PERSISTENCE_STATE";
	/**
	 * the serialization id.
	 */
	private static final long																		serialVersionUID				= 1392342401154578284L;
	/**
	 * the default divider location between {@link GanttChart} and
	 * {@link TreeTable} (<code>GanttChartPane</code>)
	 */
	private static final int																		DEFAULT_DIVIDER_LOCATION		= 0;
	
	/**
	 * a default additional UI adjusting value.
	 */
	private static int																				SCROLLBAR_SPACER				= 24;
	
	private GanttChartPane<Date, UpdateableGanttEntry<Date>>										ganttChartPane;
	private static final GanttChartVirtualTableRelationSupport<Date, UpdateableGanttEntry<Date>>	relationSupport					= new GanttChartVirtualTableRelationSupport<Date, UpdateableGanttEntry<Date>>();
	private VirtualTable																			dataVT;
	private Map<DateGanttEntryColumnType, VirtualTableColumn<?>>									mapping;
	private final DateGanttVirtualTableSupport<XdevGanttTemplate>									support							= new DateGanttVirtualTableSupport<XdevGanttTemplate>(
																																			this);
	private boolean																					persistenceState				= false;
	private boolean																					useDefaultGanttPopupMenuItems	= true;
	private PropertyChangeSupport																	gps								= new PropertyChangeSupport(
																																			this);
	
	
	/**
	 * If a empty {@link GanttTemplate} is created, the invoke of
	 * {@link AbstractGanttTemplate}s
	 * {@link #reInitialize(DefaultGanttModel, VirtualTableGanttDataInformation)}
	 * method is required to wire the default functionalities.
	 */
	public XdevGanttTemplate()
	{
		super();
		DefaultGanttModel<Date, UpdateableGanttEntry<Date>> dateModel = new DefaultGanttModel<Date, UpdateableGanttEntry<Date>>();
		dateModel.setScaleModel(DateGanttVirtualTableSupport.DEFAULT_DATE_SCALE_MODEL);
		this.ganttChartPane = new DateGanttChartPane<UpdateableGanttEntry<Date>>(dateModel);
		
		this.add(this.getGanttChartPane(),BorderLayout.CENTER);
		this.initTreeTable();
		this.initToolbar();
	}
	
	
	/**
	 * Constructs a {@link GanttChart} from the given data structure to
	 * initialize this template.
	 * 
	 * @param mapping
	 *            the column mapping to identify the relevant data columns.
	 * @param periods
	 *            the date periods to scale the chart entries.
	 * @param rootIdentifier
	 *            to initialize the entry tree structure.
	 * @param autoQuery
	 *            indicates whether the data should be auto queried or not.
	 * @param vt
	 *            the data provider.
	 */
	public XdevGanttTemplate(final Map<DateGanttEntryColumnType, VirtualTableColumn<?>> mapping,
			final List<DatePeriod> periods, final Object rootIdentifier, final boolean autoQuery,
			final VirtualTable vt)
	{
		this.dataVT = vt;
		this.mapping = mapping;
		
		try
		// code gen cant handle exceptions
		{
			support.setModel(mapping,vt,rootIdentifier,autoQuery);
		}
		catch(VirtualTableException e)
		{
			e.printStackTrace();
		}
		catch(DBException e)
		{
			e.printStackTrace();
		}
		
		this.init();
	}
	
	
	/**
	 * Constructs a {@link GanttModel} from the given data structure to
	 * initialize this template.
	 * 
	 * @param mapping
	 *            the column mapping to identify the relevant data columns.
	 * @param vt
	 *            the data provider.
	 * @param uiInfo
	 *            to scale the template/entries.
	 * @param rootIdentifier
	 *            to initialize the entry tree structure.
	 * @param autoQuery
	 *            indicates whether the data should be auto queried or not.
	 */
	public void setModel(final Map<DateGanttEntryColumnType, VirtualTableColumn<?>> mapping,
			VirtualTable vt, GanttModelUIInformation<Date> uiInfo, final Object rootIdentifier,
			final boolean autoQuery)
	{
		this.dataVT = vt;
		this.mapping = mapping;
		
		try
		// code gen cant handle exceptions
		{
			this.support.setModel(mapping,vt,uiInfo,rootIdentifier,autoQuery);
		}
		catch(VirtualTableException e)
		{
			e.printStackTrace();
		}
		catch(DBException e)
		{
			e.printStackTrace();
		}
		// re init
		this.init();
		this.getGanttChartPane().getTreeTable()
				.addKeyListener(new GanttTemplateRemoveKeyAdapter(this));
	}
	
	
	/**
	 * Constructs a {@link GanttModel} from the given data structure to
	 * initialize this template.
	 * 
	 * @param mapping
	 *            the column mapping to identify the relevant data columns.
	 * @param vt
	 *            the data provider.
	 * @param rootIdentifier
	 *            to initialize the entry tree structure.
	 * @param autoQuery
	 *            indicates whether the data should be auto queried or not.
	 */
	public void setModel(final Map<DateGanttEntryColumnType, VirtualTableColumn<?>> mapping,
			VirtualTable vt, final Object rootIdentifier, final boolean autoQuery)
	{
		this.dataVT = vt;
		this.mapping = mapping;
		
		try
		// code gen cant handle exceptions
		{
			support.setModel(mapping,vt,rootIdentifier,autoQuery);
		}
		catch(VirtualTableException e)
		{
			e.printStackTrace();
		}
		catch(DBException e)
		{
			e.printStackTrace();
		}
		// re init
		this.init();
		this.getGanttChartPane().getTreeTable()
				.addKeyListener(new GanttTemplateRemoveKeyAdapter(this));
	}
	
	
	/**
	 * Constructs a {@link GanttPersistence} from the given data structure to
	 * persist the templates data at runtime.
	 * 
	 * @param mapping
	 *            the column mapping to identify the relevant data columns.
	 * @param dataVT
	 *            the data persistence container
	 * @param dbSync
	 *            indicates whether the data should be synchronized at runtime.
	 * @param rootIdentifier
	 *            to identify the entry tree structure.
	 */
	public void setGanttPersistence(Map<DateGanttEntryColumnType, VirtualTableColumn<?>> mapping,
			VirtualTable dataVT, boolean dbSync, Object rootIdentifier)
	{
		XdevGanttEntryVTMappings<Date> dataContainer = VTDateGanttMappingConverter
				.getEntryColumnMappings(mapping,dataVT);
		
		GanttPersistence<Date, UpdateableGanttEntry<Date>> persistence = new VirtualTableGanttPersistence<Date, UpdateableGanttEntry<Date>>(
				dataContainer,dbSync,rootIdentifier);
		
		this.setGanttPersistence(persistence);
		this.gps.firePropertyChange(GANTT_PERSISTENCE_STATE,this.persistenceState,dbSync);
		this.persistenceState = dbSync;
		
	}
	
	
	/**
	 * Constructs a {@link GanttPersistence} from the given data structure to
	 * persist the templates data at runtime.
	 * 
	 * @param mapping
	 *            the column mapping to identify the relevant data columns.
	 * @param relationVT
	 *            the relation data persistence container.
	 * @param dbSync
	 *            indicates whether the relation data should be synchronized at
	 *            runtime.
	 */
	public void setRelationPersistence(Map<GanttRelationColumnType, VirtualTableColumn<?>> mapping,
			final VirtualTable relationVT, final boolean dbSync)
	{
		this.setGanttRelationPersistence(new VirtualTableGanttRelationPersistence<Date, UpdateableGanttEntry<Date>>(
				VTGanttRelationMappingConverter.getEntryRelationColumnMappings(mapping,relationVT),
				dbSync));
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setModel(GanttModel<Date, UpdateableGanttEntry<Date>> model)
	{
		support.setModel(model);
		// re init
		this.init();
		this.getGanttChartPane().getTreeTable()
				.addKeyListener(new GanttTemplateRemoveKeyAdapter(this));
	}
	
	
	/**
	 * Constructs a {@link GanttEntryRelationModel} from the given data
	 * structure to initialize the entry relations.
	 * 
	 * @param relationMappings
	 *            the column mapping to identify the relevant data columns.
	 * @param relationVT
	 *            the data provider.
	 * @param autoQuery
	 *            indicates whether the data should be auto queried or not.
	 */
	public void setRelationModel(
			Map<GanttRelationColumnType, VirtualTableColumn<?>> relationMappings,
			VirtualTable relationVT, boolean autoQuery)
	{
		if(autoQuery)
		{
			try
			{
				relationVT.queryAndFill();
			}
			catch(VirtualTableException e)
			{
				e.printStackTrace();
			}
			catch(DBException e)
			{
				e.printStackTrace();
			}
		}
		relationSupport.addRelations(getGanttChart(),relationMappings,relationVT);
		
		this.getGanttChart()
				.getModel()
				.getGanttEntryRelationModel()
				.addGanttEntryRelationListener(
						new XdevGanttEntryRelationListener<Date, UpdateableGanttEntry<Date>>(this));
		
		if(this.isRelationValidation())
		{
			relationSupport.validateRelationsInitially(this.getGanttChart().getModel(),
					this.getGanttEntryRelationManagementStrategy(),this.getGanttPersistence());
		}
		
		firePropertyChange(RelationalGanttTemplate.PROPERTY_RELATION_ENABLED,false,true);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public GanttRelationManagementStrategy<Date, UpdateableGanttEntry<Date>> getGanttEntryRelationManagementStrategy()
	{
		if(this.getGanttChart() != null)
		{
			return new UpdateDateRelationManagementStrategy<UpdateableGanttEntry<Date>>(this
					.getGanttChart().getModel());
		}
		return new UpdateDateRelationManagementStrategy<UpdateableGanttEntry<Date>>();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public GanttChartPane<Date, UpdateableGanttEntry<Date>> getGanttChartPane()
	{
		return this.ganttChartPane;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setGanttChartPane(GanttChartPane<Date, UpdateableGanttEntry<Date>> ganttChartPane)
	{
		this.ganttChartPane = ganttChartPane;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public GanttChart<Date, UpdateableGanttEntry<Date>> getGanttChart()
	{
		return this.ganttChartPane.getGanttChart();
	}
	
	
	/**
	 * Sets the scale periods which define the entry scale.
	 * 
	 * @param periods
	 *            the date scale periods to set.
	 */
	public void setScaleModel(List<DatePeriod> periods)
	{
		DatePeriod[] periodArray = periods.toArray(new DatePeriod[periods.size()]);
		
		DefaultGanttModel<Date, UpdateableGanttEntry<Date>> model = (DefaultGanttModel<Date, UpdateableGanttEntry<Date>>)this
				.getGanttChart().getModel();
		
		List<UpdateableGanttEntry<Date>> entries = XdevGanttUtils.getEntries(model);
		
		if(entries.size() > 0)
		{
			ScaleModel<Date> dateScale = new DateScaleModel(Locale.getDefault(),
					XdevGanttUtils.getMinDate(entries),XdevGanttUtils.getMaxDate(entries),
					periodArray);
			
			// refresh models
			model.setScaleModel(dateScale);
			this.getGanttChart().getScaleArea().setScaleModel(dateScale);
			this.getGanttChart().getScaleArea().setVisiblePeriods(periods);
		}
		else
		{
			ScaleModel<Date> dateScale = new DateScaleModel(periodArray);
			
			// refresh models
			model.setScaleModel(dateScale);
			this.getGanttChart().getScaleArea().setScaleModel(dateScale);
			this.getGanttChart().getScaleArea().setVisiblePeriods(periods);
		}
		
		// refresh model -> should be done automatically from jides API..
		this.getGanttChart().setModel(model);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ScaleModel<Date> getScaleModel()
	{
		return this.getGanttChart().getModel().getScaleModel();
	}
	
	
	/**
	 * Returns the selected {@link VirtualTableRow} of this component, or
	 * <code>null</code> if nothing is selected.
	 * 
	 * @return the selected {@link VirtualTableRow} of this component
	 */
	public VirtualTableRow getSelectedVirtualTableRow()
	{
		return this.support.getSelectedVirtualTableRow(this.dataVT,this.mapping);
	}
	
	
	/**
	 * Returns the selected {@link VirtualTableRow}s of this component.
	 * 
	 * @return the selected {@link VirtualTableRow} of this component
	 */
	public VirtualTableRow[] getSelectedVirtualTableRows()
	{
		return this.support.getSelectedVirtualTableRows(this.dataVT,this.mapping);
	}
	
	
	/**
	 * Selects the row if present.
	 * 
	 * @param row
	 *            the row to select
	 */
	public void setSelectedVirtualTableRow(VirtualTableRow row)
	{
		this.support.setSelectedVirtualTableRow(row,this.mapping);
	}
	
	
	/**
	 * Selects the row if present.
	 * 
	 * @param rows
	 *            the rows to select
	 */
	public void setSelectedVirtualTableRows(VirtualTableRow[] rows)
	{
		this.support.setSelectedVirtualTableRows(rows,this.mapping);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isPersistenceEnabled()
	{
		return this.persistenceState;
	}
	
	
	// ////// initialization utilities ///////////
	/**
	 * Default initialization.
	 */
	protected void init()
	{
		this.add(this.getGanttChartPane(),BorderLayout.CENTER);
		
		this.initUI();
	}
	
	
	/**
	 * {@link GanttChartPane} UI initialization.
	 */
	protected void initUI()
	{
		this.initTreeTable();
		
		this.initToolbar();
		
		this.initDefaultPopup();
		
		if(this.getGanttChart().getEntryCount() > 0)
		{
			this.getGanttChart().getScaleArea()
					.setStart(XdevGanttUtils.getFirstEntryStart(this.getGanttChart()));
			this.getGanttChart()
					.getScaleArea()
					.setEnd(XdevGanttUtils.getMaxDate(XdevGanttUtils.getEntries(this
							.getGanttChart().getModel())));
		}
		this.addGanttModelListener(new GanttDateScaleAreaUpdater(this.getGanttChart().getModel(),
				this.getGanttChart().getScaleArea()));
	}
	
	
	/**
	 * initializes the <code>GanttTemplate</code> toolbar (UI relation
	 * creation).
	 */
	private void initToolbar()
	{
		this.setToolbar(new XdevGanttTemplateToolBar<Date, UpdateableGanttEntry<Date>>(this));
	}
	
	
	/**
	 * initializes the {@link TreeTable}.
	 */
	private void initTreeTable()
	{
		AutoFilterTableHeader header = new AutoFilterTableHeader(this.getGanttChartPane()
				.getTreeTable());
		header.setUseNativeHeaderRenderer(true);
		header.setAutoFilterEnabled(true);
		
		this.getGanttChartPane().getTreeTable().setTableHeader(header);
		this.getGanttChartPane().getTreeTable().setDoubleClickEnabled(false);
		this.getGanttChartPane().getTreeTable().setShowGrid(true);
		this.getGanttChartPane().getTreeTable().setShowHorizontalLines(true);
		this.getGanttChartPane().getTreeTable().setShowVerticalLines(true);
		this.getGanttChartPane().getTreeTable().setSortable(false);
		
		// initial resize
		TableUtils.autoResizeAllColumns(this.getGanttChartPane().getTreeTable());
		
		// listeners
		this.getGanttChartPane().getTreeTable().getModel()
				.addTableModelListener(new TableModelListener()
				{
					@Override
					public void tableChanged(TableModelEvent e)
					{
						TableUtils.autoResizeAllColumns(getGanttChartPane().getTreeTable());
					}
				});
		
		this.initSplitPaneWeight();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setGanttPopupMenu(XdevMenu ganttPopupMenu)
	{
		super.setGanttPopupMenu(ganttPopupMenu);
		this.initDefaultPopup();
	}
	
	
	/**
	 * initializes the {@link GanttChartTemplateTablePopup}.
	 */
	private void initDefaultPopup()
	{
		XdevGanttChartPopupMenuInstaller installer = new XdevGanttChartPopupMenuInstaller(
				this.getGanttChart(),this.getGanttPopupMenu());
		
		if(this.isUseDefaultGanttPopupMenuItems())
		{
			GanttChartPopupMenuCustomizer relationCustomizer = new XdevRelationGanttChartPopupMenuCustomizer();
			VirtualTableUpdateDateGanttChartPopupMenuCustomizer updateCustomizer = new VirtualTableUpdateDateGanttChartPopupMenuCustomizer(
					this.mapping,this.dataVT,this.isPersistenceEnabled());
			
			this.addPropertyChangeListener(updateCustomizer);
			installer.addGanttChartPopupMenuCustomizer(relationCustomizer);
			installer.addGanttChartPopupMenuCustomizer(updateCustomizer);
		}
	}
	
	
	/**
	 * initializes the <code>TemplateSplitPane</code>.
	 */
	private void initSplitPaneWeight()
	{
		this.resizeDivider(0);
		this.addComponentListener(new ComponentAdapter()
		{
			@Override
			public void componentResized(ComponentEvent e)
			{
				super.componentResized(e);
				resizeDivider(DEFAULT_DIVIDER_LOCATION);
			}
		});
	}
	
	
	/**
	 * Resizes the divider at the given location to the maximum space that is
	 * left besides the {@link GanttChart}.
	 * 
	 * @param divider
	 *            the divider to resize.
	 */
	private void resizeDivider(final int divider)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				getGanttChartPane().setDividerLocation(
						divider,
						(int)getGanttChartPane().getTreeTable().getPreferredSize().getWidth()
								+ SCROLLBAR_SPACER);
			}
		});
	}
	
	
	/**
	 * Displays the default GanttChart persistence actions additionally to a set
	 * {@link XdevMenu}.
	 * 
	 * @param useDefaultItems
	 */
	public void setUseDefaultGanttPopupMenuItems(boolean useDefaultGanttPopupMenuItems)
	{
		this.useDefaultGanttPopupMenuItems = useDefaultGanttPopupMenuItems;
	}
	
	
	/**
	 * Displays the default GanttChart persistence actions additionally to a set
	 * {@link XdevMenu}.
	 * 
	 * @return use default items state.
	 */
	public boolean isUseDefaultGanttPopupMenuItems()
	{
		return this.useDefaultGanttPopupMenuItems;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setRelationValidation(boolean relationValidation)
	{
		super.setRelationValidation(relationValidation);
		
		// validate & persist initially
		if(this.getGanttChart().getModel().getGanttEntryRelationModel() != null
				&& relationValidation)
		{
			relationSupport.validateRelationsInitially(this.getGanttChart().getModel(),
					this.getGanttEntryRelationManagementStrategy(),this.getGanttPersistence());
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener)
	{
		this.gps.addPropertyChangeListener(listener);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener)
	{
		super.removePropertyChangeListener(listener);
	}
}
