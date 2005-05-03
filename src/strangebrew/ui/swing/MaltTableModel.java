/*
 * Created on May 3, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package strangebrew.ui.swing;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import strangebrew.Fermentable;


class MaltTableModel extends AbstractTableModel {
	private final NewSwingApp app;

	private String[] columnNames = {"Malt", "Amount", "Units", "Points",
			"Lov", "Cost/U", "%"};

	private ArrayList data = null;

	public MaltTableModel(NewSwingApp app) {
		data = new ArrayList();
		this.app = app;
	}

	public void addRow(Fermentable m) {
		data.add(m);
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
		Fermentable m = (Fermentable) data.get(row);
		try {
			switch (col) {
				case 0 :
					return m.getName();
				case 1 :
					return new Double(this.app.df1.format(m
							.getAmountAs(m.getUnits())));
				case 2 :
					return m.getUnits();
				case 3 :
					return new Double(m.getPppg());
				case 4 :
					return new Double(m.getLov());
				case 5 :
					return new Double(m.getCostPerU());
				case 6 :
					return this.app.df1.format(new Double(m.getPercent()));

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
	public Class getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

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

		Fermentable m = (Fermentable) data.get(row);
		try {
			switch (col) {
				case 0 :
					m.setName(value.toString());
					/* if (NewSwingApp.DEBUG){
						System.out.println("value is:" + value);
					}*/
				case 1 :
					m.setAmount(Double.parseDouble(value.toString()));
				case 2 :
					m.setUnits(value.toString());
				case 3 :
					m.setPppg(Double.parseDouble(value.toString()));
				case 4 :
					m.setLov(Double.parseDouble(value.toString()));
				case 5 :
					m.setCost(Double.parseDouble(value.toString()));
				case 6 :
					m.setPercent(Double.parseDouble(value.toString()));

			}
		} catch (Exception e) {
		};

		fireTableCellUpdated(row, col);
		
	}
}