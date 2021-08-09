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


import java.awt.Color;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.border.Border;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import xdev.ui.event.MenuAdapter;

import com.jidesoft.swing.JideSplitButton;


/**
 * This component represents a mixture between a button and a menu.
 * <p>
 * The left part of the component holds the button, which can be configured to
 * execute an action. The right part of the component opens a popup menu, when
 * clicked.
 * </p>
 * 
 * @see #setPopupContent(XdevMenu)
 * @see #setAlwaysDropdown(boolean)
 * 
 * @author XDEV Software
 * 
 */
public class XdevDropdownButton extends JideSplitButton implements XdevFocusCycleComponent
{
	/**
	 * serialVersionUID.
	 */
	private static final long	serialVersionUID	= 1L;
	/**
	 * tabIndex is used to store the index for {@link XdevFocusCycleComponent}
	 * functionality.
	 */
	private int					tabIndex			= -1;
	
	/**
	 * The menu connected with this {@link XdevDropdownButton}.
	 */
	private XdevMenu			popupContent;
	
	/**
	 * A {@link MenuListener} for reacting internally on menu selection and
	 * reset events.
	 */
	private MenuListener		popupContentHandler;
	
	
	/**
	 * Initializes a new instance of {@code XdevDropDownButton}.
	 */
	public XdevDropdownButton()
	{
		super();
	}
	
	
	/**
	 * Initializes a new instance of {@code XdevDropDownButton} with an
	 * {@link Action}
	 * <p>
	 * The action gets executed, when the button part of the component is
	 * clicked. The title of the action gets used as label.
	 * </p>
	 * 
	 * @param a
	 *            Action for configuring the component.
	 */
	public XdevDropdownButton(Action a)
	{
		super(a);
	}
	
	
	/**
	 * Initializes a new instance of {@code XdevDropDownButton}.
	 * 
	 * @param icon
	 *            a {@link Icon} to display in the menu
	 */
	public XdevDropdownButton(Icon icon)
	{
		super(icon);
	}
	
	
	/**
	 * Initializes a new instance of {@code XdevDropDownButton}.
	 * 
	 * @param s
	 *            the text for the menu label
	 * @param icon
	 *            a {@link Icon} to display in the menu
	 */
	public XdevDropdownButton(String s, Icon icon)
	{
		super(s,icon);
	}
	
	
	/**
	 * Initializes a new instance of {@code XdevDropDownButton}.
	 * 
	 * @param s
	 *            the text for the menu label
	 */
	public XdevDropdownButton(String s)
	{
		super(s);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getTabIndex()
	{
		return tabIndex;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTabIndex(int tabIndex)
	{
		if(this.tabIndex != tabIndex)
		{
			int oldValue = this.tabIndex;
			this.tabIndex = tabIndex;
			firePropertyChange(TAB_INDEX_PROPERTY,oldValue,tabIndex);
		}
	}
	
	
	/**
	 * Connects the menu <code>popupContent</code> with this DropDownButton.
	 * 
	 * @param popupContent
	 *            the popup menu for this button
	 */
	public void setPopupContent(XdevMenu popupContent)
	{
		this.popupContent = popupContent;
		
		if(popupContent != null)
		{
			if(popupContentHandler == null)
			{
				popupContentHandler = new MenuAdapter()
				{
					@Override
					public void menuSelected(MenuEvent e)
					{
						XdevDropdownButton button = XdevDropdownButton.this;
						button.popupContent.moveComponentsTo(button);
					}
					
					
					@Override
					public void menuDeselected(MenuEvent e)
					{
						reset();
					}
					
					
					@Override
					public void menuCanceled(MenuEvent e)
					{
						reset();
					}
					
					
					void reset()
					{
						XdevDropdownButton button = XdevDropdownButton.this;
						if(button.getMenuComponentCount() > 0)
						{
							XdevMenu.moveComponents(button,button.getMenuComponents(),
									button.popupContent);
						}
					}
				};
			}
			
			addMenuListener(popupContentHandler);
		}
		else if(popupContentHandler != null)
		{
			removeMenuListener(popupContentHandler);
		}
	}
	
	
	/**
	 * Returns the connected menu of this button, or <code>null</code> if none
	 * specified.
	 * 
	 * @return The connected menu of this button.
	 */
	public XdevMenu getPopupContent()
	{
		return popupContent;
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
	public void setBorder(Border border)
	{
		super.setBorder(border);
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
