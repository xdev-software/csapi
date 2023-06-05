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
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import xdev.lang.NotNull;
import xdev.ui.persistence.Persistable;

import com.jidesoft.icons.IconsFactory;
import com.jidesoft.swing.JideTabbedPane;


/**
 * {@code XdevTabbedPane2} is an extended version of {@code XdevTabbedPane}.
 * <p>
 * It offers a number of additional features, like:
 * <ul>
 * <li>hide tab area, if only one tab is visible</li>
 * <li>shrink tab width</li>
 * <li>scroll tabs with left / right buttons</li>
 * <li>show close button on tabs</li>
 * <li>show close button for tabbed pane</li>
 * <li>set a leading / trailing component in pane</li>
 * <li>display a drop down menu to select a tab</li>
 * </ul>
 * </p>
 * 
 * @author XDEV Software
 * 
 */
@BeanSettings(acceptChildren = true)
@XdevTabbedPaneSettings(tabType = XdevTab.class)
public class XdevTabbedPane2 extends JideTabbedPane implements Persistable
{
	/**
	 * serialVersionUID.
	 */
	private static final long			serialVersionUID	= 1L;
	
	/**
	 * Should the gui state be persisted? Defaults to {@code true}.
	 */
	private boolean						persistenceEnabled	= true;
	
	/**
	 * Right-click contextmenu for tabs.
	 */
	private XdevTabbedPane2ContextMenu	contextMenu;
	
	/**
	 * enabled state of context menu.
	 */
	private boolean						contextMenuEnabled;
	
	/**
	 * enabled state of the tab buttons. deault is enabled.
	 */
	private boolean						showTabButtons		= true;
	
	/**
	 * enabled state of the close button.
	 */
	private boolean						showCloseButton;
	
	/**
	 * enabled state of the close button on selected tab.
	 */
	private boolean						showCloseButtonOnSelectedTab;
	
	/**
	 * enabled state of the tab close buttons.
	 */
	private boolean						showCloseButtonOnTabs;
	
	
	/**
	 * Creates an empty <code>TabbedPane</code> with a default tab placement of
	 * <code>JTabbedPane.TOP</code>.
	 * 
	 * @see #addTab
	 */
	public XdevTabbedPane2()
	{
		super();
		initContextMenu();
	}
	
	
	/**
	 * Creates an empty <code>JideTabbedPane</code> with the specified tab
	 * placement and tab layout policy. Tab placement may be either:
	 * <code>JTabbedPane.TOP</code> or <code>JTabbedPane.BOTTOM</code> Tab
	 * layout policy should always be <code>JTabbedPane.SCROLL_TAB_LAYOUT</code>
	 * . <code>JTabbedPane</code> also supports
	 * <code>JTabbedPane.WRAP_TAB_LAYOUT</code>. However the style of tabs in
	 * <code>JideTabbedPane</code> doesn't match with
	 * <code>JTabbedPane.WRAP_TAB_LAYOUT</code> very well, so we decided not to
	 * support it.
	 * 
	 * @param tabPlacement
	 *            the placement for the tabs relative to the content
	 * @param tabLayoutPolicy
	 *            the policy for laying out tabs when all tabs will not fit on
	 *            one run
	 * @throws IllegalArgumentException
	 *             if tab placement or tab layout policy are not one of the
	 *             above supported values
	 * @see #addTab
	 */
	public XdevTabbedPane2(int tabPlacement, int tabLayoutPolicy) throws IllegalArgumentException
	{
		super(tabPlacement,tabLayoutPolicy);
		initContextMenu();
	}
	
	
	/**
	 * Creates an empty <code>TabbedPane</code> with the specified tab placement
	 * of either: <code>JTabbedPane.TOP</code>, <code>JTabbedPane.BOTTOM</code>,
	 * <code>JTabbedPane.LEFT</code>, or <code>JTabbedPane.RIGHT</code>.
	 * 
	 * @param tabPlacement
	 *            the placement for the tabs relative to the content
	 * @see #addTab
	 */
	public XdevTabbedPane2(int tabPlacement)
	{
		super(tabPlacement);
		initContextMenu();
	}
	
	
	/**
	 * Adds a {@link XdevTab} to the this {@link XdevTabbedPane}.
	 * 
	 * @param tab
	 *            the {@link XdevTab} to add
	 * 
	 * @throws NullPointerException
	 *             if <code>tab</code> is <code>null</code>
	 * 
	 */
	public void addTab(@NotNull XdevTab tab) throws NullPointerException
	{
		addTab(tab.getTitle(),tab.getIcon(),tab,tab.getToolTipText());
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void insertTab(String title, Icon icon, Component component, String tip, int index)
	{
		super.insertTab(title,icon,component,tip,index);
		setEnabledAt(index,component.isEnabled());
		
		if(component instanceof XdevTab)
		{
			((XdevTab)component).setIndex(index);
		}
	}
	
	
	/**
	 * Returns the component at <code>index</code>.
	 * 
	 * @param index
	 *            the index of the item being queried
	 * @return the <code>Component</code> at <code>index</code>
	 * @throws IndexOutOfBoundsException
	 *             if index is out of range (index < 0 || index >= tab count)
	 * 
	 */
	public Component getTabAt(int index) throws IndexOutOfBoundsException
	{
		return getComponentAt(index);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		String toString = UIUtils.toString(this);
		if(toString != null)
		{
			return toString;
		}
		
		return super.toString();
	}
	
	
	/**
	 * 
	 * Use {@link #addTab(XdevTab)} to add a {@link XdevTab} to the
	 * {@link XdevTabbedPane}. If use one of the &quot;add&quot; methods the
	 * {@link XdevTab} will not be displayed as tab.
	 * 
	 * <p>
	 * This method is only overridden to place a hint related to the use of the
	 * {@link #addTab(XdevTab)} method.
	 * </p>
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public Component add(Component comp)
	{
		return super.add(comp);
	}
	
	
	/**
	 * 
	 * Use {@link #addTab(XdevTab)} to add a {@link XdevTab} to the
	 * {@link XdevTabbedPane}. If use one of the &quot;add&quot; methods the
	 * {@link XdevTab} will not be displayed as tab.
	 * 
	 * <p>
	 * This method is only overridden to place a hint related to the use of the
	 * {@link #addTab(XdevTab)} method.
	 * </p>
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public Component add(Component comp, int index)
	{
		return super.add(comp,index);
	}
	
	
	/**
	 * 
	 * Use {@link #addTab(XdevTab)} to add a {@link XdevTab} to the
	 * {@link XdevTabbedPane}. If use one of the &quot;add&quot; methods the
	 * {@link XdevTab} will not be displayed as tab.
	 * 
	 * <p>
	 * This method is only overridden to place a hint related to the use of the
	 * {@link #addTab(XdevTab)} method.
	 * </p>
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public void add(Component comp, Object constraints)
	{
		super.add(comp,constraints);
	}
	
	
	/**
	 * 
	 * Use {@link #addTab(XdevTab)} to add a {@link XdevTab} to the
	 * {@link XdevTabbedPane}. If use one of the &quot;add&quot; methods the
	 * {@link XdevTab} will not be displayed as tab.
	 * 
	 * <p>
	 * This method is only overridden to place a hint related to the use of the
	 * {@link #addTab(XdevTab)} method.
	 * </p>
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public void add(Component comp, Object constraints, int index)
	{
		super.add(comp,constraints,index);
	}
	
	
	/**
	 * 
	 * Use {@link #addTab(XdevTab)} to add a {@link XdevTab} to the
	 * {@link XdevTabbedPane}. If use one of the &quot;add&quot; methods the
	 * {@link XdevTab} will not be displayed as tab.
	 * 
	 * <p>
	 * This method is only overridden to place a hint related to the use of the
	 * {@link #addTab(XdevTab)} method.
	 * </p>
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public Component add(String name, Component comp)
	{
		return super.add(name,comp);
	}
	
	
	/**
	 * Initializes the context menu and registers mouse listener on tabbedPane.
	 */
	private void initContextMenu()
	{
		
		contextMenu = new XdevTabbedPane2ContextMenu(this);
		contextMenuEnabled = true;
		
		this.addMouseListener(new MouseAdapter()
		{
			
			@Override
			public void mouseClicked(MouseEvent event)
			{
				if(contextMenuEnabled && SwingUtilities.isRightMouseButton(event))
				{
					int x = event.getX();
					int y = event.getY();
					XdevTabbedPane2 tp = (XdevTabbedPane2)event.getSource();
					
					try
					{
						int tabIndex = tp.getUI().tabForCoordinate(tp,x,y);
						
						if(tabIndex >= 0)
						{
							contextMenu.setTabIndex(tabIndex);
							contextMenu.setMenuItemsState();
							contextMenu.show(tp,x,y);
						}
					}
					catch(ArrayIndexOutOfBoundsException e)
					{
						// happens if heading or trailing component gets
						// clicked.
						// just do nothing in this case -> ignore exception
					}
				}
			}
			
		});
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see #savePersistentState()
	 */
	@Override
	public void loadPersistentState(String persistentState)
	{
		int selectedIndex = Integer.parseInt(persistentState);
		if(selectedIndex < this.getTabCount())
		{
			this.setSelectedIndex(selectedIndex);
		}
	}
	
	
	/**
	 * {@inheritDoc}.
	 * <p>
	 * Persisted properties:
	 * <ul>
	 * <li>selected index</li>
	 * </ul>
	 * </p>
	 */
	@Override
	public String savePersistentState()
	{
		return Integer.toString(this.getSelectedIndex());
	}
	
	
	/**
	 * Uses the name of the component as a persistent id.
	 * <p>
	 * If no name is specified the name of the class will be used. This will
	 * work only for one persistent instance of the class!
	 * </p>
	 * {@inheritDoc}
	 */
	@Override
	public String getPersistentId()
	{
		return (this.getName() != null) ? this.getName() : this.getClass().getSimpleName();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isPersistenceEnabled()
	{
		return persistenceEnabled;
	}
	
	
	/**
	 * Set if GUI state should be persisted
	 * @param persistenceEnabled
	 * 				{@code true} if state should be persisted
	 *
	 */
	public void setPersistenceEnabled(boolean persistenceEnabled)
	{
		this.persistenceEnabled = persistenceEnabled;
	}
	
	
	/**
	 * @return {@code true} if context menus on tabs are enabled
	 */
	public boolean isContextMenuEnabled()
	{
		return contextMenuEnabled;
	}
	
	
	/**
	 * En- or disabled the context menu on tabs.
	 * 
	 * @param contextMenuEnabled
	 *            if {@code true} context menu is enabled.
	 */
	public void setContextMenuEnabled(boolean contextMenuEnabled)
	{
		this.contextMenuEnabled = contextMenuEnabled;
	}
	
	
	
	/**
	 * Context menu for tabs. Gets displayed when user right clicks on a tab
	 * 
	 * @author XDEV Software
	 */
	private static class XdevTabbedPane2ContextMenu extends JPopupMenu
	{
		
		/**
		 * serialVersionUID.
		 */
		private static final long	serialVersionUID				= 1L;
		
		/**
		 * Language property for close.
		 */
		private static final String	KEY_CONTEXTMENT_CLOSE			= "tabbedpane2.contextmenu.close";
		/**
		 * Language property for close all.
		 */
		private static final String	KEY_CONTEXTMENT_CLOSE_ALL		= "tabbedpane2.contextmenu.closeAll";
		/**
		 * Language property for close others.
		 */
		private static final String	KEY_CONTEXTMENT_CLOSE_OTHERS	= "tabbedpane2.contextmenu.closeOthers";
		/**
		 * Language property for show next tab.
		 */
		private static final String	KEY_CONTEXTMENT_SHOW_NEXT		= "tabbedpane2.contextmenu.next";
		/**
		 * Language property for show previous tab.
		 */
		private static final String	KEY_CONTEXTMENT_SHOW_PREV		= "tabbedpane2.contextmenu.prev";
		
		/**
		 * the tabIndex for which the context menu was triggered.
		 */
		private int					tabIndex						= -1;
		/**
		 * the parent tabbed pane.
		 */
		private XdevTabbedPane2		tabbedPane;
		
		/**
		 * MenuItem for close current tab.
		 */
		private XdevMenuItem		miClose;
		/**
		 * MenuItem for close other tabs.
		 */
		private XdevMenuItem		miCloseOther;
		/**
		 * MenuItem for close all tabs.
		 */
		private XdevMenuItem		miCloseAll;
		/**
		 * MenuItem for select next tab.
		 */
		private XdevMenuItem		miNext;
		/**
		 * MenuItem for select previous tab.
		 */
		private XdevMenuItem		miPrev;
		
		
		/**
		 * Constructor for the context menu.
		 * 
		 * @param tabbedPane
		 *            the parent tabbed pane.
		 */
		public XdevTabbedPane2ContextMenu(XdevTabbedPane2 tabbedPane)
		{
			this.tabbedPane = tabbedPane;
			
			initMenu();
		}
		
		
		/**
		 * sets up the menuitem's and actions for the context menu.
		 */
		private void initMenu()
		{
			
			miClose = new XdevMenuItem(XdevCSResourceBundle.getString(KEY_CONTEXTMENT_CLOSE),
					getIcon("tabbedpane_close.gif"));
			miClose.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					tabbedPane.removeTabAt(tabIndex);
				}
			});
			this.add(miClose);
			
			miCloseAll = new XdevMenuItem(
					XdevCSResourceBundle.getString(KEY_CONTEXTMENT_CLOSE_ALL),
					getIcon("tabbedpane_closeall.gif"));
			miCloseAll.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					int lastTabIndex = tabbedPane.getTabCount() - 1;
					for(int i = lastTabIndex; i >= 0; i--)
					{
						tabbedPane.removeTabAt(i);
					}
				}
			});
			this.add(miCloseAll);
			
			miCloseOther = new XdevMenuItem(
					XdevCSResourceBundle.getString(KEY_CONTEXTMENT_CLOSE_OTHERS));
			miCloseOther.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					int lastTabIndex = tabbedPane.getTabCount() - 1;
					for(int i = lastTabIndex; i >= 0; i--)
					{
						if(i != tabIndex)
						{
							tabbedPane.removeTabAt(i);
						}
					}
				}
			});
			this.add(miCloseOther);
			
			this.add(new XdevMenuSeparator());
			
			miNext = new XdevMenuItem(XdevCSResourceBundle.getString(KEY_CONTEXTMENT_SHOW_NEXT),
					getIcon("tabbedpane_next.png"));
			miNext.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					int lastTabIndex = tabbedPane.getTabCount() - 1;
					if(tabIndex < lastTabIndex)
					{
						tabbedPane.setSelectedIndex(tabIndex + 1);
					}
				}
			});
			this.add(miNext);
			
			miPrev = new XdevMenuItem(XdevCSResourceBundle.getString(KEY_CONTEXTMENT_SHOW_PREV),
					getIcon("tabbedpane_prev.png"));
			miPrev.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					if(tabIndex > 0)
					{
						tabbedPane.setSelectedIndex(tabIndex - 1);
					}
				}
			});
			this.add(miPrev);
			
			this.setFocusable(false);
		}
		
		
		/**
		 * Helper method for retrieving a {@link Icon} by name.
		 * 
		 * @param name
		 *            the name of the icon
		 * @return the icon
		 */
		private Icon getIcon(String name)
		{
			return IconsFactory.getImageIcon(XdevTabbedPane2.class,"/icons/" + name);
		}
		
		
		/**
		 * Set the current tab index.
		 * 
		 * @param tabIndex
		 *            the current tab index.
		 */
		public void setTabIndex(int tabIndex)
		{
			this.tabIndex = tabIndex;
		}
		
		
		/**
		 * Disables menuItems that can't be applied on the current tab.
		 */
		public void setMenuItemsState()
		{
			miCloseOther.setEnabled(tabbedPane.getTabCount() > 1);
			miCloseAll.setEnabled(tabbedPane.getTabCount() > 0);
			miPrev.setEnabled(tabIndex > 0);
			miNext.setEnabled(tabIndex < tabbedPane.getTabCount() - 1);
		}
	}
	
	
	/**
	 * Sets whether or not this component is enabled.
	 * <p><b>
	 * Implicitly disables the additional tab buttons.
	 * </p></b>
	 */
	@Override
	public void setEnabled(boolean enabled)
	{
		// en/-disable the tabs
		super.setEnabled(enabled);
		
		// en/-disable the popup menu
		this.setContextMenuEnabled(enabled);
		
		// workaround for JIDE - enabled state Issues #11788, #12132
		if(!enabled)
		{
			// if the whole tabbedPane is deactivated -> enable workaround for
			// disable the additional buttons
			
			this.enableTabButtons(enabled);
			
			this.enableCloseButton(enabled);
			
			this.enableCloseButtonOnSelectedTab(enabled);
			
			this.enableCloseButtonOnTab(enabled);
		}
		else
		{
			// if whole tabbedPane is activated -> disable workaround and if
			// pre-enabled, re-enable additional buttons
			this.setShowTabButtons(this.showTabButtons);
			
			this.setShowCloseButton(this.showCloseButton);
			
			this.setShowCloseButtonOnSelectedTab(this.showCloseButtonOnSelectedTab);
			
			this.setShowCloseButtonOnTab(this.showCloseButtonOnTabs);
		}
	}
	
	
	/**
	 * Sets if the close button is visible. Close button can be either side by
	 * side with scroll buttons, or on each tab. If you call
	 * setShowCloseButton(false), it will hide close buttons for both cases.
	 */
	@Override
	public void setShowCloseButton(boolean paramBoolean)
	{
		// check for workaround flag here
		this.showCloseButton = paramBoolean;
		
		if(this.isEnabled())
		{
			enableCloseButton(paramBoolean);
		}
		
	}
	
	
	/**
	 * Workaround Methods for Issues #11788, #12132
	 * 
	 * @param paramBoolean
	 *            the enabled state
	 */
	private void enableCloseButton(boolean paramBoolean)
	{
		super.setShowCloseButton(paramBoolean);
	}
	
	
	/**
	 * Shows the close button on the selected tab only.
	 * 
	 * <p>
	 * <b> Deactivates the functionality of
	 * {@link XdevTabbedPane2#setShowCloseButtonOnTab(boolean)} and
	 * {@link XdevTabbedPane2#setShowCloseButton(boolean)}. </b>
	 * </p>
	 */
	@Override
	public void setShowCloseButtonOnSelectedTab(boolean paramBoolean)
	{
		// check for workaround flag here
		this.showCloseButtonOnSelectedTab = paramBoolean;
		
		if(this.isEnabled())
		{
			this.enableCloseButtonOnSelectedTab(paramBoolean);
		}
	}
	
	
	/**
	 * Workaround Methods for Issues #11788, #12132
	 * 
	 * @param paramBoolean
	 *            the enabled state
	 */
	private void enableCloseButtonOnSelectedTab(boolean paramBoolean)
	{
		super.setShowCloseButtonOnSelectedTab(paramBoolean);
	}
	
	
	/**
	 * Sets the value if tab buttons are always visible.
	 */
	@Override
	public void setShowTabButtons(boolean paramBoolean)
	{
		this.showTabButtons = paramBoolean;
		
		if(this.isEnabled())
		{
			this.enableTabButtons(paramBoolean);
		}
	}
	
	
	/**
	 * Workaround Methods for Issues #11788, #12132
	 * 
	 * @param paramBoolean
	 *            the enabled state
	 */
	private void enableTabButtons(boolean paramBoolean)
	{
		super.setShowTabButtons(paramBoolean);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setShowCloseButtonOnTab(boolean paramBoolean)
	{
		this.showCloseButtonOnTabs = paramBoolean;
		
		if(this.isEnabled())
		{
			this.enableCloseButtonOnTab(paramBoolean);
		}
		
	}
	
	
	/**
	 * Workaround Methods for Issues #11788, #12132
	 * 
	 * @param paramBoolean
	 *            the enabled state
	 */
	private void enableCloseButtonOnTab(boolean paramBoolean)
	{
		super.setShowCloseButtonOnTab(paramBoolean);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTabResizeMode(int resizeMode)
	{
		boolean showIcons = resizeMode != JideTabbedPane.RESIZE_MODE_COMPRESSED;
		setShowIconsOnTab(showIcons);
		setUseDefaultShowIconsOnTab(showIcons);
		super.setTabResizeMode(resizeMode);
	}
	
	
	// ////////////////////////////////////////////////////////////
	// / Following properties are now hidden on development time///
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
