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
public class RecipeDetails extends Controller {
	
	RecipeDetailsView myContents;
	Recipe myRecipe;
	
	public RecipeDetails(RecipeDetailsView aView, Recipe aRecipe) {
		super(aView);
		myContents = aView;
		myRecipe = aRecipe;
	}
	
	public void init() {
		myView.init();

		myContents.getBrewerLabel().set("Brewer:");
		populateWidgets();
		myView.layout();
	}
	
	public void dispose() {
		myView.dispose();
	}

	public void execute() {
		if(myContents.getBrewer().isUpdated()) {
			submitBrewer();
		}
	}
	
	private void populateWidgets() {
		myContents.getBrewer().setText(myRecipe.brewer);
	}

	private void submitBrewer() {
			myRecipe.brewer = myContents.getBrewer().getText();
	}
	
}
