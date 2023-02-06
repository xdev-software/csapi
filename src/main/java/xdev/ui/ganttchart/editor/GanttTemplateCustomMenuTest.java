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


import xdev.ui.DefaultAction;
import xdev.ui.XdevMenu;
import xdev.ui.XdevMenuItem;
import xdev.ui.ganttchart.GanttResourceBundle;

import com.jidesoft.gantt.GanttEntry;


/**
 * Default implementation for {@link GanttChartTemplateTablePopup}.
 * 
 * @author XDEV Software jwill
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
 */
public class GanttTemplateCustomMenuTest extends XdevMenu
{
	/**
		 * 
		 */
	private static final long	serialVersionUID						= -7992140390585736584L;
	
	/**
	 * the default description for table popup menuitem - NEW.
	 */
	public static final String	TABLE_POPUP_MENUITEM_NEW_DESCRIPTION	= GanttResourceBundle
																				.getString(
																						"viewUpdater.tablenew",
																						GanttTemplateCustomMenuTest.class);
	
	/**
	 * the default description for table popup menuitem - DELETE.
	 */
	public static final String	TABLE_POPUP_MENUITEM_DELETE_DESCRIPTION	= GanttResourceBundle
																				.getString(
																						"viewUpdater.tabledelete",
																						GanttTemplateCustomMenuTest.class);
	
	private XdevMenuItem		menuItem2;
	
	
	/**
	 * 
	 * @param chart
	 *            the chart to observe.
	 * @param support
	 *            the support, for example to provide a new {@link GanttEntry}.
	 */
	public GanttTemplateCustomMenuTest()
	{
		super();
		this.setText("Custom menu");
		menuItem2 = new XdevMenuItem(new DefaultAction("Custom stuff",null,null,null,true));
		this.add(menuItem2);
	}
	
}
