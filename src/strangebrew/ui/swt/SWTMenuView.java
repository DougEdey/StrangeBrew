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
	MenuItem myItem;

	public SWTMenuView(Shell container) {
		myContainer = container;
	}

	public void init() {
       myMenuBar = new Menu(myContainer, SWT.BAR);
       myContainer.setMenuBar(myMenuBar);
       myItem = new MenuItem(myMenuBar, SWT.NONE);
       myItem.setText("File");
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

}
