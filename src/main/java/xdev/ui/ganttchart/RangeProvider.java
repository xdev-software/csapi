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
package xdev.ui.ganttchart;


import com.jidesoft.range.IntegerRange;
import com.jidesoft.range.Range;
import com.jidesoft.range.TimeRange;


/**
 * Provides specific typed {@link Range}s prepared with given start and end
 * values.
 * 
 * 
 * @param <T>
 *            the range type to use.
 * @author XDEV Software jwill
 * @since 4.0
 * 
 */
public interface RangeProvider<T>
{
	/**
	 * Provides a range with the given start and end delimiters.
	 * 
	 * @param start
	 *            the range start value.
	 * @param end
	 *            the range end value.
	 * 
	 * @return the specific range, prepared with the given values.
	 */
	public Range<T> provideRange(T start, T end);
	
	
	/**
	 * Provides a range with default values.
	 * <p>
	 * See default constructors of the linked ranges.
	 * </p>
	 * 
	 * @see {@link TimeRange} , {@link IntegerRange}
	 * 
	 * @return the specific range, prepared with the given values.
	 */
	public Range<T> provideDefaultRange();
}
