package ca.strangebrew.ui.swing.dialogs;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;

import ca.strangebrew.Database;
import ca.strangebrew.Fermentable;
import ca.strangebrew.Hop;
import ca.strangebrew.Ingredient;
import ca.strangebrew.Misc;
import ca.strangebrew.SBStringUtils;
import ca.strangebrew.Yeast;
import ca.strangebrew.ui.swing.StrangeSwing;


public class NewIngrDialog extends javax.swing.JDialog {
	private JPanel jPanel1;
	private JTextArea jLabel1;
	private JPanel jPanel3;
	private JTable jTable1;
	private JScrollPane jScrollPane1;
	private JButton cancelButton;
	private JButton okButton;
	private JPanel jPanel2;
	private ArrayList<Ingredient> newIngrList;
	private boolean [] add;
	private Database db;
	private NewIngrTableModel jTable1Model;

	
	public NewIngrDialog() {		
		//super(frame);
		//newIngrList = list;
		initGUI();
	}
	
	public NewIngrDialog(JFrame frame, ArrayList<Ingredient> list) {		
		super(frame);
		newIngrList = list;
		db = ((StrangeSwing)frame).DB;
		initGUI();
	}
	
	private void initGUI() {
		try {

		jPanel1 = new JPanel();
		BorderLayout jPanel1Layout = new BorderLayout();
		jPanel1.setLayout(jPanel1Layout);
		getContentPane().add(jPanel1, BorderLayout.CENTER);
		jPanel1.setBorder(BorderFactory.createTitledBorder(null, "New Ingredients:", TitledBorder.LEADING, TitledBorder.TOP));

		jScrollPane1 = new JScrollPane();
		jPanel1.add(jScrollPane1, BorderLayout.CENTER);

		jTable1Model = new NewIngrTableModel();

		jTable1 = new JTable(){
			public String getToolTipText(MouseEvent e) {
				java.awt.Point p = e.getPoint();
				int rowIndex = rowAtPoint(p);
				return SBStringUtils.multiLineToolTip(40, jTable1Model
						.getDescriptionAt(rowIndex));

			}
		};
		
		jTable1.setModel(jTable1Model);
		jScrollPane1.setViewportView(jTable1);


		jPanel2 = new JPanel();
		getContentPane().add(jPanel2, BorderLayout.SOUTH);

		jPanel3 = new JPanel();
		BoxLayout jPanel3Layout = new BoxLayout(jPanel3, javax.swing.BoxLayout.X_AXIS);
		jPanel3.setLayout(jPanel3Layout);
		getContentPane().add(jPanel3, BorderLayout.NORTH);

		jLabel1 = new JTextArea();
		jPanel3.add(jLabel1);
		jLabel1.setText("New ingredients detected!  The recipe you've opened contains ingredients not in your database.  Would you like to save these permanently so you can use them in other recipes?");
		jLabel1.setEditable(false);
		jLabel1.setLineWrap(true);
		jLabel1.setWrapStyleWord(true);

		okButton = new JButton("Save");
		jPanel2.add(okButton);		
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {								
				writeIngr();
				setVisible(false);
				dispose();
			}
		});

		cancelButton = new JButton("Cancel");
		jPanel2.add(cancelButton);		
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {								
				setVisible(false);
				dispose();
			}
		});

			setSize(400, 300);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	class NewIngrTableModel extends AbstractTableModel {
		
		private String[] columnNames = {"Add?", "Type", "Name"};

		private ArrayList data = null;			

		
		public NewIngrTableModel() {
			data = newIngrList;
			add = new boolean[data.size()];
			for (int i=0; i<data.size(); i++)
				add[i] = true;
		}


		public int getColumnCount() {
			return columnNames.length;
		}

		public int getRowCount() {
			if (data != null)
				return data.size();
			else
				return 0;
		}

		public String getColumnName(int col) {
			return columnNames[col];
		}
		
		public String getDescriptionAt(int row) {
			if (row < 0)
				return "";
			Ingredient i = (Ingredient)(data.get(row));
			return i.getDescription();
		}

		public Object getValueAt(int row, int col) {
			
			Ingredient i = (Ingredient)(data.get(row));
			
			try {
				switch (col) {
					case 0 :
						return new Boolean(add[row]);
					case 1 :
						String t = data.get(row).getClass().getName();						
						return t.substring("ca.strangebrew.".length(),t.length());
					case 2 :
						return i.getName();
					

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
			if (col > 1) {
				return false;
			} else {
				return true;
			}
		}
		
		// makes the boolean into a checkbox
		public Class<?> getColumnClass(int c) {
	        return getValueAt(0, c).getClass();
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
						add[row] = Boolean.valueOf(value.toString()).booleanValue();
						break;
					case 1 :
						// data.setMaltPppg(row, Double.parseDouble(value.toString()));
						break;
					case 2 :
						// data.setMaltLov(row, Double.parseDouble(value.toString()));
						break;					

				}
			} catch (Exception e) {
			};			
			fireTableCellUpdated(row, col);
			fireTableDataChanged();				
			
			
		}
	}
	
	private void writeIngr(){

		// first, do we need to do this?
		boolean addHops = false;
		boolean addFerm = false;
		boolean addYeast = false;
		boolean addMisc = false;
		for (int i=0;i<newIngrList.size();i++){
			if (newIngrList.get(i) instanceof Hop && add[i])
					addHops = true;	
			if (newIngrList.get(i) instanceof Fermentable && add[i])
					addFerm = true;
			if (newIngrList.get(i) instanceof Yeast && add[i])
					addYeast = true;
			if (newIngrList.get(i) instanceof Misc && add[i])
					addMisc = true;
		}
		
		// now do the writing:
		if (addHops) db.writeHops(newIngrList, add);
		if (addFerm) db.writeFermentables(newIngrList, add);
		if (addYeast) db.writeYeast(newIngrList, add);
		if (addMisc) db.writeMisc(newIngrList, add);
		
	}

}
