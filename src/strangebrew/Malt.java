/*
 * Created on Oct 4, 2004
 * @author aavis
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

package strangebrew;

public class Malt {
	// base data
	public String name;
	public double pppg;
	public double lov;
	public String maltster;
	public String country;
	public String type;
	public boolean mashed;
	public double costPerU;
	public String description;

	// recipe data
	// not sure if these should be here, probably not - add to
	// parallel array in recipe?
	public double amount;
	public String units;
	public double percent;


	public Malt(String n, double p, double l, double a, String u) {
		name = n;
		pppg = p;
		lov = l;
		amount = a;
		units = u;
		mashed = true;
	}
	
	public Malt(){
		// default constructor
		name = "Malt";
		pppg = 0;
		lov = 0;
		amount = 1.0;
		units =  "lbs";
		mashed = true;
			// I want to get options working sometime,
			// but can't figure out how:
			// myOptions.getProperty("optMaltUnits");
			
	}
	
	public void setName(String n){ name = n; }
	public void setAmount(double a){ amount = a; }
	public void setPppg(double p){ pppg = p; }
	public void setCost(double c){ costPerU = c; }
	public void setCost(String c){
		CharSequence cs = "$";
		if (c.contains(cs)){
			
			String newC = c.substring(1, c.length());
			costPerU = Double.parseDouble(newC);
		}
		else
			costPerU = Double.parseDouble(c);
		
	}
	public void setUnits(String u) { units = u; }
	public void setLov(double l){ lov = l; }
	public void setDesc(String d){ description = d; }
	
	public String toXML(){
	    StringBuffer sb = new StringBuffer();
	    sb.append( "    <ITEM>\n" );
	    sb.append( "      <MALT>"+name+"</MALT>\n" );
	    sb.append( "      <AMOUNT>"+amount+"</AMOUNT>\n" );
	    sb.append( "      <UNITS>"+units+"</UNITS>\n" );
	    sb.append( "      <POINTS>"+pppg+"</POINTS>\n" );
	    sb.append( "    </ITEM>\n" );
	    return sb.toString();
	}
	
	


}
