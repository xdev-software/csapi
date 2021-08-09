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
 * {@link VirtualTableColumn} is a typed data wrapper, to avoid lots of casting
 * using storage maps, the XdevGanttChart API uses this data container.
 * <p>
 * Concrete mandatory GanttData is stored as {@link VirtualTableColumn} to avoid
 * cast exceptions when creating {@link GanttEntry}.
 * </p>
 * 
 * @author XDEV Software jwill
 * @since 4.0
 */

public class XdevGanttRelationVTWrapper
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
	 * @param vt
	 *            the {@link VirtualTable} that includes the relevant
	 *            {@link GanttEntry} fields.
	 */
	public XdevGanttRelationVTWrapper(VirtualTable vt)
	{
		this.relatedVT = vt;
	}
	
	/**
	 * the unique id.
	 */
	VirtualTableColumn<?>		id;
	
	/**
	 * the entry id.
	 */
	VirtualTableColumn<?>		entryId;
	
	/**
	 * the relation type id.
	 */
	VirtualTableColumn<Integer>	relationId;
	
	/**
	 * the root id (tree hierarchy) of the entry.
	 */
	VirtualTableColumn<?>		relationRoot;
	
	
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
	 * @return the entryId
	 */
	public VirtualTableColumn<?> getEntryId()
	{
		return entryId;
	}
	
	
	/**
	 * @param entryId
	 *            the entryId to set
	 */
	public void setEntryId(VirtualTableColumn<?> entryId)
	{
		this.entryId = entryId;
	}
	
	
	/**
	 * @return the relationType
	 */
	public VirtualTableColumn<Integer> getRelationType()
	{
		return relationId;
	}
	
	
	/**
	 * @param relationType
	 *            the relationType to set
	 */
	public void setRelationType(VirtualTableColumn<Integer> relationType)
	{
		this.relationId = relationType;
	}
	
	
	/**
	 * @return the relationRoot
	 */
	public VirtualTableColumn<?> getRelationRoot()
	{
		return relationRoot;
	}
	
	
	/**
	 * @param relationRoot
	 *            the relationRoot to set
	 */
	public void setRelationRoot(VirtualTableColumn<?> relationRoot)
	{
		this.relationRoot = relationRoot;
	}
}
