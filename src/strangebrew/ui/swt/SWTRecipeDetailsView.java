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
	SWTTextOutput myEfficiencyLabel;
	SWTNumberInput myEfficiency;

	public SWTRecipeDetailsView(Factory aFactory, Composite container) {
		myFactory = aFactory;
		myContainer = container;
	}
	
	public void init() {
		myBrewerLabel = new SWTTextOutput();
		myBrewerLabel.init(myContainer);
		myBrewer = new SWTTextInput(myController);
		myBrewer.init(myContainer);
		myEfficiencyLabel = new SWTTextOutput();
		myEfficiencyLabel.init(myContainer);
		myEfficiency = new SWTNumberInput();
		myEfficiency.init(myContainer);
		
		myLayout = new FormLayout();
		myLayout.marginHeight = 3;
		myLayout.marginWidth = 3;
		myContainer.setLayout(myLayout);
		
		FormData data1 = new FormData();
		myBrewerLabel.getWidget().setLayoutData(data1);

		FormData data2 = new FormData();
		data2.width = 200;
		data2.height = 10;
		data2.left = new FormAttachment(myBrewerLabel.getWidget(), 5);
		myBrewer.getWidget().setLayoutData(data2);
		
		FormData data3 = new FormData();
		data3.left = new FormAttachment(myBrewer.getWidget(), 10);
		myEfficiencyLabel.getWidget().setLayoutData(data3);
		
		FormData data4 = new FormData();
		data4.width = 25;
		data4.height = 10;
		data4.left = new FormAttachment(myEfficiencyLabel.getWidget(), 5);
		myEfficiency.getWidget().setLayoutData(data4);
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
	
	public TextOutput getEfficiencyLabel() {
		return myEfficiencyLabel;
	}

	public NumberInput getEfficiency() {
		return myEfficiency;
	}

}
