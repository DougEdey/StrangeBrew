package strangebrew.ui.swt;

import strangebrew.ui.core.*;

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

	Text myText;
	MyTypingListener myListener;
	
	class MyTypingListener extends SWTInput {
		SWTTextInput myTarget;
		
		public MyTypingListener(SWTTextInput aTarget) {
			super();
			myTarget = aTarget;
		    myListener.addTo(this);
		}
		
		public void addListener(TypingListener aListener) {
			myTarget.addListener(aListener);
		}
		
		public void removeListener(TypingListener aListener) {
			myTarget.removeListener(aListener);
		}
		
		public void verify() {
			myTarget.verify();
		}
	}
	
	public SWTTextInput(Controller aController) {
		super(aController);
	}


	private void addListener(SelectionListener aListener) {
		myText.addSelectionListener(aListener);
	}

	private void removeListener(SelectionListener aListener) {
		myText.removeSelectionListener(aListener);
	}

	public void init(Composite container) {
		myText = new Text(container, SWT.SINGLE);
		myListener = new MyTypingListener(this);
	}

	public void dispose() {
		myText.dispose();
	}

	public void clear() {
		myListener.suspend();
		myText.setText("");
		myListener.resume();
	}

	public String get() {
		return myText.getText();
	}
	
	public void set(String aString) {
		if(aString != null) {
			myText.setText(aString);
		}
	}
	
	public Text getWidget() {
		return myText;
	}
}
