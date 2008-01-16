/*
 * $Id: Recipe.java,v 1.67 2008/01/16 17:55:05 jimcdiver Exp $
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

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

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
	private List<Salt> brewingSalts = new ArrayList<Salt>();
	private Acid acid = Acid.getAcidByName(Acid.CITRIC);
	public Mash mash;
	
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
	
	// Carbonation
	private double bottleTemp;
	private double servTemp;
	private double targetVol;
	private PrimeSugar primeSugar = new PrimeSugar();
	private String carbTempU;
	private boolean kegged;
	private double kegPSI;

	// options:
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
	private List<Hop> hops = new ArrayList<Hop>();
	private List<Fermentable> fermentables = new ArrayList<Fermentable>();
	private List<Misc> misc = new ArrayList<Misc>();
	private List<FermentStep> fermentationSteps = new ArrayList<FermentStep>();
	// notes
	private List<Note> notes = new ArrayList<Note>();

	// default constuctor
	public Recipe() {

		Options opts = Options.getInstance();
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
			if (Database.getInstance().primeSugarDB.get(i).getName().equals(opts.getProperty("optPrimingSugar"))) {
				primeSugar = ((PrimeSugar)Database.getInstance().primeSugarDB.get(i));
			}
		}
		
		// TODO Load default Salt options!
		sourceWater = new WaterProfile(opts.getProperty("optWaterProfile"));
		//brewingSalts.addAll(Database.getInstance().saltDB);

		primeSugar.setUnits(opts.getProperty("optSugarU"));
		carbTempU = opts.getProperty("optCarbTempU");
		kegged = opts.getBProperty("optKegged");
		
		version = "";

		// trigger the first re-calc:
		setPostBoil(opts.getDProperty("optPostBoilVol"));
		setVolUnits(opts.getProperty("optSizeU"));
	}
	
	// Recipe copy constructor
	public Recipe(Recipe r) {
		this.version = r.version;

		this.isDirty = r.getDirty();
		this.allowRecalcs = r.allowRecalcs;
		this.alcohol = r.getAlcohol();
		this.attenuation = r.getAttenuation();
		this.boilMinutes = r.getBoilMinutes();
		this.brewer = r.getBrewer();
		this.comments = r.getComments();
		this.created = r.getCreated();
		this.efficiency = r.getEfficiency();
		this.estOg = r.getEstOg();
		this.estFg = r.getEstFg();
		this.evap = r.getEvap();
		this.ibu = r.getIbu();
		this.mashed = r.mashed;
		this.name = r.getName();
		this.preBoilVol = r.preBoilVol;
		this.postBoilVol = r.postBoilVol;
		this.srm = r.getSrm();
		this.ebc = r.ebc;
		this.style = r.getStyleObj();
		this.yeast = r.getYeastObj();
		this.sourceWater = r.getSourceWater();
		this.targetWater = r.getTargetWater();
		this.brewingSalts = new ArrayList<Salt>(r.getSalts());
		this.acid = r.getAcid();
		this.mash = r.mash;
			
		// water use:
		this.kettleLoss = r.getKettleLoss();
		this.trubLoss = r.getTrubLoss();
		this.miscLoss = r.getMiscLoss();		
		this.chillShrinkQTS = r.chillShrinkQTS;
		this.totalWaterQTS = r.totalWaterQTS;
		this.finalWortVolQTS = r.finalWortVolQTS;
		
		// Carbonation
		this.bottleTemp = r.getBottleTemp();
		this.servTemp = r.getServTemp();
		this.targetVol = r.getTargetVol();
		this.primeSugar = r.getPrimeSugar();
		this.carbTempU = r.getCarbTempU();
		this.kegged = r.isKegged();
		this.kegPSI = r.getKegPSI();		
		
		this.colourMethod = r.getColourMethod();
		this.hopUnits = r.getHopUnits();
		this.maltUnits = r.getMaltUnits();
		this.ibuCalcMethod = r.getIBUMethod();
		this.ibuHopUtil = r.ibuHopUtil;
		this.evapMethod = r.getEvapMethod();
		this.alcMethod = r.getAlcMethod();
		this.pelletHopPct = r.getPelletHopPct();
		this.bottleU = r.getBottleU();
		this.bottleSize = r.getBottleSize();
		this.otherCost = r.getOtherCost();
		this.fwhTime = r.fwhTime;
		this.dryHopTime = r.dryHopTime;
		this.mashHopTime = r.mashHopTime;
		
		// totals: (all should be pure calculation, not stored!!!
		this.totalMaltCost = r.getTotalMaltCost();
		this.totalHopsCost = r.getTotalHopsCost();
		this.totalMiscCost = r.getTotalMiscCost();
		this.totalMaltLbs = r.getTotalMaltLbs();
		this.totalHopsOz = r.getTotalHopsOz();
		this.totalMashLbs = r.getTotalMaltLbs();
		this.totalFermentTime = r.getTotalFermentTime();
			
			// ingredients
		this.hops = new ArrayList<Hop>(r.hops);
		this.fermentables = new ArrayList<Fermentable>(r.fermentables);
		this.misc = new ArrayList<Misc>(r.misc);
		// Fermentation
		this.fermentationSteps = new ArrayList<FermentStep>(r.fermentationSteps);		
		// notes
		this.notes = new ArrayList<Note>(r.notes);		
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
		if (colourMethod.equals(BrewCalcs.SRM)) {
			return srm;
		} else {
			return ebc;
		}
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
	public double getPreBoilVol(final String s) {
		return preBoilVol.getValueAs(s);
	}
	public double getPostBoilVol(final String s) {
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
		return Quantity.convertUnit(Quantity.OZ, hopUnits, totalHopsOz);
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
		return Quantity.convertUnit(Quantity.LB, getMaltUnits(), totalMashLbs);
	}
	public double getTotalMaltLbs() {
		return totalMaltLbs;
	}
	public double getTotalMalt() {
		return Quantity.convertUnit(Quantity.LB, maltUnits, totalMaltLbs);
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

	// water
	private double getVolConverted(final double val) {
		final double d = Quantity.convertUnit(Quantity.QT, getVolUnits(), val);
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
	public void setDirty(final boolean d) {
		isDirty = d;
	}

	/*
	 * Turn off allowRecalcs when you are importing a recipe, so that
	 * strange things don't happen.  BE SURE TO TURN BACK ON!
	 */
	public void setAllowRecalcs(final boolean b) {
		allowRecalcs = b;
	}

	public void setAlcMethod(final String s) {
		alcMethod = s;
		calcAlcohol(alcMethod);
	}
	public void setBoilMinutes(final int b) {
		isDirty = true;
		boilMinutes = b;
		double post = 0;
		// JvH changing the boiltime, changes the post boil volume (NOT the pre
		// boil)
		// TODO
		if (evapMethod.equals("Constant")) {
			post = preBoilVol.getValue() - (evap * boilMinutes / 60);
		} else { // %
			post = preBoilVol.getValue()
					- (preBoilVol.getValue() * (evap / 100) * boilMinutes / 60);

		}

		setPostBoil(post);
	}
	public void setBottleSize(final double b) {
		isDirty = true;
		bottleSize = b;
	}
	public void setBottleU(final String u) {
		isDirty = true;
		bottleU = u;
	}
	public void setBrewer(final String b) {
		isDirty = true;
		brewer = b;
	}
	public void setComments(final String c) {
		isDirty = true;
		comments = c;
	}
	public void setColourMethod(final String c) {
		isDirty = true;
		colourMethod = c;
		calcMaltTotals();
	}
	public void setCreated(final Date d) {
		isDirty = true;
		created.setTime(d);
	}

	public void setEvap(final double e) {
		isDirty = true;
		evap = e;
		double post = 0;
		// JvH changing the evaporation, changes the post boil volume (NOT the
		// pre boil)
		// TODO
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
	public void setEvapMethod(final String e) {
		isDirty = true;
		evapMethod = e;
		setEvap(getEvap());
	}

	public void setHopsUnits(final String h) {
		isDirty = true;
		hopUnits = h;
	}
	public void setIBUMethod(final String s) {
		isDirty = true;
		ibuCalcMethod = s;
		calcHopsTotals();
	}
	public void setKettleLoss(final double k) {
		isDirty = true;
		kettleLoss = k;
		calcMaltTotals();
	}
	public void setMaltUnits(final String m) {
		isDirty = true;
		maltUnits = m;
	}
	public void setMashed(final boolean m) {
		isDirty = true;
		mashed = m;
	}
	public void setMashRatio(final double m) {
		isDirty = true;
		mash.setMashRatio(m);
	}
	public void setMashRatioU(final String u) {
		isDirty = true;
		mash.setMashRatioU(u);
	}
	public void setMiscLoss(final double m) {
		isDirty = true;
		miscLoss = m;
		calcMaltTotals();
	}
	public void setName(final String n) {
		isDirty = true;
		name = n;
	}

	public void setOtherCost(final double c) {
		isDirty = true;
		otherCost = c;
	}

	public void setPelletHopPct(final double p) {
		isDirty = true;
		pelletHopPct = p;
		calcHopsTotals();
	}
	public void setStyle(final String s) {
		isDirty = true;
		style.setName(s);
	}

	public void setStyle(final Style s) {
		isDirty = true;
		style = s;
	}

	public void setTrubLoss(final double t) {
		isDirty = true;
		trubLoss = t;
		calcMaltTotals();
	}
	public void setYeastName(final String s) {
		isDirty = true;
		yeast.setName(s);
	}

	public void setYeast(final Yeast y) {
		isDirty = true;
		yeast = y;
	}
	public void setVersion(final String v) {
		isDirty = true;
		version = v;
	}

	// Fermentation Steps
	// Getters
	public int getFermentStepSize() {
		return fermentationSteps.size();
	}
	public String getFermentStepType(final int i) {
		return fermentationSteps.get(i).getType();
	}
	public int getFermentStepTime(final int i) {
		return fermentationSteps.get(i).getTime();
	}
	public double getFermentStepTemp(final int i) {
		return fermentationSteps.get(i).getTemp();
	}
	public String getFermentStepTempU(final int i) {
		return fermentationSteps.get(i).getTempU();
	}
	public FermentStep getFermentStep(final int i) {
		return fermentationSteps.get(i);
	}
	public int getTotalFermentTime() {
		return totalFermentTime;
	}
	// Setters
	public void setFermentStepType(final int i, final String s) {
		isDirty = true;
		fermentationSteps.get(i).setType(s);
		Collections.sort(fermentationSteps);		
	}
	public void setFermentStepTime(final int i, final int t) {
		isDirty = true;
		fermentationSteps.get(i).setTime(t);
		Collections.sort(fermentationSteps);		
	}
	public void setFermentStepTemp(final int i, final double d) {
		isDirty = true;
		fermentationSteps.get(i).setTemp(d);
	}
	public void setFermentStepTempU(final int i, final String s) {
		isDirty = true;
		fermentationSteps.get(i).setTempU(s);
	}
	public void addFermentStep(final FermentStep fs) {
		isDirty = true;
		fermentationSteps.add(fs);
		Collections.sort(fermentationSteps);
		calcFermentTotals();
	}
	public FermentStep delFermentStep(final int i) {
		isDirty = true;
		FermentStep temp = null;
		if (!fermentationSteps.isEmpty() && (i > -1)) {
			temp = fermentationSteps.remove(i);
			Collections.sort(fermentationSteps);
			calcFermentTotals();
		}
		
		return temp;
	}
	public void calcFermentTotals() {
		totalFermentTime = 0;
		for (int i = 0; i < fermentationSteps.size(); i++) {
			totalFermentTime += fermentationSteps.get(i).getTime();
		}
	}
	
	// hop list get functions:
	public String getHopUnits() {
		return hopUnits;
	}
	public Hop getHop(final int i) {
		return hops.get(i);
	}
	public int getHopsListSize() {
		return hops.size();
	}
	public String getHopName(final int i) {
		return hops.get(i).getName();
	}
	public String getHopType(final int i) {
		return hops.get(i).getType();
	}
	public double getHopAlpha(final int i) {
		return hops.get(i).getAlpha();
	}
	public String getHopUnits(final int i) {
		return hops.get(i).getUnits();
	}
	public String getHopAdd(final int i) {
		return hops.get(i).getAdd();
	}
	public int getHopMinutes(final int i) {
		return hops.get(i).getMinutes();
	}
	public double getHopIBU(final int i) {
		return  hops.get(i).getIBU();
	}
	public double getHopCostPerU(final int i) {
		return hops.get(i).getCostPerU();
	}
	public double getHopAmountAs(final int i, final String s) {
		return hops.get(i).getAmountAs(s);
	}
	public String getHopDescription(final int i) {
		return hops.get(i).getDescription();
	}

	// hop list set functions
	public void setHopUnits(final int i, final String u) {
		isDirty = true;
		hops.get(i).setUnits(u);
	}
	public void setHopName(final int i, final String n) {
		isDirty = true;
		hops.get(i).setName(n);
	}
	public void setHopType(final int i, final String t) {
		isDirty = true;
		hops.get(i).setType(t);
	}
	public void setHopAdd(final int i, final String a) {
		isDirty = true;
		hops.get(i).setAdd(a);
	}
	public void setHopAlpha(final int i, final double a) {
		isDirty = true;
		hops.get(i).setAlpha(a);
	}
	public void setHopMinutes(final int i, final int m) {
		isDirty = true;
		// have to re-sort hops
		hops.get(i).setMinutes(m);
		Collections.sort(hops);
	}
	public void setHopCost(final int i, final String c) {
		isDirty = true;
		hops.get(i).setCost(c);
	}
	public void setHopAmount(final int i, final double a) {
		isDirty = true;
		hops.get(i).setAmount(a);
	}

	// fermentable get methods
	// public ArrayList getFermentablesList() { return fermentables; }
	public Fermentable getFermentable(final int i) {
		if (i < fermentables.size()) {
			return (Fermentable) fermentables.get(i);
		} else {
			return null;
		}
	}
	public int getMaltListSize() {
		return fermentables.size();
	}
	public String getMaltName(final int i) {
		return fermentables.get(i).getName();
	}
	public String getMaltUnits(final int i) {
		return fermentables.get(i).getUnits();
	}
	public double getMaltPppg(final int i) {
		return fermentables.get(i).getPppg();
	}
	public double getMaltLov(final int i) {
		return fermentables.get(i).getLov();
	}
	public double getMaltCostPerU(final int i) {
		return fermentables.get(i).getCostPerU();
	}
	public double getMaltPercent(final int i) {
		return fermentables.get(i).getPercent();
	}
	public double getMaltAmountAs(final int i, final String s) {
		return fermentables.get(i).getAmountAs(s);
	}
	public String getMaltDescription(final int i) {
		return fermentables.get(i).getDescription();
	}
	public boolean getMaltMashed(final int i) {
		return fermentables.get(i).getMashed();
	}
	public boolean getMaltSteep(final int i) {
		return fermentables.get(i).getSteep();
	}

	// fermentable set methods
	public void setMaltName(final int i, final String n) {
		// have to re-sort
		isDirty = true;
		fermentables.get(i).setName(n);
		Collections.sort(fermentables);
	}
	public void setMaltUnits(final int i, final String u) {
		isDirty = true;
		fermentables.get(i).setUnits(u);
	}
	public void setMaltAmount(final int i, final double a) {
		isDirty = true;
		fermentables.get(i).setAmount(a);
	}
	public void setMaltAmountAs(final int i, final double a, final String u) {
		isDirty = true;
		fermentables.get(i).setAmountAs(a, u);
	}
	public void setMaltPppg(final int i, final double p) {
		isDirty = true;
		fermentables.get(i).setPppg(p);
	}
	public void setMaltLov(final int i, final double l) {
		isDirty = true;
		fermentables.get(i).setLov(l);
	}
	public void setMaltCost(final int i, final String c) {
		isDirty = true;
		fermentables.get(i).setCost(c);
	}
	public void setMaltPercent(final int i, final double p) {
		isDirty = true;
		fermentables.get(i).setPercent(p);
	}
	public void setMaltSteep(final int i, final boolean c) {
		isDirty = true;
		fermentables.get(i).setSteep(c);
	}
	public void setMaltMashed(final int i, final boolean c) {
		isDirty = true;
		fermentables.get(i).setMashed(c);
	}

	// misc get/set functions
	public int getMiscListSize() {
		return misc.size();
	}
	public Misc getMisc(final int i) {
		return misc.get(i);
	}
	public String getMiscName(final int i) {
		return  misc.get(i).getName();
	}
	public void setMiscName(final int i, final String n) {
		isDirty = true;
		 misc.get(i).setName(n);
	}
	public double getMiscAmount(final int i) {
		final Misc m =  misc.get(i);
		return m.getAmountAs(m.getUnits());
	}
	public void setMiscAmount(final int i, final double a) {
		isDirty = true;
		 misc.get(i).setAmount(a);
		calcMiscCost();
	}
	public String getMiscUnits(final int i) {
		return  misc.get(i).getUnits();
	}
	public void setMiscUnits(final int i, final String u) {
		isDirty = true;
		 misc.get(i).setUnits(u);
		calcMiscCost();
	}
	public double getMiscCost(final int i) {
		return  misc.get(i).getCostPerU();
	}
	public void setMiscCost(final int i, final double c) {
		isDirty = true;
		 misc.get(i).setCost(c);
		calcMiscCost();
	}
	public String getMiscStage(final int i) {
		return  misc.get(i).getStage();
	}
	public void setMiscStage(final int i, final String s) {
		isDirty = true;
		 misc.get(i).setStage(s);
	}
	public int getMiscTime(final int i) {
		return  misc.get(i).getTime();
	}
	public void setMiscTime(final int i, final int t) {
		isDirty = true;
		 misc.get(i).setTime(t);
	}
	public String getMiscDescription(final int i) {
		return  misc.get(i).getDescription();
	}
	public void setMiscComments(final int i, final String c) {
		isDirty = true;
		 misc.get(i).setComments(c);
	}
	public String getMiscComments(final int i) {
		return  misc.get(i).getComments();
	}

	// notes get/set methods
	public int getNotesListSize() {
		return notes.size();
	}
	public Date getNoteDate(final int i) {
		return  notes.get(i).getDate();
	}
	public void setNoteDate(final int i, final Date d) {
		isDirty = true;
		 notes.get(i).setDate(d);
	}
	public String getNoteType(final int i) {
		return  notes.get(i).getType();
	}
	public void setNoteType(final int i, final String t) {
		isDirty = true;
		if (i > -1) {
			notes.get(i).setType(t);
		}
	}
	public String getNoteNote(final int i) {
		return  notes.get(i).getNote();
	}
	public void setNoteNote(final int i, final String n) {
		isDirty = true;
		 notes.get(i).setNote(n);
	}

	// Setters that need to do extra work:

	public void setVolUnits(final String v) {
		isDirty = true;
		preBoilVol.setUnits(v);
		postBoilVol.setUnits(v);
		calcMaltTotals();
		calcHopsTotals();

	}

	public void setEstFg(final double f) {
		isDirty = true;
		if ((f != estFg) && (f > 0)) {
			estFg = f;
			attenuation = 100 - ((estFg - 1) / (estOg - 1) * 100);
			calcAlcohol(alcMethod);
		}
	}

	public void setEstOg(final double o) {
		isDirty = true;
		if ((o != estOg) && (o > 0)) {
			estOg = o;
			attenuation = 100 - ((estFg - 1) / (estOg - 1) * 100);
			calcEfficiency();
			calcAlcohol("Volume");
		}
	}

	public void setEfficiency(final double e) {
		isDirty = true;
		if ((e != efficiency) && (e > 0)) {
			efficiency = e;
			calcMaltTotals();
		}
	}

	public void setAttenuation(final double a) {
		isDirty = true;
		if ((a != attenuation) && (a > 0)) {
			attenuation = a;
			calcMaltTotals();
		}

	}
	public void setPreBoil(final double p) {
		isDirty = true;
		preBoilVol.setAmount(p);

		double post = 0;
		// TODO
		if (evapMethod.equals("Constant")) {
			post = p - (evap * boilMinutes / 60);
		} else {
			post = p - (p * (evap / 100) * boilMinutes / 60);
		}

		postBoilVol.setAmount(post);
		calcMaltTotals();
		calcHopsTotals();
	}

	public void setPostBoil(final double p) {
		isDirty = true;
		postBoilVol.setAmount(p);

		double pre = 0;
		// TODO
		if (evapMethod.equals("Constant")) {
			pre = p + (evap * boilMinutes / 60);
		} else {
			// pre = p + (p * (evap / 100) * boilMinutes / 60);
			pre = p / ((1 - ((evap / 100) * boilMinutes / 60)));
		}
		preBoilVol.setAmount(pre);
		calcMaltTotals();
		calcHopsTotals();
		calcPrimeSugar();
		calcKegPSI();
		
//		if (!diluted) {
//			dilution.setDilVol(p);
//		} else {
//			dilution.calcDilution();
//		}
	}

	public void setFinalWortVol(final double p) {
		isDirty = true;
		// convert to QTS and set the final vol
		finalWortVolQTS = Quantity.convertUnit(getVolUnits(), Quantity.QT, p);
		// now add in all the losses, and set the post-boil to force
		// a recalc:
		double d = finalWortVolQTS + Quantity.convertUnit(getVolUnits(), Quantity.QT, kettleLoss)
				+ Quantity.convertUnit(getVolUnits(), Quantity.QT, trubLoss)
				+ Quantity.convertUnit(getVolUnits(), Quantity.QT, miscLoss);
		// add in chill shrink:
		d = d / (1 - CHILLPERCENT);
		setPostBoil(Quantity.convertUnit(Quantity.QT, getVolUnits(), d));
	}

	/*
	 * Functions that add/remove from ingredient lists
	 */
	public void addMalt(final Fermentable m) {
		isDirty = true;
		fermentables.add(m);
		Collections.sort(fermentables);
		calcMaltTotals();
	}
	public void delMalt(final int i) {
		isDirty = true;
		if (!fermentables.isEmpty() && (i > -1)) {
			fermentables.remove(i);
			calcMaltTotals();
		}
	}
	public void addHop(final Hop h) {
		isDirty = true;
		hops.add(h);
		Collections.sort(hops);
		calcHopsTotals();
	}
	public void delHop(final int i) {
		isDirty = true;
		if (!hops.isEmpty() && (i > -1)) {
			hops.remove(i);
			calcHopsTotals();
		}
	}
	public void addMisc(final Misc m) {
		isDirty = true;
		misc.add(m);
		calcMiscCost();
	}

	public void delMisc(final int i) {
		isDirty = true;
		if (!misc.isEmpty() && (i > -1)) {
			misc.remove(i);
			calcMiscCost();
		}
	}

	private void calcMiscCost() {
		totalMiscCost = 0;
		for (int i = 0; i < misc.size(); i++) {
			final Misc m = misc.get(i);
			totalMiscCost += m.getAmountAs(m.getUnits()) * m.getCostPerU();
		}
	}

	public void addNote(final Note n) {
		isDirty = true;
		notes.add(n);
	}

	public void delNote(final int i) {
		isDirty = true;
		if (!notes.isEmpty() && (i > -1)) {
			notes.remove(i);
		}
	}

	/**
	 * Handles a string of the form "d u", where d is a double amount, and u is
	 * a string of units. For importing the quantity attribute from QBrew xml.
	 * 
	 * @param a
	 */
	public void setAmountAndUnits(final String a) {
		isDirty = true;
		final int i = a.indexOf(" ");
		final String d = a.substring(0, i);
		final String u = a.substring(i);
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
			final Fermentable m = fermentables.get(i);
			possiblePoints += (m.getPppg() - 1) * m.getAmountAs(Quantity.LB)
					/ postBoilVol.getValueAs(Quantity.GAL);
		}
		efficiency = (estOg - 1) / possiblePoints * 100;
	}

	public void calcMaltTotals() {

		if (!allowRecalcs) {
			return;
		}
		double maltPoints = 0;
		double mcu = 0;
		totalMaltLbs = 0;
		totalMaltCost = 0;
		totalMashLbs = 0;

		// first figure out the total we're dealing with
		for (int i = 0; i < fermentables.size(); i++) {
			final Fermentable m = fermentables.get(i);
			totalMaltLbs += (m.getAmountAs(Quantity.LB));
			if (m.getMashed()) { // apply efficiency and add to mash weight
				maltPoints += (m.getPppg() - 1) * m.getAmountAs(Quantity.LB) * efficiency
						/ postBoilVol.getValueAs(Quantity.GAL);
				totalMashLbs += (m.getAmountAs(Quantity.LB));
			} else {
				maltPoints += (m.getPppg() - 1) * m.getAmountAs(Quantity.LB) * 100
						/ postBoilVol.getValueAs(Quantity.GAL);
			}

			mcu += m.getLov() * m.getAmountAs(Quantity.LB) / postBoilVol.getValueAs(Quantity.GAL);
			totalMaltCost += m.getCostPerU() * m.getAmountAs(m.getUnits());
		}

		// now set the malt % by weight:
		for (int i = 0; i < fermentables.size(); i++) {
			final Fermentable m = fermentables.get(i);
			if (m.getAmountAs(Quantity.LB) == 0) {
				m.setPercent(0);
			} else {
				m.setPercent((m.getAmountAs(Quantity.LB) / totalMaltLbs * 100));
			}
		}

		// set the fields in the object
		estOg = (maltPoints / 100) + 1;
		estFg = 1 + ((estOg - 1) * ((100 - attenuation) / 100));

		srm = BrewCalcs.calcColour(mcu, BrewCalcs.SRM);
		ebc = BrewCalcs.calcColour(mcu, BrewCalcs.EBC);
		// mash.setMaltWeight(totalMashLbs);

		calcAlcohol(getAlcMethod());

		// do the water calcs w/ the updated mash:
		chillShrinkQTS = getPostBoilVol(Quantity.QT) * CHILLPERCENT;
		// spargeQTS = getPreBoilVol("qt") - (mash.getTotalWaterQts() - mash.getAbsorbedQts());
		totalWaterQTS = mash.getTotalWaterQts() + mash.getSpargeVol();

		finalWortVolQTS = postBoilVol.getValueAs(Quantity.QT)
				- (chillShrinkQTS + Quantity.convertUnit(getVolUnits(), Quantity.QT, kettleLoss)
						+ Quantity.convertUnit(getVolUnits(), Quantity.QT, trubLoss) + Quantity
						.convertUnit(getVolUnits(), Quantity.QT, miscLoss));

	}
	
	public void calcHopsTotals() {

		if (!allowRecalcs) {
			return;
		}
		double ibuTotal = 0;
		totalHopsCost = 0;
		totalHopsOz = 0;

		for (int i = 0; i < hops.size(); i++) {
			// calculate the average OG of the boil
			// first, the OG at the time of addition:
			double adjPreSize, aveOg = 0;
			final Hop h = hops.get(i);			

			int time = h.getMinutes();
			if (h.getAdd().equalsIgnoreCase(Hop.FWH)) {
				time = time - fwhTime;
			} else if (h.getAdd().equalsIgnoreCase(Hop.MASH)) {
				time = mashHopTime;
			} else if (h.getAdd().equalsIgnoreCase(Hop.DRY)) {
				time = dryHopTime;
			}

			if (h.getMinutes() > 0) {
				adjPreSize = postBoilVol.getValueAs(Quantity.GAL)
						+ (preBoilVol.getValueAs(Quantity.GAL) - postBoilVol.getValueAs(Quantity.GAL))
						/ (boilMinutes / h.getMinutes());
			} else {
				adjPreSize = postBoilVol.getValueAs(Quantity.GAL);
			}

			aveOg = 1 + (((estOg - 1) + ((estOg - 1) / (adjPreSize / postBoilVol.getValueAs(Quantity.GAL)))) / 2);

			if (ibuCalcMethod.equals(BrewCalcs.TINSETH)) {
				h.setIBU(BrewCalcs.calcTinseth(h.getAmountAs(Quantity.OZ), postBoilVol.getValueAs(Quantity.GAL), aveOg,
						time, h.getAlpha(), ibuHopUtil));
			} else if (ibuCalcMethod.equals(BrewCalcs.RAGER)) {
				h.setIBU(BrewCalcs.CalcRager(h.getAmountAs(Quantity.OZ), postBoilVol.getValueAs(Quantity.GAL), aveOg, time,
						h.getAlpha()));
			} else {
				h.setIBU(BrewCalcs.CalcGaretz(h.getAmountAs(Quantity.OZ), postBoilVol.getValueAs(Quantity.GAL), aveOg,
						time, preBoilVol.getValueAs(Quantity.GAL), 1, h.getAlpha()));
			}
			if (h.getType().equalsIgnoreCase(Hop.PELLET)) {
				h.setIBU(h.getIBU() * (1.0 + (pelletHopPct / 100)));
			}

			ibuTotal += h.getIBU();
			
			totalHopsCost += h.getCostPerU() * h.getAmountAs(h.getUnits());
			totalHopsOz += h.getAmountAs(Quantity.OZ);
		}

		ibu = ibuTotal;

	}

	// TODO should be in BrewCalcs
	private void calcAlcohol(final String method) {
		final double oPlato = BrewCalcs.SGToPlato(estOg);
		final double fPlato = BrewCalcs.SGToPlato(estFg);
		final double q = 0.22 + 0.001 * oPlato;
		final double re = (q * oPlato + fPlato) / (1.0 + q);
		// calculate by weight:
		alcohol = (oPlato - re) / (2.0665 - 0.010665 * oPlato);
		// TODO
		if (method.equalsIgnoreCase("Volume")) {
			alcohol = alcohol * estFg / 0.794;
		}
	}

	private String addXMLHeader(String in) {
		in = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n" + in;
		return in;
	}

	public String toXML(final String printOptions) {
		final StringBuffer sb = new StringBuffer();
		sb.append("<STRANGEBREWRECIPE version = \"" + version + "\">\n");
		sb.append("<!-- This is a SBJava export.  StrangeBrew 1.8 will not import it. -->\n");
		if (printOptions != null) {
			sb.append(printOptions);
		}
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
//		sb.append("  <ADDED_VOLUME>" + dilution.getAddVol() + "</ADDED_VOLUME>\n");
		sb.append("  <MALT_UNITS>" + maltUnits + "</MALT_UNITS>\n");
		sb.append("  <HOPS_UNITS>" + hopUnits + "</HOPS_UNITS>\n");
		sb.append("  <YEAST>" + yeast.getName() + "</YEAST>\n");
		final SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
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
			final Fermentable m = fermentables.get(i);
			sb.append(m.toXML());
		}
		sb.append("  </FERMENTABLES>\n");

		// hops list:
		sb.append("  <HOPS>\n");
		sb.append(SBStringUtils.xmlElement("TOTAL", ""
				+ Quantity.convertUnit(Quantity.OZ, hopUnits, totalHopsOz), 4));
		for (int i = 0; i < hops.size(); i++) {
			final Hop h = hops.get(i);
			sb.append(h.toXML());
		}
		sb.append("  </HOPS>\n");

		// misc ingredients list:
		sb.append("  <MISC>\n");
		for (int i = 0; i < misc.size(); i++) {
			final Misc mi = misc.get(i);
			sb.append(mi.toXML());
		}
		sb.append("  </MISC>\n");

		sb.append(mash.toXml());

		// Fermentation Schedual
		sb.append("   <FERMENTATION_SCHEDUAL>\n");
		for (int i = 0; i < fermentationSteps.size(); i++ ) {
			sb.append(fermentationSteps.get(i).toXML());
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

		if ((sourceWater.getName() != "") ||
			(targetWater.getName() != "")) {
			sb.append("   <WATER_PROFILE>\n");
			if (sourceWater.getName() != "") {
				sb.append("      <SOURCE_WATER>\n");
				sb.append(sourceWater.toXML(9));
				sb.append("      </SOURCE_WATER>\n");
			}
			if (targetWater.getName() != "") {
				sb.append("      <TARGET_WATER>\n");
				sb.append(targetWater.toXML(9));
				sb.append("      </TARGET_WATER>\n");
			}
			
			if (brewingSalts.size() > 0) {
				sb.append("      <SALTS>\n");
				for (int i = 0; i < brewingSalts.size(); i++ ) {
					sb.append("         <SALT>\n");
					sb.append(brewingSalts.get(i).toXML(12));
					sb.append("         </SALT>\n");
				}
				sb.append("      </SALTS>\n");
			}
			sb.append("      <ACID>\n");
			sb.append("         <NAME>" + acid.getName() + "</NAME>\n");
			sb.append("         <AMT>" + SBStringUtils.format(getAcidAmount(), 2) + "</AMT>\n");
			sb.append("         <ACIDU>" + acid.getAcidUnit() + "</ACIDU>\n");
			sb.append("      </ACID>\n");
			
			sb.append("   </WATER_PROFILE>\n");			
		}		
		
		// notes list:
		sb.append("  <NOTES>\n");
		for (int i = 0; i < notes.size(); i++) {
			sb.append(notes.get(i).toXML());
		}
		sb.append("  </NOTES>\n");

		// style xml:
		sb.append(style.toXML());

		sb.append("</STRANGEBREWRECIPE>");

		return addXMLHeader(sb.toString());
	}

	public static String padLeft(final String str, final int fullLength, final char ch) {
		return (fullLength > str.length())
				? str.concat(Recipe.buildString(ch, fullLength - str.length()))
				: str;
	}

	public static String padRight(final String str, final int fullLength, final char ch) {
		return (fullLength > str.length())
				? Recipe.buildString(ch, fullLength - str.length()).concat(str)
				: str;
	}

	public static String buildString(final char ch, final int length) {
		final char newStr[] = new char[length];
		for (int i = 0; i < length; ++i) {
			newStr[i] = ch;
		}
		return new String(newStr);
	}

	public String toText() {
		MessageFormat mf;
		final StringBuffer sb = new StringBuffer();
		sb.append("StrangeBrew J v." + version + " recipe text output\n\n");
		sb.append("Details:\n");
		sb.append("Name: " + name + "\n");
		sb.append("Brewer: " + brewer + "\n");
		sb.append("Size: " + SBStringUtils.format(postBoilVol.getValue(), 1) + " "
				+ postBoilVol.getUnits() + "\n");
		sb.append("Style: " + style.getName() + "\n");
		mf = new MessageFormat(
				"OG: {0,number,0.000},\tFG:{1,number,0.000}, \tAlc:{2,number,0.0}, \tIBU:{3,number,0.0}\n");
		final Object[] objs = {new Double(estOg), new Double(estFg), new Double(alcohol), new Double(ibu)};
		sb.append(mf.format(objs));
		sb.append("(Alc method: by " + alcMethod + "; IBU method: " + ibuCalcMethod + ")\n");
		sb.append("\nYeast: " + yeast + "\n");		
		sb.append("\nFermentables:\n");
		sb.append(Recipe.padLeft("Name ", 30, ' ') + " amount units  pppg    lov   %\n");

		mf = new MessageFormat("{0} {1} {2} {3,number,0.000} {4} {5}%\n");
		for (int i = 0; i < fermentables.size(); i++) {
			final Fermentable f = (Fermentable) fermentables.get(i);

			final Object[] objf = {Recipe.padLeft(f.getName(), 30, ' '),
					Recipe.padRight(" " + SBStringUtils.format(f.getAmountAs(f.getUnits()), 2), 6, ' '),
					Recipe.padRight(" " + f.getUnitsAbrv(), 5, ' '), new Double(f.getPppg()),
					Recipe.padRight(" " + SBStringUtils.format(f.getLov(), 1), 6, ' '),
					Recipe.padRight(" " + SBStringUtils.format(f.getPercent(), 1), 5, ' ')};
			sb.append(mf.format(objf));

		}

		sb.append("\nHops:\n");
		sb.append(Recipe.padLeft("Name ", 20, ' ') + " amount units  Alpha    Min   IBU\n");

		mf = new MessageFormat("{0} {1} {2} {3} {4} {5}\n");
		for (int i = 0; i < hops.size(); i++) {
			final Hop h = hops.get(i);

			final Object[] objh = {Recipe.padLeft(h.getName(), 20, ' '),
					Recipe.padRight(" " + SBStringUtils.format(h.getAmountAs(h.getUnits()), 2), 6, ' '),
					Recipe.padRight(" " + h.getUnitsAbrv(), 5, ' '), Recipe.padRight(" " + h.getAlpha(), 6, ' '),
					Recipe.padRight(" " + SBStringUtils.format(h.getMinutes(), 1), 6, ' '),
					Recipe.padRight(" " + SBStringUtils.format(h.getIBU(), 1), 5, ' ')};
			sb.append(mf.format(objh));

		}
		
		if (mash.getStepSize() > 0){
			sb.append("\nMash:\n");
			sb.append(Recipe.padLeft("Step ", 10, ' ') + "  Temp   End    Ramp    Min\n");
	
			mf = new MessageFormat("{0} {1} {2} {3} {4}\n");
			for (int i = 0; i < mash.getStepSize(); i++) {
	
				final Object[] objm = {Recipe.padLeft(mash.getStepType(i), 10, ' '),
						Recipe.padRight(" " + mash.getStepStartTemp(i), 6, ' '),
						Recipe.padRight(" " + mash.getStepEndTemp(i), 6, ' '),
						Recipe.padRight(" " + mash.getStepRampMin(i), 4, ' '),
						Recipe.padRight(" " + mash.getStepMin(i), 6, ' ')};
				sb.append(mf.format(objm));
			}
		}
		
		// Fermentation Schedule
		if (fermentationSteps.size() > 0){
			sb.append("\nFermentation Schedual:\n");
			sb.append(Recipe.padLeft("Step ", 10, ' ') + "  Time   Days\n");
			mf = new MessageFormat("{0} {1} {2}\n");
			for (int i = 0; i < fermentationSteps.size(); i++ ) {
				final FermentStep f = fermentationSteps.get(i);
					final Object[] objm = {Recipe.padLeft(f.getType(), 10, ' '),
									Recipe.padRight(" " + f.getTime(), 6, ' '),
									Recipe.padRight(" " + f.getTemp() + f.getTempU(), 6, ' ')};
				sb.append(mf.format(objm));
			}
		}
		
		// Carb
		sb.append("\nCarbonation:  " + targetVol + " volumes CO2\n");
		sb.append(" Bottle Temp: " + bottleTemp + carbTempU + "  Serving Temp:" + servTemp + carbTempU + "\n");
		sb.append(" Priming: " + SBStringUtils.format(primeSugar.getAmountAs(primeSugar.getUnits()),1) + primeSugar.getUnitsAbrv() +
				" of " + primeSugar.getName() + "\n");
		sb.append(" Or keg at: " + kegPSI + "PSI\n");
		
		if (notes.size() > 0){
			sb.append("\nNotes:\n");
			for (int i = 0; i < notes.size(); i++) {
				sb.append( notes.get(i).toString());
			}
		}
		
		if ((sourceWater.getName() !="") ||
			(targetWater.getName() !="") ) {
			sb.append("\nWater Profile\n");
			sb.append(" Source Water: " + sourceWater.toString() + "\n");
			sb.append(" Target Water: " + targetWater.toString() + "\n");
			
			if (brewingSalts.size() > 0) {
				sb.append(" Salt Additions per Gal\n");
				for (int i = 0; i < brewingSalts.size(); i++ ) {
					sb.append("  " + brewingSalts.get(i).toString() + "\n");
				}
			}			
			sb.append(" Acid: " + SBStringUtils.format(getAcidAmount(), 2) + acid.getAcidUnit() + " per gal of " + acid.getName() + " Acid\n");
		}

		return sb.toString();
	}
		
	/*
	 * Scale the recipe up or down, so that the new OG = old OG, and
	 * new IBU = old IBU
	 */
	public void scaleRecipe(final double newSize, final String newUnits) {
		double currentSize = getPostBoilVol(getVolUnits());
		currentSize = Quantity.convertUnit(getVolUnits(), newUnits, currentSize);
		final double conversionFactor = newSize / currentSize;

		if (conversionFactor != 1) {
			setPostBoil(newSize);
			setVolUnits(newUnits);

			// TODO: figure out a way to make sure old IBU = new IBU
			for (int i = 0; i < getHopsListSize(); i++) {
				final Hop h = getHop(i);
				h.setAmount(h.getAmountAs(h.getUnits()) * conversionFactor);
			}
			for (int i = 0; i < getMaltListSize(); i++) {
				final Fermentable f = getFermentable(i);
				f.setAmount(f.getAmountAs(f.getUnits()) * conversionFactor);
			}
			calcHopsTotals();
			calcMaltTotals();

		}
	}

	public double getBottleTemp() {
		return bottleTemp;
	}

	public void setBottleTemp(final double bottleTemp) {
		isDirty = true;
		this.bottleTemp = bottleTemp;
		calcPrimeSugar();
	}

	public String getCarbTempU() {
		return carbTempU;
	}

	public void setCarbTempU(final String carbU) {
		isDirty = true;
		this.carbTempU = carbU;
	}

	public boolean isKegged() {
		return kegged;
	}

	public void setKegged(final boolean kegged) {
		isDirty = true;
		this.kegged = kegged;
		calcKegPSI();
	}
	
	public double getKegPSI() {
		return this.kegPSI;	
	}
	
	public void setKegPSI(final double psi) {
		isDirty = true;
		this.kegPSI = psi;
	}
	
	public double getKegTubeLength() {
		double resistance = 1;
		if (getKegTubeID().equals("3/16")) {
			resistance = 2.4;
		} else {
			resistance = 0.7;
		}
		return (getKegPSI() - (getKegTubeHeight() * 0.5) - 1) / resistance;			
	}
	
	public double getKegTubeVol() {
		double mlPerFoot = 1;
		if (getKegTubeID().equals("3/16")) {
			mlPerFoot = 4.9; 
		} else {
			mlPerFoot = 9.9;
		}
		
		return getKegTubeLength() * mlPerFoot; 
	}
	
	public double getKegTubeHeight() {
		return Options.getInstance().getDProperty("optHeightAboveKeg");
	}
	
	public String getKegTubeID() {
		return Options.getInstance().getProperty("optTubingID");		
	}
	
	public String getPrimeSugarName() {
		return primeSugar.getName();
	}
	
	public String getPrimeSugarU() {
		return primeSugar.getUnitsAbrv();
	}

	public void setPrimeSugarU(final String primeU) {
		isDirty = true;
		this.primeSugar.setUnits(primeU);
		calcPrimeSugar();	
	}

	public double getPrimeSugarAmt() {
		return primeSugar.getAmountAs(primeSugar.getUnitsAbrv());
	}
		
	public void calcPrimeSugar() {
		final double dissolvedCO2 = BrewCalcs.dissolvedCO2(getBottleTemp());
		final double primeSugarGL = BrewCalcs.PrimingSugarGL(dissolvedCO2, getTargetVol(), getPrimeSugar());
				
		// Convert to selected Units
		double neededPrime = Quantity.convertUnit(Quantity.G, getPrimeSugarU(), primeSugarGL);
		neededPrime *= Quantity.convertUnit(Quantity.L, getVolUnits(), primeSugarGL);
		neededPrime *= getFinalWortVol();
		
		primeSugar.setAmount(neededPrime);
	}
	
	public void calcKegPSI() {
		final double psi = BrewCalcs.KegPSI(servTemp, getTargetVol());
		kegPSI = psi;
	}

	public void setPrimeSugarName(final String n) {
		isDirty = true;
		this.primeSugar.setName(n);
		// Name comes with Yeild! set it too
		final List<PrimeSugar> db = Database.getInstance().primeSugarDB;
		for (int i = 0; i < db.size(); i++) {
			if (n.equals(db.get(i).getName())) {
				this.primeSugar.setYield(db.get(i).getYield());
				calcPrimeSugar();	
			}
		}
	}
	
	public void setPrimeSugarAmount(final double q) {
		isDirty = true;
		this.primeSugar.setAmount(q);		
	}
	
	public double getServTemp() {
		return servTemp;
	}

	public void setServTemp(final double servTemp) {
		isDirty = true;
		this.servTemp = servTemp;
		calcKegPSI();
	}

	public double getTargetVol() {
		return targetVol;	
	}

	public void setTargetVol(final double targetVol) {
		isDirty = true;
		this.targetVol = targetVol;
		calcPrimeSugar();
		calcKegPSI();
	}

	public PrimeSugar getPrimeSugar() {
		return primeSugar;
	}

	public void setPrimeSugar(final PrimeSugar primeSugar) {
		this.primeSugar = primeSugar;
		calcPrimeSugar();			
	}

	public WaterProfile getSourceWater() {
		return sourceWater;
	}

	public WaterProfile getTargetWater() {
		return targetWater;
	}

	public void setSourceWater(final WaterProfile sourceWater) {
		this.sourceWater = sourceWater;
	}

	public void setTargetWater(final WaterProfile targetWater) {
		this.targetWater = targetWater;
	}
	
	public WaterProfile getWaterDifference() {
		final WaterProfile diff = new WaterProfile();
		diff.setCa(getTargetWater().getCa() - getSourceWater().getCa());
		diff.setCl(getTargetWater().getCl() - getSourceWater().getCl());
		diff.setMg(getTargetWater().getMg() - getSourceWater().getMg());
		diff.setNa(getTargetWater().getNa() - getSourceWater().getNa());
		diff.setSo4(getTargetWater().getSo4() - getSourceWater().getSo4());
		diff.setHco3(getTargetWater().getHco3() - getSourceWater().getHco3());
		diff.setHardness(getTargetWater().getHardness() - getSourceWater().getHardness());
		diff.setAlkalinity(getTargetWater().getAlkalinity() - getSourceWater().getAlkalinity());
		diff.setTds(getTargetWater().getTds() - getSourceWater().getTds());
		diff.setPh(getTargetWater().getPh() - getSourceWater().getPh());
		
		return diff;
	}

	public void addSalt(final Salt s) {
		// Check if ths salt already exists!
		final Salt temp = getSaltByName(s.getName());
		if (temp != null) {
			this.delSalt(temp);
		}
		this.brewingSalts.add(s);
	}
	
	public void delSalt(final Salt s) {
		this.brewingSalts.remove(s);
	}
	
	public void delSalt(final int i) {
		this.brewingSalts.remove(i);
	}
	
	public void setSalts(final ArrayList<Salt> s) {
		this.brewingSalts = s;
	}
	
	public List<Salt> getSalts() {
		return this.brewingSalts;
	}
	
	public Salt getSalt(final int i) {
		return this.brewingSalts.get(i);
	}
	
	public Salt getSaltByName(final String name) {
		for (int i = 0; i < brewingSalts.size(); i++) {
			final Salt s = brewingSalts.get(i);
			if (s.getName().equals(name)) {
				return s;
			}
		}
		
		return null;
	}

	public Acid getAcid() {
		return acid;
	}

	public void setAcid(final Acid acid) {
		this.acid = acid;
	}
	
	public double getAcidAmount() {
		final double millEs = BrewCalcs.acidMillequivelantsPerLiter(getSourceWater().getPh(), 
				getSourceWater().getAlkalinity(), getTargetWater().getPh());
		final double moles = BrewCalcs.molesByAcid(getAcid(), millEs,  getTargetWater().getPh());
		final double acidPerL = BrewCalcs.acidAmountPerL(getAcid(), moles);
		return Quantity.convertUnit(Quantity.GAL, Quantity.L, acidPerL);
	}

	public double getAcidAmount5_2() {
		final double millEs = BrewCalcs.acidMillequivelantsPerLiter(getSourceWater().getPh(), 
				getSourceWater().getAlkalinity(), 5.2);
		final double moles = BrewCalcs.molesByAcid(getAcid(), millEs,  5.2);
		final double acidPerL = BrewCalcs.acidAmountPerL(getAcid(), moles);
		return Quantity.convertUnit(Quantity.GAL, Quantity.L, acidPerL);
	}
}