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


import com.jidesoft.gantt.GanttChartPane;


/**
 * Component which defines the ability to detach parts of itself.
 * 
 * @since 4.0
 * @author XDEV Software jwill
 */
public interface SplitableComponent
{
	/**
	 * Sets the {@link ComponentSplitHandler} that understands to split the
	 * {@link GanttChartPane}s UI.
	 * <p>
	 * A common use case would be for example the seperation of the
	 * <code>TreeTable</code> from the <code>GanttChart</code>
	 * </p>
	 * 
	 * @param splitHandler
	 *            the split handler to use.
	 */
	public void setComponentSplitHandler(ComponentSplitHandler splitHandler);
	
	
	/**
	 * Returns the {@link ComponentSplitHandler} that understands to split the
	 * {@link GanttChartPane}s UI.
	 * 
	 * @return the splitHandler which is currently used.
	 */
	public ComponentSplitHandler getComponentSplitHandler();
}
