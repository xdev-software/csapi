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


import java.text.DecimalFormat;
import java.text.NumberFormat;

import com.jidesoft.converter.NaturalNumberConverter;
import com.jidesoft.converter.NumberConverter;


/**
 * A NumberConverter for <code>QuickFilterFields</code> to consider default
 * number - rendering.
 * 
 * <p>
 * For example if <code>10000</code> is the value of a column - the user can
 * type <code>10000</code> as filterstring and the <code> QuickFilter</code>
 * hits this value (without essentially enter decimal points).
 * </p>
 * 
 * @author XDEV Software JWill
 * 
 */
// see Issue http://issues.xdev-software.de/view.php?id=12804
public class XdevNumberConverter extends NaturalNumberConverter
{
	/**
	 * the stored {@link NumberFormat}.
	 */
	private NumberFormat	format;
	
	
	/**
	 * Creates a {@link NumberConverter} which considers default number -
	 * rendering.
	 * 
	 */
	public XdevNumberConverter()
	{
		super();
	}
	
	
	/**
	 * Creates a {@link NumberConverter} which considers default number -
	 * rendering.
	 * 
	 * @param paramNumberFormat
	 *            a customized numberformat to consider.
	 */
	public XdevNumberConverter(NumberFormat paramNumberFormat)
	{
		super(paramNumberFormat);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setNumberFormat(NumberFormat paramNumberFormat)
	{
		super.setNumberFormat(paramNumberFormat);
		this.format = paramNumberFormat;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected NumberFormat getNumberFormat()
	{
		if(this.format == null)
		{
			// ignore decimal points
			this.format = new DecimalFormat("0");
			
			this.format.setMaximumIntegerDigits(Integer.MAX_VALUE);
			this.format.setGroupingUsed(isGroupingUsed());
		}
		return this.format;
	}
}
