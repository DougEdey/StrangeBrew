package strangebrew.ui.swt;


import strangebrew.ui.core.*;

import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;

/**
 * @author mike
 *
 *
 */
public class SWTRecipeDetailsView extends RecipeDetailsView {
	Composite myContainer;
	FormLayout myLayout;
	SWTTextOutput myBrewerLabel;
	SWTTextInput myBrewer;

	public SWTRecipeDetailsView(Factory aFactory, Composite container) {
		myFactory = aFactory;
		myContainer = container;
	}
	
	public void init() {
		myBrewerLabel = new SWTTextOutput();
		myBrewerLabel.init(myContainer);
		myBrewer = new SWTTextInput(myController);
		myBrewer.init(myContainer);
		
		myLayout = new FormLayout();
		myContainer.setLayout(myLayout);
		
		FormData data1 = new FormData();
		myBrewerLabel.getWidget().setLayoutData(data1);

		FormData data2 = new FormData();
		data2.left = new FormAttachment(myBrewerLabel.getWidget());
		Text text = myBrewer.getWidget();
		text.setLayoutData(data2);
	}
	
	public void layout() {
		myContainer.layout();
		myContainer.pack();
	}
	
	public void display() {
		// Not needed in SWT
	}
	
	public void dispose() {
        // @TODO Figure out who should dispose myContainer
	}
	
	public Factory createChildFactory(String aTitle) {
		// This view has no children
		return null;
	}
	
	public TextOutput getBrewerLabel() {
		return myBrewerLabel;
	}
	
	public TextInput getBrewer() {
		return myBrewer;
	}

}
