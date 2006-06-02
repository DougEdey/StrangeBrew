package ca.strangebrew;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * $Id: Mash.java,v 1.23 2006/06/02 19:44:26 andrew_avis Exp $
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

	
	// calculated:
	private double volQts;
	private int totalTime;
	private double absorbedQTS;
	private double totalWaterQTS;
	private double spargeQTS;
	
	// steps:
	private ArrayList steps = new ArrayList();
	
	// configurable temps, can be set by the user:
	// target temps are 1/2 between temp + next temp

	public float ACIDTMPF = 85;
	public float GLUCANTMPF = 95;
	public float PROTEINTMPF = 113;
	public float BETATMPF = 131;
	public float ALPHATMPF = 151;
	public float MASHOUTTMPF = 161;
	public float SPARGETMPF = 170;
	
	
	public Mash(Recipe r){

		 Options opts = new Options("mash");
		 mashRatio = opts.getDProperty("optMashRatio");
		 mashRatioU = opts.getProperty("optMashRatioU");;
		 tempUnits = opts.getProperty("optMashTempU");
		 volUnits = opts.getProperty("optMashVolU");
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
		 
		 myRecipe = r;
	}

	public class MashStep {
		private String type;
		private double startTemp;
		private double endTemp;		
		private String method;
		private int minutes;
		private int rampMin;
		private String directions;

		public Quantity infuseVol = new Quantity();
		public Quantity decoctVol = new Quantity();

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
			endTemp = 152;
			startTemp = 152;
			minutes = 60;
			method = "infusion";
			type = "alpha";

		}
	
		// getter/setter methods	
		
		public Quantity getDecoctVol() {
			return decoctVol;
		}

		public void setDecoctVol(Quantity decoctVol) {
			this.decoctVol = decoctVol;
		}
	
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

		public Quantity getInfuseVol() {
			return infuseVol;
		}

		public void setInfuseVol(Quantity infuseVol) {
			this.infuseVol = infuseVol;
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

		public String getType() {
			return type;
		}
		
		public void setType(String s){ type = s; }
	}

	public void addStep(String type, double st, double et, String m, int min,
			int rmin) {
		MashStep step = new MashStep(type, st, et, m, min, rmin);
		steps.add(step);		
		calcMashSchedule();
	}
	
	public void addStep(){
		MashStep step = new MashStep();
		steps.add(step);
		calcMashSchedule();
	}
	
	public void delStep(int i){
		if (steps.size()>i && !steps.isEmpty() && i > -1){			
			steps.remove(i);
			calcMashSchedule();
		}
			
	}

	// set methods:
	public void setMaltWeight(double mw) {	maltWeightLbs = mw;	}
	public void setMashRatio(double mr){ 
		mashRatio = mr; 
		calcMashSchedule();
	}	
	
	public void setMashRatioU(String u){ 
		mashRatioU = u;
		calcMashSchedule();
	}
	
	public void setMashVolUnits(String u){ 
		volUnits = u;
		calcMashSchedule();
	}
	
	public void setMashTempUnits(String t){
		tempUnits = t;
		calcMashSchedule();
	}
	
	public void setGrainTemp(double t){
		if (tempUnits.equals("F"))
			grainTempF = t;
		else
			grainTempF = cToF(t);
		calcMashSchedule();
	}
	
	public void setBoilTemp(double t){
		if (tempUnits.equals("F"))
			boilTempF = t;
		else
			boilTempF = cToF(t);
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

	
	/**
	 * 
	 * @param val Value to convert to a string
	 * @return Val converted to the mash vol, formated to 1 decimal
	 */
	private String getVolConverted(double val){
		double d = Quantity.convertUnit("qt", volUnits, val); 
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
			return fToC(grainTempF);
		}
	public double getBoilTemp() { 
		if (tempUnits.equals("F")) 
				return boilTempF;
		else
			return fToC(boilTempF);
		}
	
	public double getSpargeVol(){
		return Quantity.convertUnit("qt", volUnits, spargeQTS);
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
		double d = Quantity.convertUnit("qt", volUnits, volQts);
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
		calcMashSchedule();
	}
	
	public String getStepMethod(int i) {
		return ((MashStep)steps.get(i)).getMethod();	
	}
	
	public void setStepStartTemp(int i, double t){		
		if (tempUnits.equals("C")){
			t = cToF(t);			
		}				
		((MashStep)steps.get(i)).setStartTemp(t);
		((MashStep)steps.get(i)).setEndTemp(t);
		((MashStep)steps.get(i)).setType(calcStepType(t));
		
		calcMashSchedule();
		
	}
	
	public double getStepStartTemp(int i) {
		if (tempUnits.equals("C"))
			return fToC(((MashStep)steps.get(i)).getStartTemp());
		else
			return ((MashStep)steps.get(i)).getStartTemp();	
	}
	
	public void setStepEndTemp(int i, double t){
		if (tempUnits.equals("C"))
			((MashStep)steps.get(i)).setEndTemp(cToF(t));
		else
			((MashStep)steps.get(i)).setEndTemp(t);
		calcMashSchedule();
	}
	
	public double getStepEndTemp(int i) {
		if (tempUnits.equals("C"))
			return fToC(((MashStep)steps.get(i)).getEndTemp());
		else
			return ((MashStep)steps.get(i)).getEndTemp();	
		
	}
	
	public void setStepRampMin(int i, int m){
		((MashStep)steps.get(i)).setRampMin(m);
	}
	
	public int getStepRampMin(int i) {
		return ((MashStep)steps.get(i)).getRampMin();	
	}
	
	public void setStepMin(int i, int m){
		((MashStep)steps.get(i)).setMinutes(m);
	}
	
	public int getStepMin(int i) {
		return ((MashStep)steps.get(i)).getMinutes();	
	}

	public double getStepInfuseVol(int i) {
		return ((MashStep)steps.get(i)).getInfuseVol().getValue();	
	}
	
	public double getStepDecoctVol(int i) {
		return ((MashStep)steps.get(i)).getDecoctVol().getValue();	
	}
	
	
	
	
	public int getStepSize(){
		return steps.size();
	}
	
	// Introducing: the big huge mash calc method!

	public void calcMashSchedule() {
		// Method to run through the mash table and calculate values

		
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
		
		// convert mash ratio to qts/lb if in l/kg
		if (mashRatioU.equalsIgnoreCase("l/kg")) {
			mr *= 0.479325;
		}

		// convert CurrentTemp to F
		if (tempUnits == "C") {
			currentTemp = cToF(currentTemp);
			tunLoss = tunLossF * 1.8;
		}
		else
			tunLoss = tunLossF;

		// perform calcs on first record	  
		if (steps.isEmpty())
			return;
		
		// sort the list
		Collections.sort(steps, new stepComparator());

		// the first step is always an infusion
		MashStep stp = ((MashStep) steps.get(0));
		targetTemp = stp.startTemp;
		strikeTemp = calcStrikeTemp(targetTemp, currentTemp, mr, tunLoss);
		waterAddedQTS = mr * maltWeightLbs;
		waterEquiv = maltWeightLbs * (0.192 + mr);
		mashVolQTS = calcMashVol(maltWeightLbs, mr);
		totalMashTime += stp.minutes;
		mashWaterQTS += waterAddedQTS;		
		stp.infuseVol.setUnits("qt");
		stp.infuseVol.setAmount(waterAddedQTS);
		stp.method = "infusion";

		// subtract the water added from the Water Equiv so that they are correct when added in the next part of the loop
		waterEquiv -= waterAddedQTS;

		// Updated the water added

		if (tempUnits == "C")
			strikeTemp = fToC(strikeTemp);
		stp.directions = "Mash in with " + SBStringUtils.format(stp.infuseVol.getValueAs(volUnits),1 ) + " " + volUnits
				+ " of water at " + SBStringUtils.format(strikeTemp, 1) + " " + tempUnits;

		// set TargetTemp to the end temp
		targetTemp = stp.endTemp;

		for (int i = 1; i < steps.size(); i++) {
			stp = ((MashStep) steps.get(i));
			currentTemp = targetTemp; // switch
			targetTemp = stp.startTemp;
			
			// if this is a former sparge step that's been changed, change
			// the method to infusion
			if (!stp.type.equals("sparge") && ( stp.method.equals("fly") || stp.method.equals("batch")))
					stp.method = "infusion";
			
			// do calcs
			if (stp.method.equals("infusion")) { // calculate an infusion step
				decoct = 0;
				waterEquiv += waterAddedQTS; // add previous addition to get WE
				strikeTemp = boilTempF; // boiling water 

				// Updated the water added
				waterAddedQTS = calcWaterAddition(targetTemp, currentTemp,
						waterEquiv, boilTempF);
				
				stp.infuseVol.setUnits("qt");
				stp.infuseVol.setAmount(waterAddedQTS);
				if (tempUnits == "C")
					strikeTemp = 100;
				stp.directions = "Add " + SBStringUtils.format(stp.infuseVol.getValueAs(volUnits), 1) + " " + volUnits
						+ " of water at " + SBStringUtils.format(strikeTemp, 1) + " " + tempUnits;

				mashWaterQTS += waterAddedQTS;
				mashVolQTS += waterAddedQTS;

			}

			else if (stp.method.indexOf("decoction") > -1) { // calculate a decoction step

				waterEquiv += waterAddedQTS; // add previous addition to get WE
				waterAddedQTS = 0;
				strikeTemp = boilTempF; // boiling water
				double ratio=0.75;

				if (stp.method.indexOf("thick") > -1)
					ratio = thickDecoctRatio;
				else if (stp.method.indexOf("thin") > -1)
					ratio = thinDecoctRatio;
				// Calculate volume (qts) of mash to remove
				decoct = calcDecoction2(targetTemp, currentTemp, mashWaterQTS, ratio);				
				stp.decoctVol.setUnits("qt");
				stp.decoctVol.setAmount(decoct);
				// Updated the decoction, convert to right units & make directions
				stp.directions = "Remove " + SBStringUtils.format(stp.decoctVol.getValueAs(volUnits), 1) + " " + volUnits
						+ " of mash, boil, and return to mash.";

			} else if (stp.method.equals("direct")) { // calculate a direct heat step
				decoct = 0;
				waterEquiv += waterAddedQTS; // add previous addition to get WE
				waterAddedQTS = 0;
				stp.directions = "Add direct heat until mash reaches " + displTemp
						+ " " + tempUnits + ".";
			}

			if (stp.type.equals("sparge")) 
				numSparge++;

		    else {
				totalMashTime += stp.minutes;

			}
			
			stp.infuseVol.setUnits("qt");
			stp.infuseVol.setAmount(waterAddedQTS);			
			stp.decoctVol.setUnits("qt");
			stp.decoctVol.setAmount(decoct);
			
			// set target temp to end temp for next step
			targetTemp = stp.endTemp;

		} // for steps.size()

		waterEquiv += waterAddedQTS; // add previous addition to get WE
		totalTime = totalMashTime;
		volQts = mashVolQTS;

		// water use stats:
		absorbedQTS = maltWeightLbs * 0.55; // figure from HBD
		
		// spargeTotalQTS = (myRecipe.getPreBoilVol("qt")) - (mashWaterQTS - absorbedQTS);
		totalWaterQTS = mashWaterQTS;
		spargeQTS = myRecipe.getPreBoilVol("qt") - (mashWaterQTS - absorbedQTS);		
		
		
		// Now let's figure out the sparging:		
		if (numSparge == 0)
			return;
		
		double col = myRecipe.getPreBoilVol("qt") / numSparge;
		double charge[] = new double[numSparge];
	    double collect[] = new double[numSparge];
	    double totalCollectQts = myRecipe.getPreBoilVol("qt");
	    
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
			if (stp.getType().equals("sparge")){				
				totalSpargeTime += stp.getMinutes();
				String collectStr = SBStringUtils.format(Quantity.convertUnit("qt", volUnits, collect[j]), 1) +
				" " + volUnits;
				String tempStr;
				if (tempUnits.equals("F"))
					tempStr = "" + SBStringUtils.format(SPARGETMPF, 1) + "F";
				else 
					tempStr = SBStringUtils.format(fToC(SPARGETMPF), 1) + "C";		
				if (numSparge > 1){
					stp.setMethod("batch");
					String add = SBStringUtils.format(Quantity.convertUnit("qt", volUnits, charge[j]), 1) +
					" " + volUnits;			
					stp.setDirections("Add " + add + " at " + tempStr + " to collect " + collectStr);
					
				}
				else {
					stp.setMethod("fly");					
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
	
	private double calcDecoction2(double targetTemp, double currentTemp, double waterVolQTS, double ratio){
		double decoctQTS=0;

		double g = 1 / (ratio + .32);
		double w = 2 * g * ratio;

		decoctQTS = ((targetTemp - currentTemp) * ((0.4 * maltWeightLbs) + (2 * waterVolQTS)))
			/ ((boilTempF - currentTemp) * (0.4 * g + w));


		return decoctQTS;
	}
	

	private String calcStepType(double temp) {
		String stepType = "none";
		// less than 90, none
		// 86 - 95 - acid
		if (temp >= ACIDTMPF && temp < GLUCANTMPF)
			stepType = "acid";
		// 95 - 113 - glucan
		else if (temp < PROTEINTMPF)
			stepType = "glucan";
		// 113 - 131 protein
		else if (temp < BETATMPF)
			stepType = "protein";
		// 131 - 150 beta
		else if (temp < ALPHATMPF)
			stepType = "beta";
		// 150-162 alpha
		else if (temp < MASHOUTTMPF)
			stepType = "alpha";
		// 163-169, mashout
		else if (temp < SPARGETMPF)
			stepType = "mashout";
		// over 170, sparge
		else if (temp >= SPARGETMPF)
			stepType = "sparge";

		return stepType;
	}

	private double calcStepTemp(String stepType) {
		float stepTempF = 0;
		if (stepType == "acid")
			stepTempF = (ACIDTMPF + GLUCANTMPF) / 2;
		else if (stepType == "glucan")
			stepTempF = (GLUCANTMPF + PROTEINTMPF) / 2;
		else if (stepType == "protein")
			stepTempF = (PROTEINTMPF + BETATMPF) / 2;
		else if (stepType == "beta")
			stepTempF = (BETATMPF + ALPHATMPF) / 2;
		else if (stepType == "alpha")
			stepTempF = (ALPHATMPF + MASHOUTTMPF) / 2;
		else if (stepType == "mashout")
			stepTempF = (MASHOUTTMPF + SPARGETMPF) / 2;
		else if (stepType == "sparge")
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

	public static double fToC(double tempF) {
		// routine to convert basic F temps to C
		return (5 * (tempF - 32)) / 9;
	}

	public static double cToF(double tempC) {
		// routine to convert Celcius to Farenheit
		return ((tempC * 9) / 5) + 32;
	}
	
		 
	 public String toXml() {
	
		StringBuffer sb = new StringBuffer();
		sb.append("  <MASH>\n");
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
			sb.append(SBStringUtils.xmlElement("GRAIN_TEMP", "" + fToC(grainTempF), 4));
			sb.append(SBStringUtils.xmlElement("BOIL_TEMP", "" + fToC(boilTempF), 4));
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
				sb.append("      <DISPL_TEMP>" + SBStringUtils.format(fToC(st.startTemp), 1) + "</DISPL_TEMP>\n");
			else
				sb.append("      <DISPL_TEMP>" + st.startTemp + "</DISPL_TEMP>\n");
			sb.append("      <END_TEMP>" + st.endTemp + "</END_TEMP>\n");
			if (tempUnits.equals("C"))
				sb.append("      <DISPL_END_TEMP>" + SBStringUtils.format(fToC(st.endTemp), 1) + "</DISPL_END_TEMP>\n");
			else
				sb.append("      <DISPL_END_TEMP>" + st.endTemp + "</DISPL_END_TEMP>\n");
			sb.append("      <MIN>" + st.minutes + "</MIN>\n");
			sb.append("      <RAMP_MIN>" + st.rampMin + "</RAMP_MIN>\n");
			sb.append("      <METHOD>" + st.method + "</METHOD>\n");
			sb.append("      <DIRECTIONS>" + st.directions + "</DIRECTIONS>\n");
			sb.append("    </ITEM>\n");
		}

		sb.append("  </MASH>\n");
		return sb.toString();
	}
	
	 /**
		 * 
		 * @author aavis
		 *
		 * step comparator to help sort mash steps
		 * 
		 */
		public class stepComparator implements Comparator {			
			public int compare(Object a, Object b) {
				Double a1 = new Double(((MashStep)a).getStartTemp());
				Double b1 = new Double(((MashStep)b).getStartTemp());				
				int result = a1.compareTo(b1);
				return (result == 0 ? -1 : result);
				
			}
		}

}