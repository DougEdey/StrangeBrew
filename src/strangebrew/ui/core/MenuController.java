/*
 * Created on Oct 15, 2004
 */
package strangebrew.ui.core;

import strangebrew.ImportXml;
import strangebrew.Recipe;

/**
 * @author mike
 *
 */
public class MenuController extends Controller {

	MenuView myContents;
	MainController myParent;
	
	public MenuController(MenuView aView, MainController parent) {
		super(aView);
		myContents = aView;
		myParent = parent;
	}

	public void init() {
		myView.init();

		myContents.getFileMenu().set("File");
		myContents.getOpenItem().set("Open");
		myContents.getQuitItem().set("Quit");
		myView.layout();
	}

	public void dispose() {
		myView.dispose();
	}
	
	public void execute() {
      if (myContents.getQuitItem().isSelected()) {
      	myContents.quit();
      }
      if (myContents.getOpenItem().isSelected()) {
      	String myFile = myContents.getOpenFilename();
      	if (myFile != "/") {
      		ImportXml imp = new ImportXml(myFile);
			Recipe recipe = imp.handler.getRecipe();
			myParent.setRecipe(recipe);
		}
      }
	}
	
	public void cleanUp() {
		// Nothing to do yet
	}
	
	public void setRecipe(Recipe aRecipe) {
		myRecipe = aRecipe;
	}
}
