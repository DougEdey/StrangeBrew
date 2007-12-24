/*
 * $Id: Recipe.java,v 1.49 2007/12/24 15:06:22 jimcdiver Exp $
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

package ca.strangebrew;

import java.awt.Color;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;

public class Recipe {

	// basics:

	private String version;

	private boolean isDirty = false;
	public boolean allowRecalcs = true;
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
	private double ebc;
	private Style style = new Style();
	private Yeast yeast = new Yeast();
	private WaterProfile sourceWater = new WaterProfile();
	private WaterProfile targetWater = new WaterProfile();
	private ArrayList brewingSalts = new ArrayList();
	public Mash mash;
	
	// Fermentation
	private ArrayList fermentationSteps = new ArrayList();

	// water use:
	private double chillShrinkQTS;
	private double kettleLoss;
	private double trubLoss;
	private double miscLoss;
	// private double spargeQTS;
	private double totalWaterQTS;
	private double finalWortVolQTS;
	// this could be user-configurable:
	private static final double CHILLPERCENT = 0.03;

	// dilution:
	private boolean diluted = false;
	public DilutedRecipe dilution;
	
	// Carbonation
	private double bottleTemp;
	private double servTemp;
	private double targetVol;
	private PrimeSugar primeSugar = new PrimeSugar();
	private String carbTempU;
	private boolean kegged;
	private double kegPSI;

	// options:
	public Options opts;
	private String colourMethod;
	private String hopUnits;
	private String maltUnits;
	private String ibuCalcMethod;
	private double ibuHopUtil;
	private String evapMethod;
	private String alcMethod;
	private double pelletHopPct;
	private String bottleU;
	private double bottleSize;
	private double otherCost;

	private int fwhTime;
	private int dryHopTime;
	private int mashHopTime;

	// totals:
	private double totalMaltCost;
	private double totalHopsCost;
	private double totalMiscCost;
	private double totalMaltLbs;
	private double totalHopsOz;
	private double totalMashLbs;
	private int totalFermentTime;
	
	// ingredients
	private ArrayList hops = new ArrayList();
	private ArrayList fermentables = new ArrayList();
	private ArrayList misc = new ArrayList();

	// notes
	private ArrayList notes = new ArrayList();

	// default constuctor
	public Recipe() {

		opts = Options.getInstance();
		mash = new Mash(this);
		name = "My Recipe";
		created = new GregorianCalendar();
		efficiency = opts.getDProperty("optEfficiency");
		//preBoilVol.setUnits(opts.getProperty("optSizeU"));
		// preBoilVol.setAmount(opts.getDProperty("optPreBoilVol"));
		// postBoilVol.setUnits(opts.getProperty("optSizeU"));
		// postBoilVol.setAmount(opts.getDProperty("optPostBoilVol"));

		attenuation = opts.getDProperty("optAttenuation");
		boilMinutes = opts.getIProperty("optBoilTime");
		ibuCalcMethod = opts.getProperty("optIBUCalcMethod");
		ibuHopUtil = opts.getDProperty("optHopsUtil");
		fwhTime = opts.getIProperty("optFWHTime");
		mashHopTime = opts.getIProperty("optMashHopTime");
		dryHopTime = opts.getIProperty("optDryHopTime");
		hopUnits = opts.getProperty("optHopsU");
		maltUnits = opts.getProperty("optMaltU");
		brewer = opts.getProperty("optBrewer");
		evapMethod = opts.getProperty("optEvapCalcMethod");
		evap = opts.getDProperty("optEvaporation");
		alcMethod = opts.getProperty("optAlcCalcMethod");
		colourMethod = opts.getProperty("optColourMethod");

		kettleLoss = opts.getDProperty("optKettleLoss");
		trubLoss = opts.getDProperty("optTrubLoss");
		miscLoss = opts.getDProperty("optMiscLoss");
		pelletHopPct = opts.getDProperty("optPelletHopsPct");

		bottleU = opts.getProperty("optBottleU");
		bottleSize = opts.getDProperty("optBottleSize");
		otherCost = opts.getDProperty("optMiscCost");
		
		bottleTemp = opts.getDProperty("optBottleTemp");
		servTemp = opts.getDProperty("optServTemp");
		targetVol = opts.getDProperty("optVolsCO2");
		// Its ugly, but eligant
		for (int i = 0; i < Database.getInstance().primeSugarDB.size(); i++) {
			if (((PrimeSugar)Database.getInstance().primeSugarDB.get(i)).getName().equals(opts.getProperty("optPrimingSugar"))) {
				primeSugar = ((PrimeSugar)Database.getInstance().primeSugarDB.get(i));
			}
		}
		
		//brewingSalts.addAll(Database.getInstance().saltDB);

		primeSugar.setUnits(opts.getProperty("optSugarU"));
		carbTempU = opts.getProperty("optCarbTempU");
		kegged = opts.getBProperty("optKegged");
		
		dilution = new DilutedRecipe();
		version = "";

		// trigger the first re-calc:
		setPostBoil(opts.getDProperty("optPostBoilVol"));
		setVolUnits(opts.getProperty("optSizeU"));
	}

	// Getters:
	public double getAlcohol() {
		return alcohol;
	}
	public String getAlcMethod() {
		return alcMethod;
	}
	public double getAttenuation() {
		return attenuation;
	}
	public int getBoilMinutes() {
		return boilMinutes;
	}
	public String getBottleU() {
		return bottleU;
	}
	public double getBottleSize() {
		return bottleSize;
	}
	public double getBUGU() {
		double bugu = 0.0;
		if ( getEstOg() != 1.0 ) { 
		   bugu = getIbu() / ((getEstOg() - 1) * 1000);
		}		
		return bugu;
	}
	public String getBrewer() {
		return brewer;
	}
	public String getComments() {
		return comments;
	}
	public double getColour() {
		if (colourMethod.equals("SRM"))
			return srm;
		else
			return ebc;
	}

	public String getColourMethod() {
		return colourMethod;
	}
	public GregorianCalendar getCreated() {
		return created;
	}
	public double getEfficiency() {
		return efficiency;
	}
	public double getEstOg() {
		return estOg;
	}
	public double getEstFg() {
		return estFg;
	}
	public double getEvap() {
		return evap;
	}
	public String getEvapMethod() {
		return evapMethod;
	}
	public double getKettleLoss() {
		return kettleLoss;
	}
	public Mash getMash() {
		return mash;
	}
	public double getIbu() {
		return ibu;
	}
	public String getIBUMethod() {
		return ibuCalcMethod;
	}
	public String getMaltUnits() {
		return maltUnits;
	}
	public double getMiscLoss() {
		return miscLoss;
	}
	public String getName() {
		return name;
	}
	public double getOtherCost() {
		return otherCost;
	}
	public double getPelletHopPct() {
		return pelletHopPct;
	}
	public double getPreBoilVol(String s) {
		return preBoilVol.getValueAs(s);
	}
	public double getPostBoilVol(String s) {
		return postBoilVol.getValueAs(s);
	}
	public double getSrm() {
		return srm;
	}
	public String getStyle() {
		return style.getName();
	}
	public Style getStyleObj() {
		return style;
	}
	public double getTotalHopsOz() {
		return totalHopsOz;
	}
	public double getTotalHops() {
		return Quantity.convertUnit("oz", hopUnits, totalHopsOz);
	}
	public double getTotalHopsCost() {
		return totalHopsCost;
	}
	public double getTotalMaltCost() {
		return totalMaltCost;
	}
	public double getTotalMashLbs() {
		return totalMashLbs;
	}
	public double getTotalMash() {
		return Quantity.convertUnit("lb", getMaltUnits(), totalMashLbs);
	}
	public double getTotalMaltLbs() {
		return totalMaltLbs;
	}
	public double getTotalMalt() {
		return Quantity.convertUnit("lb", maltUnits, totalMaltLbs);
	}
	public double getTotalMiscCost() {
		return totalMiscCost;
	}
	public double getTrubLoss() {
		return trubLoss;
	}
	public String getVolUnits() {
		return postBoilVol.getUnits();
	}
	public String getYeast() {
		return yeast.getName();
	}
	public Yeast getYeastObj() {
		return yeast;
	}
	public boolean isDiluted() {
		return diluted;
	}

	// water
	private double getVolConverted(double val) {
		double d = Quantity.convertUnit("qt", getVolUnits(), val);
		// String s = SBStringUtils.format(d, 1).toString();
		return d;
	}

	public double getTotalWater() {
		return (getVolConverted(totalWaterQTS));
	}
	public double getChillShrink() {
		return (getVolConverted(chillShrinkQTS));
	}
	public double getSparge() {
		// return (getVolConverted(spargeQTS));
		return mash.getSpargeVol();
	}
	public double getFinalWortVol() {
		return (getVolConverted(finalWortVolQTS));
	}
	
	public boolean getDirty() {
		return isDirty;
	}
	
	// Setters:
	
	// Set saved flag
	public void setDirty(boolean d) {
		isDirty = d;
	}

	/*
	 * Turn off allowRecalcs when you are importing a recipe, so that
	 * strange things don't happen.  BE SURE TO TURN BACK ON!
	 */
	public void setAllowRecalcs(boolean b) {
		allowRecalcs = b;
	}

	public void setAlcMethod(String s) {
		alcMethod = s;
		calcAlcohol(alcMethod);
	}
	public void setBoilMinutes(int b) {
		isDirty = true;
		boilMinutes = b;
		double post = 0;
		// JvH changing the boiltime, changes the post boil volume (NOT the pre
		// boil)
		if (evapMethod.equals("Constant")) {
			post = preBoilVol.getValue() - (evap * boilMinutes / 60);
		} else { // %
			post = preBoilVol.getValue()
					- (preBoilVol.getValue() * (evap / 100) * boilMinutes / 60);

		}

		setPostBoil(post);
	}
	public void setBottleSize(double b) {
		isDirty = true;
		bottleSize = b;
	}
	public void setBottleU(String u) {
		isDirty = true;
		bottleU = u;
	}
	public void setBrewer(String b) {
		isDirty = true;
		brewer = b;
	}
	public void setComments(String c) {
		isDirty = true;
		comments = c;
	}
	public void setColourMethod(String c) {
		isDirty = true;
		colourMethod = c;
		calcMaltTotals();
	}
	public void setCreated(Date d) {
		isDirty = true;
		created.setTime(d);
	}
	public void setDiluted(boolean b){
		isDirty = true;
		diluted = b;
	}
	public void setEvap(double e) {
		isDirty = true;
		evap = e;
		double post = 0;
		// JvH changing the evaporation, changes the post boil volume (NOT the
		// pre boil)
		if (evapMethod.equals("Constant")) {
			post = preBoilVol.getValue() - (evap * boilMinutes / 60);
		} else { // %
			post = preBoilVol.getValue()
					- (preBoilVol.getValue() * (evap / 100) * boilMinutes / 60);
		}
		postBoilVol.setAmount(post);
		calcMaltTotals();
		calcHopsTotals();
	}
	public void setEvapMethod(String e) {
		isDirty = true;
		evapMethod = e;
		setEvap(getEvap());
	}

	public void setHopsUnits(String h) {
		isDirty = true;
		hopUnits = h;
	}
	public void setIBUMethod(String s) {
		isDirty = true;
		ibuCalcMethod = s;
		calcHopsTotals();
	}
	public void setKettleLoss(double k) {
		isDirty = true;
		kettleLoss = k;
		calcMaltTotals();
	}
	public void setMaltUnits(String m) {
		isDirty = true;
		maltUnits = m;
	}
	public void setMashed(boolean m) {
		isDirty = true;
		mashed = m;
	}
	public void setMashRatio(double m) {
		isDirty = true;
		mash.setMashRatio(m);
	}
	public void setMashRatioU(String u) {
		isDirty = true;
		mash.setMashRatioU(u);
	}
	public void setMiscLoss(double m) {
		isDirty = true;
		miscLoss = m;
		calcMaltTotals();
	}
	public void setName(String n) {
		isDirty = true;
		name = n;
	}

	public void setOtherCost(double c) {
		isDirty = true;
		otherCost = c;
	}

	public void setPelletHopPct(double p) {
		isDirty = true;
		pelletHopPct = p;
		calcHopsTotals();
	}
	public void setStyle(String s) {
		isDirty = true;
		style.setName(s);
	}

	public void setStyle(Style s) {
		isDirty = true;
		style = s;
	}

	public void setTrubLoss(double t) {
		isDirty = true;
		trubLoss = t;
		calcMaltTotals();
	}
	public void setYeastName(String s) {
		isDirty = true;
		yeast.setName(s);
	}

	public void setYeast(Yeast y) {
		isDirty = true;
		yeast = y;
	}
	public void setVersion(String v) {
		isDirty = true;
		version = v;
	}

	// Fermentation Steps
	// Getters
	public int getFermentStepSize() {
		return fermentationSteps.size();
	}
	public String getFermentStepType(int i) {
		return ((FermentStep)fermentationSteps.get(i)).getType();
	}
	public int getFermentStepTime(int i) {
		return ((FermentStep)fermentationSteps.get(i)).getTime();
	}
	public double getFermentStepTemp(int i) {
		return ((FermentStep)fermentationSteps.get(i)).getTemp();
	}
	public String getFermentStepTempU(int i) {
		return ((FermentStep)fermentationSteps.get(i)).getTempU();
	}
	public FermentStep getFermentStep(int i) {
		return (FermentStep)fermentationSteps.get(i);
	}
	public int getTotalFermentTime() {
		return totalFermentTime;
	}
	// Setters
	public void setFermentStepType(int i, String s) {
		isDirty = true;
		((FermentStep)fermentationSteps.get(i)).setType(s);
		Collections.sort(fermentationSteps, new ingrComparator());		
	}
	public void setFermentStepTime(int i, int t) {
		isDirty = true;
		((FermentStep)fermentationSteps.get(i)).setTime(t);
		Collections.sort(fermentationSteps, new ingrComparator());		
	}
	public void setFermentStepTemp(int i, double d) {
		isDirty = true;
		((FermentStep)fermentationSteps.get(i)).setTemp(d);
	}
	public void setFermentStepTemptU(int i, String s) {
		isDirty = true;
		((FermentStep)fermentationSteps.get(i)).setTempU(s);
	}
	public void addFermentStep(FermentStep fs) {
		isDirty = true;
		fermentationSteps.add(fs);
		Collections.sort(fermentationSteps, new ingrComparator());
		calcFermentTotals();
	}
	public FermentStep delFermentStep(int i) {
		isDirty = true;
		FermentStep temp = null;
		if (!fermentationSteps.isEmpty() && i > -1) {
			temp = (FermentStep)fermentationSteps.remove(i);
			Collections.sort(fermentationSteps, new ingrComparator());
			calcFermentTotals();
		}
		
		return temp;
	}
	public void calcFermentTotals() {
		totalFermentTime = 0;
		for (int i = 0; i < fermentationSteps.size(); i++) {
			totalFermentTime += ((FermentStep)fermentationSteps.get(i)).getTime();
		}
	}
	
	// hop list get functions:
	public String getHopUnits() {
		return hopUnits;
	}
	public Hop getHop(int i) {
		return (Hop) hops.get(i);
	}
	public int getHopsListSize() {
		return hops.size();
	}
	public String getHopName(int i) {
		return ((Hop) hops.get(i)).getName();
	}
	public String getHopType(int i) {
		return ((Hop) hops.get(i)).getType();
	}
	public double getHopAlpha(int i) {
		return ((Hop) hops.get(i)).getAlpha();
	}
	public String getHopUnits(int i) {
		return ((Hop) hops.get(i)).getUnits();
	}
	public String getHopAdd(int i) {
		return ((Hop) hops.get(i)).getAdd();
	}
	public int getHopMinutes(int i) {
		return ((Hop) hops.get(i)).getMinutes();
	}
	public double getHopIBU(int i) {
		return ((Hop) hops.get(i)).getIBU();
	}
	public double getHopCostPerU(int i) {
		return ((Hop) hops.get(i)).getCostPerU();
	}
	public double getHopAmountAs(int i, String s) {
		return ((Hop) hops.get(i)).getAmountAs(s);
	}
	public String getHopDescription(int i) {
		return ((Hop) hops.get(i)).getDescription();
	}

	// hop list set functions
	public void setHopUnits(int i, String u) {
		isDirty = true;
		((Hop) hops.get(i)).setUnits(u);
	}
	public void setHopName(int i, String n) {
		isDirty = true;
		((Hop) hops.get(i)).setName(n);
	}
	public void setHopType(int i, String t) {
		isDirty = true;
		((Hop) hops.get(i)).setType(t);
	}
	public void setHopAdd(int i, String a) {
		isDirty = true;
		((Hop) hops.get(i)).setAdd(a);
	}
	public void setHopAlpha(int i, double a) {
		isDirty = true;
		((Hop) hops.get(i)).setAlpha(a);
	}
	public void setHopMinutes(int i, int m) {
		isDirty = true;
		// have to re-sort hops
		((Hop) hops.get(i)).setMinutes(m);
		Collections.sort(hops, new ingrComparator());
	}
	public void setHopCost(int i, String c) {
		isDirty = true;
		((Hop) hops.get(i)).setCost(c);
	}
	public void setHopAmount(int i, double a) {
		isDirty = true;
		((Hop) hops.get(i)).setAmount(a);
	}

	// fermentable get methods
	// public ArrayList getFermentablesList() { return fermentables; }
	public Fermentable getFermentable(int i) {
		if (i < fermentables.size())
			return (Fermentable) fermentables.get(i);
		else
			return null;
	}
	public int getMaltListSize() {
		return fermentables.size();
	}
	public String getMaltName(int i) {
		return ((Fermentable) fermentables.get(i)).getName();
	}
	public String getMaltUnits(int i) {
		return ((Fermentable) fermentables.get(i)).getUnits();
	}
	public double getMaltPppg(int i) {
		return ((Fermentable) fermentables.get(i)).getPppg();
	}
	public double getMaltLov(int i) {
		return ((Fermentable) fermentables.get(i)).getLov();
	}
	public double getMaltCostPerU(int i) {
		return ((Fermentable) fermentables.get(i)).getCostPerU();
	}
	public double getMaltPercent(int i) {
		return ((Fermentable) fermentables.get(i)).getPercent();
	}
	public double getMaltAmountAs(int i, String s) {
		return ((Fermentable) fermentables.get(i)).getAmountAs(s);
	}
	public String getMaltDescription(int i) {
		return ((Fermentable) fermentables.get(i)).getDescription();
	}
	public boolean getMaltMashed(int i) {
		return ((Fermentable) fermentables.get(i)).getMashed();
	}
	public boolean getMaltSteep(int i) {
		return ((Fermentable) fermentables.get(i)).getSteep();
	}

	// fermentable set methods
	public void setMaltName(int i, String n) {
		// have to re-sort
		isDirty = true;
		((Fermentable) fermentables.get(i)).setName(n);
		Collections.sort(fermentables, new ingrComparator());
	}
	public void setMaltUnits(int i, String u) {
		isDirty = true;
		((Fermentable) fermentables.get(i)).setUnits(u);
	}
	public void setMaltAmount(int i, double a) {
		isDirty = true;
		((Fermentable) fermentables.get(i)).setAmount(a);
	}
	public void setMaltAmountAs(int i, double a, String u) {
		isDirty = true;
		((Ingredient) fermentables.get(i)).setAmountAs(a, u);
	}
	public void setMaltPppg(int i, double p) {
		isDirty = true;
		((Fermentable) fermentables.get(i)).setPppg(p);
	}
	public void setMaltLov(int i, double l) {
		isDirty = true;
		((Fermentable) fermentables.get(i)).setLov(l);
	}
	public void setMaltCost(int i, String c) {
		isDirty = true;
		((Fermentable) fermentables.get(i)).setCost(c);
	}
	public void setMaltPercent(int i, double p) {
		isDirty = true;
		((Fermentable) fermentables.get(i)).setPercent(p);
	}
	public void setMaltSteep(int i, boolean c) {
		isDirty = true;
		((Fermentable) fermentables.get(i)).setSteep(c);
	}
	public void setMaltMashed(int i, boolean c) {
		isDirty = true;
		((Fermentable) fermentables.get(i)).setMashed(c);
	}

	// misc get/set functions
	public int getMiscListSize() {
		return misc.size();
	}
	public Misc getMisc(int i) {
		return (Misc) misc.get(i);
	}
	public String getMiscName(int i) {
		return ((Misc) misc.get(i)).getName();
	}
	public void setMiscName(int i, String n) {
		isDirty = true;
		((Misc) misc.get(i)).setName(n);
	}
	public double getMiscAmount(int i) {
		Misc m = ((Misc) misc.get(i));
		return m.getAmountAs(m.getUnits());
	}
	public void setMiscAmount(int i, double a) {
		isDirty = true;
		((Misc) misc.get(i)).setAmount(a);
		calcMiscCost();
	}
	public String getMiscUnits(int i) {
		return ((Misc) misc.get(i)).getUnits();
	}
	public void setMiscUnits(int i, String u) {
		isDirty = true;
		((Misc) misc.get(i)).setUnits(u);
		calcMiscCost();
	}
	public double getMiscCost(int i) {
		return ((Misc) misc.get(i)).getCostPerU();
	}
	public void setMiscCost(int i, double c) {
		isDirty = true;
		((Misc) misc.get(i)).setCost(c);
		calcMiscCost();
	}
	public String getMiscStage(int i) {
		return ((Misc) misc.get(i)).getStage();
	}
	public void setMiscStage(int i, String s) {
		isDirty = true;
		((Misc) misc.get(i)).setStage(s);
	}
	public int getMiscTime(int i) {
		return ((Misc) misc.get(i)).getTime();
	}
	public void setMiscTime(int i, int t) {
		isDirty = true;
		((Misc) misc.get(i)).setTime(t);
	}
	public String getMiscDescription(int i) {
		return ((Misc) misc.get(i)).getDescription();
	}
	public void setMiscComments(int i, String c) {
		isDirty = true;
		((Misc) misc.get(i)).setComments(c);
	}
	public String getMiscComments(int i) {
		return ((Misc) misc.get(i)).getComments();
	}

	// notes get/set methods
	public int getNotesListSize() {
		return notes.size();
	}
	public Date getNoteDate(int i) {
		return ((Note) notes.get(i)).getDate();
	}
	public void setNoteDate(int i, Date d) {
		isDirty = true;
		((Note) notes.get(i)).setDate(d);
	}
	public String getNoteType(int i) {
		return ((Note) notes.get(i)).getType();
	}
	public void setNoteType(int i, String t) {
		isDirty = true;
		((Note) notes.get(i)).setType(t);
	}
	public String getNoteNote(int i) {
		return ((Note) notes.get(i)).getNote();
	}
	public void setNoteNote(int i, String n) {
		isDirty = true;
		((Note) notes.get(i)).setNote(n);
	}

	// Setters that need to do extra work:

	public void setVolUnits(String v) {
		isDirty = true;
		preBoilVol.setUnits(v);
		postBoilVol.setUnits(v);
		calcMaltTotals();
		calcHopsTotals();

	}

	public void setEstFg(double f) {
		isDirty = true;
		if (f != estFg && f > 0) {
			estFg = f;
			attenuation = 100 - ((estFg - 1) / (estOg - 1) * 100);
			calcAlcohol(alcMethod);
		}
	}

	public void setEstOg(double o) {
		isDirty = true;
		if (o != estOg && o > 0) {
			estOg = o;
			attenuation = 100 - ((estFg - 1) / (estOg - 1) * 100);
			calcEfficiency();
			calcAlcohol("Volume");
		}
	}

	public void setEfficiency(double e) {
		isDirty = true;
		if (e != efficiency && e > 0) {
			efficiency = e;
			calcMaltTotals();
		}
	}

	public void setAttenuation(double a) {
		isDirty = true;
		if (a != attenuation && a > 0) {
			attenuation = a;
			calcMaltTotals();
		}

	}
	public void setPreBoil(double p) {
		isDirty = true;
		preBoilVol.setAmount(p);

		double post = 0;
		if (evapMethod.equals("Constant")) {
			post = p - (evap * boilMinutes / 60);
		} else {
			post = p - (p * (evap / 100) * boilMinutes / 60);
		}

		postBoilVol.setAmount(post);
		calcMaltTotals();
		calcHopsTotals();
		dilution.calcDilution();
	}

	public void setPostBoil(double p) {
		isDirty = true;
		postBoilVol.setAmount(p);

		double pre = 0;
		if (evapMethod.equals("Constant")) {
			pre = p + (evap * boilMinutes / 60);
		} else {
			// pre = p + (p * (evap / 100) * boilMinutes / 60);
			pre = p / ((1 - ((evap / 100) * boilMinutes / 60)));
		}
		preBoilVol.setAmount(pre);
		calcMaltTotals();
		calcHopsTotals();
		if (!diluted) {
			dilution.setDilVol(p);
		} else {
			dilution.calcDilution();
		}
	}

	public void setFinalWortVol(double p) {
		isDirty = true;
		// convert to QTS and set the final vol
		finalWortVolQTS = Quantity.convertUnit(getVolUnits(), "qt", p);
		// now add in all the losses, and set the post-boil to force
		// a recalc:
		double d = finalWortVolQTS + Quantity.convertUnit(getVolUnits(), "qt", kettleLoss)
				+ Quantity.convertUnit(getVolUnits(), "qt", trubLoss)
				+ Quantity.convertUnit(getVolUnits(), "qt", miscLoss);
		// add in chill shrink:
		d = d / (1 - CHILLPERCENT);
		setPostBoil(Quantity.convertUnit("qt", getVolUnits(), d));
	}

	/*
	 * Functions that add/remove from ingredient lists
	 */
	public void addMalt(Fermentable m) {
		isDirty = true;
		fermentables.add(m);
		Collections.sort(fermentables, new ingrComparator());
		calcMaltTotals();
	}
	public void delMalt(int i) {
		isDirty = true;
		if (!fermentables.isEmpty() && i > -1) {
			fermentables.remove(i);
			calcMaltTotals();
		}
	}
	public void addHop(Hop h) {
		isDirty = true;
		hops.add(h);
		Collections.sort(hops, new ingrComparator());
		calcHopsTotals();
	}
	public void delHop(int i) {
		isDirty = true;
		if (!hops.isEmpty() && i > -1) {
			hops.remove(i);
			calcHopsTotals();
		}
	}
	public void addMisc(Misc m) {
		isDirty = true;
		misc.add(m);
		calcMiscCost();
	}

	public void delMisc(int i) {
		isDirty = true;
		if (!misc.isEmpty() && i > -1) {
			misc.remove(i);
			calcMiscCost();
		}
	}

	private void calcMiscCost() {
		totalMiscCost = 0;
		for (int i = 0; i < misc.size(); i++) {
			Misc m = (Misc) misc.get(i);
			totalMiscCost += m.getAmountAs(m.getUnits()) * m.getCostPerU();
		}
	}

	public void addNote(Note n) {
		isDirty = true;
		notes.add(n);
	}

	public void delNote(int i) {
		isDirty = true;
		if (!notes.isEmpty() && i > -1) {
			notes.remove(i);
		}
	}

	/**
	 * Handles a string of the form "d u", where d is a double amount, and u is
	 * a string of units. For importing the quantity attribute from QBrew xml.
	 * 
	 * @param a
	 */
	public void setAmountAndUnits(String a) {
		isDirty = true;
		int i = a.indexOf(" ");
		String d = a.substring(0, i);
		String u = a.substring(i);
		// preBoilVol.setAmount(Double.parseDouble(d.trim()));
		preBoilVol.setUnits(u.trim());
		// postBoilVol.setAmount(Double.parseDouble(d.trim()));
		postBoilVol.setUnits(u.trim());
		setPostBoil(Double.parseDouble(d.trim()));

	}

	/**
	 * Calculate all the malt totals from the array of malt objects TODO: Other
	 * things to implement: - cost tracking - hopped malt extracts (IBUs) - the %
	 * that this malt represents - error checking
	 * 
	 * @return
	 */

	// Calc functions.
	private void calcEfficiency() {
		double possiblePoints = 0;
		for (int i = 0; i < fermentables.size(); i++) {
			Fermentable m = ((Fermentable) fermentables.get(i));
			possiblePoints += (m.getPppg() - 1) * m.getAmountAs("lb")
					/ postBoilVol.getValueAs("gal");
		}
		efficiency = (estOg - 1) / possiblePoints * 100;
	}

	public void calcMaltTotals() {

		if (!allowRecalcs)
			return;
		double maltPoints = 0;
		double mcu = 0;
		totalMaltLbs = 0;
		totalMaltCost = 0;
		totalMashLbs = 0;

		// first figure out the total we're dealing with
		for (int i = 0; i < fermentables.size(); i++) {
			Fermentable m = ((Fermentable) fermentables.get(i));
			totalMaltLbs += (m.getAmountAs("lb"));
			if (m.getMashed()) { // apply efficiency and add to mash weight
				maltPoints += (m.getPppg() - 1) * m.getAmountAs("lb") * efficiency
						/ postBoilVol.getValueAs("gal");
				totalMashLbs += (m.getAmountAs("lb"));
			} else
				maltPoints += (m.getPppg() - 1) * m.getAmountAs("lb") * 100
						/ postBoilVol.getValueAs("gal");

			mcu += m.getLov() * m.getAmountAs("lb") / postBoilVol.getValueAs("gal");
			totalMaltCost += m.getCostPerU() * m.getAmountAs(m.getUnits());
		}

		// now set the malt % by weight:
		for (int i = 0; i < fermentables.size(); i++) {
			Fermentable m = ((Fermentable) fermentables.get(i));
			if (m.getAmountAs("lb") == 0)
				m.setPercent(0);
			else
				m.setPercent((m.getAmountAs("lb") / totalMaltLbs * 100));
		}

		// set the fields in the object
		estOg = (maltPoints / 100) + 1;
		estFg = 1 + ((estOg - 1) * ((100 - attenuation) / 100));
		srm = calcColour(mcu, "SRM");
		ebc = calcColour(mcu, "EBC");
		// mash.setMaltWeight(totalMashLbs);

		calcAlcohol(getAlcMethod());

		// do the water calcs w/ the updated mash:
		chillShrinkQTS = getPostBoilVol("qt") * CHILLPERCENT;
		// spargeQTS = getPreBoilVol("qt") - (mash.getTotalWaterQts() - mash.getAbsorbedQts());
		totalWaterQTS = mash.getTotalWaterQts() + mash.getSpargeVol();

		finalWortVolQTS = postBoilVol.getValueAs("qt")
				- (chillShrinkQTS + Quantity.convertUnit(getVolUnits(), "qt", kettleLoss)
						+ Quantity.convertUnit(getVolUnits(), "qt", trubLoss) + Quantity
						.convertUnit(getVolUnits(), "qt", miscLoss));

	}
	
	public void calcHopsTotals() {

		if (!allowRecalcs)
			return;
		double ibuTotal = 0;
		totalHopsCost = 0;
		totalHopsOz = 0;

		for (int i = 0; i < hops.size(); i++) {
			// calculate the average OG of the boil
			// first, the OG at the time of addition:
			double adjPreSize, aveOg = 0;
			Hop h = ((Hop) hops.get(i));			

			int time = h.getMinutes();
			if (h.getAdd().equalsIgnoreCase("FWH")) {
				time = time - fwhTime;
			} else if (h.getAdd().equalsIgnoreCase("Mash")) {
				time = mashHopTime;
			} else if (h.getAdd().equalsIgnoreCase("Dry")) {
				time = dryHopTime;
			}

			if (h.getMinutes() > 0)
				adjPreSize = postBoilVol.getValueAs("gal")
						+ (preBoilVol.getValueAs("gal") - postBoilVol.getValueAs("gal"))
						/ (boilMinutes / h.getMinutes());
			else
				adjPreSize = postBoilVol.getValueAs("gal");

			aveOg = 1 + (((estOg - 1) + ((estOg - 1) / (adjPreSize / postBoilVol.getValueAs("gal")))) / 2);

			if (ibuCalcMethod.equals("Tinseth"))
				h.setIBU(calcTinseth(h.getAmountAs("oz"), postBoilVol.getValueAs("gal"), aveOg,
						time, h.getAlpha(), ibuHopUtil));
			else if (ibuCalcMethod.equals("Rager"))
				h.setIBU(CalcRager(h.getAmountAs("oz"), postBoilVol.getValueAs("gal"), aveOg, time,
						h.getAlpha()));
			else
				h.setIBU(CalcGaretz(h.getAmountAs("oz"), postBoilVol.getValueAs("gal"), aveOg,
						time, preBoilVol.getValueAs("gal"), 1, h.getAlpha()));
			if (h.getType().equalsIgnoreCase("Pellet")) {
				h.setIBU(h.getIBU() * (1.0 + (pelletHopPct / 100)));
			}

			ibuTotal += h.getIBU();
			
			totalHopsCost += h.getCostPerU() * h.getAmountAs(h.getUnits());
			totalHopsOz += h.getAmountAs("oz");
		}

		ibu = ibuTotal;

	}

	// private calculation functions:
	private double calcColour(double lov, String method) {
		double colour = 0;

		if (method.equals("EBC")) {
			// From Greg Noonan's article at
			// http://brewingtechniques.com/bmg/noonan.html
			colour = 1.4922 * Math.pow(lov, 0.6859); // SRM
			// EBC is apr. SRM * 2.65 - 1.2
			colour = (colour * 2.65) - 1.2;
		} else {
			// calculates SRM based on MCU (degrees LOV)
			if (lov > 0)
				colour = 1.4922 * Math.pow(lov, 0.6859);
			else
				colour = 0;
		}

		return colour;

	}

	private void calcAlcohol(String method) {
		double oPlato = sGToPlato(estOg);
		double fPlato = sGToPlato(estFg);
		double q = 0.22 + 0.001 * oPlato;
		double re = (q * oPlato + fPlato) / (1.0 + q);
		// calculate by weight:
		alcohol = (oPlato - re) / (2.0665 - 0.010665 * oPlato);
		if (method.equalsIgnoreCase("Volume")) // convert to by volume
			alcohol = alcohol * estFg / 0.794;

	}

	private double sGToPlato(double sg) {
		// function to convert a value in specific
		// gravity as plato
		// equation based on HBD#3204 post by AJ DeLange
		double Plato;
		Plato = -616.989 + 1111.488 * sg - 630.606 * sg * sg + 136.10305 * sg * sg * sg;
		return Plato;
	}

	/*
	 * Hop IBU calculation methods:
	 */
	private double calcTinseth(double amount, double size, double sg, double time, double aa,
			double HopsUtil) {
		double daautil; // decimal alpha acid utilization
		double bigness; // bigness factor
		double boil_fact; // boil time factor
		double mgl_aaa; // mg/l of added alpha units
		double ibu;

		bigness = 1.65 * (Math.pow(0.000125, (sg - 1))); // 0.000125 original
		boil_fact = (1 - (Math.exp(-0.04 * time))) / HopsUtil;
		daautil = bigness * boil_fact;
		mgl_aaa = (aa / 100) * amount * 7490 / size;
		ibu = daautil * mgl_aaa;
		return ibu;
	}

	// rager method of ibu calculation
	// constant 7962 is corrected to 7490 as per hop faq
	double CalcRager(double amount, double size, double sg, double time, double AA) {
		double ibu, utilization, ga;
		// should be tanh:
		double x = (time - 31.32) / 18.27;
		// tanh:
		double tanhx = (Math.exp(x) - Math.exp(-x)) / (Math.exp(x) + Math.exp(-x));
		utilization = 18.11 + (13.86 * tanhx / 18.27);
		ga = sg < 1.050 ? 0.0 : ((sg - 1.050) / 0.2);
		ibu = amount * (utilization / 100) * (AA / 100.0) * 7490;
		ibu /= size * (1 + ga);
		return ibu;
	}

	// utilization table for average floc yeast
	int util[] = {0, 0, 0, 0, 0, 0, 1, 1, 1, 3, 4, 5, 5, 6, 7, 9, 11, 13, 11, 13, 16, 13, 16, 19,
			15, 19, 23, 16, 20, 24, 17, 21, 25};

	double CalcGaretz(double amount, double size, double sg, double time, double start_vol,
			int yeast_flocc, double AA) {
		// iterative value seed - adjust to loop through value
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
				util_index = 10;
			else
				util_index = (int) (time / 5.0);
			utilization = util[(util_index * 3) + yeast_flocc];
			combined_f = gravity_f * temp_f * hopping_f;
			ibu = (utilization * AA * amount * 0.749) / (size * combined_f);
			desired_ibu = ibu;
		}

		return ibu;
	}

	// RGB estimation:

	public static Color calcRGB(int method, double srm, int rConst, int gConst, int bConst,
			int aConst) {
		// estimates the RGB equivalent colour for beer based on SRM
		// NOTE: optRed, optGreen and optBlue need to be adjusted for different
		// monitors.
		// This is from the Windows version of SB
		// typical values are: r=8, g=30, b=20

		if (method == 1) {

			int R = 0, G = 0, B = 0, A = aConst;

			if (srm < 10) {
				R = 255;
			} else {
				R = new Double(255 - ((srm - rConst) * 10)).intValue();
			}

			if (gConst != 0)
				G = new Double(250 - ((srm / gConst) * 250)).intValue();
			else
				G = new Double((250 - ((srm / 30) * 250))).intValue();
			B = new Double(200 - (srm * bConst)).intValue();

			if (R < 0)
				R = 1;
			if (G < 0)
				G = 1;
			if (B < 0)
				B = 1;

			return new Color(R, G, B, A);

		} else {
			// this is the "new" way, based on rbg screen samples from Gibby
			double R, G, B;

			if (srm < 11) {
				R = 259.13 + -7.42 * srm;
				G = 278.87 + -15.12 * srm;
				B = 82.73 + -4.48 * srm;
			} else if (srm >= 11 && srm < 21) {
				R = 335.27 + -11.73 * srm;
				G = 203.42 + -7.58 * srm;
				B = 92.50 + -2.90 * srm;
			} else {
				R = 220.20 + -7.20 * srm;
				G = 109.42 + -3.42 * srm;
				B = 50.75 + -1.33 * srm;
			}

			int r = new Double(R).intValue();
			int b = new Double(B).intValue();
			int g = new Double(G).intValue();
			if (r < 0)
				r = 0;
			if (r > 255)
				r = 255;
			if (g < 0)
				g = 0;
			if (g > 255)
				g = 255;
			if (b < 0)
				b = 0;
			if (b > 255)
				b = 255;

			return new Color(r, g, b, aConst);
		}
	}

	private String addXMLHeader(String in) {
		in = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n" + in;
		return in;
	}

	public String toXML(String printOptions) {
		StringBuffer sb = new StringBuffer();
		sb.append("<STRANGEBREWRECIPE version = \"" + version + "\">\n");
		sb.append("<!-- This is a SBJava export.  StrangeBrew 1.8 will not import it. -->\n");
		if (printOptions != null)
			sb.append(printOptions);
		sb.append("  <DETAILS>\n");
		sb.append("  <NAME>" + SBStringUtils.subEntities(name) + "</NAME>\n");
		sb.append("  <BREWER>" + SBStringUtils.subEntities(brewer) + "</BREWER>\n");
		sb.append("  <NOTES>" + SBStringUtils.subEntities(comments) + "</NOTES>\n");
		sb.append("  <EFFICIENCY>" + efficiency + "</EFFICIENCY>\n");
		sb.append("  <OG>" + SBStringUtils.format(estOg, 3) + "</OG>\n");
		sb.append("  <FG>" + SBStringUtils.format(estFg, 3) + "</FG>\n");
		sb.append("  <STYLE>" + style.getName() + "</STYLE>\n");
		sb.append("  <MASH>" + mashed + "</MASH>\n");
		// TODO: ebc vs srm
		sb.append("  <LOV>" + SBStringUtils.format(srm, 1) + "</LOV>\n");
		sb.append("  <IBU>" + SBStringUtils.format(ibu, 1) + "</IBU>\n");
		sb.append("  <ALC>" + SBStringUtils.format(alcohol, 1) + "</ALC>\n");
		sb.append("  <!-- SBJ1.0 Extensions: -->\n");
		sb.append("  <EVAP>" + evap + "</EVAP>\n");
		sb.append("  <EVAP_METHOD>" + evapMethod + "</EVAP_METHOD>\n");
		sb.append("  <!-- END SBJ1.0 Extensions -->\n");
		sb.append("  <BOIL_TIME>" + boilMinutes + "</BOIL_TIME>\n");
		sb.append("  <PRESIZE>" + preBoilVol.getValue() + "</PRESIZE>\n");
		sb.append("  <SIZE>" + postBoilVol.getValue() + "</SIZE>\n");
		sb.append("  <SIZE_UNITS>" + postBoilVol.getUnits() + "</SIZE_UNITS>\n");
		sb.append("  <ADDED_VOLUME>" + dilution.getAddVol() + "</ADDED_VOLUME>\n");
		sb.append("  <MALT_UNITS>" + maltUnits + "</MALT_UNITS>\n");
		sb.append("  <HOPS_UNITS>" + hopUnits + "</HOPS_UNITS>\n");
		sb.append("  <YEAST>" + yeast.getName() + "</YEAST>\n");
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		sb.append("  <RECIPE_DATE>" + df.format(created.getTime()) + "</RECIPE_DATE>\n");
		sb.append("  <ATTENUATION>" + attenuation + "</ATTENUATION>\n");
		sb.append("  <!-- SBJ1.0 Extensions: -->\n");
		sb.append("  <ALC_METHOD>" + alcMethod + "</ALC_METHOD>\n");
		sb.append("  <IBU_METHOD>" + ibuCalcMethod + "</IBU_METHOD>\n");
		sb.append("  <COLOUR_METHOD>" + colourMethod + "</COLOUR_METHOD>\n");
		sb.append("  <KETTLE_LOSS>" + kettleLoss + "</KETTLE_LOSS>\n");
		sb.append("  <TRUB_LOSS>" + trubLoss + "</TRUB_LOSS>\n");
		sb.append("  <MISC_LOSS>" + miscLoss + "</MISC_LOSS>\n");
		sb.append("  <PELLET_HOP_PCT>" + pelletHopPct + "</PELLET_HOP_PCT>\n");
		sb.append("  <YEAST_COST>" + yeast.getCostPerU() + "</YEAST_COST>\n");
		sb.append("  <OTHER_COST>" + otherCost + "</OTHER_COST>\n");
		sb.append("  <!-- END SBJ1.0 Extensions -->\n");
		sb.append("  </DETAILS>\n");

		// fermentables list:
		sb.append("  <FERMENTABLES>\n");
		sb.append(SBStringUtils.xmlElement("TOTAL", ""
				+ Quantity.convertUnit("lb", maltUnits, totalMaltLbs), 4));
		for (int i = 0; i < fermentables.size(); i++) {
			Fermentable m = (Fermentable) fermentables.get(i);
			sb.append(m.toXML());
		}
		sb.append("  </FERMENTABLES>\n");

		// hops list:
		sb.append("  <HOPS>\n");
		sb.append(SBStringUtils.xmlElement("TOTAL", ""
				+ Quantity.convertUnit("oz", hopUnits, totalHopsOz), 4));
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

		// Fermentation Schedual
		sb.append("   <FERMENTATION_SCHEDUAL>\n");
		for (int i = 0; i < fermentationSteps.size(); i++ ) {
			sb.append(((FermentStep)fermentationSteps.get(i)).toXML());
		}
		sb.append("   </FERMENTATION_SCHEDUAL>\n");

		// Carb
		sb.append("   <CARB>\n");
		sb.append("      <BOTTLETEMP>" + SBStringUtils.format(bottleTemp, 1) + "</BOTTLETEMP>\n");
		sb.append("      <SERVTEMP>" + SBStringUtils.format(servTemp, 1) + "</SERVTEMP>\n");
		sb.append("      <VOL>" + SBStringUtils.format(targetVol, 1) + "</VOL>\n");
		sb.append("      <SUGAR>" + primeSugar.getName() + "</SUGAR>\n");
		sb.append("      <AMOUNT>" + SBStringUtils.format(primeSugar.getAmountAs(primeSugar.getUnits()), 1) + "</AMOUNT>\n");
		sb.append("      <SUGARU>" + primeSugar.getUnitsAbrv() + "</SUGARU>\n");
		sb.append("      <TEMPU>" + carbTempU + "</TEMPU>\n");
		sb.append("      <KEG>" + Boolean.toString(kegged) + "</KEG>\n");
		sb.append("      <PSI>" + SBStringUtils.format(kegPSI, 2) + "</PSI>\n");
		sb.append("   </CARB>\n");	
	
		// notes list:
		sb.append("  <NOTES>\n");
		for (int i = 0; i < notes.size(); i++) {
			sb.append(((Note) notes.get(i)).toXML());
		}
		sb.append("  </NOTES>\n");

		// style xml:
		sb.append(style.toXML());

		sb.append("</STRANGEBREWRECIPE>");

		return addXMLHeader(sb.toString());
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

	public String toText() {
		MessageFormat mf;
		StringBuffer sb = new StringBuffer();
		sb.append("StrangeBrew J v." + version + " recipe text output\n\n");
		sb.append("Details:\n");
		sb.append("Name: " + name + "\n");
		sb.append("Brewer: " + brewer + "\n");
		sb.append("Size: " + SBStringUtils.format(postBoilVol.getValue(), 1) + " "
				+ postBoilVol.getUnits() + "\n");
		sb.append("Style: " + style.getName() + "\n");
		mf = new MessageFormat(
				"OG: {0,number,0.000},\tFG:{1,number,0.000}, \tAlc:{2,number,0.0}, \tIBU:{3,number,0.0}\n");
		Object[] objs = {new Double(estOg), new Double(estFg), new Double(alcohol), new Double(ibu)};
		sb.append(mf.format(objs));
		sb.append("(Alc method: by " + alcMethod + "; IBU method: " + ibuCalcMethod + ")\n");
		sb.append("\nYeast: " + yeast + "\n");		
		sb.append("\nFermentables:\n");
		sb.append(padLeft("Name ", 30, ' ') + " amount units  pppg    lov   %\n");

		mf = new MessageFormat("{0} {1} {2} {3,number,0.000} {4} {5}%\n");
		for (int i = 0; i < fermentables.size(); i++) {
			Fermentable f = (Fermentable) fermentables.get(i);

			Object[] objf = {padLeft(f.getName(), 30, ' '),
					padRight(" " + SBStringUtils.format(f.getAmountAs(f.getUnits()), 2), 6, ' '),
					padRight(" " + f.getUnitsAbrv(), 5, ' '), new Double(f.getPppg()),
					padRight(" " + SBStringUtils.format(f.getLov(), 1), 6, ' '),
					padRight(" " + SBStringUtils.format(f.getPercent(), 1), 5, ' ')};
			sb.append(mf.format(objf));

		}

		sb.append("\nHops:\n");
		sb.append(padLeft("Name ", 20, ' ') + " amount units  Alpha    Min   IBU\n");

		mf = new MessageFormat("{0} {1} {2} {3} {4} {5}\n");
		for (int i = 0; i < hops.size(); i++) {
			Hop h = (Hop) hops.get(i);

			Object[] objh = {padLeft(h.getName(), 20, ' '),
					padRight(" " + SBStringUtils.format(h.getAmountAs(h.getUnits()), 2), 6, ' '),
					padRight(" " + h.getUnitsAbrv(), 5, ' '), padRight(" " + h.getAlpha(), 6, ' '),
					padRight(" " + SBStringUtils.format(h.getMinutes(), 1), 6, ' '),
					padRight(" " + SBStringUtils.format(h.getIBU(), 1), 5, ' ')};
			sb.append(mf.format(objh));

		}

		sb.append("\nMash:\n");
		sb.append(padLeft("Step ", 10, ' ') + "  Temp   End    Ramp    Min\n");

		mf = new MessageFormat("{0} {1} {2} {3} {4}\n");
		for (int i = 0; i < mash.getStepSize(); i++) {

			Object[] objm = {padLeft(mash.getStepType(i), 10, ' '),
					padRight(" " + mash.getStepStartTemp(i), 6, ' '),
					padRight(" " + mash.getStepEndTemp(i), 6, ' '),
					padRight(" " + mash.getStepRampMin(i), 4, ' '),
					padRight(" " + mash.getStepMin(i), 6, ' ')};
			sb.append(mf.format(objm));
		}
		
		// Fermentation Schedual
		sb.append("\nFermentation Schedual:\n");
		sb.append(padLeft("Step ", 10, ' ') + "  Time   Days\n");
		mf = new MessageFormat("{0} {1} {2}\n");
		for (int i = 0; i < fermentationSteps.size(); i++ ) {
			FermentStep f = (FermentStep)fermentationSteps.get(i);
				Object[] objm = {padLeft(f.getType(), 10, ' '),
								padRight(" " + f.getTime(), 6, ' '),
								padRight(" " + f.getTemp() + f.getTempU(), 6, ' ')};
			sb.append(mf.format(objm));
		}
		
		// Carb
		sb.append("\nCarbonation:  " + targetVol + " volumes CO2\n");
		sb.append(" Bottle Temp: " + bottleTemp + carbTempU + "  Serving Temp:" + servTemp + carbTempU + "\n");
		sb.append(" Primeing: " + primeSugar.getAmountAs(primeSugar.getUnits()) + primeSugar.getUnitsAbrv() +
				" of " + primeSugar.getName() + "\n");
		sb.append(" Or keg at: " + kegPSI + "PSI\n");
		
		sb.append("\nNotes:\n");
		for (int i = 0; i < notes.size(); i++) {
			sb.append(((Note) notes.get(i)).toString());
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
				int result = ((Fermentable) a).getName().compareTo(((Fermentable) b).getName());
				return (result == 0 ? -1 : result);
			} else if (a.getClass().getName().equalsIgnoreCase("strangebrew.Hop")) {
				// sort hop additions by minutes
				Integer a1 = new Integer(((Hop) a).getMinutes());
				Integer b1 = new Integer(((Hop) b).getMinutes());
				int result = a1.compareTo(b1);
				return (result == 0 ? -1 : result);
			} else if (a.getClass().getName().contains("FermentStep")) {
				// Turned off for now.. some wierdness with GUI
//				// Sort by type then by time
//				FermentStep fA = (FermentStep)a;
//				FermentStep fB = (FermentStep)b;
//				int result = FermentStep.getTypeIndex(fA.getType()) - FermentStep.getTypeIndex(fB.getType());
//				
//				if (result == 0) {
//					result = fA.getTime() - fB.getTime();
//				}				
				return 0;
			} else
				return 0;
		}

	}

	public class DilutedRecipe {
		private double dilOG;
		private double dilIbu;
		private double dilAlc;
		private double dilSrm;
		private Quantity dilVol;
		private Quantity addVol;

		// constructor
		public DilutedRecipe() {
			// initialize the amounts
			dilVol = new Quantity(getVolUnits(), postBoilVol.getValue());
			addVol = new Quantity(getVolUnits(), 0);
		}

		public double getDilSrm() {
			if ( addVol.getValue() > 0 ) {
			   return dilSrm;
			} else { 
				return srm;
			}
		}
		public double getAddVol() {
			if ( addVol.getValue() > 0 ) {
			   return addVol.getValueAs(getVolUnits());
			} else { 
				return 0;
			}
		}

		public void setAddVol(double a) {
			addVol.setAmount(a);
			addVol.setUnits(getVolUnits());
			dilVol.setAmount(postBoilVol.getValue() + addVol.getValue());
			dilVol.setUnits(getVolUnits());
			calcDilution();
		}

		public double getDilAlc() {
			if ( addVol.getValue() > 0 ) {
			    return dilAlc;
			} else {
				return alcohol;
			}
		}

		public void setDilAlc(double dilAlc) {
			this.dilAlc = dilAlc;
		}

		public double getDilIbu() {
			if ( addVol.getValue() >  0 ) {
		    	return dilIbu;
			} else {
				return ibu;
			}
		}

		public void setDilIbu(double d) {
			double dilutionFactor = d / ibu;
			setDilVol(postBoilVol.getValue() / dilutionFactor);

		}

		public double getDilOG() {
			if ( addVol.getValue() > 0 ) {
			    return dilOG;
			} else {
				return estOg;
			}
		}

		public void setDilOG(double d) {
			double dilutionFactor = (d - 1) / (estOg - 1);
			setDilVol(postBoilVol.getValue() / dilutionFactor);
		}

		public double getDilVol() {
			if ( addVol.getValue() > 0 ) {
			    return dilVol.getValueAs(getVolUnits());
			} else { 
				return postBoilVol.getValueAs(getVolUnits());
			}
		}

		public void setDilVol(double d) {
			this.dilVol.setAmount(d);
			this.dilVol.setUnits(getVolUnits());
			addVol.setAmount(dilVol.getValue() - postBoilVol.getValue());
			addVol.setUnits(getVolUnits());
			calcDilution();
		}

		/*
		 * Calculates the diluted values, assuming that the diluted volume has
		 * been set
		 */
		private void calcDilution() {
			double dilutionFactor = dilVol.getValue() / postBoilVol.getValue();
			dilIbu = ibu / dilutionFactor;
			dilAlc = alcohol / dilutionFactor;
			dilOG = ((estOg - 1) / dilutionFactor) + 1;
			dilSrm = srm / dilutionFactor;

		}

	}

	/*
	 * Scale the recipe up or down, so that the new OG = old OG, and
	 * new IBU = old IBU
	 */
	public void scaleRecipe(double newSize, String newUnits) {
		double currentSize = getPostBoilVol(getVolUnits());
		currentSize = Quantity.convertUnit(getVolUnits(), newUnits, currentSize);
		double conversionFactor = newSize / currentSize;

		if (conversionFactor != 1) {
			setPostBoil(newSize);
			setVolUnits(newUnits);

			// TODO: figure out a way to make sure old IBU = new IBU
			for (int i = 0; i < getHopsListSize(); i++) {
				Hop h = getHop(i);
				h.setAmount(h.getAmountAs(h.getUnits()) * conversionFactor);
			}
			for (int i = 0; i < getMaltListSize(); i++) {
				Fermentable f = getFermentable(i);
				f.setAmount(f.getAmountAs(f.getUnits()) * conversionFactor);
			}
			calcHopsTotals();
			calcMaltTotals();

		}
	}

	public double getBottleTemp() {
		return bottleTemp;
	}

	public void setBottleTemp(double bottleTemp) {
		isDirty = true;
		this.bottleTemp = bottleTemp;
	}

	public String getCarbTempU() {
		return carbTempU;
	}

	public void setCarbTempU(String carbU) {
		isDirty = true;
		this.carbTempU = carbU;
	}

	public boolean isKegged() {
		return kegged;
	}

	public void setKegged(boolean kegged) {
		isDirty = true;
		this.kegged = kegged;
	}
	
	public double getKetPSI() {
		return this.kegPSI;
	}
	
	public void setKegPSI(double psi) {
		isDirty = true;
		this.kegPSI = psi;
	}

	public String getPrimeSugarName() {
		return primeSugar.getName();
	}
	
	public String getPrimeSugarU() {
		return primeSugar.getUnitsAbrv();
	}

	public void setPrimeSugarU(String primeU) {
		isDirty = true;
		this.primeSugar.setUnits(primeU);
	}

	public void setPrimeSugarName(String n) {
		isDirty = true;
		this.primeSugar.setName(n);
		// Name comes with Yeild! set it too
		ArrayList db = Database.getInstance().primeSugarDB;
		for (int i = 0; i < db.size(); i++) {
			if (n.equals(((PrimeSugar)db.get(i)).getName())) {
				this.primeSugar.setYield(((PrimeSugar)db.get(i)).getYield());
			}
		}
	}
	
	public void setPrimeSugarAmount(double q) {
		isDirty = true;
		this.primeSugar.setAmount(q);
	}
	
	public double getServTemp() {
		return servTemp;
	}

	public void setServTemp(double servTemp) {
		isDirty = true;
		this.servTemp = servTemp;
	}

	public double getTargetVol() {
		return targetVol;
	}

	public void setTargetVol(double targetVol) {
		isDirty = true;
		this.targetVol = targetVol;
	}

	public PrimeSugar getPrimeSugar() {
		return primeSugar;
	}

	public void setPrimeSugar(PrimeSugar primeSugar) {
		this.primeSugar = primeSugar;
	}

	public WaterProfile getSourceWater() {
		return sourceWater;
	}

	public WaterProfile getTargetWater() {
		return targetWater;
	}

	public void setSourceWater(WaterProfile sourceWater) {
		this.sourceWater = sourceWater;
	}

	public void setTargetWater(WaterProfile targetWater) {
		this.targetWater = targetWater;
	}

	public void addSalt(Salt s) {
		this.brewingSalts.add(s);
	}
	
	public void delSalt(Salt s) {
		this.brewingSalts.remove(s);
	}
	
	public void delSalt(int i) {
		this.brewingSalts.remove(i);
	}
	
	public ArrayList getSalts() {
		return this.brewingSalts;
	}
	
	public Salt getSalt(int i) {
		return (Salt)this.brewingSalts.get(i);
	}
}