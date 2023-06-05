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
package xdev.ui.ganttchart.editor;


import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.JMenu;
import javax.swing.JPopupMenu;

import com.jidesoft.gantt.GanttChart;
import com.jidesoft.gantt.GanttChartPopupMenuInstaller;


public class XdevGanttChartPopupMenuInstaller extends GanttChartPopupMenuInstaller
{
	
	private JMenu	menu;
	
	
	public XdevGanttChartPopupMenuInstaller(GanttChart<?, ?> chart)
	{
		super(chart);
	}
	
	
	public XdevGanttChartPopupMenuInstaller(GanttChart<?, ?> chart, JMenu menu)
	{
		super(chart);
		this.menu = menu;
	}
	
	
	@Override
	public void mousePressed(MouseEvent e)
	{
		showPopup(e);
	}
	
	
	@Override
	public void mouseReleased(MouseEvent e)
	{
		showPopup(e);
	}
	
	
	private void showPopup(final MouseEvent e)
	{
		if(!e.isPopupTrigger())
		{
			return;
		}
		
		GanttChart<?, ?> ganttChart = (GanttChart<?, ?>)e.getComponent();
		
		JPopupMenu popup = createPopupMenu();
		
		if(this.menu != null)
		{
			popup.add(this.menu);
		}
		
		int clickingRow = ganttChart.rowAtPoint(e.getPoint());
		Rectangle entryRect = ganttChart.getEntryRect(clickingRow);
		if(entryRect != null)
		{
			customizeMenuItems(ganttChart,popup,clickingRow,entryRect.getLocation());
			if(popup.getComponentCount() > 0)
			{
				popup.show(
						ganttChart,
						entryRect.getBounds().x
								- (int)Math.round(popup.getPreferredSize().getWidth()),
						entryRect.getBounds().y);
			}
		}
		else
		{
			customizeMenuItems(ganttChart,popup,clickingRow,e.getPoint());
			if(popup.getComponentCount() > 0)
			{
				popup.show(ganttChart,e.getX(),e.getY());
			}
		}
	}
	
}
