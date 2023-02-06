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


import static xdev.util.ObjectUtils.notNull;
import xdev.ui.ganttchart.RangeProvider;
import xdev.ui.ganttchart.SpecialRelationalUpdatableEntryCreator;
import xdev.ui.ganttchart.UpdateableGanttEntry;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTable.VirtualTableRow;

import com.jidesoft.gantt.GanttEntry;
import com.jidesoft.range.Range;


/**
 * An implementation of {@link GanttEntryMapper} for mapping
 * {@link VirtualTableRow} instances to {@link GanttEntry}s.
 * 
 * 
 * @param <T>
 *            the data type, for example <code>Date</code> or
 *            <code>Integer</code>.
 * @param <S>
 *            the customized {@link GanttEntry} type.
 * 
 * @author XDEV Software jwill
 * @since 4.0
 */
public class EntryVTMapper<S extends UpdateableGanttEntry<T>, T> implements
		GanttEntryMapper<VirtualTableRow, S, T>
{
	
	/**
	 * The apropriate range provider logic for the handled type T.
	 */
	private final RangeProvider<T>								rangeProvider;
	
	/**
	 * The special EntryCreator for {@link VirtualTable} based implementations.
	 */
	private final SpecialRelationalUpdatableEntryCreator<S, T>	entryCreator;
	
	/**
	 * The data container for VirtualTableRow (2d) mappings, to avoid unsafe
	 * casts.
	 */
	private final XdevGanttEntryVTMappings<T>					dataContainer;
	
	
	public XdevGanttEntryVTMappings<T> getDataContainer()
	{
		return dataContainer;
	}
	
	
	/**
	 * 
	 * @param rangeProvider
	 *            the {@link RangeProvider} which returns the a concrete
	 *            {@link Range} for the set data type <code>T</code> (see class
	 *            description).
	 * @param entryCreator
	 *            the {@link SpecialRelationalUpdatableEntryCreator} which
	 *            returns a concrete customized {@link GanttEntry} for the set
	 *            <GanttEntry> type <code>S</code> (see class descpription).
	 * @param dataContainer
	 *            the {@link XdevGanttEntryVTMappings} which provides data
	 *            mapping information.
	 */
	public EntryVTMapper(final RangeProvider<T> rangeProvider,
			final SpecialRelationalUpdatableEntryCreator<S, T> entryCreator,
			XdevGanttEntryVTMappings<T> dataContainer)
	{
		super();
		
		this.rangeProvider = notNull(rangeProvider);
		this.entryCreator = notNull(entryCreator);
		this.dataContainer = notNull(dataContainer);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public RangeProvider<T> getRangeProvider()
	{
		return rangeProvider;
	}
	
	
	/**
	 * Returns the given concrete EntryCreator which provides new Entries
	 * related to the given EntryType.
	 * 
	 * @return the concrete entryCreator for EntryType.
	 */
	public SpecialRelationalUpdatableEntryCreator<S, T> getEntryCreator()
	{
		return entryCreator;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public S dataToGanttEntry(final VirtualTableRow row)
	{
		final S entry = this.getTypeEntry(row);
		
		if(this.dataContainer.getId() != null)
		{
			entry.setId(row.get(this.dataContainer.getId()));
		}
		
		if(this.dataContainer.getCompletion() != null)
		{
			if(row.get(dataContainer.getCompletion()) != null)
			{
				entry.setCompletion(row.get(dataContainer.getCompletion()));
			}
			else
			{
				// default value
				entry.setCompletion(0.0);
			}
		}
		
		if(this.dataContainer.getDescription() != null)
		{
			if(row.get(dataContainer.getDescription()) != null)
			{
				entry.setName(row.get(dataContainer.getDescription()));
			}
			else
			{
				// default value
				entry.setName("New Entry");
			}
		}
		
		if(this.dataContainer.getRoot() != null)
		{
			entry.setRoot(row.get(dataContainer.getRoot()));
		}
		
		return entry;
	}
	
	
	/**
	 * Prepares the {@link GanttEntry} converted from the given
	 * {@link VirtualTableRow}.
	 * 
	 * @param row
	 *            the <code>VirtualTableRow</code> to get the
	 *            <code>GanttEntry</code> from.
	 * @return the prepared <code>GanttEntry</code>.
	 */
	private S getTypeEntry(final VirtualTableRow row)
	{
		final T start = row.get(this.dataContainer.getStart());
		final T end = row.get(this.dataContainer.getEnd());
		
		final Range<T> range = this.rangeProvider.provideRange(start,end);
		
		final S entry = this.entryCreator.createRelationalUpdatableEntry("",range,
				this.dataContainer);
		
		return entry;
	}
	
}
