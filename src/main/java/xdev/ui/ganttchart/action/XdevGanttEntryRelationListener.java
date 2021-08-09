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


import xdev.ui.ganttchart.UpdateableGanttEntry;
import xdev.ui.ganttchart.model.GanttRelationPersistence;
import xdev.ui.ganttchart.template.AbstractGanttTemplate;
import xdev.ui.ganttchart.template.RelationalGanttTemplate;

import com.jidesoft.gantt.GanttEntry;
import com.jidesoft.gantt.GanttEntryRelation;
import com.jidesoft.gantt.GanttEntryRelationEvent;
import com.jidesoft.gantt.GanttEntryRelationListener;


/**
 * 
 * 
 * 
 * @param <S>
 *            the customized {@link GanttEntry} type.
 * 
 * @author XDEV Software jwill
 * @since 4.0
 */
public class XdevGanttEntryRelationListener<T extends Comparable<T>, S extends UpdateableGanttEntry<T>>
		implements GanttEntryRelationListener
{
	private AbstractGanttTemplate<T, S>		template;
	
	private GanttRelationController<T, S>	controller;
	
	
	/**
	 * <p>
	 * Changes are transmitted to the given {@link GanttRelationPersistence}.
	 * </p>
	 * 
	 * @param persistence
	 *            the persistence which transmits changes to a dataSource.
	 */
	public XdevGanttEntryRelationListener(AbstractGanttTemplate<T, S> template)
	{
		this.template = template;
		this.initRelationController(this.template);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void ganttEntryRelationChanged(GanttEntryRelationEvent event)
	{
		this.initRelationController(this.template);
		
		// have to cast here because event#getGanttEntryRelation is <?>
		// typed
		@SuppressWarnings("unchecked")
		GanttEntryRelation<S> relation = (GanttEntryRelation<S>)event.getGanttEntryRelation();
		
		if(event.getType() == GanttEntryRelationEvent.ADD)
		{
			this.controller.insertEntryRelation(relation);
		}
		else if(event.getType() == GanttEntryRelationEvent.REMOVE)
		{
			this.controller.removeEntryRelation(relation);
		}
	}
	
	
	private void initRelationController(RelationalGanttTemplate<T, S> template)
	{
		if(this.template.isRelationValidation())
		{
			this.controller = new ValidationGanttRelationController<T, S>(this.template);
		}
		else
		{
			// no validation
			this.controller = new GanttRelationController.DefaultGanttRelationController<T, S>(
					this.template);
		}
	}
}
