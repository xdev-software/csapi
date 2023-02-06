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


import xdev.util.res.ResourceUtils;

import com.jidesoft.scale.DatePeriod;


public enum DatePeriodType
{
	AM_PM("AM_PM", DatePeriod.AM_PM, ResourceUtils.getResourceString("AM_PM",DatePeriodType.class)),
	
	CENTURY("CENTURY", DatePeriod.CENTURY, ResourceUtils.getResourceString("CENTURY",
			DatePeriodType.class)),
	
	DAY_OF_MONTH("DAY_OF_MONTH", DatePeriod.DAY_OF_MONTH, ResourceUtils.getResourceString(
			"DAY_OF_MONTH",DatePeriodType.class)),
	
	DAY_OF_WEEK("DAY_OF_WEEK", DatePeriod.DAY_OF_WEEK, ResourceUtils.getResourceString(
			"DAY_OF_WEEK",DatePeriodType.class)),
	
	DAY_OF_WEEK_IN_MONTH("DAY_OF_WEEK_IN_MONTH", DatePeriod.DAY_OF_WEEK_IN_MONTH, ResourceUtils
			.getResourceString("DAY_OF_WEEK_IN_MONTH",DatePeriodType.class)),
	
	DAY_OF_YEAR("DAY_OF_YEAR", DatePeriod.DAY_OF_YEAR, ResourceUtils.getResourceString(
			"DAY_OF_YEAR",DatePeriodType.class)),
	
	DECENNIUM("DECENNIUM", DatePeriod.DECENNIUM, ResourceUtils.getResourceString("DECENNIUM",
			DatePeriodType.class)),
	
	FIRST_DAY_OF_WEEK("FIRST_DAY_OF_WEEK", DatePeriod.FIRST_DAY_OF_WEEK, ResourceUtils
			.getResourceString("FIRST_DAY_OF_WEEK",DatePeriodType.class)),
	
	HOUR("HOUR", DatePeriod.HOUR, ResourceUtils.getResourceString("HOUR",DatePeriodType.class)),
	
	HOUR_OF_DAY("HOUR_OF_DAY", DatePeriod.HOUR_OF_DAY, ResourceUtils.getResourceString(
			"HOUR_OF_DAY",DatePeriodType.class)),
	
	MILLENIUM("MILLENIUM", DatePeriod.MILLENIUM, ResourceUtils.getResourceString("MILLENIUM",
			DatePeriodType.class)),
	
	MILLISECOND("MILLISECOND", DatePeriod.MILLISECOND, ResourceUtils.getResourceString(
			"MILLISECOND",DatePeriodType.class)),
	
	MINUTE("MINUTE", DatePeriod.MINUTE, ResourceUtils.getResourceString("MINUTE",
			DatePeriodType.class)),
	
	MONTH("MONTH", DatePeriod.MONTH, ResourceUtils.getResourceString("MONTH",DatePeriodType.class)),
	
	QUARTER("QUARTER", DatePeriod.QUARTER, ResourceUtils.getResourceString("QUARTER",
			DatePeriodType.class)),
	
	SECOND("SECOND", DatePeriod.SECOND, ResourceUtils.getResourceString("SECOND",
			DatePeriodType.class)),
	
	WEEK_OF_MONTH("WEEK_OF_MONTH", DatePeriod.WEEK_OF_MONTH, ResourceUtils.getResourceString(
			"WEEK_OF_MONTH",DatePeriodType.class)),
	
	WEEK_OF_YEAR("WEEK_OF_YEAR", DatePeriod.WEEK_OF_YEAR, ResourceUtils.getResourceString(
			"WEEK_OF_YEAR",DatePeriodType.class)),
	
	YEAR("YEAR", DatePeriod.YEAR, ResourceUtils.getResourceString("YEAR",DatePeriodType.class));
	
	/**
	 * the string representation of the {@link DatePeriod}.
	 */
	private String		periodName;
	private DatePeriod	period;
	private String		periodDescription;
	
	
	public DatePeriod getPeriod()
	{
		return this.period;
	}
	
	
	public String getPeriodName()
	{
		return this.periodName;
	}
	
	
	public String getPeriodDescription()
	{
		return this.periodDescription;
	}
	
	
	private DatePeriodType(String periodName, DatePeriod period, String periodDescription)
	{
		this.periodName = periodName;
		this.period = period;
		this.periodDescription = periodDescription;
	}
	
	
	public static DatePeriodType getPeriodType(DatePeriod period)
	{
		for(int i = 0; i < values().length; i++)
		{
			if(values()[i].getPeriod().equals(period))
			{
				return values()[i];
			}
		}
		
		return null;
	}
	
	
	public static DatePeriodType getPeriodTypeFromName(String periodName)
	{
		for(int i = 0; i < values().length; i++)
		{
			if(values()[i].getPeriodName().equals(periodName))
			{
				return values()[i];
			}
		}
		
		throw new NullPointerException("No PeriodType found with name: " + periodName);
	}
	
	
	public static DatePeriodType getPeriodTypeFromCalendarField(int periodCalendarField)
	{
		for(int i = 0; i < values().length; i++)
		{
			if(values()[i].getPeriod().getCalendarField() == periodCalendarField)
			{
				return values()[i];
			}
		}
		
		throw new NullPointerException("No PeriodType found with calendarField: "
				+ periodCalendarField);
	}
	
	
	@Override
	public String toString()
	{
		return this.getPeriodDescription();
	}
}
