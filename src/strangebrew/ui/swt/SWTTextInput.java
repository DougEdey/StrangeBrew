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
	TypingListener myListener;

	public SWTTextInput(Controller aController) {
		myController = aController;
	}

	class TypingListener implements SelectionListener {

		SWTTextInput target;
		boolean suspended = true;

		public void addTo(SWTTextInput anInput) {
			if (target != null) {
				target.removeListener(this);
			}
			suspended = false;
			target = anInput;
			target.addListener(this);
		}

		public void remove() {
			if (target != null) {
				target.removeListener(this);
				suspended = true;
				target = null;
			}
		}

		public void suspend() {
			suspended = true;
		}

		public void resume() {
			suspended = false;
		}

        /**
         * Called whenever the user presses return key while in widget.
         */
		public void widgetDefaultSelected(SelectionEvent e) {
			if((target != null)&&(!suspended)) {
				target.verifyText();
			}
		}
		
		/**
		 * Does nothing since we don't care about widget selection.
		 */
		public void widgetSelected(SelectionEvent e) {
			// Don't really need to do anything here
		}

	}

	private void addListener(TypingListener aListener) {
		myText.addSelectionListener(aListener);
	}

	private void removeListener(TypingListener aListener) {
		myText.removeSelectionListener(aListener);
	}

	public void init(Composite container) {
		myText = new Text(container, SWT.SINGLE);
		myListener = new TypingListener();
		myListener.addTo(this);
	}

	public void dispose() {
		myText.dispose();
	}

	public void clear() {
		myListener.suspend();
		myText.setText("");
		myListener.resume();
	}

	public String getText() {
		return myText.getText();
	}
	
	public Text getWidget() {
		return myText;
	}
}
