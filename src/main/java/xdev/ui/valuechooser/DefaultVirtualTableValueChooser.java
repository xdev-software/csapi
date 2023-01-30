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


import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import xdev.lang.NotNull;
import xdev.ui.UIUtils;
import xdev.ui.XScrollPane;
import xdev.ui.XdevCSResourceBundle;
import xdev.ui.XdevQuickTableFilterField;
import xdev.ui.XdevSortableTable;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTable.VirtualTableRow;
import xdev.vt.VirtualTableModel;

import com.jidesoft.filter.FilterFactoryManager;
import com.jidesoft.grid.TableModelWrapperUtils;


/**
 * 
 * The {@link DefaultVirtualTableValueChooser} is a {@link ValueChooser}
 * implementation for {@link VirtualTableRow} selection on top of
 * {@link JDialog}. It provides the functionality to chose a row from a
 * {@link VirtualTable}.
 * 
 * 
 * @since CS 1.0
 * @author XDEV Software (RHHF)
 * 
 * @see ValueChooser
 * 
 */
public class DefaultVirtualTableValueChooser extends JDialog implements
		XdevValueChooser<VirtualTableRow>
{
	/**
	 * 
	 */
	private static final long			serialVersionUID			= 1L;
	
	/**
	 * name of value filter textfield (intended to be used by unit tests).
	 */
	static final String					VALUE_FILTER_NAME			= "ValueFilter";
	
	/**
	 * name of value table (intended to be used by unit tests).
	 */
	static final String					VALUE_TABLE_NAME			= "ValueTable";
	
	/**
	 * key for the valuechooser title.
	 */
	private static final String			KEY_TITLE					= "valuechooser.title";
	
	/**
	 * key for the valuechooser ok button.
	 */
	protected static final String		KEY_OK						= "valuechooser.ok";
	
	/**
	 * name of ok button (intended to be used by unit tests).
	 */
	protected static final String		DEFAULT_BUTTON_OK_NAME		= "OkButton";
	
	/**
	 * name of cancel button (intended to be used by unit tests).
	 */
	protected static final String		DEFAULT_BUTTON_CANCEL_NAME	= "CancelButton";
	
	/**
	 * key for the valuechooser cancel button.
	 */
	protected static final String		KEY_CANCEL					= "valuechooser.cancel";
	
	/**
	 * {@link VirtualTable} containing the values to chose from.
	 */
	private VirtualTable				virtualTable				= null;
	/**
	 * the filerFiled for this instance. Helps the user to find the desired
	 * value.
	 */
	private XdevQuickTableFilterField	filterField					= null;
	/**
	 * the table for this instance. Displays the available values.
	 */
	private XdevSortableTable			table						= null;
	
	/**
	 * a button for choosing the selection.
	 */
	private JButton						buttonOk;
	
	/**
	 * a button for canceling the selection.
	 */
	private JButton						buttonCancel;
	
	/**
	 * the chosen row.
	 */
	private VirtualTableRow				selectedValue				= null;
	
	/**
	 * the {@link ValueChooserField} that owns this {@link ValueChooser}.
	 */
	private ValueChooserField			owner						= null;
	
	/**
	 * initialization flag
	 */
	private boolean						initState					= false;
	
	
	/**
	 * Creates a new {@link DefaultVirtualTableValueChooser} using the provided
	 * {@link VirtualTable} as value pool.
	 * 
	 * @param virtualTable
	 *            {@link VirtualTable} containing the valid values for this
	 *            instance.
	 */
	public DefaultVirtualTableValueChooser(final @NotNull VirtualTable virtualTable)
	{
		this.setVirtualTable(virtualTable);
	}
	
	
	/**
	 * The default constructor is meant to be used for code generation within
	 * the XDEV IDE, which requires JavaBeans compliance for its visual beans.
	 * <p>
	 * If this constructor is used, a further call to {@link #setVirtualTable(VirtualTable)}
	 * must follow, prior to using the component.
	 * </p>
	 * For manual use of this class, it is recommended to call
	 * {@link #DefaultVirtualTableValueChooser(VirtualTable)} instead.
	 */
	public DefaultVirtualTableValueChooser()
	{
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setVirtualTable(final @NotNull VirtualTable virtualTable)
	{
		if(virtualTable == null)
		{
			throw new IllegalArgumentException("virtualTable must not be null");
		}
		
		// must be called after the VT has been set.
		if(this.virtualTable != null)
		{
			this.virtualTable = virtualTable;
			this.refreshUI();
		}
		else
		{
			this.virtualTable = virtualTable;
			this.initUI();
		}
		
		this.initState = true;
	}
	
	
	/**
	 * Refreshes the VT depending components.
	 */
	private void refreshUI()
	{
		this.filterField.setModel(this.virtualTable.createTableModel());
		this.table.setModel(this.filterField.getDisplayTableModel());
	}
	
	
	/**
	 * Initializes the component's UI part.
	 */
	private void initUI()
	{
		
		JPanel contentPane = new JPanel();
		
		contentPane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		
		this.setContentPane(contentPane);
		
		this.setModal(true);
		this.setTitle(XdevCSResourceBundle.getString(KEY_TITLE));
		this.setResizable(true);
		
		filterField = new XdevQuickTableFilterField(this.virtualTable.createTableModel());
		filterField.setTabIndex(1);
		filterField.setName(VALUE_FILTER_NAME);
		
		table = new XdevSortableTable();
		table.setTabIndex(2);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setTableHeaderPopupMenuEnabled(true);
		table.setName(VALUE_TABLE_NAME);
		// #13205
		table.getSelectionModel().setSelectionInterval(0,0);
		
		FilterFactoryManager filterFactoryManager = FilterFactoryManager.getDefaultInstance();
		filterFactoryManager.registerDefaultFilterFactories();
		
		table.setModel(filterField.getDisplayTableModel());
		
		table.setEditable(false);
		
		this.setLayout(new BorderLayout(10,10));
		JScrollPane _scrl_table = new XScrollPane(table,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.add(_scrl_table,BorderLayout.CENTER);
		this.add(filterField,BorderLayout.NORTH);
		
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent event)
			{
				DefaultVirtualTableValueChooser.this.setVisible(false);
			}
		});
		
		this.table.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				
				if(UIUtils.isDoubleClick(e))
				{
					performChooseAction();
				}
			}
			
		});
		
		this.filterField.getTextField().addKeyListener(
				new XdevValueChooserSupport.SearchFieldListener(this));
		this.filterField.getTextField().addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				DefaultVirtualTableValueChooser.this.chooseValue();
				DefaultVirtualTableValueChooser.this.setVisible(false);
			}
		});
		
		this.buttonOk = new JButton(XdevCSResourceBundle.getString(KEY_OK));
		buttonOk.setEnabled(false);
		buttonOk.setName(DEFAULT_BUTTON_OK_NAME);
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener()
		{
			public void valueChanged(ListSelectionEvent e)
			{
				if(table.getSelectedRowCount() == 1)
				{
					buttonOk.setEnabled(true);
				}
				else
				{
					buttonOk.setEnabled(false);
				}
			}
		});
		buttonOk.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if(table.getSelectedRowCount() == 1)
				{
					performChooseAction();
				}
			}
		});
		
		this.buttonCancel = new JButton(XdevCSResourceBundle.getString(KEY_CANCEL));
		this.buttonCancel.setName(DEFAULT_BUTTON_CANCEL_NAME);
		buttonCancel.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				DefaultVirtualTableValueChooser.this.setVisible(false);
			}
		});
		
		JPanel buttons = new JPanel();
		buttons.add(buttonOk);
		buttons.add(buttonCancel);
		this.add(buttons,BorderLayout.SOUTH);
		
		// #12219 defaultm��ig 20 Eintr�ge anzeigen
		this.table.setVisibleRowCount(20);
		this.pack();
		
	}
	
	
	/**
	 * Perform a choose action. Gets triggered either by double click on table
	 * row or by pressing the button.
	 */
	private void performChooseAction()
	{
		DefaultVirtualTableValueChooser.this.chooseValue();
		DefaultVirtualTableValueChooser.this.setVisible(false);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public VirtualTableRow getSelectValue()
	{
		return this.selectedValue;
	}
	
	
	/**
	 * Sets the chosen {@link VirtualTableRow} to the specified value.
	 * 
	 * @param selectedValue
	 *            a {@link VirtualTableRow} to select
	 */
	@Override
	public void setSelectValue(VirtualTableRow selectedValue)
	{
		this.selectedValue = selectedValue;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void show(final ValueChooserField owner)
	{
		this.owner = owner;
		this.setLocationRelativeTo(SwingUtilities.getWindowAncestor((Component)this.owner));
		this.setVisible(true);
		
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void reset()
	{
		updateTableSelection();
		this.filterField.setText("");
	}
	
	
	/**
	 * Updates the table row selection according to the selected value.
	 */
	private void updateTableSelection()
	{
		// impl until 2012-09-10
		// if(selectedValue != null)
		// {
		// VirtualTable vt = table.getVirtualTable();
		// Object selectedPK = selectedValue.get(selectedValue.getVirtualTable()
		// .getPrimaryColumn());
		// int selectedRowIndexVt =
		// vt.getRowIndex(vt.getPrimaryColumn().getName(),selectedPK);
		// int selectedRowIndexTable =
		// TableModelWrapperUtils.getRowAt(table.getModel(),
		// selectedRowIndexVt);
		// table.setSelectedRows(new int[]{selectedRowIndexTable});
		// table.scrollRowToVisible(selectedRowIndexTable);
		// }
		// else
		// {
		// table.clearSelection();
		// table.scrollRowToVisible(0);
		// }
		
		if(selectedValue != null)
		{
			table.setSelectedVirtualTableRow(selectedValue);
		}
		else
		{
			// table.clearSelection();
			// #13205
			table.getSelectionModel().setSelectionInterval(0,0);
		}
	}
	
	
	/**
	 * Is called when a value is chosen.
	 */
	private void chooseValue()
	{
		
		VirtualTableModel model = (VirtualTableModel)TableModelWrapperUtils.getActualTableModel(
				table.getModel(),VirtualTableModel.class);
		
		int row = TableModelWrapperUtils.getActualRowAt(table.getModel(),table.getSelectedRow(),
				VirtualTableModel.class);
		if(row != -1)
		{
			DefaultVirtualTableValueChooser.this.selectedValue = model.getVirtualTable()
					.getRow(row);
		}
		
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setVisible(final boolean visible)
	{
		final XdevQuickTableFilterField filterField = this.filterField;
		if(visible)
		{
			// ensure focus for textfield
			SwingUtilities.invokeLater(new Runnable()
			{
				
				@Override
				public void run()
				{
					filterField.getTextField().requestFocus();
					
				}
			});
			
		}
		else
		{
			this.owner.chooserClosed();
		}
		
		super.setVisible(visible);
		
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public JTable getTable()
	{
		return this.table;
	}
	
	
	@Override
	public void setChooserSize(int width, int height)
	{
		setSize(width,height);
	}
	
	
	@Override
	public boolean getInitState()
	{
		return this.initState;
	}
}
