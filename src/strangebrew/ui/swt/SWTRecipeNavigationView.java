/*
 * $Id: SWTRecipeNavigationView.java,v 1.5 2004/10/21 01:46:55 tangent_ Exp $
 * Created on Oct 14, 2004
 */
package strangebrew.ui.swt;

import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

import strangebrew.ui.core.*;
import strangebrew.ui.core.widgets.TextInput;
import strangebrew.ui.core.widgets.TextOutput;
import strangebrew.ui.swt.widgets.SWTTextInput;
import strangebrew.ui.swt.widgets.SWTTextOutput;

/**
 * @author mike
 *
 */
public class SWTRecipeNavigationView extends RecipeNavigationView {
	Composite myContainer;
	SWTTextInput myRecipeName;
	SWTTextOutput myRecipeNameLabel;
	FormLayout myLayout;

	public SWTRecipeNavigationView(Composite container) {
		myContainer = container;
	}

	public void init() {
		myRecipeNameLabel = new SWTTextOutput(myContainer);
		myRecipeName = new SWTTextInput(myController, myContainer);
		
		myLayout = new FormLayout();
		myLayout.marginHeight = 3;
		myLayout.marginWidth = 3;
		myContainer.setLayout(myLayout);
	}
	
	private void attach(Control control, Control target, 
			int width, int height, int offset) {
		FormData data = new FormData();
		if (width != 0) {
			data.width = width;
		}
		if (height != 0) {
			data.height = height;
		}
		if (target != null) {
			data.left = new FormAttachment(target, offset);
		}
		control.setLayoutData(data);
	}
	
	public void layout() {
		attach(myRecipeNameLabel.getWidget(), null, 0, 0, 0);
		attach(myRecipeName.getWidget(), myRecipeNameLabel.getWidget(),
				200, 0, 5);
		
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
	
	public TextOutput getRecipeNameLabel() {
		return myRecipeNameLabel;
	}
	
	public TextInput getRecipeName() {
		return myRecipeName;
	}

}
