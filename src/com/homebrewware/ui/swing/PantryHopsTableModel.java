/**
 *    Filename: PantryHopsTableModel.java
 *     Version: 0.9.0
 * Description: Pantry Hops Table Model
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
import com.homebrewware.Hop;
import com.homebrewware.Recipe;
import com.homebrewware.StringUtils;

public class PantryHopsTableModel extends AbstractTableModel {

    static final private String[] columnNames = {"Hop", "Form", "Alpha", "Amount",
        "Units","Cost/u"};

    public List<Hop> data = new ArrayList<Hop>();

    public PantryHopsTableModel(List <Hop> d) {
        data = d;

    }

    public void addRow(Hop h) {
        data.add(h);
    }

    public void setData(List <Hop> d) {
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

    public String getDescriptionAt(int row) {
        if (row < 0)
            return "";
        return data.get(row).getDescription();
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Class<?> getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    public Object getValueAt(int row, int col) {
        // Hop h = (Hop) data.get(row);
        try {
            switch (col) {
                case 0 :
                    return data.get(row).getName();
                case 1 :
                    return data.get(row).getType();
                case 2 :
                    return new Double(data.get(row).getAlpha());
                case 3 :
                    return StringUtils.format(data.get(row).getStockAs(data.get(row).getUnits()), 2);
                case 4 :
                    return data.get(row).getUnits();
                case 5 :
                    return data.get(row).getCostPerU();

            }
        } catch (Exception e) {
        };
        return "";
    }

    public boolean isCellEditable(int row, int col) {
        //Note that the data/cell address is constant,
        //no matter where the cell appears onscreen.
        if (col < 1) {
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
          Debug.print("Value: " + value.toString() + " in row: " + row + " col: " + col);
        // Hop h = (Hop) data.get(row);
        try {
            switch (col) {
            case 0:
                data.get(row).setName(value.toString());
                // Shouldn't this re-set most of the data fields with base info
                if (value instanceof Hop) {
                    Hop h = (Hop)value;
                    data.get(row).setAlpha(h.getAlpha());
                }
                break;
            case 1:
                data.get(row).setType(value.toString());
                Debug.print("Changed the type of " + data.get(row).getName() + " to " + value.toString());
                break;
            case 2:
                try {
                    NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
                    Number number = format.parse(value.toString().trim());
                    data.get(row).setAlpha(number.doubleValue());
                } catch (NumberFormatException m) {
                    Debug.print("Could not parse "+ value.toString() + " as a double");

                }
                break;
            case 3:
                try {
                    NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
                    Number number = format.parse(value.toString().trim());
                    data.get(row).setStock(number.doubleValue());
                } catch (NumberFormatException m) {
                    Debug.print("Could not parse "+ value.toString() + " as a double");

                }
                break;
            case 4:
                data.get(row).setUnits(value.toString());
                break;
            case 5:
                data.get(row).setCost(value.toString());
                break;

            }

        } catch (Exception e) {
        }

        fireTableCellUpdated(row, col);
        fireTableDataChanged();

    }
}
