/*
 * $Id: FindDialog.java,v 1.5 2008/01/19 01:05:40 jimcdiver Exp $ 
 * Created on June 15, 2005 @author aavis find recipe window class
 */

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

package ca.strangebrew.ui.swing.dialogs;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;

import ca.strangebrew.Debug;
import ca.strangebrew.OpenImport;
import ca.strangebrew.Recipe;
import ca.strangebrew.SBStringUtils;
import ca.strangebrew.ui.swing.StrangeSwing;

public class FindDialog extends javax.swing.JDialog implements ActionListener {
	private JPanel findPanel;
	private JButton browseButton;
	private JTextField dirLocationText;
	private JPanel browsePanel;
	private JButton cancelButton;
	private JButton openButton;
	private JPanel buttonPanel;
	private JTable recipeTable;
	private JScrollPane recipeScrollPane;
	private FindTableModel recipeTableModel;

	private Recipe r;
	private List<Recipe> recipes = new ArrayList<Recipe>();
	private List<File> files = new ArrayList<File>();
	private File currentDir;

	public FindDialog(JFrame frame) {
		super(frame);
		try {
			currentDir = new File(SBStringUtils.getAppPath("recipes"));
		
			initGUI();
			dirLocationText.setText(currentDir.getAbsolutePath());
			loadRecipes(currentDir);
		} catch (UnsupportedEncodingException e) {
			JOptionPane.showMessageDialog(frame, "Couldn't get the App Path for the find recipe dialog.");
		}
	}

	private void initGUI() {
		try {
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.columnWeights = new double[]{0.1};
			thisLayout.columnWidths = new int[]{7};
			thisLayout.rowWeights = new double[]{0.7, 0.1, 0.1};
			thisLayout.rowHeights = new int[]{7, 7, 7};
			this.getContentPane().setLayout(thisLayout);
			{
				findPanel = new JPanel();
				this.getContentPane().add(
						findPanel,
						new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH,
								GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				BoxLayout findPanelLayout = new BoxLayout(findPanel, javax.swing.BoxLayout.X_AXIS);
				findPanel.setLayout(findPanelLayout);
				findPanel.setPreferredSize(new java.awt.Dimension(392, 269));
				{
					recipeScrollPane = new JScrollPane();
					findPanel.add(recipeScrollPane);
					// recipeScrollPane.setPreferredSize(new
					// java.awt.Dimension(152, 75));
					{
						recipeTableModel = new FindTableModel();
						recipeTable = new JTable();
						recipeScrollPane.setViewportView(recipeTable);
						recipeTable.setModel(recipeTableModel);
						// recipeTable.setPreferredSize(new
						// java.awt.Dimension(148, 32));
					}
				}
			}
			{
				browsePanel = new JPanel();
				this.getContentPane().add(
						browsePanel,
						new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
								GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				BorderLayout browsePanelLayout = new BorderLayout();
				browsePanel.setLayout(browsePanelLayout);
				browsePanel.setBorder(BorderFactory.createTitledBorder("Directory"));
				browsePanel.setPreferredSize(new java.awt.Dimension(392, 244));
				{
					dirLocationText = new JTextField();
					browsePanel.add(dirLocationText, BorderLayout.CENTER);
					dirLocationText.setText("jTextField1");
				}
				{
					browseButton = new JButton();
					browsePanel.add(browseButton, BorderLayout.EAST);
					browseButton.setText("Browse...");
					browseButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							Component owner = browseButton;
							JFileChooser chooser = new JFileChooser();
							chooser.setCurrentDirectory(new java.io.File("."));
							chooser.setDialogTitle("Select directory");
							chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
							if (chooser.showOpenDialog(owner) == JFileChooser.APPROVE_OPTION) {
								Debug.print("getCurrentDirectory(): "
										+ chooser.getCurrentDirectory());
								Debug.print("getSelectedFile() : " + chooser.getSelectedFile());
								currentDir = chooser.getSelectedFile();
								dirLocationText.setText(currentDir.getAbsolutePath());
								loadRecipes(currentDir);
							} else {
								Debug.print("No Selection ");
							}
						}
					});
				}
			}
			{
				buttonPanel = new JPanel();
				this.getContentPane().add(
						buttonPanel,
						new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
								GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				FlowLayout buttonPanelLayout = new FlowLayout();
				buttonPanelLayout.setAlignment(FlowLayout.RIGHT);
				buttonPanel.setLayout(buttonPanelLayout);
				{
					openButton = new JButton();
					buttonPanel.add(openButton);
					openButton.setText("Open");
					openButton.addActionListener(this);
				}
				{
					cancelButton = new JButton();
					buttonPanel.add(cancelButton);
					cancelButton.setText("Cancel");
					cancelButton.addActionListener(this);
				}
			}
			this.setSize(400, 428);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void loadRecipes(File dir) {

		recipes.clear();
		files.clear();

		for (int i = 0; i < dir.list().length; i++) {
			File file = new File(dir.list()[i]);

			if (file.getPath().endsWith(".rec") || file.getPath().endsWith(".qbrew")
					|| file.getPath().endsWith(".xml")) {

				Debug.print("Opening: " + file.getName() + ".\n");
				// file.getAbsolutePath doesn't work here for some reason,
				// so we have to build it ourselves
				String fileName = dir.getAbsolutePath() + System.getProperty("file.separator")
						+ file.getName();
				file = new File(fileName);
				OpenImport openImport = new OpenImport();
				r = openImport.openFile(file);
				if (openImport.getFileType().equals("sb")
						|| openImport.getFileType().equals("qbrew")
						|| openImport.getFileType().equals("promash")) {
					recipes.add(r);
					files.add(file);

				} else if (openImport.getFileType().equals("beerxml")) {
					List<Recipe> rs = openImport.getRecipes();
					for (int j=0; j<rs.size(); j++){
						recipes.add(rs.get(j));
						files.add(file);
					}
				}
			}
		}
		recipeTableModel.setData(recipes);
		recipeTable.updateUI();

	}

	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if (o == cancelButton) {
			setVisible(false);
			dispose();
			return;
		} else if (o == openButton) {
			int i = recipeTable.getSelectedRow();
			if (i > -1 && i < recipes.size()) {
				StrangeSwing.getInstance().setRecipe(recipes.get(i), files.get(i));
			}

			setVisible(false);
			dispose();
			return;
		}
	}

	class FindTableModel extends AbstractTableModel {

		private String[] columnNames = {"Recipe", "Style", "Brewer", "Date"};

		private List<Recipe> data;

		public FindTableModel() {
			// data = new ArrayList();
		}

		public void setData(List<Recipe> l) {
			data = l;
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

		public Object getValueAt(int row, int col) {

			try {
				Recipe r = data.get(row);
				switch (col) {
					case 0 :
						return r.getName();
					case 1 :
						return r.getStyle();
					case 2 :
						return r.getBrewer();
					case 3 :
						return "";

					default :
						return "";

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

		// public Class getColumnClass(int c) {
		// return getValueAt(0, c).getClass();
		// }
	}

}
