package strangebrew.ui.swt.widgets;

import strangebrew.ui.core.widgets.TextOutput;

import org.eclipse.swt.widgets.*;
import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;

/**
 * Concrete class for an SWT Text Output widget.
 * @author mike
 *
 *
 */
public class SWTTextOutput extends TextOutput implements SWTFormWidget {

	Label myWidget;
	FormData myFormData;

	public void init(Composite container) {
		myWidget = new Label(container, SWT.NONE);
		myFormData = new FormData();
	}

	public void set(String aString) {
		myWidget.setText(aString);
	}

	public String get() {
		return myWidget.getText();
	}

	public void dispose() {
		myWidget.dispose();
	}
	
	public Label getWidget() {
		return myWidget;
	}
	
	public FormData getFormData() {
		return myFormData;
	}
	
	public Control getControl() {
		return myWidget;
	}
}
