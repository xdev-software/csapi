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


import java.awt.Container;
import java.util.List;

import javax.swing.JTable;
import javax.swing.JViewport;

import xdev.db.DBException;
import xdev.db.sql.SELECT;
import xdev.ui.VirtualTableOwner;
import xdev.ui.paging.PageControl;
import xdev.ui.paging.Pageable;
import xdev.ui.paging.TablePageControl;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTableColumn;

import com.jidesoft.grid.ISortableTableModel;
import com.jidesoft.grid.ISortableTableModel.SortItem;
import com.jidesoft.grid.SortableTable;


/**
 * {@link PageControl} for {@link SortableTable}s.
 * 
 * 
 * @author XDEV Software
 * 
 * @param <P>
 *            SortableTable type must be also a {@link VirtualTableOwner}
 * 
 * @since 4.0
 * @see Pageable
 */
public class SortableTablePageControl<P extends SortableTable & VirtualTableOwner> extends
		TablePageControl<P>
{
	
	private P				table;
	private List<SortItem>	sortedItems;
	
	
	public void setSortedItems(List<SortItem> sortedItems)
	{
		this.sortedItems = sortedItems;
	}
	
	
	public SortableTablePageControl(P table)
	{
		super(table);
		this.table = table;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSingleRowPager()
	{
		return false;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public VirtualTable getVirtualTable()
	{
		return this.table.getVirtualTable();
	}
	
	
	/**
	 * Enables the paging component to react correctly on sorting.
	 * 
	 * @param model
	 *            the sortable table model.
	 */
	public void addPageingTableSortListener(final ISortableTableModel model)
	{
		model.addSortListener(new VirtualTablePageControlSortListener<P>(model,this));
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void changeModel(SELECT select, Object[] params, int showPageIndex) throws DBException
	{
		if(sortedItems != null)
		{
			select.clear_ORDER_BY();
			for(SortItem sortItem : sortedItems)
			{
				int viewIndex = sortItem.getColumn();
				VirtualTable vt = getVirtualTable();
				int[] indizes = vt.getVisibleColumnIndices();
				int sortindex = indizes[viewIndex];
				VirtualTableColumn<?> col = vt.getColumnAt(sortindex);
				select.ORDER_BY(col,!sortItem.isAscending());
			}
		}
		
		super.changeModel(select,params,showPageIndex);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getVisibleRowCount()
	{
		JTable table = this.table;
		if(!table.isShowing())
		{
			return 0;
		}
		
		int rowHeight = table.getRowHeight();
		int place;
		Container parent = table.getParent();
		if(parent instanceof JViewport)
		{
			place = ((JViewport)parent).getExtentSize().height;
		}
		else
		{
			place = table.getPreferredScrollableViewportSize().height;
		}
		return place / rowHeight;
	}
}
