package strangebrew;

/**
 * $Id: Yeast.java,v 1.2 2004/11/18 18:06:18 andrew_avis Exp $
 * @author aavis
 * Created on Oct 21, 2004
 *
 */
public class Yeast extends Ingredient {
	// I'm not sure what else needs to be added to yeast,
	// but if we need to, we can add it here
	
	// should handle defaults here:
	public Yeast(){
		setName("A yeast");
	}
	
	public String toString(){
		return getName();
	}
}
