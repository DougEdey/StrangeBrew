/*
 * Created on Oct 7, 2004
 *
 */
package strangebrew.ui.core;

import strangebrew.Recipe;

/**
 * @author mike
 *
 *
 */
public class MainController extends Controller {
	MainView myContents;
	RecipeDetailsController myRecipeDetails;
	Recipe myRecipe;
	
	public MainController(MainView aView, Recipe aRecipe) {
		super(aView);
		myContents = aView;
		myRecipe = aRecipe;
	}
	
	public void init() {
		myView.init();
		RecipeDetailsView newView = myContents.getRecipeDetailsView();
		myRecipeDetails = new RecipeDetailsController(newView, myRecipe);
		myRecipeDetails.init();
		myView.layout();
	}
	
	public void dispose() {
		myRecipeDetails.dispose();
		myView.dispose();
	}
	
	public void execute() {
		init();
		myView.display();
		dispose();
	}

}
