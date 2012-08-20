/*
 * Created on Jun 6, 2005
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

import ca.strangebrew.Misc;
import ca.strangebrew.Recipe;
import ca.strangebrew.SBStringUtils;



class MiscTableModel extends AbstractTableModel {
	

	static final private String[] columnNames = {"Ingredient", "Amount", "Units", "Cost/U", "Stage", "Time"};

	private Recipe data = null;

	public MiscTableModel() {
	}

	public void addRow() {
		Misc m = new Misc();
		data.addMisc(m);
	}

	public void setData(Recipe d) {
		data = d;
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		if (data != null)
			return data.getMiscListSize();
		else
			return 0;
	}
	
	public String getDescriptionAt(int row) {
		if (row < 0)
			return "";
		return data.getMiscDescription(row);
	}

	public String getColumnName(int col) {
		return columnNames[col];
	}

	public Object getValueAt(int row, int col) {
		
		try {
			switch (col) {
				case 0 :
					return data.getMiscName(row);
				case 1 :
					return new Double(SBStringUtils.format(data
							.getMiscAmount(row), 1));
				case 2 :
					return data.getMiscUnits(row);
				case 3 :
					return new Double(data.getMiscCost(row));
				case 4 :
					return data.getMiscStage(row);
				case 5 :
					return new Integer(data.getMiscTime(row));

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
					data.setMiscName(row, value.toString());
					break;
				case 1 :
					try {
						NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
						Number number = format.parse(value.toString().trim());
						data.setMiscAmount(row, number.doubleValue());
					} catch (NumberFormatException m) {
						m.printStackTrace();
					}
					break;
				case 2 :
					data.setMiscUnits(row, value.toString());
					break;
				case 3 :
					try {
						NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
						Number number = format.parse(value.toString().trim());
						data.setMiscCost(row, number.doubleValue());
					} catch (NumberFormatException m) {
						m.printStackTrace();
					}
					break;
				case 4 :
					data.setMiscStage(row, value.toString());
					break;
				case 5 :
					try {
						NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
						Number number = format.parse(value.toString().trim());
						data.setMiscTime(row, number.intValue());
					} catch (NumberFormatException m) {
						m.printStackTrace();
					}
					break;

			}
		} catch (Exception e) {
		};
		
		fireTableCellUpdated(row, col);
		fireTableDataChanged();		
		
	}
}
