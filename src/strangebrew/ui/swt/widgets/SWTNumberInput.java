/*
 * $Id: SWTNumberInput.java,v 1.4 2004/10/21 01:52:53 tangent_ Exp $
 * Created on Oct 13, 2004
 * Window - Preferences - Java - Code Style - Code Templates
 */
package strangebrew.ui.swt.widgets;

import strangebrew.ui.core.Controller;
import strangebrew.ui.core.widgets.NumberInput;

import org.eclipse.swt.widgets.*;
import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;

/**
 * @author mike
 *
 * 
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SWTNumberInput extends NumberInput implements SWTFormWidget {

	Text myWidget;
	FormData myFormData;
	
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
	
	public SWTNumberInput(Controller aController, Composite aContainer) {
		super(aController);
		myWidget = new Text(aContainer, SWT.SINGLE);
		myInput = new MyInput(this);
		myFormData = new FormData();
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
	
	public FormData getFormData() {
		return myFormData;
	}
	
	public Control getControl() {
		return myWidget;
	}
}
