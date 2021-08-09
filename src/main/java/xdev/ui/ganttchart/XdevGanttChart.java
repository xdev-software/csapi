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


import xdev.ui.ganttchart.model.EntryVTMapper;
import xdev.ui.ganttchart.model.GanttEntryMapper;
import xdev.ui.ganttchart.model.GanttPersistence;
import xdev.ui.ganttchart.model.XdevGanttEntryVTMappings;
import xdev.ui.ganttchart.model.XdevGanttModelUIInformation;
import xdev.ui.ganttchart.model.XdevVirtualTableGanttModel;
import xdev.ui.ganttchart.utils.VirtualTableGanttPersistenceUtils;
import xdev.vt.VirtualTable;

import com.jidesoft.gantt.GanttChart;
import com.jidesoft.gantt.GanttEntry;
import com.jidesoft.gantt.GanttModel;
import com.jidesoft.scale.ScaleModel;


/**
 * A gantt chart that displays tasks as {@link GanttEntry}s, scaled in date or
 * number format. The user is able to alter the {@link GanttEntry}s start, end
 * and completion state via drag�n drop.
 * 
 * <p>
 * Suitable for own {@link GanttEntry} implementations.
 * </p>
 * 
 * @param <S>
 *            the customized {@link GanttEntry} type.
 * 
 * @param <T>
 *            the gantt data type, for example <code>Date</code> or
 *            <code>Integer</code>. Has to extend {@link Comparable} for
 *            validation purpose.
 *            <p>
 *            Used for entry scaling via {@link GanttEntry#getRange()}
 *            </p>
 * @author XDEV Software jwill
 * @since 4.0
 */
public class XdevGanttChart<T, S extends UpdateableGanttEntry<T>> extends GanttChart<T, S>
{
	
	/**
	 * the serialization id.
	 */
	private static final long		serialVersionUID	= -7655478804064610622L;
	
	/**
	 * persistence {@link GanttPersistence}.
	 */
	private GanttPersistence<T, S>	ganttPersistence;
	
	/**
	 * the data provider for this {@link GanttChart}.
	 */
	private VirtualTable			ganttDataVT;
	
	
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
	public XdevGanttChart(XdevGanttEntryVTMappings<T> dataContainer,
			GanttModelUIInformation<T> uiInfo, EntryVTMapper<S, T> mapper)
	{
		this.ganttDataVT = dataContainer.getVirtualTable();
		
		VirtualTableGanttPersistenceUtils.fillData(this.ganttDataVT,dataContainer.getId());
		
		this.setModel(new XdevVirtualTableGanttModel<T, S>(dataContainer,uiInfo,mapper));
		
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
	public XdevGanttChart(GanttModel<T, S> model, GanttPersistence<T, S> persistence)
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
	public XdevGanttChart(XdevGanttEntryVTMappings<T> dataContainer,
			XdevGanttModelUIInformation<T> uiInfo, EntryVTMapper<S, T> mapper)
	{
		this.ganttDataVT = dataContainer.getVirtualTable();
		
		VirtualTableGanttPersistenceUtils.fillData(this.ganttDataVT,dataContainer.getId());
		
		this.setModel(new XdevVirtualTableGanttModel<T, S>(dataContainer,uiInfo,mapper));
		
		this.init();
	}
	
	
	/**
	 * 
	 * @param ganttPersistence
	 *            {@link GanttPersistence} to persist the updated data.
	 */
	public void setGanttPersistence(GanttPersistence<T, S> ganttPersistence)
	{
		this.ganttPersistence = ganttPersistence;
	}
	
	
	/**
	 * 
	 * 
	 * @return persistence {@link GanttPersistence} to persist the updated data.
	 */
	public GanttPersistence<T, S> getPersistence()
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
