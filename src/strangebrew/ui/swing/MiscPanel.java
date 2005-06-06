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

package strangebrew.ui.swing;

import java.awt.Dimension;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.table.TableColumn;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.util.ArrayList;

import strangebrew.Misc;
import strangebrew.Recipe;
import strangebrew.Database;

public class MiscPanel extends javax.swing.JPanel {
	private JTable miscTable;
	private JScrollPane jScrollPane1;
	private JPanel buttonPanel;
	private JScrollPane miscTableScrollPane;
	private JButton delButton;
	private JButton addButton;
	private JPanel tablePanel;
	private JTextArea miscDetailsTextArea;
	
	private Recipe myRecipe;
	private MiscTableModel miscTableModel;
	private ComboModel miscComboModel;

	/**
	* Auto-generated main method to display this 
	* JPanel inside a new JFrame.
	*/
//	public static void main(String[] args) {
//		JFrame frame = new JFrame();
//		frame.getContentPane().add(new MiscPanel(new Recipe()));
//		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
//		frame.pack();
//		frame.show();
//	}
	
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
			BoxLayout thisLayout = new BoxLayout(this, javax.swing.BoxLayout.Y_AXIS);
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
						miscTable = new JTable();
						miscTableScrollPane.setViewportView(miscTable);
						miscTable.setModel(miscTableModel);
						
						miscTable.addMouseListener(new MouseAdapter() {
							public void mouseClicked(MouseEvent evt) {
								int i = miscTable.getSelectedRow();
								miscDetailsTextArea.setText(myRecipe.getMiscDescription(i));
							}
						});
						
						// set up combo
						JComboBox miscComboBox = new JComboBox();
						miscComboModel = new ComboModel();
						miscComboBox.setModel(miscComboModel);
						TableColumn miscColumn = miscTable.getColumnModel().getColumn(0);
						miscColumn.setCellEditor(new DefaultCellEditor(miscComboBox));
						
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
				jScrollPane1 = new JScrollPane();
				this.add(jScrollPane1);
				{
					miscDetailsTextArea = new JTextArea();
					jScrollPane1.setViewportView(miscDetailsTextArea);
					miscDetailsTextArea.setText("jTextArea1");
					miscDetailsTextArea.setLineWrap(true);
					miscDetailsTextArea.setWrapStyleWord(true);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
