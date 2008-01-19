package ca.strangebrew.ui.swing;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.TableCellEditor;

public class SpinnerEditor extends AbstractCellEditor implements TableCellEditor {
	/**
	 * 
	 */

	final JSpinner spinner = new JSpinner();

	// Initializes the spinner.
	public SpinnerEditor() {

	}

	public SpinnerEditor(SpinnerNumberModel model) {
		spinner.setModel(model);
	}

	// Returns the spinners current value.
	public Object getCellEditorValue() {
		return spinner.getValue();
	}

	// Prepares the spinner component and returns it.
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		spinner.setValue(value);
		return spinner;
	}

	// Enables the editor only for double-clicks.
	public boolean isCellEditable(EventObject evt) {
		if (evt instanceof MouseEvent) {
			return ((MouseEvent) evt).getClickCount() >= 2;
		}
		return true;
	}
}