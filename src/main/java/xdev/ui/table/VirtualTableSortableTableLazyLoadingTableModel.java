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

import xdev.ui.MasterDetailComponent;
import xdev.ui.TableSupport;
import xdev.ui.paging.LazyLoadingTableModel;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTableColumn;

import com.jidesoft.grid.ISortableTableModel;
import com.jidesoft.grid.ISortableTableModel.SortItem;
import com.jidesoft.grid.SortEvent;
import com.jidesoft.grid.SortListener;
import com.jidesoft.grid.SortableTable;


/**
 * Enables 'lazy loadable' components to react correctly on model based sorting.
 * 
 * @author XDEV Software
 * 
 * @param <T>
 *            {@link SortableTable} type must be also a
 *            {@link MasterDetailComponent}
 * @since 4.0
 */
public class VirtualTableSortableTableLazyLoadingTableModel<T extends SortableTable & MasterDetailComponent<T>>
		extends LazyLoadingTableModel<T>
{
	
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -9118032210202895798L;
	private List<SortItem>		actualSortedItems;
	private List<SortItem>		lastSortedItems;
	private ISortableTableModel	concreteModel;
	private T					table;
	private boolean				sortChanging		= false;
	
	
	/**
	 * Returns the observed {@link ISortableTableModel}.
	 * 
	 * @return the observed {@link ISortableTableModel}
	 */
	public ISortableTableModel getConcreteModel()
	{
		return concreteModel;
	}
	
	
	/**
	 * Sets the {@link ISortableTableModel} to observe.
	 * 
	 * @param concreteModel
	 *            the {@link ISortableTableModel} which needs to be observed to
	 *            compute lazy loading.
	 */
	public void setConcreteModel(ISortableTableModel concreteModel)
	{
		this.concreteModel = concreteModel;
		this.addLazyLoadingTableSortListener(concreteModel);
	}
	
	
	/**
	 * Creates a new {@link VirtualTableSortableTableLazyLoadingTableModel}
	 * which wraps around a given model to provide lazy loading features.
	 * 
	 * @param t
	 *            the data model.
	 */
	public VirtualTableSortableTableLazyLoadingTableModel(T t)
	{
		super(t);
		this.table = t;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getValueAt(int row, int col)
	{
		// jide just updates/repaints all rows within the table model if a sort
		// was
		// triggered (instead of just updating the view port...)
		int viewRow = TableSupport.getTableRowConverter().modelToView(this.table,row);
		int viewColumn = TableSupport.getTableColumnConverter().modelToView(this.table,col);
		
		if(viewRow >= getPager().getRowsPerPage() && this.sortChanging
				|| viewRow <= getRequestedFirst() && this.sortChanging)
		{
			return null;
		}
		
		return super.getValueAt(viewRow,viewColumn);
	}
	
	
	/**
	 * Adds a {@link SortListener} to the given model to compute lazy loading.
	 * 
	 * @param model
	 *            the observed model.
	 */
	public void addLazyLoadingTableSortListener(final ISortableTableModel model)
	{
		model.addSortListener(new SortListener()
		{
			
			@Override
			public void sortChanged(SortEvent sortEvent)
			{
				sortChanging = false;
				List<SortItem> sortedItems = model.getSortingColumns();
				if(lastSortedItems != null
						&& JideSortItemUtils.equalsSortItemCriteria(lastSortedItems,sortedItems))
				{
					return;
				}
				
				if(sortedItems.isEmpty())
				{
					SwingUtilities.invokeLater(new Runnable()
					{
						@Override
						public void run()
						{
							refresh();
						}
					});
				}
				
				lastSortedItems = JideSortItemUtils.copySortedItems(sortedItems);
				
				actualSortedItems = sortedItems;
				
				SwingUtilities.invokeLater(new Runnable()
				{
					@Override
					public void run()
					{
						refresh();
					}
				});
			}
			
			
			@Override
			public void sortChanging(SortEvent sortEvent)
			{
				sortChanging = true;
			}
		});
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void refresh()
	{
		if(this.actualSortedItems != null)
		{
			getSelect().clear_ORDER_BY();
			for(SortItem sortItem : this.actualSortedItems)
			{
				int viewIndex = sortItem.getColumn();
				VirtualTable vt = getVirtualTable();
				int[] indizes = vt.getVisibleColumnIndices();
				int sortindex = indizes[viewIndex];
				VirtualTableColumn<?> col = vt.getColumnAt(sortindex);
				getSelect().ORDER_BY(col,!sortItem.isAscending());
			}
		}
		super.refresh();
	}
}
