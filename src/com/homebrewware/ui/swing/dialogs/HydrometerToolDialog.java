/**
 *    Filename: HydrometerToolDialog.java
 *     Version: 0.9.0
 * Description: Hydrometer Tool Dialog
 *     License: GPLv2
 *        Date: 2017-01-14
 *
 * @author unkown
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
package com.homebrewware.ui.swing.dialogs;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.homebrewware.Options;
import com.homebrewware.StringUtils;
import com.homebrewware.BrewCalcs;

public class HydrometerToolDialog extends javax.swing.JDialog implements ActionListener, FocusListener,KeyListener {
    private final JPanel mainPanel = new JPanel();
    private final JLabel lMeasuredGravity = new JLabel();
    private final JLabel lMeasuredTemp = new JLabel();
    private final JLabel lCorrectedGravity = new JLabel();
    private final JLabel lCalibratedTemp = new JLabel();
    private final JLabel tempUnits1 = new JLabel();
    private final JLabel blankGrid1 = new JLabel();
    private final JLabel tempUnits2 = new JLabel();
    private final JLabel blankGrid2 = new JLabel();
    private final JTextField measuredGravity = new JTextField();
    private final JTextField measuredTemp = new JTextField();
    private final JTextField correctedGravity = new JTextField();
    private final JTextField calibratedTemp = new JTextField();
    private final JButton okButton = new JButton();
    private final Options opts = Options.getInstance();

    public static void main(String[] args) {
        final JFrame frame = new JFrame();
        HydrometerToolDialog inst = new HydrometerToolDialog(frame);
        inst.setVisible(true);
    }

    public HydrometerToolDialog(JFrame frame) {
        super(frame);
        initGUI();
        displayCalcs();
    }

    private void displayCalcs() {
        double mT = 0;
        double mSG = 0;
        double calT = 0;
        double cSG;

        // Just ignore bad input
        try {
            mSG = Double.parseDouble(measuredGravity.getText());
            mT = Double.parseDouble(measuredTemp.getText());
            calT = Double.parseDouble(calibratedTemp.getText());
        } catch (Exception e) {
            correctedGravity.setText("Invalid");
            return;
        }

        if (opts.getProperty("optMashTempU").equals("F")) {
            mT = BrewCalcs.fToC(mT);
            calT = BrewCalcs.fToC(calT);
        }

        cSG = BrewCalcs.hydrometerCorrection(mT, mSG, calT);

        correctedGravity.setText(StringUtils.format(cSG, 3));
    }

    private void initGUI() {
        try {

        final GridBagLayout gridBag = new GridBagLayout();
        final GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;

        mainPanel.setLayout(gridBag);
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        mainPanel.setBorder(BorderFactory.createTitledBorder("Hydrometer Adjustment Tool"));
        this.setTitle("Hydrometer Tool");
        this.setSize(258, 294);
        mainPanel.setPreferredSize(new java.awt.Dimension(250, 235));

        // Set temp units text
        tempUnits1.setText(" " + opts.getProperty("optMashTempU"));
        tempUnits2.setText(" " + opts.getProperty("optMashTempU"));

        // First Line
        {
            mainPanel.add(lMeasuredGravity, c);
            lMeasuredGravity.setText("Measured Gravity: ");

            mainPanel.add(measuredGravity, c);
            measuredGravity.setText("1.040");
            measuredGravity.setPreferredSize(new java.awt.Dimension(55, measuredGravity.getFont().getSize()*2));
            measuredGravity.addFocusListener(this);
            measuredGravity.addActionListener(this);
            measuredGravity.addKeyListener(this);

            c.gridwidth = GridBagConstraints.REMAINDER;
            mainPanel.add(blankGrid1, c);
        }

        // Second Line
        {
            c.gridwidth = 1;
            mainPanel.add(lMeasuredTemp, c);
            lMeasuredTemp.setText("Measured Temp: ");

            mainPanel.add(measuredTemp, c);
            if (opts.getProperty("optMashTempU").equals("F")) {
                measuredTemp.setText("60.0");
            } else {
                measuredTemp.setText("15.0");
            }
            measuredTemp.setPreferredSize(new java.awt.Dimension(55, measuredTemp.getFont().getSize()*2));
            measuredTemp.addFocusListener(this);
            measuredTemp.addActionListener(this);
            measuredTemp.addKeyListener(this);

            c.gridwidth = GridBagConstraints.REMAINDER;
            mainPanel.add(tempUnits1, c);
        }

        // Third Line - increase vertical spaceing to offset from user entries
        {
            final Insets i = new Insets(10, 0, 0, 0);
            c.insets = i;
            c.gridwidth = 1;
            mainPanel.add(lCorrectedGravity, c);
            lCorrectedGravity.setText("Corrected Gravity: ");

            mainPanel.add(correctedGravity, c);
            correctedGravity.setText("0.0");
            correctedGravity.setEditable(false);
            correctedGravity.setPreferredSize(new java.awt.Dimension(55, correctedGravity.getFont().getSize()*2));
            correctedGravity.addFocusListener(this);
            correctedGravity.addActionListener(this);

            c.gridwidth = GridBagConstraints.REMAINDER;
            mainPanel.add(blankGrid2, c);
        }

        // Fourth Line
        {
            c.gridwidth = 1;
            mainPanel.add(lCalibratedTemp, c);
            lCalibratedTemp.setText("Calibrated Temp: ");
            if (opts.getProperty("optMashTempU").equals("F")) {
                calibratedTemp.setText("60.0");
            } else {
                calibratedTemp.setText("15.0");
            }
            mainPanel.add(calibratedTemp, c);
            calibratedTemp.setPreferredSize(new java.awt.Dimension(55, calibratedTemp.getFont().getSize()*2));
            calibratedTemp.addFocusListener(this);
            calibratedTemp.addActionListener(this);

            c.gridwidth = GridBagConstraints.REMAINDER;
            mainPanel.add(tempUnits2, c);
        }

        // Add in an OK button at the end
        {
            c.gridx = 1;
            c.fill = GridBagConstraints.NONE;
            c.gridwidth = GridBagConstraints.REMAINDER;
            mainPanel.add(okButton, c);
            okButton.setText("OK");
            okButton.addActionListener(this);
        }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent e) {

        Object o = e.getSource();

        if (o == okButton) {
            setVisible(false);
            dispose();
        }
        else
            displayCalcs();
    }

    public void focusGained(FocusEvent e) {
        // do nothing, we don't need this event
    }

    public void focusLost(FocusEvent e) {
        // trigger actionPerformed for this widget
        Object o = e.getSource();
        ActionEvent evt = new ActionEvent(o, 1, "");
        actionPerformed(evt);
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
        displayCalcs();
    }

}
