/**
 *    Filename: MashPanel.java
 *     Version: 0.9.0
 * Description: Mash Panel
 *     License: GPLv2
 *        Date: 2017-01-14
 *
 * Copyright (c) 2005 Andrew Avis
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

import java.io.File;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.table.TableColumn;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.homebrewware.Debug;
import com.homebrewware.Mash;
import com.homebrewware.MashDefaults;
import com.homebrewware.Options;
import com.homebrewware.Product;
import com.homebrewware.Quantity;
import com.homebrewware.Recipe;
import com.homebrewware.StringUtils;

public class MashPanel extends javax.swing.JPanel implements ActionListener, FocusListener {

	// Mutables
	private Recipe myRecipe;
	private MashTableModel mashModel;
	private JTable tblMash;

	// GUI Elements
	final private JScrollPane jScrollPane1 = new JScrollPane();
	final private JTextField nameTxt = new JTextField();
	final private JButton defaultsButton = new JButton();
	final private JPopupMenu mashMenu = new JPopupMenu();
	final private JPanel jPanel1 = new JPanel();
	final private JLabel jLabel2 = new JLabel();
	final private JTabbedPane jTabbedPane1 = new JTabbedPane();
	final private JPanel totalsPanel = new JPanel();
	final private JPanel buttonsPanel = new JPanel();
	final private JButton delStepButton = new JButton();
	final private JButton addStepButton = new JButton();
	final private JLabel jLabel20 = new JLabel();
	final private JLabel jLabel19 = new JLabel();
	final private JLabel jLabel18 = new JLabel();
	final private JLabel jLabel17 = new JLabel();
	final private JLabel totalMashLabel = new JLabel();
	final private JLabel jLabel16 = new JLabel();
	final private JLabel jLabel15 = new JLabel();
	final private JLabel jLabel14 = new JLabel();
	final private JLabel jLabel13 = new JLabel();
	final private JLabel boilTempULbl = new JLabel();
	final private JTextField boilTempTxt = new JTextField();
	final private JLabel jLabel1 = new JLabel();
	final private JToolBar jToolBar1 = new JToolBar();
	final private JLabel tempLostULabel = new JLabel();
	final private JTextField tunLossTxt = new JTextField();
	final private JLabel grainTempULabel = new JLabel();
	final private JTextField grainTempText = new JTextField();
	final private JLabel volLabel = new JLabel();
	final private JLabel totalTimeLabel = new JLabel();
	final private JComboBox ratioUnitsCombo = new JComboBox();
	final private ComboBoxModel ratioUnitsComboModel = new DefaultComboBoxModel(Mash.ratioUnits);
	final private JTextField ratioText = new JTextField();

	// Dead space
	final private JLabel deadSpaceLabel = new JLabel();
	final private JTextField deadSpaceText = new JTextField();
	final private JLabel deadSpaceUnit = new JLabel();

	// private ButtonGroup tempUnitsButtonGroup;
	final private JComboBox volUnitsCombo = new JComboBox();
	final private ComboModel<String> volUnitsComboModel = new ComboModel<String>();
	final private JRadioButton tempCrb = new JRadioButton();
	final private JRadioButton tempFrb = new JRadioButton();
	final private ButtonGroup tempBg = new ButtonGroup();
	final private JPanel settingsPanel = new JPanel();
	final private JPanel tablePanel = new JPanel();
	final private JButton saveButton = new JButton();
	final private JButton sendButton = new JButton();
	final private JComboBox typesComboBox = new JComboBox();
	final private JComboBox methodComboBox = new JComboBox();

	final private CellEditor sTempEditor = new CellEditor(new JTextField());
	final private CellEditor eTempEditor = new CellEditor(new JTextField());
	final private CellEditor rampEditor = new CellEditor(new JTextField());
	final private CellEditor stepEditor = new CellEditor(new JTextField());
	final private CellEditor weightEditor = new CellEditor(new JTextField());

	final private MashDefaults md = new MashDefaults();

	public MashPanel(Recipe r) {
		super();
		myRecipe = r;
		mashModel = new MashTableModel();

		if (myRecipe != null) {
			mashModel.setData(myRecipe.getMash());

		}
		initGUI();
		displayMash();
		tblMash.updateUI();

		String[] names = md.getNames();
		for (int i = 0; i < names.length; i++) {
			JMenuItem m = new JMenuItem(names[i]);
			m.addActionListener(this);
			mashMenu.add(m);
		}
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
			thisLayout.rowWeights = new double[]{0.1, 1.0};
			thisLayout.rowHeights = new int[]{7, 7};
			this.setLayout(thisLayout);

			{
				BorderLayout pnlTableLayout = new BorderLayout();
				tablePanel.setLayout(pnlTableLayout);
				this.add(tablePanel, new GridBagConstraints(0, 1, 2, 2, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0),
						0, 0));
				tablePanel.setName("");
				tablePanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

				{
					tablePanel.add(jScrollPane1, BorderLayout.CENTER);
					// jScrollPane1.setPreferredSize(new java.awt.Dimension(344, 145));

					{
						tblMash = new JTable() {
							public String getToolTipText(MouseEvent e) {
								java.awt.Point p = e.getPoint();
								int rowIndex = rowAtPoint(p);
								return StringUtils.multiLineToolTip(40, mashModel
										.getDirectionsAt(rowIndex));

							}
						};
						jScrollPane1.setViewportView(tblMash);
						tblMash.setModel(mashModel);
						tblMash.setAutoCreateColumnsFromModel(false);
						tblMash.getTableHeader().setReorderingAllowed(false);
						tblMash.setName("MashTable");

						// set up type combo
						for (String s : Mash.types) {
							typesComboBox.addItem(s);
						}
						SmartComboBox.enable(typesComboBox);
						TableColumn mashColumn = tblMash.getColumnModel().getColumn(0);
						mashColumn.setCellEditor(new ComboBoxCellEditor(typesComboBox));
						typesComboBox.addActionListener(this);

						// set up method combo;
						for (String s : Mash.methods) {
							methodComboBox.addItem(s);
						}

						SmartComboBox.enable(methodComboBox);
						mashColumn = tblMash.getColumnModel().getColumn(1);
						mashColumn.setCellEditor(new ComboBoxCellEditor(methodComboBox));


						mashColumn = tblMash.getColumnModel().getColumn(2);
						mashColumn.setCellEditor(sTempEditor);

						mashColumn = tblMash.getColumnModel().getColumn(3);
						mashColumn.setCellEditor(eTempEditor);

						mashColumn = tblMash.getColumnModel().getColumn(4);
						mashColumn.setCellEditor(rampEditor);

						mashColumn = tblMash.getColumnModel().getColumn(5);
						mashColumn.setCellEditor(stepEditor);

						mashColumn = tblMash.getColumnModel().getColumn(6);
						mashColumn.setCellEditor(weightEditor);
					}
				}
			}

			this.add(jTabbedPane1, new GridBagConstraints(2, 0, 1, 2, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0,
					0));

			FlowLayout jPanel1Layout = new FlowLayout();
			jPanel1.setLayout(jPanel1Layout);
			this.add(jPanel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0,
					0));
			{

				// this.setPreferredSize(new java.awt.Dimension(502, 276));
				this.addFocusListener(new FocusAdapter() {
					public void focusGained(FocusEvent evt) {
						displayMash();
					}
				});

			}

			jPanel1.add(nameTxt);
			nameTxt.setText("Name");
			nameTxt.setPreferredSize(new java.awt.Dimension(211, nameTxt.getFont().getSize()*2));
			nameTxt.addActionListener(this);

			jPanel1.add(saveButton);
            String gif = Product.getAppPath(Product.Path.IMAGES) + System.getProperty("file.separator") + "save.gif";
			saveButton.setIcon(new ImageIcon(gif));
			saveButton.addActionListener(this);

			jPanel1.add(defaultsButton);
			defaultsButton.setText("Defaults >");
			defaultsButton.addActionListener(this);

			// If the user has set up Elsinore in the properties, show the send button
			String elsinoreServer = Options.getInstance().getProperty("elsinoreServer");
			if (elsinoreServer != null && !elsinoreServer.equals("")) {
    			jPanel1.add(sendButton);
                sendButton.setText("Send >");
                sendButton.addActionListener(this);
    		}

			{
				jTabbedPane1.addTab("Settings", null, settingsPanel, null);
				GridBagLayout settingsPanelLayout = new GridBagLayout();
				settingsPanelLayout.rowWeights = new double[]{0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1,
						0.1};
				settingsPanelLayout.rowHeights = new int[]{7, 7, 7, 7, 7, 7, 7, 7};
				settingsPanelLayout.columnWeights = new double[]{0.3};
				settingsPanelLayout.columnWidths = new int[]{7, 7, 7};
				settingsPanel.setLayout(settingsPanelLayout);
				settingsPanel.setPreferredSize(new java.awt.Dimension(190, 208));

				settingsPanel.add(jLabel14, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
						GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0),
						0, 0));
				jLabel14.setText("Vol Units:");

				settingsPanel.add(jLabel15, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
						GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0),
						0, 0));
				jLabel15.setText("Ratio: 1:");
				{
					settingsPanel.add(ratioText, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
							GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0,
									0, 0, 0), 0, 0));
					ratioText.setText("1.25");
					ratioText.setPreferredSize(new java.awt.Dimension(51, ratioText.getFont().getSize()*2));
					ratioText.addFocusListener(this);
					ratioText.addActionListener(this);
				}
				{
					settingsPanel.add(grainTempText, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
							GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0,
									0, 0, 0), 0, 0));
					grainTempText.setText("10");
					grainTempText.setPreferredSize(new java.awt.Dimension(50, grainTempText.getFont().getSize()*2));

					grainTempText.addFocusListener(this);
					grainTempText.addActionListener(this);
				}
				{
					settingsPanel.add(grainTempULabel, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0,
							GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0,
									0), 0, 0));
					grainTempULabel.setText("F");

				}

				settingsPanel.add(jLabel19, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
						GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0),
						0, 0));
				jLabel19.setText("Grain Tmp:");

				settingsPanel.add(boilTempTxt, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0,
								0, 0), 0, 0));
				boilTempTxt.setText("212");
				boilTempTxt.setPreferredSize(new java.awt.Dimension(52, boilTempTxt.getFont().getSize()*2));

				settingsPanel.add(jLabel20, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
						GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0),
						0, 0));
				jLabel20.setText("Boil Temp:");

				settingsPanel.add(boilTempULbl, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0),
						0, 0));
				boilTempULbl.setText("F");

				{
					settingsPanel.add(tunLossTxt, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0,
							GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0,
									0, 0, 0), 0, 0));
					tunLossTxt.setPreferredSize(new java.awt.Dimension(52, tunLossTxt.getFont().getSize()*2));

					tunLossTxt.addFocusListener(this);
					tunLossTxt.addActionListener(this);
				}
				{
					settingsPanel.add(tempLostULabel, new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0,
							GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0,
									0), 0, 0));
					tempLostULabel.setText("F");
				}

				settingsPanel.add(jLabel1, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
						GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0),
						0, 0));
				jLabel1.setText("Lost in Tun:");
				{

					volUnitsComboModel.setList(Quantity.getListofUnits("vol", true));
					SmartComboBox.enable(volUnitsCombo);
					settingsPanel.add(volUnitsCombo, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
							GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0,
									0, 0, 0), 0, 0));
					volUnitsCombo.setModel(volUnitsComboModel);
					volUnitsCombo.setPreferredSize(new java.awt.Dimension(57, volUnitsCombo.getFont().getSize()*2));
					volUnitsCombo.addActionListener(this);
				}

				// Volume lost in Tun
				settingsPanel.add(deadSpaceLabel, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0,
						GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0),
						0, 0));
				deadSpaceLabel.setText("Dead Space:");
				/*{
					volUnitsComboModel.setList(Quantity.getListofUnits("vol", true));
					SmartComboBox.enable(volUnitsCombo);
					settingsPanel.add(volUnitsCombo, new GridBagConstraints(2, 5, 1, 1, 0.0, 0.0,
							GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0,
									0, 0, 0), 0, 0));
					volUnitsCombo.setModel(volUnitsComboModel);
					volUnitsCombo.setPreferredSize(new java.awt.Dimension(57, volUnitsCombo.getFont().getSize()*2));
					volUnitsCombo.addActionListener(this);
				}*/

				settingsPanel.add(deadSpaceText, new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0),
						0, 0));
				//deadSpaceText.setText(opts.getProperty("optDeadSpace"));
				deadSpaceText.setPreferredSize(new java.awt.Dimension(50, deadSpaceText.getFont().getSize()*2));

				deadSpaceText.addFocusListener(this);
				deadSpaceText.addActionListener(this);


				settingsPanel.add(deadSpaceUnit, new GridBagConstraints(2, 5, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0),
						0, 0));
				deadSpaceUnit.setText("x");

				// Cereal Mash

				settingsPanel.add(jLabel2, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0),
						0, 0));
				jLabel2.setText("Cereal Mash:");

				boilTempTxt.addFocusListener(this);
				boilTempTxt.addActionListener(this);

				SmartComboBox.enable(ratioUnitsCombo);
				settingsPanel.add(ratioUnitsCombo, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0),
						0, 0));
				ratioUnitsCombo.setModel(ratioUnitsComboModel);
				ratioUnitsCombo.addActionListener(this);

			}

			jTabbedPane1.addTab("Totals", null, totalsPanel, null);
			GridBagLayout totalsPanelLayout = new GridBagLayout();
			totalsPanelLayout.rowWeights = new double[]{0.1, 0.1, 0.1, 0.1, 0.1, 0.1};
			totalsPanelLayout.rowHeights = new int[]{7, 7, 7, 7, 7, 7};
			totalsPanelLayout.columnWeights = new double[]{0.1, 0.1};
			totalsPanelLayout.columnWidths = new int[]{7, 7};
			totalsPanel.setLayout(totalsPanelLayout);
			{
				totalsPanel.add(totalTimeLabel, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
						GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0),
						0, 0));
				totalTimeLabel.setText("Time");

			}
			{
				tablePanel.add(buttonsPanel, BorderLayout.SOUTH);
				GridBagLayout buttonsPanelLayout = new GridBagLayout();
				buttonsPanelLayout.rowWeights = new double[]{0.1};
				buttonsPanelLayout.rowHeights = new int[]{7};
				buttonsPanelLayout.columnWeights = new double[]{0.1, 0.1, 0.1, 0.1};
				buttonsPanelLayout.columnWidths = new int[]{7, 7, 7, 7};
				buttonsPanel.setLayout(buttonsPanelLayout);
				buttonsPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

				buttonsPanel.add(jToolBar1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0,
								0, 0), 0, 0));
				jToolBar1.setFloatable(false);
				jToolBar1.setPreferredSize(new java.awt.Dimension(76, jToolBar1.getFont().getSize()*2));
				{
					buttonsPanel.add(tempFrb, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
							GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0,
									0), 0, 0));
					tempFrb.setText("F");
					tempFrb.addActionListener(this);
				}

				tempBg.add(tempFrb);
				{
					buttonsPanel.add(tempCrb, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
							GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0,
									0), 0, 0));
					tempCrb.setText("C");
					tempCrb.addActionListener(this);

					buttonsPanel.add(jLabel13, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
							GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0,
									0), 0, 0));
					jLabel13.setText("Temp Units:");

				}
				tempBg.add(tempCrb);

				totalsPanel.add(jLabel18, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
						GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0),
						0, 0));
				jLabel18.setText("Total Time:");

				totalsPanel.add(jLabel16, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
						GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0),
						0, 0));
				jLabel16.setText("Total Weight:");

				totalsPanel.add(totalMashLabel, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
						GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0),
						0, 0));

				totalsPanel.add(jLabel17, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
						GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0),
						0, 0));
				jLabel17.setText("Total Vol:");

				{
					jToolBar1.add(addStepButton);
					addStepButton.setText("+");

					addStepButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							addStepButtonActionPerformed(evt);
						}
					});
				}
				{
					jToolBar1.add(delStepButton);
					delStepButton.setText("-");

					delStepButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							delStepButtonActionPerformed(evt);
						}
					});
				}
			}
			{
				totalsPanel.add(volLabel, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
						GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0),
						0, 0));
				volLabel.setText("10");
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
			ratioText.setText(new Double(myRecipe.mash.getMashRatio()).toString());
			ratioUnitsComboModel.setSelectedItem(myRecipe.mash.getMashRatioU());


			// set totals:
			String mashWeightTotal = StringUtils.format(myRecipe.getTotalMash(), 1) + " "
					+ myRecipe.getMaltUnits();
			totalMashLabel.setText(mashWeightTotal);
			totalTimeLabel.setText(new Integer(myRecipe.mash.getMashTotalTime()).toString());
			volLabel.setText(myRecipe.mash.getMashTotalVol());
			grainTempText.setText(new Double(myRecipe.mash.getGrainTemp()).toString());
			boilTempTxt.setText(new Double(myRecipe.mash.getBoilTemp()).toString());
			tunLossTxt.setText(StringUtils.format(myRecipe.mash.getTunLoss(), 1));
			Double d = myRecipe.mash.getDeadSpace();
			deadSpaceText.setText(StringUtils.format(d, 1));
			tempFrb.setSelected(myRecipe.mash.getMashTempUnits().equalsIgnoreCase("F"));
			nameTxt.setText(myRecipe.mash.getName());
			deadSpaceUnit.setText(myRecipe.mash.getMashVolUnits());

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
			try {
				NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
				Number number = format.parse(ratioText.getText().trim());

				myRecipe.mash.setMashRatio(number.doubleValue());
			} catch (ParseException m) {
				Debug.print("Could not parse "+ ratioText.getText() + " as a double");
				ratioText.setText(Double.toString(myRecipe.mash.getMashRatio()));
			}
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
			try {
				NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
				Number number = format.parse(s.toString().trim());
				myRecipe.mash.setGrainTemp(number.doubleValue());
			} catch (ParseException m) {
				Debug.print("Could not parse "+ s + " as a double");
				grainTempText.setText(Double.toString(myRecipe.mash.getGrainTemp()));
			}
		} else if (o == boilTempTxt) {
			String s = boilTempTxt.getText();
			try {
				NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
				Number number = format.parse(s.toString().trim());
				myRecipe.mash.setBoilTemp(number.doubleValue());
			} catch (ParseException m) {
				Debug.print("Could not parse "+ s + " as a double");
				boilTempTxt.setText(Double.toString(myRecipe.mash.getBoilTemp()));
			}
		} else if (o == tunLossTxt) {
			String s = tunLossTxt.getText();
			try {
				NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
				Number number = format.parse(s.toString().trim());
				myRecipe.mash.setTunLoss(number.doubleValue());
			} catch (ParseException m) {
				Debug.print("Could not parse "+ s + " as a double");
				tunLossTxt.setText(Double.toString(myRecipe.mash.getTunLoss()));
			}
		} else if (o == deadSpaceText) {
			String s = deadSpaceText.getText();
			try {
				NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
				Number number = format.parse(s.toString().trim());
				myRecipe.mash.setDeadSpace(number.doubleValue());
			} catch (ParseException m) {
				Debug.print("Could not parse "+ s + " as a double");
				deadSpaceText.setText(Double.toString(myRecipe.mash.getDeadSpace()));
			}
		} else if (o == nameTxt) {
			myRecipe.mash.setName(nameTxt.getText());
		} else if (o == defaultsButton){
			mashMenu.show(defaultsButton, 10, 10);
		} else if (o == sendButton) {
		    sendMashToElsinore();
		} else if (o == saveButton) {
			myRecipe.mash.setName(nameTxt.getText());
			md.add(myRecipe.mash, nameTxt.getText());
			JMenuItem m = new JMenuItem(nameTxt.getText());
			m.addActionListener(this);
			mashMenu.add(m);
		} else if (o instanceof JMenuItem) {
			String name = ((JMenuItem)o).getText();
			md.set(name, myRecipe);
			mashModel.setData(myRecipe.getMash());
		} else if (o == typesComboBox && typesComboBox.isValid()) {

		}

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				tblMash.updateUI();
			  	displayMash();
			}
		});

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

	/**
	 * Send the mash profile to elsinore.
	 */
	public void sendMashToElsinore() {
	    String elsinoreLoc = Options.getInstance().getProperty("elsinoreServer");

	    if (elsinoreLoc == null || elsinoreLoc.length() == 0) {
	        JOptionPane.showMessageDialog(null, "Elsinore URL is not set");
	        return;
	    }

	    if (!elsinoreLoc.startsWith("http://")) {
	        elsinoreLoc = "http://" + elsinoreLoc;
	    }

	    String statusLoc = elsinoreLoc;
	    if (!elsinoreLoc.endsWith("/getstatus")) {
	        statusLoc += "/getstatus";
	    } else {
	        elsinoreLoc = elsinoreLoc.replace("/getstatus", "");
	    }

	    URL url;
        try {
            url = new URL(statusLoc);

            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setDoOutput(true);
            urlConn.setDoInput(true);


            urlConn.connect();
            // Read back the response from the stream
            InputStream inputStream;
            int responseCode = urlConn.getResponseCode();

            if ((responseCode >= 200) && (responseCode<=202)) {
                inputStream = urlConn.getInputStream();
                int j;
                Scanner s = new Scanner(inputStream);
                s.useDelimiter("\\A");
                String jsonStatus = s.next();
                s.close();
                JSONObject statusObject = (JSONObject) JSONValue.parse(jsonStatus);
                String selectedDevice = getMashDevice(statusObject);
                if (selectedDevice == null || selectedDevice.length() == 0) {
                    return;
                }

                if (!statusObject.get("mash").equals("Unset")) {
                    JSONObject mashProfiles = (JSONObject) statusObject.get("mash");
                    if (mashProfiles.get(selectedDevice) != null) {
                        int overwrite = JOptionPane.showConfirmDialog(this, "Do you want to overwrite the mash profile?");
                        if (overwrite == JOptionPane.NO_OPTION) {
                            return;
                        }
                    }
                }
                // We got here, lets upload the Mash Profile.
                URL uploadURL = new URL(elsinoreLoc + "/mashprofile");
                HttpURLConnection mashConn = (HttpURLConnection) uploadURL.openConnection();
                mashConn.setDoOutput(true);
                mashConn.setDoInput(true);
                mashConn.connect();

                OutputStreamWriter writer = new OutputStreamWriter(mashConn.getOutputStream());
                String upload = myRecipe.mash.toJSONObject(selectedDevice).toString();
                writer.write(upload);
                writer.flush();
                String response = mashConn.getResponseMessage();
                JOptionPane.showMessageDialog(null, "Uploaded Mash profile for " + selectedDevice);

            } else {
                JOptionPane.showMessageDialog(null, "Error contacting the server: " + urlConn.getResponseMessage());
                return;
            }
        } catch (MalformedURLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (ProtocolException e) {
            String err = "Couldn't open connection: " + e.getMessage();
            return;
        } catch (IOException e) {
            String err = "IOException on connection: " + e.getMessage();
            return;
        }
	}

	public String getMashDevice(JSONObject statusObject) {
	    JSONArray devices = (JSONArray) statusObject.get("vessels");
	    // Throw an error if there's no devices
	    if (devices == null) {
	        JOptionPane.showMessageDialog(null, "No devices found on the server");
            return null;
	    }

	    // Loop round to get the valid devices
	    ArrayList<String> devArray = new ArrayList<String>();
	    for (int i = 0; i < devices.size(); i++) {
	        JSONObject dev = (JSONObject) devices.get(i);
	        if (dev.get("pidstatus") != null) {
	            devArray.add(dev.get("name").toString());
	        }
	    }

	    // Throw an error if there's no PIDs
	    if (devArray.size() == 0) {
	        JOptionPane.showMessageDialog(null, "No PIDs found on the server");
            return null;
	    }

	    devArray.add("");

	    // Prompt the user to select the PID.
	    String s = (String)JOptionPane.showInputDialog(
	                        null,
	                        "Which PID would you like to use?",
	                        "PID Selection",
	                        JOptionPane.PLAIN_MESSAGE,
	                        null,
	                        devArray.toArray(),
	                        "");
	    return s;
	}
}

