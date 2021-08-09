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

import javax.swing.ComboBoxModel;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

import xdev.ui.persistence.Persistable;
import xdev.ui.quickfilter.QuickFilterSearchMode;
import xdev.ui.quickfilter.QuickFilterSearchOption;

import com.jidesoft.combobox.QuickComboBoxFilterField;
import com.jidesoft.swing.JidePopupMenu;


/**
 * {@code XdevQuickComboBoxFilterField} can be used to dynamically filter the
 * rows of a table.
 * <p>
 * It takes any {@link ComboBoxModel} as input.
 * </p>
 * 
 * @author XDEV Software
 * @see QuickComboBoxFilterField
 */
public class XdevQuickComboBoxFilterField extends QuickComboBoxFilterField implements
		XdevFocusCycleComponent, Persistable, TextComponentOwner
{
	/**
	 * serialVersionUID.
	 */
	private static final long	serialVersionUID			= 1L;
	
	/**
	 * Should the gui state be persisted? Defaults to {@code true}.
	 */
	private boolean				persistenceEnabled			= true;
	
	/**
	 * Should the selection of the filtered combobox be automatically filtered
	 * when filter is changed?
	 */
	private boolean				autoUpdateComboboxSelection	= false;
	
	/**
	 * Flag for disabling the context menu.
	 */
	private boolean				contextMenuDisabled			= false;
	
	
	/**
	 * @see QuickComboBoxFilterField#QuickComboBoxFilterField()
	 */
	public XdevQuickComboBoxFilterField()
	{
		super();
		
		initDefaults();
	}
	
	
	/**
	 * @param ComboBoxModel
	 *            the combobox model
	 * @see QuickComboBoxFilterField#QuickComboBoxFilterField(ComboBoxModel)
	 */
	public XdevQuickComboBoxFilterField(ComboBoxModel ComboBoxModel)
	{
		super(ComboBoxModel);
		
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
	 * Connects the filter field with a combobox, meaning it always uses the
	 * combobox's model.
	 * 
	 * @param combobox
	 *            the combobox to connect with.
	 */
	public void setFilterFor(final JComboBox combobox)
	{
		setComboBox(combobox);
		setComboBoxModel(combobox.getModel());
		combobox.setModel(getDisplayComboBoxModel());
		
		combobox.setSelectedIndex(-1);
		updateComboboxSelection();
	}
	
	private PropertyChangeListener	comboBoxModelChangeListener;
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setComboBox(JComboBox newComboBox)
	{
		JComboBox oldComboBox = getComboBox();
		if(oldComboBox != newComboBox)
		{
			if(comboBoxModelChangeListener == null)
			{
				comboBoxModelChangeListener = new PropertyChangeListener()
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
								
								JComboBox comboBox = (JComboBox)evt.getSource();
								if(comboBox.getModel() != getDisplayComboBoxModel())
								{
									setComboBoxModel(comboBox.getModel());
									comboBox.setModel(getDisplayComboBoxModel());
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
			
			if(oldComboBox != null)
			{
				oldComboBox.removePropertyChangeListener("model",comboBoxModelChangeListener);
			}
			
			super.setComboBox(newComboBox);
			
			newComboBox.addPropertyChangeListener("model",comboBoxModelChangeListener);
		}
	}
	
	
	/**
	 * Updates the selection of the filtered combobox, if the filter gets.
	 * changes
	 */
	private void updateComboboxSelection()
	{
		this.addPropertyChangeListener(new PropertyChangeListener()
		{
			@Override
			public void propertyChange(PropertyChangeEvent evt)
			{
				if(autoUpdateComboboxSelection)
				{
					JComboBox filteredComboBox = XdevQuickComboBoxFilterField.this.getComboBox();
					ComboBoxModel displayModel = XdevQuickComboBoxFilterField.this
							.getDisplayComboBoxModel();
					if(filteredComboBox != null && displayModel.getSize() > 0)
					{
						filteredComboBox.setSelectedIndex(0);
					}
					else
					{
						filteredComboBox.setSelectedIndex(-1);
					}
				}
			}
		});
	}
	
	
	/**
	 * Updates the {@code XdevQuickFilterPane} instance.
	 * <p>
	 * Should be called, if the default constructor was used to create the
	 * instance.
	 * </p>
	 * 
	 * @param comboBoxModel
	 *            the model object of the table to filter
	 */
	public void setModel(ComboBoxModel comboBoxModel)
	{
		this.setComboBoxModel(comboBoxModel);
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
	 * @return {@code true} if automatic selection of the filtered combobox is
	 *         enabled.
	 */
	public boolean isAutoUpdateComboboxSelection()
	{
		return autoUpdateComboboxSelection;
	}
	
	
	/**
	 * Enables / Disables the automatic selection change of the filtered
	 * combobox.
	 * 
	 * @param autoUpdateComboboxSelection
	 *            if {@code true} automatic selection is enabled.
	 */
	public void setAutoUpdateComboboxSelection(boolean autoUpdateComboboxSelection)
	{
		this.autoUpdateComboboxSelection = autoUpdateComboboxSelection;
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
	 * Removes a {@link DocumentListener} on the contained {@link JTextField}.
	 * 
	 * @param documentListener
	 *            a {@link DocumentListener} to remove
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
	 * Removes a {@link CaretListener} on the contained {@link JTextField}.
	 * 
	 * @param caretListener
	 *            a {@link CaretListener} to remove
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
