/*
 * $Id: SWTMenuItem.java,v 1.5 2004/11/16 18:11:48 andrew_avis Exp $
 * Created on Oct 15, 2004
 */
package strangebrew.ui.swt.widgets;


import org.eclipse.swt.*;
import strangebrew.ui.core.*;

/**
 * @author mike
 *
 */
public class SWTMenuItem extends strangebrew.ui.core.widgets.MenuItem {

	org.eclipse.swt.widgets.MenuItem myWidget;
	MySelect mySelect;
	
	class MySelect extends SWTMenuSelect {
		SWTMenuItem myItem;
		
		public MySelect(SWTMenuItem anItem) {
			super(anItem.myWidget);
			myItem = anItem;
		}
		
		void verify() {
			myItem.verify();
		}
	}

	public SWTMenuItem(Controller aController, org.eclipse.swt.widgets.Menu container) {
		super(aController);
		myWidget = new org.eclipse.swt.widgets.MenuItem(container, SWT.NONE);
		mySelect = new MySelect(this);
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
