/**
 *    Filename: CellEditor.java
 *     Version: 0.9.0
 * Description: Cell Editor
 *     License: GPLv2
 *        Date: 2017-01-14
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

import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.EventObject;
import java.util.Locale;

import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTable;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import com.homebrewware.Mash;

public class CellEditor extends DefaultCellEditor implements FocusListener {

    private DefaultComboBoxModel model;

    public CellEditor(final JTextField textField) {
        super(textField);
        super.clickCountToStart = 1;
        textField.addFocusListener(this);

    }

    public boolean shouldSelectCell(EventObject anEvent) {
        return true;
    }

    // Automaticaly selectAll a field when it contains "0"
    public void focusGained(FocusEvent e) {
        JTextField textField = (JTextField)e.getComponent();
        String text = textField.getText();
        double value;

        try {
            NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
            Number number = format.parse(text.toString().trim());
            value = number.doubleValue();
        } catch (NumberFormatException ex) {
            return;
        } catch (ParseException m) {
            return;
        }

        if (value == 0)
            textField.selectAll();
    }

    public void focusLost(FocusEvent e) {
    }


}
