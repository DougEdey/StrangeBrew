/*
 * $Id: StrangeSwing.java,v 1.30 2005/06/15 16:47:06 andrew_avis Exp $ 
 * Created on June 15, 2005 @author aavis main recipe window class
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
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import ca.strangebrew.Debug;
import ca.strangebrew.SBStringUtils;

import strangebrew.Database;
import strangebrew.Fermentable;
import strangebrew.Hop;
import strangebrew.ImportXml;
import strangebrew.Options;
import strangebrew.Quantity;
import strangebrew.Recipe;
import strangebrew.Style;
import strangebrew.XmlTransformer;
import strangebrew.Yeast;
import strangebrew.ui.preferences.PreferencesDialog;

/**
 * This code was generated using CloudGarden's Jigloo
 * SWT/Swing GUI Builder, which is free for non-commercial
 * use. If Jigloo is being used commercially (ie, by a corporation,
 * company or business for any purpose whatever) then you
 * should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details.
 * Use of Jigloo implies acceptance of these licensing terms.
 * *************************************
 * A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED
 * for this machine, so Jigloo or this code cannot be used legally
 * for any corporate or commercial purpose.
 * *************************************
 */
public class StrangeSwing extends javax.swing.JFrame implements ActionListener, FocusListener {

	/*	{
	 //Set Look & Feel
	 try {
	 javax.swing.UIManager.setLookAndFeel("com.birosoft.liquid.LiquidLookAndFeel");
	 } catch (Exception e) {
	 e.printStackTrace();
	 }
	 }
	 */
	{
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	private JMenuItem helpMenuItem;
	private JMenu jMenu5;
	private JMenuItem deleteMenuItem;
	private JSeparator jSeparator1;
	private JLabel lblDate;
	private JTextField brewerNameText;
	private JLabel lblBrewer;
	private JTextField txtName;
	private JLabel lblName;
	private JPanel pnlDetails;
	private JTabbedPane jTabbedPane1;
	private JPanel jPanel1;
	private JMenu jMenu4;
	private JMenuItem exitMenuItem;
	private JSeparator jSeparator2;
	private JLabel lblAlc;
	private JButton btnDelHop;
	private JButton btnAddHop;
	private JToolBar tlbHops;
	private JPanel pnlHopsButtons;
	private JButton btnDelMalt;
	private JButton btnAddMalt;
	private JComboBox colourMethodCombo;
	private JComboBox ibuMethodCombo;
	private JComboBox alcMethodCombo;
	private JLabel alcMethodLabel;
	private JPanel alcMethodPanel;
	private JLabel ibuMethodLabel;
	private JPanel ibuMethodPanel;
	private JPanel fileNamePanel;
	private JLabel fileNameLabel;
	private MiscPanel miscPanel;
	private NotesPanel notesPanel;
	private JMenuItem aboutMenuItem;
	private JTextField evapText;
	private JTextField boilMinText;
	private JLabel evapLabel;
	private JLabel boilTimeLable;
	private JPanel statusPanel;
	private JMenuItem mashManagerMenuItem;
	private JMenu mnuView;
	private JToolBar tlbMalt;
	private JPanel pnlMaltButtons;
	private StylePanel stylePanel;
	private JMenuItem exportHTMLmenu;
	private JMenu exportMenu;
	private JScrollPane jScrollPane2;
	private JScrollPane jScrollPane1;
	private JTable tblHopsTotals;
	private JTable tblMaltTotals;
	private JPanel pnlHops;
	private JPanel pnlMalt;
	private JLabel lblSizeUnits;
	private JComboBox cmbSizeUnits;
	private JScrollPane scrMalts;
	private JPanel pnlMain;
	private JTable hopsTable;
	private JScrollPane scpComments;
	private JTable maltTable;
	private JLabel lblAlcValue;
	private JLabel lblColourValue;
	private JLabel lblIBUvalue;
	private JSpinner spnFG;
	private JSpinner spnOG;
	private JSpinner spnAtten;
	private JSpinner spnEffic;
	private JTextArea txtComments;
	private JLabel lblComments;
	private JFormattedTextField postBoilText;
	private JFormattedTextField txtPreBoil;
	private JComboBox cmbYeast;
	private JComboBox cmbStyle;
	private JFormattedTextField txtDate;
	private JLabel lblColour;
	private JLabel lblIBU;
	private JPanel pnlTables;
	private JLabel lblFG;
	private JLabel lblOG;
	private JLabel lblAtten;
	private JLabel lblEffic;
	private JLabel lblPostBoil;
	private JLabel lblPreBoil;
	private JLabel lblYeast;
	private JLabel lblStyle;
	private JMenuItem saveAsMenuItem;
	private JMenuItem saveMenuItem;
	private JMenuItem openFileMenuItem;
	private JMenuItem newFileMenuItem;
	private JMenuItem findFileMenuItem;
	private JMenu fileMenu;
	private JMenuBar jMenuBar1;
	private JMenuItem exportTextMenuItem;
	private JMenuItem editPrefsMenuItem;

	private MaltTableModel maltTableModel;
	private DefaultTableModel tblMaltTotalsModel;
	private HopsTableModel hopsTableModel;
	private DefaultTableModel tblHopsTotalsModel;
	private ComboModel cmbYeastModel;
	private ComboModel cmbStyleModel;
	private ComboModel cmbMaltModel;
	private ComboModel cmbHopsModel;
	private ComboModel cmbSizeUnitsModel;
	private ComboModel cmbMaltUnitsModel;
	private ComboModel cmbHopsUnitsModel;
	private ArrayList weightList;
	private ArrayList volList;
	private JFileChooser fileChooser;
	private MashManager mashMgr;
	private AboutDialog aboutDlg;

	private DateFormat dateFormat1 = DateFormat.getDateInstance(DateFormat.SHORT);

	private Options preferences = new Options();
	public Recipe myRecipe;

	private File currentFile;

	private DefaultComboBoxModel alcMethodComboModel;
	private DefaultComboBoxModel ibuMethodComboModel;
	private DefaultComboBoxModel colourMethodComboModel;

	/**
	 * Auto-generated main method to display this JFrame
	 */
	public static void main(String[] args) {
		StrangeSwing inst = new StrangeSwing();
		inst.setVisible(true);
	}

	public StrangeSwing() {
		super();
		initGUI();
		// There has *got* to be a better way to do this:
		Database db = new Database();
		String path = "";
		String slash = System.getProperty("file.separator");
		try {
			path = new File(".").getCanonicalPath() + slash + "src" + slash + "strangebrew" + slash
					+ "data";
		} catch (Exception e) {
			e.printStackTrace();
		}
		db.readDB(path);

		cmbStyleModel.setList(db.styleDB);
		cmbYeastModel.setList(db.yeastDB);
		cmbMaltModel.setList(db.fermDB);
		cmbHopsModel.setList(db.hopsDB);

		cmbSizeUnitsModel.setList(new Quantity().getListofUnits("vol"));
		cmbMaltUnitsModel.setList(new Quantity().getListofUnits("weight"));
		cmbHopsUnitsModel.setList(new Quantity().getListofUnits("weight"));

		fileChooser = new JFileChooser();
		String fcpath = getClass().getProtectionDomain().getCodeSource().getLocation().toString()
				.substring(6)
				+ slash;
		fileChooser.setCurrentDirectory(new File(path));

		// link malt table and totals:
		addColumnWidthListeners();

		// set up tabs:
		miscPanel.setList(db.miscDB);
		stylePanel.setList(db.styleDB);

		myRecipe = new Recipe();
		currentFile = null;
		attachRecipeData();
		displayRecipe();

	}

	public void attachRecipeData() {
		// this method attaches data from the recipe to the tables 
		// and comboboxes
		// use whenever the Recipe changes
		cmbStyleModel.addOrInsert(myRecipe.getStyleObj());
		// cmbStyle2Model.addOrInsert(myRecipe.getStyleObj());
		cmbYeastModel.addOrInsert(myRecipe.getYeastObj());
		cmbSizeUnitsModel.addOrInsert(myRecipe.getVolUnits());
		maltTableModel.setData(myRecipe);
		hopsTableModel.setData(myRecipe);
		miscPanel.setData(myRecipe);
		notesPanel.setData(myRecipe);
		stylePanel.setData(myRecipe);
		maltTable.updateUI();
		hopsTable.updateUI();
		
		alcMethodComboModel.setSelectedItem(myRecipe.getAlcMethod());
		ibuMethodComboModel.setSelectedItem(myRecipe.getIBUMethod());
		colourMethodCombo.setSelectedItem(myRecipe.getColourMethod());
		

	}

	public void displayRecipe() {
		if (myRecipe == null)
			return;
		txtName.setText(myRecipe.getName());
		brewerNameText.setText(myRecipe.getBrewer());
		txtPreBoil.setValue(new Double(myRecipe.getPreBoilVol(myRecipe.getVolUnits())));
		lblSizeUnits.setText(myRecipe.getVolUnits());
		postBoilText.setValue(new Double(myRecipe.getPostBoilVol(myRecipe.getVolUnits())));
		boilMinText.setText(myRecipe.df1.format(myRecipe.getBoilMinutes()));
		evapText.setText(myRecipe.df1.format(myRecipe.getEvap()));
		spnEffic.setValue(new Double(myRecipe.getEfficiency()));
		spnAtten.setValue(new Double(myRecipe.getAttenuation()));
		spnOG.setValue(new Double(myRecipe.getEstOg()));
		spnFG.setValue(new Double(myRecipe.getEstFg()));
		txtComments.setText(myRecipe.getComments());
		lblIBUvalue.setText(myRecipe.df1.format(myRecipe.getIbu()));
		lblColourValue.setText(myRecipe.df1.format(myRecipe.getSrm()));
		lblAlcValue.setText(myRecipe.df1.format(myRecipe.getAlcohol()));
		txtDate.setText(dateFormat1.format(myRecipe.getCreated().getTime()));
		tblMaltTotalsModel.setDataVector(new String[][]{{"Totals:",
				"" + myRecipe.df1.format(myRecipe.getTotalMaltLbs()), myRecipe.getMaltUnits(),
				"" + myRecipe.df3.format(myRecipe.getEstOg()),
				"" + myRecipe.df1.format(myRecipe.getSrm()),
				"$" + myRecipe.df2.format(myRecipe.getTotalMaltCost()), "100"}}, new String[]{"",
				"", "", "", "", "", ""});

		tblHopsTotalsModel.setDataVector(new String[][]{{"Totals:", "", "",
				"" + myRecipe.df1.format(myRecipe.getTotalHopsOz()), myRecipe.getHopUnits(), "",
				"", "" + myRecipe.df1.format(myRecipe.getIbu()),
				"$" + myRecipe.df2.format(myRecipe.getTotalHopsCost())}}, new String[]{"", "", "",
				"", "", "", "", "", ""});

		String fileName = "not saved";
		if (currentFile != null) {
			fileName = currentFile.getName();
		}
		fileNameLabel.setText("File: " + fileName);
		ibuMethodLabel.setText("IBU method: " + myRecipe.getIBUMethod());
		alcMethodLabel.setText("Alc method: " + myRecipe.getAlcMethod());

	}

	private void initGUI() {
		try {

			this.setSize(520, 532);
			this.addWindowListener(new WindowAdapter() {
				public void windowClosed(WindowEvent evt) {
					System.exit(1);
				}
			});

			{
				pnlMain = new JPanel();
				GridBagLayout jPanel2Layout = new GridBagLayout();
				jPanel2Layout.columnWeights = new double[]{0.1};
				jPanel2Layout.columnWidths = new int[]{7};
				jPanel2Layout.rowWeights = new double[]{0.1, 0.1, 0.9, 0.1};
				jPanel2Layout.rowHeights = new int[]{7, 7, 7, 7};
				pnlMain.setLayout(jPanel2Layout);
				this.getContentPane().add(pnlMain, BorderLayout.CENTER);
				{
					jPanel1 = new JPanel();
					pnlMain.add(jPanel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
							GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0,
									0, 0, 0), 0, 0));
					FlowLayout jPanel1Layout = new FlowLayout();
					jPanel1Layout.setAlignment(FlowLayout.LEFT);
					jPanel1.setLayout(jPanel1Layout);
					{
						lblName = new JLabel();
						jPanel1.add(lblName);
						lblName.setText("Name:");
					}
					{
						txtName = new JTextField();
						jPanel1.add(txtName);
						txtName.setText("Name");
						txtName.setPreferredSize(new java.awt.Dimension(179, 20));
						txtName.addActionListener(this);
						txtName.addFocusListener(this);

					}
				}
				{
					jTabbedPane1 = new JTabbedPane();
					pnlMain.add(jTabbedPane1, new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
							GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0,
									0), 0, 0));
					{
						pnlDetails = new JPanel();
						GridBagLayout pnlDetailsLayout = new GridBagLayout();
						pnlDetailsLayout.columnWeights = new double[]{0.1, 0.1, 0.1, 0.1, 0.1, 0.1,
								0.1, 0.1, 0.1, 0.1};
						pnlDetailsLayout.columnWidths = new int[]{7, 7, 7, 7, 7, 7, 7, 7, 7, 7};
						pnlDetailsLayout.rowWeights = new double[]{0.1, 0.1, 0.1, 0.1, 0.1, 0.1,
								0.1};
						pnlDetailsLayout.rowHeights = new int[]{7, 7, 7, 7, 7, 7, 7};
						pnlDetails.setLayout(pnlDetailsLayout);
						jTabbedPane1.addTab("Details", null, pnlDetails, null);
						pnlDetails.setPreferredSize(new java.awt.Dimension(20, 16));
						{
							lblBrewer = new JLabel();
							pnlDetails.add(lblBrewer, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
									GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0,
											0, 0, 0), 0, 0));
							lblBrewer.setText("Brewer:");
						}
						{
							brewerNameText = new JTextField();
							pnlDetails.add(brewerNameText, new GridBagConstraints(1, 0, 2, 1, 0.0,
									0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
									new Insets(0, 0, 0, 0), 0, 0));
							brewerNameText.addFocusListener(this);
							brewerNameText.addActionListener(this);
							brewerNameText.setText("Brewer");

						}
						{
							lblDate = new JLabel();
							pnlDetails.add(lblDate, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
									GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0,
											0, 0, 0), 0, 0));
							lblDate.setText("Date:");
						}
						{
							lblStyle = new JLabel();
							pnlDetails.add(lblStyle, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
									GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0,
											0, 0, 0), 0, 0));
							lblStyle.setText("Style:");
						}
						{
							lblYeast = new JLabel();
							pnlDetails.add(lblYeast, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
									GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0,
											0, 0, 0), 0, 0));
							lblYeast.setText("Yeast:");
						}
						{
							lblPreBoil = new JLabel();
							pnlDetails.add(lblPreBoil, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
									GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0,
											0, 0, 0), 0, 0));
							lblPreBoil.setText("Pre boil:");
						}
						{
							lblPostBoil = new JLabel();
							pnlDetails.add(lblPostBoil, new GridBagConstraints(0, 5, 1, 1, 0.0,
									0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
									new Insets(0, 0, 0, 0), 0, 0));
							lblPostBoil.setText("Post boil:");
						}
						{
							lblEffic = new JLabel();
							pnlDetails.add(lblEffic, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
									GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0,
											0, 0, 0), 0, 0));
							lblEffic.setText("Effic:");
						}
						{
							lblAtten = new JLabel();
							pnlDetails.add(lblAtten, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0,
									GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0,
											0, 0, 0), 0, 0));
							lblAtten.setText("Atten:");
						}
						{
							lblOG = new JLabel();
							pnlDetails.add(lblOG, new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0,
									GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0,
											0, 0, 0), 0, 0));
							lblOG.setText("OG:");
						}
						{
							lblFG = new JLabel();
							pnlDetails.add(lblFG, new GridBagConstraints(5, 1, 1, 1, 0.0, 0.0,
									GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0,
											0, 0, 0), 0, 0));
							lblFG.setText("FG:");
						}
						{
							lblIBU = new JLabel();
							pnlDetails.add(lblIBU, new GridBagConstraints(7, 1, 1, 1, 0.0, 0.0,
									GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0,
											0, 0, 0), 0, 0));
							lblIBU.setText("IBU:");
						}
						{
							lblAlc = new JLabel();
							pnlDetails.add(lblAlc, new GridBagConstraints(7, 0, 1, 1, 0.0, 0.0,
									GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0,
											0, 0, 0), 0, 0));
							lblAlc.setText("%Alc:");
						}
						{
							lblColour = new JLabel();
							pnlDetails.add(lblColour, new GridBagConstraints(7, 2, 1, 1, 0.0, 0.0,
									GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0,
											0, 0, 0), 0, 0));
							lblColour.setText("Colour:");
						}
						{
							txtDate = new JFormattedTextField();
							pnlDetails.add(txtDate, new GridBagConstraints(1, 1, 2, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
									new Insets(0, 0, 0, 0), 0, 0));
							txtDate.setText("Date");
							txtDate.addFocusListener(this);
							txtDate.addActionListener(this);
						}
						{
							cmbStyleModel = new ComboModel();
							cmbStyle = new JComboBox();
							pnlDetails.add(cmbStyle, new GridBagConstraints(1, 2, 5, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
									new Insets(0, 0, 0, 0), 0, 0));
							cmbStyle.setModel(cmbStyleModel);
							cmbStyle.setMaximumSize(new java.awt.Dimension(100, 32767));

							cmbStyle.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									Style s = (Style) cmbStyleModel.getSelectedItem();
									// cmbStyle2Model.setSelectedItem(s);
									if (myRecipe != null && s != myRecipe.getStyleObj()) {
										myRecipe.setStyle(s);
									}

									cmbStyle.setToolTipText(SBStringUtils.multiLineToolTip(50, s
											.getDescription()));

								}
							});
						}
						{
							txtPreBoil = new JFormattedTextField();
							pnlDetails.add(txtPreBoil, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0,
									GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
									new Insets(0, 0, 0, 0), 0, 0));
							txtPreBoil.setText("Pre Boil");
							txtPreBoil.addFocusListener(this);
							txtPreBoil.addActionListener(this);
						}
						{
							postBoilText = new JFormattedTextField();
							pnlDetails.add(postBoilText, new GridBagConstraints(1, 5, 1, 1, 0.0,
									0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
									new Insets(0, 0, 0, 0), 0, 0));
							postBoilText.setText("Post Boil");
							postBoilText.addFocusListener(new FocusAdapter() {
								public void focusLost(FocusEvent evt) {
									myRecipe.setPostBoil(Double.parseDouble(postBoilText.getText()
											.toString()));
									displayRecipe();
								}
							});
							postBoilText.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									myRecipe.setPostBoil(Double.parseDouble(postBoilText.getText()
											.toString()));
									displayRecipe();
								}
							});
						}
						{
							lblComments = new JLabel();
							pnlDetails.add(lblComments, new GridBagConstraints(6, 3, 1, 1, 0.0,
									0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
									new Insets(0, 0, 0, 0), 0, 0));
							lblComments.setText("Comments:");
						}

						{
							SpinnerNumberModel spnEfficModel = new SpinnerNumberModel(75.0, 0.0,
									100.0, 1.0);
							spnEffic = new JSpinner();
							pnlDetails.add(spnEffic, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
									new Insets(0, 0, 0, 0), 0, 0));
							spnEffic.setModel(spnEfficModel);
							spnEffic.setMaximumSize(new java.awt.Dimension(70, 32767));
							spnEffic.addChangeListener(new ChangeListener() {
								public void stateChanged(ChangeEvent evt) {
									myRecipe.setEfficiency(Double.parseDouble(spnEffic.getValue()
											.toString()));
									displayRecipe();
								}
							});
							spnEffic.setEditor(new JSpinner.NumberEditor(spnEffic, "00.#"));
							spnEffic.getEditor().setPreferredSize(new java.awt.Dimension(28, 16));
						}
						{
							SpinnerNumberModel spnAttenModel = new SpinnerNumberModel(75.0, 0.0,
									100.0, 1.0);
							spnAtten = new JSpinner();
							pnlDetails.add(spnAtten, new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
									new Insets(0, 0, 0, 0), 0, 0));
							spnAtten.setModel(spnAttenModel);
							spnAtten.addChangeListener(new ChangeListener() {
								public void stateChanged(ChangeEvent evt) {
									myRecipe.setAttenuation(Double.parseDouble(spnAtten.getValue()
											.toString()));
									displayRecipe();
								}
							});
							spnAtten.setEditor(new JSpinner.NumberEditor(spnAtten, "00.#"));
						}
						{
							SpinnerNumberModel spnOgModel = new SpinnerNumberModel(1.000, 0.900,
									2.000, 0.001);
							spnOG = new JSpinner();
							pnlDetails.add(spnOG, new GridBagConstraints(6, 0, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
									new Insets(0, 0, 0, 0), 0, 0));
							spnOG.setModel(spnOgModel);
							spnOG.addChangeListener(new ChangeListener() {
								public void stateChanged(ChangeEvent evt) {
									myRecipe.setEstOg(Double.parseDouble(spnOG.getValue()
											.toString()));
									displayRecipe();
								}
							});
							spnOG.setEditor(new JSpinner.NumberEditor(spnOG, "0.000"));
							spnOG.getEditor().setPreferredSize(new java.awt.Dimension(20, 16));
						}
						{
							SpinnerNumberModel spnFgModel = new SpinnerNumberModel(1.000, 0.900,
									2.000, 0.001);
							spnFG = new JSpinner();
							pnlDetails.add(spnFG, new GridBagConstraints(6, 1, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
									new Insets(0, 0, 0, 0), 0, 0));
							spnFG.setModel(spnFgModel);
							spnFG.setEditor(new JSpinner.NumberEditor(spnFG, "0.000"));
							spnFG.addChangeListener(new ChangeListener() {
								public void stateChanged(ChangeEvent evt) {
									// set the new FG, and update alc:
									myRecipe.setEstFg(Double.parseDouble(spnFG.getValue()
											.toString()));
									displayRecipe();
								}
							});
						}
						{
							lblIBUvalue = new JLabel();
							pnlDetails.add(lblIBUvalue, new GridBagConstraints(8, 1, 1, 1, 0.0,
									0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
									new Insets(0, 0, 0, 0), 0, 0));
							lblIBUvalue.setText("IBUs");
						}
						{
							lblColourValue = new JLabel();
							pnlDetails.add(lblColourValue, new GridBagConstraints(8, 2, 1, 1, 0.0,
									0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
									new Insets(0, 0, 0, 0), 0, 0));
							lblColourValue.setText("Colour");
						}
						{
							lblAlcValue = new JLabel();
							pnlDetails.add(lblAlcValue, new GridBagConstraints(8, 0, 1, 1, 0.0,
									0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
									new Insets(0, 0, 0, 0), 0, 0));
							lblAlcValue.setText("Alc");
						}
						{
							scpComments = new JScrollPane();
							pnlDetails.add(scpComments, new GridBagConstraints(6, 4, 4, 2, 0.0,
									0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 0, 0), 0, 0));
							{
								txtComments = new JTextArea();
								scpComments.setViewportView(txtComments);
								txtComments.setText("Comments");
								txtComments.setWrapStyleWord(true);
								txtComments.setPreferredSize(new java.awt.Dimension(117, 42));
								txtComments.addFocusListener(new FocusAdapter() {
									public void focusLost(FocusEvent evt) {
										if (!txtComments.getText().equals(myRecipe.getComments())) {
											myRecipe.setComments(txtComments.getText());
										}
									}
								});
							}
						}
						{
							cmbYeastModel = new ComboModel();
							cmbYeast = new JComboBox();
							pnlDetails.add(cmbYeast, new GridBagConstraints(1, 3, 5, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
									new Insets(0, 0, 0, 0), 0, 0));
							cmbYeast.setModel(cmbYeastModel);
							cmbYeast.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									Yeast y = (Yeast) cmbYeastModel.getSelectedItem();
									if (myRecipe != null && y != myRecipe.getYeastObj()) {
										myRecipe.setYeast(y);
									}

									cmbYeast.setToolTipText(SBStringUtils.multiLineToolTip(40, y
											.getDescription()));
								}
							});
						}
						{
							cmbSizeUnitsModel = new ComboModel();
							cmbSizeUnits = new JComboBox();
							pnlDetails.add(cmbSizeUnits, new GridBagConstraints(2, 4, 2, 1, 0.0,
									0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
									new Insets(0, 0, 0, 0), 0, 0));
							cmbSizeUnits.setModel(cmbSizeUnitsModel);
							cmbSizeUnits.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									String q = (String) cmbSizeUnits.getSelectedItem();
									if (myRecipe != null && q != myRecipe.getVolUnits()) {
										myRecipe.setPostBoilVolUnits(q);
										displayRecipe();
									}
								}
							});
						}
						{
							lblSizeUnits = new JLabel();
							pnlDetails.add(lblSizeUnits, new GridBagConstraints(2, 5, 2, 1, 0.0,
									0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
									new Insets(0, 0, 0, 0), 0, 0));
							lblSizeUnits.setText("Size Units");
						}
						{
							boilTimeLable = new JLabel();
							pnlDetails.add(boilTimeLable, new GridBagConstraints(4, 4, 1, 1, 0.0,
									0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
									new Insets(0, 0, 0, 0), 0, 0));
							boilTimeLable.setText("Boil Min:");
						}
						{
							evapLabel = new JLabel();
							pnlDetails.add(evapLabel, new GridBagConstraints(4, 5, 1, 1, 0.0, 0.0,
									GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0,
											0, 0, 0), 0, 0));
							evapLabel.setText("Evap/hr:");
						}
						{
							boilMinText = new JTextField();
							pnlDetails.add(boilMinText, new GridBagConstraints(5, 4, 1, 1, 0.0,
									0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
									new Insets(0, 0, 0, 0), 0, 0));
							boilMinText.setText("60");
							boilMinText.addFocusListener(this);
							boilMinText.addActionListener(this);
						}
						{
							evapText = new JTextField();
							pnlDetails.add(evapText, new GridBagConstraints(5, 5, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
									new Insets(0, 0, 0, 0), 0, 0));
							evapText.setText("4");
							evapText.addFocusListener(this);
							evapText.addActionListener(this);
						}
						{
							alcMethodComboModel = new DefaultComboBoxModel(new String[]{"Volume", "Weight"});
							alcMethodCombo = new JComboBox(alcMethodComboModel);							
							pnlDetails.add(alcMethodCombo, new GridBagConstraints(9, 0, 1, 1, 0.0,
									0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
									new Insets(0, 0, 0, 0), 0, 0));
							alcMethodCombo.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									recipeSettingsActionPerformed(evt);
								}
							});
						}
						{
							ibuMethodComboModel = new DefaultComboBoxModel(new String[]{"Tinseth", "Garetz",
							"Rager"});
							ibuMethodCombo = new JComboBox(ibuMethodComboModel);
							
							pnlDetails.add(ibuMethodCombo, new GridBagConstraints(9, 1, 1, 1, 0.0,
									0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
									new Insets(0, 0, 0, 0), 0, 0));
							ibuMethodCombo.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									recipeSettingsActionPerformed(evt);
								}
							});
						}
						{
							colourMethodComboModel = new DefaultComboBoxModel(new String[]{"SRM", "EBC"});
							colourMethodCombo = new JComboBox(colourMethodComboModel);
							pnlDetails.add(colourMethodCombo, new GridBagConstraints(9, 2, 1, 1,
									0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
									new Insets(0, 0, 0, 0), 0, 0));
							colourMethodCombo.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									recipeSettingsActionPerformed(evt);
								}
							});
						}
					}
					{
						stylePanel = new StylePanel();
						jTabbedPane1.addTab("Style", null, stylePanel, null);

					}
					{
						miscPanel = new MiscPanel(myRecipe);
						jTabbedPane1.addTab("Misc", null, miscPanel, null);
					}
					{
						notesPanel = new NotesPanel();
						jTabbedPane1.addTab("Notes", null, notesPanel, null);
					}
				}
				{
					pnlTables = new JPanel();
					BoxLayout pnlMaltsLayout = new BoxLayout(pnlTables,
							javax.swing.BoxLayout.Y_AXIS);
					pnlMain.add(pnlTables, new GridBagConstraints(0, 2, 1, 1, 0.5, 0.5,
							GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0,
									0), 0, 0));

					pnlTables.setLayout(pnlMaltsLayout);
					{
						pnlMalt = new JPanel();
						pnlTables.add(pnlMalt);
						BorderLayout pnlMaltLayout1 = new BorderLayout();
						FlowLayout pnlMaltLayout = new FlowLayout();
						pnlMalt.setBorder(BorderFactory.createTitledBorder(new LineBorder(
								new java.awt.Color(0, 0, 0), 1, false), "Fermentables",
								TitledBorder.LEADING, TitledBorder.TOP, new java.awt.Font("Dialog",
										1, 12), new java.awt.Color(51, 51, 51)));
						pnlMalt.setLayout(pnlMaltLayout1);
						{
							jScrollPane1 = new JScrollPane();
							pnlMalt.add(jScrollPane1, BorderLayout.CENTER);
							{
								maltTableModel = new MaltTableModel(this);
								maltTable = new JTable() {
									public String getToolTipText(MouseEvent e) {
										java.awt.Point p = e.getPoint();
										int rowIndex = rowAtPoint(p);
										return SBStringUtils.multiLineToolTip(40, maltTableModel
												.getDescriptionAt(rowIndex));

									}
								};

								jScrollPane1.setViewportView(maltTable);
								maltTable.setModel(maltTableModel);
								maltTable.setAutoCreateColumnsFromModel(false);
								maltTable.getTableHeader().setReorderingAllowed(false);
								maltTable.addMouseListener(new MouseAdapter() {
									public void mouseClicked(MouseEvent evt) {
										int i = maltTable.getSelectedRow();
										// descriptionTextArea.setText(myRecipe.getMaltDescription(i));
									}
								});
								TableColumn maltColumn = maltTable.getColumnModel().getColumn(0);

								// set up malt list combo
								JComboBox maltComboBox = new JComboBox();
								cmbMaltModel = new ComboModel();
								maltComboBox.setModel(cmbMaltModel);
								maltColumn.setCellEditor(new DefaultCellEditor(maltComboBox));
								maltComboBox.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent evt) {
										Fermentable f = (Fermentable) cmbMaltModel
												.getSelectedItem();
										int i = maltTable.getSelectedRow();
										if (myRecipe != null && i != -1) {
											Fermentable f2 = myRecipe.getFermentable(i);
											f2.setLov(f.getLov());
											f2.setPppg(f.getPppg());
											f2.setDescription(f.getDescription());
											f2.setMashed(f.getMashed());
											f2.setSteep(f.getSteep());
										}

									}
								});

								// set up malt units combo
								JComboBox maltUnitsComboBox = new JComboBox();
								cmbMaltUnitsModel = new ComboModel();
								maltUnitsComboBox.setModel(cmbMaltUnitsModel);
								maltColumn = maltTable.getColumnModel().getColumn(2);
								maltColumn.setCellEditor(new DefaultCellEditor(maltUnitsComboBox));
								maltUnitsComboBox.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent evt) {
										String u = (String) cmbMaltUnitsModel.getSelectedItem();
										int i = maltTable.getSelectedRow();
										if (myRecipe != null && i != -1) {
											Fermentable f2 = myRecipe.getFermentable(i);
											f2.setUnitsFull(u);
											myRecipe.calcMaltTotals();
											displayRecipe();
										}

									}
								});

							}
						}
						{
							tblMaltTotalsModel = new DefaultTableModel(new String[][]{{""}},
									new String[]{"Malt", "Amount", "Units", "Points", "Lov",
											"Cost/U", "%"});
							tblMaltTotals = new JTable();
							pnlMalt.add(tblMaltTotals, BorderLayout.SOUTH);
							tblMaltTotals.setModel(tblMaltTotalsModel);
							tblMaltTotals.getTableHeader().setEnabled(false);
							tblMaltTotals.setAutoCreateColumnsFromModel(false);

						}
					}
					{
						pnlMaltButtons = new JPanel();
						pnlTables.add(pnlMaltButtons);
						FlowLayout pnlMaltButtonsLayout = new FlowLayout();
						pnlMaltButtonsLayout.setAlignment(FlowLayout.LEFT);
						pnlMaltButtonsLayout.setVgap(0);
						pnlMaltButtons.setLayout(pnlMaltButtonsLayout);
						pnlMaltButtons.setPreferredSize(new java.awt.Dimension(390, 34));
						{
							tlbMalt = new JToolBar();
							pnlMaltButtons.add(tlbMalt);
							tlbMalt.setPreferredSize(new java.awt.Dimension(140, 20));
							tlbMalt.setFloatable(false);
							{
								btnAddMalt = new JButton();
								tlbMalt.add(btnAddMalt);
								btnAddMalt.setText("+");
								btnAddMalt.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent evt) {
										if (myRecipe != null) {
											Fermentable f = new Fermentable(myRecipe.getMaltUnits());
											myRecipe.addMalt(f);
											maltTable.updateUI();
											displayRecipe();
										}
									}
								});
							}
							{
								btnDelMalt = new JButton();
								tlbMalt.add(btnDelMalt);
								btnDelMalt.setText("-");
								btnDelMalt.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent evt) {
										if (myRecipe != null) {
											int i = maltTable.getSelectedRow();
											myRecipe.delMalt(i);
											maltTable.updateUI();
											displayRecipe();
										}

									}
								});
							}
						}
					}
					{
						pnlHops = new JPanel();
						BorderLayout pnlHopsLayout = new BorderLayout();
						pnlHops.setBorder(BorderFactory.createTitledBorder(new LineBorder(
								new java.awt.Color(0, 0, 0), 1, false), "Hops",
								TitledBorder.LEADING, TitledBorder.TOP, new java.awt.Font("Dialog",
										1, 12), new java.awt.Color(51, 51, 51)));
						pnlHops.setLayout(pnlHopsLayout);
						pnlTables.add(pnlHops);
						{
							tblHopsTotalsModel = new DefaultTableModel(new String[][]{{""}},
									new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9"});
							tblHopsTotals = new JTable();
							pnlHops.add(tblHopsTotals, BorderLayout.SOUTH);
							tblHopsTotals.setModel(tblHopsTotalsModel);
							tblHopsTotals.setAutoCreateColumnsFromModel(false);

						}
						{
							jScrollPane2 = new JScrollPane();
							pnlHops.add(jScrollPane2, BorderLayout.CENTER);
							{
								hopsTableModel = new HopsTableModel(this);
								hopsTable = new JTable() {
									public String getToolTipText(MouseEvent e) {
										java.awt.Point p = e.getPoint();
										int rowIndex = rowAtPoint(p);
										return SBStringUtils.multiLineToolTip(40, hopsTableModel
												.getDescriptionAt(rowIndex));

									}
								};
								jScrollPane2.setViewportView(hopsTable);
								hopsTable.setModel(hopsTableModel);
								hopsTable.getTableHeader().setReorderingAllowed(false);

								TableColumn hopColumn = hopsTable.getColumnModel().getColumn(0);
								JComboBox hopComboBox = new JComboBox();
								cmbHopsModel = new ComboModel();
								hopComboBox.setModel(cmbHopsModel);
								hopColumn.setCellEditor(new DefaultCellEditor(hopComboBox));
								hopComboBox.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent evt) {
										Hop h = (Hop) cmbHopsModel.getSelectedItem();
										int i = hopsTable.getSelectedRow();
										if (myRecipe != null && i != -1) {
											Hop h2 = (Hop) myRecipe.getHop(i);
											h2.setAlpha(h.getAlpha());
											h2.setDescription(h.getDescription());
										}

									}
								});

								// set up hop units combo
								JComboBox hopsUnitsComboBox = new JComboBox();
								cmbHopsUnitsModel = new ComboModel();
								hopsUnitsComboBox.setModel(cmbHopsUnitsModel);
								hopColumn = hopsTable.getColumnModel().getColumn(4);
								hopColumn.setCellEditor(new DefaultCellEditor(hopsUnitsComboBox));
								hopsUnitsComboBox.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent evt) {
										String u = (String) cmbHopsUnitsModel.getSelectedItem();
										int i = hopsTable.getSelectedRow();
										if (myRecipe != null && i != -1) {
											Hop h = (Hop) myRecipe.getHop(i);
											h.setUnitsFull(u);
											myRecipe.calcHopsTotals();
											// tblHops.updateUI();
											displayRecipe();

										}

									}
								});

								// set up hop type combo
								String[] forms = {"Leaf", "Pellet", "Plug"};
								JComboBox hopsFormComboBox = new JComboBox(forms);
								hopColumn = hopsTable.getColumnModel().getColumn(1);
								hopColumn.setCellEditor(new DefaultCellEditor(hopsFormComboBox));

								//								 set up hop add combo
								String[] add = {"Boil", "FWH", "Dry", "Mash"};
								JComboBox hopsAddComboBox = new JComboBox(add);
								hopColumn = hopsTable.getColumnModel().getColumn(5);
								hopColumn.setCellEditor(new DefaultCellEditor(hopsAddComboBox));

							}
						}
					}
					{
						pnlHopsButtons = new JPanel();
						FlowLayout pnlHopsButtonsLayout = new FlowLayout();
						pnlHopsButtonsLayout.setAlignment(FlowLayout.LEFT);
						pnlHopsButtonsLayout.setVgap(0);
						pnlHopsButtons.setLayout(pnlHopsButtonsLayout);
						pnlTables.add(pnlHopsButtons);
						pnlHopsButtons.setPreferredSize(new java.awt.Dimension(512, 16));
						{
							tlbHops = new JToolBar();
							pnlHopsButtons.add(tlbHops);
							tlbHops.setPreferredSize(new java.awt.Dimension(70, 19));
							tlbHops.setFloatable(false);
							{
								btnAddHop = new JButton();
								tlbHops.add(btnAddHop);
								btnAddHop.setText("+");
								btnAddHop.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent evt) {
										if (myRecipe != null) {
											Hop h = new Hop(myRecipe.getHopUnits());
											myRecipe.addHop(h);
											hopsTable.updateUI();
											displayRecipe();

										}
									}
								});
							}
							{
								btnDelHop = new JButton();
								tlbHops.add(btnDelHop);
								btnDelHop.setText("-");
								btnDelHop.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent evt) {
										if (myRecipe != null) {
											int i = hopsTable.getSelectedRow();
											myRecipe.delHop(i);
											hopsTable.updateUI();
											displayRecipe();
										}
									}
								});
							}
						}
					}

				}
				{
					statusPanel = new JPanel();
					FlowLayout statusPanelLayout = new FlowLayout();
					statusPanelLayout.setAlignment(FlowLayout.LEFT);
					statusPanelLayout.setHgap(2);
					statusPanelLayout.setVgap(2);
					statusPanel.setLayout(statusPanelLayout);
					pnlMain.add(statusPanel, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
							GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0,
									0, 0, 0), 0, 0));
					{
						fileNamePanel = new JPanel();
						statusPanel.add(fileNamePanel);
						fileNamePanel.setBorder(BorderFactory
								.createBevelBorder(BevelBorder.LOWERED));
						{
							fileNameLabel = new JLabel();
							fileNamePanel.add(fileNameLabel);
							fileNameLabel.setText("File Name");
							fileNameLabel.setFont(new java.awt.Font("Dialog", 1, 10));
						}
					}
					{
						ibuMethodPanel = new JPanel();
						statusPanel.add(ibuMethodPanel);
						ibuMethodPanel.setBorder(BorderFactory
								.createBevelBorder(BevelBorder.LOWERED));
						{
							ibuMethodLabel = new JLabel();
							ibuMethodPanel.add(ibuMethodLabel);
							ibuMethodLabel.setText("IBU Method:");
							ibuMethodLabel.setFont(new java.awt.Font("Dialog", 1, 10));
						}
					}
					{
						alcMethodPanel = new JPanel();
						statusPanel.add(alcMethodPanel);
						alcMethodPanel.setBorder(BorderFactory
								.createBevelBorder(BevelBorder.LOWERED));
						{
							alcMethodLabel = new JLabel();
							alcMethodPanel.add(alcMethodLabel);
							alcMethodLabel.setText("Alc Method:");
							alcMethodLabel.setFont(new java.awt.Font("Dialog", 1, 10));
						}
					}
				}
			}
			{
				jMenuBar1 = new JMenuBar();
				setJMenuBar(jMenuBar1);
				{
					fileMenu = new JMenu();
					jMenuBar1.add(fileMenu);
					fileMenu.setText("File");
					{
						newFileMenuItem = new JMenuItem();
						fileMenu.add(newFileMenuItem);
						newFileMenuItem.setText("New");
						newFileMenuItem.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								// This is just a test right now to see that
								// stuff is changed.
								myRecipe = new Recipe();
								currentFile = null;
								attachRecipeData();
								displayRecipe();

							}
						});
					}
					{
						openFileMenuItem = new JMenuItem();
						fileMenu.add(openFileMenuItem);
						openFileMenuItem.setText("Open");
						openFileMenuItem.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {

								// Show open dialog; this method does
								// not return until the dialog is closed
								String[] ext = {"xml", "qbrew"};
								sbFileFilter saveFileFilter = new sbFileFilter(ext);
								fileChooser.setFileFilter(saveFileFilter);

								int returnVal = fileChooser.showOpenDialog(jMenuBar1);
								if (returnVal == JFileChooser.APPROVE_OPTION) {
									File file = fileChooser.getSelectedFile();
									Debug.print("Opening: " + file.getName() + ".\n");
									ImportXml imp = new ImportXml(file.toString());
									myRecipe = imp.handler.getRecipe();
									myRecipe.calcMaltTotals();
									myRecipe.calcHopsTotals();
									myRecipe.mash.calcMashSchedule();
									attachRecipeData();
									currentFile = file;
									displayRecipe();

								} else {
									Debug.print("Open command cancelled by user.\n");
								}

							}
						});

					}
					{
						findFileMenuItem = new JMenuItem();
						findFileMenuItem.setText("Find");
						fileMenu.add(findFileMenuItem);
						final JFrame owner = this;
						findFileMenuItem.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								// open the find dialog
								FindDialog fd = new FindDialog(owner);
								fd.setVisible(true);

							}
						});
					}
					{
						saveMenuItem = new JMenuItem();
						fileMenu.add(saveMenuItem);
						saveMenuItem.setText("Save");

						saveMenuItem.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {

								int choice = 1;

								if (currentFile != null) {
									File file = currentFile;
									//This is where a real application would save the file.
									try {
										FileWriter out = new FileWriter(file);
										out.write(myRecipe.toXML());
										out.close();										
										Debug.print("Saved: " + file.getAbsoluteFile());
										
									} catch (Exception e) {
										// TODO: handle io exception
									}

								}
								// prompt to save if not already saved
								else {
									JDialog warning = new JDialog();
									choice = JOptionPane.showConfirmDialog(null,
											"File not saved.  Do you wish to save it?",
											"File note saved", JOptionPane.YES_NO_OPTION);

								}

								if (choice == 0) {
									// same as save as:
									saveAs();
								}

							}
						});
					}
					{
						saveAsMenuItem = new JMenuItem();
						fileMenu.add(saveAsMenuItem);
						saveAsMenuItem.setText("Save As ...");
						saveAsMenuItem.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								// This is just a test right now to see that
								// stuff is changed.
								Debug.print(myRecipe.toXML());
								
								saveAs();

							}
						});
					}
					{
						exportMenu = new JMenu();
						fileMenu.add(exportMenu);
						exportMenu.setText("Export");
						{
							exportHTMLmenu = new JMenuItem();
							exportMenu.add(exportHTMLmenu);
							exportHTMLmenu.setText("HTML");
							exportHTMLmenu.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									// Show save dialog; this method does
									// not return until the dialog is closed	
									String[] ext = {"html", "htm"};
									sbFileFilter saveFileFilter = new sbFileFilter(ext);
									fileChooser.setFileFilter(saveFileFilter);
									fileChooser.setSelectedFile(new File(myRecipe.getName()
											+ ".html"));

									int returnVal = fileChooser.showSaveDialog(jMenuBar1);
									if (returnVal == JFileChooser.APPROVE_OPTION) {
										File file = fileChooser.getSelectedFile();
										//This is where a real application would save the file.
										try {
											saveAsHTML(file);
										} catch (Exception e) {
											// TODO: handle io exception
										}
									} else {
										Debug.print("Save command cancelled by user.\n");
									}

								}
							});

							exportTextMenuItem = new JMenuItem();
							exportMenu.add(exportTextMenuItem);
							exportTextMenuItem.setText("Text");
							exportTextMenuItem.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									// Show save dialog; this method does
									// not return until the dialog is closed
									String[] ext = {"txt"};
									sbFileFilter saveFileFilter = new sbFileFilter(ext);
									fileChooser.setFileFilter(saveFileFilter);
									fileChooser.setSelectedFile(new File(myRecipe.getName()
											+ ".txt"));
									int returnVal = fileChooser.showSaveDialog(jMenuBar1);
									if (returnVal == JFileChooser.APPROVE_OPTION) {
										File file = fileChooser.getSelectedFile();
										//This is where a real application would save the file.
										try {
											FileWriter out = new FileWriter(file);
											out.write(myRecipe.toText());
											out.close();
										} catch (Exception e) {
											// TODO: handle io exception
										}
									} else {
										Debug.print("Export text command cancelled by user.\n");
									}

								}
							});
						}
					}
					{
						jSeparator2 = new JSeparator();
						fileMenu.add(jSeparator2);
					}
					{
						exitMenuItem = new JMenuItem();
						fileMenu.add(exitMenuItem);
						exitMenuItem.setText("Exit");
						exitMenuItem.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								// exit program								
								System.exit(0);
							}
						});
					}
				}
				{
					jMenu4 = new JMenu();
					jMenuBar1.add(jMenu4);
					jMenu4.setText("Edit");
					{
						final JFrame owner = this;
						editPrefsMenuItem = new JMenuItem();
						jMenu4.add(editPrefsMenuItem);
						editPrefsMenuItem.setText("Preferences...");
						editPrefsMenuItem.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								PreferencesDialog d = new PreferencesDialog(owner, preferences);
								d.setVisible(true);
							}
						});

					}

					{
						jSeparator1 = new JSeparator();
						jMenu4.add(jSeparator1);
					}
					{
						deleteMenuItem = new JMenuItem();
						jMenu4.add(deleteMenuItem);
						deleteMenuItem.setText("Delete");
						deleteMenuItem.setEnabled(false);
					}
				}
				{
					mnuView = new JMenu();
					jMenuBar1.add(mnuView);
					mnuView.setText("View");
					{
						mashManagerMenuItem = new JMenuItem();
						mnuView.add(mashManagerMenuItem);
						mashManagerMenuItem.setText("Mash Manager...");
						mashManagerMenuItem.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								mashMgr = new MashManager(myRecipe);
								mashMgr.setVisible(true);

							}
						});
					}
				}
				{
					jMenu5 = new JMenu();
					jMenuBar1.add(jMenu5);
					jMenu5.setText("Help");
					{
						helpMenuItem = new JMenuItem();
						jMenu5.add(helpMenuItem);
						helpMenuItem.setText("Help");
					}
					{
						aboutMenuItem = new JMenuItem();
						jMenu5.add(aboutMenuItem);
						aboutMenuItem.setText("About...");
						final JFrame owner = this;
						aboutMenuItem.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								aboutDlg = new AboutDialog(owner);
								aboutDlg.setVisible(true);

							}
						});
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Auto-generated method for setting the popup menu for a component
	 */
	private void setComponentPopupMenu(final java.awt.Component parent,
			final javax.swing.JPopupMenu menu) {
		parent.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mousePressed(java.awt.event.MouseEvent e) {
				if (e.isPopupTrigger())
					menu.show(parent, e.getX(), e.getY());
			}
			public void mouseReleased(java.awt.event.MouseEvent e) {
				if (e.isPopupTrigger())
					menu.show(parent, e.getX(), e.getY());
			}
		});
	}

	public void saveAsHTML(File f) throws Exception {
		// save file as xml, then transform it to html
		File tmp = new File("tmp.xml");
		FileWriter out = new FileWriter(tmp);
		out.write(myRecipe.toXML());
		out.close();
		String htmlFileName = myRecipe.getName() + ".html";
		FileOutputStream output = new java.io.FileOutputStream(f);
		XmlTransformer.writeStream("tmp.xml", "src/strangebrew/data/recipeToHtml.xslt", output);
		tmp.delete();

	}

	public class SpinnerEditor extends AbstractCellEditor implements TableCellEditor {
		final JSpinner spinner = new JSpinner();

		// Initializes the spinner.
		public SpinnerEditor() {

		}

		public SpinnerEditor(SpinnerNumberModel model) {
			spinner.setModel(model);
		}

		// Prepares the spinner component and returns it.
		public Component getTableCellEditorComponent(JTable table, Object value,
				boolean isSelected, int row, int column) {
			spinner.setValue(value);
			return spinner;
		}

		// Enables the editor only for double-clicks.
		public boolean isCellEditable(EventObject evt) {
			if (evt instanceof MouseEvent) {
				return ((MouseEvent) evt).getClickCount() >= 2;
			}
			return true;
		}

		// Returns the spinners current value.
		public Object getCellEditorValue() {
			return spinner.getValue();
		}
	}

	private void addColumnWidthListeners() {
		TableColumnModel mtcm = maltTable.getColumnModel();
		TableColumnModel htcm = hopsTable.getColumnModel();

		//: listener that watches the width of a column
		PropertyChangeListener mpcl = new PropertyChangeListener() {
			private int columnCount = maltTable.getColumnCount();
			private int[] width = new int[columnCount];

			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals("preferredWidth")) {
					TableColumnModel tcm = maltTable.getColumnModel();
					TableColumnModel tcmt = tblMaltTotals.getColumnModel();
					int columnCount = tcm.getColumnCount();

					// for each column, get its width
					for (int i = 0; i < columnCount; i++) {
						tcmt.getColumn(i).setPreferredWidth(tcm.getColumn(i).getPreferredWidth());
					}
				}
			}
		};

		//: listener that watches the width of a column
		PropertyChangeListener hpcl = new PropertyChangeListener() {
			private int columnCount = hopsTable.getColumnCount();
			private int[] width = new int[columnCount];

			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals("preferredWidth")) {
					TableColumnModel tcm = hopsTable.getColumnModel();
					TableColumnModel tcmt = tblHopsTotals.getColumnModel();
					int columnCount = tcm.getColumnCount();

					// for each column, get its width
					for (int i = 0; i < columnCount; i++) {
						tcmt.getColumn(i).setPreferredWidth(tcm.getColumn(i).getPreferredWidth());
					}
				}
			}
		};

		//: add the column width lister to each column
		for (Enumeration e = mtcm.getColumns(); e.hasMoreElements();) {
			TableColumn tc = (TableColumn) e.nextElement();
			tc.addPropertyChangeListener(mpcl);
		}

		//: add the column width lister to each column
		for (Enumeration e = htcm.getColumns(); e.hasMoreElements();) {
			TableColumn tc = (TableColumn) e.nextElement();
			tc.addPropertyChangeListener(hpcl);
		}
	}

	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		String s = "";
		s = ((JTextField) o).getText();

		if (o == txtName)
			myRecipe.setName(s);
		else if (o == brewerNameText)
			myRecipe.setBrewer(s);
		else if (o == txtPreBoil) {
			myRecipe.setPreBoil(Double.parseDouble(s));
			displayRecipe();
		} else if (o == postBoilText) {
			myRecipe.setPostBoil(Double.parseDouble(s));
			displayRecipe();
		} else if (o == evapText) {
			myRecipe.setEvap(Double.parseDouble(s));
			displayRecipe();
		} else if (o == boilMinText) {
			myRecipe.setBoilMinutes(Integer.parseInt(s));
			displayRecipe();
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

	private void saveAs() {
		// Show save dialog; this method does
		// not return until the dialog is closed
		String[] ext = {"xml"};
		sbFileFilter saveFileFilter = new sbFileFilter(ext);
		fileChooser.setFileFilter(saveFileFilter);

		int returnVal = fileChooser.showSaveDialog(jMenuBar1);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();

			//This is where a real application would save the file.
			try {
				FileWriter out = new FileWriter(file);
				out.write(myRecipe.toXML());
				out.close();
			} catch (Exception e) {
				// TODO: handle io exception
			}
		} else {
			Debug.print("Save command cancelled by user.\n");
		}
	}

	private class sbFileFilter extends FileFilter {

		private String[] extensions = {"xml"};
		private String description = "";

		public sbFileFilter(String[] s) {
			extensions = s;
		}

		public boolean accept(File f) {
			String ext = null;
			String s = f.getName();
			int i = s.lastIndexOf('.');

			if (i > 0 && i < s.length() - 1) {
				ext = s.substring(i + 1).toLowerCase();
			}
			if (ext == null) {
				return false;
			} else {
				i = 0;
				while (i < extensions.length) {
					if (ext.equals(extensions[i])) {
						return true;
					}
					i++;
				}
				return false;
			}
		}

		public String getDescription() {
			return description;
		}
	}

	public void setRecipe(Recipe r, File f) {
		currentFile = f;
		myRecipe = r;
		displayRecipe();
		attachRecipeData();
	}

	private void recipeSettingsActionPerformed(ActionEvent evt) {
		Object o = evt.getSource();
		String s = (String) ((JComboBox) o).getSelectedItem();

		if (o == alcMethodCombo)
			myRecipe.setAlcMethod(s);					
		else if (o == ibuMethodCombo)
			myRecipe.setIBUMethod(s);
		else if (o == colourMethodCombo)
			myRecipe.setColourMethod(s);

		displayRecipe();
	}
}
