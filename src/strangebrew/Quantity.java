/*
 * Created on Oct 7, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

package strangebrew;

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
	

	// mutators:
/*	public void convertTo(String to, String type) {
		value = getValueAs(to);
		unit = to;
	}
	*/
	
	/*
	public void convertTemp(String to){
		// if we're already the target temp units, or
		// if we're not a temp at all, do nothing
		if (unit == to || type != "temp")
			return;
		else if (to == "F")
			value = cToF(value);
		else
			value = fToC(value);		
	}
	*/
	
	
	// private functions:	
	private double getBaseValue(Converter[] u, String n){
		int i=0;
		while (i < u.length
				&& u[i].abrv != n
				&& u[i].unit != n) {
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
