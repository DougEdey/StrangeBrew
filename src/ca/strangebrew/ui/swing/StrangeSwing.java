/*
 * $Id: StrangeSwing.java,v 1.51 2007/12/10 15:53:18 jimcdiver Exp $ 
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
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
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
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ComboBoxEditor;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultCellEditor;
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
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import net.sf.wraplog.AbstractLogger;
import net.sf.wraplog.SystemLogger;
import ca.strangebrew.Database;
import ca.strangebrew.Debug;
import ca.strangebrew.Fermentable;
import ca.strangebrew.Hop;
import ca.strangebrew.OpenImport;
import ca.strangebrew.Options;
import ca.strangebrew.Quantity;
import ca.strangebrew.Recipe;
import ca.strangebrew.SBStringUtils;
import ca.strangebrew.Style;
import ca.strangebrew.XmlTransformer;
import ca.strangebrew.Yeast;
import ca.strangebrew.ui.swing.dialogs.AboutDialog;
import ca.strangebrew.ui.swing.dialogs.FindDialog;
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


public class StrangeSwing extends javax.swing.JFrame implements ActionListener, FocusListener, WindowListener {


	public String version = "2.0.1";
	
	public JTable hopsTable;
	public JTable maltTable;
	public Recipe myRecipe;

	private AboutDialog aboutDlg;
	private JMenuItem aboutMenuItem;
	private JComboBox alcMethodCombo;
	private DefaultComboBoxModel alcMethodComboModel;
	private JLabel alcMethodLabel;
	private JPanel alcMethodPanel;
	private SBCellEditor maltAmountEditor;
	private SBCellEditor hopAmountEditor;
	private SBCellEditor hopTimeEditor;
	private SBCellEditor hopAcidEditor;
	private JTextField boilMinText;
	private JLabel boilTimeLable;
	private JTextField brewerNameText;
	private JButton btnAddHop;
	private JButton btnAddMalt;
	private JButton findButton;
	private JButton saveButton;
	private JToolBar mainToolBar;
	private JButton btnDelHop;
	private JButton btnDelMalt;
	private ComboModel cmbHopsModel;
	private ComboModel cmbHopsUnitsModel;
	private ComboModel cmbMaltModel;
	private ComboModel cmbMaltUnitsModel;
	private JComboBox cmbSizeUnits;
	private ComboModel cmbSizeUnitsModel;
	private JComboBox cmbStyle;
	private ComboModel cmbStyleModel;
	private JComboBox cmbYeast;
	private ComboModel cmbYeastModel;
	private JComboBox colourMethodCombo;
	private DefaultComboBoxModel colourMethodComboModel;
	private JPanel colourPanel;
	private CostPanel costPanel;
	private String Costs;
	private File currentFile;
	private JMenuItem deleteMenuItem;
	private DilutionPanel dilutionPanel;
	
	private JMenuItem editPrefsMenuItem;
	private JLabel evapLabel;
	private JComboBox evapMethodCombo;
	private JTextField evapText;
	private JMenuItem exitMenuItem;
	private JMenuItem exportHTMLmenu;
	private JMenu exportMenu;
	private JMenuItem exportTextMenuItem;
	private JFileChooser fileChooser;
	
	private JMenu fileMenu;
	private JLabel fileNameLabel;
	private JPanel fileNamePanel;
	private JMenuItem findFileMenuItem;
	private JMenuItem helpMenuItem;
	private JComboBox hopComboBox;
	private HopsTableModel hopsTableModel;
	private JComboBox hopsUnitsComboBox;
	private JComboBox hopsTotalUnitsComboBox;
	private ComboModel hopsTotalUnitsComboModel;
	private JComboBox ibuMethodCombo;
	
	private DefaultComboBoxModel ibuMethodComboModel;
	private JLabel ibuMethodLabel;
	private JPanel ibuMethodPanel;
	private ImageIcon icon;
	private URL imgURL;
	private JMenu jMenu4;
	private JMenu jMenu5;
	private JMenuBar jMenuBar1;
	private JPanel jPanel1;
	private JScrollPane jScrollPane1;
	private JScrollPane jScrollPane2;
	private JSeparator jSeparator1;
	private JSeparator jSeparator2;
	private JTabbedPane jTabbedPane1;
	private JLabel lblAlc;

	private JLabel lblAlcValue;
	private JLabel lblAtten;
	private JLabel lblBrewer;
	private JLabel lblColour;

	private JLabel lblColourValue;
	private JLabel lblComments;
	private JLabel lblDate;
	private JLabel lblEffic;
	private JLabel lblFG;
	private JLabel lblIBU;
	private JLabel lblIBUvalue;
	private JLabel lblName;
	private JLabel lblOG;
	private JLabel lblPostBoil;
	private JLabel lblPreBoil;
	private JLabel lblSizeUnits;
	private JLabel lblStyle;
	private JLabel lblYeast;
	private JComboBox maltComboBox;
	private MaltTableModel maltTableModel;
	private JComboBox maltTotalUnitsComboBox;
	private ComboModel maltTotalUnitsComboModel;
	private JComboBox maltUnitsComboBox;
	private MashPanel mashPanel;
	private MiscPanel miscPanel;
	private JMenu mnuTools;
	private JMenuItem newFileMenuItem;
	private NotesPanel notesPanel;
	private JMenuItem openFileMenuItem;
	private JPanel pnlDetails;
	private JPanel pnlHops;
	private JPanel pnlHopsButtons;
	private JPanel pnlMain;
	private JPanel pnlMalt;
	private JPanel pnlMaltButtons;
	private JPanel pnlTables;
	private JFormattedTextField postBoilText;
	private Options preferences = new Options();
	private JMenuItem saveAsMenuItem;

	private JMenuItem saveMenuItem;
	private JScrollPane scpComments;
	// private JScrollPane scrMalts;
	private SettingsPanel settingsPanel;
	private JSpinner spnAtten;
	private JSpinner spnEffic;
	private JSpinner spnFG;
	private JSpinner spnOG;
	private JPanel statusPanel;
	private StylePanel stylePanel;
	private JTable tblHopsTotals;
	private DefaultTableModel tblHopsTotalsModel;
	private JTable tblMaltTotals;		

	private DefaultTableModel tblMaltTotalsModel;

	private JToolBar tlbHops;
	private JToolBar tlbMalt;

	private JTextArea txtComments;
	// private JFormattedTextField txtDate;
	private DatePicker txtDate;
	private JTextField txtName;
	private JFormattedTextField preBoilText;
	

	private WaterPanel waterPanel;
	public Database DB;
	
	
	
	// an object that you give to other gui objects so that they can set things on the main SB GUI
	// used by style and settings panels
	public class SBNotifier {
		public void displRecipe(){
			displayRecipe();
		}
	
		public void hopsUpdateUI(){
			hopsTable.updateUI();
		}
	
		public void maltUpdateUI(){
			maltTable.updateUI();
		}
	
		public void setStyle(Style s){
			cmbStyleModel.addOrInsert(s);
		}
	
	}

	
	public class SpinnerEditor extends AbstractCellEditor implements TableCellEditor {
		/**
		 * 
		 */
	
		final JSpinner spinner = new JSpinner();
	
		// Initializes the spinner.
		public SpinnerEditor() {
	
		}
	
		public SpinnerEditor(SpinnerNumberModel model) {
			spinner.setModel(model);
		}
	
		// Returns the spinners current value.
		public Object getCellEditorValue() {
			return spinner.getValue();
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
	}
	
	private class sbFileFilter extends FileFilter {

		private String description = "";
		private String[] extensions = {"xml"};

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
		StrangeSwing inst = new StrangeSwing();
		inst.setVisible(true);
	}

	{
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);	
		this.addWindowListener(this);
	}

/*
 * If you wanted to set a LAF, you'd do this:
 * 	{
	try {
	      UIManager.setLookAndFeel(new Plastic3DLookAndFeel());
	   } catch (Exception e) {}
	}*/


	public StrangeSwing() {
		super();	
		
		initGUI();
		// There has *got* to be a better way to do this:
		DB = new Database();
		String path = SBStringUtils.getAppPath("data");
		Debug.print("DB Path: " + path);
		
		DB.readDB(path);

		cmbStyleModel.setList(DB.styleDB);
		cmbYeastModel.setList(DB.yeastDB);
		cmbMaltModel.setList(DB.fermDB);
		cmbHopsModel.setList(DB.hopsDB);

		cmbSizeUnitsModel.setList(new Quantity().getListofUnits("vol"));
		cmbMaltUnitsModel.setList(new Quantity().getListofUnits("weight"));
		cmbHopsUnitsModel.setList(new Quantity().getListofUnits("weight"));

		path = SBStringUtils.getAppPath("recipes");
		Debug.print("Recipes path:" + path);		
		
		fileChooser = new JFileChooser();

		fileChooser.setCurrentDirectory(new File(path));

		// link malt table and totals:
		addColumnWidthListeners();

		// set up tabs:
		miscPanel.setList(DB.miscDB);
		stylePanel.setList(DB.styleDB);

		// does this speed up load?
		addListeners();

		myRecipe = new Recipe();
		myRecipe.setVersion(version);
		currentFile = null;
		attachRecipeData();
		myRecipe.setDirty(false);
		displayRecipe();
	}

	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		String s = "";
		// String t = "";
		
		if (!o.getClass().getName().endsWith("TextField"))
			return;
		
		s = ((JTextField) o).getText();
		// t = s.replace(',','.'); // accept also european decimal komma

		if (o == txtName)
			myRecipe.setName(s);
		else if (o == brewerNameText)
			myRecipe.setBrewer(s);
		else if (o == preBoilText) {
			myRecipe.setPreBoil(Double.parseDouble(s));
			displayRecipe();
		} else if (o == postBoilText) {
			myRecipe.setPostBoil(Double.parseDouble(s));
			displayRecipe();
		} else if (o == evapText) {
			myRecipe.setEvap(Double.parseDouble(s));
			displayRecipe();
		} else if (o == boilMinText) {
			if( s.indexOf('.') > 0)
			{   // parseInt doesn't like '.' or ',', so trim the string
				s = s.substring(0,s.indexOf('.'));
			}
			myRecipe.setBoilMinutes(Integer.parseInt(s));
			displayRecipe();
		}
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

	public void displayRecipe() {
		if (myRecipe == null)
			return;
		txtName.setText(myRecipe.getName());
		brewerNameText.setText(myRecipe.getBrewer());
		preBoilText.setValue(new Double(myRecipe.getPreBoilVol(myRecipe.getVolUnits())));
		lblSizeUnits.setText(myRecipe.getVolUnits());
		postBoilText.setValue(new Double(myRecipe.getPostBoilVol(myRecipe.getVolUnits())));
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		// setText(SBStringUtils.dateFormat1.format(myRecipe.getCreated().getTime()));
		Costs = SBStringUtils.myNF.format(myRecipe.getTotalMaltCost());
		tblMaltTotalsModel.setDataVector(new String[][]{{"", "", "Totals:", 
			"" + SBStringUtils.format(myRecipe.getTotalMalt(), 1), myRecipe.getMaltUnits(),
			"" + SBStringUtils.format(myRecipe.getEstOg(), 3),
			"" + SBStringUtils.format(myRecipe.getColour(), 1),
			Costs, "100"}}, new String[]{"", "", "",
				"", "", "", "", "", ""});

		Costs = SBStringUtils.myNF.format(myRecipe.getTotalHopsCost());
		tblHopsTotalsModel.setDataVector(new String[][]{{"Totals:", "", "",
			"" + SBStringUtils.format(myRecipe.getTotalHops(), 1), myRecipe.getHopUnits(), "",
			"", "" + SBStringUtils.format(myRecipe.getIbu(), 1),
			Costs}}, new String[]{"", "", "",
				"", "", "", "", "", ""});

		String fileName = "not saved";
		if (currentFile != null) {
			fileName = currentFile.getName();
		}


		fileNameLabel.setText("File: " + fileName);
		ibuMethodLabel.setText("IBU method: " + myRecipe.getIBUMethod());
		alcMethodLabel.setText("Alc method: " + myRecipe.getAlcMethod());

		double colour = myRecipe.getSrm();
		
		if (preferences.getProperty("optRGBMethod").equals("1"))
			colourPanel.setBackground(Recipe.calcRGB(1, colour, 
					preferences.getIProperty("optRed"),
					preferences.getIProperty("optGreen"),
					preferences.getIProperty("optBlue"),
					preferences.getIProperty("optAlpha")));	
		else
			colourPanel.setBackground(Recipe.calcRGB(2, colour, 
					preferences.getIProperty("optRed"),
					preferences.getIProperty("optGreen"),
					preferences.getIProperty("optBlue"),
					preferences.getIProperty("optAlpha")));

		// update the panels
		stylePanel.setStyleData();
		costPanel.displayCost();
		waterPanel.displayWater();
		mashPanel.displayMash();
		dilutionPanel.displayDilution();

		// Setup title bar
		String title = "StrangeBrew " + version;
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
	}

	public void focusGained(FocusEvent e) {
		// do nothing, we don't need this event
	}

	public void focusLost(FocusEvent e) {
		Object o = e.getSource();
		ActionEvent evt = new ActionEvent(o, 1, "");
		actionPerformed(evt);
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

	public void setRecipe(Recipe r, File f) {
		currentFile = f;
		myRecipe = r;
		myRecipe.setVersion(version);
		displayRecipe();
		attachRecipeData();
	}

	public void windowActivated(WindowEvent e) { }

	public void windowClosed(WindowEvent e) { }

	// This main window is closing, prompt to save file:
	public void windowClosing(WindowEvent e) {
		// displayMessage("WindowListener method called: windowClosing.");

		if (myRecipe.getDirty()) {
			int choice = 1;
	
			choice = JOptionPane.showConfirmDialog(null,
					"Do you wish to save the current recipe?",
					"Save Recipe?", JOptionPane.YES_NO_OPTION);
	
			if (choice == 0)
				saveAs();
		}
		
		// save the window size and location:
		preferences.setIProperty("winHeight", this.getHeight());
		preferences.setIProperty("winWidth", this.getWidth());
		preferences.setIProperty("winX", this.getX());
		preferences.setIProperty("winY", this.getY());
		preferences.saveProperties();		
	}

	public void windowDeactivated(WindowEvent e) { }

	public void windowDeiconified(WindowEvent e) { }

	public void windowIconified(WindowEvent e) { }

	public void windowOpened(WindowEvent e) { }

	private void addColumnWidthListeners() {
		TableColumnModel mtcm = maltTable.getColumnModel();
		TableColumnModel htcm = hopsTable.getColumnModel();
		

		//: listener that watches the width of a column
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

		//: listener that watches the width of a column
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
		
		// set preferred widths of the malt table
		maltTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		TableColumn col = mtcm.getColumn(0);
		col.setPreferredWidth(10);
		col = mtcm.getColumn(1);
		col.setPreferredWidth(10);
		col = mtcm.getColumn(2);
		col.setPreferredWidth(200);
		maltTable.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
		
		// now do the same for the hops table
		hopsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		col = htcm.getColumn(0);
		col.setPreferredWidth(200);
		hopsTable.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
	}
    
	// add the listeners *after* all the data has been attached to speed
	// up startup
	private void addListeners(){

		txtName.addActionListener(this);
		txtName.addFocusListener(this);
		brewerNameText.addFocusListener(this);
		brewerNameText.addActionListener(this);
		//txtDate.addFocusListener(this);
		//txtDate.addActionListener(this);
		preBoilText.addFocusListener(this);
		preBoilText.addActionListener(this);
		postBoilText.addFocusListener(this);
		postBoilText.addActionListener(this);
		boilMinText.addFocusListener(this);
		boilMinText.addActionListener(this);
		evapText.addFocusListener(this);
		evapText.addActionListener(this);
                
                txtDate.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        java.util.Date newDate = txtDate.getDate();
                        java.util.Date oldDate = myRecipe.getCreated().getTime();
                        if(!oldDate.equals(newDate))
                        {
                            myRecipe.setCreated(newDate);
                        }
                    }
		});

		cmbStyle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				Style s = (Style) cmbStyleModel.getSelectedItem();									
				if (myRecipe != null && s != myRecipe.getStyleObj()) {
					myRecipe.setStyle(s);
					stylePanel.setStyle(s);

				}

				cmbStyle.setToolTipText(SBStringUtils.multiLineToolTip(50, s.getDescription()));

			}
		});

/*		postBoilText.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent evt) {
				myRecipe.setPostBoil(Double.parseDouble(postBoilText.getText()
						.toString()));

				displayRecipe();
			}
		});*/


		cmbYeast.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				Yeast y = (Yeast) cmbYeastModel.getSelectedItem();
				if (myRecipe != null && y != myRecipe.getYeastObj()) {
					myRecipe.setYeast(y);
				}
				String st = SBStringUtils.multiLineToolTip(40, y
						.getDescription());

				cmbYeast.setToolTipText(st);
			}
		});

		cmbSizeUnits.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				String q = (String) cmbSizeUnits.getSelectedItem();
				if (myRecipe != null && q != myRecipe.getVolUnits()) {
					// also sets postboil units:
					myRecipe.setVolUnits(q);

					displayRecipe();
				}
			}
		});

		alcMethodCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				recipeSettingsActionPerformed(evt);
			}
		});

		ibuMethodCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				recipeSettingsActionPerformed(evt);
			}
		});

		colourMethodCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				recipeSettingsActionPerformed(evt);
			}
		});

		evapMethodCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				recipeSettingsActionPerformed(evt);
			}
		});

		maltComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				Fermentable f = (Fermentable) cmbMaltModel
				.getSelectedItem();
				int i = maltTable.getSelectedRow();
				if (myRecipe != null && i != -1 ) {
					Fermentable f2 = myRecipe.getFermentable(i);
					if (f2 != null){
						f2.setLov(f.getLov());
						f2.setPppg(f.getPppg());
						f2.setDescription(f.getDescription());
						f2.setMashed(f.getMashed());
						f2.setSteep(f.getSteep());
						f2.setCost(f.getCostPerU());
					}
				}

			}
		});	

		maltUnitsComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				String u = (String) cmbMaltUnitsModel.getSelectedItem();
				int i = maltTable.getSelectedRow();
				if (myRecipe != null && i != -1) {
					Fermentable f2 = myRecipe.getFermentable(i);
					if (f2 != null){
						// this converts the cost and amount:					
						f2.convertTo(u);
						myRecipe.calcMaltTotals();
						displayRecipe();
					}
				}

			}
		});		

		hopComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				Hop h = (Hop) cmbHopsModel.getSelectedItem();
				int i = hopsTable.getSelectedRow();
				if (myRecipe != null && i != -1) {
					Hop h2 = myRecipe.getHop(i);
					h2.setAlpha(h.getAlpha());
					h2.setDescription(h.getDescription());
					h2.setCost(h.getCostPerU());
				}

			}
		});

		hopsUnitsComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				String u = (String) cmbHopsUnitsModel.getSelectedItem();
				int i = hopsTable.getSelectedRow();
				if (myRecipe != null && i != -1) {
					Hop h = myRecipe.getHop(i);
					h.convertTo(u);
					myRecipe.calcHopsTotals();
					// tblHops.updateUI();
					displayRecipe();

				}

			}
		});

	}
	
	private void initGUI() {
		try {

			// restore the saved size and location:
			this.setSize(preferences.getIProperty("winWidth"), 
					preferences.getIProperty("winHeight"));
			this.setLocation(preferences.getIProperty("winX"), 
					preferences.getIProperty("winY"));
			imgURL = getClass().getClassLoader().getResource("ca/strangebrew/icons/sb2.gif");
			icon = new ImageIcon(imgURL);
			this.setIconImage(icon.getImage());
			this.setTitle("StrangeBrew " + version + " - [<new>]");
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
							brewerNameText.setPreferredSize(new java.awt.Dimension(69, 20));							
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
							lblEffic.setPreferredSize(new java.awt.Dimension(31, 14));
						}
						{
							lblAtten = new JLabel();
							pnlDetails.add(lblAtten, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0,
									GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0,
											0, 0, 0), 0, 0));
							lblAtten.setText("Atten:");
							lblAtten.setPreferredSize(new java.awt.Dimension(34, 14));
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
							//txtDate = new JFormattedTextField();
							txtDate = new DatePicker();
							pnlDetails.add(txtDate, new GridBagConstraints(1, 1, 2, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
									new Insets(0, 0, 0, 0), 0, 0));
							// txtDate.setText("Date");
							txtDate.setPreferredSize(new java.awt.Dimension(73, 20));
							txtDate.setDateStyle(DateFormat.SHORT);

						}
						{
							cmbStyleModel = new ComboModel();
							cmbStyle = new JComboBox();
							// Install the custom key selection manager
							cmbStyle.setKeySelectionManager(new SBKeySelectionManager());
							
							pnlDetails.add(cmbStyle, new GridBagConstraints(1, 2, 5, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
									new Insets(0, 0, 0, 0), 0, 0));
							cmbStyle.setModel(cmbStyleModel);
							cmbStyle.setMaximumSize(new java.awt.Dimension(100, 32767));
							cmbStyle.setPreferredSize(new java.awt.Dimension(190, 20));

						}
						{
							preBoilText = new JFormattedTextField();
							pnlDetails.add(preBoilText, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0,
									GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
									new Insets(0, 0, 0, 0), 0, 0));
							preBoilText.setText("Pre Boil");
							preBoilText.setPreferredSize(new java.awt.Dimension(37, 20));

						}
						{
							postBoilText = new JFormattedTextField();
							pnlDetails.add(postBoilText, new GridBagConstraints(1, 5, 1, 1, 0.0,
									0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
									new Insets(0, 0, 0, 0), 0, 0));
							postBoilText.setText("Post Boil");
							postBoilText.setPreferredSize(new java.awt.Dimension(46, 20));

						}
						{
							lblComments = new JLabel();
							pnlDetails.add(lblComments, new GridBagConstraints(6, 4, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
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
							spnEffic.setPreferredSize(new java.awt.Dimension(53, 18));
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
							spnAtten.setPreferredSize(new java.awt.Dimension(49, 20));
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
							spnOG.setPreferredSize(new java.awt.Dimension(67, 18));
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
							spnFG.setPreferredSize(new java.awt.Dimension(69, 20));
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
							pnlDetails.add(scpComments, new GridBagConstraints(7, 4, 3, 2, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
							{
								txtComments = new JTextArea();
								scpComments.setViewportView(txtComments);
								txtComments.setText("Comments");
								txtComments.setWrapStyleWord(true);
								// txtComments.setPreferredSize(new java.awt.Dimension(117, 42));
								txtComments.setLineWrap(true);
								scpComments.setPreferredSize(new java.awt.Dimension(263, 40));
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
							// Install the custom key selection manager
							cmbYeast.setKeySelectionManager(new SBKeySelectionManager());
							pnlDetails.add(cmbYeast, new GridBagConstraints(1, 3, 5, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
									new Insets(0, 0, 0, 0), 0, 0));
							cmbYeast.setModel(cmbYeastModel);
							cmbYeast.setPreferredSize(new java.awt.Dimension(193, 20));

						}
						{
							cmbSizeUnitsModel = new ComboModel();
							cmbSizeUnits = new JComboBox();
							pnlDetails.add(cmbSizeUnits, new GridBagConstraints(2, 4, 2, 1, 0.0,
									0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
									new Insets(0, 0, 0, 0), 0, 0));
							cmbSizeUnits.setModel(cmbSizeUnitsModel);

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
							boilMinText.setPreferredSize(new java.awt.Dimension(22, 20));

						}
						{
							evapText = new JTextField();
							pnlDetails.add(evapText, new GridBagConstraints(5, 5, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
									new Insets(0, 0, 0, 0), 0, 0));
							evapText.setText("4");
							evapText.setPreferredSize(new java.awt.Dimension(23, 20));							
						}
						{
							alcMethodComboModel = new DefaultComboBoxModel(new String[]{"Volume", "Weight"});
							alcMethodCombo = new JComboBox(alcMethodComboModel);							
							pnlDetails.add(alcMethodCombo, new GridBagConstraints(9, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
							alcMethodCombo.setPreferredSize(new java.awt.Dimension(71, 20));

						}
						{
							ibuMethodComboModel = new DefaultComboBoxModel(new String[]{"Tinseth", "Garetz",
							"Rager"});
							ibuMethodCombo = new JComboBox(ibuMethodComboModel);

							pnlDetails.add(ibuMethodCombo, new GridBagConstraints(9, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
							ibuMethodCombo.setPreferredSize(new java.awt.Dimension(72, 20));

						}
						{
							colourMethodComboModel = new DefaultComboBoxModel(new String[]{"SRM", "EBC"});
							colourMethodCombo = new JComboBox(colourMethodComboModel);
							pnlDetails.add(colourMethodCombo, new GridBagConstraints(9, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
							colourMethodCombo.setPreferredSize(new java.awt.Dimension(52, 20));

						}

						ComboBoxModel evapMethodComboModel = new DefaultComboBoxModel(new String[] {
								"Constant", "Percent" });
						{
							jPanel1 = new JPanel();
							pnlMain.add(jPanel1, new GridBagConstraints(
								0,
								0,
								1,
								1,
								0.0,
								0.0,
								GridBagConstraints.WEST,
								GridBagConstraints.HORIZONTAL,
								new Insets(0, 0, 0, 0),
								0,
								0));
							FlowLayout jPanel1Layout = new FlowLayout();
							jPanel1Layout.setAlignment(FlowLayout.LEFT);
							jPanel1.setLayout(jPanel1Layout);

							mainToolBar = new JToolBar();
							getContentPane().add(mainToolBar, BorderLayout.NORTH);
							mainToolBar.setFloatable(false);
							mainToolBar.setRollover(true);

							saveButton = new JButton();
							mainToolBar.add(saveButton);
							saveButton.setMnemonic(java.awt.event.KeyEvent.VK_S);
							saveButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource(
								"ca/strangebrew/icons/save.gif")));			
							
							saveButton.addActionListener(new ActionListener(){
								public void actionPerformed(ActionEvent evt) {
									saveFile(evt);
								}
							});

							findButton = new JButton();
							mainToolBar.add(findButton);
							findButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource(
								"ca/strangebrew/icons/find.gif")));
							final JFrame owner = this;
							findButton.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									FindDialog fd = new FindDialog(owner);
									fd.setModal(true);
									fd.setVisible(true);
								}
							});
							
							JButton printButton = new JButton();
							mainToolBar.add(printButton);
							printButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource(
							"ca/strangebrew/icons/print.gif")));
							printButton.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									PrintDialog pd = new PrintDialog(owner);
									pd.setModal(true);
									pd.setVisible(true);
								}
							});
							

							{
								lblName = new JLabel();
								jPanel1.add(lblName);
								lblName.setText("Name:");
							}
							{
								txtName = new JTextField();
								jPanel1.add(txtName);
								txtName.setText("Name");
								txtName.setPreferredSize(new java.awt.Dimension(297, 20));

							}

						}

						evapMethodCombo = new JComboBox();
						pnlDetails.add(evapMethodCombo, new GridBagConstraints(6, 5, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						evapMethodCombo.setModel(evapMethodComboModel);
						evapMethodCombo.setPreferredSize(new java.awt.Dimension(64, 20));

						colourPanel = new JPanel();
						pnlDetails.add(colourPanel, new GridBagConstraints(9, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
						colourPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
						colourPanel.setPreferredSize(new java.awt.Dimension(93, 32));

					}
					{
						SBNotifier sbn = new SBNotifier();
						stylePanel = new StylePanel(sbn);
						jTabbedPane1.addTab("Style", null, stylePanel, null);

						miscPanel = new MiscPanel(myRecipe);
						jTabbedPane1.addTab("Misc", null, miscPanel, null);

						notesPanel = new NotesPanel();
						jTabbedPane1.addTab("Notes", null, notesPanel, null);

						dilutionPanel = new DilutionPanel();
						jTabbedPane1.addTab("Dilution", null, dilutionPanel, null);

						mashPanel = new MashPanel(myRecipe);
						jTabbedPane1.addTab("Mash", null, mashPanel, null);

						waterPanel = new WaterPanel(sbn);
						jTabbedPane1.addTab("Water", null, waterPanel, null);

						costPanel = new CostPanel();
						jTabbedPane1.addTab("Cost", null, costPanel, null);
						
						// SBNotifier sbn = new SBNotifier();
						settingsPanel = new SettingsPanel(sbn);
						jTabbedPane1.addTab("Settings", null, settingsPanel, null);
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
								// maltTable.setAutoCreateColumnsFromModel(false);
								maltTable.getTableHeader().setReorderingAllowed(false);

								TableColumn maltColumn = maltTable.getColumnModel().getColumn(2);

								// set up malt list combo
								maltComboBox = new JComboBox();	
								ComboBoxEditor editor = maltComboBox.getEditor();
								editor.getEditorComponent().addKeyListener(new KeyAdapter(){
								        public void keyPressed(KeyEvent evt){
								        	// TODO: handle keypress
								        }

								}); 

								cmbMaltModel = new ComboModel();
								maltComboBox.setModel(cmbMaltModel);
								maltColumn.setCellEditor(new DefaultCellEditor(maltComboBox));
								
								// set up malt amount editor
								maltAmountEditor = new SBCellEditor(new JTextField());								
								maltColumn = maltTable.getColumnModel().getColumn(3);
								maltColumn.setCellEditor(maltAmountEditor);

								// set up malt units combo
								maltUnitsComboBox = new JComboBox();
								cmbMaltUnitsModel = new ComboModel();
								maltUnitsComboBox.setModel(cmbMaltUnitsModel);
								maltColumn = maltTable.getColumnModel().getColumn(4);
								maltColumn.setCellEditor(new DefaultCellEditor(maltUnitsComboBox));



							}
						}
						{
							tblMaltTotalsModel = new DefaultTableModel(new String[][]{{""}},
									new String[]{"S", "M", "Malt", "Amount", "Units", "Points", "Lov",
									"Cost/U", "%"});
							tblMaltTotals = new JTable();
							pnlMalt.add(tblMaltTotals, BorderLayout.SOUTH);
							tblMaltTotals.setModel(tblMaltTotalsModel);
							tblMaltTotals.getTableHeader().setEnabled(false);
							tblMaltTotals.setAutoCreateColumnsFromModel(false);
							
							// set up the units combobox
							maltTotalUnitsComboBox = new JComboBox();
							maltTotalUnitsComboModel = new ComboModel();
							maltTotalUnitsComboModel.setList(new Quantity().getListofUnits("weight"));
							maltTotalUnitsComboBox.setModel(maltTotalUnitsComboModel);
							TableColumn t = tblMaltTotals.getColumnModel().getColumn(4);
							t.setCellEditor(new DefaultCellEditor(maltTotalUnitsComboBox));
							maltTotalUnitsComboBox.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									String u = (String) maltTotalUnitsComboModel.getSelectedItem();				
									if (myRecipe != null) {
										myRecipe.setMaltUnits(u);
										displayRecipe();					
									}

								}
							});

						}
					}
					{
						pnlMaltButtons = new JPanel();
						pnlTables.add(pnlMaltButtons);
						FlowLayout pnlMaltButtonsLayout = new FlowLayout();
						pnlMaltButtonsLayout.setAlignment(FlowLayout.LEFT);
						pnlMaltButtonsLayout.setVgap(0);
						pnlMaltButtons.setLayout(pnlMaltButtonsLayout);
						pnlMaltButtons.setPreferredSize(new java.awt.Dimension(592, 27));
						{
							tlbMalt = new JToolBar();
							pnlMaltButtons.add(tlbMalt);
							tlbMalt.setPreferredSize(new java.awt.Dimension(386, 20));
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
							
							// set up the units combobox
							hopsTotalUnitsComboBox = new JComboBox();
							hopsTotalUnitsComboModel = new ComboModel();
							hopsTotalUnitsComboModel.setList(new Quantity().getListofUnits("weight"));
							hopsTotalUnitsComboBox.setModel(hopsTotalUnitsComboModel);
							TableColumn t = tblHopsTotals.getColumnModel().getColumn(4);
							t.setCellEditor(new DefaultCellEditor(hopsTotalUnitsComboBox));
							hopsTotalUnitsComboBox.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									String u = (String) hopsTotalUnitsComboModel.getSelectedItem();				
									if (myRecipe != null) {
										myRecipe.setHopsUnits(u);
										displayRecipe();					
									}

								}
							});

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
								hopComboBox = new JComboBox();
								// Install the custom key selection manager
								hopComboBox.setKeySelectionManager(new SBKeySelectionManager());
								cmbHopsModel = new ComboModel();
								hopComboBox.setModel(cmbHopsModel);
								hopColumn.setCellEditor(new DefaultCellEditor(hopComboBox));
								
								// set up hop alpha acid editor
								hopAcidEditor = new SBCellEditor(new JTextField());								
								hopColumn = hopsTable.getColumnModel().getColumn(2);
								hopColumn.setCellEditor(hopAcidEditor);
								
								// set up hop amount editor
								hopAmountEditor = new SBCellEditor(new JTextField());								
								hopColumn = hopsTable.getColumnModel().getColumn(3);
								hopColumn.setCellEditor(hopAmountEditor);
								
								// set up hop units combo
								hopsUnitsComboBox = new JComboBox();
								cmbHopsUnitsModel = new ComboModel();
								hopsUnitsComboBox.setModel(cmbHopsUnitsModel);
								hopColumn = hopsTable.getColumnModel().getColumn(4);
								hopColumn.setCellEditor(new DefaultCellEditor(hopsUnitsComboBox));


								// set up hop type combo
								String[] forms = Hop.getHopTypes();
								JComboBox hopsFormComboBox = new JComboBox(forms);
								hopColumn = hopsTable.getColumnModel().getColumn(1);
								hopColumn.setCellEditor(new DefaultCellEditor(hopsFormComboBox));								

								//								 set up hop add combo
								String[] add = {"Boil", "FWH", "Dry", "Mash"};
								JComboBox hopsAddComboBox = new JComboBox(add);
								hopColumn = hopsTable.getColumnModel().getColumn(5);
								hopColumn.setCellEditor(new DefaultCellEditor(hopsAddComboBox));

								// set up hop amount editor
								hopTimeEditor = new SBCellEditor(new JTextField());								
								hopColumn = hopsTable.getColumnModel().getColumn(6);
								hopColumn.setCellEditor(hopTimeEditor);								
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
							tlbHops.setPreferredSize(new java.awt.Dimension(413, 19));
							tlbHops.setFloatable(false);
							{
								btnAddHop = new JButton();
								tlbHops.add(btnAddHop);
								btnAddHop.setText("+");
								btnAddHop.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent evt) {
										if (myRecipe != null) {
											Hop h = new Hop(myRecipe.getHopUnits(), preferences.getProperty("optHopsType"));
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
								myRecipe.setVersion(version);
								currentFile = null;
								attachRecipeData();
								myRecipe.setDirty(false);
								displayRecipe();
							}
						});
					}
					{
						openFileMenuItem = new JMenuItem();
						fileMenu.add(openFileMenuItem);
						openFileMenuItem.setText("Open");
						openFileMenuItem.setAccelerator(KeyStroke.getKeyStroke(
						        KeyEvent.VK_O, ActionEvent.CTRL_MASK));
						openFileMenuItem.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {

								// Show open dialog; this method does
								// not return until the dialog is closed
								fileChooser.resetChoosableFileFilters();
								String[] ext = {"xml", "qbrew", "rec"};
								String desc = "StrangBrew and importable formats";
								sbFileFilter openFileFilter = new sbFileFilter(ext, desc);

								// fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
								
								fileChooser.setFileFilter(openFileFilter);


								int returnVal = fileChooser.showOpenDialog(jMenuBar1);
								if (returnVal == JFileChooser.APPROVE_OPTION) {
									File file = fileChooser.getSelectedFile();
									Debug.print("Opening: " + file.getName() + ".\n");
									
									OpenImport oi = new OpenImport();
									myRecipe = oi.openFile(file);
									if (oi.getFileType().equals("")){										

										JOptionPane.showMessageDialog(
												null, 
												"The file you've tried to open isn't a recognized format. \n" +
												"You can open: \n" +
												"StrangeBrew 1.x and Java files (.xml)\n" +
												"QBrew files (.qbrew)\n" +
												"BeerXML files (.xml)\n" +
												"Promash files (.rec)",
												"Unrecognized Format!", 
												JOptionPane.INFORMATION_MESSAGE);																					
									}
									if (oi.getFileType().equals("beerxml")){	
										JOptionPane.showMessageDialog(
												null, 
												"The file you've opened is in BeerXML format.  It may contain \n" +
												"several recipes.  Only the first recipe is opened.  Use the Find \n" +
												"dialog to open other recipes in a BeerXML file.",
												"BeerXML!", 
												JOptionPane.INFORMATION_MESSAGE);
									}
									
									myRecipe.setVersion(version);									
									myRecipe.calcMaltTotals();
									myRecipe.calcHopsTotals();
									myRecipe.mash.calcMashSchedule();
									checkIngredientsInDB();
									attachRecipeData();
									currentFile = file;
									myRecipe.setDirty(false);
									displayRecipe();
								} else {
									Debug.print("Open command cancelled by user.\n");
								}

							}
						});

					}
					{
						imgURL = getClass().getClassLoader().getResource("ca/strangebrew/icons/find.gif");
						icon = new ImageIcon(imgURL);
						findFileMenuItem = new JMenuItem("Find", icon);						
						findFileMenuItem.setAccelerator(KeyStroke.getKeyStroke(
						        KeyEvent.VK_F, ActionEvent.CTRL_MASK));
						
						fileMenu.add(findFileMenuItem);
						final JFrame owner = this;
						findFileMenuItem.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								// open the find dialog
								FindDialog fd = new FindDialog(owner);
								fd.setModal(true);
								fd.setVisible(true);

							}
						});
					}
					{						
						imgURL = getClass().getClassLoader().getResource("ca/strangebrew/icons/save.gif");
						icon = new ImageIcon(imgURL);
						saveMenuItem = new JMenuItem("Save", icon);
						fileMenu.add(saveMenuItem);						
						saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(
						        KeyEvent.VK_S, ActionEvent.CTRL_MASK));

						saveMenuItem.addActionListener(new ActionListener(){
								public void actionPerformed(ActionEvent evt) {
									saveFile(evt);
								}
						});
					}
					{
						imgURL = getClass().getClassLoader().getResource("ca/strangebrew/icons/saveas.gif");
						icon = new ImageIcon(imgURL);
						saveAsMenuItem = new JMenuItem("Save As ...", icon);						
						fileMenu.add(saveAsMenuItem);						
						saveAsMenuItem.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
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
									fileChooser.resetChoosableFileFilters();
									String[] ext = {"html", "htm"};
									sbFileFilter saveFileFilter = new sbFileFilter(ext, "HTML");
									fileChooser.setFileFilter(saveFileFilter);
									fileChooser.setSelectedFile(new File(myRecipe.getName()
											+ ".html"));

									int returnVal = fileChooser.showSaveDialog(jMenuBar1);
									if (returnVal == JFileChooser.APPROVE_OPTION) {
										File file = fileChooser.getSelectedFile();
										//This is where a real application would save the file.
										try {
											saveAsHTML(file, "recipeToHtml.xslt", null);

										} catch (Exception e) {
											showError(e);
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
									fileChooser.resetChoosableFileFilters();
									String[] ext = {"txt"};
									sbFileFilter saveFileFilter = new sbFileFilter(ext, "Text");
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
											showError(e);


										}
									} else {
										Debug.print("Export text command cancelled by user.\n");
									}

								}
							});
						}
					}
					{
						JMenuItem clipboardMenuItem = new JMenuItem("Copy to Clipboard");
						fileMenu.add(clipboardMenuItem);					
						clipboardMenuItem.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								// Copy current recipe to clipboard
								 Clipboard clipboard = getToolkit ().getSystemClipboard ();
								 StringSelection s = new StringSelection(myRecipe.toText());
								 clipboard.setContents(s, s);								 
							}
						});
						
						JMenuItem printMenuItem = new JMenuItem("Print...");
						fileMenu.add(printMenuItem);
						final JFrame owner = this;
						printMenuItem.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								PrintDialog pd = new PrintDialog(owner);
								pd.setModal(true);
								pd.setVisible(true);
							}
						});
						
						
					}
					{
						jSeparator2 = new JSeparator();
						fileMenu.add(jSeparator2);
					}
					{
						exitMenuItem = new JMenuItem();
						fileMenu.add(exitMenuItem);
						exitMenuItem.setText("Exit");	
						exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(
						        KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
						
						final JFrame owner = this;
						exitMenuItem.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								// exit program	
								
								processWindowEvent(new WindowEvent(owner,WindowEvent.WINDOW_CLOSING));
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
					mnuTools = new JMenu();
					jMenuBar1.add(mnuTools);
					mnuTools.setText("Tools");
					{
						final JFrame owner = this;
						
						JMenuItem scalRecipeMenuItem = new JMenuItem();
						mnuTools.add(scalRecipeMenuItem);
						scalRecipeMenuItem.setText("Resize / Convert Recipe...");
						scalRecipeMenuItem.setAccelerator(KeyStroke.getKeyStroke(
						        KeyEvent.VK_R, ActionEvent.CTRL_MASK));
						
						scalRecipeMenuItem.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								ScaleRecipeDialog scaleRecipe = new ScaleRecipeDialog(owner);
								scaleRecipe.setModal(true);
								scaleRecipe.setVisible(true);
							}
						});


						JMenuItem maltPercentMenuItem = new JMenuItem();
						mnuTools.add(maltPercentMenuItem);
						maltPercentMenuItem.setText("Malt Percent...");
						maltPercentMenuItem.setAccelerator(KeyStroke.getKeyStroke(
						        KeyEvent.VK_M, ActionEvent.CTRL_MASK));
						
						maltPercentMenuItem.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								MaltPercentDialog maltPercent = new MaltPercentDialog(owner);
								maltPercent.setModal(true);
								maltPercent.setVisible(true);
							}
						});
						
						JMenuItem refractometerMenuItem = new JMenuItem();
						mnuTools.add(refractometerMenuItem);
						refractometerMenuItem.setText("Refractometer Utility...");
												
						refractometerMenuItem.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								RefractometerDialog refract = new RefractometerDialog(owner);
								refract.setModal(true);
								refract.setVisible(true);
							}
						});					
						
						
						JMenuItem extractPotentialMenuItem = new JMenuItem();
						mnuTools.add(extractPotentialMenuItem);
						extractPotentialMenuItem.setText("Extract Potential...");
						extractPotentialMenuItem.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								PotentialExtractCalcDialog extCalc = new PotentialExtractCalcDialog(owner);
								extCalc.setModal(true);
								extCalc.setVisible(true);
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
						helpMenuItem.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								String urlString = SBStringUtils.getAppPath("help") + "index.html";
								Debug.print(urlString);
								AbstractLogger logger = new SystemLogger();								
								BrowserLauncher launcher;
								try {
									launcher = new BrowserLauncher(logger);
									BrowserLauncherRunner runner = new BrowserLauncherRunner(
						                    launcher,
						                    urlString,
						                    null);
						            Thread launcherThread = new Thread(runner);
						            launcherThread.start();
								} catch (BrowserLaunchingInitializingException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (UnsupportedOperatingSystemException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}							

							}
						});
						
					}
					{
						aboutMenuItem = new JMenuItem();
						jMenu5.add(aboutMenuItem);
						aboutMenuItem.setText("About...");
						final JFrame owner = this;
						aboutMenuItem.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								aboutDlg = new AboutDialog(owner, version);
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
	
	private void saveFile(ActionEvent evt){

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

			choice = JOptionPane.showConfirmDialog(null,
					"File not saved.  Do you wish to save it?",
					"File note saved", JOptionPane.YES_NO_OPTION);

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
		String[] ext = {"xml"};
		sbFileFilter saveFileFilter = new sbFileFilter(ext, "StrangeBrew XML");
		fileChooser.setFileFilter(saveFileFilter);
		fileChooser.setSelectedFile(new File(myRecipe.getName() + ".xml"));		

		int returnVal = fileChooser.showSaveDialog(jMenuBar1);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();

			//This is where a real application would save the file.
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

	private void showError(Exception e) {		

		ByteArrayOutputStream bs = new ByteArrayOutputStream();
		e.printStackTrace(new PrintStream(bs));
		String stackStr = bs.toString();


		JOptionPane.showMessageDialog(
				null, 
				"There seems to be a problem: " + e.toString()
				+ "\n" + stackStr,
				"Pain!", 
				JOptionPane.INFORMATION_MESSAGE);
	}

	public void checkIngredientsInDB(){
		
		// while we're doing this, why not synch up the style?
		// default style is just a name - find the matching style
		// in the style db.		
		int j = DB.inDB(myRecipe.getStyleObj());
		if (j>-1)
			myRecipe.setStyle((Style)DB.styleDB.get(j));
		// TODO: dialog w/ close matches to this style
		
		ArrayList newIngr = new ArrayList();
		
		// check yeast
		if(DB.inDB(myRecipe.getYeastObj())<0){
			newIngr.add(myRecipe.getYeastObj());
		}
		
		// check malts:
		for (int i=0; i<myRecipe.getMaltListSize(); i++){
			if (DB.inDB(myRecipe.getFermentable(i))<0){
				newIngr.add(myRecipe.getFermentable(i));
			}			
		}
		
		// check hops:
		for (int i=0; i<myRecipe.getHopsListSize(); i++){
			if (DB.inDB(myRecipe.getHop(i))<0){
				newIngr.add(myRecipe.getHop(i));
			}
			
		}
		// show dialog:
		if (newIngr.size() > 0){
			final JFrame owner = this;
			NewIngrDialog n = new NewIngrDialog(owner, newIngr);
			n.setModal(true);
			n.setVisible(true);
		}
		
	}
	   
    // This key selection manager will handle selections based on multiple keys.
    private class SBKeySelectionManager implements JComboBox.KeySelectionManager {
        long lastKeyTime = 0;
        String pattern = "";
    
        public int selectionForKey(char aKey, ComboBoxModel model) {
            // Find index of selected item
            int selIx = 01;
            Object sel = model.getSelectedItem();
            if (sel != null) {
                for (int i=0; i<model.getSize(); i++) {
                    if (sel.equals(model.getElementAt(i))) {
                        selIx = i;
                        break;
                    }
                }
            }
    
            // Get the current time
            long curTime = System.currentTimeMillis();
    
            // If last key was typed less than 300 ms ago, append to current pattern
            if (curTime - lastKeyTime < 300) {
                pattern += ("" + aKey).toLowerCase();
            } else {
                pattern = ("" + aKey).toLowerCase();
            }
    
            // Save current time
            lastKeyTime = curTime;
    
            // Search forward from current selection
            for (int i=selIx+1; i<model.getSize(); i++) {
                String s = model.getElementAt(i).toString().toLowerCase();
                if (s.startsWith(pattern)) {
                    return i;
                }
            }
    
            // Search from top to current selection
            for (int i=0; i<selIx ; i++) {
                if (model.getElementAt(i) != null) {
                    String s = model.getElementAt(i).toString().toLowerCase();
                    if (s.startsWith(pattern)) {
                        return i;
                    }
                }
            }
            return -1;
        }
    }

}
