/*
 * Created on Oct 13, 2004
 * Window - Preferences - Java - Code Style - Code Templates
 */
package strangebrew.ui.swt;

import strangebrew.ui.core.Controller;
import strangebrew.ui.core.NumberInput;

import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.*;

/**
 * @author mike
 *
 * 
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SWTNumberInput extends NumberInput {

	Text myWidget;
	
	MyTypingListener myListener;
	
	class MyTypingListener extends SWTInput {
		SWTNumberInput myTarget;
		
		public MyTypingListener(SWTNumberInput aTarget) {
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
	
	public SWTNumberInput(Controller aController) {
		super(aController);
	}
	
	public void init(Composite aContainer) {
		myWidget = new Text(aContainer, SWT.SINGLE);
		myListener = new MyTypingListener(this);
	}
	
	public void dispose() {
		myWidget.dispose();
	}

	public void clear() {
		myWidget.setSelection(0);
	}

	public double get() {
		Double n = new Double(myWidget.getText());
		return n.doubleValue();
	}

	public void set(double aNumber) {
		Double n=new Double(aNumber);
		myWidget.setText(n.toString());
	}

	public Text getWidget() {
		return myWidget;
	}
}
