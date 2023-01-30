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
package xdev.ui.ganttchart.action;


import java.util.Date;

import xdev.ui.ganttchart.UpdateableGanttEntry;
import xdev.ui.ganttchart.utils.XdevGanttUtils;
import xdev.util.Duration;
import xdev.util.XdevDate;

import com.jidesoft.gantt.GanttChart;
import com.jidesoft.gantt.GanttEntry;
import com.jidesoft.gantt.GanttEntryRelation;
import com.jidesoft.gantt.GanttModel;
import com.jidesoft.range.Range;
import com.jidesoft.range.TimeRange;


/**
 * A concrete implementation of {@link GanttRelationManagementStrategy} which
 * automatically updates all the concerned {@link GanttEntry} start/end data to
 * valid values, if the relation validation failed.
 * <p>
 * Is used as default implementation in <code>Date</code> based
 * GanttChartTemplates.
 * </p>
 * 
 * <p>
 * The {@link Date} typed implementation because of date specific implementation
 * details.
 * </p>
 * 
 * 
 * @param <S>
 *            the customized {@link GanttEntry} type.
 * 
 * @author XDEV Software jwill
 * @since 4.0
 */
public class UpdateDateRelationManagementStrategy<S extends UpdateableGanttEntry<Date>> extends
		AbstractRelationManagementStrategy<Date, S>
{
	
	/**
	 * Initialy validates the existing relations within the {@link GanttChart}.
	 * 
	 * @param chart
	 *            the <code>Chart</code> to validate.
	 */
	public UpdateDateRelationManagementStrategy(GanttModel<Date, S> model)
	{
		super(model);
	}
	
	
	public UpdateDateRelationManagementStrategy()
	{
		super();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean manageRelation(GanttEntryRelation<S> relation)
	{
		S pre = relation.getPredecessorEntry();
		S suc = relation.getSuccessorEntry();
		int type = relation.getRelationType();
		
		switch(type)
		{
		// if the precessor end is not lower, then the sucessor
		// start
		// the sucessor start must be set to precessor end.
			case GanttEntryRelation.ENTRY_RELATION_FINISH_TO_START:
			{
				if(!new FtSRelationManagementController<Date, S>().validateEntryRelation(pre,suc))
				{
					suc.setRange(new TimeRange(pre.getRange().upper(),this.getUpdatedEndDate(
							suc.getRange(),pre.getRange().upper())));
					
					for(int i = 0; i < suc.getChildrenCount(); i++)
					{
						// Have to cast here because of jide typing. The childs
						// should be always the same type.
						@SuppressWarnings("unchecked")
						S childEntry = (S)suc.getChildAt(i);
						
						if(!new FtSRelationManagementController<Date, S>().validateEntryRelation(
								pre,childEntry))
						{
							childEntry.setRange(new TimeRange(pre.getRange().upper(),
									this.getUpdatedEndDate(childEntry.getRange(),pre.getRange()
											.upper())));
						}
					}
				}
				return true;
			}
			// if the precessor start is not lower, then the sucessor
			// start
			// the precessor start must be set to precessor start.
			case GanttEntryRelation.ENTRY_RELATION_START_TO_START:
			{
				if(!new StSRelationManagementController<Date, S>().validateEntryRelation(pre,suc))
				{
					suc.setRange(new TimeRange(pre.getRange().lower(),this.getUpdatedEndDate(
							suc.getRange(),pre.getRange().lower())));
					
					for(int i = 0; i < suc.getChildrenCount(); i++)
					{
						// Have to cast here because of jide typing. The childs
						// should be always the same type.
						@SuppressWarnings("unchecked")
						S childEntry = (S)suc.getChildAt(i);
						
						if(!new StSRelationManagementController<Date, S>().validateEntryRelation(
								pre,childEntry))
						{
							childEntry.setRange(new TimeRange(pre.getRange().lower(),
									this.getUpdatedEndDate(childEntry.getRange(),pre.getRange()
											.lower())));
						}
					}
				}
				return true;
			}
			// if the sucessor end is not lower, then the precessor
			// end
			// the sucessor end must be set to precessor end.
			case GanttEntryRelation.ENTRY_RELATION_FINISH_TO_FINISH:
			{
				if(!new FtFRelationManagementController<Date, S>().validateEntryRelation(pre,suc))
				{
					suc.setRange(new TimeRange(this.getUpdatedStartDate(suc.getRange(),pre
							.getRange().upper()),pre.getRange().upper()));
					
					if(suc.getChildrenCount() > 0)
					{
						S relevantChildEntry = XdevGanttUtils.getMaxDateEntry(XdevGanttUtils
								.getChildEntries(suc));
						if(!new FtFRelationManagementController<Date, S>().validateEntryRelation(
								pre,relevantChildEntry))
						{
							relevantChildEntry.setRange(new TimeRange(this.getUpdatedStartDate(
									relevantChildEntry.getRange(),pre.getRange().upper()),pre
									.getRange().upper()));
						}
					}
				}
				return true;
			}
			// if the precessor start is not lower, then the sucessor
			// end
			// the sucessor end must be set to precessor start.
			case GanttEntryRelation.ENTRY_RELATION_START_TO_FINISH:
			{
				if(!new StFRelationManagementController<Date, S>().validateEntryRelation(pre,suc))
				{
					suc.setRange(new TimeRange(this.getUpdatedStartDate(suc.getRange(),pre
							.getRange().lower()),pre.getRange().lower()));
					
					if(suc.getChildrenCount() > 0)
					{
						S relevantChildEntry = XdevGanttUtils.getMaxDateEntry(XdevGanttUtils
								.getChildEntries(suc));
						if(!new StFRelationManagementController<Date, S>().validateEntryRelation(
								pre,relevantChildEntry))
						{
							relevantChildEntry.setRange(new TimeRange(this.getUpdatedStartDate(
									relevantChildEntry.getRange(),pre.getRange().lower()),pre
									.getRange().lower()));
						}
					}
				}
				return true;
			}
			// default:
			// throw new RuntimeException("The relationtype is not registered");
		}
		return false;
		
	}
	
	
	/**
	 * Calculates the new end date, relative to the previous range and the given
	 * start date.
	 * 
	 * @param entryRange
	 *            the previous set range (start until end).
	 * @param newStart
	 *            the new start date.
	 * 
	 * @return the new end date, relative to the new start date.
	 */
	private Date getUpdatedEndDate(Range<Date> entryRange, Date newStart)
	{
		XdevDate newTime = new XdevDate(newStart);
		
		XdevDate xstart = new XdevDate(entryRange.lower());
		XdevDate xend = new XdevDate(entryRange.upper());
		
		Duration span = new Duration(xstart,xend);
		
		newTime.rollForward(span);
		
		return new Date(newTime.getTimeInMillis());
		
	}
	
	
	/**
	 * Calculates the new start date, relative to the previous range and the
	 * given end date.
	 * 
	 * @param entryRange
	 *            the previous set range (start until end).
	 * @param newEnd
	 *            the new end date.
	 * 
	 * @return the new start date, relative to the new end date.
	 */
	private Date getUpdatedStartDate(Range<Date> entryRange, Date newEnd)
	{
		
		XdevDate newTime = new XdevDate(newEnd);
		
		XdevDate xstart = new XdevDate(entryRange.lower());
		XdevDate xend = new XdevDate(entryRange.upper());
		
		Duration span = new Duration(xstart,xend);
		
		newTime.rollBack(span);
		
		return new Date(newTime.getTimeInMillis());

	}
	
}
