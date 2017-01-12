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
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import ca.strangebrew.SBStringUtils;
import ca.strangebrew.SBVersion;


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
	private JButton updateButton;

	
	private String version;

	
	public AboutDialog(JFrame frame, String v) {
		super(frame);
		version = v;
		initGUI();
	}
	
	private void initGUI() {
		try {
			String iniPath = SBStringUtils.getAppPath("ini");
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
												
						java.net.URL imgURL = StrangeBrew.class.getResource("splash.gif");						
						JLabel splashLabel = new JLabel(new ImageIcon(imgURL));						
						splashPanel.add(splashLabel);
						
					}
					{
						versionLabel = new JLabel();
						aboutPanel.add(versionLabel);
						
						versionLabel.setText("<html>Version: " + SBVersion.VERSION + "<br />" +
								"Build Date: " + SBVersion.BUILDDATE + "<br />" +
								"Build: " + SBVersion.BUILDNUMBER + "</html>");
								
							
					}
					{
						emailLabel = new JLabel();
						aboutPanel.add(emailLabel);
						emailLabel.setText("Contact: doug.edey@gmail.com");
					}
					{
						copyrightLabel = new JLabel();
						aboutPanel.add(copyrightLabel);
						copyrightLabel.setText("(c) 2005-2006 Drew Avis");
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

                        /* Set contents for this tab */
                        String content = "";
						readmeTextArea = new JTextArea();
						jScrollPane1.setViewportView(readmeTextArea);
						try {
						    content = fileToString(iniPath + "README.md");
						} catch (FileNotFoundException e) {
                            content = iniPath + "README.md not found";
						}
						readmeTextArea.setWrapStyleWord(true);
						readmeTextArea.setLineWrap(true);
                        readmeTextArea.setText(content);

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

                        /* Set contents for this tab */
                        String content = iniPath + "gpl.txt";
						licenseTextArea = new JTextArea();
						jScrollPane2.setViewportView(licenseTextArea);
                        try {
                            content = fileToString(content);
                        } catch (FileNotFoundException e) {
                            content += " not found";
                        }
					    licenseTextArea.setLineWrap(true);
					    licenseTextArea.setText(content);

					}
				}
			}
			{
				buttonPanel = new JPanel();
				FlowLayout jPanel1Layout = new FlowLayout();
				jPanel1Layout.setAlignment(FlowLayout.RIGHT);
				buttonPanel.setLayout(jPanel1Layout);
				this.getContentPane().add(buttonPanel);
				buttonPanel.add(getUpdateButton());
				buttonPanel.add(getOkButton());				
				
			}
			this.setSize(518, 372);
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
	
	private JButton getUpdateButton () {
		if (updateButton == null){
			updateButton = new JButton();
			buttonPanel.add(updateButton);
			updateButton.setText("Check for Updates");
			updateButton.addActionListener(this);
		
		}
		
		return updateButton;
	}
	
	private void checkVersion() {
		// check the latest URL on github
		URL Build = null;
		try {
			Build = new URL("https://raw.github.com/DougEdey/StrangeBrew/master/src/ca/strangebrew/SBVersion.java");
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return;
		}
		
        BufferedReader in;
		try {
			in = new BufferedReader(
			new InputStreamReader(Build.openStream()));
	        String inputLine;
	        while ((inputLine = in.readLine()) != null)
	            if(inputLine.contains("BUILDNUMBER")) {
	            	//got the build ID line
	            	String [] splitLine = inputLine.split(" ");
	            	String vTemp = splitLine[splitLine.length-1];
	            	vTemp = vTemp.substring(1, vTemp.length()-2);
	            	int newBuildID = Integer.parseInt(vTemp);
	            	
	            	if( newBuildID != Integer.parseInt(SBVersion.BUILDNUMBER)) {
	            		// newest Build ID means there's a new download avalable!
	            		Object[] options = {"Yes, please",
	                            "No, thanks"};
	            		
	            		int n = JOptionPane.showOptionDialog(this.getContentPane(),
	            			    "New download available (Build ID " + newBuildID + ")"
	            			    + ". Would you like to download it?",
	            				
	            			    "New Version Available!",
	            			    JOptionPane.YES_NO_OPTION,
	            			    JOptionPane.QUESTION_MESSAGE,
	            			    null,
	            			    options,
	            			    options[0]);
	            		
	            		if (n == 0) {
	            			// User Selected Yes to download
	            			Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
	            		    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
	            		        try {
	            		        	URL DLURL = new URL("https://github.com/DougEdey/StrangeBrew/blob/master/StrangeBrew-2.1.0-b"+newBuildID+".zip?raw=true");
	            		        	
	            		            desktop.browse(DLURL.toURI());
	            		        } catch (Exception e) {
	            		            e.printStackTrace();
	            		        }
	            		    }
	            		}
	            	} else {
	            		JOptionPane.showMessageDialog(this.getContentPane(),
	            			    "No updates available!");
	            	}
	            }
	        in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return;
	}
	
//	Make the button do the same thing as the default close operation
	//(DISPOSE_ON_CLOSE).
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == updateButton) {
			checkVersion();
		} else {
			setVisible(false);
			dispose();
		}
	}

    private String fileToString(String abspath) throws FileNotFoundException, IOException {
        BufferedReader in = new BufferedReader(new FileReader(new File(abspath)));
        StringBuilder out = new StringBuilder();
	    String c;
	    while ((c = in.readLine()) != null){
	        out.append(c + "\n");
	    }
        in.close();
        return out.toString();
    }
}
