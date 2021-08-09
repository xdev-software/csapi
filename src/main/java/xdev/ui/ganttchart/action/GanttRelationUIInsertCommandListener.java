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


import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import xdev.lang.XDEV;
import xdev.lang.cmd.OpenWindow;
import xdev.ui.XdevWindow;
import xdev.ui.ganttchart.UpdateableGanttEntry;
import xdev.ui.ganttchart.editor.XdevRelationTypeDialog;
import xdev.ui.ganttchart.template.RelationalGanttTemplate;

import com.jidesoft.gantt.DefaultGanttEntryRelation;
import com.jidesoft.gantt.GanttChart;
import com.jidesoft.gantt.GanttEntry;
import com.jidesoft.gantt.GanttEntryRelation;
import com.jidesoft.gantt.GanttModel;


/**
 * 
 * Manages the dis/-connection betweeen {@link GanttEntry}s.
 * <p>
 * This concrete implementation validates the {@link GanttEntryRelation}.
 * </p>
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
public class GanttRelationUIInsertCommandListener<T extends Comparable<T>, S extends UpdateableGanttEntry<T>>
		implements ListSelectionListener
{
	
	/**
	 * the {@link GanttModel} to get the selected entries from.
	 */
	private GanttModel<T, S>				model;
	
	/**
	 * the {@link GanttChart} to get the selection from.
	 */
	private GanttChart<T, S>				chart;
	
	/**
	 * the precessor entry of the {@link GanttEntryRelation}.
	 */
	private S								precessorEntry		= null;
	
	/**
	 * the sucessor entry of the {@link GanttEntryRelation}.
	 */
	private S								sucessorEntry		= null;
	
	/**
	 * the relation type of the {@link GanttEntryRelation}.
	 */
	private Integer							relationType		= null;
	
	private RelationalGanttTemplate<T, S>	template			= null;
	
	/**
	 * the counter which indicates the minimum selection count of
	 * {@link GanttEntry}s to create a relation.
	 */
	private static final int				CONNECTION_COUNT	= 2;
	
	
	/**
	 * 
	 * @param template
	 *            the {@link RelationalGanttTemplate} that gains
	 *            {@link GanttEntryRelation} creation functionality.
	 */
	public GanttRelationUIInsertCommandListener(RelationalGanttTemplate<T, S> template)
	{
		this.chart = template.getGanttChartPane().getGanttChart();
		this.model = template.getGanttChartPane().getGanttChart().getModel();
		this.template = template;
	}
	
	
	/**
	 * Invokes XDEV.Window in modal mode.
	 * 
	 * @param dialog
	 *            the <code>XdevWindow</code> to open.
	 */
	private void openDialog(final XdevWindow dialog)
	{
		XDEV.OpenWindow(new OpenWindow()
		{
			@Override
			public void init()
			{
				setXdevWindow(dialog);
				setContainerType(ContainerType.DIALOG);
				setModal(true);
			}
		});
	}
	
	
	/**
	 * removes this {@link ListSelectionListener} from the {@link GanttChart}s
	 * selection model.
	 */
	private void disableConnectionMode()
	{
		this.chart.getSelectionModel().removeListSelectionListener(this);
	}
	
	
	// take care of subentrys with row entry selection - or use
	// getEntryAtPoint()
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void valueChanged(final ListSelectionEvent e)
	{
		int[] selectedRows = chart.getSelectedRows();
		
		if(selectedRows != null && selectedRows.length == CONNECTION_COUNT)
		{
			// invoke later for proper selection painting
			// the last selected rowindex
			int sucessor = chart.getLeadSelectionIndex();
			
			// the first selected rowindex
			int precessor = getAnchorRow(sucessor,chart.getSelectedRows());
			
			precessorEntry = chart.getEntryAt(precessor);
			sucessorEntry = chart.getEntryAt(sucessor);
			
			final XdevRelationTypeDialog dialog = XdevRelationTypeDialog.getInstance();
			openDialog(dialog);
			
			relationType = dialog.getSelectedRelationType();
			
			// no relation selected
			if(relationType != null)
			{
				final GanttEntryRelation<S> relation = new DefaultGanttEntryRelation<S>(
						precessorEntry,sucessorEntry,relationType);
				
				SwingUtilities.invokeLater(new Runnable()
				{
					
					@Override
					public void run()
					{
						if(template.isRelationValidation())
						{
							// avoid drawing if created relation is
							// invalid
							if(new UIRelationManagementStrategy<T, S>().manageRelation(relation))
							{
								model.getGanttEntryRelationModel().addEntryRelation(relation);
							}
						}
						else
						{
							model.getGanttEntryRelationModel().addEntryRelation(relation);
						}
					}
				});
			}
			
			chart.clearSelection();
			
			disableConnectionMode();
			template.getGanttChartPane().getTreeTable().setExpandable(true);
		}
	}
	
	
	/**
	 * Returns the anchor row of the given selected rows, dedicated by the
	 * leadRow.
	 * 
	 * @param leadRow
	 *            the current lead row.
	 * @param selectedRows
	 *            the selected rows.
	 * @return the anchor row.
	 * 
	 * @throws NullPointerException
	 *             if no anchor row was found.
	 */
	private int getAnchorRow(int leadRow, int... selectedRows)
	{
		for(int i = 0; i < selectedRows.length; i++)
		{
			if(selectedRows[i] != leadRow)
			{
				return selectedRows[i];
			}
		}
		throw new NullPointerException("No anchor row found.");
	}
}
