package ca.strangebrew.ui.swing.dialogs;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ca.strangebrew.Options;
import ca.strangebrew.SBStringUtils;

public class HydrometerToolDialog extends javax.swing.JDialog implements ActionListener, FocusListener,KeyListener {
	private final JPanel mainPanel = new JPanel();
	private final JLabel lMeasuredGravity = new JLabel();
	private final JLabel lMeasuredTemp = new JLabel();
	private final JLabel lCorrectedGravity = new JLabel();
	private final JLabel lCalibratedTemp = new JLabel();
	private final JLabel tempUnits1 = new JLabel();
	private final JLabel blankGrid1 = new JLabel();
	private final JLabel tempUnits2 = new JLabel();
	private final JLabel blankGrid2 = new JLabel();
	private final JTextField measuredGravity = new JTextField();
	private final JTextField measuredTemp = new JTextField();
	private final JTextField correctedGravity = new JTextField();
	private final JTextField calibratedTemp = new JTextField();
	private final JButton okButton = new JButton();
	private final Options opts = Options.getInstance();
	
	// This should be a setable option, and then reflected in the caclulation
	// Currently, it is displayed and ignored!
	private final static double FCALTEMP = 60.0;
	private final static double CCALTEMP = 15.0;
	
	
	public static void main(String[] args) {
		final JFrame frame = new JFrame();
		HydrometerToolDialog inst = new HydrometerToolDialog(frame);
		inst.setVisible(true);
	}
	
	public HydrometerToolDialog(JFrame frame) {
		super(frame);
		initGUI();
		displayCalcs();
	}

	private void displayCalcs() {
		double mT = 0;
		double mSG = 0;
		final double correction;
		final double cSG;
		
		// Just ignore bad input
		try {
			mSG = Double.parseDouble(measuredGravity.getText());
			mT = Double.parseDouble(measuredTemp.getText());
		} catch (Exception e) {	
			correctedGravity.setText("Invalid");
			return;
		}
				
		if (opts.getProperty("optMashTempU").equals("F")) {
			correction = (1.313454 - 
					(0.132674 * mT) + 
					(0.002057793 * mT * mT) - 
					(0.000002627634 * mT * mT * mT)) *
					0.001;
		} else {
			correction = -0.0008031922 - 
					(0.0000473773 * mT) +
					(0.000007231263 * mT * mT) - 
					(0.00000003078278 * mT * mT * mT);
		}

		cSG = mSG + correction;		
		correctedGravity.setText(SBStringUtils.format(cSG, 3));		
	}
	
	private void initGUI() {
		try {

		final GridBagLayout gridBag = new GridBagLayout();
		final GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		
		mainPanel.setLayout(gridBag);
		getContentPane().add(mainPanel, BorderLayout.CENTER);
		mainPanel.setBorder(BorderFactory.createTitledBorder("Hydrometer Adjustment Tool"));
		this.setTitle("Hydrometer Tool");
		this.setSize(258, 294);
		mainPanel.setPreferredSize(new java.awt.Dimension(250, 235));

		// Set temp units text
		tempUnits1.setText(" " + opts.getProperty("optMashTempU"));
		tempUnits2.setText(" " + opts.getProperty("optMashTempU"));

		// First Line	
		{
			mainPanel.add(lMeasuredGravity, c);
			lMeasuredGravity.setText("Measured Gravity: ");

			mainPanel.add(measuredGravity, c);
			measuredGravity.setText("1.040");
			measuredGravity.setPreferredSize(new java.awt.Dimension(55, 20));
			measuredGravity.addFocusListener(this);
			measuredGravity.addActionListener(this);	
			measuredGravity.addKeyListener(this);

			c.gridwidth = GridBagConstraints.REMAINDER;
			mainPanel.add(blankGrid1, c);
		}

		// Second Line
		{
			c.gridwidth = 1;
			mainPanel.add(lMeasuredTemp, c);
			lMeasuredTemp.setText("Measured Temp: ");

			mainPanel.add(measuredTemp, c);
			if (opts.getProperty("optMashTempU").equals("F")) {
				measuredTemp.setText("60.0");
			} else {
				measuredTemp.setText("15.0");
			}
			measuredTemp.setPreferredSize(new java.awt.Dimension(55, 20));
			measuredTemp.addFocusListener(this);
			measuredTemp.addActionListener(this);		
			measuredTemp.addKeyListener(this);

			c.gridwidth = GridBagConstraints.REMAINDER;
			mainPanel.add(tempUnits1, c);
		}

		// Third Line - increase vertical spaceing to offset from user entries
		{
			final Insets i = new Insets(10, 0, 0, 0);
			c.insets = i;
			c.gridwidth = 1;
			mainPanel.add(lCorrectedGravity, c);
			lCorrectedGravity.setText("Corrected Gravity: ");

			mainPanel.add(correctedGravity, c);
			correctedGravity.setText("0.0");
			correctedGravity.setEditable(false);
			correctedGravity.setPreferredSize(new java.awt.Dimension(55, 20));
			correctedGravity.addFocusListener(this);
			correctedGravity.addActionListener(this);

			c.gridwidth = GridBagConstraints.REMAINDER;
			mainPanel.add(blankGrid2, c);
		}

		// Fourth Line
		{
			c.gridwidth = 1;
			mainPanel.add(lCalibratedTemp, c);
			lCalibratedTemp.setText("Calibrated Temp: ");

			mainPanel.add(calibratedTemp, c);
			if (opts.getProperty("optMashTempU").equals("F")) {
				calibratedTemp.setText(Double.toString(FCALTEMP));
			} else {
				calibratedTemp.setText(Double.toString(CCALTEMP));			
			}		
			calibratedTemp.setEditable(false);
			calibratedTemp.setPreferredSize(new java.awt.Dimension(55, 20));
			calibratedTemp.addFocusListener(this);
			calibratedTemp.addActionListener(this);		

			c.gridwidth = GridBagConstraints.REMAINDER;
			mainPanel.add(tempUnits2, c);
		}

		// Add in an OK button at the end
		{
			c.gridx = 1;
			c.fill = GridBagConstraints.NONE;
			c.gridwidth = GridBagConstraints.REMAINDER;
			mainPanel.add(okButton, c);
			okButton.setText("OK");
			okButton.addActionListener(this);
		}

		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	public void actionPerformed(ActionEvent e) {
		
		Object o = e.getSource();
		
		if (o == okButton) {
			setVisible(false);
			dispose();
		}
		else
			displayCalcs();
	}
	
	public void focusGained(FocusEvent e) {
		// do nothing, we don't need this event
	}

	public void focusLost(FocusEvent e) {
		// trigger actionPerformed for this widget
		Object o = e.getSource();
		ActionEvent evt = new ActionEvent(o, 1, "");
		actionPerformed(evt);
	}

	public void keyTyped(KeyEvent e) {
	}
	
	public void keyPressed(KeyEvent e) {
	}

	public void keyReleased(KeyEvent e) {
		displayCalcs();
	}

}
