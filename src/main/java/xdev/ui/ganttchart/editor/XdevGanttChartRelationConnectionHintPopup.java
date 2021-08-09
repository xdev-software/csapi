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


import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.atomic.AtomicBoolean;

import xdev.lang.EventHandlerDelegate;
import xdev.ui.XdevButton;
import xdev.ui.XdevCheckBox;
import xdev.ui.XdevContainer;
import xdev.ui.XdevLabel;
import xdev.ui.ganttchart.GanttResourceBundle;

import com.jidesoft.gantt.GanttEntryRelation;
import com.jidesoft.popup.JidePopup;


/**
 * Tutorial dialog which provides several hints how to create
 * {@link GanttEntryRelation}s via UI - commands.
 * 
 * @author XDEV Software jwill
 * @since 4.0
 */
public class XdevGanttChartRelationConnectionHintPopup extends JidePopup
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 4726209956033418675L;
	
	/**
	 * the close/ok <code>Button</code>.
	 */
	private XdevButton			button;
	
	/**
	 * the descripton <code>Label</code>.
	 */
	private XdevLabel			label;
	
	/**
	 * the 'show hint again' <code>Combobox</code>.
	 */
	private XdevCheckBox		checkBox;
	
	/**
	 * the root container.
	 */
	private XdevContainer		container;
	
	/**
	 * indicates wether this information dialog is shown again.
	 */
	private AtomicBoolean		showHint;
	
	/**
	 * the dialog owner <code>Component</code>.
	 */
	private Component			owner;
	
	
	// use AtomicBoolean here - because the Popup does not want to know any
	// additional references.
	/**
	 * 
	 * @param owner
	 *            the dialog owner <code>Component</code>.
	 * @param showHint
	 *            the <code>boolean</code> which indicates wether this
	 *            information dialog is shown again.
	 */
	public XdevGanttChartRelationConnectionHintPopup(Component owner, AtomicBoolean showHint)
	{
		super();
		
		this.showHint = showHint;
		this.owner = owner;
		
		this.init();
	}
	
	
	/**
	 * UI initialization.
	 */
	private void init()
	{
		this.setPreferredSize(new Dimension(430,130));
		this.setMovable(true);
		this.setOwner(this.owner);
		this.setLayout(new BorderLayout(0,0));
		
		this.initGUI();
	}
	
	
	/**
	 * UI initialization.
	 */
	private void initGUI()
	{
		label = new XdevLabel();
		label.setText(GanttResourceBundle.getString("connectionHint.hint"));
		
		container = new XdevContainer();
		
		button = new XdevButton();
		button.setName("button2");
		button.setTabIndex(5);
		button.setText("OK");
		
		checkBox = new XdevCheckBox();
		checkBox.setName("checkBox2");
		checkBox.setTabIndex(4);
		checkBox.setText(GanttResourceBundle.getString("connectionHint.question"));
		checkBox.setReturnValue(true,"true");
		checkBox.setReturnValue(false,"false");
		checkBox.setFormularValueList("true,yes,on");
		
		button.setBounds(340,10,85,33);
		container.add(button);
		checkBox.setBounds(8,15,197,23);
		container.add(checkBox);
		
		label.setPreferredSize(new Dimension(430,100));
		this.add(label,BorderLayout.CENTER);
		container.setPreferredSize(new Dimension(300,50));
		container.setPreferredSize(new Dimension(430,50));
		this.add(container,BorderLayout.SOUTH);
		
		label.saveState();
		checkBox.saveState();
		
		checkBox.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				checkBox_stateChanged(event);
			}
		});
		
		button.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				button_actionPerformed(event);
			}
		});
	}
	
	
	/**
	 * Showhint checkbox state changed delegate.
	 * 
	 * @param event
	 *            the <code>ActionEvent</code> that indicates wether the hint
	 *            should be shown or not.
	 */
	@EventHandlerDelegate
	void checkBox_stateChanged(ActionEvent event)
	{
		this.showHint.set(!checkBox.isSelected());
	}
	
	
	/**
	 * The close button delegate.
	 * 
	 * @param event
	 *            the <code>ActionEvent</code> that performs the close window
	 *            action.
	 */
	@EventHandlerDelegate
	void button_actionPerformed(ActionEvent event)
	{
		this.hidePopupImmediately();
	}
}
