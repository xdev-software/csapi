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
package xdev.ui;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import xdev.lang.OperationCanceledException;
import xdev.ui.ItemList.Entry;
import xdev.ui.dnd.DragAdapter;
import xdev.ui.dnd.DragGestureEvent;
import xdev.ui.dnd.DropAdapter;
import xdev.ui.dnd.DropEvent;
import xdev.ui.duallistbox.XdevDualListBoxDefaultHandler;
import xdev.ui.duallistbox.XdevDualListBoxHandler;
import xdev.util.IntList;
import xdev.vt.VirtualTable;

import com.jidesoft.icons.IconsFactory;
import com.jidesoft.list.ListModelWrapperUtils;
import com.jidesoft.swing.JideButton;


/**
 * A {@link XdevDualListBox} is a composite component consisting of two
 * listboxe's, one for available entries on the left and one for selected
 * entries on the right.
 * <p>
 * The user can control this component by pressing the add/remove buttons in the
 * middle, or by using left/right keys, Drag&Drop or mouse double click.
 * </p>
 * <p>
 * The component supports three selection modes.
 * <ul>
 * <li>KEEP_SELECTED selected items stay visible in the available list</li>
 * <li>REMOVE_SELECTED selected items are removed from the available list</li>
 * <li>DISABLE_SELECTED selected items stay visible in the available list, but
 * are disabled</li>
 * </ul>
 * </p>
 * <p>
 * The developer can customize the mapping of selected and available entries by
 * implementing and setting a custom {@link XdevDualListBoxHandler}. The default
 * handler implementation {@link XdevDualListBoxDefaultHandler} copies entries
 * as they are added or removed.
 * </p>
 * 
 * @author XDEV Software
 * 
 */
public class XdevDualListBox extends XComponent implements XdevFocusCycleComponent
{
	/**
	 * serialVersionUID.
	 */
	private static final long	serialVersionUID				= 1L;
	
	/**
	 * Subcomponents name.
	 */
	public static final String	AVAILABLE_LISTBOX_NAME			= "AvailableListbox";
	/**
	 * Subcomponents name.
	 */
	public static final String	SELECTED_LISTBOX_NAME			= "SelectedListbox";
	/**
	 * Subcomponents name.
	 */
	public static final String	AVAILABLE_FILTER_NAME			= "AvailableFilter";
	/**
	 * Subcomponents name.
	 */
	public static final String	SELECTED_FILTER_NAME			= "SelectedFilter";
	/**
	 * Subcomponents name.
	 */
	public static final String	REMOVE_SELECTED_BUTTON_NAME		= "RemoveButton";
	/**
	 * Subcomponents name.
	 */
	public static final String	REMOVE_ALL_SELECTED_BUTTON_NAME	= "RemoveAllButton";
	/**
	 * Subcomponents name.
	 */
	public static final String	ADD_SELECTED_BUTTON_NAME		= "AddButton";
	/**
	 * Subcomponents name.
	 */
	public static final String	ADD_ALL_SELECTED_BUTTON_NAME	= "AddAllButton";
	/**
	 * Subcomponents name.
	 */
	public static final String	UP_BUTTON_NAME					= "ButtonUp";
	/**
	 * Subcomponents name.
	 */
	public static final String	TOP_BUTTON_NAME					= "ButtonTop";
	/**
	 * Subcomponents name.
	 */
	public static final String	DOWN_BUTTON_NAME				= "ButtonDown";
	/**
	 * Subcomponents name.
	 */
	public static final String	BOTTOM_BUTTON_NAME				= "ButtonBottom";
	
	
	
	/**
	 * This enum contains the available behaviors of available entries, when
	 * chosen.
	 * 
	 * @author XDEV Software
	 * 
	 */
	public static enum SelectionMode
	{
		/**
		 * enabled entry remains in available list.
		 */
		KEEP_SELECTED,
		/**
		 * entry is removed from available list.
		 */
		REMOVE_SELECTED,
		/**
		 * disabled entry remains in available list.
		 */
		DISABLE_SELECTED
	}
	
	
	
	/**
	 * This enum contains Keys for all {@code XdevDualListBox} action that are
	 * triggered by buttons.
	 * 
	 * @author XDEV Software
	 * 
	 */
	public static enum ButtonKey
	{
		/**
		 * move one or more entries from available to selected.
		 */
		ADD_TO_SELECTED(new ButtonConfiguration(XdevDualListBox.getIcon("select.png"))),
		/**
		 * move one or more entries from selected to available.
		 */
		REMOVE_FROM_SELECTED(new ButtonConfiguration(XdevDualListBox.getIcon("remove.png"))),
		/**
		 * move all entries from available to selected.
		 */
		ADD_ALL_TO_SELECTED(new ButtonConfiguration(XdevDualListBox.getIcon("select_all.png"))),
		/**
		 * move all entries from selected to available.
		 */
		REMOVE_ALL_FROM_SELECTED(new ButtonConfiguration(XdevDualListBox.getIcon("remove_all.png"))),
		/**
		 * move selected entries in selected list up by one position.
		 */
		MOVE_SELECTED_UP(new ButtonConfiguration(XdevDualListBox.getIcon("up.png"))),
		/**
		 * move selected entries in selected list down by one position.
		 */
		MOVE_SELECTED_DOWN(new ButtonConfiguration(XdevDualListBox.getIcon("down.png"))),
		/**
		 * move selected entries in selected list to the top position.
		 */
		MOVE_SELECTED_TOP(new ButtonConfiguration(XdevDualListBox.getIcon("top.png"))),
		/**
		 * move selected entries in selected list to the bottom position.
		 */
		MOVE_SELECTED_BOTTOM(new ButtonConfiguration(XdevDualListBox.getIcon("bottom.png")));
		
		/**
		 * stores the default button configuration.
		 */
		private ButtonConfiguration	defaultConfiguration;
		
		
		/**
		 * Initializes a new ButtonKey.
		 * 
		 * @param defaultConfig
		 *            the configuration
		 */
		private ButtonKey(ButtonConfiguration defaultConfig)
		{
			this.defaultConfiguration = defaultConfig;
		}
	}
	
	/**
	 * name to register the keystroke for add.
	 */
	private static final String					CMD_ADD_TO_SELECTED			= "ADD";
	/**
	 * name to register the keystroke for remove.
	 */
	private static final String					CMD_REMOVE_FROM_SELECTED	= "REMOVE";
	/**
	 * name to register the keystroke for move to right.
	 */
	private static final String					CMD_FOCUS_SELECTED_LIST		= "RIGHT";
	/**
	 * name to register the keystroke for move to left.
	 */
	private static final String					CMD_FOCUS_AVAILABLE_LIST	= "LEFT";
	
	/**
	 * relative path (to package name of this class) of icon directory.
	 */
	private static final String					RELATIVE_ICON_PATH			= "duallistbox/icon/";
	
	/**
	 * The {@code XdevListBox} used to store the available items.
	 */
	protected XdevListBox						availableList;
	
	/**
	 * The {@code XdevListBox} used to store the selected items.
	 */
	protected XdevListBox						selectedList;
	
	/**
	 * The {@code XdevQuickListFilterField} used to filter the available items.
	 */
	private XdevQuickListFilterField			availableFilterField;
	/**
	 * The {@code XdevQuickListFilterField} used to filter the available items.
	 */
	private XdevQuickListFilterField			selectedFilterField;
	
	/**
	 * The button pane used for sorting items in the selected list.
	 */
	private JPanel								sortButtonPane;
	
	/**
	 * {@code Map} for storing {@code Actions} for {@code ButtonKeys}.
	 */
	private Map<ButtonKey, Action>				actionMap					= new HashMap<>();
	
	/**
	 * {@link Map} for storing {@link ButtonConfiguration}s for
	 * {@link ButtonKey}s.
	 */
	private Map<ButtonKey, ButtonConfiguration>	buttonConfigurations		= new HashMap<>();
	
	/**
	 * The handler of this component. The default copies unmodified items
	 * between lists.
	 */
	protected XdevDualListBoxHandler			handler						= new XdevDualListBoxDefaultHandler();
	
	/**
	 * The selection mode of this component. Defaults to REMOVE_SELECTED.
	 */
	protected SelectionMode						selectionMode				= SelectionMode.REMOVE_SELECTED;
	
	/**
	 * The flag for multiple selections. Defaults to {@code false}.
	 */
	private boolean								multipleSelectionsEnabled	= false;
	
	/**
	 * the initial tabIndex.
	 */
	private int									tabIndex					= -1;
	
	/**
	 * the itemList of available items.
	 */
	protected ItemList							availableItems;
	
	/**
	 * the itemList of selected items.
	 */
	protected ItemList							selectedItems;
	
	/**
	 * the control button pane.
	 */
	private JPanel								buttonPane;
	
	/**
	 * a {@link List} containing all control buttons (such as filterbuttons).
	 */
	private List<JButton>						controlButtonList			= new ArrayList<JButton>();
	
	
	/**
	 * Creates a new instance of {@link XdevDualListBox}.
	 * <p>
	 * If this constructor is used, one of the init-methods should be called.
	 * </p>
	 */
	@SuppressWarnings("rawtypes")
	// empty array lists in setModel can be untyped
	public XdevDualListBox()
	{
		availableItems = new ItemList();
		selectedItems = new ItemList();
		
		selectedList = new XdevListBox();
		availableList = new XdevListBox();
		
		setupComponents();
		setModel(new ArrayList(),new ArrayList());
	}
	
	
	/**
	 * Creates a new instance of {@link XdevDualListBox}.
	 * <p>
	 * This is a convenience constructor for
	 * {@link #XdevDualListBox(ItemList, ItemList, SelectionMode)}. The elements of the
	 * collections get mapped as data in the {@code ItemList}, the items are
	 * generated by calling {@code toString()} on them.
	 * </p>
	 * 
	 * @param available
	 *            a collection containing the available entries for this
	 *            component
	 * @param selected
	 *            a collection containing the selected entries for this
	 *            component
	 * @param selectionMode
	 *            a {@link SelectionMode}
	 * @see #XdevDualListBox(ItemList, ItemList, SelectionMode)
	 */
	public XdevDualListBox(Collection<?> available, Collection<?> selected,
			SelectionMode selectionMode)
	{
		setupComponents();
		setModel(available,selected,selectionMode);
	}
	
	
	/**
	 * Creates a new instance of {@link XdevDualListBox}.
	 * 
	 * @param available
	 *            a {@code ItemList} containing the available entries for this
	 *            component
	 * @param selected
	 *            a {@code ItemList} containing the selected entries for this
	 *            component
	 * @param selectionMode
	 *            a {@link SelectionMode}
	 */
	public XdevDualListBox(ItemList available, ItemList selected, SelectionMode selectionMode)
	{
		setupComponents();
		setModel(available,selected,selectionMode);
	}
	
	
	/**
	 * Initializes this component with two collections.
	 * 
	 * @param available
	 *            a collection containing the available entries for this
	 *            component
	 * @param selected
	 *            a collection containing the selected entries for this
	 *            component
	 * @param selectionMode
	 *            a {@link SelectionMode}
	 */
	public void setModel(Collection<?> available, Collection<?> selected,
			SelectionMode selectionMode)
	{
		availableItems = createItemListFromCollection(available);
		selectedItems = createItemListFromCollection(selected);
		this.selectionMode = selectionMode;
		update();
	}
	
	
	/**
	 * Updates the component, if the available and selected items change.
	 */
	protected void update()
	{
		availableList.setItemList(availableItems);
		availableFilterField.setModel(availableList.getModel());
		availableList.setModel(availableFilterField.getDisplayListModel());
		
		selectedList.setItemList(selectedItems);
		selectedFilterField.setModel(selectedList.getModel());
		selectedList.setModel(selectedFilterField.getDisplayListModel());
		
		synchronizeLists();
	}
	
	
	/**
	 * Initializes the contained components, does layout and registers actions.
	 */
	protected void setupComponents()
	{
		initActions();
		initGui();
	}
	
	
	/**
	 * Initializes this component with two collections in mode
	 * REMOVE_FROM_SELECTED.
	 * 
	 * @param available
	 *            a collection containing the available entries for this
	 *            component
	 * @param selected
	 *            a collection containing the selected entries for this
	 *            component
	 */
	public void setModel(Collection<?> available, Collection<?> selected)
	{
		availableItems = createItemListFromCollection(available);
		selectedItems = createItemListFromCollection(selected);
		update();
	}
	
	
	/**
	 * Initializes this component with two {@code ItemList}s.
	 * 
	 * @param availableItems
	 *            a {@code ItemList} containing the available entries for this
	 *            component
	 * @param selectedItems
	 *            a {@code ItemList} containing the selected entries for this
	 *            component
	 * @param selectionMode
	 *            a {@link SelectionMode}
	 */
	public void setModel(ItemList availableItems, ItemList selectedItems,
			SelectionMode selectionMode)
	{
		this.availableItems = availableItems != null ? availableItems : new ItemList();
		this.selectedItems = selectedItems != null ? selectedItems : new ItemList();
		this.selectionMode = selectionMode;
		update();
	}
	
	
	/**
	 * Initializes this component with two {@code ItemList}s with in mode
	 * REMOVE_FROM_SELECTED.
	 * 
	 * @param availableItems
	 *            a {@code ItemList} containing the available entries for this
	 *            component
	 * @param selectedItems
	 *            a {@code ItemList} containing the selected entries for this
	 *            component
	 */
	public void setModel(ItemList availableItems, ItemList selectedItems)
	{
		this.availableItems = availableItems;
		this.selectedItems = selectedItems;
		update();
	}
	
	
	/**
	 * This method can be used to customize the title of a
	 * {@code XdevDualListBox} button.
	 * <p>
	 * This method has to be called before the
	 * {@link #setModel(ItemList, ItemList)} or
	 * {@link #setModel(ItemList, ItemList)} method is called. Otherwise the
	 * configuration will have no effect
	 * </p>
	 * 
	 * @param buttonKey
	 *            the key for the button to customize
	 * @param title
	 *            the title of the button
	 * @see ButtonKey
	 * @see #configureButton(ButtonKey, String, Icon)
	 */
	public void configureButton(ButtonKey buttonKey, String title)
	{
		buttonConfigurations.put(buttonKey,new ButtonConfiguration(title));
	}
	
	
	/**
	 * This method can be used to customize the title of a
	 * {@code XdevDualListBox} button.
	 * <p>
	 * This method has to be called before the
	 * {@link #setModel(ItemList, ItemList)} or
	 * {@link #setModel(ItemList, ItemList)} method is called. Otherwise the
	 * configuration will have no effect
	 * </p>
	 * 
	 * @param buttonKey
	 *            the key for the button to customize
	 * @param title
	 *            the title of the button
	 * @param icon
	 *            the icon of the button
	 * @see ButtonKey
	 */
	public void configureButton(ButtonKey buttonKey, String title, Icon icon)
	{
		buttonConfigurations.put(buttonKey,new ButtonConfiguration(title,icon));
	}
	
	
	/**
	 * This method can be used to customize the title of a
	 * {@code XdevDualListBox} button.
	 * <p>
	 * This method has to be called before the
	 * {@link #setModel(ItemList, ItemList)} or
	 * {@link #setModel(ItemList, ItemList)} method is called. Otherwise the
	 * configuration will have no effect
	 * </p>
	 * 
	 * @param buttonKey
	 *            the key for the button to customize
	 * @param icon
	 *            the icon of the button
	 * @see ButtonKey
	 */
	public void configureButton(ButtonKey buttonKey, Icon icon)
	{
		buttonConfigurations.put(buttonKey,new ButtonConfiguration(icon));
	}
	
	
	/**
	 * Initializes the specific {@link Action} instances of this component and
	 * registers them in a {@code Map}.
	 */
	private void initActions()
	{
		actionMap.put(ButtonKey.ADD_TO_SELECTED,new AddToSelectedAction(
				getButtonConfiguration(ButtonKey.ADD_TO_SELECTED)));
		actionMap.put(ButtonKey.ADD_ALL_TO_SELECTED,new AddAllToSelectedAction(
				getButtonConfiguration(ButtonKey.ADD_ALL_TO_SELECTED)));
		actionMap.put(ButtonKey.REMOVE_FROM_SELECTED,new RemoveFromSelectedAction(
				getButtonConfiguration(ButtonKey.REMOVE_FROM_SELECTED)));
		actionMap.put(ButtonKey.REMOVE_ALL_FROM_SELECTED,new RemoveAllFromSelectedAction(
				getButtonConfiguration(ButtonKey.REMOVE_ALL_FROM_SELECTED)));
		
		actionMap.put(ButtonKey.MOVE_SELECTED_TOP,new MoveTopAction(
				getButtonConfiguration(ButtonKey.MOVE_SELECTED_TOP)));
		actionMap.put(ButtonKey.MOVE_SELECTED_UP,new MoveUpAction(
				getButtonConfiguration(ButtonKey.MOVE_SELECTED_UP)));
		actionMap.put(ButtonKey.MOVE_SELECTED_DOWN,new MoveDownAction(
				getButtonConfiguration(ButtonKey.MOVE_SELECTED_DOWN)));
		actionMap.put(ButtonKey.MOVE_SELECTED_BOTTOM,new MoveBottomAction(
				getButtonConfiguration(ButtonKey.MOVE_SELECTED_BOTTOM)));
		
	}
	
	
	/**
	 * Get the button configuration for specified button key from the components
	 * {@code Map}.
	 * <p>
	 * If no {@link ButtonConfiguration} is found there, the default
	 * Configuration of the {@link ButtonKey} gets returned.
	 * </p>
	 * 
	 * @param buttonKey
	 *            The {@link ButtonKey} to get the {@link ButtonConfiguration}
	 *            for.
	 * @return a {@link ButtonConfiguration}
	 */
	private ButtonConfiguration getButtonConfiguration(ButtonKey buttonKey)
	{
		ButtonConfiguration buttonConfiguration = buttonConfigurations.get(buttonKey);
		if(buttonConfiguration == null)
		{
			return buttonKey.defaultConfiguration;
		}
		else
		{
			return buttonConfiguration;
		}
	}
	
	
	/**
	 * Does the layout of this component and triggers the registration of its
	 * events.
	 * 
	 */
	private void initGui()
	{
		availableList = createAvailableListbox();
		availableList.setName(AVAILABLE_LISTBOX_NAME);
		availableFilterField = new XdevQuickListFilterField(availableList.getModel());
		availableFilterField.setName(AVAILABLE_FILTER_NAME);
		JPanel availableMainPane = createListBoxPane(availableList,availableFilterField);
		
		selectedList = createSelectedListbox();
		selectedList.setName(SELECTED_LISTBOX_NAME);
		selectedFilterField = new XdevQuickListFilterField(selectedList.getModel());
		selectedFilterField.setName(SELECTED_FILTER_NAME);
		JPanel selectedMainPane = createListBoxPane(selectedList,selectedFilterField);
		
		this.buttonPane = createButtonPane();
		this.sortButtonPane = createSortButtonPane();
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		setGBC(c,GridBagConstraints.BOTH,1.0,1,0,0);
		this.add(availableMainPane,c);
		
		setGBC(c,GridBagConstraints.NONE,0.0,2,3,3);
		this.add(buttonPane,c);
		
		setGBC(c,GridBagConstraints.BOTH,1.0,3,0,0);
		this.add(selectedMainPane,c);
		
		setGBC(c,GridBagConstraints.NONE,0.0,4,3,3);
		this.add(sortButtonPane,c);
		
		initDragAndDrop();
		initKeyEvents();
		initMouseEvents();
		initDisabledCellRenderers();
	}
	
	
	/**
	 * Helper methods for configuring {@link GridBagConstraints}.
	 * 
	 * @param c
	 *            the {@link GridBagConstraints} to configure
	 * @param fill
	 *            the fill of the constraint
	 * @param weightx
	 *            the x weight of the constraint
	 * @param x
	 *            the x coordinate
	 * @param marginLeft
	 *            the left margin
	 * @param marginRight
	 *            the right margin
	 */
	private void setGBC(GridBagConstraints c, int fill, double weightx, int x, int marginLeft,
			int marginRight)
	{
		c.fill = fill;
		c.weightx = weightx;
		c.weighty = 1;
		c.gridx = x;
		c.gridy = 1;
		c.insets = new Insets(0,marginLeft,0,marginRight);
	}
	
	
	/**
	 * Creates an instance of {@link XdevListBox} and populates it with selected
	 * values.
	 * 
	 * @return an {@link XdevListBox}
	 */
	protected XdevListBox createSelectedListbox()
	{
		XdevListBox xdevListBox = new XdevListBox(selectedItems);
		// avoid traversing this component by XdevFormular #12276
		xdevListBox.putClientProperty(ClientProperties.FORMULAR_SKIP,Boolean.valueOf(true));
		return xdevListBox;
	}
	
	
	/**
	 * Creates an instance of {@link XdevListBox} and populates it with
	 * available values.
	 * 
	 * @return an {@link XdevListBox}
	 */
	protected XdevListBox createAvailableListbox()
	{
		XdevListBox xdevListBox = new XdevListBox(availableItems);
		// avoid traversing this component by XdevFormular #12276
		xdevListBox.putClientProperty(ClientProperties.FORMULAR_SKIP,Boolean.valueOf(true));
		return xdevListBox;
	}
	
	
	/**
	 * Sets the {@link DisabledCellRenderer} for the {@link XdevListBox}es.
	 */
	private void initDisabledCellRenderers()
	{
		ListCellRenderer disabledAvailableRenderer = new DisabledCellRenderer(
				this.availableList.getCellRenderer());
		this.availableList.setCellRenderer(disabledAvailableRenderer);
		
		ListCellRenderer disabledSelectedRenderer = new DisabledCellRenderer(
				this.availableList.getCellRenderer());
		this.selectedList.setCellRenderer(disabledSelectedRenderer);
	}
	
	
	/**
	 * Registers callback method for drag and drop events on the lists.
	 */
	private void initDragAndDrop()
	{
		new DragAdapter(selectedList)
		{
			public void dragGestureRecognized(DragGestureEvent event)
			{
				onDrag(selectedList,event);
			}
		};
		new DragAdapter(availableList)
		{
			public void dragGestureRecognized(DragGestureEvent event)
			{
				onDrag(availableList,event);
			}
		};
		new DropAdapter(selectedList)
		{
			
			public void drop(DropEvent event)
			{
				onDrop(selectedList,event);
			}
		};
		new DropAdapter(availableList)
		{
			
			public void drop(DropEvent event)
			{
				onDrop(availableList,event);
				
			}
		};
	}
	
	
	/**
	 * Callback method for drag events.
	 * 
	 * @param listbox
	 *            the {@link XdevListBox} a drag event was triggered for.
	 * @param event
	 *            the event
	 */
	private void onDrag(XdevListBox listbox, DragGestureEvent event)
	{
		XdevListBox origin = listbox;
		int[] selectedIndices = listbox.getSelectedIndices();
		Object[] eventData = new Object[]{origin,selectedIndices};
		if(selectedIndices.length != 0)
		{
			event.startDrag(eventData);
		}
	}
	
	
	/**
	 * Callback method for drop events.
	 * 
	 * @param listbox
	 *            the {@link XdevListBox} a drop event was triggered for.
	 * @param event
	 *            the event
	 */
	private void onDrop(XdevListBox listbox, DropEvent event)
	{
		Object[] eventData = (Object[])event.getData();
		XdevListBox origin = (XdevListBox)eventData[0];
		if(origin != listbox)
		{
			if(listbox == availableList)
			{
				int[] selectedIndices = (int[])eventData[1];
				removeFromSelected(selectedIndices);
			}
			else
			{
				int[] availableIndices = (int[])eventData[1];
				addToSelected(availableIndices);
			}
		}
	}
	
	
	/**
	 * Sets up the key events for moving focus and items between lists.
	 */
	private void initKeyEvents()
	{
		// left cursor removes selected entry/ies
		selectedList.getInputMap(JComponent.WHEN_FOCUSED).put(
				KeyStroke.getKeyStroke(KeyEvent.VK_LEFT,0),CMD_REMOVE_FROM_SELECTED);
		selectedList.getActionMap().put(CMD_REMOVE_FROM_SELECTED,
				actionMap.get(ButtonKey.REMOVE_FROM_SELECTED));
		
		// ctrl + left cursor moves focus to available list
		selectedList.getInputMap(JComponent.WHEN_FOCUSED).put(
				KeyStroke.getKeyStroke(KeyEvent.VK_LEFT,InputEvent.CTRL_MASK),
				CMD_FOCUS_AVAILABLE_LIST);
		selectedList.getActionMap().put(CMD_FOCUS_AVAILABLE_LIST,new AbstractAction()
		{
			private static final long	serialVersionUID	= 1L;
			
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				XdevDualListBox.this.availableList.requestFocus();
			}
		});
		
		// right cursor adds selected entry/ies
		availableList.getInputMap(JComponent.WHEN_FOCUSED).put(
				KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT,0),CMD_ADD_TO_SELECTED);
		availableList.getActionMap().put(CMD_ADD_TO_SELECTED,
				actionMap.get(ButtonKey.ADD_TO_SELECTED));
		
		// ctrl + right cursor moves focus to selected list
		availableList.getInputMap(JComponent.WHEN_FOCUSED).put(
				KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT,InputEvent.CTRL_MASK),
				CMD_FOCUS_SELECTED_LIST);
		availableList.getActionMap().put(CMD_FOCUS_SELECTED_LIST,new AbstractAction()
		{
			private static final long	serialVersionUID	= 1L;
			
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				XdevDualListBox.this.selectedList.requestFocus();
			}
		});
		
	}
	
	
	/**
	 * Sets up the mouse events. Double clicks move items between lists.
	 */
	private void initMouseEvents()
	{
		// process double clicks for listboxes
		selectedList.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if(e.getClickCount() == 2)
				{
					int[] selectedIndices = selectedList.getSelectedIndices();
					removeFromSelected(selectedIndices);
				}
			}
		});
		availableList.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if(e.getClickCount() == 2)
				{
					int[] availableIndices = availableList.getSelectedIndices();
					addToSelected(availableIndices);
				}
			}
		});
	}
	
	
	/**
	 * Creates the Panes, the {@link XdevListBox}es are contained in.
	 * 
	 * @param listbox
	 *            the {@link XdevListBox}
	 * @param filterField
	 *            the {@link XdevQuickListFilterField} at the top.
	 * @return a {@link JPanel}.
	 */
	private JPanel createListBoxPane(XdevListBox listbox, XdevQuickListFilterField filterField)
	{
		JPanel availableMainPane = new JPanel(new BorderLayout(0,5));
		
		listbox.setModel(filterField.getDisplayListModel());
		availableMainPane.add(filterField,BorderLayout.NORTH);
		
		XScrollPane availablePane = new XScrollPane(listbox);
		availableMainPane.add(availablePane,BorderLayout.CENTER);
		return availableMainPane;
	}
	
	
	/**
	 * Creates and sets up the button pane.
	 * 
	 * @return a {@link JPanel} containing the move buttons
	 */
	private JPanel createButtonPane()
	{
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BorderLayout());
		
		JToolBar tb = createButtonPaneToolBar();
		tb.add(createButton(actionMap.get(ButtonKey.ADD_ALL_TO_SELECTED),
				ADD_ALL_SELECTED_BUTTON_NAME));
		tb.add(createButton(actionMap.get(ButtonKey.ADD_TO_SELECTED),ADD_SELECTED_BUTTON_NAME));
		tb.add(createButton(actionMap.get(ButtonKey.REMOVE_FROM_SELECTED),
				REMOVE_SELECTED_BUTTON_NAME));
		tb.add(createButton(actionMap.get(ButtonKey.REMOVE_ALL_FROM_SELECTED),
				REMOVE_ALL_SELECTED_BUTTON_NAME));
		
		buttonPane.add(createButtonPaneToolBar(),BorderLayout.NORTH);
		buttonPane.add(tb,BorderLayout.CENTER);
		buttonPane.add(createButtonPaneToolBar(),BorderLayout.SOUTH);
		
		return buttonPane;
	}
	
	
	/**
	 * Creates and sets up the sort button pane.
	 * 
	 * @return a {@link JPanel} containing the sort buttons.
	 */
	private JPanel createSortButtonPane()
	{
		JPanel sortButtonPane = new JPanel();
		sortButtonPane.setLayout(new BorderLayout());
		
		JToolBar tb = createButtonPaneToolBar();
		tb.add(createButton(actionMap.get(ButtonKey.MOVE_SELECTED_TOP),TOP_BUTTON_NAME));
		tb.add(createButton(actionMap.get(ButtonKey.MOVE_SELECTED_UP),UP_BUTTON_NAME));
		tb.add(createButton(actionMap.get(ButtonKey.MOVE_SELECTED_DOWN),DOWN_BUTTON_NAME));
		tb.add(createButton(actionMap.get(ButtonKey.MOVE_SELECTED_BOTTOM),BOTTOM_BUTTON_NAME));
		
		sortButtonPane.add(createButtonPaneToolBar(),BorderLayout.NORTH);
		sortButtonPane.add(tb,BorderLayout.CENTER);
		sortButtonPane.add(createButtonPaneToolBar(),BorderLayout.SOUTH);
		
		return sortButtonPane;
	}
	
	
	/**
	 * Creates a {@link JToolBar} used for button panes.
	 * 
	 * @return a {@link JToolBar}.
	 */
	private JToolBar createButtonPaneToolBar()
	{
		JToolBar tb = new JToolBar(JToolBar.VERTICAL);
		tb.setRollover(true);
		tb.setFloatable(false);
		return tb;
	}
	
	
	/**
	 * Creates a new button, configures it with an action and places it in a
	 * pane.
	 * 
	 * @param action
	 *            the {@link Action} to configure.
	 * @param buttonName
	 *            the name of the components (set mainly for testing purposes)
	 * @return a newly created button instance.
	 */
	private JideButton createButton(Action action, String buttonName)
	{
		JideButton btn = new JideButton();
		btn.setFocusable(false);
		btn.setAction(action);
		btn.setName(buttonName);
		
		this.controlButtonList.add(btn);
		return btn;
	}
	
	
	/**
	 * Creates a {@link ItemList} of a specified {@link Collection}.
	 * 
	 * @param collection
	 *            the source {@link Collection}
	 * @return a {@link ItemList}
	 */
	private ItemList createItemListFromCollection(Collection<?> collection)
	{
		List<?> data = new ArrayList<Object>(collection);
		List<String> items = new ArrayList<String>(collection.size());
		for(Object item : collection)
		{
			items.add(item.toString());
		}
		ItemList itemList = new ItemList(items,data);
		return itemList;
	}
	
	
	/**
	 * Gets the currently set Handler for this instance.
	 * 
	 * @return a {@link XdevDualListBoxDefaultHandler}
	 */
	public XdevDualListBoxHandler getHandler()
	{
		return handler;
	}
	
	
	/**
	 * Sets a handler for this instance.
	 * <p>
	 * This method is useful for configuring {@link XdevDualListBox} with a
	 * custom handler implementation. By doing so, the behavior of
	 * {@link XdevDualListBox}, when adding or removing elements can be
	 * customized.
	 * </p>
	 * 
	 * @param handler
	 *            a new {@link XdevDualListBoxHandler}
	 */
	public void setHandler(XdevDualListBoxHandler handler)
	{
		this.handler = handler;
	}
	
	
	/**
	 * Returns the selection mode for this instance.
	 * 
	 * @return a {@link SelectionMode}
	 */
	public SelectionMode getSelectionMode()
	{
		return selectionMode;
	}
	
	
	/**
	 * Enables / Disables the display of the filter fields on top of the lists.
	 * 
	 * @param showFilterFields
	 *            if {@code true} filters fields are displayed.
	 */
	public void setShowFilterFields(boolean showFilterFields)
	{
		availableFilterField.setVisible(showFilterFields);
		selectedFilterField.setVisible(showFilterFields);
	}
	
	
	/**
	 * Are filter fields displayed?
	 * 
	 * @return {@code true} if filter fields are displayed.
	 */
	public boolean isShowFilterFields()
	{
		return availableFilterField.isVisible();
	}
	
	
	/**
	 * This method gets called at initialization time and synchronizes the
	 * ItemsLists dependent on the {@link SelectionMode} of the component.
	 * 
	 */
	private void synchronizeLists()
	{
		for(int i = 0; i < selectedItems.size(); i++)
		{
			Entry selectedEntry = selectedItems.get(i);
			Entry availableEntryFromSelected = this.handler.onRemoveFromSelected(selectedEntry);
			for(int j = 0; j < availableItems.size(); j++)
			{
				Entry availableEntry = availableItems.get(j);
				if(availableEntry.equals(availableEntryFromSelected))
				{
					switch(selectionMode)
					{
						case DISABLE_SELECTED:
							availableEntry.setEnabled(false);
						break;
						case REMOVE_SELECTED:
							availableItems.remove(j);
						break;
						case KEEP_SELECTED:
						// nothing to do
						break;
					}
				}
			}
		}
	}
	
	
	/**
	 * Returns, if multiple selection is enabled.
	 * <p>
	 * Only useful in selection mode {@code KEEP_SELECTION}.
	 * 
	 * @return true, if multiple selections are enabled.
	 */
	public boolean isMultipleSelectionsEnabled()
	{
		return multipleSelectionsEnabled;
	}
	
	
	/**
	 * Enables / Disabled multiple selections.
	 * <p>
	 * Only useful in selection mode {@link SelectionMode#KEEP_SELECTED}.
	 * Default is {@code false}
	 * </p>
	 * 
	 * @param multipleSelectionsEnabled
	 *            if set to {@code true} multiple selections are enabled
	 */
	public void setMultipleSelectionsEnabled(boolean multipleSelectionsEnabled)
	{
		this.multipleSelectionsEnabled = multipleSelectionsEnabled;
	}
	
	
	/**
	 * Returns the currently selected entries as a item list.
	 * 
	 * @return an {@link ItemList}
	 */
	public ItemList getSelectedAsItemList()
	{
		return selectedList.getItemList();
	}
	
	
	/**
	 * Returns the currently available entries as a item list.
	 * 
	 * @return an
	 */
	public ItemList getAvailableAsItemList()
	{
		return availableList.getItemList();
	}
	
	
	/**
	 * Returns the currently selected entries (data portion of item list entry)
	 * as a collection.
	 * 
	 * @return an {@link Collection}
	 */
	public Collection<?> getSelectedAsCollection()
	{
		Collection<Object> collection = new ArrayList<Object>();
		final int size = selectedList.getItemList().size();
		for(int i = 0; i < size; i++)
		{
			collection.add(selectedList.getItemList().getData(i));
		}
		return collection;
	}
	
	
	/**
	 * Returns {@code true} if nothing is selected, else {@code false}.
	 * 
	 * @return {@code true} if nothing is selected, else {@code false}
	 * @see #deselectAll()
	 */
	public boolean isSelectionEmpty()
	{
		return selectedItems.size() == 0;
	}
	
	
	/**
	 * Are the sort buttons currently visible?
	 * 
	 * @return {@code true} if visible
	 */
	public boolean isShowSortButtons()
	{
		return sortButtonPane.isVisible();
	}
	
	
	/**
	 * Set the visibility of the sort buttons.
	 * 
	 * @param showSortButtons
	 *            boolean flag.
	 */
	public void setShowSortButtons(boolean showSortButtons)
	{
		this.sortButtonPane.setVisible(showSortButtons);
	}
	
	
	/**
	 * Are the quick filter fields currently visible.
	 * 
	 * @return {@code true} if visible
	 */
	public boolean isQuickFiltersEnabled()
	{
		return selectedFilterField.isVisible();
	}
	
	
	/**
	 * Set the visibility of quick filters fields.
	 * 
	 * @param enabled
	 *            boolean flag.
	 */
	public void setQuickFiltersEnabled(boolean enabled)
	{
		this.selectedFilterField.setVisible(enabled);
		this.availableFilterField.setVisible(enabled);
	}
	
	
	
	//
	// inner classes
	//
	
	//
	// Actions
	//
	
	/**
	 * Action for adding one or more items to the selected list.
	 */
	private class AddToSelectedAction extends AbstractAction
	{
		/**
		 * serialVersionUID.
		 */
		private static final long	serialVersionUID	= 1L;
		
		
		/**
		 * Create a new Action for a specified {@link ButtonConfiguration}.
		 * 
		 * @param bc
		 *            the {@link ButtonConfiguration} to use.
		 */
		public AddToSelectedAction(ButtonConfiguration bc)
		{
			this(bc.name,bc.icon);
		}
		
		
		/**
		 * Create a new Action for a specified name and icon.
		 * 
		 * @param name
		 *            the name to use
		 * @param icon
		 *            the icon to use
		 */
		public AddToSelectedAction(String name, Icon icon)
		{
			super(name,icon);
		}
		
		
		/**
		 * Create a new Action for a specified name and icon.
		 * 
		 * @param name
		 *            the name to use
		 */
		public AddToSelectedAction(String name)
		{
			super(name);
		}
		
		
		@Override
		public void actionPerformed(ActionEvent e)
		{
			int[] availableIndices = availableList.getSelectedIndices();
			
			addToSelected(availableIndices);
		}
	}
	
	
	
	/**
	 * Action for adding all items to the selected list.
	 */
	private class AddAllToSelectedAction extends AddToSelectedAction
	{
		
		/**
		 * serialVersionUID.
		 */
		private static final long	serialVersionUID	= 1L;
		
		
		/**
		 * Create a new Action for a specified {@link ButtonConfiguration}.
		 * 
		 * @param bc
		 *            the {@link ButtonConfiguration} to use.
		 */
		public AddAllToSelectedAction(ButtonConfiguration bc)
		{
			this(bc.name,bc.icon);
		}
		
		
		/**
		 * Create a new Action for a specified name and icon.
		 * 
		 * @param name
		 *            the name to use
		 * @param icon
		 *            the icon to use
		 */
		public AddAllToSelectedAction(String name, Icon icon)
		{
			super(name,icon);
		}
		
		
		/**
		 * Create a new Action for a specified name and icon.
		 * 
		 * @param name
		 *            the name to use
		 */
		public AddAllToSelectedAction(String name)
		{
			super(name);
		}
		
		
		@Override
		public void actionPerformed(ActionEvent e)
		{
			availableFilterField.setText("");
			availableFilterField.applyFilter();
			int size = availableList.getModel().getSize();
			availableList.setSelectionInterval(0,size - 1);
			
			availableList.setSelectionInterval(0,availableList.getModel().getSize() - 1);
			super.actionPerformed(e);
		}
		
	}
	
	
	
	/**
	 * Action for removing one or more items from the selected list.
	 */
	private class RemoveFromSelectedAction extends AbstractAction
	{
		
		/**
		 * serialVersionUID.
		 */
		private static final long	serialVersionUID	= 1L;
		
		
		/**
		 * Create a new Action for a specified {@link ButtonConfiguration}.
		 * 
		 * @param bc
		 *            the {@link ButtonConfiguration} to use.
		 */
		public RemoveFromSelectedAction(ButtonConfiguration bc)
		{
			this(bc.name,bc.icon);
		}
		
		
		/**
		 * Create a new Action for a specified name and icon.
		 * 
		 * @param name
		 *            the name to use
		 * @param icon
		 *            the icon to use
		 */
		public RemoveFromSelectedAction(String name, Icon icon)
		{
			super(name,icon);
		}
		
		
		/**
		 * Create a new Action for a specified name and icon.
		 * 
		 * @param name
		 *            the name to use
		 */
		public RemoveFromSelectedAction(String name)
		{
			super(name);
		}
		
		
		@Override
		public void actionPerformed(ActionEvent e)
		{
			int[] selectedIndices = selectedList.getSelectedIndices();
			
			removeFromSelected(selectedIndices);
		}
	}
	
	
	
	/**
	 * Action for removing all items from the selected list.
	 */
	private class RemoveAllFromSelectedAction extends RemoveFromSelectedAction
	{
		
		/**
		 * serialVersionUID.
		 */
		private static final long	serialVersionUID	= 1L;
		
		
		/**
		 * Create a new Action for a specified {@link ButtonConfiguration}.
		 * 
		 * @param bc
		 *            the {@link ButtonConfiguration} to use.
		 */
		public RemoveAllFromSelectedAction(ButtonConfiguration bc)
		{
			this(bc.name,bc.icon);
		}
		
		
		/**
		 * Create a new Action for a specified name and icon.
		 * 
		 * @param name
		 *            the name to use
		 * @param icon
		 *            the icon to use
		 */
		public RemoveAllFromSelectedAction(String name, Icon icon)
		{
			super(name,icon);
		}
		
		
		/**
		 * Create a new Action for a specified name and icon.
		 * 
		 * @param name
		 *            the name to use
		 */
		public RemoveAllFromSelectedAction(String name)
		{
			super(name);
		}
		
		
		@Override
		public void actionPerformed(ActionEvent e)
		{
			selectedFilterField.setText("");
			selectedFilterField.applyFilter();
			int size = selectedList.getModel().getSize();
			selectedList.setSelectionInterval(0,size - 1);
			
			super.actionPerformed(e);
		}
		
	}
	
	
	
	/**
	 * Action for moving one or more items up in the selected list.
	 */
	private class MoveUpAction extends AbstractAction
	{
		
		/**
		 * serialVersionUID.
		 */
		private static final long	serialVersionUID	= 1L;
		
		
		/**
		 * Create a new Action for a specified {@link ButtonConfiguration}.
		 * 
		 * @param bc
		 *            the {@link ButtonConfiguration} to use.
		 */
		public MoveUpAction(ButtonConfiguration bc)
		{
			this(bc.name,bc.icon);
		}
		
		
		/**
		 * Create a new Action for a specified name and icon.
		 * 
		 * @param name
		 *            the name to use
		 * @param icon
		 *            the icon to use
		 */
		public MoveUpAction(String name, Icon icon)
		{
			super(name,icon);
		}
		
		
		/**
		 * Create a new Action for a specified name and icon.
		 * 
		 * @param name
		 *            the name to use
		 */
		@SuppressWarnings("unused")
		public MoveUpAction(String name)
		{
			super(name);
		}
		
		
		@Override
		public void actionPerformed(ActionEvent e)
		{
			int[] selectedIndices = selectedList.getSelectedIndices();
			mapIndicesOfInnerModel(selectedList,selectedIndices);
			int[] newSelectedIndices = new int[selectedIndices.length];
			
			if(selectedIndices.length > 0 && selectedIndices[0] > 0)
			{
				for(int i = 0; i < selectedIndices.length; i++)
				{
					Entry entry = selectedList.getItemList().remove(selectedIndices[i]);
					newSelectedIndices[i] = selectedIndices[i] - 1;
					selectedList.getItemList().add(newSelectedIndices[i],entry);
				}
				mapIndicesOfOuterModel(selectedList,newSelectedIndices);
				selectedList.setSelectedIndices(newSelectedIndices);
				selectedList.ensureIndexIsVisible(newSelectedIndices[0]);
			}
		}
		
	}
	
	
	
	/**
	 * Action for moving one or more items down in the selected list.
	 */
	private class MoveDownAction extends AbstractAction
	{
		
		/**
		 * serialVersionUID.
		 */
		private static final long	serialVersionUID	= 1L;
		
		
		/**
		 * Create a new Action for a specified {@link ButtonConfiguration}.
		 * 
		 * @param bc
		 *            the {@link ButtonConfiguration} to use.
		 */
		public MoveDownAction(ButtonConfiguration bc)
		{
			this(bc.name,bc.icon);
		}
		
		
		/**
		 * Create a new Action for a specified name and icon.
		 * 
		 * @param name
		 *            the name to use
		 * @param icon
		 *            the icon to use
		 */
		public MoveDownAction(String name, Icon icon)
		{
			super(name,icon);
		}
		
		
		/**
		 * Create a new Action for a specified name and icon.
		 * 
		 * @param name
		 *            the name to use
		 */
		@SuppressWarnings("unused")
		public MoveDownAction(String name)
		{
			super(name);
		}
		
		
		@Override
		public void actionPerformed(ActionEvent e)
		{
			int[] selectedIndices = selectedList.getSelectedIndices();
			final int selectedIndicesLength = selectedIndices.length;
			mapIndicesOfInnerModel(selectedList,selectedIndices);
			
			int[] newSelectedIndices = new int[selectedIndicesLength];
			
			final int itemListSize = selectedList.getItemList().size();
			
			if(selectedIndices.length > 0)
			{
				final int highestSelectedIndex = selectedIndices[selectedIndicesLength - 1];
				if(highestSelectedIndex < itemListSize - 1)
				{
					for(int i = selectedIndicesLength - 1; i >= 0; i--)
					{
						Entry entry = selectedList.getItemList().remove(selectedIndices[i]);
						newSelectedIndices[i] = selectedIndices[i] + 1;
						selectedList.getItemList().add(newSelectedIndices[i],entry);
					}
					mapIndicesOfOuterModel(selectedList,newSelectedIndices);
					selectedList.setSelectedIndices(newSelectedIndices);
					selectedList.ensureIndexIsVisible(newSelectedIndices[newSelectedIndices.length-1]);
				}
			}
			
		}
	}
	
	
	
	/**
	 * Action for moving one or more items to the top in the selected list.
	 */
	private class MoveTopAction extends AbstractAction
	{
		/**
		 * serialVersionUID.
		 */
		private static final long	serialVersionUID	= 1L;
		
		
		/**
		 * Create a new Action for a specified {@link ButtonConfiguration}.
		 * 
		 * @param bc
		 *            the {@link ButtonConfiguration} to use.
		 */
		public MoveTopAction(ButtonConfiguration bc)
		{
			this(bc.name,bc.icon);
		}
		
		
		/**
		 * Create a new Action for a specified name and icon.
		 * 
		 * @param name
		 *            the name to use
		 * @param icon
		 *            the icon to use
		 */
		public MoveTopAction(String name, Icon icon)
		{
			super(name,icon);
		}
		
		
		/**
		 * Create a new Action for a specified name and icon.
		 * 
		 * @param name
		 *            the name to use
		 */
		@SuppressWarnings("unused")
		public MoveTopAction(String name)
		{
			super(name);
		}
		
		
		@Override
		public void actionPerformed(ActionEvent e)
		{
			int[] selectedIndices = selectedList.getSelectedIndices();
			mapIndicesOfInnerModel(selectedList,selectedIndices);
			int[] newSelectedIndices = new int[selectedIndices.length];
			
			for(int i = 0; i < selectedIndices.length; i++)
			{
				Entry entry = selectedList.getItemList().remove(selectedIndices[i]);
				newSelectedIndices[i] = i;
				selectedList.getItemList().add(i,entry);
			}
			mapIndicesOfOuterModel(selectedList,newSelectedIndices);
			selectedList.setSelectedIndices(newSelectedIndices);
			selectedList.ensureIndexIsVisible(newSelectedIndices[0]);
		}
		
	}
	
	
	
	/**
	 * Action for moving one or more items to the bottom in the selected list.
	 */
	private class MoveBottomAction extends AbstractAction
	{
		/**
		 * serialVersionUID.
		 */
		private static final long	serialVersionUID	= 1L;
		
		
		/**
		 * Create a new Action for a specified {@link ButtonConfiguration}.
		 * 
		 * @param bc
		 *            the {@link ButtonConfiguration} to use.
		 */
		public MoveBottomAction(ButtonConfiguration bc)
		{
			this(bc.name,bc.icon);
		}
		
		
		/**
		 * Create a new Action for a specified name and icon.
		 * 
		 * @param name
		 *            the name to use
		 * @param icon
		 *            the icon to use
		 */
		public MoveBottomAction(String name, Icon icon)
		{
			super(name,icon);
		}
		
		
		/**
		 * Create a new Action for a specified name and icon.
		 * 
		 * @param name
		 *            the name to use
		 */
		@SuppressWarnings("unused")
		public MoveBottomAction(String name)
		{
			super(name);
		}
		
		
		@Override
		public void actionPerformed(ActionEvent e)
		{
			int[] selectedIndices = selectedList.getSelectedIndices();
			mapIndicesOfInnerModel(selectedList,selectedIndices);
			int[] newSelectedIndices = new int[selectedIndices.length];
			
			for(int i = selectedIndices.length - 1; i >= 0; i--)
			{
				Entry entry = selectedList.getItemList().remove(selectedIndices[i]);
				int selectedListItemSize = selectedList.getItemList().size();
				newSelectedIndices[i] = selectedListItemSize - (selectedIndices.length - 1 - i);
				selectedList.getItemList().add(newSelectedIndices[i],entry);
			}
			mapIndicesOfOuterModel(selectedList,newSelectedIndices);
			selectedList.setSelectedIndices(newSelectedIndices);
			selectedList.ensureIndexIsVisible(newSelectedIndices[newSelectedIndices.length-1]);
		}
	}
	
	
	/**
	 * Converts the indices in the visual representation of a listbox to the
	 * indices of the inner model.
	 * 
	 * @param listbox
	 *            the {@link XdevListBox} to map indices for
	 * @param indices
	 *            an array with indices, the entries in this array are changed
	 *            to the corresponding indices of the inner model.
	 */
	private void mapIndicesOfInnerModel(XdevListBox listbox, int[] indices)
	{
		for(int i = 0; i < indices.length; i++)
		{
			int mappedIndex = ListModelWrapperUtils.getActualIndexAt(listbox.getModel(),indices[i]);
			
			if(mappedIndex > -1)
			{
				indices[i] = mappedIndex;
			}
		}
	}
	
	
	/**
	 * Converts the indices of the inner model of a listbox to the indices of
	 * the visual representation model.
	 * 
	 * @param listbox
	 *            the {@link XdevListBox} to map indices for
	 * @param indices
	 *            an array with indices, the entries in this array are changed
	 *            to the corresponding indices of the outer model.
	 */
	private void mapIndicesOfOuterModel(XdevListBox listbox, int[] indices)
	{
		for(int i = 0; i < indices.length; i++)
		{
			int mappedIndex = ListModelWrapperUtils.getIndexAt(listbox.getModel(),indices[i]);
			
			if(mappedIndex > -1)
			{
				indices[i] = mappedIndex;
			}
		}
	}
	
	
	/**
	 * Converts the index in the visual representation of a listbox to the index
	 * of the inner model.
	 * 
	 * @param listbox
	 *            listbox the {@link XdevListBox} to map index for
	 * @param index
	 *            the index of the inner model
	 * @return the corresponding index of the outer model
	 */
	private int mapIndexOfInnerModel(XdevListBox listbox, int index)
	{
		return ListModelWrapperUtils.getActualIndexAt(listbox.getModel(),index);
	}
	
	
	/**
	 * Converts the index inner model of a listbox to the index of the in the
	 * visual representation model.
	 * 
	 * @param listbox
	 *            listbox the {@link XdevListBox} to map index for
	 * @param index
	 *            the index of the outer model
	 * @return the corresponding index of the inner model
	 */
	@SuppressWarnings("unused")
	private int mapIndexOfOuterModel(XdevListBox listbox, int index)
	{
		return ListModelWrapperUtils.getIndexAt(listbox.getModel(),index);
	}
	
	
	/**
	 * Removes the specified items from the selected list.
	 * 
	 * @param selectedIndices
	 *            indices to remove from the selected list.
	 */
	void removeFromSelected(int[] selectedIndices)
	{
		int lowestSelectedIndex = 0;
		if(selectedIndices.length > 0)
		{
			lowestSelectedIndex = selectedIndices[0];
		}
		
		mapIndicesOfInnerModel(selectedList,selectedIndices);
		
		Arrays.sort(selectedIndices);
		
		selectedList.getItemList();
		final int length = selectedIndices.length;
		
		int availableItemListSizeStart = availableList.getItemList().size();
		
		for(int i = length - 1; i >= 0; i--)
		{
			Entry selectedEntry = null;
			final ItemList availableItemList = availableList.getItemList();
			switch(selectionMode)
			{
				case DISABLE_SELECTED:
					// enable corresponding entry in available list
					selectedEntry = selectedList.getItemList().get(selectedIndices[i]);
					Entry tempEntry = handler.onRemoveFromSelected(selectedEntry);
					List<?> list = availableItemList.getDataAsList();
					int index = list.indexOf(tempEntry.getData());
					if(index > -1)
					{
						Entry availableEntry = availableItemList.get(index);
						availableEntry.setEnabled(true);
					}
					availableList.repaint();
				break;
				case KEEP_SELECTED:
				break;
				case REMOVE_SELECTED:
					selectedEntry = selectedList.getItemList().get(selectedIndices[i]);
					
					Entry availableEntry = handler.onRemoveFromSelected(selectedEntry);
					
					if(!availableList.getItemList().contains(availableEntry))
					{
						int availableIndex = availableList.getMaxSelectionIndex();
						if(availableIndex > -1)
						{
							int availableInnerIndex = mapIndexOfInnerModel(availableList,
									availableIndex);
							availableItemList.add(availableInnerIndex + 1,availableEntry);
						}
						else
						{
							availableItemList.add(availableItemListSizeStart,availableEntry);
						}
					}
				
				break;
			}
			
			selectedList.getItemList().remove(selectedIndices[i]);
		}
		
		selectedList.setSelectedIndex(lowestSelectedIndex);
		
		notifyListSelectionListeners(selectedIndices);
	}
	
	
	/**
	 * Adds the specified items to the selected list.
	 * 
	 * @param availableIndices
	 *            indices to add to the selected list.
	 */
	void addToSelected(int... availableIndices)
	{
		addToSelected(availableIndices,true);
	}
	
	
	void addToSelected(int[] availableIndices, boolean useHandler)
	{
		int lowestAvailableIndex = 0;
		if(availableIndices.length > 0)
		{
			lowestAvailableIndex = availableIndices[0];
		}
		
		mapIndicesOfInnerModel(availableList,availableIndices);
		
		Arrays.sort(availableIndices);
		
		int length = availableIndices.length;
		
		int selectedItemListSizeStart = selectedList.getItemList().size();
		
		for(int i = length - 1; i >= 0; i--)
		{
			Entry availableEntry = availableList.getItemList().get(availableIndices[i]);
			
			if(selectionMode != SelectionMode.DISABLE_SELECTED || availableEntry.isEnabled())
			{
				Entry selectedEntry = null;
				
				if(useHandler)
				{
					// handler behavior can be overwritten
					try
					{
						selectedEntry = this.handler.onAddToSelected(availableEntry);
					}
					catch(OperationCanceledException e)
					{
						if(e.isCancelMultipleOperations())
						{
							break;
						}
						else
						{
							/*
							 * if user canceled add for this entry, proceed with
							 * next one
							 */
							continue;
						}
					}
				}
				else
				{
					selectedEntry = availableEntry.clone();
				}
				
				final ItemList selectedItemList = selectedList.getItemList();
				if(isMultipleSelectionsEnabled() || !selectedItemList.contains(selectedEntry))
				{
					int selectedIndex = selectedList.getMaxSelectionIndex();
					if(selectedIndex > -1)
					{
						int selectedInnerIndex = mapIndexOfInnerModel(selectedList,selectedIndex);
						selectedItemList.add(selectedInnerIndex + 1,selectedEntry);
					}
					else
					{
						selectedItemList.add(selectedItemListSizeStart,selectedEntry);
					}
				}
			}
			
			switch(selectionMode)
			{
				case DISABLE_SELECTED:
					for(int j = length - 1; j >= 0; j--)
					{
						availableEntry = availableList.getItemList().get(availableIndices[i]);
						availableEntry.setEnabled(false);
					}
				break;
				case KEEP_SELECTED:
				break;
				case REMOVE_SELECTED:
					availableList.getItemList().remove(availableIndices[i]);
				break;
			}
			availableList.setSelectedIndex(lowestAvailableIndex);
			
			int[] indicesForNotification = new int[length];
			int selectedIndex = selectedList.getMaxSelectionIndex();
			for(int j = 0; j < length; j++)
			{
				if(selectedIndex > -1)
				{
					indicesForNotification[j] = selectedIndex + 1 + j;
				}
				else
				{
					indicesForNotification[j] = selectedItemListSizeStart + j;
				}
			}
			notifyListSelectionListeners(indicesForNotification);
		}
		
	}
	
	
	/**
	 * Notifies all registered ListSelectionListener, if entries get added or
	 * removed.
	 * 
	 * @param indices
	 *            the list indices of the elements that get added or removed
	 */
	private void notifyListSelectionListeners(int[] indices)
	{
		int firstIndex = Integer.MAX_VALUE;
		int lastIndex = 0;
		for(int i = 0; i < indices.length; i++)
		{
			if(indices[i] < firstIndex)
			{
				firstIndex = indices[i];
			}
			if(indices[i] > lastIndex)
			{
				lastIndex = indices[i];
			}
		}
		ListSelectionEvent event = new ListSelectionEvent(this,firstIndex,lastIndex,false);
		for(ListSelectionListener listener : getListSelectionListeners())
		{
			listener.valueChanged(event);
		}
	}
	
	
	
	/**
	 * Custom Version of a {@link ListCellRenderer} for displaying disabled
	 * items.
	 * 
	 * @author XDEV Software
	 */
	private class DisabledCellRenderer implements ListCellRenderer
	{
		
		/**
		 * The default renderer used.
		 */
		private ListCellRenderer	enabledRenderer;
		
		
		/**
		 * Construct a new {@link DisabledCellRenderer}.
		 * 
		 * @param enabledRenderer
		 *            the default renderer
		 */
		public DisabledCellRenderer(ListCellRenderer enabledRenderer)
		{
			this.enabledRenderer = enabledRenderer;
		}
		
		
		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index,
				boolean isSelected, boolean cellHasFocus)
		{
			Component c = enabledRenderer.getListCellRendererComponent(list,value,index,isSelected,
					cellHasFocus);
			int innerModelIndex = mapIndexOfInnerModel((XdevListBox)list,index);
			Entry entry = ((XdevListBox)list).getItemList().get(innerModelIndex);
			if(!entry.isEnabled())
			{
				c.setEnabled(false);
			}
			return c;
		}
	}
	
	
	
	/**
	 * Stores the configuration for a button.
	 * 
	 * @author XDEV Software
	 * 
	 */
	private static class ButtonConfiguration
	{
		/**
		 * name.
		 */
		private String	name;
		/**
		 * icon.
		 */
		private Icon	icon;
		
		
		/**
		 * Creates a new {@link ButtonConfiguration}.
		 * 
		 * @param name
		 *            the name
		 * @param icon
		 *            the icon
		 */
		public ButtonConfiguration(String name, Icon icon)
		{
			this.name = name;
			this.icon = icon;
		}
		
		
		/**
		 * Creates a new {@link ButtonConfiguration}.
		 * 
		 * @param name
		 *            the name
		 */
		public ButtonConfiguration(String name)
		{
			this(name,null);
		}
		
		
		/**
		 * Creates a new {@link ButtonConfiguration}.
		 * 
		 * @param icon
		 *            the icon
		 */
		public ButtonConfiguration(Icon icon)
		{
			this("",icon);
		}
	}
	
	
	/**
	 * Helper method for retrieving a {@link Icon} by name.
	 * 
	 * @param name
	 *            the name of the icon
	 * @return the icon
	 */
	private static Icon getIcon(String name)
	{
		return IconsFactory.getImageIcon(XdevDualListBox.class,RELATIVE_ICON_PATH + name);
	}
	
	
	//
	// XdevFocusCycleComponent impl - start
	//
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getTabIndex()
	{
		return tabIndex;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTabIndex(int tabIndex)
	{
		if(this.tabIndex != tabIndex)
		{
			int oldValue = this.tabIndex;
			this.tabIndex = tabIndex;
			firePropertyChange(TAB_INDEX_PROPERTY,oldValue,tabIndex);
		}
	}
	
	
	//
	// XdevFocusCycleComponent impl - end
	//
	
	@Override
	public void setForeground(Color fg)
	{
		super.setForeground(fg);
		if(selectedList != null)
		{
			selectedList.setForeground(fg);
			availableList.setForeground(fg);
		}
		if(selectedFilterField != null)
		{
			selectedFilterField.getTextField().setForeground(fg);
			availableFilterField.getTextField().setForeground(fg);
		}
	}
	
	
	@Override
	public void setFont(Font font)
	{
		super.setFont(font);
		if(selectedList != null)
		{
			selectedList.setFont(font);
			availableList.setFont(font);
		}
		if(selectedFilterField != null)
		{
			selectedFilterField.getTextField().setFont(font);
			availableFilterField.getTextField().setFont(font);
		}
	}
	
	
	/**
	 * Registers a {@link ListSelectionListener} for this instance.
	 * 
	 * @param listener
	 *            a {@link ListSelectionListener}
	 */
	public void addListSelectionListener(ListSelectionListener listener)
	{
		listenerList.add(ListSelectionListener.class,listener);
	}
	
	
	/**
	 * Removes a specified {@link ListSelectionListener} from the registered
	 * listeners.
	 * 
	 * @param listener
	 *            a {@link ListSelectionListener}
	 */
	public void removeListSelectionListener(ListSelectionListener listener)
	{
		listenerList.remove(ListSelectionListener.class,listener);
	}
	
	
	/**
	 * Returns the registered listeners for this instance.
	 * 
	 * @return an array of {@link ListSelectionListener}s.
	 */
	public ListSelectionListener[] getListSelectionListeners()
	{
		return listenerList.getListeners(ListSelectionListener.class);
	}
	
	
	/**
	 * Set the selected indices manually.
	 * <p>
	 * All currently selected entries will be deselected.
	 * </p>
	 * <p>
	 * The method {@link #getAvailableAsItemList()} can be used to retrieve all
	 * currently available entries that are candidates to being selected.
	 * </p>
	 * 
	 * @param entries
	 *            a Collection of the {@link ItemList}s entries to set as
	 *            selected.
	 */
	public void setSelectedEntries(Collection<Entry> entries)
	{
		setSelectedEntries(entries,true);
	}
	
	
	void setSelectedEntries(Collection<Entry> entries, boolean useHandler)
	{
		deselectAll();
		
		this.addToSelected(getIndicesOfEntries(entries),useHandler);
	}
	
	
	private int[] getIndicesOfEntries(Collection<Entry> entries)
	{
		ItemList availableItems = this.getAvailableAsItemList();
		IntList indices = new IntList();
		for(int i = 0; i < availableItems.size(); i++)
		{
			if(entries.contains(availableItems.get(i)))
			{
				indices.add(i);
			}
		}
		return indices.toArray();
	}
	
	
	int[] getIndicesOfData(Collection data)
	{
		ItemList availableItems = this.getAvailableAsItemList();
		List availableData = availableItems.getDataAsList();
		int ac = availableData.size();
		IntList indices = new IntList();
		for(Object value : data)
		{
			for(int i = 0; i < ac; i++)
			{
				if(VirtualTable.equals(availableData.get(i),value))
				{
					indices.add(i);
					break;
				}
			}
		}
		indices.sort();
		return indices.toArray();
	}
	
	
	/**
	 * Deselect's all currently selected entries.
	 */
	public void deselectAll()
	{
		RemoveAllFromSelectedAction removeAllFromSelectedAction = new RemoveAllFromSelectedAction(
				"");
		removeAllFromSelectedAction.actionPerformed(new ActionEvent(this,-1,""));
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setEnabled(boolean enabled)
	{
		super.setEnabled(enabled);
		
		// en/- disable filter
		this.availableFilterField.setEnabled(enabled);
		this.selectedFilterField.setEnabled(enabled);
		
		// en/- disable lists
		this.selectedList.setEnabled(enabled);
		this.availableList.setEnabled(enabled);
		
		// en/- disable buttons
		for(JButton btn : this.controlButtonList)
		{
			btn.setEnabled(enabled);
		}
	}
	
}
