/*
 * Created on Oct 15, 2004
 */
package strangebrew.ui.swt;

import org.eclipse.swt.widgets.*;
import org.eclipse.swt.*;

import strangebrew.ui.core.Controller;
import strangebrew.ui.core.MenuItem;

/**
 * @author mike
 *
 */
public class SWTMenuItem extends MenuItem {

	org.eclipse.swt.widgets.MenuItem myWidget;

	public SWTMenuItem(Controller aController) {
		super(aController);
	}

	public void init(Menu container) {
		myWidget = new org.eclipse.swt.widgets.MenuItem(container, SWT.NONE);
	}

	public void dispose() {
		myWidget.dispose();
	}

	public void set(String aString) {
		if(aString != null) {
			myWidget.setText(aString);
		}
	}
	
	public org.eclipse.swt.widgets.MenuItem getWidget() {
		return myWidget;
	}
}
