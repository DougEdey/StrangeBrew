package strangebrew.ui.swt.widgets;

import strangebrew.ui.core.*;
import strangebrew.ui.core.widgets.TextInput;

import org.eclipse.swt.widgets.*;
import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;

/**
 * $Id: SWTTextInput.java,v 1.4 2004/10/21 01:52:14 tangent_ Exp $
 * @author mike
 *
 * Concrete class for SWT TextInput widgets.
 */
public class SWTTextInput extends TextInput implements SWTFormWidget {

	Text myWidget;
	MyInput myInput;
	FormData myFormData;
	
	class MyInput extends SWTInput {
		SWTTextInput myText;
		
		public MyInput(SWTTextInput aText) {
			super(aText.myWidget);
			myText = aText;
		}
		
		void verify() {
			myText.verify();
		}
	}
	
	public SWTTextInput(Controller aController, Composite container) {
		super(aController);
		myWidget = new Text(container, SWT.SINGLE);
		myInput = new MyInput(this);
		myFormData = new FormData();
	}

	public void dispose() {
		myWidget.dispose();
	}

	public void clear() {
		myInput.suspend();
		myWidget.setText("");
		myInput.resume();
	}

	public String get() {
		return myWidget.getText();
	}
	
	public void set(String aString) {
		if(aString != null) {
			myWidget.setText(aString);
		}
	}
	
	public Text getWidget() {
		return myWidget;
	}
	
	public FormData getFormData() {
		return myFormData;
	}
	
	public Control getControl() {
		return myWidget;
	}
}
