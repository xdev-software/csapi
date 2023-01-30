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
package xdev.ui.ganttchart.utils;


import java.util.Collection;
import java.util.Date;
import java.util.HashSet;


/**
 * The supported column types of an activity.
 * 
 * @author XDEV Software
 */
//FOR INTEGRATION PURPOSE
public enum DateGanttEntryColumnType
{
	
	/**
	 * the id column of type Object (mandatory).
	 */
	ID(true, Object.class),
	
	/**
	 * the start column of type Date (mandatory).
	 */
	START(true, Date.class),
	
	/**
	 * the end column of type Date.
	 */
	END(true, Date.class),
	
	/**
	 * the category column of type Object.
	 */
	PARENT_ID(true, Object.class),
	
	/**
	 * the description column of type String.
	 */
	DESCRIPTION(false, String.class),
	
	/**
	 * the location column of type String.
	 */
	COMPLETION(false, Double.class);
	
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
	private DateGanttEntryColumnType(boolean mandatory, Class<?> type)
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
	public static Collection<DateGanttEntryColumnType> getMandatoryTypes()
	{
		Collection<DateGanttEntryColumnType> mandatoryTypes = new HashSet<DateGanttEntryColumnType>();
		for(DateGanttEntryColumnType type : DateGanttEntryColumnType.values())
		{
			if(type.isMandatory())
			{
				mandatoryTypes.add(type);
			}
		}
		return mandatoryTypes;
	}
}
