/*
 * $Id: DilutionPanel.java,v 1.12 2008/07/07 17:51:14 andrew_avis Exp $
 * Created on June 4, 2005
 * Dilution panel to help you figure out the results of diluting
 * your wort with water post-boil.
 */
/**
 *  StrangeBrew Java - a homebrew recipe calculator
    Copyright (C) 2005  Drew Avis

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

package ca.strangebrew.ui.swing;


import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ca.strangebrew.BrewCalcs;
import ca.strangebrew.Debug;
import ca.strangebrew.DilutedRecipe;
import ca.strangebrew.Quantity;
import ca.strangebrew.Recipe;
import ca.strangebrew.SBStringUtils;
import ca.strangebrew.Style;



public class DilutionPanel extends javax.swing.JPanel implements ChangeListener, ActionListener {
	private JPanel infoPanel;
	private JPanel stylePanel;
	private JLabel colourLowLabel;
	private JLabel colourRecipeLabel;
	private JLabel jLabel4;
	private JLabel ibuRecipeLabel;
	private JLabel jLabel8;
	private JLabel ogHighLabel;
	private JSpinner ogDilutedSpin;
	private JLabel ogRecipeLabel;
	private JLabel ogLowLabel;
	private JLabel abvHighLabel;
	private JLabel abvDilutedLabel;
	private JLabel abvRecipeLabel;
	private JLabel abvLowLabel;
	private JLabel jLabel7;
	private JLabel colourHighLabel;
	private JLabel colourDilutedLabel;
	private JLabel jLabel6;
	private JSpinner ibuDilutedSpin;
	private JLabel ibuHighLabel;
	private JLabel ibuLowLabel;
	private JLabel jLabel5;
	private JLabel spaceLabel;
	private JLabel jLabel3;
	private JLabel jLabel2;
	private JLabel jLabel1;
	private JPanel postBoilPanel;
	private JPanel totalVolPanel;
	private JPanel diluteWithPanel;
	private JLabel totalVolLabel;
	private JLabel diluteWithLabel;
	private JLabel postBoilVolLabel;
	private JSpinner totalVolumeSpinner;
	private JSpinner diluteWithText;
	private JSpinner postBoilText;
	private JCheckBox dilutedCheckBox;
	
	private Recipe myRecipe;
	boolean dontUpdate = false;

		
	public DilutionPanel() {
		super();
		initGUI();
	}
	
	public void setData(Recipe r){
		myRecipe = r;
	    //displayDilution();
	}
	
	public void displayDilution(){
		dontUpdate = true;
		if (myRecipe instanceof DilutedRecipe) {
			DilutedRecipe dilRecipe = (DilutedRecipe)myRecipe;
			
			dilutedCheckBox.setSelected(true);
			totalVolumeSpinner.setEnabled(true);
			diluteWithText.setEnabled(true);
			
			// Dilution specific
			postBoilText.setValue(new Double(dilRecipe.getPostBoilVol(dilRecipe.getVolUnits())));
			diluteWithText.setValue(new Double(dilRecipe.getAddVol(dilRecipe.getVolUnits())));
			totalVolumeSpinner.setValue(new Double(dilRecipe.getFinalWortVol(dilRecipe.getVolUnits())));
		
			// Diluted Numbers
			ibuDilutedSpin.setValue(new Double(dilRecipe.getIbu()));
			colourDilutedLabel.setText(SBStringUtils.format(dilRecipe.getColour(BrewCalcs.SRM), 0));
			abvDilutedLabel.setText(SBStringUtils.format(dilRecipe.getAlcohol(), 1));
			ogDilutedSpin.setValue(new Double(dilRecipe.getEstOg()));			
			
			// Original numbers
			ibuRecipeLabel.setText(SBStringUtils.format(dilRecipe.getOrigIbu(), 1));
			colourRecipeLabel.setText(SBStringUtils.format(dilRecipe.getOrigColour(BrewCalcs.SRM), 1));
			abvRecipeLabel.setText(SBStringUtils.format(dilRecipe.getOrigAlcohol(), 1));
			ogRecipeLabel.setText(SBStringUtils.format(dilRecipe.getOrigEstOg(), 3));		
		} else {
			dilutedCheckBox.setSelected(false);
			totalVolumeSpinner.setEnabled(false);
			diluteWithText.setEnabled(false);

			// Dil specific
			postBoilText.setValue(new Double(myRecipe.getPostBoilVol(myRecipe.getVolUnits())));
			diluteWithText.setValue(0.0);
			totalVolumeSpinner.setValue(new Double(myRecipe.getFinalWortVol(myRecipe.getVolUnits())));
		
			// Diluted Numbers
			ibuDilutedSpin.setValue(0.0);
			colourDilutedLabel.setText("0.0");
			abvDilutedLabel.setText("0.0");
			ogDilutedSpin.setValue(0.0);
			
			// recipe values:
			ibuRecipeLabel.setText(SBStringUtils.format(myRecipe.getIbu(), 1));
			colourRecipeLabel.setText(SBStringUtils.format(myRecipe.getColour(BrewCalcs.SRM), 1));
			abvRecipeLabel.setText(SBStringUtils.format(myRecipe.getAlcohol(), 1));
			ogRecipeLabel.setText(SBStringUtils.format(myRecipe.getEstOg(), 3));
		}
		
		// style values
		Style s = myRecipe.getStyleObj();		
		ogLowLabel.setText(SBStringUtils.format(s.getOgLow(), 3));		
		ogHighLabel.setText(SBStringUtils.format(s.getOgHigh(), 3));
		abvLowLabel.setText(SBStringUtils.format(s.getAlcLow(), 1));		
		abvHighLabel.setText(SBStringUtils.format(s.getAlcHigh(), 1));
		colourLowLabel.setText(SBStringUtils.format(s.getSrmLow(), 1));		
		colourHighLabel.setText(SBStringUtils.format(s.getSrmHigh(), 1));
		ibuLowLabel.setText(SBStringUtils.format(s.getIbuLow(), 1));		
		ibuHighLabel.setText(SBStringUtils.format(s.getIbuHigh(), 1));	
		
		dontUpdate = false;
	}
	
	private void initGUI() {
		try {
			BoxLayout thisLayout = new BoxLayout(this, javax.swing.BoxLayout.X_AXIS);
			this.setLayout(thisLayout);
			this.setPreferredSize(new java.awt.Dimension(472, 301));
			{
				infoPanel = new JPanel();
				BoxLayout infoPanelLayout = new BoxLayout(infoPanel, javax.swing.BoxLayout.Y_AXIS);
				infoPanel.setLayout(infoPanelLayout);
				this.add(infoPanel);
				{
					dilutedCheckBox = new JCheckBox();
					infoPanel.add(dilutedCheckBox);
					dilutedCheckBox.setText("Dilute Recipe");
					dilutedCheckBox.addActionListener(this);
				}
				{
					postBoilPanel = new JPanel();
					FlowLayout postBoilPanelLayout = new FlowLayout();
					postBoilPanelLayout.setAlignment(FlowLayout.LEFT);
					postBoilPanel.setLayout(postBoilPanelLayout);
					infoPanel.add(postBoilPanel);
					{
						postBoilVolLabel = new JLabel();
						postBoilPanel.add(postBoilVolLabel);
						postBoilVolLabel.setText("Post Boil:");
					}
					{
						postBoilText = new JSpinner();
						postBoilPanel.add(postBoilText);
						postBoilText.setPreferredSize(new java.awt.Dimension(85, 20));
						postBoilText.addChangeListener(this);
					}
				}
				{
					diluteWithPanel = new JPanel();
					FlowLayout diluteWithPanelLayout = new FlowLayout();
					diluteWithPanelLayout.setAlignment(FlowLayout.LEFT);
					diluteWithPanel.setLayout(diluteWithPanelLayout);
					infoPanel.add(diluteWithPanel);
					{
						diluteWithLabel = new JLabel();
						diluteWithPanel.add(diluteWithLabel);
						diluteWithLabel.setText("Dilute With:");
					}
					{
						SpinnerNumberModel diluteWithTextSpinnerModel = new SpinnerNumberModel(0.0, -999.9,
								999.9, 0.5);
						diluteWithText = new JSpinner();
						diluteWithText.setModel(diluteWithTextSpinnerModel);
						diluteWithPanel.add(diluteWithText);
						diluteWithText.setPreferredSize(new java.awt.Dimension(79, 20));
						diluteWithText.addChangeListener(this);
					}
				}
				{
					totalVolPanel = new JPanel();
					FlowLayout totalVolPanelLayout = new FlowLayout();
					totalVolPanelLayout.setAlignment(FlowLayout.LEFT);
					totalVolPanel.setLayout(totalVolPanelLayout);
					infoPanel.add(totalVolPanel);
					{
						totalVolLabel = new JLabel();
						totalVolPanel.add(totalVolLabel);
						totalVolLabel.setText("Total Vol:");
					}
					{

						SpinnerNumberModel totalVolumeSpinnerModel = new SpinnerNumberModel(0.0, 0.0,
								999.9, 0.5);
						
						totalVolumeSpinner = new JSpinner();
						totalVolPanel.add(totalVolumeSpinner);
						totalVolumeSpinner.setModel(totalVolumeSpinnerModel);
						totalVolumeSpinner.setPreferredSize(new java.awt.Dimension(91, 21));
						totalVolumeSpinner.addChangeListener(this);
					}
				}
			}
			{
				stylePanel = new JPanel();
				GridLayout stylePanelLayout = new GridLayout(5, 5);
				stylePanelLayout.setColumns(5);
				stylePanelLayout.setRows(5);
				stylePanel.setLayout(stylePanelLayout);
				this.add(stylePanel);
				stylePanel.setPreferredSize(new java.awt.Dimension(277, 154));
				stylePanel.setBorder(BorderFactory.createTitledBorder("Style Conformance"));
				{
					spaceLabel = new JLabel();
					stylePanel.add(spaceLabel);
				}
				{
					jLabel1 = new JLabel();
					stylePanel.add(jLabel1);
					jLabel1.setText("Low:");
				}
				{
					jLabel2 = new JLabel();
					stylePanel.add(jLabel2);
					jLabel2.setText("Recipe:");
				}
				{
					jLabel3 = new JLabel();
					stylePanel.add(jLabel3);
					jLabel3.setText("Diluted:");
				}
				{
					jLabel4 = new JLabel();
					stylePanel.add(jLabel4);
					jLabel4.setText("High:");
				}
				{
					jLabel5 = new JLabel();
					stylePanel.add(jLabel5);
					jLabel5.setText("IBU:");
				}
				{
					ibuLowLabel = new JLabel();
					stylePanel.add(ibuLowLabel);
					ibuLowLabel.setText("15");
				}
				{
					ibuRecipeLabel = new JLabel();
					stylePanel.add(ibuRecipeLabel);
					ibuRecipeLabel.setText("10");
				}
				{
					SpinnerNumberModel ibuDilutedSpinModel = new SpinnerNumberModel(10.0, 0.1,
							999.9, 0.5);
					ibuDilutedSpin = new JSpinner();
					stylePanel.add(ibuDilutedSpin);
					ibuDilutedSpin.setModel(ibuDilutedSpinModel);
					ibuDilutedSpin.addChangeListener(this);
				}
				{
					ibuHighLabel = new JLabel();
					stylePanel.add(ibuHighLabel);
					ibuHighLabel.setText("55");
				}
				{
					jLabel6 = new JLabel();
					stylePanel.add(jLabel6);
					jLabel6.setText("Colour:");
				}
				{
					colourLowLabel = new JLabel();
					stylePanel.add(colourLowLabel);
					colourLowLabel.setText("5");
				}
				{
					colourRecipeLabel = new JLabel();
					stylePanel.add(colourRecipeLabel);
					colourRecipeLabel.setText("10");
				}
				{
					colourDilutedLabel = new JLabel();
					stylePanel.add(colourDilutedLabel);
					colourDilutedLabel.setText("10");
					colourDilutedLabel.setBounds(47, 53, 60, 30);
				}
				{
					colourHighLabel = new JLabel();
					stylePanel.add(colourHighLabel);
					colourHighLabel.setText("20");
				}
				{
					jLabel7 = new JLabel();
					stylePanel.add(jLabel7);
					jLabel7.setText("ABV:");
				}
				{
					abvLowLabel = new JLabel();
					stylePanel.add(abvLowLabel);
					abvLowLabel.setText("0");
				}
				{
					abvRecipeLabel = new JLabel();
					stylePanel.add(abvRecipeLabel);
					abvRecipeLabel.setText("10");
				}
				{
					abvDilutedLabel = new JLabel();
					stylePanel.add(abvDilutedLabel);
					abvDilutedLabel.setText("8");
				}
				{
					abvHighLabel = new JLabel();
					stylePanel.add(abvHighLabel);
					abvHighLabel.setText("50");
				}
				{
					jLabel8 = new JLabel();
					stylePanel.add(jLabel8);
					jLabel8.setText("OG:");
				}
				{
					ogLowLabel = new JLabel();
					stylePanel.add(ogLowLabel);
					ogLowLabel.setText("1.000");
				}
				{
					ogRecipeLabel = new JLabel();
					stylePanel.add(ogRecipeLabel);
					ogRecipeLabel.setText("1.050");
				}
				{
					SpinnerNumberModel ogSpinModel = new SpinnerNumberModel(1.000, 0.900,
							2.000, 0.001);
					
					ogDilutedSpin = new JSpinner();
					stylePanel.add(ogDilutedSpin);
					ogDilutedSpin.setModel(ogSpinModel);
					ogDilutedSpin.addChangeListener(this);
				}
				{
					ogHighLabel = new JLabel();
					stylePanel.add(ogHighLabel);
					ogHighLabel.setText("1.090");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();	

		if (myRecipe != null) {
			if (o == dilutedCheckBox ) {
				if (dilutedCheckBox.isSelected()) {
					DilutedRecipe dil = new DilutedRecipe(myRecipe);
					StrangeSwing.getInstance().setRecipe(dil);
					myRecipe = dil;
				} else {
					Recipe unDil = new Recipe(myRecipe);
					StrangeSwing.getInstance().setRecipe(unDil);
					myRecipe = unDil;
				}
				displayDilution();
			}
		}
	}
	
	public void stateChanged(ChangeEvent e) {
		if (dontUpdate == true) {
			return;
		}
		
		Object o = e.getSource();	
		
		if (myRecipe != null) {
			if (o == postBoilText) {
				try {
					NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
					Number number = format.parse(postBoilText.toString().trim());
					myRecipe.setPostBoil(new Quantity(myRecipe.getVolUnits(), number.doubleValue()));
				} catch (NumberFormatException m) {
					Debug.print("Could not parse postBoilText as a double");
					postBoilText.setValue(myRecipe.getPostBoilVol().toString());
				} catch (ParseException m) {
					Debug.print("Could not parse postBoilText as a double");
					postBoilText.setValue(myRecipe.getPostBoilVol().toString());
				}
				displayDilution();
			}
			
//			if (myRecipe instanceof DilutedRecipe) {
//				DilutedRecipe dilRecipe = (DilutedRecipe)myRecipe;
//				if (o == diluteWithText) {
//					dilRecipe.setAddVol(new Quantity(dilRecipe.getVolUnits(), Double.parseDouble(diluteWithText.getValue().toString())));
//					displayDilution();
//				} else if (o == totalVolumeSpinner) {
//					// If total vol is being set here, we are ONLY adding dilution water. So find the dif
//					// between this val, and the original final vol.. that is the dilution addision volume
//					final double origFinal = dilRecipe.getOrigFinalWortVol(dilRecipe.getVolUnits());
//					final double newFinal = Double.parseDouble(totalVolumeSpinner.getValue().toString());
//					if (newFinal < origFinal) {
//						// Should pop an error msg
//						dilRecipe.setAddVol(new Quantity(dilRecipe.getVolUnits(), 0));
//					} else {
//						dilRecipe.setAddVol(new Quantity(dilRecipe.getVolUnits(), newFinal - origFinal));
//					}
//					displayDilution();
//				}
//			}
//			if (o == ibuDilutedSpin){
//				myRecipe.dilution.setDilIbu(Double.parseDouble(ibuDilutedSpin.getValue().toString()));
//				displayDilution();
//			}
//			if (o == ogDilutedSpin){
//				myRecipe.dilution.setDilOG(Double.parseDouble(ogDilutedSpin.getValue().toString()));
//				displayDilution();
//			}
		}
	}

}
