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


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import xdev.Application;
import xdev.db.sql.SELECT;
import xdev.ui.ganttchart.UpdateableGanttEntry;
import xdev.ui.ganttchart.model.XdevGanttEntryVTMappings;
import xdev.ui.ganttchart.model.XdevGanttRelationVTWrapper;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTable.VirtualTableRow;
import xdev.vt.VirtualTableColumn;

import com.jidesoft.gantt.GanttEntry;
import com.jidesoft.gantt.GanttEntryRelation;
import com.jidesoft.range.Range;


/**
 * Utility class which simplification and avoids redundancy to get data
 * representation of the {@link GanttEntry} / {@link GanttEntryRelation}y oop
 * layer.
 * 
 * @author XDEV Software jwill
 * @since 4.0
 */
public class VirtualTableGanttPersistenceUtils
{
	
	/**
	 * Returns the {@link VirtualTableRow} representation of the given
	 * {@link GanttEntry}.
	 * 
	 * <p>
	 * The VirtualTableRow is selected via entry id.
	 * </p>
	 * 
	 * @param entry
	 *            the entry to get the VirtualTableRow representation from.
	 * 
	 * @param dataContainer
	 *            the {@link XdevGanttEntryVTMappings} which provides
	 *            {@link GanttEntry} data mapping information.
	 * 
	 * @param <S>
	 *            the customized {@link GanttEntry} type.
	 * 
	 * @return the VirttualTableRow representation of the given entry, if no
	 *         entry was found, a new {@link VirtualTableRow} is returned.
	 * 
	 * @throws NullPointerException
	 *             if now {@link VirtualTableRow} representation of the given
	 *             {@link GanttEntry} was found.
	 * 
	 * @see {@link UpdateableGanttEntry#getId()}
	 */
	public static <S extends UpdateableGanttEntry<?>> VirtualTableRow getEntryRowAt(S entry,
			XdevGanttEntryVTMappings<?> dataContainer) throws NullPointerException
	{
		for(int i = 0; i < dataContainer.getVirtualTable().getRowCount(); i++)
		{
			if(dataContainer.getVirtualTable().getRow(i).get(dataContainer.getId())
					.equals(entry.getId()))
			{
				return dataContainer.getVirtualTable().getRow(i);
			}
		}
		
		return dataContainer.getVirtualTable().createRow();
	}
	
	
	/**
	 * Returns the {@link VirtualTableRow} representation of the given
	 * {@link GanttEntry}.
	 * 
	 * <p>
	 * The VirtualTableRow is selected via entry id.
	 * </p>
	 * 
	 * @param <S>
	 *            the {@link GanttEntry} custom type.
	 * 
	 * @param entry
	 *            the entry to get the VirtualTableRow representation from.
	 * 
	 * @param relatCnt
	 *            the relation data container which provides information about
	 *            the mapped data fields as {@link VirtualTableColumn}s.
	 * 
	 * @return the VirttualTableRow representation of the given entry.
	 * 
	 * @see {@link UpdateableGanttEntry#getId()}
	 */
	public static <S extends UpdateableGanttEntry<?>> List<VirtualTableRow> getEntryRelationRowsAt(
			S entry, XdevGanttRelationVTWrapper relatCnt)
	{
		List<VirtualTableRow> relationRows = new ArrayList<VirtualTableRow>();
		
		for(int i = 0; i < relatCnt.getVirtualTable().getRowCount(); i++)
		{
			if(relatCnt.getVirtualTable().getRow(i).get(relatCnt.getEntryId())
					.equals(entry.getId())
					|| relatCnt.getVirtualTable().getRow(i).get(relatCnt.getRelationRoot())
							.equals(entry.getId()))
			{
				relationRows.add(relatCnt.getVirtualTable().getRow(i));
			}
		}
		return relationRows;
	}
	
	
	/**
	 * Returns the {@link VirtualTableRow} representation of the given
	 * {@link GanttEntry}.
	 * 
	 * <p>
	 * The VirtualTableRow is selected via entry id.
	 * </p>
	 * 
	 * @param relation
	 *            the entry relation to get the VirtualTableRow representation
	 *            from.
	 * 
	 * @param container
	 *            the {@link XdevGanttRelationVTWrapper} which provides
	 *            {@link GanttEntryRelation} data mapping information.
	 * 
	 * @param <S>
	 *            the customized {@link GanttEntry} type.
	 * 
	 * @return the VirttualTableRow representation of the given entry.
	 * 
	 * @throws NullPointerException
	 *             if now {@link VirtualTableRow} representaion of the given
	 *             {@link GanttEntry} was found.
	 * 
	 * @see {@link UpdateableGanttEntry#getId()}
	 */
	public static <S extends UpdateableGanttEntry<?>> VirtualTableRow getEntryRelationRowAt(
			GanttEntryRelation<S> relation, XdevGanttRelationVTWrapper container)
			throws NullPointerException
	{
		VirtualTable relationVt = container.getVirtualTable();
		
		S pre = relation.getPredecessorEntry();
		S suc = relation.getSuccessorEntry();
		int type = relation.getRelationType();
		
		for(int i = 0; i < relationVt.getRowCount(); i++)
		{
			VirtualTableRow currentRow = relationVt.getRow(i);
			
			// find nm-row
			if(currentRow.get(container.getRelationRoot()).equals(pre.getId())
					&& currentRow.get(container.getEntryId()).equals(suc.getId())
					&& currentRow.get(container.getRelationType()).equals(type))
			{
				return currentRow;
			}
		}
		throw new NullPointerException("No VirtualTableRow found for the given relation");
	}
	
	
	/**
	 * Checks for changed in the <code>GanttEntries</code> default data fields.
	 * <p>
	 * The {@link GanttEntry} default data fields are:
	 * 
	 * <ul>
	 * <li>Name</li>
	 * <li>Completion</li>
	 * <li>Range (= start until end)</li>
	 * </ul>
	 * 
	 * <p>
	 * If changes were detected, updated fields in the origin entry get
	 * synchronized to prevent a entry swap in the model.
	 * </p>
	 * 
	 * @param updatedEntry
	 *            the {@link GanttEntry} which was changed.
	 * @param originalEntry
	 *            the original {@link GanttEntry} (previous state with equal id)
	 */
	public static <T, S extends UpdateableGanttEntry<T>> void syncronizeChangedFields(
			S updatedEntry, S originalEntry)
	{
		
		if(!updatedEntry.getName().equals(originalEntry.getName()))
		{
			originalEntry.setName(updatedEntry.getName());
		}
		else if(updatedEntry.getCompletion() != originalEntry.getCompletion())
		{
			originalEntry.setCompletion(updatedEntry.getCompletion());
		}
		else if(!updatedEntry.getRange().lower().equals(originalEntry.getRange().lower())
				|| !updatedEntry.getRange().upper().equals(originalEntry.getRange().upper()))
		{
			// fire new event to update UI -> adjust() does not fire update
			// event
			originalEntry.getRange().adjust(updatedEntry.getRange().lower(),
					updatedEntry.getRange().upper());
		}
	}
	
	
	public static <T, S extends UpdateableGanttEntry<T>> boolean equalsUpdateableValues(S entry1,
			S entry2)
	{
		if(entry2 == null)
		{
			return false;
		}
		else
		{
			if(entry1.getName().equals(entry2.getName()))
			{
				if(entry1.getRange().equals(entry2.getRange()))
				{
					if(entry1.getCompletion() == entry2.getCompletion())
					{
						return true;
					}
					else
					{
						return false;
					}
				}
				else
				{
					return false;
				}
			}
			else
			{
				return false;
			}
		}
	}
	
	
	public static void fillData(VirtualTable dataToFill, VirtualTableColumn<?> orderColumn)
	{
		SELECT select = null;
		
		select = dataToFill.getSelect();
		
		fillData(dataToFill,select,orderColumn);
	}
	
	
	/**
	 * Sets the model for the supported table based on a {@link VirtualTable}
	 * and additional configuration.
	 * 
	 * @param vt
	 *            a {@link VirtualTable}.
	 * @param select
	 *            a custom {@link SELECT} used to query the {@link VirtualTable}
	 *            .
	 * @param params
	 *            a number of parameters for the custom select
	 */
	private static void fillData(VirtualTable vt, SELECT select, VirtualTableColumn<?> orderColumn)
	{
		if(select != null)
		{
			try
			{
				vt.queryAndFill(select.ORDER_BY(orderColumn,false));
			}
			catch(Exception e)
			{
				Application.getLogger().error(e);
			}
		}
	}
	
	
	public static boolean compareTimeRange(Range<Date> origiginalRange, Range<Date> actualRange)
	{
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(origiginalRange.lower());
		cal2.setTime(actualRange.lower());
		boolean startEquals = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
				&& cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)
				&& cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
				&& cal1.get(Calendar.HOUR) == cal2.get(Calendar.HOUR)
				&& cal1.get(Calendar.MINUTE) == cal2.get(Calendar.MINUTE)
				&& cal1.get(Calendar.SECOND) == cal2.get(Calendar.SECOND);
		
		cal1.setTime(origiginalRange.upper());
		cal2.setTime(actualRange.upper());
		boolean endEquals = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
				&& cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)
				&& cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
				&& cal1.get(Calendar.HOUR) == cal2.get(Calendar.HOUR)
				&& cal1.get(Calendar.MINUTE) == cal2.get(Calendar.MINUTE)
				&& cal1.get(Calendar.SECOND) == cal2.get(Calendar.SECOND);
		;
		
		return (endEquals && startEquals) ? true : false;
	}
}
