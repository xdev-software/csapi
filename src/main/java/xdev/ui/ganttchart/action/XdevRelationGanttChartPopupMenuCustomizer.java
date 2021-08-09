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


import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.MessageFormat;
import java.util.Set;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import xdev.ui.ganttchart.GanttResourceBundle;

import com.jidesoft.gantt.GanttChart;
import com.jidesoft.gantt.GanttChartPopupMenuCustomizer;
import com.jidesoft.gantt.GanttEntry;
import com.jidesoft.gantt.GanttEntryRelation;
import com.jidesoft.gantt.GanttEntryRelationModel;


/**
 * 
 * @author XDEV Software jwill
 * @since 4.0
 */
public class XdevRelationGanttChartPopupMenuCustomizer implements GanttChartPopupMenuCustomizer
{
	
	public <T, S extends GanttEntry<T>> void customizePopupMenu(final GanttChart<T, S> ganttChart,
			final JPopupMenu popup, final int clickingRow, final Point p)
	{
		S entry = ganttChart.getEntryAt(clickingRow);
		if(entry != null)
		{
			final GanttEntryRelationModel<S> relationModel = ganttChart.getModel()
					.getGanttEntryRelationModel();
			Set<GanttEntryRelation<S>> set = relationModel.getEntryRelations(entry);
			if(set.size() > 1)
			{
				for(final GanttEntryRelation<S> relation : set)
				{
					String mItemName = ganttChart.getResourceString("GanttChart.MenuItem.remove");
					JMenuItem removeRelationItem = new JMenuItem(
							GanttResourceBundle.loadResIcon("disconnect.png"));
					S ganttEntry = entry == relation.getSuccessorEntry() ? relation
							.getPredecessorEntry() : relation.getSuccessorEntry();
					mItemName += " " + ganttEntry.getName() + " "
							+ getRelationString(ganttChart,relation.getRelationType());
					removeRelationItem.addActionListener(new ActionListener()
					{
						@Override
						public void actionPerformed(ActionEvent e)
						{
							relationModel.removeEntryRelation(relation);
						}
					});
					removeRelationItem.setText(mItemName);
					popup.add(removeRelationItem);
				}
			}
			else if(set.size() == 1)
			{
				for(final GanttEntryRelation<S> relation : set)
				{
					S ganttEntry = entry == relation.getSuccessorEntry() ? relation
							.getPredecessorEntry() : relation.getSuccessorEntry();
					String miName = MessageFormat.format(
							ganttChart.getResourceString("GanttChart.MenuItem.removeRelation"),
							getRelationString(ganttChart,relation.getRelationType()),
							ganttEntry.getName());
					JMenuItem removeRelationItem = new JMenuItem(miName,
							GanttResourceBundle.loadResIcon("disconnect.png"));
					removeRelationItem.addActionListener(new ActionListener()
					{
						@Override
						public void actionPerformed(ActionEvent e)
						{
							relationModel.removeEntryRelation(relation);
						}
					});
					popup.add(removeRelationItem);
				}
			}
		}
	}
	
	
	protected String getRelationString(GanttChart<?, ?> chart, int relationType)
	{
		switch(relationType)
		{
			case GanttEntryRelation.ENTRY_RELATION_FINISH_TO_START:
				return chart.getResourceString("GanttChart.Relation.fs");
			case GanttEntryRelation.ENTRY_RELATION_START_TO_START:
				return chart.getResourceString("GanttChart.Relation.ss");
			case GanttEntryRelation.ENTRY_RELATION_FINISH_TO_FINISH:
				return chart.getResourceString("GanttChart.Relation.ff");
			case GanttEntryRelation.ENTRY_RELATION_START_TO_FINISH:
				return chart.getResourceString("GanttChart.Relation.sf");
		}
		return "";
	}
	
}
