package ca.strangebrew.ui.swing.dialogs;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ca.strangebrew.Quantity;
import ca.strangebrew.Recipe;
import ca.strangebrew.ui.swing.ComboModel;
import ca.strangebrew.ui.swing.SmartComboBox;
import ca.strangebrew.ui.swing.StrangeSwing;

public class ScaleRecipeDialog extends javax.swing.JDialog implements ActionListener {
	private JPanel jPanel1;
	private JComboBox sizeUnitsCombo;
	private JComboBox hopsUnitsCombo;
	private JComboBox maltUnitsCombo;
	private ComboModel<String> maltUnitsComboModel;
	private ComboModel<String> hopsUnitsComboModel;	
	private JCheckBox convHopsChk;
	private JCheckBox convMaltsChk;
	private JPanel jPanel4;
	private JPanel jPanel3;
	private ComboModel<String> sizeUnitsComboModel;
	private JTextField sizeTxt;
	private JLabel jLabel1;
	private JButton cancelButton;
	private JButton okBtn;
	private JPanel jPanel2;

	private Recipe myRecipe;
	private StrangeSwing app;

	public ScaleRecipeDialog(JFrame frame) {
		super(frame);
		app = (StrangeSwing) frame;
		myRecipe = app.myRecipe;
		initGUI();
	}

	private void initGUI() {
		try {

			this.setTitle("Resize / Convert Recipe");		

			jPanel2 = new JPanel();
			FlowLayout jPanel2Layout = new FlowLayout();
			jPanel2Layout.setAlignment(FlowLayout.RIGHT);
			jPanel2.setLayout(jPanel2Layout);
			getContentPane().add(jPanel2, BorderLayout.SOUTH);

			jPanel3 = new JPanel();
			BoxLayout jPanel3Layout = new BoxLayout(jPanel3, javax.swing.BoxLayout.Y_AXIS);
			jPanel3.setLayout(jPanel3Layout);
			getContentPane().add(jPanel3, BorderLayout.CENTER);

			jPanel1 = new JPanel();
			jPanel3.add(jPanel1);

			jLabel1 = new JLabel();

			sizeTxt = new JTextField();
			sizeTxt.setText("" + myRecipe.getPostBoilVol(myRecipe.getVolUnits()));

			jPanel1.add(jLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0,
					0));
			jPanel1.add(sizeTxt, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
					new Insets(0, 0, 0, 0), 0, 0));

			sizeUnitsCombo = new JComboBox();
			SmartComboBox.enable(sizeUnitsCombo);
			jPanel1.add(sizeUnitsCombo, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0,
					0));
			sizeUnitsComboModel = new ComboModel<String>();
			sizeUnitsComboModel.setList(Quantity.getListofUnits("vol"));
			sizeUnitsComboModel.addOrInsert(myRecipe.getVolUnits());
			sizeUnitsCombo.setModel(sizeUnitsComboModel);

			jLabel1.setText("New Size:");

			GridBagLayout jPanel1Layout = new GridBagLayout();
			jPanel1Layout.rowWeights = new double[]{0.1, 0.1};
			jPanel1Layout.rowHeights = new int[]{7, 7};
			jPanel1Layout.columnWeights = new double[]{0.1, 0.1, 0.1};
			jPanel1Layout.columnWidths = new int[]{7, 7, 7};
			jPanel1.setLayout(jPanel1Layout);
			jPanel1.setBorder(BorderFactory.createTitledBorder("Resize"));

			jPanel4 = new JPanel();
			GridBagLayout jPanel4Layout = new GridBagLayout();
			jPanel4Layout.rowWeights = new double[]{0.1, 0.1};
			jPanel4Layout.rowHeights = new int[]{7, 7};
			jPanel4Layout.columnWeights = new double[]{0.1, 0.1};
			jPanel4Layout.columnWidths = new int[]{7, 7};
			jPanel4.setLayout(jPanel4Layout);
			jPanel3.add(jPanel4);
			jPanel4.setBorder(BorderFactory.createTitledBorder("Convert"));

			convMaltsChk = new JCheckBox();
			jPanel4.add(convMaltsChk, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0,
					0));
			convMaltsChk.setText("Convert Malts:");

			convHopsChk = new JCheckBox();
			jPanel4.add(convHopsChk, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0,
					0));
			convHopsChk.setText("Convert Hops:");

			maltUnitsComboModel = new ComboModel<String>();
			maltUnitsComboModel.setList(Quantity.getListofUnits("weight"));
			maltUnitsComboModel.addOrInsert(myRecipe.getMaltUnits());
			

			maltUnitsCombo = new JComboBox();
			SmartComboBox.enable(maltUnitsCombo);
			jPanel4.add(maltUnitsCombo, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0,
					0));
			maltUnitsCombo.setModel(maltUnitsComboModel);
			maltUnitsCombo.setPreferredSize(new java.awt.Dimension(96, 20));

			hopsUnitsComboModel = new ComboModel<String>();
			hopsUnitsComboModel.setList(Quantity.getListofUnits("weight"));
			hopsUnitsComboModel.addOrInsert(myRecipe.getHopUnits());

			hopsUnitsCombo = new JComboBox();
			SmartComboBox.enable(hopsUnitsCombo);
			jPanel4.add(hopsUnitsCombo, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0,
					0));
			hopsUnitsCombo.setModel(hopsUnitsComboModel);
			hopsUnitsCombo.setPreferredSize(new java.awt.Dimension(97, 20));

			cancelButton = new JButton();
			cancelButton.addActionListener(this);
			jPanel2.add(cancelButton);
			cancelButton.setText("Cancel");

			okBtn = new JButton();
			okBtn.addActionListener(this);
			jPanel2.add(okBtn);
			okBtn.setText("OK");

			this.setSize(306, 193);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void scaleRecipe() {

		String newUnits = sizeUnitsComboModel.getSelectedItem().toString();
		double newSize = Double.parseDouble(sizeTxt.getText());

		myRecipe.scaleRecipe(new Quantity(newUnits, newSize));
		
		if (convMaltsChk.isSelected()){
			for (int i=0;i<myRecipe.getMaltListSize();i++){
				myRecipe.getFermentable(i).convertTo(maltUnitsComboModel.getSelectedItem().toString());
			}
		}
		if (convHopsChk.isSelected()){
			for (int i=0;i<myRecipe.getHopsListSize();i++){
				myRecipe.getHop(i).convertTo(hopsUnitsComboModel.getSelectedItem().toString());
			}			
		}

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
		} else if (o == cancelButton) {
			setVisible(false);
			dispose();
		}

	}

}
