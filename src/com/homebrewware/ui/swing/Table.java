/**
 *    Filename: Table.java
 *     Version: 0.9.0
 * Description: Table
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

import java.util.Vector;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import com.homebrewware.Debug;
import com.homebrewware.Options;

/*
 * This table will save its column widths, and restore them when you restart.
 */
public class Table extends JTable {

    private String name = "";

    public Table(String n) {
        name = n;
    }

    public Table() {
        // TODO Auto-generated constructor stub
    }

    public Table(TableModel arg0) {
        super(arg0);
        // TODO Auto-generated constructor stub
    }

    public Table(TableModel arg0, TableColumnModel arg1) {
        super(arg0, arg1);
        // TODO Auto-generated constructor stub
    }

    public Table(int arg0, int arg1) {
        super(arg0, arg1);
        // TODO Auto-generated constructor stub
    }

    public Table(Vector arg0, Vector arg1) {
        super(arg0, arg1);
        // TODO Auto-generated constructor stub
    }

    public Table(Object[][] arg0, Object[] arg1) {
        super(arg0, arg1);
        // TODO Auto-generated constructor stub
    }

    public Table(TableModel arg0, TableColumnModel arg1, ListSelectionModel arg2) {
        super(arg0, arg1, arg2);
        // TODO Auto-generated constructor stub
    }


    public void setColumnWidths(Options p){
        if (name == "") return;
        setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        int colCount = getColumnCount();
        for (int i=0; i<colCount; i++){
            String key = ""+name+i;
            int k = p.getIProperty(key);
            if (k != 0)
                getColumnModel().getColumn(i).setPreferredWidth(k);
        }
        setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
    }

    public void saveColumnWidths(Options p){
        if (name == "") return;
        int colCount = getColumnCount();
        for (int j=0; j<colCount; j++){
            int k = getColumnModel().getColumn(j).getWidth();
            p.setIProperty(""+ name + j, k );
        }

    }

    @Override
    public Class<?> getColumnClass(int c) {
        Debug.print("Column Class " + c + " is " + getValueAt(0, c).getClass());
        return getValueAt(0, c).getClass();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
