package strangebrew;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;

/**
 * $Id: Mash.java,v 1.10 2005/06/01 17:13:45 andrew_avis Exp $
 * @author aavis
 *
 */

public class Mash {
	
	// set this:
	private double maltWeightLbs;
	
	//options:
	private double mashRatio;
	private String mashRatioU;
	private String tempUnits;
	private String volUnits;
	private double grainTemp;
	private String mashTempU;
	
	// calculated:
	private double volQts;
	private int totalTime;
	private double absorbedQTS;
	private double spargeTotalQTS;
	private double totalWaterQTS;
	private double chillShrink;
	
	// steps:
	private ArrayList steps = new ArrayList();
	
	// format:
	public DecimalFormat df1 = new DecimalFormat("#####0.0");
	
	public Mash(){

		Options opts = new Options("mash");
		 mashRatio = opts.getDProperty("optMashRatio");
		 mashRatioU = opts.getProperty("optMashRatioU");;
		 tempUnits = opts.getProperty("optMashTempU");
		 volUnits = opts.getProperty("optMashVolU");
		 grainTemp = opts.getDProperty("optGrainTemp");
	}

	public class MashStep {
		private String type;
		private double startTemp;
		private double endTemp;
		private String tempU;
		private String method;
		private int minutes;
		private int rampMin;
		private String directions;

		public Quantity infuseVol = new Quantity();
		public Quantity decoctVol = new Quantity();

		public MashStep(String t, double st, double et, String tu, String m, int min,
				int rmin) {
			type = t;
			startTemp = st;
			endTemp = et;
			tempU = tu;
			method = m;
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

	public void addStep(String t, double st, double et, String tu, String m, int min,
			int rmin) {
		MashStep step = new MashStep(t, st, et, tu, m, min, rmin);
		steps.add(step);		
		calcMashSchedule();
	}
	
	public void addStep(){
		MashStep step = new MashStep();
		steps.add(step);
		calcMashSchedule();
	}
	
	public void delStep(int i){
		if (steps.size()>i && !steps.isEmpty()){			
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
	
	// get methods:
	public String getMashVolUnits(){ return volUnits; }
	public String getMashTempUnits(){ return tempUnits; }
	
	
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
	
	public void setStepEndTemp(int i, int t){
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
		// Generic routine to run through the mash table and calculate values

		
		double strikeTemp = 0;
		double targetTemp = 0;
		double endTemp = 0;
		double waterAddedQTS = 0;
		double waterEquiv = 0;
		double mr = mashRatio;
		double currentTemp = grainTemp;

		double displTemp = 0;
		double tunLoss = 0; // figure out a better way to do this, eg: themal mass
		double decoct = 0;
		int totalMashTime = 0;
		double totalSpargeTime = 0;
		double mashWaterQTS = 0;
		double mashVolQTS = 0;
		String stepType;
		int numSparge = 0;
		String mashVolU; // gotta define this somewhere

		// convert mash ratio to qts/lb if in l/kg
		if (mashRatioU.equalsIgnoreCase("l/kg")) {
			mr *= 0.479325;
		}

		// convert CurrentTemp to F
		if (mashTempU == "C") {
			currentTemp = cToF(currentTemp);
			tunLoss = tunLoss / 1.8;
		}

		// perform calcs on first record	  
		if (steps.isEmpty())
			return;
		
		// sort the list
		Collections.sort(steps, new stepComparator());

		MashStep stp = ((MashStep) steps.get(0));
		targetTemp = stp.startTemp;
		endTemp = stp.endTemp;
		strikeTemp = calcStrikeTemp(targetTemp, currentTemp, mr, tunLoss);
		waterAddedQTS = mr * maltWeightLbs;
		waterEquiv = maltWeightLbs * (0.192 + mr);
		mashVolQTS = calcMashVol(maltWeightLbs, mr);
		totalMashTime += stp.minutes;
		mashWaterQTS += waterAddedQTS;
		stp.infuseVol.setQuantity(null, "qt", waterAddedQTS);
		stp.method = "infusion";

		// subtract the water added from the Water Equiv so that they are correct when added in the next part of the loop
		waterEquiv -= waterAddedQTS;

		// Updated the water added

		if (tempUnits == "C")
			strikeTemp = fToC(strikeTemp);
		stp.directions = "Mash in with " + df1.format(stp.infuseVol.getValueAs(volUnits)) + " " + volUnits
				+ " of water at " + df1.format(strikeTemp) + " " + tempUnits;

		// set TargetTemp to the end temp
		targetTemp = stp.endTemp;

		for (int i = 1; i < steps.size(); i++) {
			stp = ((MashStep) steps.get(i));
			currentTemp = targetTemp; // switch
			targetTemp = stp.startTemp;

			// do calcs
			if (stp.method.equals("infusion")) { // calculate an infusion step
				decoct = 0;
				waterEquiv += waterAddedQTS; // add previous addition to get WE
				strikeTemp = 212; // boiling water -- TODO: add from options

				// Updated the water added
				waterAddedQTS = calcWaterAddition(targetTemp, currentTemp,
						waterEquiv, 212);
				stp.infuseVol.setQuantity(null, "qt", waterAddedQTS);
				if (tempUnits == "C")
					strikeTemp = 100;
				stp.directions = "Add " + df1.format(stp.infuseVol.getValueAs(volUnits)) + " " + volUnits
						+ " of water at " + df1.format(strikeTemp) + " " + tempUnits;

				mashWaterQTS += waterAddedQTS;
				mashVolQTS += waterAddedQTS;

			}

			else if (stp.method.equals("decoction")) { // calculate a decoction step

				waterEquiv += waterAddedQTS; // add previous addition to get WE
				waterAddedQTS = 0;
				strikeTemp = 212; // boiling water

				// Calculate volume (qts) of mash to remove
				decoct = calcDecoction(targetTemp, currentTemp, waterEquiv, 212);

				// Updated the decoction, convert to right units & make directions
				double decoctConv = decoct;
				stp.directions = "Remove " + df1.format(decoctConv) + " " + volUnits
						+ " of mash, boil, and return to mash.";

			} else if (stp.method.equals("direct")) { // calculate a direct heat step
				decoct = 0;
				waterEquiv += waterAddedQTS; // add previous addition to get WE
				waterAddedQTS = 0;
				stp.directions = "Add direct heat until mash reaches " + displTemp
						+ " " + tempUnits + ".";
			}

			if (stp.method.equals("sparge")) { // Count it
				numSparge++;
			} else
				totalMashTime += stp.minutes;

			stp.infuseVol.setQuantity(null, "qt", waterAddedQTS);
			stp.decoctVol.setQuantity(null, "qt", decoct);

			// set target temp to end temp for next step
			targetTemp = stp.endTemp;

		} // while not eof

		waterEquiv += waterAddedQTS; // add previous addition to get WE
		totalTime = totalMashTime;

		// water use stats:
		absorbedQTS = maltWeightLbs * 0.55; // figure from HBD
		// spargeTotalQTS = (r.getPreBoilVol("qt")) - (mashWaterQTS - absorbedQTS);
		totalWaterQTS = spargeTotalQTS + mashWaterQTS;
		// chillShrink = r.getPostBoilVol("gal") * 0.03;
		
		// TODO: sparge stuff should get figured here:		 

	}
	

	// private methods:
	private String calcStepType(double temp) {
		String stepType = "none";
		// less than 90, none
		// 90 - 103 - acid
		if (temp > 89 && temp < 104)
			stepType = "acid";
		// 103 - 110 - gluten
		else if (temp < 111)
			stepType = "gluten";
		// 110 - 135 protein
		else if (temp < 136)
			stepType = "protein";
		// 135 - 149 beta
		else if (temp < 150)
			stepType = "beta";
		// 150-160 alpha
		else if (temp < 161)
			stepType = "alpha";
		// 160-169, mashout
		else if (temp < 170)
			stepType = "mashout";
		// over 170, sparge
		else if (temp >= 170)
			stepType = "sparge";

		return stepType;
	}

	private double calcStepTemp(String stepType) {
		float stepTempF = 0;
		if (stepType == "acid")
			stepTempF = 95;
		else if (stepType == "gluten")
			stepTempF = 105;
		else if (stepType == "protein")
			stepTempF = 122;
		else if (stepType == "beta")
			stepTempF = 149;
		else if (stepType == "alpha")
			stepTempF = 155;
		else if (stepType == "mashout")
			stepTempF = 168;
		else if (stepType == "sparge")
			stepTempF = 170;

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
		return (targetTemp + 0.192 * (targetTemp - currentTemp) / ratio)
				+ tunLossF;
	}

	double calcWaterAddition(double targetTemp, double currentTemp,
			double mashVol, double boilTempF) {
		// calculate amount of boiling water to add to raise mash to new temp
		return (mashVol * (targetTemp - currentTemp) / (boilTempF - targetTemp));
	}

	double calcDecoction(double targetTemp, double currentTemp, double mashVol,
			double boilTempF) {
		// calculate amount of grain/mash to remove and boil to raise mash temp to
		// a target temperature
		// returns percent

		double dectPercent = (targetTemp - boilTempF)
				/ (currentTemp - boilTempF);
		dectPercent = 1 - dectPercent;
		return (mashVol * dectPercent);
	}

	private double fToC(double tempF) {
		// routine to convert basic F temps to C
		return (5 * (tempF - 32)) / 9;
	}

	private double cToF(double tempC) {
		// routine to convert Celcius to Farenheit
		return ((tempC * 9) / 5) + 32;
	}
	
	
	 private class batchSparge {
	    int charges;
	    double charge[];
	    double temp[];
	    double collect[];
	 };

	 void calc_batch_sparge(batchSparge bs, double absorbedQts, double usedQts, double total_collectQts) {
	   int i=0;
	   double collect = total_collectQts / bs.charges;

	   // all units in Qts!!!

	   // is there more in the tun than we need?
	   if (collect < usedQts - absorbedQts) {
	     bs.charge[0] = 0;
	     bs.collect[0] = usedQts - absorbedQts;
	     total_collectQts = total_collectQts - bs.collect[0];
	   }
	   else {
	     bs.charge[0] = collect - (usedQts - absorbedQts);
	     bs.collect[0] = collect;
	     total_collectQts = total_collectQts - bs.collect[0];
	   }

	   // do we need any more steps?
	   if (bs.charges == 1) return;

	   collect = total_collectQts / (bs.charges - 1);
	   for (i=1; i<bs.charges; i++) {
	     bs.charge[i] = collect;
	     bs.collect[i] = collect;
	   }

	   return;
	 };
	 
	 public String toXml() {
	
		StringBuffer sb = new StringBuffer();
		sb.append("    <MASH>\n");
		for (int i = 0; i < steps.size(); i++) {
			MashStep st = (MashStep) steps.get(i);
			sb.append("    <ITEM>\n");
			sb.append("      <TYPE>" + st.type + "</TYPE>\n");
			sb.append("      <TEMP>" + st.startTemp + "</TEMP>\n");
			sb.append("      <END_TEMP>" + st.endTemp + "</END_TEMP>\n");
			sb.append("      <METHOD>" + st.method + "</METHOD>\n");
			sb.append("      <DIRECTIONS>" + st.directions + "</DIRECTIONS>\n");
			sb.append("    </ITEM>\n");
		}

		sb.append("    </MASH>\n");
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