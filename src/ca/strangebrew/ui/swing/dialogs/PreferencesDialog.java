/*
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

package ca.strangebrew.ui.swing.dialogs;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ca.strangebrew.BrewCalcs;
import ca.strangebrew.Database;
import ca.strangebrew.Debug;
import ca.strangebrew.Hop;
import ca.strangebrew.Options;
import ca.strangebrew.Quantity;
import ca.strangebrew.SBStringUtils;
import ca.strangebrew.WaterProfile;
import ca.strangebrew.ui.swing.ComboModel;
import ca.strangebrew.ui.swing.SmartComboBox;
import ca.strangebrew.ui.swing.StrangeSwing;


public class PreferencesDialog extends javax.swing.JDialog implements ActionListener, ChangeListener {

	// Mutables
	private Options opts;

	// Final UI elements
	final private GridBagLayout gridBag = new GridBagLayout();
	final private JPanel pnlBrewer = new JPanel();
	final private JTextField txtBottleSize = new JTextField();
	final private JButton okButton = new JButton();
	final private JButton cancelButton = new JButton();
	final private JLabel jLabel4 = new JLabel();
	final private JPanel pnlCalculations = new JPanel();
	final private JTextField txtEmail = new JTextField();
	final private JLabel jLabel7 = new JLabel();
	final private JTextField txtClubName = new JTextField();
	final private JLabel jLabel6 = new JLabel();
	final private JTextField txtPhone = new JTextField();
	final private JLabel jLabel5 = new JLabel();
	final private JTextField txtBrewerName = new JTextField();
	
	final private JLabel jLabelRecipe = new JLabel();
	final private JTextField txtRecipe = new JTextField();
	
	final private JPanel carbPanel = new JPanel();
	final private JComboBox cmbBottleSize = new JComboBox();
	final private ComboModel<String> cmbBottleSizeModel = new ComboModel<String>();
	final private JLabel jLabel2 = new JLabel();
	final private JTextField txtOtherCost = new JTextField();
	final private JLabel jLabel1 = new JLabel();
	final private JPanel jPanel2 = new JPanel();
	final private JPanel costCarbPanel = new JPanel();
	final private JTabbedPane jTabbedPane1 = new JTabbedPane();

	
	// calcs panel:
	final private JPanel pnlHopsCalc = new JPanel();
	final private ButtonGroup bgHopsCalc = new ButtonGroup();
	final private JRadioButton rbTinseth = new JRadioButton();
	final private JRadioButton rbGaretz = new JRadioButton();
	final private JRadioButton rbRager = new JRadioButton();

	final private JPanel pnlAlc = new JPanel();
	final private ButtonGroup bgAlc = new ButtonGroup();
	final private JRadioButton rbABW = new JRadioButton();
	final private JRadioButton rbABV = new JRadioButton();

	final private JPanel pnlEvaporation = new JPanel();
	final private ButtonGroup bgEvap = new ButtonGroup();
	final private JRadioButton rbConstant = new JRadioButton();
	final private JRadioButton rbPercent = new JRadioButton();

	final private JPanel pnlColourOptions = new JPanel();
	final private ButtonGroup bgColour = new ButtonGroup();
	final private JRadioButton rbEBC = new JRadioButton();
	final private JRadioButton rbSRM = new JRadioButton();

	final private JPanel pnlHops = new JPanel();
	final private JTextField txtPellet = new JTextField();
	final private JLabel jLabelc2 = new JLabel();
	final private JTextField txtTinsethUtil = new JTextField();
	final private JLabel jLabelc4 = new JLabel();
	final private JTextField txtFWHTime = new JTextField();
	private JComboBox comboStyleYear;
	final private JTextField boilTimeTxt = new JTextField();
	final private JLabel jLabel30 = new JLabel();
	final private JComboBox mashRatioUCombo = new JComboBox();
	final private JTextField mashRatioTxt = new JTextField();
	final private JLabel jLabel29 = new JLabel();
	final private JComboBox mashVolCombo = new JComboBox();
	final private ComboModel<String> mashVolComboModel = new ComboModel<String>();
	final private JLabel jLabel28 = new JLabel();
	final private JRadioButton crb = new JRadioButton();
	final private JRadioButton frb = new JRadioButton();
	final private JLabel jLabel27 = new JLabel();
	final private ButtonGroup tempUBG = new ButtonGroup();
	final private JLabel boilTempULbl = new JLabel();
	final private JLabel evapAmountLbl = new JLabel();
	final private JTextField evapAmountTxt = new JTextField();
	final private JPanel jPanel3 = new JPanel();
	final private JTextField batchSizeTxt = new JTextField();
	final private JComboBox volUnitsCombo = new JComboBox();
	final private JComboBox hopsUnitsCombo = new JComboBox();
	final private JComboBox hopsTypeCombo = new JComboBox();
	final private JComboBox maltUnitsCombo = new JComboBox();
	final private ComboModel<String> maltUnitsComboModel = new ComboModel<String>();
	final private ComboModel<String> hopsUnitsComboModel = new ComboModel<String>();
	final private ComboModel<String> hopsTypeComboModel = new ComboModel<String>();
	final private ComboModel<String> volUnitsComboModel = new ComboModel<String>();
	final private JPanel miscPanel = new JPanel();
	final private JLabel hopsTypeLabel = new JLabel();
	final private JLabel jLabel26 = new JLabel();
	final private JLabel jLabel25 = new JLabel();
	final private JLabel jLabel24 = new JLabel();
	final private JPanel unitsPanel = new JPanel();
	final private JLabel jLabel23 = new JLabel();
	final private JLabel jLabel22 = new JLabel();
	final private JLabel jLabel21 = new JLabel();
	final private JLabel jLabel20 = new JLabel();
	final private JSpinner alphaSpn = new JSpinner();
	final private JSpinner blueSpn = new JSpinner();
	final private JSpinner greenSpn = new JSpinner();
	final private JSpinner redSpn = new JSpinner();
	final private JLabel jLabel19 = new JLabel();
	final private JPanel blackPanel = new JPanel();
	final private JPanel brownPanel = new JPanel();
	final private JPanel copperPanel = new JPanel();
	final private JPanel amberPanel = new JPanel();
	final private JPanel palePanel = new JPanel();
	final private JPanel stawPanel = new JPanel();
	final private JLabel jLabel18 = new JLabel();
	final private JLabel jLabel17 = new JLabel();
	final private JLabel jLabel16 = new JLabel();
	final private JLabel jLabel15 = new JLabel();
	final private JLabel jLabel14 = new JLabel();
	final private JLabel jLabel13 = new JLabel();
	final private JRadioButton colMethod2rb = new JRadioButton();
	final private JRadioButton colMethod1rb = new JRadioButton();
	final private ButtonGroup colourGroup = new ButtonGroup();
	final private JPanel colourPanel = new JPanel();
	
	final private JPanel appearancePanel = new JPanel();
	final private JTextField boilTempTxt = new JTextField();
	final private JLabel jLabel12 = new JLabel();
	final private JPanel mashPanel = new JPanel();
	final private JPanel newRecipePanel = new JPanel();
	final private JTextField txtLostInTrub = new JTextField();
	final private JTextField txtMiscLosses = new JTextField();
	final private JTextField txtLeftInKettle = new JTextField();
	final private JLabel jLabel11 = new JLabel();
	final private JLabel jLabel10 = new JLabel();
	final private JLabel jLabel9 = new JLabel();
	final private JPanel pnlWaterUsage = new JPanel();
	final private JPanel pnlDatabase = new JPanel();
	final private JTextField txtMashHopTime = new JTextField();
	final private JLabel jLabelc5 = new JLabel();
	final private JTextField txtDryHopTime = new JTextField();
	final private JLabel jLabelc3 = new JLabel();
	final private JPanel pnlHopTimes = new JPanel();

	final private JLabel jLabelc1 = new JLabel();
	final private JPanel pnlDefaultDB = new JPanel();

	final private JLabel localeLabel = new JLabel();
	final private JComboBox localeComboBox = new JComboBox();
	final private JLabel defaultLocaleLable = new JLabel();
	final private JLabel swingTheme = new JLabel();
	private JComboBox swingComboBox = new JComboBox();
	final private JLabel currentTheme = new JLabel();
	
	// Carb
	final private GridBagLayout layoutCarbPanel = new GridBagLayout();
	final private GridBagConstraints constraints = new GridBagConstraints();
	final private JLabel labelPrimeSugar = new JLabel("Prime Sugar: ");
	final private JLabel labelSugarU = new JLabel("Sugar Weight Unit: ");
	final private JLabel labelCarbTempU = new JLabel("Carb Temp Unit: ");
	final private JLabel labelServTemp = new JLabel("Serving Temp: ");
	final private JLabel labelBottleTemp = new JLabel("Bottle Temp: ");
	final private JLabel labelVol = new JLabel("Target C02 Volumn: ");
	final private JComboBox comboPrimeSugar = new JComboBox(Database.getInstance().getPrimeSugarNameList());
	// TODO come back and fix all the temperature selection everywhere to use some sort of static list from
	// somewhere. Probably Recipe, Options or Database!
	final private JComboBox comboCarbTempU = new JComboBox(new String[] {"F", "C"});
	final private JComboBox comboSugarU = new JComboBox(Quantity.getListofUnits("weight").toArray());
	final private JTextField txtServTemp = new JTextField();
	final private JTextField txtBottleTemp = new JTextField();
	final private JTextField txtVol = new JTextField();
	final private JCheckBox checkKegged = new JCheckBox("Kegged");
	final private JLabel lHeight = new JLabel("Height to Faucet: ");
	final private JLabel lTubingID = new JLabel("Tubing ID: ");
	final private JTextField textHeight = new JTextField();
	// TODO needs to be pulled out of some equipment class we don't have
	// from the equipment managemnt that we haven't done yet!
	final private JComboBox comboTubingID = new JComboBox(new String[] {"3/16", "1/4"});	
	
	// default water profile and selected salts
	final private JLabel lWaterProfile = new JLabel("Water Profile: ");
	final private JComboBox comboWaterProfile = new JComboBox();
	
/*	private ArrayList looks;*/
	
	final private Frame sb;

	public PreferencesDialog(Frame owner) {
		super(owner, "Recipe Preferences", true);
		opts = Options.getInstance();
		sb = owner;
		
		layoutUi();		
		setLocation(owner.getLocation());
		setOptions();				
		
	}

	class Looks {
		String name;
		String value;
		
		public Looks(String n, String v) {
			name = n;
			value = v;
		}
		
		public String toString() { return name; }
		public String getValue() { return value; }
	}
	
	private void setOptions() {
		
		// new recipe tab:		
		batchSizeTxt.setText(opts.getProperty("optPostBoilVol"));
		maltUnitsComboModel.addOrInsert(opts.getProperty("optMaltU"));
		hopsUnitsComboModel.addOrInsert(opts.getProperty("optHopsU"));
		volUnitsComboModel.addOrInsert(opts.getProperty("optSizeU"));
		boilTimeTxt.setText(opts.getProperty("optBoilTime"));
		hopsTypeComboModel.addOrInsert(opts.getProperty("optHopsType"));
		List<WaterProfile> db = Database.getInstance().waterDB;
		for (int i = 0; i < db.size(); i++) {
			comboWaterProfile.addItem(db.get(i).getName());
		}		
		comboWaterProfile.setSelectedItem(opts.getProperty("optWaterProfile"));
		
		// Not sure why we need to convert every time we save, but keeping it like this for now
		if(opts.getProperty("optMashTempU").equalsIgnoreCase("C")){
			boilTempTxt.setText(Double.toString(BrewCalcs.fToC(opts.getDProperty("optBoilTempF"))));
		} else {		
			boilTempTxt.setText(opts.getProperty("optBoilTempF"));
		}
		
		mashVolComboModel.addOrInsert(opts.getProperty("optMashVolU"));
		frb.setSelected(opts.getProperty("optMashTempU").equalsIgnoreCase("F"));
		crb.setSelected(opts.getProperty("optMashTempU").equalsIgnoreCase("C"));
		mashRatioUCombo.setSelectedItem(opts.getProperty("optMashRatioU"));
		mashRatioTxt.setText(opts.getProperty("optMashRatio"));
		
		// cost/carb tab:
		txtOtherCost.setText(opts.getProperty("optMiscCost"));
		txtBottleSize.setText(opts.getProperty("optBottleSize"));
		cmbBottleSizeModel.addOrInsert(opts.getProperty("optBottleU"));
		comboPrimeSugar.setSelectedItem(opts.getProperty("optPrimingSugar"));
		comboSugarU.setSelectedItem(opts.getProperty("optSugarU"));
		comboCarbTempU.setSelectedItem(opts.getProperty("optCarbTempU"));
		txtBottleTemp.setText(opts.getProperty("optBottleTemp"));
		txtServTemp.setText(opts.getProperty("optServTemp"));
		txtVol.setText(opts.getProperty("optVolsCO2"));
		checkKegged.setSelected(opts.getBProperty("optKegged"));
		comboTubingID.setSelectedItem(opts.getProperty("optTubingID"));
		textHeight.setText(opts.getProperty("optHeightAboveKeg"));
		
		// brewer tab:
		txtBrewerName.setText(opts.getProperty("optBrewer"));
		txtPhone.setText(opts.getProperty("optPhone"));
		txtClubName.setText(opts.getProperty("optClub"));
		txtEmail.setText(opts.getProperty("optEmail"));
		String recipeDir = opts.getProperty("optRecipe");
		if((recipeDir == null) || recipeDir.equalsIgnoreCase("") ) {
			try {
				recipeDir = SBStringUtils.getAppPath("recipes");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		txtRecipe.setText(recipeDir);
		localeComboBox.setSelectedItem(opts.getLocale());		

		// calculations tab:
		rbTinseth.setSelected(opts.getProperty("optIBUCalcMethod").equalsIgnoreCase(BrewCalcs.TINSETH));
		rbRager.setSelected(opts.getProperty("optIBUCalcMethod").equalsIgnoreCase(BrewCalcs.RAGER));
		rbGaretz.setSelected(opts.getProperty("optIBUCalcMethod").equalsIgnoreCase(BrewCalcs.GARTEZ));

		rbABV.setSelected((opts.getProperty("optAlcCalcMethod").equalsIgnoreCase("Volume")));
		rbABW.setSelected((opts.getProperty("optAlcCalcMethod").equalsIgnoreCase("Weight")));

		rbSRM.setSelected((opts.getProperty("optColourMethod").equalsIgnoreCase(BrewCalcs.SRM)));
		rbEBC.setSelected((opts.getProperty("optColourMethod").equalsIgnoreCase(BrewCalcs.EBC)));

		rbPercent.setSelected((opts.getProperty("optEvapCalcMethod").equalsIgnoreCase("Percent")));
		rbConstant.setSelected((opts.getProperty("optEvapCalcMethod").equalsIgnoreCase("Constant")));
		evapAmountTxt.setText(opts.getProperty("optEvaporation"));
		setEvapLable();
		
		txtPellet.setText(opts.getProperty("optPelletHopsPct"));
		txtTinsethUtil.setText(opts.getProperty("optHopsUtil"));
		txtDryHopTime.setText(opts.getProperty("optDryHopTime"));
		txtFWHTime.setText(opts.getProperty("optFWHTime"));
		txtMashHopTime.setText(opts.getProperty("optMashHopTime"));
		txtLeftInKettle.setText(opts.getProperty("optKettleLoss"));
		txtMiscLosses.setText(opts.getProperty("optMiscLoss"));
		txtLostInTrub.setText(opts.getProperty("optTrubLoss"));		
		
		// appearances tab:
		redSpn.setValue(new Integer(opts.getIProperty("optRed")));
		greenSpn.setValue(new Integer(opts.getIProperty("optGreen")));
		blueSpn.setValue(new Integer(opts.getIProperty("optBlue")));
		alphaSpn.setValue(new Integer(opts.getIProperty("optAlpha")));
		colMethod1rb.setSelected(opts.getProperty("optRGBMethod").equals("1"));
		colMethod2rb.setSelected(opts.getProperty("optRGBMethod").equals("2"));
		displayColour();
		
		// style DB tab:		
		comboStyleYear.setSelectedItem(opts.getProperty("optStyleYear"));
		
	}

	private void saveOptions() {
		
		// overall appearance
		opts.setProperty("optsPrefDiagHeight", String.valueOf(getContentPane().getHeight()));
		opts.setProperty("optsPrefDiagWidth", String.valueOf(getContentPane().getWidth()));
		
		// cost/carb tab:
		opts.setProperty("optMiscCost", txtOtherCost.getText());
		opts.setProperty("optBottleSize", txtBottleSize.getText());
		opts.setProperty("optBottleU", (String)cmbBottleSizeModel.getSelectedItem());
		opts.setProperty("optPrimingSugar", (String)comboPrimeSugar.getSelectedItem());
		opts.setProperty("optSugarU", (String)comboSugarU.getSelectedItem());
		opts.setProperty("optCarbTempU", (String)comboCarbTempU.getSelectedItem());
		opts.setProperty("optBottleTemp", txtBottleTemp.getText());
		opts.setProperty("optServTemp", txtServTemp.getText());
		opts.setProperty("optVolsCO2", txtVol.getText());
		opts.setProperty("optTubingID", (String)comboTubingID.getSelectedItem());
		opts.setProperty("optHeightAboveKeg", textHeight.getText());
		if (checkKegged.isSelected()) {
			opts.setProperty("optKegged", "true");			
		} else {
			opts.setProperty("optKegged", "false");
		}

		// Brewer tab:
		opts.setProperty("optBrewer", txtBrewerName.getText());
		opts.setProperty("optPhone", txtPhone.getText());
		opts.setProperty("optClub", txtClubName.getText());
		opts.setProperty("optEmail", txtEmail.getText());
		opts.setProperty("optRecipe", txtRecipe.getText());
		opts.setProperty("optAppearance", ((Looks)swingComboBox.getSelectedItem()).value);
		
		// TODO
		opts.setLocale((Locale)localeComboBox.getSelectedItem());

		// calculations tab:
		if (rbTinseth.isSelected())
			opts.setProperty("optIBUCalcMethod", BrewCalcs.TINSETH);
		if (rbRager.isSelected())
			opts.setProperty("optIBUCalcMethod", BrewCalcs.RAGER);
		if (rbGaretz.isSelected())
			opts.setProperty("optIBUCalcMethod", BrewCalcs.GARTEZ);

		if (rbABV.isSelected())
			opts.setProperty("optAlcCalcMethod", "Volume");
		if (rbABW.isSelected())
			opts.setProperty("optAlcCalcMethod", "Weight");

		if (rbSRM.isSelected())
			opts.setProperty("optColourMethod", BrewCalcs.SRM);
		if (rbEBC.isSelected())
			opts.setProperty("optColourMethod", BrewCalcs.EBC);

		if (rbPercent.isSelected())
			opts.setProperty("optEvapCalcMethod", "Percent");
		if (rbConstant.isSelected())
			opts.setProperty("optEvapCalcMethod", "Constant");
		
		opts.setProperty("optEvaporation", evapAmountTxt.getText());

		opts.setProperty("optPelletHopsPct", txtPellet.getText());
		opts.setProperty("optHopsUtil", txtTinsethUtil.getText());
		opts.setProperty("optDryHopTime", txtDryHopTime.getText());
		opts.setProperty("optFWHTime", txtFWHTime.getText());
		opts.setProperty("optMashHopTime", txtMashHopTime.getText());
		opts.setProperty("optKettleLoss", txtLeftInKettle.getText());
		opts.setProperty("optMiscLoss", txtMiscLosses.getText());
		opts.setProperty("optTrubLoss", txtLostInTrub.getText());

		// new recipe tab:		
		opts.setProperty("optPostBoilVol", batchSizeTxt.getText());
		opts.setProperty("optMaltU", maltUnitsComboModel.getSelectedItem().toString());
		opts.setProperty("optHopsU", hopsUnitsComboModel.getSelectedItem().toString());
		opts.setProperty("optSizeU", volUnitsComboModel.getSelectedItem().toString());
		opts.setProperty("optBoilTime", boilTimeTxt.getText());
		opts.setProperty("optHopsType", hopsTypeComboModel.getSelectedItem().toString());
		opts.setProperty("optMashVolU", mashVolComboModel.getSelectedItem().toString());
		opts.setProperty("optWaterProfile", comboWaterProfile.getSelectedItem().toString());
		
		if (frb.isSelected()){
			opts.setProperty("optMashTempU", "F");
			opts.setProperty("optBoilTempF", boilTempTxt.getText());
		}
		else {
			opts.setProperty("optMashTempU", "C");
			double t = Double.parseDouble(boilTempTxt.getText());
			opts.setDProperty("optBoilTempF", BrewCalcs.cToF(t));
		}
		
		opts.setProperty("optMashRatioU", mashRatioUCombo.getSelectedItem().toString());
		opts.setProperty("optMashRatio", mashRatioTxt.getText());
		
		// appearances:
		
		opts.setProperty("optRed", redSpn.getValue().toString());
		opts.setProperty("optGreen", greenSpn.getValue().toString());
		opts.setProperty("optBlue", blueSpn.getValue().toString());
		opts.setProperty("optAlpha", alphaSpn.getValue().toString());
		if (colMethod1rb.isSelected())
			opts.setProperty("optRGBMethod", "1");
		else 
			opts.setProperty("optRGBMethod", "2");
		
		opts.setProperty("optStyleYear", comboStyleYear.getSelectedItem().toString());
	
	}

	private void layoutUi() {

		JPanel buttons = new JPanel();
		okButton.setText("OK");
		okButton.addActionListener(this);
		cancelButton.setText("Cancel");
		cancelButton.addActionListener(this);
		buttons.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttons.add(cancelButton);
		buttons.add(okButton);

		getContentPane().setLayout(new BorderLayout());
		this.setFocusTraversalKeysEnabled(false);
		{			
			getContentPane().add(jTabbedPane1, BorderLayout.CENTER);
			{
				jTabbedPane1.addTab("Calculations", null, pnlCalculations, null);
				{
					try {
						{
							GridBagLayout thisLayout = new GridBagLayout();
							thisLayout.rowWeights = new double[]{0.1, 0.1, 0.1, 0.1};
							thisLayout.rowHeights = new int[]{7, 7, 7, 7};
							thisLayout.columnWeights = new double[]{0.1, 0.2};
							thisLayout.columnWidths = new int[]{7, 7};
							pnlCalculations.setLayout(thisLayout);
							pnlCalculations.setPreferredSize(new java.awt.Dimension(524, 372));
							{
								{
									{
										GridLayout pnlHopsLayout = new GridLayout(2, 2);
										pnlHopsLayout.setColumns(2);
										pnlHopsLayout.setHgap(5);
										pnlHopsLayout.setVgap(5);
										pnlHopsLayout.setRows(2);
										pnlHops.setLayout(pnlHopsLayout);
										pnlCalculations.add(pnlHops, new GridBagConstraints(1, 0,
												1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
												GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0,
														0), 0, 0));
										pnlHops
										.setBorder(BorderFactory
												.createTitledBorder("Hops:"));
										{
											pnlHops.add(jLabelc1);
											jLabelc1.setText("Pellet Hops +%");
										}
										{
											pnlHops.add(txtPellet);
											txtPellet.setPreferredSize(new java.awt.Dimension(20,
													20));
										}
										{
											pnlHops.add(jLabelc2);
											jLabelc2.setText("Tinseth Utilization Factor");
										}
										{
											pnlHops.add(txtTinsethUtil);
											txtTinsethUtil.setText("4.15");
										}
									}
									{
										BoxLayout pnlAlcLayout = new BoxLayout(pnlAlc,
												javax.swing.BoxLayout.Y_AXIS);
										pnlAlc.setLayout(pnlAlcLayout);
										pnlCalculations.add(pnlAlc, new GridBagConstraints(0, 1, 1,
												1, 0.0, 0.0, GridBagConstraints.NORTH,
												GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0,
														0), 0, 0));
										pnlAlc.setBorder(BorderFactory
												.createTitledBorder("Alcohol By:"));
										{
											pnlAlc.add(rbABV);
											bgAlc.add(rbABV);
											rbABV.setText("Volume");
										}
										{
											pnlAlc.add(rbABW);
											bgAlc.add(rbABW);
											rbABW.setText("Weight");
										}
									}
									{
										GridLayout pnlHopTimesLayout = new GridLayout(3, 2);
										pnlHopTimesLayout.setColumns(2);
										pnlHopTimesLayout.setHgap(5);
										pnlHopTimesLayout.setVgap(5);
										pnlHopTimesLayout.setRows(3);
										pnlHopTimes.setLayout(pnlHopTimesLayout);
										pnlCalculations.add(pnlHopTimes, new GridBagConstraints(1,
												1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
												GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0,
														0), 0, 0));
										pnlHopTimes.setBorder(BorderFactory
												.createTitledBorder("Hop Times:"));
										{
											pnlHopTimes.add(jLabelc3);
											jLabelc3.setText("Dry (min):");
										}
										{
											pnlHopTimes.add(txtDryHopTime);
											txtDryHopTime.setText("0.0");
										}
										{
											pnlHopTimes.add(jLabelc4);
											jLabelc4.setText("FWH, boil minus (min):");
										}
										{
											pnlHopTimes.add(txtFWHTime);
											txtFWHTime.setText("20.0");
										}
										{
											pnlHopTimes.add(jLabelc5);
											jLabelc5.setText("Mash Hop (min):");
										}
										{
											pnlHopTimes.add(txtMashHopTime);
											txtMashHopTime.setText("2.0");
										}
									}
								}
								BoxLayout pnlHopsCalcLayout = new BoxLayout(pnlHopsCalc,
										javax.swing.BoxLayout.Y_AXIS);
								pnlHopsCalc.setLayout(pnlHopsCalcLayout);
								pnlCalculations.add(pnlHopsCalc,
										new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
												GridBagConstraints.NORTH,
												GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0,
														0), 0, 0));
								pnlCalculations.add(pnlWaterUsage,
										new GridBagConstraints(1, 2, 1, 2, 0.0, 0.0,
												GridBagConstraints.NORTHWEST,
												GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0,
														0), 0, 0));
								pnlCalculations.add(pnlColourOptions,
										new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
												GridBagConstraints.NORTH,
												GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0,
														0), 0, 0));
								pnlCalculations.add(pnlEvaporation,
										new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
												GridBagConstraints.NORTH,
												GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0,
														0), 0, 0));
								pnlHopsCalc.setPreferredSize(new java.awt.Dimension(117, 107));
								pnlHopsCalc.setBorder(BorderFactory
										.createTitledBorder("IBU Calc Method:"));
								{
									pnlHopsCalc.add(rbTinseth);
									rbTinseth.setText("Tinseth");
									bgHopsCalc.add(rbTinseth);
								}
								{
									pnlHopsCalc.add(rbRager);
									rbRager.setText("Rager");
									bgHopsCalc.add(rbRager);
								}
								{
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
				BorderLayout costCarbPanelLayout = new BorderLayout();
				costCarbPanel.setLayout(costCarbPanelLayout);
				jTabbedPane1.addTab("Cost & Carb", null, costCarbPanel, null);
				{
					costCarbPanel.add(carbPanel, BorderLayout.CENTER);					
					layoutCarbPanel.rowWeights = new double[]{0.1, 0.1, 0.4};
					layoutCarbPanel.rowHeights = new int[]{7, 7, 7};
					layoutCarbPanel.columnWeights = new double[]{0.1, 0.1, 0.1};
					layoutCarbPanel.columnWidths = new int[]{7, 7, 7};
					carbPanel.setLayout(layoutCarbPanel);				
					carbPanel.setBorder(BorderFactory.createTitledBorder(null, "Carbonation",
							TitledBorder.LEADING, TitledBorder.TOP));
					{
						// carbPanel
						constraints.fill = GridBagConstraints.HORIZONTAL;
						// Prime Sugar and U - line 1
						constraints.gridx = 0;
						constraints.gridy = 0;
						carbPanel.add(labelPrimeSugar, constraints);
						constraints.gridx = 1;
						carbPanel.add(comboPrimeSugar, constraints);						
						constraints.gridx = 2;
						carbPanel.add(labelSugarU, constraints);
						constraints.gridx = 3;
						carbPanel.add(comboSugarU, constraints);
						
						// Serv temp and U - line 2
						constraints.gridx = 0;
						constraints.gridy = 1;
						carbPanel.add(labelServTemp, constraints);
						constraints.ipadx = 30;
						constraints.gridx = 1;
						carbPanel.add(txtServTemp, constraints);
						constraints.ipadx = 0;
						constraints.gridx = 2;
						carbPanel.add(labelCarbTempU, constraints);
						constraints.gridx = 3;
						constraints.ipadx = 30;
						carbPanel.add(comboCarbTempU, constraints);
						constraints.ipadx = 0;
						
						// Bottle temp, Co2 Vol - line 3
						constraints.gridx = 0;
						constraints.gridy = 2;
						carbPanel.add(labelBottleTemp, constraints);
						constraints.gridx = 1;
						constraints.ipadx = 30;
						carbPanel.add(txtBottleTemp, constraints);
						constraints.gridx = 2;
						carbPanel.add(labelVol, constraints);
						constraints.gridx = 3;
						constraints.ipadx = 30;
						carbPanel.add(txtVol, constraints);
						constraints.ipadx = 0;

						// height and tube ID - line 4
						constraints.gridx = 0;
						constraints.gridy = 3;
						carbPanel.add(lHeight, constraints);
						constraints.gridx = 1;
						constraints.ipadx = 30;
						carbPanel.add(textHeight, constraints);
						constraints.ipadx = 0;						
						constraints.gridx = 2;
						carbPanel.add(lTubingID, constraints);
						constraints.gridx = 3;
						constraints.ipadx = 30;
						carbPanel.add(comboTubingID, constraints);
						constraints.ipadx = 0;

						// Kegged - line 5
						constraints.ipadx = 0;			
						constraints.gridx = 0;
						constraints.gridy = 5;
						constraints.gridwidth = 2;
						carbPanel.add(checkKegged, constraints);						
						constraints.gridwidth = 1;
					}
				}
				{
					costCarbPanel.add(jPanel2, BorderLayout.NORTH);
					GridBagLayout jPanel2Layout = new GridBagLayout();
					jPanel2Layout.rowWeights = new double[]{0.1, 0.1, 0.4};
					jPanel2Layout.rowHeights = new int[]{7, 7, 7};
					jPanel2Layout.columnWeights = new double[]{0.1, 0.1, 0.1};
					jPanel2Layout.columnWidths = new int[]{7, 7, 7};

					jPanel2.setPreferredSize(new java.awt.Dimension(232, 176));
					jPanel2.setBorder(BorderFactory.createTitledBorder("Cost"));
					jPanel2.setLayout(jPanel2Layout);
					{
						jPanel2.add(jLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						jLabel1.setText("Other Cost:");
					}
					{
						jPanel2.add(txtOtherCost, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						txtOtherCost.setText("$0.00");
						txtOtherCost.setPreferredSize(new java.awt.Dimension(62, 20));
					}
					{
						jPanel2.add(jLabel2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						jLabel2.setText("Bottle Size:");
					}
					{
						SmartComboBox.enable(cmbBottleSize);
						cmbBottleSizeModel.setList(Quantity.getListofUnits("vol"));
						cmbBottleSize.setModel(cmbBottleSizeModel);
						jPanel2.add(cmbBottleSize, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						cmbBottleSize.setPreferredSize(new java.awt.Dimension(89, 20));
						jPanel2.add(txtBottleSize, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

					}
				}
				{

				}
			}
			{
				GridBagLayout pnlBrewerLayout = new GridBagLayout();
				pnlBrewerLayout.rowWeights = new double[]{0.1, 0.1, 0.3, 0.3, 0.3};
				pnlBrewerLayout.rowHeights = new int[]{2, 2, 7, 7, 7};
				pnlBrewerLayout.columnWeights = new double[]{0.1, 0.1, 0.1, 0.1};
				pnlBrewerLayout.columnWidths = new int[]{7, 7, 7, 7};
				pnlBrewer.setLayout(pnlBrewerLayout);
				jTabbedPane1.addTab("Brewer", null, pnlBrewer, null);
				jTabbedPane1.addTab("Style Database", null, pnlDatabase, null);
				{
					pnlBrewer.add(jLabel4, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
							GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(0, 0,
									0, 0), 0, 0));
					jLabel4.setText("Name:");
				}
				{
					pnlBrewer.add(txtBrewerName, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
							GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0,
									0, 0, 0), 0, 0));
					txtBrewerName.setText("Your Name");
				}
				{
					pnlBrewer.add(jLabel5, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
							GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(0, 0,
									0, 0), 0, 0));
					jLabel5.setText("Phone:");
				}
				{
					pnlBrewer.add(txtPhone, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
							GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0,
									0, 0, 0), 0, 0));
					txtPhone.setText("Your Phone");
				}
				{
					pnlBrewer.add(jLabel6, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
							GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(0, 0,
									0, 0), 0, 0));
					jLabel6.setText("Club Name:");
				}
				{
					pnlBrewer.add(txtClubName, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
							GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0,
									0, 0, 0), 0, 0));
					txtClubName.setText("Club Name");
				}
				{
					pnlBrewer.add(jLabel7, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
							GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(0, 0,
									0, 0), 0, 0));
					jLabel7.setText("Email:");
				}
				{
					pnlBrewer.add(txtEmail, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0,
							GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0,
									0, 0, 0), 0, 0));
					txtEmail.setText("Email");
				}
				{
					pnlBrewer.add(jLabelRecipe, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
							GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(0, 0,
									0, 0), 0, 0));
					jLabelRecipe.setText("Recipes:");
				}
				{
					pnlBrewer.add(txtRecipe, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,
							GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0,
									0, 0, 0), 0, 0));
					
					txtEmail.setText("Recipe Directory");
				}			
				
				
				{
					pnlBrewer.add(localeLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
							GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(0, 0,
									0, 0), 0, 0));
					localeLabel.setText("Set Locale:");
				}
				{
					pnlBrewer.add(localeComboBox, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
							GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0,
									0, 0, 0), 0, 0));					
					Locale[] l = Locale.getAvailableLocales();
					Locale t;
					// Cheapass bubble sort to avoid using 1.5 <type>'s with a comparator
					for (int i = 0; i < l.length - 1; i++) {
						for (int j = 0; j < l.length -1 - i; j++) {
							if (l[j+1].toString().compareTo(l[j].toString()) < 0) {
								t = l[j];
								l[j] = l[j+1];
								l[j+1] = t;
							}
						}
					}
					for (int i = 0; i < l.length; i++) {
						localeComboBox.addItem(l[i]);
					}
				}
				{
					pnlBrewer.add(defaultLocaleLable, new GridBagConstraints(2, 2, 2, 1, 0.0, 0.0,
							GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0,
									0, 0), 0, 0));
					defaultLocaleLable.setText("System Locale is " + Locale.getDefault().toString());
				}	
				{
					
					
					
					
					ArrayList<Looks> looks = new ArrayList<Looks>();
					
					for(UIManager.LookAndFeelInfo look : UIManager.getInstalledLookAndFeels()) {
						
						looks.add(new Looks(look.getName(), look.getClassName()));
						
					}
					
					swingComboBox = new JComboBox(looks.toArray());
					pnlBrewer.add(swingComboBox, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0,
							GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0,
									0, 0, 0), 0, 0));	
					
					swingComboBox.addActionListener(new ActionListener() {
						public void actionPerformed (ActionEvent e){
							
							try {
								Debug.print("UIManager trying to set look and feel to " + swingComboBox.getSelectedItem().toString());
								UIManager.setLookAndFeel(((Looks)swingComboBox.getSelectedItem()).value);
								SwingUtilities.updateComponentTreeUI(sb);
								sb.pack();
							} catch (ClassNotFoundException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (InstantiationException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (IllegalAccessException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (UnsupportedLookAndFeelException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					});
					
				}
				{
					pnlBrewer.add(swingTheme, new GridBagConstraints(2, 4, 2, 1, 0.0, 0.0,
							GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0,
									0, 0), 0, 0));
					swingTheme.setText("Swing Theme is " + UIManager.getLookAndFeel().getName());
				}	
			}
			{
				
				BorderLayout pnlDatabaseLayout = new BorderLayout();
				pnlDatabase.setLayout(pnlDatabaseLayout);
				pnlDatabase.add(pnlDefaultDB, BorderLayout.NORTH);
				pnlDatabase.setVisible(false);
			}

			BorderLayout appearancePanelLayout = new BorderLayout();
			appearancePanel.setLayout(appearancePanelLayout);
			jTabbedPane1.addTab("Appearance", null, appearancePanel, null);

/*			landfPanel = new JPanel();
			appearancePanel.add(landfPanel, BorderLayout.NORTH);

			jLabel19 = new JLabel();
			landfPanel.add(jLabel19);
			jLabel19.setText("Look and Feel:");

			landfCombo = new JComboBox(looks.toArray());
			
			landfPanel.add(landfCombo);*/
			
			//appearancePanel.add(swingPanel, BorderLayout.CENTER);
			//GridBagLayout swingPanelLayout = new GridBagLayout();
			
			
			appearancePanel.add(colourPanel, BorderLayout.CENTER);
			GridBagLayout colourPanelLayout = new GridBagLayout();
			colourPanelLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1, 0.1, 0.1};
			colourPanelLayout.rowHeights = new int[] {7, 7, 7, 7, 7, 7};
			colourPanelLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1, 0.1, 0.1};
			colourPanelLayout.columnWidths = new int[] {7, 7, 7, 7, 7, 7};
			colourPanel.setLayout(colourPanelLayout);
			colourPanel.setPreferredSize(new java.awt.Dimension(340, 223));
			colourPanel.setBorder(BorderFactory.createTitledBorder("Colour Swatch"));

			colMethod1rb.addActionListener(this);
			colourGroup.add(colMethod1rb);
			colourPanel.add(colMethod1rb, new GridBagConstraints(0, 0, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			colMethod1rb.setText("Colour Method 1");

			colMethod2rb.addActionListener(this);
			colourGroup.add(colMethod2rb);
			colourPanel.add(colMethod2rb, new GridBagConstraints(3, 0, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			colMethod2rb.setText("Colour Method 2");

			colourPanel.add(jLabel13, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			jLabel13.setText("Straw \n(2)");

			colourPanel.add(jLabel14, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			jLabel14.setText("Pale\n(4)");

			colourPanel.add(jLabel15, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			jLabel15.setText("Amber\n(8)");
			
			colourPanel.add(jLabel16, new GridBagConstraints(3, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			jLabel16.setText("Copper (15)");

			colourPanel.add(jLabel17, new GridBagConstraints(4, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			jLabel17.setText("Brown (20)");

			colourPanel.add(jLabel18, new GridBagConstraints(5, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			jLabel18.setText("Black (30)");

			colourPanel.add(stawPanel, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			stawPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

			colourPanel.add(palePanel, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			palePanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

			colourPanel.add(amberPanel, new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			amberPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

			colourPanel.add(copperPanel, new GridBagConstraints(3, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			copperPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

			colourPanel.add(brownPanel, new GridBagConstraints(4, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			brownPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

			colourPanel.add(blackPanel, new GridBagConstraints(5, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			blackPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

			SpinnerNumberModel redSpnModel = new SpinnerNumberModel(8,0,255,1);

			colourPanel.add(redSpn, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			redSpn.setModel(redSpnModel);
			redSpn.addChangeListener(this);

			SpinnerNumberModel greenSpnModel = new SpinnerNumberModel(30,0,255,1);

			colourPanel.add(greenSpn, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			greenSpn.setModel(greenSpnModel);
			greenSpn.addChangeListener(this);

			SpinnerNumberModel blueSpnModel = new SpinnerNumberModel(20,0,255,1);

			colourPanel.add(blueSpn, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			blueSpn.setModel(blueSpnModel);
			blueSpn.addChangeListener(this);

			SpinnerNumberModel alphaSpnModel = new SpinnerNumberModel(255,0,255,1);

			colourPanel.add(alphaSpn, new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			alphaSpn.setModel(alphaSpnModel);
			alphaSpn.addChangeListener(this);

			colourPanel.add(jLabel20, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jLabel20.setText("Red:");

			colourPanel.add(jLabel21, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jLabel21.setText("Blue:");

			colourPanel.add(jLabel22, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jLabel22.setText("Green:");

			colourPanel.add(jLabel23, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jLabel23.setText("Alpha:");
			
			// New recipe panel
			{
				// Setup main panel
				newRecipePanel.setLayout(new GridLayout(3,0));
				jTabbedPane1.addTab("New Recipe Defaults", null, newRecipePanel, null);	
				
				gridBag.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
				gridBag.rowHeights = new int[] {7, 7, 7, 7};
				gridBag.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
				gridBag.columnWidths = new int[] {7, 7, 7, 7};				
				
				// Set up units
				{
					unitsPanel.setLayout(gridBag);
					newRecipePanel.add(unitsPanel);
					unitsPanel.setBorder(BorderFactory.createTitledBorder("Units"));			
								
					// Malt units
					constraints.fill = GridBagConstraints.HORIZONTAL;
					constraints.gridx = 0;
					constraints.gridy = 0;
					unitsPanel.add(jLabel19, constraints);							
					jLabel19.setText("Malt Units:");
					SmartComboBox.enable(maltUnitsCombo);
					constraints.gridx = 1;
					unitsPanel.add(maltUnitsCombo, constraints);
					maltUnitsComboModel.setList(Quantity.getListofUnits("weight"));
					maltUnitsCombo.setModel(maltUnitsComboModel);
		
					// Hop Units
					constraints.gridx = 0;
					constraints.gridy = 1;
					unitsPanel.add(jLabel24, constraints);
					jLabel24.setText("Hops Units:");		
					constraints.gridx = 1;
					SmartComboBox.enable(hopsUnitsCombo);
					unitsPanel.add(hopsUnitsCombo, constraints);
					hopsUnitsComboModel.setList(Quantity.getListofUnits("weight"));
					hopsUnitsCombo.setModel(hopsUnitsComboModel);
		
					// Vol units
					constraints.gridx = 0;
					constraints.gridy = 2;
					unitsPanel.add(jLabel25, constraints);
					jLabel25.setText("Vol Units:");
					constraints.gridx = 1;
					SmartComboBox.enable(volUnitsCombo);
					volUnitsCombo.addActionListener(new ActionListener() {
						public void actionPerformed (ActionEvent e){
							setEvapLable();
						}
					});
					unitsPanel.add(volUnitsCombo, constraints);
					volUnitsComboModel.setList(Quantity.getListofUnits("vol"));
					volUnitsCombo.setModel(volUnitsComboModel);
		
					// batch size
					constraints.gridx = 0;
					constraints.gridy = 3;
					unitsPanel.add(jLabel30, constraints);
					jLabel30.setText("Batch Size:");
					constraints.gridx = 1;
					unitsPanel.add(batchSizeTxt, constraints);
					batchSizeTxt.setText("jTextField1");
		
					// Boil Time
					constraints.gridx = 0;
					constraints.gridy = 4;
					unitsPanel.add(jLabel26, constraints);
					jLabel26.setText("Boil Time (min):");
					constraints.gridx = 1;
					unitsPanel.add(boilTimeTxt, constraints);
					boilTimeTxt.setText("60");
					boilTimeTxt.setPreferredSize(new java.awt.Dimension(55, 20));
				}
				
				// Mash
				{
					mashPanel.setLayout(gridBag);
					newRecipePanel.add(mashPanel);
					mashPanel.setBorder(BorderFactory.createTitledBorder("Mash"));					

					// Vol units
					constraints.gridx = 0;
					constraints.gridy = 0;
					mashPanel.add(jLabel28, constraints);
					jLabel28.setText("Vol Units:");		
					mashVolComboModel.setList(Quantity.getListofUnits("vol"));
					SmartComboBox.enable(mashVolCombo);
					constraints.gridx = 1;
					constraints.gridwidth = 2;
					mashPanel.add(mashVolCombo, constraints);
					constraints.gridwidth = 1;
					mashVolCombo.setModel(mashVolComboModel);
					mashVolCombo.setPreferredSize(new java.awt.Dimension(137, 20));
					
					// Temp units
					constraints.gridx = 0;
					constraints.gridy = 1;
					mashPanel.add(jLabel27, constraints);
					jLabel27.setText("Temp Units:");
					constraints.gridx = 1;
					mashPanel.add(frb, constraints);
					frb.setText("F");
					tempUBG.add(frb);
					frb.setSelected(true);
					frb.addActionListener(this);		
					constraints.gridx = 2;
					mashPanel.add(crb, constraints);
					crb.setText("C");
					constraints.gridx = 2;
					tempUBG.add(crb);
					crb.addActionListener(this);
					
					// Temp
					constraints.gridx = 0;
					constraints.gridy = 2;
					mashPanel.add(jLabel12, constraints);
					jLabel12.setText("Boil Temp:");
					constraints.gridx = 1;
					mashPanel.add(boilTempTxt, constraints);
					boilTempTxt.setText(opts.getProperty("optBoilTempC"));
					boilTempTxt.setPreferredSize(new java.awt.Dimension(45, 20));
					constraints.gridx = 2;
					mashPanel.add(boilTempULbl, constraints);
					boilTempULbl.setText(opts.getProperty("optMashTempU"));
					boilTempULbl.setPreferredSize(new java.awt.Dimension(21, 14));
					
					// Ratio
					constraints.gridx = 0;
					constraints.gridy = 3;
					mashPanel.add(jLabel29, constraints);
					jLabel29.setText("Ratio:");	
					constraints.gridx = 1;
					mashPanel.add(mashRatioTxt, constraints);
					mashRatioTxt.setText("1.25");
					mashRatioTxt.setPreferredSize(new java.awt.Dimension(45, 20));		
					ComboBoxModel mashRatioUComboModel = new DefaultComboBoxModel(new String[] {
							"qt/l", "l/kg" });		
					SmartComboBox.enable(mashRatioUCombo);
					constraints.gridx = 2;
					mashPanel.add(mashRatioUCombo, constraints);
					mashRatioUCombo.setModel(mashRatioUComboModel);
					mashRatioUCombo.setPreferredSize(new java.awt.Dimension(71, 20));				
				}
				
				// Misc
				{
					miscPanel.setLayout(gridBag);
					newRecipePanel.add(miscPanel);
					miscPanel.setBorder(BorderFactory.createTitledBorder("Misc"));					

					// Hop Type
					constraints.gridx = 0;
					constraints.gridy = 0;				
					miscPanel.add(hopsTypeLabel, constraints);
					hopsTypeLabel.setText("Hop Type:");
					SmartComboBox.enable(hopsTypeCombo);
					hopsTypeComboModel.setList(Hop.forms);
					hopsTypeCombo.setModel(hopsTypeComboModel);
					constraints.gridx = 1;
					miscPanel.add(hopsTypeCombo, constraints);
					hopsTypeCombo.setModel(hopsTypeComboModel);
					hopsTypeCombo.setPreferredSize(new java.awt.Dimension(137, 20));

					// Water Profile
					SmartComboBox.enable(comboWaterProfile);
					constraints.gridx = 0;
					constraints.gridy = 1;
					miscPanel.add(lWaterProfile, constraints);
					constraints.gridx = 1;
					miscPanel.add(comboWaterProfile, constraints);
				}	
			}
		}
		// Layout the other tabs
		{
			{
				BorderLayout pnlDefaultDBLayout = new BorderLayout();
				pnlDefaultDB.setLayout(pnlDefaultDBLayout);
				pnlDefaultDB.setPreferredSize(new java.awt.Dimension(387, 48));
				pnlDefaultDB.setBorder(BorderFactory.createTitledBorder("BJCP Style DB:"));
				{
					ComboBoxModel styleYearComboModel = 
						new DefaultComboBoxModel(
								new String[] { "2004", "2008" });
					comboStyleYear = new JComboBox();
					pnlDefaultDB.add(comboStyleYear, BorderLayout.CENTER);
					comboStyleYear.setModel(styleYearComboModel);
				}
			}

			

			{
				GridLayout pnlWaterUsageLayout = new GridLayout(3, 2);
				pnlWaterUsageLayout.setColumns(2);
				pnlWaterUsageLayout.setHgap(5);
				pnlWaterUsageLayout.setVgap(5);
				pnlWaterUsageLayout.setRows(3);
				pnlWaterUsage.setLayout(pnlWaterUsageLayout);
				pnlWaterUsage.setBorder(BorderFactory.createTitledBorder("Water Usage:"));
				pnlWaterUsage.add(jLabel10);
				pnlWaterUsage.add(txtLeftInKettle);
				pnlWaterUsage.add(jLabel9);
				pnlWaterUsage.add(txtMiscLosses);
				pnlWaterUsage.add(jLabel11);
				pnlWaterUsage.add(txtLostInTrub);
			}

			{
				jLabel9.setText("Misc. Losses:");
			}

			{
				jLabel10.setText("Water Left In Kettle:");
			}

			{
				jLabel11.setText("Lost in Trub:");
			}

			{
				txtLeftInKettle.setText("0");

			}

			{
				txtMiscLosses.setText("0");
			}

			{
				txtLostInTrub.setText("0");
			}

			{
				BoxLayout pnlColourOptionsLayout = new BoxLayout(pnlColourOptions,
						javax.swing.BoxLayout.Y_AXIS);
				pnlColourOptions.setLayout(pnlColourOptionsLayout);
				pnlColourOptions.setBorder(BorderFactory.createTitledBorder("Colour Method:"));
				pnlColourOptions.add(rbSRM);
				pnlColourOptions.add(rbEBC);
				bgColour.add(rbSRM);
				bgColour.add(rbEBC);
			}

			{
				rbSRM.setText("SRM");
			}

			{
				rbEBC.setText("EBC");
			}

			{
				GridBagLayout pnlEvaporationLayout = new GridBagLayout();
				pnlEvaporationLayout.rowWeights = new double[] {0.1, 0.1, 0.1};
				pnlEvaporationLayout.rowHeights = new int[] {7, 7, 7};
				pnlEvaporationLayout.columnWeights = new double[] {0.1, 0.1};
				pnlEvaporationLayout.columnWidths = new int[] {7, 7};
				pnlEvaporation.setLayout(pnlEvaporationLayout);
				
				pnlEvaporation.setBorder(BorderFactory.createTitledBorder("Evaporation:"));
				pnlEvaporation.add(rbPercent, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				pnlEvaporation.add(rbConstant, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

				FlowLayout jPanel3Layout = new FlowLayout();
				jPanel3Layout.setAlignment(FlowLayout.LEFT);
				jPanel3.setLayout(jPanel3Layout);
				pnlEvaporation.add(jPanel3, new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

				jPanel3.add(evapAmountTxt);
				evapAmountTxt.setText("0");
				evapAmountTxt.setPreferredSize(new java.awt.Dimension(56, 20));

				jPanel3.add(evapAmountLbl);
				evapAmountLbl.setPreferredSize(new java.awt.Dimension(39, 14));
				evapAmountLbl.setText(Quantity.getVolAbrv(opts.getProperty("optVolUnits")));

				bgEvap.add(rbPercent);
				bgEvap.add(rbConstant);
				rbPercent.addActionListener(this);
				rbConstant.addActionListener(this);

			}

			{
				rbPercent.setText("Percent");
			}

			{
				rbConstant.setText("Constant");
			}

			{
				txtBottleSize.setText("351");
				txtBottleSize.setPreferredSize(new java.awt.Dimension(61, 20));
			}			
		}
		getContentPane().add(BorderLayout.CENTER, jTabbedPane1);
		getContentPane().add(BorderLayout.SOUTH, buttons);

		int dHeight = 500, dWidth = 500;
		try {
			
			
			if( (opts.getProperty("optsPrefDiagHeight")!= null) && opts.getProperty("optsPrefDiagWidth") != null) {
				dHeight = Integer.parseInt(opts.getProperty("optsPrefDiagHeight"));
				dWidth = Integer.parseInt(opts.getProperty("optsPrefDiagWidth"));
			}
		} catch ( NumberFormatException e) {
			System.err.println("Something wrong with the Diag Options");
		}
		setSize(dWidth, dHeight);
		
	}	

	//	Action Performed 
	public void actionPerformed(ActionEvent e) {
		
		Object o = e.getSource();
		
		Debug.print("Action performed on: " + o);
		
		if (o == okButton) {
			saveOptions();
			opts.saveProperties();
			((StrangeSwing)sb).displayRecipe();
			((StrangeSwing)sb).updateUI();
			setVisible(false);
			dispose();
		}
		else if (o == cancelButton){
			setVisible(false);
			dispose();
		}
		else if (o == colMethod1rb || o == colMethod2rb){
			redSpn.setEnabled(o == colMethod1rb);
			greenSpn.setEnabled(o == colMethod1rb);
			blueSpn.setEnabled(o == colMethod1rb);
			displayColour();
		}
		else if (o == rbPercent){
			evapAmountLbl.setText("%");
		}
		else if (o == rbConstant){
			evapAmountLbl.setText(Quantity.getVolAbrv(volUnitsComboModel.getSelectedItem().toString()) + "/hr");
		}
		else if (o == crb || o == frb){
			// check the current value and see if we need to switch
			
			if (crb.isSelected()) {
			
				if(boilTempULbl.getText().equals("F"))
				{
					boilTempTxt.setText(
							String.valueOf(
									BrewCalcs.fToC(
											Double.parseDouble(
													boilTempTxt.getText()
															)
													)
											)
										);
				}
				boilTempULbl.setText("C");
			}
			else {
				if(boilTempULbl.getText().equals("C"))
				{
					boilTempTxt.setText(
							String.valueOf(
									BrewCalcs.cToF(
											Double.parseDouble(
													boilTempTxt.getText()
															)
													)
											)
										);
				}
				boilTempULbl.setText("F");
			}
		}	
	}
	
	public void stateChanged(ChangeEvent e){
		displayColour();
	}
	
	private void displayColour(){
		
		int r = Integer.parseInt(redSpn.getValue().toString());
		int g = Integer.parseInt(greenSpn.getValue().toString());
		int b = Integer.parseInt(blueSpn.getValue().toString());
		int a = Integer.parseInt(alphaSpn.getValue().toString());
		
		if (colMethod1rb.isSelected()){
			stawPanel.setBackground(BrewCalcs.calcRGB(1, 2, r, g, b, a));
			palePanel.setBackground(BrewCalcs.calcRGB(1, 4, r, g, b, a));
			amberPanel.setBackground(BrewCalcs.calcRGB(1,8, r, g, b, a));
			copperPanel.setBackground(BrewCalcs.calcRGB(1,15, r, g, b, a));
			brownPanel.setBackground(BrewCalcs.calcRGB(1,20, r, g, b, a));
			blackPanel.setBackground(BrewCalcs.calcRGB(1,30, r, g, b, a));
		}
		else {
			stawPanel.setBackground(BrewCalcs.calcRGB(2, 2, r, g, b, a));
			palePanel.setBackground(BrewCalcs.calcRGB(2, 4, r, g, b, a));
			amberPanel.setBackground(BrewCalcs.calcRGB(2, 8, r, g, b, a));
			copperPanel.setBackground(BrewCalcs.calcRGB(2, 15, r, g, b, a));
			brownPanel.setBackground(BrewCalcs.calcRGB(2, 20, r, g, b, a));
			blackPanel.setBackground(BrewCalcs.calcRGB(2, 30, r, g, b, a));
			
		}
	}
	
	private void setEvapLable(){
		if (rbPercent.isSelected())				
			evapAmountLbl.setText("%");
		else
			evapAmountLbl.setText(volUnitsComboModel.getSelectedItem().toString() + "/hr");
	}

	public Options getOpts() {
		return opts;
	}

}
