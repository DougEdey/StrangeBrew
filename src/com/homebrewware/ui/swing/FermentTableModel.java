/**
 *    Filename: FermentTableModel.java
 *     Version: 0.9.0
 * Description: Ferment Table Model
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

import com.homebrewware.FermentStep;
import com.homebrewware.Recipe;

public class FermentTableModel extends AbstractTableModel {
	private static final String[] columnNames = {"Type", "Days", "Temp", "U"};
	private Recipe data = null;
	private int prevRow = -1;

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

	    if (prevRow == -1) {
	        prevRow = row;
	    }
		// Fermentable m = (Fermentable) data.get(row);
		try {
			switch (col) {
				case 0:
					data.setFermentStepType(row, value.toString());
				case 1:
					try {
						NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
						Number number = format.parse(value.toString().trim());
						data.setFermentStepTime(row, number.intValue());
					} catch (NumberFormatException m) {
						m.printStackTrace();
					}
					break;
				case 2 :
					try {
						NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
						Number number = format.parse(value.toString().trim());
						data.setFermentStepTemp(row, number.doubleValue());
					} catch (NumberFormatException m) {
						m.printStackTrace();
					}
					break;
				case 3 :
					data.setFermentStepTempU(row, value.toString());
					break;
			}
		} catch (Exception e) {
		};

		if (prevRow != row) {
    		data.calcFermentTotals();
    		// Don't fire off the updates until the row is deselected

    		fireTableCellUpdated(row, col);
    		fireTableDataChanged();
		}
		//displayRecipe();
	}
}
