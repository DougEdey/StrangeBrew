package strangebrew.ui.core.widgets;

/**
 * $Id: TextOutput.java,v 1.2 2004/10/21 02:09:11 tangent_ Exp $
 * @author mike
 *
 * Abstract class for a text output (like a label).
 */
public abstract class TextOutput {

	public abstract String get();

	public abstract void dispose();

	public abstract void set(String aString);
}
