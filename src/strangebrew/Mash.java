package strangebrew;

import java.util.ArrayList;
/**
 * $Id: Mash.java,v 1.3 2004/10/25 17:08:21 andrew_avis Exp $
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
	
	public Mash(){

		Options opts = new Options("mash");
		 mashRatio = opts.getDProperty("optMashRatio");
		 mashRatioU = opts.getProperty("optMashRatioU");;
		 tempUnits = opts.getProperty("optMashTempU");
		 volUnits = opts.getProperty("optMashVolU");
		 grainTemp = opts.getDProperty("optGrainTemp");
	}

	private class MashStep {
		private String type;
		private double startTemp;
		private double endTemp;
		private String tempU;
		private String method;
		private int minutes;
		private int rampMin;
		private String directions;

		private double infuseVol;
		private double decoctVol;

		private MashStep(double st, double et, String tu, String m, int min,
				int rmin) {
			startTemp = st;
			endTemp = et;
			tempU = m;
			minutes = min;
			rampMin = rmin;
		}

		// default constructor:
		private MashStep() {
			rampMin = 0;
			endTemp = 155;
			startTemp = 155;
			minutes = 60;
			method = "infusion";
			type = "alpha";

		}
	}

	public void addStep(double st, double et, String tu, String m, int min,
			int rmin) {
		MashStep step = new MashStep(st, et, tu, m, min, rmin);
		steps.add(step);
	}

	// set methods:
	public void setMaltWeight(double mw) {
		maltWeightLbs = mw;
	}

	public void setMashRatio(double mr){
		mashRatio = mr;
	}
	
	public void setMashRatioU(String u){
		mashRatioU = u;
	}
	
	// Introducing: the big huge mash calc method!

	public void calcMashSchedule(Recipe r) {
		// Generic routine to run through the mash table and calculate values

		
		double strikeTemp = 0;
		double targetTemp = 0;
		double endTemp = 0;
		double waterAddedQTS = 0;
		double waterEquiv = 0;

		double displTemp = 0;
		double tunLoss = 0; // figure out a better way to do this, eg: themal mass
		double decoct = 0;
		int totalMashTime = 0;
		double totalSpargeTime = 0;
		double mashWaterQTS = 0;
		double mashVolQTS = 0;
		String directions;
		String stepType;
		int numSparge = 0;
		String mashVolU; // gotta define this somewhere

		// convert mash ratio to qts/lb if in l/kg
		if (mashRatioU.equalsIgnoreCase("l/kg")) {
			mashRatio *= 0.479325;
		}

		// convert CurrentTemp to F
		if (mashTempU == "C") {
			grainTemp = cToF(grainTemp);
			tunLoss = tunLoss / 1.8;
		}

		// perform calcs on first record	  
		if (steps.isEmpty())
			return;

		MashStep stp = ((MashStep) steps.get(0));
		targetTemp = stp.startTemp;
		endTemp = stp.endTemp;
		strikeTemp = calcStrikeTemp(targetTemp, grainTemp, mashRatio, tunLoss);
		waterAddedQTS = mashRatio * r.getTotalMashLbs();
		waterEquiv = r.getTotalMashLbs() * (0.192 + mashRatio);
		mashVolQTS = calcMashVol(r.getTotalMashLbs(), mashRatio);
		totalMashTime += stp.minutes;
		mashWaterQTS += waterAddedQTS;
		stp.infuseVol = waterAddedQTS;
		stp.method = "infusion";

		// subtract the water added from the Water Equiv so that they are correct when added in the next part of the loop
		waterEquiv -= waterAddedQTS;

		// Updated the water added
		double waterAddedConv = stp.infuseVol;
		if (tempUnits == "C")
			strikeTemp = fToC(strikeTemp);
		directions = "Mash in with " + waterAddedConv + " " + volUnits
				+ " of water at " + strikeTemp + " " + tempUnits;

		// set TargetTemp to the end temp
		targetTemp = stp.endTemp;

		for (int i = 1; i < steps.size(); i++) {
			stp = ((MashStep) steps.get(i));
			grainTemp = targetTemp; // switch
			targetTemp = stp.startTemp;

			// do calcs
			if (stp.method.equals("infusion")) { // calculate an infusion step
				decoct = 0;
				waterEquiv += waterAddedQTS; // add previous addition to get WE
				strikeTemp = 212; // boiling water -- TODO: add from options

				// Updated the water added
				waterAddedQTS = calcWaterAddition(targetTemp, grainTemp,
						waterEquiv, 212);
				waterAddedConv = waterAddedQTS;
				if (tempUnits == "C")
					strikeTemp = 100;
				directions = "Add " + waterAddedConv + " " + volUnits
						+ " of water at " + strikeTemp + " " + tempUnits;

				mashWaterQTS += waterAddedQTS;
				mashVolQTS += waterAddedQTS;

			}

			else if (stp.method.equals("decoction")) { // calculate a decoction step

				waterEquiv += waterAddedQTS; // add previous addition to get WE
				waterAddedQTS = 0;
				strikeTemp = 212; // boiling water

				// Calculate volume (qts) of mash to remove
				decoct = calcDecoction(targetTemp, grainTemp, waterEquiv, 212);

				// Updated the decoction, convert to right units & make directions
				double decoctConv = decoct;
				directions = "Remove " + decoctConv + " " + volUnits
						+ " of mash, boil, and return to mash.";

			} else if (stp.method.equals("direct")) { // calculate a direct heat step
				decoct = 0;
				waterEquiv += waterAddedQTS; // add previous addition to get WE
				waterAddedQTS = 0;
				directions = "Add direct heat until mash reaches " + displTemp
						+ " " + tempUnits + ".";
			}

			if (stp.method.equals("sparge")) { // Count it
				numSparge++;
			} else
				totalMashTime += stp.minutes;

			stp.infuseVol = waterAddedQTS;
			stp.directions = directions;
			stp.decoctVol = decoct;

			// set target temp to end temp for next step
			targetTemp = stp.endTemp;

		} // while not eof

		waterEquiv += waterAddedQTS; // add previous addition to get WE
		totalTime = totalMashTime;

		// water use stats:
		absorbedQTS = r.getTotalMashLbs() * 0.55; // figure from HBD
		spargeTotalQTS = (r.getPreBoilVol("qt")) - (mashWaterQTS - absorbedQTS);
		totalWaterQTS = spargeTotalQTS + mashWaterQTS;
		chillShrink = r.getPostBoilVol("gal") * 0.03;
		 

	}

	// private methods:
	private String calcStepType(double temp) {
		String stepType = "none";
		// less than 90, none
		// 90 - 103 - acid
		if (temp > 90)
			stepType = "acid";
		// 103 - 110 - gluten
		else if (temp > 103)
			stepType = "gluten";
		// 110 - 135 protein
		else if (temp > 110)
			stepType = "protein";
		// 135 - 149 beta
		else if (temp > 135)
			stepType = "beta";
		// 150-160 alpha
		else if (temp > 150)
			stepType = "alpha";
		// 160-169, mashout
		else if (temp > 160)
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
	
	/*
	 struct batchSparge_t {
	    int charges;
	    float charge[3];
	    float temp[3];
	    float collect[3];
	 };

	 void calc_batch_sparge(batchSparge_t &batch, float absorbedQts, float usedQts, float total_collectQts) {
	   int i=0;
	   float collect = total_collectQts / batch.charges;

	   // all units in Qts!!!

	   // is there more in the tun than we need?
	   if (collect < usedQts - absorbedQts) {
	     batch.charge[0] = 0;
	     batch.collect[0] = usedQts - absorbedQts;
	     total_collectQts = total_collectQts - batch.collect[0];
	   }
	   else {
	     batch.charge[0] = collect - (usedQts - absorbedQts);
	     batch.collect[0] = collect;
	     total_collectQts = total_collectQts - batch.collect[0];
	   }

	   // do we need any more steps?
	   if (batch.charges == 1) return;

	   collect = total_collectQts / (batch.charges - 1);
	   for (i=1; i<batch.charges; i++) {
	     batch.charge[i] = collect;
	     batch.collect[i] = collect;
	   }

	   return;
	 };
	 */

}