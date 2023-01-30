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


import xdev.ui.UIUtils;
import xdev.ui.UIUtils.MessageDialogType;
import xdev.ui.ganttchart.GanttResourceBundle;
import xdev.ui.ganttchart.UpdateableGanttEntry;

import com.jidesoft.gantt.DefaultGanttEntryRelation;
import com.jidesoft.gantt.GanttChart;
import com.jidesoft.gantt.GanttEntry;
import com.jidesoft.gantt.GanttEntryRelation;
import com.jidesoft.gantt.GanttModel;


/**
 * The default implementation of {@link GanttRelationManagementStrategy} which
 * notifies the user if the relation validation failed.
 * 
 * @param <T>
 *            the gantt data type, for example <code>Date</code> or
 *            <code>Integer</code>. Has to extend {@link Comparable} for
 *            validation purpose.
 *            <p>
 *            Used for entry scaling via {@link GanttEntry#getRange()}
 *            </p>
 * @param <S>
 *            the customized {@link GanttEntry} type. .
 * 
 * @author XDEV Software jwill
 * @since 4.0
 */
public class UIRelationManagementStrategy<T extends Comparable<T>, S extends UpdateableGanttEntry<T>>
		extends AbstractRelationManagementStrategy<T, S> implements
		GanttRelationManagementStrategy<T, S>
{
	
	/**
	 * Initialy validates the existing relations within the {@link GanttChart}.
	 * 
	 * @param chart
	 *            the <code>Chart</code> to validate.
	 */
	public UIRelationManagementStrategy(GanttModel<T, S> model)
	{
		super(model);
	}
	
	
	public UIRelationManagementStrategy()
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
			case DefaultGanttEntryRelation.ENTRY_RELATION_FINISH_TO_START:
			{
				if(!new FtSRelationManagementController<T, S>().validateEntryRelation(pre,suc))
				{
					UIUtils.showMessage(GanttResourceBundle.getString("relationnotifier.title"),
							GanttResourceBundle.getString("relationmanagement.error0"),
							MessageDialogType.INFORMATION_MESSAGE);
					return false;
				}
				else
				{
					return true;
				}
			}
			case DefaultGanttEntryRelation.ENTRY_RELATION_START_TO_START:
			{
				if(!new StSRelationManagementController<T, S>().validateEntryRelation(pre,suc))
				{
					UIUtils.showMessage(GanttResourceBundle.getString("relationnotifier.title"),
							GanttResourceBundle.getString("relationmanagement.error1"),
							MessageDialogType.INFORMATION_MESSAGE);
					return false;
				}
				else
				{
					return true;
				}
				
			}
			case DefaultGanttEntryRelation.ENTRY_RELATION_FINISH_TO_FINISH:
			{
				if(!new FtFRelationManagementController<T, S>().validateEntryRelation(pre,suc))
				{
					UIUtils.showMessage(GanttResourceBundle.getString("relationnotifier.title"),
							GanttResourceBundle.getString("relationmanagement.error2"),
							MessageDialogType.INFORMATION_MESSAGE);
					return false;
				}
				else
				{
					return true;
				}
			}
			case DefaultGanttEntryRelation.ENTRY_RELATION_START_TO_FINISH:
			{
				if(!new StFRelationManagementController<T, S>().validateEntryRelation(pre,suc))
				{
					UIUtils.showMessage(GanttResourceBundle.getString("relationnotifier.title"),
							GanttResourceBundle.getString("relationmanagement.error3"),
							MessageDialogType.INFORMATION_MESSAGE);
					return false;
				}
				else
				{
					return true;
				}
			}
		}
		throw new RuntimeException("The relationtype is not registered");
	}
}
