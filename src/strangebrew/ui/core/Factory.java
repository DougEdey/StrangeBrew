package strangebrew.ui.core;

/**
 * Abstract Factory for creating widgets.
 * There is one function for each type of widget you might want to create.
 * @author mike
 */
public abstract class Factory {

	public abstract RecipeDetailsView newRecipeDetailsView();
}
