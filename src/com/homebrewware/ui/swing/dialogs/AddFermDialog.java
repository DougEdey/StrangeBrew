/**
 *    Filename: AddFermDialog.java
 *     Version: 0.9.0
 * Description: Add Fermentables Dialog
 *     License: GPLv2
 *        Date: 2017-01-14
 *
 * This class creates a tabbed dialog box with all the preferences
 * used by the application.  The constructor will initialize all the
 * UI components to values from the Options object in the constructor.
 *
 * If the dialog box is closed with the OK button then the Options object
 * given in the constructor will be updated with new values entered by
 * the user.  If the dialog box is closed any other way then no changes will
 * be made to the Options object.
 *
 * @author Drew Avis
 *
 * @author Dallas Fletchall
 * Copyright (c) 2017 Dallas Fletchall
 */

/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as
 * published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package com.homebrewware.ui.swing.dialogs;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneLayout;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.homebrewware.BrewCalcs;
import com.homebrewware.Database;
import com.homebrewware.Debug;
import com.homebrewware.Fermentable;
import com.homebrewware.Hop;
import com.homebrewware.Options;
import com.homebrewware.Quantity;
import com.homebrewware.WaterProfile;
import com.homebrewware.ui.swing.ComboModel;
import com.homebrewware.ui.swing.SmartComboBox;
import com.homebrewware.ui.swing.StrangeSwing;


public class AddFermDialog extends javax.swing.JDialog implements ActionListener, ChangeListener, FocusListener {

	private Database db;

	public static void main(String[] args) {
		final JFrame frame = new JFrame();
		AddFermDialog inst = new AddFermDialog(frame);
		inst.setVisible(true);
	}

	// Mutables
	private Options opts;

	private final JPanel mainPanel = new JPanel();
	// Final UI elements
	final private GridBagLayout gridBag = new GridBagLayout();
	final private JPanel pnlFerm = new JPanel();


	final private JLabel lblName = new JLabel();
	final private JTextField txtName = new JTextField();

	final private JLabel lblYield = new JLabel();
	final private JTextField txtYield = new JTextField();

	final private JLabel lblLov = new JLabel();
	final private JTextField txtLov = new JTextField();

	final private JLabel lblCost = new JLabel();
	final private JTextField txtCost = new JTextField();

	final private JLabel lblStock = new JLabel();
	final private JTextField txtStock = new JTextField();

	final private JLabel lblUnits = new JLabel();
	final private JComboBox cUnits = new JComboBox();

	final private JLabel lblMash = new JLabel();
	final private JCheckBox bMash = new JCheckBox();

	final private JLabel lblDescr = new JLabel();
	final private JTextArea txtDescr = new JTextArea();
	JScrollPane jScrollDescr =new JScrollPane(txtDescr, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

	final private JLabel lblSteep = new JLabel();
	final private JCheckBox bSteep = new JCheckBox();

	final private JLabel lblModified = new JLabel();
	final private JCheckBox bModified = new JCheckBox();

	final private JLabel lblFerments = new JLabel();
	final private JCheckBox bFerments = new JCheckBox();

	final private JButton okButton = new JButton();
	final private JButton cancelButton = new JButton();

/*	private ArrayList looks;*/

	final private Frame sb;

	public AddFermDialog(Frame owner) {
		super(owner, "Add Fermentable", true);
		opts = Options.getInstance();
		sb = owner;
		db = ((StrangeSwing)owner).DB;

		layoutUi();
		setLocation(owner.getLocation());
		//setOptions();

	}


	private void layoutUi() {

		GridLayout gridBag = new GridLayout(0,1);
		GridBagConstraints c = new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
		c.fill = GridBagConstraints.HORIZONTAL;
		txtName.addFocusListener(this);
		c = null;

		JPanel lOne = new JPanel(new FlowLayout(FlowLayout.LEFT));//new GridLayout());
		JPanel lTwo = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel lThree = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel lFour = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel lFive = new JPanel(new FlowLayout(FlowLayout.LEFT));

		mainPanel.setLayout(gridBag);
		getContentPane().add(mainPanel);
		mainPanel.setBorder(BorderFactory.createTitledBorder("Add Fermentable Ingredient"));

		mainPanel.setLayout(gridBag);
		this.setTitle("Add Fermentable");
		this.setSize(500,300);
		//

		mainPanel.add(lOne);
		mainPanel.add(lTwo);
		mainPanel.add(jScrollDescr);
		mainPanel.add(lFour);
		mainPanel.add(lFive);

		try {
			// First Line
			{
				lOne.add(lblName, c);
				lblName.setText("Name: ");

				lOne.add(txtName, c);
				txtName.setPreferredSize(new java.awt.Dimension(120, txtName.getFont().getSize()*2));

				lOne.add(lblYield, c);
				lblYield.setText("Yield: ");

				lOne.add(txtYield, c);
				txtYield.setText("1.000");
				txtYield.setPreferredSize(new java.awt.Dimension(55, txtYield.getFont().getSize()*2));

			}
			// Second Line
			{
				lTwo.add(lblLov, c);
				lblLov.setText("Lovibond: ");

				lTwo.add(txtLov, c);
				txtLov.setText("14");
				txtLov.setPreferredSize(new java.awt.Dimension(55, txtLov.getFont().getSize()*2));

				lTwo.add(lblCost, c);
				lblCost.setText("Cost: ");

				lTwo.add(txtCost, c);
				txtCost.setText("$0.00");
				txtCost.setPreferredSize(new java.awt.Dimension(55, txtCost.getFont().getSize()*2));


				lTwo.add(lblStock, c);
				lblStock.setText("Stock: ");

				lTwo.add(txtStock, c);
				txtStock.setText("0");
				txtStock.setPreferredSize(new java.awt.Dimension(55, txtStock.getFont().getSize()*2));


				//c.gridwidth = GridBagConstraints.REMAINDER;

			}
			//Third
			{
				txtDescr.setLineWrap(true);

				jScrollDescr.setPreferredSize(new java.awt.Dimension(400,jScrollDescr.getFont().getSize()*6));
				jScrollDescr.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
				jScrollDescr.setLayout(new ScrollPaneLayout());

			}
			//Fourth
			{
				lFour.add(lblUnits, c);
				lblUnits.setText("Units: ");

				lFour.add(cUnits, c);
				cUnits.addItem("lb");
				cUnits.addItem("kg");

				lFour.add(lblMash, c);
				lblMash.setText("Mashed: ");
				lFour.add(bMash, c);

				lFour.add(lblSteep, c);
				lblSteep.setText("Steep: ");
				lFour.add(bSteep, c);


				lFour.add(lblModified, c);
				lblModified.setText("Modified: ");
				lFour.add(bModified, c);

				lFour.add(lblFerments, c);
				lblFerments.setText("Ferments: ");
				lFour.add(bFerments, c);
				// Default to true
				bFerments.setSelected(true);

			}
			//fifth
			{
				//c.gridx = 1;
				//c.fill = GridBagConstraints.NONE;

				lFive.add(okButton, c);
				okButton.setText("OK");
				okButton.addActionListener(this);

				lFive.add(cancelButton, c);
				cancelButton.setText("Cancel");
				cancelButton.addActionListener(this);

				//c.gridwidth = GridBagConstraints.REMAINDER;
			}
		//
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	//	Action Performed
	public void actionPerformed(ActionEvent e) {

		Object o = e.getSource();

		Debug.print("Action performed on: " + o);
		if(o == okButton) {


			// create a new Ferm object
			Fermentable i = new Fermentable();
			i.setName(txtName.getText());
			try {
				NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
				Number number = format.parse(txtYield.getText().trim());
				i.setPppg(number.doubleValue());
			} catch (ParseException  m) {
				Debug.print("Could not read txtYield as double");
			}

			try {
				NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
				Number number = format.parse(txtLov.getText().trim());
				i.setLov(number.doubleValue());
			} catch (ParseException  m) {
				Debug.print("Could not read txtLov as double");
			}
			i.setCost(txtCost.getText());

			try {
				NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
				Number number = format.parse(txtStock.getText().trim());
				i.setStock(number.doubleValue());
			} catch (ParseException  m) {
				Debug.print("Could not read txtStock as double");
			}

			i.setUnits(cUnits.getSelectedItem().toString());
			i.setMashed(bMash.isSelected());
			i.setDescription(txtDescr.getText());
			i.setModified(bModified.isSelected());
			i.setSteep(bSteep.isSelected());
			i.ferments(bFerments.isSelected());

			int result = 1;
			int found = 0;

			// check to see if we have this fermentable already in the db
			if((found = db.find(i)) >= 0){
				// This already exists, do we want to overwrite?
				result = JOptionPane.showConfirmDialog((Component) null,
							"This ingredient exists, overwrite original?","alert", JOptionPane.YES_NO_CANCEL_OPTION);
				switch(result) {
				case JOptionPane.NO_OPTION:
					setVisible(false);
					dispose();
					return;

				case JOptionPane.YES_OPTION:
					db.fermDB.remove(found);
					break;

				case JOptionPane.CANCEL_OPTION:
					return;
				}
			}



			db.fermDB.add(i);
			db.writeFermentables();

			setVisible(false);
			dispose();

		} else if(o == cancelButton) {
			dispose();
		}

	}

	public void focusLost(FocusEvent e) {
		Debug.print("Focus changed on: " + e.getSource());
		if(e.getSource() == txtName) {
			// this is where we have lost focus, so now we need to populate the data
			// See if we can find the Fermentable
			Fermentable temp = new Fermentable();
			temp.setName(txtName.getText());
			int result = db.find(temp);
			Debug.print("Searching for " + txtName.getText() + " found: " + result);
			if(result >= 0) {

				// we have the index, load it into the hop
				temp = db.fermDB.get(result);

				// set the fields
				if (Double.toString(temp.getPppg())!= null) {
					txtYield.setText(Double.toString(temp.getPppg()));
				}

				if (Double.toString(temp.getLov()) != null) {
					txtCost.setText(Double.toString(temp.getLov()));
				}

				if (Double.toString(temp.getCostPerU()) != null) {
					txtCost.setText(Double.toString(temp.getCostPerU()));
				}

				if (Double.toString(temp.getStock()) != null) {
					txtStock.setText(Double.toString(temp.getStock()));
				}

				if (temp.getUnits() != null) {
					cUnits.setSelectedItem(temp.getUnits());
				}

				bMash.setSelected(temp.getMashed());
				bSteep.setSelected(temp.getSteep());
				bModified.setSelected(temp.getModified());
				bFerments.setSelected(temp.ferments());

				if (temp.getDescription() != null){

					txtDescr.setText(temp.getDescription());
					jScrollDescr.invalidate();
				}

				jScrollDescr.getVerticalScrollBar().setValue(0);
				jScrollDescr.revalidate();

			}
		}



	}

	public void stateChanged(ChangeEvent e){
		Object o = e.getSource();

		Debug.print("State changed on: " + o);
	}


	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub

	}

}
