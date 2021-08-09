package xdev.ui.ganttchart;

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


import com.jidesoft.gantt.DefaultGanttEntry;
import com.jidesoft.gantt.GanttEntry;
import com.jidesoft.grid.TreeTableModel;
import com.jidesoft.scale.ScaleModel;


/**
 * @param <T>
 *            the gantt data type, for example <code>Date</code> or
 *            <code>Integer</code>.
 *            <p>
 *            Used for entry scaling via {@link GanttEntry#getRange()}
 *            </p>
 * @author XDEV Software jwill
 * @since 4.0
 */
public interface GanttModelUIInformation<T>
{
	/**
	 * Sets a custom {@link ScaleModel} refered to the data type <code>T</code>
	 * (see class description).
	 * 
	 * @param scaleModel
	 *            the scalemodel which makes it possible to render the
	 *            {@link GanttEntry}s start and end.
	 */
	public void setScaleModel(ScaleModel<T> scaleModel);
	
	
	/**
	 * Sets a custom {@link TreeTableModel}.
	 * 
	 * @param treeTableModel
	 *            the {@link TreeTableModel} for advanced UI analytic support.
	 */
	public void setTreeTableModel(TreeTableModel<DefaultGanttEntry<T>> treeTableModel);
	
	
	/**
	 * Returns the {@link ScaleModel}.
	 * 
	 * @return the {@link ScaleModel}
	 */
	public ScaleModel<T> getScaleModel();
	
	
	/**
	 * Returns the customized {@link TreeTableModel}.
	 * 
	 * @return the customized {@link TreeTableModel}
	 */
	public TreeTableModel<DefaultGanttEntry<T>> getTreeTableModel();
}
