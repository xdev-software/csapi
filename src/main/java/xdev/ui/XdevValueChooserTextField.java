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
import java.awt.Component;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import xdev.db.Operator;
import xdev.db.QueryInfo;
import xdev.db.sql.Condition;
import xdev.db.sql.SELECT;
import xdev.db.sql.WHERE;
import xdev.lang.NotNull;
import xdev.lang.Nullable;
import xdev.ui.valuechooser.DefaultVirtualTableValueChooser;
import xdev.ui.valuechooser.PopupVirtualTableValueChooser;
import xdev.ui.valuechooser.ValueChooser;
import xdev.ui.valuechooser.ValueChooserField;
import xdev.ui.valuechooser.ValueChooserTextField;
import xdev.ui.valuechooser.XdevValueChooser;
import xdev.util.ArrayUtils;
import xdev.util.ObjectUtils;
import xdev.util.logging.LoggerFactory;
import xdev.util.logging.XdevLogger;
import xdev.vt.KeyValues;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTable.VirtualTableRow;
import xdev.vt.VirtualTableColumn;

import com.jidesoft.grid.JideTable;


/**
 * The {@link XdevValueChooserTextField} is a {@link ValueChooserField}
 * implementation for {@link VirtualTableRow} selection. It provides the
 * functionality to chose a row from a {@link VirtualTable}.
 * <p>
 * {@link XdevValueChooserTextField} has a similar usage pattern as a
 * {@link XdevComboBox}, but differs in the way the selection happens. By double
 * clicking on the component or pressing the selection button on the right a
 * dialog with a table will be displayed, where the selection can be done. This
 * component allows to filter and select the available rows and is therefore
 * well suited for larger tables.
 * </p>
 * <p>
 * By implementing {@link MasterDetailComponent} instances of this class can be
 * used within a {@link Formular}.
 * </p>
 * <p>
 * This class uses a {@link DefaultVirtualTableValueChooser} as its
 * {@link ValueChooser} implementation.
 * </p>
 * 
 * @since CS 1.0
 * @author XDEV Software (RHHF)
 * @see ValueChooserField
 * 
 * 
 */
@BeanSettings(acceptChildren = false, useXdevCustomizer = true)
public class XdevValueChooserTextField extends ValueChooserTextField<VirtualTableRow> implements
		MasterDetailComponent<XdevValueChooserTextField>, XdevFocusCycleComponent
{
	
	/**
	 * Logger instance for this class.
	 */
	private static final XdevLogger										log								= LoggerFactory
																												.getLogger(XdevValueChooserTextField.class);
	
	/**
	 * serialVersionUID.
	 */
	private static final long											serialVersionUID				= 1L;
	/**
	 * {@link VirtualTable} to chose the row from.
	 */
	
	@NotNull
	private VirtualTable												virtualTable					= null;
	/**
	 * chosen {@link VirtualTableRow}; may be <code>null</code>.
	 */
	@Nullable
	private VirtualTableRow												virtualTableRow					= null;
	
	/**
	 * used to save the value state of the component.
	 */
	@Nullable
	private VirtualTableRow												savedState						= null;
	
	/**
	 * a null object for the {@link #formatPattern} member.
	 */
	@NotNull
	public static final String											NO_FORMAT						= "";
	
	/**
	 * defines the pattern to display the chosen {@link VirtualTableRow}.
	 */
	@NotNull
	private String														formatPattern					= NO_FORMAT;
	
	/**
	 * the user ide-defined auto resize mode.
	 */
	private Integer														clientProperty_AutoResizeMode	= null;
	
	/**
	 * tabIndex is used to store the index for {@link XdevFocusCycleComponent}
	 * functionality.
	 */
	private int															tabIndex						= -1;
	
	/**
	 * provides {@link FormularComponent} support.
	 * 
	 */
	private final FormularComponentSupport<XdevValueChooserTextField>	support							= new FormularComponentSupport<XdevValueChooserTextField>(
																												this);
	
	/**
	 * detail handler instance.
	 */
	protected DetailHandler												detailHandler;
	
	
	/**
	 * Creates a new {@link XdevValueChooserTextField}.
	 * 
	 * @see PopupVirtualTableValueChooser#PopupVirtualTableValueChooser()
	 * 
	 */
	public XdevValueChooserTextField()
	{
		// #12550
		this.setChooser(new PopupVirtualTableValueChooser());
	}
	
	
	/**
	 * Creates a new {@link XdevValueChooserTextField} using the provided
	 * {@link VirtualTable} as value pool.
	 * 
	 * @param virtualTable
	 *            {@link VirtualTable} containing the valid values for this
	 *            instance.
	 */
	public XdevValueChooserTextField(final @NotNull VirtualTable virtualTable)
	{
		// #12550
		this.setChooser(new PopupVirtualTableValueChooser());
		this.setVirtualTable(virtualTable,false);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setVirtualTable(final @NotNull VirtualTable virtualTable)
	{
		this.setVirtualTable(virtualTable,false);
	}
	
	
	/**
	 * Sets the {@link VirtualTable} containing the valid values for this
	 * instance.
	 * 
	 * @param virtualTable
	 *            {@link VirtualTable} containing the valid values for this
	 *            instance.
	 * @param queryData
	 *            if set to {@code true}, the default query will be performed on
	 *            the specified {@code virtualTable}.
	 */
	public void setVirtualTable(final @NotNull VirtualTable virtualTable, final boolean queryData)
	{
		if(virtualTable == null)
		{
			throw new IllegalArgumentException("virtualTable must not be null");
		}
		
		// use clone to avoid unwanted dependencies
		this.virtualTable = virtualTable.clone(true);
		if(queryData)
		{
			try
			{
				this.virtualTable.queryAndFill();
			}
			catch(Exception e)
			{
				throw new IllegalStateException("query on virtualTable could not be performed",e);
			}
		}
		
		this.reset();
		
		XdevValueChooser<?> chooser = (XdevValueChooser<?>)this.getChooser();
		
		if(chooser != null)
		{
			chooser.setVirtualTable(this.virtualTable);
			
			// #12881
			if(clientProperty_AutoResizeMode != null)
			{
				chooser.getTable().setAutoResizeMode(clientProperty_AutoResizeMode);
			}
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void displaySelectedValue()
	{
		this.setVirtualTableRow(getChooser().getSelectValue());
	}
	
	
	/**
	 * Sets the {@link VirtualTableRow} for this instance.
	 * 
	 * @param virtualTableRow
	 *            the {@link VirtualTableRow} to be set
	 */
	public void setVirtualTableRow(final VirtualTableRow virtualTableRow)
	{
		this.virtualTableRow = virtualTableRow;
		String text = this.getTextField().getText();
		
		if(this.virtualTableRow != null)
		{
			
			if(NO_FORMAT.equals(this.formatPattern))
			{
				text = this.virtualTableRow.getFormattedValue(this.virtualTable
						.getColumnIndex(this.virtualTable.getPrimaryColumn()));
			}
			else
			{
				text = this.virtualTableRow.format(this.formatPattern);
			}
			
		}
		else
		{
			if(text == null || text.trim().equals(""))
			{
				text = this.getNoValueSelectedText();
			}
		}
		
		this.getTextField().setText(text);
		
		// can�t avoid casting without interface API change
		if(getChooser() instanceof XdevValueChooser)
		{
			XdevValueChooser<VirtualTableRow> chooser = (XdevValueChooser<VirtualTableRow>)getChooser();
			chooser.setSelectValue(virtualTableRow);
		}
	}
	
	
	/**
	 * Convenience method to keep the set/getSelectedVirtualTableRow designation
	 * standard.
	 * 
	 * @see #setVirtualTableRow(VirtualTableRow)
	 * 
	 * @param virtualTableRow
	 *            the {@link VirtualTableRow} to be set
	 */
	public void setSelectedVirtualTableRow(final VirtualTableRow virtualTableRow)
	{
		this.setVirtualTableRow(virtualTableRow);
	}
	
	
	/**
	 * Sets a format pattern that defines how the chosen {@link VirtualTableRow}
	 * is displayed by the {@link XdevValueChooserTextField}.
	 * 
	 * *
	 * <p>
	 * <strong>Example:</strong><br>
	 * 
	 * The VirtualTable has the columns "name" and "color". The current row has
	 * (name -> Peter, color -> blue). <br>
	 * formatPattern = <code>{$name} loves the color {$color}.</code><br>
	 * result = "<i>Peter</i> loves the color <i>blue</i>".
	 * </p>
	 * 
	 * @param formatPattern
	 *            {@link String} format pattern that defines how the chosen
	 *            {@link VirtualTableRow} is displayed by the
	 *            {@link XdevValueChooserTextField}.
	 * 
	 * 
	 */
	public void setFormat(final @Nullable String formatPattern)
	{
		if(formatPattern == null)
		{
			this.formatPattern = NO_FORMAT;
		}
		else
		{
			// #11974 interpret a single column name of the virtual table as a
			// placeholder
			this.formatPattern = makePlaceholderForColumnName(formatPattern);
		}
	}
	
	
	/**
	 * If formatPattern is a column name of the virtual table, this method
	 * returns an placeholder for it. Otherwise the input parameter will be
	 * returned unchanged.
	 * 
	 * @param formatPattern
	 *            a format to be used for display the selected value in the text
	 *            field.
	 * @return a placeholder either static text or a column placeholder
	 */
	private String makePlaceholderForColumnName(final String formatPattern)
	{
		for(int i = 0; i < virtualTable.getColumnCount(); i++)
		{
			String colName = virtualTable.getColumnName(i);
			if(formatPattern.equals(colName))
			{
				return "{$" + formatPattern + "}";
			}
		}
		return formatPattern;
	}
	
	
	/**
	 * Returns the current formatPattern.
	 * 
	 * @return the current formatPattern. If there is no formatPattern set
	 *         {@link #NO_FORMAT}.
	 * 
	 * @see #setFormat(String)
	 */
	@NotNull
	public String getFormat()
	{
		return formatPattern;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Component getComponent()
	{
		return this;
	}
	
	
	/**
	 * Returns the text of the wrapped TextField.
	 * 
	 * @return a String representing the text.
	 */
	public String getText()
	{
		return this.getTextField().getText();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getFormularName()
	{
		return this.support.getFormularName();
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.1
	 */
	@Override
	public void setDataField(String dataField)
	{
		this.support.setDataField(dataField);
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.1
	 */
	@Override
	public String getDataField()
	{
		return this.support.getDataField();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Deprecated
	public void setFormularValue(VirtualTable vt, int columnIndex, Object value)
	{
		this.support.setFormularValue(vt,columnIndex,value);
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.1
	 */
	@Override
	public void setFormularValue(VirtualTable vt, Map<String, Object> record)
	{
		if(!support.hasDataField())
		{
			return;
		}
		
		reset();
		setValue(vt,support.getValuesForDataFields(record));
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.2
	 */
	@Override
	public void setMasterValue(VirtualTable vt, Map<String, Object> record)
	{
		setValue(vt,record);
	}
	
	
	private void setValue(VirtualTable vt, Map<String, Object> value)
	{
		VirtualTable valueChooserVT = getVirtualTable();
		selectFormularValue(vt,valueChooserVT,value);
		if(getSelectedVirtualTableRow() == null && detailHandler != null)
		{
			detailHandler.checkDetailView(value);
			selectFormularValue(vt,valueChooserVT,value);
		}
	}
	
	
	/**
	 * Sets the current {@link VirtualTableRow} for a specified value.
	 */
	private void selectFormularValue(VirtualTable formularVT, VirtualTable valueChooserVT,
			Map<String, Object> valueMap)
	{
		if(virtualTable == null)
		{
			return;
		}
		
		VirtualTableColumn<?>[] pkColumns = virtualTable.getPrimaryKeyColumns();
		int pkColumnCount = pkColumns.length;
		if(pkColumnCount == 0)
		{
			throw new MasterDetailException(formularVT,valueChooserVT,"no primary key defined in '"
					+ formularVT.getName() + "'");
		}
		
		if(pkColumnCount != valueMap.size())
		{
			throw new IllegalArgumentException("value count " + valueMap.size() + " <> "
					+ pkColumnCount + " key column count");
		}
		
		Object[] values = valueMap.values().toArray();
		Map<String, Object> foreignMap = new HashMap<String, Object>();
		for(int i = 0; i < values.length; i++)
		{
			foreignMap.put(pkColumns[i].getName(),values[i]);
		}
		
		KeyValues key = new KeyValues(virtualTable,foreignMap);
		VirtualTableRow row = virtualTable.getRow(key);
		if(row != null)
		{
			setVirtualTableRow(row);
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getFormularValue()
	{
		VirtualTableRow row = getSelectedVirtualTableRow();
		if(row != null)
		{
			KeyValues key = new KeyValues(row);
			if(key.getColumnCount() == 1)
			{
				return key.getValue(key.columnNames().iterator().next());
			}
			
			return key;
		}
		
		return null;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void saveState()
	{
		this.savedState = this.virtualTableRow;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void restoreState()
	{
		this.virtualTableRow = this.savedState;
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.1
	 */
	@Override
	public boolean hasStateChanged()
	{
		return !ObjectUtils.equals(savedState,virtualTableRow);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isMultiSelect()
	{
		return false;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean verify()
	{
		return support.verify();
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.1
	 */
	@Override
	public void addValidator(Validator validator)
	{
		support.addValidator(validator);
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.1
	 */
	@Override
	public void removeValidator(Validator validator)
	{
		support.removeValidator(validator);
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.1
	 */
	@Override
	public Validator[] getValidators()
	{
		return support.getValidators();
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.1
	 */
	@Override
	public void validateState() throws ValidationException
	{
		support.validateState();
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.1
	 */
	@Override
	public void validateState(Validation validation) throws ValidationException
	{
		support.validateState(validation);
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.1
	 */
	@Override
	public void setFilterOperator(Operator filterOperator)
	{
		support.setFilterOperator(filterOperator);
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.1
	 */
	@Override
	public Operator getFilterOperator()
	{
		return support.getFilterOperator();
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.2
	 */
	@Override
	public void setReadOnly(boolean readOnly)
	{
		support.setReadOnly(readOnly);
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.2
	 */
	@Override
	public boolean isReadOnly()
	{
		return support.isReadOnly();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public VirtualTable getVirtualTable()
	{
		return this.virtualTable;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public VirtualTableRow getSelectedVirtualTableRow()
	{
		return this.virtualTableRow;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void refresh()
	{
		try
		{
			this.virtualTable.reload();
		}
		catch(Exception e)
		{
			log.error(e);
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateModel(final Condition condition, final Object... params)
	{
		/*
		 * SuppressWarnings is OK because the super class does not specify a
		 * type
		 */
		if(this.virtualTable != null)
		{
			Object[] newParams = null;
			
			QueryInfo query = this.virtualTable.getLastQuery();
			QueryInfo queryClone = query != null ? query.clone() : null;
			SELECT select;
			if(query != null)
			{
				select = query.getSelect();
				
				Object[] lastParams = query.getParameters();
				if(lastParams.length > 0)
				{
					newParams = ArrayUtils.concat(Object.class,lastParams,params);
				}
			}
			else
			{
				select = this.virtualTable.getSelect();
			}
			
			if(condition != null)
			{
				WHERE where = select.getWhere();
				if(where != null && !where.isEmpty())
				{
					where = new WHERE(where.encloseWithPars().and(condition));
				}
				else
				{
					where = new WHERE(condition);
				}
				select.WHERE(where);
			}
			
			try
			{
				if(newParams != null)
				{
					this.virtualTable.queryAndFill(select,newParams);
				}
				else
				{
					this.virtualTable.queryAndFill(select,params);
				}
			}
			catch(Exception e)
			{
				log.error(e);
			}
			this.reset();
			this.virtualTable.setLastQuery(queryClone);
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clearModel()
	{
		this.virtualTable.clear();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addValueChangeListener(final ValueChangeListener l)
	{
		getTextField().getDocument().addDocumentListener(new DocumentListener()
		{
			@Override
			public void insertUpdate(DocumentEvent e)
			{
				l.valueChanged(e);
			}
			
			
			@Override
			public void removeUpdate(DocumentEvent e)
			{
				/*
				 * do nothing insertUpdate handles everything
				 */
			}
			
			
			@Override
			public void changedUpdate(DocumentEvent e)
			{
				/*
				 * do nothing insertUpdate handles everything
				 */
			}
		});
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDetailHandler(DetailHandler detailHandler)
	{
		this.detailHandler = detailHandler;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setNoValueSelectedText(String noValueSelectedText)
	{
		super.setNoValueSelectedText(noValueSelectedText);
		
		if(this.virtualTableRow == null)
		{
			this.getTextField().setText(noValueSelectedText);
		}
	}
	
	
	/**
	 * Configures the resize mode of this choosers table.
	 * 
	 * @param mode
	 *            a valid mode as defined {@link JideTable}s AUTO_RESIZE
	 *            constants
	 */
	public void setAutoResizeModeForTable(int mode)
	{
		ValueChooser<?> chooser = this.getChooser();
		JTable table;
		// see #12881
		if(chooser instanceof XdevValueChooser<?>
				&& (table = ((XdevValueChooser<?>)chooser).getTable()) != null)
		{
			// initialized with vt
			table.setAutoResizeMode(mode);
		}
		else
		{
			// cache mode
			this.clientProperty_AutoResizeMode = mode;
		}
	}
	
	
	/**
	 * Returns the auto resize mode of the table. The default mode is
	 * AUTO_RESIZE_SUBSEQUENT_COLUMNS.
	 * 
	 * @return {@code int} the auto resize mode
	 */
	public int getAutoResizeModeForTable()
	{
		ValueChooser<?> chooser = this.getChooser();
		JTable table;

		if(chooser instanceof XdevValueChooser<?>
				&& (table = ((XdevValueChooser<?>)chooser).getTable()) != null)
		{
			return table.getAutoResizeMode();
		}
		
		if(this.clientProperty_AutoResizeMode != null)
		{
			return this.clientProperty_AutoResizeMode;
		}
		
		return JTable.AUTO_RESIZE_OFF;
	}
	
	
	/**
	 * Sets the size of the value chooser components.
	 * 
	 * @param size
	 *            the new size
	 */
	public void setChooserSize(Dimension size)
	{
		setChooserSize(size.width,size.height);
	}
	
	
	/**
	 * Sets the size of the value chooser components.
	 * 
	 * @param width
	 *            the width in pixel
	 * @param height
	 *            the height in pixel
	 */
	public void setChooserSize(int width, int height)
	{
		((XdevValueChooser<?>)this.getChooser()).setChooserSize(width,height);
	}
	
	
	/**
	 * <p>
	 * Gets invoked, when clear button gets clicked.
	 * </p>
	 * <p>
	 * Clears the <code>selectedValue/{@link VirtualTableRow}</code> and sets
	 * the {@link XdevValueChooserTextField#getNoValueSelectedText()} as
	 * <code>TextField</code> text.
	 * </p>
	 */
	@Override
	public void clearValue()
	{
		virtualTableRow = null;
		((XdevValueChooser<?>)this.getChooser()).setSelectValue(null);
		getTextComponent().setText(this.getNoValueSelectedText());
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
		if(this.tabIndex != tabIndex)
		{
			int oldValue = this.tabIndex;
			this.tabIndex = tabIndex;
			
			// fire event
			firePropertyChange(TAB_INDEX_PROPERTY,oldValue,tabIndex);
		}
	}
	
	
	// ////////////////////////////////////////////////////////////
	// / Following properties are hidden on development time ///
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
	public void setBackgroundType(int backgroundType)
	{
		super.setBackgroundType(backgroundType);
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
	public void setGradientAlign(int gradientAlign)
	{
		super.setGradientAlign(gradientAlign);
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
	public void setGradientColor1(Color gradientColor1)
	{
		super.setGradientColor1(gradientColor1);
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
	public void setGradientColor2(Color gradientColor2)
	{
		super.setGradientColor2(gradientColor2);
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
	public void setTexturePath(String texturePath)
	{
		super.setTexturePath(texturePath);
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
	public void setTexture(XdevImage image)
	{
		super.setTexture(image);
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
	public void setTextureOption(int textureOption)
	{
		super.setTextureOption(textureOption);
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
	
	
	/**
	 * {@inheritDoc}
	 * <p>
	 * <b>This property does alter the layout on JComponent level so nothing is
	 * going to be updated on this component level.</b>
	 * </p>
	 */
	@BeanProperty(category = DefaultBeanCategories.MISC)
	@Override
	public void setAlpha(int alpha)
	{
		super.setAlpha(alpha);
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
	public void setAlphaCondition(int alphaCondition)
	{
		super.setAlphaCondition(alphaCondition);
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
	public void setAlphaTolerance(int alphaTolerance)
	{
		super.setAlphaTolerance(alphaTolerance);
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
	public void setAlphaColors(Color... alphaColors)
	{
		super.setAlphaColors(alphaColors);
	}
}
