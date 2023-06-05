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


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.SwingConstants;

import com.jidesoft.gantt.GanttEntry;

import xdev.lang.EventHandlerDelegate;
import xdev.ui.GBC;
import xdev.ui.XComponent;
import xdev.ui.XdevButton;
import xdev.ui.XdevContainer;
import xdev.ui.XdevDateTextField;
import xdev.ui.XdevFormular;
import xdev.ui.XdevLabel;
import xdev.ui.XdevTextField;
import xdev.ui.XdevWindow;
import xdev.ui.ganttchart.GanttResourceBundle;
import xdev.ui.ganttchart.UpdateableGanttEntry;
import xdev.ui.ganttchart.model.XdevGanttEntryVTMappings;
import xdev.ui.ganttchart.utils.DateGanttEntryColumnType;
import xdev.ui.ganttchart.utils.VTDateGanttMappingConverter;
import xdev.util.XdevDate;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTable.VirtualTableRow;
import xdev.vt.VirtualTableColumn;


/**
 * Default form for altering or inserting a {@link GanttEntry}.
 * 
 * @author XDEV Software jwill
 * @since 4.0
 */
public class GanttDateDefaultForm extends XdevWindow
{
	/**
	 * 
	 */
	private static final long						serialVersionUID	= 8790299288225996902L;
	private final VirtualTable						vt;
	private final XdevGanttEntryVTMappings<Date>	dataContainer;
	private UpdateableGanttEntry<Date>				rootEntry			= null;
	private boolean									dbSync				= false;
	
	
	public GanttDateDefaultForm(final Map<DateGanttEntryColumnType, VirtualTableColumn<?>> mapping,
			VirtualTable vt, boolean dbSync)
	{
		this.vt = vt;
		this.dataContainer = VTDateGanttMappingConverter.getEntryColumnMappings(mapping,this.vt);
		this.dbSync = dbSync;
		this.initUI(this.vt);
	}
	
	
	public GanttDateDefaultForm(final Map<DateGanttEntryColumnType, VirtualTableColumn<?>> mapping,
			VirtualTableRow selectedRow, boolean dbSync)
	{
		this.vt = selectedRow.getVirtualTable();
		this.dataContainer = VTDateGanttMappingConverter.getEntryColumnMappings(mapping,vt);
		this.dbSync = dbSync;
		this.initUI(this.vt);
		
		this.formular.setModel(selectedRow);
	}
	
	
	public GanttDateDefaultForm(final Map<DateGanttEntryColumnType, VirtualTableColumn<?>> mapping,
			UpdateableGanttEntry<Date> rootEntry, VirtualTable vt, boolean dbSync)
	{
		this.vt = vt;
		this.rootEntry = rootEntry;
		this.dbSync = dbSync;
		this.dataContainer = VTDateGanttMappingConverter.getEntryColumnMappings(mapping,this.vt);
		this.initUI(this.vt);
	}
	
	
	@EventHandlerDelegate
	void this_windowClosing(WindowEvent event)
	{
		close();
	}
	
	
	@EventHandlerDelegate
	void cmdNew_actionPerformed(ActionEvent event)
	{
		formular.reset(this.vt);
	}
	
	
	@EventHandlerDelegate
	void cmdSave_actionPerformed(ActionEvent event)
	{
		if(formular.verifyFormularComponents())
		{
			try
			{
				formular.save(dbSync);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		close();
	}
	
	
	@EventHandlerDelegate
	void this_init()
	{
		// this.endDateTextField.setDate(new XdevDate());
		// this.startDateTextField.setDate(new XdevDate());
		if(this.rootEntry != null)
		{
			textField2.setText(this.rootEntry.getId().toString());
			startDateTextField.setDate(new XdevDate(this.rootEntry.getRange().lower()));
			endDateTextField.setDate(new XdevDate(this.rootEntry.getRange().upper()));
		}
		else
		{
			// "default" insert mode
			startDateTextField.setDate(new XdevDate());
			endDateTextField.setDate(new XdevDate());
		}
	}
	
	private XdevDateTextField		startDateTextField, endDateTextField;
	private XdevLabel				label2, label3, label4, label, label5;
	private GanttPercentageSpinner	spinner;
	private XdevFormular			formular;
	private XdevContainer			container2, container;
	private XdevButton				cmdNew, cmdSave;
	private XdevTextField			textField, textField2;
	
	
	private void initUI(VirtualTable vt)
	
	{
		formular = new XdevFormular();
		label2 = new XdevLabel();
		textField = new XdevTextField();
		label3 = new XdevLabel();
		startDateTextField = new XdevDateTextField();
		label4 = new XdevLabel();
		endDateTextField = new XdevDateTextField();
		label = new XdevLabel();
		container2 = new XdevContainer();
		spinner = new GanttPercentageSpinner();
		label5 = new XdevLabel();
		textField2 = new XdevTextField();
		container = new XdevContainer();
		cmdNew = new XdevButton();
		cmdSave = new XdevButton();
		
		this.setBorder(BorderFactory.createEmptyBorder(5,5,0,5));
		this.setPreferredSize(new Dimension(498,195));
		label2.setText(dataContainer.getDescription().getCaption() + ":");
		label2.setName("label2");
		textField.setTabIndex(1);
		textField.setDataField(dataContainer.getDescription().getName());
		textField.setHorizontalAlignment(SwingConstants.LEFT);
		textField.setMaxSignCount(250);
		label3.setText(dataContainer.getStart().getCaption() + ":");
		label3.setName("label3");
		startDateTextField.setTabIndex(2);
		startDateTextField.setTextFormat(this.dataContainer.getStart().getTextFormat());
		startDateTextField.setDataField(dataContainer.getStart().getName());
		startDateTextField.setHorizontalAlignment(SwingConstants.LEFT);
		startDateTextField.setName("dateTextField2");
		label4.setText(dataContainer.getEnd().getCaption() + ":");
		label4.setName("label4");
		endDateTextField.setTabIndex(3);
		endDateTextField.setTextFormat(this.dataContainer.getEnd().getTextFormat());
		endDateTextField.setDataField(dataContainer.getEnd().getName());
		endDateTextField.setHorizontalAlignment(SwingConstants.LEFT);
		label.setText(dataContainer.getCompletion().getCaption() + ":");
		spinner.setTabIndex(11);
		spinner.setDataField(dataContainer.getCompletion().getName());
		label5.setText("%");
		label5.setBackground(Color.WHITE);
		textField2.setTabIndex(12);
		textField2.setVisible(false);
		textField2.setDataField(dataContainer.getRoot().getName());
		cmdNew.setTabIndex(9);
		cmdNew.setText(GanttResourceBundle.getString("ganttDateDefaultForm.new"));
		cmdSave.setTabIndex(7);
		cmdSave.setText(GanttResourceBundle.getString("ganttDateDefaultForm.save"));
		
		label2.saveState();
		textField.saveState();
		label3.saveState();
		startDateTextField.saveState();
		label4.saveState();
		endDateTextField.saveState();
		label.saveState();
		spinner.saveState();
		label5.saveState();
		textField2.saveState();
		
		container2.setLayout(new BoxLayout(container2,BoxLayout.LINE_AXIS));
		container2.add(spinner);
		container2.add(label5);
		container.setLayout(new FlowLayout(FlowLayout.CENTER,3,3));
		container.add(cmdNew);
		container.add(cmdSave);
		formular.setLayout(new GridBagLayout());
		formular.add(label2,new GBC(1,1,1,1,0.0,0.0,GBC.BASELINE_LEADING,GBC.NONE,new Insets(3,3,3,
				3),0,0));
		formular.add(textField,new GBC(2,1,1,1,1.0,0.0,GBC.BASELINE_LEADING,GBC.HORIZONTAL,
				new Insets(3,3,3,3),0,0));
		formular.add(label3,new GBC(1,2,1,1,0.0,0.0,GBC.BASELINE_LEADING,GBC.NONE,new Insets(3,3,3,
				3),0,0));
		XComponent dateTextField2_carrier = startDateTextField.createPanel();
		formular.add(dateTextField2_carrier,new GBC(2,2,1,1,1.0,0.0,GBC.BASELINE_LEADING,
				GBC.HORIZONTAL,new Insets(3,3,3,3),0,0));
		formular.add(label4,new GBC(1,3,1,1,0.0,0.0,GBC.BASELINE_LEADING,GBC.NONE,new Insets(3,3,3,
				3),0,0));
		XComponent dateTextField_carrier = endDateTextField.createPanel();
		formular.add(dateTextField_carrier,new GBC(2,3,1,1,1.0,0.0,GBC.BASELINE_LEADING,
				GBC.HORIZONTAL,new Insets(3,3,3,3),0,0));
		formular.add(label,new GBC(1,4,1,1,0.0,0.0,GBC.BASELINE_LEADING,GBC.NONE,
				new Insets(3,3,3,3),0,0));
		formular.add(container2,new GBC(2,4,1,1,0.1,0.0,GBC.WEST,GBC.BOTH,new Insets(3,3,3,3),0,0));
		formular.add(textField2,new GBC(2,5,1,1,0.0,0.0,GBC.WEST,GBC.HORIZONTAL,
				new Insets(3,3,3,3),0,0));
		formular.add(container,new GBC(1,6,2,1,1.0,0.0,GBC.CENTER,GBC.HORIZONTAL,
				new Insets(3,3,3,3),0,0));
		GBC.addSpacer(formular,true,true);
		this.setLayout(new BorderLayout());
		this.add(formular,BorderLayout.CENTER);
		this.formular.setModel(vt);
		this.setTitle(GanttResourceBundle.getString("ganttDateDefaultForm.title"));
		this.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent event)
			{
				this_windowClosing(event);
			}
		});
		cmdNew.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				cmdNew_actionPerformed(event);
			}
		});
		cmdSave.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				cmdSave_actionPerformed(event);
			}
		});
		this_init();
	}
}
