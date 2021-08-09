package xdev.ui;

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


import java.awt.Dimension;
import java.beans.PropertyVetoException;

import javax.swing.Icon;
import javax.swing.JPanel;

import com.jidesoft.pane.CollapsiblePane;


/**
 * <p>
 * CollapsiblePane, as the name indicates, is a pane which can be collapsed.
 * Users can expand the pane so that they can work on it. When it is done, they
 * can collapse it to save place.
 * </p>
 * 
 * <p>
 * All you need to do is to create a CollapsiblePane by passing in the title and
 * icon (if any), and set the content panel which will be expanded or collapsed.
 * </p>
 * 
 * <p>
 * CollapsiblePane has several different styles built-in. They are
 * DROPDOWN_STYLE: this is the default style which looks like the the task pane
 * as in the Windows XP. In this style, the icon is on the left, then the title.
 * The collapsible/expand icon is on the far right. TREE_STYLE: in this style,
 * the collapsible/expand icon will be the same icon as in JTree. It is aligned
 * to the left, then the icon, then the title. PLAIN_STYLE: This style is almost
 * the same as TREE_STYLE except it doesn't have its own background. If you are
 * using CollapsiblePane inside CollapsiblePanes, the CollapsiblePanes
 * background will be seen. SEPARATOR_STYLE: The style, if used with
 * CollapsiblePanes, makes the title pane looking like a separator that divides
 * the CollapsiblePanes into several areas. It might be difficult to understand
 * what each style looks like so if you run the CollapsiblePaneDemo, it would be
 * easy to see all four styles.
 * </p>
 * 
 * <p>
 * CollapsiblePane also supports sliding in all four directions. Typically, the
 * sliding direction is south just like Windows XP's task pane. But sliding in
 * other directions is also useful. For example, if the CollapsiblePane is on
 * the left and there is only one, you can make it sliding east to create an
 * effect like the auto-hidden feature in JIDE Docking Framework.
 * </p>
 * 
 * @author XDEV Software jwill
 * @since 4.0
 */
public class XdevCollapsiblePane extends CollapsiblePane implements XdevFocusCycleComponent
{
	/**
	 * the serialization id.
	 */
	private static final long	serialVersionUID	= -3036351652399341777L;
	/**
	 * the tab index.
	 */
	private int					tabIndex			= -1;
	
	private boolean				flexible			= false;
	private float				flexibleWeight		= 1f;
	
	
	public XdevCollapsiblePane()
	{
		super();
		this.init();
	}
	
	
	public XdevCollapsiblePane(String title)
	{
		super(title);
		this.init();
	}
	
	
	public XdevCollapsiblePane(String title, Icon icon)
	{
		super(title,icon);
		this.init();
	}
	
	
	public XdevCollapsiblePane(String title, int slidingDirection)
	{
		super(title,slidingDirection);
		this.init();
	}
	
	
	/**
	 * initializes the content pane as {@link JPanel} by default.
	 */
	protected void init()
	{
		this.setLayout(null);
		
		// JIDE rendering issue
		this.setTitle(" ");
		
		// Prevent performance issues
		this.setSteps(1);
		this.setStepDelay(1);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getTabIndex()
	{
		return this.tabIndex;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTabIndex(int tabIndex)
	{
		this.tabIndex = tabIndex;
	}
	
	
	/**
	 * @return if the height of this collapsible pane is flexible
	 */
	public boolean isFlexible()
	{
		return flexible;
	}
	
	
	/**
	 * Sets if the height of this collapsible pane is flexible. The weight can
	 * be adjusted with {@link #setFlexible(boolean)}.
	 * 
	 * @param flexible
	 */
	@BeanProperty(category = DefaultBeanCategories.OBJECT)
	public void setFlexible(boolean flexible)
	{
		this.flexible = flexible;
	}
	
	
	/**
	 * 
	 * @return the weight of this collapsible pane, used if
	 *         {@link #isFlexible()} returns <code>true</code>.
	 */
	public float getFlexibleWeight()
	{
		return flexibleWeight;
	}
	
	
	/**
	 * Sets the weight of this collapsible pane, used if {@link #isFlexible()}
	 * returns <code>true</code>.
	 * 
	 * @param flexibleWeight
	 *            the weight between 0.0 and 1.0
	 */
	@BeanProperty(owner = "flexible", floatMin = 0f, floatMax = 1f)
	public void setFlexibleWeight(float flexibleWeight)
	{
		this.flexibleWeight = flexibleWeight;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Dimension getPreferredSize()
	{
		if(isCollapsed())
		{
			// ignore user-defined preferred size
			return getUI().getPreferredSize(this);
		}
		
		return super.getPreferredSize();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isPreferredSizeSet()
	{
		if(flexible)
		{
			// stretch according to flexible weight
			return false;
		}
		
		return super.isPreferredSizeSet();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCollapsed(boolean b)
	{
		try
		{
			super.setCollapsed(b);
		}
		catch(PropertyVetoException e)
		{
			throw new RuntimeException(e);
		}
	}
}
