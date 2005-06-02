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
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.Enumeration;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.border.BevelBorder;
import java.awt.event.MouseAdapter;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.event.TableColumnModelListener;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.ListSelectionEvent;

import strangebrew.Database;
import strangebrew.Fermentable;
import strangebrew.Hop;
import strangebrew.ImportXml;
import strangebrew.Quantity;
import strangebrew.Recipe;
import strangebrew.Style;
import strangebrew.XmlTransformer;
import strangebrew.Yeast;
import strangebrew.Options;
import strangebrew.ui.preferences.PreferencesDialog;

public class StrangeSwing extends javax.swing.JFrame {

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
	private JMenuItem pasteMenuItem;
	private JLabel lblDate;
	private JTextField txtBrewer;
	private JLabel lblBrewer;
	private JTextField txtName;
	private JLabel lblName;
	private JPanel pnlDetails;
	private JTabbedPane jTabbedPane1;
	private JPanel jPanel1;
	private JMenuItem copyMenuItem;
	private JMenuItem cutMenuItem;
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
	private JPanel statusPanel;
	private JTextArea descriptionTextArea;
	private JPanel descriptionPanel;
	private JMenuItem mashManagerMenuItem;
	private JMenu mnuView;
	private JToolBar tlbMalt;
	private JPanel pnlMaltButtons;
	private JSlider sldMatch;
	private JLabel jLabel7;
	private JLabel jLabel6;
	private JLabel jLabel5;
	private JLabel jLabel4;
	private JLabel jLabel3;
	private JLabel jLabel2;
	private JLabel jLabel1;
	private JTextArea txaStyles;
	private JScrollPane jScrollPane3;
	private JPanel jPanel2;
	private JComboBox cmbStyle2;
	private JLabel lblStyle2;
	private JPanel pnlStyle;
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
	private JTable tblHops;
	private JScrollPane scpComments;
	private JTable tblMalt;
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
	private JTextField txtDate;
	private JLabel lblColour;
	private JLabel lblIBU;
	private JPanel pnlTables;
	private JLabel lblFG;
	private JLabel lblOG;
	private JLabel lblAtten;
	private JLabel lblEffic;
	private JLabel lblMash;
	private JLabel lblPostBoil;
	private JLabel lblPreBoil;
	private JLabel lblYeast;
	private JLabel lblStyle;
	private JMenuItem saveAsMenuItem;
	private JMenuItem saveMenuItem;
	private JMenuItem openFileMenuItem;
	private JMenuItem newFileMenuItem;
	private JMenu jMenu3;
	private JMenuBar jMenuBar1;
	private JMenuItem exportTextMenuItem;
	private JMenuItem editPrefsMenuItem;

	private MaltTableModel tblMaltModel;
	private DefaultTableModel tblMaltTotalsModel;
	private HopsTableModel tblHopsModel;
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
	

	public Recipe myRecipe;
	private Options preferences = new Options();

	private final static boolean DEBUG = true;

	/**
	 * Auto-generated main method to display this JFrame
	 */
	public static void main(String[] args) {
		StrangeSwing inst = new StrangeSwing();
		inst.setVisible(true);
	}

	public StrangeSwing()  {
		super();
		initGUI();
		// There has *got* to be a better way to do this:
		Database db = new Database();
		String path="";
		String slash = System.getProperty("file.separator"); 
		try {
			 path = new File(".").getCanonicalPath() + slash + "src" + slash 
			 + "strangebrew" + slash + "data";
		}
		catch (Exception e) {
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
		String fcpath = getClass().getProtectionDomain().getCodeSource()
		.getLocation().toString().substring(6) + slash;
		fileChooser.setCurrentDirectory(new File(path));
		
		// link malt table and totals:
		addColumnStateSupport();				
		myRecipe = new Recipe();
		attachRecipeData();
		displayRecipe();

	}

	public void attachRecipeData(){
		// this method attaches data from the recipe to the tables 
		// and comboboxes
		// use whenever the Recipe changes
		cmbStyleModel.addOrInsert(myRecipe.getStyleObj());
		cmbYeastModel.addOrInsert(myRecipe.getYeastObj());
		cmbSizeUnitsModel.addOrInsert(myRecipe.getVolUnits());
		tblMaltModel.setData(myRecipe);
		tblHopsModel.setData(myRecipe);
		tblMalt.updateUI();
		tblHops.updateUI();		

	}
	
	public void displayRecipe() {
		if (myRecipe == null)
			return;
		txtName.setText(myRecipe.getName());
		txtBrewer.setText(myRecipe.getBrewer());		
		txtPreBoil.setValue(new Double(myRecipe.getPreBoilVol(myRecipe.getVolUnits())));		
		lblSizeUnits.setText(myRecipe.getVolUnits());
		postBoilText.setValue(new Double(myRecipe.getPostBoilVol(myRecipe.getVolUnits())));
		spnEffic.setValue(new Double(myRecipe.getEfficiency()));
		spnAtten.setValue(new Double(myRecipe.getAttenuation()));
		spnOG.setValue(new Double(myRecipe.getEstOg()));
		spnFG.setValue(new Double(myRecipe.getEstFg()));
		txtComments.setText(myRecipe.getComments());
		lblIBUvalue.setText(myRecipe.df1.format(myRecipe.getIbu()));
		lblColourValue.setText(myRecipe.df1.format(myRecipe.getSrm()));
		lblAlcValue.setText(myRecipe.df1.format(myRecipe.getAlcohol()));
		tblMaltTotalsModel.setDataVector(new String[][]{{"Totals:",
			"" + myRecipe.df1.format(myRecipe.getTotalMaltLbs()), myRecipe.getMaltUnits(),
			"" + myRecipe.df3.format(myRecipe.getEstOg()), "" + myRecipe.df1.format(myRecipe.getSrm()),
			"$" + myRecipe.df2.format(myRecipe.getTotalMaltCost()), "100"}}, new String[]{"", "", "",
			"", "", "", ""}); 

		tblHopsTotalsModel.setDataVector(
			new String[][]{{"Totals:", "", "", "" + myRecipe.df1.format(myRecipe.getTotalHopsOz()),
					myRecipe.getHopUnits(), "", "", "" + myRecipe.df1.format(myRecipe.getIbu()),
					"$" + myRecipe.df2.format(myRecipe.getTotalHopsCost())}}, new String[]{"", "", "",
					"", "", "", "", "", ""});

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
				jPanel2Layout.columnWeights = new double[] {0.1,0.1};
				jPanel2Layout.columnWidths = new int[] {7,7};
				jPanel2Layout.rowWeights = new double[] {0.1,0.1,0.1,0.1};
				jPanel2Layout.rowHeights = new int[] {7,7,7,7};
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
						txtName.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								myRecipe.setName(txtName.getText());
							}
						});
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
								0.1};
						pnlDetailsLayout.columnWidths = new int[]{7, 7, 7, 7, 7, 7, 7};
						pnlDetailsLayout.rowWeights = new double[]{0.1, 0.1, 0.1, 0.1, 0.1, 0.1,
								0.1};
						pnlDetailsLayout.rowHeights = new int[]{7, 7, 7, 7, 7, 7, 7};
						pnlDetails.setLayout(pnlDetailsLayout);
						jTabbedPane1.addTab("Details", null, pnlDetails, null);
						{
							lblBrewer = new JLabel();
							pnlDetails.add(lblBrewer, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
									GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0,
											0, 0, 0), 0, 0));
							lblBrewer.setText("Brewer:");
						}
						{
							txtBrewer = new JTextField();
							pnlDetails.add(txtBrewer, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
									new Insets(0, 0, 0, 0), 0, 0));
							txtBrewer.setText("Brewer");
							txtBrewer.addFocusListener(new FocusAdapter() {
								public void focusLost(FocusEvent evt) {
									System.out.println("txtBrewer.focusLost, event=" + evt);
									//TODO add your code for txtBrewer.focusLost
								}
							});
							txtBrewer.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									myRecipe.setBrewer(txtBrewer.getText());
								}
							});
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
							lblMash = new JLabel();
							pnlDetails.add(lblMash, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0,
									GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0,
											0, 0, 0), 0, 0));
							lblMash.setText("Mash:");
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
							pnlDetails.add(lblOG, new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0,
									GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0,
											0, 0, 0), 0, 0));
							lblOG.setText("OG:");
						}
						{
							lblFG = new JLabel();
							pnlDetails.add(lblFG, new GridBagConstraints(3, 3, 1, 1, 0.0, 0.0,
									GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0,
											0, 0, 0), 0, 0));
							lblFG.setText("FG:");
						}
						{
							lblIBU = new JLabel();
							pnlDetails.add(lblIBU, new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0,
									GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0,
											0, 0, 0), 0, 0));
							lblIBU.setText("IBU:");
						}
						{
							lblAlc = new JLabel();
							pnlDetails.add(lblAlc, new GridBagConstraints(5, 2, 1, 1, 0.0, 0.0,
									GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0,
											0, 0, 0), 0, 0));
							lblAlc.setText("%Alc:");
						}
						{
							lblColour = new JLabel();
							pnlDetails.add(lblColour, new GridBagConstraints(5, 1, 1, 1, 0.0, 0.0,
									GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0,
											0, 0, 0), 0, 0));
							lblColour.setText("Colour:");
						}
						{
							txtDate = new JTextField();
							pnlDetails.add(txtDate, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
									new Insets(0, 0, 0, 0), 0, 0));
							txtDate.setText("Date");
						}
						{
							cmbStyleModel = new ComboModel();
							cmbStyle = new JComboBox();
							pnlDetails.add(cmbStyle, new GridBagConstraints(1, 2, 2, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
									new Insets(0, 0, 0, 0), 0, 0));
							cmbStyle.setModel(cmbStyleModel);
							cmbStyle.setMaximumSize(new java.awt.Dimension(100, 32767));

							cmbStyle.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									Style s = (Style) cmbStyleModel.getSelectedItem();
									if (myRecipe != null && s != myRecipe.getStyleObj()) {
										myRecipe.setStyle(s);
									}
									descriptionTextArea.setText(s.getDescription());
								}
							});
						}
						{
							txtPreBoil = new JFormattedTextField();
							pnlDetails.add(txtPreBoil, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0,
									GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
									new Insets(0, 0, 0, 0), 0, 0));
							txtPreBoil.setText("Pre Boil");
							txtPreBoil.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									myRecipe.setPreBoil(Double.parseDouble(txtPreBoil.getText()
											.toString()));
									displayRecipe();
								}
							});
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
							pnlDetails.add(lblComments, new GridBagConstraints(3, 4, 1, 1, 0.0,
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
							pnlDetails.add(spnOG, new GridBagConstraints(4, 2, 1, 1, 0.0, 0.0,
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
						}
						{
							SpinnerNumberModel spnFgModel = new SpinnerNumberModel(1.000, 0.900,
									2.000, 0.001);
							spnFG = new JSpinner();
							pnlDetails.add(spnFG, new GridBagConstraints(4, 3, 1, 1, 0.0, 0.0,
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
							pnlDetails.add(lblIBUvalue, new GridBagConstraints(6, 0, 1, 1, 0.0,
									0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
									new Insets(0, 0, 0, 0), 0, 0));
							lblIBUvalue.setText("IBUs");
						}
						{
							lblColourValue = new JLabel();
							pnlDetails.add(lblColourValue, new GridBagConstraints(6, 1, 1, 1, 0.0,
									0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
									new Insets(0, 0, 0, 0), 0, 0));
							lblColourValue.setText("Colour");
						}
						{
							lblAlcValue = new JLabel();
							pnlDetails.add(lblAlcValue, new GridBagConstraints(6, 2, 1, 1, 0.0,
									0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
									new Insets(0, 0, 0, 0), 0, 0));
							lblAlcValue.setText("Alc");
						}
						{
							scpComments = new JScrollPane();
							pnlDetails.add(scpComments, new GridBagConstraints(3, 5, 4, 2, 0.0,
									0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 0, 0), 0, 0));
							{
								txtComments = new JTextArea();
								scpComments.setViewportView(txtComments);
								txtComments.setText("Comments");
								txtComments.setWrapStyleWord(true);
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
							pnlDetails.add(cmbYeast, new GridBagConstraints(1, 3, 2, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
									new Insets(0, 0, 0, 0), 0, 0));
							cmbYeast.setModel(cmbYeastModel);
							cmbYeast.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									Yeast y = (Yeast) cmbYeastModel.getSelectedItem();
									if (myRecipe != null && y != myRecipe.getYeastObj()) {
										myRecipe.setYeast(y);
									}
									descriptionTextArea.setText(y.getDescription());
								}
							});
						}
						{
							cmbSizeUnitsModel = new ComboModel();
							cmbSizeUnits = new JComboBox();
							pnlDetails.add(cmbSizeUnits, new GridBagConstraints(2, 4, 1, 1, 0.0,
									0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
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
							pnlDetails.add(lblSizeUnits, new GridBagConstraints(2, 5, 1, 1, 0.0,
									0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
									new Insets(0, 0, 0, 0), 0, 0));
							lblSizeUnits.setText("Size Units");
						}
					}
					{
						pnlStyle = new JPanel();
						FlowLayout pnlStyleLayout = new FlowLayout();
						pnlStyle.setLayout(pnlStyleLayout);
						jTabbedPane1.addTab("Style", null, pnlStyle, null);
						{
							lblStyle2 = new JLabel();
							pnlStyle.add(lblStyle2);
							lblStyle2.setText("Style:");
						}
						{
							ComboBoxModel cmbStyle2Model = new DefaultComboBoxModel(new String[]{
									"Item One", "Item Two"});
							cmbStyle2 = new JComboBox();
							pnlStyle.add(cmbStyle2);
							cmbStyle2.setModel(cmbStyle2Model);
						}
						{
							jPanel2 = new JPanel();
							GridBagLayout jPanel2Layout1 = new GridBagLayout();
							jPanel2Layout1.columnWeights = new double[]{0.1, 0.1, 0.1, 0.1};
							jPanel2Layout1.columnWidths = new int[]{7, 7, 7, 7};
							jPanel2Layout1.rowWeights = new double[]{0.1, 0.1, 0.1, 0.1, 0.1, 0.1};
							jPanel2Layout1.rowHeights = new int[]{7, 7, 7, 7, 7, 7};
							jPanel2.setPreferredSize(new java.awt.Dimension(179, 120));
							jPanel2.setLayout(jPanel2Layout1);
							pnlStyle.add(jPanel2);
							jPanel2.setBorder(BorderFactory.createTitledBorder(new LineBorder(
									new java.awt.Color(0, 0, 0), 1, false), "Recipe Conformance:",
									TitledBorder.LEADING, TitledBorder.TOP, new java.awt.Font(
											"Dialog", 0, 12), new java.awt.Color(0, 0, 0)));
							{
								jLabel5 = new JLabel();
								jPanel2.add(jLabel5, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
										GridBagConstraints.EAST, GridBagConstraints.NONE,
										new Insets(0, 0, 0, 0), 0, 0));
								jLabel5.setText("OG:");
								jLabel5.setBounds(74, 3, 60, 30);
							}
							{
								jLabel1 = new JLabel();
								jPanel2.add(jLabel1, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
										GridBagConstraints.CENTER, GridBagConstraints.NONE,
										new Insets(0, 0, 0, 0), 0, 0));
								GridLayout jLabel1Layout = new GridLayout(1, 1);
								jLabel1.setLayout(jLabel1Layout);
								jLabel1.setText("Low:");
							}
							{
								jLabel2 = new JLabel();
								jPanel2.add(jLabel2, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
										GridBagConstraints.CENTER, GridBagConstraints.NONE,
										new Insets(0, 0, 0, 0), 0, 0));
								jLabel2.setText("Recipe:");
							}
							{
								jLabel3 = new JLabel();
								jPanel2.add(jLabel3, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
										GridBagConstraints.CENTER, GridBagConstraints.NONE,
										new Insets(0, 0, 0, 0), 0, 0));
								jLabel3.setText("High:");
							}
							{
								jLabel4 = new JLabel();
								jPanel2.add(jLabel4, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
										GridBagConstraints.EAST, GridBagConstraints.NONE,
										new Insets(0, 0, 0, 0), 0, 0));
								jLabel4.setText("IBU:");
							}
							{
								jLabel6 = new JLabel();
								jPanel2.add(jLabel6, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
										GridBagConstraints.EAST, GridBagConstraints.NONE,
										new Insets(0, 0, 0, 0), 0, 0));
								jLabel6.setText("Colour:");
							}
							{
								jLabel7 = new JLabel();
								jPanel2.add(jLabel7, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
										GridBagConstraints.EAST, GridBagConstraints.NONE,
										new Insets(0, 0, 0, 0), 0, 0));
								jLabel7.setText("ABV:");
							}
						}
						{
							jScrollPane3 = new JScrollPane();
							pnlStyle.add(jScrollPane3);
							{
								txaStyles = new JTextArea();
								jScrollPane3.setViewportView(txaStyles);
								txaStyles.setText("Matched Styles");
							}
						}
						{
							sldMatch = new JSlider();
							pnlStyle.add(sldMatch);
						}
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
								tblMaltModel = new MaltTableModel(this);
								tblMalt = new JTable();
								jScrollPane1.setViewportView(tblMalt);
								tblMalt.setModel(tblMaltModel);
								tblMalt.setAutoCreateColumnsFromModel(false);
								tblMalt.addMouseListener(new MouseAdapter() {
									public void mouseClicked(MouseEvent evt) {
										int i = tblMalt.getSelectedRow();
										descriptionTextArea.setText(myRecipe.getMaltDescription(i));
									}
								});
								TableColumn maltColumn = tblMalt.getColumnModel().getColumn(0);

								// set up malt list combo
								JComboBox maltComboBox = new JComboBox();
								cmbMaltModel = new ComboModel();
								maltComboBox.setModel(cmbMaltModel);
								maltColumn.setCellEditor(new DefaultCellEditor(maltComboBox));
								maltComboBox.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent evt) {
										Fermentable f = (Fermentable) cmbMaltModel
												.getSelectedItem();
										int i = tblMalt.getSelectedRow();
										if (myRecipe != null && i != -1) {
											Fermentable f2 = (Fermentable) myRecipe
													.getFermentablesList().get(i);
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
								maltColumn = tblMalt.getColumnModel().getColumn(2);
								maltColumn.setCellEditor(new DefaultCellEditor(maltUnitsComboBox));
								maltUnitsComboBox.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent evt) {
										String u = (String) cmbMaltUnitsModel.getSelectedItem();
										int i = tblMalt.getSelectedRow();
										if (myRecipe != null && i != -1) {
											Fermentable f2 = (Fermentable) myRecipe
													.getFermentablesList().get(i);
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
						pnlMaltButtons.setLayout(pnlMaltButtonsLayout);
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
											tblMalt.updateUI();
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
											int i = tblMalt.getSelectedRow();
											myRecipe.delMalt(i);
											tblMalt.updateUI();
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
									new String[]{"1", "2","3","4","5","6","7","8","9"});
							tblHopsTotals = new JTable();
							pnlHops.add(tblHopsTotals, BorderLayout.SOUTH);
							tblHopsTotals.setModel(tblHopsTotalsModel);
							tblHopsTotals.setAutoCreateColumnsFromModel(false);

						}
						{
							jScrollPane2 = new JScrollPane();
							pnlHops.add(jScrollPane2, BorderLayout.CENTER);
							{
								tblHopsModel = new HopsTableModel(this);
								tblHops = new JTable();
								jScrollPane2.setViewportView(tblHops);
								tblHops.setModel(tblHopsModel);
								tblHops.addMouseListener(new MouseAdapter() {
									public void mouseClicked(MouseEvent evt) {										
										int i = tblHops.getSelectedRow();
										descriptionTextArea.setText(myRecipe.getHopDescription(i));
									}
								});
								TableColumn hopColumn = tblHops.getColumnModel().getColumn(0);
								JComboBox hopComboBox = new JComboBox();
								cmbHopsModel = new ComboModel();
								hopComboBox.setModel(cmbHopsModel);
								hopColumn.setCellEditor(new DefaultCellEditor(hopComboBox));
								hopComboBox.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent evt) {
										Hop h = (Hop) cmbHopsModel.getSelectedItem();
										int i = tblHops.getSelectedRow();
										if (myRecipe != null && i != -1) {
											Hop h2 = (Hop) myRecipe.getHopsList().get(i);
											h2.setAlpha(h.getAlpha());
											h2.setDescription(h.getDescription());
										}

									}
								});

								// set up hop units combo
								JComboBox hopsUnitsComboBox = new JComboBox();
								cmbHopsUnitsModel = new ComboModel();
								hopsUnitsComboBox.setModel(cmbHopsUnitsModel);
								hopColumn = tblHops.getColumnModel().getColumn(4);
								hopColumn.setCellEditor(new DefaultCellEditor(hopsUnitsComboBox));
								hopsUnitsComboBox.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent evt) {
										String u = (String) cmbHopsUnitsModel.getSelectedItem();
										int i = tblHops.getSelectedRow();
										if (myRecipe != null && i != -1) {
											Hop h = (Hop) myRecipe.getHopsList().get(i);
											h.setUnitsFull(u);
											myRecipe.calcHopsTotals();
											// tblHops.updateUI();
											displayRecipe();
											
										}

									}
								});
								
								// set up hop type combo
								String [] forms = {"Leaf","Pellet","Plug"};
								JComboBox hopsFormComboBox = new JComboBox(forms);
								hopColumn = tblHops.getColumnModel().getColumn(1);
								hopColumn.setCellEditor(new DefaultCellEditor(hopsFormComboBox));
								
//								 set up hop add combo
								String [] add = {"Boil","FWH","Dry", "Mash"};
								JComboBox hopsAddComboBox = new JComboBox(add);
								hopColumn = tblHops.getColumnModel().getColumn(5);
								hopColumn.setCellEditor(new DefaultCellEditor(hopsAddComboBox));

							}
						}
					}
					{
						pnlHopsButtons = new JPanel();
						FlowLayout pnlHopsButtonsLayout = new FlowLayout();
						pnlHopsButtonsLayout.setAlignment(FlowLayout.LEFT);
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
											tblHops.updateUI();
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
											int i = tblHops.getSelectedRow();
											myRecipe.delHop(i);
											tblHops.updateUI();
											displayRecipe();
										}
									}
								});
							}
						}
					}

				}
				{
					descriptionPanel = new JPanel();
					FlowLayout descriptionPanelLayout = new FlowLayout();
					descriptionPanel.setLayout(descriptionPanelLayout);
					pnlMain.add(descriptionPanel, new GridBagConstraints(
						1,
						0,
						1,
						3,
						0.0,
						0.0,
						GridBagConstraints.NORTH,
						GridBagConstraints.VERTICAL,
						new Insets(0, 0, 0, 0),
						0,
						0));
					descriptionPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
					{
						descriptionTextArea = new JTextArea();
						descriptionPanel.add(descriptionTextArea);
						descriptionTextArea.setText("Description");
						descriptionTextArea.setFont(new java.awt.Font("Dialog", 0, 10));
						descriptionTextArea.setEditable(false);
						descriptionTextArea.setFocusable(false);
						descriptionTextArea.setLineWrap(true);
						descriptionTextArea.setWrapStyleWord(true);
					}
				}
				{
					statusPanel = new JPanel();
					pnlMain.add(statusPanel, new GridBagConstraints(
						0,
						3,
						2,
						1,
						0.0,
						0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL,
						new Insets(0, 0, 0, 0),
						0,
						0));
					statusPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
				}
			}
			{
				jMenuBar1 = new JMenuBar();
				setJMenuBar(jMenuBar1);
				{
					jMenu3 = new JMenu();
					jMenuBar1.add(jMenu3);
					jMenu3.setText("File");
					{
						newFileMenuItem = new JMenuItem();
						jMenu3.add(newFileMenuItem);
						newFileMenuItem.setText("New");
						newFileMenuItem.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								// This is just a test right now to see that
								// stuff is changed.
								myRecipe = new Recipe();
								attachRecipeData();
								displayRecipe();

							}
						});
					}
					{
						openFileMenuItem = new JMenuItem();
						jMenu3.add(openFileMenuItem);
						openFileMenuItem.setText("Open");
						openFileMenuItem.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {								

								// Show open dialog; this method does
								// not return until the dialog is closed
								int returnVal = fileChooser.showOpenDialog(jMenuBar1);
								if (returnVal == JFileChooser.APPROVE_OPTION) {
									File file = fileChooser.getSelectedFile();

									System.out.print("Opening: " + file.getName() + ".\n");
									ImportXml imp = new ImportXml(file.toString());
									myRecipe = imp.handler.getRecipe();
									myRecipe.calcMaltTotals();
									myRecipe.calcHopsTotals();									
									myRecipe.mash.calcMashSchedule();
									attachRecipeData();
									displayRecipe();
								} else {
									System.out.print("Open command cancelled by user.\n");
								}

							}
						});
					}
					{
						saveMenuItem = new JMenuItem();
						jMenu3.add(saveMenuItem);
						saveMenuItem.setText("Save");
					}
					{
						saveAsMenuItem = new JMenuItem();
						jMenu3.add(saveAsMenuItem);
						saveAsMenuItem.setText("Save As ...");
						saveAsMenuItem.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt)  {
								if (DEBUG){
									// This is just a test right now to see that
									// stuff is changed.
									System.out.print(myRecipe.toXML());
								}
								
								// Show save dialog; this method does
								// not return until the dialog is closed
								int returnVal = fileChooser.showSaveDialog(jMenuBar1);
								if (returnVal == JFileChooser.APPROVE_OPTION) {
					                File file = fileChooser.getSelectedFile();
					                //This is where a real application would save the file.
					                try {
					                	FileWriter out = new FileWriter(file);
					                	out.write(myRecipe.toXML());
					                	out.close();
					                } catch (Exception e){
					                // TODO: handle io exception
					                }
					            } else {
					            	System.out.print("Save command cancelled by user.\n");
					            }

							}
						});
					}
					{
						exportMenu = new JMenu();
						jMenu3.add(exportMenu);
						exportMenu.setText("Export");
						{
							exportHTMLmenu = new JMenuItem();
							exportMenu.add(exportHTMLmenu);
							exportHTMLmenu.setText("HTML");
							exportHTMLmenu.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									// Show save dialog; this method does
									// not return until the dialog is closed	
									fileChooser.setSelectedFile(new File(myRecipe.getName()+".html"));
									
									int returnVal = fileChooser.showSaveDialog(jMenuBar1);
									if (returnVal == JFileChooser.APPROVE_OPTION) {
						                File file = fileChooser.getSelectedFile();						                
						                //This is where a real application would save the file.
						                try {
						                	saveAsHTML(file);
						                } catch (Exception e){
						                // TODO: handle io exception
						                }
						            } else {
						            	System.out.print("Save command cancelled by user.\n");
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
									fileChooser.setSelectedFile(new File(myRecipe.getName()+".txt"));
									int returnVal = fileChooser.showSaveDialog(jMenuBar1);
									if (returnVal == JFileChooser.APPROVE_OPTION) {
						                File file = fileChooser.getSelectedFile();
						                //This is where a real application would save the file.
						                try {
						                	FileWriter out = new FileWriter(file);
						                	out.write(myRecipe.toText());
						                	out.close();
						                } catch (Exception e){
						                // TODO: handle io exception
						                }
						            } else {
						            	System.out.print("Export text command cancelled by user.\n");
						            }			

								}
							});
						}
					}
					{
						jSeparator2 = new JSeparator();
						jMenu3.add(jSeparator2);
					}
					{
						exitMenuItem = new JMenuItem();
						jMenu3.add(exitMenuItem);
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
							    d.show();
							}
						});
						
						
					}
					{
						cutMenuItem = new JMenuItem();
						jMenu4.add(cutMenuItem);
						cutMenuItem.setText("Cut");
					}
					{
						copyMenuItem = new JMenuItem();
						jMenu4.add(copyMenuItem);
						copyMenuItem.setText("Copy");
					}
					{
						pasteMenuItem = new JMenuItem();
						jMenu4.add(pasteMenuItem);
						pasteMenuItem.setText("Paste");
					}
					{
						jSeparator1 = new JSeparator();
						jMenu4.add(jSeparator1);
					}
					{
						deleteMenuItem = new JMenuItem();
						jMenu4.add(deleteMenuItem);
						deleteMenuItem.setText("Delete");
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

	private void addColumnStateSupport()
	   {
	     TableColumnModel mtcm = tblMalt.getColumnModel();
	     TableColumnModel htcm = tblHops.getColumnModel();


	     //: listener that watches the width of a column
	     PropertyChangeListener mpcl = new PropertyChangeListener()
	     {
	       private int columnCount = tblMalt.getColumnCount();
	       private int[] width = new int[columnCount];

	       public void propertyChange(PropertyChangeEvent evt)
	       {	       	
	         if (evt.getPropertyName().equals("preferredWidth"))
	         {	      	         	
	         	TableColumnModel tcm = tblMalt.getColumnModel();
	         	TableColumnModel tcmt = tblMaltTotals.getColumnModel();
	         	int columnCount = tcm.getColumnCount();

	         	// for each column, get its width
		        for (int i = 0; i < columnCount; i++)
		        {
		         	tcmt.getColumn(i).setPreferredWidth(tcm.getColumn(i).getPreferredWidth());
		        }          	
	         }
	       }
	     };
	     
	     //: listener that watches the width of a column
	     PropertyChangeListener hpcl = new PropertyChangeListener()
	     {
	       private int columnCount = tblHops.getColumnCount();
	       private int[] width = new int[columnCount];

	       public void propertyChange(PropertyChangeEvent evt)
	       {	       	
	         if (evt.getPropertyName().equals("preferredWidth"))
	         {	      	         	
	         	TableColumnModel tcm = tblHops.getColumnModel();
	         	TableColumnModel tcmt = tblHopsTotals.getColumnModel();
	         	int columnCount = tcm.getColumnCount();

	         	// for each column, get its width
		        for (int i = 0; i < columnCount; i++)
		        {
		         	tcmt.getColumn(i).setPreferredWidth(tcm.getColumn(i).getPreferredWidth());
		        }          	
	         }
	       }
	     };

	     //: add the column width lister to each column
	     for (Enumeration e = mtcm.getColumns(); e.hasMoreElements();)
	     {
	       TableColumn tc = (TableColumn) e.nextElement();
	       tc.addPropertyChangeListener(mpcl);
	     }
	     
	     //: add the column width lister to each column
	     for (Enumeration e = htcm.getColumns(); e.hasMoreElements();)
	     {
	       TableColumn tc = (TableColumn) e.nextElement();
	       tc.addPropertyChangeListener(hpcl);
	     }
	   } 
}