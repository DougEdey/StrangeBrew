package strangebrew.ui.core.widgets;

/**
 * Abstract class for a text output (like a label).
 * @author mike
 *
 */
public abstract class TextOutput {

	public abstract String get();

	public abstract void dispose();

	public abstract void set(String aString);
}
