/*
 * Created on Oct 7, 2004
 *
 */
package strangebrew.ui.core;

/**
 * @author mike
 *
 *
 */
public class MainController extends Controller {
	RecipeDetails myRecipeDetails;
	
	public MainController(View aView) {
		super(aView);
	}
	
	public void init() {
		myView.init();
		Factory factory = myView.createChildFactory("Recipe Details");
		RecipeDetailsView view = factory.newRecipeDetailsView();
		myRecipeDetails = new RecipeDetails(view);
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
