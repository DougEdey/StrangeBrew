/*
 * $Id: NumberInput.java,v 1.2 2004/10/21 02:10:25 tangent_ Exp $
 * Created on Oct 13, 2004
 */
package strangebrew.ui.core.widgets;

import strangebrew.ui.core.Controller;

/**
 * @author mike
 *
 */
public abstract class NumberInput {
	protected Controller myController;
	protected boolean isUpdated = false;

	public NumberInput(Controller aController) {
		myController = aController;
	}

	public abstract void dispose();
	
	public abstract void clear();
	
	public abstract double get();
	
	public abstract void set(double aNumber);

	public void verify() {
		isUpdated = true;
		myController.execute();
		isUpdated = false;
	}
	
	public boolean isUpdated() {
		return isUpdated;
	}

}
