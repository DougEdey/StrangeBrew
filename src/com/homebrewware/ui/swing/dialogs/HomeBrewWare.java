/**
 *    Filename: HomeBrewWare.java
 *     Version: 0.9.0
 * Description: Home Brew Ware
 *     License: GPLv2
 *        Date: 2017-01-14
 *
 * Copyright (c) 2003-2004 Werner Randelshofer
 * Staldenmattweg 2, Immensee, CH-6405, Switzerland.
 * All rights reserved.
 * @author Werner Randelshofer
 *
 * This software is in the public domain.
 * Copyright (c) 2017 Dallas Fletchall
 * @author Dallas Fletchall
 */

/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as
 * published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package com.homebrewware.ui.swing.dialogs;

import java.awt.Font;
import java.awt.GraphicsEnvironment;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

import com.homebrewware.Debug;
import com.homebrewware.Options;
import com.homebrewware.ServerRunner;
import com.homebrewware.WebServer;


public class HomeBrewWare {
    /**
     * Shows the splash screen, launches the application and then disposes
     * the splash screen.
     * @param args the command line arguments
     */
	private static Options opts;


    public static void main(String[] args) {
    	opts = Options.getInstance();

    	// check if this is a mac/osx machine, and deal accordingly
    	String lcOSName = System.getProperty("os.name").toLowerCase();
    	Debug.print("OS is: " + lcOSName);
    	//Debug.flush();

    	// Manually add extras, until I can find out why the L&F manager doesn't find it.
        UIManager.installLookAndFeel("Synthetica", "de.javasoft.plaf.synthetica.SyntheticaStandardLookAndFeel");

    	if (lcOSName.indexOf("mac") > -1) {
    		System.setProperty("apple.laf.useScreenMenuBar","true");
    		System.setProperty("com.apple.mrj.application.apple.menu.about.name","StrangeBrew");
    	}

    	// Set the default exception handler, there's a lot of places where errors are not caught
    	if (args.length > 0){
			// Are we in debug mode?
			if (args[0].equals("-d")) {
				Debug.set(true);
			}
		}

    	//Not the cleanest, but it'll do as a backup until I can fix stuff.
    	Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
		    public void uncaughtException(Thread t, Throwable e) {
		        System.err.println("Caught Exception in thread" + t.getName() + " - " + e.getMessage());
		        e.printStackTrace();
		        System.exit(-1);

		    }
		});

    	// What fonts do we have
    	//String cFront = GraphicsEnvironment.getLocalGraphicsEnvironment().
    	String[] fList = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
    	/*Debug.print("Available fonts: ");
    	for(String f : fList) {
    		Debug.print(f);
    	}*/

    	Debug.print("Current font: "+ UIManager.getDefaults().getFont("TabbedPane.font"));

    	//Is someone overriding the Look And Feel?
    	if(System.getProperty("swing.defaultlaf")==null)
    	{
	    	// Lets try to get the look and feel running nicely.
	    	try {
	    		// check for a option first

	    		String appearance = opts.getProperty("optAppearance");

	    		if(appearance == null) {
	    			appearance = UIManager.getSystemLookAndFeelClassName();
	    		}

	    		Debug.print("Attempting to set the UI to: " + appearance);
	            // Set System L&F
			    UIManager.setLookAndFeel(appearance);
			}
			catch (UnsupportedLookAndFeelException e) {
			   // handle exception
			}
			catch (ClassNotFoundException e) {
			   // handle exception
			}
			catch (InstantiationException e) {
			   // handle exception
			}
			catch (IllegalAccessException e) {
			   // handle exception
			}
    	} else
    	{

    	}



    	System.out.println("Checking for webserver");
    	// Startup the WebServer if it's requested (this'll be set to on for now)
    	if (opts.getBProperty("optWebServer")) {

    		int port = opts.getIProperty("optWebServerPort");
    		if ( port == 0 ) {
    			port = 8080;
    		}

    		System.out.println("Starting the webserver on port " + port);

    		ServerRunner server = new ServerRunner(WebServer.class, port);
    		Thread serverThread = new Thread(server);
    		serverThread.start();

    	}
    	System.out.println("Server started");


        SplashWindow.splash(HomeBrewWare.class.getResource("splash.gif"));
        SplashWindow.invokeMain("StrangeSwing", args);
        SplashWindow.disposeSplash();
    }

}
