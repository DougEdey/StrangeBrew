/*
 * $Id: Recipe.java,v 1.14 2004/10/21 16:44:24 andrew_avis Exp $
 * Created on Oct 4, 2004 @author aavis recipe class
 */

package strangebrew;

import java.util.*;

public class Recipe {

	// basics:
	private String name;
	private String brewer;
	private GregorianCalendar created;
	private double estOg;
	private double estFg;
	private double ibu;
	private double srm;
	private double alcohol;
	private Quantity preBoilVol = new Quantity();
	private Quantity postBoilVol = new Quantity();
	private double efficiency;
	private int boilMinutes;
	private double attenuation;
	private String style; // change to Style object later
	private String hopUnits;
	private String maltUnits;
	private Options opts;
	
	// totals:	
	private double totalMaltCost;
	private double totalHopsCost;
	private double totalMaltLbs;
	private double totalHopsOz;
	private double totalMashLbs;

	// ingredients
	private ArrayList hops = new ArrayList();
	private ArrayList fermentables = new ArrayList();
	private ArrayList misc = new ArrayList();

	// default constuctor
	public Recipe() {
		
		opts = new Options();
		name = "My Recipe";
		created = new GregorianCalendar();
		efficiency = opts.getDProperty("optEfficiency");
		preBoilVol.setQuantity(opts.getProperty("optVolUnits"), null, opts.getDProperty("optPreBoilVol"));
		postBoilVol.setQuantity(opts.getProperty("optVolUnits"), null, opts.getDProperty("optPostBoilVol"));
		attenuation = opts.getDProperty("optAttenuation");
		boilMinutes = opts.getIProperty("optBoilTime");

	}
	
	// Get functions:
	public String getName(){ return name; }	
	public String getBrewer(){ return brewer; }	
	public double getEstOg(){ return estOg; }	
	public double getEstFg(){ return estFg; }	
	public double getIbu(){ return ibu; }	
	public double getSrm(){ return srm; }
	public double getPreBoilVol(String s){ return preBoilVol.getValueAs(s); }
	public double getPostBoilVol(String s){ return postBoilVol.getValueAs(s); }
	public String getVolUnits(){ return postBoilVol.getUnits(); }
	public double getEfficiency(){ return efficiency; }
	public int getBoilMinutes(){ return boilMinutes; }
	public double getAttenuation(){ return attenuation; }
	public String getStyle(){ return style; } 
	public String getHopUnits(){ return hopUnits; }
	public String getMaltUnits(){ return maltUnits; }
	public double getMashRatio(){ return opts.getDProperty("optMashRatio"); }
	public String getMashRatioU(){ return opts.getProperty("optMashRatioU"); }
	public GregorianCalendar getCreated(){ return created; }
	public double getAlcohol(){ return alcohol; }
	public double getTotalMaltCost(){ return totalMaltCost; }
	public double getTotalMashLbs(){ return totalMashLbs; }
	public String getSProp(String s){ return opts.getProperty(s); }
	public double getDProp(String s){ return opts.getDProperty(s); }
	public int getIProp(String s){ return opts.getIProperty(s); }

	// Set functions:
	public void setName(String n) {	name = n; }
	public void setBrewer(String b) { brewer = b; }
	public void setPreBoil(double p) { preBoilVol.setQuantity(null, null, p); }
	public void setPostBoil(double p) { postBoilVol.setQuantity(null, null, p); }
	public void setPreBoilVolUnits(String v) {	preBoilVol.setQuantity( v, null, -1); }
	public void setPostBoilVolUnits(String v) {	postBoilVol.setQuantity( v, null, -1); }
	public void setEfficiency(double e) { efficiency = e; }
	public void setAttenuation(double a) {	attenuation = a; }
	public void addMalt(Fermentable m) { fermentables.add(m);	}
	public void addHop(Hop h) { hops.add(h); }
	public void addMisc(Misc m) { misc.add(m); }
	public void setStyle(String s) { style = s; }
	public void setBoilMinutes(int b) { boilMinutes = b; }
	public void setHopsUnits(String h) { hopUnits = h; }
	public void setMaltUnits(String m) { maltUnits = m; }
	public void setMashRatio(double m) { opts.setDProperty("optMashRatio", m); }
	public void setMashRatioU(String u) { opts.setProperty("optMashRatioU", u); }
	public void setCreated(Date d) { created.setTime(d); }
	public void setEstOg(double o) { estOg = o; }
	public void setEstFg(double f) { estFg = f; }
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
		postBoilVol.setQuantity( null, u.trim(), Double.parseDouble(d.trim()));

	}
	
	

	/**
	 * Calculate all the malt totals from the array of malt objects 
	 * TODO:  Other things to implement: -
	 * cost tracking - hopped malt extracts (IBUs) - the % that this malt
	 * represents - error checking
	 * 
	 * @return
	 */

	// Calc functions.  We'll want to hide these soon.
	public void calcMaltTotals() {

		double og = 0;
		double fg = 0;
		double lov = 0;
		double maltPoints = 0;
		double maltTotalCost = 0;
		double mcu = 0;
		totalMaltLbs = 0;
		totalMashLbs = 0;

		// first figure out the total we're dealing with
		for (int i = 0; i < fermentables.size(); i++) {
			Fermentable m = ((Fermentable) fermentables.get(i));
			totalMaltLbs += (m.getAmountAs("lb"));
			if (m.getMashed()){ // apply efficiency and add to mash weight
				maltPoints += (m.getPppg() - 1) * m.getAmountAs("lb") * efficiency
						/ postBoilVol.getValueAs("gal");
				totalMashLbs += (m.getAmountAs("lb"));
			}
			else
				maltPoints += (m.getPppg() - 1) * m.getAmountAs("lb") * 100 / postBoilVol.getValueAs("gal");

			mcu += m.getLov() * m.getAmountAs("lb") / postBoilVol.getValueAs("gal");
			maltTotalCost += m.getCostPerU() * m.getAmountAs("lb");
		}

		// set the fields in the object
		estOg = (maltPoints / 100) + 1;
		estFg = 1 + ((estOg - 1) * ((100 - attenuation) / 100));
		srm = calcColour(mcu);

		// Calculate alcohol
		calcAlcohol("Volume");

	}

	public void calcHopsTotals() {

		double ibuTotal = 0;
		double hopsOzTotal = 0;
		double hopsCostTotal = 0;

		for (int i = 0; i < hops.size(); i++) {
			// calculate the average OG of the boil
			// first, the OG at the time of addition:
			double adjPreSize, aveOg = 0;
			Hop h = ((Hop) hops.get(i));
			if (h.getMinutes() > 0)
				adjPreSize = postBoilVol.getValueAs("gal") + (preBoilVol.getValueAs("gal") - postBoilVol.getValueAs("gal"))
						/ (boilMinutes / h.getMinutes());
			else
				adjPreSize = postBoilVol.getValueAs("gal");
			aveOg = 1 + (((estOg - 1) + ((estOg - 1) / (adjPreSize / postBoilVol.getValueAs("gal")))) / 2);
			ibuTotal += calcTinseth(h.getAmountAs("oz"), postBoilVol.getValueAs("gal"), aveOg, h.getMinutes(),
					h.getAlpha(), 4.15);
			hopsCostTotal += h.getCostPerU() * h.getAmountAs("oz");
			hopsOzTotal += h.getAmountAs("oz");
		}

		ibu = ibuTotal;

	}

	// private calculation functions:
	private double calcColour(double lov) {
		// calculates SRM based on MCU (degrees LOV)
		double colour = 0;
		if (lov > 0)
			colour = 1.4922 * Math.pow(lov, 0.6859);
		else
			colour = 0;
		return colour;

	}

	private void calcAlcohol(String method) {
		double oPlato = sGToPlato(estOg);
		double fPlato = sGToPlato(estFg);
		double q = 0.22 + 0.001 * oPlato;
		double re = (q * oPlato + fPlato) / (1.0 + q);
		alcohol = (oPlato - re) / (2.0665 - 0.010665 * oPlato);
		if (method == "Volume") // by volume
			alcohol = alcohol * estFg / 0.794;

	}

	private double sGToPlato(double sg) { 
		// function to convert a value in specific
		// gravity as plato
		// equation based on HBD#3204 post by AJ DeLange
		double Plato;
		Plato = -616.989 + 1111.488 * sg - 630.606 * sg * sg + 136.10305 * sg
				* sg * sg;
		return Plato;
	}

	private double calcTinseth(double amount, double size, double sg, double time,
			double aa, double HopsUtil) {
		double daautil; // decimal alpha acid utilization
		double bigness; // bigness factor
		double boil_fact; // boil time factor
		double mgl_aaa; // mg/l of added alpha units
		double ibu;

		bigness = 1.65 * (Math.pow(0.000125, (sg - 1))); //0.000125 original
		boil_fact = (1 - (Math.exp(-0.04 * time))) / HopsUtil;
		daautil = bigness * boil_fact;
		mgl_aaa = (aa / 100) * amount * 7490 / size;
		ibu = daautil * mgl_aaa;
		return ibu;
	}

	public String toXML() {
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n");
		sb.append("<STRANGEBREWRECIPE version = \"2.0A\">\n");
		sb.append("  <NAME>" + name + "</NAME>\n");
		sb.append("  <SIZE>" + postBoilVol.getValue() + "</SIZE>\n");
		sb.append("  <SIZE_UNITS>" + postBoilVol.getUnits() + "</SIZE_UNITS>\n");
		
		// fermentables list:
		sb.append("  <FERMENTABLES>\n");		
		for (int i = 0; i < fermentables.size(); i++) {
			Fermentable m = (Fermentable) fermentables.get(i);
			sb.append(m.toXML());
		}
		sb.append("  </FERMENTABLES>\n");
		
		// hops list:
		sb.append("  <HOPS>\n");
		for (int i = 0; i < hops.size(); i++) {
			Hop h = (Hop) hops.get(i);
			sb.append(h.toXML());
		}
		sb.append("  </HOPS>\n");
		
		// misc ingredients list:
		sb.append("  <MISC>\n");
		for (int i = 0; i < misc.size(); i++) {
			Misc mi = (Misc) misc.get(i);
			sb.append(mi.toXML());
		}
		sb.append("  </MISC>\n");
		
		sb.append("</RECIPE>");

		return sb.toString();
	}

	public void testRecipe() {
		calcMaltTotals();
		calcHopsTotals();
		System.out.print("Recipe totals for " + name + ": \n");
		System.out.print("Vol: " + postBoilVol.getValue() + " " +
				postBoilVol.getUnits() + "\n");
		System.out.print("Effic: " + efficiency + "\n");
		System.out.print("estOG: " + estOg + "\n");
		System.out.print("estFG: " + estFg + "\n");
		System.out.print("srm: " + srm + "\n");
		System.out.print("%alc: " + alcohol + "\n");
		System.out.print("IBUs: " + ibu + "\n");
	}
	


}