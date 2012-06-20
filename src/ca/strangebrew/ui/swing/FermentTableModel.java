package ca.strangebrew.ui.swing;

import javax.swing.table.AbstractTableModel;

import ca.strangebrew.FermentStep;
import ca.strangebrew.Recipe;

public class FermentTableModel extends AbstractTableModel {
	private static final String[] columnNames = {"Type", "Days", "Temp", "U"};
	private Recipe data = null;

	public FermentTableModel() {
	}

	public static String[] getColumnNames() {
		return columnNames;
	}
	
	public void addRow(FermentStep fs) {
		data.addFermentStep(fs);
	}

	public void setData(Recipe d) {
		data = d;
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		if (data != null)
			return data.getFermentStepSize();
		else
			return 0;
	}

	public String getColumnName(int col) {
		return columnNames[col];
	}
	
	public String getTypeAt(int row) {
		if (row < 0)
			return "";
		return data.getFermentStepType(row);
	}
	
	public Object getValueAt(int row, int col) {
		// Fermentable m = (Fermentable) data.get(row);
		try {
			switch (col) {
				case 0: // steep
					return data.getFermentStepType(row);
				case 1: // mash
					return new Integer(data.getFermentStepTime(row));
				case 2 :					
					return new Double(data.getFermentStepTemp(row));
				case 3:
					return data.getFermentStepTempU(row);					
			}
		} catch (Exception e) {
		};
		return "";
	}

	/*
	 * JTable uses this method to determine the default renderer/ editor for
	 * each cell. If we didn't implement this method, then the last column
	 * would contain text ("true"/"false"), rather than a check box.
	 */
	
	public Class<?> getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

	/*
	 * Don't need to implement this method unless your table's editable.
	 */
	public boolean isCellEditable(int row, int col) {
		//Note that the data/cell address is constant,
		//no matter where the cell appears onscreen.
		if (col == 0 || col == 1 || col == 2 || col == 3)
			return true;
		
		return false;
	}

	/*
	 * Don't need to implement this method unless your table's data can
	 * change.
	 */
	public void setValueAt(Object value, int row, int col) {

		// Fermentable m = (Fermentable) data.get(row);
		try {
			switch (col) {
				case 0:
					data.setFermentStepType(row, value.toString());
				case 1:
					try {
						data.setFermentStepTime(row, Integer.parseInt(value.toString()));
					} catch (NumberFormatException m) {
						m.printStackTrace();
					}
					break;
				case 2 :
					data.setFermentStepTemp(row, Double.parseDouble(value.toString()));
					break;
				case 3 :
					data.setFermentStepTempU(row, value.toString());
					break;	
			}
		} catch (Exception e) {
		};
		
		data.calcFermentTotals();
		fireTableCellUpdated(row, col);
		fireTableDataChanged();		
		//displayRecipe();		
	}
}
