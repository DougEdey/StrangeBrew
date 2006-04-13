/**
    StrangeBrew Java - a homebrew recipe calculator
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


import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import ca.strangebrew.Debug;
import ca.strangebrew.Options;
import ca.strangebrew.Quantity;


/**
 * @author aavis
 * 
 * This class creates a tabbed dialog box with all the preferences
 * used by the application.  The constructor will initialize all the
 * UI components to values from the Options object in the constructor.
 * 
 * If the dialog box is closed with the OK button then the Options object
 * given in the constructor will be updated with new values entered by
 * the user.  If the dialog box is closed any other way then no changes will
 * be made to the Options object.
 */
public class PreferencesDialog extends javax.swing.JDialog implements ActionListener
{

	private boolean m_savePreferences = false;
	private Options opts = null;

	private JPanel pnlBrewer;

	private JTextField txtBottleSize;

	private JButton okButton;
	private JPanel pnlButtons;
	private JLabel jLabel8;
	private JPanel pnlSortOrder;
	private JLabel jLabel4;
	private JPanel pnlCalculations;
	private JTextField txtEmail;
	private JLabel jLabel7;
	private JTextField txtClubName;
	private JLabel jLabel6;
	private JTextField txtPhone;
	private JLabel jLabel5;
	private JTextField txtBrewerName;
	private JButton jButton2;
	private JLabel jLabel3;
	private JPanel carbPanel;
	private JComboBox cmbBottleSize;
	private ComboModel cmbBottleSizeModel;
	private JLabel jLabel2;
	private JTextField txtOtherCost;
	private JLabel jLabel1;
	private JPanel jPanel2;
	private JPanel costCarbPanel;
	private JTabbedPane jTabbedPane1;
	
	// calcs panel:
	private JPanel pnlHopsCalc;
	private ButtonGroup bgHopsCalc = new ButtonGroup();
	private JRadioButton rbTinseth;
	private JRadioButton rbGaretz;
	private JRadioButton rbRager;
	
	private JPanel pnlAlc;	
	private ButtonGroup bgAlc = new ButtonGroup();
	private JRadioButton rbABW;
	private JRadioButton rbABV;

	private JPanel pnlEvaporation;
	private ButtonGroup bgEvap = new ButtonGroup();
	private JRadioButton rbConstant;
	private JRadioButton rbPercent;

	private JPanel pnlColourOptions;
	private ButtonGroup bgColour = new ButtonGroup();
	private JRadioButton rbEBC;
	private JRadioButton rbSRM;
	
	
	private JPanel pnlHops;
	private JTextField txtPellet;
	private JLabel jLabelc2;
	private JTextField txtTinsethUtil;
	private JLabel jLabelc4;
	private JTextField txtFWHTime;
	private JTextField txtLostInTrub;
	private JTextField txtMiscLosses;
	private JTextField txtLeftInKettle;
	private JLabel jLabel11;
	private JLabel jLabel10;
	private JLabel jLabel9;
	private JPanel pnlWaterUsage;
	private JPanel pnlDatabase;
	private JTextField txtMashHopTime;
	private JLabel jLabelc5;
	private JTextField txtDryHopTime;
	private JLabel jLabelc3;
	private JPanel pnlHopTimes;

	private JLabel jLabelc1;

	private JPanel pnlDefaultDB = null;
	private JTextField txtDBLocation = null;
	private JButton btnBrowse = null;


	public PreferencesDialog(Frame owner, Options preferences)
	{
		super(owner, "Recipe Preferences", true);
		opts = preferences;
		layoutUi();
		setLocation(owner.getLocation());
		setOptions();
		
	}

	
	private void setOptions(){
		// cost tab:
		txtOtherCost.setText(opts.getProperty("optMiscCost"));
		txtBottleSize.setText(opts.getProperty("optBottleSize"));
		cmbBottleSizeModel.addOrInsert(opts.getProperty("optBottleU"));
		
		// brewer tab:
		txtBrewerName.setText(opts.getProperty("optBrewer"));
		txtPhone.setText(opts.getProperty("optPhone"));
		txtClubName.setText(opts.getProperty("optClub"));
		txtEmail.setText(opts.getProperty("optEmail"));
		
		// calculations tab:
		rbTinseth.setSelected(opts.getProperty("optIBUCalcMethod").equalsIgnoreCase("Tinseth"));
		rbRager.setSelected(opts.getProperty("optIBUCalcMethod").equalsIgnoreCase("Rager"));
		rbGaretz.setSelected(opts.getProperty("optIBUCalcMethod").equalsIgnoreCase("Garetz"));
		
		rbABV.setSelected((opts.getProperty("optAlcCalcMethod").equalsIgnoreCase("Volume")));
		rbABW.setSelected((opts.getProperty("optAlcCalcMethod").equalsIgnoreCase("Weight")));
		
		rbSRM.setSelected((opts.getProperty("optColourMethod").equalsIgnoreCase("SRM")));
		rbEBC.setSelected((opts.getProperty("optColourMethod").equalsIgnoreCase("EBC")));
		
		rbPercent.setSelected((opts.getProperty("optEvapCalcMethod").equalsIgnoreCase("Percent")));
		rbConstant.setSelected((opts.getProperty("optEvapCalcMethod").equalsIgnoreCase("Constant")));
		
		txtPellet.setText(opts.getProperty("optPelletHopsPct"));
		txtTinsethUtil.setText(opts.getProperty("optHopsUtil"));
		txtDryHopTime.setText(opts.getProperty("optDryHopTime"));
		txtFWHTime.setText(opts.getProperty("optFWHTime"));
		txtMashHopTime.setText(opts.getProperty("optMashHopTime"));
		txtLeftInKettle.setText(opts.getProperty("optKettleLoss"));
		txtMiscLosses.setText(opts.getProperty("optMiscLoss"));
		txtLostInTrub.setText(opts.getProperty("optTrubLoss"));
	}
	
	private void saveOptions(){
		// cost tab:
		opts.setProperty("optMiscCost", txtOtherCost.getText());
		opts.setProperty("optBottleSize", txtBottleSize.getText());
		opts.setProperty("optBottleU", (String)cmbBottleSize.getSelectedItem());
		
		// Brewer tab:
		opts.setProperty("optBrewer", txtBrewerName.getText());
		opts.setProperty("optPhone", txtPhone.getText());
		opts.setProperty("optClub", txtClubName.getText());
		opts.setProperty("optEmail", txtEmail.getText());
		
		// calculations tab:
		if (rbTinseth.isSelected())
			opts.setProperty("optIBUCalcMethod", "Tinseth");
		if (rbRager.isSelected())
			opts.setProperty("optIBUCalcMethod", "Rager");
		if (rbGaretz.isSelected())
			opts.setProperty("optIBUCalcMethod", "Garetz");
		
		if (rbABV.isSelected())
			opts.setProperty("optAlcCalcMethod", "Volume");
		if (rbABW.isSelected())
			opts.setProperty("optAlcCalcMethod", "Weight");
		
		if (rbSRM.isSelected())
			opts.setProperty("optColourMethod", "SRM");
		if (rbEBC.isSelected())
			opts.setProperty("optColourMethod", "EBC");
		
		if (rbPercent.isSelected())
			opts.setProperty("optEvapCalcMethod", "Percent");
		if (rbConstant.isSelected())
			opts.setProperty("optEvapCalcMethod", "Constant");
		
		opts.setProperty("optPelletHopsPct", txtPellet.getText());
		opts.setProperty("optHopsUtil", txtTinsethUtil.getText());
		opts.setProperty("optDryHopTime", txtDryHopTime.getText());
		opts.setProperty("optFWHTime", txtFWHTime.getText());
		opts.setProperty("optMashHopTime", txtMashHopTime.getText());
		opts.setProperty("optKettleLoss", txtLeftInKettle.getText());
		opts.setProperty("optMiscLoss", txtMiscLosses.getText());
		opts.setProperty("optTrubLoss", txtLostInTrub.getText());

		
	}
	
	private void layoutUi()
	{
		
		JPanel buttons = new JPanel();
		okButton = new JButton("OK");
		okButton.addActionListener(this);
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);
		buttons.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttons.add(cancelButton);
		buttons.add(okButton);
		
		getContentPane().setLayout(new BorderLayout());
		this.setFocusTraversalKeysEnabled(false);
		{
			jTabbedPane1 = new JTabbedPane();
			getContentPane().add(jTabbedPane1, BorderLayout.CENTER);
			{
				pnlCalculations = new JPanel();
				jTabbedPane1.addTab("Calculations", null, pnlCalculations, null);
				{
					try {
						{
							GridBagLayout thisLayout = new GridBagLayout();
							thisLayout.rowWeights = new double[] { 0.1, 0.1, 0.1, 0.1 };
							thisLayout.rowHeights = new int[] { 7, 7, 7, 7 };
							thisLayout.columnWeights = new double[] { 0.1, 0.2 };
							thisLayout.columnWidths = new int[] { 7, 7 };
							pnlCalculations.setLayout(thisLayout);
							pnlCalculations.setPreferredSize(new java.awt.Dimension(524, 372));
							{
								{
									bgHopsCalc = new ButtonGroup();
									{
										pnlHops = new JPanel();
										GridLayout pnlHopsLayout = new GridLayout(2, 2);
										pnlHopsLayout.setColumns(2);
										pnlHopsLayout.setHgap(5);
										pnlHopsLayout.setVgap(5);
										pnlHopsLayout.setRows(2);
										pnlHops.setLayout(pnlHopsLayout);
										pnlCalculations.add(pnlHops, new GridBagConstraints(
											1,
											0,
											1,
											1,
											0.0,
											0.0,
											GridBagConstraints.NORTHWEST,
											GridBagConstraints.HORIZONTAL,
											new Insets(0, 0, 0, 0),
											0,
											0));
										pnlHops
											.setBorder(BorderFactory.createTitledBorder("Hops:"));
										{
											jLabelc1 = new JLabel();
											pnlHops.add(jLabelc1);
											jLabelc1.setText("Pellet Hops +%");
										}
										{
											txtPellet = new JTextField();
											pnlHops.add(txtPellet);
											txtPellet.setPreferredSize(new java.awt.Dimension(
												20,
												20));
										}
										{
											jLabelc2 = new JLabel();
											pnlHops.add(jLabelc2);
											jLabelc2.setText("Tinseth Utilization Factor");
										}
										{
											txtTinsethUtil = new JTextField();
											pnlHops.add(txtTinsethUtil);
											txtTinsethUtil.setText("4.15");
										}
									}
									{
										pnlAlc = new JPanel();
										BoxLayout pnlAlcLayout = new BoxLayout(
											pnlAlc,
											javax.swing.BoxLayout.Y_AXIS);
										pnlAlc.setLayout(pnlAlcLayout);
										pnlCalculations.add(pnlAlc, new GridBagConstraints(
											0,
											1,
											1,
											1,
											0.0,
											0.0,
											GridBagConstraints.NORTH,
											GridBagConstraints.HORIZONTAL,
											new Insets(0, 0, 0, 0),
											0,
											0));
										pnlAlc.setBorder(BorderFactory
											.createTitledBorder("Alcohol By:"));
										{
											rbABV = new JRadioButton();
											pnlAlc.add(rbABV);
											bgAlc.add(rbABV);
											rbABV.setText("Volume");
										}
										{
											rbABW = new JRadioButton();
											pnlAlc.add(rbABW);
											bgAlc.add(rbABW);
											rbABW.setText("Weight");
										}
									}
									{
										pnlHopTimes = new JPanel();
										GridLayout pnlHopTimesLayout = new GridLayout(3, 2);
										pnlHopTimesLayout.setColumns(2);
										pnlHopTimesLayout.setHgap(5);
										pnlHopTimesLayout.setVgap(5);
										pnlHopTimesLayout.setRows(3);
										pnlHopTimes.setLayout(pnlHopTimesLayout);
										pnlCalculations.add(pnlHopTimes, new GridBagConstraints(
											1,
											1,
											1,
											1,
											0.0,
											0.0,
											GridBagConstraints.NORTHWEST,
											GridBagConstraints.HORIZONTAL,
											new Insets(0, 0, 0, 0),
											0,
											0));
										pnlHopTimes.setBorder(BorderFactory
											.createTitledBorder("Hop Times:"));
										{
											jLabelc3 = new JLabel();
											pnlHopTimes.add(jLabelc3);
											jLabelc3.setText("Dry (min):");
										}
										{
											txtDryHopTime = new JTextField();
											pnlHopTimes.add(txtDryHopTime);
											txtDryHopTime.setText("0.0");
										}
										{
											jLabelc4 = new JLabel();
											pnlHopTimes.add(jLabelc4);
											jLabelc4.setText("FWH, boil minus (min):");
										}
										{
											txtFWHTime = new JTextField();
											pnlHopTimes.add(txtFWHTime);
											txtFWHTime.setText("20.0");
										}
										{
											jLabelc5 = new JLabel();
											pnlHopTimes.add(jLabelc5);
											jLabelc5.setText("Mash Hop (min):");
										}
										{
											txtMashHopTime = new JTextField();
											pnlHopTimes.add(txtMashHopTime);
											txtMashHopTime.setText("2.0");
										}
									}
								}
								pnlHopsCalc = new JPanel();
								BoxLayout pnlHopsCalcLayout = new BoxLayout(
									pnlHopsCalc,
									javax.swing.BoxLayout.Y_AXIS);
								pnlHopsCalc.setLayout(pnlHopsCalcLayout);
								pnlCalculations.add(pnlHopsCalc, new GridBagConstraints(
									0,
									0,
									1,
									1,
									0.0,
									0.0,
									GridBagConstraints.NORTH,
									GridBagConstraints.HORIZONTAL,
									new Insets(0, 0, 0, 0),
									0,
									0));
								pnlCalculations.add(getPnlWaterUsage(), new GridBagConstraints(
									1,
									2,
									1,
									2,
									0.0,
									0.0,
									GridBagConstraints.NORTHWEST,
									GridBagConstraints.HORIZONTAL,
									new Insets(0, 0, 0, 0),
									0,
									0));
								pnlCalculations.add(getPnlColourOptions(), new GridBagConstraints(
									0,
									2,
									1,
									1,
									0.0,
									0.0,
									GridBagConstraints.NORTH,
									GridBagConstraints.HORIZONTAL,
									new Insets(0, 0, 0, 0),
									0,
									0));
								pnlCalculations.add(getPnlEvaporation(), new GridBagConstraints(
									0,
									3,
									1,
									1,
									0.0,
									0.0,
									GridBagConstraints.NORTH,
									GridBagConstraints.HORIZONTAL,
									new Insets(0, 0, 0, 0),
									0,
									0));
								pnlHopsCalc.setPreferredSize(new java.awt.Dimension(117, 107));
								pnlHopsCalc.setBorder(BorderFactory
									.createTitledBorder("IBU Calc Method:"));
								{
									rbTinseth = new JRadioButton();
									pnlHopsCalc.add(rbTinseth);
									rbTinseth.setText("Tinseth");
									bgHopsCalc.add(rbTinseth);
								}
								{
									rbRager = new JRadioButton();
									pnlHopsCalc.add(rbRager);
									rbRager.setText("Rager");
									bgHopsCalc.add(rbRager);
								}
								{
									rbGaretz = new JRadioButton();
									pnlHopsCalc.add(rbGaretz);
									rbGaretz.setText("Garetz");
									bgHopsCalc.add(rbGaretz);
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			{
				costCarbPanel = new JPanel();
				BorderLayout costCarbPanelLayout = new BorderLayout();
				costCarbPanel.setLayout(costCarbPanelLayout);
				jTabbedPane1.addTab("Cost & Carb", null, costCarbPanel, null); 
				{
					carbPanel = new JPanel();
					costCarbPanel.add(carbPanel, BorderLayout.CENTER);
					carbPanel.setBorder(BorderFactory.createTitledBorder(null, "Carbonation", TitledBorder.LEADING, TitledBorder.TOP));
					{
						jLabel3 = new JLabel();
						carbPanel.add(jLabel3);
						jLabel3.setText("Not implemented");
					}
				}
				{
					jPanel2 = new JPanel();
					costCarbPanel.add(jPanel2, BorderLayout.NORTH);
					GridBagLayout jPanel2Layout = new GridBagLayout();
					jPanel2Layout.rowWeights = new double[] {0.1, 0.1, 0.4};
					jPanel2Layout.rowHeights = new int[] {7, 7, 7};
					jPanel2Layout.columnWeights = new double[] {0.1, 0.1, 0.1};
					jPanel2Layout.columnWidths = new int[] {7, 7, 7};

					jPanel2.setPreferredSize(new java.awt.Dimension(232, 176));
					jPanel2.setBorder(BorderFactory.createTitledBorder("Cost"));
					jPanel2.setLayout(jPanel2Layout);
					{
						jLabel1 = new JLabel();
						jPanel2.add(jLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						jLabel1.setText("Other Cost:");
					}
					{
						txtOtherCost = new JTextField();
						jPanel2.add(txtOtherCost, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						txtOtherCost.setText("$0.00");
					}
					{
						jLabel2 = new JLabel();
						jPanel2.add(jLabel2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						jLabel2.setText("Bottle Size:");
					}
					{
						cmbBottleSize = new JComboBox();
						cmbBottleSizeModel = new ComboModel();
						cmbBottleSizeModel.setList(new Quantity().getListofUnits("vol"));						
						cmbBottleSize.setModel(cmbBottleSizeModel);
						jPanel2.add(cmbBottleSize, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						jPanel2.add(getTxtBottleSize(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						
					}
				}
				{
										

					
				}
			}
			{
				pnlBrewer = new JPanel();
				GridBagLayout pnlBrewerLayout = new GridBagLayout();
				pnlBrewerLayout.rowWeights = new double[] {0.1, 0.1, 0.3, 0.3};
				pnlBrewerLayout.rowHeights = new int[] {2, 2, 7, 7};
				pnlBrewerLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
				pnlBrewerLayout.columnWidths = new int[] {7, 7, 7, 7};
				pnlBrewer.setLayout(pnlBrewerLayout);
				jTabbedPane1.addTab("Brewer", null, pnlBrewer, null);
				{
					jLabel4 = new JLabel();
					pnlBrewer.add(jLabel4, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jLabel4.setText("Name:");
				}
				{
					txtBrewerName = new JTextField();
					pnlBrewer.add(txtBrewerName, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
					txtBrewerName.setText("Your Name");
				}
				{
					jLabel5 = new JLabel();
					pnlBrewer.add(jLabel5, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jLabel5.setText("Phone:");
				}
				{
					txtPhone = new JTextField();
					pnlBrewer.add(txtPhone, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
					txtPhone.setText("Your Phone");
				}
				{
					jLabel6 = new JLabel();
					pnlBrewer.add(jLabel6, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jLabel6.setText("Club Name:");
				}
				{
					txtClubName = new JTextField();
					pnlBrewer.add(txtClubName, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
					txtClubName.setText("Club Name");
				}
				{
					jLabel7 = new JLabel();
					pnlBrewer.add(jLabel7, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jLabel7.setText("Email:");
				}
				{
					txtEmail = new JTextField();
					pnlBrewer.add(txtEmail, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
					txtEmail.setText("Email");
				}
			}
			{
				pnlDatabase = new JPanel();
				BorderLayout pnlDatabaseLayout = new BorderLayout();
				pnlDatabase.setLayout(pnlDatabaseLayout);
				pnlDatabase.add(getPnlDefaultDB(), BorderLayout.NORTH);
				pnlDatabase.add(getPnlSortOrder(), BorderLayout.WEST);
				jTabbedPane1.addTab("Database", null, pnlDatabase, null);
				pnlDatabase.setVisible(false);
			}

		}
		getContentPane().add(BorderLayout.CENTER, jTabbedPane1);
		getContentPane().add(BorderLayout.SOUTH, buttons);
		
		setSize(400, 400);
		

	}

	/**
	 * This method initializes pnlDefaultDB	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPnlDefaultDB() {
		if (pnlDefaultDB == null) {
			pnlDefaultDB = new JPanel();
			BorderLayout pnlDefaultDBLayout = new BorderLayout();
			pnlDefaultDB.setLayout(pnlDefaultDBLayout);
			pnlDefaultDB.setPreferredSize(new java.awt.Dimension(387, 48));
			pnlDefaultDB.setBorder(BorderFactory.createTitledBorder("Default Database:"));
			pnlDefaultDB.add(getBtnBrowse(), BorderLayout.EAST);
			pnlDefaultDB.add(getTxtDBLocation(), BorderLayout.CENTER);
		}
		return pnlDefaultDB;
	}

	/**
	 * This method initializes txtDBLocation	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTxtDBLocation() {
		if (txtDBLocation == null) {
			txtDBLocation = new JTextField();
			txtDBLocation.setHorizontalAlignment(javax.swing.JTextField.LEFT);
		}
		return txtDBLocation;
	}

	/**
	 * This method initializes btnBrowse	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnBrowse() {
		if (btnBrowse == null) {
			btnBrowse = new JButton();
			btnBrowse.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		}
		return btnBrowse;
	}
	
	private JPanel getPnlSortOrder() {
		if (pnlSortOrder == null) {
			pnlSortOrder = new JPanel();
			pnlSortOrder.setBorder(BorderFactory.createTitledBorder(null,"Recipe Sort Order",TitledBorder.LEADING,TitledBorder.TOP));
			pnlSortOrder.setPreferredSize(new java.awt.Dimension(167, 270));
			pnlSortOrder.add(getJLabel8());
		}
		return pnlSortOrder;
	}
	
	private JLabel getJLabel8() {
		if (jLabel8 == null) {
			jLabel8 = new JLabel();
			jLabel8.setText("Not Implemented");
		}
		return jLabel8;
	}

	
	private JPanel getPnlWaterUsage() {
		if (pnlWaterUsage == null) {
			pnlWaterUsage = new JPanel();
			GridLayout pnlWaterUsageLayout = new GridLayout(3, 2);
			pnlWaterUsageLayout.setColumns(2);
			pnlWaterUsageLayout.setHgap(5);
			pnlWaterUsageLayout.setVgap(5);
			pnlWaterUsageLayout.setRows(3);
			pnlWaterUsage.setLayout(pnlWaterUsageLayout);
			pnlWaterUsage.setBorder(BorderFactory.createTitledBorder("Water Usage:"));
			pnlWaterUsage.add(getJLabel10());
			pnlWaterUsage.add(getTxtLeftInKettle());
			pnlWaterUsage.add(getJLabel9());
			pnlWaterUsage.add(getTxtMiscLosses());
			pnlWaterUsage.add(getJLabel11());
			pnlWaterUsage.add(getTxtLostInTrub());
		}
		return pnlWaterUsage;
	}
	
	private JLabel getJLabel9() {
		if (jLabel9 == null) {
			jLabel9 = new JLabel();
			jLabel9.setText("Misc. Losses:");
		}
		return jLabel9;
	}
	
	private JLabel getJLabel10() {
		if (jLabel10 == null) {
			jLabel10 = new JLabel();
			jLabel10.setText("Water Left In Kettle:");
		}
		return jLabel10;
	}
	
	private JLabel getJLabel11() {
		if (jLabel11 == null) {
			jLabel11 = new JLabel();
			jLabel11.setText("Lost in Trub:");
		}
		return jLabel11;
	}
	
	private JTextField getTxtLeftInKettle() {
		if (txtLeftInKettle == null) {
			txtLeftInKettle = new JTextField();
			txtLeftInKettle.setText("0");
		}
		return txtLeftInKettle;
	}
	
	private JTextField getTxtMiscLosses() {
		if (txtMiscLosses == null) {
			txtMiscLosses = new JTextField();
			txtMiscLosses.setText("0");
		}
		return txtMiscLosses;
	}
	
	private JTextField getTxtLostInTrub() {
		if (txtLostInTrub == null) {
			txtLostInTrub = new JTextField();
			txtLostInTrub.setText("0");
		}
		return txtLostInTrub;
	}
	
	private JPanel getPnlColourOptions() {
		if (pnlColourOptions == null) {
			pnlColourOptions = new JPanel();
			BoxLayout pnlColourOptionsLayout = new BoxLayout(
				pnlColourOptions,
				javax.swing.BoxLayout.Y_AXIS);
			pnlColourOptions.setLayout(pnlColourOptionsLayout);
			pnlColourOptions.setBorder(BorderFactory.createTitledBorder("Colour Method:"));
			pnlColourOptions.add(getRbSRM());
			pnlColourOptions.add(getRbEBC());
			bgColour.add(rbSRM);
			bgColour.add(rbEBC);
		}
		return pnlColourOptions;
	}
	
	private JRadioButton getRbSRM() {
		if (rbSRM == null) {
			rbSRM = new JRadioButton();
			rbSRM.setText("SRM");
		}
		return rbSRM;
	}
	
	private JRadioButton getRbEBC() {
		if (rbEBC == null) {
			rbEBC = new JRadioButton();
			rbEBC.setText("EBC");
		}
		return rbEBC;
	}
	
	private JPanel getPnlEvaporation() {
		if (pnlEvaporation == null) {
			pnlEvaporation = new JPanel();
			BoxLayout pnlEvaporationLayout = new BoxLayout(
				pnlEvaporation,
				javax.swing.BoxLayout.Y_AXIS);
			pnlEvaporation.setLayout(pnlEvaporationLayout);
			pnlEvaporation.setBorder(BorderFactory.createTitledBorder("Evaporation:"));
			pnlEvaporation.add(getRbPercent());
			pnlEvaporation.add(getRbConstant());
			bgEvap.add(rbPercent);
			bgEvap.add(rbConstant);
		}
		return pnlEvaporation;
	}
	
	private JRadioButton getRbPercent() {
		if (rbPercent == null) {
			rbPercent = new JRadioButton();
			rbPercent.setText("Percent");
		}
		return rbPercent;
	}
	
	private JRadioButton getRbConstant() {
		if (rbConstant == null) {
			rbConstant = new JRadioButton();
			rbConstant.setText("Constant");
		}
		return rbConstant;
	}
	
	
	private JTextField getTxtBottleSize() {
		if (txtBottleSize == null) {
			txtBottleSize = new JTextField();
			txtBottleSize.setText("351");
		}
		return txtBottleSize;
	}
	
	
	//	Make the button do the same thing as the default close operation
	// (DISPOSE_ON_CLOSE).
	public void actionPerformed(ActionEvent e) {
		saveOptions();
		opts.saveProperties();
		setVisible(false);
		dispose();
	}
	

}
