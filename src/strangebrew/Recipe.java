/*
 * $Id: Recipe.java,v 1.23 2004/11/17 17:56:16 andrew_avis Exp $
 * Created on Oct 4, 2004 @author aavis recipe class
 */

package strangebrew;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

public class Recipe {

	// basics:
	
	private double alcohol;
	private double attenuation;
	private int boilMinutes;
	private String brewer;
	private String comments;	
	private GregorianCalendar created;
	private double efficiency;
	private double estOg;
	private double estFg;
	private double ibu;
	public Mash mash = new Mash();	
	private boolean mashed;
	private String name;
	private Quantity preBoilVol = new Quantity();
	private Quantity postBoilVol = new Quantity();
	private double srm;
	private Style style = new Style();
	private Yeast yeast = new Yeast();

	// options:
	private String hopUnits;
	private String maltUnits;
	private String ibuCalcMethod;
	private double ibuHopUtil;
	
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
		
		Options opts = new Options();
		name = "My Recipe";
		created = new GregorianCalendar();
		efficiency = opts.getDProperty("optEfficiency");
		preBoilVol.setQuantity(opts.getProperty("optVolUnits"), null, opts.getDProperty("optPreBoilVol"));
		postBoilVol.setQuantity(opts.getProperty("optVolUnits"), null, opts.getDProperty("optPostBoilVol"));
		attenuation = opts.getDProperty("optAttenuation");
		boilMinutes = opts.getIProperty("optBoilTime");
		ibuCalcMethod = opts.getProperty("optIBUCalcMethod");
		ibuHopUtil = opts.getDProperty("optHopsUtil");

	}
	
	// Get functions:
	
	public double getAlcohol(){ return alcohol; }
	public double getAttenuation(){ return attenuation; }
	public int getBoilMinutes(){ return boilMinutes; }
	public String getBrewer(){ return brewer; }	
	public String getComments() { return comments; }
	public GregorianCalendar getCreated(){ return created; }
	public double getEfficiency(){ return efficiency; }
	public double getEstOg(){ return estOg; }	
	public double getEstFg(){ return estFg; }
	public ArrayList getFermentablesList() { return fermentables; }
	public ArrayList getHopsList() { return hops; }
	public String getHopUnits(){ return hopUnits; }
	public double getIbu(){ return ibu; }	
	public String getIBUMethod(){ return ibuCalcMethod; }
	public String getMaltUnits(){ return maltUnits; }
	public String getName(){ return name; }		
	public double getPreBoilVol(String s){ return preBoilVol.getValueAs(s); }
	public double getPostBoilVol(String s){ return postBoilVol.getValueAs(s); }
	public double getSrm(){ return srm; }
	public String getStyle(){ return style.getName(); } 
	public double getTotalMaltCost(){ return totalMaltCost; }
	public double getTotalMashLbs(){ return totalMashLbs; }
	public String getVolUnits(){ return postBoilVol.getUnits(); }
	public String getYeast(){ return yeast.getName();}	
	

	// Set functions:
	public void addMalt(Fermentable m) { fermentables.add(m);	}
	public void addHop(Hop h) { hops.add(h); }
	public void addMisc(Misc m) { misc.add(m); }
	
	public void setBoilMinutes(int b) { boilMinutes = b; }
	public void setBrewer(String b) { brewer = b; }
	public void setCreated(Date d) { created.setTime(d); }
	public void setHopsUnits(String h) { hopUnits = h; }
	public void setMaltUnits(String m) { maltUnits = m; }
	public void setMashed(boolean m) { mashed = m; }
	public void setMashRatio(double m) { mash.setMashRatio(m); }
	public void setMashRatioU(String u) { mash.setMashRatioU(u); }
	public void setName(String n) {	name = n; }
	public void setPreBoil(double p) { preBoilVol.setQuantity(null, null, p); }
	public void setPostBoil(double p) { postBoilVol.setQuantity(null, null, p); }
	public void setPreBoilVolUnits(String v) {	preBoilVol.setQuantity( v, null, -1); }
	public void setPostBoilVolUnits(String v) {	postBoilVol.setQuantity( v, null, -1); }
	public void setStyle(String s) { style.setName(s); }
	public void setYeastName(String s) { yeast.setName(s); }
		
	// Setters that need to do extra work:
	public void setEstFg(double f) {
		if (f != estFg && f > 0) {
			estFg = f;
			attenuation = 100 - ((estFg - 1) / (estOg - 1) * 100);
			calcAlcohol("Volume");
		}
	}

	public void setEstOg(double o) {
		if (o != estOg && o > 0) {
			estOg = o;
			attenuation = 100 - ((estFg - 1) / (estOg - 1) * 100);
			calcEfficiency();
			calcAlcohol("Volume");
		}
	}
	
	public void setEfficiency(double e) {
		if (e != efficiency && e > 0){
		efficiency = e; 
		calcMaltTotals();		
		}
	}
	
	public void setAttenuation(double a) {
		if (a != attenuation && a > 0){
		attenuation = a; 
		calcMaltTotals();
		}
		
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
	
	private void calcEfficiency() {
		double possiblePoints=0;
		for (int i = 0; i < fermentables.size(); i++) {
			Fermentable m = ((Fermentable) fermentables.get(i));
			possiblePoints += (m.getPppg() - 1) * m.getAmountAs("lb") / postBoilVol.getValueAs("gal");
		}
		efficiency =  (estOg - 1) / possiblePoints * 100;
	}
	
	
	
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
			if (ibuCalcMethod.equals("Tinseth"))
				ibuTotal += calcTinseth(h.getAmountAs("oz"), postBoilVol.getValueAs("gal"), aveOg, h.getMinutes(),
					h.getAlpha(), ibuHopUtil);
			else if (ibuCalcMethod.equals("Rager"))
				ibuTotal += CalcRager(h.getAmountAs("oz"), postBoilVol.getValueAs("gal"), aveOg, h.getMinutes(), h.getAlpha());
			else
				ibuTotal += CalcGaretz(h.getAmountAs("oz"), postBoilVol.getValueAs("gal"), aveOg, h.getMinutes(), preBoilVol.getValueAs("gal"), 1, h.getAlpha()); 
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

	/*
	 * Hop IBU calculation methods:
	 */
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
	

	//	 rager method of ibu calculation
	//	 constant 7962 is corrected to 7490 as per hop faq
	double CalcRager(double amount, double size, double sg, double time,
			double AA) {
		double ibu, utilization, ga;
		utilization = 18.11 + (13.86 * (Math.tanh((time - 31.32) / 18.27)));
		ga = sg < 1.050 ? 0.0 : 0.2;
		ibu = amount * (utilization / 100) * (AA / 100.0) * 7490;
		ibu /= size * (1 + ga);
		return ibu;
	}

	//	 utilization table for average floc yeast
	int util[] = {0, 0, 0, 0, 0, 0, 1, 1, 1, 3, 4, 5, 5, 6, 7, 9, 11, 13, 11,
			13, 16, 13, 16, 19, 15, 19, 23, 16, 20, 24, 17, 21, 25};

	double CalcGaretz(double amount, double size, double sg, double time,
			double start_vol, int yeast_flocc, double AA) {
//		 iterative value seed - adjust to loop through value
		double desired_ibu = CalcRager(amount, size, sg, time, AA);
		int elevation = 500; // elevation in feet - change for user setting
		double concentration_f = size / start_vol;
		double boil_gravity = (concentration_f * (sg - 1)) + 1;
		double gravity_f = ((boil_gravity - 1.050) / 0.2) + 1;
		double temp_f = (elevation / 550 * 0.02) + 1;

		// iterative loop, uses desired_ibu to define hopping_f, then seeds
		// itself
		double hopping_f, utilization, combined_f;
		double ibu = desired_ibu;
		int util_index;
		for (int i = 0; i < 5; i++) { // iterate loop 5 times
			hopping_f = ((concentration_f * desired_ibu) / 260) + 1;
			if (time > 50)
				util_index =  10;
			else
				util_index = (int)(time / 5.0);
			utilization = util[(util_index * 3) + yeast_flocc];
			combined_f = gravity_f * temp_f * hopping_f;
			ibu = (utilization * AA * amount * 0.749) / (size * combined_f);
			desired_ibu = ibu;
		}

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
		
		sb.append(mash.toXml());
		
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