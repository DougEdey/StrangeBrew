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
	
	public RecipeNavigationController(RecipeNavigationView aView) {
		super(aView);
		myContents = aView;
	}

	public void init() {
		myView.init();
		
		myContents.getRecipeNameLabel().set("Recipe Name:");

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
	
	public void cleanUp() {
		submitRecipeName();
	}
	
	public void setRecipe(Recipe aRecipe) {
		myRecipe = aRecipe;
		populateWidgets();
	}
	
	public void submitRecipeName() {
		if (myContents.getRecipeName().get() != null) {
			myRecipe.name = myContents.getRecipeName().get();
		}
	}

}
