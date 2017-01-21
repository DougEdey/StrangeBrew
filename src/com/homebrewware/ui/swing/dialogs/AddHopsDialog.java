/**
 *    Filename: AddHopsDialog.java
 *     Version: 0.9.0
 * Description: Add Hops Dialog
 *     License: GPLv2
 *        Date: 2017-01-14
 *
 * This class creates a tabbed dialog box with all the preferences
 * used by the application.  The constructor will initialize all the
 * UI components to values from the Options object in the constructor.
 *
 * If the dialog box is closed with the OK button then the Options object
 * given in the constructor will be updated with new values entered by
 * the user.  If the dialog box is closed any other way then no changes will
 * be made to the Options object.
 *
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
package com.homebrewware.ui.swing.dialogs;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.ScrollPaneLayout;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.homebrewware.Database;
import com.homebrewware.Debug;
import com.homebrewware.Hop;
import com.homebrewware.Options;
import com.homebrewware.ui.swing.StrangeSwing;


public class AddHopsDialog extends javax.swing.JDialog implements ActionListener, ChangeListener, FocusListener {

    private Database db;

    public static void main(String[] args) {
        final JFrame frame = new JFrame();
        AddHopsDialog inst = new AddHopsDialog(frame);
        inst.setVisible(true);
    }


    private final JPanel mainPanel = new JPanel();


    // fields are Name,Alpha,Cost,Stock,Units,Descr,Storage,Date,Modified
    final private JLabel lblName = new JLabel();
    final private JTextField txtName = new JTextField();

    final private JLabel lblAlpha = new JLabel();
    final private JTextField txtAlpha = new JTextField();

    final private JLabel lblCost = new JLabel();
    final private JTextField txtCost = new JTextField();

    final private JLabel lblDate = new JLabel();
    final private JTextField txtDate = new JTextField();

    final private JLabel lblStock = new JLabel();
    final private JTextField txtStock = new JTextField();

    final private JLabel lblUnits = new JLabel();
    final private JComboBox cUnits = new JComboBox();

    final private JLabel lblDescr = new JLabel();
    final private JTextArea txtDescr = new JTextArea();


    final private JLabel lblStorage= new JLabel();
    final private JTextField txtStorage = new JTextField();

    final private JLabel lblModified = new JLabel();
    final private JCheckBox bModified = new JCheckBox();

    final private JButton okButton = new JButton();
    final private JButton cancelButton = new JButton();
    JScrollPane jScrollDescr =new JScrollPane(txtDescr, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    public AddHopsDialog(Frame owner) {
        super(owner, "Add Hop", true);
        db = ((StrangeSwing)owner).DB;

        layoutUi();
        setLocation(owner.getLocation());
        //setOptions();

    }




    private void layoutUi() {

        GridLayout gridBag = new GridLayout(0,1);
        GridBagConstraints c = new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
        c.fill = GridBagConstraints.HORIZONTAL;

        c = null;

        JPanel lOne = new JPanel(new FlowLayout(FlowLayout.LEFT));//new GridLayout());
        JPanel lTwo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel lThree = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lThree.setPreferredSize(new Dimension(400,60));
        JPanel lFour = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel lFive = new JPanel(new FlowLayout(FlowLayout.LEFT));

        mainPanel.setLayout(gridBag);
        getContentPane().add(mainPanel);
        mainPanel.setBorder(BorderFactory.createTitledBorder("Add Hop"));

        mainPanel.setLayout(gridBag);
        this.setTitle("Add Hop");
        this.setSize(500,400);
        //


        try {
            // First Line
            {
                lOne.add(lblName, c);
                lblName.setText("Name: ");


                lOne.add(txtName, c);
                txtName.setPreferredSize(new java.awt.Dimension(120, txtName.getFont().getSize()*2));
                txtName.addActionListener(this);
                txtName.addFocusListener(this);

                lOne.add(lblAlpha, c);
                lblAlpha.setText("Alpha: ");

                lOne.add(txtAlpha, c);
                txtAlpha.setText("0.00");
                txtAlpha.setPreferredSize(new java.awt.Dimension(55, txtAlpha.getFont().getSize()*2));

                //c.gridwidth = GridBagConstraints.REMAINDER;

            }
            // Second Line
            {
                lTwo.add(lblCost, c);
                lblCost.setText("Cost: ");

                lTwo.add(txtCost, c);
                txtCost.setText("$0.00");
                txtCost.setPreferredSize(new java.awt.Dimension(55, txtCost.getFont().getSize()*2));

                lTwo.add(lblDate, c);
                lblDate.setText("Date: ");

                lTwo.add(txtDate, c);
                txtDate.setText("00/00/0000");
                txtDate.setPreferredSize(new java.awt.Dimension(55, txtDate.getFont().getSize()*2));

                lTwo.add(lblStock, c);
                lblStock.setText("Stock: ");

                lTwo.add(txtStock, c);
                txtStock.setText("0");
                txtStock.setPreferredSize(new java.awt.Dimension(55, txtStock.getFont().getSize()*2));


                //c.gridwidth = GridBagConstraints.REMAINDER;

            }
            //Third
            {

//              Mash.setPreferredSize(new java.awt.Dimension(55, 20));
            //  lThree.setLayout(null);
                //lThree.add(lblDescr, c);
                //lblDescr.setText("Description: ");

                txtDescr.setLineWrap(true);
                //txtDescr.setPreferredSize(new java.awt.Dimension(400, 40));

                jScrollDescr.setPreferredSize(new java.awt.Dimension(400,jScrollDescr.getFont().getSize()*6));
                jScrollDescr.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                jScrollDescr.setLayout(new ScrollPaneLayout());
                //lThree.add(jScrollDescr , c);

                //c.gridwidth = GridBagConstraints.REMAINDER;
            }
            //Fourth
            {
                lFour.add(lblUnits, c);
                lblUnits.setText("Units: ");

                lFour.add(cUnits, c);
                cUnits.addItem("g");
                cUnits.addItem("oz");




                lFour.add(lblStorage, c);
                lblStorage.setText("Storage: ");

                lFour.add(txtStorage, c);
                txtStorage.setText("0");
                txtStorage.setPreferredSize(new java.awt.Dimension(55, txtStorage.getFont().getSize()*2));

                lFour.add(lblModified, c);
                lblModified.setText("Modified: ");

                lFour.add(bModified, c);
                //Modified.setPreferredSize(new java.awt.Dimension(55, 20));
                //c.gridwidth = GridBagConstraints.REMAINDER;
            }
            //fifth
            {
                //c.gridx = 1;
                //c.fill = GridBagConstraints.NONE;

                lFive.add(okButton, c);
                okButton.setText("OK");
                okButton.addActionListener(this);

                lFive.add(cancelButton, c);
                cancelButton.setText("Cancel");
                cancelButton.addActionListener(this);

                //c.gridwidth = GridBagConstraints.REMAINDER;
            }

            mainPanel.add(lOne);
            mainPanel.add(lTwo);
            mainPanel.add(jScrollDescr);
            mainPanel.add(lFour);
            mainPanel.add(lFive);
        //
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //  Action Performed
    public void actionPerformed(ActionEvent e) {

        Object o = e.getSource();

        Debug.print("Action performed on: " + o);
        if(o == okButton) {


            // create a new Ferm object
            Hop i = new Hop();
            i.setName(txtName.getText());
            i.setAlpha(Double.parseDouble(txtAlpha.getText()));

            i.setCost(txtCost.getText());
            i.setStock(Double.parseDouble(txtStock.getText()));
            i.setUnits(cUnits.getSelectedItem().toString());
            i.setDescription(txtDescr.getText());
            i.setStorage(Double.parseDouble(txtStorage.getText()));
            i.setDate(txtDate.getText());
            i.setModified(bModified.isSelected());

            int result = 1;
            int found = db.find(i);

            Debug.print("Searched for Hop, returned: "+found);

            // check to see if we have this hop already in the db
            if(found  >= 0){
                // This already exists, do we want to overwrite?
                result = JOptionPane.showConfirmDialog((Component) null,
                            "This ingredient exists, overwrite original?","alert", JOptionPane.YES_NO_CANCEL_OPTION);

                switch(result) {
                    case JOptionPane.NO_OPTION:
                        setVisible(false);
                        dispose();
                        return;


                    case JOptionPane.YES_OPTION:
                        db.hopsDB.remove(found);
                        break;

                    case JOptionPane.CANCEL_OPTION:
                        return;

                    default:
                        break;
                }
            }



            db.hopsDB.add(i);
            db.writeHops();

            setVisible(false);
            dispose();

        } else if(o == cancelButton) {
            dispose();
        }

    }

    public void focusLost(FocusEvent e) {
        if(e.getSource() == txtName) {
            // this is where we have lost focus, so now we need to populate the data
            // See if we can find the hop
            Hop temp = new Hop();
            temp.setName(txtName.getText());
            int result = db.find(temp);
            Debug.print("Searching for " + txtName.getText() + " found: " + result);
            if(result >= 0) {

                // we have the index, load it into the hop
                temp = db.hopsDB.get(result);
                // set the fields
                if(Double.toString(temp.getAlpha())!= null)
                    txtAlpha.setText(Double.toString(temp.getAlpha()));
                if(Double.toString(temp.getCostPerU()) != null)
                    txtCost.setText(Double.toString(temp.getCostPerU()));
                if(temp.getDate() != null)
                    txtDate.setText(temp.getDate().toString());
                if(Double.toString(temp.getStock()) != null)
                    txtStock.setText(Double.toString(temp.getStock()));
                if(temp.getUnits() != null)
                    cUnits.setSelectedItem(temp.getUnits());
                if(temp.getDescription() != null){

                    txtDescr.setText(temp.getDescription());
                    jScrollDescr.invalidate();
                }
                if(Double.toString(temp.getStorage()) != null)
                    txtStorage.setText(Double.toString(temp.getStorage()));

                jScrollDescr.getVerticalScrollBar().setValue(0);
                jScrollDescr.revalidate();

            }
        }

        Debug.print("Focus changed on: " + e.getSource());

    }
    public void stateChanged(ChangeEvent e){
        Object o = e.getSource();

        Debug.print("State changed on: " + o);


    }




    public void focusGained(FocusEvent e) {
        // TODO Auto-generated method stub

    }


}
