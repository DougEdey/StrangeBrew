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

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * @author zymurgist
 */
public class BrewerPanel extends JPanel
{
	private JTextField m_name = null;
	private JTextField m_phoneNumber = null;
	private JTextField m_clubName = null;
	private JTextField m_email = null;
	private JTextField m_street = null;
	private JTextField m_city = null;
	private JTextField m_state = null;
	private JTextField m_postalCode = null;
	private JTextField m_country = null;
	
	public BrewerPanel()
	{	
		JPanel components = new JPanel();
		components.setLayout(new BorderLayout(5, 5));
		components.add(BorderLayout.WEST, layoutLabels());
		components.add(BorderLayout.CENTER, layoutFields());

		setLayout(new BorderLayout());
		add(BorderLayout.NORTH, components);
	}
	
	private JPanel layoutLabels()
	{
		JPanel labels = new JPanel();
		labels.setLayout(new GridLayout(9, 1, 5, 5));	
		labels.add(new JLabel("Club Name:"));
		labels.add(new JLabel("Brewer Name:"));		
		labels.add(new JLabel("Phone Number:"));				
		labels.add(new JLabel("Email:"));		
		labels.add(new JLabel("Street:"));		
		labels.add(new JLabel("City:"));		
		labels.add(new JLabel("State/Province:"));				
		labels.add(new JLabel("Postal Code:"));			
		labels.add(new JLabel("Country:"));
		
		return labels;
	}
	
	private JPanel layoutFields()
	{
		m_name = new JTextField(20);
		m_phoneNumber = new JTextField(20);
		m_clubName = new JTextField(20);
		m_email = new JTextField(20);
		m_street = new JTextField(20);
		m_city = new JTextField(20);
		m_state = new JTextField(20);
		m_postalCode = new JTextField(20);
		m_country = new JTextField(20);

		JPanel fields = new JPanel();
		fields.setLayout(new GridLayout(9, 1, 5, 5));
		
		fields.add(m_clubName);
		fields.add(m_name);
		fields.add(m_phoneNumber);
		fields.add(m_email);
		fields.add(m_street);
		fields.add(m_city);
		fields.add(m_state);
		fields.add(m_postalCode);
		fields.add(m_country);
		
		return fields;
	}
	
	public String getName()
	{
		return m_name.getText();
	}
	
	public void setName(String name)
	{
		m_name.setText(name);
	}

	public String getPhoneNumber()
	{
		return m_phoneNumber.getText();
	}
	
	public void setPhoneNumber(String number)
	{
		m_phoneNumber.setText(number);
	}
	
	public String getClubName()
	{
		return m_clubName.getText();
	}
	
	public void setClubName(String name)
	{
		m_clubName.setText(name);
	}
		
	public String getEmail()
	{
		return m_email.getText();
	}
	
	public void setEmail(String email)
	{
		m_email.setText(email);
	}
	
	public String getStreet()
	{
		return m_street.getText();
	}
	
	public void setStreet(String street)
	{
		m_street.setText(street);
	}
	
	public String getCity()
	{
		return m_city.getText();
	}
	
	public void setCity(String city)
	{
		m_city.setText(city);
	}	
	
	public String getState()
	{
		return m_state.getText();
	}
	
	public void setState(String state)
	{
		m_state.setText(state);
	}
	
	public String getPostalCode()
	{
		return m_postalCode.getText();
	}
	
	public void setPostalCode(String postalCode)
	{
		m_postalCode.setText(postalCode);
	}
	
	public String getCountry()
	{
		return m_country.getText();
	}
	
	public void setCountry(String country)
	{
		m_country.setText(country);
	}	
}
