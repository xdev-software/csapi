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


import java.util.Date;

import com.jidesoft.range.Range;
import com.jidesoft.range.TimeRange;


/**
 * Provides {@link TimeRange}s prepared with given start and end values.
 * 
 * @author XDEV Software jwill
 * @since 4.0
 */
public class DateRangeProvider implements RangeProvider<Date>
{
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Range<Date> provideRange(Date start, Date end)
	{
		return new TimeRange(start,end);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Range<Date> provideDefaultRange()
	{
		return new TimeRange();
	}
	
}
