/*
 * Created on May 26, 2005
 *
 */
package ca.strangebrew.ui.swing;

/**
 * @author aavis
 *
 */
import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.table.AbstractTableModel;

import ca.strangebrew.Debug;
import ca.strangebrew.Mash;
import ca.strangebrew.SBStringUtils;
import ca.strangebrew.Mash.MashStep;

class MashTableModel extends AbstractTableModel {
	// private final StrangeSwing app;


	private String[] columnNames = {"Type", "Method", "Start Temp", "End Temp",
			"Ramp Min", "Step Min", "Weight", "In", "Out", "Temp" };

	private Mash data = null;


	public MashTableModel() {
		
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

	public String getDirectionsAt(int row) {
		if (row < 0)
			return "";
		return data.getStepDirections(row);
	}
	
	public Object getValueAt(int row, int col) {
		
		try {
			switch (col) {
				case 0 :
					return data.getStepType(row);
				case 1 :
					return data.getStepMethod(row);
				case 2 :					
					return SBStringUtils.format(data.getStepStartTemp(row), 1);
				case 3 :
					return SBStringUtils.format(data.getStepEndTemp(row), 1) ;
				case 4 :
					return new Integer(data.getStepRampMin(row));
				case 5 :
					return new Integer(data.getStepMin(row));				
				case 6 :
					return SBStringUtils.format(data.getStepWeight(row), 1);
				case 7 :
					return SBStringUtils.format(data.getStepInVol(row), 1);
				case 8 :
					return SBStringUtils.format(data.getStepOutVol(row), 1);
				case 9 :
					return SBStringUtils.format(data.getStepTemp(row), 1);	


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
		if (col < 0 || col > 6) {
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
					try {
						NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
						Number number = format.parse(value.toString().trim());
						data.setStepStartTemp(row, number.doubleValue());
					} catch (NumberFormatException m) {
						m.printStackTrace();
					}
					break;
				case 3 :
					try {
						NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
						Number number = format.parse(value.toString().trim());
						data.setStepEndTemp(row, number.doubleValue());
					} catch (NumberFormatException m) {
						m.printStackTrace();
					}
					break;
				case 4 :
					try {
						NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
						Number number = format.parse(value.toString().trim());
						data.setStepRampMin(row, number.intValue());
					} catch (NumberFormatException m) {
						m.printStackTrace();
					}
					break;
				case 5 :
					try {
						NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
						Number number = format.parse(value.toString().trim());
						data.setStepMin(row, number.intValue());
					} catch (NumberFormatException m) {
						m.printStackTrace();
					}
					break;
				case 6 :
					try {
						NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
						Number number = format.parse(value.toString().trim());
						data.setStepWeight(row, number.doubleValue());
					} catch (NumberFormatException m) {
						m.printStackTrace();
					}
					break;
				default:
					Debug.print("Invalid column value! " + col);
					break;


			}
		} catch (Exception e) {
			Debug.print(e.toString());
		};
		data.calcMashSchedule();
		fireTableCellUpdated(row, col);
		fireTableDataChanged();
		
		
	}
}
