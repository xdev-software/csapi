package xdev.ui.ganttchart.model;

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


import java.util.Set;

import com.jidesoft.gantt.DefaultGanttEntryRelationModel;
import com.jidesoft.gantt.GanttEntry;
import com.jidesoft.gantt.GanttEntryRelation;
import com.jidesoft.gantt.GanttEntryRelationModel;

import xdev.ui.ganttchart.UpdateableGanttEntry;


/**
 * Workaround implementation for JIDES {@link GanttEntryRelationModel} because
 * no event is fired on entry removal.
 * 
 * 
 * @param <T>
 *            the gantt data type, for example <code>Date</code> or
 *            <code>Integer</code>.
 *            <p>
 *            Used for entry scaling via {@link GanttEntry#getRange()}
 *            </p>
 * @param <S>
 *            the customized {@link GanttEntry} type.
 * 
 * @author XDEV Software jwill
 * @since 4.0
 */
public class XdevGanttEntryRelationModel<T, S extends UpdateableGanttEntry<T>> extends
		DefaultGanttEntryRelationModel<S>
{
	/**
	 * the serialization id.
	 */
	private static final long	serialVersionUID	= 1514636929819934463L;
	
	
	/**
	 * Initializes {@link GanttEntryRelationModel} with default constructor.
	 */
	public XdevGanttEntryRelationModel()
	{
		super();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeEntryRelation(final GanttEntryRelation<S> paramGanttEntryRelation)
	{
		if(paramGanttEntryRelation == null)
		{
			return;
		}
		final Set<?> localSet1 = this._relations.get(paramGanttEntryRelation.getPredecessorEntry());
		final Set<?> localSet2 = this._relations.get(paramGanttEntryRelation.getSuccessorEntry());
		if(localSet1 != null)
		{
			localSet1.remove(paramGanttEntryRelation);
		}
		if(localSet2 != null)
		{
			localSet2.remove(paramGanttEntryRelation);
		}
		
		this.fireGanttEntryRealtionRemoved(paramGanttEntryRelation);
	}
}
