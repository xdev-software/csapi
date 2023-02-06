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
package xdev.ui.ganttchart.editor;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.EventListener;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JComponent;
import javax.swing.JToolBar;

import xdev.ui.XdevButton;
import xdev.ui.XdevToolBar;
import xdev.ui.ganttchart.GanttResourceBundle;
import xdev.ui.ganttchart.UpdateableGanttEntry;
import xdev.ui.ganttchart.action.GanttRelationUIInsertCommandListener;
import xdev.ui.ganttchart.template.GanttTemplate;
import xdev.ui.ganttchart.template.RelationalGanttTemplate;

import com.jidesoft.gantt.GanttChartPane;
import com.jidesoft.gantt.GanttEntry;
import com.jidesoft.gantt.GanttEntryRelation;


/**
 * {@link JToolBar} which is aligned at the top/north anchor of the
 * {@link GanttTemplate} by default.
 * 
 * <p>
 * Provides runtime {@link GanttEntryRelation} update features
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
 * 
 * @author XDEV Software jwill
 * @since 4.0
 */
public class XdevGanttTemplateToolBar<T extends Comparable<T>, S extends UpdateableGanttEntry<T>>
		extends XdevToolBar
{
	
	/**
	 * the serialization id.
	 */
	private static final long							serialVersionUID					= 6608890673293654048L;
	
	public static final String							PROPERTY_RELATION_TOOLBAR_ENABLED	= "relationToolbarEnabled";
	
	/**
	 * the {@link GanttTemplate} to use for runtime modification.
	 */
	private RelationalGanttTemplate<T, S>				template;
	
	/**
	 * the {@link GanttChartPane} to use for runtime modification.
	 */
	private GanttChartPane<T, S>						ganttChartPane;
	
	/**
	 * the indicator wether the connection hint is shown.
	 */
	private AtomicBoolean								showConnectionHint					= new AtomicBoolean(
																									true);
	
	private XdevButton									connect;
	
	final GanttRelationUIInsertCommandListener<T, S>	eventListener;
	
	
	// private XdevButton disconnect;
	
	/**
	 * 
	 * @param template
	 *            the {@link GanttTemplate} to use for runtime modification.
	 */
	public <TT extends JComponent & RelationalGanttTemplate<T, S>> XdevGanttTemplateToolBar(
			TT template)
	{
		this.setOrientation(XdevToolBar.HORIZONTAL);
		this.ganttChartPane = template.getGanttChartPane();
		this.template = template;
		this.eventListener = new GanttRelationUIInsertCommandListener<T, S>(template);
		
		template.addPropertyChangeListener(RelationalGanttTemplate.PROPERTY_RELATION_ENABLED,
				new PropertyChangeListener()
				{
					@Override
					public void propertyChange(PropertyChangeEvent evt)
					{
						// must assume that the getValue is boolean typed,
						// because prop change
						// listener supports no generics
						connect.setVisible((Boolean)evt.getNewValue());
						// disconnect.setVisible((Boolean)evt.getNewValue());
					}
				});
		
		this.initButtons();
	}
	
	
	/**
	 * /** initialize UI.
	 */
	private void initButtons()
	{
		this.initRelationButtons();
	}
	
	
	/**
	 * initialize UI.
	 */
	private void initRelationButtons()
	{
		this.connect = new XdevButton();
		this.connect.setTabIndex(1);
		this.connect.setText(GanttResourceBundle.getString("toolbar.connect"));
		this.connect.setIcon(GanttResourceBundle.loadResIcon("connect.png"));
		this.connect.setVisible(false);
		this.connect.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// already in entry relation connection mode?
				if(!checkContainsListener(GanttRelationUIInsertCommandListener.class,eventListener))
				{
					// check tutorial mode
					if(showConnectionHint.get())
					{
						XdevGanttChartRelationConnectionHintPopup popup = new XdevGanttChartRelationConnectionHintPopup(
								connect,showConnectionHint);
						
						ganttChartPane.getTreeTable().clearSelection();
						
						popup.showPopup();
					}
					
					// expandable workaround see JIDE Issue:
					// http://www.jidesoft.com/forum/viewtopic.php?f=11&t=14911
					template.getGanttChartPane().getTreeTable().setExpandable(false);
					// activate entry relation connection mode
					ganttChartPane.getGanttChart().getSelectionModel()
							.addListSelectionListener(eventListener);
				}
			}
		});

		this.add(this.connect);
	}
	
	
	/**
	 * Utility to gain listener connection information.
	 * 
	 * @param listenerType
	 *            the listener type to search for.
	 * @param listenerClass
	 *            the listener class to search for.
	 * 
	 * @return the listener connection state.
	 */
	private boolean checkContainsListener(Class<? extends EventListener> listenerType,
			EventListener listener)
	{
		if(this.ganttChartPane.getGanttChart().getSelectionModel() instanceof DefaultListSelectionModel)
		{
			DefaultListSelectionModel selectionModel = (DefaultListSelectionModel)this.ganttChartPane
					.getGanttChart().getSelectionModel();
			for(int i = 0; i < selectionModel.getListSelectionListeners().length; i++)
			{
				if(selectionModel.getListSelectionListeners()[i].equals(listener))
				{
					return true;
				}
			}
		}
		// throw non researchable listselectionmodel exception or something...
		return false;
	}
}
