package xdev.ui.quickfilter;

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


import xdev.ui.XdevQuickComboBoxFilterField;
import xdev.ui.XdevQuickListFilterField;
import xdev.ui.XdevQuickTableFilterField;
import xdev.ui.XdevQuickTreeFilterField;

import com.jidesoft.grid.QuickFilterField;


/**
 * Represents search options for {@link XdevQuickListFilterField},
 * {@link XdevQuickComboBoxFilterField}, {@link XdevQuickTableFilterField} and
 * {@link XdevQuickTreeFilterField}.
 * 
 * @author XDEV Software
 * 
 */
public enum QuickFilterSearchOption
{
	/**
	 * Filters out everything that does not match the filter condition from
	 * start to end.
	 */
	MATCH_EXACTLY,
	
	/**
	 * Filters out everything that does not begin with the entered filter
	 * condition.
	 */
	MATCH_FROM_START,
	
	/**
	 * Filters out everything that does not containt the entered filter
	 * condition.
	 */
	MATCH_ANYWHERE;
	
	/**
	 * Static helper Method to set {@link QuickFilterSearchOption}s for
	 * {@link QuickFilterField}s.
	 * 
	 * @param quickFilterField
	 *            to set the {@link QuickFilterSearchOption} on.
	 * @param searchOption
	 *            {@link QuickFilterSearchOption} to be set.
	 */
	public static void setSearchOption(final QuickFilterField quickFilterField,
			final QuickFilterSearchOption searchOption)
	{
		switch(searchOption)
		{
			case MATCH_EXACTLY:
				quickFilterField.setFromStart(true);
				quickFilterField.setFromEnd(true);
			break;
			
			case MATCH_FROM_START:
				quickFilterField.setFromStart(true);
				quickFilterField.setFromEnd(false);
			break;
			
			case MATCH_ANYWHERE:
				quickFilterField.setFromStart(false);
				quickFilterField.setFromEnd(false);
			break;
			
			default:
				throw new EnumConstantNotPresentException(QuickFilterSearchOption.class,
						String.valueOf(searchOption));
		}
	}
	
	
	/**
	 * Static helper method to get the {@link QuickFilterSearchOption} of a
	 * {@link QuickFilterField}.
	 * 
	 * @param quickFilterField
	 * @return the {@link QuickFilterSearchOption} of a {@link QuickFilterField}
	 * @since 1.1
	 */
	public static QuickFilterSearchOption getSearchOption(final QuickFilterField quickFilterField)
	{
		boolean fromStart = quickFilterField.isFromStart();
		boolean fromEnd = quickFilterField.isFromEnd();
		if(fromStart && fromEnd)
		{
			return MATCH_ANYWHERE;
		}
		if(fromStart)
		{
			return MATCH_FROM_START;
		}
		return MATCH_ANYWHERE;
	}
}
