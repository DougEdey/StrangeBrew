/*
 * @(#)Splash.java  2.0  January 31, 2004
 *
 * Copyright (c) 2003-2004 Werner Randelshofer
 * Staldenmattweg 2, Immensee, CH-6405, Switzerland.
 * All rights reserved.
 *
 * This software is in the public domain.
 */

/**
 *
 * @author  werni
 */
package ca.strangebrew.ui.swing.dialogs;

import java.awt.Font;
import java.awt.GraphicsEnvironment;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

import sun.font.FontScaler;

import ca.strangebrew.Debug;
import ca.strangebrew.Options;


public class Splash {
    /**
     * Shows the splash screen, launches the application and then disposes
     * the splash screen.
     * @param args the command line arguments
     */
	private static Options opts;
	
	
    public static void main(String[] args) {
    	opts = Options.getInstance();
    	
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
		
    	// check if this is a mac/osx machine, and deal accordingly
    	String lcOSName = System.getProperty("os.name").toLowerCase();
    	Debug.print("OS is: " + lcOSName);
    	//Debug.flush();
    	
    	if (lcOSName.indexOf("mac") > -1) {
    		System.setProperty("apple.laf.useScreenMenuBar","true");
    		System.setProperty("com.apple.mrj.application.apple.menu.about.name","StrangeBrew");
    	}
    	
        SplashWindow.splash(Splash.class.getResource("splash.gif"));
        SplashWindow.invokeMain("StrangeSwing", args);
        SplashWindow.disposeSplash();
    }
    
}
