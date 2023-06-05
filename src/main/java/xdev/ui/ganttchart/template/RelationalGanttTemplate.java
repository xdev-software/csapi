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


import xdev.ui.ganttchart.UpdateableGanttEntry;
import xdev.ui.ganttchart.action.GanttEntryRelationManager;
import xdev.ui.ganttchart.model.GanttRelationPersistence;

import com.jidesoft.gantt.GanttChart;
import com.jidesoft.gantt.GanttEntry;
import com.jidesoft.gantt.GanttEntryRelation;


/**
 * {@link RelationalGanttTemplate} provides a summarized and simplified way to
 * use a {@link GanttChart} implicated with persistence features.
 * <p>
 * Also prepares the possibility to create relations between
 * <code>gantt entries</code>.
 * </p>
 * 
 * <p>
 * <b>Including convertible:</b>
 * <ul>
 * <li> {@link GanttChartTemplateTablePopup}</li>
 * <li> {@link ComponentSplitHandler}</li>
 * <li> {@link GanttEntryEditor}/creator</li>
 * </ul>
 * </p>
 * 
 * <p>
 * Additionally enables the user to create and validate
 * {@link GanttEntryRelation}s.
 * </p>
 * 
 * @param <S>
 *            the customized {@link GanttEntry} type. .
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
public interface RelationalGanttTemplate<T, S extends UpdateableGanttEntry<T>> extends
		GanttTemplate<T, S>, GanttEntryRelationManager<T, S>
{
	public static final String	PROPERTY_RELATION_ENABLED	= "relationsEnabled";
	
	
	public void setGanttRelationPersistence(GanttRelationPersistence<S> relationPersistence);
	
	
	public GanttRelationPersistence<S> getGanttRelationPersistence();
	
	
	/**
	 * Predicates whether the relation validation is enabled or not.
	 * 
	 * @return the relation validation flag.
	 */
	public boolean isRelationValidation();
	
	
	/**
	 * Enables validation of upcoming {@link GanttEntryRelation}s.
	 * 
	 * @param relationInteraction
	 *            the relation validation flag to set.
	 */
	public void setRelationValidation(boolean relationInteraction);
}
