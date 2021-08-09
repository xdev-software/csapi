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


import java.util.Date;
import java.util.List;

import xdev.ui.ganttchart.UpdateableGanttEntry;
import xdev.ui.ganttchart.utils.XdevGanttUtils;

import com.jidesoft.gantt.GanttModel;
import com.jidesoft.gantt.GanttModelEvent;
import com.jidesoft.gantt.GanttModelListener;
import com.jidesoft.scale.ScaleArea;


/**
 * 
 * @author XDEV Software jwill
 * @since 4.0
 */
public class GanttDateScaleAreaUpdater implements GanttModelListener
{
	
	private GanttModel<Date, UpdateableGanttEntry<Date>>	model;
	private ScaleArea<Date>									dateScaleArea;
	
	
	public GanttDateScaleAreaUpdater(GanttModel<Date, UpdateableGanttEntry<Date>> model,
			ScaleArea<Date> dateScaleArea)
	{
		this.model = model;
		this.dateScaleArea = dateScaleArea;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void ganttChartChanged(GanttModelEvent arg0)
	{
		if(model.getEntryAt(arg0.getFirstRow()) != null)
		{
			if(!model.getEntryAt(arg0.getFirstRow()).isAdjusting())
			{
				List<UpdateableGanttEntry<Date>> entries = XdevGanttUtils.getEntries(this.model);
				
				Date minRange = XdevGanttUtils.getMinDate(entries);
				Date maxRange = XdevGanttUtils.getMaxDate(entries);
				
				if(arg0.getType() != GanttModelEvent.UPDATE)
				{
					this.dateScaleArea.setStart(minRange);
					this.dateScaleArea.setEnd(maxRange);
				}
			}
		}
	}
}
