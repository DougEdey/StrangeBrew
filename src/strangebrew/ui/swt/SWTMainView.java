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
	FillLayout myLayout;
	TabFolder myFolder;

	public SWTMainView() {
		myDisplay = new Display();
		
		// This is supposed to set the class type of the app (for X windows)
		// but as far as I can tell, it doesn't work...
		// @TODO Fix X Class type
		Display.setAppName("StrangeBrew.StrangeBrew");
		
		myShell = new Shell(myDisplay);
	}
	
	public void init() {
		myLayout = new FillLayout();
 		myLayout.type = SWT.VERTICAL;
 		myShell.setLayout(myLayout);
 		
 		// @TODO Create a separate view/controller for the TabFolder
		myFolder = new TabFolder(myShell, SWT.NONE);
	}
	
	public void layout() {
		myShell.layout();
		myShell.pack();
	}

    public void dispose() {
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

	public RecipeDetailsView getRecipeDetailsView() {
		TabItem details = new TabItem(myFolder, SWT.NONE);
		
		details.setText("Recipe Details");
		Group group = new Group(myFolder, SWT.NONE);
		details.setControl(group);
		return new SWTRecipeDetailsView(group);
	}
	
}

