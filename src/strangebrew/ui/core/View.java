package strangebrew.ui.core;

/**
 * Abstract parent class for the controller.
 * This class holds the logic for the UI in the app.
 * @author mike
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
