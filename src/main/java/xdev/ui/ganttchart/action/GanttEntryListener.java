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
package xdev.ui.ganttchart.action;


import xdev.ui.ganttchart.UpdateableGanttEntry;
import xdev.ui.ganttchart.template.RelationalGanttTemplate;

import com.jidesoft.gantt.GanttChart;
import com.jidesoft.gantt.GanttEntry;
import com.jidesoft.gantt.GanttEntryRelation;
import com.jidesoft.gantt.GanttModel;
import com.jidesoft.gantt.GanttModelEvent;
import com.jidesoft.gantt.GanttModelListener;


/**
 * Listens to changes on {@link GanttEntry} structure in a {@link GanttChart}.
 * <p>
 * Also takes care of {@link GanttEntry} range restrictions forced through
 * {@link GanttEntryRelation}s.
 * </p>
 * 
 * 
 * @param <T>
 *            data type, either date or number format.
 * @param <S>
 *            {@link GanttEntry} type
 * 
 * @author XDEV Software JWill
 * @since 4.0
 */
public class GanttEntryListener<T extends Comparable<T>, S extends UpdateableGanttEntry<T>>
		implements GanttModelListener
{
	/**
	 * the {@link GanttModel} which provides information about
	 * {@link GanttEntry} structure in a {@link GanttChart}.
	 */
	private GanttModel<T, S>						model;
	
	private GanttRelationManagementStrategy<T, S>	strategy;
	/**
	 * the {@link GanttEntryController} which manages the propper handling of
	 * event actions.
	 */
	private GanttEntryController<T, S>				entryController;
	
	private RelationalGanttTemplate<T, S>			template;
	
	
	/**
	 * @param model
	 *            the <code>GanttChart</code>s model to relate the changed
	 *            entries which are to persist.
	 * 
	 * @param persistence
	 *            the <code>GanttChart</code>s persistence.
	 * 
	 */
	public GanttEntryListener(RelationalGanttTemplate<T, S> template)
	{
		this.model = template.getGanttChartPane().getGanttChart().getModel();
		this.template = template;
		this.initEntryController(this.model);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void ganttChartChanged(GanttModelEvent paramGanttModelEvent)
	{
		this.initEntryController(this.model);
		
		if(paramGanttModelEvent.getType() == GanttModelEvent.UPDATE)
		{
			int first = paramGanttModelEvent.getFirstRow();
			int last = paramGanttModelEvent.getLastRow();
			
			for(int j = 0; j < this.model.getEntryCount(); j++)
			{
				if(j >= first && j <= last)
				{
					final S entry = this.model.getEntryAt(j);
					if(!entry.isAdjusting())
					{
						this.entryController.updateEntry(entry);
					}
				}
			}
		}
		else if(paramGanttModelEvent.getType() == GanttModelEvent.INSERT)
		{
			S currentEntry = this.model.getEntryAt(paramGanttModelEvent.getLastRow());
			this.template.getGanttChart().setSelectedIndex(model.getIndexOf(currentEntry));
			
			this.entryController.insertEntry(currentEntry);
		}
		else if(paramGanttModelEvent.getType() == GanttModelEvent.DELETE)
		{
			// check here for possible root updates if jide does not
			// automatically handles it
		}
	}
	
	
	private void initEntryController(GanttModel<T, S> model)
	{
		if(this.template.isRelationValidation())
		{
			this.entryController = new ValidationGanttEntryController<T, S>(this.template,
					this.strategy);
		}
		else
		{
			this.entryController = new DefaultGanttEntryController<T, S>(this.template);
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public void setRelationManagementStrategy(
			GanttRelationManagementStrategy<T, S> relationManagementStrategy)
	{
		this.entryController.getRelationValidationStrategy()
				.setGanttEntryRelationManagementStrategy(relationManagementStrategy);
		this.strategy = relationManagementStrategy;
	}
	
}
