/*
 * Created on Oct 7, 2004
 *
 */
package strangebrew.ui.core;

import strangebrew.Recipe;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

/**
 * @author mike
 *
 *
 */
public class RecipeDetailsController extends Controller {
	
	RecipeDetailsView myContents;
	
	public RecipeDetailsController(RecipeDetailsView aView) {
		super(aView);
		myContents = aView;
	}
	
	public void init() {
		myView.init();

		myContents.getBrewerLabel().set("Brewer:");
		myContents.getEfficiencyLabel().set("% Effic:");
		myContents.getAlcoholLabel().set("% Alc:");
		myContents.getAlcoholPostfix().set("by Volume");
		myContents.getDateLabel().set("Date:");
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
		if(myContents.getDate().isUpdated()) {
			submitDate();
		}
	}
	
	private void populateWidgets() {
		myContents.getBrewer().set(myRecipe.brewer);
		myContents.getEfficiency().set(myRecipe.efficiency);
		Double alc = new Double(myRecipe.alcohol);
		myContents.getAlcohol().set(alc.toString());
		if (myRecipe.created == null) {
			myRecipe.created = new GregorianCalendar();
		}
		SimpleDateFormat df = new SimpleDateFormat();
		myContents.getDate().set(df.format(myRecipe.created.getTime()));
	}
	
	public void cleanUp() {
		submitBrewer();
		submitEfficiency();
	}

	public void setRecipe(Recipe aRecipe) {
		myRecipe = aRecipe;
		populateWidgets();
	}

	private void submitBrewer() {
		if (myContents.getBrewer().get() != null) {
			myRecipe.brewer = myContents.getBrewer().get();
		}
	}

	private void submitEfficiency() {
		myRecipe.efficiency = myContents.getEfficiency().get();
	}
	
	private void submitDate() {
		if (myRecipe.created == null) {
			// since there is no date, might as well set it
			myRecipe.created = new GregorianCalendar();
		}
		if (myContents.getDate().get() != null) {
			SimpleDateFormat df = new SimpleDateFormat();
			try {
				myRecipe.created.setTime(df.parse(myContents.getDate().get()));
			} catch (Exception e) {
				// The date was invalid, so reset the widget
				myContents.getDate().set(df.format(myRecipe.created.getTime()));				
			}
		}
	}

}
