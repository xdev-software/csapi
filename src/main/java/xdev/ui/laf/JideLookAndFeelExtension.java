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


import javax.swing.UIManager;

import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.utils.SystemInfo;


/**
 * Customize the jide - look and feel to set, depending on the current OS.
 * 
 * @author XDEV Software
 * @since CS 1.0
 * 
 */

public class JideLookAndFeelExtension implements LookAndFeelExtension
{
	@Override
	public void installLookAndFeelExtension(LookAndFeel laf) throws LookAndFeelException
	{
		System.setProperty("shadingtheme","true");
		
		if(laf.getClass() == SystemLookAndFeel.class && SystemInfo.isWindows())
		{
			LookAndFeelFactory.setDefaultStyle(LookAndFeelFactory.XERTO_STYLE_WITHOUT_MENU);
		}
		
		LookAndFeelFactory.installJideExtension(LookAndFeelFactory.getDefaultStyle());
		
		UIManager.put("Chevron.alwaysVisible",Boolean.FALSE);
	}
}
