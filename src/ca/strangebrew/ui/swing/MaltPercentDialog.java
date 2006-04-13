package ca.strangebrew.ui.swing;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import ca.strangebrew.Fermentable;
import ca.strangebrew.Recipe;
import ca.strangebrew.SBStringUtils;




/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class MaltPercentDialog extends javax.swing.JDialog {
	private JScrollPane jPanel1;
	private JPanel jPanel3;
	private JButton cancelBtn;
	private JTable maltTable;
	private JButton okBtn;
	private JPanel jPanel2;
	private Recipe myRecipe;
	private StrangeSwing app;

	/**
	* Auto-generated main method to display this JDialog
	*/
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		MaltPercentDialog inst = new MaltPercentDialog(frame);
		inst.setVisible(true);
	}
	
	public MaltPercentDialog(JFrame frame) {
		super(frame);
		app = (StrangeSwing)frame;
		myRecipe = app.myRecipe;
		initGUI();
	}

	private void calcWeights() {
		// we've set the % in the fermentables list, now
		// let's figure out the correct amounts:
		for (int i=0; i<myRecipe.getMaltListSize(); i++){
			double newAmount = myRecipe.getTotalMaltLbs() *  myRecipe.getMaltPercent(i) / 100;
			myRecipe.setMaltAmountAs(i,newAmount,"lb" );
		}
		myRecipe.calcMaltTotals();
	}
	
	private void initGUI() {
		try {
			
			this.setTitle("Malt Percent");

		TableModel maltTableModel = new MaltPercentTableModel();

		jPanel2 = new JPanel();
		FlowLayout jPanel2Layout = new FlowLayout();
		jPanel2Layout.setAlignment(FlowLayout.RIGHT);
		jPanel2.setLayout(jPanel2Layout);
		getContentPane().add(jPanel2, BorderLayout.SOUTH);

		jPanel3 = new JPanel();
		getContentPane().add(jPanel3, BorderLayout.NORTH);

		jPanel1 = new JScrollPane();
		jPanel3.add(jPanel1);
		jPanel1.setPreferredSize(new java.awt.Dimension(392, 252));
		jPanel1.setBorder(BorderFactory.createTitledBorder("Fermentables"));

		maltTable = new JTable();
		// jPanel1.add(maltTable, BorderLayout.CENTER);
		jPanel1.setViewportView(maltTable);
		maltTable.setModel(maltTableModel);
		maltTable.setPreferredSize(new java.awt.Dimension(203, 84));

		cancelBtn = new JButton();
		jPanel2.add(cancelBtn);
		cancelBtn.setText("Cancel");
		cancelBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				setVisible(false);
				dispose();
			}
		});

		okBtn = new JButton();
		jPanel2.add(okBtn);
		okBtn.setText("OK");

		okBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				calcWeights();
				setVisible(false);
				dispose();
			}
		});

			setSize(400, 300);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	class MaltPercentTableModel extends AbstractTableModel {
		
		private String[] columnNames = {"Malt", "Points", "Lov", "%"};

		private Recipe data = null;
		private double[] percents;

		
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
						return SBStringUtils.df1.format(new Double(data.getMaltPercent(row)));

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
