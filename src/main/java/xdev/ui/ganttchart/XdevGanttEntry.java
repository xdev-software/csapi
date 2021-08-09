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


import xdev.util.ObjectUtils;

import com.jidesoft.gantt.DefaultGanttEntry;
import com.jidesoft.range.Range;


/**
 * 
 * 
 * @param <T>
 * 
 * @author XDEV Software jwill
 * @since 4.0
 */
public class XdevGanttEntry<T> extends DefaultGanttEntry<T> implements UpdateableGanttEntry<T>
{
	
	private Object	id;
	private Object	root;
	
	
	public XdevGanttEntry(String description)
	{
		super(description);
	}
	
	
	public XdevGanttEntry(String description, Range<T> range)
	{
		super(description,range);
	}
	
	
	public XdevGanttEntry(String description, Class<T> rangeClass, Range<T> range, double completion)
	{
		super(description,rangeClass,range,completion);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setId(Object id)
	{
		this.id = ObjectUtils.notNull(id);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getId()
	{
		return this.id;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setRoot(Object root)
	{
		this.root = ObjectUtils.notNull(root);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getRoot()
	{
		return this.root;
	}
	
}
