/**
 *  StrangeBrew Java - a homebrew recipe calculator
    Copyright (C) 2005  Drew Avis

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

package ca.strangebrew.ui.swing;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import ca.strangebrew.Misc;
import ca.strangebrew.Recipe;
import ca.strangebrew.SBStringUtils;


public class MiscPanel extends javax.swing.JPanel {
	final private JScrollPane jScrollPane1 = new JScrollPane();
	final private JPanel buttonPanel = new JPanel();
	final private JPanel commentsPanel = new JPanel();
	final private JScrollPane miscTableScrollPane = new JScrollPane();
	final private JButton delButton = new JButton();
	final private JButton addButton = new JButton();
	final private JPanel tablePanel = new JPanel();
	final private JTextArea miscCommentsTextArea = new JTextArea();
	final private JComboBox stageComboBox = new JComboBox(Misc.stages);	
	final private JComboBox miscComboBox = new JComboBox();
	final private SBCellEditor miscAmountEditor = new SBCellEditor(new JTextField());
	final private SBCellEditor miscTimeEditor = new SBCellEditor(new JTextField());	
	final private MiscTableModel miscTableModel = new MiscTableModel();
	final private ComboModel miscComboModel = new ComboModel();
	final private FlowLayout buttonPanelLayout = new FlowLayout();


	private Recipe myRecipe;
	private int selectedRow; // the selected row of the table
	private JTable miscTable;

	
	public MiscPanel(Recipe r) {		
		super();
		myRecipe = r;		
		initGUI();		
	}
	
	public void setData(Recipe r) {
		myRecipe = r;
		miscTableModel.setData(myRecipe);
	}
	
	public void setList(ArrayList miscList){
		miscComboModel.setList(miscList);
	}


	private void initGUI() {
		try {
			this.setLayout(new BoxLayout(this, javax.swing.BoxLayout.X_AXIS));
			this.setPreferredSize(new Dimension(400, 300));
			{
				this.add(tablePanel);
				tablePanel.setLayout(new BoxLayout(tablePanel, javax.swing.BoxLayout.Y_AXIS));
				tablePanel.setPreferredSize(new java.awt.Dimension(261, 175));
				{
					tablePanel.add(miscTableScrollPane);
					{
						miscTable = new JTable() {						
							public String getToolTipText(MouseEvent e) {
								java.awt.Point p = e.getPoint();
								int rowIndex = rowAtPoint(p);
								return SBStringUtils.multiLineToolTip(40, miscTableModel
										.getDescriptionAt(rowIndex));

							}
						};
						
						miscTableScrollPane.setViewportView(miscTable);
						miscTable.setModel(miscTableModel);
						
						// set preferred widths of the misc table
						TableColumnModel mtcm = miscTable.getColumnModel();
						miscTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
						TableColumn col = mtcm.getColumn(0);
						col.setPreferredWidth(200);
						miscTable.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
						
	
						// set up name combo
						SmartComboBox.enable(miscComboBox);
						miscComboBox.setModel(miscComboModel);
						TableColumn miscColumn = miscTable.getColumnModel().getColumn(0);
						miscColumn.setCellEditor(new SBComboBoxCellEditor(miscComboBox));
						miscComboBox.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								Misc m = (Misc) miscComboModel.getSelectedItem();
								int i = miscTable.getSelectedRow();
								if (myRecipe != null && i != -1) {
									Misc m2 = myRecipe.getMisc(i);
									m2.setStage(m.getStage());									
									m2.setDescription(m.getDescription());
									m2.setCost(m.getCostPerU());									
								}

							}
						});
						
						// set up misc amount editor
						miscColumn = miscTable.getColumnModel().getColumn(1);
						miscColumn.setCellEditor(miscAmountEditor);								
						
						// set up stage combo
						SmartComboBox.enable(stageComboBox);
						miscColumn = miscTable.getColumnModel().getColumn(4);
						miscColumn.setCellEditor(new SBComboBoxCellEditor(stageComboBox));
						
						// set up misc time editor
						miscColumn = miscTable.getColumnModel().getColumn(5);
						miscColumn.setCellEditor(miscTimeEditor);								
					}
				}
				{
					buttonPanelLayout.setAlignment(FlowLayout.LEFT);
					buttonPanel.setLayout(buttonPanelLayout);
					tablePanel.add(buttonPanel);
					{
						buttonPanel.add(addButton);
						addButton.setText("+");
						addButton.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								if (myRecipe != null) {									
									Misc m = new Misc();
									myRecipe.addMisc(m);											
									miscTable.updateUI();									
								}
								
							}
						});
					}
					{
						buttonPanel.add(delButton);
						delButton.setText("-");
						delButton.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								if (myRecipe != null) {									
									int i = miscTable.getSelectedRow();
									myRecipe.delMisc(i);
									miscTable.updateUI();					
								}

							}
						});
					}
				}
			}
			{
				commentsPanel.setLayout(new BoxLayout(commentsPanel, javax.swing.BoxLayout.Y_AXIS));
				this.add(commentsPanel);
				commentsPanel.setBorder(BorderFactory.createTitledBorder(new LineBorder(new java.awt.Color(0,0,0), 1, false), "Comments", TitledBorder.LEADING, TitledBorder.TOP));
				commentsPanel.setPreferredSize(new java.awt.Dimension(147, 300));
				{
					commentsPanel.add(jScrollPane1);
					{
						jScrollPane1.setViewportView(miscCommentsTextArea);						
						miscCommentsTextArea.setLineWrap(true);
						miscCommentsTextArea.setWrapStyleWord(true);
						miscCommentsTextArea.setPreferredSize(new java.awt.Dimension(136, 272));

						miscCommentsTextArea.addFocusListener(new FocusAdapter() {
							public void focusLost(FocusEvent evt) {								
								if ( selectedRow > -1 && !miscCommentsTextArea.getText().equals(myRecipe.getMiscComments(selectedRow))) {
									myRecipe.setMiscComments(selectedRow, miscCommentsTextArea.getText());									
								}
							}
						});	
						miscCommentsTextArea.addFocusListener(new FocusAdapter() {
							public void focusGained(FocusEvent evt) {
								selectedRow = miscTable.getSelectedRow();
							}
						});
						
						
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	
}
