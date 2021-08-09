package xdev.ui.ganttchart.model;

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


import xdev.ui.ganttchart.GanttModelUIInformation;

import com.jidesoft.gantt.DefaultGanttEntry;
import com.jidesoft.gantt.GanttEntry;
import com.jidesoft.grid.TreeTableModel;
import com.jidesoft.scale.ScaleModel;


/**
 * This concrete {@link GanttModelUIInformation} implementation stores a
 * {@link ScaleModel} and a {@link TreeTableModel} to upgrade the UI on demand.
 * 
 * @author XDEV Software jwill
 * @since 4.0
 * 
 * @param <T>
 *            the gantt data type, for example <code>Date</code> or
 *            <code>Integer</code>.
 *            <p>
 *            Used for entry scaling via {@link GanttEntry#getRange()}
 *            </p>
 */
public class XdevGanttModelUIInformation<T> implements GanttModelUIInformation<T>
{
	
	/**
	 * a custom {@link TreeTableModel}.
	 */
	private TreeTableModel<DefaultGanttEntry<T>>	treeTableModel;
	
	/**
	 * a custom data type based {@link ScaleModel}.
	 */
	private ScaleModel<T>							scaleModel;
	
	
	/**
	 * {@inheritDoc}
	 */
	public ScaleModel<T> getScaleModel()
	{
		return this.scaleModel;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public TreeTableModel<DefaultGanttEntry<T>> getTreeTableModel()
	{
		return treeTableModel;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public void setScaleModel(ScaleModel<T> scaleModel)
	{
		this.scaleModel = scaleModel;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public void setTreeTableModel(TreeTableModel<DefaultGanttEntry<T>> treeTableModel)
	{
		this.treeTableModel = treeTableModel;
	}
	
}
