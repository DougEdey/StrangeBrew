package strangebrew.ui.core;

/**
 * Abstract parent class for the controller.
 * This class holds the logic for the UI in the app.
 * @author mike
 **/

public abstract class View {
	
	protected Factory myFactory;
	protected Controller myController;

	public abstract void init();
	public abstract void layout();
	public abstract void dispose();
	public abstract void display();
	public abstract Factory createChildFactory(String aTitle);

	public Factory getFactory() {
		return myFactory;
	}
	
	public void setController(Controller aController) {
		myController = aController;
	}

}
