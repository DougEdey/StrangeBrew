/*
 * Created on May 25, 2005
 * $Id: MashManager.java,v 1.1 2006/04/07 13:59:14 andrew_avis Exp $
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

package ca.strangebrew.ui.swing;

import java.awt.GridBagLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import java.awt.Insets;
import java.awt.GridBagConstraints;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import javax.swing.JLabel;
import java.awt.BorderLayout;

import javax.swing.DefaultCellEditor;
import javax.swing.JRadioButton;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JTextField;

import ca.strangebrew.Quantity;
import ca.strangebrew.Recipe;


public class MashManager extends javax.swing.JFrame implements ActionListener, FocusListener {
	private JScrollPane jScrollPane1;
	private JTable tblMash;
	private JPanel pnlButtons;
	private JPanel buttonsPanel;
	private JButton delStepButton;
	private JButton addStepButton;
	private JLabel recipeNameLabel;
	private JLabel titleLabel;
	private JPanel directionsPanel;
	private JLabel tempLostULabel;
	private JTextField tempLostText;
	private JPanel tempLostPanel;
	private JLabel grainTempULabel;
	private JTextField grainTempText;
	private JPanel grainTempPanel;
	private JLabel volLabel;
	private JPanel volPanel;
	private JPanel moreSettingsPanel;
	private JLabel totalTimeLabel;
	private JPanel timePanel;
	private JComboBox ratioUnitsCombo;
	private JLabel totalMashLabel;
	private JPanel weightPanel;
	private JTextField ratioText;
	private JLabel ratioLabel;
	private JPanel ratioPanel;
	private JTextArea directionsTextArea;
	private ButtonGroup tempUnitsButtonGroup;
	private JComboBox volUnitsCombo;
	private ComboModel volUnitsComboModel;
	private JPanel volUnitsPanel;
	private JRadioButton tempCrb;
	private JRadioButton tempFrb;
	private JPanel tempPanel;
	private JPanel settingsPanel;
	private JPanel titlePanel;
	private JButton btnOk;
	private JPanel tablePanel;

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
		if (myRecipe != null) {
			mashModel.setData(myRecipe.getMash());

		}
		displayMash();
	}

	private void initGUI() {
		try {
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.columnWeights = new double[]{0.1, 0.1};
			thisLayout.columnWidths = new int[]{7, 7};
			thisLayout.rowWeights = new double[] {0.1,0.8,0.1,0.1,0.1};
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
						new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, GridBagConstraints.NORTH,
								GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				{
					titleLabel = new JLabel();
					titlePanel.add(titleLabel);
					titleLabel.setText("Mash schedule for:");
					titleLabel.setFont(new java.awt.Font("Dialog", 0, 12));
				}
				{
					recipeNameLabel = new JLabel();
					titlePanel.add(recipeNameLabel);
					recipeNameLabel.setText("Recipe Name");
				}
			}
			{
				tablePanel = new JPanel();
				BorderLayout pnlTableLayout = new BorderLayout();
				tablePanel.setLayout(pnlTableLayout);
				this.getContentPane().add(
						tablePanel,
						new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
								GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				tablePanel.setName("");
				tablePanel.setBorder(BorderFactory.createTitledBorder("Mash Steps"));
				{
					jScrollPane1 = new JScrollPane();
					tablePanel.add(jScrollPane1, BorderLayout.CENTER);
					jScrollPane1.setPreferredSize(new java.awt.Dimension(424, -32));
					{
						mashModel = new MashTableModel(this);
						tblMash = new JTable();
						jScrollPane1.setViewportView(tblMash);
						tblMash.setModel(mashModel);
						tblMash.addMouseListener(new MouseAdapter() {
							public void mouseClicked(MouseEvent evt) {
								int i = tblMash.getSelectedRow();
								directionsTextArea.setText(myRecipe.mash.getStepDirections(i));
							}
						});
						
						// set up type combo
						String [] types = {"acid","gluten","protein","beta","alpha","mashout"};
						JComboBox typesComboBox = new JComboBox(types);
						TableColumn mashColumn = tblMash.getColumnModel().getColumn(0);
						mashColumn.setCellEditor(new DefaultCellEditor(typesComboBox));
						
						// set up method combo
						String [] methods = {"infusion","decoction","direct"};
						JComboBox methodComboBox = new JComboBox(methods);
						mashColumn = tblMash.getColumnModel().getColumn(1);
						mashColumn.setCellEditor(new DefaultCellEditor(methodComboBox));
					}
				}
				{
					buttonsPanel = new JPanel();
					FlowLayout buttonsPanelLayout = new FlowLayout();
					buttonsPanelLayout.setAlignment(FlowLayout.LEFT);
					buttonsPanel.setLayout(buttonsPanelLayout);
					tablePanel.add(buttonsPanel, BorderLayout.SOUTH);
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
					{
						weightPanel = new JPanel();
						BoxLayout statsPanelLayout = new BoxLayout(weightPanel, javax.swing.BoxLayout.X_AXIS);
						weightPanel.setLayout(statsPanelLayout);
						buttonsPanel.add(weightPanel);
						weightPanel.setBorder(BorderFactory.createTitledBorder("Total Weight"));
						weightPanel.setPreferredSize(new java.awt.Dimension(98, 39));
						{
							totalMashLabel = new JLabel();
							weightPanel.add(totalMashLabel);
							totalMashLabel.setPreferredSize(new java.awt.Dimension(118, 13));
							totalMashLabel.setText("total");
						}
					}
					{
						timePanel = new JPanel();
						BoxLayout timePanelLayout = new BoxLayout(timePanel, javax.swing.BoxLayout.X_AXIS);
						timePanel.setLayout(timePanelLayout);
						buttonsPanel.add(timePanel);
						timePanel.setBorder(BorderFactory.createTitledBorder("Total Min"));
						timePanel.setPreferredSize(new java.awt.Dimension(79, 43));
						{
							totalTimeLabel = new JLabel();
							timePanel.add(totalTimeLabel);
							totalTimeLabel.setText("Time");
							totalTimeLabel.setPreferredSize(new java.awt.Dimension(111, 17));
						}
					}
					{
						volPanel = new JPanel();
						BoxLayout volPanelLayout = new BoxLayout(volPanel, javax.swing.BoxLayout.X_AXIS);
						volPanel.setLayout(volPanelLayout);
						buttonsPanel.add(volPanel);
						volPanel.setBorder(BorderFactory.createTitledBorder("Total Vol"));
						volPanel.setPreferredSize(new java.awt.Dimension(117, 42));
						{
							volLabel = new JLabel();
							volPanel.add(volLabel);
							volLabel.setText("10");
						}
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
						new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
								GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				{
					tempPanel = new JPanel();
					settingsPanel.add(tempPanel);
					tempPanel.setBorder(BorderFactory.createTitledBorder("Temp Units"));
					{
						tempFrb = new JRadioButton();
						tempPanel.add(tempFrb);
						tempFrb.setText("F");
						tempFrb.addActionListener(this);
					}
					{
						tempCrb = new JRadioButton();
						tempPanel.add(tempCrb);
						tempCrb.setText("C");
						tempCrb.addActionListener(this);
						
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
						// volList = new ArrayList(q.getListofUnits("vol"));
						
						
						volUnitsComboModel = new ComboModel();						
						volUnitsComboModel.setList(new Quantity().getListofUnits("vol"));
						volUnitsCombo = new JComboBox();
						volUnitsPanel.add(volUnitsCombo);
						volUnitsCombo.setModel(volUnitsComboModel);
						volUnitsCombo.addActionListener(this);
					}
				}
				{
					ratioPanel = new JPanel();
					settingsPanel.add(ratioPanel);
					ratioPanel.setBorder(BorderFactory.createTitledBorder("Mash Ratio"));
					{
						ratioLabel = new JLabel();
						ratioPanel.add(ratioLabel);
						ratioLabel.setText("1:");
					}
					{
						ratioText = new JTextField();
						ratioPanel.add(ratioText);
						ratioText.setText("1.25");
						ratioText.addFocusListener(this);
						ratioText.addActionListener(this); 
					}
					{
						ComboBoxModel ratioUnitsComboModel = new DefaultComboBoxModel(new String[] {
								"qt/lb", "l/kg" });
						ratioUnitsCombo = new JComboBox();
						ratioPanel.add(ratioUnitsCombo);
						ratioUnitsCombo.setModel(ratioUnitsComboModel);
						ratioUnitsCombo.addActionListener(this);
					}
				}
			}
			{
				directionsPanel = new JPanel();
				this.getContentPane().add(
					directionsPanel,
					new GridBagConstraints(
						0,
						3,
						1,
						1,
						0.0,
						0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL,
						new Insets(0, 0, 0, 0),
						0,
						0));
				BorderLayout directionsPanelLayout = new BorderLayout();
				directionsPanel.setLayout(directionsPanelLayout);
				directionsPanel.setBorder(BorderFactory.createTitledBorder("Directions"));
				directionsPanel.setPreferredSize(new java.awt.Dimension(181, 75));
				{
					directionsTextArea = new JTextArea();
					directionsPanel.add(directionsTextArea, BorderLayout.CENTER);
					directionsTextArea.setText("Directions");
					directionsTextArea.setPreferredSize(new java.awt.Dimension(171, 38));
					directionsTextArea.setEditable(false);
					directionsTextArea.setLineWrap(true);
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
			{
				moreSettingsPanel = new JPanel();
				FlowLayout moreSettingsPanelLayout = new FlowLayout();
				moreSettingsPanelLayout.setAlignment(FlowLayout.LEFT);
				moreSettingsPanel.setLayout(moreSettingsPanelLayout);
				this.getContentPane().add(
					moreSettingsPanel,
					new GridBagConstraints(
						1,
						3,
						1,
						1,
						0.0,
						0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL,
						new Insets(0, 0, 0, 0),
						0,
						0));
				{
					grainTempPanel = new JPanel();
					BoxLayout grainTempPanelLayout = new BoxLayout(grainTempPanel, javax.swing.BoxLayout.X_AXIS);
					grainTempPanel.setLayout(grainTempPanelLayout);
					moreSettingsPanel.add(grainTempPanel);
					grainTempPanel.setBorder(BorderFactory.createTitledBorder("Grain Temp"));
					grainTempPanel.setPreferredSize(new java.awt.Dimension(94, 45));
					{
						grainTempText = new JTextField();
						grainTempPanel.add(grainTempText);
						grainTempText.setText("10");
						grainTempText.setPreferredSize(new java.awt.Dimension(67, 15));
						grainTempText.addFocusListener(this);
						grainTempText.addActionListener(this);
					}
					{
						grainTempULabel = new JLabel();
						grainTempPanel.add(grainTempULabel);
						grainTempULabel.setText("F");
					}
				}
				{
					tempLostPanel = new JPanel();
					BoxLayout tempLostPanelLayout = new BoxLayout(tempLostPanel, javax.swing.BoxLayout.X_AXIS);
					tempLostPanel.setLayout(tempLostPanelLayout);
					moreSettingsPanel.add(tempLostPanel);
					tempLostPanel.setPreferredSize(new java.awt.Dimension(104, 45));
					tempLostPanel.setBorder(BorderFactory.createTitledBorder("Tun Temp Lost"));
					{
						tempLostText = new JTextField();
						tempLostPanel.add(tempLostText);
						tempLostText.setText("3");
						tempLostText.setEditable(false);
						tempLostText.addFocusListener(this);
						tempLostText.addActionListener(this);
					}
					{
						tempLostULabel = new JLabel();
						tempLostPanel.add(tempLostULabel);
						tempLostULabel.setText("F");
					}
				}
			}
			pack();
			this.setSize(468, 400);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	

	public void displayMash() {
		if (myRecipe != null) {
			recipeNameLabel.setText(myRecipe.getName());
			mashModel.setData(myRecipe.getMash());
			tblMash.updateUI();
			volUnitsComboModel.addOrInsert(myRecipe.mash.getMashVolUnits());
			
			// temp units:
			if (myRecipe.mash.getMashTempUnits().equals("F"))
				tempFrb.setSelected(true);
			else
				tempCrb.setSelected(true);						
			grainTempULabel.setText(myRecipe.mash.getMashTempUnits());
			tempLostULabel.setText(myRecipe.mash.getMashTempUnits());
			
			// set totals:
			String mashWeightTotal = myRecipe.df1.format(myRecipe.getTotalMash()) 
				+ " " + myRecipe.getMaltUnits();
			totalMashLabel.setText(mashWeightTotal);
			totalTimeLabel.setText(new Integer(myRecipe.mash.getMashTotalTime()).toString());
			volLabel.setText(myRecipe.mash.getMashTotalVol());
			grainTempText.setText(new Double(myRecipe.mash.getGrainTemp()).toString());			
			
			int i = tblMash.getSelectedRow();
			if (i>-1){
				directionsTextArea.setText(myRecipe.mash.getStepDirections(i));
			}
			
		}
	}

	private void addStepButtonActionPerformed(ActionEvent evt) {
		myRecipe.mash.addStep();
		tblMash.updateUI();
		displayMash();

	}

	private void delStepButtonActionPerformed(ActionEvent evt) {
		int i = tblMash.getSelectedRow();
		myRecipe.mash.delStep(i);
		tblMash.updateUI();
		displayMash();

	}
	
	//	Make the button do the same thing as the default close operation
	//(DISPOSE_ON_CLOSE).
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if (o == btnOk) {
			setVisible(false);
			dispose();	
			return;
		}
		else if (o == ratioText) {
			double d = Double.parseDouble(ratioText.getText());
			myRecipe.mash.setMashRatio(d);			
		}
		else if (o == volUnitsCombo){
			String s = (String)volUnitsComboModel.getSelectedItem();
			myRecipe.mash.setMashVolUnits(s);
						
		}
		else if (o == ratioUnitsCombo) {
			String s = (String)ratioUnitsCombo.getSelectedItem();
			myRecipe.mash.setMashRatioU(s);			
		}
		else if (o == tempFrb) {
			myRecipe.mash.setMashTempUnits("F");
			myRecipe.mash.calcMashSchedule();					
		}
		else if (o == tempCrb) {
			myRecipe.mash.setMashTempUnits("C");
			myRecipe.mash.calcMashSchedule();
						
		}		
		else if (o == grainTempText) {
			String s = grainTempText.getText();
			myRecipe.mash.setGrainTemp(Double.parseDouble(s));
		}
		
		tblMash.updateUI();
		displayMash();	
		
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
