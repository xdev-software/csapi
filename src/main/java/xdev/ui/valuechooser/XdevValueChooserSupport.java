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
package xdev.ui.valuechooser;


import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTable;

import xdev.lang.NotNull;
import xdev.ui.table.ExtendedTable;
import xdev.ui.table.TableSelectionHelper;


/**
 * This class provides common functionality for different implementations of
 * type {@link XdevValueChooser}.
 * 
 * @since CS 1.0
 * @author XDEV Software (RHHF)
 * 
 * @param <T>
 *            the type of the {@link ValueChooser} that should be supported
 */
public class XdevValueChooserSupport<T extends XdevValueChooser<?>>
{
	
	// private final T valueChooser;
	//
	//
	// public XdevValueChooserSupport(final T valueChooser)
	// {
	// this.valueChooser = valueChooser;
	// }
	
	/**
	 * Enables full keyboard support.
	 * 
	 * @since CS 1.0
	 * 
	 * @author XDEV Software
	 */
	static class SearchFieldListener implements KeyListener
	{
		/**
		 * the {@link ValueChooser} to operate on.
		 */
		final XdevValueChooser<?>	valueChooser;
		
		/**
		 * the table to navigate on.
		 */
		final JTable				table;
		
		/**
		 * {@link TableSelectionHelper} instance.
		 */
		final TableSelectionHelper	tableSelectionHandler;
		

		/**
		 * Creates a new {@link SearchFieldListener}.
		 * 
		 * @param valueChooser
		 *            {@link XdevValueChooser} to operate on.
		 * 
		 */
		public SearchFieldListener(final @NotNull XdevValueChooser<?> valueChooser)
		{
			if(valueChooser == null)
			{
				throw new IllegalArgumentException("valueChooser must not be null");
			}
			this.valueChooser = valueChooser;
			
			this.table = valueChooser.getTable();
			this.tableSelectionHandler = new TableSelectionHelper(table);
		}
		

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void keyTyped(KeyEvent e)
		{
			// empty - no business here
		}
		

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void keyReleased(KeyEvent e)
		{
			// empty - no business here
		}
		

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void keyPressed(KeyEvent e)
		{
			if(e.getKeyCode() == KeyEvent.VK_UP)
			{
				this.tableSelectionHandler.selectPreviousRow();
				// scroll to the selected row
				if(this.table instanceof ExtendedTable)
				{
					((ExtendedTable)this.table).ensureRowIsVisible(this.table.getSelectedRow());
				}
				
			}
			if(e.getKeyCode() == KeyEvent.VK_DOWN)
			{
				this.tableSelectionHandler.selectNextRow();
				// scroll to the selected row
				if(this.table instanceof ExtendedTable)
				{
					((ExtendedTable)this.table).ensureRowIsVisible(this.table.getSelectedRow());
				}
			}
			
			if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
			{
				// abort
				this.valueChooser.setVisible(false);
			}
			
		}
	}
	
}
