/**
 *    Filename: PantryFermTableModel.java
 *     Version: 0.9.0
 * Description: Pantry Fermentables Table Model
 *     License: GPLv2
 *        Date: 2017-01-14
 *
 * Copyright (c) 2005 Drew Avis
 * @author aavis
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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.table.AbstractTableModel;

import com.homebrewware.Debug;
import com.homebrewware.Fermentable;

import com.homebrewware.StringUtils;

public class PantryFermTableModel extends AbstractTableModel {

	static final private String[] columnNames = {"S", "M", "Malt", "Stock", "Units", "Points",
			"Lov", "Cost/U", "%"};

	public List<Fermentable> data = new ArrayList<Fermentable>();

	public PantryFermTableModel() {

	}

	public void addRow(Fermentable m) {
		data.add(m);
	}

	public void setData(List<Fermentable> d) {
		data = d;
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		if (data != null)
			return data.size();
		else
			return 0;
	}

	public String getColumnName(int col) {
		return columnNames[col];
	}

	public String getDescriptionAt(int row) {
		if (row < 0)
			return "";
		return data.get(row).getDescription();
	}

	public Object getValueAt(int row, int col) {
		// Fermentable m = (Fermentable) data.get(row);
		try {
			switch (col) {
				case 0: // steep
					return new Boolean(data.get(row).getSteep());
				case 1: // mash
					return new Boolean (data.get(row).getMashed());
				case 2 :
					return data.get(row).getName();
				case 3 :

					return StringUtils.format(data.get(row)
							.getStockAs(data.get(row).getUnits()), 2);
				case 4 :
					return data.get(row).getUnits();
				case 5 :
					return StringUtils.format(data.get(row).getPppg(), 3);
				case 6 :
					return StringUtils.format(data.get(row).getLov(), 0);
				case 7 :

					return new Double(data.get(row).getCostPerU());

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
		if (col < 0 || col == 2) {
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
		Debug.print("Setting at " + row + "/" + col);
		// Fermentable m = (Fermentable) data.get(row);
		try {
			switch (col) {
				case 0:
					data.get(row).setSteep(new Boolean (value.toString()).booleanValue());
					break;
				case 1:
					data.get(row).setMashed(new Boolean(value.toString()).booleanValue());
					break;
				case 2 :

					data.get(row).setName(value.toString());

					// Shouldn't this re-set most of the data fields with base info
					if (value instanceof Fermentable) {
						Fermentable m = (Fermentable)value;
						data.get(row).setPppg(m.getPppg());
						data.get(row).setLov(m.getLov());
						data.get(row).setSteep(m.getSteep());
						data.get(row).setMashed(m.getMashed());
					}
					break;
				case 3 :
					try {
						Debug.print("Setting Stock to: "+value.toString());
						NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
						Number number = format.parse(value.toString().trim());
						data.get(row).setStock(number.doubleValue());
						Debug.print("Stock set to: " + data.get(row).getStockAs(data.get(row).getUnits()));
					} catch (NumberFormatException m) {
						Debug.print("Could not parse "+ value.toString() + " as a double");

					}
					//sort by the malt amount

					break;
				case 4 :
					 data.get(row).setUnits(value.toString());
					break;
				case 5 :
					try {
						NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
						Number number = format.parse(value.toString().trim());
						data.get(row).setPppg(number.doubleValue());
					} catch (NumberFormatException m) {
						Debug.print("Could not parse "+ value.toString() + " as a double");

					}
					break;
				case 6 :
					try {
						NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
						Number number = format.parse(value.toString().trim());
						data.get(row).setLov(number.doubleValue());
					} catch (NumberFormatException m) {
						Debug.print("Could not parse "+ value.toString() + " as a double");

					}
					break;
				case 7 :

					data.get(row).setCost(value.toString());
					break;


			}
		} catch (Exception e) {
		};

		fireTableCellUpdated(row, col);
		fireTableDataChanged();

	}
}
