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
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.text.ParseException;
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
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneLayout;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import ca.strangebrew.BrewCalcs;
import ca.strangebrew.Database;
import ca.strangebrew.Debug;
import ca.strangebrew.FermentStep;
import ca.strangebrew.Fermentable;
import ca.strangebrew.Hop;
import ca.strangebrew.Options;
import ca.strangebrew.Quantity;
import ca.strangebrew.SBStringUtils;
import ca.strangebrew.WaterProfile;
import ca.strangebrew.ui.swing.ComboModel;
import ca.strangebrew.ui.swing.FermentTableModel;
import ca.strangebrew.ui.swing.PantryHopsTableModel;
import ca.strangebrew.ui.swing.PantryFermTableModel;
import ca.strangebrew.ui.swing.SBCellEditor;
import ca.strangebrew.ui.swing.SBComboBoxCellEditor;
import ca.strangebrew.ui.swing.SBTable;
import ca.strangebrew.ui.swing.SmartComboBox;
import ca.strangebrew.ui.swing.StrangeSwing;


public class Pantry extends javax.swing.JDialog implements ActionListener, ChangeListener, FocusListener {

	private Database db;
	
	public static void main(String[] args) {
		Debug.print("In Pantry Main");
		final JFrame frame = new JFrame();
		Pantry inst = new Pantry(frame);
		inst.setVisible(true);
	}
	
	// Mutables
	private Options opts;
	PantryFermTableModel pantryFermTableModel;
	PantryHopsTableModel pantryHopsTableModel;
	SBTable pantryFermTable;
	SBTable pantryHopsTable;
	// Final GUI Elements
	final private JPanel pnlTables = new JPanel();
	final private JComboBox maltComboBox = new JComboBox();
	final private JComboBox hopsComboBox = new JComboBox();

	final private JComboBox maltUnitsComboBox = new JComboBox();
	final private JComboBox hopsUnitsComboBox = new JComboBox();
	final private SBCellEditor hopAcidEditor = new SBCellEditor(new JTextField());
	final private JPanel pnlMalt = new JPanel();
	final private JPanel pnlHops = new JPanel();
	final private JPanel pnlMaltButtons = new JPanel();
	
	final private JButton okButton = new JButton();
	final private JButton cancelButton = new JButton();
		
	final private JPanel jPanel1 = new JPanel();
	final private JScrollPane jScrollPane1 = new JScrollPane();
	final private JScrollPane jScrollPane2 = new JScrollPane();
	final private JSeparator jSeparator1 = new JSeparator();
	final private JSeparator jSeparator2 = new JSeparator();
/*	private ArrayList looks;*/
	final private ComboModel<Hop> cmbHopsModel = new ComboModel<Hop>();
	final private ComboModel<String> cmbHopsUnitsModel = new ComboModel<String>();
	final private ComboModel<Fermentable> cmbMaltModel = new ComboModel<Fermentable>();
	final private ComboModel<String> cmbMaltUnitsModel = new ComboModel<String>();
	
	final private SBCellEditor maltAmountEditor = new SBCellEditor(new JTextField());
	final private SBCellEditor hopAmountEditor = new SBCellEditor(new JTextField());
	
	final private JToolBar tlbHops = new JToolBar();
	final private JToolBar tlbMalt = new JToolBar();

	// Buttons
	final private JButton btnAddHop = new JButton();
	final private JButton btnAddMalt = new JButton();
	final private JButton btnDelHop = new JButton();
	final private JButton btnDelMalt = new JButton();
	final private Frame sb;

	public Pantry(Frame owner) {
		
		super(owner, "Pantry", true);
		opts = Options.getInstance();
		sb = owner;
		db = ((StrangeSwing)owner).DB;
		
		this.layoutUi();		
		setLocation(owner.getLocation());
		//setOptions();				
		Debug.print("In Pantry Constructor "+db.fermDB.size());
	}


	private void layoutUi() {
		
		cmbMaltUnitsModel.setList(Quantity.getListofUnits("weight"));
		cmbHopsUnitsModel.setList(Quantity.getListofUnits("weight"));
		
		GridLayout gridBag = new GridLayout(0,1);
		GridBagConstraints c = new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
		c.fill = GridBagConstraints.HORIZONTAL;
		
		c = null;
		
		Debug.print("Pantry layout started");
		BoxLayout pnlMaltsLayout = new BoxLayout(pnlTables, javax.swing.BoxLayout.Y_AXIS);
		pnlTables.setLayout(gridBag);
		
		pnlTables.setBorder(BorderFactory.createTitledBorder("Manage the pantry"));
		getContentPane().add(pnlTables);
				/*, new GridBagConstraints(0, 2, 1, 1, 0.5, 0.5, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));*/
		this.setTitle("Manage Pantry");
		this.setSize(opts.getIProperty("winWidth"),opts.getIProperty("winHeight"));
		pnlTables.setLayout(pnlMaltsLayout);
		{
			pnlTables.add(pnlMalt);
			BorderLayout pnlMaltLayout1 = new BorderLayout();
			pnlMalt.setBorder(BorderFactory.createTitledBorder(new LineBorder(new java.awt.Color(0, 0, 0),
					1, false), "Fermentables", TitledBorder.LEADING, TitledBorder.TOP, new java.awt.Font(
					"Dialog", 1, 12), new java.awt.Color(51, 51, 51)));
			pnlMalt.setLayout(pnlMaltLayout1);
			{
				pnlMalt.add(jScrollPane1, BorderLayout.CENTER);
				{
					pantryFermTableModel = new PantryFermTableModel();
					pantryFermTable = new SBTable("maltTable") {
						public String getToolTipText(MouseEvent e) {
							java.awt.Point p = e.getPoint();
							int rowIndex = rowAtPoint(p);
							return SBStringUtils.multiLineToolTip(40, pantryFermTableModel
									.getDescriptionAt(rowIndex));

						}
					};

					jScrollPane1.setViewportView(pantryFermTable);
					pantryFermTableModel.setData(db.fermDB);
					pantryFermTable.setModel(pantryFermTableModel);
					
					// pantryFermTable.setAutoCreateColumnsFromModel(false);
					pantryFermTable.getTableHeader().setReorderingAllowed(false);

					TableColumn maltColumn = pantryFermTable.getColumnModel().getColumn(2);

					// set up malt list combo
					SmartComboBox.enable(maltComboBox);
					maltComboBox.setModel(cmbMaltModel);
					
					maltColumn.setCellEditor(new SBComboBoxCellEditor(maltComboBox));

					// set up malt amount editor
					maltColumn = pantryFermTable.getColumnModel().getColumn(3);
					maltColumn.setCellEditor(maltAmountEditor);

					// set up malt units combo
					SmartComboBox.enable(maltUnitsComboBox);
					maltUnitsComboBox.setModel(cmbMaltUnitsModel);
					maltColumn = pantryFermTable.getColumnModel().getColumn(4);
					maltColumn.setCellEditor(new SBComboBoxCellEditor(maltUnitsComboBox));

				}
			}
			
		}
		BorderLayout pnlHopsLayout = new BorderLayout();
		pnlHops.setBorder(BorderFactory.createTitledBorder(new LineBorder(new java.awt.Color(0, 0, 0),
				1, false), "Hops", TitledBorder.LEADING, TitledBorder.TOP, new java.awt.Font("Dialog",
				1, 12), new java.awt.Color(51, 51, 51)));
		pnlHops.setLayout(pnlHopsLayout);
		pnlTables.add(pnlHops);
		{
			pnlHops.add(jScrollPane2, BorderLayout.CENTER);
			{
				pantryHopsTableModel = new PantryHopsTableModel(db.hopsDB);
				pantryHopsTable = new SBTable("pantryHopsTable") {
					public String getToolTipText(MouseEvent e) {
						java.awt.Point p = e.getPoint();
						int rowIndex = rowAtPoint(p);
						return SBStringUtils.multiLineToolTip(40, pantryHopsTableModel
								.getDescriptionAt(rowIndex));

					}
				};
				jScrollPane2.setViewportView(pantryHopsTable);
				pantryHopsTable.setModel(pantryHopsTableModel);
				pantryHopsTable.getTableHeader().setReorderingAllowed(false);

				TableColumn hopColumn = pantryHopsTable.getColumnModel().getColumn(0);
				
				// Install the custom key selection manager
			/*	SmartComboBox.enable(hopsUnitsComboBox);
				hopsUnitsComboBox.setModel(cmbHopsModel);
				hopColumn.setCellEditor(new SBComboBoxCellEditor(hopsUnitsComboBox));*/

				// set up hop alpha acid editor
				hopColumn = pantryHopsTable.getColumnModel().getColumn(2);
				hopColumn.setCellEditor(hopAcidEditor);

				// set up hop stock editor
				hopColumn = pantryHopsTable.getColumnModel().getColumn(3);
				hopColumn.setCellEditor(hopAmountEditor);

				// set up hop units combo
				SmartComboBox.enable(hopsUnitsComboBox);
				hopsUnitsComboBox.setModel(cmbHopsUnitsModel);
				hopColumn = pantryHopsTable.getColumnModel().getColumn(4);
				hopColumn.setCellEditor(new SBComboBoxCellEditor(hopsUnitsComboBox));

				// set up hop type combo
				JComboBox hopsFormComboBox = new JComboBox(Hop.forms);
				SmartComboBox.enable(hopsFormComboBox);
				hopColumn = pantryHopsTable.getColumnModel().getColumn(1);
				hopColumn.setCellEditor(new SBComboBoxCellEditor(hopsFormComboBox));

				
			}
		}
	
		{
			pnlTables.add(pnlMaltButtons);
			FlowLayout pnlMaltButtonsLayout = new FlowLayout();
			pnlMaltButtonsLayout.setAlignment(FlowLayout.LEFT);
			pnlMaltButtonsLayout.setVgap(0);
			pnlMaltButtons.setLayout(pnlMaltButtonsLayout);
			pnlMaltButtons.setPreferredSize(new java.awt.Dimension(592, 27));
			{
				
				
				pnlMaltButtons.add(okButton);
				okButton.setText("OK");
				okButton.addActionListener(this);
				
				pnlMaltButtons.add(cancelButton, c);
				cancelButton.setText("Cancel");
				cancelButton.addActionListener(this);
			}
		}
		
	}	

	//	Action Performed 
	public void actionPerformed(ActionEvent e) {
		
		Object o = e.getSource();
		
		Debug.print("Action performed on: " + o);
		if(o == okButton) {
			// update everything in the fermDB
			db.fermDB = pantryFermTableModel.data;			
			db.writeFermentables();
			
			db.hopsDB = pantryHopsTableModel.data;
			db.writeHops();
			
			setVisible(false);
			dispose();
			
		} else if(o == cancelButton) {
			dispose();
		} else if (o == btnAddMalt) {
			Fermentable f = new Fermentable();
			pantryFermTableModel.addRow(f);
			pantryFermTable.updateUI();
		} else if (o == maltUnitsComboBox) {
			String u = (String) cmbMaltUnitsModel.getSelectedItem();
			int i = pantryFermTable.getSelectedRow();
			pantryFermTableModel.data.get(i).setUnits(u);
			pantryFermTableModel.data.get(i).convertTo(u);
		} else if (o == hopsUnitsComboBox) {
			String u = (String) cmbHopsUnitsModel.getSelectedItem();
			int i = pantryHopsTable.getSelectedRow();
			pantryHopsTableModel.data.get(i).setUnits(u);
			pantryHopsTableModel.data.get(i).convertTo(u);
		} 
	}
	
	public void focusLost(FocusEvent e) {
		Debug.print("Focus changed on: " + e.getSource());
		if(e.getSource() == pantryFermTable) {
			// this is where we have lost focus, so now we need to populate the data
			// See if we can find the Fermentable
			/*
			Fermentable temp = new Fermentable();
			temp.setName(txtName.getText());
			int result = db.find(temp);
			Debug.print("Searching for " + txtName.getText() + " found: " + result);
			if(result >= 0) {
			
				// we have the index, load it into the hop
				temp = db.fermDB.get(result);
				// set the fields
				if(Double.toString(temp.getPppg())!= null)
					txtYield.setText(Double.toString(temp.getPppg()));
				
				if(Double.toString(temp.getLov()) != null)
					txtCost.setText(Double.toString(temp.getLov()));
				
				if(Double.toString(temp.getCostPerU()) != null)
					txtCost.setText(Double.toString(temp.getCostPerU()));
				
				if(Double.toString(temp.getStock()) != null)
					txtStock.setText(Double.toString(temp.getStock()));
				
				if(temp.getUnits() != null)
					cUnits.setSelectedItem(temp.getUnits());
				
				if(temp.getMashed()) 
					bMash.setSelected(temp.getMashed());
				
				if(temp.getSteep()) 
					bSteep.setSelected(temp.getSteep());
				
				if(temp.getModified()) 
					bModified.setSelected(temp.getModified());
				
				if(temp.getDescription() != null){
					
					txtDescr.setText(temp.getDescription());
					jScrollDescr.invalidate();
				}
				
				jScrollDescr.getVerticalScrollBar().setValue(0);
				jScrollDescr.revalidate();
				
			}*/
			
//			pantryFermTable.setValueAt(aValue, row, column)
		}
		
		
		
	}
	
	public void stateChanged(ChangeEvent e){
		Object o = e.getSource();
		
		Debug.print("State changed on: " + o);
		
		
	}


	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub
		
	}
	

}