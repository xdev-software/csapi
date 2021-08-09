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


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import com.jidesoft.gantt.GanttChart;
import com.jidesoft.gantt.GanttEntry;
import com.jidesoft.gantt.GanttModel;
import com.jidesoft.range.TimeRange;
import com.jidesoft.scale.DatePeriod;
import com.jidesoft.scale.DatePeriodConverter;
import com.jidesoft.scale.ScaleArea;

import xdev.ui.ganttchart.UpdateableGanttEntry;
import xdev.ui.ganttchart.XdevGanttEntry;
import xdev.ui.ganttchart.template.GanttTemplate;


/**
 * Provides utility methods to initialize {@link GanttTemplate} scale type
 * specific parts.
 * 
 * <p>
 * The <code>GanttCharts</code> {@link ScaleArea} should be initialized with
 * {@link XdevGanttUtils#initializeDateScaleArea(GanttChart)} to set up default
 * UI-layout and localization preferences.
 * </p>
 * 
 * @author XDEV Software jwill
 * @since 4.0
 */
public class XdevGanttUtils
{
	
	/**
	 * If a {@link GanttTemplate} gets configured manually, this scale area
	 * initialization has to be invoked.
	 * 
	 * <p>
	 * The initialization includes default period initialization and default
	 * localization.
	 * </p>
	 * 
	 * @param chart
	 *            the {@link GanttChart} to modify the scale area from.
	 */
	public static void initializeDateScaleArea(final GanttChart<Date, UpdateableGanttEntry<Date>> chart)
	{
		final ScaleArea<Date> localScaleArea = chart.getScaleArea();
		localScaleArea.setDefaultPeriodConverter(DatePeriodConverter
				.createDefaultPeriodConverter(localScaleArea));
		final Iterator<DatePeriod> localObject = DatePeriod.getDefaultDatePeriods().iterator();
		while(localObject.hasNext())
		{
			final DatePeriod localDatePeriod = localObject.next();
			localScaleArea.setPeriodConverter(localDatePeriod,
			
			DatePeriodConverter.createPeriodConverter(localScaleArea,localDatePeriod));
		}
	}
	
	
	/**
	 * Returns the first {@link GanttEntry} start value.
	 * 
	 * @param chart
	 *            the ganttchart to operate with.
	 * @return the start value of the first found {@link GanttEntry}.
	 */
	public static <T extends Comparable<T>> T getFirstEntryStart(
			final GanttChart<T, ? extends UpdateableGanttEntry<T>> chart)
	{
		if(chart.getEntryCount() > 0)
		{
			T lowest = chart.getEntryAt(0).getRange().lower();
			
			for(int i = 1; i < chart.getEntryCount(); i++)
			{
				if(chart.getEntryAt(i).getRange().lower().compareTo(lowest) == -1)
				{
					lowest = chart.getEntryAt(i).getRange().lower();
				}
			}
			return lowest;
		}
		throw new NullPointerException(
				"No research could be initiated due to the missing gantt entries");
	}
	
	/**
	 * Suited for Default DateScaleModel
	 * (d/DatePeriod.Day_OF_MONTH/-/DatePeriod.MONTH/mm-/DatePeriod.Year/yyyy)
	 * 
	 * @return a list of sample entries.
	 */
	public static List<UpdateableGanttEntry<Date>> createSampleGanttEntries()
	{
		final DateFormat formatter = new SimpleDateFormat("d-M-yyyy");
		final List<UpdateableGanttEntry<Date>> entryList = new ArrayList<>();
		
		// ######################### SAMPLE DATES #############################
		
		final Calendar entryC1 = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR),Calendar
				.getInstance().get(Calendar.MONTH),Calendar.getInstance()
				.get(Calendar.DAY_OF_MONTH));
		entryC1.add(Calendar.MONTH,1);
		
		final Calendar entryC2 = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR),Calendar
				.getInstance().get(Calendar.MONTH),Calendar.getInstance()
				.get(Calendar.DAY_OF_MONTH));
		entryC2.add(Calendar.MONTH,1);
		entryC2.add(Calendar.DAY_OF_MONTH,10);
		
		final Calendar entry2C2 = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR),
				Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(
						Calendar.DAY_OF_MONTH));
		entry2C2.add(Calendar.MONTH,1);
		entry2C2.add(Calendar.DAY_OF_MONTH,5);
		
		final Calendar entry3C1 = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR),
				Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(
						Calendar.DAY_OF_MONTH));
		entry3C1.add(Calendar.MONTH,1);
		entry3C1.add(Calendar.DAY_OF_MONTH,13);
		
		final Calendar entry3C2 = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR),
				Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(
						Calendar.DAY_OF_MONTH));
		entry3C2.add(Calendar.MONTH,1);
		entry3C2.add(Calendar.DAY_OF_MONTH,25);
		
		// ################### SAMPLE ENTRIES ##########################
		
		final UpdateableGanttEntry<Date> entry = new XdevGanttEntry<>("Entry",Date.class,
				new TimeRange(getDate(entryC1,formatter),getDate(entryC2,formatter)),0.3);
		entryList.add(entry);
		
		final UpdateableGanttEntry<Date> entry2 = new XdevGanttEntry<>("Entry2",Date.class,
				new TimeRange(getDate(entryC1,formatter),getDate(entry2C2,formatter)),0.5);
		entryList.add(entry2);
		
		final UpdateableGanttEntry<Date> entry3 = new XdevGanttEntry<>("Entry2",Date.class,
				new TimeRange(getDate(entry3C1,formatter),getDate(entry3C2,formatter)),0.0);
		entryList.add(entry3);
		
		return entryList;
	}
	
	
	private static Date getDate(final Calendar date, final DateFormat format)
	{
		final String dateString = date.get(Calendar.DAY_OF_MONTH) + "-" + date.get(Calendar.MONTH) + "-"
				+ date.get(Calendar.YEAR);
		
		try
		{
			return format.parse(dateString);
		}
		catch(final ParseException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	
	public static <T extends Comparable<T>, ET extends GanttEntry<T>> List<ET> getEntries(
			final GanttModel<T, ET> model)
	{
		final List<ET> entryList = new ArrayList<>();
		
		for(int i = 0; i < model.getEntryCount(); i++)
		{
			entryList.add(model.getEntryAt(i));
		}
		
		return entryList;
	}
	
	
	public static <ET extends UpdateableGanttEntry<Date>> Date getMaxDate(final List<ET> entries)
	{
		final List<Date> entryDateList = new ArrayList<>();
		
		for(final GanttEntry<Date> ganttEntry : entries)
		{
			entryDateList.add(ganttEntry.getRange().upper());
		}
		
		return Collections.max(entryDateList);
	}
	
	
	public static <ET extends UpdateableGanttEntry<Date>> ET getMaxDateEntry(final List<ET> entries)
	{
		final List<Date> entryDateList = new ArrayList<>();
		
		for(final GanttEntry<Date> ganttEntry : entries)
		{
			entryDateList.add(ganttEntry.getRange().upper());
		}
		
		final Date maxDate = Collections.max(entryDateList);
		
		for(final ET entry : entries)
		{
			if(entry.getRange().upper().equals(maxDate))
			{
				return entry;
			}
		}
		
		return null;
	}
	
	
	public static <ET extends UpdateableGanttEntry<Date>> Date getMinDate(final List<ET> entries)
	{
		final List<Date> entryDateList = new ArrayList<>();
		
		for(final GanttEntry<Date> ganttEntry : entries)
		{
			entryDateList.add(ganttEntry.getRange().lower());
		}
		
		return Collections.min(entryDateList);
	}
	
	
	public static <ET extends UpdateableGanttEntry<Date>> ET getMinDateEntry(final List<ET> entries)
	{
		final List<Date> entryDateList = new ArrayList<>();
		
		for(final GanttEntry<Date> ganttEntry : entries)
		{
			entryDateList.add(ganttEntry.getRange().lower());
		}
		
		final Date minDate = Collections.min(entryDateList);
		
		for(final ET entry : entries)
		{
			if(entry.getRange().lower().equals(minDate))
			{
				return entry;
			}
		}
		
		return null;
	}
	
	
	// Have to cast here because of jide typing. The childs
	// should be always the same type.
	@SuppressWarnings("unchecked")
	public static <T extends Comparable<T>, ET extends GanttEntry<T>> List<ET> getChildEntries(
			final ET entry)
	{
		final List<ET> childs = new ArrayList<>();
		
		for(int i = 0; i < entry.getChildrenCount(); i++)
		{
			childs.add((ET)entry.getChildAt(i));
		}
		
		return childs;
	}
}
