package strangebrew.ui.swt;

import strangebrew.ui.core.*;
import strangebrew.ui.swt.SWTInput.TypingListener;

import org.eclipse.swt.events.*;
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
	MyTypingListener myListener;
	
	class MyTypingListener extends SWTInput {
		SWTTextInput myTarget;
		
		public MyTypingListener(SWTTextInput aTarget) {
			super();
			myTarget = aTarget;
		    myListener.addTo(this);
		}
		
		public void addListener(TypingListener aListener) {
			myTarget.myWidget.addSelectionListener(aListener);
			myTarget.myWidget.addFocusListener(aListener);
		}
		
		public void removeListener(TypingListener aListener) {
			myTarget.myWidget.removeSelectionListener(aListener);
			myTarget.myWidget.removeFocusListener(aListener);
		}
		
		public void verify() {
			myTarget.verify();
		}
	}
	
	public SWTTextInput(Controller aController) {
		super(aController);
	}

	public void init(Composite container) {
		myWidget = new Text(container, SWT.SINGLE);
		myListener = new MyTypingListener(this);
	}

	public void dispose() {
		myWidget.dispose();
	}

	public void clear() {
		myListener.suspend();
		myWidget.setText("");
		myListener.resume();
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
