package strangebrew.ui.swt;

import strangebrew.ui.core.*;

import org.eclipse.swt.widgets.*;

/**
 * Concrete SWT Widget Factory.
 * @author mike
 *
 * @see strangebrew.ui.core.Factory
 */
public class SWTFactory extends Factory {

	Composite myContainer;

	public SWTFactory(Composite container) {
		myContainer = container;
	}

	public RecipeDetailsView newRecipeDetailsView() {
		return new SWTRecipeDetailsView(this, myContainer);
	}
}
