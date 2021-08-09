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


import java.util.Date;
import java.util.Map;

import xdev.ui.ganttchart.model.XdevGanttEntryVTMappings;
import xdev.ui.ganttchart.model.XdevGanttRelationVTWrapper;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTableColumn;


/**
 * Maps the UI generated {@link DateGanttEntryMapping}s into their corresponding
 * <code>Object</code> representation.
 * 
 * @author XDEV Software JWill
 * 
 * @see XdevGanttEntryVTMappings
 * @see XdevGanttRelationVTWrapper
 */
public class VTDateGanttMappingConverter
{
	/**
	 * {@inheritDoc}
	 */
	public static XdevGanttEntryVTMappings<Date> getEntryColumnMappings(
			Map<DateGanttEntryColumnType, VirtualTableColumn<?>> entryMappings, VirtualTable dataVT)
	{
		
		for(DateGanttEntryColumnType mandatoryType : DateGanttEntryColumnType.getMandatoryTypes())
		{
			if(!entryMappings.containsKey(mandatoryType))
			{
				throw new NullPointerException("Missing mandatory gantt data mapping parameter");
			}
		}
		
		XdevGanttEntryVTMappings<Date> dataContainer = new XdevGanttEntryVTMappings<Date>(dataVT);
		
		VirtualTableColumn<?> idColumn = entryMappings.get(DateGanttEntryColumnType.ID);
		dataContainer.setId(idColumn);
		
		@SuppressWarnings("unchecked")
		// the user must ensure that the set columns are correclty mapped.
		VirtualTableColumn<Date> startColumn = (VirtualTableColumn<Date>)entryMappings
				.get(DateGanttEntryColumnType.START);
		dataContainer.setStart(startColumn);
		
		@SuppressWarnings("unchecked")
		// the user must ensure that the set columns are correclty mapped.
		VirtualTableColumn<Date> endColumn = (VirtualTableColumn<Date>)entryMappings
				.get(DateGanttEntryColumnType.END);
		dataContainer.setEnd(endColumn);
		
		@SuppressWarnings("unchecked")
		// the user must ensure that the set columns are correclty mapped.
		VirtualTableColumn<String> descriptionColumn = (VirtualTableColumn<String>)entryMappings
				.get(DateGanttEntryColumnType.DESCRIPTION);
		dataContainer.setDescription(descriptionColumn);
		
		@SuppressWarnings("unchecked")
		// the user must ensure that the set columns are correclty mapped.
		VirtualTableColumn<Double> completionColumn = (VirtualTableColumn<Double>)entryMappings
				.get(DateGanttEntryColumnType.COMPLETION);
		dataContainer.setCompletion(completionColumn);
		
		VirtualTableColumn<?> rootColumn = entryMappings.get(DateGanttEntryColumnType.PARENT_ID);
		dataContainer.setRoot(rootColumn);
		
		return dataContainer;
	}
}
