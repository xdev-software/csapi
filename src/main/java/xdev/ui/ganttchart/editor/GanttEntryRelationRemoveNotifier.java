package xdev.ui.ganttchart.editor;

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


import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import xdev.ui.ganttchart.GanttResourceBundle;

import com.jidesoft.gantt.GanttEntryRelation;


/**
 * Notifier for {@link GanttEntryRelation} remove actions.
 * <p>
 * A yes/no action {@link JOptionPane} is added
 * </p>
 * .
 * 
 * @author XDEV Software jwill
 * @since 4.0
 */
public class GanttEntryRelationRemoveNotifier extends JDialog
{
	/**
	 * the {@link JOptionPane} which defines the applications possibilities.
	 */
	private final JOptionPane	optionPane;
	
	
	/**
	 * @return the optionPane
	 */
	public JOptionPane getOptionPane()
	{
		return optionPane;
	}
	
	/**
	 * the serialization id.
	 */
	private static final long	serialVersionUID	= -3175604141817727808L;
	
	
	/**
	 * Generates a notifier for {@link GanttEntryRelation} remove actions.
	 * <p>
	 * A yes/no {@link JOptionPane} is added
	 * </p>
	 * .
	 */
	public GanttEntryRelationRemoveNotifier()
	{
		super();
		
		optionPane = new JOptionPane(GanttResourceBundle.getString("removenotifier.hint"),
				JOptionPane.QUESTION_MESSAGE,JOptionPane.YES_NO_OPTION);
		
		this.init();
	}
	
	
	/**
	 * UI initialization.
	 */
	private void init()
	{
		this.setTitle(GanttResourceBundle.getString("removenotifier.title"));
		this.setContentPane(this.optionPane);
		this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		this.setModal(true);
		
		this.initOptionPaneListeners();
	}
	
	
	/**
	 * initialize {@link JOptionPane} listeners.
	 */
	private void initOptionPaneListeners()
	{
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent we)
			{
				setVisible(false);
			}
		});
		
		this.optionPane.addPropertyChangeListener(new PropertyChangeListener()
		{
			public void propertyChange(PropertyChangeEvent e)
			{
				String prop = e.getPropertyName();
				
				if(isVisible() && (e.getSource() == optionPane)
						&& (prop.equals(JOptionPane.VALUE_PROPERTY)))
				{
					setVisible(false);
				}
			}
		});
	}
}
