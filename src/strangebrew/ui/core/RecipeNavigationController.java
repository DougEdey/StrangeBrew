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
		
		myContents.getRecipeNameLabel().set("Recipe Name:");
		
		populateWidgets();
		myView.layout();
	}

	public void dispose() {
		myView.dispose();
	}
	
	public void populateWidgets() {
		myContents.getRecipeName().set(myRecipe.name);
	}

	public void execute() {
		if(myContents.getRecipeName().isUpdated()) {
			submitRecipeName();
		}
	}
	
	public void submitRecipeName() {
		myRecipe.name = myContents.getRecipeName().get();
	}

}
