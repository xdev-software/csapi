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
package xdev.ui.listbox;


import javax.swing.JList;
import javax.swing.ListModel;

import xdev.ui.ItemListOwner;
import xdev.vt.VirtualTable;

import com.jidesoft.grid.QuickFilterField;
import com.jidesoft.list.ListModelWrapperUtils;


/**
 * Component Suite implementation of {@link ListBoxRowConverter}.
 * <p>
 * For handling of view modified data for example by filtering or sorting.
 * </p>
 * <p>
 * Gets initialized at Component Suite loadtime.
 * <p>
 * Extends {@link ListBoxRowConverter} behavior to the ability of reconverting
 * view updates in updated models. For example the ComponentSuite
 * {@link QuickFilterField}s filtering algorithms modify the model-data on model
 * level.
 * </p>
 * </p>
 * 
 * @author XDEV Software
 * 
 */
public class CSListBoxRowConverter extends DefaultListBoxRowConverter
{
	
	/**
	 * Calculates the data representation of a given view-model row. For example
	 * for saving filtered or sorted data independent to its view index.
	 * 
	 * <p>
	 * <b> Use only if the component holds a {@link VirtualTable} as model. </b>
	 * </p>
	 * 
	 * @param listbox
	 *            the listbox to calculate model modifications from.
	 * @param row
	 *            the view row to get the model representation from.
	 * 
	 * @return the representation of the given row in the datamodel
	 * 
	 */
	@Override
	public int viewToModel(JList listbox, int row)
	{
		if(listbox.getModel() instanceof ItemListOwner)
		{
			return super.viewToModel(listbox,row);
		}
		
		if(row != -1)
		{
			// use Jide functionality to convert from a virtual table model to
			// view index
			int subModelRow = ListModelWrapperUtils.getActualIndexAt(listbox.getModel(),row,
					ItemListOwner.class);
			if(subModelRow == -1)
			{
				// if no virtual table model is found, use first inner model
				// that is not a table wrapper
				row = super.viewToModel(listbox,row);
			}
			else
			{
				row = subModelRow;
			}
		}
		return row;
	}
	
	
	/**
	 * Calculates the view representation of a given data-model row. For example
	 * for setting dataindex to to a sorted or filtered component.
	 * 
	 * <p>
	 * <b> Use only if the component holds a {@link VirtualTable} as model. </b>
	 * </p>
	 * 
	 * @param listbox
	 *            the listbox to calculate model modifications from.
	 * @param row
	 *            the model row to get the view representation from.
	 * 
	 * @return the representation of the given row in the view layer.
	 * 
	 */
	@Override
	public int modelToView(JList listbox, int row)
	{
		// invoke default behavior
		if((listbox.getModel() instanceof ItemListOwner))
		{
			return super.modelToView(listbox,row);
		}
		
		if(row != -1)
		{
			ListModel outerModel = listbox.getModel();
			ListModel innerModel = ListModelWrapperUtils.getActualListModel(outerModel,
					ItemListOwner.class);
			
			if(innerModel != null && innerModel != outerModel)
			{
				row = ListModelWrapperUtils.getIndexAt(outerModel,innerModel,row);
			}
			else
			{
				row = ListModelWrapperUtils.getIndexAt(outerModel,row);
			}
		}
		return row;
	}
}
