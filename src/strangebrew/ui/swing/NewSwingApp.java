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
import java.text.DecimalFormat;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
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

import strangebrew.Database;
import strangebrew.Fermentable;
import strangebrew.ImportXml;
import strangebrew.Recipe;
import strangebrew.Style;
import strangebrew.XmlTransformer;
import strangebrew.Yeast;

public class NewSwingApp extends javax.swing.JFrame {

	{
		//Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e) {
			e.printStackTrace();
		}
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
	private JFormattedTextField txtPostBoil;
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
	private JMenuItem closeFileMenuItem;
	private JMenuItem saveAsMenuItem;
	private JMenuItem saveMenuItem;
	private JMenuItem openFileMenuItem;
	private JMenuItem newFileMenuItem;
	private JMenu jMenu3;
	private JMenuBar jMenuBar1;

	private MaltTableModel tblMaltModel;
	private DefaultTableModel tblMaltTotalsModel;
	private HopsTableModel tblHopsModel;
	private DefaultTableModel tblHopsTotalsModel;
	private ComboModel cmbYeastModel;
	private ComboModel cmbStyleModel;
	private ComboModel cmbMaltModel;
	private ComboModel cmbHopsModel;

	public Recipe myRecipe;
	DecimalFormat df1 = new DecimalFormat("####.0");
	DecimalFormat df2 = new DecimalFormat("#.00");
	DecimalFormat df3 = new DecimalFormat("0.000");
	
	private final static boolean DEBUG = true;

	/**
	 * Auto-generated main method to display this JFrame
	 */
	public static void main(String[] args) {
		NewSwingApp inst = new NewSwingApp();
		inst.setVisible(true);
	}

	public NewSwingApp() {
		super();
		initGUI();
		// There has *got* to be a better way to do this:
		Database db = new Database();
		db.readDB("src/strangebrew/data");

		cmbStyleModel.setList(db.styleDB);
		cmbYeastModel.setList(db.yeastDB);
		cmbMaltModel.setList(db.fermDB);
		cmbHopsModel.setList(db.hopsDB);
		

	}

	public void displayRecipe() {
		if (myRecipe == null)
			return;
		txtName.setText(myRecipe.getName());
		txtBrewer.setText(myRecipe.getBrewer());
		cmbStyleModel.addOrInsert(myRecipe.getStyleObj());
		cmbYeastModel.addOrInsert(myRecipe.getYeastObj());
		txtPreBoil.setValue(new Double(myRecipe.getPreBoilVol(myRecipe
				.getVolUnits())));
		txtPostBoil.setValue(new Double(myRecipe.getPostBoilVol(myRecipe
				.getVolUnits())));
		spnEffic.setValue(new Double(myRecipe.getEfficiency()));
		spnAtten.setValue(new Double(myRecipe.getAttenuation()));
		spnOG.setValue(new Double(myRecipe.getEstOg()));
		spnFG.setValue(new Double(myRecipe.getEstFg()));
		txtComments.setText(myRecipe.getComments());

		lblIBUvalue.setText(df1.format(myRecipe.getIbu()));
		lblColourValue.setText(df1.format(myRecipe.getSrm()));
		lblAlcValue.setText(df1.format(myRecipe.getAlcohol()));
		tblMaltModel.setData(myRecipe.getFermentablesList());
		tblHopsModel.setData(myRecipe.getHopsList());
		tblMaltTotalsModel.setDataVector(new String[][]{{"Totals:",
				"" + df1.format(myRecipe.getTotalMaltLbs()),
				myRecipe.getMaltUnits(), "" + df3.format(myRecipe.getEstOg()),
				"" + df1.format(myRecipe.getSrm()),
				"$" + df2.format(myRecipe.getTotalMaltCost()), "100"}},
				new String[]{"", "", "", "", "", "", ""});

		tblHopsTotalsModel.setDataVector(new String[][]{{"Totals:", "", "",
				"" + df1.format(myRecipe.getTotalHopsOz()),
				myRecipe.getHopUnits(), "", "",
				"" + df1.format(myRecipe.getIbu()),
				"$" + df2.format(myRecipe.getTotalHopsCost())}}, new String[]{
				"", "", "", "", "", "", "", "", ""});
		tblMalt.updateUI();
		tblHops.updateUI();

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
				jPanel2Layout.rowWeights = new double[]{0.1, 0.1, 0.1};
				jPanel2Layout.rowHeights = new int[]{7, 7, 7};
				pnlMain.setLayout(jPanel2Layout);
				this.getContentPane().add(pnlMain, BorderLayout.CENTER);
				{
					jPanel1 = new JPanel();
					pnlMain.add(jPanel1, new GridBagConstraints(0, 0, 1, 1,
							0.0, 0.0, GridBagConstraints.WEST,
							GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0,
									0), 0, 0));
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
						txtName
								.setPreferredSize(new java.awt.Dimension(179,
										20));
						txtName.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								myRecipe.setName(txtName.getText());
							}
						});
					}
				}
				{
					jTabbedPane1 = new JTabbedPane();
					pnlMain.add(jTabbedPane1, new GridBagConstraints(0, 1, 1,
							1, 0.1, 0.1, GridBagConstraints.CENTER,
							GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0,
							0));
					{
						pnlDetails = new JPanel();
						GridBagLayout pnlDetailsLayout = new GridBagLayout();
						pnlDetailsLayout.columnWeights = new double[]{0.1, 0.1,
								0.1, 0.1, 0.1, 0.1, 0.1};
						pnlDetailsLayout.columnWidths = new int[]{7, 7, 7, 7,
								7, 7, 7};
						pnlDetailsLayout.rowWeights = new double[]{0.1, 0.1,
								0.1, 0.1, 0.1, 0.1, 0.1};
						pnlDetailsLayout.rowHeights = new int[]{7, 7, 7, 7, 7,
								7, 7};
						pnlDetails.setLayout(pnlDetailsLayout);
						jTabbedPane1.addTab("Details", null, pnlDetails, null);
						{
							lblBrewer = new JLabel();
							pnlDetails.add(lblBrewer, new GridBagConstraints(0,
									0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
									GridBagConstraints.NONE, new Insets(0, 0,
											0, 0), 0, 0));
							lblBrewer.setText("Brewer:");
						}
						{
							txtBrewer = new JTextField();
							pnlDetails.add(txtBrewer, new GridBagConstraints(1,
									0, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER,
									GridBagConstraints.HORIZONTAL, new Insets(
											0, 0, 0, 0), 0, 0));
							txtBrewer.setText("Brewer");
							txtBrewer.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									myRecipe.setBrewer(txtBrewer.getText());
								}
							});
						}
						{
							lblDate = new JLabel();
							pnlDetails.add(lblDate, new GridBagConstraints(0,
									1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
									GridBagConstraints.NONE, new Insets(0, 0,
											0, 0), 0, 0));
							lblDate.setText("Date:");
						}
						{
							lblStyle = new JLabel();
							pnlDetails.add(lblStyle, new GridBagConstraints(0,
									2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
									GridBagConstraints.NONE, new Insets(0, 0,
											0, 0), 0, 0));
							lblStyle.setText("Style:");
						}
						{
							lblYeast = new JLabel();
							pnlDetails.add(lblYeast, new GridBagConstraints(0,
									3, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
									GridBagConstraints.NONE, new Insets(0, 0,
											0, 0), 0, 0));
							lblYeast.setText("Yeast:");
						}
						{
							lblPreBoil = new JLabel();
							pnlDetails.add(lblPreBoil, new GridBagConstraints(
									0, 4, 1, 1, 0.0, 0.0,
									GridBagConstraints.EAST,
									GridBagConstraints.NONE, new Insets(0, 0,
											0, 0), 0, 0));
							lblPreBoil.setText("Pre boil:");
						}
						{
							lblPostBoil = new JLabel();
							pnlDetails.add(lblPostBoil, new GridBagConstraints(
									0, 5, 1, 1, 0.0, 0.0,
									GridBagConstraints.EAST,
									GridBagConstraints.NONE, new Insets(0, 0,
											0, 0), 0, 0));
							lblPostBoil.setText("Post boil:");
						}
						{
							lblMash = new JLabel();
							pnlDetails.add(lblMash, new GridBagConstraints(0,
									6, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
									GridBagConstraints.NONE, new Insets(0, 0,
											0, 0), 0, 0));
							lblMash.setText("Mash:");
						}
						{
							lblEffic = new JLabel();
							pnlDetails.add(lblEffic, new GridBagConstraints(3,
									0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
									GridBagConstraints.NONE, new Insets(0, 0,
											0, 0), 0, 0));
							lblEffic.setText("Effic:");
						}
						{
							lblAtten = new JLabel();
							pnlDetails.add(lblAtten, new GridBagConstraints(3,
									1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
									GridBagConstraints.NONE, new Insets(0, 0,
											0, 0), 0, 0));
							lblAtten.setText("Atten:");
						}
						{
							lblOG = new JLabel();
							pnlDetails.add(lblOG, new GridBagConstraints(3, 2,
									1, 1, 0.0, 0.0, GridBagConstraints.EAST,
									GridBagConstraints.NONE, new Insets(0, 0,
											0, 0), 0, 0));
							lblOG.setText("OG:");
						}
						{
							lblFG = new JLabel();
							pnlDetails.add(lblFG, new GridBagConstraints(3, 3,
									1, 1, 0.0, 0.0, GridBagConstraints.EAST,
									GridBagConstraints.NONE, new Insets(0, 0,
											0, 0), 0, 0));
							lblFG.setText("FG:");
						}
						{
							lblIBU = new JLabel();
							pnlDetails.add(lblIBU, new GridBagConstraints(5, 0,
									1, 1, 0.0, 0.0, GridBagConstraints.EAST,
									GridBagConstraints.NONE, new Insets(0, 0,
											0, 0), 0, 0));
							lblIBU.setText("IBU:");
						}
						{
							lblAlc = new JLabel();
							pnlDetails.add(lblAlc, new GridBagConstraints(5, 2,
									1, 1, 0.0, 0.0, GridBagConstraints.EAST,
									GridBagConstraints.NONE, new Insets(0, 0,
											0, 0), 0, 0));
							lblAlc.setText("%Alc:");
						}
						{
							lblColour = new JLabel();
							pnlDetails.add(lblColour, new GridBagConstraints(5,
									1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
									GridBagConstraints.NONE, new Insets(0, 0,
											0, 0), 0, 0));
							lblColour.setText("Colour:");
						}
						{
							txtDate = new JTextField();
							pnlDetails.add(txtDate, new GridBagConstraints(1,
									1, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER,
									GridBagConstraints.HORIZONTAL, new Insets(
											0, 0, 0, 0), 0, 0));
							txtDate.setText("Date");
						}
						{
							cmbStyleModel = new ComboModel();
							cmbStyle = new JComboBox();
							pnlDetails.add(cmbStyle, new GridBagConstraints(1,
									2, 2, 1, 0.0, 0.0,
									GridBagConstraints.CENTER,
									GridBagConstraints.HORIZONTAL, new Insets(
											0, 0, 0, 0), 0, 0));
							cmbStyle.setModel(cmbStyleModel);
							cmbStyle.setMaximumSize(new java.awt.Dimension(100,
									32767));
							
							cmbStyle.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									Style s = (Style) cmbStyleModel
										.getSelectedItem();
									if (myRecipe != null
										&& s != myRecipe.getStyleObj()) {
										myRecipe.setStyle(s);
									}
								}
							});
						}
						{
							txtPreBoil = new JFormattedTextField();
							pnlDetails.add(txtPreBoil, new GridBagConstraints(
									1, 4, 1, 1, 0.0, 0.0,
									GridBagConstraints.WEST,
									GridBagConstraints.HORIZONTAL, new Insets(
											0, 0, 0, 0), 0, 0));
							txtPreBoil.setText("Pre Boil");
							txtPreBoil.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									myRecipe.setPreBoil(Double
											.parseDouble(txtPreBoil.getText()
													.toString()));
									displayRecipe();
								}
							});
						}
						{
							txtPostBoil = new JFormattedTextField();
							pnlDetails.add(txtPostBoil, new GridBagConstraints(
									1, 5, 1, 1, 0.0, 0.0,
									GridBagConstraints.WEST,
									GridBagConstraints.HORIZONTAL, new Insets(
											0, 0, 0, 0), 0, 0));
							txtPostBoil.setText("Post Boil");
							txtPostBoil.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									System.out
											.println("txtPostBoil.actionPerformed, event="
													+ evt);
									myRecipe.setPostBoil(Double
											.parseDouble(txtPostBoil.getText()
													.toString()));
									displayRecipe();
								}
							});
						}
						{
							lblComments = new JLabel();
							pnlDetails.add(lblComments, new GridBagConstraints(
									3, 4, 1, 1, 0.0, 0.0,
									GridBagConstraints.EAST,
									GridBagConstraints.NONE, new Insets(0, 0,
											0, 0), 0, 0));
							lblComments.setText("Comments:");
						}

						{
							SpinnerNumberModel spnEfficModel = new SpinnerNumberModel(
									75.0, 0.0, 100.0, 1.0);
							spnEffic = new JSpinner();
							pnlDetails.add(spnEffic, new GridBagConstraints(4,
									0, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER,
									GridBagConstraints.HORIZONTAL, new Insets(
											0, 0, 0, 0), 0, 0));
							spnEffic.setModel(spnEfficModel);
							spnEffic.setMaximumSize(new java.awt.Dimension(70, 32767));
							spnEffic.addChangeListener(new ChangeListener() {
								public void stateChanged(ChangeEvent evt) {
									myRecipe.setEfficiency(Double
											.parseDouble(spnEffic.getValue()
													.toString()));
									displayRecipe();
								}
							});
							spnEffic.setEditor(new JSpinner.NumberEditor(
									spnEffic, "00.#"));
						}
						{
							SpinnerNumberModel spnAttenModel = new SpinnerNumberModel(
									75.0, 0.0, 100.0, 1.0);
							spnAtten = new JSpinner();
							pnlDetails.add(spnAtten, new GridBagConstraints(4,
									1, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER,
									GridBagConstraints.HORIZONTAL, new Insets(
											0, 0, 0, 0), 0, 0));
							spnAtten.setModel(spnAttenModel);
							spnAtten.addChangeListener(new ChangeListener() {
								public void stateChanged(ChangeEvent evt) {
									myRecipe.setAttenuation(Double
											.parseDouble(spnAtten.getValue()
													.toString()));
									displayRecipe();
								}
							});
							spnAtten.setEditor(new JSpinner.NumberEditor(
									spnAtten, "00.#"));
						}
						{
							SpinnerNumberModel spnOgModel = new SpinnerNumberModel(
									1.000, 0.900, 2.000, 0.001);
							spnOG = new JSpinner();
							pnlDetails.add(spnOG, new GridBagConstraints(4, 2,
									1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
									GridBagConstraints.HORIZONTAL, new Insets(
											0, 0, 0, 0), 0, 0));
							spnOG.setModel(spnOgModel);
							spnOG.addChangeListener(new ChangeListener() {
								public void stateChanged(ChangeEvent evt) {
									myRecipe.setEstOg(Double.parseDouble(spnOG
											.getValue().toString()));
									displayRecipe();
								}
							});
							spnOG.setEditor(new JSpinner.NumberEditor(spnOG,
									"0.000"));
						}
						{
							SpinnerNumberModel spnFgModel = new SpinnerNumberModel(
									1.000, 0.900, 2.000, 0.001);
							spnFG = new JSpinner();
							pnlDetails.add(spnFG, new GridBagConstraints(4, 3,
									1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
									GridBagConstraints.HORIZONTAL, new Insets(
											0, 0, 0, 0), 0, 0));
							spnFG.setModel(spnFgModel);
							spnFG.setEditor(new JSpinner.NumberEditor(spnFG,
									"0.000"));
							spnFG.addChangeListener(new ChangeListener() {
								public void stateChanged(ChangeEvent evt) {
									// set the new FG, and update alc:
									myRecipe.setEstFg(Double.parseDouble(spnFG
											.getValue().toString()));
									displayRecipe();
								}
							});
						}
						{
							lblIBUvalue = new JLabel();
							pnlDetails.add(lblIBUvalue, new GridBagConstraints(
									6, 0, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER,
									GridBagConstraints.NONE, new Insets(0, 0,
											0, 0), 0, 0));
							lblIBUvalue.setText("IBUs");
						}
						{
							lblColourValue = new JLabel();
							pnlDetails.add(lblColourValue,
									new GridBagConstraints(6, 1, 1, 1, 0.0,
											0.0, GridBagConstraints.CENTER,
											GridBagConstraints.NONE,
											new Insets(0, 0, 0, 0), 0, 0));
							lblColourValue.setText("Colour");
						}
						{
							lblAlcValue = new JLabel();
							pnlDetails.add(lblAlcValue, new GridBagConstraints(
									6, 2, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER,
									GridBagConstraints.NONE, new Insets(0, 0,
											0, 0), 0, 0));
							lblAlcValue.setText("Alc");
						}
						{
							scpComments = new JScrollPane();
							pnlDetails.add(scpComments, new GridBagConstraints(
									3, 5, 4, 2, 0.0, 0.0,
									GridBagConstraints.CENTER,
									GridBagConstraints.BOTH, new Insets(0, 0,
											0, 0), 0, 0));
							{
								txtComments = new JTextArea();
								scpComments.setViewportView(txtComments);
								txtComments.setText("Comments");
								txtComments.setWrapStyleWord(true);
								txtComments
									.addFocusListener(new FocusAdapter() {
									public void focusLost(FocusEvent evt) {
										if (!txtComments.getText().equals(myRecipe.getComments())){
											myRecipe.setComments(txtComments.getText());
										}
									}
									});
							}
						}
						{
							cmbYeastModel = new ComboModel();
							cmbYeast = new JComboBox();
							pnlDetails.add(cmbYeast, new GridBagConstraints(1,
									3, 2, 1, 0.0, 0.0,
									GridBagConstraints.CENTER,
									GridBagConstraints.HORIZONTAL, new Insets(
											0, 0, 0, 0), 0, 0));
							cmbYeast.setModel(cmbYeastModel);
							cmbYeast.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									Yeast y = (Yeast) cmbYeastModel
											.getSelectedItem();
									if (myRecipe!= null && y != myRecipe.getYeastObj()) {
										myRecipe.setYeast(y);
									}
								}
							});
						}
						{
							ComboBoxModel cmbSizeUnitsModel = new DefaultComboBoxModel(
									new String[]{"Item One", "Item Two"});
							cmbSizeUnits = new JComboBox();
							pnlDetails.add(cmbSizeUnits,
									new GridBagConstraints(2, 4, 1, 1, 0.0,
											0.0, GridBagConstraints.CENTER,
											GridBagConstraints.NONE,
											new Insets(0, 0, 0, 0), 0, 0));
							cmbSizeUnits.setModel(cmbSizeUnitsModel);
						}
						{
							lblSizeUnits = new JLabel();
							pnlDetails.add(lblSizeUnits,
									new GridBagConstraints(2, 5, 1, 1, 0.0,
											0.0, GridBagConstraints.CENTER,
											GridBagConstraints.NONE,
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
							ComboBoxModel cmbStyle2Model = new DefaultComboBoxModel(
								new String[] { "Item One", "Item Two" });
							cmbStyle2 = new JComboBox();
							pnlStyle.add(cmbStyle2);
							cmbStyle2.setModel(cmbStyle2Model);
						}
						{
							jPanel2 = new JPanel();
							GridBagLayout jPanel2Layout1 = new GridBagLayout();
							jPanel2Layout1.columnWeights = new double[] {0.1,0.1,0.1,0.1};
							jPanel2Layout1.columnWidths = new int[] {7,7,7,7};
							jPanel2Layout1.rowWeights = new double[] {0.1,0.1,0.1,0.1,0.1,0.1};
							jPanel2Layout1.rowHeights = new int[] {7,7,7,7,7,7};
							jPanel2.setPreferredSize(new java.awt.Dimension(179, 120));
							jPanel2.setLayout(jPanel2Layout1);
							pnlStyle.add(jPanel2);
							jPanel2.setBorder(BorderFactory.createTitledBorder(new LineBorder(new java.awt.Color(0,0,0), 1, false), "Recipe Conformance:", TitledBorder.LEADING, TitledBorder.TOP, new java.awt.Font("Dialog",0,12), new java.awt.Color(0,0,0)));
							{
								jLabel5 = new JLabel();
								jPanel2.add(jLabel5, new GridBagConstraints(
									0,
									4,
									1,
									1,
									0.0,
									0.0,
									GridBagConstraints.EAST,
									GridBagConstraints.NONE,
									new Insets(0, 0, 0, 0),
									0,
									0));
								jLabel5.setText("OG:");
								jLabel5.setBounds(74, 3, 60, 30);
							}
							{
								jLabel1 = new JLabel();
								jPanel2.add(jLabel1, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
								GridLayout jLabel1Layout = new GridLayout(1, 1);
								jLabel1.setLayout(jLabel1Layout);
								jLabel1.setText("Low:");
							}
							{
								jLabel2 = new JLabel();
								jPanel2.add(jLabel2, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
								jLabel2.setText("Recipe:");
							}
							{
								jLabel3 = new JLabel();
								jPanel2.add(jLabel3, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
								jLabel3.setText("High:");
							}
							{
								jLabel4 = new JLabel();
								jPanel2.add(jLabel4, new GridBagConstraints(
									0,
									1,
									1,
									1,
									0.0,
									0.0,
									GridBagConstraints.EAST,
									GridBagConstraints.NONE,
									new Insets(0, 0, 0, 0),
									0,
									0));
								jLabel4.setText("IBU:");
							}
							{
								jLabel6 = new JLabel();
								jPanel2.add(jLabel6, new GridBagConstraints(
									0,
									2,
									1,
									1,
									0.0,
									0.0,
									GridBagConstraints.EAST,
									GridBagConstraints.NONE,
									new Insets(0, 0, 0, 0),
									0,
									0));
								jLabel6.setText("Colour:");
							}
							{
								jLabel7 = new JLabel();
								jPanel2.add(jLabel7, new GridBagConstraints(
									0,
									3,
									1,
									1,
									0.0,
									0.0,
									GridBagConstraints.EAST,
									GridBagConstraints.NONE,
									new Insets(0, 0, 0, 0),
									0,
									0));
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
					pnlMain.add(pnlTables, new GridBagConstraints(0, 2, 1, 1,
							0.5, 0.5, GridBagConstraints.CENTER,
							GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0,
							0));

					pnlTables.setLayout(pnlMaltsLayout);
					{
						pnlMalt = new JPanel();
						pnlTables.add(pnlMalt);
						BorderLayout pnlMaltLayout1 = new BorderLayout();
						FlowLayout pnlMaltLayout = new FlowLayout();
						pnlMalt.setBorder(BorderFactory.createTitledBorder(
								new LineBorder(new java.awt.Color(0, 0, 0), 1,
										false), "Fermentables",
								TitledBorder.LEADING, TitledBorder.TOP,
								new java.awt.Font("Dialog", 1, 12),
								new java.awt.Color(51, 51, 51)));
						pnlMalt.setLayout(pnlMaltLayout1);
						{
							jScrollPane1 = new JScrollPane();
							pnlMalt.add(jScrollPane1, BorderLayout.CENTER);
							{
								tblMaltModel = new MaltTableModel(this);
								tblMalt = new JTable();
								jScrollPane1.setViewportView(tblMalt);
								tblMalt.setModel(tblMaltModel);								
								// TableColumn column = null;
								TableColumn maltColumn = tblMalt.getColumnModel().getColumn(0);
								JComboBox maltComboBox = new JComboBox();
								cmbMaltModel = new ComboModel();
								maltComboBox.setModel(cmbMaltModel);
								maltColumn.setCellEditor(new DefaultCellEditor(maltComboBox));
								
								// set up spin editor for amount
								maltColumn = tblMalt.getColumnModel().getColumn(1);
								JSpinner maltSpin = new JSpinner();
								maltColumn.setCellEditor(new SpinnerEditor());						
								
								
								for (int i = 0; i < tblMalt.getColumnCount(); i++) {
									TableColumn column;
									column = tblMalt.getColumnModel()
											.getColumn(i);
									if (i == 0) {
										column.setPreferredWidth(100); 	   
										
									} else {
										column.setPreferredWidth(50);
									}
								}
								
										
								maltComboBox
										.addActionListener(new ActionListener() {
											public void actionPerformed(
													ActionEvent evt) {
												Fermentable f = (Fermentable) cmbMaltModel
														.getSelectedItem();
												int i = tblMalt
														.getSelectedRow();
												if (myRecipe != null) {
													Fermentable f2 = (Fermentable) myRecipe
															.getFermentablesList()
															.get(i);
													f2.setLov(f.getLov());
													f2.setPppg(f.getPppg());
												}						

											}
										});
							
								
								
								
							}
						}
						{
							tblMaltTotalsModel = new DefaultTableModel(
									new String[][]{{""}}, new String[]{"Malt",
											"Amount", "Units", "Points", "Lov",
											"Cost/U", "%"});
							tblMaltTotals = new JTable();
							pnlMalt.add(tblMaltTotals, BorderLayout.SOUTH);
							tblMaltTotals.setModel(tblMaltTotalsModel);
							tblMaltTotals.getTableHeader().setEnabled(false);							

						}
					}
					{
						pnlHops = new JPanel();
						BorderLayout pnlHopsLayout = new BorderLayout();
						pnlHops
								.setBorder(BorderFactory.createTitledBorder(
										new LineBorder(new java.awt.Color(0, 0,
												0), 1, false), "Hops",
										TitledBorder.LEADING, TitledBorder.TOP,
										new java.awt.Font("Dialog", 1, 12),
										new java.awt.Color(51, 51, 51)));
						pnlHops.setLayout(pnlHopsLayout);
						pnlTables.add(pnlHops);
						{
							tblHopsTotalsModel = new DefaultTableModel(
									new String[][]{{""}}, new String[]{
											"Column 1", "Column 2"});
							tblHopsTotals = new JTable();
							pnlHops.add(tblHopsTotals, BorderLayout.SOUTH);
							tblHopsTotals.setModel(tblHopsTotalsModel);

						}
						{
							jScrollPane2 = new JScrollPane();
							pnlHops.add(jScrollPane2, BorderLayout.CENTER);
							{
								tblHopsModel = new HopsTableModel(this);
								tblHops = new JTable();
								jScrollPane2.setViewportView(tblHops);
								BorderLayout tblHopsLayout = new BorderLayout();
								tblHops.setLayout(tblHopsLayout);
								tblHops.setModel(tblHopsModel);
								TableColumn hopColumn = tblHops.getColumnModel().getColumn(0);
								JComboBox hopComboBox = new JComboBox();
								cmbHopsModel = new ComboModel();
								hopComboBox.setModel(cmbHopsModel);
								hopColumn.setCellEditor(new DefaultCellEditor(hopComboBox));
							
								

							}
						}
					}

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
					}
					{
						openFileMenuItem = new JMenuItem();
						jMenu3.add(openFileMenuItem);
						openFileMenuItem.setText("Open");
						openFileMenuItem
								.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent evt) {
										String path = getClass()
												.getProtectionDomain()
												.getCodeSource().getLocation()
												.toString().substring(6)
												+ "\\";
										JFileChooser fc = new JFileChooser(path);

										// Show open dialog; this method does
										// not return until the dialog is closed
										int returnVal = fc
												.showOpenDialog(jMenuBar1);
										if (returnVal == JFileChooser.APPROVE_OPTION) {
											File file = fc.getSelectedFile();

											System.out.print("Opening: "
													+ file.getName() + ".\n");
											ImportXml imp = new ImportXml(file
													.toString());
											myRecipe = imp.handler.getRecipe();
											myRecipe.calcMaltTotals();
											myRecipe.calcHopsTotals();
											myRecipe.mash
													.setMaltWeight(myRecipe
															.getTotalMashLbs());
											myRecipe.mash.calcMashSchedule();
											displayRecipe();
										} else {
											System.out
													.print("Open command cancelled by user.\n");
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
							public void actionPerformed(ActionEvent evt) {
								// This is just a test right now to see that
								// stuff is changed.
								System.out.print(myRecipe.toText());

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
							exportHTMLmenu
									.addActionListener(new ActionListener() {
										public void actionPerformed (
												ActionEvent evt) {
											
											try{
											saveAsHTML();
											} catch (Exception e){
												
											}

										}
									});
						}
					}
					{
						closeFileMenuItem = new JMenuItem();
						jMenu3.add(closeFileMenuItem);
						closeFileMenuItem.setText("Close");
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

	public void saveAsHTML() throws Exception{
			// save file as xml, then transform it to html
			File tmp = new File("tmp.xml");
			FileWriter out = new FileWriter(tmp);
			out.write(myRecipe.toXML());
			out.close();
			String htmlFileName = myRecipe.getName() + ".html";
			File htmlFile = new File(htmlFileName);
			FileOutputStream output = new java.io.FileOutputStream(htmlFile);
			XmlTransformer.writeStream("tmp.xml",
					"src/strangebrew/data/recipeToHtml.xslt", output);

	}
	
	public class SpinnerEditor extends AbstractCellEditor
			implements
				TableCellEditor {
		final JSpinner spinner = new JSpinner();

		// Initializes the spinner.
		public SpinnerEditor() {

		}

		// Prepares the spinner component and returns it.
		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected, int row, int column) {
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

}