package ca.strangebrew.ui.swing;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ca.strangebrew.Quantity;
import ca.strangebrew.Recipe;

public class ScaleRecipeDialog extends javax.swing.JDialog implements ActionListener{
	private JPanel jPanel1;
	private JComboBox sizeUnitsCombo;
	private ComboModel sizeUnitsComboModel;
	private JTextField sizeTxt;
	private JLabel jLabel1;
	private JButton cancelButton;
	private JButton okBtn;
	private JPanel jPanel2;
	
	private Recipe myRecipe;
	private StrangeSwing app;

	
	public ScaleRecipeDialog(JFrame frame) {
		super(frame);
		app = (StrangeSwing)frame;
		myRecipe = app.myRecipe;
		initGUI();
	}
	
	private void initGUI() {
		try {
			
			this.setTitle("Resize Recipe");

		jPanel1 = new JPanel();
		GridBagLayout jPanel1Layout = new GridBagLayout();
		jPanel1Layout.rowWeights = new double[] {0.1, 0.1};
		jPanel1Layout.rowHeights = new int[] {7, 7};
		jPanel1Layout.columnWeights = new double[] {0.1, 0.1, 0.1};
		jPanel1Layout.columnWidths = new int[] {7, 7, 7};
		jPanel1.setLayout(jPanel1Layout);
		getContentPane().add(jPanel1, BorderLayout.CENTER);

		jLabel1 = new JLabel();
		jPanel1.add(jLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		jLabel1.setText("New Size:");

		sizeTxt = new JTextField();
		jPanel1.add(sizeTxt, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		sizeTxt.setText("" + myRecipe.getPostBoilVol(myRecipe.getVolUnits()));

		sizeUnitsComboModel = new ComboModel();
		sizeUnitsComboModel.setList(new Quantity().getListofUnits("vol"));
		sizeUnitsComboModel.addOrInsert(myRecipe.getVolUnits());

		sizeUnitsCombo = new JComboBox();
		jPanel1.add(sizeUnitsCombo, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		sizeUnitsCombo.setModel(sizeUnitsComboModel);

		jPanel2 = new JPanel();
		FlowLayout jPanel2Layout = new FlowLayout();
		jPanel2Layout.setAlignment(FlowLayout.RIGHT);
		jPanel2.setLayout(jPanel2Layout);
		getContentPane().add(jPanel2, BorderLayout.SOUTH);

		cancelButton = new JButton();
		cancelButton.addActionListener(this);
		jPanel2.add(cancelButton);
		cancelButton.setText("Cancel");

		okBtn = new JButton();
		okBtn.addActionListener(this);
		jPanel2.add(okBtn);
		okBtn.setText("OK");

			this.setSize(306, 135);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void scaleRecipe() {

		String newUnits = sizeUnitsComboModel.getSelectedItem().toString();
		double newSize = Double.parseDouble(sizeTxt.getText());

		myRecipe.scaleRecipe(newSize, newUnits);

		app.displayRecipe();
		app.maltTable.updateUI();
		app.hopsTable.updateUI();

	}

	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();			
		
		if (o == okBtn) {
			scaleRecipe();			
			setVisible(false);
			dispose();
		}
		else if (o == cancelButton){
			setVisible(false);
			dispose();
		}		
		
	}
	
}
