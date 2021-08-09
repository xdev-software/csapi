package xdev.ui.ganttchart.editor;

/*-
 * #%L
 * XDEV BI Suite
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


import java.util.Map;

import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import xdev.ui.FormularComponentSupport;
import xdev.ui.XdevSpinner;
import xdev.util.logging.LoggerFactory;
import xdev.util.logging.XdevLogger;
import xdev.vt.VirtualTable;


//maybe create unified bean with % sign label
public class GanttPercentageSpinner extends XdevSpinner
{
	
	/**
	 * 
	 */
	private static final long							serialVersionUID	= 2558968817433351128L;
	private final FormularComponentSupport<XdevSpinner>	support				= new FormularComponentSupport<XdevSpinner>(
																					this);
	/**
	 * Logger instance for this class.
	 */
	private static final XdevLogger						log					= LoggerFactory
																					.getLogger(GanttPercentageSpinner.class);
	
	
	public GanttPercentageSpinner()
	{
		super();
		this.init();
	}
	
	
	private void init()
	{
		this.setModel(new SpinnerNumberModel(0.0,0.0,100,10.0));
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getFormularValue()
	{
		// valid - see intern setModel invoke
		Double superValue = (Double)super.getValue();
		
		return superValue / 100;
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.2
	 */
	@Override
	public void setFormularValue(VirtualTable vt, Map<String, Object> record)
	{
		if(!support.hasDataField())
		{
			return;
		}
		
		Object value = support.getSingleValue(vt,record);
		if(value != null)
		{
			SpinnerModel spinnerModel = getModel();
			if(spinnerModel instanceof SpinnerNumberModel)
			{
				if(value instanceof Number)
				{
					Number nValue = (Number)value;
					spinnerModel.setValue(nValue.doubleValue() * 100);
				}
				else
				{
					try
					{
						spinnerModel.setValue(Double.valueOf(value.toString()) * 100);
					}
					catch(NumberFormatException e)
					{
						log.error(e);
					}
				}
			}
		}
	}
}
