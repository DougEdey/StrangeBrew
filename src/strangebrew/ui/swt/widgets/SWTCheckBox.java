/*
 * Created on Oct 15, 2004
 */
package strangebrew.ui.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.*;

import strangebrew.ui.core.Controller;
import strangebrew.ui.core.widgets.CheckBox;
import strangebrew.ui.swt.widgets.*;

/**
 * @author mike
 *
 */
public class SWTCheckBox extends CheckBox implements SWTFormWidget {
	Button myWidget;
	MySelect mySelect;
	FormData myFormData;
	
	class MySelect extends SWTButtonSelect {
		SWTCheckBox myItem;
		
		public MySelect(SWTCheckBox anItem) {
			super(anItem.myWidget);
			myItem = anItem;
		}
		
		void verify() {
			myItem.verify();
		}
	}

	public SWTCheckBox(Controller aController) {
		super(aController);
	}

	public void init(Composite container) {
		myWidget = new Button(container, SWT.CHECK);
		mySelect = new MySelect(this);
		myFormData = new FormData();
	}

	public void dispose() {
		myWidget.dispose();
	}

	public void set(boolean state) {
		myWidget.setSelection(state);
	}

	public boolean isSet() {
		return myWidget.getSelection();
	}
	
	public Button getWidget() {
		return myWidget;
	}
	
	public FormData getFormData() {
		return myFormData;
	}
	
	public Control getControl() {
		return myWidget;
	}
}
