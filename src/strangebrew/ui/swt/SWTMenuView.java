/*
 * Created on Oct 15, 2004
 */
package strangebrew.ui.swt;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import strangebrew.ui.core.*;
import strangebrew.ui.swt.widgets.SWTMenu;
import strangebrew.ui.swt.widgets.SWTMenuItem;


/**
 * @author mike
 *
 */
public class SWTMenuView extends MenuView {
	Shell myContainer;
	FileDialog myOpenFileDialog;
	org.eclipse.swt.widgets.Menu myMenuBar;
	SWTMenu myFileMenu;
	SWTMenuItem myOpenItem;
	SWTMenuItem myQuitItem;

	public SWTMenuView(Shell container) {
		myContainer = container;
	}

	public void init() {
		myOpenFileDialog = new FileDialog(myContainer, SWT.OPEN);
		
       myMenuBar = new org.eclipse.swt.widgets.Menu(myContainer, SWT.BAR);
       myContainer.setMenuBar(myMenuBar);
       
       myFileMenu = new SWTMenu(myContainer, myMenuBar);      
       myOpenItem = new SWTMenuItem(myController, myFileMenu.getWidget());       
       myQuitItem = new SWTMenuItem(myController, myFileMenu.getWidget());
 	}
	
	public void layout() {
		// Nothing to do right now
		myContainer.layout();
		myContainer.pack();
	}
	
	public void display() {
		// Not needed in SWT
	}
	
	public void dispose() {
        // @TODO Figure out who should dispose myContainer
	}
	
	public strangebrew.ui.core.widgets.Menu getFileMenu() {
		return myFileMenu;
	}
	
	public strangebrew.ui.core.widgets.MenuItem getOpenItem() {
		return myOpenItem;
	}

	public strangebrew.ui.core.widgets.MenuItem getQuitItem() {
		return myQuitItem;
	}
	
	public void quit() {
		myContainer.close();
	}
	
	public String getOpenFilename() {
		return myOpenFileDialog.open();
	}
	
}
