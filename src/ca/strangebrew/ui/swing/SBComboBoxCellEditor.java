package ca.strangebrew.ui.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.table.TableCellEditor;

import ca.strangebrew.Debug;
import ca.strangebrew.Mash;

public class SBComboBoxCellEditor extends AbstractCellEditor implements
		ActionListener, TableCellEditor, Serializable {

	private JComboBox comboBox;

	public SBComboBoxCellEditor(JComboBox comboBox) {
		this.comboBox = comboBox;
		this.comboBox.putClientProperty("JComboBox.isTableCellEditor",
				Boolean.TRUE);
		// hitting enter in the combo box should stop cellediting (see below)
		this.comboBox.addActionListener(this);
		// remove the editor's border - the cell itself already has one
		((JComponent) comboBox.getEditor().getEditorComponent())
				.setBorder(null);
	}

	private void setValue(Object value) {
		comboBox.setSelectedItem(value);
	}

	// Implementing ActionListener
	public void actionPerformed(java.awt.event.ActionEvent e) {
		// Selecting an item results in an actioncommand "comboBoxChanged".
		// We should be reacting to these since the drop down doesn't update immediately. 
		// Hitting enter results in an actioncommand "comboBoxEdited" 
		// But checking for isValid() tells us if an item was selected
		// SB ComboBoxes are meant to be JComboBoxes
		
		if (e.getSource() instanceof JComboBox) {
			JComboBox source = (JComboBox) e.getSource();
			if (source.isValid()) {
				stopCellEditing();
				return;
			}
		}
		// Fall back if needed
		if (e.getActionCommand().equals("comboBoxEdited")) {
			stopCellEditing();
		}
	}

	// Implementing CellEditor
	public Object getCellEditorValue() {
		return comboBox.getSelectedItem();
	}

	public boolean stopCellEditing() {
		if (comboBox.isEditable()) {
			// Notify the combo box that editing has stopped (e.g. User pressed
			// F2)
			comboBox.actionPerformed(new ActionEvent(this, 0, ""));
		}
		fireEditingStopped();
		return true;
	}

	// Implementing TableCellEditor
	public java.awt.Component getTableCellEditorComponent(
			javax.swing.JTable table, Object value, boolean isSelected,
			int row, int column) {
		
		if (table.getName().equals("MashTable")) {
			// Check to see if we have a row and column for the sparge type
			if (column == 1) {
				if (row == 0) {
					
				} else {
					comboBox.removeAllItems();
					
					int countSparge = 0;
					
					for (int i = 0; i < table.getRowCount(); i++) {
						if (((String)table.getValueAt(i, 0)).equals(Mash.SPARGE)) {
							countSparge++;
						}
					}
					
					 // We are in the types column on the mash table
					if (((String) table.getValueAt(row, 0)).equals(Mash.SPARGE)) {
						if (countSparge == 1) {
							comboBox.addItem(Mash.FLY);
						} else {
							comboBox.addItem(Mash.SPARGE);
						}
					} else {
						for (String s : Mash.methods) {
							comboBox.addItem(s);
						}
					}
				}
			}
		}
		setValue(value);
		return comboBox;
	}
}