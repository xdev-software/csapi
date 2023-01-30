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
package xdev.ui;


import com.jidesoft.action.CommandBar;


/**
 * {@link XdevCommandBar} is a kind of Dockable Bar. It can be used to implement
 * toolbar's and menu bars.
 * 
 * @author XDEV Software
 * 
 */
public class XdevCommandBar extends CommandBar
{
	/**
	 * serialVersionUID.
	 */
	private static final long	serialVersionUID	= 1L;
	

	/**
	 * @see CommandBar#CommandBar()
	 */
	public XdevCommandBar()
	{
		super();
	}
	

	/**
	 * @param orientation
	 *            the orientation, either HORIZONTAL or VERTICAL
	 * @see CommandBar#CommandBar()
	 */
	public XdevCommandBar(int orientation)
	{
		super(orientation);
	}
	

	/**
	 * @param key
	 *            the key of the CommandBar
	 * @param title
	 *            the title of the CommandBar
	 * @param orientation
	 *            the orientation, either HORIZONTAL or VERTICAL
	 * @see CommandBar#CommandBar(String, String, int)
	 */
	public XdevCommandBar(String key, String title, int orientation)
	{
		super(key,title,orientation);
	}
	

	/**
	 * @param key
	 *            the key of the CommandBar
	 * @param title
	 *            the title of the CommandBar
	 * @see CommandBar#CommandBar(String, String)
	 */
	public XdevCommandBar(String key, String title)
	{
		super(key,title);
	}
	

	/**
	 * @param key
	 *            the key of the CommandBar
	 * @see CommandBar#CommandBar(String)
	 */
	public XdevCommandBar(String key)
	{
		super(key);
	}
	
}
