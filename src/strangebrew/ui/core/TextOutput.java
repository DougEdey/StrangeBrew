package strangebrew.ui.core;

/**
 * Abstract class for a text output (like a label).
 * @author mike
 *
 */
public abstract class TextOutput {

	public abstract String getText();

	public abstract void dispose();

	public abstract void set(String aString);
}
