package ca.strangebrew.ui.swing;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import ca.strangebrew.Acid;
import ca.strangebrew.BrewCalcs;
import ca.strangebrew.Database;
import ca.strangebrew.Recipe;
import ca.strangebrew.SBStringUtils;
import ca.strangebrew.Salt;
import ca.strangebrew.WaterProfile;

public class WaterTreatmentPanel extends javax.swing.JPanel implements ActionListener, FocusListener, KeyListener {
	
	// Mutables
	private Recipe myRecipe = null;
	
	// Final GUI Elements
	final private GridBagLayout waterGridBag = new GridBagLayout();
	final private GridBagLayout saltGridBag = new GridBagLayout();
	final private GridBagConstraints constraints = new GridBagConstraints(); 
	final private JComboBox comboSource = new JComboBox();
	final private JComboBox comboTarget = new JComboBox();
	final private JPanel panelWater = new JPanel();
	final private JPanel panelSalt = new JPanel();
	
	// Labels
	final private JLabel lCa = new JLabel("Calcium (Ca): ");
	final private JLabel lCl = new JLabel("Chloride (Cl): ");
	final private JLabel lMg = new JLabel("Magnesium (Mg): ");
	final private JLabel lNa = new JLabel("Sodium (Na): ");
	final private JLabel lSo4 = new JLabel("Sulphate (SO4): ");
	final private JLabel lCarbonate = new JLabel("Carbonate: ");
	final private JLabel lHardness = new JLabel("Hardness: ");
	final private JLabel lAlk = new JLabel("Alkalinity (CaCO3): ");
	final private JLabel lTDS = new JLabel("TDS: ");
	final private JLabel lMashPH = new JLabel("Mash pH: ");
	//final private JLabel l = new JLabel(": ");
	final private JLabel lSource = new JLabel("Source");
	final private JLabel lTarget = new JLabel("Target");
	final private JLabel lTreated = new JLabel("Treated");
	final private JLabel lDiff = new JLabel("Diff");
	
	// Source text fields
	final private JTextField textCaS = new JTextField();
	final private JTextField textClS = new JTextField();
	final private JTextField textMgS = new JTextField();
	final private JTextField textNaS = new JTextField();
	final private JTextField textSo4S = new JTextField();
	final private JTextField textCarbonateS = new JTextField();
	final private JTextField textHardnessS = new JTextField();
	final private JTextField textAlkS = new JTextField();
	final private JTextField textTDSS = new JTextField();
	final private JTextField textMashPHS = new JTextField();
	
	// Target text fields 
	final private JTextField textCaT = new JTextField();
	final private JTextField textClT = new JTextField();
	final private JTextField textMgT = new JTextField();
	final private JTextField textNaT = new JTextField();
	final private JTextField textSo4T = new JTextField();
	final private JTextField textCarbonateT = new JTextField();
	final private JTextField textHardnessT = new JTextField();
	final private JTextField textAlkT = new JTextField();
	final private JTextField textTDST = new JTextField();
	final private JTextField textMashPHT = new JTextField();

	// Diff text fields
	final private JTextField textCaD = new JTextField();
	final private JTextField textClD = new JTextField();
	final private JTextField textMgD = new JTextField();
	final private JTextField textNaD = new JTextField();
	final private JTextField textSo4D = new JTextField();
	final private JTextField textCarbonateD = new JTextField();
	final private JTextField textHardnessD = new JTextField();
	final private JTextField textAlkD = new JTextField();
	final private JTextField textTDSD = new JTextField();
	final private JTextField textMashPHD = new JTextField();
	
	// Treated/Result text fields
	final private JTextField textCaR = new JTextField();
	final private JTextField textClR = new JTextField();
	final private JTextField textMgR = new JTextField();
	final private JTextField textNaR = new JTextField();
	final private JTextField textSo4R = new JTextField();
	final private JTextField textCarbonateR = new JTextField();
	final private JTextField textHardnessR = new JTextField();
	final private JTextField textAlkR = new JTextField();
	final private JTextField textTDSR = new JTextField();
	final private JTextField textMashPHR = new JTextField();
	
	// Salts
	final private JLabel[] lSalts = new JLabel[Salt.salts.length];
	final private JLabel[] lSaltsU = new JLabel[Salt.salts.length];
	final private JLabel[] lSaltsVolU = new JLabel[Salt.salts.length];
	final private JTextField[] textSaltsAmount = new JTextField[Salt.salts.length];
	final private JTextField[] textSaltsVol = new JTextField[Salt.salts.length];
	final private JCheckBox[] checkSaltsUse = new JCheckBox[Salt.salts.length];
	final private String[] saltName = new String[Salt.salts.length];
	
	// Acidification
	final private JPanel panelAcid = new JPanel();
	final private JLabel lSourcePH = new JLabel("Source pH: ");
	final private JLabel lSourceAlk = new JLabel("Source Alk: ");
	final private JLabel lTargetPH = new JLabel("Target pH: ");
	final private JLabel lAcid = new JLabel("Acid: ");
	final private JLabel lAdd = new JLabel("Add for Target: ");
	final private JLabel lAdd5_2 = new JLabel("Add for 5.2pH: "); 
	final private JLabel lAcidUnit = new JLabel();
	final private JLabel lAcidUnit5_2 = new JLabel();
	final private JTextField textSourcePH = new JTextField("8.3");
	final private JTextField textSourceAlk = new JTextField("100");
	final private JTextField textTargetPH = new JTextField("5.0");
	final private JTextField textAcidAmount = new JTextField();
	final private JTextField textAcidAmount5_2 = new JTextField();
	final private JComboBox comboAcid = new JComboBox(Acid.acidNames);
	
	public WaterTreatmentPanel() {
		super();
		initGUI();
	}
	
	public void setData(Recipe r) {
		myRecipe = r;		
		
		for (int i = 0; i < saltName.length; i++) {
			if (r.getSaltByName(saltName[i]) != null) {
				checkSaltsUse[i].setSelected(true);
			}
		}
	}

	public void setList(ArrayList<WaterProfile> waterDB) {
		// Populate combo	
		ArrayList<WaterProfile> db = waterDB;
		for (int i = 0; i < db.size(); i++) {
			comboSource.addItem(db.get(i).getName());
			comboTarget.addItem(db.get(i).getName());
		}
	}
	
	public void displayWaterTreatment() {
		WaterProfile source = myRecipe.getSourceWater();
		WaterProfile target = myRecipe.getTargetWater();
		WaterProfile diff = myRecipe.getWaterDifference();
		WaterProfile resultWater = BrewCalcs.calculateSalts(myRecipe.getSalts(), diff, 1);
		
		comboSource.setSelectedItem(source.getName());
		comboTarget.setSelectedItem(target.getName());
		
		textCaS.setText(SBStringUtils.format(source.getCa(), 1));
		textClS.setText(SBStringUtils.format(source.getCl(), 1));
		textMgS.setText(SBStringUtils.format(source.getMg(), 1));
		textNaS.setText(SBStringUtils.format(source.getNa(), 1));
		textSo4S.setText(SBStringUtils.format(source.getSo4(), 1));
		textCarbonateS.setText(SBStringUtils.format(source.getHco3(), 1));
		textHardnessS.setText(SBStringUtils.format(source.getHardness(), 1));
		textAlkS.setText(SBStringUtils.format(source.getAlkalinity(), 1));
		textTDSS.setText(SBStringUtils.format(source.getTds(), 1));
		textMashPHS.setText(SBStringUtils.format(source.getPh(), 1));

		textCaT.setText(SBStringUtils.format(target.getCa(), 1));
		textClT.setText(SBStringUtils.format(target.getCl(), 1));
		textMgT.setText(SBStringUtils.format(target.getMg(), 1));
		textNaT.setText(SBStringUtils.format(target.getNa(), 1));
		textSo4T.setText(SBStringUtils.format(target.getSo4(), 1));
		textCarbonateT.setText(SBStringUtils.format(target.getHco3(), 1));
		textHardnessT.setText(SBStringUtils.format(target.getHardness(), 1));
		textAlkT.setText(SBStringUtils.format(target.getAlkalinity(), 1));
		textTDST.setText(SBStringUtils.format(target.getTds(), 1));
		textMashPHT.setText(SBStringUtils.format(target.getPh(), 1));	
		
		textCaD.setText(SBStringUtils.format(diff.getCa(), 1));
		textClD.setText(SBStringUtils.format(diff.getCl(), 1));
		textMgD.setText(SBStringUtils.format(diff.getMg(), 1));
		textNaD.setText(SBStringUtils.format(diff.getNa(), 1));
		textSo4D.setText(SBStringUtils.format(diff.getSo4(), 1));
		textCarbonateD.setText(SBStringUtils.format(diff.getHco3(), 1));
		textHardnessD.setText(SBStringUtils.format(diff.getHardness(), 1));
		textAlkD.setText(SBStringUtils.format(diff.getAlkalinity(), 1));
		textTDSD.setText(SBStringUtils.format(diff.getTds(), 1));
		textMashPHD.setText(SBStringUtils.format(diff.getPh(), 1));
		
		textCaR.setText(SBStringUtils.format(resultWater.getCa(), 1));
		textClR.setText(SBStringUtils.format(resultWater.getCl(), 1));
		textMgR.setText(SBStringUtils.format(resultWater.getMg(), 1));
		textNaR.setText(SBStringUtils.format(resultWater.getNa(), 1));
		textSo4R.setText(SBStringUtils.format(resultWater.getSo4(), 1));
		textCarbonateR.setText(SBStringUtils.format(resultWater.getHco3(), 1));
		textHardnessR.setText(SBStringUtils.format(resultWater.getHardness(), 1));
		textAlkR.setText(SBStringUtils.format(resultWater.getAlkalinity(), 1));
		textTDSR.setText(SBStringUtils.format(resultWater.getTds(), 1));
		textMashPHR.setText(SBStringUtils.format(resultWater.getPh(), 1));	
		
		for (int i = 0; i < saltName.length; i++) {
			Salt s = myRecipe.getSaltByName(saltName[i]);
			if (myRecipe.getSaltByName(saltName[i]) != null) {
				textSaltsAmount[i].setText(SBStringUtils.format(s.getAmount(), 2)); 	
				textSaltsVol[i].setText(SBStringUtils.format(s.getVol(), 2));
			} else {
				textSaltsAmount[i].setText("0.0");
				textSaltsVol[i].setText("0.0");
			}
		}
		
		// TODO save/load/options!!!
		textSourcePH.setText(SBStringUtils.format(source.getPh(), 1));
		textSourceAlk.setText(SBStringUtils.format(source.getAlkalinity(), 1));
		textTargetPH.setText(SBStringUtils.format(target.getPh(), 1));
		comboAcid.setSelectedItem(myRecipe.getAcid().getName());
		lAcidUnit.setText(myRecipe.getAcid().getAcidUnit());
		lAcidUnit5_2.setText(myRecipe.getAcid().getAcidUnit());
		// This seems backwards but, its not if you think about it
		// basicaly, I am cheating the function to get the conversion I want
		textAcidAmount.setText(SBStringUtils.format(myRecipe.getAcidAmount(), 2));
		textAcidAmount5_2.setText(SBStringUtils.format(myRecipe.getAcidAmount5_2(), 2));
	}
	
	private void initGUI() {
		try {
			// Set up main window			
			this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
			this.add(panelWater);
			panelWater.setLayout(waterGridBag);
			panelWater.setBorder(BorderFactory.createTitledBorder(new LineBorder(
							new java.awt.Color(0, 0, 0), 1, false), "Water Chemistry",
								TitledBorder.LEADING, TitledBorder.TOP, new java.awt.Font("Dialog",
										1, 12), new java.awt.Color(51, 51, 51)));
			this.add(panelSalt);
			panelSalt.setLayout(saltGridBag);
			panelSalt.setBorder(BorderFactory.createTitledBorder(new LineBorder(
							new java.awt.Color(0, 0, 0), 1, false), "Brewing Salts (per gallon)",
								TitledBorder.LEADING, TitledBorder.TOP, new java.awt.Font("Dialog",
										1, 12), new java.awt.Color(51, 51, 51)));
			this.add(panelAcid);
			panelAcid.setLayout(saltGridBag);
			panelAcid.setBorder(BorderFactory.createTitledBorder(new LineBorder(
							new java.awt.Color(0, 0, 0), 1, false), "Acidification (per gallon)",
								TitledBorder.LEADING, TitledBorder.TOP, new java.awt.Font("Dialog",
										1, 12), new java.awt.Color(51, 51, 51)));
			
			
			// Water Panel
			{
				constraints.fill = GridBagConstraints.HORIZONTAL;
				// Combos
				{
					constraints.gridx = 0;
					constraints.gridy = 0;
					constraints.gridwidth = 2;
					panelWater.add(comboSource, constraints);
					comboSource.addActionListener(this);
					constraints.gridx = 2;
					panelWater.add(comboTarget, constraints);
					comboTarget.addActionListener(this);
					constraints.gridwidth = 1;
				}
				
				// column headers
				{			
					constraints.gridx = 1;
					constraints.gridy = 1;
					panelWater.add(lSource, constraints);
					constraints.gridx = 2;
					panelWater.add(lTarget, constraints);
					constraints.gridx = 3;
					panelWater.add(lDiff, constraints);
					constraints.gridx = 4;
					panelWater.add(lTreated, constraints);
				}
				
				// Labels
				{
					constraints.gridx = 0;
					constraints.gridy = 2;
					panelWater.add(lCa, constraints);
					constraints.gridy = 3;
					panelWater.add(lCl, constraints);
					constraints.gridy = 4;
					panelWater.add(lMg, constraints);
					constraints.gridy = 5;
					panelWater.add(lNa, constraints);
					constraints.gridy = 6;
					panelWater.add(lSo4, constraints);
					constraints.gridy = 7;
					panelWater.add(lCarbonate, constraints);
					constraints.gridy = 8;
					panelWater.add(lHardness, constraints);
					constraints.gridy = 9;
					panelWater.add(lAlk, constraints);
					constraints.gridy = 10;
					panelWater.add(lTDS, constraints);
					constraints.gridy = 11;
					panelWater.add(lMashPH, constraints);
				}
				// Source text Fields
				constraints.ipadx = 60;
				{
					constraints.gridx = 1;
					constraints.gridy = 2;
					panelWater.add(textCaS, constraints);
					constraints.gridy = 3;
					panelWater.add(textClS, constraints);
					constraints.gridy = 4;
					panelWater.add(textMgS, constraints);
					constraints.gridy = 5;
					panelWater.add(textNaS, constraints);
					constraints.gridy = 6;
					panelWater.add(textSo4S, constraints);
					constraints.gridy = 7;
					panelWater.add(textCarbonateS, constraints);
					constraints.gridy = 8;
					panelWater.add(textHardnessS, constraints);
					constraints.gridy = 9;
					panelWater.add(textAlkS, constraints);
					constraints.gridy = 10;
					panelWater.add(textTDSS, constraints);
					constraints.gridy = 11;
					panelWater.add(textMashPHS, constraints);
				}
				// Target text Fields
				{
					constraints.gridx = 2;
					constraints.gridy = 2;
					panelWater.add(textCaT, constraints);
					textCaT.setEditable(false);
					constraints.gridy = 3;
					panelWater.add(textClT, constraints);
					textClT.setEditable(false);
					constraints.gridy = 4;
					panelWater.add(textMgT, constraints);
					textMgT.setEditable(false);
					constraints.gridy = 5;
					panelWater.add(textNaT, constraints);
					textNaT.setEditable(false);
					constraints.gridy = 6;
					panelWater.add(textSo4T, constraints);
					textSo4T.setEditable(false);
					constraints.gridy = 7;
					panelWater.add(textCarbonateT, constraints);
					textCarbonateT.setEditable(false);
					constraints.gridy = 8;
					panelWater.add(textHardnessT, constraints);
					textHardnessT.setEditable(false);
					constraints.gridy = 9;
					panelWater.add(textAlkT, constraints);
					textAlkT.setEditable(false);
					constraints.gridy = 10;
					panelWater.add(textTDST, constraints);
					textTDST.setEditable(false);
					constraints.gridy = 11;
					panelWater.add(textMashPHT, constraints);
					textMashPHT.setEditable(false);
				}
				// Diff column
				{
					constraints.gridx = 3;
					constraints.gridy = 2;
					panelWater.add(textCaD, constraints);
					textCaD.setEditable(false);
					constraints.gridy = 3;
					panelWater.add(textClD, constraints);
					textClD.setEditable(false);
					constraints.gridy = 4;
					panelWater.add(textMgD, constraints);
					textMgD.setEditable(false);
					constraints.gridy = 5;
					panelWater.add(textNaD, constraints);
					textNaD.setEditable(false);
					constraints.gridy = 6;
					panelWater.add(textSo4D, constraints);
					textSo4D.setEditable(false);
					constraints.gridy = 7;
					panelWater.add(textCarbonateD, constraints);
					textCarbonateD.setEditable(false);
					constraints.gridy = 8;
					panelWater.add(textHardnessD, constraints);
					textHardnessD.setEditable(false);
					constraints.gridy = 9;
					panelWater.add(textAlkD, constraints);
					textAlkD.setEditable(false);
					constraints.gridy = 10;
					panelWater.add(textTDSD, constraints);
					textTDSD.setEditable(false);
					constraints.gridy = 11;
					panelWater.add(textMashPHD, constraints);
					textMashPHD.setEditable(false);
				}
				// Result text Fields
				{
					constraints.gridx = 4;
					constraints.gridy = 2;
					panelWater.add(textCaR, constraints);
					textCaR.setEditable(false);
					constraints.gridy = 3;
					panelWater.add(textClR, constraints);
					textClR.setEditable(false);
					constraints.gridy = 4;
					panelWater.add(textMgR, constraints);
					textMgR.setEditable(false);
					constraints.gridy = 5;
					panelWater.add(textNaR, constraints);
					textNaR.setEditable(false);
					constraints.gridy = 6;
					panelWater.add(textSo4R, constraints);
					textSo4R.setEditable(false);
					constraints.gridy = 7;
					panelWater.add(textCarbonateR, constraints);
					textCarbonateR.setEditable(false);
					constraints.gridy = 8;
					panelWater.add(textHardnessR, constraints);
					textHardnessR.setEditable(false);
					constraints.gridy = 9;
					panelWater.add(textAlkR, constraints);
					textAlkR.setEditable(false);
					constraints.gridy = 10;
					panelWater.add(textTDSR, constraints);
					textTDSR.setEditable(false);
					constraints.gridy = 11;
					panelWater.add(textMashPHR, constraints);
					textMashPHR.setEditable(false);
				}		
				constraints.ipadx = 0;
			}
			
			// Salt Panel
			{
				// Setup special arrays
				for (int i = 0; i < Salt.salts.length; i++) {
					Salt salt = Salt.salts[i];
									
					saltName[i] = new String(salt.getName());
					lSalts[i] = new JLabel(salt.getCommonName() + " (" + salt.getChemicalName() + "): ");
					lSaltsU[i] = new JLabel(salt.getAmountU());
					lSaltsVolU[i] = new JLabel(salt.getVolU());
					textSaltsAmount[i] = new JTextField("0.0");
					textSaltsVol[i] = new JTextField("0.0");
					checkSaltsUse[i] = new JCheckBox();
					checkSaltsUse[i].setSelected(false);
							
					// Add to panel
					constraints.fill = GridBagConstraints.HORIZONTAL;
					constraints.gridx = 0;
					constraints.gridy = i;
					panelSalt.add(lSalts[i], constraints);
					constraints.gridx = 1;
					panelSalt.add(checkSaltsUse[i], constraints);
					checkSaltsUse[i].addActionListener(this);
					constraints.gridx = 2;
					constraints.ipadx = 40;
					panelSalt.add(textSaltsAmount[i], constraints);
					constraints.ipadx = 0;
					constraints.gridx = 3;
					panelSalt.add(lSaltsU[i], constraints);
					constraints.gridx = 4;
					constraints.ipadx = 40;
					panelSalt.add(textSaltsVol[i], constraints);
					constraints.ipadx = 0;
					constraints.gridx = 5;
					panelSalt.add(lSaltsVolU[i], constraints);
				}				
			}
			
			// Acidification
			{
				constraints.gridx = 0;
				constraints.gridy = 0;
				panelAcid.add(lSourcePH, constraints);
				constraints.gridx = 1;
				constraints.gridwidth = 2;
				panelAcid.add(textSourcePH, constraints);
				textSourcePH.setEditable(false);
				constraints.gridwidth = 1;

				constraints.gridx = 0;
				constraints.gridy = 1;
				panelAcid.add(lSourceAlk, constraints);
				constraints.gridx = 1;
				constraints.gridwidth = 2;
				panelAcid.add(textSourceAlk, constraints);
				textSourceAlk.setEditable(false);
				constraints.gridwidth = 1;

				constraints.gridx = 0;
				constraints.gridy = 2;
				panelAcid.add(lTargetPH, constraints);
				constraints.gridx = 1;
				constraints.gridwidth = 2;
				panelAcid.add(textTargetPH, constraints);
				textTargetPH.setEditable(false);
				constraints.gridwidth = 1;
				
				constraints.gridx = 0;
				constraints.gridy = 3;
				panelAcid.add(lAcid, constraints);
				constraints.gridx = 1;
				constraints.gridwidth = 2;
				panelAcid.add(comboAcid, constraints);
				comboAcid.addActionListener(this);
				constraints.gridwidth = 1;
				
				constraints.gridx = 0;
				constraints.gridy = 4;
				panelAcid.add(lAdd, constraints);
				constraints.gridx = 1;
				constraints.weightx = 1;
				panelAcid.add(textAcidAmount, constraints);
				constraints.weightx = 0;
				textAcidAmount.setEditable(false);
				constraints.gridx = 2;
				panelAcid.add(lAcidUnit, constraints);					

				constraints.gridx = 0;
				constraints.gridy = 5;
				panelAcid.add(lAdd5_2, constraints);
				constraints.gridx = 1;
				constraints.weightx = 1;
				panelAcid.add(textAcidAmount5_2, constraints);
				constraints.weightx = 0;
				textAcidAmount5_2.setEditable(false);
				constraints.gridx = 2;
				panelAcid.add(lAcidUnit5_2, constraints);					
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		
		if (myRecipe == null) {
			return;
		}
		
		if (o == comboSource) {
			String s = (String)comboSource.getSelectedItem();
			ArrayList<WaterProfile> a = Database.getInstance().waterDB;
			WaterProfile w = null;
			for (int i = 0; i < a.size(); i++) {
				w = a.get(i);
				if (w.getName().equals(s)) {
					myRecipe.setSourceWater(w);					
				}
			}
		} else if (o == comboTarget) {
			String s = (String)comboTarget.getSelectedItem();
			ArrayList<WaterProfile> a = Database.getInstance().waterDB;
			WaterProfile w = null;
			for (int i = 0; i < a.size(); i++) {
				w = a.get(i);
				if (w.getName().equals(s)) {
					myRecipe.setTargetWater(w);					
				}
			}
		} else if (o == textSourcePH) {
		} else if (o == textSourceAlk) {
		} else if (o == textTargetPH) {
		} else if (o == comboAcid) {
			Acid a = Acid.getAcidByName((String)comboAcid.getSelectedItem());
			myRecipe.setAcid(a);
		} else {
			for (int i = 0; i < checkSaltsUse.length; i++) {
				if (o == checkSaltsUse[i]) {
					if (checkSaltsUse[i].isSelected()) {
						// Add this salt to recipe
						Salt s = new Salt(Salt.getSaltByName(saltName[i]));
						myRecipe.addSalt(s);
					} else {
						// Remove this salt from recipe
						myRecipe.delSalt(myRecipe.getSaltByName(saltName[i]));
					}
				}
			}			
		}
		
		displayWaterTreatment();
	}
	
	
	
	public void focusLost(FocusEvent e) {		
		Object o = e.getSource();
		ActionEvent evt = new ActionEvent(o, 1, "");
		actionPerformed(evt);		
	}
	
	public void focusGained(FocusEvent e) {
		// do nothing, we don't need this event
	}

	public void keyPressed(KeyEvent e) {
		Object o = e.getSource();
		ActionEvent evt = new ActionEvent(o, 1, "");
		actionPerformed(evt);	
	}

	public void keyTyped(KeyEvent e) {
		Object o = e.getSource();
		ActionEvent evt = new ActionEvent(o, 1, "");
		actionPerformed(evt);		
	}

	public void keyReleased(KeyEvent e) {
		Object o = e.getSource();
		ActionEvent evt = new ActionEvent(o, 1, "");
		actionPerformed(evt);	
	}
}

