package xdev.ui.ganttchart.action;

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


import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import xdev.db.DBException;
import xdev.ui.UIUtils;
import xdev.ui.UIUtils.MessageDialogType;
import xdev.ui.ganttchart.template.XdevGanttTemplate;
import xdev.util.res.ResourceUtils;
import xdev.vt.VirtualTableException;


/**
 * 
 * @author XDEV Software jwill
 * @since 4.0
 */
public class GanttTemplateRemoveKeyAdapter extends KeyAdapter
{
	private final XdevGanttTemplate	template;
	
	
	public GanttTemplateRemoveKeyAdapter(XdevGanttTemplate template)
	{
		this.template = template;
	}
	
	
	public void keyPressed(KeyEvent e)
	{
		if(e.getKeyChar() == '\u007F')
		{
			List<String> s = new ArrayList<String>();
			Collections.addAll(s,
					ResourceUtils.getResourceString("Y",GanttTemplateRemoveKeyAdapter.class),
					ResourceUtils.getResourceString("N",GanttTemplateRemoveKeyAdapter.class));
			Object confirmation = UIUtils.showConfirmMessage(
					ResourceUtils.getResourceString("Title",GanttTemplateRemoveKeyAdapter.class),
					ResourceUtils.getResourceString("Message",GanttTemplateRemoveKeyAdapter.class),
					s,0,MessageDialogType.WARNING_MESSAGE);
			if(confirmation != null)
			{
				if(confirmation.equals(ResourceUtils.getResourceString("Y",
						GanttTemplateRemoveKeyAdapter.class)))
				{
					try
					{
						template.getSelectedVirtualTableRow().delete(
								template.isPersistenceEnabled());
					}
					catch(VirtualTableException e1)
					{
						e1.printStackTrace();
					}
					catch(DBException e1)
					{
						e1.printStackTrace();
					}
				}
			}
		}
	}
}
