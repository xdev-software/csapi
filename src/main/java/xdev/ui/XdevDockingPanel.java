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


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.swing.RootPaneContainer;
import javax.swing.SwingConstants;

import xdev.ui.persistence.Persistable;

import com.jidesoft.action.DockableBarDockableHolderPanel;
import com.jidesoft.docking.DockableFrame;
import com.jidesoft.docking.DockingManager;
import com.jidesoft.docking.DockingManager.TabbedPaneCustomizer;
import com.jidesoft.docking.Workspace;
import com.jidesoft.docking.event.DockableFrameListener;
import com.jidesoft.swing.JideTabbedPane;
import com.jidesoft.utils.Base64;
import com.jidesoft.utils.SystemInfo;


/**
 * A parent container for holding {@link XdevCommandBar} and
 * {@link XdevDockableFrame} instances.
 * 
 * @author XDEV Software
 * 
 */
@BeanSettings(acceptChildren = true)
public class XdevDockingPanel extends DockableBarDockableHolderPanel implements SwingConstants,
		TabbedPaneCustomizer, Persistable
{
	/**
	 * serialVersionUID.
	 */
	private static final long	serialVersionUID	= 1L;
	
	/**
	 * The tabulator placement (defaults to {@link SwingConstants#TOP}).
	 */
	private int					tabPlacement		= TOP;
	
	/**
	 * Should the gui state be persisted? Defaults to {@code true}.
	 */
	private boolean				persistenceEnabled	= true;
	
	
	/**
	 * @see DockableBarDockableHolderPanel#DockableBarDockableHolderPanel()
	 */
	public XdevDockingPanel()
	{
		super();
		
		init();
	}
	
	
	/**
	 * @param rootPaneContainer
	 *            the rootContainer
	 * @see DockableBarDockableHolderPanel#DockableBarDockableHolderPanel(RootPaneContainer)
	 */
	public XdevDockingPanel(RootPaneContainer rootPaneContainer)
	{
		super(rootPaneContainer);
		
		init();
	}
	
	
	/**
	 * Initializes the {@link XdevDockingPanel}.
	 */
	private void init()
	{
		DockingManager dockingManager = getDockingManager();
		
		dockingManager.setFloatingContainerType(DockingManager.FLOATING_CONTAINER_TYPE_FRAME);
		dockingManager.setUseDecoratedFloatingContainer(true);
		dockingManager.setCrossDraggingAllowed(false);
		dockingManager.setCrossDroppingAllowed(false);
		dockingManager.getUndoManager().setLimit(100);
		dockingManager.setEasyTabDock(true);
		dockingManager.setContinuousLayout(true);
		dockingManager.setTabbedPaneCustomizer(this);
		dockingManager.getWorkspace().setLayout(new BorderLayout());
		
		if(SystemInfo.isJdk6u10Above())
		{
			dockingManager.setOutlineMode(DockingManager.HW_OUTLINE_MODE);
		}
	}
	
	
	/**
	 * @param l
	 *            a {@link DockableFrameListener}
	 * @see com.jidesoft.docking.DockingManager#addDockableFrameListener(DockableFrameListener)
	 */
	public void addDockableFrameListener(DockableFrameListener l)
	{
		getDockingManager().addDockableFrameListener(l);
	}
	
	
	/**
	 * @param l
	 *            a {@link DockableFrameListener}
	 * @see com.jidesoft.docking.DockingManager#removeDockableFrameListener(DockableFrameListener)
	 */
	public void removeDockableFrameListener(DockableFrameListener l)
	{
		getDockingManager().removeDockableFrameListener(l);
	}
	
	
	/**
	 * @see com.jidesoft.docking.DockingManager#getDockableFrameListeners()
	 */
	public DockableFrameListener[] getDockableFrameListeners()
	{
		return getDockingManager().getDockableFrameListeners();
	}
	
	
	/**
	 * 
	 * @see com.jidesoft.swing.LayoutPersistence#beginLoadLayoutData()
	 */
	public void beginLoadLayoutData()
	{
		getLayoutPersistence().beginLoadLayoutData();
	}
	
	
	/**
	 * @param dockableframe
	 *            a {@link DockableFrame}
	 * @see com.jidesoft.docking.DockingManager#addFrame(com.jidesoft.docking.DockableFrame)
	 */
	public void addFrame(DockableFrame dockableframe)
	{
		getDockingManager().addFrame(dockableframe);
	}
	
	
	/**
	 * 
	 * @see com.jidesoft.swing.LayoutPersistence#resetToDefault()
	 */
	public void resetToDefault()
	{
		getLayoutPersistence().resetToDefault();
	}
	
	
	/**
	 * Returns a {@link String} representation of this {@link XdevDockingPanel}s
	 * layout.
	 * 
	 * @return a base 64 encoded {@link String}
	 */
	public String getLayoutDataString()
	{
		try
		{
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			getDockingManager().saveLayoutTo(bout);
			byte[] data = bout.toByteArray();
			return Base64.encodeBytes(data,Base64.GZIP | Base64.DONT_BREAK_LINES);
		}
		catch(Exception e)
		{
			// shouldn't happen
			e.printStackTrace();
			return "";
		}
	}
	
	
	/**
	 * Restores the layout of this {@link XdevDockingPanel} from a specified
	 * {@link String}.
	 * 
	 * @param layoutData
	 *            a base 64 encoded String containing layout data.
	 */
	public void setLayoutDataString(String layoutData)
	{
		try
		{
			getDockingManager().loadLayoutFrom(new ByteArrayInputStream(Base64.decode(layoutData)));
		}
		catch(Exception e)
		{
			// shouldn't happen
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Sets the workspace component for this {@link XdevDockingPanel}.
	 * 
	 * @param component
	 *            the {@link Component} to be used as the workspace component.
	 */
	public void setWorkspaceComponent(Component component)
	{
		Workspace workspace = getDockingManager().getWorkspace();
		workspace.removeAll();
		workspace.add(component);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void customize(JideTabbedPane tabbedPane)
	{
		tabbedPane.setTabPlacement(tabPlacement);
	}
	
	
	/**
	 * Sets the tab placement for the tabbedpane's. Possible values are:
	 * <ul>
	 * <li><code>JTabbedPane.TOP</code>
	 * <li><code>JTabbedPane.BOTTOM</code>
	 * <li><code>JTabbedPane.LEFT</code>
	 * <li><code>JTabbedPane.RIGHT</code>
	 * </ul>
	 * The default value, if not set, is <code>SwingConstants.TOP</code>.
	 * 
	 * @param tabPlacement
	 *            the placement for the tabs relative to the content
	 * @exception IllegalArgumentException
	 *                if tab placement value isn't one of the above valid values
	 */
	public void setTabPlacement(int tabPlacement) throws IllegalArgumentException
	{
		this.tabPlacement = tabPlacement;
		getDockingManager().setTabbedPaneCustomizer(this);
	}
	
	
	/**
	 * Returns the placement of the tabs for the tabbedpane's.
	 * 
	 * @return the current tab placement as defined in {@link SwingConstants}.
	 * @see #setTabPlacement
	 */
	public int getTabPlacement()
	{
		return tabPlacement;
	}
	
	
	/**
	 * @param flag
	 *            a flag
	 * @see com.jidesoft.docking.DockingManager#setDragAllTabs(boolean)
	 */
	public void setDragAllTabs(boolean flag)
	{
		getDockingManager().setDragAllTabs(flag);
	}
	
	
	/**
	 * @see com.jidesoft.docking.DockingManager#isDragAllTabs()
	 */
	public boolean isDragAllTabs()
	{
		return getDockingManager().isDragAllTabs();
	}
	
	
	/**
	 * @param flag
	 *            a flag
	 * @see com.jidesoft.docking.DockingManager#setGroupAllowedOnSidePane(boolean)
	 */
	public void setGroupAllowedOnSidePane(boolean flag)
	{
		getDockingManager().setGroupAllowedOnSidePane(flag);
	}
	
	
	/**
	 * @see com.jidesoft.docking.DockingManager#isGroupAllowedOnSidePane()
	 */
	public boolean isGroupAllowedOnSidePane()
	{
		return getDockingManager().isGroupAllowedOnSidePane();
	}
	
	
	/**
	 * @param flag
	 *            a flag
	 * @see com.jidesoft.docking.DockingManager#setContinuousLayout(boolean)
	 */
	public void setContinuousLayout(boolean flag)
	{
		getDockingManager().setContinuousLayout(flag);
	}
	
	
	/**
	 * @see com.jidesoft.docking.DockingManager#isContinuousLayout()
	 */
	public boolean isContinuousLayout()
	{
		return getDockingManager().isContinuousLayout();
	}
	
	
	/**
	 * @param flag
	 *            a flag
	 * @see com.jidesoft.docking.DockingManager#setEasyTabDock(boolean)
	 */
	public void setEasyTabDock(boolean flag)
	{
		getDockingManager().setEasyTabDock(flag);
	}
	
	
	/**
	 * @see com.jidesoft.docking.DockingManager#isEasyTabDock()
	 */
	public boolean isEasyTabDock()
	{
		return getDockingManager().isEasyTabDock();
	}
	
	
	/**
	 * @param flag
	 *            a flag
	 * @see com.jidesoft.docking.DockingManager#setShowGripper(boolean)
	 */
	public void setShowGripper(boolean flag)
	{
		getDockingManager().setShowGripper(flag);
	}
	
	
	/**
	 * @see com.jidesoft.docking.DockingManager#isShowGripper()
	 */
	public boolean isShowGripper()
	{
		return getDockingManager().isShowGripper();
	}
	
	
	/**
	 * @param flag
	 *            a flag
	 * @see com.jidesoft.docking.DockingManager#setShowDividerGripper(boolean)
	 */
	public void setShowDividerGripper(boolean flag)
	{
		getDockingManager().setShowDividerGripper(flag);
	}
	
	
	/**
	 * @see com.jidesoft.docking.DockingManager#isShowDividerGripper()
	 */
	public boolean isShowDividerGripper()
	{
		return getDockingManager().isShowDividerGripper();
	}
	
	
	/**
	 * @param flag
	 *            a flag
	 * @see com.jidesoft.docking.DockingManager#setDragGripperOnly(boolean)
	 */
	public void setDragGripperOnly(boolean flag)
	{
		getDockingManager().setDragGripperOnly(flag);
	}
	
	
	/**
	 * @see com.jidesoft.docking.DockingManager#isDragGripperOnly()
	 */
	public boolean isDragGripperOnly()
	{
		return getDockingManager().isDragGripperOnly();
	}
	
	
	/**
	 * @param flag
	 *            a flag
	 * @see com.jidesoft.docking.DockingManager#setShowTitleBar(boolean)
	 */
	public void setShowTitleBar(boolean flag)
	{
		getDockingManager().setShowTitleBar(flag);
	}
	
	
	/**
	 * @param i
	 *            a valid double click action
	 * @see com.jidesoft.docking.DockingManager#setDoubleClickAction(int)
	 */
	public void setDoubleClickAction(int i)
	{
		getDockingManager().setDoubleClickAction(i);
	}
	
	
	/**
	 * @see com.jidesoft.docking.DockingManager#getDoubleClickAction()
	 */
	public int getDoubleClickAction()
	{
		return getDockingManager().getDoubleClickAction();
	}
	
	
	/**
	 * @param flag
	 *            a flag
	 * @see com.jidesoft.docking.DockingManager#setAutoDocking(boolean)
	 */
	public void setAutoDocking(boolean flag)
	{
		getDockingManager().setAutoDocking(flag);
	}
	
	
	/**
	 * @see com.jidesoft.docking.DockingManager#isAutoDocking()
	 */
	public boolean isAutoDocking()
	{
		return getDockingManager().isAutoDocking();
	}
	
	
	/**
	 * @param flag
	 *            a flag
	 * @see com.jidesoft.docking.DockingManager#setUseDecoratedFloatingContainer(boolean)
	 */
	public void setUseDecoratedFloatingContainer(boolean flag)
	{
		getDockingManager().setUseDecoratedFloatingContainer(flag);
	}
	
	
	/**
	 * @see com.jidesoft.docking.DockingManager#isUseDecoratedFloatingContainer()
	 */
	public boolean isUseDecoratedFloatingContainer()
	{
		return getDockingManager().isUseDecoratedFloatingContainer();
	}
	
	
	/**
	 * @param flag
	 *            a flag
	 * @see com.jidesoft.docking.DockingManager#setAutohideShowingContentHidden(boolean)
	 */
	public void setAutohideShowingContentHidden(boolean flag)
	{
		getDockingManager().setAutohideShowingContentHidden(flag);
	}
	
	
	/**
	 * @see com.jidesoft.docking.DockingManager#isAutohideShowingContentHidden()
	 */
	public boolean isAutohideShowingContentHidden()
	{
		return getDockingManager().isAutohideShowingContentHidden();
	}
	
	
	/**
	 * @param i
	 *            a valid container type
	 * @see com.jidesoft.docking.DockingManager#setFloatingContainerType(int)
	 */
	public void setFloatingContainerType(int i)
	{
		getDockingManager().setFloatingContainerType(i);
	}
	
	
	/**
	 * @see com.jidesoft.docking.DockingManager#getFloatingContainerType()
	 */
	public int getFloatingContainerType()
	{
		return getDockingManager().getFloatingContainerType();
	}
	
	
	/**
	 * @param flag
	 *            a flag
	 * @see com.jidesoft.docking.DockingManager#setShowWorkspace(boolean)
	 */
	public void setShowWorkspace(boolean flag)
	{
		getDockingManager().setShowWorkspace(flag);
	}
	
	
	/**
	 * @see com.jidesoft.docking.DockingManager#isShowWorkspace()
	 */
	public boolean isShowWorkspace()
	{
		return getDockingManager().isShowWorkspace();
	}
	
	
	/**
	 * @param i
	 *            size in pixel
	 * @see com.jidesoft.docking.DockingManager#setSensitiveAreaSize(int)
	 */
	public void setSensitiveAreaSize(int i)
	{
		getDockingManager().setSensitiveAreaSize(i);
	}
	
	
	/**
	 * @see com.jidesoft.docking.DockingManager#getSensitiveAreaSize()
	 */
	public int getSensitiveAreaSize()
	{
		return getDockingManager().getSensitiveAreaSize();
	}
	
	
	/**
	 * @param i
	 *            size in pixel
	 * @see com.jidesoft.docking.DockingManager#setOutsideSensitiveAreaSize(int)
	 */
	public void setOutsideSensitiveAreaSize(int i)
	{
		getDockingManager().setOutsideSensitiveAreaSize(i);
	}
	
	
	/**
	 * @see com.jidesoft.docking.DockingManager#getOutsideSensitiveAreaSize()
	 */
	public int getOutsideSensitiveAreaSize()
	{
		return getDockingManager().getOutsideSensitiveAreaSize();
	}
	
	
	/**
	 * @param flag
	 *            a flag
	 * @see com.jidesoft.docking.DockingManager#setTabDockAllowed(boolean)
	 */
	public void setTabDockAllowed(boolean flag)
	{
		getDockingManager().setTabDockAllowed(flag);
	}
	
	
	/**
	 * @see com.jidesoft.docking.DockingManager#isTabDockAllowed()
	 */
	public boolean isTabDockAllowed()
	{
		return getDockingManager().isTabDockAllowed();
	}
	
	
	/**
	 * @param flag
	 *            a flag
	 * @see com.jidesoft.docking.DockingManager#setSideDockAllowed(boolean)
	 */
	public void setSideDockAllowed(boolean flag)
	{
		getDockingManager().setSideDockAllowed(flag);
	}
	
	
	/**
	 * @see com.jidesoft.docking.DockingManager#isSideDockAllowed()
	 */
	public boolean isSideDockAllowed()
	{
		return getDockingManager().isSideDockAllowed();
	}
	
	
	/**
	 * @param flag
	 *            a flag
	 * @see com.jidesoft.docking.DockingManager#setTabReorderAllowed(boolean)
	 */
	public void setTabReorderAllowed(boolean flag)
	{
		getDockingManager().setTabReorderAllowed(flag);
	}
	
	
	/**
	 * @see com.jidesoft.docking.DockingManager#isTabReorderAllowed()
	 */
	public boolean isTabReorderAllowed()
	{
		return getDockingManager().isTabReorderAllowed();
	}
	
	
	/**
	 * @param flag
	 *            a flag
	 * @see com.jidesoft.docking.DockingManager#setNestedFloatingAllowed(boolean)
	 */
	public void setNestedFloatingAllowed(boolean flag)
	{
		getDockingManager().setNestedFloatingAllowed(flag);
	}
	
	
	/**
	 * @param flag
	 *            a flag
	 * @see com.jidesoft.docking.DockingManager#setSidebarRollover(boolean)
	 */
	public void setSidebarRollover(boolean flag)
	{
		getDockingManager().setSidebarRollover(flag);
	}
	
	
	/**
	 * @see com.jidesoft.docking.DockingManager#isSidebarRollover()
	 */
	public boolean isSidebarRollover()
	{
		return getDockingManager().isSidebarRollover();
	}
	
	
	/**
	 * @param i
	 *            snap grid size in pixel
	 * @see com.jidesoft.docking.DockingManager#setSnapGridSize(int)
	 */
	public void setSnapGridSize(int i)
	{
		getDockingManager().setSnapGridSize(i);
	}
	
	
	/**
	 * @see com.jidesoft.docking.DockingManager#getSnapGridSize()
	 */
	public int getSnapGridSize()
	{
		return getDockingManager().getSnapGridSize();
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see #savePersistentState()
	 */
	@Override
	public void loadPersistentState(String persistentState)
	{
		this.setLayoutDataString(persistentState);
	}
	
	
	/**
	 * {@inheritDoc}
	 * <p>
	 * Persists the layout information for this {@link XdevDockingPanel}.
	 * </p>
	 */
	@Override
	public String savePersistentState()
	{
		return this.getLayoutDataString();
	}
	
	
	/**
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
	 * Sets the persistenceEnabled flag.
	 * 
	 * @param persistenceEnabled
	 *            the state for this instance
	 */
	public void setPersistenceEnabled(boolean persistenceEnabled)
	{
		this.persistenceEnabled = persistenceEnabled;
	}
	
	
	// ////////////////////////////////////////////////////////////
	// / Following properties are hidden on development time///
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
	public void setEnabled(boolean paramBoolean)
	{
		super.setEnabled(paramBoolean);
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
