/*
 * Created on Oct 15, 2004
 */
package strangebrew.ui.swt;

import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;


/**
 * @author mike
 *
 */
public abstract class SWTSelect {
	protected ClickListener myListener;
	protected org.eclipse.swt.widgets.MenuItem myTarget;
	
	public SWTSelect(org.eclipse.swt.widgets.MenuItem aTarget) {
		myTarget = aTarget;
		myListener = new ClickListener();
	    myListener.addTo(this);
	}
	
	abstract void verify();
	
	public void addListener(ClickListener aListener) {
		myTarget.addSelectionListener(aListener);
	}
	
	public void removeListener(ClickListener aListener) {
		myTarget.removeSelectionListener(aListener);
	}
		
	public void suspend() {
		myListener.suspend();
	}
	
	public void resume() {
		myListener.resume();
	}
	
	protected class ClickListener implements SelectionListener {

		SWTSelect target;
		boolean suspended = true;

		public void addTo(SWTSelect anSelect) {
			if (target != null) {
				target.removeListener(this);
			}
			suspended = false;
			target = anSelect;
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
			// Nothing to do here
		}
		
		/**
		 * Does nothing since we don't care about widget selection.
		 */
		public void widgetSelected(SelectionEvent e) {
			if((target != null)&&(!suspended)) {
				target.verify();
			}
		}

	}

}
