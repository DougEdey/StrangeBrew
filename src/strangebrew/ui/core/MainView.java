/*
 * $Id: MainView.java,v 1.4 2004/10/21 02:04:28 tangent_ Exp $
 * Created on Oct 14, 2004
 */

package strangebrew.ui.core;

/**
 * @author mike
 *
 */
public abstract class MainView extends View {

	public abstract MenuView getMenuView();
	public abstract RecipeNavigationView getRecipeNavigationView();
	public abstract RecipeDetailsView getRecipeDetailsView(String title);
	
}
