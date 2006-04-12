/*
 * Created on May 25, 2005
 * $Id: MashManager.java,v 1.4 2006/04/12 16:18:14 andrew_avis Exp $
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

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableColumn;

import ca.strangebrew.Debug;
import ca.strangebrew.Quantity;
import ca.strangebrew.Recipe;
import ca.strangebrew.SBStringUtils;



public class MashManager extends javax.swing.JFrame implements ActionListener, FocusListener {
	private JScrollPane jScrollPane1;
	private JTable tblMash;
	private JPanel pnlButtons;
	private JPanel buttonsPanel;
	private JButton delStepButton;
	private JButton addStepButton;
	private JLabel recipeNameLabel;
	private JLabel titleLabel;
	private JLabel finalUnitsLbl;
	private JLabel miscLosUnitsLbl;
	private JLabel trubLossUnitsLbl;
	private JFormattedTextField finalVolTxt;
	private JFormattedTextField miscLossTxt;
	private JFormattedTextField trubLossTxt;
	private JLabel kettleUnitsLbl;
	private JFormattedTextField kettleTxt;
	private JLabel chillShrinkLbl;
	private JFormattedTextField postBoilTxt;
	private JFormattedTextField collectTxt;
	private JLabel postBoilUnitsLbl;
	private JLabel totalUnitsLbl;
	private JLabel usedInMashUnitsLbl;
	private JLabel absorbedUnitsLbl;
	private JLabel spargeUnitsLbl;
	private JLabel collectUnitsLbl;
	private JLabel spargeWithLbl;
	private JLabel usedMashLbl;
	private JLabel absorbedLbl;
	private JLabel totalWaterLbl;
	private JLabel jLabel11;
	private JLabel jLabel10;
	private JLabel jLabel9;
	private JPanel directionsPanel;
	private JLabel jLabel4;
	private JLabel jLabel8;
	private JLabel jLabel7;
	private JLabel jLabel6;
	private JLabel jLabel5;
	private JLabel jLabel3;
	private JLabel jLabel2;
	private JLabel jLabel1;
	private JPanel waterUsePanel;
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
			thisLayout.columnWeights = new double[] {0.1, 0.1, 0.1};
			thisLayout.columnWidths = new int[] {7, 7, 7};
			thisLayout.rowWeights = new double[] {0.1, 0.8, 0.1, 0.1, 0.1, 0.1};
			thisLayout.rowHeights = new int[] {7, 7, 7, 7, 7, 7};
			getContentPane().setLayout(thisLayout);
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
				getContentPane().add(directionsPanel, new GridBagConstraints(0, 3, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
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
				getContentPane().add(pnlButtons, new GridBagConstraints(1, 5, 2, 1, 0.0, 0.0, GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
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
			}
			{
				waterUsePanel = new JPanel();
				GridBagLayout waterUsePanelLayout = new GridBagLayout();
				waterUsePanelLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1};
				waterUsePanelLayout.rowHeights = new int[] {7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7};
				waterUsePanelLayout.columnWeights = new double[] {0.1, 0.1};
				waterUsePanelLayout.columnWidths = new int[] {7, 7};
				waterUsePanel.setLayout(waterUsePanelLayout);
				getContentPane().add(waterUsePanel, new GridBagConstraints(2, 1, 1, 3, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				waterUsePanel.setBorder(BorderFactory.createTitledBorder(null, "Water Use:", TitledBorder.LEADING, TitledBorder.TOP));
				{
					jLabel1 = new JLabel();
					waterUsePanel.add(jLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jLabel1.setText("Total Water Used:");
				}
				{
					jLabel2 = new JLabel();
					waterUsePanel.add(jLabel2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jLabel2.setText("Used in Mash:");
				}
				{
					jLabel3 = new JLabel();
					waterUsePanel.add(jLabel3, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jLabel3.setText("Absorbed in Mash");
				}
				{
					jLabel4 = new JLabel();
					waterUsePanel.add(jLabel4, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jLabel4.setText("Sparge With:");
				}
				{
					jLabel5 = new JLabel();
					waterUsePanel.add(jLabel5, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jLabel5.setText("To Collect:");
				}
				{
					jLabel6 = new JLabel();
					waterUsePanel.add(jLabel6, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jLabel6.setText("Post Boil:");
				}
				{
					jLabel7 = new JLabel();
					waterUsePanel.add(jLabel7, new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jLabel7.setText("Left in Kettle:");
				}
				{
					jLabel8 = new JLabel();
					waterUsePanel.add(jLabel8, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jLabel8.setText("Chill Shrinkage:");
				}
				{
					jLabel9 = new JLabel();
					waterUsePanel.add(jLabel9, new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jLabel9.setText("Lost in Trub:");
				}
				{
					jLabel10 = new JLabel();
					waterUsePanel.add(jLabel10, new GridBagConstraints(0, 9, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jLabel10.setText("Misc. Losses:");
				}
				{
					jLabel11 = new JLabel();
					waterUsePanel.add(jLabel11, new GridBagConstraints(0, 10, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jLabel11.setText("Final Beer Volume:");
				}
				{
					totalWaterLbl = new JLabel();
					waterUsePanel.add(totalWaterLbl, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
							GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0,
									0), 0, 0));
					totalWaterLbl.setText("0");
				}
				{
					absorbedLbl = new JLabel();
					waterUsePanel.add(absorbedLbl, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
							GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0,
									0), 0, 0));
					absorbedLbl.setText("jLabel13");
				}
				{
					usedMashLbl = new JLabel();
					waterUsePanel.add(usedMashLbl, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
							GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0,
									0), 0, 0));
					usedMashLbl.setText("absorbedUnitsLbl");
				}
				{
					spargeWithLbl = new JLabel();
					waterUsePanel.add(spargeWithLbl, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,
							GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0,
									0), 0, 0));
					spargeWithLbl.setText("l");
				}
				{
					collectUnitsLbl = new JLabel();
					waterUsePanel.add(collectUnitsLbl, new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					collectUnitsLbl.setText("l");
				}
				{
					spargeUnitsLbl = new JLabel();
					waterUsePanel.add(spargeUnitsLbl, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					spargeUnitsLbl.setText("l");
				}
				{
					absorbedUnitsLbl = new JLabel();
					waterUsePanel.add(absorbedUnitsLbl, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					absorbedUnitsLbl.setText("l");
				}
				{
					usedInMashUnitsLbl = new JLabel();
					waterUsePanel.add(usedInMashUnitsLbl, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					usedInMashUnitsLbl.setText("l");
				}
				{
					totalUnitsLbl = new JLabel();
					waterUsePanel.add(totalUnitsLbl, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					totalUnitsLbl.setText("l");
				}
				{
					postBoilUnitsLbl = new JLabel();
					waterUsePanel.add(postBoilUnitsLbl, new GridBagConstraints(2, 5, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					postBoilUnitsLbl.setText("l");
				}
				{
					collectTxt = new JFormattedTextField();
					collectTxt.addFocusListener(this);
					collectTxt.addActionListener(this);
					waterUsePanel.add(collectTxt, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0,
							GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0,
									0, 0, 0), 0, 0));
					collectTxt.setText("l");
				}
				{
					postBoilTxt = new JFormattedTextField();
					postBoilTxt.addFocusListener(this);
					postBoilTxt.addActionListener(this);
					waterUsePanel.add(postBoilTxt, new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0,
							GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0,
									0, 0, 0), 0, 0));
					postBoilTxt.setText("l");
				}
				{
					chillShrinkLbl = new JLabel();
					waterUsePanel.add(chillShrinkLbl, new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0,
							GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0,
									0), 0, 0));
					chillShrinkLbl.setText("l");
				}
				{
					kettleTxt = new JFormattedTextField();
					kettleTxt.addFocusListener(this);
					kettleTxt.addActionListener(this);
					waterUsePanel.add(kettleTxt, new GridBagConstraints(1, 7, 1, 1, 0.0, 0.0,
							GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0,
									0, 0, 0), 0, 0));
					kettleTxt.setText("1");
				}
				{
					kettleUnitsLbl = new JLabel();
					waterUsePanel.add(kettleUnitsLbl, new GridBagConstraints(2, 7, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					kettleUnitsLbl.setText("l");
				}
				{
					trubLossTxt = new JFormattedTextField();
					trubLossTxt.addFocusListener(this);
					trubLossTxt.addActionListener(this);
					waterUsePanel.add(trubLossTxt, new GridBagConstraints(1, 8, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
					trubLossTxt.setText("jTextField1");
				}
				{
					miscLossTxt = new JFormattedTextField();
					miscLossTxt.addFocusListener(this);
					miscLossTxt.addActionListener(this);
					waterUsePanel.add(miscLossTxt, new GridBagConstraints(1, 9, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
					miscLossTxt.setText("jTextField1");
				}
				{
					finalVolTxt = new JFormattedTextField();
					finalVolTxt.addFocusListener(this);
					finalVolTxt.addActionListener(this);
					waterUsePanel.add(finalVolTxt, new GridBagConstraints(1, 10, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
					finalVolTxt.setText("jTextField1");
				}
				{
					trubLossUnitsLbl = new JLabel();
					waterUsePanel.add(trubLossUnitsLbl, new GridBagConstraints(2, 8, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					trubLossUnitsLbl.setText("l");
				}
				{
					miscLosUnitsLbl = new JLabel();
					waterUsePanel.add(miscLosUnitsLbl, new GridBagConstraints(2, 9, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					miscLosUnitsLbl.setText("l");
				}
				{
					finalUnitsLbl = new JLabel();
					waterUsePanel.add(finalUnitsLbl, new GridBagConstraints(2, 10, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					finalUnitsLbl.setText("l");
				}
			}
			{
				grainTempPanel = new JPanel();
				getContentPane().add(grainTempPanel, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				BoxLayout grainTempPanelLayout = new BoxLayout(
					grainTempPanel,
					javax.swing.BoxLayout.X_AXIS);
				grainTempPanel.setLayout(grainTempPanelLayout);
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
				getContentPane().add(tempLostPanel, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				BoxLayout tempLostPanelLayout = new BoxLayout(
					tempLostPanel,
					javax.swing.BoxLayout.X_AXIS);
				tempLostPanel.setLayout(tempLostPanelLayout);
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
			String mashWeightTotal = SBStringUtils.df1.format(myRecipe.getTotalMash()) 
				+ " " + myRecipe.getMaltUnits();
			totalMashLabel.setText(mashWeightTotal);
			totalTimeLabel.setText(new Integer(myRecipe.mash.getMashTotalTime()).toString());
			volLabel.setText(myRecipe.mash.getMashTotalVol());
			grainTempText.setText(new Double(myRecipe.mash.getGrainTemp()).toString());			
			
			int i = tblMash.getSelectedRow();
			if (i>-1){
				directionsTextArea.setText(myRecipe.mash.getStepDirections(i));
			}
			
			displayWater();
			
		}
	}
	
	public void displayWater(){
		
		String recipeUnitsAbrv = new Quantity().getVolAbrv(myRecipe.getVolUnits());
		String mashUnitsAbrv = new Quantity().getVolAbrv(myRecipe.mash.getMashVolUnits());

		totalUnitsLbl.setText(recipeUnitsAbrv);
		
		usedMashLbl.setText(myRecipe.mash.getTotalWaterStr());
		usedInMashUnitsLbl.setText(mashUnitsAbrv);		
		absorbedLbl.setText(myRecipe.mash.getAbsorbedStr());
		absorbedUnitsLbl.setText(mashUnitsAbrv);
		spargeWithLbl.setText(myRecipe.getSparge());
		spargeUnitsLbl.setText(mashUnitsAbrv);
		
		collectTxt.setValue(new Double(myRecipe.getPreBoilVol(myRecipe.getVolUnits())));
		collectUnitsLbl.setText(recipeUnitsAbrv);
		postBoilTxt.setValue(new Double(myRecipe.getPostBoilVol(myRecipe.getVolUnits())));
		postBoilUnitsLbl.setText(recipeUnitsAbrv);
		
		chillShrinkLbl.setText(myRecipe.getChillShrink());
		kettleTxt.setValue(new Double(myRecipe.getKettleLoss()));
		kettleUnitsLbl.setText(recipeUnitsAbrv);
		trubLossTxt.setValue(new Double(myRecipe.getTrubLoss()));
		trubLossUnitsLbl.setText(recipeUnitsAbrv);
		miscLossTxt.setValue(new Double(myRecipe.getMiscLoss()));
		miscLosUnitsLbl.setText(recipeUnitsAbrv);
		
		finalVolTxt.setValue(new Double(myRecipe.getFinalWortVol()));
		finalUnitsLbl.setText(recipeUnitsAbrv);
		
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
		
		// water use stuff:
		else if (o == kettleTxt) {
			String s = kettleTxt.getText();
			myRecipe.setKettleLoss(Double.parseDouble(s));
		}
		else if (o == miscLossTxt) {
			String s = miscLossTxt.getText();
			myRecipe.setMiscLoss(Double.parseDouble(s));
		}
		else if (o == trubLossTxt) {
			String s = trubLossTxt.getText();
			myRecipe.setTrubLoss(Double.parseDouble(s));
		}
		else if (o == collectTxt) {
			String s = collectTxt.getText();
			myRecipe.setPreBoil(Double.parseDouble(s));

		} else if (o == postBoilTxt) {
			String s = postBoilTxt.getText();
			myRecipe.setPostBoil(Double.parseDouble(s));

		} else if (o == finalVolTxt) {
			String s = finalVolTxt.getText();
			myRecipe.setFinalWortVol(Double.parseDouble(s));

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
