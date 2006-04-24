/*
 * Created on May 26, 2005
 *
 */
package ca.strangebrew.ui.swing;

/**
 * @author aavis
 *
 */
import javax.swing.table.AbstractTableModel;

import ca.strangebrew.Debug;
import ca.strangebrew.Mash;
import ca.strangebrew.Mash.MashStep;

class MashTableModel extends AbstractTableModel {
	// private final StrangeSwing app;
	private final MashManager mashManager;

	private String[] columnNames = {"Type", "Method", "Start Temp", "End Temp",
			"Ramp Min", "Step Min"};

	private Mash data = null;

	public MashTableModel(MashManager m) {
		mashManager = m;
	}
	
	public MashTableModel() {
		mashManager = null;
	}

	public void addRow(MashStep step) {
		data.addStep();
	}

	public void setData(Mash d) {
		data = d;
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		if (data != null)
			return data.getStepSize();
		else
			return 0;
	}

	public String getColumnName(int col) {
		return columnNames[col];
	}

	public Object getValueAt(int row, int col) {
		
		try {
			switch (col) {
				case 0 :
					return data.getStepType(row);
				case 1 :
					return data.getStepMethod(row);
				case 2 :					
					return new Double(data.getStepStartTemp(row)) ;
				case 3 :
					return new Double(data.getStepEndTemp(row)) ;
				case 4 :
					return new Integer(data.getStepRampMin(row));
				case 5 :
					return new Integer(data.getStepMin(row));

			}
		} catch (Exception e) {
			Debug.print(e.toString());
		};
		return "";
	}

	/*
	 * JTable uses this method to determine the default renderer/ editor for
	 * each cell. If we didn't implement this method, then the last column
	 * would contain text ("true"/"false"), rather than a check box.
	 */
	
/*	public Class getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}*/

	/*
	 * Don't need to implement this method unless your table's editable.
	 */
	public boolean isCellEditable(int row, int col) {
		//Note that the data/cell address is constant,
		//no matter where the cell appears onscreen.
		if (col < 0) {
			return false;
		} else {
			return true;
		}
	}

	/*
	 * Don't need to implement this method unless your table's data can
	 * change.
	 */
	public void setValueAt(Object value, int row, int col) {

		
		try {
			switch (col) {
				case 0 :
					data.setStepType(row, value.toString());
					break;
				case 1 :
					data.setStepMethod(row, value.toString());					
					break;
				case 2 :
					data.setStepStartTemp(row, Double.parseDouble(value.toString()));
					break;
				case 3 :
					data.setStepEndTemp(row, Double.parseDouble(value.toString()));
					break;
				case 4 :
					data.setStepRampMin(row, Integer.parseInt(value.toString()));
					break;
				case 5 :
					data.setStepMin(row, Integer.parseInt(value.toString()));
					break;


			}
		} catch (Exception e) {
			Debug.print(e.toString());
		};
		data.calcMashSchedule();
		fireTableCellUpdated(row, col);
		fireTableDataChanged();		
		mashManager.displayMash();
		
		
	}
}
