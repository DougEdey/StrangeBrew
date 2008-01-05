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
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import ca.strangebrew.Quantity;
import ca.strangebrew.Recipe;
import ca.strangebrew.SBStringUtils;
import ca.strangebrew.ui.swing.ComboModel;
import ca.strangebrew.ui.swing.SmartComboBox;
import ca.strangebrew.ui.swing.StrangeSwing;


public class MaltPercentDialog extends javax.swing.JDialog {
	private JScrollPane jPanel1;
	private JSpinner weightSpn;
	private JPanel jPanel5;
	private JRadioButton OGBtn;
	private JRadioButton weightBtn;
	private JComboBox weightUCombo;
	private JPanel jPanel4;
	private JSpinner OGSpn;
	private JPanel jPanel3;
	private JButton cancelBtn;
	private JTable maltTable;
	private JButton okBtn;
	private JPanel jPanel2;
	private Recipe myRecipe;
	private StrangeSwing app;
	private ButtonGroup bg = new ButtonGroup();


	public MaltPercentDialog(JFrame frame) {
		super(frame);
		app = (StrangeSwing)frame;
		myRecipe = app.myRecipe;
		initGUI();
	}

	private double calcOG(double weightLbs){
		double OG=0;
		for (int i = 0; i < myRecipe.getMaltListSize(); i++) {
			double points = (myRecipe.getMaltPercent(i) / 100) 
				* weightLbs 
				* (myRecipe.getMaltPppg(i) - 1.0)
				/ myRecipe.getPostBoilVol(Quantity.GAL);
			if (myRecipe.getMaltMashed(i))
				points *= myRecipe.getEfficiency() / 100;
			OG += points;
		}
		return OG;
	}
	
	private void calcWeights() {
		// we've set the % in the fermentables list, now
		// let's figure out the correct amounts:
		
		// by weight?
		if (weightBtn.isSelected()) {
			double totalWeight = Double.parseDouble(weightSpn.getValue().toString());
			// convert to lbs
			totalWeight = Quantity.convertUnit(weightUCombo.getSelectedItem().toString(), Quantity.LB, totalWeight);
			for (int i = 0; i < myRecipe.getMaltListSize(); i++) {
				double newAmount = totalWeight * myRecipe.getMaltPercent(i) / 100;
				myRecipe.setMaltAmountAs(i, newAmount, Quantity.LB);
			}
		}
		
		// no, by OG
		else {

			double targOG = Double.parseDouble(OGSpn.getValue().toString());
			
			// "seed" value -- assume conservative 30 ppppg
			double totalPoints = (targOG - 1) * 1000 * myRecipe.getPostBoilVol(Quantity.GAL);
			double totalWeightLbs = (totalPoints / 30) * (myRecipe.getEfficiency() / 100);

			// brute force - just keep incrementing the total grain bill
			// by .1 lb until it's close to what we want (95%)
						
			while (calcOG(totalWeightLbs) < ((targOG -1))*0.95) {
				totalWeightLbs += 0.1;
			}
			
			while (calcOG(totalWeightLbs) < (targOG -1)) {
				totalWeightLbs += 0.01;
			}
	
			for (int k = 0; k < myRecipe.getMaltListSize(); k++) {
				double newAmount = totalWeightLbs * (myRecipe.getMaltPercent(k)/ 100); 
				myRecipe.setMaltAmountAs(k, newAmount, Quantity.LB);
			}

			
		}
		myRecipe.calcMaltTotals();
		
	}
	
	private void initGUI() {
		try {
			
			this.setTitle("Malt Percent");

		TableModel maltTableModel = new MaltPercentTableModel();

		jPanel5 = new JPanel();
		BoxLayout jPanel5Layout = new BoxLayout(jPanel5, javax.swing.BoxLayout.Y_AXIS);
		jPanel5.setLayout(jPanel5Layout);
		getContentPane().add(jPanel5, BorderLayout.CENTER);

		jPanel3 = new JPanel();
		jPanel5.add(jPanel3);
		BoxLayout jPanel3Layout = new BoxLayout(jPanel3, javax.swing.BoxLayout.Y_AXIS);
		jPanel3.setLayout(jPanel3Layout);

		jPanel1 = new JScrollPane();
		jPanel3.add(jPanel1);
		jPanel1.setPreferredSize(new java.awt.Dimension(392, 252));
		jPanel1.setBorder(BorderFactory.createTitledBorder("Fermentables"));

		jPanel4 = new JPanel();
		GridBagLayout jPanel4Layout = new GridBagLayout();
		jPanel4Layout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
		jPanel4Layout.rowHeights = new int[] {7, 7, 7, 7};
		jPanel4Layout.columnWeights = new double[] {0.1, 0.2, 0.1, 0.1};
		jPanel4Layout.columnWidths = new int[] {5, 9, 7, 7};
		jPanel4.setLayout(jPanel4Layout);
		jPanel3.add(jPanel4);

		weightBtn = new JRadioButton("Target weight:");		
		jPanel4.add(weightBtn, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		bg.add(weightBtn);
		weightBtn.setSelected(true);
		

		weightSpn = new JSpinner();
		jPanel4.add(weightSpn, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		// SpinnerListModel weightSpnModel = new SpinnerListModel(new String[] { "Sun", "Mon", "Tue",
		// 		"Wed", "Thu", "Fri", "Sat" });
		// weightSpn.setModel(weightSpnModel);
		weightSpn.setValue(new Double(myRecipe.getTotalMalt()));

		OGBtn = new JRadioButton("Target OG:");
		jPanel4.add(OGBtn, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		bg.add(OGBtn);


		OGSpn = new JSpinner();
		SpinnerNumberModel OGSpnModel = new SpinnerNumberModel(1.000, 0.900,
				2.000, 0.001);
		jPanel4.add(OGSpn, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		OGSpn.setModel(OGSpnModel);

		ComboModel<String> weightUComboModel = new ComboModel<String>();
		weightUComboModel.setList(Quantity.getListofUnits("weight"));
		weightUComboModel.addOrInsert(myRecipe.getMaltUnits());

		weightUCombo = new JComboBox();
		SmartComboBox.enable(weightUCombo);
		jPanel4.add(weightUCombo, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		weightUCombo.setModel(weightUComboModel);

		OGSpn.setValue(new Double(myRecipe.getEstOg()));

		maltTable = new JTable();
		jPanel1.setViewportView(maltTable);
		maltTable.setModel(maltTableModel);
		maltTable.setPreferredSize(new java.awt.Dimension(203, 84));

		jPanel2 = new JPanel();
		jPanel5.add(jPanel2);
		FlowLayout jPanel2Layout = new FlowLayout();
		jPanel2Layout.setAlignment(FlowLayout.RIGHT);
		jPanel2.setLayout(jPanel2Layout);

		cancelBtn = new JButton();
		jPanel2.add(cancelBtn);
		cancelBtn.setText("Cancel");

		okBtn = new JButton();
		jPanel2.add(okBtn);
		okBtn.setText("OK");
		okBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				calcWeights();				
				setVisible(false);
				app.displayRecipe();
				app.maltTable.updateUI();
				dispose();
			}
		});

		cancelBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				setVisible(false);
				dispose();
			}
		});

		// jPanel1.add(maltTable, BorderLayout.CENTER);

			setSize(400, 300);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	class MaltPercentTableModel extends AbstractTableModel {
		
		private String[] columnNames = {"Malt", "Points", "Lov", "%"};

		private Recipe data = null;
		// private double[] percents;

		
		public MaltPercentTableModel() {
			data = myRecipe;						
		}


		public int getColumnCount() {
			return columnNames.length;
		}

		public int getRowCount() {
			if (data != null)
				return data.getMaltListSize();
			else
				return 0;
		}

		public String getColumnName(int col) {
			return columnNames[col];
		}
		
		public String getDescriptionAt(int row) {
			if (row < 0)
				return "";
			return data.getMaltDescription(row);
		}

		public Object getValueAt(int row, int col) {
			// Fermentable m = (Fermentable) data.get(row);
			try {
				switch (col) {
					case 0 :
						return data.getMaltName(row);
					case 1 :
						return new Double(data.getMaltPppg(row));
					case 2 :
						return new Double(data.getMaltLov(row));
					case 3 :
						return SBStringUtils.format(data.getMaltPercent(row),1);

				}
			} catch (Exception e) {
			};
			return "";
		}

		/*
		 * JTable uses this method to determine the default renderer/ editor for
		 * each cell. If we didn't implement this method, then the last column
		 * would contain text ("true"/"false"), rather than a check box.
		 */
		
	/*	public Class getColumnClass(int c) {
			return getValueAt(0, c).getClass();
		}*/

		/*
		 * Don't need to implement this method unless your table's editable.
		 */
		public boolean isCellEditable(int row, int col) {
			//Note that the data/cell address is constant,
			//no matter where the cell appears onscreen.
			if (col < 2) {
				return false;
			} else {
				return true;
			}
		}

		/*
		 * Don't need to implement this method unless your table's data can
		 * change.
		 */
		public void setValueAt(Object value, int row, int col) {

			// Fermentable m = (Fermentable) data.get(row);
			try {
				switch (col) {
					case 0 :
						// data.setMaltName(row, value.toString());
						break;
					case 1 :
						// data.setMaltPppg(row, Double.parseDouble(value.toString()));
						break;
					case 2 :
						// data.setMaltLov(row, Double.parseDouble(value.toString()));
						break;
					case 3 :
						data.setMaltPercent(row, Double.parseDouble(value.toString()));
						break;

				}
			} catch (Exception e) {
			};			
			fireTableCellUpdated(row, col);
			fireTableDataChanged();				
			
			
		}
	}

}
