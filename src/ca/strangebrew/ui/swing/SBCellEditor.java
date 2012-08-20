package ca.strangebrew.ui.swing;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.EventObject;
import java.util.Locale;

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
			NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
			Number number = format.parse(text.toString().trim());
			value = number.doubleValue();
		} catch (NumberFormatException ex) {
			return;
		} catch (ParseException m) {
			return;
		}

		if (value == 0)
			textField.selectAll();
	}
	
	public void focusLost(FocusEvent e) {			
	}

}
