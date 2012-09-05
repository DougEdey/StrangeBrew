package ca.strangebrew.ui.swing;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import ca.strangebrew.FermentStep;
import ca.strangebrew.Recipe;


public class FermentPanel extends javax.swing.JPanel implements ActionListener, FocusListener {
	
	// Mutables
	private Recipe myRecipe;
	
	// Final GUI Elements
	final private BorderLayout fermentPanelLayout = new BorderLayout();
	final private JPanel fermentPanel = new JPanel();
	final private JScrollPane scrollFermentPanel = new JScrollPane();
	final private FlowLayout fermentButtonsPanelLayout = new FlowLayout();
	final private JPanel fermentButtonsPanel = new JPanel();
	final private FermentTableModel fermentTableModel = new FermentTableModel();
	final private JTable fermentTable = new JTable();
	final private JTable fermentTotalsTable = new JTable();
	final private DefaultTableModel fermentTotalsTableModel = new DefaultTableModel(
			FermentTableModel.getColumnNames(), 1);		
	final private JComboBox comboType = new JComboBox(FermentStep.types);
	final private JComboBox comboUnits = new JComboBox(new String[] {"F","C"});
	final private SBCellEditor timeEditor = new SBCellEditor(new JTextField());
	final private SBCellEditor tempEditor = new SBCellEditor(new JTextField());
	final private JToolBar fermentButtonBar = new JToolBar();
	final private JButton fermentAddButton = new JButton();
	final private JButton fermentDelButton = new JButton();
	
	public FermentPanel() {
		super();
		initGUI();
	}
	
	public void setData(Recipe r) {
		myRecipe = r;
		fermentTableModel.setData(myRecipe);
		fermentTable.updateUI();		
	}
	
	public void displayFerment(){
		fermentTotalsTableModel.setDataVector(
				new String[][]{{"Totals:", Integer.toString(myRecipe.getTotalFermentTime()), "", ""}},
				new String[]{"", "", "", ""}	
		);
	}
	
	private void initGUI() {
		try {
			// Set up main window			
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));			
			this.add(fermentPanel);
			this.add(fermentButtonsPanel);
			
			// Setup button panel
			fermentButtonsPanelLayout.setAlignment(FlowLayout.LEFT);
			fermentButtonsPanelLayout.setVgap(0);
			fermentButtonsPanel.setLayout(fermentButtonsPanelLayout);
			fermentButtonsPanel.setPreferredSize(new java.awt.Dimension(592, fermentButtonsPanel.getFont().getSize()*2));
			
			// Setup ferment panel
			fermentPanel.setBorder(BorderFactory.createTitledBorder(new LineBorder(
							new java.awt.Color(0, 0, 0), 1, false), "Fermentation Schedule",
								TitledBorder.LEADING, TitledBorder.TOP, new java.awt.Font("Dialog",
										1, 12), new java.awt.Color(51, 51, 51)));
			fermentPanel.setLayout(fermentPanelLayout);
			fermentPanel.add(scrollFermentPanel, BorderLayout.CENTER);
			fermentPanel.add(fermentTotalsTable, BorderLayout.SOUTH);
			
			// Setup scrolling table
			scrollFermentPanel.setViewportView(fermentTable);
			fermentTable.setModel(fermentTableModel);
			fermentTable.getTableHeader().setReorderingAllowed(false);
			// Setup totals table
			fermentTotalsTable.setModel(fermentTotalsTableModel);
			fermentTotalsTable.getTableHeader().setEnabled(false);
			fermentTotalsTable.setAutoCreateColumnsFromModel(false);
			//fermentButtonsPanel.setPreferredSize(new java.awt.Dimension(592, 27));
			
			// Setup special table column stamps on scrolling table
			{
				// type combo
				TableColumn col = fermentTable.getColumnModel().getColumn(0);			
				SmartComboBox.enable(comboType);
				col.setCellEditor(new SBComboBoxCellEditor(comboType));
				comboType.addActionListener(this);
				
				// time editor
				col = fermentTable.getColumnModel().getColumn(1);
				col.setCellEditor(timeEditor);

				// temp editor
				col = fermentTable.getColumnModel().getColumn(2);
				col.setCellEditor(tempEditor);
				
				// units editor
				col = fermentTable.getColumnModel().getColumn(3);
				// SmartComboBox.enable(comboUnits);
				col.setCellEditor(new SBComboBoxCellEditor(comboUnits));
				comboUnits.addActionListener(this);
			}						

			// Setup totals table column stamps
			{
				// Do nothing
			}
			
			// Setup Bottom Panel
			{
				fermentButtonsPanel.add(fermentButtonBar);
				fermentButtonBar.setPreferredSize(new java.awt.Dimension(386, fermentButtonBar.getFont().getSize()*2));
				fermentButtonBar.setFloatable(false);
				
				// Setup button bar
				{
					// Add button
					fermentButtonBar.add(fermentAddButton);
					fermentAddButton.setText("+");
					fermentAddButton.addActionListener(this);
					
					// Remove button
					fermentButtonBar.add(fermentDelButton);
					fermentDelButton.setText("-");
					fermentDelButton.addActionListener(this);					
				}
			}				
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();

		if (o == fermentAddButton) {
			if (myRecipe != null) {
				FermentStep f = new FermentStep();
				myRecipe.addFermentStep(f);
				fermentTable.updateUI();
				displayFerment();
			}
		} else if (o == fermentDelButton) {
			if (myRecipe != null) {
				int i = fermentTable.getSelectedRow();
				myRecipe.delFermentStep(i);	
				fermentTable.updateUI();
				displayFerment();
			}
		} else if (o == comboType) {
			String newType = (String)comboType.getSelectedItem();
			int index = fermentTable.getSelectedRow();
			if (myRecipe != null && index >= 0)
			{
				myRecipe.setFermentStepType(index, newType);
				fermentTable.updateUI();
				displayFerment();
			}			
		} else if (o == comboUnits) {
			String newUnit = (String)comboUnits.getSelectedItem();
			int index = fermentTable.getSelectedRow();
			if (myRecipe != null && index >= 0)
			{
				myRecipe.setFermentStepTempU(index, newUnit);
				fermentTable.updateUI();
				displayFerment();
			}	
		}
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
