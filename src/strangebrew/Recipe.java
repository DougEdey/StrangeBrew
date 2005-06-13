/*
 * $Id: Recipe.java,v 1.47 2005/06/13 16:30:53 andrew_avis Exp $
 * Created on Oct 4, 2004 @author aavis recipe class
 */

/**
 *  StrangeBrew Java - a homebrew recipe calculator
    Copyright (C) 2005  Drew Avis

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

package strangebrew;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;

import ca.strangebrew.SBStringUtils;

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
	private double evap;
	private double ibu;	
	private boolean mashed;
	private String name;
	private Quantity preBoilVol = new Quantity();
	private Quantity postBoilVol = new Quantity();
	private double srm;
	private Style style = new Style();
	private Yeast yeast = new Yeast();
	
	public Mash mash = new Mash();	

	// options:
	private String hopUnits;
	private String maltUnits;
	private String ibuCalcMethod;
	private double ibuHopUtil;
	private String evapMethod;
	
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
	
	// notes
	private ArrayList notes = new ArrayList();
	
	// formats
	public DecimalFormat df1 = new DecimalFormat("####.0");
	public DecimalFormat df2 = new DecimalFormat("#.00");
	public DecimalFormat df3 = new DecimalFormat("0.000");

	// default constuctor
	public Recipe() {
		
		Options opts = new Options();
		name = "My Recipe";
		created = new GregorianCalendar();
		efficiency = opts.getDProperty("optEfficiency");
		preBoilVol.setQuantity(opts.getProperty("optSizeU"), null, opts.getDProperty("optPreBoilVol"));
		postBoilVol.setQuantity(opts.getProperty("optSizeU"), null, opts.getDProperty("optPostBoilVol"));
		attenuation = opts.getDProperty("optAttenuation");
		boilMinutes = opts.getIProperty("optBoilTime");
		ibuCalcMethod = opts.getProperty("optIBUCalcMethod");
		ibuHopUtil = opts.getDProperty("optHopsUtil");		
		hopUnits = opts.getProperty("optHopsU");
		maltUnits = opts.getProperty("optMaltU");
		brewer = opts.getProperty("optBrewer");
		evapMethod = opts.getProperty("optEvapCalcMethod");
		evap = opts.getDProperty("optEvaporation");

	}
	
	// Getters:	
	public double getAlcohol(){ return alcohol; }
	public double getAttenuation(){ return attenuation; }
	public int getBoilMinutes(){ return boilMinutes; }
	public String getBrewer(){ return brewer; }	
	public String getComments() { return comments; }
	public GregorianCalendar getCreated(){ return created; }
	public double getEfficiency(){ return efficiency; }
	public double getEstOg(){ return estOg; }	
	public double getEstFg(){ return estFg; }
	public double getEvap(){ return evap; }
	public Mash getMash() { return mash; }	
	public double getIbu(){ return ibu; }	
	public String getIBUMethod(){ return ibuCalcMethod; }
	public String getMaltUnits(){ return maltUnits; }
	public String getName(){ return name; }		
	public double getPreBoilVol(String s){ return preBoilVol.getValueAs(s); }
	public double getPostBoilVol(String s){ return postBoilVol.getValueAs(s); }
	public double getSrm(){ return srm; }
	public String getStyle(){ return style.getName(); } 
	public Style getStyleObj(){ return style;}
	public double getTotalHopsOz(){ return totalHopsOz; }
	public double getTotalHopsCost(){ return totalHopsCost; }
	public double getTotalMaltCost(){ return totalMaltCost; }
	public double getTotalMashLbs(){ return totalMashLbs; }
	public double getTotalMash() {
		Quantity q = new Quantity();
		q.setQuantity(null,"lb",totalMashLbs);
		return q.getValueAs(mash.getMashVolUnits());
	}
	public double getTotalMaltLbs(){ return totalMaltLbs; }
	public String getVolUnits(){ return postBoilVol.getUnits(); }
	public String getYeast(){ return yeast.getName();}	
	public Yeast getYeastObj(){ return yeast;}
	
	// Setters:
	public void setBoilMinutes(int b) { boilMinutes = b; }
	public void setBrewer(String b) { brewer = b; }
	public void setComments(String c) { comments = c; }
	public void setCreated(Date d) { created.setTime(d); }
	public void setEvap(double e) { evap = e; }
	public void setHopsUnits(String h) { hopUnits = h; }
	public void setMaltUnits(String m) { maltUnits = m; }
	public void setMashed(boolean m) { mashed = m; }
	public void setMashRatio(double m) { mash.setMashRatio(m); }
	public void setMashRatioU(String u) { mash.setMashRatioU(u); }
	public void setName(String n) {	name = n; }
	public void setStyle(String s) { style.setName(s); }
	public void setStyle(Style s) { style = s; }
	public void setYeastName(String s) { yeast.setName(s); }
	public void setYeast(Yeast y) { yeast = y; }

	
	// hop list get functions:
	public String getHopUnits(){ return hopUnits; }
	public Hop getHop(int i) { return (Hop)hops.get(i); }
	public int getHopsListSize() { return hops.size(); }
	public String getHopName(int i){ return ((Hop)hops.get(i)).getName(); }
	public String getHopType(int i){ return ((Hop)hops.get(i)).getType(); }
	public double getHopAlpha(int i){ return ((Hop)hops.get(i)).getAlpha(); }
	public String getHopUnits(int i){ return ((Hop)hops.get(i)).getUnits(); }
	public String getHopAdd(int i){ return ((Hop)hops.get(i)).getAdd(); }
	public int getHopMinutes(int i){ return ((Hop)hops.get(i)).getMinutes(); }
	public double getHopIBU(int i){ return ((Hop)hops.get(i)).getIBU(); }
	public double getHopCostPerU(int i){ return ((Hop)hops.get(i)).getCostPerU(); }
	public double getHopAmountAs(int i, String s ){ return ((Hop)hops.get(i)).getAmountAs(s); }
	public String getHopDescription(int i){ return ((Hop)hops.get(i)).getDescription(); }
	
	// hop list set functions
	public void setHopUnits(int i, String u) { ((Hop)hops.get(i)).setUnits(u); }
	public void setHopName(int i, String n) { ((Hop)hops.get(i)).setName(n); }
	public void setHopType(int i, String t) { ((Hop)hops.get(i)).setType(t); }
	public void setHopAdd(int i, String a) { ((Hop)hops.get(i)).setAdd(a); }
	public void setHopAlpha(int i, double a) { ((Hop)hops.get(i)).setAlpha(a); }
	public void setHopMinutes(int i, int m) { 
		// have to re-sort hops
		((Hop)hops.get(i)).setMinutes(m); 
		Collections.sort(hops, new ingrComparator());
		}
	public void setHopCost(int i, String c) { ((Hop)hops.get(i)).setCost(c); }
	public void setHopAmount(int i, double a) { ((Hop)hops.get(i)).setAmount(a); }
	
	// fermentable get methods
	// public ArrayList getFermentablesList() { return fermentables; }
	public Fermentable getFermentable(int i) { return (Fermentable)fermentables.get(i); }
	public int getMaltListSize() { return fermentables.size(); }
	public String getMaltName(int i){ return ((Fermentable)fermentables.get(i)).getName(); }
	public String getMaltUnits(int i){ return ((Fermentable)fermentables.get(i)).getUnits(); }
	public double getMaltPppg(int i){ return ((Fermentable)fermentables.get(i)).getPppg(); }
	public double getMaltLov(int i){ return ((Fermentable)fermentables.get(i)).getLov(); }
	public double getMaltCostPerU(int i){ return ((Fermentable)fermentables.get(i)).getCostPerU(); }
	public double getMaltPercent(int i){ return ((Fermentable)fermentables.get(i)).getPercent(); }
	public double getMaltAmountAs(int i, String s){ return ((Fermentable)fermentables.get(i)).getAmountAs(s); }
	public String getMaltDescription(int i){ return ((Fermentable)fermentables.get(i)).getDescription(); }
	
	// fermentable set methods
	public void setMaltName(int i, String n) {
		// have to re-sort
		((Fermentable)fermentables.get(i)).setName(n); 
		Collections.sort(fermentables, new ingrComparator());
		}
	public void setMaltUnits(int i, String u) {((Fermentable)fermentables.get(i)).setUnits(u); }
	public void setMaltAmount(int i, double a) {((Fermentable)fermentables.get(i)).setAmount(a); }
	public void setMaltPppg(int i, double p) {((Fermentable)fermentables.get(i)).setPppg(p); }
	public void setMaltLov(int i, double l) {((Fermentable)fermentables.get(i)).setLov(l); }
	public void setMaltCost(int i, String c) {((Fermentable)fermentables.get(i)).setCost(c); }
	
	
	// misc get/set functions
	public int getMiscListSize(){ return misc.size(); }
	public Misc getMisc(int i){ return (Misc)misc.get(i); }
	public String getMiscName(int i) { return ((Misc)misc.get(i)).getName();}
	public void setMiscName(int i, String n) {((Misc)misc.get(i)).setName(n);}
	public double getMiscAmount(int i) { 
		Misc m = ((Misc)misc.get(i));
		return m.getAmountAs(m.getUnits());		
	}
	public void setMiscAmount(int i, double a) {((Misc)misc.get(i)).setAmount(a); }
	public String getMiscUnits(int i) { return ((Misc)misc.get(i)).getUnits();}
	public void setMiscUnits(int i, String u) {((Misc)misc.get(i)).setUnits(u); }
	public double getMiscCost(int i) { return ((Misc)misc.get(i)).getCostPerU();}
	public void setMiscCost(int i, double c) {((Misc)misc.get(i)).setCost(c); }
	public String getMiscStage(int i) { return ((Misc)misc.get(i)).getStage();}
	public void setMiscStage(int i, String s) {((Misc)misc.get(i)).setStage(s);}
	public int getMiscTime(int i) { return ((Misc)misc.get(i)).getTime();}
	public void setMiscTime(int i, int t) {((Misc)misc.get(i)).setTime(t);}
	public String getMiscDescription(int i) { return ((Misc)misc.get(i)).getDescription();}
	public void setMiscComments(int i, String c) {((Misc)misc.get(i)).setComments(c); }
	public String getMiscComments(int i) { return ((Misc)misc.get(i)).getComments();}
	
	// notes get/set methods
	public int getNotesListSize() { return notes.size(); }
	public Date getNoteDate(int i) { return ((Note)notes.get(i)).getDate(); }
	public void setNoteDate(int i, Date d) { ((Note)notes.get(i)).setDate(d); }
	public String getNoteType(int i) { return ((Note)notes.get(i)).getType(); }
	public void setNoteType(int i, String t) { ((Note)notes.get(i)).setType(t); }
	public String getNoteNote(int i) { return ((Note)notes.get(i)).getNote(); }
	public void setNoteNote(int i, String n) { ((Note)notes.get(i)).setNote(n); }

	
	
	
	// Setters that need to do extra work:
	
	public void setPreBoilVolUnits(String v) {
		preBoilVol.setQuantity( v, null, -1);
		postBoilVol.setQuantity( v, null, -1);
		calcMaltTotals();
		calcHopsTotals();
		
	}
	public void setPostBoilVolUnits(String v) {
		postBoilVol.setQuantity( v, null, -1); 
		preBoilVol.setQuantity( v, null, -1);
		calcMaltTotals();
		calcHopsTotals();
	}
	
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
	public void setPreBoil(double p) { 
		preBoilVol.setQuantity(null, null, p);
		// TODO: implement % as well as constant
		double post = p - (evap * boilMinutes / 60);
		postBoilVol.setQuantity(null, null, post);
		calcMaltTotals();
		calcHopsTotals();
		}
	
	public void setPostBoil(double p) {
		postBoilVol.setQuantity(null, null, p); 
		// TODO: implement % as well as constant 
		double pre = p + (evap * boilMinutes / 60);
		preBoilVol.setQuantity(null, null, pre);
		calcMaltTotals();
		calcHopsTotals();
		}
	/*
	 * Functions that add/remove from ingredient lists
	 */
	public void addMalt(Fermentable m) { 
		fermentables.add(m);
		Collections.sort(fermentables, new ingrComparator());
		calcMaltTotals();
		}
	public void delMalt(int i) {
		if (!fermentables.isEmpty()){
			fermentables.remove(i);
			calcMaltTotals();
		}
		}
	public void addHop(Hop h) { 
		hops.add(h);
		Collections.sort(hops, new ingrComparator());
		calcHopsTotals();
		}
	public void delHop(int i){
		if (!hops.isEmpty()){
			hops.remove(i);
			calcHopsTotals();
		}
	}
	public void addMisc(Misc m) { misc.add(m); }
	
	public void delMisc(int i){
		if (!misc.isEmpty()){
			misc.remove(i);
		}
	}
	
	public void addNote(Note n) { notes.add(n); }
	
	public void delNote(int i){
		if (!notes.isEmpty()){
			notes.remove(i);
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

	// Calc functions.  
	
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
		double mcu = 0;
		totalMaltLbs = 0;
		totalMaltCost = 0;
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
			totalMaltCost += m.getCostPerU() * m.getAmountAs("lb");
		}
		
		// now set the malt % by weight:
		for (int i = 0; i < fermentables.size(); i++) {
			Fermentable m = ((Fermentable) fermentables.get(i));
			m.setPercent((m.getAmountAs("lb")/totalMaltLbs * 100));
		}
				 

		// set the fields in the object
		estOg = (maltPoints / 100) + 1;
		estFg = 1 + ((estOg - 1) * ((100 - attenuation) / 100));
		srm = calcColour(mcu);
		mash.setMaltWeight(totalMashLbs);


		calcAlcohol("Volume");

	}

	public void calcHopsTotals() {

		double ibuTotal = 0;
		totalHopsCost = 0;
		totalHopsOz = 0;

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
				h.setIBU(calcTinseth(h.getAmountAs("oz"), postBoilVol.getValueAs("gal"), aveOg, h.getMinutes(),
					h.getAlpha(), ibuHopUtil));
			else if (ibuCalcMethod.equals("Rager"))
				h.setIBU(CalcRager(h.getAmountAs("oz"), postBoilVol.getValueAs("gal"), aveOg, h.getMinutes(), h.getAlpha()));
			else
				h.setIBU(CalcGaretz(h.getAmountAs("oz"), postBoilVol.getValueAs("gal"), aveOg, h.getMinutes(), preBoilVol.getValueAs("gal"), 1, h.getAlpha())); 
			ibuTotal += h.getIBU();
			totalHopsCost += h.getCostPerU() * h.getAmountAs("oz");
			totalHopsOz += h.getAmountAs("oz");
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
		// should be tanh:
		double x = (time - 31.32) / 18.27;
		// tanh:		
		double tanhx = (Math.exp(x)-Math.exp(-x))/(Math.exp(x)+Math.exp(-x));
		utilization = 18.11 + (13.86 * tanhx / 18.27);
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
		SBStringUtils sbStringU = new SBStringUtils();
		sb.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n");
		sb.append("<STRANGEBREWRECIPE version = \"2.0A\">\n");
		sb.append("  <DETAILS>\n");
		sb.append("  <NAME>" + sbStringU.subEntities(name) + "</NAME>\n");
		sb.append("  <BREWER>" + sbStringU.subEntities(brewer) + "</BREWER>\n");
		sb.append("  <NOTES>" + sbStringU.subEntities(comments) + "</NOTES>\n");
		sb.append("  <EFFICIENCY>" + efficiency + "</EFFICIENCY>\n");
		sb.append("  <OG>" + df3.format(estOg) + "</OG>\n");
		sb.append("  <FG>" + df3.format(estFg) + "</FG>\n");
		sb.append("  <STYLE>" + style.getName() + "</STYLE>\n");
		sb.append("  <MASH>" + mashed + "</MASH>\n");
		sb.append("  <LOV>" + df1.format(srm) + "</LOV>\n");
		sb.append("  <IBU>" + df1.format(ibu) + "</IBU>\n");
		sb.append("  <ALC>" + df1.format(alcohol) + "</ALC>\n");
		sb.append("  <BOIL_TIME>" + boilMinutes + "</BOIL_TIME>\n");
		sb.append("  <PRESIZE>" + preBoilVol.getValue() + "</PRESIZE>\n");
		sb.append("  <SIZE>" + postBoilVol.getValue() + "</SIZE>\n");
		sb.append("  <SIZE_UNITS>" + postBoilVol.getUnits() + "</SIZE_UNITS>\n");
		sb.append("  <MALT_UNITS>" + maltUnits + "</MALT_UNITS>\n");
		sb.append("  <HOPS_UNITS>" + hopUnits + "</HOPS_UNITS>\n");
		sb.append("  <YEAST>" + yeast.getName() + "</YEAST>\n");
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");		
		sb.append("  <RECIPE_DATE>" + df.format(created.getTime()) + "</RECIPE_DATE>\n");
		sb.append("  <ATTENUATION>" + attenuation + "</ATTENUATION>\n");
		sb.append("  </DETAILS>\n");
		
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
		
		// notes list:
		sb.append("  <NOTES>\n");
		for (int i = 0; i < notes.size(); i++) {
			sb.append(((Note) notes.get(i)).toXML());
		}
		sb.append("  </NOTES>\n");
		
		sb.append("</STRANGEBREWRECIPE>");

		return sb.toString();
	}
	
	public static String padLeft(String str, int fullLength, char ch) {
		return (fullLength > str.length()) 
		? str.concat(buildString(ch, fullLength - str.length()))
		: str;
	}
	
	public static String padRight(String str, int fullLength, char ch) {
		return (fullLength > str.length()) 
		? buildString(ch, fullLength - str.length()).concat(str)
		: str;
	}

	public static String buildString(char ch, int length) {
		char newStr[] = new char[length];
		for (int i = 0; i < length; ++i)
			newStr[i] = ch;
		return new String(newStr);
	}
	
	public String toText(){
		DecimalFormat df1 = new DecimalFormat("0.0");
		DecimalFormat df2 = new DecimalFormat("0.00");
		MessageFormat mf;
		StringBuffer sb = new StringBuffer();
		sb.append("StrangeBrew 2.0 recipe text output\n\n");
		sb.append("Details:\n");
		sb.append("Name: " + name + "\n");
		sb.append("Brewer: " + brewer + "\n");
		sb.append("Size: " + df1.format(postBoilVol.getValue()) + " " + postBoilVol.getUnits()+"\n");
		sb.append("Style: " + style.getName() + "\n");
	    mf = new MessageFormat("OG: {0,number,0.000},\tFG:{1,number,0.000}, \tALC:{2,number,0.0}\n");
		Object[] objs = {new Double(estOg), new Double(estFg), new Double(alcohol) };
		sb.append(mf.format( objs ));
		sb.append("Fermentables:\n");
		sb.append(padLeft("Name ", 30, ' ') + " amount units  pppg    lov   %\n");

		mf = new MessageFormat("{0} {1} {2} {3,number,0.000} {4} {5,number, 0.0}%\n");
		for (int i=0; i<fermentables.size(); i++){
			Fermentable f = (Fermentable)fermentables.get(i);
			
			Object[] objf = {padLeft(f.getName(), 30, ' '),
					padRight(" "+df2.format(f.getAmountAs(f.getUnits())), 6, ' '),
					f.getUnits(),
					new Double(f.getPppg()),
					padRight(" "+df1.format(f.getLov()), 6, ' '),
					new Double(f.getPercent())};
			sb.append(mf.format(objf));
			
		}
		
		sb.append("Hops:\n");
		sb.append(padLeft("Name ", 20, ' ') + " amount units  Alpha   Min    IBU\n");
		
		mf = new MessageFormat("{0} {1} {2} {3} {4} {5}\n");
		for (int i=0; i<hops.size(); i++){
			Hop h = (Hop)hops.get(i);
			
			Object[] objh = {padLeft(h.getName(), 20, ' '),
					padRight(" "+df2.format(h.getAmountAs(h.getUnits())), 6, ' '),
					h.getUnits(),
					padRight(" "+h.getAlpha(), 5, ' '),
					padRight(" "+df1.format(h.getMinutes()), 6, ' '),
					padRight(" "+df1.format(h.getIBU()), 5, ' ')};
			sb.append(mf.format(objh));
			
		}


		return sb.toString();
	}	
	
	
	/**
	 * 
	 * @author aavis
	 *
	 * ingredient comparator to help sort lists of malts / hops
	 * 
	 */
	private class ingrComparator implements Comparator {

		
		public int compare(Object a, Object b) {
			if (a.getClass().getName().equalsIgnoreCase("strangebrew.Fermentable")) {
				// sort malts by name, default
				// TODO: read sort order option to sort by other parameters
				int result = ((Fermentable)a).getName().compareTo(((Fermentable)b).getName());
				return (result == 0 ? -1 : result);
			} else if (a.getClass().getName().equalsIgnoreCase("strangebrew.Hop")) {
				// sort hop additions by minutes
				Integer a1 = new Integer(((Hop)a).getMinutes());
				Integer b1 = new Integer(((Hop)b).getMinutes());				
				int result = a1.compareTo(b1);
				return (result == 0 ? -1 : result);
			}
			else return 0;
		}

	}
}