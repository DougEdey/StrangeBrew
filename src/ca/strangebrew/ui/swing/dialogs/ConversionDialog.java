package ca.strangebrew.ui.swing.dialogs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import ca.strangebrew.BrewCalcs;
import ca.strangebrew.Quantity;
import ca.strangebrew.SBStringUtils;

public class ConversionDialog extends JDialog implements ActionListener, FocusListener, KeyListener {

	// Main Windows/Panels
	final private GridBagLayout gridBag = new GridBagLayout();
	final private GridBagConstraints constraints = new GridBagConstraints();
	
	final private JTabbedPane tabbedConversion = new JTabbedPane();
	final private JPanel panelPressure = new JPanel();
	final private JPanel panelTemp = new JPanel();
	final private JPanel panelGravity = new JPanel();
	final private JPanel panelVol = new JPanel();
	final private JPanel panelWeight = new JPanel();
	
	// Pressure
	// TODO Yes, this SHOULD be an array of structures with 3 fields.. blah blah
	final private JLabel lPres[] = new JLabel[Quantity.getPressures().length];
	final private JTextField textPres[] = new JTextField[Quantity.getPressures().length];
	final private JLabel lPresU[] = new JLabel[Quantity.getPressures().length];
	
	// Temp
	final private JLabel lF = new JLabel("Fahrenhiet: ");
	final private JTextField textF = new JTextField("0");
	final private JLabel lFU = new JLabel("F");
	final private JLabel lC = new JLabel("Celsius: ");
	final private JTextField textC = new JTextField("0");
	final private JLabel lCU = new JLabel("C");
	
	// Gravity
	final private JLabel lSG = new JLabel("Specific Gravity: ");
	final private JTextField textSG = new JTextField("0");
	final private JLabel lSGU = new JLabel("sg");
	final private JLabel lPlato = new JLabel("Degrees Plato: ");
	final private JTextField textPlato = new JTextField("0");
	final private JLabel lPlatoU = new JLabel("Plato");
	final private JLabel lGU = new JLabel("Gravity Units: ");
	final private JTextField textGU = new JTextField("0");
	final private JLabel lGUU = new JLabel("gu");

	// Vol
	// TODO Dito with pressure
	final private JLabel[] lVols = new JLabel[Quantity.getVols().length];
	final private JTextField[] textVols = new JTextField[Quantity.getVols().length];
	final private JLabel[] lVolsU = new JLabel[Quantity.getVols().length];

	// Weight
	// TODO dito
	final private JLabel[] lWeights = new JLabel[Quantity.getWeights().length];
	final private JTextField[] textWeights = new JTextField[Quantity.getWeights().length];
	final private JLabel[] lWeightsU = new JLabel[Quantity.getWeights().length];
	
	// Debugging main func
	public static void main(String[] args) {
		final JFrame frame = new JFrame();
		ConversionDialog inst = new ConversionDialog(frame);
		inst.setVisible(true);
	}
	
	public ConversionDialog(JFrame frame) {
		super(frame);
		initGUI();
		setDefaults();
	}
	
	private void setDefaults() {
		// Bad assumption, but it works until someone attempts to use
		// the Quantity static Convertion structs ;)
		// psi
		textPres[0].setText("12.0");
		textF.setText("72.0");
		textSG.setText("1.040");
		// Gallons
		textVols[4].setText("1.00");
		// lb
		textWeights[3].setText("1.00");
		
		// All the calcs are in the Action event.. so fire a bunch off
		actionPerformed(new ActionEvent(textPres[0], 1, ""));
		actionPerformed(new ActionEvent(textF, 1, ""));
		actionPerformed(new ActionEvent(textSG, 1, ""));
		actionPerformed(new ActionEvent(textVols[4], 1, ""));
		actionPerformed(new ActionEvent(textWeights[3], 1, ""));
	}
	
	public void initGUI() {
		try {
			this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), javax.swing.BoxLayout.Y_AXIS));
			this.getContentPane().add(tabbedConversion);
			this.setTitle("Conversion Tool");
			this.setSize(400, 400);
			
			// Setup tabbed pane
			tabbedConversion.addTab("Pressure", null, panelPressure, null);
			tabbedConversion.addTab("Temperature", null, panelTemp, null);
			tabbedConversion.addTab("Gravity", null, panelGravity, null);
			tabbedConversion.addTab("Volume", null, panelVol, null);
			tabbedConversion.addTab("Weight", null, panelWeight, null);
			
			// Pressure
			{
				constraints.fill = GridBagConstraints.BOTH;
				panelPressure.setLayout(gridBag);
				panelPressure.setBorder(BorderFactory.createTitledBorder("Pressure Units"));
				constraints.fill = GridBagConstraints.HORIZONTAL;

				// Setup special arrays
				Quantity.Converter[] pres = Quantity.getPressures();
				for (int i = 0; i < pres.length; i++) {
					lPres[i] = new JLabel(SBStringUtils.capitalize(pres[i].unit));
					textPres[i] = new JTextField("0");
					lPresU[i] = new JLabel(pres[i].abrv);
					
					// Add to panel
					constraints.fill = GridBagConstraints.HORIZONTAL;
					constraints.gridx = 0;
					constraints.gridy = i;
					panelPressure.add(lPres[i], constraints);
					constraints.gridx = 1;
					constraints.ipadx = 60;
					panelPressure.add(textPres[i], constraints);
					textPres[i].addFocusListener(this);
					textPres[i].addActionListener(this);
					textPres[i].addKeyListener(this);
					constraints.ipadx = 0;
					constraints.gridx = 2;
					panelPressure.add(lPresU[i], constraints);
				}	
			}
			// Temp
			{
				constraints.fill = GridBagConstraints.BOTH;
				panelTemp.setLayout(gridBag);
				panelTemp.setBorder(BorderFactory.createTitledBorder("Temperature Units"));
				constraints.fill = GridBagConstraints.HORIZONTAL;

				constraints.gridx = 0;
				constraints.gridy = 0;
				panelTemp.add(lF, constraints);				
				constraints.gridx = 1;
				constraints.ipadx = 60;
				panelTemp.add(textF, constraints);				
				textF.addFocusListener(this);
				textF.addActionListener(this);
				textF.addKeyListener(this);
				constraints.ipadx = 0;
				constraints.gridx = 2;
				panelTemp.add(lFU, constraints);				

				constraints.gridx = 0;
				constraints.gridy = 1;
				panelTemp.add(lC, constraints);				
				constraints.gridx = 1;
				constraints.ipadx = 60;
				panelTemp.add(textC, constraints);				
				textC.addFocusListener(this);
				textC.addActionListener(this);
				textC.addKeyListener(this);
				constraints.ipadx = 0;
				constraints.gridx = 2;
				panelTemp.add(lCU, constraints);				
			}
			// Gravity
			{
				constraints.fill = GridBagConstraints.BOTH;
				panelGravity.setLayout(gridBag);
				panelGravity.setBorder(BorderFactory.createTitledBorder("Graivty Units"));
				constraints.fill = GridBagConstraints.HORIZONTAL;

				constraints.gridx = 0;
				constraints.gridy = 0;
				panelGravity.add(lSG, constraints);				
				constraints.gridx = 1;
				constraints.ipadx = 60;
				panelGravity.add(textSG, constraints);				
				textSG.addFocusListener(this);
				textSG.addActionListener(this);
				textSG.addKeyListener(this);
				constraints.ipadx = 0;
				constraints.gridx = 2;
				panelGravity.add(lSGU, constraints);				

				constraints.gridx = 0;
				constraints.gridy = 1;
				panelGravity.add(lPlato, constraints);				
				constraints.gridx = 1;
				constraints.ipadx = 60;
				panelGravity.add(textPlato, constraints);				
				textPlato.addFocusListener(this);
				textPlato.addActionListener(this);
				textPlato.addKeyListener(this);
				constraints.ipadx = 0;
				constraints.gridx = 2;
				panelGravity.add(lPlatoU, constraints);				

				constraints.gridx = 0;
				constraints.gridy = 2;
				panelGravity.add(lGU, constraints);				
				constraints.gridx = 1;
				constraints.ipadx = 60;
				panelGravity.add(textGU, constraints);				
				textGU.addFocusListener(this);
				textGU.addActionListener(this);
				textGU.addKeyListener(this);
				constraints.ipadx = 0;
				constraints.gridx = 2;
				panelGravity.add(lGUU, constraints);								
			}
			// Vol
			{
				constraints.fill = GridBagConstraints.BOTH;
				panelVol.setLayout(gridBag);
				panelVol.setBorder(BorderFactory.createTitledBorder("Volume Units"));
				constraints.fill = GridBagConstraints.HORIZONTAL;
				
				// Setup special arrays
				Quantity.Converter[] vols = Quantity.getVols();
				for (int i = 0; i < vols.length; i++) {
					lVols[i] = new JLabel(SBStringUtils.capitalize(vols[i].unit));
					textVols[i] = new JTextField("0");
					lVolsU[i] = new JLabel(vols[i].abrv);
					
					// Add to panel
					constraints.fill = GridBagConstraints.HORIZONTAL;
					constraints.gridx = 0;
					constraints.gridy = i;
					panelVol.add(lVols[i], constraints);
					constraints.gridx = 1;
					constraints.ipadx = 60;
					panelVol.add(textVols[i], constraints);
					textVols[i].addFocusListener(this);
					textVols[i].addActionListener(this);
					textVols[i].addKeyListener(this);
					constraints.ipadx = 0;
					constraints.gridx = 2;
					panelVol.add(lVolsU[i], constraints);
				}
			}
			// Weight
			{
				constraints.fill = GridBagConstraints.BOTH;
				panelWeight.setLayout(gridBag);
				panelWeight.setBorder(BorderFactory.createTitledBorder("Weight Units"));
				constraints.fill = GridBagConstraints.HORIZONTAL;
				
				// Setup special arrays
				Quantity.Converter[] weights = Quantity.getWeights();
				for (int i = 0; i < weights.length; i++) {
					lWeights[i] = new JLabel(SBStringUtils.capitalize(weights[i].unit));
					textWeights[i] = new JTextField("0");
					lWeightsU[i] = new JLabel(weights[i].abrv);
					
					// Add to panel
					constraints.fill = GridBagConstraints.HORIZONTAL;
					constraints.gridx = 0;
					constraints.gridy = i;
					panelWeight.add(lWeights[i], constraints);
					constraints.gridx = 1;
					constraints.ipadx = 60;
					panelWeight.add(textWeights[i], constraints);
					textWeights[i].addFocusListener(this);
					textWeights[i].addActionListener(this);
					textWeights[i].addKeyListener(this);
					constraints.ipadx = 0;
					constraints.gridx = 2;
					panelWeight.add(lWeightsU[i], constraints);
				}								
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// Event Listners
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		
		if (o == textF ) {
			double f = Double.parseDouble(textF.getText());
			textC.setText(SBStringUtils.format(BrewCalcs.fToC(f), 1));
		} else if (o == textC ) {
			double c = Double.parseDouble(textC.getText());
			textF.setText(SBStringUtils.format(BrewCalcs.cToF(c), 1));
		} else if (o == textSG ) {
			double sg = Double.parseDouble(textSG.getText());
			textPlato.setText(SBStringUtils.format(BrewCalcs.SGToPlato(sg), 2));
			textGU.setText(SBStringUtils.format(BrewCalcs.sgToGU(sg), 0));
		} else if (o == textPlato ) {
			double plato = Double.parseDouble(textPlato.getText());
			double sg = BrewCalcs.platoToSG(plato);
			textSG.setText(SBStringUtils.format(sg, 3));
			textGU.setText(SBStringUtils.format(BrewCalcs.sgToGU(sg), 0));
		} else if (o == textGU ) {
			double su = Double.parseDouble(textGU.getText());
			double sg = BrewCalcs.guToSG(su);
			textSG.setText(SBStringUtils.format(sg, 3));
			textPlato.setText(SBStringUtils.format(BrewCalcs.SGToPlato(sg), 2));
		} else {
			for (int i = 0; i < textPres.length; i++) {
				if (o == textPres[i]) {
					String fromU = lPresU[i].getText();
					double value = Double.parseDouble(textPres[i].getText());
					for (int j = 0; j < textPres.length; j++) {
						if (i != j) {
							double conv = Quantity.convertUnit(fromU, lPres[j].getText(), value);
							textPres[j].setText(SBStringUtils.format(conv, 2));
						}
					}					
					return;
				}
			}
						
			for (int i = 0; i < textVols.length; i++) {
				if (o == textVols[i]) {
					String fromU = lVolsU[i].getText();
					double value = Double.parseDouble(textVols[i].getText());
					for (int j = 0; j < textVols.length; j++) {
						if (i != j) {
							double conv = Quantity.convertUnit(fromU, lVols[j].getText(), value);
							textVols[j].setText(SBStringUtils.format(conv, 2));
						}
					}					
					return;
				}
			}
			
			for (int i = 0; i < textWeights.length; i++ ) {
				if (o == textWeights[i]) {
					String fromU = lWeightsU[i].getText();
					double value = Double.parseDouble(textWeights[i].getText());
					for (int j = 0; j < textWeights.length; j++) {
						if (i != j) {
							double conv = Quantity.convertUnit(fromU, lWeights[j].getText(), value);
							textWeights[j].setText(SBStringUtils.format(conv, 2));
						}
					}					
					return;
				}
			}
		}
	}
		
	public void focusGained(FocusEvent e) {
		Object o = e.getSource();
		ActionEvent evt = new ActionEvent(o, 1, "");
		actionPerformed(evt);		
	}
	
	public void focusLost(FocusEvent e) {	
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
