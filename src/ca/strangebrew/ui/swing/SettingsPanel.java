/*
 * $Id: SettingsPanel.java,v 1.8 2007/12/28 16:41:23 jimcdiver Exp $
 */

package ca.strangebrew.ui.swing;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import ca.strangebrew.Recipe;
import ca.strangebrew.Mash;
import ca.strangebrew.SBStringUtils;

public class SettingsPanel extends javax.swing.JPanel implements ActionListener, FocusListener {
	
	private Recipe myRecipe;
	private StrangeSwing.SBNotifier sbn;

	private JLabel jLabel4;
	private JLabel jLabel8;
	private JTextField spargeTmpTxt;
	private JLabel sprgtmpuLbl;
	private JLabel mtmpuLbl;
	private JTextField mashoutTmpTxt;
	private JPanel jPanel1;
	private JLabel jLabel7;
	private JLabel jLabel6;
	private JLabel jLabel5;
	private JTextField thickDecoctTxt;
	private JTextField thinDecoctTxt;
	private JLabel jLabel2;
	private JLabel jLabel1;
	private JTextField pelletHopPctTxt;
	private JLabel jLabel3;


	public SettingsPanel() {
		super();
		initGUI();
	}
	
	public SettingsPanel(StrangeSwing.SBNotifier sb) {
		super();
		sbn = sb;
		initGUI();
	}
	

	
	public void setData(Recipe r){
		myRecipe = r;
		displaySettings();		
	}
	
	public void displaySettings(){
		pelletHopPctTxt.setText(new Double(myRecipe.getPelletHopPct()).toString());
		thickDecoctTxt.setText(new Double(myRecipe.mash.getThickDecoctRatio()).toString());
		thinDecoctTxt.setText(new Double(myRecipe.mash.getThinDecoctRatio()).toString());
		mashoutTmpTxt.setText(SBStringUtils.format(myRecipe.mash.getTempRange(Mash.MASHOUT),2));
		spargeTmpTxt.setText(SBStringUtils.format(myRecipe.mash.getTempRange(Mash.SPARGE),2));
		mtmpuLbl.setText(myRecipe.mash.getMashTempUnits());
		sprgtmpuLbl.setText(myRecipe.mash.getMashTempUnits());
	}
	
	private void initGUI() {
		try {


		GridBagLayout thisLayout = new GridBagLayout();
		thisLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
		thisLayout.rowHeights = new int[] {7, 7, 7, 7};
		thisLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
		thisLayout.columnWidths = new int[] {7, 7, 7, 7};
		this.setLayout(thisLayout);

		jLabel3 = new JLabel();
		this.add(jLabel3, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		jLabel3.setText("Pellet Hops +IBU:");

		jLabel4 = new JLabel();
		this.add(jLabel4, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		jLabel4.setText("%");

		pelletHopPctTxt = new JTextField();
		this.add(pelletHopPctTxt, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		pelletHopPctTxt.setPreferredSize(new java.awt.Dimension(58, 20));

		jPanel1 = new JPanel();
		GridBagLayout jPanel1Layout = new GridBagLayout();
		jPanel1Layout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
		jPanel1Layout.rowHeights = new int[] {7, 7, 7, 7};
		jPanel1Layout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
		jPanel1Layout.columnWidths = new int[] {7, 7, 7, 7};
		jPanel1.setLayout(jPanel1Layout);
		this.add(jPanel1, new GridBagConstraints(0, 1, 3, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		jPanel1.setBorder(BorderFactory.createTitledBorder(null, "Mash Settings", TitledBorder.LEADING, TitledBorder.TOP));

		jLabel1 = new JLabel();
		jPanel1.add(jLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		jLabel1.setText("Thin decoction:");

		thinDecoctTxt = new JTextField();
		jPanel1.add(thinDecoctTxt, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		thinDecoctTxt.setText(".9");
		thinDecoctTxt.setPreferredSize(new java.awt.Dimension(55, 20));

		jLabel5 = new JLabel();
		jPanel1.add(jLabel5, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		jLabel5.setText("qt/lb");

		jLabel2 = new JLabel();
		jPanel1.add(jLabel2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		jLabel2.setText("Thick decoction:");

		thickDecoctTxt = new JTextField();
		jPanel1.add(thickDecoctTxt, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		thickDecoctTxt.setText(".6");
		thickDecoctTxt.setPreferredSize(new java.awt.Dimension(55, 20));

		jLabel6 = new JLabel();
		jPanel1.add(jLabel6, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		jLabel6.setText("qt/lb");

		jLabel7 = new JLabel();
		jPanel1.add(jLabel7, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		jLabel7.setText("Mashout Temp:");

		jLabel8 = new JLabel();
		jPanel1.add(jLabel8, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		jLabel8.setText("Sparge Temp:");

		mashoutTmpTxt = new JTextField();
		jPanel1.add(mashoutTmpTxt, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		mashoutTmpTxt.setText("0");
		mashoutTmpTxt.setPreferredSize(new java.awt.Dimension(55, 20));

		spargeTmpTxt = new JTextField();
		jPanel1.add(spargeTmpTxt, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		spargeTmpTxt.setText("0");
		spargeTmpTxt.setPreferredSize(new java.awt.Dimension(55, 20));

		mtmpuLbl = new JLabel();
		jPanel1.add(mtmpuLbl, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		mtmpuLbl.setText("F");

		sprgtmpuLbl = new JLabel();
		jPanel1.add(sprgtmpuLbl, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		sprgtmpuLbl.setText("F");

		thickDecoctTxt.addActionListener(this);
		thickDecoctTxt.addFocusListener(this);
		thinDecoctTxt.addActionListener(this);
		thinDecoctTxt.addFocusListener(this);
		pelletHopPctTxt.addActionListener(this);
		pelletHopPctTxt.addFocusListener(this);		
		mashoutTmpTxt.addActionListener(this);
		mashoutTmpTxt.addFocusListener(this);		
		spargeTmpTxt.addActionListener(this);
		spargeTmpTxt.addFocusListener(this);
		
		this.setPreferredSize(new Dimension(400, 300));
		this.addComponentListener(new ComponentAdapter() {
			public void componentShown(ComponentEvent evt) {
				displaySettings();
			}
		});
		

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		
		if (o == pelletHopPctTxt){
			double u = Double.parseDouble( pelletHopPctTxt.getText() );				
			if (myRecipe != null) {
				myRecipe.setPelletHopPct(u);
				sbn.displRecipe();
				sbn.hopsUpdateUI();
			}
		}
		if (o == thickDecoctTxt){
			double u = Double.parseDouble( thickDecoctTxt.getText() );				
			myRecipe.mash.setDecoctRatio("thick", u);
		}
		if (o == thinDecoctTxt){
			double u = Double.parseDouble( thinDecoctTxt.getText() );				
			myRecipe.mash.setDecoctRatio("thin", u);
		}
		if (o == mashoutTmpTxt){
			double u = Double.parseDouble( mashoutTmpTxt.getText() );				
			myRecipe.mash.setTempRange("mashout", u);
		}
		if (o == spargeTmpTxt){
			double u = Double.parseDouble( spargeTmpTxt.getText() );				
			myRecipe.mash.setTempRange("sparge", u);
		}
	}
	
	public void focusGained(FocusEvent e) {
		// no action required
	}

	public void focusLost(FocusEvent e) {
		Object o = e.getSource();
		ActionEvent evt = new ActionEvent(o, 1, "");
		actionPerformed(evt);
	}
}
