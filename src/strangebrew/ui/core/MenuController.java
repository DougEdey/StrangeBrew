/*
 * Created on Oct 15, 2004
 */
package strangebrew.ui.core;

import strangebrew.Recipe;

/**
 * @author mike
 *
 */
public class MenuController extends Controller {

	MenuView myContents;
	
	public MenuController(MenuView aView, Recipe aRecipe) {
		super(aView, aRecipe);
		myContents = aView;
	}

	public void init() {
		myView.init();

		myContents.getFileMenu().set("File");
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
	}
	
}
