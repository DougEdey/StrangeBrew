package strangebrew.ui.core;

/**
 * $Id: View.java,v 1.4 2004/10/21 01:57:42 tangent_ Exp $
 * @author mike
 * 
 * Abstract parent class for the controller.
 * This class holds the logic for the UI in the app.
 **/

public abstract class View {
	
	protected Controller myController;

	public abstract void init();
	public abstract void layout();
	public abstract void dispose();
	public abstract void display();
	
	public void setController(Controller aController) {
		myController = aController;
	}

}
