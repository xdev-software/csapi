package xdev.ui.ganttchart.utils;

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


import java.util.Collection;
import java.util.HashSet;


/**
 * The supported column types of an activity.
 * 
 * @author XDEV Software
 */
//FOR INTEGRATION PURPOSE
public enum GanttRelationColumnType
{
	
	/**
	 * the id column of type Object (mandatory).
	 */
	ID(true, Object.class),
	
	/**
	 * the start column of type Date (mandatory).
	 */
	RELATION_ID(true, Object.class),
	
	/**
	 * the end column of type Date.
	 */
	ENTRY_ID(true, Object.class),
	
	/**
	 * the category column of type Object.
	 */
	RELATION_PARENT_ID(true, Object.class);
	
	/**
	 * is the column type a mandatory type (required field).
	 */
	private boolean		mandatory	= false;
	
	/**
	 * the data type of the column.
	 */
	private Class<?>	type;
	
	
	/**
	 * Create new instance of {@link ActivityColumnType}.
	 * 
	 * @param mandatory
	 *            flag
	 * @param type
	 *            the required type of the column
	 */
	private GanttRelationColumnType(boolean mandatory, Class<?> type)
	{
		this.mandatory = mandatory;
		this.type = type;
	}
	
	
	/**
	 * 
	 * @return {@code true} if this instance is mandatory.
	 */
	public boolean isMandatory()
	{
		return mandatory;
	}
	
	
	/**
	 * @return the required data type of this column.
	 */
	public Class<?> getType()
	{
		return type;
	}
	
	
	/**
	 * @return a Collection of all mandatory instances.
	 */
	public static Collection<GanttRelationColumnType> getMandatoryTypes()
	{
		Collection<GanttRelationColumnType> mandatoryTypes = new HashSet<GanttRelationColumnType>();
		for(GanttRelationColumnType type : GanttRelationColumnType.values())
		{
			if(type.isMandatory())
			{
				mandatoryTypes.add(type);
			}
		}
		return mandatoryTypes;
	}
}
