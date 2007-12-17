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
import ca.strangebrew.Fermentable;
import ca.strangebrew.Quantity;
import ca.strangebrew.Recipe;

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
	final private JLabel lBottleTempU  = new JLabel();
	final private JLabel lServTemp  = new JLabel("Serving Temp");
	final private JLabel lServTempU  = new JLabel();
	final private JLabel lTargetVol  = new JLabel("Target CO2 Volumn");
	final private JLabel lStyleVol  = new JLabel("Style CO2 Volumn");
	final private JLabel lDissolvedVol  = new JLabel("Dissolved CO2 at Bottling: ");
	final private JLabel lKegPresure  = new JLabel("Keg Presure: ");
	final private JLabel lKegPSI  = new JLabel("PSI");
	final private JLabel lBatchSize = new JLabel("Batch Size: ");
	final private JLabel lPrimeU = new JLabel("");
	final private JTextField textBottleTemp = new JTextField();
	final private JTextField textServTemp = new JTextField();
	final private JTextField textTargetVol = new JTextField();
	final private JTextField textStyleVol = new JTextField();
	final private JTextField textDissolvedVol = new JTextField();
	final private JTextField textPrimeAmount = new JTextField();
	final private JTextField textKegPresure = new JTextField();
	final private JComboBox comboPrime = new JComboBox();
	final private JCheckBox checkKegged = new JCheckBox("Kegged");
	
	// Resitual CO2 Volumn Formula Constants
	final static private double cM = 1.69648934463088;
	final static private double cB = 0.9672731854783;
	// Vol = log(temp/cM) / log(cB)
	
	// Primeing constants
	final static private double ozPerGalPerVol = 0.5;
	final static private double basePppg = 1.046;
	
	// Force carbing
	// P = -16.6999 - 0.0101059 * T + 0.00116512 * T2 + 0.173354 * T * V + 4.24267 * V - 0.0684226 * V2
	//  *  P = Pressure needed (psi)
    //	* T = Temperature of keg in °F
    // 	* V = Volumes of CO2 desired 
	
	public CarbonationPanel() {
		super();
		initGUI();
	}
	
	public void setData(Recipe r) {
		myRecipe = r;

		// Populate combo	
		ArrayList db = Database.getInstance().primeSugarDB;
		for (int i = 0; i < db.size(); i++) {
			comboPrime.addItem(((Fermentable)db.get(i)).getName());
		}
	}
	
	public void displayCarb(){	
		lServTempU.setText(myRecipe.getCarbTempU());
		lBottleTempU.setText(myRecipe.getCarbTempU());
		textBottleTemp.setText(Double.toString(myRecipe.getBottleTemp()));
		textServTemp.setText(Double.toString(myRecipe.getServTemp()));
		textTargetVol.setText(Double.toString(myRecipe.getTargetVol()));
		textStyleVol.setText("calc");
		
		// Extrapolated from http://hbd.org/brewery/library/YPrimerMH.html useing normal geometry line y=mx+b
		double disolvedCO2 = 0.0;
		double tempInC = myRecipe.getBottleTemp();
		
		if (myRecipe.getCarbTempU().equals("F")) {
			tempInC = BrewCalcs.fToC(tempInC);
		}		
		
		// NOT Working!
		disolvedCO2 = Math.log(tempInC/cM) / Math.log(cB);
		textDissolvedVol.setText(Double.toString(disolvedCO2));

		// Calc prime sugar needed
		double neededVol = myRecipe.getTargetVol() - disolvedCO2;
		double volumnInGal = myRecipe.getFinalWortVol() / 4.0;
		double primeSugarInOZ = neededVol * ozPerGalPerVol * volumnInGal;
		double pppgFactor = basePppg / myRecipe.getPrimeSugar().getPppg();
		double atenuation = myRecipe.getPrimeSugar().getPercent();
		
		primeSugarInOZ = primeSugarInOZ / atenuation;
		primeSugarInOZ = primeSugarInOZ * pppgFactor;
		
		// Convert to selecteed Units
		double neededPrime = Quantity.convertUnit("oz", myRecipe.getPrimeSugarU(), primeSugarInOZ);
		textPrimeAmount.setText(Double.toHexString(neededPrime));
		
		// Force carb
		double psi = -16.6999 - 
				(0.0101059 * myRecipe.getServTemp()) +
				(0.00116512 * myRecipe.getServTemp() * myRecipe.getServTemp()) + 
				(0.173354 * myRecipe.getServTemp() * myRecipe.getTargetVol()) +
				(4.24267 * myRecipe.getTargetVol()) - 
				(0.0684226 * myRecipe.getTargetVol() * myRecipe.getTargetVol());
		textKegPresure.setText(Double.toString(psi));
		comboPrime.setSelectedItem(myRecipe.getPrimeSugarType());
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
				constraints.gridx = 2;
				infoPanel.add(lBottleTempU, constraints);
				
				// Line 2
				constraints.gridx = 0;
				constraints.gridy = 1;
				infoPanel.add(lServTemp, constraints);
				constraints.gridx = 1;
				infoPanel.add(textServTemp, constraints);
				constraints.gridx = 2;
				infoPanel.add(lServTempU, constraints);

				// Line 3
				constraints.gridx = 0;
				constraints.gridy = 2;
				infoPanel.add(lTargetVol, constraints);
				constraints.gridx = 1;
				infoPanel.add(textTargetVol, constraints);

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
					constraints.gridwidth = 1;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void actionPerformed(ActionEvent e) {
		//Object o = e.getSource();		
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
