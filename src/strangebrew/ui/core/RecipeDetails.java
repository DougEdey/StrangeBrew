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
public class RecipeDetails extends Controller {
	
	RecipeDetailsView myContents;
	
	public RecipeDetails(RecipeDetailsView aView) {
		super(aView);
		myContents = aView;
	}
	
	public void init() {
		myView.init();

		myContents.getBrewerLabel().set("Brewer:");
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

	private void submitBrewer() {
		if (myContents.getBrewer().getText().equals
				(myContents.getBrewerLabel().getText())) {
			myContents.getBrewer().clear();
		}
	}
	
}
