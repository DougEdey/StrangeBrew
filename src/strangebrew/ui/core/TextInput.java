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

	public TextInput() {
	}

	public TextInput(Controller aController) {
		myController = aController;
	}

	public abstract void dispose();
	
	public abstract void clear();
	
	public abstract String getText();
	
	public abstract void setText(String aString);

	public void verifyText() {
		isUpdated = true;
		myController.execute();
		isUpdated = false;
	}
	
	public boolean isUpdated() {
		return isUpdated;
	}

}
