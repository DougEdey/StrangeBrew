/*
 * Created on Oct 5, 2004
 *
 * This is a placeholder test class to play with the various other classes
 * and produce output.
 */

package strangebrew;

/**
 * $Id: TestUI.java,v 1.19 2004/11/15 18:00:07 andrew_avis Exp $
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
			MainView view = new SWTMainView();
			MainController controller = new MainController(view);
			controller.execute();
			myRecipe = controller.getRecipe();
		} else if (args[0].equals("--db")) {
			// test the database loader:
			Database db = new Database();
			db.readStyles("C:/Program Files/eclipse/workspace/StrangeBrew/src/strangebrew/data/malts.csv");
		
		} else {
			// Import an xml recipe:
			ImportXml imp = new ImportXml(args[0]);
			myRecipe = imp.handler.getRecipe();		
			myRecipe.calcMaltTotals();
			myRecipe.calcHopsTotals();
			
			myRecipe.mash.setMaltWeight(myRecipe.getTotalMashLbs());
			myRecipe.mash.calcMashSchedule();
			System.out.print(myRecipe.toXML());
			
		}	
		myRecipe.testRecipe();
	
	}	

}
