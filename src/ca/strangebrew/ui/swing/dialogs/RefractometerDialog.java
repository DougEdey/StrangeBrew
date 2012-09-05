package ca.strangebrew.ui.swing.dialogs;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ca.strangebrew.BrewCalcs;
import ca.strangebrew.SBStringUtils;

public class RefractometerDialog extends javax.swing.JDialog implements ActionListener, FocusListener {
	private JPanel jPanel1;
	private JLabel jLabel4;
	private JLabel jLabel8;
	private JTextField finalBrix2;
	private JLabel abvLbl;
	private JTextField fgTxt;
	private JLabel jLabel9;
	private JLabel jLabel7;
	private JPanel jPanel5;
	private JLabel fgLbl;
	private JTextField finalBrixTxt;
	private JTextField origBrixTxt;
	private JLabel jLabel5;
	private JLabel jLabel3;
	private JPanel jPanel4;
	private JLabel sgLbl;
	private JLabel jLabel2;
	private JTextField brixTxt;
	private JLabel jLabel1;
	private JPanel jPanel3;
	private JButton okButton;
	private JPanel jPanel2;

	/**
	* Auto-generated main method to display this JDialog
	*/
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		RefractometerDialog inst = new RefractometerDialog(frame);
		inst.setVisible(true);
	}
	
	public RefractometerDialog(JFrame frame) {
		super(frame);
		initGUI();
		displayCalcs();
	}
	
	private void displayCalcs() {
		double brix = Double.parseDouble(brixTxt.getText());
		double sg = BrewCalcs.brixToSG(brix);
		sgLbl.setText(SBStringUtils.format(sg, 3));
		
		double origBrix = Double.parseDouble(origBrixTxt.getText());
		double finalBrix = Double.parseDouble(finalBrixTxt.getText());
		double fg = BrewCalcs.brixToFG(origBrix, finalBrix);
		fgLbl.setText(SBStringUtils.format(fg, 3));
		
		finalBrix = Double.parseDouble(finalBrix2.getText());
		fg = Double.parseDouble(fgTxt.getText());
		double abv = BrewCalcs.SGBrixToABV(fg, finalBrix);
		abvLbl.setText(SBStringUtils.format(abv, 1));
		
	}
	
	private void initGUI() {
		try {

		jPanel1 = new JPanel();
		GridBagLayout jPanel1Layout = new GridBagLayout();
		jPanel1Layout.rowWeights = new double[] {0.1, 0.1};
		jPanel1Layout.rowHeights = new int[] {7, 7};
		jPanel1Layout.columnWeights = new double[] {0.1, 0.1};
		jPanel1Layout.columnWidths = new int[] {7, 7};
		jPanel1.setLayout(jPanel1Layout);
		getContentPane().add(jPanel1, BorderLayout.CENTER);

		jPanel3 = new JPanel();
		GridBagLayout jPanel3Layout = new GridBagLayout();
		jPanel3Layout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
		jPanel3Layout.rowHeights = new int[] {7, 7, 7, 7};
		jPanel3Layout.columnWeights = new double[] {0.1, 0.1};
		jPanel3Layout.columnWidths = new int[] {7, 7};

		jPanel3.setLayout(jPanel3Layout);
		jPanel1.add(jPanel3, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		jPanel3.setBorder(BorderFactory.createTitledBorder("Brix to SG"));

		jPanel4 = new JPanel();
		GridBagLayout jPanel4Layout = new GridBagLayout();
		jPanel4Layout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
		jPanel4Layout.rowHeights = new int[] {7, 7, 7, 7};
		jPanel4Layout.columnWeights = new double[] {0.1, 0.1};
		jPanel4Layout.columnWidths = new int[] {7, 7};
		jPanel4.setLayout(jPanel4Layout);
		jPanel1.add(jPanel4, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		jPanel4.setBorder(BorderFactory.createTitledBorder("FG from Brix"));

		jPanel5 = new JPanel();
		GridBagLayout jPanel5Layout = new GridBagLayout();
		jPanel5Layout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
		jPanel5Layout.rowHeights = new int[] {7, 7, 7, 7};
		jPanel5Layout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
		jPanel5Layout.columnWidths = new int[] {7, 7, 7, 7};
		jPanel5.setLayout(jPanel5Layout);
		jPanel1.add(jPanel5, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		jPanel5.setBorder(BorderFactory.createTitledBorder("ABV from FG and Brix"));

		jLabel7 = new JLabel();
		jPanel5.add(jLabel7, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		jLabel7.setText("Final Brix:");

		jLabel8 = new JLabel();
		jPanel5.add(jLabel8, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		jLabel8.setText("FG:");

		jLabel9 = new JLabel();
		jPanel5.add(jLabel9, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		jLabel9.setText("ABV:");

		finalBrix2 = new JTextField();
		jPanel5.add(finalBrix2, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		finalBrix2.setText("0.0");
		finalBrix2.setPreferredSize(new java.awt.Dimension(55, finalBrix2.getFont().getSize()*2));
		finalBrix2.addFocusListener(this);
		finalBrix2.addActionListener(this);

		fgTxt = new JTextField();
		jPanel5.add(fgTxt, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		fgTxt.setText("0.0");
		fgTxt.setPreferredSize(new java.awt.Dimension(55, fgTxt.getFont().getSize()*2));
		fgTxt.addFocusListener(this);
		fgTxt.addActionListener(this);

		abvLbl = new JLabel();
		jPanel5.add(abvLbl, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		abvLbl.setText("jLabel10");

		jLabel3 = new JLabel();
		jPanel4.add(jLabel3, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		jLabel3.setText("Original Brix:");

		jLabel4 = new JLabel();
		jPanel4.add(jLabel4, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		jLabel4.setText("Final Brix:");

		jLabel5 = new JLabel();
		jPanel4.add(jLabel5, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		jLabel5.setText("FG:");

		origBrixTxt = new JTextField();
		jPanel4.add(origBrixTxt, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		origBrixTxt.setText("0.0");
		origBrixTxt.setPreferredSize(new java.awt.Dimension(51, origBrixTxt.getFont().getSize()*2));
		origBrixTxt.addFocusListener(this);
		origBrixTxt.addActionListener(this);

		finalBrixTxt = new JTextField();
		jPanel4.add(finalBrixTxt, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		finalBrixTxt.setText("0.0");
		finalBrixTxt.setPreferredSize(new java.awt.Dimension(51, finalBrixTxt.getFont().getSize()*2));
		finalBrixTxt.addFocusListener(this);
		finalBrixTxt.addActionListener(this);

		fgLbl = new JLabel();
		jPanel4.add(fgLbl, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		fgLbl.setText("fgLbl");
		fgLbl.setPreferredSize(new java.awt.Dimension(35, fgLbl.getFont().getSize()*2));

		jLabel1 = new JLabel();
		jPanel3.add(jLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		jLabel1.setText("Brix:");

		brixTxt = new JTextField();
		jPanel3.add(brixTxt, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		brixTxt.setText("0.0" +
				"");
		brixTxt.setPreferredSize(new java.awt.Dimension(47, brixTxt.getFont().getSize()*2));
		brixTxt.addFocusListener(this);
		brixTxt.addActionListener(this);

		jLabel2 = new JLabel();
		jPanel3.add(jLabel2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		jLabel2.setText("SG:");

		sgLbl = new JLabel();
		jPanel3.add(sgLbl, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		sgLbl.setText("jLabel3");

		jPanel2 = new JPanel();
		getContentPane().add(jPanel2, BorderLayout.SOUTH);

		okButton = new JButton();
		jPanel2.add(okButton);
		okButton.setText("OK");
		okButton.addActionListener(this);

			this.setTitle("Refractometer");

			this.setSize(253, 284);
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

}
