package xdev.ui.listbox;

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


import javax.swing.ListModel;

import com.jidesoft.list.ListModelWrapperUtils;


public class CSListModelWrapperLookup extends DefaultListModelWrapperLookup
{
	@Override
	public ListModel lookupListModel(ListModel outerModel, Class innerModelClass)
	{
		return ListModelWrapperUtils.getActualListModel(outerModel,innerModelClass);
	}
}
