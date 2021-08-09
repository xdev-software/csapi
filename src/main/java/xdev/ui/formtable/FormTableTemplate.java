package xdev.ui.formtable;

/*-
 * #%L
 * XDEV Component Suite
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


import xdev.ui.componenttable.ComponentTableTemplate;
import xdev.vt.VirtualTable.VirtualTableRow;


/**
 * 
 * @author XDEV Software
 * @since 4.0
 */
public interface FormTableTemplate extends ComponentTableTemplate<VirtualTableRow>
{
	/**
	 * Sets whether values of this editor will be written to the database or
	 * not.
	 * 
	 * @param synchronizeDB
	 *            if <code>true</code> the values of this editor will be written
	 *            to the database; otherwise they will not.
	 */
	public void setSynchronizeDB(boolean synchronizeDB);
	
	
	/**
	 * Determines whether values of this editor will be written to the database
	 * or not.
	 * 
	 * @return <code>true</code> if the values of this editor will be written to
	 *         the database; otherwise <code>false</code>
	 * @see #setSynchronizeDB(boolean)
	 */
	public boolean isSynchronizeDB();
}
