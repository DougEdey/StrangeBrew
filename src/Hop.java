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
public class Hop {
	public String name;
	public double alpha;
	public String country;
	public String type;
	public double costPerU;
	public String form;
	public String add;
	public String description;

	// not sure if these should be here, probably not - add to
	// parallel array in recipe?
	public double amount;
	public String units;
	public int minutes;

	public Hop(String n, double alph, double am, int m) {
		name = n;
		alpha = alph;
		amount = am;
		minutes = m;
		units = "gr";
	}
	
	public Hop(){
		// default constructor
		// TODO: fill in with preferences
	}
	
	public void setName(String n){ name = n; }
	public void setAmount(double a){ amount = a; }
	public void setAlpha(double a){ alpha = a; }
	public void setUnits(String u){ units = u; }
	public void setForm(String f){ form = f; }
	public void setAdd(String a){ add = a; }
	public void setCost(double c){ costPerU = c; }
	public void setDesc(String d){ description = d; }
	
	public String toXML(){
	    StringBuffer sb = new StringBuffer();
	    sb.append( "<HOP>\n" );
	    sb.append( "\t<NAME>"+name+"</NAME>\n" );
	    sb.append( "\t<AMOUNT>"+amount+"</AMOUNT>\n" );
	    sb.append( "\t<UNITS>"+units+"</UNITS>\n" );
	    sb.append( "\t<ALPHA>"+alpha+"</ALPHA>\n" );
	    sb.append( "</HOP>\n" );
	    return sb.toString();
	}
}
