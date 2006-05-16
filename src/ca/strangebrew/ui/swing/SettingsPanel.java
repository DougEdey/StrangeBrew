/*
 * $Id: SettingsPanel.java,v 1.5 2006/05/16 14:36:52 andrew_avis Exp $
 */

package ca.strangebrew.ui.swing;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JTextField;

import ca.strangebrew.Recipe;



public class SettingsPanel extends javax.swing.JPanel {
	
	private Recipe myRecipe;
	private StrangeSwing.SBNotifier sbn;

	private JLabel jLabel4;
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
		this.add(jLabel3, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		jLabel3.setText("Pellet Hops +IBU:");

		jLabel4 = new JLabel();
		this.add(jLabel4, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		jLabel4.setText("%");

		pelletHopPctTxt = new JTextField();
		this.add(pelletHopPctTxt, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		pelletHopPctTxt.setPreferredSize(new java.awt.Dimension(67, 20));
		pelletHopPctTxt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				double u = Double.parseDouble( pelletHopPctTxt.getText() );				
				if (myRecipe != null) {
					myRecipe.setPelletHopPct(u);
					sbn.displRecipe();
					sbn.hopsUpdateUI();
				}

			}
		});
		

			setPreferredSize(new Dimension(400, 300));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
