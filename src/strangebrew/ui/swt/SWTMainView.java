package strangebrew.ui.swt;

import strangebrew.ui.core.*;

import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.*;

/**
 * SWT version of the Controller.
 * This class should only contain functions that are used in the template
 * method run.  Additionally, it may contain functions that are used
 * privately.
 * @author mike
 * @see strangebrew.ui.core.View#run
 *
 */
public class SWTMainView extends MainView {

	Display myDisplay;
	Shell myShell;
	FormLayout myLayout;
	TabFolder myFolder;
	Group myRecipeNavigationGroup;
	Group myMenuGroup;

	public SWTMainView() {
		myDisplay = new Display();
		
		// This is supposed to set the class type of the app (for X windows)
		// but as far as I can tell, it doesn't work...
		// @TODO Fix X Class type
		Display.setAppName("StrangeBrew.StrangeBrew");
		
		myShell = new Shell(myDisplay);
		myShell.setText("StrangeBrew");
	}
	
	public void init() {
		myLayout = new FormLayout();
 		myShell.setLayout(myLayout);

 		myRecipeNavigationGroup = new Group(myShell, SWT.NONE);
 		// @TODO Create a separate view/controller for the TabFolder
		myFolder = new TabFolder(myShell, SWT.NONE);
		
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
			data.top = new FormAttachment(target, offset);
		}
		control.setLayoutData(data);
	}
	
	public void layout() {
//		attach(myRecipeNavigationGroup, null, 0, 0, 0);
		attach(myFolder, myRecipeNavigationGroup, 0, 0, 0);
		myRecipeNavigationGroup.pack();
		myFolder.layout();
		myFolder.pack();
		myShell.layout();
		myShell.pack();
	}

    public void dispose() {
    	myRecipeNavigationGroup.dispose();
		myFolder.dispose();	
		myDisplay.dispose();	
	}
    
	public void display() {
		myShell.open();
		// SWT needs to explicitly run the event loop
		runEventLoop();
	}

	private void runEventLoop() {
		while(!myShell.isDisposed()) {
			if(!myDisplay.readAndDispatch()) {
				myDisplay.sleep();
			}
		}
	}
	
	public MenuView getMenuView() {
		return new SWTMenuView(myShell);
	}
	
	public RecipeNavigationView getRecipeNavigationView() {
		return new SWTRecipeNavigationView(myRecipeNavigationGroup);
	}

	public RecipeDetailsView getRecipeDetailsView(String title) {
		TabItem details = new TabItem(myFolder, SWT.NONE);
		
		details.setText(title);
		Group group = new Group(myFolder, SWT.NONE);
		details.setControl(group);
		return new SWTRecipeDetailsView(group);
	}
	
}

