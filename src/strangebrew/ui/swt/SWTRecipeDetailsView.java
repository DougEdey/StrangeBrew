package strangebrew.ui.swt;


import strangebrew.ui.core.*;
import strangebrew.ui.core.widgets.*;

import strangebrew.ui.swt.widgets.*;


import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;

/**
 * $Id: SWTRecipeDetailsView.java,v 1.22 2004/10/21 01:48:12 tangent_ Exp $
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
	SWTTextOutput myMashLabel;
	SWTCheckBox myMash;
	SWTTextOutput myAttenuationLabel;
	SWTNumberInput myAttenuation;
	SWTTextOutput myIBULabel;
	SWTTextOutput myIBU;
	SWTTextOutput myIBUPostfix;
	SWTTextOutput myStyleLabel;
	SWTDropdown myStyle;
	SWTTextOutput myOGLabel;
	SWTNumberInput myOG;
	SWTTextOutput myColourLabel;
	SWTTextOutput myColour;
	SWTTextOutput myColourPostfix;
	SWTTextOutput myYeastLabel;
	SWTDropdown myYeast;
	SWTTextOutput myFGLabel;
	SWTNumberInput myFG;


	public SWTRecipeDetailsView(Composite container) {
		myContainer = container;
	}
	
	public void init() {
		myBrewerLabel = new SWTTextOutput(myContainer);
		myBrewer = new SWTTextInput(myController, myContainer);
		myEfficiencyLabel = new SWTTextOutput(myContainer);
		myEfficiency = new SWTNumberInput(myController, myContainer);
		myAlcoholLabel = new SWTTextOutput(myContainer);
		myAlcohol = new SWTTextOutput(myContainer);
		myAlcoholPostfix = new SWTTextOutput(myContainer);
		myDateLabel = new SWTTextOutput(myContainer);
		myDate = new SWTTextInput(myController, myContainer);
		myMashLabel = new SWTTextOutput(myContainer);
		myMash = new SWTCheckBox(myController, myContainer);
		myAttenuationLabel = new SWTTextOutput(myContainer);
		myAttenuation = new SWTNumberInput(myController, myContainer);
		myIBULabel = new SWTTextOutput(myContainer);
		myIBU = new SWTTextOutput(myContainer);
		myIBUPostfix = new SWTTextOutput(myContainer);
		myStyleLabel = new SWTTextOutput(myContainer);
		myStyle = new SWTDropdown(myController, myContainer);
		myOGLabel = new SWTTextOutput(myContainer);
		myOG = new SWTNumberInput(myController, myContainer);
		myColourLabel = new SWTTextOutput(myContainer);
		myColour = new SWTTextOutput(myContainer);
		myColourPostfix = new SWTTextOutput(myContainer);
		myYeastLabel = new SWTTextOutput(myContainer);
		myYeast = new SWTDropdown(myController, myContainer);
		myFGLabel = new SWTTextOutput(myContainer);
		myFG = new SWTNumberInput(myController, myContainer);
		
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
	
	public void layout() {
		setSize(myBrewer, 0, 200);
		setSize(myAlcohol, 0, 50);
		setSize(myEfficiency, 0, 50);
		setSize(myDate, 0, 120);
		setSize(myAttenuation, 0, 50);
		setSize(myIBU, 0, 50);
		setSize(myStyle, 0, 200);
		setSize(myOG, 0, 50);
		setSize(myColour, 0, 50);
		setSize(myYeast, 0, 200);
		setSize(myFG, 0, 50);
		
		SWTFormWidget rowAnchor;
		
		rowAnchor = myBrewer;
		
		align(myBrewerLabel, SWT.LEFT, null, SWT.NONE, 5);
		align(myBrewerLabel, SWT.BOTTOM, myBrewer, SWT.BOTTOM, 0);
		align(myBrewer, SWT.LEFT, myBrewerLabel, SWT.RIGHT, 5);
		align(myBrewer, SWT.TOP, null, SWT.NONE, 5);
		
		align(myEfficiencyLabel, SWT.LEFT, myBrewer, SWT.RIGHT, 20);
		align(myEfficiencyLabel, SWT.BOTTOM, myEfficiency, SWT.BOTTOM, 0);
		align(myEfficiency, SWT.LEFT, myEfficiencyLabel, SWT.RIGHT, 5);
        align(myEfficiency, SWT.BOTTOM, rowAnchor, SWT.BOTTOM, 0);

        align(myAlcoholLabel, SWT.LEFT, myEfficiency, SWT.RIGHT, 10);
		align(myAlcoholLabel, SWT.BOTTOM, myAlcohol, SWT.BOTTOM, 0);
        align(myAlcohol, SWT.LEFT, myAlcoholLabel, SWT.RIGHT, 5);
        align(myAlcohol, SWT.BOTTOM, rowAnchor, SWT.BOTTOM, 0);
		align(myAlcoholPostfix, SWT.LEFT, myAlcohol, SWT.RIGHT, 5);
		align(myAlcoholPostfix, SWT.BOTTOM, myAlcohol, SWT.BOTTOM, 0);

		rowAnchor = myDate;
		
		align(myDateLabel, SWT.RIGHT, myDate, SWT.LEFT, -5);
		align(myDateLabel, SWT.BOTTOM, myDate, SWT.BOTTOM, 0);
		align(myDate, SWT.LEFT, myBrewer, SWT.LEFT, 0);
		align(myDate, SWT.TOP, myBrewer, SWT.BOTTOM, 5);
		
		align(myMashLabel, SWT.RIGHT, myMash, SWT.LEFT, -10);
		align(myMashLabel, SWT.BOTTOM, myMash, SWT.BOTTOM, 0);
		align(myMash, SWT.RIGHT, myBrewer, SWT.RIGHT, 10);
		align(myMash, SWT.BOTTOM, rowAnchor, SWT.BOTTOM, 0);

		align(myAttenuationLabel, SWT.RIGHT, myEfficiencyLabel, SWT.RIGHT, 0);
		align(myAttenuationLabel, SWT.BOTTOM, myAttenuation, SWT.BOTTOM, 0);
		align(myAttenuation, SWT.LEFT, myEfficiency, SWT.LEFT, 0);
		align(myAttenuation, SWT.BOTTOM, rowAnchor, SWT.BOTTOM, 0);

		align(myIBULabel, SWT.RIGHT, myAlcoholLabel, SWT.RIGHT, 0);
		align(myIBULabel, SWT.BOTTOM, myIBU, SWT.BOTTOM, 0);
		align(myIBU, SWT.LEFT, myAlcohol, SWT.LEFT, 0);
		align(myIBU, SWT.BOTTOM, rowAnchor, SWT.BOTTOM, 0);
		align(myIBUPostfix, SWT.LEFT, myAlcoholPostfix, SWT.LEFT, 0);
		align(myIBUPostfix, SWT.BOTTOM, myIBU, SWT.BOTTOM, 0);

		rowAnchor = myStyle;
		
		align(myStyleLabel, SWT.RIGHT, myStyle, SWT.LEFT, -5);
		align(myStyleLabel, SWT.BOTTOM, myStyle, SWT.BOTTOM, 0);
		align(myStyle, SWT.LEFT, myDate, SWT.LEFT, 0);
		align(myStyle, SWT.TOP, myDate, SWT.BOTTOM, 5);

		align(myOGLabel, SWT.RIGHT, myAttenuationLabel, SWT.RIGHT, 0);
		align(myOGLabel, SWT.BOTTOM, myOG, SWT.BOTTOM, 0);
		align(myOG, SWT.LEFT, myAttenuation, SWT.LEFT, 0);
		align(myOG, SWT.BOTTOM, rowAnchor, SWT.BOTTOM, 0);

		align(myColourLabel, SWT.RIGHT, myIBULabel, SWT.RIGHT, 0);
		align(myColourLabel, SWT.BOTTOM, myColour, SWT.BOTTOM, 0);
		align(myColour, SWT.LEFT, myIBU, SWT.LEFT, 0);
		align(myColour, SWT.BOTTOM, rowAnchor, SWT.BOTTOM, 0);
		align(myColourPostfix, SWT.LEFT, myIBUPostfix, SWT.LEFT, 0);
		align(myColourPostfix, SWT.BOTTOM, myColour, SWT.BOTTOM, 0);

		rowAnchor = myYeast;
		
		align(myYeastLabel, SWT.RIGHT, myYeast, SWT.LEFT, -5);
		align(myYeastLabel, SWT.BOTTOM, myYeast, SWT.BOTTOM, 0);
		align(myYeast, SWT.LEFT, myStyle, SWT.LEFT, 0);
		align(myYeast, SWT.TOP, myStyle, SWT.BOTTOM, 5);
		
		align(myFGLabel, SWT.RIGHT, myOGLabel, SWT.RIGHT, 0);
		align(myFGLabel, SWT.BOTTOM, myFG, SWT.BOTTOM, 0);
		align(myFG, SWT.LEFT, myOG, SWT.LEFT, 0);
		align(myFG, SWT.BOTTOM, rowAnchor, SWT.BOTTOM, 0);
		
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
	
	public CheckBox getMash() {
		return myMash;
	}
	
	public TextOutput getMashLabel() {
		return myMashLabel;
	}
	
	public TextOutput getAttenuationLabel() {
		return myAttenuationLabel;
	}
	
	public NumberInput getAttenuation() {
		return myAttenuation;
	}

	public TextOutput getIBULabel() {
		return myIBULabel;
	}

	public TextOutput getIBU() {
		return myIBU;
	}

	public TextOutput getIBUPostfix() {
		return myIBUPostfix;
	}

	public TextOutput getStyleLabel() {
		return myStyleLabel;
	}
	
	public Dropdown getStyle() {
		return myStyle;
	}
	
	public TextOutput getOGLabel() {
		return myOGLabel;
	}

	public NumberInput getOG() {
		return myOG;
	}

	public TextOutput getColourLabel() {
		return myColourLabel;
	}

	public TextOutput getColour() {
		return myColour;
	}

	public TextOutput getColourPostfix() {
		return myColourPostfix;
	}

	public TextOutput getYeastLabel() {
		return myYeastLabel;
	}
	
	public Dropdown getYeast() {
		return myYeast;
	}

	public TextOutput getFGLabel() {
		return myFGLabel;
	}

	public NumberInput getFG() {
		return myFG;
	}


}
