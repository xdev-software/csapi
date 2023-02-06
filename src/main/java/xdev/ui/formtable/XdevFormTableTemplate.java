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
package xdev.ui.formtable;


import java.awt.Dimension;
import java.awt.Rectangle;
import java.beans.Beans;

import xdev.db.DBException;
import xdev.ui.BeanProperty;
import xdev.ui.DefaultBeanCategories;
import xdev.ui.XdevFormular;
import xdev.ui.XdevStyle;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTable.VirtualTableRow;
import xdev.vt.VirtualTableException;


/**
 * {@link XdevFormTableTemplate} serves as an editor / renderer for a
 * {@link XdevFormTable}. To implement a concrete form inherit from this class.
 * 
 * @author XDEV Software
 * @since 4.0
 */
public class XdevFormTableTemplate extends XdevFormular implements FormTableTemplate
{
	
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -6223152492111492727L;
	/**
	 * If <code>true</code> all changes are immediately synchronized with the
	 * database; otherwise you have synchronize them manually.
	 */
	@BeanProperty(category = DefaultBeanCategories.DATA)
	private boolean				synchronizeDB		= false;
	
	
	/**
	 * Create a new instance of {@link XdevFormTableTemplate}.
	 * 
	 */
	public XdevFormTableTemplate()
	{
		setBackgroundType(XdevStyle.COLOR);
		setOpaque(true);
		// Default size
		setSize(new Dimension(100,100));
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onEditorClose()
	{
		try
		{
			this.save(isSynchronizeDB());
		}
		catch(final VirtualTableException e)
		{
			handleEditorCloseException(e);
		}
		catch(final DBException e)
		{
			handleEditorCloseException(e);
		}
	}
	
	
	/**
	 * occurs an error during the editor is closed, the error can be handled in
	 * this method
	 * 
	 * @param e the {@link Exception}
	 */
	protected void handleEditorCloseException(final Exception e)
	{
		throw new RuntimeException(e);
	}
	
	
	/**
	 * Fills this {@link XdevFormTableTemplate} / the mapped components in the
	 * XdevFormTableTemplate with the values provided by the specified
	 * {@link VirtualTableRow}.
	 * 
	 * @param o
	 *            the {@link VirtualTableRow} to take the data from.
	 */
	@Override
	public void setValue(final VirtualTableRow o)
	{
		if(o == null)
		{
			final VirtualTable vt = getVirtualTable();
			if(vt != null)
			{
				this.reset(vt);
			}
			else
			{
				this.reset();
			}
		}
		else
		{
			this.setModel(o);
		}
	}
	
	
	/**
	 * Sets whether values of this editor will be written to the database or
	 * not.
	 * 
	 * @param synchronizeDB
	 *            if <code>true</code> the values of this editor will be written
	 *            to the database; otherwise they will not.
	 */
	@Override
	public void setSynchronizeDB(final boolean synchronizeDB)
	{
		this.synchronizeDB = synchronizeDB;
	}
	
	
	/**
	 * Determines whether values of this editor will be written to the database
	 * or not.
	 * 
	 * @return <code>true</code> if the values of this editor will be written to
	 *         the database; otherwise <code>false</code>
	 * @see #setSynchronizeDB(boolean)
	 */
	@Override
	public boolean isSynchronizeDB()
	{
		return synchronizeDB;
	}
	
	
	/*
	 * The following methods are overridden as a performance measure to to prune
	 * code-paths are often called in the case of renders but which we know are
	 * unnecessary. Great care should be taken when writing your own renderer to
	 * weigh the benefits and drawbacks of overriding methods like these.
	 */
	
	/**
	 * Overridden for performance reasons. See the <a
	 * href="#override">Implementation Note</a> for more information.
	 */
	@Override
	public void repaint(final long tm, final int x, final int y, final int width, final int height)
	{
		if(Beans.isDesignTime())
		{
			super.repaint(tm,x,y,width,height);
		}
	}
	
	
	/**
	 * Overridden for performance reasons. See the <a
	 * href="#override">Implementation Note</a> for more information.
	 */
	@Override
	public void repaint(final Rectangle r)
	{
		if(Beans.isDesignTime())
		{
			super.repaint(r);
		}
	}
	
	
	/**
	 * Overridden for performance reasons. See the <a
	 * href="#override">Implementation Note</a> for more information.
	 * 
	 * @since 1.5
	 */
	@Override
	public void repaint()
	{
		if(Beans.isDesignTime())
		{
			super.repaint();
		}
	}
	
	
	/**
	 * Overridden for performance reasons. See the <a
	 * href="#override">Implementation Note</a> for more information.
	 */
	@Override
	protected void firePropertyChange(final String propertyName, final Object oldValue,
			final Object newValue)
	{
		// Strings get interned...
		if(Beans.isDesignTime()
				|| propertyName == "text"
				|| propertyName == "labelFor"
				|| propertyName == "displayedMnemonic"
				|| ((propertyName == "font" || propertyName == "foreground")
						&& oldValue != newValue && getClientProperty(javax.swing.plaf.basic.BasicHTML.propertyKey) != null))
		{
			
			super.firePropertyChange(propertyName,oldValue,newValue);
		}
	}
}
