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


import java.util.Map;

import xdev.ui.ganttchart.model.XdevGanttRelationVTWrapper;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTableColumn;


public class VTGanttRelationMappingConverter
{
	public static XdevGanttRelationVTWrapper getEntryRelationColumnMappings(
			Map<GanttRelationColumnType, VirtualTableColumn<?>> relationMappings,
			VirtualTable relationVT)
	{
		
		XdevGanttRelationVTWrapper relationDataContainer = new XdevGanttRelationVTWrapper(
				relationVT);
		
		for(GanttRelationColumnType mandatoryType : GanttRelationColumnType.getMandatoryTypes())
		{
			if(!relationMappings.containsKey(mandatoryType))
			{
				throw new NullPointerException(
						"Missing mandatory gantt relation data mapping parameter");
			}
		}
		
		VirtualTableColumn<?> idColumn = relationMappings.get(GanttRelationColumnType.ID);
		relationDataContainer.setId(idColumn);
		
		@SuppressWarnings("unchecked")
		// the user must ensure that the set columns are correclty mapped.
		VirtualTableColumn<Integer> relationType = (VirtualTableColumn<Integer>)relationMappings
				.get(GanttRelationColumnType.RELATION_ID);
		relationDataContainer.setRelationType(relationType);
		
		VirtualTableColumn<?> entryId = relationMappings.get(GanttRelationColumnType.ENTRY_ID);
		relationDataContainer.setEntryId(entryId);
		
		VirtualTableColumn<?> relationRoot = relationMappings
				.get(GanttRelationColumnType.RELATION_PARENT_ID);
		relationDataContainer.setRelationRoot(relationRoot);
		
		return relationDataContainer;
	}
}
