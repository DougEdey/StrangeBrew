package strangebrew.ui.swt;


import strangebrew.ui.core.*;
import strangebrew.ui.core.widgets.*;

import strangebrew.ui.swt.widgets.*;


import org.eclipse.swt.*;
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
	SWTTextOutput myDateLabel;
	SWTTextInput myDate;

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
		myDateLabel = new SWTTextOutput();
		myDateLabel.init(myContainer);
		myDate = new SWTTextInput(myController);
		myDate.init(myContainer);
		
		myLayout = new FormLayout();
		myLayout.marginHeight = 3;
		myLayout.marginWidth = 3;
		myContainer.setLayout(myLayout);		
	}
	
	private void setSize(SWTFormWidget control, int height, int width) {
		FormData data = control.getFormData();
		if (width != 0) {
			data.width = width;
		}
		if (height != 0) {
			data.height = height;
		}
		control.getControl().setLayoutData(data);
	}
	
	private void align(SWTFormWidget control, int direction, 
			SWTFormWidget target, int alignment, int offset) {

		FormData data = control.getFormData();
		FormAttachment fa;

		if (target != null) {
			fa = new FormAttachment(target.getControl(), offset, alignment);
		} else {
			fa = new FormAttachment(0, offset);
		}
		
		switch (direction) {
		case SWT.LEFT:
			data.left = fa;
			break;
		case SWT.BOTTOM:
			data.bottom = fa;
			break;
		case SWT.RIGHT:
			data.right = fa;
			break;
		case SWT.TOP:
			data.top = fa;
			break;
		} 
		control.getControl().setLayoutData(data);
	}
	
	private void attach(SWTFormWidget control, SWTFormWidget target,
			int offset) {
		align(control, SWT.LEFT, target, SWT.RIGHT, offset);
		align(control, SWT.BOTTOM, target, SWT.BOTTOM, 0);
	}

	public void layout() {
		setSize(myBrewer, 0, 200);
		setSize(myAlcohol, 0, 50);
		setSize(myEfficiency, 0, 50);
		setSize(myDate, 0, 200);
		
		align(myBrewerLabel, SWT.LEFT, null, SWT.NONE, 5);
		align(myBrewerLabel, SWT.TOP, null, SWT.NONE, 5);
		attach(myBrewer, myBrewerLabel, 5);
		attach(myEfficiencyLabel, myBrewer, 10);
        attach(myEfficiency, myEfficiencyLabel, 5);		
		attach(myAlcoholLabel, myEfficiency, 10);
        attach(myAlcohol, myAlcoholLabel, 5);		
		attach(myAlcoholPostfix, myAlcohol, 5);
		align(myDateLabel, SWT.RIGHT, myDate, SWT.LEFT, -5);
		align(myDateLabel, SWT.BOTTOM, myDate, SWT.BOTTOM, 0);
		align(myDate, SWT.TOP, myBrewer, SWT.BOTTOM, 5);
		align(myDate, SWT.LEFT, myBrewer, SWT.LEFT, 0);
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
	
	public TextOutput getDateLabel() {
		return myDateLabel;
	}
	
	public TextInput getDate() {
		return myDate;
	}
	
}
