/*
 * $Id: SettingsPanel.java,v 1.6 2006/06/02 19:44:26 andrew_avis Exp $
 */

package ca.strangebrew.ui.swing;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JLabel;
import javax.swing.JTextField;

import ca.strangebrew.Recipe;



public class SettingsPanel extends javax.swing.JPanel implements ActionListener, FocusListener {
	
	private Recipe myRecipe;
	private StrangeSwing.SBNotifier sbn;

	private JLabel jLabel4;
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
		pelletHopPctTxt.setText(new Double(myRecipe.getPelletHopPct()).toString());
		thickDecoctTxt.setText(new Double(myRecipe.mash.getThickDecoctRatio()).toString());
		thinDecoctTxt.setText(new Double(myRecipe.mash.getThinDecoctRatio()).toString());
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

		jLabel1 = new JLabel();
		this.add(jLabel1, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		jLabel1.setText("Thin decoction:");

		jLabel2 = new JLabel();
		this.add(jLabel2, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		jLabel2.setText("Thick decoction:");

		thinDecoctTxt = new JTextField();
		this.add(thinDecoctTxt, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		thinDecoctTxt.setText(".9");
		thinDecoctTxt.setPreferredSize(new java.awt.Dimension(58, 20));
		thinDecoctTxt.addActionListener(this);
		thinDecoctTxt.addFocusListener(this);
		
		thickDecoctTxt = new JTextField();
		this.add(thickDecoctTxt, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		thickDecoctTxt.setText(".6");
		thickDecoctTxt.setPreferredSize(new java.awt.Dimension(58, 20));
		thickDecoctTxt.addActionListener(this);
		thickDecoctTxt.addFocusListener(this);
		
		jLabel5 = new JLabel();
		this.add(jLabel5, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		jLabel5.setText("qt/lb");

		jLabel6 = new JLabel();
		this.add(jLabel6, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		jLabel6.setText("qt/lb");

		pelletHopPctTxt.addActionListener(this);
		pelletHopPctTxt.addFocusListener(this);
		

			setPreferredSize(new Dimension(400, 300));

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
	}
	
	public void focusGained(FocusEvent e) {
		// do nothing, we don't need this event
	}

	public void focusLost(FocusEvent e) {
		Object o = e.getSource();
		ActionEvent evt = new ActionEvent(o, 1, "");
		actionPerformed(evt);
	}
}
