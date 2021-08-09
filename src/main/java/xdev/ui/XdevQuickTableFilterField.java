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
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import javax.swing.text.JTextComponent;

import xdev.ui.persistence.Persistable;
import xdev.ui.quickfilter.QuickFilterSearchMode;
import xdev.ui.quickfilter.QuickFilterSearchOption;
import xdev.ui.quickfilter.XdevNumberConverter;
import xdev.ui.table.ExtendedTableSupport;

import com.jidesoft.converter.ObjectConverter;
import com.jidesoft.converter.ObjectConverterManager;
import com.jidesoft.grid.QuickTableFilterField;
import com.jidesoft.swing.JidePopupMenu;


/**
 * {@code XdevQuickTableFilterField} can be used to dynamically filter the rows
 * of a table.
 * <p>
 * It takes any {@link TableModel} as input.
 * </p>
 * 
 * @author XDEV Software
 * @see XdevQuickTableFilterField
 */
public class XdevQuickTableFilterField extends QuickTableFilterField implements
		XdevFocusCycleComponent, Persistable, TextComponentOwner
{
	/**
	 * column separator for gui persistence.
	 */
	private static final String	GUI_PERSISTENCE_COLUMN_SEP	= ",";
	
	/**
	 * empty identifier for gui persistence.
	 */
	private static final String	GUI_PERSISTENCE_EMPTY		= "empty";
	
	/**
	 * null identifier for gui persistence.
	 */
	private static final String	GUI_PERSISTENCE_NULL		= "null";
	
	/**
	 * serialVersionUID.
	 */
	private static final long	serialVersionUID			= 1L;
	
	/**
	 * tabIndex is used to store the index for {@link XdevFocusCycleComponent}
	 * functionality.
	 */
	private int					tabIndex					= -1;
	
	/**
	 * Should the gui state be persisted? Defaults to {@code true}.
	 */
	private boolean				persistenceEnabled			= true;
	
	/**
	 * Flag for disabling the context menu.
	 */
	private boolean				contextMenuDisabled			= false;
	
	
	/**
	 * @see QuickTableFilterField#QuickTableFilterField()
	 */
	public XdevQuickTableFilterField()
	{
		super();
		
		initDefaults();
	}
	
	
	/**
	 * @param tableModel
	 *            the TableModel
	 * @param columnIndices
	 *            the columns that you want to give user an option in the popup
	 *            menu to limit the search.
	 * @param displayNames
	 *            the text appears on the popup menu.
	 * @see QuickTableFilterField#QuickTableFilterField(TableModel, int[],
	 *      String[])
	 */
	public XdevQuickTableFilterField(TableModel tableModel, int[] columnIndices,
			String[] displayNames)
	{
		super(tableModel,columnIndices,displayNames);
		
		initDefaults();
	}
	
	
	/**
	 * @param tableModel
	 *            the TableModel
	 * @param columnIndices
	 *            the columns that you want to give user an option in the popup
	 *            menu to limit the search.
	 * @see QuickTableFilterField#QuickTableFilterField(TableModel, int[])
	 */
	public XdevQuickTableFilterField(TableModel tableModel, int[] columnIndices)
	{
		super(tableModel,columnIndices);
		
		initDefaults();
	}
	
	
	/**
	 * @param tableModel
	 *            the TableModel
	 * @see QuickTableFilterField#QuickTableFilterField(TableModel)
	 */
	public XdevQuickTableFilterField(TableModel tableModel)
	{
		super(tableModel);
		
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
		
		initFilterConverter();
	}
	
	
	/**
	 * Initializes considering of default number rendering (no decimal points)
	 */
	protected void initFilterConverter()
	{
		ObjectConverterManager.registerConverter(Integer.class,new XdevNumberConverter());
		ObjectConverterManager.registerConverter(Long.class,new XdevNumberConverter());
		ObjectConverterManager.registerConverter(Short.class,new XdevNumberConverter());
		ObjectConverterManager.registerConverter(Float.class,new XdevNumberConverter());
	}
	
	
	/**
	 * Connects the filter field with a table, meaning it always uses the
	 * table's model.
	 * 
	 * @param table
	 *            the table to connect with.
	 * @param columns
	 *            the filtered columns, leave empty to use all columns of the
	 *            model
	 */
	public void setFilterFor(final JTable table, String... columns)
	{
		setTable(table);
		if(columns != null && columns.length > 0)
		{
			setModel(table.getModel(),ExtendedTableSupport.getColumnIndices(table,columns));
		}
		else
		{
			setModel(table.getModel());
		}
		table.setModel(getDisplayTableModel());
	}
	
	private PropertyChangeListener	tableModelChangeListener;
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTable(JTable newTable)
	{
		JTable oldTable = getTable();
		if(oldTable != newTable)
		{
			if(tableModelChangeListener == null)
			{
				tableModelChangeListener = new PropertyChangeListener()
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
								
								JTable table = (JTable)evt.getSource();
								if(table.getModel() != getDisplayTableModel())
								{
									setTableModel(table.getModel());
									table.setModel(getDisplayTableModel());
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
				oldTable.removePropertyChangeListener("model",tableModelChangeListener);
			}
			
			super.setTable(newTable);
			
			newTable.addPropertyChangeListener("model",tableModelChangeListener);
		}
	}
	
	
	/**
	 * Updates the {@code XdevQuickTableFilterField} instance.
	 * <p>
	 * Should be called, if the default constructor was used to create the
	 * instance.
	 * </p>
	 * 
	 * @param tableModel
	 *            the model object of the table to filter
	 */
	public void setModel(TableModel tableModel)
	{
		this.setModel(tableModel,(int[])null);
	}
	
	
	/**
	 * Updates the {@code XdevQuickTableFilterField} instance.
	 * <p>
	 * Should be called, if the default constructor was used to create the
	 * instance.
	 * </p>
	 * 
	 * @param tableModel
	 *            the model object of the table to filter
	 * @param columnIndices
	 *            an array of column indexes. for each specified column a
	 *            separate filter list will be created.
	 */
	public void setModel(TableModel tableModel, int[] columnIndices)
	{
		this.setModel(tableModel,columnIndices,null);
	}
	
	
	/**
	 * Updates the {@code XdevQuickTableFilterField} instance.
	 * 
	 * @param tableModel
	 *            the model object of the table to filter
	 * @param columnNames
	 *            an array of column names
	 * @param displayNames
	 *            an array of column display names
	 * @throws IllegalArgumentException
	 *             if column name does not exist in filtered table
	 * @see #setModel(TableModel, int[])
	 */
	public void setModel(TableModel tableModel, String[] columnNames, String[] displayNames)
			throws IllegalArgumentException
	{
		int[] columnIndices = ExtendedTableSupport.getColumnIndices(tableModel,columnNames);
		this.setModel(tableModel,columnIndices,displayNames);
	}
	
	
	/**
	 * Updates the {@code XdevQuickTableFilterField} instance.
	 * 
	 * @param tableModel
	 *            the model object of the table to filter
	 * @param columnNames
	 *            an array of column names
	 * @throws IllegalArgumentException
	 *             if column name does not exist in filtered table
	 * @see #setModel(TableModel, int[])
	 */
	public void setModel(TableModel tableModel, String[] columnNames)
			throws IllegalArgumentException
	{
		int[] columnIndices = ExtendedTableSupport.getColumnIndices(tableModel,columnNames);
		this.setModel(tableModel,columnIndices,columnNames);
	}
	
	
	/**
	 * Updates the {@code XdevQuickTableFilterField} instance.
	 * <p>
	 * Should be called, if the default constructor was used to create the
	 * instance.
	 * </p>
	 * 
	 * @param tableModel
	 *            the model object of the table to filter
	 * @param columnIndices
	 *            an array of column indexes. for each specified column a
	 *            separate filter list will be created.
	 * @param displayNames
	 *            an array of String for the titles of the column filter lists
	 */
	public void setModel(TableModel tableModel, int[] columnIndices, String[] displayNames)
	{
		this.setTableModel(tableModel);
		this.setColumnIndices(columnIndices);
		this.setDisplayNames(displayNames);
	}
	
	
	/**
	 * Overridden to do appropriate ObjectConversion when dealing with localized
	 * types.
	 * 
	 * @param element
	 *            an value from the table
	 * @return a String representation of the passed in value
	 */
	@Override
	protected String convertElementToString(Object element)
	{
		// see the following link for an explanation, why this method was
		// overridden.
		// http://www.jidesoft.com/forum/viewtopic.php?f=11&t=11629
		
		if(element == null)
		{
			return super.convertElementToString(element);
		}
		
		ObjectConverter objectConverter = ObjectConverterManager.getConverter(element.getClass());
		
		if(objectConverter == null)
		{
			return super.convertElementToString(element);
		}
		
		return objectConverter.toString(element,null);
	}
	
	
	//
	// XdevFocusCycleComponent impl - start
	//
	
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
			// table specific
			String searchColumnString = persistentValues[index++];
			if(searchColumnString.equals(GUI_PERSISTENCE_NULL))
			{
				this.setSearchingColumnIndices(null);
			}
			else if(searchColumnString.equals(GUI_PERSISTENCE_EMPTY))
			{
				this.setSearchingColumnIndices(new int[]{});
			}
			else
			{
				String[] searchColumnIndices = searchColumnString.split(GUI_PERSISTENCE_COLUMN_SEP);
				int[] indices = new int[searchColumnIndices.length];
				for(int i = 0; i < searchColumnIndices.length; i++)
				{
					indices[i] = Integer.parseInt(searchColumnIndices[i]);
				}
				this.setSearchingColumnIndices(indices);
			}
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
		// table specific
		persistentState.append(Persistable.VALUE_SEPARATOR);
		int[] searchColumnIndices = this.getSearchingColumnIndices();
		if(searchColumnIndices == null)
		{
			persistentState.append(GUI_PERSISTENCE_NULL);
		}
		else if(searchColumnIndices.length == 0)
		{
			persistentState.append(GUI_PERSISTENCE_EMPTY);
		}
		else
		{
			for(int i = 0; i < searchColumnIndices.length; i++)
			{
				if(i != 0)
				{
					persistentState.append(GUI_PERSISTENCE_COLUMN_SEP);
				}
				persistentState.append(searchColumnIndices[i]);
			}
		}
		
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
