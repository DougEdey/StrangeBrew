/**
 * $ld$
 * Created on Oct 5, 2004
 *
 * Base class for hops.  This object doesn't do much except hold data and
 * get/set data.
 */

package strangebrew;

public class Hop {
	private String name;
	private double alpha;
	private String country;
	private String type;
	private double costPerU;
	private String form;
	private String add;
	private String description;

	// Recipe-specific values.  Not sure if they should be here.
	private Quantity amount = new Quantity();
	private int minutes;

	// Constructors:
	public Hop(String n, double alph, double am, int m) {
		name = n;
		alpha = alph;
		amount.setQuantity("grams", "gr", am);
		minutes = m;
	}
	
	public Hop(){
		// default constructor
		// TODO: fill in with preferences
	}
	
	// get methods:
	public int getMinutes(){ return minutes; }
	public double getAmountAs(String s){ return amount.getValueAs(s); }
	public double getAlpha(){ return alpha; }
	public double getCostPerU(){ return costPerU;
	}
	
	
	// Setter methods:
	public void setName(String n){ name = n; }
	public void setAmount(double a){ amount.setQuantity(null, null, a); }
	public void setAlpha(double a){ alpha = a; }
	public void setUnits(String u){ amount.setQuantity( null, u, 0); }
	public void setForm(String f){ form = f; }
	public void setAdd(String a){ add = a; }
	public void setCost(double c){ costPerU = c; }
	public void setDesc(String d){ description = d; }
	public void setMinutes(int m){ minutes = m; }
	
	/**
	 * Handles a string of the form "d u", where d is a double
	 * amount, and u is a string of units.  For importing the
	 * quantity attribute from QBrew xml.
	 * @param a
	 */
	public void setAmountAndUnits(String a){
		int i = a.indexOf(" ");
		String d = a.substring(0,i);
		String u = a.substring(i);
		amount.setQuantity(null, u.trim(), Double.parseDouble(d.trim()));
	}
	
	public String toXML(){
	    StringBuffer sb = new StringBuffer();
	    sb.append( "    <ITEM>\n" );
	    sb.append( "      <HOP>"+name+"</HOP>\n" );
	    sb.append( "      <AMOUNT>"+amount.getValueAs(amount.getUnits())+"</AMOUNT>\n" );
	    sb.append( "      <UNITS>"+amount.getUnits()+"</UNITS>\n" );
	    sb.append( "      <ALPHA>"+alpha+"</ALPHA>\n" );
	    sb.append( "    </ITEM>\n" );
	    return sb.toString();
	}
}
