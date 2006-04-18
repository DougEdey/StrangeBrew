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

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import ca.strangebrew.Debug;


public class AboutDialog extends javax.swing.JDialog implements ActionListener {
	private JTabbedPane aboutTabPanel;
	private JPanel buttonPanel;
	private JPanel licensePanel;
	private JTextArea licenseTextArea;
	private JScrollPane jScrollPane2;
	private JTextArea readmeTextArea;
	private JScrollPane jScrollPane1;
	private JLabel copyrightLabel;
	private JLabel emailLabel;
	private JLabel versionLabel;
	private JPanel splashPanel;
	private JPanel readmePanel;
	private JPanel aboutPanel;
	private JButton okButton;
	private Image image;
	
	private String version;

	
	public AboutDialog(JFrame frame, String v) {
		super(frame);
		version = v;
		initGUI();
	}
	
	private void initGUI() {
		try {
			BoxLayout thisLayout = new BoxLayout(this.getContentPane(), javax.swing.BoxLayout.Y_AXIS);
			this.getContentPane().setLayout(thisLayout);
			{
				aboutTabPanel = new JTabbedPane();
				this.getContentPane().add(aboutTabPanel);				
				{
					aboutPanel = new JPanel();
					BoxLayout aboutPanelLayout = new BoxLayout(aboutPanel, javax.swing.BoxLayout.Y_AXIS);
					aboutPanel.setLayout(aboutPanelLayout);
					aboutTabPanel.addTab("About", null, aboutPanel, null);
					{
						splashPanel = new JPanel();
						FlowLayout splashPanelLayout = new FlowLayout();
						splashPanel.setLayout(splashPanelLayout);
						aboutPanel.add(splashPanel);
												
						java.net.URL imgURL = Splash.class.getResource("splash.gif");						
						JLabel splashLabel = new JLabel(new ImageIcon(imgURL));						
						splashPanel.add(splashLabel);
						
					}
					{
						versionLabel = new JLabel();
						aboutPanel.add(versionLabel);
						versionLabel.setText("Version: " + version);
					}
					{
						emailLabel = new JLabel();
						aboutPanel.add(emailLabel);
						emailLabel.setText("Contact: drew.avis@gmail.com");
					}
					{
						copyrightLabel = new JLabel();
						aboutPanel.add(copyrightLabel);
						copyrightLabel.setText("(c) 2005 Drew Avis");
					}
				}
				{
					readmePanel = new JPanel();
					BorderLayout readmePanelLayout = new BorderLayout();
					readmePanel.setLayout(readmePanelLayout);
					aboutTabPanel.addTab("Readme", null, readmePanel, null);
					{
						jScrollPane1 = new JScrollPane();
						jScrollPane1.setAutoscrolls(true);
						readmePanel.add(jScrollPane1, BorderLayout.CENTER);						
						jScrollPane1.setPreferredSize(new java.awt.Dimension(3, 19));
						{
							readmeTextArea = new JTextArea();
							jScrollPane1.setViewportView(readmeTextArea);
							readmeTextArea.setText("jTextArea1");							
							readmeTextArea.setWrapStyleWord(true);
							
							File readme = new File("readme");
							FileReader in = new FileReader(readme);
							BufferedReader inb = new BufferedReader(in);
							String c;
							String s="";
							// TODO: what if there's no readme file?
					        while ((c = inb.readLine()) != null){
					        	s = s + c + "\n";
					        }					           
					        readmeTextArea.setText(s);
					        readmeTextArea.setLineWrap(true);

					        in.close();
						}
					}
				}
				{
					licensePanel = new JPanel();
					BorderLayout licensePanelLayout = new BorderLayout();
					licensePanel.setLayout(licensePanelLayout);
					aboutTabPanel.addTab("License", null, licensePanel, null);
					{
						jScrollPane2 = new JScrollPane();
						licensePanel.add(jScrollPane2, BorderLayout.CENTER);
												{
							licenseTextArea = new JTextArea();
							jScrollPane2.setViewportView(licenseTextArea);
							licenseTextArea.setText("jTextArea1");
							File readme = new File("gpl.txt");
							FileReader in = new FileReader(readme);
							BufferedReader inb = new BufferedReader(in);
							String c;
							String s="";
							// TODO: what if there's no readme file?
					        while ((c = inb.readLine()) != null){
					        	s = s + c + "\n";
					        }
					        licenseTextArea.setText(s);
					        licenseTextArea.setLineWrap(true);
						}
					}
				}
			}
			{
				buttonPanel = new JPanel();
				FlowLayout jPanel1Layout = new FlowLayout();
				jPanel1Layout.setAlignment(FlowLayout.RIGHT);
				buttonPanel.setLayout(jPanel1Layout);
				this.getContentPane().add(buttonPanel);				
				buttonPanel.add(getOkButton());				
			}
			setSize(500, 350);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private JButton getOkButton () {
		if (okButton == null){
		okButton = new JButton();
		buttonPanel.add(okButton);
		okButton.setText("OK");
		okButton.addActionListener(this);
		
		}
		return okButton;
	}
	
//	Make the button do the same thing as the default close operation
	//(DISPOSE_ON_CLOSE).
	public void actionPerformed(ActionEvent e) {
		setVisible(false);
		dispose();
	}

}
