/*
 * Created on May 25, 2005
 * $Id: MashManager.java,v 1.3 2005/05/27 16:21:19 andrew_avis Exp $
 *  @author aavis 
 */

/**
 *  StrangeBrew Java - a homebrew recipe calculator
    Copyright (C) 2005 Drew Avis

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

import java.awt.GridBagLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTable;
import java.awt.Insets;
import java.awt.GridBagConstraints;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.JRadioButton;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.ButtonGroup;
import strangebrew.Recipe;


public class MashManager extends javax.swing.JFrame implements ActionListener{
	private JScrollPane jScrollPane1;
	private JTable tblMash;
	private JPanel pnlButtons;
	private JPanel buttonsPanel;
	private JButton delStepButton;
	private JButton addStepButton;
	private JLabel recipeNameLabel;
	private JLabel titleLabel;
	private ButtonGroup tempUnitsButtonGroup;
	private JComboBox volUnitsCombo;
	private JPanel volUnitsPanel;
	private JRadioButton tempCrb;
	private JRadioButton tempFrb;
	private JPanel tempPanel;
	private JPanel settingsPanel;
	private JPanel titlePanel;
	private JButton btnOk;
	private JPanel pnlTable;
	
	private Recipe myRecipe;
	private MashTableModel mashModel;

	/**
	* Auto-generated main method to display this JFrame
	*/
/*	public static void main(String[] args) {
		MashManager inst = new MashManager();
		inst.setVisible(true);
	}*/
	
	public MashManager(Recipe r) {
		super();
		initGUI();
		myRecipe = r;
		if (myRecipe != null){
    		mashModel.setData(myRecipe.getMash());   		
    		
    	}
	}
	
	private void initGUI() {
		try {
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.columnWeights = new double[] {0.1,0.1};
			thisLayout.columnWidths = new int[] {7,7};
			thisLayout.rowWeights = new double[] {0.1,0.9,0.1,0.1,0.1};
			thisLayout.rowHeights = new int[] {7,7,7,7,7};
			this.getContentPane().setLayout(thisLayout);
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			this.setTitle("Mash Manager");
			{
				{
					
				}
				titlePanel = new JPanel();
				FlowLayout titlePanelLayout = new FlowLayout();
				titlePanelLayout.setAlignment(FlowLayout.LEFT);
				titlePanel.setLayout(titlePanelLayout);
				this.getContentPane().add(
					titlePanel,
					new GridBagConstraints(
						0,
						0,
						2,
						1,
						0.0,
						0.0,
						GridBagConstraints.NORTH,
						GridBagConstraints.HORIZONTAL,
						new Insets(0, 0, 0, 0),
						0,
						0));
				{
					titleLabel = new JLabel();
					titlePanel.add(titleLabel);
					titleLabel.setText("Mash schedule for:");
					titleLabel.setFont(new java.awt.Font("Dialog",0,12));
				}
				{
					recipeNameLabel = new JLabel();
					titlePanel.add(recipeNameLabel);
					recipeNameLabel.setText("Recipe Name");
				}
			}
			{
				pnlTable = new JPanel();
				BorderLayout pnlTableLayout = new BorderLayout();
				pnlTable.setLayout(pnlTableLayout);
				this.getContentPane().add(
					pnlTable,
					new GridBagConstraints(
						0,
						1,
						2,
						1,
						0.0,
						0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.BOTH,
						new Insets(0, 0, 0, 0),
						0,
						0));
				pnlTable.setName("");
				pnlTable.setBorder(BorderFactory.createTitledBorder("Mash Steps"));
				{
					jScrollPane1 = new JScrollPane();
					pnlTable.add(jScrollPane1, BorderLayout.CENTER);
					jScrollPane1.setPreferredSize(new java.awt.Dimension(382, 343));
					{
						mashModel = new MashTableModel();
						tblMash = new JTable();
						jScrollPane1.setViewportView(tblMash);
						tblMash.setModel(mashModel);
					}
				}
				{
					buttonsPanel = new JPanel();
					FlowLayout buttonsPanelLayout = new FlowLayout();
					buttonsPanelLayout.setAlignment(FlowLayout.LEFT);
					buttonsPanel.setLayout(buttonsPanelLayout);
					pnlTable.add(buttonsPanel, BorderLayout.SOUTH);
					{						
						addStepButton = new JButton();
						buttonsPanel.add(addStepButton);						
						addStepButton.setText("+");
						addStepButton.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								addStepButtonActionPerformed(evt);
							}
						});
					}
					{
						delStepButton = new JButton();
						buttonsPanel.add(delStepButton);
						delStepButton.setText("-");
						delStepButton.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								delStepButtonActionPerformed(evt);
							}
						});
					}
				}
			}
			{
				settingsPanel = new JPanel();
				FlowLayout settingsPanelLayout = new FlowLayout();
				settingsPanelLayout.setAlignment(FlowLayout.LEFT);
				settingsPanel.setLayout(settingsPanelLayout);
				this.getContentPane().add(
					settingsPanel,
					new GridBagConstraints(
						0,
						2,
						2,
						1,
						0.0,
						0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL,
						new Insets(0, 0, 0, 0),
						0,
						0));
				{
					tempPanel = new JPanel();
					settingsPanel.add(tempPanel);
					tempPanel.setBorder(BorderFactory.createTitledBorder("Temp Units"));
					{
						tempFrb = new JRadioButton();
						tempPanel.add(tempFrb);
						tempFrb.setText("F");
					}
					{
						tempCrb = new JRadioButton();
						tempPanel.add(tempCrb);
						tempCrb.setText("C");
					}
					tempUnitsButtonGroup = new ButtonGroup();
					tempUnitsButtonGroup.add(tempFrb);
					tempUnitsButtonGroup.add(tempCrb);
					
				}
				{
					volUnitsPanel = new JPanel();
					settingsPanel.add(volUnitsPanel);
					volUnitsPanel.setBorder(BorderFactory.createTitledBorder("Vol Units"));
					{
						ComboBoxModel volUnitsComboModel = new DefaultComboBoxModel(new String[] {
								"Item One", "Item Two" });
						volUnitsCombo = new JComboBox();
						volUnitsPanel.add(volUnitsCombo);
						volUnitsCombo.setModel(volUnitsComboModel);
					}
				}
			}
			{
				pnlButtons = new JPanel();
				FlowLayout pnlButtonsLayout = new FlowLayout();
				pnlButtonsLayout.setAlignment(FlowLayout.RIGHT);
				pnlButtons.setLayout(pnlButtonsLayout);
				this.getContentPane().add(
					pnlButtons,
					new GridBagConstraints(
						0,
						4,
						2,
						1,
						0.0,
						0.0,
						GridBagConstraints.SOUTH,
						GridBagConstraints.HORIZONTAL,
						new Insets(0, 0, 0, 0),
						0,
						0));
				{
					btnOk = new JButton();
					pnlButtons.add(btnOk);
					btnOk.setText("OK");
					btnOk.addActionListener(this);
				}
			}
			pack();
			setSize(400, 300);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	//	Make the button do the same thing as the default close operation
    //(DISPOSE_ON_CLOSE).
    public void actionPerformed(ActionEvent e) {    	
    	setVisible(false);
        dispose();
    }
    
    public void displayMash() {
    	if (myRecipe != null){
    		mashModel.setData(myRecipe.getMash());   		
    		
    	}
    }
    
    private void addStepButtonActionPerformed(ActionEvent evt) {
		myRecipe.mash.addStep();		
		
	}
	
	private void delStepButtonActionPerformed(ActionEvent evt) {
		// myRecipe.mash.de
		
	}

}
