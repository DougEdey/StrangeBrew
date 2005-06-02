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
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

/**
 * @author zymurgist
 */
public class TimePanel extends JPanel
{
	private JTextField m_dayStart = null;
	private JSpinner m_prep = null;
	private JSpinner m_sparge = null;
	private JSpinner m_timeToBoil = null;
	private JSpinner m_boilTime = null;
	private JSpinner m_chill = null;
	private JSpinner m_cleanup = null;
	
	public TimePanel()
	{
		setLayout(new BorderLayout());
		
		m_dayStart = new JTextField(10);
		m_prep = new JSpinner(new SpinnerNumberModel(0, 0, 480, 1));
		m_sparge = new JSpinner(new SpinnerNumberModel(0, 0, 480, 1));
		m_timeToBoil = new JSpinner(new SpinnerNumberModel(0, 0, 480, 1));
		m_boilTime = new JSpinner(new SpinnerNumberModel(0, 0, 480, 1));
		m_chill = new JSpinner(new SpinnerNumberModel(0, 0, 480, 1));
		m_cleanup = new JSpinner(new SpinnerNumberModel(0, 0, 480, 1));
		
		JPanel components = new JPanel();
		components.setLayout(new GridLayout(7, 2, 5, 5));

		components.add(layoutLabel(new JLabel("Brewday starts at:")));
		components.add(layoutField(m_dayStart));
		
		components.add(layoutLabel(new JLabel("Preparation:")));
		components.add(layoutField(m_prep));
		
		components.add(layoutLabel(new JLabel("Sparge:")));
		components.add(layoutField(m_sparge));
		
		components.add(layoutLabel(new JLabel("Time to boil:")));
		components.add(layoutField(m_timeToBoil));
		
		components.add(layoutLabel(new JLabel("Boil time:")));
		components.add(layoutField(m_boilTime));
		
		components.add(layoutLabel(new JLabel("Chill and collect:")));
		components.add(layoutField(m_chill));
		
		components.add(layoutLabel(new JLabel("Cleanup:")));
		components.add(layoutField(m_cleanup));
				
		add(BorderLayout.NORTH, components);
	}	
	
	JPanel layoutLabel(JLabel label)
	{
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
		panel.add(label);
		return panel;
	}

	JPanel layoutField(JComponent label)
	{
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		panel.add(label);
		return panel;
	}
	
	
	public void setDayStart(String value)
	{
		m_dayStart.setText(value);
	}
	
	public String getDayStart()
	{
		return m_dayStart.getText();
	}
	
	
	public void setPrep(String value)
	{
		m_prep.setValue(new Integer(value));
	}
	
	public String getPrep()
	{
		return m_prep.getValue().toString();
	}
	
	
	public void setSparge(String value)
	{
		m_sparge.setValue(new Integer(value));
	}
	
	public String getSparge()
	{
		return m_sparge.getValue().toString();
	}

	
	public void setTimeToBoil(String value)
	{
		m_timeToBoil.setValue(new Integer(value));
	}
	
	public String getTimeToBoil()
	{
		return m_timeToBoil.getValue().toString();
	}

	public void setBoilTime(String value)
	{
		m_boilTime.setValue(new Integer(value));
	}
	
	public String getBoilTime()
	{
		return m_boilTime.getValue().toString();
	}


	public void setChill(String value)
	{
		m_chill.setValue(new Integer(value));
	}
	
	public String getChill()
	{
		return m_chill.getValue().toString();
	}

	
	public void setCleanup(String value)
	{
		m_cleanup.setValue(new Integer(value));
	}
	
	public String getCleanup()
	{
		return m_cleanup.getValue().toString();
	}
	
}
