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
package xdev.ui.laf;


import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.plaf.office2003.Office2003Painter;


/**
 * A XDEV wrapper for Jides OFFICE2003_STYLE.
 * 
 * @author XDEV Software
 */
public class JideOffice2003HomeSteadLookAndFeel extends SystemLookAndFeel
{
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setLookAndFeel() throws LookAndFeelException
	{
		super.setLookAndFeel();
		
		LookAndFeelFactory.setDefaultStyle(LookAndFeelFactory.OFFICE2003_STYLE);
		
		((Office2003Painter)Office2003Painter.getInstance()).setColorName("HomeStead");
	}
}
