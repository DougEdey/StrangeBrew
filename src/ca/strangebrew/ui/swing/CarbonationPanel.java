package ca.strangebrew.ui.swing;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import ca.strangebrew.BrewCalcs;
import ca.strangebrew.Database;
import ca.strangebrew.Debug;
import ca.strangebrew.Options;
import ca.strangebrew.PrimeSugar;
import ca.strangebrew.Recipe;
import ca.strangebrew.SBStringUtils;

// TODO
// Make selections reflect in myrecipe.primeSugar
// Calculations
// HTML display

public class CarbonationPanel extends javax.swing.JPanel implements ActionListener, FocusListener {
	
	// Mutables
	private Recipe myRecipe;
	
	// Final GUI Elements
	final private GridBagLayout pnlCarbLayout = new GridBagLayout();
	final private GridBagConstraints constraints = new GridBagConstraints();
	final private JPanel carbTypePanel = new JPanel();
	final private GridBagLayout pnlCarbTypeLayout = new GridBagLayout();
	final private GridBagLayout pnlPrimeLayout = new GridBagLayout();
	final private JPanel primePanel = new JPanel();
	final private GridBagLayout pnlKegLayout = new GridBagLayout();
	final private JPanel kegPanel = new JPanel();
	final private JPanel infoPanel = new JPanel();
	final private GridBagLayout pnlInfoLayout = new GridBagLayout();
	final private JLabel lBottleTemp  = new JLabel("Bottle Temp");
	final private JLabel lBottleTempU  = new JLabel("F");
	final private JLabel lServTemp  = new JLabel("Serving Temp");
	final private JLabel lServTempU  = new JLabel("F");
	final private JLabel lTargetVol  = new JLabel("Target CO2 Volume");
	final private JLabel lStyleVol  = new JLabel("Style CO2 Volume");
	final private JLabel lDissolvedVol  = new JLabel("Dissolved CO2 at Bottling: ");
	final private JLabel lKegPresure  = new JLabel("Keg Presure: ");
	final private JLabel lKegPSI  = new JLabel("PSI");
	final private JLabel lBatchSize = new JLabel("Batch Size: ");
	final private JLabel lPrimeU = new JLabel("oz");
	final private JLabel lHeight = new JLabel("Height to Faucet: ");
	final private JLabel lTubingFeet = new JLabel("Feet of Tubing: ");
	final private JLabel lTubingID = new JLabel("Tubing Inner Diameter: ");
	final private JLabel lTubingVol = new JLabel("Volume in Tubing: ");
	// Should be configurable!
	final private JLabel lWasteU = new JLabel("ml");
	final private JTextField textBottleTemp = new JTextField();
	final private JTextField textServTemp = new JTextField();
	final private JTextField textTargetVol = new JTextField();
	final private JTextField textStyleVol = new JTextField();
	final private JTextField textDissolvedVol = new JTextField("1");
	final private JTextField textPrimeAmount = new JTextField("1");
	final private JTextField textKegPresure = new JTextField("1");
	final private JTextField textTubingFeet = new JTextField("1");
	final private JTextField textTubingVol = new JTextField("1");
	final private JTextField textHeight = new JTextField();
	final private JComboBox comboPrime = new JComboBox();
	// TODO needs to be pulled out of some equipment class we don't have
	// from the equipment management that we haven't done yet!
	final private JComboBox comboTubingID = new JComboBox(new String[] {"3/16", "1/4"});
	final private JCheckBox checkKegged = new JCheckBox("Kegged");
	private boolean dontUpdate = false;
	
	public CarbonationPanel() {
		super();
		initGUI();
	}
	
	public void setList(List<PrimeSugar> primeDB) {
		// Populate combo	
		List<PrimeSugar> db = primeDB;
		for (int i = 0; i < db.size(); i++) {
			comboPrime.addItem(db.get(i).getName());
		}
	}
	
	public void setData(Recipe r) {
		myRecipe = r;	
		displayCarb();
	}
	
	public void displayCarb(){	
		//todo
		// A bit cludgy, but we don't want this to fire off ann update on the recipe, so uninstall the handler
		dontUpdate = true;
		
		lServTempU.setText(myRecipe.getCarbTempU());
		lBottleTempU.setText(myRecipe.getCarbTempU());	
		textBottleTemp.setText(SBStringUtils.format(myRecipe.getBottleTemp(), 1));
		textServTemp.setText(SBStringUtils.format(myRecipe.getServTemp(), 1));
		textTargetVol.setText(SBStringUtils.format(myRecipe.getTargetVol(), 1));
		textStyleVol.setText("0.0");
		comboPrime.setSelectedItem(myRecipe.getPrimeSugarName());
		
		checkKegged.setSelected(myRecipe.isKegged());
		lBatchSize.setText("Batch Size: " + myRecipe.getFinalWortVol(myRecipe.getVolUnits()) + 
				" " + myRecipe.getVolUnits());
		
		double dissolvedCO2 = 0.0;
		double bottleTemp = myRecipe.getBottleTemp();
		
		if (myRecipe.getCarbTempU().equals("C")) {
			bottleTemp = BrewCalcs.cToF(bottleTemp);
		}		
		
		dissolvedCO2 = BrewCalcs.dissolvedCO2(bottleTemp);
		textDissolvedVol.setText(SBStringUtils.format(dissolvedCO2, 1));	
		textPrimeAmount.setText(SBStringUtils.format(myRecipe.getPrimeSugarAmt(), 2));		
		lPrimeU.setText(myRecipe.getPrimeSugarU());
		
		// Force carb
		textKegPresure.setText(SBStringUtils.format(myRecipe.getKegPSI(), 1));
		lPrimeU.setText(myRecipe.getPrimeSugarU());
		checkKegged.setSelected(myRecipe.isKegged());	
		
		textHeight.setText(SBStringUtils.format(myRecipe.getKegTubeHeight(), 1));
		comboTubingID.setSelectedItem(myRecipe.getKegTubeID());
		textTubingFeet.setText(SBStringUtils.format(myRecipe.getKegTubeLength(), 1));
		textTubingVol.setText(SBStringUtils.format(myRecipe.getKegTubeVol(), 1));
		dontUpdate = false;
	}
	
	private void initGUI() {
		try {
			// Set up main Panel window
			pnlCarbLayout.rowWeights = new double[] {0.1,0.1};
			pnlCarbLayout.rowHeights = new int[] {7,7};
			pnlCarbLayout.columnWeights = new double[] {0.1,0.1};
			pnlCarbLayout.columnWidths = new int[] {7,7};
			this.setLayout(pnlCarbLayout);
			//setPreferredSize(new Dimension(400, 400));
			
			// Divide main window area into two panels
			constraints.fill = GridBagConstraints.BOTH;
			constraints.gridx = 0;
			infoPanel.setLayout(pnlInfoLayout);
			this.add(infoPanel, constraints);
			constraints.gridx = 1;
			constraints.weightx = 0.5;
			carbTypePanel.setLayout(pnlCarbTypeLayout);
			carbTypePanel.setBorder(BorderFactory.createTitledBorder(null, "Carbonation Method", TitledBorder.LEADING, TitledBorder.TOP));
			this.add(carbTypePanel, constraints);
			constraints.weightx = 0.0;

			// Set up info panel
			{
				// Line 1
				constraints.gridx = 0;
				constraints.gridy = 0;
				infoPanel.add(lBottleTemp, constraints);
				constraints.gridx = 1;
				constraints.ipadx = 80;
				infoPanel.add(textBottleTemp, constraints);
				textBottleTemp.addActionListener(this);
				textBottleTemp.addFocusListener(this);
				constraints.ipadx = 0;
				constraints.gridx = 2;
				infoPanel.add(lBottleTempU, constraints);
				
				// Line 2
				constraints.gridx = 0;
				constraints.gridy = 1;
				infoPanel.add(lServTemp, constraints);
				constraints.gridx = 1;
				constraints.ipadx = 80;
				infoPanel.add(textServTemp, constraints);
				textServTemp.addActionListener(this);
				textServTemp.addFocusListener(this);
				constraints.ipadx = 0;
				constraints.gridx = 2;
				infoPanel.add(lServTempU, constraints);

				// Line 3
				constraints.gridx = 0;
				constraints.gridy = 2;
				infoPanel.add(lTargetVol, constraints);
				constraints.gridx = 1;
				constraints.ipadx = 80;
				infoPanel.add(textTargetVol, constraints);
				textTargetVol.addActionListener(this);
				textTargetVol.addFocusListener(this);
				constraints.ipadx = 0;

				// Line 4
				constraints.gridx = 0;
				constraints.gridy = 3;
				infoPanel.add(lStyleVol, constraints);
				constraints.gridx = 1;
				constraints.ipadx = 80;
				infoPanel.add(textStyleVol, constraints);
				textStyleVol.setEditable(false);
				constraints.ipadx = 0;

				// Line 5
				constraints.gridx = 0;
				constraints.gridy = 4;
				infoPanel.add(lDissolvedVol, constraints);
				constraints.gridx = 1;
				constraints.ipadx = 80;
				infoPanel.add(textDissolvedVol, constraints);
				textDissolvedVol.setEditable(false);
				constraints.ipadx = 0;
			}
			
			// Setup carb panel
			{
				constraints.fill = GridBagConstraints.HORIZONTAL;
				constraints.gridx = GridBagConstraints.REMAINDER;
				constraints.gridy = 0;
				constraints.gridwidth = 2;
				carbTypePanel.add(lBatchSize, constraints);
				constraints.gridwidth = 1;
				
				// Add prime and ekg panels
				pnlPrimeLayout.rowWeights = new double[] {0.1,0.1};
				pnlPrimeLayout.rowHeights = new int[] {7,7};
				pnlPrimeLayout.columnWeights = new double[] {0.1,0.1};
				pnlPrimeLayout.columnWidths = new int[] {7,7};
				primePanel.setLayout(pnlPrimeLayout);
				primePanel.setBorder(BorderFactory.createTitledBorder(null, "Prime", TitledBorder.LEADING, TitledBorder.TOP));
				constraints.fill = GridBagConstraints.BOTH;
				constraints.gridx = 0;
				constraints.gridy = 1;
				constraints.weighty = 1.0;
				carbTypePanel.add(primePanel, constraints);

				pnlKegLayout.rowWeights = new double[] {0.1,0.1};
				pnlKegLayout.rowHeights = new int[] {7,7};
				pnlKegLayout.columnWeights = new double[] {0.1,0.1};
				pnlKegLayout.columnWidths = new int[] {7,7};
				kegPanel.setLayout(pnlKegLayout);
				kegPanel.setBorder(BorderFactory.createTitledBorder(null, "Keg", TitledBorder.LEADING, TitledBorder.TOP));
				constraints.gridx = 1;
				constraints.gridy = 1;
				constraints.weighty = 1.0;
				carbTypePanel.add(kegPanel, constraints);
				constraints.weighty = 0.0;
				constraints.fill = GridBagConstraints.HORIZONTAL;
				
				// set up prime panel
				{
					constraints.gridx = 0;
					constraints.gridy = 0;
					constraints.gridwidth = 2;
					primePanel.add(comboPrime, constraints);
					comboPrime.addActionListener(this);
					comboPrime.addFocusListener(this);					
					constraints.gridwidth = 1;
					
					constraints.gridx = 0;
					constraints.gridy = 1;
					primePanel.add(textPrimeAmount, constraints);
					textPrimeAmount.setEditable(false);
					constraints.gridx = 1;
					primePanel.add(lPrimeU, constraints);
				}
				// set up force panel
				{
					constraints.gridx = 0;
					constraints.gridy = 0;
					kegPanel.add(lKegPresure, constraints);
					constraints.gridx = 1;
					constraints.ipadx = 80;
					kegPanel.add(textKegPresure, constraints);
					textKegPresure.setEditable(false);					
					constraints.ipadx = 0;
					constraints.gridx = 2;
					kegPanel.add(lKegPSI, constraints);
					
					constraints.gridx = 0;
					constraints.gridy = 1;
					kegPanel.add(lTubingID, constraints);
					constraints.gridx = 1;
					constraints.ipadx = 80;
					kegPanel.add(comboTubingID, constraints);
					comboTubingID.addActionListener(this);
					comboTubingID.addFocusListener(this);
					constraints.ipadx = 0;

					constraints.gridx = 0;
					constraints.gridy = 2;
					kegPanel.add(lHeight, constraints);
					constraints.gridx = 1;
					constraints.ipadx = 80;
					kegPanel.add(textHeight, constraints);
					textHeight.addActionListener(this);
					textHeight.addFocusListener(this);
					constraints.ipadx = 0;
					
					constraints.gridx = 0;
					constraints.gridy = 3;
					kegPanel.add(lTubingFeet, constraints);
					constraints.gridx = 1;
					constraints.ipadx = 80;
					kegPanel.add(textTubingFeet, constraints);
					constraints.ipadx = 0;
					textTubingFeet.setEditable(false);
					
					constraints.gridx = 0;
					constraints.gridy = 4;
					kegPanel.add(lTubingVol, constraints);
					constraints.gridx = 1;
					constraints.ipadx = 80;
					kegPanel.add(textTubingVol, constraints);
					textTubingVol.setEditable(false);
					constraints.ipadx = 0;
					constraints.gridx = 2;
					kegPanel.add(lWasteU, constraints);
					
					constraints.gridx = 0;
					constraints.gridy = 5;
					constraints.gridwidth = 3;
					kegPanel.add(checkKegged, constraints);		
					checkKegged.addActionListener(this);
					checkKegged.addFocusListener(this);
					constraints.gridwidth = 1;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if (myRecipe != null) {
			if (o == comboPrime) {
				updatePrimeSugar((String)comboPrime.getSelectedItem(), myRecipe.getPrimeSugarU());			
				displayCarb();
			} else if (o == textBottleTemp) {
				try {
					NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
					Number number = format.parse(textBottleTemp.getText().trim());
					myRecipe.setBottleTemp(number.doubleValue());
				} catch (NumberFormatException m) {
					Debug.print("Could not parse textBottleTemp as a double");
					textBottleTemp.setText(Double.toString(myRecipe.getBottleTemp()));
				} catch (ParseException m) {
					Debug.print("Could not parse textBottleTemp as a double");
					textBottleTemp.setText(Double.toString(myRecipe.getBottleTemp()));
				}
				
				displayCarb();
				updatePrimeSugar((String)comboPrime.getSelectedItem(), myRecipe.getPrimeSugarU());			
			} else if (o == textServTemp) {
				try {
					NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
					Number number = format.parse(textServTemp.getText().trim());
					myRecipe.setServTemp(number.doubleValue());
				} catch (NumberFormatException m) {
					Debug.print("Could not parse textServTemp as a double");
					textServTemp.setText(Double.toString(myRecipe.getServTemp()));
				} catch (ParseException m) {
					Debug.print("Could not parse textServTemp as a double");
					textServTemp.setText(Double.toString(myRecipe.getServTemp()));
				}
				
				displayCarb();
				updatePrimeSugar((String)comboPrime.getSelectedItem(), myRecipe.getPrimeSugarU());			
			} else if (o == checkKegged) {			
				myRecipe.setKegged(checkKegged.isSelected());
				displayCarb();
			} else if (o == comboTubingID) {
				String tubeID = (String)comboTubingID.getSelectedItem();
				Options.getInstance().setProperty("optTubingID", tubeID);
				displayCarb();
			} else if (o == textHeight) {
				try {
					NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
					Number number = format.parse(textHeight.getText().trim());
					Options.getInstance().setDProperty("optHeightAboveKeg", number.doubleValue());
				} catch (NumberFormatException m) {
					Debug.print("Could not parse textHeight as a double");
					textHeight.setText(Double.toString(Options.getInstance().getDProperty("optHeighAboveKeg")));
				} catch (ParseException m) {
					Debug.print("Could not parse textHeight as a double");
					textHeight.setText(Double.toString(Options.getInstance().getDProperty("optHeighAboveKeg")));
				}
				
				
				displayCarb();
			} else if (o == textTargetVol) {
				try {
					NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
					Number number = format.parse(textTargetVol.getText().trim());
					myRecipe.setTargetVol(number.doubleValue());
				} catch (NumberFormatException m) {
					Debug.print("Could not parse textTargetVol as a double");
					textTargetVol.setText(Double.toString(myRecipe.getTargetVol()));
				} catch (ParseException m) {
					Debug.print("Could not parse textTargetVol as a double");
					textTargetVol.setText(Double.toString(myRecipe.getTargetVol()));
				}	
				displayCarb();
			}
		}
	}
	
	private void updatePrimeSugar(String name, String unit) {
		if (dontUpdate == false) {
			PrimeSugar newF = new PrimeSugar();
			List<PrimeSugar> ar = Database.getInstance().primeSugarDB;
			for (int i = 0; i < ar.size(); i++) {
				if (name.equals(ar.get(i).getName())) {
					newF = (PrimeSugar)ar.get(i);
				}
			}
			
			myRecipe.setPrimeSugar(newF);
			myRecipe.setPrimeSugarU(unit);
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
}
