/*
 * Created on Oct 15, 2004
 */
package strangebrew.ui.swt;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import strangebrew.ui.core.*;


/**
 * @author mike
 *
 */
public class SWTMenuView extends MenuView {
	Shell myContainer;
	Menu myMenuBar;
	SWTMenuItem myFileMenu;

	public SWTMenuView(Shell container) {
		myContainer = container;
	}

	public void init() {
       myMenuBar = new Menu(myContainer, SWT.BAR);
       myContainer.setMenuBar(myMenuBar);
       myFileMenu = new SWTMenuItem(myController);
       myFileMenu.init(myMenuBar);
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
	
	public strangebrew.ui.core.MenuItem getFileMenu() {
		return myFileMenu;
	}

}
