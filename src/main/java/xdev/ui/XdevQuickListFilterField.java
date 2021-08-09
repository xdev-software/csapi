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

import javax.swing.Icon;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

import xdev.ui.persistence.Persistable;
import xdev.ui.quickfilter.QuickFilterSearchMode;
import xdev.ui.quickfilter.QuickFilterSearchOption;

import com.jidesoft.list.QuickListFilterField;
import com.jidesoft.swing.JidePopupMenu;


/**
 * {@code XdevQuickListFilterField} can be used to dynamically filter the rows
 * of a list.
 * <p>
 * It takes any {@link ListModel} as input.
 * </p>
 * 
 * @author XDEV Software
 * @see XdevQuickListFilterField
 */
public class XdevQuickListFilterField extends QuickListFilterField implements
		XdevFocusCycleComponent, Persistable, TextComponentOwner
{
	/**
	 * Subcomponents name.
	 */
	public static final String	FILTER_TEXT_FIELD_NAME		= "FilterTextField";
	/**
	 * Subcomponents name.
	 */
	public static final String	SHOW_POPUP_LABEL_NAME		= "ShowPopupLabel";
	/**
	 * Subcomponents name.
	 */
	public static final String	CLEAR_FILTER_BUTTON_NAME	= "ClearFilterButton";
	
	/**
	 * serialVersionUID.
	 */
	private static final long	serialVersionUID			= 1L;
	
	/**
	 * Should the gui state be persisted? Defaults to {@code true}.
	 */
	private boolean				persistenceEnabled			= true;
	
	/**
	 * Flag for disabling the context menu.
	 */
	private boolean				contextMenuDisabled			= false;
	
	
	/**
	 * @see QuickListFilterField#QuickListFilterField()
	 */
	public XdevQuickListFilterField()
	{
		super();
		
		initDefaults();
	}
	
	
	/**
	 * 
	 * @param listModel
	 *            the ListModel
	 * @see QuickListFilterField#QuickListFilterField(ListModel)
	 */
	public XdevQuickListFilterField(ListModel listModel)
	{
		super(listModel);
		
		initDefaults();
	}
	
	
	/**
	 * Initializes the default values for search mode (wildcard) and search
	 * option (match anywhere).
	 */
	private void initDefaults()
	{
		this.getButton().setName(CLEAR_FILTER_BUTTON_NAME);
		this.getLabel().setName(SHOW_POPUP_LABEL_NAME);
		this.getTextField().setName(FILTER_TEXT_FIELD_NAME);
		
		setSearchMode(QuickFilterSearchMode.WILDCARD);
		setSearchOption(QuickFilterSearchOption.MATCH_ANYWHERE);
	}
	
	
	/**
	 * Connects the filter field with a list, meaning it always uses the list's
	 * model.
	 * 
	 * @param list
	 *            the list to connect with.
	 */
	public void setFilterFor(final JList list)
	{
		setList(list);
		setListModel(list.getModel());
		list.setModel(getDisplayListModel());
	}
	
	private PropertyChangeListener	listBoxModelChangeListener;
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setList(JList newList)
	{
		JList oldListBox = getList();
		if(oldListBox != newList)
		{
			if(listBoxModelChangeListener == null)
			{
				listBoxModelChangeListener = new PropertyChangeListener()
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
								
								JList list = (JList)evt.getSource();
								if(list.getModel() != getDisplayListModel())
								{
									setListModel(list.getModel());
									list.setModel(getDisplayListModel());
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
			
			if(oldListBox != null)
			{
				oldListBox.removePropertyChangeListener("model",listBoxModelChangeListener);
			}
			
			super.setList(newList);
			
			newList.addPropertyChangeListener("model",listBoxModelChangeListener);
		}
	}
	
	
	/**
	 * Updates the {@code XdevQuickListFilterField} instance.
	 * <p>
	 * Should be called, if the default constructor was used to create the
	 * instance.
	 * </p>
	 * 
	 * @param listModel
	 *            the model object of the list to filter
	 */
	public void setModel(ListModel listModel)
	{
		this.setListModel(listModel);
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
