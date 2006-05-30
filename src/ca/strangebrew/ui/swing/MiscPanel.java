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
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import ca.strangebrew.Misc;
import ca.strangebrew.Recipe;
import ca.strangebrew.SBStringUtils;


public class MiscPanel extends javax.swing.JPanel {
	private JTable miscTable;
	private JScrollPane jScrollPane1;
	private JPanel buttonPanel;
	private JPanel commentsPanel;
	private JScrollPane miscTableScrollPane;
	private JButton delButton;
	private JButton addButton;
	private JPanel tablePanel;
	private JTextArea miscCommentsTextArea;
	
	private Recipe myRecipe;
	private MiscTableModel miscTableModel;
	private ComboModel miscComboModel;
	private int selectedRow; // the selected row of the table
	
	
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
			BoxLayout thisLayout = new BoxLayout(this, javax.swing.BoxLayout.X_AXIS);
			this.setLayout(thisLayout);
			setPreferredSize(new Dimension(400, 300));
			{
				tablePanel = new JPanel();
				this.add(tablePanel);
				BoxLayout tablePanelLayout = new BoxLayout(tablePanel, javax.swing.BoxLayout.Y_AXIS);
				tablePanel.setLayout(tablePanelLayout);
				tablePanel.setPreferredSize(new java.awt.Dimension(261, 175));
				{
					miscTableScrollPane = new JScrollPane();
					tablePanel.add(miscTableScrollPane);
					{
						miscTableModel = new MiscTableModel(myRecipe);
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
						JComboBox miscComboBox = new JComboBox();
						miscComboModel = new ComboModel();
						miscComboBox.setModel(miscComboModel);
						TableColumn miscColumn = miscTable.getColumnModel().getColumn(0);
						miscColumn.setCellEditor(new DefaultCellEditor(miscComboBox));
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
						
						// set up stage combo
						String [] stage = {"Mash", "Boil", "Primary", "Secondary",
								"Bottle", "Keg"	};
						JComboBox stageComboBox = new JComboBox(stage);						
						miscColumn = miscTable.getColumnModel().getColumn(4);
						miscColumn.setCellEditor(new DefaultCellEditor(stageComboBox));
						
					}
				}
				{
					buttonPanel = new JPanel();
					FlowLayout buttonPanelLayout = new FlowLayout();
					buttonPanelLayout.setAlignment(FlowLayout.LEFT);
					buttonPanel.setLayout(buttonPanelLayout);
					tablePanel.add(buttonPanel);
					{
						addButton = new JButton();
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
						delButton = new JButton();
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
				commentsPanel = new JPanel();
				BoxLayout commentsPanelLayout = new BoxLayout(commentsPanel, javax.swing.BoxLayout.Y_AXIS);
				commentsPanel.setLayout(commentsPanelLayout);
				this.add(commentsPanel);
				commentsPanel.setBorder(BorderFactory.createTitledBorder(new LineBorder(new java.awt.Color(0,0,0), 1, false), "Comments", TitledBorder.LEADING, TitledBorder.TOP));
				commentsPanel.setPreferredSize(new java.awt.Dimension(147, 300));
				{
					jScrollPane1 = new JScrollPane();
					commentsPanel.add(jScrollPane1);
					{
						miscCommentsTextArea = new JTextArea();
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
