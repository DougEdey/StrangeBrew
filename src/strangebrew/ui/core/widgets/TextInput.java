package strangebrew.ui.core.widgets;

import strangebrew.ui.core.Controller;

/**
 * $Id: TextInput.java,v 1.2 2004/10/21 02:09:59 tangent_ Exp $
 * @author mike
 *
 * Abstract class for for a text input widget.
 */
public abstract class TextInput {
	protected Controller myController;
	protected boolean isUpdated = false;

	public TextInput(Controller aController) {
		myController = aController;
	}

	public abstract void dispose();
	
	public abstract void clear();
	
	public abstract String get();
	
	public abstract void set(String aString);

	public void verify() {
		isUpdated = true;
		myController.execute();
		isUpdated = false;
	}
	
	public boolean isUpdated() {
		return isUpdated;
	}

}
