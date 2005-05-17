/*
 * $Id: Quantity.java,v 1.9 2005/05/17 20:20:06 andrew_avis Exp $
 * Created on Oct 7, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

package strangebrew;

import java.util.ArrayList;

/**
 * @author aavis
 *
 * This is the class for amounts + units.
 * It's pretty smart, and knows how to convert itself to other units.
 */

public class Quantity {
	
	private String type; // can be vol, weight, or temp
	private String unit; // must match one of the known units
	private String abrv; // ditto
	private double value;

	// why can't we have structs?????
	private class Converter {
		String abrv;
		String unit;
		double toBase;
		
		private Converter(String n, String a, double t) {
			unit = n;
			abrv = a;
			toBase = t;
		}
	}

	Converter volUnits[] =
		{
			new Converter("barrel IMP", "bbl Imp", 0.023129837),
			new Converter("barrel US", "bbl", 0.032258065),
			new Converter("fl. ounces", "oz", 128),
			new Converter("gallons IMP", "gal Imp", 0.8327),
			new Converter("gallons US", "gal", 1),
			new Converter("litres", "l", 3.7854),
			new Converter("millilitres", "ml", 3785.4118),
			new Converter("pint US", "pt", 8),
			new Converter("quart US", "qt", 4)
		};

	Converter weightUnits[] =
		{
			new Converter("grams", "gr", 453.59237),
			new Converter("grams", "g", 453.59237),
			new Converter("kilograms", "kg", 0.45359237),
			new Converter("ounces", "oz", 16),
			new Converter("pounds", "lb", 1),
			new Converter("ton S", "T", 0.0005),
			new Converter("tonne SI", "T SI", 0.000453592)
		};
	
	// Get/Set:
	public void setQuantity(String u, String a, double am){
		if (u!=null && !u.equals("")){
			unit = u;
			type = getTypeFromUnit(u);
			if (a==null) // gotta set the abrv too:
				abrv = getAbrvFromUnit(type, u);
		}		
		if (a!=null && !a.equals("")){
			abrv = a;
			type = getTypeFromUnit(a);
			if (u==null) // gotta set the unit too:
				unit = getUnitFromAbrv(type, a);
		}
		if (am >= 0)
			value = am;
	}
	
	public double getValue(){ return value;	}
	public String getUnits(){ return unit; }
	public double getValueAs(String to){
		double fromBase = 0;
		double toBase = 0;
		int i = 0;
		Converter[] u;
		
		// don't do any work if we're converting to ourselves
		if (to == unit || type == abrv)
			return value;
		
		if (type == "vol")
		  u = volUnits;
		else // assume weight
		  u = weightUnits;

		fromBase = getBaseValue(u, unit);
		toBase = getBaseValue(u, to);
		
		return value * toBase / fromBase;
	}
	

	public void add(double v, String u){
		// convert v from u to current units
		// then add it
		Quantity q = new Quantity();
		q.setQuantity(u, null, v);
		double v2 = q.getValueAs(getUnits());
		value += v2;
	}
	
//	 implement to support comboboxes in Swing:
	public String getName(){
		return unit;
	}
	
	public ArrayList getListofUnits(String type) {
		ArrayList list = new ArrayList();
		int i = 0;
		if (type.equals("weight")) {
			for (i = 0; i < weightUnits.length; i++) 
				list.add(weightUnits[i].unit);
		}
		else {
			for (i = 0; i < volUnits.length; i++) 
				list.add(volUnits[i].unit);
		}			

		return list;
	}
	
	// private functions:	
	private double getBaseValue(Converter[] u, String n){
		int i=0;
		while (i < u.length
				&& !u[i].abrv.equalsIgnoreCase(n)
				&& !u[i].unit.equalsIgnoreCase(n)) {
			i++;
		}
		if (i >= u.length)
			return 1;
		else 
			return u[i].toBase;
	}
	
	private String getUnitFromAbrv(String t, String a){
		int i=0;
		Converter[] u;
		
		if (type == "vol")
		  u = volUnits;
		else // assume weight
		  u = weightUnits;
		
		while (i < u.length
				&& !u[i].abrv.equalsIgnoreCase(a)) {
			i++;
		}
		if (i >= u.length)
			return null;
		else 
			return u[i].unit;
	}
	
	private String getAbrvFromUnit(String t, String s){
		int i=0;	
		Converter[] u;
		
		if (type == "vol")
		  u = volUnits;
		else // assume weight
		  u = weightUnits;
		
		while (i < u.length
				&& u[i].unit != s) {
			i++;
		}
		if (i >= u.length)
			return null;
		else 
			return u[i].abrv;
	}
	
	private String getTypeFromUnit(String s){
		int i=0;
		while (i < weightUnits.length
				&& !weightUnits[i].unit.equalsIgnoreCase(s)
				&& !weightUnits[i].abrv.equalsIgnoreCase(s)) {
			i++;
		}
		if (i >= weightUnits.length)
			return "vol";
		else 
			return "weight";
		
	}
	


}
