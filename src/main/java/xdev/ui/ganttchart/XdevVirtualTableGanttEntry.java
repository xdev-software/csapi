package xdev.ui.ganttchart;

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


import xdev.ui.ganttchart.model.XdevGanttEntryVTMappings;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTable.VirtualTableRow;

import com.jidesoft.gantt.DefaultGanttEntry;
import com.jidesoft.gantt.GanttChart;
import com.jidesoft.gantt.GanttEntry;
import com.jidesoft.range.NumericRange;
import com.jidesoft.range.Range;
import com.jidesoft.range.TimeRange;


/**
 * 
 * This customized {@link GanttEntry} represents an entry in a
 * {@link XdevGanttChart}. It has an unique ID within the {@link GanttChart}.
 * <p>
 * It has a start and end value, which is represented via {@link Range}.
 * </p>
 * <p>
 * The value could be a date, time or an int value, depending on the use case.
 * </p>
 * It also has a completion value which is the percentage of the completion.
 * 
 * 
 * @param <T>
 *            data type, either date or number format.
 * 
 * @author XDEV Software jwill
 * @since 4.0
 */
public class XdevVirtualTableGanttEntry<T> extends DefaultGanttEntry<T> implements
		UpdateableGanttEntry<T>
{
	
	/**
	 * 
	 * @param description
	 *            the default description for the {@link GanttEntry} in
	 *            {@link String} format.
	 * @param dataInfo
	 *            the {@link XdevGanttEntryVTMappings} which provides mapping
	 *            information about the <code>GanttEntry</code>.
	 * @param vt
	 *            the {@link VirtualTable} for specific visibility mapping
	 *            actions.
	 */
	public XdevVirtualTableGanttEntry(String description, XdevGanttEntryVTMappings<T> dataInfo,
			VirtualTable vt)
	{
		super(description);
		this.dcnt = dataInfo;
		this.vt = vt;
	}
	
	
	/**
	 * 
	 * @param description
	 *            the default description value for the {@link GanttEntry} in
	 *            {@link String} format.
	 * @param dataInfo
	 *            the {@link XdevGanttEntryVTMappings} which provides mapping
	 *            information about the <code>GanttEntry</code>.
	 * 
	 * @param range
	 *            the default {@link Range} for the {@link GanttEntry}, range
	 *            type is the {@link GanttEntry} type <code>T</code> (see class
	 *            descpiption).
	 * 
	 * @param vt
	 *            the {@link VirtualTable} for specific visibility mapping
	 *            actions.
	 * 
	 * 
	 * @see TimeRange
	 * @see NumericRange
	 */
	public XdevVirtualTableGanttEntry(String description, Range<T> range,
			XdevGanttEntryVTMappings<T> dataInfo, VirtualTable vt)
	{
		super(description,range);
		this.dcnt = dataInfo;
		this.vt = vt;
	}
	
	
	/**
	 * 
	 * @param description
	 *            the default description value for the {@link GanttEntry} in
	 *            {@link String} format.
	 * @param dataInfo
	 *            the {@link XdevGanttEntryVTMappings} which provides mapping
	 *            information about the <code>GanttEntry</code>.
	 * 
	 * @param range
	 *            the default range for the {@link GanttEntry}, range type is
	 *            the {@link GanttEntry} type <code>T</code> (see class
	 *            descpiption).
	 * 
	 * @param completion
	 *            the default completion value for the {@link GanttEntry} in
	 *            {@link Double} format.
	 * 
	 * @param vt
	 *            the {@link VirtualTable} for specific visibility mapping
	 *            actions.
	 * 
	 * @see TimeRange
	 * @see NumericRange
	 */
	public XdevVirtualTableGanttEntry(String description, Range<T> range, double completion,
			XdevGanttEntryVTMappings<T> dataInfo, VirtualTable vt)
	{
		super(description,null,range,completion);
		this.dcnt = dataInfo;
		this.vt = vt;
	}
	
	/**
	 * the entry id.
	 */
	private Object						id		= null;
	
	/**
	 * the entries root entry id.
	 */
	private Object						root	= null;
	
	/**
	 * linked {@link VirtualTableRow} data provider.
	 */
	private VirtualTable				vt;
	
	/**
	 * the {@link XdevGanttEntryVTMappings} which provides mapping informatin
	 * about the {@link GanttEntry}.
	 */
	private XdevGanttEntryVTMappings<T>	dcnt;
	
	
	// asks for the mandatory columns indices e.g. completion to set the
	// specific
	// renderer
	@Override
	protected int getActualColumnIndex(int column)
	{
		
		return GanttEntryVirtualTableColumnConverter.getActualMandatoryColumnIndex(column,
				this.dcnt,this.vt);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getValueAt(int columnIndex)
	{
		return super.getValueAt(columnIndex);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setValueAt(Object value, int columnIndex)
	{
		super.setValueAt(value,columnIndex);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getRoot()
	{
		return root;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setRoot(Object root)
	{
		this.root = root;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getId()
	{
		return id;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setId(Object id)
	{
		this.id = id;
	}
	
}
