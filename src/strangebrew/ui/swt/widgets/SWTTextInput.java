package strangebrew.ui.swt.widgets;

import strangebrew.ui.core.*;
import strangebrew.ui.core.widgets.TextInput;

import org.eclipse.swt.widgets.*;
import org.eclipse.swt.*;

/**
 * Concrete class for SWT TextInput widgets.
 * @author mike
 *
 *
 */
public class SWTTextInput extends TextInput {

	Text myWidget;
	MyInput myInput;
	
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
	
	public SWTTextInput(Controller aController) {
		super(aController);
	}

	public void init(Composite container) {
		myWidget = new Text(container, SWT.SINGLE);
		myInput = new MyInput(this);
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
}
