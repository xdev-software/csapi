package xdev.ui.ganttchart;

/*-
 * #%L
 * XDEV BI Suite
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


import java.util.Date;

import xdev.ui.ganttchart.model.EntryVTMapper;
import xdev.ui.ganttchart.model.GanttEntryMapper;
import xdev.ui.ganttchart.model.GanttPersistence;
import xdev.ui.ganttchart.model.XdevGanttEntryVTMappings;
import xdev.ui.ganttchart.model.XdevGanttModelUIInformation;
import xdev.ui.ganttchart.model.XdevVirtualTableGanttModel;
import xdev.vt.VirtualTable;

import com.jidesoft.gantt.GanttChart;
import com.jidesoft.gantt.GanttEntry;
import com.jidesoft.gantt.GanttModel;
import com.jidesoft.scale.ScaleModel;


/**
 * A gantt chart that displays tasks as {@link GanttEntry}s, scaled in date
 * format. The user is able to alter the {@link GanttEntry}s start, end and
 * completion state via drag�n drop.
 * 
 * <p>
 * Pre typed for Date gantt data for easier usage. Use {@link XdevGanttChart}
 * for extended customization.
 * </p>
 * 
 * 
 * @author XDEV Software jwill
 * @since 4.0
 */

public class XdevDateGanttChart extends GanttChart<Date, UpdateableGanttEntry<Date>>
{
	
	/**
	 * the serialization id.
	 */
	private static final long									serialVersionUID	= -7655478804064610622L;
	
	/**
	 * persistence {@link GanttPersistence}.
	 */
	private GanttPersistence<Date, UpdateableGanttEntry<Date>>	ganttPersistence;
	
	/**
	 * the data provider for this {@link GanttChart}.
	 */
	private VirtualTable										ganttDataVT;
	
	
	/**
	 * There is no persistence manager behind this initialization.
	 * 
	 * @param dataInfo
	 *            necessary {@link GanttModel} information for example the data
	 *            mappings.
	 * @param uiInfo
	 *            additional {@link GanttModel} information for example a custom
	 *            {@link ScaleModel}.
	 * 
	 * @param mapper
	 *            the {@link GanttEntryMapper} which provides common
	 *            functionality to map data to a {@link GanttEntry}.
	 */
	public XdevDateGanttChart(XdevGanttEntryVTMappings<Date> dataContainer,
			GanttModelUIInformation<Date> uiInfo,
			EntryVTMapper<UpdateableGanttEntry<Date>, Date> mapper)
	{
		this.ganttDataVT = dataContainer.getVirtualTable();
		
		this.setModel(new XdevVirtualTableGanttModel<Date, UpdateableGanttEntry<Date>>(
				dataContainer,uiInfo,mapper));
		
		this.init();
	}
	
	
	/**
	 * 
	 * 
	 * @param model
	 *            a filled {@link GanttModel}.
	 * @param persistence
	 *            {@link GanttPersistence} to persist the updated data
	 */
	public XdevDateGanttChart(GanttModel<Date, UpdateableGanttEntry<Date>> model,
			GanttPersistence<Date, UpdateableGanttEntry<Date>> persistence)
	{
		super(model);
		this.ganttPersistence = persistence;
		this.init();
	}
	
	
	/**
	 * 
	 * @param dataInfo
	 *            necessary {@link GanttModel} information for example the data
	 *            mappings.
	 * 
	 * @param uiInfo
	 *            additional {@link GanttModel} information for example a custom
	 *            {@link ScaleModel}
	 * 
	 * @param persistence
	 *            {@link GanttPersistence} to persist the updated data
	 * 
	 * @param mapper
	 *            the {@link GanttEntryMapper} which provides common
	 *            functionality to map data to a {@link GanttEntry}.
	 */
	public XdevDateGanttChart(XdevGanttEntryVTMappings<Date> dataContainer,
			XdevGanttModelUIInformation<Date> uiInfo,
			EntryVTMapper<UpdateableGanttEntry<Date>, Date> mapper)
	{
		this.ganttDataVT = dataContainer.getVirtualTable();
		
		this.setModel(new XdevVirtualTableGanttModel<Date, UpdateableGanttEntry<Date>>(
				dataContainer,uiInfo,mapper));
		
		this.init();
	}
	
	
	/**
	 * 
	 * @param ganttPersistence
	 *            {@link GanttPersistence} to persist the updated data.
	 */
	public void setGanttPersistence(
			GanttPersistence<Date, UpdateableGanttEntry<Date>> ganttPersistence)
	{
		this.ganttPersistence = ganttPersistence;
	}
	
	
	/**
	 * 
	 * 
	 * @return persistence {@link GanttPersistence} to persist the updated data.
	 */
	public GanttPersistence<Date, UpdateableGanttEntry<Date>> getPersistence()
	{
		return this.ganttPersistence;
	}
	
	
	/**
	 * common initialization.
	 */
	private void init()
	{
		this.initGUI();
	}
	
	
	/**
	 * UI initialization.
	 */
	private void initGUI()
	{
		this.setViewMode(GanttChart.VIEW_MODE_PANNING);
	}
	
	
	/**
	 * 
	 * @return the <code>VirtualTable</code> which holds the data.
	 */
	public VirtualTable getVirtualTable()
	{
		return ganttDataVT;
	}
}
