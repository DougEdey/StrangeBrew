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
 * This is what I think Mike calls a poltergeist, so I'm betting
 * I'll have to refactor this later.
 * It's basically a convenience class that holds the simple conversion
 * logic from SBWin.
 */
public class Convert {
	class Unit {
		String abrv;
		String name;
		double toBase;
		public Unit(String n, String a, double t) {
			name = n;
			abrv = a;
			toBase = t;
		}
	}

	Unit volUnits[] =
		{
			new Unit("barrel IMP", "bbl Imp", 0.023129837),
			new Unit("barrel US", "bbl", 0.032258065),
			new Unit("fl. ounces", "oz", 128),
			new Unit("gallons IMP", "gal Imp", 0.8327),
			new Unit("gallons US", "gal", 1),
			new Unit("litres", "l", 3.7854),
			new Unit("millilitres", "ml", 3785.4118),
			new Unit("pint US", "pt", 8),
			new Unit("quart US", "qt", 4)};

	Unit weightUnits[] =
		{
			new Unit("grams", "gr", 453.59237),
			new Unit("kilograms", "kg", 0.45359237),
			new Unit("ounces", "oz", 16),
			new Unit("pounds", "lb", 1),
			new Unit("ton S", "T", 0.0005),
			new Unit("tonne SI", "T SI", 0.000453592)};

	public double convert(double amount, String from, String to, String type) {
		double fromBase = 0;
		double toBase = 0;
		int i = 0;
		Unit[] u;
		
		if (type == "vol")
		  u = volUnits;
		else
		  u = weightUnits;

		fromBase = getBaseValue(u, from);
		toBase = getBaseValue(u, to);
		
		return amount * toBase / fromBase;
	}
	
	public double getBaseValue(Unit[] u, String n){
		int i=0;
		while (u[i].abrv != n
			|| u[i].name != n
			|| i > u.length) {
			i++;
		}
		return u[i].toBase;
	}
	

	double fToc(double tempF) {
		// routine to convert basic F temps to C
		return (5 * (tempF - 32)) / 9;
	}

	double cToF(double tempC) {
		// routine to convert Celcius to Farenheit
		return ((tempC * 9) / 5) + 32;
	}
}
