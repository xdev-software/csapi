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


import java.util.ArrayList;
import java.util.List;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import xdev.ui.FormularComponent.ValueChangeListener;
import xdev.ui.ItemList.Entry;


/**
 * 
 * @author XDEV Software
 * 
 * @param <L>
 */
class DualListBoxSupport<L extends XdevDualListBox & FormularComponent<L>> extends
		FormularComponentSupport<L>
{
	protected List<Entry>	savedValue	= new ArrayList();
	
	
	protected DualListBoxSupport(L list)
	{
		super(list);
	}
	
	
	public boolean isMultiSelect()
	{
		return component.isMultipleSelectionsEnabled();
	}
	
	
	// public void setFormularValue(VirtualTable vt, int col, Object value)
	// {
	// list.deselectAll();
	//
	// if(value != null)
	// {
	// List values;
	// if(isMultiSelect() && value.getClass().isArray())
	// {
	// values = CollectionUtils.asList((Object[])value);
	// }
	// else
	// {
	// values = CollectionUtils.asList(value);
	// }
	// list.addToSelected(list.getIndicesOfData(values),false);
	// }
	// }
	
	public Object getFormularValue()
	{
		ItemList itemList = component.selectedItems;
		if(itemList.size() == 0)
		{
			return null;
		}
		else if(itemList.size() == 1 && !isMultiSelect())
		{
			return itemList.getData(0);
		}
		
		return itemList.getDataAsList().toArray();
	}
	
	
	public void saveState()
	{
		savedValue.clear();
		ItemList selected = component.selectedItems;
		int c = selected.size();
		for(int i = 0; i < c; i++)
		{
			savedValue.add(selected.get(i).clone());
		}
	}
	
	
	public void restoreState()
	{
		component.setSelectedEntries(savedValue,false);
	}
	
	
	public boolean hasStateChanged()
	{
		List<Entry> selectedEntries = new ArrayList();
		ItemList selected = component.selectedItems;
		int c = selected.size();
		for(int i = 0; i < c; i++)
		{
			selectedEntries.add(selected.get(i));
		}
		
		return !savedValue.equals(selectedEntries);
	}
	
	
	public void addValueChangeListener(final ValueChangeListener l)
	{
		component.addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent e)
			{
				l.valueChanged(e);
			}
		});
	}
}
