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
	MenuController myMenuController;
	RecipeDetailsController myRecipeDetails;
	RecipeNavigationController myRecipeNavigation;

	public MainController(MainView aView, Recipe aRecipe) {
		super(aView, aRecipe);
		myContents = aView;
	}
	
	public void init() {
		myView.init();
		MenuView mv = myContents.getMenuView();
		myMenuController = new MenuController(mv, myRecipe);
		myMenuController.init();
		RecipeNavigationView nv = 
			myContents.getRecipeNavigationView();
		myRecipeNavigation = 
			new RecipeNavigationController(nv, myRecipe);
		myRecipeNavigation.init();
		RecipeDetailsView dv = 
			myContents.getRecipeDetailsView("Recipe Details");
		myRecipeDetails = new RecipeDetailsController(dv, myRecipe);
		myRecipeDetails.init();
		myView.layout();
	}
	
	public void dispose() {
		myRecipeNavigation.dispose();
		myRecipeDetails.dispose();
		myView.dispose();
	}
	
	public void execute() {
		init();
		myView.display();
		dispose();
	}

}
