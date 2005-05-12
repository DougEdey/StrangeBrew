/*
 * Created on May 3, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package strangebrew.ui.swing;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import strangebrew.Hop;


class HopsTableModel extends AbstractTableModel {
	private final NewSwingApp app;

	private String[] columnNames = {"Hop", "Type", "Alpha", "Amount",
			"Units", "Add", "Min", "IBU", "Cost/u"};

	private ArrayList data = null;

	public HopsTableModel(NewSwingApp app) {
		data = new ArrayList();
		this.app = app;
	}

	public void addRow(Hop h) {
		data.add(h);
	}

	public void setData(ArrayList d) {
		data = d;
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		return data.size();
	}

	public String getColumnName(int col) {
		return columnNames[col];
	}

	public Object getValueAt(int row, int col) {
		Hop h = (Hop) data.get(row);
		try {
			switch (col) {
				case 0 :
					return h.getName();
				case 1 :
					return h.getType();
				case 2 :
					return new Double(h.getAlpha());
				case 3 :
					return new Double(h.getAmountAs(h.getUnits()));
				case 4 :
					return new Double(h.getUnits());
				case 5 :
					return new Double(h.getAdd());
				case 6 :
					return new Double(h.getMinutes());
				case 7 :
					return new Double(this.app.df1.format(h.getIBU()));
				case 8 :
					return new Double(h.getCostPerU());

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

		Hop h = (Hop) data.get(row);
		try {
			switch (col) {
			case 0:
				h.setName(value.toString());
				break;
			case 1:
				h.setType(value.toString());
				break;
			case 2:
				h.setAlpha(Double.parseDouble(value.toString()));
				break;
			case 3:
				h.setAmount(Double.parseDouble(value.toString()));
				break;
			case 4:
				h.setUnits(value.toString());
				break;
			case 5:
				h.setAdd(value.toString());
				break;
			case 6:
				h.setMinutes(Integer.parseInt(value.toString()));
				break;
			case 7:
				break;
			case 8:
				h.setCost(value.toString());
				break;
			}
		} catch (Exception e) {
		}
		;

		fireTableCellUpdated(row, col);
		fireTableDataChanged();
		app.myRecipe.calcHopsTotals();
		app.displayRecipe();
	}
	 
}