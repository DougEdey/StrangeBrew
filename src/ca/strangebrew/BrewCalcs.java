/*
 * Created on 5-Jun-2006
 * by aavis
 *
 */
package ca.strangebrew;

import java.util.ArrayList;

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
	
	public static double sgToGU(double sg) {
		return (sg - 1) * 1000;
	}
	
	public static double guToSG(double gu) {
		return (gu * 1000 )+ 1;
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

	public static double PrimingSugarGL(double DisVolsCO2, double TargetVolsCO2, PrimeSugar sugar)
	{
		// returns the priming sugar in grams/litre needed to
		// carbonate beer w/ a dissolved vols CO2 to reach the target vols CO2
		// based on an article by Dave Draper in the July/August 1996 issue of Brewing Techniques.
		double GramsPerLitre = (TargetVolsCO2 - DisVolsCO2) / 0.286;   

		GramsPerLitre /= sugar.getYield();

		return GramsPerLitre;
	}
	
	public static double waterChemistryDiff(double s, double t) {
		double r = s - t;
		if (r < 0) {
			r = r * -1;
		}
		
		return r;
	}
	
	public static WaterProfile calculateSalts(ArrayList salts, WaterProfile waterNeeds) {
		// Start with Epsom and set for Mg
		WaterProfile result = new WaterProfile();

		if (waterNeeds.getMg() > 0) {
			Salt epsom = Salt.getSaltByName(salts, "Magnesium Sulfate");
			if (epsom != null) {
				epsom.setAmount(waterNeeds.getMg() / epsom.getEffectByChem(Salt.MAGNESIUM));
				updateWater(result, epsom);
			}
		}
		
		if (waterNeeds.getSo4() > 0 &&
			result.getSo4() < waterNeeds.getSo4()) {
			Salt gypsum = Salt.getSaltByName(salts, "Calcium Carbonate");
			if (gypsum != null) {
				gypsum.setAmount((waterNeeds.getSo4() - result.getSo4()) / gypsum.getEffectByChem(Salt.SULPHATE));
				updateWater(result, gypsum);
			}
		}
		
		// This stuff needs work!
		// TODO
		// It shoudl do some sort of itterative process to find ideal levels
		
		if (waterNeeds.getNa() > 0 &&
			result.getNa() < waterNeeds.getNa()) {
			Salt salt = Salt.getSaltByName(salts, "Sodium Chloride");
			if (salt != null) {
				salt.setAmount((waterNeeds.getNa() - result.getNa()) / salt.getEffectByChem(Salt.SODIUM));
				updateWater(result, salt);
			}
		}

		if (waterNeeds.getHco3() > 0 &&
			result.getHco3() < waterNeeds.getHco3()) {
			Salt soda = Salt.getSaltByName(salts, "Sodium Bicarbonate");
			if (soda != null) {
				soda.setAmount((waterNeeds.getHco3() - result.getHco3()) / soda.getEffectByChem(Salt.CARBONATE));
				updateWater(result, soda);
			}
		}

		if (waterNeeds.getCa() > 0 &&
			result.getCa() < waterNeeds.getCa()) {
			Salt chalk = Salt.getSaltByName(salts, "Calcium Sulphate");
			if (chalk != null) {
				chalk.setAmount((waterNeeds.getCa() - result.getCa()) / chalk.getEffectByChem(Salt.CALCIUM));
				updateWater(result, chalk);
			}
		}
		
		// Missing CaCl2
		
		return result;
	}
	
	public static void updateWater(WaterProfile w, Salt s) {
		ArrayList effs = s.getChemicalEffects();
		for (int i = 0; i < effs.size(); i++) {
			Salt.ChemicalEffect eff = (Salt.ChemicalEffect)effs.get(i);
			
			if (eff.getElem().equals(Salt.MAGNESIUM)) {
				w.setMg(w.getMg() + (eff.getEffect() *  s.getAmount()));
			} else if (eff.getElem().equals(Salt.CHLORINE)) {
				w.setCl(w.getCl() + (eff.getEffect() *  s.getAmount()));
			} else if (eff.getElem().equals(Salt.SODIUM)) {
				w.setNa(w.getNa() + (eff.getEffect() *  s.getAmount()));
			} else if (eff.getElem().equals(Salt.SULPHATE)) {
				w.setSo4(w.getSo4() + (eff.getEffect() *  s.getAmount()));
			} else if (eff.getElem().equals(Salt.CARBONATE)) {
				w.setHco3(w.getHco3() + (eff.getEffect() *  s.getAmount()));
			} else if (eff.getElem().equals(Salt.CALCIUM)) {
				w.setCa(w.getCa() + (eff.getEffect() *  s.getAmount()));
			} else if (eff.getElem().equals(Salt.HARDNESS)) {
				w.setHardness(w.getHardness() + (eff.getEffect() *  s.getAmount()));
			} else if (eff.getElem().equals(Salt.ALKALINITY)) {
				w.setAlkalinity(w.getAlkalinity() + (eff.getEffect() * s.getAmount()));
			}
		}
	}	
}
