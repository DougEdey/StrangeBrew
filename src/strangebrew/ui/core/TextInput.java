package strangebrew.ui.core;

/**
 * Abstract class for for a text input widget.
 * @author mike
 *
 *
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
