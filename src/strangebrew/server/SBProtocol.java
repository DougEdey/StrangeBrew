/**
 * Created on Nov 25, 2004
 * @author aavis
 */
package strangebrew.server;

import strangebrew.*;

public class SBProtocol {
	// states:
	private static final int WAITING = 0;
	private static final int CONNECTED = 1;

    private int state = CONNECTED;

    private Recipe myRecipe = null;
    
    public SBProtocol(){
    
//  Import an xml recipe:
	ImportXml imp = new ImportXml("src/strangebrew/data/test_rec.xml");
	myRecipe = imp.handler.getRecipe();		
	myRecipe.calcMaltTotals();
	myRecipe.calcHopsTotals();			
	myRecipe.mash.setMaltWeight(myRecipe.getTotalMashLbs());
	myRecipe.mash.calcMashSchedule();
	System.out.print(myRecipe.toText());
    }

    

    public String processInput(String theInput) {
        String theOutput = "";       

        if (state == WAITING) {      
        }
        
        else if (theInput == "bye"){
        	theOutput = "Bye\r\n";
        }
        else if (theInput.equals("getname")){
        	theOutput=myRecipe.getName();
        }
        else if (theInput.equals("getstyle")){
        	theOutput=myRecipe.getStyle();
        }
        else if (theInput.equals("getstats")){
        	theOutput=myRecipe.getEstOg()+","+myRecipe.getEstFg();
        }
        return theOutput;
    }
}
