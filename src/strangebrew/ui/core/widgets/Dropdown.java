/*
 * $Id: Dropdown.java,v 1.2 2004/10/21 02:11:47 tangent_ Exp $
 * Created on Oct 18, 2004
 */
package strangebrew.ui.core.widgets;

import strangebrew.ui.core.Controller;

/**
 * @author mike
 *
 */
public abstract class Dropdown {
	protected Controller myController;
	protected boolean isSelected = false;

	public Dropdown(Controller aController) {
		myController = aController;
	}

	public abstract void dispose();

	public void verify() {
		isSelected = true;
		myController.execute();
		isSelected = false;
	}
	
	public boolean isSelected() {
		return isSelected;
	}
	
	public abstract int getItemSelected();
	public abstract void add(String aString);
	public abstract String get();
	
}
