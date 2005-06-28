/**
StrangeBrew Java - a homebrew recipe calculator
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
package strangebrew.ui.preferences;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

/**
 * @author zymurgist
 */
public class CostPanel extends JPanel
{
	private JTextField m_cost = null;

	public CostPanel()
	{
		JPanel border = new JPanel();
		border.setBorder(new TitledBorder("Cost Defaults"));
		
		JPanel fields = layoutFields();
		JPanel units = layoutUnits();

		border.setLayout(new BorderLayout(5, 5));
		border.add(BorderLayout.WEST, layoutLabels());
		border.add(BorderLayout.CENTER, layoutFields());
				
		setLayout(new BorderLayout());
		add(BorderLayout.NORTH, border);
	}

	private JPanel layoutUnits() 
	{
		JPanel layout = new JPanel();
		layout.setLayout(new BorderLayout());
		
		JComboBox unitOptions = new JComboBox();
		unitOptions.addItem("barrel IMP");
		unitOptions.addItem("barrel US");
		unitOptions.addItem("fl. ounces");
		unitOptions.addItem("gallons IMP");
		unitOptions.addItem("gallons US");
		unitOptions.addItem("litres");
		unitOptions.addItem("millilitres");
		unitOptions.addItem("pint US");
		unitOptions.addItem("quart US");
		
		JPanel units = new JPanel();
		units.setLayout(new GridLayout(2, 1));
		units.add(new JPanel());
		units.add(unitOptions);
		
		layout.add(BorderLayout.WEST, units);
		return layout;
	}

	private JPanel layoutFields() 
	{
		JPanel fieldsPanel = new JPanel();
		fieldsPanel.setLayout(new BorderLayout());
		
		m_cost = new JTextField(10);
		JPanel fields = new JPanel();
		fields.setLayout(new GridLayout(1, 1));
		fields.add(m_cost);
		//fields.add(size);
		
		fieldsPanel.add(BorderLayout.WEST, fields);
		//fieldsAndUnits.add(BorderLayout.CENTER, layoutUnits());
		return fieldsPanel;
	}

	private JPanel layoutLabels() 
	{
		JLabel costLabel = new JLabel("Other Cost:");
		//JLabel sizeLabel = new JLabel("Bottle Size:");
		
		JPanel labels = new JPanel();
		labels.setLayout(new GridLayout(1, 1));
		labels.add(costLabel);
		//labels.add(sizeLabel);
		return labels;
	}
	
	public String getOtherCost()
	{
		return m_cost.getText();
	}
	
	public void setOtherCost(String cost)
	{
		if (null == cost)
			m_cost.setText("");
		else
			m_cost.setText(cost);
	}	
}
