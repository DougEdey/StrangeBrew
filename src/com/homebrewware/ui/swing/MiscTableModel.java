/**
 *    Filename: MiscTableModel.java
 *     Version: 0.9.0
 * Description: Miscellaneous Table Model
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
import java.util.Locale;

import javax.swing.table.AbstractTableModel;

import com.homebrewware.Misc;
import com.homebrewware.Recipe;
import com.homebrewware.StringUtils;

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

    public Class<?> getColumnClass(int c) {
        if (null == getValueAt(0, c)) {
            return String.class;
        }
        return getValueAt(0, c).getClass();
    }

    public Object getValueAt(int row, int col) {

        try {
            switch (col) {
                case 0 :
                    return data.getMiscName(row);
                case 1 :
                    return new Double(StringUtils.format(data
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

/*  public Class getColumnClass(int c) {
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
