/*
 * Created on Oct 13, 2004
 * Window - Preferences - Java - Code Style - Code Templates
 */
package strangebrew.ui.swt;

import strangebrew.ui.core.NumberInput;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.*;
import java.lang.Number.*;

/**
 * @author mike
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SWTNumberInput extends NumberInput {

	Text myWidget;
	
	public void init(Composite aContainer) {
		myWidget = new Text(aContainer, SWT.SINGLE);
	}
	
	public void dispose() {
		myWidget.dispose();
	}

	public void clear() {
		myWidget.setSelection(0);
	}

	public float get() {
		Float n = new Float(myWidget.getText());
		return n.floatValue();
	}

	public void set(float aNumber) {
		Float n=new Float(aNumber);
		myWidget.setText(n.toString());
	}

	public Text getWidget() {
		return myWidget;
	}
}
