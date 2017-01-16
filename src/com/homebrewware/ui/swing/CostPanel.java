/**
 *    Filename: CostPanel.java
 *     Version: 0.9.0
 * Description: Cost Panel
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

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.homebrewware.Debug;
import com.homebrewware.Quantity;
import com.homebrewware.Recipe;
import com.homebrewware.StringUtils;


public class CostPanel extends javax.swing.JPanel implements ActionListener, FocusListener{
    private JPanel jPanel1;
    private JLabel jLabel4;
    private JLabel bottleCostLbl;
    private JLabel numBottlesLbl;
    private JLabel finalVolLbl;
    private JComboBox bottleSizeUCmb;
    private ComboModel<String> bottleSizeUCmbModel;
    private JTextField bottleSizeTxt;
    private JTextField yeastTxt;
    private JLabel hopsLbl;
    private JLabel jLabel8;
    private JLabel totalLbl;
    private JTextField otherTxt;
    private JLabel miscLbl;
    private JLabel grainLbl;
    private JLabel jLabel10;
    private JLabel jLabel9;
    private JLabel jLabel7;
    private JLabel jLabel6;
    private JLabel jLabel5;
    private JLabel jLabel3;
    private JLabel jLabel2;
    private JLabel jLabel1;
    private JPanel jPanel2;

    private Recipe myRecipe;

    public CostPanel() {
        super();
        initGUI();
    }

    public void setData(Recipe r) {
        myRecipe = r;
        // displayCost();
    }

    public void displayCost(){
        grainLbl.setText(StringUtils.myNF.format(myRecipe.getTotalMaltCost()));
        hopsLbl.setText(StringUtils.myNF.format(myRecipe.getTotalHopsCost()));
        yeastTxt.setText(StringUtils.myNF.format(myRecipe.getYeastObj().getCostPerU()));
        miscLbl.setText(StringUtils.myNF.format(myRecipe.getTotalMiscCost()));
        otherTxt.setText(StringUtils.myNF.format(myRecipe.getOtherCost()));
        double totalCost = myRecipe.getTotalMaltCost() +
            myRecipe.getTotalHopsCost() +
            myRecipe.getYeastObj().getCostPerU() +
            myRecipe.getTotalMiscCost() +
            myRecipe.getOtherCost();

        totalLbl.setText(StringUtils.myNF.format(totalCost));

        bottleSizeTxt.setText(StringUtils.format(myRecipe.getBottleSize(), 0));
        bottleSizeUCmbModel.addOrInsert(myRecipe.getBottleU());

        finalVolLbl.setText(myRecipe.getFinalWortVol(myRecipe.getVolUnits()) + " " + myRecipe.getVolUnits());
        double numBottles = Quantity.convertUnit(myRecipe.getVolUnits(),
                myRecipe.getBottleU(),
                myRecipe.getFinalWortVol(myRecipe.getVolUnits())) / myRecipe.getBottleSize();
        numBottlesLbl.setText(StringUtils.format(numBottles, 1));
        bottleCostLbl.setText(StringUtils.myNF.format( totalCost/numBottles ));


    }   private void initGUI() {
        try {

            jPanel1 = new JPanel();
            GridBagLayout jPanel1Layout = new GridBagLayout();
            jPanel1Layout.rowWeights = new double[]{0.1, 0.1, 0.1, 0.1, 0.1, 0.1};
            jPanel1Layout.rowHeights = new int[]{7, 7, 7, 7, 7, 7};
            jPanel1Layout.columnWeights = new double[]{0.1, 0.1, 0.1, 0.1};
            jPanel1Layout.columnWidths = new int[]{7, 7, 7, 7};
            jPanel1.setLayout(jPanel1Layout);
            BoxLayout thisLayout = new BoxLayout(this, javax.swing.BoxLayout.X_AXIS);
            this.setLayout(thisLayout);
            this.add(jPanel1);
            jPanel1.setBorder(BorderFactory.createTitledBorder("Recipe Cost:"));

            jLabel1 = new JLabel();
            jPanel1.add(jLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
            jLabel1.setText("Grain:");

            jLabel2 = new JLabel();
            jPanel1.add(jLabel2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
            jLabel2.setText("Hops:");

            jLabel3 = new JLabel();
            jPanel1.add(jLabel3, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
            jLabel3.setText("Misc Ingr.:");

            jLabel4 = new JLabel();
            jPanel1.add(jLabel4, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
            jLabel4.setText("Yeast:");

            jLabel5 = new JLabel();
            jPanel1.add(jLabel5, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
            jLabel5.setText("Other:");

            jLabel6 = new JLabel();
            jPanel1.add(jLabel6, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
            jLabel6.setText("Total:");
            jLabel6.setFont(new java.awt.Font("Tahoma", 1, 11));

            grainLbl = new JLabel();
            jPanel1.add(grainLbl, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
            grainLbl.setText("jLabel11");

            hopsLbl = new JLabel();
            jPanel1.add(hopsLbl, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
            hopsLbl.setText("jLabel11");

            yeastTxt = new JTextField();
            jPanel1.add(yeastTxt, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
            yeastTxt.setText("jLabel11");
            yeastTxt.setPreferredSize(new java.awt.Dimension(48, yeastTxt.getFont().getSize()*2));
            yeastTxt.addFocusListener(this);
            yeastTxt.addActionListener(this);

            miscLbl = new JLabel();
            jPanel1.add(miscLbl, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
            miscLbl.setText("jLabel11");

            otherTxt = new JTextField();
            jPanel1.add(otherTxt, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
            otherTxt.setText("jLabel11");
            otherTxt.setPreferredSize(new java.awt.Dimension(48, otherTxt.getFont().getSize()*2));
            otherTxt.addFocusListener(this);
            otherTxt.addActionListener(this);

            totalLbl = new JLabel();
            jPanel1.add(totalLbl, new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
            totalLbl.setText("jLabel11");
            totalLbl.setFont(new java.awt.Font("Tahoma", 1, 11));

            jPanel2 = new JPanel();
            GridBagLayout jPanel2Layout = new GridBagLayout();
            jPanel2Layout.rowWeights = new double[]{0.1, 0.1, 0.1, 0.1};
            jPanel2Layout.rowHeights = new int[]{7, 7, 7, 7};
            jPanel2Layout.columnWeights = new double[]{0.1, 0.1, 0.1, 0.1};
            jPanel2Layout.columnWidths = new int[]{7, 7, 7, 7};
            jPanel2.setLayout(jPanel2Layout);
            this.add(jPanel2);
            jPanel2.setBorder(BorderFactory.createTitledBorder("Bottle Cost:"));

            jLabel7 = new JLabel();
            jPanel2.add(jLabel7, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
            jLabel7.setText("Bottle Size:");

            jLabel8 = new JLabel();
            jPanel2.add(jLabel8, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
            jLabel8.setText("Final Volume:");

            jLabel9 = new JLabel();
            jPanel2.add(jLabel9, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
            jLabel9.setText("# Bottles:");

            jLabel10 = new JLabel();
            jPanel2.add(jLabel10, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
            jLabel10.setText("Cost / Bottle:");

            bottleSizeTxt = new JTextField();
            jPanel2.add(bottleSizeTxt, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
            bottleSizeTxt.setText("jTextField1");
            bottleSizeTxt.setPreferredSize(new java.awt.Dimension(62, bottleSizeTxt.getFont().getSize()*2));
            bottleSizeTxt.addFocusListener(this);
            bottleSizeTxt.addActionListener(this);

            bottleSizeUCmbModel = new ComboModel<String>();
            bottleSizeUCmbModel.setList(Quantity.getListofUnits("vol"));

            bottleSizeUCmb = new JComboBox();
            SmartComboBox.enable(bottleSizeUCmb);
            jPanel2.add(bottleSizeUCmb, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
            bottleSizeUCmb.setModel(bottleSizeUCmbModel);
            bottleSizeUCmb.setPreferredSize(new java.awt.Dimension(80, bottleSizeUCmb.getFont().getSize()*2));
            bottleSizeUCmb.addFocusListener(this);
            bottleSizeUCmb.addActionListener(this);

            finalVolLbl = new JLabel();
            jPanel2.add(finalVolLbl, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
            finalVolLbl.setText("jLabel11");

            numBottlesLbl = new JLabel();
            jPanel2.add(numBottlesLbl, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
            numBottlesLbl.setText("jLabel11");

            bottleCostLbl = new JLabel();
            jPanel2.add(bottleCostLbl, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
            bottleCostLbl.setText("jLabel11");

            setPreferredSize(new Dimension(400, 300));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();

        if (o == yeastTxt){
            try {
                myRecipe.getYeastObj().setCost(StringUtils.myNF.parse(yeastTxt.getText()).doubleValue());
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
            displayCost();
        }
        if (o == otherTxt){
            try {
                myRecipe.setOtherCost(StringUtils.myNF.parse(otherTxt.getText()).doubleValue());
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
            displayCost();
        }
        if (o == bottleSizeTxt){
            try {
                NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
                Number number = format.parse(bottleSizeTxt.getText().trim());
                myRecipe.setBottleSize(number.doubleValue());
            } catch (NumberFormatException m) {
                Debug.print("Could not parse bottleSizeTxt as a double");
                bottleSizeTxt.setText(Double.toString(myRecipe.getBottleSize()));
            } catch (ParseException m) {
                Debug.print("Could not parse bottleSizeTxt as a double");
                bottleSizeTxt.setText(Double.toString(myRecipe.getBottleSize()));
            }
            displayCost();
        }
        if (o == bottleSizeUCmb){
            myRecipe.setBottleU(bottleSizeUCmbModel.getSelectedItem().toString());
            displayCost();
        }

    }

    public void focusLost(FocusEvent e) {
        Object o = e.getSource();
        ActionEvent evt = new ActionEvent(o, 1, "");
        actionPerformed(evt);
    }

    public void focusGained(FocusEvent e) {
        // do nothing, we don't need this event
    }

}
