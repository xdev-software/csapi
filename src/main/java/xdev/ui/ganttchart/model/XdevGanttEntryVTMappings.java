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


import xdev.vt.VirtualTable;
import xdev.vt.VirtualTableColumn;

import com.jidesoft.gantt.GanttEntry;


/**
 * 
 * 
 * 
 * @author XDEV Software jwill
 * @since 4.0
 * 
 * @param <T>
 *            the concrete Gantt datatype, for example <code>Date</code> or
 *            <code>Integer</code>.
 */
public class XdevGanttEntryVTMappings<T>
{
	/**
	 * the related mapping {@link VirtualTable}.
	 */
	private VirtualTable	relatedVT;
	
	
	/**
	 * @return the relatedVT
	 */
	public VirtualTable getVirtualTable()
	{
		return relatedVT;
	}
	
	
	/**
	 * 
	 * @param ganttDataVT
	 *            the {@link VirtualTable} to map {@link GanttEntry} fields
	 *            with.
	 */
	public XdevGanttEntryVTMappings(VirtualTable ganttDataVT)
	{
		
		this.relatedVT = ganttDataVT;
	}
	
	/**
	 * the unique id.
	 */
	private VirtualTableColumn<?>		id;
	
	/**
	 * the start point.
	 */
	private VirtualTableColumn<T>		start;
	
	/**
	 * the end point.
	 */
	private VirtualTableColumn<T>		end;
	
	/**
	 * the description.
	 */
	private VirtualTableColumn<String>	description;
	
	/**
	 * the completion.
	 */
	private VirtualTableColumn<Double>	completion;
	
	/**
	 * the root id (tree hierarchy).
	 */
	private VirtualTableColumn<?>		root;
	
	
	/**
	 * @return the id
	 */
	public VirtualTableColumn<?> getId()
	{
		return id;
	}
	
	
	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(VirtualTableColumn<?> id)
	{
		this.id = id;
	}
	
	
	/**
	 * @return the start
	 */
	public VirtualTableColumn<T> getStart()
	{
		return start;
	}
	
	
	/**
	 * @param start
	 *            the start to set
	 */
	public void setStart(VirtualTableColumn<T> start)
	{
		this.start = start;
	}
	
	
	/**
	 * @return the end
	 */
	public VirtualTableColumn<T> getEnd()
	{
		return end;
	}
	
	
	/**
	 * @param end
	 *            the end to set
	 */
	public void setEnd(VirtualTableColumn<T> end)
	{
		this.end = end;
	}
	
	
	/**
	 * @return the description
	 */
	public VirtualTableColumn<String> getDescription()
	{
		return description;
	}
	
	
	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(VirtualTableColumn<String> description)
	{
		this.description = description;
	}
	
	
	/**
	 * @return the completion
	 */
	public VirtualTableColumn<Double> getCompletion()
	{
		return completion;
	}
	
	
	/**
	 * @param completion
	 *            the completion to set
	 */
	public void setCompletion(VirtualTableColumn<Double> completion)
	{
		this.completion = completion;
	}
	
	
	/**
	 * @return the root
	 */
	public VirtualTableColumn<?> getRoot()
	{
		return root;
	}
	
	
	/**
	 * @param root
	 *            the root to set
	 */
	public void setRoot(VirtualTableColumn<?> root)
	{
		this.root = root;
	}
}
