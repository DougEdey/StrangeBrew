package strangebrew;

import java.util.*;
/**
 * $Id: Mash.java,v 1.1 2004/10/19 17:26:29 andrew_avis Exp $
 * @author aavis
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Mash {
	private double maltWeightLbs;
	private double volQts;
	private int totalTime;
	private ArrayList steps = new ArrayList();
	
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

	
		private MashStep(double st, double et, String tu, String m, int min, int rmin){
			startTemp = st;
			endTemp = et;
			tempU = m;
			minutes = min;
			rampMin = rmin;
		}
		
		// default constructor:
		private MashStep(){
			  rampMin = 0;
			  endTemp = 155;
			  startTemp = 155;
			  minutes = 60;
			  method = "infusion";
			  type = "alpha";
			  
	}
	
	public void addStep(double st, double et, String tu, String m, int min, int rmin){
		MashStep step = new MashStep(st, et, tu, m, min, rmin);
		steps.add(step);
	}
	
	// set methods:
	public void setMaltWeight(double mw){maltWeightLbs = mw;}
	
	
	// Introducing: the big huge mash calc method!
	
	public void calcMashSchedule(String tempU, String volU, double ct, double mr, String mru, double mlbs)
	{
	  // Generic routine to run through the mash table and calculate values

	  String tempUnits = tempU; 
	  String volUnits = volU; 
	  double currentTemp = ct;  
	  double strikeTemp = 0;
	  double targetTemp = 0; 
	  double endTemp = 0; 
	  double waterAddedQTS = 0;
	  double waterEquiv = 0;
	  double mashRatio = mr;  
	  double mashLBS = mlbs;
	  double displTemp = 0; 
	  double tunLoss = 0; // figure out a better way to do this, eg: themal mass
	  double decoct = 0;
	  int totalMashTime = 0;
	  double totalSpargeTime = 0;
	  double mashWaterQTS = 0;
	  double mashVolQTS = 0;
	  String directions;
	  String stepType;
	  int numSparge=0;

	  // convert mash ratio to qts/lb if in l/kg
	  if (mru.equalsIgnoreCase("l/kg")){
	    mashRatio *= 0.479325;
	  }
	  
	  // convert CurrentTemp to F
	  if (tempUnits == "C") {
	    currentTemp = cToF(currentTemp);
	    tunLoss = tunLoss/1.8;
	  }

	  // perform calcs on first record	  
	  if (steps.isEmpty())
	  	return;
	  
	  MashStep stp = ((MashStep) steps.get(0));
	  targetTemp = stp.startTemp;
	  endTemp = stp.endTemp;
	  strikeTemp = calcStrikeTemp(targetTemp, currentTemp, mashRatio, tunLoss);	  
	  waterAddedQTS = mashRatio * mashLBS;
	  waterEquiv = mashLBS * (0.192 + mashRatio);
	  mashVolQTS = calcMashVol(mashLBS, mashRatio);
	  totalMashTime += stp.minutes;
	  mashWaterQTS += waterAddedQTS;
	  stp.infuseVol = waterAddedQTS;
	  stp.method = "infusion";
	  
	  // subtract the water added from the Water Equiv so that they are correct when added in the next part of the loop
	  waterEquiv -= waterAddedQTS;

	    // Updated the water added
	    double waterAddedConv = stp.infuseVol;
	    if (tempUnits == "C") strikeTemp = fToC(strikeTemp);
	    directions = "Mash in with " + waterAddedConv + " " + volUnits + " of water at "
	       + strikeTemp + " " + tempUnits;

	  // set TargetTemp to the end temp
	  targetTemp = stp.endTemp;


	  for (int i = 1; i < steps.size(); i++) {
	  	stp = ((MashStep) steps.get(i));
	    currentTemp = targetTemp; // switch
	    targetTemp = stp.startTemp;
	
	    // do calcs
	    if (stp.method.equals("infusion"))
	    { // calculate an infusion step
	      decoct = 0;
	      waterEquiv += waterAddedQTS; // add previous addition to get WE
	      strikeTemp = 212;  // boiling water -- TODO: add from options

	      // Updated the water added
	      waterAddedQTS = calcWaterAddition(targetTemp, currentTemp, waterEquiv, 212);
	      waterAddedConv = waterAddedQTS;
	      if (tempUnits == "C") strikeTemp = 100;
	        directions = "Add " + waterAddedConv + " " + volUnits + " of water at "
	          + strikeTemp + " " + tempUnits;

	      mashWaterQTS += waterAddedQTS;
	      mashVolQTS += waterAddedQTS;

	    }

	    else if (stp.method.equals("decoction"))
	    { // calculate a decoction step

	      waterEquiv += waterAddedQTS; // add previous addition to get WE
	      waterAddedQTS = 0;
	      strikeTemp = 212;  // boiling water

	      // Calculate volume (qts) of mash to remove
	      decoct = calcDecoction(targetTemp, currentTemp, waterEquiv, 212);

	      // Updated the decoction, convert to right units & make directions
	      double decoctConv = decoct;
	      directions = "Remove " + decoctConv + " " + volUnits + " of mash, boil, and return to mash.";

	    }
	    else if (stp.method.equals("direct"))
	    { // calculate a direct heat step
	      decoct = 0;
	      waterEquiv += waterAddedQTS; // add previous addition to get WE
	      waterAddedQTS = 0;
	      directions = "Add direct heat until mash reaches " +
	        displTemp + " " + tempUnits + ".";
	    }

	    if (stp.method.equals("sparge"))
	    { // Count it
	      numSparge ++;
	    }
	    else
	      totalMashTime += stp.minutes;


	    stp.infuseVol = waterAddedQTS;
	    stp.directions = directions;
	    stp.decoctVol = decoct;

	    // set target temp to end temp for next step
	    targetTemp = stp.endTemp;
	    

	  } // while not eof

	
	
	  waterEquiv += waterAddedQTS; // add previous addition to get WE
	  totalTime = totalMashTime;

	  /*
	   * some water use stuff we can fix later:
	  double absorbedQTS = 0;
      absorbedQTS = mashWeightLbs * 0.55; // figure from HBD
	  // double spargeTotalQTS = (recipe presize in Qts )) - (mashWaterQTS - absorbedQTS);
	  double totalWaterQTS = spargeTotalQTS + mashWaterQTS;
	  double size = recipe size;
	  double chillShrink =  size * 0.03;
	  */

	  /*
	  // calculate sparge records:
	  int batches = ??;
	  batchSparge_t batchSparge;

	   // update the sparge records:

	  if(batches == NumSparge) { // we only need to update the records

	       // calc steps
	       if (batches == 1) {  // fly sparge
	         Directions = "Fly sparge with " + doubleToStrF(ConvertUnit("qt", VolUnits, "volume", SpargeTotalQTS), ffFixed, 5, 1)
	        + " " +  DataModule1->tblRecipe->FieldByName("MASH_VOL_U")->AsString;
	         DataModule1->tblMash->Edit();
	         DataModule1->tblMash->FieldByName("DIRECTIONS")->AsString = Directions;
	         DataModule1->tblMash->Post();
	         TotalSpargeTime += DataModule1->tblMash->FieldByName("MIN")->Asdouble;
	       }
	       else if (batches > 1) { // batch sparge
	         batchSparge.charges = batches;
	         calc_batch_sparge(batchSparge, AbsorbedQTS, MashWaterQTS,
	           ConvertUnit(DataModule1->tblRecipe->FieldByName("SIZE_UNITS")->AsString,
	           "qt", "volume", DataModule1->tblRecipe->FieldByName("PRESIZE")->Asdouble) );

	         // update records
	         DataModule1->tblMash->First();
	         while (DataModule1->tblMash->FieldByName("TYPE")->AsString != "sparge" &&  !DataModule1->tblMash->Eof)
	           DataModule1->tblMash->Next();

	         int i=0;
	         while (!DataModule1->tblMash->Eof && i < batchSparge.charges) {
	           Directions = "Batch " + String(i+1) + ": add " + doubleToStrF(ConvertUnit("qt", VolUnits, "volume", batchSparge.charge[i]), ffFixed, 5, 1)
	             + " " +  DataModule1->tblRecipe->FieldByName("MASH_VOL_U")->AsString;
	           Directions += ". Collect: " + doubleToStrF(ConvertUnit("qt", VolUnits, "volume", batchSparge.collect[i]), ffFixed, 5, 1)
	             + " " +  DataModule1->tblRecipe->FieldByName("MASH_VOL_U")->AsString;
	           DataModule1->tblMash->Edit();
	           DataModule1->tblMash->FieldByName("DIRECTIONS")->AsString = Directions;
	           DataModule1->tblMash->Post();
	           TotalSpargeTime += DataModule1->tblMash->FieldByName("MIN")->Asdouble;
	           i++;
	           DataModule1->tblMash->Next();
	         }
	       }  
	    }


	  else { // we need to calculate the steps, and update the records
	     // delete sparge records
	     DataModule1->tblMash->First();
	     while (!DataModule1->tblMash->Eof){
	        if (DataModule1->tblMash->FieldByName("TYPE")->AsString == "sparge" ||
	            DataModule1->tblMash->FieldByName("METHOD")->AsString == "fly sparge" ||
	            DataModule1->tblMash->FieldByName("METHOD")->AsString == "batch sparge")
	          DataModule1->tblMash->Delete();
	        else
	          DataModule1->tblMash->Next();
	     }

	     // calc steps
	     if (batches == 1) {  // fly sparge
	      Directions = "Fly sparge with " + doubleToStrF(ConvertUnit("qt", VolUnits, "volume", SpargeTotalQTS), ffFixed, 5, 1)
	         + " " +  DataModule1->tblRecipe->FieldByName("MASH_VOL_U")->AsString;
	      Directions += ". Collect: " + doubleToStrF(DataModule1->tblRecipe->FieldByName("PRESIZE")->Asdouble, ffFixed, 5, 1)
	         + " " +  DataModule1->tblRecipe->FieldByName("SIZE_UNITS")->AsString;
	      DataModule1->tblMash->Insert();
	      DataModule1->tblMash->FieldByName("DIRECTIONS")->AsString = Directions;
	      DataModule1->tblMash->FieldByName("TYPE")->AsString = "sparge";
	      DataModule1->tblMash->FieldByName("METHOD")->AsString = "fly sparge";
	      DataModule1->tblMash->FieldByName("TEMP")->Asdouble = 170;
	      DataModule1->tblMash->FieldByName("MIN")->AsInteger = appOptions.optSpargeTime;
	      DataModule1->tblMash->Post();
	      TotalSpargeTime += DataModule1->tblMash->FieldByName("MIN")->Asdouble;
	     }

	     else if (batches > 1) { // batch sparge
	       batchSparge.charges = batches;
	       calc_batch_sparge(batchSparge, AbsorbedQTS, MashWaterQTS,
	       ConvertUnit(DataModule1->tblRecipe->FieldByName("SIZE_UNITS")->AsString,
	         "qt", "volume", DataModule1->tblRecipe->FieldByName("PRESIZE")->Asdouble) );
	       // add records
	       DataModule1->tblMash->Last();
	       for (int i=0; i<batchSparge.charges; i++) {
	         Directions = "Batch " + String(i+1) + ": add " + doubleToStrF(ConvertUnit("qt", VolUnits, "volume", batchSparge.charge[i]), ffFixed, 5, 1)
	           + " " +  DataModule1->tblRecipe->FieldByName("MASH_VOL_U")->AsString;
	         Directions += ". Collect: " + doubleToStrF(ConvertUnit("qt", VolUnits, "volume", batchSparge.collect[i]), ffFixed, 5, 1)
	           + " " +  DataModule1->tblRecipe->FieldByName("MASH_VOL_U")->AsString;
	         DataModule1->tblMash->Insert();
	         DataModule1->tblMash->FieldByName("DIRECTIONS")->AsString = Directions;
	         DataModule1->tblMash->FieldByName("TYPE")->AsString = "sparge";
	         DataModule1->tblMash->FieldByName("METHOD")->AsString = "batch sparge";
	         DataModule1->tblMash->FieldByName("TEMP")->Asdouble = 170;
	         DataModule1->tblMash->FieldByName("MIN")->AsInteger = appOptions.optSpargeTime;
	         DataModule1->tblMash->Post();
	         TotalSpargeTime += DataModule1->tblMash->FieldByName("MIN")->Asdouble;
	       }
	   } */
	  }

	}
	
	
	
	// private methods:
	private String calcStepType(double temp)
	{
	  String stepType = "none";
	  // less than 90, none
	  // 90 - 103 - acid
	  if (temp > 90) stepType = "acid";
	  // 103 - 110 - gluten
	  else if (temp > 103) stepType = "gluten";
	  // 110 - 135 protein
	  else if (temp > 110) stepType = "protein";
	  // 135 - 149 beta
	  else if (temp > 135) stepType = "beta";
	  // 150-160 alpha
	  else if (temp > 150) stepType = "alpha";
	  // 160-169, mashout
	  else if (temp > 160) stepType = "mashout";
	  // over 170, sparge
	  else if (temp >= 170) stepType = "sparge";

	  return stepType;
	}

	private double calcStepTemp(String stepType)
	{
	  float stepTempF = 0;
	  if (stepType == "acid") stepTempF = 95;
	  else if (stepType == "gluten") stepTempF = 105;
	  else if (stepType == "protein") stepTempF = 122;
	  else if (stepType == "beta") stepTempF = 149;
	  else if (stepType == "alpha") stepTempF = 155;
	  else if (stepType == "mashout") stepTempF = 168;
	  else if (stepType == "sparge") stepTempF = 170;
	  
	  return stepTempF;
	}

	double calcMashVol(double grainWeightLBS, double ratio)
	{
	  // given lbs and ratio, what is the volume of the grain in quarts?
	  // note: this calc is for the first record only, and returns the heat equivalent of
	  // grain + water added for first infusion
	  // HBD posts indicate 0.32, but reality is closer to 0.42

	 return (grainWeightLBS * (0.42 + ratio));
	}

	double calcStrikeTemp(double targetTemp, double currentTemp, double ratio, double tunLossF)
	{
	  // calculate strike temp
	  // Ratio is in quarts / lb, TunLoss is in F
	  return (targetTemp + 0.192 * (targetTemp - currentTemp) / ratio) + tunLossF;
	}

	double calcWaterAddition(double targetTemp, double currentTemp, double mashVol, double boilTempF)
	{
	  // calculate amount of boiling water to add to raise mash to new temp
	  return (mashVol * (targetTemp - currentTemp) / (boilTempF - targetTemp));
	}

	double calcDecoction(double targetTemp, double currentTemp, double mashVol, double boilTempF)
	{
	  // calculate amount of grain/mash to remove and boil to raise mash temp to
	  // a target temperature
	  // returns percent

	  double dectPercent = (targetTemp - boilTempF) / (currentTemp - boilTempF);
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

}
