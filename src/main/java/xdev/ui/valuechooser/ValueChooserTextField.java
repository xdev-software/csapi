package xdev.ui.valuechooser;

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


import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

import xdev.lang.NotNull;
import xdev.ui.ClientProperties;
import xdev.ui.TextComponentOwner;
import xdev.ui.XdevContainer;
import xdev.ui.event.DocumentAdapter;

import com.jidesoft.swing.JideButton;


/**
 * 
 * This class implements default behavior for a {@link ValueChooserField} based
 * on a textfield and a selection button.
 * <p>
 * This class contains an instance of type {@link ValueChooser} for providing
 * the functionality to choose a value. The concrete {@link ValueChooser}
 * implementation to be used has to be set in concrete subclasses of this class.
 * </p>
 * <p>
 * A optional clear button is available on this component to allow the user to
 * clear the current selection.
 * </p>
 * 
 * 
 * @param <T>
 *            The value's type
 * 
 * @since CS 1.0
 * @author XDEV Software (RHHF)
 * 
 * @see ValueChooserField
 */

public abstract class ValueChooserTextField<T> extends XdevContainer implements ValueChooserField,
		TextComponentOwner
{
	/**
	 * 
	 */
	private static final long	serialVersionUID		= 1L;
	
	/**
	 * name of chooser button (intended to be used by unit tests).
	 */
	static final String			CHOOSER_BUTTON_NAME		= "ChooserButton";
	
	/**
	 * name of chooser button (intended to be used by unit tests).
	 */
	static final String			CLEAR_BUTTON_NAME		= "ClearButton";
	
	/**
	 * name of chooser textfield (intended to be used by unit tests).
	 */
	static final String			CHOOSER_TEXTFIELD_NAME	= "ChooserTextField";
	
	/**
	 * {@link ValueChooser} instance.
	 */
	private ValueChooser<T>		chooser					= null;
	/**
	 * Textfield used to display the chosen value.
	 */
	private JTextField			textField				= null;
	/**
	 * Button to show the {@link ValueChooser}.
	 */
	private AbstractButton		chooserButton			= null;
	/**
	 * Button to clear the current value.
	 */
	private AbstractButton		clearButton				= null;
	
	/**
	 * Text to display, if no value is currently selected.
	 */
	private String				noValueSelectedText		= "";
	
	
	/**
	 * Creates a new {@link ValueChooserField} instance.
	 * 
	 * <p>
	 * A {@link ValueChooser} must be manually using the setter method
	 * {@link #setChooser(ValueChooser)}
	 * </p>
	 */
	public ValueChooserTextField()
	{
		this.initUI();
	}
	
	
	/**
	 * Creates a new {@link ValueChooserField} instance and sets the provided
	 * {@link ValueChooser}.
	 * 
	 * @param chooser
	 *            {@link ValueChooser} to use by this instance.
	 */
	public ValueChooserTextField(final ValueChooser<T> chooser)
	{
		this.chooser = chooser;
		this.initUI();
	}
	
	
	/**
	 * Initializes the component's UI part.
	 */
	private void initUI()
	{
		this.setLayout(new BorderLayout());
		
		this.textField = this.createTextField();
		
		/*
		 * FH, fix for 11873
		 */
		this.textField.putClientProperty(ClientProperties.FORMULAR_SKIP,Boolean.TRUE);
		
		this.textField.setEditable(false);
		// displays the valuechooser by left clicking on the textfield (#11957)
		this.textField.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if(e.getModifiers() == InputEvent.BUTTON1_MASK)
				{
					showValueChooser();
				}
			}
		});
		this.registerChangeListener();
		this.add(this.textField,BorderLayout.CENTER);
		
		this.chooserButton = this.createButton();
		this.chooserButton.addActionListener(new ShowValueChooser());
		
		this.clearButton = this.createClearButton();
		this.clearButton.setVisible(false);
		this.clearButton.addActionListener(new ClearAction());
		
		JPanel buttonsPanel = new JPanel(new FlowLayout(0,0,0));
		
		buttonsPanel.add(this.chooserButton);
		buttonsPanel.add(this.clearButton);
		
		this.add(buttonsPanel,BorderLayout.EAST);
		
		setFocusable(true);
	}
	
	
	/**
	 * Registers a {@link DocumentListener} on the text field to be notified on
	 * value changes.
	 */
	private void registerChangeListener()
	{
		getTextField().getDocument().addDocumentListener(new DocumentAdapter()
		{
			@Override
			public void insertUpdate(DocumentEvent e)
			{
				fireValueChanged();
			}
		});
	}
	
	
	/**
	 * Sends a {@link ChangeEvent} to all registered listeners.
	 */
	private void fireValueChanged()
	{
		Object[] listeners = listenerList.getListenerList();
		for(int i = listeners.length - 2; i >= 0; i -= 2)
		{
			if(listeners[i] == ChangeListener.class)
			{
				ChangeEvent event = new ChangeEvent(this);
				((ChangeListener)listeners[i + 1]).stateChanged(event);
			}
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setEnabled(boolean enabled)
	{
		/*
		 * lets the container and its children act like one component
		 */
		super.setEnabled(enabled);
		this.chooserButton.setEnabled(enabled);
		this.clearButton.setEnabled(enabled);
		this.textField.setEnabled(enabled);
	}
	
	
	
	/**
	 * {@link ActionListener} that shows the {@link ValueChooser}.
	 * 
	 * @since CS 1.0
	 * @author XDEV Software
	 * 
	 */
	protected class ShowValueChooser implements ActionListener
	{
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void actionPerformed(ActionEvent e)
		{
			showValueChooser();
		}
		
	}
	
	
	/**
	 * Called when user requests the display of the value chooser.
	 */
	private void showValueChooser()
	{
		checkChooser();
		chooser.reset();
		chooser.show(ValueChooserTextField.this);
	}
	
	
	
	/**
	 * {@link ActionListener} that clears the chosen value.
	 * 
	 * @since CS 1.0
	 * @author XDEV Software
	 * 
	 */
	protected class ClearAction implements ActionListener
	{
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void actionPerformed(ActionEvent e)
		{
			clearValue();
		}
		
	}
	
	
	/**
	 * Concrete implementations have to override this method to clear their
	 * value representation.
	 */
	protected abstract void clearValue();
	
	
	/**
	 * Displays the selected value.
	 * 
	 * <p>
	 * Is called when the {@link ValueChooser} is closed.
	 * </p>
	 */
	protected abstract void displaySelectedValue();
	
	
	/**
	 * Checks if a valid {@link ValueChooser} instance is available.
	 */
	private void checkChooser()
	{
		if(this.chooser == null || this.chooser.getInitState() == false)
		{
			throw new IllegalStateException("no chooser has been set");
		}
	}
	
	
	/**
	 * Returns the {@link JButton} that is used to call the {@link ValueChooser}
	 * .
	 * 
	 * <p>
	 * Can be overridden by subclasses to modify the button's appearance and
	 * functions. To set simple properties of the button use
	 * {@link #setButtonIcon(Icon)}, {@link #setButtonText(String)} and
	 * {@link #setButtonToolTipText(String)}.
	 * </p>
	 * 
	 * @return the {@link JButton} the is used to call the {@link ValueChooser}.
	 */
	protected AbstractButton createButton()
	{
		JideButton button = new JideButton("...");
		button.setButtonStyle(JideButton.TOOLBOX_STYLE);
		button.setName(CHOOSER_BUTTON_NAME);
		return button;
	}
	
	
	/**
	 * Returns the {@link JButton} that is used to clear the chosen value.
	 * 
	 * <p>
	 * Can be overridden by subclasses to modify the button's appearance and
	 * functions. To set simple properties of the button use
	 * </p>
	 * 
	 * @return the {@link JButton} the is used to call the {@link ValueChooser}.
	 */
	protected AbstractButton createClearButton()
	{
		JideButton button = new JideButton(" X ");
		button.setButtonStyle(JideButton.TOOLBOX_STYLE);
		button.setName(CLEAR_BUTTON_NAME);
		return button;
	}
	
	
	/**
	 * Returns the {@link JTextField} that is used to display the chosen value.
	 * 
	 * <p>
	 * Can be overridden by subclasses to modify the textfield's appearance and
	 * functions.
	 * </p>
	 * 
	 * @return the {@link JTextField} that is used to display the chosen value.
	 */
	protected JTextField createTextField()
	{
		JTextField textField = new JTextField();
		textField.setColumns(20);
		textField.setName(CHOOSER_TEXTFIELD_NAME);
		return textField;
	}
	
	
	/**
	 * Returns the {@link JTextField} used by this instance.
	 * 
	 * @return the {@link JTextField} used by this instance.
	 */
	protected JTextField getTextField()
	{
		return this.textField;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public JTextComponent getTextComponent()
	{
		return getTextField();
	}
	
	
	/**
	 * Returns the {@link AbstractButton} used to display the chooser.
	 * 
	 * @return the {@link AbstractButton} used to display the chooser.
	 */
	public AbstractButton getChooserButton()
	{
		return chooserButton;
	}
	
	
	/**
	 * Returns the {@link AbstractButton} used to reset the chooser.
	 * 
	 * @return the {@link AbstractButton} used to reset the chooser.
	 */
	public AbstractButton getClearButton()
	{
		return clearButton;
	}
	
	
	/**
	 * Returns the {@link ValueChooser} used by this instance.
	 * 
	 * @return the {@link ValueChooser} used by this instance.
	 */
	public ValueChooser<T> getChooser()
	{
		return chooser;
	}
	
	
	/**
	 * Sets the {@link ValueChooser} used by this instance.
	 * 
	 * @param chooser
	 *            {@link ValueChooser} to be used by this instance.
	 */
	public void setChooser(final ValueChooser<T> chooser)
	{
		this.chooser = chooser;
	}
	
	
	/**
	 * Sets the text that is displayed, if no value is currently selected.
	 * 
	 * @param noValueSelectedText
	 *            a {@link String} that is displayed, if no value is currently
	 *            selected.
	 */
	public void setNoValueSelectedText(final @NotNull String noValueSelectedText)
	{
		if(noValueSelectedText == null)
		{
			throw new IllegalArgumentException("noValueSelectedText must not be null");
		}
		this.noValueSelectedText = noValueSelectedText;
	}
	
	
	/**
	 * Returns the text that is displayed, if no value is currently selected.
	 * 
	 * @return the text that is displayed, if no value is currently selected.
	 */
	public String getNoValueSelectedText()
	{
		return this.noValueSelectedText;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void chooserClosed()
	{
		this.displaySelectedValue();
		
	}
	
	
	/**
	 * Sets the button's text.
	 * 
	 * @param text
	 *            {@link String} button text
	 */
	public void setButtonText(final String text)
	{
		this.chooserButton.setText(text);
	}
	
	
	/**
	 * Returns the button's text.
	 * 
	 * @return the buttons text
	 */
	public String getButtonText()
	{
		return this.chooserButton.getText();
	}
	
	
	/**
	 * Sets the botton's {@link Icon}.
	 * 
	 * @param icon
	 *            {@link Icon} the button's icon
	 */
	public void setButtonIcon(final Icon icon)
	{
		this.chooserButton.setIcon(icon);
	}
	
	
	/**
	 * Returns the default icon.
	 * 
	 * @return the default <code>Icon</code>
	 */
	public Icon getButtonIcon()
	{
		return this.chooserButton.getIcon();
	}
	
	
	/**
	 * Sets the button's tooltip.
	 * 
	 * @param tooltip
	 *            {@link String} the button's tooltip
	 */
	public void setButtonToolTipText(final String tooltip)
	{
		this.chooserButton.setToolTipText(tooltip);
	}
	
	
	/**
	 * Returns the tooltip string that has been set with
	 * <code>setToolTipText</code>.
	 * 
	 * @return the text of the tool tip
	 */
	public String getButtonToolTipText()
	{
		return this.chooserButton.getToolTipText();
	}
	
	
	/**
	 * Sets the visibility of the clear button (defaults to {@code false}).
	 * 
	 * @param visible
	 *            {@code boolean} visibility of clear button
	 */
	public void setClearButtonVisible(final boolean visible)
	{
		this.clearButton.setVisible(visible);
	}
	
	
	public boolean isClearButtonVisible()
	{
		return this.clearButton.isVisible();
	}
	
	
	/**
	 * Sets the clear button's text.
	 * 
	 * @param text
	 *            {@link String} clear button text
	 */
	public void setClearButtonText(final String text)
	{
		this.clearButton.setText(text);
	}
	
	
	/**
	 * Returns the button's text.
	 * 
	 * @return the buttons text
	 */
	public String getClearButtonText()
	{
		return this.clearButton.getText();
	}
	
	
	/**
	 * Sets the clear botton's {@link Icon}.
	 * 
	 * @param icon
	 *            {@link Icon} the clear button's icon
	 */
	public void setClearButtonIcon(final Icon icon)
	{
		this.clearButton.setIcon(icon);
	}
	
	
	/**
	 * Returns the default icon.
	 * 
	 * @return the default <code>Icon</code>
	 */
	public Icon getClearButtonIcon()
	{
		return this.clearButton.getIcon();
	}
	
	
	/**
	 * Sets the clear button's tooltip.
	 * 
	 * @param tooltip
	 *            {@link String} the reset button's tooltip
	 */
	public void setClearButtonToolTipText(final String tooltip)
	{
		this.clearButton.setToolTipText(tooltip);
	}
	
	
	/**
	 * Returns the tooltip string that has been set with
	 * <code>setToolTipText</code>.
	 * 
	 * @return the text of the tool tip
	 */
	public String getClearButtonToolTipText()
	{
		return this.clearButton.getToolTipText();
	}
	
	
	/**
	 * Resets the component.
	 */
	protected void reset()
	{
		this.getTextField().setText(this.getNoValueSelectedText());
	}
	
	
	/**
	 * Registers a {@link ChangeListener}.
	 * 
	 * @param cl
	 *            a {@link ChangeListener} to register for this component.
	 */
	public void addChangeListener(ChangeListener cl)
	{
		listenerList.add(ChangeListener.class,cl);
	}
	
	
	/**
	 * Unregisters a {@link ChangeListener}.
	 * 
	 * @param cl
	 *            a {@link ChangeListener} to unregister for this component
	 */
	public void removeChangeListener(ChangeListener cl)
	{
		listenerList.remove(ChangeListener.class,cl);
	}
	
	
	/**
	 * Delegates the call for setting the tooltip text to the embedded
	 * textfield.
	 * 
	 * @param text
	 *            tooltip text
	 */
	@Override
	public void setToolTipText(String text)
	{
		this.textField.setToolTipText(text);
	}
	
	
	/**
	 * Delegates the call to the embedded textfield.
	 */
	@Override
	public void requestFocus()
	{
		this.textField.requestFocus();
	}
	
	
	/**
	 * Delegates the call to the embedded textfield.
	 * 
	 * @param l
	 *            a {@link MouseListener}
	 */
	@Override
	public void addMouseListener(MouseListener l)
	{
		this.textField.addMouseListener(l);
	}
	
	
	@Override
	public void addMouseMotionListener(MouseMotionListener l)
	{
		this.textField.addMouseMotionListener(l);
	}
	
	
	/**
	 * Delegates the call to the embedded textfield.
	 * 
	 * @param l
	 *            a {@link KeyListener}
	 */
	@Override
	public synchronized void addKeyListener(KeyListener l)
	{
		this.textField.addKeyListener(l);
	}
	
	
	/**
	 * Delegates the call to the embedded textfield.
	 * 
	 * @param l
	 *            a {@link FocusListener}
	 */
	@Override
	public synchronized void addFocusListener(FocusListener l)
	{
		this.textField.addFocusListener(l);
	}
}
