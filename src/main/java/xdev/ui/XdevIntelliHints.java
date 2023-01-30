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


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;

import xdev.util.StringUtils;
import xdev.util.logging.LoggerFactory;
import xdev.util.logging.XdevLogger;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTableColumn;
import xdev.vt.VirtualTableEvent;
import xdev.vt.VirtualTableListener;

import com.jidesoft.hints.ListDataIntelliHints;


/**
 * XdevIntelliHint can be used as an decorator on other components.
 * <p>
 * It enhances the functionality of the decorated component by displaying a
 * popup dialog with a list of hints for the currently typed text. The list gets
 * automatically updated while the user types.
 * </p>
 * 
 * @author XDEV Software
 * 
 */
public class XdevIntelliHints extends ListDataIntelliHints<String>
{
	
	/**
	 * Logger instance for this class.
	 */
	private static final XdevLogger	log							= LoggerFactory
																		.getLogger(XdevIntelliHints.class);
	
	/**
	 * The default number of visible entries in the hints popup.
	 */
	private static final int		DEFAULT_NR_OF_HINT_ENTRIES	= 3;
	
	/**
	 * The default key stroke to display the hints popup (Ctrl + Space).
	 */
	private static final KeyStroke	DEFAULT_HINT_KEYSTROKE		= KeyStroke.getKeyStroke(
																		KeyEvent.VK_SPACE,
																		KeyEvent.CTRL_MASK);
	/**
	 * the {@link VirtualTable} used to fill the hints popup.
	 */
	private VirtualTable			hintsTable;
	/**
	 * A {@link VirtualTableListener} used to sync changes the
	 * {@link XdevIntelliHints} with changes on the model.
	 */
	private VirtualTableListener	hintsTableListener;
	
	/**
	 * the currently visible number of rows in the hints popup.
	 */
	private int						visibleRowCount				= DEFAULT_NR_OF_HINT_ENTRIES;
	
	/**
	 * the {@link JTextComponent} that is connected to this instance.
	 */
	private JTextComponent			textComponent;
	
	
	/**
	 * Initializes a new instance of {@link XdevIntelliHints}.
	 * 
	 * @param comp
	 *            the {@link JTextComponent} to be decorated with this
	 *            {@link XdevIntelliHints}
	 * @param hintsList
	 *            an List of Objects containing the hints
	 * 
	 */
	public XdevIntelliHints(JTextComponent comp, List<String> hintsList)
	{
		super(comp,hintsList);
		this.textComponent = comp;
		this.setFollowCaret(false);
		this.setShowHintsKeyStroke(DEFAULT_HINT_KEYSTROKE);
	}
	
	
	/**
	 * Initializes a new instance of {@link XdevIntelliHints}.
	 * 
	 * @param comp
	 *            the {@link JTextComponent} to be decorated with this
	 *            {@link XdevIntelliHints}
	 * @param hintsArray
	 *            an Array of objects containing the hints
	 */
	public XdevIntelliHints(JTextComponent comp, String[] hintsArray)
	{
		super(comp,hintsArray);
		this.textComponent = comp;
		this.setFollowCaret(false);
		this.setShowHintsKeyStroke(DEFAULT_HINT_KEYSTROKE);
	}
	
	
	/**
	 * Initializes a new instance of {@link XdevIntelliHints}.
	 * <p>
	 * The values of this {@code hintsColumn} get automatically converted to
	 * type String.
	 * </p>
	 * 
	 * @param comp
	 *            the {@link JTextComponent} to be decorated with this
	 *            {@link XdevIntelliHints}
	 * @param hintsTable
	 *            a {@link VirtualTable} of which a column should be used for
	 *            hints
	 * @param hintsColumnIndex
	 *            the index of the column to use for the hints
	 */
	public XdevIntelliHints(JTextComponent comp, VirtualTable hintsTable, int hintsColumnIndex)
	{
		this(comp,hintsTable,hintsColumnIndex,false);
	}
	
	
	/**
	 * Initializes a new instance of {@link XdevIntelliHints}.
	 * <p>
	 * The values of this {@code hintsColumn} get automatically converted to
	 * type String.
	 * </p>
	 * 
	 * @param comp
	 *            the {@link JTextComponent} to be decorated with this
	 *            {@link XdevIntelliHints}
	 * @param hintsTable
	 *            a {@link VirtualTable} of which a column should be used for
	 *            hints
	 * @param hintsColumnIndex
	 *            the index of the column to use for the hints
	 * @param queryData
	 *            if {@code true} a query on the {@code hintsTable} gets
	 *            performed.
	 */
	public XdevIntelliHints(JTextComponent comp, VirtualTable hintsTable, int hintsColumnIndex,
			boolean queryData)
	{
		this(comp,hintsTable,hintsTable.getColumnAt(hintsColumnIndex).getName(),queryData);
	}
	
	
	/**
	 * Initializes a new instance of {@link XdevIntelliHints}.
	 * <p>
	 * The values of the specified {@code hintsColumn} get automatically
	 * converted to type String.
	 * </p>
	 * 
	 * @param comp
	 *            the {@link JTextComponent} to be decorated with this
	 *            {@link XdevIntelliHints}
	 * @param hintsColumn
	 *            a {@link VirtualTableColumn} containing the hints
	 */
	@SuppressWarnings({"unchecked"})
	public XdevIntelliHints(JTextComponent comp, final VirtualTableColumn hintsColumn)
	{
		this(comp,hintsColumn,false);
	}
	
	
	/**
	 * Initializes a new instance of {@link XdevIntelliHints}.
	 * <p>
	 * The values of the specified {@code hintsColumn} get automatically
	 * converted to type String.
	 * </p>
	 * 
	 * @param comp
	 *            the {@link JTextComponent} to be decorated with this
	 *            {@link XdevIntelliHints}
	 * @param hintsColumn
	 *            a {@link VirtualTableColumn} containing the hints
	 * @param queryData
	 *            if {@code true} a query on the {@code hintsTable} gets
	 *            performed.
	 */
	@SuppressWarnings({"unchecked"})
	public XdevIntelliHints(JTextComponent comp, final VirtualTableColumn hintsColumn,
			boolean queryData)
	{
		this(comp,hintsColumn.getVirtualTable(),hintsColumn.getName(),queryData);
	}
	
	
	/**
	 * Initializes a new instance of {@link XdevIntelliHints}.
	 * <p>
	 * The values of the specified hintsColumn} get automatically converted to
	 * type String.
	 * </p>
	 * 
	 * @param comp
	 *            the {@link JTextComponent} to be decorated with this
	 *            {@link XdevIntelliHints}
	 * @param hintsTable
	 *            a {@link VirtualTable} of which a column should be used for
	 *            hints
	 * @param columns
	 *            the columns to use for the hints, for format description see
	 *            {@link StringUtils#format(String, xdev.util.StringUtils.ParameterProvider)}
	 */
	public XdevIntelliHints(JTextComponent comp, final VirtualTable hintsTable, final String columns)
	{
		this(comp,hintsTable,columns,false);
	}
	
	
	/**
	 * Initializes a new instance of {@link XdevIntelliHints}.
	 * <p>
	 * The values of the specified hintsColumn} get automatically converted to
	 * type String.
	 * </p>
	 * 
	 * @param comp
	 *            the {@link JTextComponent} to be decorated with this
	 *            {@link XdevIntelliHints}
	 * @param hintsTable
	 *            a {@link VirtualTable} of which a column should be used for
	 *            hints
	 * @param columns
	 *            the columns to use for the hints, for format description see
	 *            {@link StringUtils#format(String, xdev.util.StringUtils.ParameterProvider)}
	 * @param queryData
	 *            if {@code true} a query on the {@code hintsTable} gets
	 *            performed.
	 */
	public XdevIntelliHints(JTextComponent comp, final VirtualTable hintsTable,
			final String columns, boolean queryData)
	{
		super(comp,new String[0]);
		
		if(hintsTable != null)
		{
			this.textComponent = comp;
			
			if(queryData)
			{
				try
				{
					hintsTable.queryAndFill();
				}
				catch(Exception e)
				{
					log.error(e);
				}
			}
			
			setCompletionList(hintsTable,columns);
			
			this.hintsTable = hintsTable;
			this.hintsTableListener = new VirtualTableListener()
			{
				@Override
				public void virtualTableDataChanged(VirtualTableEvent event)
				{
					sync();
				}
				
				
				@Override
				public void virtualTableRowDeleted(VirtualTableEvent event)
				{
					sync();
				}
				
				
				@Override
				public void virtualTableRowInserted(VirtualTableEvent event)
				{
					sync();
				}
				
				
				@Override
				public void virtualTableRowUpdated(VirtualTableEvent event)
				{
					sync();
				}
				
				
				@Override
				public void virtualTableStructureChanged(VirtualTableEvent event)
				{
					sync();
				}
				
				
				void sync()
				{
					setCompletionList(hintsTable,columns);
				}
			};
			hintsTable.addVirtualTableListener(hintsTableListener);
			
			this.setShowHintsKeyStroke(DEFAULT_HINT_KEYSTROKE);
		}
	}
	
	
	/**
	 * Sets a custom KeyStroke to be used for showing the hints popup.
	 * 
	 * @param keyStroke
	 *            a {@link KeyStroke}
	 * @see XdevIntelliHints#DEFAULT_HINT_KEYSTROKE
	 */
	public void setShowHintsKeyStroke(KeyStroke keyStroke)
	{
		KeyStroke[] all = getAllShowHintsKeyStrokes();
		if(all != null)
		{
			for(KeyStroke ks : all)
			{
				removeShowHintsKeyStroke(ks);
			}
		}
		
		addShowHintsKeyStroke(keyStroke);
	}
	
	
	/**
	 * Sets the hints of this {@link XdevIntelliHints}.
	 * <p>
	 * The values of the specified {@code hintsColumn} get automatically
	 * converted to type String.
	 * </p>
	 * 
	 * @param hintsColumn
	 *            a {@link VirtualTableColumn} containing the hints
	 */
	@SuppressWarnings({"rawtypes"})
	public void setCompletionList(VirtualTableColumn hintsColumn)
	{
		this.setCompletionList(hintsColumn.getVirtualTable(),hintsColumn.getName());
	}
	
	
	/**
	 * Sets the hints of this {@link XdevIntelliHints}.
	 * 
	 * @param hintsTable
	 *            a {@link VirtualTable} of which a column should be used for
	 *            hints
	 * @param hintsColumnIndex
	 *            the index of the column to use for the hints
	 */
	public void setCompletionList(VirtualTable hintsTable, int hintsColumnIndex)
	{
		this.setCompletionList(hintsTable,hintsTable.getColumnAt(hintsColumnIndex).getName());
	}
	
	
	/**
	 * Sets the hints of this {@link XdevIntelliHints}.
	 * 
	 * @param hintsTable
	 *            a {@link VirtualTable} of which a column should be used for
	 *            hints
	 * @param columns
	 *            the columns to use for the hints, for format description see
	 *            {@link StringUtils#format(String, xdev.util.StringUtils.ParameterProvider)}
	 */
	public void setCompletionList(VirtualTable hintsTable, String columns)
	{
		super.setCompletionList(getHints(hintsTable,columns));
	}
	
	
	/**
	 * Sorts the underlying hint list, if possible.
	 */
	public void sortCompletionList()
	{
		try
		{
			Collections.sort(this.getCompletionList());
		}
		catch(Exception e)
		{
			// fail silently, if sorting is not possible, e.g. because of
			// unmodifiable list
		}
	}
	
	
	/**
	 * Returns a {@code List} of {@code Strings} for the values of all rows of a
	 * specified column.
	 * 
	 * @param vt
	 *            a {@link VirtualTable} to get the values from
	 * @param column
	 *            the column name
	 * @return a {@code List} of {@code Strings}
	 */
	private static List<String> getHints(VirtualTable vt, String column)
	{
		Set<String> hints = new LinkedHashSet<String>();
		
		int columnIndex = vt.getColumnIndex(column);
		int rowCount = vt.getRowCount();
		for(int row = 0; row < rowCount; row++)
		{
			if(columnIndex >= 0)
			{
				hints.add(vt.getFormattedValueAt(row,columnIndex));
			}
			else
			{
				hints.add(vt.format(row,column));
			}
		}
		return new ArrayList<String>(hints);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Rectangle getCaretRectangleForPopup(int caretPosition) throws BadLocationException
	{
		Rectangle r = super.getCaretRectangleForPopup(caretPosition);
		// align the x coordinate of the hints popup with the text component
		r.x = 0;
		return r;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public JComponent createHintsComponent()
	{
		JComponent hintsComponent = super.createHintsComponent();
		
		// Customize hints component to have the same width as the related
		// textfield and have configurable nr of entries (#11992)
		getList().setVisibleRowCount(visibleRowCount);
		@SuppressWarnings("serial")
		JPanel pnl = new JPanel(new BorderLayout())
		{
			@Override
			public Dimension getPreferredSize()
			{
				Dimension d = super.getPreferredSize();
				d.width = Math.max(d.width,textComponent.getWidth() - 2);
				return d;
			}
		};
		pnl.add(hintsComponent,BorderLayout.CENTER);
		return pnl;
	}
	
	
	/**
	 * @return the count of concurrently visual entries in the hints popup
	 */
	public int getVisibleRowCount()
	{
		return visibleRowCount;
	}
	
	
	/**
	 * Sets the number of concurrently visible entries in the hints popup.
	 * 
	 * @param visibleRowCount
	 *            an int value
	 */
	public void setVisibleRowCount(int visibleRowCount)
	{
		this.visibleRowCount = visibleRowCount;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void finalize() throws Throwable
	{
		if(hintsTable != null && hintsTableListener != null)
		{
			hintsTable.removeVirtualTableListener(hintsTableListener);
		}
		
		super.finalize();
	}
}
