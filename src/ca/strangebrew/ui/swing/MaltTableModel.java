/*
 * Created on May 3, 2005
 */
package ca.strangebrew.ui.swing;

import javax.swing.table.AbstractTableModel;

import ca.strangebrew.Fermentable;
import ca.strangebrew.Recipe;
import ca.strangebrew.SBStringUtils;


class MaltTableModel extends AbstractTableModel {
	private final StrangeSwing app;

	private String[] columnNames = {"Malt", "Amount", "Units", "Points",
			"Lov", "Cost/U", "%"};

	private Recipe data = null;

	public MaltTableModel(StrangeSwing app) {
		data = app.myRecipe;
		this.app = app;
	}

	public void addRow(Fermentable m) {
		data.addMalt(m);
	}

	public void setData(Recipe d) {
		data = d;
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		if (data != null)
			return data.getMaltListSize();
		else
			return 0;
	}

	public String getColumnName(int col) {
		return columnNames[col];
	}
	
	public String getDescriptionAt(int row) {
		if (row < 0)
			return "";
		return data.getMaltDescription(row);
	}

	public Object getValueAt(int row, int col) {
		// Fermentable m = (Fermentable) data.get(row);
		try {
			switch (col) {
				case 0 :
					// indicate this is a sugar:
					String s = "";
					if (!data.getMaltMashed(row) && data.getMaltSteep(row))
						s = "*";
					return s + data.getMaltName(row);
				case 1 :
					return SBStringUtils.format(data
							.getMaltAmountAs(row, data.getMaltUnits(row)), 1);
				case 2 :
					return data.getMaltUnits(row);
				case 3 :
					return SBStringUtils.format(data.getMaltPppg(row), 3);
				case 4 :
					return SBStringUtils.format(data.getMaltLov(row), 0);
				case 5 :
					return new Double(data.getMaltCostPerU(row));
				case 6 :
					return SBStringUtils.format(data.getMaltPercent(row), 1);

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

		// Fermentable m = (Fermentable) data.get(row);
		try {
			switch (col) {
				case 0 :
					data.setMaltName(row, value.toString());
					break;
				case 1 :
					data.setMaltAmount(row, Double.parseDouble(value.toString()));					
					break;
				case 2 :
					// m.setUnits(value.toString());
					break;
				case 3 :
					data.setMaltPppg(row, Double.parseDouble(value.toString()));
					break;
				case 4 :
					data.setMaltLov(row, Double.parseDouble(value.toString()));
					break;
				case 5 :
					data.setMaltCost(row, value.toString());
					break;
				case 6 :
					// data.setMaltPercent(row, Double.parseDouble(value.toString()));
					break;

			}
		} catch (Exception e) {
		};
		app.myRecipe.calcMaltTotals();
		fireTableCellUpdated(row, col);
		fireTableDataChanged();		
		app.displayRecipe();
		
		
	}
}