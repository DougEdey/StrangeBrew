/**
 * $Id: Hop.java,v 1.8 2004/10/21 16:44:24 andrew_avis Exp $
 * Created on Oct 5, 2004
 *
 * Base class for hops.  This object doesn't do much except hold data and
 * get/set data.
 */

package strangebrew;

public class Hop extends Ingredient{
	private double alpha;
	private String country;
	private String form;
	private String add;
	private int minutes;
	private double storage;

	// Constructors:
	
	public Hop(){
		// default constructor
		// TODO: fill in with preferences
	}
	
	// get methods:
	public int getMinutes(){ return minutes; }
	public double getAlpha(){ return alpha; }

	
	// Setter methods:
	public void setAlpha(double a){ alpha = a; }
	public void setForm(String f){ form = f; }
	public void setAdd(String a){ add = a; }
	public void setMinutes(int m){ minutes = m; }
	public void setStorage(double s){ storage = s; }	

	
	public String toXML(){
	    StringBuffer sb = new StringBuffer();
	    sb.append( "    <ITEM>\n" );
	    sb.append( "      <HOP>"+getName()+"</HOP>\n" );
	    sb.append( "      <AMOUNT>"+getAmountAs(getUnits())+"</AMOUNT>\n" );
	    sb.append( "      <UNITS>"+getUnits()+"</UNITS>\n" );
	    sb.append( "      <ALPHA>"+alpha+"</ALPHA>\n" );
	    sb.append( "      <DATE>"+getDate()+"</date>\n" );
	    sb.append( "    </ITEM>\n" );
	    return sb.toString();
	}
}
