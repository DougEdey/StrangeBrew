package strangebrew.ui.swt;

import strangebrew.ui.core.*;

import org.eclipse.swt.widgets.*;
import org.eclipse.swt.*;

/**
 * Concrete class for an SWT Text Output widget.
 * @author mike
 *
 *
 */
public class SWTTextOutput extends TextOutput {

	Label myLabel;

	public void init(Composite container) {
		myLabel = new Label(container, SWT.NONE);
	}

	public void set(String aString) {
		myLabel.setText(aString);
	}

	public String getText() {
		return myLabel.getText();
	}

	public void dispose() {
		myLabel.dispose();
	}
	
	public Label getWidget() {
		return myLabel;
	}
}
