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
package xdev.ui.ganttchart;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.UIManager;

import xdev.db.DBException;
import xdev.ui.ganttchart.action.GanttEntryListener;
import xdev.ui.ganttchart.model.DateGanttDataVTSyncronizer;
import xdev.ui.ganttchart.model.EntryVTMapper;
import xdev.ui.ganttchart.model.XdevGanttEntryVTMappings;
import xdev.ui.ganttchart.model.XdevGanttModelUIInformation;
import xdev.ui.ganttchart.model.XdevVirtualTableGanttModel;
import xdev.ui.ganttchart.model.XdevVirtualTableGanttTreeTableModel;
import xdev.ui.ganttchart.template.GanttTemplate;
import xdev.ui.ganttchart.template.RelationalGanttTemplate;
import xdev.ui.ganttchart.utils.DateGanttEntryColumnType;
import xdev.ui.ganttchart.utils.VTDateGanttMappingConverter;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTable.VirtualTableRow;
import xdev.vt.VirtualTableColumn;
import xdev.vt.VirtualTableException;

import com.jidesoft.gantt.DateGanttChartPane;
import com.jidesoft.gantt.DefaultGanttModel;
import com.jidesoft.gantt.GanttEntry;
import com.jidesoft.gantt.GanttModel;
import com.jidesoft.scale.DatePeriod;
import com.jidesoft.scale.DateScaleModel;
import com.jidesoft.scale.ScaleModel;


/**
 * Provides common functionality for a {@link GanttModel} to cooperate with a
 * {@link VirtualTable}.
 * 
 * @param <T>
 *            the {@link GanttEntry} datatype.
 * @author XDEV Software jwill
 * @since 4.0
 */

public class DateGanttVirtualTableSupport<T extends JComponent & RelationalGanttTemplate<Date, UpdateableGanttEntry<Date>>>
{
	private final T							template;
	public static final ScaleModel<Date>	DEFAULT_DATE_SCALE_MODEL	= new DateScaleModel(
																				DatePeriod.MONTH,
																				DatePeriod.YEAR);
	
	
	public DateGanttVirtualTableSupport(T template)
	{
		this.template = template;
	}
	
	
	public void setModel(final Map<DateGanttEntryColumnType, VirtualTableColumn<?>> mapping,
			VirtualTable vt, GanttModelUIInformation<Date> uiInfo, final Object rootIdentifier,
			final boolean autoQuery) throws VirtualTableException, DBException
	{
		autoQueryGanttData(vt,autoQuery);
		XdevGanttEntryVTMappings<Date> dataContainer = VTDateGanttMappingConverter
				.getEntryColumnMappings(mapping,vt);
		
		EntryVTMapper<UpdateableGanttEntry<Date>, Date> entryMapper = initVirtualTableRowMapper(dataContainer);
		
		XdevVirtualTableGanttModel<Date, UpdateableGanttEntry<Date>> ganttModel = new XdevVirtualTableGanttModel<Date, UpdateableGanttEntry<Date>>(
				dataContainer,uiInfo,entryMapper);
		ganttModel.setRootIdentifier(rootIdentifier);
		this.template.setGanttChartPane(new DateGanttChartPane<UpdateableGanttEntry<Date>>(
				ganttModel));
		this.setModel(ganttModel);
		this.prepareSyncronization(vt,entryMapper,ganttModel,rootIdentifier);
	}
	
	
	public void setModel(final Map<DateGanttEntryColumnType, VirtualTableColumn<?>> mapping,
			VirtualTable vt, final Object rootIdentifier, final boolean autoQuery)
			throws VirtualTableException, DBException
	{
		autoQueryGanttData(vt,autoQuery);
		XdevGanttEntryVTMappings<Date> dataContainer = VTDateGanttMappingConverter
				.getEntryColumnMappings(mapping,vt);
		
		EntryVTMapper<UpdateableGanttEntry<Date>, Date> entryMapper = initVirtualTableRowMapper(dataContainer);
		GanttModelUIInformation<Date> uiInfo = createDefaultDateUIInformation(vt);
		
		XdevVirtualTableGanttModel<Date, UpdateableGanttEntry<Date>> ganttModel = new XdevVirtualTableGanttModel<Date, UpdateableGanttEntry<Date>>(
				dataContainer,uiInfo,entryMapper);
		ganttModel.setRootIdentifier(rootIdentifier);
		this.template.setGanttChartPane(new DateGanttChartPane<UpdateableGanttEntry<Date>>(
				ganttModel));
		this.setModel(ganttModel);
		this.prepareSyncronization(vt,entryMapper,ganttModel,rootIdentifier);
	}
	
	
	public void setModel(GanttModel<Date, UpdateableGanttEntry<Date>> model)
	{
		if(this.template.getGanttChartPane() != null)
		{
			this.template.remove(this.template.getGanttChartPane());
		}
		this.template.setGanttChartPane(new DateGanttChartPane<UpdateableGanttEntry<Date>>(model));
		
		this.initModelListener();
	}
	
	
	public static void autoQueryGanttData(VirtualTable vt, boolean autoQuery)
			throws VirtualTableException, DBException
	{
		if(autoQuery)
		{
			vt.queryAndFill();
		}
	}
	
	
	private void prepareSyncronization(VirtualTable dataVT,
			EntryVTMapper<UpdateableGanttEntry<Date>, Date> mapper,
			DefaultGanttModel<Date, UpdateableGanttEntry<Date>> model, Object rootIdentifier)
	{
		
		DateGanttDataVTSyncronizer<UpdateableGanttEntry<Date>> syncronizer = new DateGanttDataVTSyncronizer<UpdateableGanttEntry<Date>>(
				model,mapper,rootIdentifier);
		dataVT.addVirtualTableListener(syncronizer);
		template.addPropertyChangeListener(syncronizer);
	}
	
	
	public EntryVTMapper<UpdateableGanttEntry<Date>, Date> initVirtualTableRowMapper(
			XdevGanttEntryVTMappings<Date> cnt)
	{
		SpecialRelationalUpdatableEntryCreator<UpdateableGanttEntry<Date>, Date> entryCreator = new XdevVirtualTableGanttEntryCreator<Date>();
		EntryVTMapper<UpdateableGanttEntry<Date>, Date> mapper = new EntryVTMapper<UpdateableGanttEntry<Date>, Date>(
				new DateRangeProvider(),entryCreator,cnt);
		
		return mapper;
	}
	
	
	public GanttModelUIInformation<Date> createDefaultDateUIInformation(VirtualTable vt)
	{
		GanttModelUIInformation<Date> uiInfo = new XdevGanttModelUIInformation<Date>();
		if(this.template.getGanttChart().getModel().getScaleModel() != null)
		{
			uiInfo.setScaleModel(this.template.getGanttChart().getModel().getScaleModel());
		}
		else
		{
			uiInfo.setScaleModel(DEFAULT_DATE_SCALE_MODEL);
		}
		
		XdevVirtualTableGanttTreeTableModel<Date> tableModel = new XdevVirtualTableGanttTreeTableModel<Date>(
				vt,true,UIManager.getFont("Label.font"));
		this.template.addPropertyChangeListener(GanttTemplate.EDITABLE_PROPERTY,tableModel);
		uiInfo.setTreeTableModel(tableModel);
		
		return uiInfo;
	}
	
	
	/**
	 * Returns the selected {@link VirtualTableRow} of this component, or
	 * <code>null</code> if nothing is selected.
	 * 
	 * @return the selected {@link VirtualTableRow} of this component
	 */
	public VirtualTableRow getSelectedVirtualTableRow(VirtualTable vt,
			Map<DateGanttEntryColumnType, VirtualTableColumn<?>> mapping)
	{
		UpdateableGanttEntry<Date> selectedEntry = this.template.getGanttChart().getModel()
				.getEntryAt(this.template.getGanttChart().getSelectedRow());
		if(selectedEntry != null)
		{
			for(int index = 0; index < vt.getRowCount(); index++)
			{
				VirtualTableColumn<?> idColumn = VTDateGanttMappingConverter
						.getEntryColumnMappings(mapping,vt).getId();
				Object currentId = vt.getValueAt(index,idColumn);
				if(currentId.equals(selectedEntry.getId()))
				{
					return vt.getRow(index);
				}
			}
		}
		return null;
	}
	
	
	/**
	 * Returns the selected {@link VirtualTableRow}s of this component.
	 * 
	 * @return the selected {@link VirtualTableRow} of this component
	 */
	public VirtualTableRow[] getSelectedVirtualTableRows(VirtualTable vt,
			Map<DateGanttEntryColumnType, VirtualTableColumn<?>> mapping)
	{
		List<VirtualTableRow> selectedRows = new ArrayList<VirtualTableRow>();
		List<UpdateableGanttEntry<Date>> selectedEntries = new ArrayList<UpdateableGanttEntry<Date>>();
		for(int i = 0; i < this.template.getGanttChart().getSelectedRows().length; i++)
		{
			selectedEntries.add(this.template.getGanttChart().getModel()
					.getEntryAt(this.template.getGanttChart().getSelectedRows()[i]));
		}
		
		if(selectedEntries.size() > 0)
		{
			for(UpdateableGanttEntry<Date> updateableGanttEntry : selectedEntries)
			{
				for(int index = 0; index < vt.getRowCount(); index++)
				{
					VirtualTableColumn<?> idColumn = VTDateGanttMappingConverter
							.getEntryColumnMappings(mapping,vt).getId();
					Object currentId = vt.getValueAt(index,idColumn);
					if(currentId.equals(updateableGanttEntry.getId()))
					{
						selectedRows.add(vt.getRow(index));
					}
				}
			}
		}
		return selectedRows.toArray(new VirtualTableRow[selectedRows.size()]);
	}
	
	
	/**
	 * Selects the row if present.
	 * 
	 * @param row
	 *            the row to select
	 */
	public void setSelectedVirtualTableRow(VirtualTableRow row,
			Map<DateGanttEntryColumnType, VirtualTableColumn<?>> mapping)
	{
		VirtualTableColumn<?> idColumn = VTDateGanttMappingConverter.getEntryColumnMappings(
				mapping,row.getVirtualTable()).getId();
		
		for(int i = 0; i < this.template.getGanttChart().getModel().getEntryCount(); i++)
		{
			UpdateableGanttEntry<Date> currentEntry = this.template.getGanttChart().getModel()
					.getEntryAt(i);
			Object currentId = currentEntry.getId();
			if(currentId.equals(row.get(idColumn)))
			{
				this.template.getGanttChartPane().getTreeTable().setSelectedRow(currentEntry);
			}
		}
	}
	
	
	/**
	 * Selects the row if present.
	 * 
	 * @param rows
	 *            the rows to select
	 */
	public void setSelectedVirtualTableRows(VirtualTableRow[] rows,
			Map<DateGanttEntryColumnType, VirtualTableColumn<?>> mapping)
	{
		if(rows.length > 0)
		{
			
			VirtualTableColumn<?> idColumn = VTDateGanttMappingConverter.getEntryColumnMappings(
					mapping,rows[0].getVirtualTable()).getId();
			List<UpdateableGanttEntry<Date>> selectedEntries = new ArrayList<UpdateableGanttEntry<Date>>();
			
			for(int e = 0; e < rows.length; e++)
			{
				for(int i = 0; i < this.template.getGanttChart().getModel().getEntryCount(); i++)
				{
					UpdateableGanttEntry<Date> currentEntry = this.template.getGanttChart()
							.getModel().getEntryAt(i);
					Object currentId = currentEntry.getId();
					if(currentId.equals(rows[e].get(idColumn)))
					{
						selectedEntries.add(currentEntry);
					}
				}
			}
			this.template
					.getGanttChartPane()
					.getTreeTable()
					.setSelectedRows(
							selectedEntries.toArray(new UpdateableGanttEntry[selectedEntries.size()]));
		}
	}
	
	
	private void initModelListener()
	{
		GanttEntryListener<Date, UpdateableGanttEntry<Date>> listener = new GanttEntryListener<Date, UpdateableGanttEntry<Date>>(
				this.template);
		listener.setRelationManagementStrategy(this.template
				.getGanttEntryRelationManagementStrategy());
		
		this.template.getGanttChart().getModel().addGanttModelListener(listener);
	}
}
