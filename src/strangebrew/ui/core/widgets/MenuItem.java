/*
 * $Id: MenuItem.java,v 1.2 2004/10/21 02:10:54 tangent_ Exp $
 * Created on Oct 15, 2004
 */
package strangebrew.ui.core.widgets;

import strangebrew.ui.core.Controller;

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
