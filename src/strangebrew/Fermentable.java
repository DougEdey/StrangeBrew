/**
 * Created on Oct 4, 2004
 * $Id: Fermentable.java,v 1.5 2004/11/12 15:20:00 andrew_avis Exp $
 * @author aavis
 *
 * This is the base malt class.  It doesn't do much, except hold data
 * and get/set data
 */

package strangebrew;

public class Fermentable extends Ingredient {
	// base data
	private double pppg;
	private double lov;
	private String country;
	private boolean mashed;
	private boolean steeped;
	private double percent;

	// constructors:
	public Fermentable(String n, double p, double l, double a, String u) {
		setName(n);
		pppg = p;
		lov = l;
		setAmount(a);
		setUnits(u);
		mashed = true;
	}
	
	public Fermentable(){
		// default constructor
		setName("Malt");
		pppg = 0;
		lov = 0;
		setAmount(1.0);
		setUnits("pounds");
		mashed = true;
			// I want to get options working sometime,
			// but can't figure out how:
			// myOptions.getProperty("optMaltUnits");
			
	}
	
	// getter methods:
	public double getLov(){ return lov; }
	public boolean getMashed(){ return mashed; }
	public double getPercent() { return percent; }
	public double getPppg(){ return pppg; }
		
	// setter methods:	
	public void setLov(double l){ lov = l; }
	public void setMash(boolean m){ mashed = m; }
	public void setPercent(double p){ percent = p; }
	public void setPppg(double p){ pppg = p; }
	public void setSteep(boolean s){ steeped = s; }
	
	
	// Need to add the spaces and type attributes to make this
	// backwards-compatible with SB1.8:
	public String toXML(){
	    StringBuffer sb = new StringBuffer();
	    sb.append( "    <ITEM>\n" );
	    sb.append( "      <MALT>"+getName()+"</MALT>\n" );
	    sb.append( "      <AMOUNT>"+getAmountAs(getUnits())+"</AMOUNT>\n" );
	    sb.append( "      <UNITS>"+getUnits()+"</UNITS>\n" );
	    sb.append( "      <POINTS>"+pppg+"</POINTS>\n" );
	    sb.append( "      <MASHED>"+mashed+"</MASHED>\n" );
	    sb.append( "      <STEEPED>"+steeped+"</STEEPED>\n" );
	    sb.append( "    </ITEM>\n" );
	    return sb.toString();
	}

}
