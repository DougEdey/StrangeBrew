package ca.strangebrew.ui.swing;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import ca.strangebrew.BrewCalcs;
import ca.strangebrew.Database;
import ca.strangebrew.PrimeSugar;
import ca.strangebrew.Quantity;
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
	final private JLabel lTargetVol  = new JLabel("Target CO2 Volumn");
	final private JLabel lStyleVol  = new JLabel("Style CO2 Volumn");
	final private JLabel lDissolvedVol  = new JLabel("Dissolved CO2 at Bottling: ");
	final private JLabel lKegPresure  = new JLabel("Keg Presure: ");
	final private JLabel lKegPSI  = new JLabel("PSI");
	final private JLabel lBatchSize = new JLabel("Batch Size: ");
	final private JLabel lPrimeU = new JLabel("oz");
	final private JTextField textBottleTemp = new JTextField("60.0");
	final private JTextField textServTemp = new JTextField("40.0");
	final private JTextField textTargetVol = new JTextField("2.0");
	final private JTextField textStyleVol = new JTextField("0.0");
	final private JTextField textDissolvedVol = new JTextField("0.8");
	final private JTextField textPrimeAmount = new JTextField("100.0");
	final private JTextField textKegPresure = new JTextField("12.0");
	final private JComboBox comboPrime = new JComboBox();
	final private JCheckBox checkKegged = new JCheckBox("Kegged");
	
	public CarbonationPanel() {
		super();
		initGUI();
	}
	
	public void setList(ArrayList primeDB) {
		// Populate combo	
		ArrayList db = primeDB;
		for (int i = 0; i < db.size(); i++) {
			comboPrime.addItem(((PrimeSugar)db.get(i)).getName());
		}
	}
	
	public void setData(Recipe r) {
		myRecipe = r;
	}
	
	public void displayCarb(){	
		lServTempU.setText(myRecipe.getCarbTempU());
		lBottleTempU.setText(myRecipe.getCarbTempU());	
		textBottleTemp.setText(SBStringUtils.format(myRecipe.getBottleTemp(), 1));
		textServTemp.setText(SBStringUtils.format(myRecipe.getServTemp(), 1));
		textTargetVol.setText(SBStringUtils.format(myRecipe.getTargetVol(), 1));
		textStyleVol.setText("0.0");
		comboPrime.setSelectedItem(myRecipe.getPrimeSugarName());
		lPrimeU.setText(myRecipe.getPrimeSugarU());
		checkKegged.setSelected(myRecipe.isKegged());
		lBatchSize.setText("Batch Size: " + myRecipe.getFinalWortVol() + 
				" " + myRecipe.getVolUnits());
		
		double dissolvedCO2 = 0.0;
		double bottleTemp = myRecipe.getBottleTemp();
		double servTemp = myRecipe.getServTemp();
		
		if (myRecipe.getCarbTempU().equals("C")) {
			bottleTemp = BrewCalcs.cToF(bottleTemp);
			servTemp =  BrewCalcs.cToF(servTemp);
		}		
		
		dissolvedCO2 = BrewCalcs.dissolvedCO2(bottleTemp);
		textDissolvedVol.setText(SBStringUtils.format(dissolvedCO2, 1));

		// Calc prime sugar needed
		double primeSugarGL = BrewCalcs.PrimingSugarGL(dissolvedCO2, myRecipe.getTargetVol(), myRecipe.getPrimeSugar());
		
		// Convert to selecteed Units
		double neededPrime = Quantity.convertUnit("g", myRecipe.getPrimeSugarU(), primeSugarGL);
		neededPrime *= Quantity.convertUnit("l", myRecipe.getVolUnits(), primeSugarGL);
		neededPrime *= myRecipe.getFinalWortVol();
		textPrimeAmount.setText(SBStringUtils.format(neededPrime, 2));
		
		// Force carb
		double psi = BrewCalcs.KegPSI(servTemp, myRecipe.getTargetVol());
		textKegPresure.setText(SBStringUtils.format(psi, 1));
		lPrimeU.setText(myRecipe.getPrimeSugarU());
		checkKegged.setSelected(myRecipe.isKegged());		
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
				infoPanel.add(textBottleTemp, constraints);
				textBottleTemp.addActionListener(this);
				textBottleTemp.addFocusListener(this);
				textBottleTemp.setPreferredSize(new java.awt.Dimension(85, 20));
				constraints.gridx = 2;
				infoPanel.add(lBottleTempU, constraints);
				
				// Line 2
				constraints.gridx = 0;
				constraints.gridy = 1;
				infoPanel.add(lServTemp, constraints);
				constraints.gridx = 1;
				infoPanel.add(textServTemp, constraints);
				textServTemp.addActionListener(this);
				textServTemp.addFocusListener(this);
				constraints.gridx = 2;
				infoPanel.add(lServTempU, constraints);

				// Line 3
				constraints.gridx = 0;
				constraints.gridy = 2;
				infoPanel.add(lTargetVol, constraints);
				constraints.gridx = 1;
				infoPanel.add(textTargetVol, constraints);
				textTargetVol.addActionListener(this);
				textTargetVol.addFocusListener(this);

				// Line 4
				constraints.gridx = 0;
				constraints.gridy = 3;
				infoPanel.add(lStyleVol, constraints);
				constraints.gridx = 1;
				infoPanel.add(textStyleVol, constraints);
				textStyleVol.setEditable(false);

				// Line 5
				constraints.gridx = 0;
				constraints.gridy = 4;
				infoPanel.add(lDissolvedVol, constraints);
				constraints.gridx = 1;
				infoPanel.add(textDissolvedVol, constraints);
				textDissolvedVol.setEditable(false);
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
				constraints.fill = GridBagConstraints.NONE;
				
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
					constraints.gridy = 1;
					primePanel.add(lPrimeU, constraints);
				}
				// set up force panel
				{
					constraints.gridx = 0;
					constraints.gridy = 0;
					kegPanel.add(lKegPresure, constraints);

					constraints.gridx = 1;
					constraints.gridy = 0;
					kegPanel.add(textKegPresure, constraints);
					textKegPresure.setEditable(false);
					
					constraints.gridx = 2;
					constraints.gridy = 0;
					kegPanel.add(lKegPSI, constraints);
					
					constraints.gridx = 2;
					constraints.gridy = 1;
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
				updatePrimeSugar((String)comboPrime.getSelectedItem(), 
					Double.parseDouble(textPrimeAmount.getText()),
					myRecipe.getPrimeSugarU());			
				displayCarb();
			} else if (o == textBottleTemp) {
				myRecipe.setBottleTemp(Double.parseDouble(textBottleTemp.getText()));
				displayCarb();
				updatePrimeSugar((String)comboPrime.getSelectedItem(), 
					Double.parseDouble(textPrimeAmount.getText()),
					myRecipe.getPrimeSugarU());			
			} else if (o == textServTemp) {			
				myRecipe.setServTemp(Double.parseDouble(textServTemp.getText()));
				displayCarb();
				updatePrimeSugar((String)comboPrime.getSelectedItem(), 
					Double.parseDouble(textPrimeAmount.getText()),
					myRecipe.getPrimeSugarU());			
			} else if (o == checkKegged) {			
				myRecipe.setKegged(checkKegged.isSelected());
				displayCarb();
				myRecipe.setKegPSI(Double.parseDouble(textKegPresure.getText()));
			}
		}
	}
	
	private void updatePrimeSugar(String name, double quantity, String unit) {
		PrimeSugar newF = new PrimeSugar();
		ArrayList ar = Database.getInstance().primeSugarDB;
		for (int i = 0; i < ar.size(); i++) {
			if (name.equals(((PrimeSugar)ar.get(i)).getName())) {
				newF = (PrimeSugar)ar.get(i);
			}
		}
		
		myRecipe.setPrimeSugar(newF);
		myRecipe.setPrimeSugarU(unit);
		myRecipe.setPrimeSugarAmount(quantity);
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
