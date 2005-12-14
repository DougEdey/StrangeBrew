/*
 * $Id: DilutionPanel.java,v 1.2 2005/12/14 17:53:38 andrew_avis Exp $
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

package strangebrew.ui.swing;


import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import javax.swing.JSpinner;
import javax.swing.JLabel;
import javax.swing.SpinnerNumberModel;

import java.awt.GridLayout;
import java.awt.FlowLayout;
import javax.swing.JCheckBox;
import javax.swing.WindowConstants;
import javax.swing.JFrame;

import strangebrew.Recipe;

public class DilutionPanel extends javax.swing.JPanel {
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

	/**
	* Auto-generated main method to display this 
	* JPanel inside a new JFrame.
	*/
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.getContentPane().add(new DilutionPanel());
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
	
	public DilutionPanel(Recipe r){
		super();
		myRecipe = r;
		initGUI();
		// displayDilution();
	}
		
	public DilutionPanel() {
		super();
		initGUI();
	}
	
	public void setData(Recipe r){
		myRecipe = r;
	}
	
	private void displayDilution(){
		postBoilText.setValue(new Double(myRecipe.getPostBoilVol(myRecipe.getVolUnits())));
		diluteWithText.setValue(new Double(myRecipe.dilution.getAddVol().getValue()));
		
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
						diluteWithText = new JSpinner();
						diluteWithPanel.add(diluteWithText);
						diluteWithText.setPreferredSize(new java.awt.Dimension(79, 20));
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

						SpinnerNumberModel totalVolumeSpinnerModel = new SpinnerNumberModel(10.0, 0.1,
								999.9, 0.5);
						
						totalVolumeSpinner = new JSpinner();
						totalVolPanel.add(totalVolumeSpinner);
						totalVolumeSpinner.setModel(totalVolumeSpinnerModel);
						totalVolumeSpinner.setPreferredSize(new java.awt.Dimension(91, 21));
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

}
