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
package xdev.ui.quickfilter;


import xdev.ui.XdevQuickComboBoxFilterField;
import xdev.ui.XdevQuickListFilterField;
import xdev.ui.XdevQuickTableFilterField;
import xdev.ui.XdevQuickTreeFilterField;

import com.jidesoft.grid.QuickFilterField;


/**
 * Represents search modes for {@link XdevQuickListFilterField},
 * {@link XdevQuickComboBoxFilterField}, {@link XdevQuickTableFilterField} and
 * {@link XdevQuickTreeFilterField}.
 * 
 * @author XDEV Software
 * 
 */
public enum QuickFilterSearchMode
{
	/**
	 * Plain text search; no use of wildcards or regex.
	 */
	PLAINTEXT,
	
	/**
	 * Wildcard search.
	 */
	WILDCARD,
	
	/**
	 * Regex (regular expressions) search.
	 */
	REGEX;
	
	/**
	 * Static helper Method to set {@link QuickFilterSearchMode}s for
	 * {@link QuickFilterField}s.
	 * 
	 * @param quickFilterField
	 *            to set the {@link QuickFilterSearchMode} on.
	 * @param searchMode
	 *            {@link QuickFilterSearchMode} to be set.
	 */
	public static void setSearchMode(final QuickFilterField quickFilterField,
			final QuickFilterSearchMode searchMode)
	{
		switch(searchMode)
		{
			case PLAINTEXT:
				quickFilterField.setWildcardEnabled(false);
				quickFilterField.setRegexEnabled(false);
			break;
			
			case WILDCARD:
				quickFilterField.setWildcardEnabled(true);
				quickFilterField.setRegexEnabled(false);
			break;
			
			case REGEX:
				quickFilterField.setWildcardEnabled(false);
				quickFilterField.setRegexEnabled(true);
			break;
			
			default:
				throw new EnumConstantNotPresentException(QuickFilterSearchMode.class,
						String.valueOf(searchMode));
		}
	}
	
	
	/**
	 * Static helper method to get the {@link QuickFilterSearchMode} of a
	 * {@link QuickFilterField}.
	 * 
	 * @param quickFilterField
	 * @return the {@link QuickFilterSearchMode} of a {@link QuickFilterField}
	 * @since 1.1
	 */
	public static QuickFilterSearchMode getSearchMode(final QuickFilterField quickFilterField)
	{
		boolean wildcardEnabled = quickFilterField.isWildcardEnabled();
		boolean regexEnabled = quickFilterField.isRegexEnabled();
		if(regexEnabled)
		{
			return REGEX;
		}
		if(wildcardEnabled)
		{
			return WILDCARD;
		}
		return PLAINTEXT;
	}
}
