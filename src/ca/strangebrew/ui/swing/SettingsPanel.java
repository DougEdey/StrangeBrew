/*
 * $Id: SettingsPanel.java,v 1.1 2006/04/20 17:26:39 andrew_avis Exp $
 */

package ca.strangebrew.ui.swing;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import javax.swing.WindowConstants;
import javax.swing.JFrame;
import javax.swing.JLabel;

import ca.strangebrew.Fermentable;
import ca.strangebrew.Quantity;
import ca.strangebrew.Recipe;



public class SettingsPanel extends javax.swing.JPanel {
	private JLabel jLabel1;
	private JComboBox maltUnitsCombo;
	private ComboModel maltUnitsComboModel;
	
	private Recipe myRecipe;
	private StrangeSwing.SBNotifier sbn;
	private JLabel jLabel2;
	private JComboBox hopUnitsCombo;
	private ComboModel hopUnitsComboModel;

	public SettingsPanel(StrangeSwing.SBNotifier sb) {
		super();
		sbn = sb;
		initGUI();
	}
	

	
	public void setData(Recipe r){
		myRecipe = r;
		maltUnitsComboModel.setSelectedItem(myRecipe.getMaltUnits());
		hopUnitsComboModel.setSelectedItem(myRecipe.getHopUnits());
	}
	
	private void initGUI() {
		try {

		jLabel1 = new JLabel();
		GridBagLayout thisLayout = new GridBagLayout();
		thisLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
		thisLayout.rowHeights = new int[] {7, 7, 7, 7};
		thisLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
		thisLayout.columnWidths = new int[] {7, 7, 7, 7};
		this.setLayout(thisLayout);
		this.add(jLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		jLabel1.setText("Malt Units:");

		maltUnitsComboModel = new ComboModel();
		maltUnitsComboModel.setList(new Quantity().getListofUnits("weight"));
		maltUnitsCombo = new JComboBox();
		this.add(maltUnitsCombo, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		maltUnitsCombo.setModel(maltUnitsComboModel);
		
		maltUnitsCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				String u = (String) maltUnitsComboModel.getSelectedItem();				
				if (myRecipe != null) {
					myRecipe.setMaltUnits(u);
					sbn.displRecipe();
				}

			}
		});

		jLabel2 = new JLabel();
		this.add(jLabel2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		jLabel2.setText("Hop Units:");

		hopUnitsComboModel = new ComboModel();
		hopUnitsComboModel.setList(new Quantity().getListofUnits("weight"));

		hopUnitsCombo = new JComboBox();
		this.add(hopUnitsCombo, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		hopUnitsCombo.setModel(hopUnitsComboModel);

		hopUnitsCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				String u = (String) hopUnitsComboModel.getSelectedItem();				
				if (myRecipe != null) {
					myRecipe.setHopsUnits(u);
					sbn.displRecipe();
				}

			}
		});

			setPreferredSize(new Dimension(400, 300));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
