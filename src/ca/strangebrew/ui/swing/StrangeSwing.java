/*
 * $Id: StrangeSwing.java,v 1.82 2008/03/11 17:20:20 andrew_avis Exp $ 
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

package ca.strangebrew.ui.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintStream;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
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
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import net.sf.wraplog.AbstractLogger;
import net.sf.wraplog.SystemLogger;
import ca.strangebrew.BrewCalcs;
import ca.strangebrew.Database;
import ca.strangebrew.Debug;
import ca.strangebrew.Fermentable;
import ca.strangebrew.Hop;
import ca.strangebrew.Ingredient;
import ca.strangebrew.OpenImport;
import ca.strangebrew.Options;
import ca.strangebrew.Quantity;
import ca.strangebrew.Recipe;
import ca.strangebrew.SBStringUtils;
import ca.strangebrew.Style;
import ca.strangebrew.XmlTransformer;
import ca.strangebrew.Yeast;
import ca.strangebrew.ui.swing.dialogs.AboutDialog;
import ca.strangebrew.ui.swing.dialogs.ConversionDialog;
import ca.strangebrew.ui.swing.dialogs.FindDialog;
import ca.strangebrew.ui.swing.dialogs.HydrometerToolDialog;
import ca.strangebrew.ui.swing.dialogs.MaltPercentDialog;
import ca.strangebrew.ui.swing.dialogs.NewIngrDialog;
import ca.strangebrew.ui.swing.dialogs.PotentialExtractCalcDialog;
import ca.strangebrew.ui.swing.dialogs.PreferencesDialog;
import ca.strangebrew.ui.swing.dialogs.PrintDialog;
import ca.strangebrew.ui.swing.dialogs.RefractometerDialog;
import ca.strangebrew.ui.swing.dialogs.ScaleRecipeDialog;

import com.michaelbaranov.microba.calendar.DatePicker;

import edu.stanford.ejalbert.BrowserLauncher;
import edu.stanford.ejalbert.BrowserLauncherRunner;
import edu.stanford.ejalbert.exception.BrowserLaunchingInitializingException;
import edu.stanford.ejalbert.exception.UnsupportedOperatingSystemException;

public class StrangeSwing extends javax.swing.JFrame implements ActionListener, FocusListener, WindowListener,
		ChangeListener {

	// The one and only StrangeSwing
	private static StrangeSwing instance = null;

	public String version = "2.0.2";
	public String edition = "Free";

	// Stuff that should be final
	public SBTable hopsTable;
	public SBTable maltTable;
	private HopsTableModel hopsTableModel;
	private MaltTableModel maltTableModel;
	private Options preferences;
	private AboutDialog aboutDlg;

	// Mutable data members
	public Recipe myRecipe;
	private String Costs;
	private File currentFile;
	private ImageIcon icon;
	private URL imgURL;

	// All the GUE elements are final, speeds app since the VM doesnt try and
	// garbage collect these
	final private MashPanel mashPanel = new MashPanel(myRecipe);;
	final private MiscPanel miscPanel = new MiscPanel(myRecipe);
	final private NotesPanel notesPanel = new NotesPanel();
	final private SettingsPanel settingsPanel = new SettingsPanel();
	final private StylePanel stylePanel = new StylePanel();
	final private WaterPanel waterPanel = new WaterPanel();
	final private FermentPanel fermentPanel = new FermentPanel();
	final private CarbonationPanel carbPanel = new CarbonationPanel();
	final private WaterTreatmentPanel waterTreatmentPanel = new WaterTreatmentPanel();

	final private JMenuItem aboutMenuItem = new JMenuItem();
	// TODO
	final private DefaultComboBoxModel alcMethodComboModel = new DefaultComboBoxModel(
			new String[] { "Volume", "Weight" });
	final private JComboBox alcMethodCombo = new JComboBox(alcMethodComboModel);
	final private JLabel alcMethodLabel = new JLabel();
	final private JPanel alcMethodPanel = new JPanel();
	final private SBCellEditor maltAmountEditor = new SBCellEditor(new JTextField());
	final private SBCellEditor hopAmountEditor = new SBCellEditor(new JTextField());
	final private SBCellEditor hopTimeEditor = new SBCellEditor(new JTextField());
	final private SBCellEditor hopAcidEditor = new SBCellEditor(new JTextField());
	final private JTextField boilMinText = new JTextField();
	final private JLabel boilTimeLable = new JLabel();
	final private JTextField brewerNameText = new JTextField();
	final private JButton btnAddHop = new JButton();
	final private JButton btnAddMalt = new JButton();
	final private JButton findButton = new JButton();
	final private JButton saveButton = new JButton();
	final private JToolBar mainToolBar = new JToolBar();
	final private JButton btnDelHop = new JButton();
	final private JButton btnDelMalt = new JButton();
	final private ComboModel<Hop> cmbHopsModel = new ComboModel<Hop>();
	final private ComboModel<String> cmbHopsUnitsModel = new ComboModel<String>();
	final private ComboModel<Fermentable> cmbMaltModel = new ComboModel<Fermentable>();
	final private ComboModel<String> cmbMaltUnitsModel = new ComboModel<String>();
	final private JComboBox cmbSizeUnits = new JComboBox();
	final private ComboModel<String> cmbSizeUnitsModel = new ComboModel<String>();
	final private JComboBox cmbStyle = new JComboBox();
	final private ComboModel<Style> cmbStyleModel = new ComboModel<Style>();
	final private JComboBox cmbYeast = new JComboBox();
	final private ComboModel<Yeast> cmbYeastModel = new ComboModel<Yeast>();
	final private DefaultComboBoxModel colourMethodComboModel = new DefaultComboBoxModel(BrewCalcs.colourMethods);
	final private JComboBox colourMethodCombo = new JComboBox(colourMethodComboModel);
	final private JPanel colourPanel = new JPanel();
	final private CostPanel costPanel = new CostPanel();
	final private JMenuItem deleteMenuItem = new JMenuItem();
	final private DilutionPanel dilutionPanel = new DilutionPanel();

	final private JMenuItem editPrefsMenuItem = new JMenuItem();
	final private JLabel evapLabel = new JLabel();
	final private JComboBox evapMethodCombo = new JComboBox();
	final private JTextField evapText = new JTextField();
	final private JMenuItem exitMenuItem = new JMenuItem();
	final private JMenuItem exportHTMLmenu = new JMenuItem();
	final private JMenu exportMenu = new JMenu();
	final private JMenuItem exportTextMenuItem = new JMenuItem();
	final private JFileChooser fileChooser = new JFileChooser();

	final private JMenu fileMenu = new JMenu();
	final private JLabel fileNameLabel = new JLabel();
	final private JPanel fileNamePanel = new JPanel();
	final private JMenuItem findFileMenuItem = new JMenuItem();
	final private JMenuItem helpMenuItem = new JMenuItem();
	final private JComboBox hopComboBox = new JComboBox();
	final private JComboBox hopsUnitsComboBox = new JComboBox();
	final private JComboBox hopsTotalUnitsComboBox = new JComboBox();
	final private ComboModel<String> hopsTotalUnitsComboModel = new ComboModel<String>();
	final private DefaultComboBoxModel ibuMethodComboModel = new DefaultComboBoxModel(BrewCalcs.ibuMethods);
	final private JComboBox ibuMethodCombo = new JComboBox(ibuMethodComboModel);
	final private JLabel ibuMethodLabel = new JLabel();
	final private JPanel ibuMethodPanel = new JPanel();
	final public JMenu editMenu = new JMenu();
	final private JMenu helpMenu = new JMenu();
	final public JMenuBar mainMenuBar = new JMenuBar();
	final private JPanel jPanel1 = new JPanel();
	final private JScrollPane jScrollPane1 = new JScrollPane();
	final private JScrollPane jScrollPane2 = new JScrollPane();
	final private JSeparator jSeparator1 = new JSeparator();
	final private JSeparator jSeparator2 = new JSeparator();
	final public JTabbedPane jTabbedPane1 = new JTabbedPane();
	final private JLabel lblAlc = new JLabel();

	final private JLabel lblAlcValue = new JLabel();
	final private JLabel lblAtten = new JLabel();
	final private JLabel lblBrewer = new JLabel();
	final private JLabel lblColour = new JLabel();

	final private JLabel lblColourValue = new JLabel();
	final private JLabel lblComments = new JLabel();
	final private JLabel lblDate = new JLabel();
	final private JLabel lblEffic = new JLabel();
	final private JLabel lblFG = new JLabel();
	final private JLabel lblIBU = new JLabel();
	final private JLabel lblIBUvalue = new JLabel();
	final private JLabel lblName = new JLabel();
	final private JLabel lblOG = new JLabel();
	final private JLabel lblFinalWortVol = new JLabel();
	final private JLabel lblPreBoil = new JLabel();
	final private JLabel lblSizeUnits = new JLabel();
	final private JLabel lblStyle = new JLabel();
	final private JLabel lblYeast = new JLabel();
	final private JComboBox maltComboBox = new JComboBox();
	final private JComboBox maltTotalUnitsComboBox = new JComboBox();
	final private ComboModel<String> maltTotalUnitsComboModel = new ComboModel<String>();
	final private JComboBox maltUnitsComboBox = new JComboBox();
	final private JMenu mnuTools = new JMenu();
	final private JMenuItem scaleRecipeMenuItem = new JMenuItem();
	final private JMenuItem extractPotentialMenuItem = new JMenuItem();
	final private JMenuItem maltPercentMenuItem = new JMenuItem();
	final private JMenuItem refractometerMenuItem = new JMenuItem();
	final private JMenuItem hydrometerToolMenuItem = new JMenuItem();
	final private JMenuItem conversionToolMenuItem = new JMenuItem();
	final private JMenuItem clipboardMenuItem = new JMenuItem("Copy to Clipboard");
	final private JMenuItem printMenuItem = new JMenuItem("Print...");

	final private JMenuItem newFileMenuItem = new JMenuItem();
	final private JMenuItem openFileMenuItem = new JMenuItem();
	final private JPanel pnlDetails = new JPanel();
	final private JPanel pnlHops = new JPanel();
	final private JPanel pnlHopsButtons = new JPanel();
	final private JPanel pnlMain = new JPanel();
	final private JPanel pnlMalt = new JPanel();
	final private JPanel pnlMaltButtons = new JPanel();
	final private JPanel pnlTables = new JPanel();
	final private JFormattedTextField finalWortVolText = new JFormattedTextField();
	final private JMenuItem saveAsMenuItem = new JMenuItem();

	final private JMenuItem saveMenuItem = new JMenuItem();
	final private JScrollPane scpComments = new JScrollPane();
	// private JScrollPane scrMalts;
	final private JSpinner spnAtten = new JSpinner();
	final private JSpinner spnEffic = new JSpinner();
	final private JSpinner spnFG = new JSpinner();
	final private JSpinner spnOG = new JSpinner();
	final private JPanel statusPanel = new JPanel();
	final private JTable tblHopsTotals = new JTable();
	final private DefaultTableModel tblHopsTotalsModel = new DefaultTableModel(new String[][] { { "" } }, new String[] {
			"1", "2", "3", "4", "5", "6", "7", "8", "9" });
	final private DefaultTableModel tblMaltTotalsModel = new DefaultTableModel(new String[][] { { "" } }, new String[] {
			"S", "M", "Malt", "Amount", "Units", "Points", "Lov", "Cost/U", "%" });
	final private JTable tblMaltTotals = new JTable();

	final private JToolBar tlbHops = new JToolBar();
	final private JToolBar tlbMalt = new JToolBar();

	final private JTextArea txtComments = new JTextArea();
	// private JFormattedTextField txtDate;
	final private DatePicker txtDate = new DatePicker();
	final private JTextField txtName = new JTextField();
	final private JFormattedTextField preBoilText = new JFormattedTextField();
	final private JButton printButton = new JButton();
	final private JButton copyButton = new JButton();

	final public Database DB;

	private boolean dontUpdate = false;

	// an object that you give to other gui objects so that they can set things
	// on the main SB GUI
	// used by style and settings panels
	// sbn.displayRecipe() causes infinite loops as it trigers updateActions all
	// over which call back into displayRecipe()
	// Notifer obsolete on these with singelton instance
	public void hopsUpdateUI() {
		hopsTable.updateUI();
	}

	public void maltUpdateUI() {
		maltTable.updateUI();
	}

	public void setStyle(Style s) {
		cmbStyleModel.addOrInsert(s);
	}

	public class sbFileFilter extends FileFilter {

		private String description = "";
		private String[] extensions = { "xml" };

		public sbFileFilter(String[] s, String desc) {
			extensions = s;
			description = desc;
		}

		public boolean accept(File f) {
			String ext = null;
			String s = f.getName();
			int i = s.lastIndexOf('.');

			if (f.isDirectory())
				return true;

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

		public void setDescription(String d) {
			description = d;
		}
	}

	/**
	 * Auto-generated main method to display this JFrame
	 */
	public static void main(String[] args) {
		final StrangeSwing inst = new StrangeSwing();
		inst.setVisible(true);
		if (args.length > 0) {
			// Are we in debug mode?
			if (args[0].equals("-d")) {
				Debug.set(true);
			}
		}

	}

	{
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.addWindowListener(this);
	}

	/*
	 * If you wanted to set a LAF, you'd do this: { try {
	 * UIManager.setLookAndFeel(new Plastic3DLookAndFeel()); } catch (Exception
	 * e) {} }
	 */

	public StrangeSwing() {
		super();

		preferences = Options.getInstance();

		// There has *got* to be a better way to do this: (yeah, singleton it
		// for starters)
		// Later this singleton should be propagated though out such that the
		// "setList()"
		// functions are not needed. Each Model can call up the one and only
		// Database instance
		// at any time
		// On the same note, StrangeSwing could be singeltoned which would allow
		// all the sub
		// windows to pull up the public data stored here
		// TODO
		DB = Database.getInstance();
		String path = SBStringUtils.getAppPath("data");
		Debug.print("DB Path: " + path);

		// let's check that the path is good, if not, ask for a better one
		// if no path provided, exit
		File maltFile = new File(path, "malts.csv");
		File hopsFile = new File(path, "hops.csv");
		if (!maltFile.exists() || !hopsFile.exists()) {
			Object[] options = { "Yes", "CANCEL" };
			int choice = JOptionPane.showOptionDialog(null,
					"Ingredient databases not found.\n For more information, see the release notes.\n "
							+ "Do you want to select a new location?  (Select Cancel to exit).", "Databases Not Found",
					JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
			if (choice == 0) {
				fileChooser.resetChoosableFileFilters();
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				String[] ext = { "csv" };
				String desc = "StrangBrew Databases";
				sbFileFilter openFileFilter = new sbFileFilter(ext, desc);
				fileChooser.setFileFilter(openFileFilter);
				int returnVal = fileChooser.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					path = file.getPath();
					Debug.print("Updated DB path: " + path + "\n");
					fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

				} else
					System.exit(1);
			} else
				System.exit(1);
		}
		DB.readDB(path);

		cmbStyleModel.setList(DB.styleDB);
		cmbYeastModel.setList(DB.yeastDB);
		cmbMaltModel.setList(DB.fermDB);
		cmbHopsModel.setList(DB.hopsDB);
		carbPanel.setList(DB.primeSugarDB);
		waterTreatmentPanel.setList(DB.waterDB);

		initGUI();

		cmbSizeUnitsModel.setList(Quantity.getListofUnits("vol"));
		cmbMaltUnitsModel.setList(Quantity.getListofUnits("weight"));
		cmbHopsUnitsModel.setList(Quantity.getListofUnits("weight"));

		path = SBStringUtils.getAppPath("recipes");
		Debug.print("Recipes path:" + path);

		fileChooser.setCurrentDirectory(new File(path));

		// link malt table and totals:
		addColumnWidthListeners();

		// set up tabs:
		miscPanel.setList(DB.miscDB);
		stylePanel.setList(DB.styleDB);

		// does this speed up load?
		addListeners();

		myRecipe = new Recipe();
		currentFile = null;
		myRecipe.setDirty(false);
		attachRecipeData();
		displayRecipe();
	}

	public static StrangeSwing getInstance() {
		if (instance == null) {
			instance = new StrangeSwing();
		}

		return instance;
	}

	public void attachRecipeData() {
		// this method attaches data from the recipe to the tables
		// and comboboxes
		// use whenever the Recipe changes
		cmbStyleModel.addOrInsert(myRecipe.getStyleObj());
		cmbYeastModel.addOrInsert(myRecipe.getYeastObj());
		cmbSizeUnitsModel.addOrInsert(myRecipe.getVolUnits());
		maltTableModel.setData(myRecipe);
		hopsTableModel.setData(myRecipe);
		miscPanel.setData(myRecipe);
		notesPanel.setData(myRecipe);
		stylePanel.setData(myRecipe);
		dilutionPanel.setData(myRecipe);
		mashPanel.setData(myRecipe);
		waterPanel.setData(myRecipe);
		costPanel.setData(myRecipe);
		settingsPanel.setData(myRecipe);
		fermentPanel.setData(myRecipe);
		carbPanel.setData(myRecipe);
		waterTreatmentPanel.setData(myRecipe);
		maltTable.updateUI();
		hopsTable.updateUI();

		alcMethodComboModel.setSelectedItem(myRecipe.getAlcMethod());
		ibuMethodComboModel.setSelectedItem(myRecipe.getIBUMethod());
		colourMethodCombo.setSelectedItem(myRecipe.getColourMethod());
		evapMethodCombo.setSelectedItem(myRecipe.getEvapMethod());

		// set the yeast and style descriptions:
		Yeast y = (Yeast) cmbYeastModel.getSelectedItem();
		String st = SBStringUtils.multiLineToolTip(40, y.getDescription());
		cmbYeast.setToolTipText(st);
		Style s = (Style) cmbStyleModel.getSelectedItem();
		st = SBStringUtils.multiLineToolTip(40, s.getDescription());
		cmbStyle.setToolTipText(st);

	}

	public void updateUI() {
		txtDate.setLocale(preferences.getLocale());
	}

	public void displayRecipe() {
		if (myRecipe == null)
			return;

		dontUpdate = true;
		txtName.setText(myRecipe.getName());
		brewerNameText.setText(myRecipe.getBrewer());
		preBoilText.setValue(SBStringUtils.format(myRecipe.getPreBoilVol(myRecipe.getVolUnits()), 2));
		lblSizeUnits.setText(myRecipe.getVolUnits());
		finalWortVolText.setValue(SBStringUtils.format(myRecipe.getFinalWortVol(myRecipe.getVolUnits()), 2));
		boilMinText.setText(SBStringUtils.format(myRecipe.getBoilMinutes(), 0));
		evapText.setText(SBStringUtils.format(myRecipe.getEvap(), 1));
		spnEffic.setValue(new Double(myRecipe.getEfficiency()));
		spnAtten.setValue(new Double(myRecipe.getAttenuation()));
		spnOG.setValue(new Double(myRecipe.getEstOg()));
		spnFG.setValue(new Double(myRecipe.getEstFg()));
		txtComments.setText(myRecipe.getComments());
		lblIBUvalue.setText(SBStringUtils.format(myRecipe.getIbu(), 1));
		lblColourValue.setText(SBStringUtils.format(myRecipe.getColour(), 1));
		lblAlcValue.setText(SBStringUtils.format(myRecipe.getAlcohol(), 1));
		try {
			txtDate.setDate(myRecipe.getCreated().getTime());
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
		// setText(SBStringUtils.dateFormat1.format(myRecipe.getCreated().getTime()));
		Costs = SBStringUtils.myNF.format(myRecipe.getTotalMaltCost());
		tblMaltTotalsModel.setDataVector(new String[][] { { "", "", "Totals:",
				"" + SBStringUtils.format(myRecipe.getTotalMalt(), 1), myRecipe.getMaltUnits(),
				"" + SBStringUtils.format(myRecipe.getEstOg(), 3), "" + SBStringUtils.format(myRecipe.getColour(), 1),
				Costs, "100" } }, new String[] { "", "", "", "", "", "", "", "", "" });

		Costs = SBStringUtils.myNF.format(myRecipe.getTotalHopsCost());
		tblHopsTotalsModel.setDataVector(new String[][] { { "Totals:", "", "",
				"" + SBStringUtils.format(myRecipe.getTotalHops(), 1), myRecipe.getHopUnits(), "", "",
				"" + SBStringUtils.format(myRecipe.getIbu(), 1), Costs } }, new String[] { "", "", "", "", "", "", "",
				"", "" });

		String fileName = "not saved";
		if (currentFile != null) {
			fileName = currentFile.getName();
		}

		fileNameLabel.setText("File: " + fileName);
		ibuMethodLabel.setText("IBU method: " + myRecipe.getIBUMethod());
		alcMethodLabel.setText("Alc method: " + myRecipe.getAlcMethod());

		double colour = myRecipe.getColour(BrewCalcs.SRM);

		if (preferences.getProperty("optRGBMethod").equals("1"))
			colourPanel.setBackground(BrewCalcs.calcRGB(1, colour, preferences.getIProperty("optRed"), preferences
					.getIProperty("optGreen"), preferences.getIProperty("optBlue"), preferences
					.getIProperty("optAlpha")));
		else
			colourPanel.setBackground(BrewCalcs.calcRGB(2, colour, preferences.getIProperty("optRed"), preferences
					.getIProperty("optGreen"), preferences.getIProperty("optBlue"), preferences
					.getIProperty("optAlpha")));

		// update the panels - but not here! done on the fly with event from tab
		// pane to help stop infinte recursion

		// Setup title bar
		String title = "StrangeBrew " + version + " " + edition;
		String file = "";
		String dirty = "";

		if (currentFile != null) {
			file = " - [" + currentFile.getAbsolutePath();
		} else {
			file = " - [<new>";
		}

		if (myRecipe.getDirty()) {
			dirty = " *]";
		} else {
			dirty = "]";
		}

		this.setTitle(title + file + dirty);

		dontUpdate = false;
	}

	public void focusGained(FocusEvent e) {
		// do nothing, we don't need this event
	}

	public void focusLost(FocusEvent e) {
		Object o = e.getSource();
		ActionEvent evt = new ActionEvent(o, 1, "");
		actionPerformed(evt);

		if (o == txtComments) {
			if (!txtComments.getText().equals(myRecipe.getComments())) {
				myRecipe.setComments(txtComments.getText());
			}
		}
	}

	public void saveAsHTML(File f, String xslt, String options) throws Exception {
		// save file as xml, then transform it to html
		File tmp = new File("tmp.xml");
		FileWriter out = new FileWriter(tmp);
		out.write(myRecipe.toXML(options));
		out.close();

		// find the xslt stylesheet in the classpath
		// URL xsltUrl = getClass().getClassLoader().getResource(xslt);
		String path = SBStringUtils.getAppPath("data");
		File xsltFile = new File(path, xslt);

		FileOutputStream output = new FileOutputStream(f);

		XmlTransformer.writeStream(tmp, xsltFile, output);
		// tmp.delete();

	}

	public void setRecipe(Recipe r) {
		setRecipe(r, currentFile);
	}

	public void setRecipe(Recipe r, File f) {
		currentFile = f;
		myRecipe = r;
		myRecipe.setVersion(version);
		attachRecipeData();
		displayRecipe();
	}

	public void windowActivated(WindowEvent e) {
	}

	public void windowClosed(WindowEvent e) {
	}

	// This main window is closing, prompt to save file:
	public void windowClosing(WindowEvent e) {
		// displayMessage("WindowListener method called: windowClosing.");

		if (myRecipe.getDirty()) {
			int choice = 1;

			choice = JOptionPane.showConfirmDialog(null, "Do you wish to save the current recipe?", "Save Recipe?",
					JOptionPane.YES_NO_OPTION);

			if (choice == 0)
				saveAs();
		}

		// save the window size and location:
		preferences.setIProperty("winHeight", this.getHeight());
		preferences.setIProperty("winWidth", this.getWidth());
		preferences.setIProperty("winX", this.getX());
		preferences.setIProperty("winY", this.getY());
		maltTable.saveColumnWidths(preferences);
		hopsTable.saveColumnWidths(preferences);

		preferences.saveProperties();
	}

	public void windowDeactivated(WindowEvent e) {
	}

	public void windowDeiconified(WindowEvent e) {
	}

	public void windowIconified(WindowEvent e) {
	}

	public void windowOpened(WindowEvent e) {
	}

	private void addColumnWidthListeners() {
		TableColumnModel mtcm = maltTable.getColumnModel();
		TableColumnModel htcm = hopsTable.getColumnModel();

		// : listener that watches the width of a column
		PropertyChangeListener mpcl = new PropertyChangeListener() {
			// private int columnCount = maltTable.getColumnCount();
			// private int[] width = new int[columnCount];

			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals("preferredWidth")) {
					TableColumnModel tcm = maltTable.getColumnModel();
					TableColumnModel tcmt = tblMaltTotals.getColumnModel();
					int colCount = tcm.getColumnCount();

					// for each column, get its width
					for (int i = 0; i < colCount; i++) {
						tcmt.getColumn(i).setPreferredWidth(tcm.getColumn(i).getPreferredWidth());
					}
				}
			}
		};

		// : listener that watches the width of a column
		PropertyChangeListener hpcl = new PropertyChangeListener() {
			// private int columnCount = hopsTable.getColumnCount();
			// private int[] width = new int[columnCount];

			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals("preferredWidth")) {
					TableColumnModel tcm = hopsTable.getColumnModel();
					TableColumnModel tcmt = tblHopsTotals.getColumnModel();
					int colCount = tcm.getColumnCount();

					// for each column, get its width
					for (int i = 0; i < colCount; i++) {
						tcmt.getColumn(i).setPreferredWidth(tcm.getColumn(i).getPreferredWidth());
					}
				}
			}
		};

		// : add the column width lister to each column
		for (Enumeration e = mtcm.getColumns(); e.hasMoreElements();) {
			TableColumn tc = (TableColumn) e.nextElement();
			tc.addPropertyChangeListener(mpcl);
		}

		// : add the column width lister to each column
		for (Enumeration e = htcm.getColumns(); e.hasMoreElements();) {
			TableColumn tc = (TableColumn) e.nextElement();
			tc.addPropertyChangeListener(hpcl);
		}

		// set preferred widths of the malt table
		maltTable.setColumnWidths(preferences);

		// now do the same for the hops table
		hopsTable.setColumnWidths(preferences);
	}

	// add the listeners *after* all the data has been attached to speed
	// up startup
	private void addListeners() {
		txtName.addActionListener(this);
		txtName.addFocusListener(this);
		brewerNameText.addFocusListener(this);
		brewerNameText.addActionListener(this);
		// txtDate.addFocusListener(this);
		// txtDate.addActionListener(this);
		preBoilText.addFocusListener(this);
		preBoilText.addActionListener(this);
		finalWortVolText.addFocusListener(this);
		finalWortVolText.addActionListener(this);
		boilMinText.addFocusListener(this);
		boilMinText.addActionListener(this);
		evapText.addFocusListener(this);
		evapText.addActionListener(this);
		txtDate.addActionListener(this);

		cmbStyle.addActionListener(this);

		cmbSizeUnits.addActionListener(this);
		btnAddMalt.addActionListener(this);
		btnDelMalt.addActionListener(this);
		maltUnitsComboBox.addActionListener(this);
		maltTotalUnitsComboBox.addActionListener(this);
		hopComboBox.addActionListener(this);
		hopsUnitsComboBox.addActionListener(this);
		hopsTotalUnitsComboBox.addActionListener(this);

		alcMethodCombo.addActionListener(this);
		ibuMethodCombo.addActionListener(this);
		colourMethodCombo.addActionListener(this);
		evapMethodCombo.addActionListener(this);

		saveButton.addActionListener(this);
		findButton.addActionListener(this);
		printButton.addActionListener(this);
		copyButton.addActionListener(this);
		aboutMenuItem.addActionListener(this);
		helpMenuItem.addActionListener(this);
		conversionToolMenuItem.addActionListener(this);
		hydrometerToolMenuItem.addActionListener(this);
		extractPotentialMenuItem.addActionListener(this);
		refractometerMenuItem.addActionListener(this);
		maltPercentMenuItem.addActionListener(this);
		scaleRecipeMenuItem.addActionListener(this);
		editPrefsMenuItem.addActionListener(this);
		exitMenuItem.addActionListener(this);
		printMenuItem.addActionListener(this);
		clipboardMenuItem.addActionListener(this);
		exportTextMenuItem.addActionListener(this);
		exportHTMLmenu.addActionListener(this);
		saveAsMenuItem.addActionListener(this);
		saveMenuItem.addActionListener(this);
		findFileMenuItem.addActionListener(this);
		openFileMenuItem.addActionListener(this);
		newFileMenuItem.addActionListener(this);

	}

	private void initGUI() {
		try {

			// restore the saved size and location:
			this.setSize(preferences.getIProperty("winWidth"), preferences.getIProperty("winHeight"));
			this.setLocation(preferences.getIProperty("winX"), preferences.getIProperty("winY"));
			imgURL = getClass().getClassLoader().getResource("ca/strangebrew/icons/sb2.gif");
			icon = new ImageIcon(imgURL);
			this.setIconImage(icon.getImage());
			this.setTitle("StrangeBrew " + version + " " + edition + " - [<new>]");
			this.addWindowListener(new WindowAdapter() {
				public void windowClosed(WindowEvent evt) {
					System.exit(1);
				}
			});

			{
				GridBagLayout jPanel2Layout = new GridBagLayout();
				jPanel2Layout.columnWeights = new double[] { 0.1 };
				jPanel2Layout.columnWidths = new int[] { 7 };
				jPanel2Layout.rowWeights = new double[] { 0.1, 0.1, 0.9, 0.1 };
				// jPanel2Layout.rowHeights = new int[] { 7, 7, 7, 7 };
				pnlMain.setLayout(jPanel2Layout);
				this.getContentPane().add(pnlMain, BorderLayout.CENTER);
				{
					pnlMain.add(jTabbedPane1, new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER,
							GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					{
						GridBagLayout pnlDetailsLayout = new GridBagLayout();
						pnlDetailsLayout.columnWeights = new double[] { 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1,
								0.1 };
						pnlDetailsLayout.columnWidths = new int[] { 7, 7, 7, 7, 7, 7, 7, 7, 7, 7 };
						pnlDetailsLayout.rowWeights = new double[] { 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1 };
						pnlDetailsLayout.rowHeights = new int[] { 7, 7, 7, 7, 7, 7, 7 };
						pnlDetails.setLayout(pnlDetailsLayout);
						pnlDetails.setPreferredSize(new java.awt.Dimension(20, 16));
						{
							pnlDetails.add(lblBrewer, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
									GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
							lblBrewer.setText("Brewer:");
						}
						{
							pnlDetails.add(brewerNameText, new GridBagConstraints(1, 0, 2, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
									0, 0));
							brewerNameText.setPreferredSize(new java.awt.Dimension(69, 20));
							brewerNameText.setText("Brewer");

						}
						{
							pnlDetails.add(lblDate, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
									GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
							lblDate.setText("Date:");
						}
						{
							pnlDetails.add(lblStyle, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
									GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
							lblStyle.setText("Style:");
						}
						{
							pnlDetails.add(lblYeast, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
									GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
							lblYeast.setText("Yeast:");
						}
						{
							pnlDetails.add(lblPreBoil, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
									GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
							lblPreBoil.setText("Pre boil:");
						}
						{
							pnlDetails.add(lblFinalWortVol, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0,
									GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
							lblFinalWortVol.setText("Final Volume:");
						}
						{
							pnlDetails.add(lblEffic, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
									GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
							lblEffic.setText("Effic:");
							lblEffic.setPreferredSize(new java.awt.Dimension(31, 14));
						}
						{
							pnlDetails.add(lblAtten, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0,
									GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
							lblAtten.setText("Atten:");
							lblAtten.setPreferredSize(new java.awt.Dimension(34, 14));
						}
						{
							pnlDetails.add(lblOG, new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
									GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
							lblOG.setText("OG:");
						}
						{
							pnlDetails.add(lblFG, new GridBagConstraints(5, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
									GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
							lblFG.setText("FG:");
						}
						{
							pnlDetails.add(lblIBU, new GridBagConstraints(7, 1, 1, 1, 0.0, 0.0,
									GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
							lblIBU.setText("IBU:");
						}
						{
							pnlDetails.add(lblAlc, new GridBagConstraints(7, 0, 1, 1, 0.0, 0.0,
									GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
							lblAlc.setText("%Alc:");
						}
						{
							pnlDetails.add(lblColour, new GridBagConstraints(7, 2, 1, 1, 0.0, 0.0,
									GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
							lblColour.setText("Colour:");
						}
						{
							// txtDate = new JFormattedTextField();
							pnlDetails.add(txtDate, new GridBagConstraints(1, 1, 2, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
									0, 0));
							// txtDate.setText("Date");
							txtDate.setPreferredSize(new java.awt.Dimension(73, 20));
							txtDate.setDateStyle(DateFormat.SHORT);
							txtDate.setLocale(preferences.getLocale());
						}
						{
							SmartComboBox.enable(cmbStyle);
							pnlDetails.add(cmbStyle, new GridBagConstraints(1, 2, 5, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
									0, 0));
							cmbStyle.setModel(cmbStyleModel);
							cmbStyle.setMaximumSize(new java.awt.Dimension(100, 32767));
							cmbStyle.setPreferredSize(new java.awt.Dimension(190, 20));

						}
						{
							pnlDetails.add(preBoilText, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0,
									GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0,
									0));
							preBoilText.setText("Pre Boil");
							preBoilText.setPreferredSize(new java.awt.Dimension(37, 20));

						}
						{
							pnlDetails.add(finalWortVolText, new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0,
									GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0,
									0));
							finalWortVolText.setText("FinalVol");
							finalWortVolText.setPreferredSize(new java.awt.Dimension(46, 20));

						}
						{
							pnlDetails.add(lblComments, new GridBagConstraints(6, 4, 1, 1, 0.0, 0.0,
									GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
							lblComments.setText("Comments:");
						}

						{
							SpinnerNumberModel spnEfficModel = new SpinnerNumberModel(75.0, 0.0, 100.0, 1.0);
							pnlDetails.add(spnEffic, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
									0, 0));
							spnEffic.setModel(spnEfficModel);
							spnEffic.setMaximumSize(new java.awt.Dimension(70, 32767));
							spnEffic.addChangeListener(this);
							spnEffic.setEditor(new JSpinner.NumberEditor(spnEffic, "00.#"));
							spnEffic.getEditor().setPreferredSize(new java.awt.Dimension(28, 16));
							spnEffic.setPreferredSize(new java.awt.Dimension(53, 18));
						}
						{
							SpinnerNumberModel spnAttenModel = new SpinnerNumberModel(75.0, 0.0, 100.0, 1.0);
							pnlDetails.add(spnAtten, new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
									0, 0));
							spnAtten.setModel(spnAttenModel);
							spnAtten.addChangeListener(this);
							spnAtten.setEditor(new JSpinner.NumberEditor(spnAtten, "00.#"));
							spnAtten.setPreferredSize(new java.awt.Dimension(49, 20));
						}
						{
							SpinnerNumberModel spnOgModel = new SpinnerNumberModel(1.000, 0.900, 2.000, 0.001);
							pnlDetails.add(spnOG, new GridBagConstraints(6, 0, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
									0, 0));
							spnOG.setModel(spnOgModel);
							spnOG.addChangeListener(this);
							spnOG.setEditor(new JSpinner.NumberEditor(spnOG, "0.000"));
							spnOG.getEditor().setPreferredSize(new java.awt.Dimension(20, 16));
							spnOG.setPreferredSize(new java.awt.Dimension(67, 18));
						}
						{
							SpinnerNumberModel spnFgModel = new SpinnerNumberModel(1.000, 0.900, 2.000, 0.001);
							pnlDetails.add(spnFG, new GridBagConstraints(6, 1, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
									0, 0));
							spnFG.setModel(spnFgModel);
							spnFG.setEditor(new JSpinner.NumberEditor(spnFG, "0.000"));
							spnFG.setPreferredSize(new java.awt.Dimension(69, 20));
							spnFG.addChangeListener(this);
						}
						{
							pnlDetails.add(lblIBUvalue, new GridBagConstraints(8, 1, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
							lblIBUvalue.setText("IBUs");
						}
						{
							pnlDetails.add(lblColourValue, new GridBagConstraints(8, 2, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
							lblColourValue.setText("Colour");
						}
						{
							pnlDetails.add(lblAlcValue, new GridBagConstraints(8, 0, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
							lblAlcValue.setText("Alc");
						}
						{
							pnlDetails.add(scpComments, new GridBagConstraints(7, 4, 3, 2, 0.0, 0.0,
									GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
							{
								scpComments.setViewportView(txtComments);
								txtComments.setText("Comments");
								txtComments.setWrapStyleWord(true);
								// txtComments.setPreferredSize(new
								// java.awt.Dimension(117, 42));
								txtComments.setLineWrap(true);
								scpComments.setPreferredSize(new java.awt.Dimension(263, 40));
								txtComments.addFocusListener(this);
							}
						}
						{
							// Install the custom key selection manager
							SmartComboBox.enable(cmbYeast);
							pnlDetails.add(cmbYeast, new GridBagConstraints(1, 3, 5, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
									0, 0));
							cmbYeast.setModel(cmbYeastModel);
							cmbYeast.setPreferredSize(new java.awt.Dimension(193, 20));

						}
						{
							SmartComboBox.enable(cmbSizeUnits);
							pnlDetails.add(cmbSizeUnits, new GridBagConstraints(2, 4, 2, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
									0, 0));
							cmbSizeUnits.setModel(cmbSizeUnitsModel);

						}
						{
							pnlDetails.add(lblSizeUnits, new GridBagConstraints(2, 5, 2, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
									0, 0));
							lblSizeUnits.setText("Size Units");
						}
						{
							pnlDetails.add(boilTimeLable, new GridBagConstraints(4, 4, 1, 1, 0.0, 0.0,
									GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
							boilTimeLable.setText("Boil Min:");
						}
						{
							pnlDetails.add(evapLabel, new GridBagConstraints(4, 5, 1, 1, 0.0, 0.0,
									GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
							evapLabel.setText("Evap/hr:");
						}
						{
							pnlDetails.add(boilMinText, new GridBagConstraints(5, 4, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
									0, 0));
							boilMinText.setText("60");
							boilMinText.setPreferredSize(new java.awt.Dimension(22, 20));

						}
						{
							pnlDetails.add(evapText, new GridBagConstraints(5, 5, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
									0, 0));
							evapText.setText("4");
							evapText.setPreferredSize(new java.awt.Dimension(23, 20));
						}
						{
							SmartComboBox.enable(alcMethodCombo);
							pnlDetails.add(alcMethodCombo, new GridBagConstraints(9, 0, 1, 1, 0.0, 0.0,
									GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0,
									0));
							alcMethodCombo.setPreferredSize(new java.awt.Dimension(71, 20));

						}
						{
							SmartComboBox.enable(ibuMethodCombo);
							pnlDetails.add(ibuMethodCombo, new GridBagConstraints(9, 1, 1, 1, 0.0, 0.0,
									GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0,
									0));
							ibuMethodCombo.setPreferredSize(new java.awt.Dimension(72, 20));

						}
						{
							SmartComboBox.enable(colourMethodCombo);
							pnlDetails.add(colourMethodCombo, new GridBagConstraints(9, 2, 1, 1, 0.0, 0.0,
									GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0,
									0));
							colourMethodCombo.setPreferredSize(new java.awt.Dimension(52, 20));

						}

						ComboBoxModel evapMethodComboModel = new DefaultComboBoxModel(new String[] { "Constant",
								"Percent" });
						{
							pnlMain.add(jPanel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
									GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
							FlowLayout jPanel1Layout = new FlowLayout();
							jPanel1Layout.setAlignment(FlowLayout.LEFT);
							jPanel1.setLayout(jPanel1Layout);

							getContentPane().add(mainToolBar, BorderLayout.NORTH);
							mainToolBar.setFloatable(false);
							mainToolBar.setRollover(true);

							mainToolBar.add(saveButton);
							saveButton.setMnemonic(java.awt.event.KeyEvent.VK_S);
							saveButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource(
									"ca/strangebrew/icons/save.gif")));
							saveButton.setToolTipText("Save Recipe");

							mainToolBar.add(findButton);
							findButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource(
									"ca/strangebrew/icons/find.gif")));
							findButton.setToolTipText("Find Recipes");

							mainToolBar.add(printButton);
							printButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource(
									"ca/strangebrew/icons/print.gif")));
							printButton.setToolTipText("Print Recipe");

							mainToolBar.add(copyButton);
							copyButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource(
									"ca/strangebrew/icons/copy.gif")));
							copyButton.setToolTipText("Copy to clipboard");

							{
								jPanel1.add(lblName);
								lblName.setText("Name:");
							}
							{
								jPanel1.add(txtName);
								txtName.setText("Name");
								txtName.setPreferredSize(new java.awt.Dimension(297, 20));

							}

						}

						SmartComboBox.enable(evapMethodCombo);
						pnlDetails.add(evapMethodCombo, new GridBagConstraints(6, 5, 1, 1, 0.0, 0.0,
								GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						evapMethodCombo.setModel(evapMethodComboModel);
						evapMethodCombo.setPreferredSize(new java.awt.Dimension(64, 20));

						pnlDetails.add(colourPanel, new GridBagConstraints(9, 3, 1, 1, 0.0, 0.0,
								GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
						colourPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
						colourPanel.setPreferredSize(new java.awt.Dimension(93, 32));

					}
					{
						jTabbedPane1.addTab("Details", null, pnlDetails, null);
						jTabbedPane1.addTab("Style", null, stylePanel, null);
						jTabbedPane1.addTab("Misc", null, miscPanel, null);
						jTabbedPane1.addTab("Notes", null, notesPanel, null);
						jTabbedPane1.addTab("Dilution", null, dilutionPanel, null);
						jTabbedPane1.addTab("Mash", null, mashPanel, null);
						jTabbedPane1.addTab("Water Treatment", null, waterTreatmentPanel, null);
						jTabbedPane1.addTab("Ferment", null, fermentPanel, null);
						jTabbedPane1.addTab("Carbonation", null, carbPanel, null);
						jTabbedPane1.addTab("Water", null, waterPanel, null);
						jTabbedPane1.addTab("Cost", null, costPanel, null);
						jTabbedPane1.addTab("Settings", null, settingsPanel, null);
						jTabbedPane1.addChangeListener(this);
					}
				}
				{
					BoxLayout pnlMaltsLayout = new BoxLayout(pnlTables, javax.swing.BoxLayout.Y_AXIS);
					pnlMain.add(pnlTables, new GridBagConstraints(0, 2, 1, 1, 0.5, 0.5, GridBagConstraints.CENTER,
							GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

					pnlTables.setLayout(pnlMaltsLayout);
					{
						pnlTables.add(pnlMalt);
						BorderLayout pnlMaltLayout1 = new BorderLayout();
						pnlMalt.setBorder(BorderFactory.createTitledBorder(new LineBorder(new java.awt.Color(0, 0, 0),
								1, false), "Fermentables", TitledBorder.LEADING, TitledBorder.TOP, new java.awt.Font(
								"Dialog", 1, 12), new java.awt.Color(51, 51, 51)));
						pnlMalt.setLayout(pnlMaltLayout1);
						{
							pnlMalt.add(jScrollPane1, BorderLayout.CENTER);
							{
								maltTableModel = new MaltTableModel(this);
								maltTable = new SBTable("maltTable") {
									public String getToolTipText(MouseEvent e) {
										java.awt.Point p = e.getPoint();
										int rowIndex = rowAtPoint(p);
										return SBStringUtils.multiLineToolTip(40, maltTableModel
												.getDescriptionAt(rowIndex));

									}
								};

								jScrollPane1.setViewportView(maltTable);
								maltTable.setModel(maltTableModel);
								// maltTable.setAutoCreateColumnsFromModel(false);
								maltTable.getTableHeader().setReorderingAllowed(false);

								TableColumn maltColumn = maltTable.getColumnModel().getColumn(2);

								// set up malt list combo
								SmartComboBox.enable(maltComboBox);
								maltComboBox.setModel(cmbMaltModel);
								maltColumn.setCellEditor(new SBComboBoxCellEditor(maltComboBox));

								// set up malt amount editor
								maltColumn = maltTable.getColumnModel().getColumn(3);
								maltColumn.setCellEditor(maltAmountEditor);

								// set up malt units combo
								SmartComboBox.enable(maltUnitsComboBox);
								maltUnitsComboBox.setModel(cmbMaltUnitsModel);
								maltColumn = maltTable.getColumnModel().getColumn(4);
								maltColumn.setCellEditor(new SBComboBoxCellEditor(maltUnitsComboBox));

							}
						}
						{
							pnlMalt.add(tblMaltTotals, BorderLayout.SOUTH);
							tblMaltTotals.setModel(tblMaltTotalsModel);
							tblMaltTotals.getTableHeader().setEnabled(false);
							tblMaltTotals.setAutoCreateColumnsFromModel(false);

							// set up the units combobox
							SmartComboBox.enable(maltTotalUnitsComboBox);
							maltTotalUnitsComboModel.setList(Quantity.getListofUnits("weight"));
							maltTotalUnitsComboBox.setModel(maltTotalUnitsComboModel);
							TableColumn t = tblMaltTotals.getColumnModel().getColumn(4);
							t.setCellEditor(new SBComboBoxCellEditor(maltTotalUnitsComboBox));

						}
					}
					{
						pnlTables.add(pnlMaltButtons);
						FlowLayout pnlMaltButtonsLayout = new FlowLayout();
						pnlMaltButtonsLayout.setAlignment(FlowLayout.LEFT);
						pnlMaltButtonsLayout.setVgap(0);
						pnlMaltButtons.setLayout(pnlMaltButtonsLayout);
						pnlMaltButtons.setPreferredSize(new java.awt.Dimension(592, 27));
						{
							pnlMaltButtons.add(tlbMalt);
							tlbMalt.setPreferredSize(new java.awt.Dimension(386, 20));
							tlbMalt.setFloatable(false);
							{
								tlbMalt.add(btnAddMalt);
								btnAddMalt.setText("+");
							}
							{
								tlbMalt.add(btnDelMalt);
								btnDelMalt.setText("-");
							}
						}
					}
					{
						BorderLayout pnlHopsLayout = new BorderLayout();
						pnlHops.setBorder(BorderFactory.createTitledBorder(new LineBorder(new java.awt.Color(0, 0, 0),
								1, false), "Hops", TitledBorder.LEADING, TitledBorder.TOP, new java.awt.Font("Dialog",
								1, 12), new java.awt.Color(51, 51, 51)));
						pnlHops.setLayout(pnlHopsLayout);
						pnlTables.add(pnlHops);
						{
							pnlHops.add(tblHopsTotals, BorderLayout.SOUTH);
							tblHopsTotals.setModel(tblHopsTotalsModel);
							tblHopsTotals.setAutoCreateColumnsFromModel(false);

							// set up the units combobox
							SmartComboBox.enable(hopsTotalUnitsComboBox);
							hopsTotalUnitsComboModel.setList(Quantity.getListofUnits("weight"));
							hopsTotalUnitsComboBox.setModel(hopsTotalUnitsComboModel);
							TableColumn t = tblHopsTotals.getColumnModel().getColumn(4);
							t.setCellEditor(new SBComboBoxCellEditor(hopsTotalUnitsComboBox));

						}
						{
							pnlHops.add(jScrollPane2, BorderLayout.CENTER);
							{
								hopsTableModel = new HopsTableModel(this);
								hopsTable = new SBTable("hopsTable") {
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
								// Install the custom key selection manager
								SmartComboBox.enable(hopComboBox);
								hopComboBox.setModel(cmbHopsModel);
								hopColumn.setCellEditor(new SBComboBoxCellEditor(hopComboBox));

								// set up hop alpha acid editor
								hopColumn = hopsTable.getColumnModel().getColumn(2);
								hopColumn.setCellEditor(hopAcidEditor);

								// set up hop amount editor
								hopColumn = hopsTable.getColumnModel().getColumn(3);
								hopColumn.setCellEditor(hopAmountEditor);

								// set up hop units combo
								SmartComboBox.enable(hopsUnitsComboBox);
								hopsUnitsComboBox.setModel(cmbHopsUnitsModel);
								hopColumn = hopsTable.getColumnModel().getColumn(4);
								hopColumn.setCellEditor(new SBComboBoxCellEditor(hopsUnitsComboBox));

								// set up hop type combo
								JComboBox hopsFormComboBox = new JComboBox(Hop.forms);
								SmartComboBox.enable(hopsFormComboBox);
								hopColumn = hopsTable.getColumnModel().getColumn(1);
								hopColumn.setCellEditor(new SBComboBoxCellEditor(hopsFormComboBox));

								// set up hop add combo
								JComboBox hopsAddComboBox = new JComboBox(Hop.addTypes);
								SmartComboBox.enable(hopsAddComboBox);
								hopColumn = hopsTable.getColumnModel().getColumn(5);
								hopColumn.setCellEditor(new SBComboBoxCellEditor(hopsAddComboBox));

								// set up hop amount editor
								hopColumn = hopsTable.getColumnModel().getColumn(6);
								hopColumn.setCellEditor(hopTimeEditor);
							}
						}
					}
					{
						FlowLayout pnlHopsButtonsLayout = new FlowLayout();
						pnlHopsButtonsLayout.setAlignment(FlowLayout.LEFT);
						pnlHopsButtonsLayout.setVgap(0);
						pnlHopsButtons.setLayout(pnlHopsButtonsLayout);
						pnlTables.add(pnlHopsButtons);
						pnlHopsButtons.setPreferredSize(new java.awt.Dimension(512, 16));
						{
							pnlHopsButtons.add(tlbHops);
							tlbHops.setPreferredSize(new java.awt.Dimension(413, 19));
							tlbHops.setFloatable(false);
							{
								tlbHops.add(btnAddHop);
								btnAddHop.setText("+");
								btnAddHop.addActionListener(this);
							}
							{
								tlbHops.add(btnDelHop);
								btnDelHop.setText("-");
								btnDelHop.addActionListener(this);
							}
						}
					}

				}
				{
					FlowLayout statusPanelLayout = new FlowLayout();
					statusPanelLayout.setAlignment(FlowLayout.LEFT);
					statusPanelLayout.setHgap(2);
					statusPanelLayout.setVgap(2);
					statusPanel.setLayout(statusPanelLayout);
					pnlMain.add(statusPanel, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
					{
						statusPanel.add(fileNamePanel);
						fileNamePanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
						{
							fileNamePanel.add(fileNameLabel);
							fileNameLabel.setText("File Name");
							fileNameLabel.setFont(new java.awt.Font("Dialog", 1, 10));
						}
					}
					{
						statusPanel.add(ibuMethodPanel);
						ibuMethodPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
						{
							ibuMethodPanel.add(ibuMethodLabel);
							ibuMethodLabel.setText("IBU Method:");
							ibuMethodLabel.setFont(new java.awt.Font("Dialog", 1, 10));
						}
					}
					{
						statusPanel.add(alcMethodPanel);
						alcMethodPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
						{
							alcMethodPanel.add(alcMethodLabel);
							alcMethodLabel.setText("Alc Method:");
							alcMethodLabel.setFont(new java.awt.Font("Dialog", 1, 10));
						}
					}
				}
			}
			{
				setJMenuBar(mainMenuBar);
				{
					mainMenuBar.add(fileMenu);
					fileMenu.setText("File");
					{
						fileMenu.add(newFileMenuItem);
						newFileMenuItem.setText("New");
						
					}
					{
						fileMenu.add(openFileMenuItem);
						openFileMenuItem.setText("Open");
						openFileMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
						
					}
					{
						imgURL = getClass().getClassLoader().getResource("ca/strangebrew/icons/find.gif");
						icon = new ImageIcon(imgURL);
						findFileMenuItem.setIcon(icon);
						findFileMenuItem.setText("Find");
						findFileMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.CTRL_MASK));
						fileMenu.add(findFileMenuItem);
						
					}
					{
						imgURL = getClass().getClassLoader().getResource("ca/strangebrew/icons/save.gif");
						icon = new ImageIcon(imgURL);
						saveMenuItem.setText("Save");
						saveMenuItem.setIcon(icon);
						fileMenu.add(saveMenuItem);
						saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
						
					}
					{
						imgURL = getClass().getClassLoader().getResource("ca/strangebrew/icons/saveas.gif");
						icon = new ImageIcon(imgURL);
						saveAsMenuItem.setText("Save As ...");
						saveAsMenuItem.setIcon(icon);
						fileMenu.add(saveAsMenuItem);
						
					}
					{

						fileMenu.add(exportMenu);
						exportMenu.setText("Export");
						{
							exportMenu.add(exportHTMLmenu);
							exportHTMLmenu.setText("HTML");
							

							exportMenu.add(exportTextMenuItem);
							exportTextMenuItem.setText("Text");
							
						}
					}
					{
						fileMenu.add(clipboardMenuItem);
						

						imgURL = getClass().getClassLoader().getResource("ca/strangebrew/icons/print.gif");
						icon = new ImageIcon(imgURL);
						printMenuItem.setIcon(icon);
						fileMenu.add(printMenuItem);
						
					}
					{
						fileMenu.add(jSeparator2);
					}
					{
						fileMenu.add(exitMenuItem);
						exitMenuItem.setText("Exit");
						exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
						
					}
				}
				{
					mainMenuBar.add(editMenu);
					editMenu.setText("Edit");
					{
						editMenu.add(editPrefsMenuItem);
						editPrefsMenuItem.setText("Preferences...");
						

					}

					{
						editMenu.add(jSeparator1);
					}
					{
						editMenu.add(deleteMenuItem);
						deleteMenuItem.setText("Delete");
						deleteMenuItem.setEnabled(false);
					}
				}
				{
					mainMenuBar.add(mnuTools);
					mnuTools.setText("Tools");
					{
						mnuTools.add(scaleRecipeMenuItem);
						scaleRecipeMenuItem.setText("Resize / Convert Recipe...");
						scaleRecipeMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));

						mnuTools.add(maltPercentMenuItem);
						maltPercentMenuItem.setText("Malt Percent...");
						maltPercentMenuItem
								.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, ActionEvent.CTRL_MASK));

						mnuTools.add(refractometerMenuItem);
						refractometerMenuItem.setText("Refractometer Utility...");

						mnuTools.add(extractPotentialMenuItem);
						extractPotentialMenuItem.setText("Extract Potential...");

						mnuTools.add(hydrometerToolMenuItem);
						hydrometerToolMenuItem.setText("Hydrometer Tool...");

						mnuTools.add(conversionToolMenuItem);
						conversionToolMenuItem.setText("Conversion Tool...");

					}
				}
				{
					mainMenuBar.add(helpMenu);
					helpMenu.setText("Help");
					{
						helpMenu.add(helpMenuItem);
						helpMenuItem.setText("Help");

					}
					{
						helpMenu.add(aboutMenuItem);
						aboutMenuItem.setText("About...");

					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void saveFile() {

		int choice = 1;

		if (currentFile != null) {
			File file = currentFile;
			try {
				FileWriter out = new FileWriter(file);
				out.write(myRecipe.toXML(null));
				out.close();
				Debug.print("Saved: " + file.getAbsoluteFile());
				currentFile = file;
				myRecipe.setDirty(false);

			} catch (Exception e) {
				showError(e);
			}

		}
		// prompt to save if not already saved
		else {

			choice = JOptionPane.showConfirmDialog(null, "File not saved.  Do you wish to save it?", "File note saved",
					JOptionPane.YES_NO_OPTION);

		}

		if (choice == 0) {
			// same as save as:
			saveAs();
		}
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
		else if (o == evapMethodCombo)
			myRecipe.setEvapMethod(s);

		displayRecipe();
	}

	private void saveAs() {
		// Show save dialog; this method does
		// not return until the dialog is closed
		fileChooser.resetChoosableFileFilters();
		String[] ext = { "xml" };
		sbFileFilter saveFileFilter = new sbFileFilter(ext, "StrangeBrew XML");
		fileChooser.setFileFilter(saveFileFilter);
		fileChooser.setSelectedFile(new File(myRecipe.getName() + ".xml"));

		int returnVal = fileChooser.showSaveDialog(mainMenuBar);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();

			// This is where a real application would save the file.
			try {
				FileWriter out = new FileWriter(file);
				out.write(myRecipe.toXML(null));
				out.close();
				currentFile = file;
				myRecipe.setDirty(false);

				displayRecipe();

			} catch (Exception e) {
				showError(e);
			}
		} else {
			Debug.print("Save command cancelled by user.\n");
		}
	}

	public void showError(Exception e) {

		ByteArrayOutputStream bs = new ByteArrayOutputStream();
		e.printStackTrace(new PrintStream(bs));
		String stackStr = bs.toString();

		JOptionPane.showMessageDialog(null, "There seems to be a problem: " + e.toString() + "\n" + stackStr, "Pain!",
				JOptionPane.INFORMATION_MESSAGE);
	}

	public void checkIngredientsInDB() {

		// while we're doing this, why not synch up the style?
		// default style is just a name - find the matching style
		// in the style db.
		int j = DB.inDB(myRecipe.getStyleObj());
		if (j > -1)
			myRecipe.setStyle((Style) DB.styleDB.get(j));
		// TODO: dialog w/ close matches to this style

		List<Ingredient> newIngr = new ArrayList<Ingredient>();

		// check yeast
		if (DB.inDB(myRecipe.getYeastObj()) < 0) {
			newIngr.add(myRecipe.getYeastObj());
		}

		// check malts:
		for (int i = 0; i < myRecipe.getMaltListSize(); i++) {
			if (DB.inDB(myRecipe.getFermentable(i)) < 0) {
				newIngr.add(myRecipe.getFermentable(i));
			}
		}

		// check hops:
		for (int i = 0; i < myRecipe.getHopsListSize(); i++) {
			if (DB.inDB(myRecipe.getHop(i)) < 0) {
				newIngr.add(myRecipe.getHop(i));
			}

		}
		// show dialog:
		if (newIngr.size() > 0) {
			final JFrame owner = this;
			NewIngrDialog n = new NewIngrDialog(owner, newIngr);
			n.setModal(true);
			n.setVisible(true);
		}

	}

	public void actionPerformed(ActionEvent e) {
		if (dontUpdate) {
			return;
		}
		Object o = e.getSource();

		// Fields
		if (o == txtName)
			myRecipe.setName(txtName.getText());
		else if (o == brewerNameText)
			myRecipe.setBrewer(brewerNameText.getText());

		else if (o == preBoilText) {
			double x = SBStringUtils.round(myRecipe.getPreBoilVol(myRecipe.getVolUnits()), 2);
			double y = SBStringUtils.round(Double.parseDouble(preBoilText.getText()), 2);
			if (x != y) {
				Debug.print("Preboil Recipe: " + x + " UI: " + y);
				myRecipe.setPreBoil(new Quantity(myRecipe.getVolUnits(), Double.parseDouble(preBoilText.getText())));
				displayRecipe();
			}
		} else if (o == finalWortVolText) {
			double x = SBStringUtils.round(myRecipe.getFinalWortVol(myRecipe.getVolUnits()), 2);
			double y = SBStringUtils.round(Double.parseDouble(finalWortVolText.getText()), 2);			
			if (x != y) {				
				myRecipe.setFinalWortVol(new Quantity(myRecipe.getVolUnits(), Double
						.parseDouble(finalWortVolText.getText())));
				displayRecipe();
			}
		} else if (o == evapText) {
			myRecipe.setEvap(Double.parseDouble(evapText.getText()));
			displayRecipe();
		} else if (o == boilMinText) {
			String s = boilMinText.getText();
			if (s.indexOf('.') > 0) { // parseInt doesn't like '.' or ',', so
				// trim the string
				s = s.substring(0, s.indexOf('.'));
			}
			myRecipe.setBoilMinutes(Integer.parseInt(s));
			displayRecipe();
		} else if (o == maltTotalUnitsComboBox) {
			String u = (String) maltTotalUnitsComboModel.getSelectedItem();
			if (myRecipe != null) {
				myRecipe.setMaltUnits(u);
				displayRecipe();
			}
		} else if (o == btnAddMalt) {
			if (myRecipe != null) {
				Fermentable f = new Fermentable(myRecipe.getMaltUnits());
				myRecipe.addMalt(f);
				maltTable.updateUI();
				displayRecipe();
			}
		} else if (o == btnDelMalt) {
			if (myRecipe != null) {
				int i = maltTable.getSelectedRow();
				myRecipe.delMalt(i);
				maltTable.updateUI();
				displayRecipe();
			}
		} else if (o == hopsTotalUnitsComboBox) {
			String u = (String) hopsTotalUnitsComboModel.getSelectedItem();
			if (myRecipe != null) {
				myRecipe.setHopsUnits(u);
				displayRecipe();
			}
		} else if (o == btnAddHop) {
			if (myRecipe != null) {
				Hop h = new Hop(myRecipe.getHopUnits(), preferences.getProperty("optHopsType"));
				myRecipe.addHop(h);
				hopsTable.updateUI();
				displayRecipe();

			}
		} else if (o == btnDelHop) {
			if (myRecipe != null) {
				int i = hopsTable.getSelectedRow();
				myRecipe.delHop(i);
				hopsTable.updateUI();
				displayRecipe();
			}
		} else if (o == cmbYeast) {
			Yeast y = (Yeast) cmbYeastModel.getSelectedItem();
			if (myRecipe != null && y != myRecipe.getYeastObj()) {
				myRecipe.setYeast(y);
			}
			String st = SBStringUtils.multiLineToolTip(40, y.getDescription());

			cmbYeast.setToolTipText(st);
		} else if (o == cmbSizeUnits) {
			String q = (String) cmbSizeUnits.getSelectedItem();
			if (myRecipe != null && q != myRecipe.getVolUnits()) {
				// also sets postboil units:
				myRecipe.setVolUnits(q);
				displayRecipe();
			}
		} else if (o == alcMethodCombo) {
			recipeSettingsActionPerformed(e);
		} else if (o == ibuMethodCombo) {
			recipeSettingsActionPerformed(e);
		} else if (o == colourMethodCombo) {
			recipeSettingsActionPerformed(e);
		} else if (o == evapMethodCombo) {
			recipeSettingsActionPerformed(e);
		} else if (o == maltComboBox) {
			Fermentable f = (Fermentable) cmbMaltModel.getSelectedItem();
			int i = maltTable.getSelectedRow();
			if (myRecipe != null && i != -1) {
				Fermentable f2 = myRecipe.getFermentable(i);
				if (f2 != null) {
					f2.setLov(f.getLov());
					f2.setPppg(f.getPppg());
					f2.setDescription(f.getDescription());
					f2.setMashed(f.getMashed());
					f2.setSteep(f.getSteep());
					f2.setCost(f.getCostPerU());
				}
			}
		} else if (o == maltUnitsComboBox) {
			String u = (String) cmbMaltUnitsModel.getSelectedItem();
			int i = maltTable.getSelectedRow();
			if (myRecipe != null && i != -1) {
				Fermentable f2 = myRecipe.getFermentable(i);
				if (f2 != null) {
					// this converts the cost and amount:
					f2.convertTo(u);
					myRecipe.calcMaltTotals();
					displayRecipe();
				}
			}
		} else if (o == hopComboBox) {
			Hop h = (Hop) cmbHopsModel.getSelectedItem();
			int i = hopsTable.getSelectedRow();
			if (myRecipe != null && i != -1) {
				Hop h2 = myRecipe.getHop(i);
				h2.setAlpha(h.getAlpha());
				h2.setDescription(h.getDescription());
				h2.setCost(h.getCostPerU());
			}
		} else if (o == hopsUnitsComboBox) {
			String u = (String) cmbHopsUnitsModel.getSelectedItem();
			int i = hopsTable.getSelectedRow();
			if (myRecipe != null && i != -1) {
				Hop h = myRecipe.getHop(i);
				h.convertTo(u);
				myRecipe.calcHopsTotals();
				// tblHops.updateUI();
				displayRecipe();
			}
		} else if (o == txtDate) {
			java.util.Date newDate = txtDate.getDate();
			java.util.Date oldDate = myRecipe.getCreated().getTime();
			if (!oldDate.equals(newDate)) {
				myRecipe.setCreated(newDate);
			}
		} else if (o == cmbStyle) {
			Style st = (Style) cmbStyleModel.getSelectedItem();
			if (myRecipe != null && st != myRecipe.getStyleObj()) {
				myRecipe.setStyle(st);
				stylePanel.setStyle(st);

			}
			cmbStyle.setToolTipText(SBStringUtils.multiLineToolTip(50, st.getDescription()));
		}

		// Button and Menu listners
		else if (o == saveButton) {
			saveFile();
		} else if (o == findButton) {
			FindDialog fd = new FindDialog(this);
			fd.setModal(true);
			fd.setVisible(true);
		} else if (o == printButton) {
			PrintDialog pd = new PrintDialog(this);
			pd.setModal(true);
			pd.setVisible(true);
		} else if (o == copyButton) {
			// Copy current recipe to clipboard
			Clipboard clipboard = getToolkit().getSystemClipboard();
			StringSelection ss = new StringSelection(myRecipe.toText());
			clipboard.setContents(ss, ss);
		} else if (o == aboutMenuItem) {
			aboutDlg = new AboutDialog(this, version + " " + edition);
			aboutDlg.setVisible(true);
		} else if (o == helpMenuItem) {
			String urlString = SBStringUtils.getAppPath("help") + "index.html";
			Debug.print(urlString);
			AbstractLogger logger = new SystemLogger();
			BrowserLauncher launcher;
			try {
				launcher = new BrowserLauncher(logger);
				BrowserLauncherRunner runner = new BrowserLauncherRunner(launcher, urlString, null);
				Thread launcherThread = new Thread(runner);
				launcherThread.start();
			} catch (BrowserLaunchingInitializingException ex) {
				ex.printStackTrace();
			} catch (UnsupportedOperatingSystemException ex) {
				ex.printStackTrace();
			}
		} else if (o == conversionToolMenuItem) {
			ConversionDialog convTool = new ConversionDialog(this);
			convTool.setModal(true);
			convTool.setVisible(true);
		} else if (o == hydrometerToolMenuItem) {
			HydrometerToolDialog hydroTool = new HydrometerToolDialog(this);
			hydroTool.setModal(true);
			hydroTool.setVisible(true);
		} else if (o == extractPotentialMenuItem) {
			PotentialExtractCalcDialog extCalc = new PotentialExtractCalcDialog(this);
			extCalc.setModal(true);
			extCalc.setVisible(true);
		} else if (o == refractometerMenuItem) {
			RefractometerDialog refract = new RefractometerDialog(this);
			refract.setModal(true);
			refract.setVisible(true);
		} else if (o == maltPercentMenuItem) {
			MaltPercentDialog maltPercent = new MaltPercentDialog(this);
			maltPercent.setModal(true);
			maltPercent.setVisible(true);
		} else if (o == scaleRecipeMenuItem) {
			ScaleRecipeDialog scaleRecipe = new ScaleRecipeDialog(this);
			scaleRecipe.setModal(true);
			scaleRecipe.setVisible(true);
		} else if (o == editPrefsMenuItem) {
			PreferencesDialog d = new PreferencesDialog(this);
			d.setVisible(true);
		} else if (o == exitMenuItem) {
			// exit program
			processWindowEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
			System.exit(0);
		} else if (o == printMenuItem) {
			PrintDialog pd = new PrintDialog(this);
			pd.setModal(true);
			pd.setVisible(true);
		} else if (o == clipboardMenuItem) {
			// Copy current recipe to clipboard
			Clipboard clipboard = getToolkit().getSystemClipboard();
			StringSelection s = new StringSelection(myRecipe.toText());
			clipboard.setContents(s, s);
		} else if (o == exportTextMenuItem) {
			// Show save dialog; this method does
			// not return until the dialog is closed
			fileChooser.resetChoosableFileFilters();
			String[] ext = { "txt" };
			sbFileFilter saveFileFilter = new sbFileFilter(ext, "Text");
			fileChooser.setFileFilter(saveFileFilter);
			fileChooser.setSelectedFile(new File(myRecipe.getName() + ".txt"));
			int returnVal = fileChooser.showSaveDialog(mainMenuBar);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				// This is where a real application would save the file.
				try {
					FileWriter out = new FileWriter(file);
					out.write(myRecipe.toText());
					out.close();
				} catch (Exception ex) {
					showError(ex);

				}
			} else {
				Debug.print("Export text command cancelled by user.\n");
			}

		} else if (o == exportHTMLmenu) {
			// Show save dialog; this method does
			// not return until the dialog is closed
			fileChooser.resetChoosableFileFilters();
			String[] ext = { "html", "htm" };
			sbFileFilter saveFileFilter = new sbFileFilter(ext, "HTML");
			fileChooser.setFileFilter(saveFileFilter);
			fileChooser.setSelectedFile(new File(myRecipe.getName() + ".html"));

			int returnVal = fileChooser.showSaveDialog(mainMenuBar);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				// This is where a real application would save the file.
				try {
					saveAsHTML(file, "recipeToHtml.xslt", null);

				} catch (Exception ex) {
					showError(ex);
				}
			} else {
				Debug.print("Save command cancelled by user.\n");
			}
		} else if (o == saveAsMenuItem) {
			saveAs();
		} else if (o == saveMenuItem) {
			saveFile();
		} else if (o == findFileMenuItem) {
			// open the find dialog
			FindDialog fd = new FindDialog(this);
			fd.setModal(true);
			fd.setVisible(true);
		} else if (o == openFileMenuItem) {

			// Show open dialog; this method does
			// not return until the dialog is closed
			fileChooser.resetChoosableFileFilters();
			String[] ext = { "xml", "qbrew", "rec" };
			String desc = "StrangBrew and importable formats";
			sbFileFilter openFileFilter = new sbFileFilter(ext, desc);

			// fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

			fileChooser.setFileFilter(openFileFilter);

			int returnVal = fileChooser.showOpenDialog(mainMenuBar);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				Debug.print("Opening: " + file.getName() + ".\n");

				OpenImport oi = new OpenImport();
				myRecipe = oi.openFile(file);
				currentFile = file;
				if (oi.getFileType().equals("")) {

					JOptionPane.showMessageDialog(null, "The file you've tried to open isn't a recognized format. \n"
							+ "You can open: \n" + "StrangeBrew 1.x and Java files (.xml)\n" + "QBrew files (.qbrew)\n"
							+ "BeerXML files (.xml)\n" + "Promash files (.rec)", "Unrecognized Format!",
							JOptionPane.INFORMATION_MESSAGE);
				}
				if (oi.getFileType().equals("beerxml")) {
					JOptionPane.showMessageDialog(null,
							"The file you've opened is in BeerXML format.  It may contain \n"
									+ "several recipes.  Only the first recipe is opened.  Use the Find \n"
									+ "dialog to open other recipes in a BeerXML file.", "BeerXML!",
							JOptionPane.INFORMATION_MESSAGE);
				}
				attachRecipeData();
				checkIngredientsInDB();
				myRecipe.setDirty(false);
				myRecipe.calcMaltTotals();
				myRecipe.calcHopsTotals();
				myRecipe.mash.calcMashSchedule();
				displayRecipe();
			} else {
				Debug.print("Open command cancelled by user.\n");
			}
		} else if (o == newFileMenuItem) {
			// This is just a test right now to see that
			// stuff is changed.
			setRecipe(new Recipe(), null);
			myRecipe.setDirty(false);
			displayRecipe();
		}
	}

	public void stateChanged(ChangeEvent e) {
		if (dontUpdate) {
			return;
		}
		Object o = e.getSource();

		if (o == jTabbedPane1) {
			JTabbedPane pane = (JTabbedPane) o;
			Component tab = pane.getSelectedComponent();
			if (tab == pnlDetails) {
				displayRecipe();
			} else if (tab == stylePanel) {
				stylePanel.setStyleData();
			} else if (tab == miscPanel) {
				// n/a
			} else if (tab == notesPanel) {
				// n/a
			} else if (tab == dilutionPanel) {
				dilutionPanel.displayDilution();
			} else if (tab == mashPanel) {
				mashPanel.displayMash();
			} else if (tab == waterTreatmentPanel) {
				waterTreatmentPanel.displayWaterTreatment();
			} else if (tab == fermentPanel) {
				fermentPanel.displayFerment();
			} else if (tab == carbPanel) {
				carbPanel.displayCarb();
			} else if (tab == waterPanel) {
				waterPanel.displayWater();
			} else if (tab == costPanel) {
				costPanel.displayCost();
			} else if (tab == settingsPanel) {
				// n/a
			}
		} else if (o == spnAtten) {
			myRecipe.setAttenuation(Double.parseDouble(spnAtten.getValue().toString()));
			displayRecipe();
		} else if (o == spnOG) {
			myRecipe.setEstOg(Double.parseDouble(spnOG.getValue().toString()));
			displayRecipe();

		} else if (o == spnFG) {
			myRecipe.setEstFg(Double.parseDouble(spnFG.getValue().toString()));
			displayRecipe();

		} else if (o == spnEffic) {
			myRecipe.setEfficiency(Double.parseDouble(spnEffic.getValue().toString()));
			displayRecipe();
		}
	}
}
