/*
 * Created on May 25, 2005
 * $Id: MashPanel.java,v 1.12 2006/06/02 19:44:26 andrew_avis Exp $
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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.border.BevelBorder;
import javax.swing.table.TableColumn;

import ca.strangebrew.Quantity;
import ca.strangebrew.Recipe;
import ca.strangebrew.SBStringUtils;


/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class MashPanel extends javax.swing.JPanel implements ActionListener, FocusListener {
	private JScrollPane jScrollPane1;
	private JPanel totalsPanel;
	private JTable tblMash;
	private JPanel buttonsPanel;
	private JButton delStepButton;
	private JButton addStepButton;
	private JPanel jPanel1;
	private JLabel jLabel20;
	private JLabel jLabel19;
	private JLabel jLabel18;
	private JLabel jLabel17;
	private JLabel totalMashLabel;
	private JLabel jLabel16;
	private JLabel jLabel15;
	private JLabel jLabel14;
	private JLabel jLabel13;
	private JLabel boilTempULbl;
	private JTextField boilTempTxt;
	private JLabel jLabel1;
	private JToolBar jToolBar1;
	private JLabel tempLostULabel;
	private JTextField tunLossTxt;
	private JLabel grainTempULabel;
	private JTextField grainTempText;
	private JLabel volLabel;
	private JLabel totalTimeLabel;
	private JComboBox ratioUnitsCombo;
	private JTextField ratioText;
	// private ButtonGroup tempUnitsButtonGroup;
	private JComboBox volUnitsCombo;
	private ComboModel volUnitsComboModel;
	private JRadioButton tempCrb;
	private JRadioButton tempFrb;
	private ButtonGroup tempBg;
	private JPanel settingsPanel;
	private JPanel tablePanel;

	private Recipe myRecipe;
	private MashTableModel mashModel;

	/**
	 * Auto-generated main method to display this JFrame
	 */
	/*
	 * public static void main(String[] args) { MashManager inst = new
	 * MashManager(); inst.setVisible(true); }
	 */

	public MashPanel(Recipe r) {
		super();
		initGUI();
		myRecipe = r;
		if (myRecipe != null) {
			mashModel.setData(myRecipe.getMash());

		}
		displayMash();
		tblMash.updateUI();
	}

	public void setData(Recipe r) {
		myRecipe = r;
		mashModel.setData(myRecipe.mash);
		displayMash();
		tblMash.updateUI();
	}

	private void initGUI() {
		try {
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.columnWeights = new double[]{0.3};
			thisLayout.columnWidths = new int[]{7};
			thisLayout.rowWeights = new double[]{0.1, 0.8, 0.1};
			thisLayout.rowHeights = new int[]{7, 7, 7};
			this.setLayout(thisLayout);

			{
				tablePanel = new JPanel();
				BorderLayout pnlTableLayout = new BorderLayout();
				tablePanel.setLayout(pnlTableLayout);
				this.add(tablePanel, new GridBagConstraints(0, 0, 2, 2, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0),
						0, 0));
				tablePanel.setName("");
				tablePanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

				{
					jScrollPane1 = new JScrollPane();
					tablePanel.add(jScrollPane1, BorderLayout.CENTER);
					jScrollPane1.setPreferredSize(new java.awt.Dimension(344, 145));

					{
						mashModel = new MashTableModel();
						tblMash = new JTable() {
							public String getToolTipText(MouseEvent e) {
								java.awt.Point p = e.getPoint();
								int rowIndex = rowAtPoint(p);
								return SBStringUtils.multiLineToolTip(40, mashModel
										.getDirectionsAt(rowIndex));

							}
						};
						jScrollPane1.setViewportView(tblMash);
						tblMash.setModel(mashModel);
						tblMash.setAutoCreateColumnsFromModel(false);
						tblMash.getTableHeader().setReorderingAllowed(false);

						// set up type combo
						String[] types = {"acid", "glucan", "protein", "beta", "alpha", "mashout",
								"sparge"};
						JComboBox typesComboBox = new JComboBox(types);
						TableColumn mashColumn = tblMash.getColumnModel().getColumn(0);
						mashColumn.setCellEditor(new DefaultCellEditor(typesComboBox));

						// set up method combo
						String[] methods = {"infusion", "decoction", "decoction thick",
								"decoction thin", "direct"};
						JComboBox methodComboBox = new JComboBox(methods);
						mashColumn = tblMash.getColumnModel().getColumn(1);
						mashColumn.setCellEditor(new DefaultCellEditor(methodComboBox));
					}
				}
			}

			jPanel1 = new JPanel();
			BorderLayout jPanel1Layout = new BorderLayout();
			jPanel1.setLayout(jPanel1Layout);
			this.add(jPanel1, new GridBagConstraints(4, 0, 1, 2, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0,
					0));
			jPanel1.setPreferredSize(new java.awt.Dimension(158, 230));

			totalsPanel = new JPanel();
			GridBagLayout totalsPanelLayout = new GridBagLayout();
			totalsPanelLayout.rowWeights = new double[]{0.1, 0.1, 0.1, 0.1, 0.1, 0.1};
			totalsPanelLayout.rowHeights = new int[]{7, 7, 7, 7, 7, 7};
			totalsPanelLayout.columnWeights = new double[]{0.1, 0.1};
			totalsPanelLayout.columnWidths = new int[]{7, 7};
			totalsPanel.setLayout(totalsPanelLayout);
			this.add(totalsPanel, new GridBagConstraints(2, 0, 1, 2, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0,
					0));

			{
				totalTimeLabel = new JLabel();
				totalsPanel.add(totalTimeLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0),
						0, 0));
				totalTimeLabel.setText("Time");

			}
			{
				buttonsPanel = new JPanel();
				tablePanel.add(buttonsPanel, BorderLayout.SOUTH);
				GridBagLayout buttonsPanelLayout = new GridBagLayout();
				buttonsPanelLayout.rowWeights = new double[]{0.1, 0.1, 0.1, 0.1};
				buttonsPanelLayout.rowHeights = new int[]{7, 7, 7, 7};
				buttonsPanelLayout.columnWeights = new double[]{0.1, 0.1, 0.1, 0.1};
				buttonsPanelLayout.columnWidths = new int[]{7, 7, 7, 7};
				buttonsPanel.setLayout(buttonsPanelLayout);
				buttonsPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

				jToolBar1 = new JToolBar();
				buttonsPanel.add(jToolBar1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0,
								0, 0), 0, 0));
				jToolBar1.setFloatable(false);
				jToolBar1.setPreferredSize(new java.awt.Dimension(76, 18));
				{
					tempFrb = new JRadioButton();
					buttonsPanel.add(tempFrb, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
							GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0,
									0), 0, 0));
					tempFrb.setText("F");
					tempFrb.addActionListener(this);
				}

				tempBg = new ButtonGroup();
				tempBg.add(tempFrb);
				{
					tempCrb = new JRadioButton();
					buttonsPanel.add(tempCrb, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
							GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0,
									0), 0, 0));
					tempCrb.setText("C");
					tempCrb.addActionListener(this);

					jLabel13 = new JLabel();
					buttonsPanel.add(jLabel13, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
							GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0,
									0), 0, 0));
					jLabel13.setText("Temp Units:");

				}
				tempBg.add(tempCrb);

				jLabel18 = new JLabel();
				totalsPanel.add(jLabel18, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0),
						0, 0));
				jLabel18.setText("Total Time:");

				jLabel16 = new JLabel();
				totalsPanel.add(jLabel16, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0),
						0, 0));
				jLabel16.setText("Total Weight:");

				totalMashLabel = new JLabel();
				totalsPanel.add(totalMashLabel, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0),
						0, 0));

				jLabel17 = new JLabel();
				totalsPanel.add(jLabel17, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0),
						0, 0));
				jLabel17.setText("Total Vol:");

				{
					delStepButton = new JButton();
					jToolBar1.add(delStepButton);
					delStepButton.setText("-");

					delStepButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							delStepButtonActionPerformed(evt);
						}
					});
				}
				{
					addStepButton = new JButton();
					jToolBar1.add(addStepButton);
					addStepButton.setText("+");

					addStepButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							addStepButtonActionPerformed(evt);
						}
					});
				}
			}
			{
				volLabel = new JLabel();
				totalsPanel.add(volLabel, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0),
						0, 0));
				volLabel.setText("10");
			}

			{
				settingsPanel = new JPanel();
				jPanel1.add(settingsPanel, BorderLayout.CENTER);
				GridBagLayout settingsPanelLayout = new GridBagLayout();
				settingsPanelLayout.rowWeights = new double[]{0.1, 0.1, 0.1, 0.1, 0.1};
				settingsPanelLayout.rowHeights = new int[]{7, 7, 7, 7, 7};
				settingsPanelLayout.columnWeights = new double[]{0.3};
				settingsPanelLayout.columnWidths = new int[]{7};
				settingsPanel.setLayout(settingsPanelLayout);
				settingsPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
				settingsPanel.setPreferredSize(new java.awt.Dimension(172, 175));

				jLabel14 = new JLabel();
				settingsPanel.add(jLabel14, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
						GridBagConstraints.EAST, GridBagConstraints.NONE,
						new Insets(0, 0, 0, 0), 0, 0));
				jLabel14.setText("Vol Units:");
				
				jLabel15 = new JLabel();
				settingsPanel.add(jLabel15, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
						GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0),
						0, 0));
				jLabel15.setText("Ratio: 1:");
				{
					ratioText = new JTextField();
					settingsPanel.add(ratioText, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
							GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0,
									0, 0, 0), 0, 0));
					ratioText.setText("1.25");
					ratioText.setPreferredSize(new java.awt.Dimension(51, 20));
					ratioText.addFocusListener(this);
					ratioText.addActionListener(this);
				}
				{
					ComboBoxModel ratioUnitsComboModel = new DefaultComboBoxModel(new String[]{
							"qt/lb", "l/kg"});
					this.setPreferredSize(new java.awt.Dimension(502, 276));
					this.addFocusListener(new FocusAdapter() {
						public void focusGained(FocusEvent evt) {
							displayMash();
						}
					});
					ratioUnitsCombo = new JComboBox();
					settingsPanel.add(ratioUnitsCombo, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
							GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0,
									0), 0, 0));
					ratioUnitsCombo.setModel(ratioUnitsComboModel);
					ratioUnitsCombo.addActionListener(this);
				}
				{
					grainTempText = new JTextField();
					settingsPanel.add(grainTempText, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
							GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0,
									0, 0, 0), 0, 0));
					grainTempText.setText("10");
					grainTempText.setPreferredSize(new java.awt.Dimension(50, 20));

					grainTempText.addFocusListener(this);
					grainTempText.addActionListener(this);
				}
				{
					grainTempULabel = new JLabel();
					settingsPanel.add(grainTempULabel, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0,
							GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0,
									0), 0, 0));
					grainTempULabel.setText("F");

				}

				jLabel19 = new JLabel();
				settingsPanel.add(jLabel19, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
						GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0),
						0, 0));
				jLabel19.setText("Grain Tmp:");

				boilTempTxt = new JTextField();
				settingsPanel.add(boilTempTxt, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0,
								0, 0), 0, 0));
				boilTempTxt.setText("212");
				boilTempTxt.setPreferredSize(new java.awt.Dimension(52, 20));

				jLabel20 = new JLabel();
				settingsPanel.add(jLabel20, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
						GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0),
						0, 0));
				jLabel20.setText("Boil Temp:");

				boilTempULbl = new JLabel();
				settingsPanel.add(boilTempULbl, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0),
						0, 0));
				boilTempULbl.setText("F");

				{
					tunLossTxt = new JTextField();
					settingsPanel.add(tunLossTxt, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0,
							GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0,
									0, 0, 0), 0, 0));
					tunLossTxt.setPreferredSize(new java.awt.Dimension(52, 20));

					tunLossTxt.addFocusListener(this);
					tunLossTxt.addActionListener(this);
				}
				{
					tempLostULabel = new JLabel();
					settingsPanel.add(tempLostULabel, new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0,
							GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0,
									0), 0, 0));
					tempLostULabel.setText("F");
				}

				jLabel1 = new JLabel();
				settingsPanel.add(jLabel1, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
						GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0),
						0, 0));
				jLabel1.setText("Lost in Tun:");
				{

					volUnitsComboModel = new ComboModel();
					volUnitsComboModel.setList(new Quantity().getListofUnits("vol", true));
					volUnitsCombo = new JComboBox();
					settingsPanel.add(volUnitsCombo, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
							GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0,
									0, 0, 0), 0, 0));
					volUnitsCombo.setModel(volUnitsComboModel);
					volUnitsCombo.setPreferredSize(new java.awt.Dimension(57, 20));
					volUnitsCombo.addActionListener(this);
				}

				boilTempTxt.addFocusListener(this);
				boilTempTxt.addActionListener(this);

			}

			totalMashLabel.setText("10");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void displayMash() {
		if (myRecipe != null) {

			volUnitsComboModel.addOrInsert(Quantity.getVolAbrv(myRecipe.mash.getMashVolUnits()));

			// temp units:
			if (myRecipe.mash.getMashTempUnits().equals("F"))
				tempFrb.setSelected(true);
			else
				tempCrb.setSelected(true);
			grainTempULabel.setText(myRecipe.mash.getMashTempUnits());
			tempLostULabel.setText(myRecipe.mash.getMashTempUnits());
			boilTempULbl.setText(myRecipe.mash.getMashTempUnits());

			// set totals:
			String mashWeightTotal = SBStringUtils.format(myRecipe.getTotalMash(), 1) + " "
					+ myRecipe.getMaltUnits();
			totalMashLabel.setText(mashWeightTotal);
			totalTimeLabel.setText(new Integer(myRecipe.mash.getMashTotalTime()).toString());
			volLabel.setText(myRecipe.mash.getMashTotalVol());
			grainTempText.setText(new Double(myRecipe.mash.getGrainTemp()).toString());
			boilTempTxt.setText(new Double(myRecipe.mash.getBoilTemp()).toString());
			tunLossTxt.setText(SBStringUtils.format(myRecipe.mash.getTunLoss(), 1));
			tempFrb.setSelected(myRecipe.mash.getMashTempUnits().equalsIgnoreCase("F"));

		}
	}

	private void addStepButtonActionPerformed(ActionEvent evt) {
		myRecipe.mash.addStep();
		tblMash.updateUI();
		displayMash();

	}

	private void delStepButtonActionPerformed(ActionEvent evt) {
		int i = tblMash.getSelectedRow();
		if (i > -1) {
			myRecipe.mash.delStep(i);
			tblMash.updateUI();
			displayMash();
		}

	}


	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();

		if (o == ratioText) {
			double d = Double.parseDouble(ratioText.getText());
			myRecipe.mash.setMashRatio(d);
		} else if (o == volUnitsCombo) {
			String s = (String) volUnitsComboModel.getSelectedItem();
			myRecipe.mash.setMashVolUnits(s);

		} else if (o == ratioUnitsCombo) {
			String s = (String) ratioUnitsCombo.getSelectedItem();
			myRecipe.mash.setMashRatioU(s);
		} else if (o == tempFrb) {
			myRecipe.mash.setMashTempUnits("F");
			myRecipe.mash.calcMashSchedule();
		} else if (o == tempCrb) {
			myRecipe.mash.setMashTempUnits("C");
			myRecipe.mash.calcMashSchedule();

		} else if (o == grainTempText) {
			String s = grainTempText.getText();
			myRecipe.mash.setGrainTemp(Double.parseDouble(s));
		} else if (o == boilTempTxt) {
			String s = boilTempTxt.getText();
			myRecipe.mash.setBoilTemp(Double.parseDouble(s));
		} else if (o == tunLossTxt) {
			String s = tunLossTxt.getText();
			myRecipe.mash.setTunLoss(Double.parseDouble(s));
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

	public class Notifier {
		public void displMash() {
			displayMash();
		}
	}

}
