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
package xdev.ui.table;


import java.util.List;

import javax.swing.SwingUtilities;

import xdev.ui.VirtualTableOwner;
import xdev.ui.paging.PageControl;

import com.jidesoft.grid.ISortableTableModel;
import com.jidesoft.grid.ISortableTableModel.SortItem;
import com.jidesoft.grid.SortEvent;
import com.jidesoft.grid.SortListener;
import com.jidesoft.grid.SortableTable;


/**
 * Enables paging components to react correctly on model based sorting.
 * 
 * @author XDEV Software
 * 
 * @param <P>
 *            {@link SortableTable} type must be also a
 *            {@link VirtualTableOwner}
 * @since 4.0
 */
public class VirtualTablePageControlSortListener<P extends SortableTable & VirtualTableOwner>
		implements SortListener
{
	private ISortableTableModel			model;
	private SortableTablePageControl<P>	pageControl;
	private List<SortItem>				lastSortedItems;
	
	
	/**
	 * Creates a {@link VirtualTablePageControlSortListener} connected to the
	 * given {@link ISortableTableModel}.
	 * 
	 * @param model
	 *            the model to connect with.
	 * @param pageControl
	 *            the {@link PageControl} to inform.
	 * 
	 * @see SortListener
	 */
	public VirtualTablePageControlSortListener(ISortableTableModel model,
			SortableTablePageControl<P> pageControl)
	{
		this.model = model;
		this.pageControl = pageControl;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sortChanged(SortEvent e)
	{
		List<SortItem> actualSortedItems = this.model.getSortingColumns();
		if(this.lastSortedItems != null
				&& JideSortItemUtils.equalsSortItemCriteria(this.lastSortedItems,actualSortedItems))
		{
			return;
		}
		
		if(actualSortedItems.isEmpty())
		{
			SwingUtilities.invokeLater(new Runnable()
			{
				@Override
				public void run()
				{
					pageControl.refresh();
				}
			});
		}
		
		this.lastSortedItems = JideSortItemUtils.copySortedItems(actualSortedItems);
		
		pageControl.setSortedItems(actualSortedItems);
		
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				pageControl.refresh();
			}
		});
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sortChanging(SortEvent arg0)
	{
		// nothing required
	}
}
