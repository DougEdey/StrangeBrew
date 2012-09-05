package ca.strangebrew.ui.swing;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import ca.strangebrew.Debug;
import ca.strangebrew.Quantity;
import ca.strangebrew.Recipe;
import ca.strangebrew.SBStringUtils;


public class WaterPanel extends javax.swing.JPanel implements ActionListener, FocusListener{
	
	private JLabel finalUnitsLbl;
	private JLabel miscLosUnitsLbl;
	private JLabel trubLossUnitsLbl;
	private JFormattedTextField finalVolTxt;
	private JFormattedTextField miscLossTxt;
	private JFormattedTextField trubLossTxt;
	private JLabel kettleUnitsLbl;
	private JFormattedTextField kettleTxt;
	private JLabel chillShrinkLbl;
	private JFormattedTextField postBoilTxt;
	private JFormattedTextField collectTxt;
	private JLabel postBoilUnitsLbl;
	private JLabel totalUnitsLbl;
	private JLabel usedInMashUnitsLbl;
	private JLabel absorbedUnitsLbl;
	private JLabel spargeUnitsLbl;
	private JLabel collectUnitsLbl;
	private JLabel spargeWithLbl;
	private JLabel usedMashLbl;
	private JLabel absorbedLbl;
	private JLabel totalWaterLbl;
	private JLabel jLabel11;
	private JLabel jLabel10;
	private JLabel jLabel9;
	private JLabel jLabel4;
	private JLabel jLabel8;
	private JPanel waterLossPanel;
	private JLabel jLabel7;
	private JLabel jLabel6;
	private JLabel jLabel5;
	private JLabel jLabel3;
	private JLabel jLabel2;
	private JLabel jLabel1;
	private JPanel waterUsePanel;
	
	private Recipe myRecipe;


	public WaterPanel() {
		super();
		initGUI();
	}
	
	public void setData(Recipe r) {
		myRecipe = r;
		displayWater();
	}
	
	public void displayWater(){
		
		String recipeUnitsAbrv = Quantity.getVolAbrv(myRecipe.getVolUnits());
		String mashUnitsAbrv = Quantity.getVolAbrv(myRecipe.mash.getMashVolUnits());

		totalWaterLbl.setText(SBStringUtils.format(myRecipe.getTotalWaterVol(myRecipe.getVolUnits()), 2));
		totalUnitsLbl.setText(recipeUnitsAbrv);
		
		usedMashLbl.setText(myRecipe.mash.getTotalWaterStr());
		usedInMashUnitsLbl.setText(mashUnitsAbrv);		
		absorbedLbl.setText(myRecipe.mash.getAbsorbedStr());
		absorbedUnitsLbl.setText(mashUnitsAbrv);
		spargeWithLbl.setText(SBStringUtils.format(myRecipe.getSparge(), 1));
		spargeUnitsLbl.setText(mashUnitsAbrv);
		
		collectTxt.setValue(SBStringUtils.format(myRecipe.getPreBoilVol(myRecipe.getVolUnits()), 2));
		collectUnitsLbl.setText(recipeUnitsAbrv);
		postBoilTxt.setValue(SBStringUtils.format(myRecipe.getPostBoilVol(myRecipe.getVolUnits()), 2));
		postBoilUnitsLbl.setText(recipeUnitsAbrv);
		finalVolTxt.setValue(SBStringUtils.format(myRecipe.getFinalWortVol(myRecipe.getVolUnits()), 2));
		finalUnitsLbl.setText(recipeUnitsAbrv);

		chillShrinkLbl.setText(SBStringUtils.format(myRecipe.getChillShrinkVol(myRecipe.getVolUnits()), 2));
		kettleTxt.setValue(SBStringUtils.format(myRecipe.getKettleLoss(myRecipe.getVolUnits()), 2));
		kettleUnitsLbl.setText(recipeUnitsAbrv);
		trubLossTxt.setValue(SBStringUtils.format(myRecipe.getTrubLoss(myRecipe.getVolUnits()), 2));
		trubLossUnitsLbl.setText(recipeUnitsAbrv);
		miscLossTxt.setValue(SBStringUtils.format(myRecipe.getMiscLoss(myRecipe.getVolUnits()), 2));
		miscLosUnitsLbl.setText(recipeUnitsAbrv);	
	}
	
	private void initGUI() {
		try {
			
			waterUsePanel = new JPanel();
			GridBagLayout waterUsePanelLayout = new GridBagLayout();
			waterUsePanelLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1};
			waterUsePanelLayout.rowHeights = new int[] {7, 7, 7, 7, 7, 7, 7, 7, 7};
			waterUsePanelLayout.columnWeights = new double[] {0.1, 0.1, 0.1};
			waterUsePanelLayout.columnWidths = new int[] {7, 7, 7};
			waterUsePanel.setLayout(waterUsePanelLayout);
			BoxLayout thisLayout = new BoxLayout(this, javax.swing.BoxLayout.X_AXIS);
			this.setLayout(thisLayout);
			this.add(waterUsePanel);
			waterUsePanel.setBorder(BorderFactory.createTitledBorder(null, "Water Use:", TitledBorder.LEADING, TitledBorder.TOP));
			waterUsePanel.setPreferredSize(new java.awt.Dimension(351, 223));

			waterLossPanel = new JPanel();
			GridBagLayout waterLossPanelLayout = new GridBagLayout();
			waterLossPanelLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1, 0.1, 0.1};
			waterLossPanelLayout.rowHeights = new int[] {7, 7, 7, 7, 7, 7};
			waterLossPanelLayout.columnWeights = new double[] {0.1, 0.1, 0.1};
			waterLossPanelLayout.columnWidths = new int[] {7, 7, 7};
			waterLossPanel.setLayout(waterLossPanelLayout);
			this.add(waterLossPanel);
			waterLossPanel.setPreferredSize(new java.awt.Dimension(400, 58));
			waterLossPanel.setBorder(BorderFactory.createTitledBorder("Losses:"));
			{
				jLabel8 = new JLabel();
				waterLossPanel.add(jLabel8, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabel8.setText("Chill Shrinkage:");
			}
			{
				chillShrinkLbl = new JLabel();
				waterLossPanel.add(chillShrinkLbl, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				chillShrinkLbl.setText("l");
				chillShrinkLbl.setPreferredSize(new java.awt.Dimension(30, chillShrinkLbl.getFont().getSize()*2));
			}
			{
				jLabel7 = new JLabel();
				waterLossPanel.add(jLabel7, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabel7.setText("Left in Kettle:");
			}
			{
				kettleTxt = new JFormattedTextField();
				waterLossPanel.add(kettleTxt, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				kettleTxt.addFocusListener(this);
				kettleTxt.addActionListener(this);
				kettleTxt.setText("1");
				kettleTxt.setPreferredSize(new java.awt.Dimension(30, kettleTxt.getFont().getSize()*2));
			}
			{
				kettleUnitsLbl = new JLabel();
				waterLossPanel.add(kettleUnitsLbl, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				kettleUnitsLbl.setText("l");
				kettleUnitsLbl.setPreferredSize(new java.awt.Dimension(39, kettleUnitsLbl.getFont().getSize()*2));
			}
			{
				jLabel9 = new JLabel();
				waterLossPanel.add(jLabel9, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabel9.setText("Lost in Trub:");
			}
			{
				trubLossTxt = new JFormattedTextField();
				waterLossPanel.add(trubLossTxt, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				trubLossTxt.addFocusListener(this);
				trubLossTxt.addActionListener(this);
				trubLossTxt.setText("jTextField1");
				trubLossTxt.setPreferredSize(new java.awt.Dimension(30, trubLossTxt.getFont().getSize()*2));
			}
			{
				trubLossUnitsLbl = new JLabel();
				waterLossPanel.add(trubLossUnitsLbl, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				trubLossUnitsLbl.setText("l");
				trubLossUnitsLbl.setPreferredSize(new java.awt.Dimension(39, trubLossUnitsLbl.getFont().getSize()*2));
			}
			{
				jLabel10 = new JLabel();
				waterLossPanel.add(jLabel10, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabel10.setText("Misc. Losses:");
			}
			{
				miscLossTxt = new JFormattedTextField();
				waterLossPanel.add(miscLossTxt, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				miscLossTxt.addFocusListener(this);
				miscLossTxt.addActionListener(this);
				miscLossTxt.setText("jTextField1");
				miscLossTxt.setPreferredSize(new java.awt.Dimension(30, miscLossTxt.getFont().getSize()*2));
			}
			{
				miscLosUnitsLbl = new JLabel();
				waterLossPanel.add(miscLosUnitsLbl, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				miscLosUnitsLbl.setText("l");
				miscLosUnitsLbl.setPreferredSize(new java.awt.Dimension(39, miscLosUnitsLbl.getFont().getSize()*2));
			}

			{
				jLabel1 = new JLabel();
				waterUsePanel.add(jLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabel1.setText("Total Water Used:");
			}
			{
				jLabel2 = new JLabel();
				waterUsePanel.add(jLabel2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabel2.setText("Used in Mash:");
			}
			{
				jLabel3 = new JLabel();
				waterUsePanel.add(jLabel3, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabel3.setText("Absorbed in Mash");
			}
			{
				jLabel4 = new JLabel();
				waterUsePanel.add(jLabel4, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabel4.setText("Sparge With:");
			}
			{
				jLabel5 = new JLabel();
				waterUsePanel.add(jLabel5, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabel5.setText("To Collect:");
			}
			{
				jLabel6 = new JLabel();
				waterUsePanel.add(jLabel6, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabel6.setText("Post Boil:");
			}
			{
				jLabel11 = new JLabel();
				waterUsePanel.add(jLabel11, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabel11.setText("Final Beer Volume:");
			}
			{
				totalWaterLbl = new JLabel();
				waterUsePanel.add(totalWaterLbl, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				totalWaterLbl.setText("0");
				totalWaterLbl.setPreferredSize(new java.awt.Dimension(30, totalWaterLbl.getFont().getSize()*2));
				totalWaterLbl.setSize(30, 20);
			}
			{
				absorbedLbl = new JLabel();
				waterUsePanel.add(absorbedLbl, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				absorbedLbl.setText("0");
				absorbedLbl.setPreferredSize(new java.awt.Dimension(30, absorbedLbl.getFont().getSize()*2));
				absorbedLbl.setSize(30, absorbedLbl.getFont().getSize()*2);
			}
			{
				usedMashLbl = new JLabel();
				waterUsePanel.add(usedMashLbl, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				usedMashLbl.setText("0");
				usedMashLbl.setPreferredSize(new java.awt.Dimension(30, usedMashLbl.getFont().getSize()*2));
				usedMashLbl.setSize(30, usedMashLbl.getFont().getSize()*2);
			}
			{
				spargeWithLbl = new JLabel();
				waterUsePanel.add(spargeWithLbl, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				spargeWithLbl.setText("l");
				spargeWithLbl.setPreferredSize(new java.awt.Dimension(30, spargeWithLbl.getFont().getSize()*2));
				spargeWithLbl.setSize(30, spargeWithLbl.getFont().getSize()*2);
			}
			{
				collectUnitsLbl = new JLabel();
				waterUsePanel.add(collectUnitsLbl, new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				collectUnitsLbl.setText("l");
			}
			{
				spargeUnitsLbl = new JLabel();
				waterUsePanel.add(spargeUnitsLbl, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				spargeUnitsLbl.setText("l");
			}
			{
				absorbedUnitsLbl = new JLabel();
				waterUsePanel.add(absorbedUnitsLbl, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				absorbedUnitsLbl.setText("l");
			}
			{
				usedInMashUnitsLbl = new JLabel();
				waterUsePanel.add(usedInMashUnitsLbl, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				usedInMashUnitsLbl.setText("l");
			}
			{
				totalUnitsLbl = new JLabel();
				waterUsePanel.add(totalUnitsLbl, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				totalUnitsLbl.setText("l");
			}
			{
				postBoilUnitsLbl = new JLabel();
				waterUsePanel.add(postBoilUnitsLbl, new GridBagConstraints(2, 5, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				postBoilUnitsLbl.setText("l");
			}
			{
				collectTxt = new JFormattedTextField();
				collectTxt.addFocusListener(this);
				collectTxt.addActionListener(this);
				waterUsePanel.add(collectTxt, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0,
								0, 0, 0), 0, 0));
				collectTxt.setText("l");
				collectTxt.setPreferredSize(new java.awt.Dimension(30, collectTxt.getFont().getSize()*2));
				collectTxt.setSize(30, collectTxt.getFont().getSize()*2);
			}
			{
				postBoilTxt = new JFormattedTextField();
				postBoilTxt.addFocusListener(this);
				postBoilTxt.addActionListener(this);
				waterUsePanel.add(postBoilTxt, new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0,
								0, 0, 0), 0, 0));
				postBoilTxt.setText("l");
				postBoilTxt.setPreferredSize(new java.awt.Dimension(30, postBoilTxt.getFont().getSize()*2));
				postBoilTxt.setSize(30, postBoilTxt.getFont().getSize()*2);
			}
			{
				finalVolTxt = new JFormattedTextField();
				finalVolTxt.addFocusListener(this);
				finalVolTxt.addActionListener(this);
				waterUsePanel.add(finalVolTxt, new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				finalVolTxt.setText("jTextField1");
				finalVolTxt.setPreferredSize(new java.awt.Dimension(30, finalVolTxt.getFont().getSize()*2));
				finalVolTxt.setSize(30, finalVolTxt.getFont().getSize()*2);
			}
			{
				finalUnitsLbl = new JLabel();
				waterUsePanel.add(finalUnitsLbl, new GridBagConstraints(2, 6, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				finalUnitsLbl.setText("l");
			}
		
			
			setPreferredSize(new Dimension(400, 300));
			this.addFocusListener(new FocusAdapter() {
				public void focusGained(FocusEvent evt) {
					displayWater();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//	Make the button do the same thing as the default close operation
	//(DISPOSE_ON_CLOSE).
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		
		if (o == kettleTxt) {
			try {
				double x = SBStringUtils.round(myRecipe.getKettleLoss(myRecipe.getVolUnits()),2);
				
				NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
				Number number = format.parse(kettleTxt.getText().trim());
				double y = SBStringUtils.round(number.doubleValue(), 2);
				if (x != y) {
					myRecipe.setKettleLoss(new Quantity(myRecipe.getVolUnits(), number.doubleValue()));
				}
			} catch (NumberFormatException  m) {
				Debug.print("Could not read kettleTxt as double");
				kettleTxt.setText(Double.toString(myRecipe.getKettleLoss(myRecipe.getVolUnits())));
			} catch (ParseException m) {
				Debug.print("Could not read kettleTxt as double");
				kettleTxt.setText(Double.toString(myRecipe.getKettleLoss(myRecipe.getVolUnits())));
			}
		}
		else if (o == miscLossTxt) {
			try {
				double x = SBStringUtils.round(myRecipe.getMiscLoss(myRecipe.getVolUnits()),2);
				
				NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
				Number number = format.parse(miscLossTxt.getText().trim());
				double y = SBStringUtils.round(number.doubleValue(),2);
				if (x != y) {
					myRecipe.setMiscLoss(new Quantity(myRecipe.getVolUnits(), number.doubleValue()));
				}
			} catch (NumberFormatException  m) {
				Debug.print("Could not read miscLossTxt as double");
				miscLossTxt.setText(Double.toString(myRecipe.getMiscLoss(myRecipe.getVolUnits())));
			} catch (ParseException m) {
				Debug.print("Could not read miscLossTxt as double");
				miscLossTxt.setText(Double.toString(myRecipe.getMiscLoss(myRecipe.getVolUnits())));
			}
		}
		else if (o == trubLossTxt) {
			try {
				double x = SBStringUtils.round(myRecipe.getTrubLoss(myRecipe.getVolUnits()),2);

				NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
				Number number = format.parse(trubLossTxt.getText().trim());
				double y = SBStringUtils.round(number.doubleValue(), 2);
				if (x != y) {
					myRecipe.setTrubLoss(new Quantity(myRecipe.getVolUnits(), number.doubleValue()));
				}
			} catch (NumberFormatException  m) {
				Debug.print("Could not read trubLossTxt as double");
				trubLossTxt.setText(Double.toString(myRecipe.getTrubLoss(myRecipe.getVolUnits())));
			} catch (ParseException m) {
				Debug.print("Could not read trubLossTxt as double");
				trubLossTxt.setText(Double.toString(myRecipe.getTrubLoss(myRecipe.getVolUnits())));
			}
		}
		else if (o == collectTxt) {
			try {
				double x = SBStringUtils.round(myRecipe.getPreBoilVol(myRecipe.getVolUnits()),2);

				NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
				Number number = format.parse(collectTxt.getText().trim());
				double y = SBStringUtils.round(number.doubleValue(), 2);
				if (x != y) {
					myRecipe.setPreBoil(new Quantity(myRecipe.getVolUnits(), number.doubleValue()));
				}
			} catch (ParseException  m) {
				Debug.print("Could not read collectTxt as double");
				collectTxt.setText(Double.toString(myRecipe.getPreBoilVol(myRecipe.getVolUnits())));
			}
		} else if (o == postBoilTxt) {
			try {
				double x = SBStringUtils.round(myRecipe.getPostBoilVol(myRecipe.getVolUnits()),2);

				NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
				Number number = format.parse(postBoilTxt.getText().trim());
				double y = SBStringUtils.round(number.doubleValue(), 2);
				if (x != y) {
					myRecipe.setPostBoil(new Quantity(myRecipe.getVolUnits(), number.doubleValue()));
				}
			} catch (ParseException  m) {
				Debug.print("Could not read postBoilTxt as double");
				postBoilTxt.setText(Double.toString(myRecipe.getPostBoilVol(myRecipe.getVolUnits())));
			}
		} else if (o == finalVolTxt) {
			try {
				double x = SBStringUtils.round(myRecipe.getFinalWortVol(myRecipe.getVolUnits()),2);

				NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
				Number number = format.parse(finalVolTxt.getText().trim());
				double y = SBStringUtils.round(number.doubleValue(),2);
				if (x != y) {
					myRecipe.setFinalWortVol(new Quantity(myRecipe.getVolUnits(), number.doubleValue()));
				}
			} catch (ParseException  m) {
				Debug.print("Could not read finalVolTxt as double");
				finalVolTxt.setText(Double.toString(myRecipe.getFinalWortVol(myRecipe.getVolUnits())));
			}
		}		
		
		// don't do display water here call displrecipe on the SB notifier to fortce a refresh of the whole recipe. this will call display water from inside Strangeswing.displayrecipe.
		displayWater(); 
		//sbn.displRecipe();
		// Only update affected stuff!
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
