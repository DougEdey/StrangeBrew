/*
 * Created on Oct 5, 2004
 *
 * This is a placeholder test class to play with the various other classes
 * and produce output.
 */

package strangebrew;

/**
 * @author aavis
 *
 *
 */

import strangebrew.ui.core.*;
import strangebrew.ui.swt.*;

public class TestUI {

	public static void main(String[] args) {
		Recipe myRecipe = new Recipe();

		if (args.length != 1) {
			System.err.println("Usage: filename or --gui");
			System.exit(1);
		} else if (args[0].equals("--gui")) {
			View view = new SWTMainView();
			view.init();
			MainController controller = new MainController(view, myRecipe);
			controller.execute();
		} else {
			// this is copied from the ImportXml main method
			// import an xml file, and write a bunch of stuff about it
			// Dont't know how to get the recipe returned from this thing, though
			
			// Use an instance of the ImportXml as the SAX event handler
			ImportXml imp = new ImportXml(args[0]);
			myRecipe = imp.handler.getRecipe();
			
		}
		
		myRecipe.testRecipe();		

}
	

}
