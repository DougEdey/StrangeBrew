/*
 * @author aavis
 * 
 * This class creates a tabbed dialog box with all the preferences
 * used by the application.  The constructor will initialize all the
 * UI components to values from the Options object in the constructor.
 * 
 * If the dialog box is closed with the OK button then the Options object
 * given in the constructor will be updated with new values entered by
 * the user.  If the dialog box is closed any other way then no changes will
 * be made to the Options object.
 */

package ca.strangebrew.ui.swing.dialogs;

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
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ca.strangebrew.BrewCalcs;
import ca.strangebrew.Database;
import ca.strangebrew.Debug;
import ca.strangebrew.Fermentable;
import ca.strangebrew.Hop;
import ca.strangebrew.Options;
import ca.strangebrew.Quantity;
import ca.strangebrew.WaterProfile;
import ca.strangebrew.Yeast;
import ca.strangebrew.ui.swing.ComboModel;
import ca.strangebrew.ui.swing.SmartComboBox;
import ca.strangebrew.ui.swing.StrangeSwing;


public class AddYeastDialog extends javax.swing.JDialog implements ActionListener, ChangeListener {

	private Database db;
	
	public static void main(String[] args) {
		final JFrame frame = new JFrame();
		AddYeastDialog inst = new AddYeastDialog(frame);
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
	
	final private JLabel lblCost = new JLabel();
	final private JTextField txtCost = new JTextField();
	
	final private JLabel lblDescr = new JLabel();
	final private JTextArea txtDescr = new JTextArea();
	
	final private JLabel lblModified = new JLabel();
	final private JCheckBox bModified = new JCheckBox();
	
	final private JButton okButton = new JButton();
	final private JButton cancelButton = new JButton();
		
/*	private ArrayList looks;*/
	
	final private Frame sb;

	public AddYeastDialog(Frame owner) {
		super(owner, "Add Yeast", true);
		opts = Options.getInstance();
		sb = owner;
		db = ((StrangeSwing)owner).DB;
		
		layoutUi();		
		setLocation(owner.getLocation());
		//setOptions();				
		
	}


	private void layoutUi() {
		//Item,Name,Cost,Descr,Modified

		GridLayout gridBag = new GridLayout(0,1);
		GridBagConstraints c = new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
		c.fill = GridBagConstraints.HORIZONTAL;
		
		c = null;
		
		JPanel lOne = new JPanel(new FlowLayout(FlowLayout.LEFT));//new GridLayout());
		JPanel lTwo = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel lThree = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel lFour = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel lFive = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		mainPanel.setLayout(gridBag);
		getContentPane().add(mainPanel);
		mainPanel.setBorder(BorderFactory.createTitledBorder("Add Yeast"));
		
		mainPanel.setLayout(gridBag);
		this.setTitle("Add Yeast");
		this.setSize(500,300);
		//

		mainPanel.add(lOne);
		mainPanel.add(lTwo);
		mainPanel.add(lThree);
		mainPanel.add(lFour);
		mainPanel.add(lFive);
		
		try {
			// First Line	
			{
				lOne.add(lblName, c);
				lblName.setText("Name: ");
				
	
				lOne.add(txtName, c);
				txtName.setPreferredSize(new java.awt.Dimension(120, txtName.getFont().getSize()*2));
	
				
			}
			// Second Line	
			{
				lTwo.add(lblCost, c);
				lblCost.setText("Cost: ");
	
				lTwo.add(txtCost, c);
				txtCost.setText("$0.00");
				txtCost.setPreferredSize(new java.awt.Dimension(55, txtCost.getFont().getSize()*2));
				
			}
			//Third
			{			
				
				lThree.add(lblDescr, c);
				lblDescr.setText("Description: ");
	
				lThree.add(txtDescr, c);
				txtDescr.setPreferredSize(new java.awt.Dimension(120, txtDescr.getFont().getSize()*6));
	
	
				//c.gridwidth = GridBagConstraints.REMAINDER;
			}
			//Fourth
			{
				lFour.add(lblModified, c);
				lblModified.setText("Modified: ");
	
				lFour.add(bModified, c);
				//Modified.setPreferredSize(new java.awt.Dimension(55, 20));
				//c.gridwidth = GridBagConstraints.REMAINDER;
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
			
			Debug.print("Checking Yeast...");
			// create a new Yeast object
			Yeast i = new Yeast();
			i.setName(txtName.getText());
			i.setCost(txtCost.getText());
			i.setDescription(txtDescr.getText());
			i.setModified(bModified.isSelected());
			
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
			
			
			Debug.print("Adding yeast: " + i.getName());
			db.yeastDB.add(i);
			db.writeYeast();

			setVisible(false);
			dispose();
			
		} else if(o == cancelButton) {
			dispose();
		}
		
	}
	
	public void stateChanged(ChangeEvent e){
		Object o = e.getSource();
		
		Debug.print("State changed on: " + o);
		
		
	}
	

}