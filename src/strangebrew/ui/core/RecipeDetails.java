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
		myContents.getEfficiencyLabel().set("% Effic:");
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
		if(myContents.getEfficiency().isUpdated()) {
			submitEfficiency();
		}
	}
	
	private void populateWidgets() {
		myContents.getBrewer().set(myRecipe.brewer);
		myContents.getEfficiency().set(myRecipe.efficiency);
	}

	private void submitBrewer() {
			myRecipe.brewer = myContents.getBrewer().get();
	}

	private void submitEfficiency() {
		myRecipe.efficiency = myContents.getEfficiency().get();
}

}
