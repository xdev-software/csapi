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
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Enumeration;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import com.jidesoft.combobox.QuickComboBoxFilterField;
import com.jidesoft.swing.JidePopupMenu;
import com.jidesoft.tree.QuickTreeFilterField;

import xdev.ui.persistence.Persistable;
import xdev.ui.quickfilter.QuickFilterSearchMode;
import xdev.ui.quickfilter.QuickFilterSearchOption;
import xdev.ui.tree.XdevTreeNode;
import xdev.util.XdevList;


/**
 * {@code XdevQuickTreeFilterField} can be used to dynamically filter the rows
 * of a table.
 * <p>
 * It takes any {@link TreeModel} as input.
 * </p>
 * 
 * @author XDEV Software
 * @see QuickComboBoxFilterField
 */
public class XdevQuickTreeFilterField extends QuickTreeFilterField implements
		XdevFocusCycleComponent, Persistable, TextComponentOwner
{
	/**
	 * serialVersionUID.
	 */
	private static final long	serialVersionUID		= 1L;
	
	/**
	 * Should the gui state be persisted? Defaults to {@code true}.
	 */
	private boolean				persistenceEnabled		= true;
	
	/**
	 * Flag for disabling the context menu.
	 */
	private boolean				contextMenuDisabled		= false;
	
	/**
	 * Flag for dynamic tree expansion for filtered leaf nodes.
	 */
	private boolean				expandFilteredLeafNodes	= false;
	
	
	/**
	 * @see QuickTreeFilterField#QuickTreeFilterField()
	 */
	public XdevQuickTreeFilterField()
	{
		super();
		
		initDefaults();
	}
	
	
	/**
	 * @param treeModel
	 *            the TreeModel
	 * @see QuickTreeFilterField#QuickTreeFilterField(TreeModel)
	 */
	public XdevQuickTreeFilterField(TreeModel treeModel)
	{
		super(treeModel);
		
		initDefaults();
	}
	
	
	/**
	 * Initializes the default values for search mode (wildcard) and search
	 * option (match anywhere).
	 */
	protected void initDefaults()
	{
		setSearchMode(QuickFilterSearchMode.WILDCARD);
		setSearchOption(QuickFilterSearchOption.MATCH_ANYWHERE);
	}
	
	
	/**
	 * Connects the filter field with a tree, meaning it always uses the tree's
	 * model.
	 * 
	 * @param tree
	 *            the tree to connect with.
	 */
	public void setFilterFor(final JTree tree)
	{
		setTree(tree);
		setTreeModel(tree.getModel());
		tree.setModel(getDisplayTreeModel());
	}
	
	private PropertyChangeListener	treeModelChangeListener;
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTree(JTree newTree)
	{
		JTree oldTable = getTree();
		if(oldTable != newTree)
		{
			if(treeModelChangeListener == null)
			{
				treeModelChangeListener = new PropertyChangeListener()
				{
					boolean	fireChanged	= true;
					
					
					@Override
					public void propertyChange(PropertyChangeEvent evt)
					{
						if(fireChanged)
						{
							try
							{
								fireChanged = false;
								
								JTree tree = (JTree)evt.getSource();
								if(tree.getModel() != getDisplayTreeModel())
								{
									setTreeModel(tree.getModel());
									tree.setModel(getDisplayTreeModel());
								}
							}
							finally
							{
								fireChanged = true;
							}
						}
					}
				};
			}
			
			if(oldTable != null)
			{
				oldTable.removePropertyChangeListener("model",treeModelChangeListener);
			}
			
			super.setTree(newTree);
			
			newTree.addPropertyChangeListener("model",treeModelChangeListener);
		}
	}
	
	
	/**
	 * Updates the {@code XdevQuickFilterPane} instance.
	 * <p>
	 * Should be called, if the default constructor was used to create the
	 * instance.
	 * </p>
	 * 
	 * @param treeModel
	 *            the model object of the tree to filter
	 */
	public void setModel(TreeModel treeModel)
	{
		this.setTreeModel(treeModel);
	}
	
	//
	// XdevFocusCycleComponent impl - start
	//
	
	/**
	 * the initial tabIndex.
	 */
	private int	tabIndex	= -1;
	
	
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
	
	
	//
	// XdevFocusCycleComponent impl - end
	//
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see #savePersistentState()
	 */
	@Override
	public void loadPersistentState(String persistentState)
	{
		String[] persistentValues = persistentState.split(Persistable.VALUE_SEPARATOR);
		int index = 0;
		try
		{
			this.setCaseSensitive(Boolean.parseBoolean(persistentValues[index++]));
			this.setRegexEnabled(Boolean.parseBoolean(persistentValues[index++]));
			this.setWildcardEnabled(Boolean.parseBoolean(persistentValues[index++]));
			this.setFromStart(Boolean.parseBoolean(persistentValues[index++]));
			this.setFromEnd(Boolean.parseBoolean(persistentValues[index++]));
			// tree specific flags
			this.setHideEmptyParentNode(Boolean.parseBoolean(persistentValues[index++]));
			this.setKeepAllChildren(Boolean.parseBoolean(persistentValues[index++]));
			this.setMatchesLeafNodeOnly(Boolean.parseBoolean(persistentValues[index++]));
			
		}
		catch(Exception e)
		{
			// do nothing her, if persistent value can't be retrieved...
		}
	}
	
	
	/**
	 * {@inheritDoc}.
	 * <p>
	 * Persisted properties:
	 * <ul>
	 * <li>Case Sensitivity</li>
	 * <li>FilterMode (Regex or Wildcard used?)</li>
	 * <li>MatchMode (exact, from start, any)</li>
	 * </ul>
	 * </p>
	 */
	@Override
	public String savePersistentState()
	{
		StringBuilder persistentState = new StringBuilder();
		
		persistentState.append(Boolean.toString(this.isCaseSensitive()));
		persistentState.append(Persistable.VALUE_SEPARATOR);
		persistentState.append(Boolean.toString(this.isRegexEnabled()));
		persistentState.append(Persistable.VALUE_SEPARATOR);
		persistentState.append(Boolean.toString(this.isWildcardEnabled()));
		persistentState.append(Persistable.VALUE_SEPARATOR);
		persistentState.append(Boolean.toString(this.isFromStart()));
		persistentState.append(Persistable.VALUE_SEPARATOR);
		persistentState.append(Boolean.toString(this.isFromEnd()));
		// tree specific flags
		persistentState.append(Persistable.VALUE_SEPARATOR);
		persistentState.append(Boolean.toString(this.isHideEmptyParentNode()));
		persistentState.append(Persistable.VALUE_SEPARATOR);
		persistentState.append(Boolean.toString(this.isKeepAllChildren()));
		persistentState.append(Persistable.VALUE_SEPARATOR);
		persistentState.append(Boolean.toString(this.isMatchesLeafNodeOnly()));
		
		return persistentState.toString();
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
		return this.persistenceEnabled;
	}
	
	
	/**
	 * Enables / Disables the persisting of the GuiState for this component.
	 * 
	 * @param persistenceEnabled
	 *            if {@code true} gui persistence is enabled.
	 */
	public void setPersistenceEnabled(boolean persistenceEnabled)
	{
		this.persistenceEnabled = persistenceEnabled;
	}
	
	
	/**
	 * {@inheritDoc}.
	 * 
	 * <p>
	 * To set the filter icon you may use {@link #setFilterIcon(Icon)}
	 * </p>
	 * 
	 * @see #setFilterIcon(Icon)
	 */
	@Override
	public void setIcon(Icon icon)
	{
		super.setIcon(icon);
	}
	
	
	/**
	 * Sets the {@link QuickFilterSearchMode} for this QuickFilterField.
	 * 
	 * @param searchMode
	 *            {@link QuickFilterSearchMode} to used by this QuickFilterField
	 * 
	 * @see QuickFilterSearchMode
	 */
	public void setSearchMode(final QuickFilterSearchMode searchMode)
	{
		QuickFilterSearchMode.setSearchMode(this,searchMode);
	}
	
	
	/**
	 * Returns the {@link QuickFilterSearchMode} of this QuickFilterField.
	 * 
	 * @return the {@link QuickFilterSearchMode} of this QuickFilterField.
	 * @since 1.1
	 */
	public QuickFilterSearchMode getSearchMode()
	{
		return QuickFilterSearchMode.getSearchMode(this);
	}
	
	
	/**
	 * Returns the {@link QuickFilterSearchOption} of this QuickFilterField.
	 * 
	 * @return the {@link QuickFilterSearchOption} of this QuickFilterField.
	 * @since 1.1
	 */
	public QuickFilterSearchOption getSearchOption()
	{
		return QuickFilterSearchOption.getSearchOption(this);
	}
	
	
	/**
	 * Sets the {@link QuickFilterSearchOption} for this QuickFilterField.
	 * 
	 * @param searchOption
	 *            {@link QuickFilterSearchOption} to used by this
	 *            QuickFilterField
	 * 
	 * @see QuickFilterSearchOption
	 */
	public void setSearchOption(final QuickFilterSearchOption searchOption)
	{
		QuickFilterSearchOption.setSearchOption(this,searchOption);
	}
	
	
	/**
	 * Overriding this method here to make the display of the context menu
	 * configurable.
	 * 
	 * @return a {@link JidePopupMenu} containing the context menu.
	 */
	@Override
	protected JidePopupMenu createContextMenu()
	{
		JidePopupMenu popupMenu = super.createContextMenu();
		if(isContextMenuDisabled())
		{
			popupMenu.removeAll();
		}
		return popupMenu;
	}
	
	
	/**
	 * Is the context menu currently disabled?
	 * 
	 * @return {@code true}, if context menu is disabled
	 */
	public boolean isContextMenuDisabled()
	{
		return contextMenuDisabled;
	}
	
	
	/**
	 * En- or disables the context menu (defaults to enabled).
	 * 
	 * @param contextMenuDisabled
	 *            if set to {@code true}, context menu gets disabled
	 */
	public void setContextMenuDisabled(boolean contextMenuDisabled)
	{
		this.contextMenuDisabled = contextMenuDisabled;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public JTextComponent getTextComponent()
	{
		return this.getTextField();
	}
	
	
	/**
	 * Registers a {@link DocumentListener} on the contained {@link JTextField}.
	 * 
	 * @param documentListener
	 *            a {@link DocumentListener} to register
	 */
	public void addDocumentListener(DocumentListener documentListener)
	{
		this.getTextField().getDocument().addDocumentListener(documentListener);
	}
	
	
	/**
	 * Deregisters a {@link DocumentListener} on the contained
	 * {@link JTextField}.
	 * 
	 * @param documentListener
	 *            a {@link DocumentListener} to deregister
	 */
	public void removeDocumentListener(DocumentListener documentListener)
	{
		this.getTextField().getDocument().removeDocumentListener(documentListener);
	}
	
	
	/**
	 * Registers a {@link CaretListener} on the contained {@link JTextField}.
	 * 
	 * @param caretListener
	 *            a {@link CaretListener} to register
	 */
	public void addCaretListener(CaretListener caretListener)
	{
		this.getTextField().addCaretListener(caretListener);
	}
	
	
	/**
	 * Deregisters a {@link CaretListener} on the contained {@link JTextField}.
	 * 
	 * @param caretListener
	 *            a {@link CaretListener} to deregister
	 */
	public void removeCaretListener(CaretListener caretListener)
	{
		this.getTextField().removeCaretListener(caretListener);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void applyFilter(String filterText)
	{
		super.applyFilter(filterText);
		
		/*
		 * #12407 - if expandFilteredLeafNodes is true, the tree will
		 * automatically be expanded on to the leaf node(s), matching the
		 * current filter condition. (works only for instances of XdevTree)
		 */
		if(isExpandFilteredLeafNodes())
		{
			if(this.getTree() instanceof XdevTree)
			{
				XdevTree xdevTree = (XdevTree)this.getTree();
				xdevTree.collapseAll();
				
				List<XdevTreeNode> searchNodes = searchNodesMatchingFilter(filterText);
				if(searchNodes != null)
				{
					for(XdevTreeNode xdevTreeNode : searchNodes)
					{
						XdevTreeNode parentOfSearchNode = (XdevTreeNode)xdevTreeNode.getParent();
						if(parentOfSearchNode != null)
						{
							TreePath pathToExpand = new TreePath(parentOfSearchNode.getPath());
							xdevTree.expandPath(pathToExpand);
						}
					}
				}
			}
		}
	}
	
	
	/**
	 * Returns a list of XdevTreeNode instances, which captions match the
	 * current filter condition. current filter condition
	 * 
	 * @param filterText
	 *            String containing the current filter text
	 * @return a list of XdevTreeNode instances
	 */
	@SuppressWarnings("unchecked")
	private List<XdevTreeNode> searchNodesMatchingFilter(String filterText)
	{
		// (19.10.2020 TB)FIXME: cast to xdevtreenode?
		List<XdevTreeNode> list = null;
		final XdevTree xdevTree = (XdevTree)this.getTree();
		final XdevTreeNode rootNode = xdevTree.getRoot();
		final Enumeration<TreeNode> e = rootNode.breadthFirstEnumeration();
		while(e.hasMoreElements())
		{
			final TreeNode node = e.nextElement();
			if(filterText == null || filterText.isEmpty() ? false : !this.getFilter()
					.isValueFiltered(filterText))
			{
				if(list == null)
				{
					list = new XdevList<>();
				}
				
				list.add((XdevTreeNode)node);
			}
		}
		return list;
	}
	
	
	/**
	 * Is tree currently expanded on filtered leaf nodes automatically?
	 * 
	 * @return {@code true} if expanded automatically
	 */
	public boolean isExpandFilteredLeafNodes()
	{
		return expandFilteredLeafNodes;
	}
	
	
	/**
	 * En- or disables the automatic expansion on filtered leaf nodes. Defaults
	 * to {@code false}.
	 * <p>
	 * May have an negative effect on the performance when enabled, as the
	 * expansion logic is triggered on every single key press.
	 * </p>
	 * to {@code false}
	 * 
	 * @param expandFilteredLeafNodes
	 *            if {@code true} expands on filtered leaf nodes
	 */
	public void setExpandFilteredLeafNodes(boolean expandFilteredLeafNodes)
	{
		this.expandFilteredLeafNodes = expandFilteredLeafNodes;
	}
	
	
	// ////////////////////////////////////////////////////////////
	// / Following properties are now shown on development time///
	// ///////////////////////////////////////////////////////////
	
	/**
	 * {@inheritDoc}
	 */
	@BeanProperty(category = DefaultBeanCategories.MISC)
	@Override
	public void setFont(Font paramFont)
	{
		super.setFont(paramFont);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@BeanProperty(category = DefaultBeanCategories.MISC)
	@Override
	public void setForeground(Color paramColor)
	{
		super.setForeground(paramColor);
	}
}
