package strangebrew.ui.swt;


import strangebrew.ui.core.*;
import strangebrew.ui.core.widgets.NumberInput;
import strangebrew.ui.core.widgets.TextInput;
import strangebrew.ui.core.widgets.TextOutput;
import strangebrew.ui.swt.widgets.SWTNumberInput;
import strangebrew.ui.swt.widgets.SWTTextInput;
import strangebrew.ui.swt.widgets.SWTTextOutput;

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
	SWTTextOutput myAlcoholLabel;
	SWTTextOutput myAlcohol;
	SWTTextOutput myAlcoholPostfix;

	public SWTRecipeDetailsView(Composite container) {
		myContainer = container;
	}
	
	public void init() {
		myBrewerLabel = new SWTTextOutput();
		myBrewerLabel.init(myContainer);
		myBrewer = new SWTTextInput(myController);
		myBrewer.init(myContainer);
		myEfficiencyLabel = new SWTTextOutput();
		myEfficiencyLabel.init(myContainer);
		myEfficiency = new SWTNumberInput(myController);
		myEfficiency.init(myContainer);
		myAlcoholLabel = new SWTTextOutput();
		myAlcoholLabel.init(myContainer);
		myAlcohol = new SWTTextOutput();
		myAlcohol.init(myContainer);
		myAlcoholPostfix = new SWTTextOutput();
		myAlcoholPostfix.init(myContainer);
		
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
		attach(myBrewerLabel.getWidget(), null, 0, 0, 0);
		attach(myBrewer.getWidget(), myBrewerLabel.getWidget(), 
				200, 14, 5);
		attach(myEfficiencyLabel.getWidget(), myBrewer.getWidget(), 
				0, 0, 10);
        attach(myEfficiency.getWidget(), myEfficiencyLabel.getWidget(), 
        		25, 14, 5);		
		attach(myAlcoholLabel.getWidget(), myEfficiency.getWidget(), 
				0, 0, 10);
        attach(myAlcohol.getWidget(), myAlcoholLabel.getWidget(), 
        		25, 14, 5);		
		attach(myAlcoholPostfix.getWidget(), myAlcohol.getWidget(), 
				0, 0, 5);
		myContainer.layout();
		myContainer.pack();
	}
	
	public void display() {
		// Not needed in SWT
	}
	
	public void dispose() {
        // @TODO Figure out who should dispose myContainer
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
	
	public TextOutput getAlcoholLabel() {
		return myAlcoholLabel;
	}

	public TextOutput getAlcohol() {
		return myAlcohol;
	}

	public TextOutput getAlcoholPostfix() {
		return myAlcoholPostfix;
	}
	
}
