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


import java.net.URL;
import java.text.MessageFormat;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;


/**
 * Helper class for BI suite resource bundle.
 * 
 * @author XDEV Software jwill
 * @since 4.0
 */
public class GanttResourceBundle
{
	/**
	 * serialVersionUID.
	 */
	private static ResourceBundle	resourceBundle;
	
	/**
	 * name of the language properties file.
	 */
	private static final String		BUNDLE_NAME	= ".gantt";
	
	
	/**
	 * @return the ResourceBundle for the calendar package
	 */
	private static ResourceBundle getBundle()
	{
		if(resourceBundle == null)
		{
			resourceBundle = ResourceBundle.getBundle(GanttResourceBundle.class.getPackage()
					.getName() + BUNDLE_NAME);
		}
		
		return resourceBundle;
	}
	
	
	/**
	 * Return a specified String from the bundle of the calendar.
	 * 
	 * @param key
	 *            the key to lookup the value
	 * @param args
	 *            optional values to replace the placeholder with
	 * @return a String
	 */
	public static String getString(String key, Object... args)
	{
		String str = getBundle().getString(key);
		
		if(args != null && args.length > 0)
		{
			str = MessageFormat.format(str,args);
		}
		
		return str;
	}
	
	
	public static ImageIcon loadResIcon(String name)
	{
		try
		{
			URL url = GanttResourceBundle.class.getResource("/xdev/ui/ganttchart/icons/" + name);
			return new ImageIcon(ImageIO.read(url));
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
