package xdev.ui.table;

/*-
 * #%L
 * XDEV Component Suite
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


import java.util.ArrayList;
import java.util.List;

import com.jidesoft.grid.ISortableTableModel.SortItem;


/**
 * 
 * @author XDEV Software
 * @since 4.0
 */
public class JideSortItemUtils
{
	/**
	 * 
	 * @param originalItems
	 * @param newItems
	 * @return {@code true} if equal
	 */
	public static boolean equalsSortItemCriteria(List<SortItem> originalItems,
			List<SortItem> newItems)
	{
		if(originalItems.size() != newItems.size())
		{
			return false;
		}
		
		for(int i = 0; i < originalItems.size(); i++)
		{
			SortItem originalItem = originalItems.get(i);
			SortItem newItem = newItems.get(i);
			
			if(originalItem.getColumn() != newItem.getColumn()
					|| originalItem.isAscending() != newItem.isAscending())
			{
				return false;
			}
		}
		return true;
	}
	
	
	/**
	 * 
	 * @param currentItems
	 * @return {@link List} of {@link SortItem}
	 */
	public static List<SortItem> copySortedItems(List<SortItem> currentItems)
	{
		List<SortItem> copy = new ArrayList<>();
		
		for(SortItem sortItem : currentItems)
		{
			copy.add(new SortItem(sortItem.getColumn(),sortItem.isAscending()));
		}
		
		return copy;
	}
}
