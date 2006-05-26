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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;

import ca.strangebrew.Note;
import ca.strangebrew.Recipe;
import ca.strangebrew.SBStringUtils;

import com.michaelbaranov.microba.calendar.DatePicker;

public class NotesPanel extends javax.swing.JPanel {
	private JTable notesTable;
	private JPanel tablePanel;
	private JPanel notePanel;
	private JTextArea noteTextArea;
	private JScrollPane noteScroll;
	private JScrollPane notesTableScroll;
	private JButton delNoteButton;
	private JButton addNoteButton;
	private JPanel buttonsPanel;
	private Recipe myRecipe;
	private NotesTableModel notesTableModel;
	private int selectedRow;

	public NotesPanel() {
		super();
		initGUI();
	}

	public void setData(Recipe r) {
		myRecipe = r;
		notesTableModel.setData(myRecipe);
	}

	private void initGUI() {
		try {
			BoxLayout thisLayout = new BoxLayout(this, javax.swing.BoxLayout.X_AXIS);
			this.setLayout(thisLayout);
			setPreferredSize(new Dimension(400, 300));
			{
				tablePanel = new JPanel();
				BoxLayout tablePanelLayout = new BoxLayout(tablePanel, javax.swing.BoxLayout.Y_AXIS);
				tablePanel.setLayout(tablePanelLayout);
				this.add(tablePanel);
				{
					notesTableScroll = new JScrollPane();
					tablePanel.add(notesTableScroll);
					notesTableScroll.setPreferredSize(new java.awt.Dimension(235, 264));
					{
						notesTableModel = new NotesTableModel(myRecipe);
						notesTable = new JTable();
						notesTableScroll.setViewportView(notesTable);
						notesTable.setModel(notesTableModel);

						// set up type combo
						String[] types = {"Planning", "Brewed", "Fermentation", "Racked", "Conditioned", "Kegged",
								"Bottled", "Tasting", "Contest"};
						JComboBox typeComboBox = new JComboBox(types);
						TableColumn noteColumn = notesTable.getColumnModel().getColumn(1);
						noteColumn.setCellEditor(new DefaultCellEditor(typeComboBox));				

						// set up the date picker
						DateEditor dateEditor = new DateEditor();						
						noteColumn = notesTable.getColumnModel().getColumn(0);
						noteColumn.setCellEditor(dateEditor);
						
						// listen for row selections:
						ListSelectionModel rowSM = notesTable.getSelectionModel();						
						notesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
						
						// Ask to be notified of selection changes.
						
						rowSM.addListSelectionListener(new ListSelectionListener() {
						    public void valueChanged(ListSelectionEvent e) {
						        //Ignore extra messages.
						        if (e.getValueIsAdjusting()) return;
						        ListSelectionModel lsm =
						            (ListSelectionModel)e.getSource();
						        if (lsm.isSelectionEmpty()) {
						            // ...//no rows are selected
						        } else {
						            int selectedRow = lsm.getMinSelectionIndex();
						            noteTextArea.setText(myRecipe.getNoteNote(selectedRow));
						        }
						    }
						});

					}
				}
				{
					buttonsPanel = new JPanel();
					tablePanel.add(buttonsPanel);
					FlowLayout buttonsPanelLayout = new FlowLayout();
					buttonsPanelLayout.setAlignment(FlowLayout.LEFT);
					buttonsPanel.setLayout(buttonsPanelLayout);
					{
						addNoteButton = new JButton();
						buttonsPanel.add(addNoteButton);
						addNoteButton.setText("+");
						addNoteButton.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								if (myRecipe != null) {
									Note n = new Note();
									myRecipe.addNote(n);
									notesTable.updateUI();
								}

							}
						});
					}
					{
						delNoteButton = new JButton();
						buttonsPanel.add(delNoteButton);
						delNoteButton.setText("-");
						delNoteButton.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								if (myRecipe != null) {
									int i = notesTable.getSelectedRow();
									myRecipe.delNote(i);
									notesTable.updateUI();
								}

							}
						});
					}
				}
			}
			{
				notePanel = new JPanel();
				BoxLayout notePanelLayout = new BoxLayout(notePanel, javax.swing.BoxLayout.Y_AXIS);
				notePanel.setLayout(notePanelLayout);
				this.add(notePanel);
				notePanel.setBorder(BorderFactory.createTitledBorder("Note:"));
				{
					noteScroll = new JScrollPane();
					notePanel.add(noteScroll);
					{
						noteTextArea = new JTextArea();
						noteScroll.setViewportView(noteTextArea);
						noteTextArea.setWrapStyleWord(true);
						noteTextArea.setLineWrap(true);
						noteTextArea.addFocusListener(new FocusAdapter() {
							public void focusLost(FocusEvent evt) {
								selectedRow = notesTable.getSelectedRow();
								if (selectedRow > -1) {
									myRecipe.setNoteNote(selectedRow, noteTextArea.getText());
								}
							}
						});	

					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	class NotesTableModel extends AbstractTableModel {

		private String[] columnNames = {"Date", "Type"};

		private Recipe data = null;

		public NotesTableModel(Recipe r) {
			data = r;
		}

		public void addRow() {
			Note n = new Note();
			data.addNote(n);
		}

		public void setData(Recipe d) {
			data = d;
		}

		public int getColumnCount() {
			return columnNames.length;
		}

		public int getRowCount() {
			if (data != null)
				return data.getNotesListSize();
			else
				return 0;
		}

		public String getColumnName(int col) {
			return columnNames[col];
		}

		public Object getValueAt(int row, int col) {		
			
			try {
				switch (col) {
					case 0 :
						return SBStringUtils.dateFormatShort.format(data.getNoteDate(row));
					case 1 :						
						return data.getNoteType(row);

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
			if (col < 0) {
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

			try {
				switch (col) {
					case 0 :
						try {							
							Date d = SBStringUtils.dateFormatShort.parse(value.toString());
							data.setNoteDate(row, d);
						} catch (ParseException e) {
							System.out.println("Unable to parse " + value.toString());
						}
						break;
					case 1 :
						data.setNoteType(row, value.toString());
						break;

				}
			} catch (Exception e) {
			};

			fireTableCellUpdated(row, col);
			fireTableDataChanged();

		}
	}

	public class DateEditor extends AbstractCellEditor implements TableCellEditor {
		DatePicker datePicker;
//		JButton button;
//		JColorChooser colorChooser;
//		JDialog dialog;
		protected static final String EDIT = "edit";

		public DateEditor() {
			datePicker = new DatePicker();
			datePicker.setDateStyle(DateFormat.SHORT);
		}

		
		//Implement the one CellEditor method that AbstractCellEditor doesn't.
		public Object getCellEditorValue() {
			return  SBStringUtils.dateFormatShort.format(datePicker.getDate());
		}

		//Implement the one method defined by TableCellEditor.
		public Component getTableCellEditorComponent(JTable table, Object value,
				boolean isSelected, int row, int column) {			
			try {				
				datePicker.setDate(SBStringUtils.dateFormatShort.parse(value.toString()));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return datePicker;
		}
	}

}
