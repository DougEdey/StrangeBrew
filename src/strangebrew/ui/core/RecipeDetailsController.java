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
public class RecipeDetailsController extends Controller {
	
	RecipeDetailsView myContents;
	
	public RecipeDetailsController(RecipeDetailsView aView, Recipe aRecipe) {
		super(aView, aRecipe);
		myContents = aView;
	}
	
	public void init() {
		myView.init();

		myContents.getBrewerLabel().set("Brewer:");
		myContents.getEfficiencyLabel().set("% Effic:");
		myContents.getAlcoholLabel().set("% Alc:");
		myContents.getAlcoholPostfix().set("by Volume");
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
		Double alc = new Double(myRecipe.alcohol);
		myContents.getAlcohol().set(alc.toString());
	}
	
	public void cleanUp() {
		submitBrewer();
		submitEfficiency();
	}

	private void submitBrewer() {
		if (myContents.getBrewer().get() != null) {
			myRecipe.brewer = myContents.getBrewer().get();
		}
	}

	private void submitEfficiency() {
		myRecipe.efficiency = myContents.getEfficiency().get();
	}

}
