/*
 * $Id: RecipeNavigationView.java,v 1.4 2004/10/21 01:58:06 tangent_ Exp $
 * Created on Oct 14, 2004
 */
package strangebrew.ui.core;

import strangebrew.ui.core.widgets.TextInput;
import strangebrew.ui.core.widgets.TextOutput;

/**
 * @author mike
 *
 */
public abstract class RecipeNavigationView extends View {

	public abstract TextInput getRecipeName();
	public abstract TextOutput getRecipeNameLabel();
}
