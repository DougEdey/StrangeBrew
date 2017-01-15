/**
 *    Filename: HopsTableModel.java
 *     Version: 0.9.0
 * Description: Hops Table Model
 *     License: GPLv2
 *        Date: 2017-01-14
 *
 * @author unknown
 *
 * Copyright (c) 2017 Dallas Fletchall
 * @author Dallas Fletchall
 */

/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as
 * published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package com.homebrewware.ui.swing;

import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.table.AbstractTableModel;

import com.homebrewware.Debug;
import com.homebrewware.Hop;
import com.homebrewware.Recipe;
import com.homebrewware.StringUtils;

class HopsTableModel extends AbstractTableModel {
	final private StrangeSwing app;
	static final private String[] columnNames = {"Hop", "Form", "Alpha", "Amount",
			"Units", "Add", "Min", "IBU", "Cost/u"};

	private Recipe data = null;
	private int prevRow = -1;

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
					return StringUtils.format(data.getHopAmountAs(row, data.getHopUnits(row)), 2);
				case 4 :
					return data.getHopUnits(row);
				case 5 :
					return data.getHopAdd(row);
				case 6 :
					return new Double(data.getHopMinutes(row));
				case 7 :
					return StringUtils.format(data.getHopIBU(row), 1);
				case 8 :
					return StringUtils.format(data.getHopCostPerU(row), 2);

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

	@Override
	public Class<?> getColumnClass(int c) {
		Debug.print("Hop Column Class: " +getValueAt(0, c).getClass() );
		return getValueAt(0, c).getClass();
	}

	/*
	 * Don't need to implement this method unless your table's data can
	 * change.
	 */

	  public void setValueAt(Object value, int row, int col) {

	      if (prevRow == -1) {
	          prevRow = row;
	      }

		// Hop h = (Hop) data.get(row);
		try {
			switch (col) {
			case 0:
				data.setHopName(row, value.toString());
				// Shouldn't this re-set most of the data fields with base info
				if (value instanceof Hop) {
					Hop h = (Hop)value;
					data.setHopAlpha(row, h.getAlpha());
					data.setHopType(row, h.getType());
				}
				break;
			case 1:
				data.setHopType(row, value.toString());
				break;
			case 2:
				try {
					NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
					Number number = format.parse(value.toString().trim());
					data.setHopAlpha(row, number.doubleValue());
				} catch (NumberFormatException m) {
					Debug.print("Could not parse "+ value.toString() + " as a double");

				}
				break;
			case 3:
				try {
					NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
					Number number = format.parse(value.toString().trim());
					data.setHopAmount(row, number.doubleValue());
				} catch (NumberFormatException m) {
					Debug.print("Could not parse "+ value.toString() + " as a double");

				}
				break;
			case 4:
				Debug.print("Converting units on row " + row);
				// h.setUnits(value.toString());
				break;
			case 5:
				data.setHopAdd(row, value.toString());
				break;
			case 6:
				try {
					NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
					Number number = format.parse(value.toString().trim());
					data.setHopMinutes(row, number.intValue());
				} catch (NumberFormatException m) {
					Debug.print("Could not parse "+ value.toString() + " as a integer");

				}
				break;
			case 7:
				break;
			case 8:
				data.setHopCost(row, value.toString());
				break;
			}
		} catch (Exception e) {
		}

		if (prevRow != row) {
    		app.myRecipe.calcHopsTotals();
    		fireTableCellUpdated(row, col);
    		fireTableDataChanged();
    		app.displayRecipe();
		}
	}

}
