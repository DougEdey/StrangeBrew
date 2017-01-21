/**
 *    Filename: RecipeURLPopup.java
 *     Version: 0.9.0
 * Description: Recipe URL Popup
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
package com.homebrewware;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class RecipeURLPopup extends JDialog implements ActionListener {
    private JButton closeButton;
    private JTextArea taskOutput;

    public RecipeURLPopup(long id, String brewer, String name) {
        super();

        BoxLayout thisLayout = new BoxLayout(this.getContentPane(), javax.swing.BoxLayout.Y_AXIS);
        /*thisLayout.columnWeights = new double[]{0.1};
        thisLayout.columnWidths = new int[]{7};
        thisLayout.rowWeights = new double[]{0.7, 0.1, 0.1};
        thisLayout.rowHeights = new int[]{7, 7, 7};*/
        this.getContentPane().setLayout(thisLayout);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String baseURL = Options.getInstance().getProperty("cloudURL");
        if (!baseURL.startsWith("http://")) {
            baseURL = "http://" + baseURL;
        }
        //Create the demo's UI.
        closeButton = new JButton("Close");
        closeButton.setActionCommand("close");
        closeButton.addActionListener(this);
        closeButton.setEnabled(true);

        taskOutput = new JTextArea(5, 100);
        taskOutput.setMargin(new Insets(5,5,5,5));
        taskOutput.setEditable(false);
        taskOutput.append(name + " by " + brewer + "\n");
        taskOutput.append("Recipe URL is: \n");
        taskOutput.append(baseURL + "/recipes/" + id);

        JPanel panel = new JPanel();

        add(panel, BorderLayout.PAGE_START);
        add(new JScrollPane(taskOutput), BorderLayout.CENTER);
        panel.add(closeButton);
        this.pack();
        this.setVisible(true);
        this.setSize(400, 300);
        this.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width)/2 - getWidth()/2,
                (Toolkit.getDefaultToolkit().getScreenSize().height)/2 - getHeight()/2);
        this.setModal(true);
        this.setFocusable(true);

        //setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getActionCommand().equals("close")) {
            this.dispose();
        }
    }

}

