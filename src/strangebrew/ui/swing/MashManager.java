/*
 * Created on May 25, 2005
 * $Id: MashManager.java,v 1.2 2005/05/26 18:15:21 andrew_avis Exp $
 *  @author aavis 
 */

/**
 *  StrangeBrew Java - a homebrew recipe calculator
    Copyright (C) 2005 Drew Avis

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

import java.awt.GridBagLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTable;
import java.awt.Insets;
import java.awt.GridBagConstraints;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import strangebrew.Recipe;

public class MashManager extends javax.swing.JFrame implements ActionListener{
	private JScrollPane jScrollPane1;
	private JTable tblMash;
	private JPanel pnlButtons;
	private JButton btnOk;
	private JPanel pnlTable;
	
	private Recipe myRecipe;
	private MashTableModel mashModel;

	/**
	* Auto-generated main method to display this JFrame
	*/
/*	public static void main(String[] args) {
		MashManager inst = new MashManager();
		inst.setVisible(true);
	}*/
	
	public MashManager(Recipe r) {
		super();
		initGUI();
		myRecipe = r;
		if (myRecipe != null){
    		mashModel.setData(myRecipe.getMash());   		
    		
    	}
	}
	
	private void initGUI() {
		try {
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.columnWeights = new double[] {0.1,0.1};
			thisLayout.columnWidths = new int[] {7,7};
			thisLayout.rowWeights = new double[] {0.1,0.1};
			thisLayout.rowHeights = new int[] {7,7};
			this.getContentPane().setLayout(thisLayout);
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			this.setTitle("Mash Manager");
			{
				pnlTable = new JPanel();
				BoxLayout pnlTableLayout = new BoxLayout(pnlTable, javax.swing.BoxLayout.X_AXIS);
				pnlTable.setLayout(pnlTableLayout);
				this.getContentPane().add(
					pnlTable,
					new GridBagConstraints(
						0,
						0,
						2,
						1,
						0.0,
						0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.BOTH,
						new Insets(0, 0, 0, 0),
						0,
						0));
				pnlTable.setName("");
				pnlTable.setBorder(BorderFactory.createTitledBorder("Mash Steps"));
				{
					jScrollPane1 = new JScrollPane();
					pnlTable.add(jScrollPane1);
					{
						mashModel = new MashTableModel();
						tblMash = new JTable();
						jScrollPane1.setViewportView(tblMash);
						tblMash.setModel(mashModel);
					}
				}
			}
			{
				pnlButtons = new JPanel();
				FlowLayout pnlButtonsLayout = new FlowLayout();
				pnlButtonsLayout.setAlignment(FlowLayout.RIGHT);
				pnlButtons.setLayout(pnlButtonsLayout);
				this.getContentPane().add(
					pnlButtons,
					new GridBagConstraints(
						0,
						1,
						2,
						1,
						0.0,
						0.0,
						GridBagConstraints.SOUTH,
						GridBagConstraints.HORIZONTAL,
						new Insets(0, 0, 0, 0),
						0,
						0));
				{
					btnOk = new JButton();
					pnlButtons.add(btnOk);
					btnOk.setText("OK");
					btnOk.addActionListener(this);
				}
			}
			pack();
			setSize(400, 300);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	//	Make the button do the same thing as the default close operation
    //(DISPOSE_ON_CLOSE).
    public void actionPerformed(ActionEvent e) {
        setVisible(false);
        dispose();
    }
    
    public void displayMash() {
    	if (myRecipe != null){
    		mashModel.setData(myRecipe.getMash());   		
    		
    	}
    }

}
