/*
 * Created on Oct 15, 2004
 */
package strangebrew.ui.swt;

import org.eclipse.swt.widgets.*;
import org.eclipse.swt.*;
import strangebrew.ui.core.*;

/**
 * @author mike
 *
 */
public class SWTMenuItem extends strangebrew.ui.core.MenuItem {

	org.eclipse.swt.widgets.MenuItem myWidget;
	MySelect mySelect;
	
	class MySelect extends SWTSelect {
		SWTMenuItem myItem;
		
		public MySelect(SWTMenuItem anItem) {
			super(anItem.myWidget);
			myItem = anItem;
		}
		
		void verify() {
			myItem.verify();
		}
	}

	public SWTMenuItem(Controller aController) {
		super(aController);
	}

	public void init(org.eclipse.swt.widgets.Menu container) {
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
