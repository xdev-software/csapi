package xdev.ui.ganttchart.action;

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


import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import xdev.db.DBException;
import xdev.lang.XDEV;
import xdev.lang.cmd.OpenWindow;
import xdev.ui.UIUtils;
import xdev.ui.UIUtils.MessageDialogType;
import xdev.ui.ganttchart.GanttResourceBundle;
import xdev.ui.ganttchart.UpdateableGanttEntry;
import xdev.ui.ganttchart.editor.GanttDateDefaultForm;
import xdev.ui.ganttchart.model.XdevGanttEntryVTMappings;
import xdev.ui.ganttchart.template.XdevGanttTemplate;
import xdev.ui.ganttchart.utils.DateGanttEntryColumnType;
import xdev.ui.ganttchart.utils.VTDateGanttMappingConverter;
import xdev.util.res.ResourceUtils;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTable.VirtualTableRow;
import xdev.vt.VirtualTableColumn;
import xdev.vt.VirtualTableException;

import com.jidesoft.gantt.GanttChart;
import com.jidesoft.gantt.GanttChartPopupMenuCustomizer;
import com.jidesoft.gantt.GanttEntry;


/**
 * 
 * @author XDEV Software jwill
 * @since 4.0
 */
public class VirtualTableUpdateDateGanttChartPopupMenuCustomizer implements
		GanttChartPopupMenuCustomizer, PropertyChangeListener
{
	private final VirtualTable											vt;
	private final XdevGanttEntryVTMappings<Date>						dataContainer;
	private final Map<DateGanttEntryColumnType, VirtualTableColumn<?>>	mapping;
	private VirtualTableRow												selectedRow;
	private boolean														persisted	= false;
	
	
	public VirtualTableUpdateDateGanttChartPopupMenuCustomizer(
			final Map<DateGanttEntryColumnType, VirtualTableColumn<?>> mapping, VirtualTable vt,
			boolean persisted)
	{
		this.dataContainer = VTDateGanttMappingConverter.getEntryColumnMappings(mapping,vt);
		this.vt = vt;
		this.mapping = mapping;
		this.persisted = persisted;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T, S extends GanttEntry<T>> void customizePopupMenu(final GanttChart<T, S> ganttChart,
			JPopupMenu popup, final int clickingRow, Point p)
	{
		ganttChart.setSelectedIndex(clickingRow);
		@SuppressWarnings("unchecked")
		// date implementation
		final UpdateableGanttEntry<Date> selectedEntry = (UpdateableGanttEntry<Date>)ganttChart
				.getEntryAt(clickingRow);
		
		if(selectedEntry != null)
		{
			for(int index = 0; index < this.vt.getRowCount(); index++)
			{
				if(selectedEntry.getId().equals(
						this.vt.getRow(index).get(this.dataContainer.getId())))
				{
					selectedRow = this.vt.getRow(index);
					
					JMenuItem editItem = new JMenuItem(ResourceUtils.getResourceString("EDITENTRY",
							VirtualTableUpdateDateGanttChartPopupMenuCustomizer.class),
							GanttResourceBundle.loadResIcon("edit.png"));
					editItem.addActionListener(new ActionListener()
					{
						@Override
						public void actionPerformed(ActionEvent e)
						{
							XDEV.OpenWindow(new OpenWindow()
							{
								
								@Override
								public void init()
								{
									setXdevWindow(new GanttDateDefaultForm(mapping,selectedRow,
											persisted));
									setContainerType(ContainerType.DIALOG);
								}
							});
						}
					});
					
					JMenuItem createChildItem = new JMenuItem(ResourceUtils.getResourceString(
							"CREATECHILDENTRY",
							VirtualTableUpdateDateGanttChartPopupMenuCustomizer.class),
							GanttResourceBundle.loadResIcon("add.png"));
					createChildItem.addActionListener(new ActionListener()
					{
						
						@Override
						public void actionPerformed(ActionEvent e)
						{
							XDEV.OpenWindow(new OpenWindow()
							{
								
								@Override
								public void init()
								{
									setXdevWindow(new GanttDateDefaultForm(mapping,selectedEntry,
											vt,persisted));
									setContainerType(ContainerType.DIALOG);
								}
							});
						}
					});
					
					JMenuItem removeEntryItem = new JMenuItem(
							ResourceUtils.getResourceString("REMOVEENTRY",
									VirtualTableUpdateDateGanttChartPopupMenuCustomizer.class),
							GanttResourceBundle.loadResIcon("remove.png"));
					removeEntryItem.addActionListener(new ActionListener()
					{
						
						@Override
						public void actionPerformed(ActionEvent e)
						{
							List<String> s = new ArrayList<String>();
							Collections.addAll(s,ResourceUtils.getResourceString("Y",
									GanttTemplateRemoveKeyAdapter.class),ResourceUtils
									.getResourceString("N",GanttTemplateRemoveKeyAdapter.class));
							Object confirmation = UIUtils.showConfirmMessage(
									ResourceUtils.getResourceString("Title",
											GanttTemplateRemoveKeyAdapter.class),ResourceUtils
											.getResourceString("Message",
													GanttTemplateRemoveKeyAdapter.class),s,0,
									MessageDialogType.WARNING_MESSAGE);
							if(confirmation != null)
							{
								if(confirmation.equals(ResourceUtils.getResourceString("Y",
										GanttTemplateRemoveKeyAdapter.class)))
								{
									try
									{
										if(selectedRow != null)
										{
											selectedRow.delete(persisted);
										}
									}
									catch(VirtualTableException e1)
									{
										e1.printStackTrace();
									}
									catch(DBException e1)
									{
										e1.printStackTrace();
									}
								}
							}
						}
					});
					
					popup.add(editItem);
					popup.add(createChildItem);
					popup.add(removeEntryItem);
				}
			}
		}
		else
		{
			JMenuItem addItem = new JMenuItem(ResourceUtils.getResourceString("CREATEENTRY",
					VirtualTableUpdateDateGanttChartPopupMenuCustomizer.class),
					GanttResourceBundle.loadResIcon("new.png"));
			addItem.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					XDEV.OpenWindow(new OpenWindow()
					{
						
						@Override
						public void init()
						{
							setXdevWindow(new GanttDateDefaultForm(mapping,vt,persisted));
							setContainerType(ContainerType.DIALOG);
						}
					});
				}
			});
			popup.add(addItem);
		}
		
	}
	
	
	@Override
	public void propertyChange(PropertyChangeEvent evt)
	{
		if(evt.getPropertyName().equals(XdevGanttTemplate.GANTT_PERSISTENCE_STATE))
		{
			this.persisted = (Boolean)evt.getNewValue();
		}
	}
}
