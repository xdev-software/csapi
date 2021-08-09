package xdev.ui.laf;

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


import com.jidesoft.plaf.LookAndFeelFactory;


/**
 * A XDEV wrapper for Jides OFFICE2003_STYLE.
 * 
 * @author XDEV Software
 */
public class JideOffice2003DefaultLookAndFeel extends SystemLookAndFeel
{
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setLookAndFeel() throws LookAndFeelException
	{
		super.setLookAndFeel();
		
		LookAndFeelFactory.setDefaultStyle(LookAndFeelFactory.OFFICE2003_STYLE);
	}
}
