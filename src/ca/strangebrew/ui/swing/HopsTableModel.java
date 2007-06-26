/*
 * Created on May 3, 2005
 *
 */
package ca.strangebrew.ui.swing;

import javax.swing.table.AbstractTableModel;

import ca.strangebrew.Hop;
import ca.strangebrew.Recipe;
import ca.strangebrew.SBStringUtils;



class HopsTableModel extends AbstractTableModel {
	private final StrangeSwing app;

	private String[] columnNames = {"Hop", "Form", "Alpha", "Amount",
			"Units", "Add", "Min", "IBU", "Cost/u"};

	private Recipe data = null;

	public HopsTableModel(StrangeSwing app) {
		data = app.myRecipe;
		this.app = app;
	}

	public void addRow(Hop h) {
		data.addHop(h);
	}

	public void setData(Recipe d) {
		data = d;
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		if (data != null)
			return data.getHopsListSize();
		else
			return 0;
	}
	
	public String getDescriptionAt(int row) {
		if (row < 0)
			return "";
		return data.getHopDescription(row);
	}

	public String getColumnName(int col) {
		return columnNames[col];
	}

	public Object getValueAt(int row, int col) {
		// Hop h = (Hop) data.get(row);
		try {
			switch (col) {
				case 0 :
					return data.getHop(row);
				case 1 :
					return data.getHopType(row);
				case 2 :
					return new Double(data.getHopAlpha(row));
				case 3 :
					return SBStringUtils.format(data.getHopAmountAs(row, data.getHopUnits(row)), 2);
				case 4 :
					return data.getHopUnits(row);
				case 5 :
					return data.getHopAdd(row);
				case 6 :
					return new Double(data.getHopMinutes(row));
				case 7 :
					return SBStringUtils.format(data.getHopIBU(row), 1);
				case 8 :
					return SBStringUtils.format(data.getHopCostPerU(row), 2);

			}
		} catch (Exception e) {
		};
		return "";
	}

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

		// Hop h = (Hop) data.get(row);
		try {
			switch (col) {
			case 0:
				data.setHopName(row, value.toString());
				break;
			case 1:
				data.setHopType(row, value.toString());
				break;
			case 2:
				data.setHopAlpha(row, Double.parseDouble(value.toString()));
				break;
			case 3:
				data.setHopAmount(row, Double.parseDouble(value.toString()));
				break;
			case 4:
				// h.setUnits(value.toString());
				break;
			case 5:
				data.setHopAdd(row, value.toString());
				break;
			case 6:
				data.setHopMinutes(row, (int)Double.parseDouble(value.toString()));
				break;
			case 7:
				break;
			case 8:
				data.setHopCost(row, value.toString());
				break;
			}
		} catch (Exception e) {
		}
		;

		app.myRecipe.calcHopsTotals();
		fireTableCellUpdated(row, col);
		fireTableDataChanged();		
		app.displayRecipe();
	}
	 
}