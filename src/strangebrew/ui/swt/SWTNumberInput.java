/*
 * Created on Oct 13, 2004
 * Window - Preferences - Java - Code Style - Code Templates
 */
package strangebrew.ui.swt;

import strangebrew.ui.core.NumberInput;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.*;

/**
 * @author mike
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SWTNumberInput extends NumberInput {

	Scale myScale;
	
	public void init(Composite aContainer) {
		myScale = new Scale(aContainer, SWT.VERTICAL);
	}
	
	public void dispose() {
		myScale.dispose();
	}

	public void clear() {
		myScale.setSelection(0);
	}

	public float get() {
		return myScale.getSelection();
	}

	public void set(float aNumber) {
		myScale.setSelection((int)aNumber);
	}

	public Scale getWidget() {
		return myScale;
	}
}
