/*
 * Created on Oct 5, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

/**
 * @author aavis
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */


public class TestUI {

	public static void main(String[] args) {

   
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