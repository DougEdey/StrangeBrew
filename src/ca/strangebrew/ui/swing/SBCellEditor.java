package ca.strangebrew.ui.swing;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.EventObject;

import javax.swing.DefaultCellEditor;
import javax.swing.JTextField;

public class SBCellEditor extends DefaultCellEditor implements FocusListener {

	public SBCellEditor(final JTextField textField) {			
		super(textField);
		super.clickCountToStart = 1;
		textField.addFocusListener(this);			
	}

	public boolean shouldSelectCell(EventObject anEvent) {
		return true;
	}

	// Automaticaly selectAll a field when it contains "0"
	public void focusGained(FocusEvent e) {
		JTextField textField = (JTextField)e.getComponent(); 
		String text = textField.getText();
		double value;
		
		try {
			value = Double.parseDouble(text);
		} catch (NumberFormatException ex) {
			return;
		}

		if (value == 0)
			textField.selectAll();
	}
	
	public void focusLost(FocusEvent e) {			
	}

}
