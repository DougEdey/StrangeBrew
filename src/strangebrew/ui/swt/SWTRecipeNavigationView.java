/*
 * Created on Oct 14, 2004
 */
package strangebrew.ui.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import strangebrew.ui.core.RecipeNavigationView;

/**
 * @author mike
 *
 */
public class SWTRecipeNavigationView extends RecipeNavigationView {
	Composite myContainer;
	SWTTextInput myRecipeName;
	FillLayout myLayout;

	public SWTRecipeNavigationView(Composite container) {
		myContainer = container;
	}

	public void init() {
		myRecipeName = new SWTTextInput(myController);
		myRecipeName.init(myContainer);
	}
	
	public void layout() {
		myRecipeName.getWidget().pack();
		myContainer.layout();
		myContainer.pack();
	}
	
	public void display() {
		// Not needed in SWT
	}
	
	public void dispose() {
        // @TODO Figure out who should dispose myContainer
	}

}
