/*
 * Created on Oct 13, 2004
 */
package strangebrew.ui.core;

/**
 * @author mike
 *
 */
public abstract class NumberInput {
	protected Controller myController;
	protected boolean isUpdated = false;

	public NumberInput() {
	}

	public NumberInput(Controller aController) {
		myController = aController;
	}

	public abstract void dispose();
	
	public abstract void clear();
	
	public abstract float get();
	
	public abstract void set(float aNumber);

	public void verify() {
		isUpdated = true;
		myController.execute();
		isUpdated = false;
	}
	
	public boolean isUpdated() {
		return isUpdated;
	}

}
