/**
 *    Filename: MaltTableModel.java
 *     Version: 0.9.0
 * Description: Malt Table Model
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
import com.homebrewware.Fermentable;
import com.homebrewware.Options;
import com.homebrewware.Recipe;
import com.homebrewware.StringUtils;

class MaltTableModel extends AbstractTableModel {
    final private StrangeSwing app;
    static final private String[] columnNames = {"S", "M", "Malt", "Amount", "Units", "Points",
            "Lov", "Cost/U", "%"};

    private Recipe data = null;
    private int prevRow = -1;

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
                case 0: // steep
                    return new Boolean(data.getMaltSteep(row));
                case 1: // mash
                    return new Boolean (data.getMaltMashed(row));
                case 2 :
                    return data.getFermentable(row);
                case 3 :
                    return StringUtils.format(data
                            .getMaltAmountAs(row, data.getMaltUnits(row)), 2);
                case 4 :
                    return data.getMaltUnits(row);
                case 5 :
                    return StringUtils.format(data.getMaltPppg(row), 3);
                case 6 :
                    Debug.print(row + "Malt Lov: " + data.getMaltLov(row));
                    return StringUtils.format(data.getMaltLov(row), 0);
                case 7 :
                    return new Double(data.getMaltCostPerUAs(row, data.getMaltUnits()));
                case 8 :
                    return StringUtils.format(data.getMaltPercent(row), 1);

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
        Debug.print("Malt Column " + c + " Class: " + getValueAt(0, c).getClass() );
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
        Debug.print("Setting at " + row + "/" + col);
        // Fermentable m = (Fermentable) data.get(row);
        if (prevRow == -1) {
            prevRow = row;
        }

        try {
            switch (col) {
                case 0:
                    data.setMaltSteep(row, new Boolean (value.toString()).booleanValue());
                    break;
                case 1:
                    data.setMaltMashed(row, new Boolean(value.toString()).booleanValue());
                    break;
                case 2 :

                    data.setMaltName(row, value.toString());


                    // Shouldn't this re-set most of the data fields with base info
                    if (value instanceof Fermentable) {
                        Fermentable m = (Fermentable)value;
                        data.setMaltPppg(row, m.getPppg());
                        Debug.print("Setting value from name: " + m.getLov());
                        data.setMaltLov(row, m.getLov());
                        data.setMaltSteep(row, m.getSteep());
                        data.setMaltMashed(row, m.getMashed());
                        data.setMaltFerments(row, m.ferments());
                    }
                    break;
                case 3 :
                    try {
                        NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
                        Number number = format.parse(value.toString().trim());
                        data.setMaltAmount(row, number.doubleValue());
                    } catch (NumberFormatException m) {
                        Debug.print("Could not parse "+ value.toString() + " as a double");

                    }
                    //sort by the malt amount

                    break;
                case 4 :
                    Debug.print("Setting malt units");
                    if (value instanceof Fermentable) {
                        Fermentable m = (Fermentable)value;
                        data.setMaltCost(row, m.getCostPerUAs(m.getUnits()));
                    }
                    fireTableCellUpdated(row, 7);
                    // m.setUnits(value.toString());

                    break;
                case 5 :
                    try {
                        NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
                        Number number = format.parse(value.toString().trim());
                        data.setMaltPppg(row, number.doubleValue());
                    } catch (NumberFormatException m) {
                        Debug.print("Could not parse "+ value.toString() + " as a double");

                    }
                    break;
                case 6 :
                    try {
                        NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
                        Number number = format.parse(value.toString().trim());
                        Debug.print("Setting malt lov from lov setting; " + number.doubleValue());
                        data.setMaltLov(row, number.doubleValue());
                    } catch (NumberFormatException m) {
                        Debug.print("Could not parse "+ value.toString() + " as a double");

                    }
                    break;
                case 7 :

                    data.setMaltCost(row, value.toString());
                    break;
                case 8 :
                    // data.setMaltPercent(row, Double.parseDouble(value.toString()));
                    break;

            }
        } catch (Exception e) {
            e.printStackTrace();
        };

        Debug.print("FireTableCellUpdated");

        // Don't update unless the row has changed
        if (row != prevRow) {
            app.myRecipe.calcMaltTotals();
            System.out.println("Prev: " + prevRow + " : Cur: " + row);
            fireTableCellUpdated(row, col);
            fireTableDataChanged();
            app.displayRecipe();
            prevRow = row;
        }
    }
}
