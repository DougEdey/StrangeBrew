/*
 * Created on Oct 18, 2004
 */
package strangebrew.ui.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

import strangebrew.ui.core.Controller;
import strangebrew.ui.core.widgets.*;

/**
 * @author mike
 *
 */
public class SWTDropdown extends Dropdown implements SWTFormWidget {

	Combo myWidget;
	MySelect mySelect;
	FormData myFormData;

	
	class MySelect extends SWTComboSelect {
		SWTDropdown myItem;
		
		public MySelect(SWTDropdown anItem) {
			super(anItem.myWidget);
			myItem = anItem;
		}
		
		void verify() {
			myItem.verify();
		}
	}

	public SWTDropdown(Controller aController) {
		super(aController);
	}

	public void init(Composite container) {
		myWidget = new Combo(container, SWT.SINGLE);
		mySelect = new MySelect(this);
		myFormData = new FormData();
	}

	public void dispose() {
		myWidget.dispose();
	}
	
	public int getItemSelected() {
		return myWidget.getSelectionIndex();
	}
	
	public void add(String aString) {
		if(aString != null) {
			int index = myWidget.indexOf(aString);
			if(index == -1) {
				myWidget.add(aString);
				// Select the one we just added
				myWidget.select(myWidget.getItemCount() - 1);
			} else {
				myWidget.select(index);
			}
		}
	}
	
	public String get() {
		return myWidget.getItem(getItemSelected());
	}
	
	public Combo getWidget() {
		return myWidget;
	}
	
	public FormData getFormData() {
		return myFormData;
	}
	
	public Control getControl() {
		return myWidget;
	}
}
