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
import java.util.Date;

import javax.swing.event.ListSelectionListener;

import xdev.ui.XdevContainer;
import xdev.ui.XdevMenu;
import xdev.ui.XdevToolBar;
import xdev.ui.ganttchart.UpdateableGanttEntry;
import xdev.ui.ganttchart.action.GanttEntryRelationManager;
import xdev.ui.ganttchart.action.GanttRelationManagementStrategy;
import xdev.ui.ganttchart.action.UIRelationManagementStrategy;
import xdev.ui.ganttchart.model.GanttPersistence;
import xdev.ui.ganttchart.model.GanttRelationPersistence;
import xdev.util.ObjectUtils;

import com.jidesoft.gantt.GanttChart;
import com.jidesoft.gantt.GanttEntry;
import com.jidesoft.gantt.GanttEntryRelationListener;
import com.jidesoft.gantt.GanttModel;
import com.jidesoft.gantt.GanttModelListener;
import com.jidesoft.grid.TreeTable;


/**
 * The {@link AbstractGanttTemplate} which is a conrete implementation of
 * {@link RelationalGanttTemplate} provides a UI representation for project
 * data.
 * 
 * <p>
 * Suitable for own {@link GanttEntry} implementations.
 * </p>
 * 
 * <p>
 * The data is displayed in a {@link GanttChart} on the right hand side, and a
 * {@link TreeTable} for more detailed information, at the left hand side.
 * </p>
 * 
 * <p>
 * Several UI interaction interfaces allow the user to update the data on the ui
 * layer at runtime.
 * </p>
 * 
 * <p>
 * It is also possible to disable the <code>GanttChartTemplates</code>
 * components at runtime, a common use case would be to disable the
 * <code>TreeTable</code> for a better general view.
 * </p>
 * 
 * 
 * 
 * @param <T>
 *            the {@link GanttEntry} data type, for example {@link Date} or
 *            {@link Integer}, must be a subtype of {@link Comparable}
 * 
 * 
 * @param <S>
 *            the customized {@link GanttEntry} type.
 * 
 * 
 * 
 * @see ComponentSplitHandler
 * 
 * @author XDEV Software jwill
 * @since 4.0
 */
public abstract class AbstractGanttTemplate<T extends Comparable<T>, S extends UpdateableGanttEntry<T>>
		extends XdevContainer implements RelationalGanttTemplate<T, S>,
		GanttEntryRelationManager<T, S>
{
	/**
	 * the serialization id.
	 */
	private static final long						serialVersionUID			= 1392342401154578284L;
	
	/**
	 * the {@link GanttTemplate}s toolbar, which provides additonal features for
	 * runtime ui modification.
	 */
	private XdevToolBar								ganttChartToolbar;
	
	private XdevMenu								ganttPopupMenu;
	
	/**
	 * the <code>TreeTable</code> visibility indicator.
	 */
	private boolean									showTreeTable				= true;
	
	/**
	 * the indicator whether the {@link GanttTemplate} is used as
	 * {@link RelationalGanttTemplate} or not.
	 */
	private boolean									relationValidation			= false;
	
	private boolean									treeTableVisibilityState	= true;
	
	private boolean									treeTableEditable			= false;
	
	private GanttPersistence<T, S>					persistence;
	private GanttRelationPersistence<S>				relationPersistence;
	
	/**
	 * the currently used {@link ComponentSplitHandler}.
	 */
	private ComponentSplitHandler					splitHandler;
	
	private GanttRelationManagementStrategy<T, S>	relationManagementStrategy;
	
	
	public AbstractGanttTemplate()
	{
		super();
		this.setLayout(new BorderLayout());
	}
	
	
	/**
	 * Initializes the template with the given {@link GanttModel}.
	 * 
	 * @param model
	 *            the {@link GanttModel} to set.
	 */
	public abstract void setModel(GanttModel<T, S> model);
	
	
	/**
	 * Delivers information wether the {@link TreeTable} is currently shown.
	 * 
	 * @return the UI-visibility indicator for the <code>TreeTable</code>
	 */
	public boolean isShowTreeTable()
	{
		return showTreeTable;
	}
	
	
	/**
	 * @return the ganttChartToolbar
	 */
	public XdevToolBar getToolbar()
	{
		return ganttChartToolbar;
	}
	
	
	/**
	 * Sets the <code>toolbar</code> for this template.
	 * <p>
	 * Also triggers a UI refresh if a toolbar already exists.
	 * </p>
	 * 
	 * @param ganttChartToolbar
	 *            the ganttChartToolbar to set
	 */
	public void setToolbar(XdevToolBar ganttChartToolbar)
	{
		if(this.ganttChartToolbar != null)
		{
			this.remove(this.ganttChartToolbar);
		}
		this.ganttChartToolbar = ganttChartToolbar;
		
		this.add(this.getToolbar(),BorderLayout.NORTH);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isRelationValidation()
	{
		return relationValidation;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setRelationValidation(boolean relationValidation)
	{
		this.relationValidation = relationValidation;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setGanttPersistence(GanttPersistence<T, S> persistence)
	{
		this.persistence = persistence;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public GanttPersistence<T, S> getGanttPersistence()
	{
		return this.persistence;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setGanttRelationPersistence(GanttRelationPersistence<S> relationPersistence)
	{
		this.relationPersistence = relationPersistence;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public GanttRelationPersistence<S> getGanttRelationPersistence()
	{
		return this.relationPersistence;
	}
	
	
	/**
	 * Returns wether the template {@link TreeTable} is enabled or not.
	 * 
	 * @return the template {@link TreeTable} visiblity state.
	 */
	public boolean isTreeTableVisibilityState()
	{
		return treeTableVisibilityState;
	}
	
	
	/**
	 * Sets the template {@link TreeTable} visibility state.
	 * 
	 * @param treeTableVisibilityState
	 *            the visibility state to set.
	 */
	public void setTreeTableVisibilityState(boolean treeTableVisibilityState)
	{
		this.treeTableVisibilityState = treeTableVisibilityState;
		
		// init handler
		if(this.splitHandler == null)
		{
			this.splitHandler = new GanttTemplateSplitHandler(this.getGanttChartPane());
		}
		
		if(!treeTableVisibilityState)
		{
			this.splitHandler.splitAtIndex(0);
		}
		else
		{
			if(this.splitHandler.getSeveredComponent() != null)
			{
				this.splitHandler.restoreAtIndex(0);
			}
		}
	}
	
	
	public XdevMenu getGanttPopupMenu()
	{
		return ganttPopupMenu;
	}
	
	
	public void setGanttPopupMenu(XdevMenu ganttPopupMenu)
	{
		this.ganttPopupMenu = ganttPopupMenu;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setGanttEntryRelationManagementStrategy(
			GanttRelationManagementStrategy<T, S> strategy)
	{
		this.relationManagementStrategy = strategy;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public GanttRelationManagementStrategy<T, S> getGanttEntryRelationManagementStrategy()
	{
		if(this.relationManagementStrategy == null)
		{
			return new UIRelationManagementStrategy<T, S>(ObjectUtils.notNull((this.getGanttChart()
					.getModel())));
		}
		else
		{
			return this.relationManagementStrategy;
		}
	}
	
	
	
	public boolean isTreeTableEditable()
	{
		return treeTableEditable;
	}
	
	

	public void setTreeTableEditable(boolean treeTableEditableState)
	{
		if(this.treeTableEditable != treeTableEditableState)
		{
			boolean oldValue = this.treeTableEditable;
			this.treeTableEditable = treeTableEditableState;
			firePropertyChange(EDITABLE_PROPERTY,oldValue,treeTableEditableState);
		}
	}
	
	
	// ############ Delegates ############## //
	
	public void addGanttModelListener(GanttModelListener listener)
	{
		this.getGanttChartPane().getGanttModel().addGanttModelListener(listener);
	}
	
	
	public void removeGanttModelListener(GanttModelListener listener)
	{
		this.getGanttChartPane().getGanttModel().removeGanttModelListener(listener);
	}
	
	
	public void addGanttEntryRelationListener(GanttEntryRelationListener listener)
	{
		this.getGanttChartPane().getGanttModel().getGanttEntryRelationModel()
				.addGanttEntryRelationListener(listener);
	}
	
	
	public void removeGanttEntryRelationListener(GanttEntryRelationListener listener)
	{
		this.getGanttChartPane().getGanttModel().getGanttEntryRelationModel()
				.removeGanttEntryRelationListener(listener);
	}
	
	
	public void addListSelectionListener(ListSelectionListener listSelectionListener)
	{
		this.getGanttChart().getSelectionModel().addListSelectionListener(listSelectionListener);
	}
	
	
	public void removeListSelectionListener(ListSelectionListener listSelectionListener)
	{
		this.getGanttChart().getSelectionModel().removeListSelectionListener(listSelectionListener);
	}
}
