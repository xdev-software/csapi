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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xdev.db.DBConnection;
import xdev.db.DBException;
import xdev.db.sql.Condition;
import xdev.ui.ManyToMany.State;
import xdev.util.IntList;
import xdev.util.StringUtils;
import xdev.util.logging.LoggerFactory;
import xdev.util.logging.XdevLogger;
import xdev.vt.KeyValues;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTable.VirtualTableRow;
import xdev.vt.VirtualTableColumn;
import xdev.vt.VirtualTableException;


/**
 * A specialized version of {@link XdevDualListBox} to be used for editing
 * many-to-many relationships within {@link VirtualTable} instances. This class
 * can be used within {@link Formular} instances to select multiple
 * {@link VirtualTableRow}s from a detail table to a master record.
 * <p>
 * Prior to using this component, its
 * {@link #setModel(VirtualTable, String, String, boolean, SelectionMode)}
 * method has to be called providing information on a {@link VirtualTable} that
 * represents the N:M relationship. The related master table is retrieved
 * automatically from the surrounding {@link Formular} component of this
 * instance.
 * </p>
 * 
 * @author XDEV Software
 * @see XdevDualListBox
 * @see XdevNmListBox
 */
public class XdevNmDualListBox extends XdevDualListBox implements ManyToManyComponent
{
	/**
	 * Logger instance for this class.
	 */
	private static final XdevLogger	log					= LoggerFactory
																.getLogger(XdevNmDualListBox.class);
	
	/**
	 * serialVersionUID.
	 */
	private static final long		serialVersionUID	= 1L;
	
	/**
	 * Instance of helper class for managing nm state.
	 */
	private State					state;
	
	/**
	 * The state of the nm-table at the time of refreshing.
	 */
	private VirtualTable			savedState;
	
	/**
	 * The state of the available lists virtual table at the time of refreshing.
	 */
	private VirtualTable			availableSavedState;
	
	/**
	 * the column name used as item representation.
	 */
	private String					itemColumn;
	
	/**
	 * the column name used as data representation.
	 */
	private String					dataColumn;
	
	
	/**
	 * the list with the current selected items.
	 */
	// private List selectionState;
	
	/**
	 * Creates a new instance of {@link XdevNmDualListBox}.
	 */
	public XdevNmDualListBox()
	{
		super();
		
		// don't display sort buttons per default, as sorting doesn't happen in
		// nm-table
		this.setShowSortButtons(false);
	}
	
	
	/**
	 * Updates the underlying model with data from the {@link VirtualTable} vt.
	 *
	 * @param nmVirtualTable
	 *            the {@link VirtualTable} containing the data for the model
	 * @param itemCol
	 *            columnname to fill <code>item</code> from or string with
	 *            variables like <code>"{%SURNAME} {%NAME} - {%AGE}"</code>
	 * @param dataCol
	 *            column name to fill <code>data</code> from, or multiple
	 *            columns names, comma-separated
	 * @param queryData
	 *            if {@code true}, the best fitting select for this {@code vt}
	 *            is used
	 *
	 * @see ItemList#setModel(VirtualTable, String, String, boolean)
	 * @see StringUtils#format(String, xdev.util.StringUtils.ParameterProvider)
	 */
	public void setModel(VirtualTable nmVirtualTable, String itemCol, String dataCol,
			boolean queryData)
	{
		setModel(nmVirtualTable,itemCol,dataCol,queryData,false);
	}
	
	
	/**
	 * Updates the underlying model with data from the {@link VirtualTable} vt.
	 *
	 * @param nmVirtualTable
	 *            the {@link VirtualTable} containing the data for the model
	 * @param itemCol
	 *            columnname to fill <code>item</code> from or string with
	 *            variables like <code>"{%SURNAME} {%NAME} - {%AGE}"</code>
	 * @param dataCol
	 *            column name to fill <code>data</code> from, or multiple
	 *            columns names, comma-separated
	 * @param queryData
	 *            if {@code true}, the best fitting select for this {@code vt}
	 *            is used
	 * @param selectiveQuery
	 *            if {@code true}, only the used <code>columns</code> are
	 *            queried
	 *
	 * @see ItemList#setModel(VirtualTable, String, String, boolean, boolean)
	 * @see StringUtils#format(String, xdev.util.StringUtils.ParameterProvider)
	 *
	 * @since 5.0
	 */
	public void setModel(VirtualTable nmVirtualTable, final String itemCol, final String dataCol,
			final boolean queryData, final boolean selectiveQuery)
	{
		this.itemColumn = itemCol;
		this.dataColumn = dataCol;
		
		/*
		 * Dont't use unique indices in this VT because most of the values are
		 * not used, default values will be added and
		 * UniqueIndexDoubleValuesException are thrown
		 */
		nmVirtualTable = nmVirtualTable.clone(!queryData);
		nmVirtualTable.setCheckUniqueIndexDoubleValues(false);
		availableList.setModel(nmVirtualTable,this.itemColumn,this.dataColumn,queryData,selectiveQuery);
		
		nmVirtualTable = nmVirtualTable.clone(!queryData);
		nmVirtualTable.setCheckUniqueIndexDoubleValues(false);
		selectedList.setModel(nmVirtualTable,this.itemColumn,this.dataColumn,queryData,selectiveQuery);
		
		/*
		 * Don't query all the data of the nm-table, use the refresh method
		 * instead to get a reasonable result.
		 */
		if(queryData)
		{
			Formular form = FormularSupport.getFormularOf(this);
			if(form != null)
			{
				VirtualTable masterVT = form.getVirtualTable();
				if(masterVT != null)
				{
					refresh(masterVT.createRow());
				}
			}
			else
			{
				// #11981 avoid initial fill of the selectedList, if no form
				// is configured
				selectedList.getItemList().clear();
			}
		}
		
		availableItems = availableList.getItemList();
		selectedItems = selectedList.getItemList();
		
		update();
		
		// selectionState = selectedItems.getDataAsList();
	}
	
	
	/**
	 * Updates the underlying model with data from the {@link VirtualTable} vt.
	 *
	 * @param nmVirtualTable
	 *            the {@link VirtualTable} containing the data for the model
	 * @param itemCol
	 *            columnname to fill <code>item</code> from or string with
	 *            variables like <code>"{%SURNAME} {%NAME} - {%AGE}"</code>
	 * @param dataCol
	 *            column name to fill <code>data</code> from, or multiple
	 *            columns names, comma-separated
	 * @param queryData
	 *            if {@code true}, the best fitting select for this {@code vt}
	 *            is used
	 *
	 * @see ItemList#setModel(VirtualTable, String, String, boolean)
	 * @see StringUtils#format(String, xdev.util.StringUtils.ParameterProvider)
	 */
	public void setModel(VirtualTable nmVirtualTable, String itemCol, String dataCol,
			boolean queryData, SelectionMode selectionMode)
	{
		setModel(nmVirtualTable,itemCol,dataCol,queryData,false,selectionMode);
	}
	
	
	/**
	 * Updates the underlying model with data from the {@link VirtualTable} vt.
	 *
	 * @param nmVirtualTable
	 *            the {@link VirtualTable} containing the data for the model
	 * @param itemCol
	 *            columnname to fill <code>item</code> from or string with
	 *            variables like <code>"{%SURNAME} {%NAME} - {%AGE}"</code>
	 * @param dataCol
	 *            column name to fill <code>data</code> from, or multiple
	 *            columns names, comma-separated
	 * @param queryData
	 *            if {@code true}, the best fitting select for this {@code vt}
	 *            is used
	 * @param selectiveQuery
	 *            if {@code true}, only the used <code>columns</code> are
	 *            queried
	 *
	 * @see ItemList#setModel(VirtualTable, String, String, boolean, boolean)
	 * @see StringUtils#format(String, xdev.util.StringUtils.ParameterProvider)
	 *
	 * @since 5.0
	 */
	public void setModel(final VirtualTable nmVirtualTable, final String itemCol, final String dataCol,
			final boolean queryData, final boolean selectiveQuery, SelectionMode selectionMode)
	{
		this.selectionMode = selectionMode;
		this.setModel(nmVirtualTable,itemCol,dataCol,queryData,selectiveQuery);
	}
	
	
	/**
	 * Refresh updates the displayed state of the component according to the
	 * specified master record.
	 * 
	 * @param masterRecord
	 *            the master record of this component.
	 */
	@Override
	public void refresh(VirtualTableRow masterRecord)
	{
		try
		{
			state = new State(masterRecord,availableList.getVirtualTable());
			
			selectedList.getItemList().clear();
			
			List<Object> values = new ArrayList<Object>();
			Condition condition = state.getForeignKeyValues().getCondition(values);
			availableList.updateModel(condition,values.toArray());
			VirtualTable vt = availableList.getVirtualTable(); // re-get vt
			// after
			// updateModel
			savedState = vt.clone(true);
			availableSavedState = vt.clone(true);
			int[] indices = state.fillUpNMTable(vt,savedState);
			availableList.getItemList().syncWithVT();
			this.addToSelected(indices);
		}
		catch(Exception e)
		{
			log.error(e);
		}
	}
	
	
	/**
	 * Saves the state of the component to the underlying virtual table.
	 * 
	 * @param synchronizeDB
	 *            if {@code true} changes get propagated to db
	 * @param connection
	 *            the connection used for synchronizing with db.
	 * @throws DBException
	 *             a {@link DBException}
	 * @throws VirtualTableException
	 *             a {@link VirtualTableException}
	 */
	@Override
	public void save(boolean synchronizeDB, @SuppressWarnings("rawtypes") DBConnection connection)
			throws DBException, VirtualTableException
	{
		VirtualTable vt = selectedList.getVirtualTable();
		KeyValues foreignKeyValues = state.getForeignKeyValues();
		MasterDetail.updateForeignKeys(vt,foreignKeyValues);
		
		// build vt from itemlist entries of the selected list
		vt.clear();
		ItemList selectedItemlist = selectedList.getItemList();
		VirtualTableColumn<?> pkColumn = vt.getPrimaryColumn();
		for(int i = 0; i < selectedItemlist.size(); i++)
		{
			Map<String, Object> map = new HashMap<String, Object>();
			
			map.put(dataColumn,selectedItemlist.getData(i));
			String masterName = foreignKeyValues.getColumnNames()[0];
			map.put(masterName,foreignKeyValues.getValue(masterName));
			// look up id from saved available vt by comparing master + detail
			// id
			Object pk = getPk(selectedItemlist.getData(i),foreignKeyValues.getValue(masterName),
					foreignKeyValues);
			if(pk != null)
			{
				map.put(pkColumn.getName(),pk);
			}
			
			vt.addRow(map,false);
		}
		
		List<VirtualTableRow> added = new ArrayList<VirtualTableRow>();
		List<VirtualTableRow> changed = new ArrayList<VirtualTableRow>();
		List<VirtualTableRow> deleted = new ArrayList<VirtualTableRow>();
		
		for(int i = 0; i < vt.getRowCount(); i++)
		{
			added.add(vt.getRow(i));
		}
		
		ManyToMany.getChanges(savedState,added,changed,deleted);
		
		if(synchronizeDB)
		{
			ManyToMany.synchronize(vt,added,changed,deleted,connection);
		}
		
		savedState.clear();
		for(int i = 0; i < vt.getRowCount(); i++)
		{
			// re-get rows with generated keys
			savedState.addRow(vt.getRow(i),false);
		}
		state = new State(state.masterRecord,vt);
		
		// refresh(masterRecord);
		// this.invalidate();
	}
	
	
	/**
	 * Gets the primary key value from the availableSavedState with same detail
	 * and master id.
	 * 
	 * @param detailId
	 *            the referenced id from the detail
	 * @param masterId
	 *            the references id from the master
	 * @param foreignKeyValues
	 *            the foreign key used to get the current master id
	 * @return the primary key value, or {@code null} if none found
	 */
	private Object getPk(Object detailId, Object masterId, KeyValues foreignKeyValues)
	{
		VirtualTableColumn<?> pkColumn = availableSavedState.getPrimaryColumn();
		for(int i = 0; i < availableSavedState.getRowCount(); i++)
		{
			Object currentDetailId = availableSavedState.getValueAt(i,dataColumn);
			Object currentMasterId = availableSavedState.getValueAt(i,
					foreignKeyValues.getColumnNames()[0]);
			if(detailId.equals(currentDetailId) && masterId.equals(currentMasterId))
			{
				return availableSavedState.getValueAt(i,pkColumn.getName());
			}
		}
		return null;
	}
	
	
	/**
	 * Override returns the already configured instance.
	 * 
	 * @return an {@link XdevListBox}
	 * @see #setModel(VirtualTable, String, String, boolean)
	 */
	@Override
	protected XdevListBox createSelectedListbox()
	{
		// avoid traversing this component by XdevFormular #12276
		selectedList.putClientProperty(ClientProperties.FORMULAR_SKIP,Boolean.valueOf(true));
		return selectedList;
	}
	
	
	/**
	 * Override returns the already configured instance.
	 * 
	 * @return an {@link XdevListBox}
	 * @see #setModel(VirtualTable, String, String, boolean)
	 */
	@Override
	protected XdevListBox createAvailableListbox()
	{
		// avoid traversing this component by XdevFormular #12276
		availableList.putClientProperty(ClientProperties.FORMULAR_SKIP,Boolean.valueOf(true));
		return availableList;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public VirtualTable getVirtualTable()
	{
		return selectedList.getVirtualTable();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public void saveState()
	{
		try
		{
			save(false,null);
		}
		catch(DBException e)
		{
			e.printStackTrace();
		}
		catch(VirtualTableException e)
		{
			e.printStackTrace();
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasStateChanged()
	{
		Object[] values = new Object[savedState.getRowCount()];
		
		for(int i = 0; i < savedState.getRowCount(); i++)
		{
			values[i] = savedState.getValueAt(i,this.dataColumn);
		}
		
		// must validate savedState-VT with itemlist because altering the list
		// does
		// not
		// automatically syncs with it�s VT
		Object[] valuesNew = new Object[this.selectedList.getItemList().size()];
		
		for(int i = 0; i < this.selectedList.getItemList().size(); i++)
		{
			valuesNew[i] = this.selectedList.getItemList().getData(i);
		}
		
		// Object is not comparable but the nm-ids should be always a comparable
		// type -> warnings ok
		List saved = Arrays.asList(values);
		Collections.sort(saved);
		List actual = Arrays.asList(valuesNew);
		Collections.sort(actual);
		
		return !Arrays.equals(values,valuesNew);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void restoreState()
	{
		int[] indices = null;
		try
		{
			indices = this.state.fillUpNMTable(this.getVirtualTable(),this.savedState);
		}
		catch(DBException e)
		{
			e.printStackTrace();
		}
		catch(VirtualTableException e)
		{
			e.printStackTrace();
		}
		
		this.selectedList.clearModel();
		this.addToSelected(indices);
	}
	
	
	// Delegate to dualListBoxsupport in later release (+ FormComponent
	// impl.)
	
	/**
	 * Inserts the given {@link VirtualTableRow} in the selected list.
	 * 
	 * @param row
	 *            the {@link VirtualTableRow} to select.
	 * @since 4.0
	 */
	public void setSelectedVirtualTableRow(VirtualTableRow row)
	{
		if(row == null)
		{
			this.deselectAll();
		}
		else
		{
			VirtualTable vt = getVirtualTable();
			if(vt != null)
			{
				int rc = vt.getRowCount();
				for(int i = 0; i < rc; i++)
				{
					if(vt.getRow(i).equals(row))
					{
						addToSelected(i);
						break;
					}
				}
			}
		}
	}
	
	
	// Delegate to dualListBoxsupport in later release (+ FormComponent
	// impl.)
	
	/**
	 * Inserts the given {@link VirtualTableRow}s in the selected list.
	 * 
	 * @param rows
	 *            the {@link VirtualTableRow}s to select.
	 * @since 4.0
	 */
	public void setSelectedVirtualTableRows(VirtualTableRow[] rows)
	{
		if(rows == null || rows.length == 0)
		{
			this.deselectAll();
		}
		else
		{
			VirtualTable vt = getVirtualTable();
			if(vt != null)
			{
				IntList rowIndices = new IntList();
				int rc = vt.getRowCount();
				for(int i = 0; i < rc; i++)
				{
					VirtualTableRow listRow = vt.getRow(i);
					for(VirtualTableRow row : rows)
					{
						if(listRow.equals(row))
						{
							rowIndices.add(i);
							break;
						}
					}
				}
				addToSelected(rowIndices.toArray());
			}
		}
	}
	
	
	// Delegate to dualListBoxsupport in later release (+ FormComponent
	// impl.)
	/**
	 * Returns the <code>SelectedList</code>s items.
	 * 
	 * @return the {@link VirtualTableRow} items which are part of the
	 *         <code>SelectedList</code>.
	 * @since 4.0
	 */
	public VirtualTableRow[] getSelectedVirtualTableRows()
	{
		VirtualTable vt = getVirtualTable();
		ItemList list = this.selectedList.getItemList();
		List<VirtualTableRow> rows = new ArrayList<VirtualTableRow>();
		
		if(vt != null)
		{
			for(Object o : list.getItemsAsList())
			{
				for(int i = 0; i < vt.getRowCount(); i++)
				{
					VirtualTableRow row = vt.getRow(i);
					if(o.equals(row.get(this.itemColumn)))
					{
						rows.add(vt.getRow(i));
					}
				}
			}
			return rows.toArray(new VirtualTableRow[0]);
		}
		return null;
	}
}
