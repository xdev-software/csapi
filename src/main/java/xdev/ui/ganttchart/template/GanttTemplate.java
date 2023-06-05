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
import xdev.ui.ganttchart.model.GanttPersistence;

import com.jidesoft.gantt.GanttChart;
import com.jidesoft.gantt.GanttChartPane;
import com.jidesoft.gantt.GanttEntry;
import com.jidesoft.grid.TreeTable;
import com.jidesoft.scale.ScaleModel;


/**
 * {@link GanttTemplate} provides a summarized and simplified way to use a
 * {@link GanttChart} implicated with persistence.
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
public interface GanttTemplate<T, S extends UpdateableGanttEntry<T>>
{
	
	public void setGanttPersistence(GanttPersistence<T, S> persistence);
	
	
	public GanttPersistence<T, S> getGanttPersistence();
	
	
	public boolean isPersistenceEnabled();
	
	
	public ScaleModel<T> getScaleModel();
	
	
	/**
	 * Returns the {@link GanttChartPane} which is used to display a
	 * {@link TreeTable} and a {@link GanttChart} as UI representation of the
	 * project data.
	 * 
	 * @return the <code>GanttChartPane</code>
	 */
	public GanttChartPane<T, S> getGanttChartPane();
	
	
	public void setGanttChartPane(GanttChartPane<T, S> ganttChartPane);
	
	
	public GanttChart<T, S> getGanttChart();
	
	/**
	 * Property constant for "editable".
	 */
	public final static String	EDITABLE_PROPERTY	= "editable";
}
