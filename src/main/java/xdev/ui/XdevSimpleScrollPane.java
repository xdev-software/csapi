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


import java.awt.Component;

import com.jidesoft.swing.SimpleScrollPane;


/**
 * XdevSimpleScrollPane provides an alternative for XdevScrollPane/JScrollPane
 * without scroll bars.
 * <p>
 * The scrolling is triggered by by using buttons on the borders of the pane
 * instead of scroll bars.
 * </p>
 * 
 * @author XDEV Software
 * 
 */
public class XdevSimpleScrollPane extends SimpleScrollPane
{
	/**
	 * serialVersionUID.
	 */
	private static final long	serialVersionUID	= 1L;
	

	/**
	 * @see SimpleScrollPane
	 */
	public XdevSimpleScrollPane()
	{
		super();
	}
	

	/**
	 * 
	 * @param view
	 *            the component to display in the scrollpanes viewport
	 * @param vsbPolicy
	 *            an integer that specifies the vertical scrollbar policy
	 * @param hsbPolicy
	 *            an integer that specifies the horizontal scrollbar policy
	 * @see SimpleScrollPane#SimpleScrollPane(Component, int, int)
	 */
	public XdevSimpleScrollPane(Component view, int vsbPolicy, int hsbPolicy)
	{
		super(view,vsbPolicy,hsbPolicy);
	}
	

	/**
	 * 
	 * @param view
	 *            the component to display in the scrollpanes viewport
	 * @param vsbPolicy
	 *            an integer that specifies the vertical scrollbar policy
	 * @param hsbPolicy
	 *            an integer that specifies the horizontal scrollbar policy
	 * @param scrollOnRollover
	 *            if {@code true}, scrolling is enabled on rollover
	 * @see SimpleScrollPane#SimpleScrollPane(Component, int, int)
	 */
	public XdevSimpleScrollPane(Component view, int vsbPolicy, int hsbPolicy,
			boolean scrollOnRollover)
	{
		super(view,vsbPolicy,hsbPolicy);
		setScrollOnRollover(scrollOnRollover);
	}
	

	/**
	 * 
	 * @param view
	 *            the component to display in the scrollpane's viewport
	 * @see SimpleScrollPane#SimpleScrollPane(Component)
	 */
	public XdevSimpleScrollPane(Component view)
	{
		super(view);
	}
	

	/**
	 * 
	 * @param vsbPolicy
	 *            an integer that specifies the vertical scrollbar policy
	 * @param hsbPolicy
	 *            an integer that specifies the horizontal scrollbar policy
	 * @see SimpleScrollPane#SimpleScrollPane(int, int)
	 */
	public XdevSimpleScrollPane(int vsbPolicy, int hsbPolicy)
	{
		super(vsbPolicy,hsbPolicy);
	}
	

	/**
	 * If set to {@code true}, the scrolling will start when the mouse is
	 * positioned over one of the navigation buttons.
	 * <p>
	 * Pressing one of the navigation buttons will scroll to the first / last
	 * position possible, if this option is enabled.
	 * </p>
	 * 
	 * @param enabled
	 *            if {@code true}, scrolling is enabled on rollover
	 */
	@Override
	public void setScrollOnRollover(boolean enabled)
	{
		super.setScrollOnRollover(enabled);
	}
	
}
