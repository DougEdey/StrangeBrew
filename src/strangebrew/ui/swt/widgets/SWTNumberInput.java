/*
 * Created on Oct 13, 2004
 * Window - Preferences - Java - Code Style - Code Templates
 */
package strangebrew.ui.swt.widgets;

import strangebrew.ui.core.Controller;
import strangebrew.ui.core.widgets.NumberInput;

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
	
	MyInput myInput;
	
	class MyInput extends SWTInput {
		SWTNumberInput myNumber;
		
		public MyInput(SWTNumberInput aNumber) {
			super(aNumber.myWidget);
			myNumber = aNumber;
		}
		
		void verify() {
			myNumber.verify();
		}
	}
	
	public SWTNumberInput(Controller aController) {
		super(aController);
	}
	
	public void init(Composite aContainer) {
		myWidget = new Text(aContainer, SWT.SINGLE);
		myInput = new MyInput(this);
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
