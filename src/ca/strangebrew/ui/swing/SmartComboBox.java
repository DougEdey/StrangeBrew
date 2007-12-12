package ca.strangebrew.ui.swing;

import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.ComboBoxEditor;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;

/* This work is hereby released into the Public Domain.
 * To view a copy of the public domain dedication, visit
 * http://creativecommons.org/licenses/publicdomain/
 */
public class SmartComboBox extends PlainDocument {
	JComboBox comboBox;

	ComboBoxModel model;

	JTextComponent editor;

	// flag to indicate if setSelectedItem has been called
	// subsequent calls to remove/insertString should be ignored
	boolean selecting = false;

	boolean hidePopupOnFocusLoss;

	boolean hitBackspace = false;

	boolean hitBackspaceOnSelection;

	KeyListener editorKeyListener;

	FocusListener editorFocusListener;

	public SmartComboBox(final JComboBox comboBox) {
		this.comboBox = comboBox;
		model = comboBox.getModel();
	
		comboBox.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e) {
				if (e.getPropertyName().equals("editor"))
					configureEditor((ComboBoxEditor) e.getNewValue());
				if (e.getPropertyName().equals("model"))
					model = (ComboBoxModel) e.getNewValue();
			}
		});
		editorKeyListener = new KeyAdapter() {
    
			public void keyPressed(KeyEvent e) {
				long lastKeyTime = 0;
				String pattern = "";

				if (comboBox.isDisplayable())
					comboBox.setPopupVisible(true);

				char aKey = e.getKeyChar();
				// Find index of selected item
				int selIx = 01;
				Object sel = model.getSelectedItem();
				if (sel != null) {
					for (int i=0; i<model.getSize(); i++) {
						if (sel.equals(model.getElementAt(i))) {
							selIx = i;
							break;
						}
					}
				}

				// Get the current time
				long curTime = System.currentTimeMillis();

				// If last key was typed less than 300 ms ago, append to current
				// pattern
				if (curTime - lastKeyTime < 300) {
					pattern += ("" + aKey).toLowerCase();
				} else {
					pattern = ("" + aKey).toLowerCase();
				}

				// Save current time
				lastKeyTime = curTime;

				// Search forward from current selection
				for (int i=selIx+1; i<model.getSize(); i++) {
					String s = model.getElementAt(i).toString().toLowerCase();
					if (s.startsWith(pattern)) {
						model.setSelectedItem(s);
						return;
					}
				}

				// Search from top to current selection
				for (int i=0; i<selIx ; i++) {
					if (model.getElementAt(i) != null) {
						String s = model.getElementAt(i).toString().toLowerCase();
						if (s.startsWith(pattern)) {
							model.setSelectedItem(s);
							return;
						}
					}
				}
				return;        			
			}
		};
		
		configureEditor(comboBox.getEditor());

		// Handle initially selected object
		Object selected = comboBox.getSelectedItem();
		if (selected != null)
			setText(selected.toString());
	}

	public static void enable(JComboBox comboBox) {
		// has to be editable
		//comboBox.setEditable(true);
		// change the editor's document
		new SmartComboBox(comboBox);
	}

	void configureEditor(ComboBoxEditor newEditor) {
		if (editor != null) {
			editor.removeKeyListener(editorKeyListener);
			editor.removeFocusListener(editorFocusListener);
		}

		if (newEditor != null) {
			editor = (JTextComponent) newEditor.getEditorComponent();
			editor.addKeyListener(editorKeyListener);
			editor.addFocusListener(editorFocusListener);
			editor.setDocument(this);
		}
	}

	public void remove(int offs, int len) throws BadLocationException {
		// return immediately when selecting an item
		if (selecting)
			return;
		if (hitBackspace) {
			// user hit backspace => move the selection backwards
			// old item keeps being selected
			if (offs > 0) {
				if (hitBackspaceOnSelection)
					offs--;
			} else {
				// User hit backspace with the cursor positioned on the start => beep
				comboBox.getToolkit().beep(); // when available use: UIManager.getLookAndFeel().provideErrorFeedback(comboBox);
			}
		} else {
			super.remove(offs, len);
		}
	}

	public void insertString(int offs, String str, AttributeSet a)
			throws BadLocationException {
		// return immediately when selecting an item
		if (selecting)
			return;
		// insert the string into the document
		super.insertString(offs, str, a);
		// lookup and select a matching item
		Object item = lookupItem(getText(0, getLength()));
		if (item != null) {
			setSelectedItem(item);
		} else {
			// keep old item selected if there is no match
			item = comboBox.getSelectedItem();
			// imitate no insert (later on offs will be incremented by str.length(): selection won't move forward)
			offs = offs - str.length();
			// provide feedback to the user that his input has been received but can not be accepted
			comboBox.getToolkit().beep(); // when available use: UIManager.getLookAndFeel().provideErrorFeedback(comboBox);
		}
		setText(item.toString());
		// select the completed part
	}

	private void setText(String text) {
		try {
			// remove all text and insert the completed string
			super.remove(0, getLength());
			super.insertString(0, text, null);
		} catch (BadLocationException e) {
			throw new RuntimeException(e.toString());
		}
	}

	private void setSelectedItem(Object item) {
		selecting = true;
		model.setSelectedItem(item);
		selecting = false;
	}

	private Object lookupItem(String pattern) {
		Object selectedItem = model.getSelectedItem();
		// only search for a different item if the currently selected does not match
		if (selectedItem != null
				&& startsWithIgnoreCase(selectedItem.toString(), pattern)) {
			return selectedItem;
		} else {
			// iterate over all items
			for (int i = 0, n = model.getSize(); i < n; i++) {
				Object currentItem = model.getElementAt(i);
				// current item starts with the pattern?
				if (currentItem != null
						&& startsWithIgnoreCase(currentItem.toString(), pattern)) {
					return currentItem;
				}
			}
		}
		// no item starts with the pattern => return null
		return null;
	}

	// checks if str1 starts with str2 - ignores case
	private boolean startsWithIgnoreCase(String str1, String str2) {
		return str1.toUpperCase().startsWith(str2.toUpperCase());
	}
}
