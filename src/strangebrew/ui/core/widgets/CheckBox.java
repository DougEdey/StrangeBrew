/*
 * Created on Oct 15, 2004
 */
package strangebrew.ui.core.widgets;

import strangebrew.ui.core.Controller;

/**
 * @author mike
 *
 */
public abstract class CheckBox {
	protected Controller myController;
	protected boolean isUpdated = false;

	public CheckBox(Controller aController) {
		myController = aController;
	}

	public abstract void dispose();
	public abstract boolean isSet();
		public abstract void set(boolean state);

	public void verify() {
		isUpdated = true;
		myController.execute();
		isUpdated = false;
	}
	
	public boolean isUpdated() {
		return isUpdated;
	}

}
