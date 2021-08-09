package xdev.ui.ganttchart.template;

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


import java.awt.Component;
import java.awt.event.ContainerListener;

import com.jidesoft.gantt.GanttChart;
import com.jidesoft.grid.TreeTable;
import com.jidesoft.swing.JideSplitPane;


/**
 * Manages the runtime fragmentation of a {@link AbstractGanttTemplate}.
 * <p>
 * GanttChartTemplate is a vertical divided <code>SplitPane</code> which stores
 * a {@link TreeTable} and a {@link GanttChart} by default.
 * </p>
 * 
 * <p>
 * <ul>
 * <b>Enables the user to:</b>
 * <li>split up the template</li>
 * <li>get the split off component for restoring</li>
 * </ul>
 * </p>
 * 
 * @author XDEV Software jwill
 * @since 4.0
 */
public interface ComponentSplitHandler
{
	/**
	 * Splits of the given component from the GanttTemplate.
	 * 
	 * @param component
	 *            the <code>Component</code> to split off.
	 * 
	 * @see ContainerListener
	 * @see JideSplitPane#componentRemoved(java.awt.event.ContainerEvent)
	 */
	public void splitAtComponent(Component component);
	
	
	/**
	 * Splits off the according component at the given index from the
	 * GanttTemplate.
	 * 
	 * @param index
	 *            the component index, which indicates the component to split
	 *            off.
	 * 
	 * @see ContainerListener
	 * @see JideSplitPane#componentRemoved(java.awt.event.ContainerEvent)
	 */
	public void splitAtIndex(int index);
	
	
	/**
	 * /** Restores latest removed component from the template. Adds the
	 * component at the given layout index.
	 * 
	 * @param index
	 *            the layout index to add the component.
	 */
	public void restoreAtIndex(int index);
	
	
	/**
	 * Returns the split off component to restore it.
	 * 
	 * @see JideSplitPane#addPane(Component)
	 * 
	 * @return a previously split off component.
	 */
	public Component getSeveredComponent();
}
