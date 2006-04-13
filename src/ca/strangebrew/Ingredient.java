package ca.strangebrew;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * $Id: Ingredient.java,v 1.2 2006/04/13 20:00:49 andrew_avis Exp $
 * Created on Oct 21, 2004
 * @author aavis
 *
 * Base class for all ingredients.  Dunno why I didn't do this
 * in the first place.
 */
public class Ingredient {
	private String name="";
	private double costPerU;
	private String description;
	private Quantity amount = new Quantity();
	private Date dateBought;
	private String type;
	
	// Get methods:
	public double getCostPerU(){ return costPerU; }
	public String getName(){ return name; }
	public String getDescription(){ return description; }
	public double getAmountAs(String s){ return amount.getValueAs(s); }
	public String getUnits(){ return amount.getUnits(); }
	public String getUnitsAbrv(){ return amount.getAbrv(); }
	public Date getDate(){ return dateBought; }
	public String getType(){ return type; }
	
	
	// Setter methods:
	public void setName(String n){ name = n; }
	public void setAmount(double a){ amount.setQuantity(null, null, a); }
	public void setAmountAs(double a, String u) {
		double converted = Quantity.convertUnit(u, amount.getUnits(), a);
		amount.setQuantity(null, null, converted);
	}
	public void setUnits(String a){ amount.setQuantity( null, a, -1); }
	public void setUnitsFull(String u){ amount.setQuantity( u, null, -1); }
	public void setCost(double c){ costPerU = c; }
	
	public void setCost(String c){
		if (c.substring(0,1).equals("$")) {
			c = c.substring(1, c.length()); // trim leading "$"
		}
		costPerU = Double.parseDouble(c);
	}
	
	public void setDescription(String d){ description = d; }
	public void setType(String t){ type = t; }
	public void setDate(String d){ 
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		try{
		dateBought = df.parse(d);
		}catch (ParseException p){}
		
	}
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
	    sb.append( "      <HOP>"+getName()+"</HOP>\n" );
	    sb.append( "      <AMOUNT>"+getAmountAs(getUnits())+"</AMOUNT>\n" );
	    sb.append( "      <UNITS>"+getUnits()+"</UNITS>\n" );
	    sb.append( "      <DATE>"+getDate()+"</date>\n" );
	    sb.append( "      <DESCR>"+getDescription()+"</DESCR>\n" );
	    sb.append( "    </ITEM>\n" );
	    return sb.toString();
	}
	
	// implement to support comboboxes in Swing:
	public String toString(){
		return name;
	}
	
	

}
