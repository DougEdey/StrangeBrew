/*
 * $Id: RecipeDetailsController.java,v 1.19 2004/11/16 18:11:39 andrew_avis Exp $
 * Created on Oct 7, 2004
 *
 */
package strangebrew.ui.core;

import strangebrew.Recipe;
import java.text.SimpleDateFormat;

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
		myContents.getMashLabel().set("Mash?");
		myContents.getAttenuationLabel().set("% Atten:");
		myContents.getIBULabel().set("IBU:");
		myContents.getIBUPostfix().set("(Rager)");
		myContents.getStyleLabel().set("Style:");
		myContents.getOGLabel().set("OG:");
		myContents.getColourLabel().set("Colour:");
		myContents.getColourPostfix().set("(SRM)");
		myContents.getYeastLabel().set("Yeast:");
		myContents.getFGLabel().set("FG:");

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
		
		if(myContents.getMash().isUpdated()) {
			// TODO update this when mash is in Recipe
		}
		
		if(myContents.getAttenuation().isUpdated()) {
			submitAttenuation();
		}
		
		if(myContents.getStyle().isSelected()) {
			submitStyle();
		}
		
		if(myContents.getOG().isUpdated()) {
			submitOG();
		}
		
		if(myContents.getYeast().isSelected()) {
			submitYeast();
		}

		if(myContents.getFG().isUpdated()) {
			submitFG();
		}
		
	}
	
	private void populateWidgets() {
		myContents.getBrewer().set(myRecipe.getBrewer());
		myContents.getEfficiency().set(myRecipe.getEfficiency());
		Double alc = new Double(myRecipe.getAlcohol());
		myContents.getAlcohol().set(alc.toString());

		SimpleDateFormat df = new SimpleDateFormat();
		myContents.getDate().set(df.format(myRecipe.getCreated().getTime()));
		// TODO update Mash checkbox when it is in recipe
		myContents.getMash().set(true);
		myContents.getAttenuation().set(myRecipe.getAttenuation());
		Double ibu = new Double(myRecipe.getIbu());
		myContents.getIBU().set(ibu.toString());
		// TODO update Style dropdown from database
		myContents.getStyle().add(myRecipe.getStyle());
		myContents.getOG().set(myRecipe.getEstOg());
		Double colour = new Double(myRecipe.getSrm());
		myContents.getColour().set(colour.toString());
		// TODO update Yeast dropdown when it is in recipe
		// myContents.getYeast().set(myRecipe.???);
		myContents.getFG().set(myRecipe.getEstFg());
		myContents.getIBUPostfix().set("("+myRecipe.getIBUMethod()+")");
	}
	
	public void cleanUp() {
		submitBrewer();
		submitEfficiency();
		submitDate();
		submitAttenuation();
		submitStyle();
		submitOG();
		submitYeast();
		submitFG();
	}

	public void setRecipe(Recipe aRecipe) {
		myRecipe = aRecipe;
		populateWidgets();
	}

	private void submitBrewer() {
		if (myContents.getBrewer().get() != null) {
			myRecipe.setBrewer( myContents.getBrewer().get() );
		}
	}

	private void submitEfficiency() {
		myRecipe.setEfficiency( myContents.getEfficiency().get() );
	}
	
	private void submitDate() {
		if (myContents.getDate().get() != null) {
			SimpleDateFormat df = new SimpleDateFormat();
			try {
				myRecipe.setCreated( df.parse(myContents.getDate().get()) );
			} catch (Exception e) {
				// The date was invalid, so reset the widget
				myContents.getDate().set(df.format(myRecipe.getCreated().getTime()));				
			}
		}
	}
	
	private void submitAttenuation() {
		myRecipe.setAttenuation( myContents.getAttenuation().get() );
	}

	private void submitStyle() {
		if (myContents.getStyle().getItemSelected() != -1) {
			myRecipe.setBrewer( myContents.getStyle().get() );
		}
	}

	private void submitOG() {
		myRecipe.setEstOg(  myContents.getOG().get() );
	}

	private void submitYeast() {
		// TODO Update when yeast is added to Recipe
	}

	private void submitFG() {
		myRecipe.setEstFg( myContents.getFG().get() );
	}
	

	
}
