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


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import xdev.ui.ganttchart.UpdateableGanttEntry;
import xdev.ui.ganttchart.editor.GanttEntryRelationRemoveNotifier;
import xdev.ui.ganttchart.template.RelationalGanttTemplate;

import com.jidesoft.gantt.GanttChart;
import com.jidesoft.gantt.GanttEntry;
import com.jidesoft.gantt.GanttEntryRelation;
import com.jidesoft.gantt.GanttModel;


/**
 * Listens to specific UI-actions on {@link GanttEntry}s and delegates remove
 * commands if {@link GanttEntryRelation}s exist.
 * 
 * 
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
public class GanttRelationUIDeleteCommandListener<T extends Comparable<T>, S extends UpdateableGanttEntry<T>>
		implements ActionListener
{
	/**
	 * the {@link GanttModel} to get the selected {@link GanttEntry} from.
	 */
	private GanttModel<T, S>	model;
	
	/**
	 * the {@link GanttChart} for after invoke actions.
	 */
	private GanttChart<T, S>	chart;
	
	/**
	 * the selected {@link GanttEntry}.
	 */
	private S					entry1	= null;
	
	
	/**
	 * @param ganttChart
	 *            the {@link GanttChart} for entry selection purpose.
	 */
	public GanttRelationUIDeleteCommandListener(RelationalGanttTemplate<T, S> template)
	{
		this.chart = template.getGanttChartPane().getGanttChart();
		this.model = template.getGanttChartPane().getGanttChart().getModel();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		this.entry1 = chart.getEntryAt(this.chart.getSelectedRow());
		
		if(entry1 != null)
		{
			
			GanttEntryRelationRemoveNotifier removeNotifier = new GanttEntryRelationRemoveNotifier();
			
			removeNotifier.pack();
			removeNotifier.setVisible(true);
			
			Integer returnValue = null;
			
			if(removeNotifier.getOptionPane().getValue() instanceof Integer)
			{
				returnValue = ((Integer)removeNotifier.getOptionPane().getValue()).intValue();
				
				if(returnValue == JOptionPane.YES_OPTION)
				{
					model.getGanttEntryRelationModel().removeEntryRelations(entry1);
				}
			}
		}
	}
}
