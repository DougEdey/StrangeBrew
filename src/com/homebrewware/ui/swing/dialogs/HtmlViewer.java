/**
 *    Filename: HtmlViewer.java
 *     Version: 0.9.0
 * Description: HTML Viewer
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
package com.homebrewware.ui.swing.dialogs;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;

public class HtmlViewer extends javax.swing.JDialog {
    private JPanel jPanel1;
    private JEditorPane jEditorPane1;
    private JScrollPane jScrollPane1;
    private JButton jButton2;
    private JButton jButton1;
    private JToolBar jToolBar1;
    private JPanel jPanel5;
    private JPanel jPanel4;
    private JTextField jTextField1;
    private JPanel jPanel3;
    private JButton okButton;
    private JPanel jPanel2;
    private URL html;

    public HtmlViewer(JFrame frame, URL url) {
        super(frame);
        html = url;
        initGUI();
    }

    private void initGUI() {
        try {

        jPanel1 = new JPanel();
        BoxLayout jPanel1Layout = new BoxLayout(jPanel1, javax.swing.BoxLayout.X_AXIS);
        jPanel1.setLayout(jPanel1Layout);
        getContentPane().add(jPanel1, BorderLayout.CENTER);

        jScrollPane1 = new JScrollPane();
        jPanel1.add(jScrollPane1);

        jEditorPane1 = new JEditorPane();
        jScrollPane1.setViewportView(jEditorPane1);
        jEditorPane1.setPage(html);
        jEditorPane1.setEnabled(false);

        jPanel2 = new JPanel();
        getContentPane().add(jPanel2, BorderLayout.SOUTH);

        jPanel4 = new JPanel();
        BorderLayout jPanel4Layout = new BorderLayout();
        jPanel4.setLayout(jPanel4Layout);
        getContentPane().add(jPanel4, BorderLayout.NORTH);

        jPanel3 = new JPanel();
        jPanel4.add(jPanel3, BorderLayout.CENTER);
        BoxLayout jPanel3Layout = new BoxLayout(jPanel3, javax.swing.BoxLayout.Y_AXIS);
        jPanel3.setLayout(jPanel3Layout);

        jPanel5 = new JPanel();
        BoxLayout jPanel5Layout = new BoxLayout(jPanel5, javax.swing.BoxLayout.X_AXIS);
        jPanel5.setLayout(jPanel5Layout);
        jPanel4.add(jPanel5, BorderLayout.NORTH);

        jToolBar1 = new JToolBar();
        jPanel5.add(jToolBar1);
        jToolBar1.setRollover(true);

        jButton2 = new JButton();
        jToolBar1.add(jButton2);
        jButton2.setText("jButton2");

        jButton1 = new JButton();
        jToolBar1.add(jButton1);
        jButton1.setText("jButton1");

        jTextField1 = new JTextField();
        jPanel3.add(jTextField1);
        jTextField1.setText(html.getPath());
        jTextField1.setEditable(false);

        okButton = new JButton();
        jPanel2.add(okButton);
        okButton.setText("OK");
        okButton.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    setVisible(false);
                    dispose();
                }}) ;

            setSize(400, 300);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
