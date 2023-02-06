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


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.Icon;
import javax.swing.JFrame;

import com.jidesoft.docking.DockableFrame;
import com.jidesoft.docking.DockingManager;


/**
 * A lightweight frame component that can be embedded in {@link JFrame}
 * instances that implement {@link XdevDockingPanel}.
 * 
 * @author XDEV Software
 * 
 */
@BeanSettings(acceptChildren = true)
public class XdevDockableFrame extends DockableFrame
{
	/**
	 * serialVersionUID.
	 */
	private static final long	serialVersionUID	= 1L;
	
	
	/**
	 * Return the default icon.
	 * 
	 * @return an {@link Icon}.
	 */
	public static Icon getDefaultIcon()
	{
		return GraphicUtils.loadResIcon("dockableframe.png",XdevDockableFrame.class);
	}
	
	
	/**
	 * @see DockableFrame#DockableFrame()
	 */
	public XdevDockableFrame()
	{
		super(getDefaultIcon());
	}
	
	
	/**
	 * @see DockableFrame#DockableFrame(Icon)
	 * @param icon
	 *            a {@link Icon} for the frame
	 */
	public XdevDockableFrame(Icon icon)
	{
		super(icon);
	}
	
	
	/**
	 * @see DockableFrame#DockableFrame(String, Icon)
	 * @param icon
	 *            a {@link Icon} for the frame
	 * @param title
	 *            a title for the frame
	 */
	public XdevDockableFrame(String title, Icon icon)
	{
		super(title,icon);
	}
	
	
	/**
	 * @see DockableFrame#DockableFrame(String)
	 * @param title
	 *            a title for the frame
	 */
	public XdevDockableFrame(String title)
	{
		super(title,getDefaultIcon());
	}
	
	{
		setLayout(null);
		setFocusTraversalPolicy(new XdevFocusTraversalPolicy(this));
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Dimension getMinimumSize()
	{
		// only use minimum size if set
		if(isMinimumSizeSet())
		{
			return super.getMinimumSize();
		}
		
		return new Dimension(30,30);
	}
	
	
	/**
	 * <p>
	 * <strong> Hint: Don't use <code>setVisible(boolean)</code> the show or
	 * hide this frame because it has to be done via the docking manager.
	 * Therefore two convenience methods are provided: {@link #showFrame()} and
	 * {@link #hideFrame()}. </strong>
	 * </p>
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public void setVisible(boolean aFlag)
	{
		super.setVisible(aFlag);
	}
	
	
	/**
	 * Adds this dockable frame to the docking manager if necessary and
	 * activates it.
	 * 
	 * @see #hideFrame()
	 */
	public void showFrame()
	{
		DockingManager dockingManager = getDockingManager();
		if(dockingManager != null)
		{
			final String key = getKey();
			if(dockingManager.getFrame(key) == null)
			{
				dockingManager.addFrame(this);
			}
			dockingManager.activateFrame(key);
		}
	}
	
	
	/**
	 * Hides this frame via {@link DockingManager#hideFrame(String)}.
	 * 
	 * @see #showFrame()
	 */
	public void hideFrame()
	{
		DockingManager dockingManager = getDockingManager();
		if(dockingManager != null)
		{
			dockingManager.hideFrame(getKey());
		}
	}
	
	
	// ////////////////////////////////////////////////////////////
	// / Following properties are now shown on development time///
	// ///////////////////////////////////////////////////////////
	
	/**
	 * {@inheritDoc}
	 * <p>
	 * <b>This property does alter the layout on JComponent level so nothing is
	 * going to be updated on this component level.</b>
	 * </p>
	 */
	@BeanProperty(category = DefaultBeanCategories.MISC)
	@Override
	public void setFont(Font paramFont)
	{
		super.setFont(paramFont);
	}
	
	
	/**
	 * {@inheritDoc}
	 * <p>
	 * <b>This property does alter the layout on JComponent level so nothing is
	 * going to be updated on this component level.</b>
	 * </p>
	 */
	@BeanProperty(category = DefaultBeanCategories.MISC)
	@Override
	public void setForeground(Color paramColor)
	{
		super.setForeground(paramColor);
	}
	
	
	/**
	 * {@inheritDoc}
	 * <p>
	 * <b>This property does alter the layout on JComponent level so nothing is
	 * going to be updated on this component level.</b>
	 * </p>
	 */
	@BeanProperty(category = DefaultBeanCategories.MISC)
	@Override
	public void setBackground(Color bg)
	{
		super.setBackground(bg);
	}
	
	
	/**
	 * {@inheritDoc}
	 * <p>
	 * <b>This property does alter the layout on JComponent level so nothing is
	 * going to be updated on this component level.</b>
	 * </p>
	 */
	@BeanProperty(category = DefaultBeanCategories.MISC)
	@Override
	public void setOpaque(boolean arg0)
	{
		super.setOpaque(arg0);
	}
}
