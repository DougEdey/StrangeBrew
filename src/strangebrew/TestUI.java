/*
 * Created on Oct 5, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

package strangebrew;

/**
 * @author aavis
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

import strangebrew.ui.core.*;
import strangebrew.ui.swt.*;

public class TestUI {

	public static void main(String[] args) {

		if (args.length > 0) {
			if (args[0].equals("--gui")) {
	            View view = new SWTMainView();
	            view.init();
	            MainController controller = new MainController(view);
	            controller.execute();
			}
		}
		
		Recipe myRecipe = new Recipe();
		
		myRecipe.calcMaltTotals();
		myRecipe.calcHopsTotals();
		System.out.print("Recipe totals: \n");
		System.out.print("estOG: " + myRecipe.estOg + "\n");
		System.out.print("estFG: " + myRecipe.estFg + "\n");
		System.out.print("srm: " + myRecipe.srm + "\n");
		System.out.print("%alc: " + myRecipe.alcohol + "\n");
		System.out.print("IBUs: " + myRecipe.ibu + "\n");
	}
	

}
