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


import java.awt.Component;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;

import com.jidesoft.gantt.GanttChart;
import com.jidesoft.gantt.GanttChartPane;
import com.jidesoft.grid.TreeTable;


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
 * <li>restore the previously split off components</li>
 * </ul>
 * </p>
 * 
 * @author XDEV Software jwill
 * @since 4.0
 */
public class GanttTemplateSplitHandler implements ComponentSplitHandler
{
	/**
	 * the previously split off component.
	 */
	private Component				removedContainer;
	
	/**
	 * the {@link GanttChartPane} component seperation is performed at.
	 */
	private GanttChartPane<?, ?>	ganttChartPane;
	
	
	/**
	 * Wraps some implementation logic that triggers when the
	 * {@link GanttChartPane} is split up.
	 * 
	 * @param ganttChartPane
	 *            the {@link GanttChartPane} component seperation is performed
	 *            at.
	 */
	public GanttTemplateSplitHandler(GanttChartPane<?, ?> ganttChartPane)
	{
		this.ganttChartPane = ganttChartPane;
		
		this.ganttChartPane.addContainerListener(new ContainerAdapter()
		{
			@Override
			public void componentRemoved(ContainerEvent e)
			{
				removedContainer = e.getChild();
			}
		});
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void splitAtComponent(Component component)
	{
		this.ganttChartPane.removePane(component);
		
		this.ganttChartPane.revalidate();
		this.ganttChartPane.repaint();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void splitAtIndex(int index)
	{
		this.ganttChartPane.removePane(index);
		
		this.ganttChartPane.revalidate();
		this.ganttChartPane.repaint();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Component getSeveredComponent()
	{
		return this.removedContainer;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void restoreAtIndex(int index)
	{
		if(this.getSeveredComponent() != null)
		{
			this.ganttChartPane.insertPane(this.getSeveredComponent(),index);
			
			this.ganttChartPane.revalidate();
			this.ganttChartPane.repaint();
		}
	}
	
}
