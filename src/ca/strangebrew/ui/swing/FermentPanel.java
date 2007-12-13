package ca.strangebrew.ui.swing;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import ca.strangebrew.Recipe;
import ca.strangebrew.SBStringUtils;


public class FermentPanel extends javax.swing.JPanel implements ActionListener, FocusListener {
	
	// Mutables
	private Recipe myRecipe;
	
	// Final GUI Elements
	final private GridBagLayout pnlFermentLayout = new GridBagLayout();
	final private GridBagConstraints constraints = new GridBagConstraints();
	final private GridBagLayout pnlTypeLayout = new GridBagLayout();
	final private JPanel typePanel = new JPanel();
	final private GridBagLayout pnlSchedualLayout = new GridBagLayout();
	final private JPanel schedualPanel = new JPanel();
	final private JLabel lType = new JLabel("Fermentation Type: ");
	final private JLabel lPrimary = new JLabel("Primary: ");
	final private JLabel lSecondary = new JLabel("Secondary: ");
	final private JLabel lTertiary = new JLabel("Tertiary: ");
	final private JLabel lDays = new JLabel("Days");
	final private JLabel lTemp = new JLabel("Temp");
	final private JLabel lPrimaryTempU = new JLabel();
	final private JLabel lSecondaryTempU = new JLabel();
	final private JLabel lTertiaryTempU = new JLabel();
	final private JComboBox comboType = new JComboBox(new String[]{"one stage", "two stage", "three stage"});
	final private JTextField textPrimaryTime = new JTextField();
	final private JTextField textPrimaryTemp = new JTextField();
	final private JTextField textSecondaryTime = new JTextField();
	final private JTextField textSecondaryTemp = new JTextField();
	final private JTextField textTertiaryTime = new JTextField();
	final private JTextField textTertiaryTemp = new JTextField();
	
	public FermentPanel() {
		super();
		initGUI();
	}
	
	public void setData(Recipe r) {
		myRecipe = r;
	}
	
	public void displayFerment(){	
		// Load up recipe data
		final String tempU = myRecipe.getFermentU();	
		final String type = myRecipe.getFermentType();
		
		lPrimaryTempU.setText(tempU);
		lSecondaryTempU.setText(tempU);
		lTertiaryTempU.setText(tempU);
		
		textPrimaryTemp.setText(SBStringUtils.format(myRecipe.getPrimaryTemp(), 3));
		textSecondaryTemp.setText(SBStringUtils.format(myRecipe.getSecondaryTemp(), 3));
		textTertiaryTemp.setText(SBStringUtils.format(myRecipe.getTertiaryTemp(), 3));
		
		textPrimaryTime.setText(Integer.toString(myRecipe.getPrimaryTime()));
		textSecondaryTime.setText(Integer.toString(myRecipe.getSecondaryTime()));
		textTertiaryTime.setText(Integer.toString(myRecipe.getTertiaryTime()));
		
		comboType.setSelectedItem(type);
	}
	
	private void initGUI() {
		try {
			// Set up main Panel window
			pnlFermentLayout.rowWeights = new double[] {0.1,0.1};
			pnlFermentLayout.rowHeights = new int[] {7,7};
			pnlFermentLayout.columnWeights = new double[] {0.1,0.1};
			pnlFermentLayout.columnWidths = new int[] {7,7};
			this.setLayout(pnlFermentLayout);
			//setPreferredSize(new Dimension(400, 400));
			
			// Divide main window area into two panels
			constraints.fill = GridBagConstraints.BOTH;
			constraints.gridx = 0;
			typePanel.setLayout(pnlTypeLayout);
			typePanel.setBorder(BorderFactory.createTitledBorder(null, "Fermentation Details", TitledBorder.LEADING, TitledBorder.TOP));
			this.add(typePanel, constraints);
			constraints.gridx = 1;
			schedualPanel.setLayout(pnlSchedualLayout);
			schedualPanel.setBorder(BorderFactory.createTitledBorder(null, "Fermentation Schedual", TitledBorder.LEADING, TitledBorder.TOP));
			this.add(schedualPanel, constraints);
						
			// Setup typePanel
			{
				// Type Combobox
				constraints.anchor = GridBagConstraints.PAGE_START;
				constraints.gridx = 0;
				typePanel.add(lType, constraints);
				constraints.gridx = 1;
				typePanel.add(comboType, constraints);
				comboType.addActionListener(this);
			}
			
			// Setup schedualPanel
			{
				// Header Row
				constraints.gridx = 1;
				constraints.gridy = 0;
				schedualPanel.add(lDays, constraints);
				constraints.gridx = 2;
				schedualPanel.add(lTemp, constraints);

				// Primary
				constraints.gridx = 0;
				constraints.gridy = 1;
				schedualPanel.add(lPrimary, constraints);
				constraints.gridx = 1;
				schedualPanel.add(textPrimaryTime, constraints);
				textPrimaryTime.addFocusListener(this);
				textPrimaryTime.addActionListener(this);				
				constraints.gridx = 2;
				schedualPanel.add(textPrimaryTemp, constraints);
				textPrimaryTemp.addFocusListener(this);
				textPrimaryTemp.addActionListener(this);				
				constraints.gridx = 3;
				schedualPanel.add(lPrimaryTempU, constraints);

				// Secondary
				constraints.gridx = 0;
				constraints.gridy = 2;
				schedualPanel.add(lSecondary, constraints);
				constraints.gridx = 1;
				schedualPanel.add(textSecondaryTime, constraints);
				textSecondaryTime.addFocusListener(this);
				textSecondaryTime.addActionListener(this);				
				constraints.gridx = 2;
				schedualPanel.add(textSecondaryTemp, constraints);
				textSecondaryTemp.addFocusListener(this);
				textSecondaryTemp.addActionListener(this);				
				constraints.gridx = 3;
				schedualPanel.add(lSecondaryTempU, constraints);

				// Tertiary
				constraints.gridx = 0;
				constraints.gridy = 3;
				schedualPanel.add(lTertiary, constraints);
				constraints.gridx = 1;
				schedualPanel.add(textTertiaryTime, constraints);
				textTertiaryTime.addFocusListener(this);
				textTertiaryTime.addActionListener(this);				
				constraints.gridx = 2;
				schedualPanel.add(textTertiaryTemp, constraints);
				textTertiaryTemp.addFocusListener(this);
				textTertiaryTemp.addActionListener(this);				
				constraints.gridx = 3;
				schedualPanel.add(lTertiaryTempU, constraints);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		
		if (o == textPrimaryTime) {
			String s = textPrimaryTime.getText();
			myRecipe.setPrimaryTime(Integer.parseInt(s));
		} else if (o == textPrimaryTemp) {
			String s = textPrimaryTemp.getText();
			myRecipe.setPrimaryTemp(Double.parseDouble(s));
		} else if (o == textSecondaryTime) {
			String s = textSecondaryTime.getText();
			myRecipe.setSecondaryTime(Integer.parseInt(s));
		} else if (o == textSecondaryTemp) {
			String s = textSecondaryTemp.getText();
			myRecipe.setSecondaryTemp(Double.parseDouble(s));
		} else if (o == textTertiaryTime) {
			String s = textTertiaryTime.getText();
			myRecipe.setTertiaryTime(Integer.parseInt(s));
		} else if (o == textTertiaryTemp) {
			String s = textTertiaryTemp.getText();
			myRecipe.setTertiaryTemp(Double.parseDouble(s));
		} else if (o == comboType) {
			String s = (String)comboType.getSelectedItem();
			myRecipe.setFermentType(s);
			if (s.equals("one stage")) {
				textTertiaryTime.setText("0");
				textSecondaryTime.setText("0");
				myRecipe.setTertiaryTime(0);
				myRecipe.setSecondaryTime(0);
				textTertiaryTemp.setEnabled(false);
				textTertiaryTime.setEnabled(false);
				textSecondaryTemp.setEnabled(false);
				textSecondaryTime.setEnabled(false);
			} else if (s.equals("two stage")) {
				textTertiaryTime.setText("0");
				myRecipe.setTertiaryTime(0);
				textTertiaryTemp.setEnabled(false);
				textTertiaryTime.setEnabled(false);
				textSecondaryTemp.setEnabled(true);
				textSecondaryTime.setEnabled(true);
			} else if (s.equals("three stage")) {
				textTertiaryTemp.setEnabled(true);
				textTertiaryTime.setEnabled(true);
				textSecondaryTemp.setEnabled(true);
				textSecondaryTime.setEnabled(true);				
			}
		}
		
		// don't do display water here call displrecipe on the SB notifier to fortce a refresh of the whole recipe. this will call display water from inside Strangeswing.displayrecipe.
		//displayWater(); 
		//sbn.displRecipe();		
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
