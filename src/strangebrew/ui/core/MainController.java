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

	public MainController(MainView aView) {
		super(aView);
		myContents = aView;
	}
	
	public void init() {
		myView.init();
		
		MenuView mv = myContents.getMenuView();
		myMenuController = new MenuController(mv, this);
		myMenuController.init();
		
		RecipeNavigationView nv = 
			myContents.getRecipeNavigationView();
		myRecipeNavigation = 
			new RecipeNavigationController(nv);
		myRecipeNavigation.init();
		
		RecipeDetailsView dv = 
			myContents.getRecipeDetailsView("Recipe Details");
		myRecipeDetails = new RecipeDetailsController(dv);
		myRecipeDetails.init();

		setRecipe(new Recipe());

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

	/**
	 * Called before the app exits.
	 *
	 */
	public void cleanUp() {
		myMenuController.cleanUp();
		myRecipeNavigation.cleanUp();
		myRecipeDetails.cleanUp();
	}
	
	public void setRecipe(Recipe aRecipe) {
		myRecipe = aRecipe;
		myMenuController.setRecipe(aRecipe);
		myRecipeNavigation.setRecipe(aRecipe);
		myRecipeDetails.setRecipe(aRecipe);
	}
}
