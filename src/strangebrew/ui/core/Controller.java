package strangebrew.ui.core;

import strangebrew.Recipe;

public abstract class Controller {

	protected View myView;
	protected Recipe myRecipe;

	
	public Controller(View aView, Recipe aRecipe) {
		myView = aView;
		myRecipe = aRecipe;
		aView.setController(this);
	}
	public abstract void init();
	public abstract void dispose();
	public abstract void execute();
	public abstract void cleanUp();
	
}
