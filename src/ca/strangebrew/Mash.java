package ca.strangebrew;

import java.util.ArrayList;
import java.util.Collections;

/**
 * $Id: Mash.java,v 1.37 2008/01/16 17:55:04 jimcdiver Exp $
 * @author aavis
 *
 */

public class Mash {
	// set this:
	private double maltWeightLbs;
	private Recipe myRecipe;
	
	
	//options:
	private double mashRatio;
	private String mashRatioU;
	private String tempUnits;
	private String volUnits;
	private double grainTempF;
	private double boilTempF;
	// private double thermalMass;
	private double tunLossF;
	private double thinDecoctRatio;
	private double thickDecoctRatio;
	private double cerealMashTemp;
	private String name;
	
	// calculated:
	private double volQts;
	private int totalTime;
	private double absorbedQTS;
	private double totalWaterQTS;
	private double spargeQTS;
	
	// steps:
	private ArrayList<MashStep> steps = new ArrayList<MashStep>();
	
	// configurable temps, can be set by the user:
	// target temps are 1/2 between temp + next temp

	private float ACIDTMPF = 85;
	private float GLUCANTMPF = 95;
	private float PROTEINTMPF = 113;
	private float BETATMPF = 131;
	private float ALPHATMPF = 151;
	private float MASHOUTTMPF = 161;
	private float SPARGETMPF = 170;
	
	static final public String QT_PER_LB = "qt/lb";
	static final public String L_PER_KG = "l/kg";
	static final public String ACID = "acid";
	static final public String GLUCAN = "glucan";
	static final public String PROTEIN = "protein";
	static final public String BETA = "beta";
	static final public String ALPHA = "alpha";
	static final public String MASHOUT = "mashout";
	static final public String SPARGE = "sparge";
	static final public String INFUSION = "infusion";
	static final public String DECOCTION = "decoction";
	static final public String DECOCTION_THICK = "decoction thick";
	static final public String DECOCTION_THIN = "decoction thin";
	static final public String DIRECT = "direct";
	static final public String CEREAL_MASH = "cereal mash";
	static final public String FLY = "fly";
	static final public String BATCH = "batch";
	
	static final public String[] ratioUnits = {QT_PER_LB, L_PER_KG};
	static final public String[] types = {ACID, GLUCAN, PROTEIN, BETA, ALPHA, MASHOUT, SPARGE};
	static final public String[] methods = {INFUSION, DECOCTION, DECOCTION_THICK, DECOCTION_THIN, DIRECT, CEREAL_MASH};
	static final public String[] spargeMethods = {FLY, BATCH};
	
	
	public Mash(Recipe r){
		 Options opts = Options.getInstance();
		 mashRatio = opts.getDProperty("optMashRatio");
		 mashRatioU = opts.getProperty("optMashRatioU");;
		 tempUnits = opts.getProperty("optMashTempU");
		 
		 if(myRecipe != null) {
			 volUnits = myRecipe.getVolUnits();
		 } else {
			 volUnits = opts.getProperty("optMashVolU");
		 }
		 grainTempF = opts.getDProperty("optGrainTemp");
		 boilTempF = opts.getDProperty("optBoilTempF");
		 ACIDTMPF = opts.getFProperty("optAcidTmpF");
		 GLUCANTMPF = opts.getFProperty("optGlucanTmpF");
		 PROTEINTMPF = opts.getFProperty("optProteinTmpF");
		 BETATMPF = opts.getFProperty("optBetaTmpF");
		 ALPHATMPF = opts.getFProperty("optAlphaTmpF");
		 MASHOUTTMPF = opts.getFProperty("optMashoutTmpF");
		 SPARGETMPF = opts.getFProperty("optSpargeTmpF");
		 thickDecoctRatio = opts.getDProperty("optThickDecoctRatio");
		 thinDecoctRatio = opts.getDProperty("optThinDecoctRatio");
		 cerealMashTemp = opts.getDProperty("optCerealMashTmpF");
		 name = "Mash";

		 myRecipe = r;
	}
	
	public class MashStep implements Comparable<MashStep> {
		private String type;
		private double startTemp;
		private double endTemp;		
		private String method;
		private int minutes;
		private int rampMin;
		private String directions;
		private double temp;
		private double weightLbs;
		
		public Quantity vol = new Quantity();
		
		// public Quantity decoctVol = new Quantity();

		public MashStep(String type, double startTemp, double endTemp, String method, int min,
				int rmin) {
			this.type = type;
			this.startTemp = startTemp;
			this.endTemp = endTemp;			
			this.method = method;
			minutes = min;
			rampMin = rmin;		
			
		}

		// default constructor:
		public MashStep() {
			rampMin = 0;
			endTemp = ALPHATMPF + 1;
			startTemp = ALPHATMPF + 1;
			minutes = 60;
			method = INFUSION;
			type = ALPHA;
			weightLbs = 0;

		}
	
		// getter/setter methods	
	
		public String getDirections() {
			return directions;
		}

		public void setDirections(String directions) {
			this.directions = directions;
		}

		public double getEndTemp() {
			return endTemp;
		}

		public void setEndTemp(double endTemp) {
			this.endTemp = endTemp;
		}

		public Quantity getVol() {
			return vol;
		}

		public void setvol(Quantity vol) {
			this.vol = vol;
		}

		public String getMethod() {
			return method;
		}

		public void setMethod(String method) {
			this.method = method;
		}

		public int getMinutes() {
			return minutes;
		}

		public void setMinutes(int minutes) {
			this.minutes = minutes;
		}

		public int getRampMin() {
			return rampMin;
		}

		public void setRampMin(int rampMin) {
			this.rampMin = rampMin;
		}

		public double getStartTemp() {
			return startTemp;
		}

		public void setStartTemp(double startTemp) {
			this.startTemp = startTemp;
		}
		
		public double getTemp() {
			return temp;
		}

		public String getType() {
			return type;
		}
		
		public void setType(String s) {
			type = s; 
		}
		
		public int compareTo(MashStep m) {
				int result = ((Double)this.getStartTemp()).compareTo((Double)m.getStartTemp());
				return (result == 0 ? -1 : result);
		}		
	}

	public void addStep(String type, double st, double et, String m, int min,
			int rmin, double weight) {
		MashStep step = new MashStep(type, st, et, m, min, rmin);
		if (m.equals(CEREAL_MASH))
			step.weightLbs = weight;
		steps.add(step);		
		calcMashSchedule();
	}
	
	public int addStep(){
		MashStep step = new MashStep();
		// calcStepType(temp);
		if (!steps.isEmpty()) {
			MashStep lastStep = (Mash.MashStep)steps.get(steps.size() -1);	
			step.setStartTemp(lastStep.getEndTemp() + 1);
			step.setEndTemp(step.getStartTemp());
			step.setType(calcStepType(step.getStartTemp()));
		}
		
		steps.add(step);
		int i = steps.size();
		calcMashSchedule();
		// return the index of the last added step:
		return i-1;
		
	}
	
	public void delStep(int i){
		if (steps.size()>i && !steps.isEmpty() && i > -1){			
			steps.remove(i);
			calcMashSchedule();
		}
			
	}

	// set methods:
	// public void setMaltWeight(double mw) {	maltWeightLbs = mw;	}
	public void setMashRatio(double mr){ 
		mashRatio = mr; 
		calcMashSchedule();
	}	
	
	public double getMashRatio(){
		return mashRatio;		
	}
	
	public void setMashRatioU(String u){ 
		mashRatioU = u;
		calcMashSchedule();
	}
	
	public String getMashRatioU(){
		return mashRatioU;		
	}
	
	public void setMashVolUnits(String u){ 
		volUnits = u;
		calcMashSchedule();
	}
	
	public void setMashTempUnits(String t){
		tempUnits = t;
		calcMashSchedule();
	}
	
	// TODO hardcoded temp strings should be a static somewhere
	public void setGrainTemp(double t){
		if (tempUnits.equals("F"))
			grainTempF = t;
		else
			grainTempF = BrewCalcs.cToF(t);
		calcMashSchedule();
	}
	
	public void setBoilTemp(double t){
		if (tempUnits.equals("F"))
			boilTempF = t;
		else
			boilTempF = BrewCalcs.cToF(t);
		calcMashSchedule();
	}
	
	public void setTunLoss(double t){
		if (tempUnits.equals("F"))
			tunLossF = t;
		else
			tunLossF = t * 1.8;
		calcMashSchedule();
	}
	
	public void setDecoctRatio(String type, double r){
		if (type.equals("thick"))
			thickDecoctRatio = r;
		else
			thinDecoctRatio = r;
		calcMashSchedule();
	}
	
	public void setTempRange(String type, double t){
		if (tempUnits.equals("C"))
			t = BrewCalcs.cToF(t);
		if (type.equalsIgnoreCase(MASHOUT))
			MASHOUTTMPF = (float)t;
		if (type.equalsIgnoreCase(SPARGE))
			SPARGETMPF = (float)t;
		
	}
	
	public void setName(String s) { name = s; }
	
	/**
	 * 
	 * @param val Value to convert to a string
	 * @return Val converted to the mash vol, formated to 1 decimal
	 */
	private String getVolConverted(double val){
		double d = Quantity.convertUnit(Quantity.QT, volUnits, val); 
		String s = SBStringUtils.format(d, 1);
		return s;
	}
		
	// get methods:
	public String getMashVolUnits(){ return volUnits; }
	public String getMashTempUnits(){ return tempUnits; }
	public int getMashTotalTime(){ return totalTime; }
	public double getGrainTemp() {
		if (tempUnits.equals("F"))
			return grainTempF; 
		else
			return BrewCalcs.fToC(grainTempF);
		}
	public double getBoilTemp() { 
		if (tempUnits.equals("F")) 
				return boilTempF;
		else
			return BrewCalcs.fToC(boilTempF);
		}
	
	public double getTempRange(String type){
		double t=0;
		if (type.equals(MASHOUT))
			t = MASHOUTTMPF;
		else if (type.equals(SPARGE))
			t = SPARGETMPF;
		if (tempUnits.equals("C"))
			t = BrewCalcs.fToC(t);
		
		return t;
	}
	
	public double getSpargeVol(){
		return Quantity.convertUnit(Quantity.QT, volUnits, spargeQTS);
	}
	
	public double getSpargeQts(){ return spargeQTS; }

	public double getTunLoss() { 
		if (tempUnits.equals("F"))
			return tunLossF; 
		else
			return ( tunLossF / 1.8 );
		}
	
	/**
	 * 
	 * @return A string, which is the total converted to the mash units
	 * + the units.
	 */
	public String getMashTotalVol() {
		double d = Quantity.convertUnit(Quantity.QT, volUnits, volQts);
		String s = SBStringUtils.format(d, 1) + " " + volUnits;
		return s;	
	}	
	
	public String getAbsorbedStr() {
		return getVolConverted(absorbedQTS);
	}
	public double getAbsorbedQts() {
		return absorbedQTS;
	}

	public String getTotalWaterStr() {
		return getVolConverted(totalWaterQTS);
	}
	public double getTotalWaterQts() {
		return totalWaterQTS;
	}
	public double getThickDecoctRatio() {
		return thickDecoctRatio;
	}
	public double getThinDecoctRatio() {
		return thinDecoctRatio;
	}
	
	public String getName(){ return name; }

	
	
	
	// mash step methods:
	public int setStepType(int i, String t){
		if (steps.size() < i || steps.isEmpty())
			return -1;
		MashStep ms = (MashStep)steps.get(i);
		ms.setType(t);
		ms.setStartTemp(calcStepTemp(t));
		ms.setEndTemp(calcStepTemp(t));
		return 0;
	}
	
	public String getStepType(int i) {
		if (steps.size() < i || steps.isEmpty())
			return "";
		MashStep ms = (MashStep)steps.get(i);
		return ms.getType();		
	}
	
	
	public String getStepDirections(int i){
		return ((MashStep)steps.get(i)).getDirections();
	}
	
	public void setStepMethod(int i, String m){
		((MashStep)steps.get(i)).setMethod(m);
		if (m.equals(CEREAL_MASH))
			((MashStep)steps.get(i)).weightLbs = 0;
		calcMashSchedule();
	}
	
	public String getStepMethod(int i) {
		return ((MashStep)steps.get(i)).getMethod();	
	}
	
	public void setStepStartTemp(int i, double t){		
		if (tempUnits.equals("C")){
			t = BrewCalcs.cToF(t);			
		}				
		((MashStep)steps.get(i)).setStartTemp(t);
		((MashStep)steps.get(i)).setEndTemp(t);
		((MashStep)steps.get(i)).setType(calcStepType(t));
		
		calcMashSchedule();
		
	}
	
	public double getStepStartTemp(int i) {
		if (tempUnits.equals("C"))
			return BrewCalcs.fToC(((MashStep)steps.get(i)).getStartTemp());
		else
			return ((MashStep)steps.get(i)).getStartTemp();	
	}
	
	public void setStepEndTemp(int i, double t){
		if (tempUnits.equals("C"))
			((MashStep)steps.get(i)).setEndTemp(BrewCalcs.cToF(t));
		else
			((MashStep)steps.get(i)).setEndTemp(t);
		calcMashSchedule();
	}
	
	public double getStepEndTemp(int i) {
		if (tempUnits.equals("C"))
			return BrewCalcs.fToC(((MashStep)steps.get(i)).getEndTemp());
		else
			return ((MashStep)steps.get(i)).getEndTemp();	
		
	}
	
	public void setStepRampMin(int i, int m){
		((MashStep)steps.get(i)).setRampMin(m);
	}
	
	public int getStepRampMin(int i) {
		return ((MashStep)steps.get(i)).getRampMin();	
	}
	
	public double getStepTemp(int i) {
		if (((MashStep)steps.get(i)).getTemp() == 0)
			return 0;
		if (tempUnits.equals("F"))
			return ((MashStep)steps.get(i)).getTemp();	
		else
			return BrewCalcs.fToC(((MashStep)steps.get(i)).getTemp());
	}
	
	public double getStepWeight(int i) {
		double w = 	((MashStep)steps.get(i)).weightLbs;
		return Quantity.convertUnit(Quantity.LB, myRecipe.getMaltUnits(), w);

	}
	
	public void setStepWeight(int i, double w){
		// you can only set the weight on a cereal mash step
		MashStep s = (MashStep)steps.get(i);
		if (s.method.equals(CEREAL_MASH)){
			double w2 = Quantity.convertUnit(myRecipe.getMaltUnits(), Quantity.LB, w);
			s.weightLbs = w2;
			calcMashSchedule();
		}			
	}
	
	public void setStepMin(int i, int m){
		((MashStep)steps.get(i)).setMinutes(m);
	}
	
	public int getStepMin(int i) {
		return ((MashStep)steps.get(i)).getMinutes();	
	}

	public double getStepVol(int i) {
		double vol = ((MashStep)steps.get(i)).getVol().getValue();
		return Quantity.convertUnit(Quantity.QT, volUnits, vol);
	
	}
	
	
	public int getStepSize(){
		return steps.size();
	}
	
	// Introducing: the big huge mash calc method!

	public void calcMashSchedule() {
		// Method to run through the mash table and calculate values

		if (!myRecipe.allowRecalcs)
			return;
		
		double strikeTemp = 0;
		double targetTemp = 0;
		double waterAddedQTS = 0;
		double waterEquiv = 0;
		double mr = mashRatio;
		double currentTemp = grainTempF;

		double displTemp = 0;
		double tunLoss; // figure out a better way to do this, eg: themal mass
		double decoct = 0;
		int totalMashTime = 0;
		int totalSpargeTime = 0;
		double mashWaterQTS = 0;
		double mashVolQTS = 0;
		int numSparge = 0;
		double totalWeightLbs = 0;
		double totalCerealLbs = 0;
		
		maltWeightLbs = myRecipe.getTotalMashLbs();
		
		// convert mash ratio to qts/lb if in l/kg
		if (mashRatioU.equalsIgnoreCase(L_PER_KG)) {
			mr *= 0.479325;
		}

		// convert CurrentTemp to F
		if (tempUnits == "C") {
			currentTemp = BrewCalcs.cToF(currentTemp);
			tunLoss = tunLossF * 1.8;
		}
		else
			tunLoss = tunLossF;

		// perform calcs on first record	  
		if (steps.isEmpty())
			return;
		
		// sort the list
		Collections.sort(steps);
		
		// add up the cereal mash lbs
		for (int i=0; i<steps.size(); i++){
			MashStep stp = ((MashStep) steps.get(i));
			if (stp.method.equals("cereal mash")){
				totalCerealLbs += stp.weightLbs;
			}
		}
		
		totalWeightLbs = maltWeightLbs - totalCerealLbs;
		

		// the first step is always an infusion
		MashStep stp = ((MashStep) steps.get(0));
		targetTemp = stp.startTemp;
		strikeTemp = calcStrikeTemp(targetTemp, currentTemp, mr, tunLoss);
		waterAddedQTS = mr * totalWeightLbs;
		waterEquiv = totalWeightLbs * (0.192 + mr);
		mashVolQTS = calcMashVol(totalWeightLbs, mr);
		totalMashTime += stp.minutes;
		mashWaterQTS += waterAddedQTS;		
		stp.vol.setUnits(Quantity.QT);
		stp.vol.setAmount(waterAddedQTS);
		stp.temp = strikeTemp;
		stp.method = INFUSION;
		stp.weightLbs = totalWeightLbs;

		// subtract the water added from the Water Equiv so that they are correct when added in the next part of the loop
		waterEquiv -= waterAddedQTS;

		// Updated the water added

		if (tempUnits == "C")
			strikeTemp = BrewCalcs.fToC(strikeTemp);
		stp.directions = "Mash in with " + SBStringUtils.format(stp.vol.getValueAs(volUnits),1 ) + " " + volUnits
				+ " of water at " + SBStringUtils.format(strikeTemp, 1) + " " + tempUnits;

		// set TargetTemp to the end temp
		targetTemp = stp.endTemp;

		for (int i = 1; i < steps.size(); i++) {
			stp = ((MashStep) steps.get(i));
			currentTemp = targetTemp; // switch
			targetTemp = stp.startTemp;
			
			// if this is a former sparge step that's been changed, change
			// the method to infusion
			if (!stp.type.equals(SPARGE) && ( stp.method.equals(FLY) || stp.method.equals(BATCH)))
					stp.method = INFUSION;
			
			// do calcs
			if (stp.method.equals(INFUSION)) { // calculate an infusion step
				decoct = 0;
				waterEquiv += waterAddedQTS; // add previous addition to get WE
				strikeTemp = boilTempF; // boiling water 

				// Updated the water added
				waterAddedQTS = calcWaterAddition(targetTemp, currentTemp,
						waterEquiv, boilTempF);
				
				stp.vol.setUnits(Quantity.QT);
				stp.vol.setAmount(waterAddedQTS);
				stp.temp = strikeTemp;
				stp.weightLbs = totalWeightLbs;
				if (tempUnits == "C")
					strikeTemp = 100;				
				stp.directions = "Add " + SBStringUtils.format(stp.vol.getValueAs(volUnits), 1) + " " + volUnits
						+ " of water at " + SBStringUtils.format(strikeTemp, 1) + " " + tempUnits;

				mashWaterQTS += waterAddedQTS;
				mashVolQTS += waterAddedQTS;

			}

			else if (stp.method.indexOf(DECOCTION) > -1) { // calculate a decoction step

				waterEquiv += waterAddedQTS; // add previous addition to get WE
				waterAddedQTS = 0;
				strikeTemp = boilTempF; // boiling water
				double ratio=0.75;

				if (stp.method.indexOf(DECOCTION_THICK) > -1)
					ratio = thickDecoctRatio;
				else if (stp.method.indexOf(DECOCTION_THIN) > -1)
					ratio = thinDecoctRatio;
				// Calculate volume (qts) of mash to remove
				decoct = calcDecoction2(targetTemp, currentTemp, mashWaterQTS, ratio, totalWeightLbs);				
				stp.vol.setUnits(Quantity.QT);
				stp.vol.setAmount(decoct);
				stp.temp = boilTempF;
				stp.weightLbs = totalWeightLbs;
				
				// Updated the decoction, convert to right units & make directions
				stp.directions = "Remove " + SBStringUtils.format(stp.vol.getValueAs(volUnits), 1) + " " + volUnits
						+ " of mash, boil, and return to mash.";

			} else if (stp.method.equals(DIRECT)) { // calculate a direct heat step
				decoct = 0;
				waterEquiv += waterAddedQTS; // add previous addition to get WE
				waterAddedQTS = 0;
				displTemp = stp.startTemp;
				if (tempUnits.equals("C"))
					displTemp = BrewCalcs.fToC(displTemp);
				stp.directions = "Add direct heat until mash reaches " + displTemp
						+ " " + tempUnits + ".";
				stp.vol.setAmount(0);
				stp.temp = 0;
				stp.weightLbs = totalWeightLbs;
				
			} else if (stp.method.indexOf(CEREAL_MASH) > -1) { // calculate a cereal mash step

				waterEquiv += waterAddedQTS; // add previous addition to get WE
				waterAddedQTS = 0;	
				targetTemp = stp.startTemp;
				double extraWaterQTS = 0;				
				double cerealTemp = boilTempF;
				double cerealTargTemp = cerealMashTemp;
				String addStr = "";
				
				/*
				 * 1. check the temp of the mash when you add the boiling cereal mash @ default ratio back
				 * 2. if it's > than the step temp, adjust the step temp
				 * 3. if it's < than the step temp, add extra water to increase the "heat equivalencey" of the cereal mash 
				 */

				double cerealWaterEquiv = stp.weightLbs * (0.192 + mr);	
				waterAddedQTS = mr * stp.weightLbs;
				strikeTemp = calcStrikeTemp(cerealMashTemp, grainTempF, mr, 0);
				
				double newTemp = ((waterEquiv * currentTemp) + (cerealWaterEquiv * cerealTemp)) / (waterEquiv + cerealWaterEquiv);
				
				if (newTemp > targetTemp){
					stp.startTemp = newTemp;
				}
				if (newTemp < targetTemp){
					double addQts = ((waterEquiv * (targetTemp - currentTemp)) / (cerealTemp - targetTemp)) - 0.192;
					extraWaterQTS = addQts - waterAddedQTS;
					addStr = " Add " + SBStringUtils.format(Quantity.convertUnit("qt", volUnits, extraWaterQTS), 1) 
					  + " " + volUnits + " water to the cereal mash.";
				}
				
				// Calculate final temp of cereal mash
				// cerealTemp = (targetTemp * (waterEquiv + cerealWaterEquiv) - (waterEquiv * currentTemp)) / cerealWaterEquiv;				


				totalMashTime += stp.minutes;				
				mashWaterQTS += waterAddedQTS + extraWaterQTS;		
				stp.vol.setUnits(Quantity.QT);
				stp.vol.setAmount(waterAddedQTS);				
				stp.temp = strikeTemp;				
				
				// make directions
				
				String weightStr = SBStringUtils.format(Quantity.convertUnit(Quantity.LB, myRecipe.getMaltUnits(), stp.weightLbs), 1) 
				+ " " + myRecipe.getMaltUnits(); 
				String volStr = SBStringUtils.format(Quantity.convertUnit(Quantity.QT, volUnits, waterAddedQTS), 1) 
				+ " " + volUnits;				
				if (tempUnits == "C"){
					strikeTemp = BrewCalcs.fToC(strikeTemp);
					cerealTemp = BrewCalcs.fToC(cerealTemp);
					targetTemp = BrewCalcs.fToC(targetTemp);
					cerealTargTemp = BrewCalcs.fToC(cerealTargTemp);
				}
				String tempStr = SBStringUtils.format(strikeTemp, 1) + tempUnits;
				String tempStr2 = SBStringUtils.format(cerealTemp, 1) + tempUnits;
				String tempStr3 = SBStringUtils.format(targetTemp, 1) + tempUnits;
				String tempStr4 = SBStringUtils.format(cerealTargTemp, 1) + tempUnits;
				stp.directions = "Cereal mash: mash " + weightStr + " grain with " + volStr + " water at " + 
					tempStr + " to hit " + tempStr4 + " and rest.";
				stp.directions += addStr;
				stp.directions += " Raise to " +  tempStr2 + " and add to the main mash to reach " + tempStr3;
				
				// add cereal mash to total weight
				totalWeightLbs += stp.weightLbs;

			}

			if (stp.type.equals(SPARGE)) 
				numSparge++;

		    else {
				totalMashTime += stp.minutes;
			}				
			// set target temp to end temp for next step
			targetTemp = stp.endTemp;

		} // for steps.size()

		waterEquiv += waterAddedQTS; // add previous addition to get WE
		totalTime = totalMashTime;
		volQts = mashVolQTS;

		// water use stats:
		absorbedQTS = totalWeightLbs * 0.55; // figure from HBD
		
		// spargeTotalQTS = (myRecipe.getPreBoilVol("qt")) - (mashWaterQTS - absorbedQTS);
		totalWaterQTS = mashWaterQTS;
		spargeQTS = myRecipe.getPreBoilVol(Quantity.QT) - (mashWaterQTS - absorbedQTS);		
		
		
		// Now let's figure out the sparging:		
		if (numSparge == 0)
			return;
		
		double col = myRecipe.getPreBoilVol(Quantity.QT) / numSparge;
		double charge[] = new double[numSparge];
	    double collect[] = new double[numSparge];
	    double totalCollectQts = myRecipe.getPreBoilVol(Quantity.QT);
	    
	    if (col < mashWaterQTS - absorbedQTS) {
		     charge[0] = 0;
		     collect[0] = mashWaterQTS - absorbedQTS;
		     totalCollectQts = totalCollectQts - collect[0];
		   }
		   else {
		     charge[0] = col - (mashWaterQTS - absorbedQTS);
		     collect[0] = col;
		     totalCollectQts = totalCollectQts - collect[0];
		   }

		   // do we need any more steps?
		   if (numSparge > 1) {

			col = totalCollectQts / (numSparge - 1);
			for (int i = 1; i < numSparge; i++) {
				charge[i] = col;
				collect[i] = col;
			}
		}
	    
	    int j=0;
		for (int i = 1; i < steps.size(); i++) {
			stp = ((MashStep) steps.get(i));			
			if (stp.getType().equals(SPARGE)) {	
				stp.vol.setUnits(Quantity.QT);
				stp.vol.setAmount(collect[j]);
				stp.temp = SPARGETMPF;
				totalSpargeTime += stp.getMinutes();
				String collectStr = SBStringUtils.format(Quantity.convertUnit(Quantity.QT, volUnits, collect[j]), 1) +
				" " + volUnits;
				String tempStr;
				if (tempUnits.equals("F")){
					tempStr = "" + SBStringUtils.format(SPARGETMPF, 1) + "F";					
				} else { 
					tempStr = SBStringUtils.format(BrewCalcs.fToC(SPARGETMPF), 1) + "C";
				}
				
				if (numSparge > 1){
					stp.setMethod(BATCH);
					String add = SBStringUtils.format(Quantity.convertUnit(Quantity.QT, volUnits, charge[j]), 1) +
					" " + volUnits;			
					stp.setDirections("Add " + add + " at " + tempStr + " to collect " + collectStr);
					
				} else {
					stp.vol.setUnits(Quantity.QT);
					stp.vol.setAmount(spargeQTS);
					stp.setMethod(FLY);					
					stp.setDirections("Sparge with " + 
							SBStringUtils.format(Quantity.convertUnit("qt", volUnits, spargeQTS), 1) +
							" " + volUnits + " at " + tempStr + " to collect " + collectStr);
				}				
				j++;				
			}
		}
	}
	

	// private methods:
	
	/* from John Palmer:
	 * 		Vd (quarts) = [(T2 - T1)(.4G + 2W)] / [(Td - T1)(.4g + w)]
            Where:
            Vd = decoction volume
            T1 = initial mash temperature
            T2 = target mash temperature
            Td = decoction temperature (212F)
            G = Grainbill weight
            W = volume of water in mash (i.e. initial infusion volume)
            g = pounds of grain per quart of decoction = 1/(Rd + .32)
            w = quarts of water per quart of decoction = g*Rd*water density = 2gRd
            Rd = ratio of grain to water in the decoction volume (range of .6 to 1
            quart/lb)
            thick decoctions will have a ratio of .6-.7, thinner decoctions will
           have a ratio of .8-.9
	 */
	
	private double calcDecoction2(double targetTemp, double currentTemp, double waterVolQTS, double ratio, double weightLbs){
		double decoctQTS=0;

		double g = 1 / (ratio + .32);
		double w = 2 * g * ratio;

		decoctQTS = ((targetTemp - currentTemp) * ((0.4 * weightLbs) + (2 * waterVolQTS)))
			/ ((boilTempF - currentTemp) * (0.4 * g + w));


		return decoctQTS;
	}
	

	private String calcStepType(double temp) {
		String stepType = "none";
		// less than 90, none
		// 86 - 95 - acid
		if (temp >= ACIDTMPF && temp < GLUCANTMPF)
			stepType = ACID;
		// 95 - 113 - glucan
		else if (temp < PROTEINTMPF)
			stepType = GLUCAN;
		// 113 - 131 protein
		else if (temp < BETATMPF)
			stepType = PROTEIN;
		// 131 - 150 beta
		else if (temp < ALPHATMPF)
			stepType = BETA;
		// 150-162 alpha
		else if (temp < MASHOUTTMPF)
			stepType = ALPHA;
		// 163-169, mashout
		else if (temp < SPARGETMPF)
			stepType = MASHOUT;
		// over 170, sparge
		else if (temp >= SPARGETMPF)
			stepType = SPARGE;

		return stepType;
	}

	private double calcStepTemp(String stepType) {
		float stepTempF = 0;
		if (stepType == ACID)
			stepTempF = (ACIDTMPF + GLUCANTMPF) / 2;
		else if (stepType == GLUCAN)
			stepTempF = (GLUCANTMPF + PROTEINTMPF) / 2;
		else if (stepType == PROTEIN)
			stepTempF = (PROTEINTMPF + BETATMPF) / 2;
		else if (stepType == BETA)
			stepTempF = (BETATMPF + ALPHATMPF) / 2;
		else if (stepType == ALPHA)
			stepTempF = (ALPHATMPF + MASHOUTTMPF) / 2;
		else if (stepType == MASHOUT)
			stepTempF = (MASHOUTTMPF + SPARGETMPF) / 2;
		else if (stepType == SPARGE)
			stepTempF = SPARGETMPF;

		return stepTempF;
	}

	double calcMashVol(double grainWeightLBS, double ratio) {
		// given lbs and ratio, what is the volume of the grain in quarts?
		// note: this calc is for the first record only, and returns the heat equivalent of
		// grain + water added for first infusion
		// HBD posts indicate 0.32, but reality is closer to 0.42

		return (grainWeightLBS * (0.42 + ratio));
	}

	double calcStrikeTemp(double targetTemp, double currentTemp, double ratio,
			double tunLossF) {
		// calculate strike temp
		// Ratio is in quarts / lb, TunLoss is in F
		
		// this uses thermal mass:
		// double strikeTemp = (maltWeightLbs + thermalMass)*( targetTemp - currentTemp )/( boilTempF - targetTemp );

		return (targetTemp + 0.192 * (targetTemp - currentTemp) / ratio)
				+ tunLossF;
	}

	double calcWaterAddition(double targetTemp, double currentTemp,
			double mashVol, double boilTempF) {
		// calculate amount of boiling water to add to raise mash to new temp
		return (mashVol * (targetTemp - currentTemp) / (boilTempF - targetTemp));
	}
	

		 
	 public String toXml() {
	
		StringBuffer sb = new StringBuffer();
		sb.append("  <MASH>\n");
		sb.append(SBStringUtils.xmlElement("NAME", name, 4));
		sb.append(SBStringUtils.xmlElement("MASH_VOLUME", SBStringUtils.format(Quantity.convertUnit("qt", volUnits, volQts), 2) , 4));
		sb.append(SBStringUtils.xmlElement("MASH_VOL_U", "" + volUnits, 4));
		sb.append(SBStringUtils.xmlElement("MASH_RATIO", "" + mashRatio, 4));
		sb.append(SBStringUtils.xmlElement("MASH_RATIO_U", "" + mashRatioU, 4));
		sb.append(SBStringUtils.xmlElement("MASH_TIME", "" + totalTime, 4));
		sb.append(SBStringUtils.xmlElement("MASH_TMP_U", "" + tempUnits, 4));
		sb.append(SBStringUtils.xmlElement("THICK_DECOCT_RATIO", "" + thickDecoctRatio, 4));
		sb.append(SBStringUtils.xmlElement("THIN_DECOCT_RATIO", "" + thinDecoctRatio, 4));		
		if (tempUnits.equals("C")){
			sb.append(SBStringUtils.xmlElement("MASH_TUNLOSS_TEMP", "" + (tunLossF/1.8), 4));
			sb.append(SBStringUtils.xmlElement("GRAIN_TEMP", "" + BrewCalcs.fToC(grainTempF), 4));
			sb.append(SBStringUtils.xmlElement("BOIL_TEMP", "" + BrewCalcs.fToC(boilTempF), 4));
		}
		else {			
			sb.append(SBStringUtils.xmlElement("MASH_TUNLOSS_TEMP", "" + tunLossF, 4));
			sb.append(SBStringUtils.xmlElement("GRAIN_TEMP", "" + grainTempF, 4));
			sb.append(SBStringUtils.xmlElement("BOIL_TEMP", "" + boilTempF, 4));
		}
		for (int i = 0; i < steps.size(); i++) {
			MashStep st = (MashStep) steps.get(i);
			sb.append("    <ITEM>\n");
			sb.append("      <TYPE>" + st.type + "</TYPE>\n");
			sb.append("      <TEMP>" + st.startTemp + "</TEMP>\n");
			if (tempUnits.equals("C"))
				sb.append("      <DISPL_TEMP>" + SBStringUtils.format(BrewCalcs.fToC(st.startTemp), 1) + "</DISPL_TEMP>\n");
			else
				sb.append("      <DISPL_TEMP>" + st.startTemp + "</DISPL_TEMP>\n");
			sb.append("      <END_TEMP>" + st.endTemp + "</END_TEMP>\n");
			if (tempUnits.equals("C"))
				sb.append("      <DISPL_END_TEMP>" + SBStringUtils.format(BrewCalcs.fToC(st.endTemp), 1) + "</DISPL_END_TEMP>\n");
			else
				sb.append("      <DISPL_END_TEMP>" + st.endTemp + "</DISPL_END_TEMP>\n");
			sb.append("      <MIN>" + st.minutes + "</MIN>\n");
			sb.append("      <RAMP_MIN>" + st.rampMin + "</RAMP_MIN>\n");
			sb.append("      <METHOD>" + st.method + "</METHOD>\n");
			sb.append("      <WEIGHT_LBS>"	+ st.weightLbs + "</WEIGHT_LBS>\n");
			sb.append("      <DIRECTIONS>" + st.directions + "</DIRECTIONS>\n");
			sb.append("    </ITEM>\n");
		}

		sb.append("  </MASH>\n");
		return sb.toString();
	}
}