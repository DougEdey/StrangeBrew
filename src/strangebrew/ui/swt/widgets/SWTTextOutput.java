package strangebrew.ui.swt.widgets;

import strangebrew.ui.core.*;
import strangebrew.ui.core.widgets.TextOutput;

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

	public String get() {
		return myLabel.getText();
	}

	public void dispose() {
		myLabel.dispose();
	}
	
	public Label getWidget() {
		return myLabel;
	}
}
