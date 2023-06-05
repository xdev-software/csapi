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
package xdev.ui.ganttchart.model;


import java.util.ArrayList;
import java.util.List;

import xdev.ui.ganttchart.GanttModelUIInformation;
import xdev.ui.ganttchart.UpdateableGanttEntry;
import xdev.ui.ganttchart.XdevVirtualTableGanttEntry;
import xdev.util.IntList;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTable.VirtualTableRow;

import com.jidesoft.gantt.DefaultGanttEntryRelationModel;
import com.jidesoft.gantt.DefaultGanttModel;
import com.jidesoft.gantt.GanttEntry;
import com.jidesoft.gantt.GanttModel;
import com.jidesoft.grid.SortableTreeTableModel;
import com.jidesoft.grid.TreeTableModel;


/**
 * XdevVirtualTableGanttModel is an {@link VirtualTable}/
 * {@link VirtualTableRow} based implementation of {@link GanttModel} that uses
 * a {@link TreeTableModel} to store the {@link GanttEntry}s.
 * 
 * <p>
 * Suitable mappings must be provided to assign enhanced {@link GanttEntry}s
 * values.
 * </p>
 * 
 * @author XDEV Software jwill
 * @since 4.0
 * 
 * @param <T>
 *            data type, either date or number format.
 * @param <S>
 *            {@link GanttEntry} type
 */
public class XdevVirtualTableGanttModel<T, S extends UpdateableGanttEntry<T>> extends
		DefaultGanttModel<T, S>
{
	/**
	 * the serialization id.
	 */
	private static final long						serialVersionUID	= -2433427496084849177L;
	
	/**
	 * data storage.
	 */
	private VirtualTable							virtualTable;
	
	/**
	 * mapping class.
	 */
	private GanttEntryMapper<VirtualTableRow, S, T>	mapper				= null;
	
	private XdevGanttEntryVTMappings<T>				dataContainer;
	
	/**
	 * additional information.
	 */
	private GanttModelUIInformation<T>				uiInformation;
	
	private Object									rootIdentifier		= null;
	
	/**
	 * All relationable stored {@link GanttEntry list} for relation purpose.
	 */
	private List<S>									relationEntries		= new ArrayList<S>();
	
	
	public XdevVirtualTableGanttModel(XdevGanttEntryVTMappings<T> dataContainer,
			GanttEntryMapper<VirtualTableRow, S, T> mapper)
	{
		// to avoid ambiguous
		this(dataContainer,new XdevGanttModelUIInformation<T>(),mapper);
	}
	
	
	/**
	 * 
	 * 
	 * @param mapper
	 *            the mappings to match the {@link GanttEntry}s fields.
	 * 
	 * @param dataInfo
	 *            the necessary{@link GanttDataInformation}
	 * 
	 * @param uiInfo
	 */
	public XdevVirtualTableGanttModel(XdevGanttEntryVTMappings<T> dataContainer,
			GanttModelUIInformation<T> uiInfo, GanttEntryMapper<VirtualTableRow, S, T> mapper)
	{
		this.dataContainer = dataContainer;
		this.uiInformation = uiInfo;
		
		// init mandatory data vt
		this.virtualTable = dataContainer.getVirtualTable();
		this.mapper = mapper;
		
		transmitUIModels(this.uiInformation);
		
		for(S entry : createGanttEntryTree(dataContainer))
		{
			this.addGanttEntry(entry);
		}
	}
	
	
	/**
	 * Creates a {@link List} containing tree structure like
	 * {@link XdevVirtualTableGanttEntry}s - created from the given
	 * {@link VirtualTable}.
	 * 
	 * @return a {@link List} containing the {@link XdevVirtualTableGanttEntry}
	 *         s.
	 */
	private List<S> createGanttEntryTree(XdevGanttEntryVTMappings<T> dataContainer)
	{
		IntList rootRows = new IntList();
		List<S> entries = new ArrayList<S>();
		
		Object o;
		
		// sort asc by default
		this.virtualTable.sortByCol(dataContainer.getId().getName(),true);
		
		for(int i = 0; i < virtualTable.getRowCount(); i++)
		{
			o = virtualTable.getValueAt(i,
					this.virtualTable.getColumnIndex(this.dataContainer.getRoot()));
			if(VirtualTable.equals(o,this.getRootIdentifier()))
			{
				rootRows.add(i);
			}
		}
		
		for(int i = 0, c = rootRows.size(); i < c; i++)
		{
			int rootRow = rootRows.get(i);
			
			S mRoot = mapper.dataToGanttEntry(virtualTable.getRow(rootRow));
			mRoot.setExpandable(true);
			mRoot.setExpanded(true);
			
			// prepare relation data
			relationEntries.add(mRoot);
			
			createTree(
					mRoot,
					virtualTable.getValueAt(rootRow,
							this.virtualTable.getColumnIndex(this.dataContainer.getId())));
			
			entries.add(mRoot);
		}
		return entries;
	}
	
	
	/**
	 * 
	 * @param uiInformation
	 * 
	 *            the ui information container which provides information about
	 *            the ui representation of the <code>GanttChart</code>.
	 *            <p>
	 *            For example the ScaleModel calculation.
	 *            </p>
	 */
	private void transmitUIModels(GanttModelUIInformation<T> uiInformation)
	{
		if(uiInformation != null)
		{
			if(uiInformation.getScaleModel() != null)
			{
				this.setScaleModel(uiInformation.getScaleModel());
			}
			
			if(this.uiInformation.getTreeTableModel() != null)
			{
				this.setTreeTableModel(new SortableTreeTableModel<S>(uiInformation
						.getTreeTableModel()));
			}
		}
	}
	
	
	/**
	 * Creates the tree hierarchy recursively. Used by
	 * {@link #createGanttEntryTree()}
	 * 
	 * @param owner
	 *            the parent entry.
	 * 
	 * @param id
	 *            the next child id.
	 */
	private void createTree(S owner, Object id)
	{
		Object o;
		
		for(int row = 0; row < virtualTable.getRowCount(); row++)
		{
			o = virtualTable.getValueAt(row,this.dataContainer.getRoot());
			if(VirtualTable.equals(o,id))
			{
				
				S node = mapper.dataToGanttEntry(virtualTable.getRow(row));
				node.setExpandable(true);
				node.setExpanded(true);
				
				// prepare relation data
				if(!relationEntries.contains(node))
				{
					relationEntries.add(node);
				}
				
				owner.addChild(node);
				
				// continue traversing
				createTree(
						node,
						virtualTable.getValueAt(row,
								this.virtualTable.getColumnIndex(this.dataContainer.getId())));
			}
		}
	}
	
	
	// //WORKAROUND FOR JIDES
	// ganttEntryRelationModel#removeEntryRelation/////////////
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected DefaultGanttEntryRelationModel<S> createGanttEntryRelationModel()
	{
		return new XdevGanttEntryRelationModel<T, S>();
	}
	
	
	public Object getRootIdentifier()
	{
		return rootIdentifier;
	}
	
	
	public void setRootIdentifier(Object rootIdentifier)
	{
		this.rootIdentifier = rootIdentifier;
	}
	
}
