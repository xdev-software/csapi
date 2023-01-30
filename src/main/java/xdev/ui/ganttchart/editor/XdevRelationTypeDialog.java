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
package xdev.ui.ganttchart.editor;


import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;

import xdev.ui.XdevWindow;
import xdev.ui.ganttchart.GanttResourceBundle;
import xdev.ui.ganttchart.template.GanttTemplate;

import com.jidesoft.gantt.GanttEntryRelation;


/**
 * The dialog which is shown if the user is in a runtime relation creation
 * action.
 * 
 * <p>
 * The relation can be triggered via UI action in the {@link GanttTemplate} s
 * toolbar.
 * </p>
 * 
 * @author XDEV Software jwill
 * @since 4.0
 * 
 * @see XdevGanttTemplateToolBar
 * 
 */
public class XdevRelationTypeDialog extends XdevWindow
{
	/**
	 * the serialization id.
	 */
	private static final long				serialVersionUID	= -7032639998026986621L;
	
	/**
	 * the relation type to use.
	 */
	private Integer							relationType		= null;
	
	private static XdevRelationTypeDialog	instance			= null;
	
	
	public static XdevRelationTypeDialog getInstance()
	{
		if(instance != null)
		{
			return instance;
		}
		instance = new XdevRelationTypeDialog();
		return instance;
	}
	
	
	/**
	 * 
	 */
	private XdevRelationTypeDialog()
	{
		this.init();
	}
	
	
	/**
	 * UI initialization.
	 */
	private void init()
	{
		this.setTitle(GanttResourceBundle.getString("relationTypeDialog.title"));
		
		this.setBorder(BorderFactory.createEmptyBorder(10,5,5,5));
		this.setLayout(new FlowLayout());
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent event)
			{
				close();
			}
		});
		
		this.initButtons();
	}
	
	
	/**
	 * UI initialization.
	 */
	private void initButtons()
	{
		JButton finishToStart = new JButton();
		finishToStart.setIcon(GanttResourceBundle.loadResIcon("finish_to_start.png"));
		finishToStart.setToolTipText("Finish to start relation");
		finishToStart.addActionListener(new RelationListener(
				GanttEntryRelation.ENTRY_RELATION_FINISH_TO_START));
		
		JButton startToStart = new JButton();
		startToStart.setIcon(GanttResourceBundle.loadResIcon("start_to_start.png"));
		startToStart.setToolTipText("Start to start relation");
		startToStart.addActionListener(new RelationListener(
				GanttEntryRelation.ENTRY_RELATION_START_TO_START));
		
		JButton finishToFinish = new JButton();
		finishToFinish.setIcon(GanttResourceBundle.loadResIcon("finish_to_finish.png"));
		finishToFinish.setToolTipText("Finish to finish relation");
		finishToFinish.addActionListener(new RelationListener(
				GanttEntryRelation.ENTRY_RELATION_FINISH_TO_FINISH));
		
		JButton startToFinish = new JButton();
		startToFinish.setIcon(GanttResourceBundle.loadResIcon("start_to_finish.png"));
		startToFinish.setToolTipText("Start to finish relation");
		startToFinish.addActionListener(new RelationListener(
				GanttEntryRelation.ENTRY_RELATION_START_TO_FINISH));
		
		this.setPreferredSize(new Dimension(370,60));
		
		this.add(finishToStart);
		this.add(startToStart);
		this.add(finishToFinish);
		this.add(startToFinish);
	}
	
	
	/**
	 * Returns the currently selected relation type.
	 * 
	 * <p>
	 * <b>Relationtypes can be:</b>
	 * <ul>
	 * <li>start to start</li>
	 * <li>start to finish</li>
	 * <li>finish to finish</li>
	 * <li>finish to start</li>
	 * </ul>
	 * </p>
	 * 
	 * @return the selected relation type.
	 */
	public Integer getSelectedRelationType()
	{
		return this.relationType;
	}
	
	
	
	/**
	 * The action used from the relation type buttons in this dialog, to select
	 * the relation type.
	 * 
	 * @author XDEV Software JWill
	 */
	private class RelationListener implements ActionListener
	{
		/**
		 * the relation type to use.
		 */
		private int	relationType;
		
		
		/**
		 * @param relationType
		 *            the currently selected relation type.
		 */
		private RelationListener(int relationType)
		{
			this.relationType = relationType;
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void actionPerformed(ActionEvent e)
		{
			XdevRelationTypeDialog.this.relationType = this.relationType;
			XdevRelationTypeDialog.this.close();
		}
		
	}
}
