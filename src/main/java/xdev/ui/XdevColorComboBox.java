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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import xdev.db.Operator;
import xdev.ui.util.ColorParserUtils;
import xdev.util.ObjectUtils;
import xdev.vt.DataFlavor;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTable.VirtualTableRow;
import xdev.vt.VirtualTableColumn;

import com.jidesoft.combobox.ColorExComboBox;


/**
 * XdevColorComboBox is a <code>combobox</code> which can be used to choose a
 * {@link Color} from a provided RGB-Color-Dialog.
 * <p>
 * The selected <code>color</code> can be accessed via
 * {@link XdevColorComboBox#getSelectedColor()}.
 * </p>
 * 
 * @author XDEV Software JWill
 * 
 * @since 4.0
 * 
 */
public class XdevColorComboBox extends ColorExComboBox implements
		ColorFormularComponent<XdevColorComboBox>, XdevFocusCycleComponent
{
	/**
	 * Creates a new ColorComboBox using ColorChooserPanel with 40 colors.
	 */
	public XdevColorComboBox()
	{
		super();
	}
	
	
	/**
	 * Creates a new ColorComboBox.
	 * 
	 * @param palette
	 *            the color values.
	 */
	public XdevColorComboBox(int palette)
	{
		super(palette);
	}
	
	/**
	 * the serialization id.
	 */
	private static final long							serialVersionUID	= 3185538391567326635L;
	
	/**
	 * tabIndex is used to store the index for {@link XdevFocusCycleComponent}
	 * functionality.
	 */
	private int											tabIndex			= -1;
	protected Color										savedValue			= null;
	private FormularComponentSupport<XdevColorComboBox>	support;
	private boolean										includeAlpha		= false;
	private Object										currentValue		= null;
	
	
	/**
	 * @return the includeAlpha
	 */
	public boolean isIncludeAlpha()
	{
		return includeAlpha;
	}
	
	
	/**
	 * @param includeAlpha
	 *            the includeAlpha to set
	 */
	public void setIncludeAlpha(boolean includeAlpha)
	{
		this.includeAlpha = includeAlpha;
	}
	
	
	private FormularComponentSupport<XdevColorComboBox> getSupport()
	{
		if(this.support == null)
		{
			this.support = new FormularComponentSupport<XdevColorComboBox>(this);
		}
		
		return this.support;
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
			firePropertyChange(TAB_INDEX_PROPERTY,oldValue,tabIndex);
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getFormularName()
	{
		return this.getSupport().getFormularName();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDataField(String dataField)
	{
		this.getSupport().setDataField(dataField);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDataField()
	{
		return this.getSupport().getDataField();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Deprecated
	public void setFormularValue(VirtualTable vt, int columnIndex, Object value)
	{
		this.getSupport().setFormularValue(vt,columnIndex,value);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setFormularValue(VirtualTable vt, Map<String, Object> record)
	{
		if(!this.getSupport().hasDataField())
		{
			return;
		}
		
		Map<String, Object> value = this.getSupport().getValuesForDataFields(record);
		
		this.currentValue = getFormularSelectionValue(vt,value);
		if(this.currentValue != null)
		{
			this.setSelectedColor(ColorParserUtils.getRGBAColorFrom(currentValue,
					this.isIncludeAlpha()));
		}
		else
		{
			this.setSelectedIndex(-1);
		}
	}
	
	
	/**
	 * Returns the color column value from the given record {@link Map}.
	 * 
	 * @param vt
	 *            the {@link VirtualTable} which includes the color column.
	 * @param valueMap
	 *            the {@link VirtualTableRow} {@link Map} representation.
	 * @return the color column, extracted from the given row.
	 */
	protected Object getFormularSelectionValue(VirtualTable vt, Map<String, Object> valueMap)
	{
		for(String key : valueMap.keySet())
		{
			VirtualTableColumn<?> col = vt.getColumn(key);
			
			if(col.getFullQualifiedName().equals(this.getDataField())
					&& col.getDataFlavor().equals(DataFlavor.COLOR))
			{
				Object colorValue = valueMap.get(key);
				
				return colorValue;
			}
		}
		return null;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getFormularValue()
	{
		// use default value to determine the value object/column type
		return ColorParserUtils.getRGBAValueFromColor(this.getSelectedColor(),FormularSupport
				.getVirtualTableColumn(this.getSupport().getDataField()).getType(),this
				.isIncludeAlpha());
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void saveState()
	{
		this.savedValue = this.getSelectedColor();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void restoreState()
	{
		if(this.savedValue != null)
		{
			this.setSelectedColor(savedValue);
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasStateChanged()
	{
		return !ObjectUtils.equals(savedValue,this.getSelectedColor());
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addValueChangeListener(final xdev.ui.FormularComponent.ValueChangeListener l)
	{
		this.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				l.valueChanged(e);
			}
		});
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
		return this.getSupport().verify();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addValidator(Validator validator)
	{
		this.getSupport().addValidator(validator);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeValidator(Validator validator)
	{
		this.getSupport().removeValidator(validator);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Validator[] getValidators()
	{
		return this.getSupport().getValidators();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void validateState() throws ValidationException
	{
		this.getSupport().validateState();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void validateState(Validation validation) throws ValidationException
	{
		this.getSupport().validateState(validation);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Operator getFilterOperator()
	{
		return this.getSupport().getFilterOperator();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setFilterOperator(Operator filterOperator)
	{
		this.getSupport().setFilterOperator(filterOperator);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isReadOnly()
	{
		return this.getSupport().isReadOnly();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setReadOnly(boolean readOnly)
	{
		this.getSupport().setReadOnly(readOnly);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Color getColor()
	{
		return this.getSelectedColor();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setColor(Color color)
	{
		this.setSelectedColor(color);
	}
}
