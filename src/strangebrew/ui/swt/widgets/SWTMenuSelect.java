/*
 * $Id: SWTMenuSelect.java,v 1.3 2004/11/16 18:11:48 andrew_avis Exp $
 * Created on Oct 15, 2004
 */
package strangebrew.ui.swt.widgets;

import org.eclipse.swt.events.*;



/**
 * @author mike
 *
 */
public abstract class SWTMenuSelect {
	protected ClickListener myListener;
	protected org.eclipse.swt.widgets.MenuItem myTarget;
	
	public SWTMenuSelect(org.eclipse.swt.widgets.MenuItem aTarget) {
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

		SWTMenuSelect target;
		boolean suspended = true;

		public void addTo(SWTMenuSelect anSelect) {
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
