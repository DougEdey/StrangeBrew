/*
 * Created on 5-Jun-2006
 * by aavis
 *
 */
package ca.strangebrew;

public class BrewCalcs {

	//	 Hydrometer correction formula
	static double deltaSG(double TempC, double SG)
	{  // hydrometer correction formula based on post by AJ DeLange
	   // in HBD 3701
	   double coeffic[][] = {{56.084,   -0.17885,   -0.13063},    // 0-4.99
	                        {69.685,   -1.367,     -0.10621},    // 5 - 9.99
	                        {77.782,   -1.7288,    -0.10822},    // 10 - 14.99
	                        {87.895,   -2.3601,    -0.10285},    // 15 - 19.99
	                        {97.052,   -2.7729,    -0.10596}};   // 20 - 24.99

	   double plato = SGToPlato(SG);
	   int coefficIndex=4;
	   if (plato < 20)
	     coefficIndex = 3;
	   else
	   if (plato < 15)
	     coefficIndex = 2;
	   if (plato < 10)
	     coefficIndex = 1;
	   if (plato < 5)
	     coefficIndex = 0;

	   double deltaSG = (coeffic[coefficIndex][0])
	                   + (coeffic[coefficIndex][1] * TempC)
	                   + (coeffic[coefficIndex][2] * TempC * TempC);

	   // changed + to - from original
	   double CorrectedSG = platoToSG(plato - (deltaSG/100));
	   return CorrectedSG;

	}

	public static double SGToPlato(double SG)
	{ // function to convert a value in specific gravity as plato
	  // equation based on HBD#3204 post by AJ DeLange
	  double plato;
	  plato = -616.989 + 1111.488*SG - 630.606*SG*SG + 136.10305*SG*SG*SG;
	  return plato;
	}

	public static double platoToSG(double plato)
	{  // function to convert a value in Plato to SG
	   // equation based on HBD#3723 post by AJ DeLange
	   double SG;
	   SG = 1.0000131 + 0.00386777*plato + 1.27447E-5*plato*plato + 6.34964E-8*plato*plato*plato;
	   return SG;
	}

	public static double hydrometerCorrection(double tempC, double SG, double refTempC)
	{
	  double correctedSG = 0;
	  if (refTempC == 20.0)
	    correctedSG = deltaSG(tempC,SG);
	  else
	    correctedSG = SG * deltaSG(tempC, SG) / deltaSG(refTempC, SG);

	  return correctedSG;

	}

//	 Refractometer calculations

	public static double brixToSG(double brix) {
	  double sg = 1.000898 + 0.003859118*brix +
	             0.00001370735*brix*brix + 0.00000003742517*brix*brix*brix;
	  return sg;
	}

	public static double brixToFG(double ob, double fb){
	  double fg = 1.001843 - 0.002318474*ob -
	             0.000007775*ob*ob - 0.000000034*ob*ob*ob +
	             0.00574*fb + 0.00003344*fb*fb + 0.000000086*fb*fb*fb;
	  return fg;
	}

	public static double SGBrixToABV(double sg, double fb){
	  double ri  = 1.33302 + 0.001427193*fb + 0.000005791157*fb*fb;
	  double abw = 1017.5596 - (277.4*sg) + ri*((937.8135*ri) - 1805.1228);
	  double abv = abw * 1.25;
	  return abv;
	}

	
	public static double fToC(double tempF) {
		// routine to convert basic F temps to C
		return (5 * (tempF - 32)) / 9;
	}

	public static double cToF(double tempC) {
		// routine to convert Celcius to Farenheit
		return ((tempC * 9) / 5) + 32;
	}
	
	/*
	 *  Carbonation calculations	 * 
	 */
	
	public static double dissolvedCO2(double BottleTemp)
	  {
	  // returns the amount of dissolved CO2 in beer at BottleTemp
	   double disCO2 = (1.266789*BottleTemp) + 31.00342576 - (0.0000009243372*
	        (Math.sqrt((1898155717178L* Math.pow(BottleTemp,2)) +
	        91762600000000L*BottleTemp + 839352900000000L - 1710565000000L*14.5)));
	   return disCO2;
	  }

	public static double  KegPSI(double Temp, double VolsCO2)
	{
	  // returns the PSI needed to carbonate beer at Temp at VolsCO2
		double PSI = -16.6999 - (0.0101059 * Temp) + (0.00116512 * Math.pow(Temp,2)) + (0.173354 * Temp * VolsCO2)
	    + (4.24267 * VolsCO2) - (0.0684226 * Math.pow(VolsCO2,2));
		return PSI;
	}

	public static double PrimingSugarGL(double DisVolsCO2, double TargetVolsCO2, String SugarType)
	{
	  // returns the priming sugar in grams/litre needed to
	  // carbonate beer w/ a dissolved vols CO2 to reach the target vols CO2
	  // based on an article by Dave Draper in the July/August 1996 issue of Brewing Techniques.
		double GramsPerLitre = (TargetVolsCO2 - DisVolsCO2) / 0.286;   

	  if (SugarType == "dextrose")
	    GramsPerLitre *= 1.15; // add 15%
	  if (SugarType == "honey")
	    GramsPerLitre *= 1.40; // add 40%
	  if (SugarType == "maple syrup")
	    GramsPerLitre *= 1.50; // add 50%
	  if (SugarType == "molasses")
	    GramsPerLitre *= 1.80; // add 80%
	  if (SugarType == "DME")
	    GramsPerLitre *= 1.30; // add 30%
	  if (SugarType == "LME")
	    GramsPerLitre *= 1.40; // add 40%
	  // brown sugar is the same as table sugar

	  return GramsPerLitre;
	}
}
