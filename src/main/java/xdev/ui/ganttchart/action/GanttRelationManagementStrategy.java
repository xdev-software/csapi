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


import com.jidesoft.gantt.GanttEntryRelation;

import xdev.ui.ganttchart.UpdateableGanttEntry;


/**
 * Gantt relation management strategy which determines how invalid gantt
 * relations are handled.
 * 
 * 
 * @param <T>
 * @param <S>
 * 
 * @see UIRelationManagementStrategy
 * @see UpdateDateRelationManagementStrategy
 * 
 * @author XDEV Software jwill
 * @since 4.0
 */
public interface GanttRelationManagementStrategy<T, S extends UpdateableGanttEntry<T>>
{
	public boolean manageRelation(GanttEntryRelation<S> relation);
}
