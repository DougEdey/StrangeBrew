/*
 * Created on Oct 14, 2004
 */
package strangebrew.ui.core;

import strangebrew.Recipe;

/**
 * @author mike
 *
 */
public class RecipeNavigationController extends Controller {

	RecipeNavigationView myContents;
	
	public RecipeNavigationController(RecipeNavigationView aView, Recipe aRecipe) {
		super(aView, aRecipe);
		myContents = aView;
	}

	public void init() {
		myView.init();
		populateWidgets();
		myView.layout();
	}

	public void dispose() {
		myView.dispose();
	}
	
	public void populateWidgets() {
		// Nothing to do yet.
	}

	public void execute() {
		// Nothing to do yet.
	}

}
