/*
 * Created on Oct 14, 2004
 */
package strangebrew.ui.swt;

import org.eclipse.swt.events.*;

/**
 * @author mike
 *
 */
public abstract class SWTInput {
	
	protected TypingListener myListener;
	
	public SWTInput() {
		myListener = new TypingListener();
	}
	
	protected abstract void addListener(TypingListener listener);
	protected abstract void removeListener(TypingListener listener);
	protected abstract void verify();
	
	public void suspend() {
		myListener.suspend();
	}
	
	public void resume() {
		myListener.resume();
	}
	
	protected class TypingListener implements SelectionListener, FocusListener {

		SWTInput target;
		boolean suspended = true;

		public void addTo(SWTInput anInput) {
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
				target.verify();
			}
		}
		
		/**
		 * Does nothing since we don't care about widget selection.
		 */
		public void widgetSelected(SelectionEvent e) {
			// Don't really need to do anything here
		}
		
		/**
		 * Does nothing since we don't care about gaining focus.
		 */
		public void focusGained(FocusEvent e) {
			//Don't really need to do anything here
		}
		
		/**
		 * Update the widget when the user leaves it.
		 */
		public void focusLost(FocusEvent e) {
			if((target != null)&&(!suspended)) {
				target.verify();
			}			
		}

	}

}
