/*
 * Created on Oct 15, 2004
 */
package strangebrew.ui.core;

/**
 * @author mike
 *
 */
public abstract class MenuItem {

	protected Controller myController;
	protected boolean isSelected = false;

	public MenuItem(Controller aController) {
		myController = aController;
	}

	public abstract void dispose();
	public abstract void set(String aString);

	public void verify() {
		isSelected = true;
		myController.execute();
		isSelected = false;
	}
	
	public boolean isSelected() {
		return isSelected;
	}	
	
}
