/*
 * Created on Oct 6, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

package strangebrew;

/**
 * @author aavis
 *
 * This is the class that holds the program options, and
 * knows how to save and read them.
 * Theoretically you could have options apply to each individual 
 * recipe, which SBWin only half implements.  I think it's a good
 * idea, and would make every recipe tweakable.
 * 
 * Now we create defaults, and we can load a file.
 *
 */

import java.util.*;
import java.io.*;

public class Options {
	//	declare global variables, set hard defaults		
	final boolean DEBUG = true;
	public Properties p;
	
	// constructor
	public Options(){
		Properties d = new Properties();
		setDefaults(d);
		p = new Properties(d);
	}
	
	public void loadProperties() {		
		try {	
			
			String path =
				getClass()
					.getProtectionDomain()
					.getCodeSource()
					.getLocation()
					.toString()
					.substring(6);
			p.load(new FileInputStream(path + "\\strangebrew.ini"));
			if (DEBUG) {

				System.out.println("ini file read. Contents: \n");
				p.list(System.out);
			}
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public void saveProperties() {

		try{
			String path =
				  getClass()
					  .getProtectionDomain()
					  .getCodeSource()
					  .getLocation()
					  .toString()
					  .substring(6);
		  FileOutputStream out = new FileOutputStream(path + "\\strangebrew.ini");
		  p.store(out, "/* properties updated */");
		  }
		catch (Exception e) {
		  System.out.println(e);
		  }
	}

	void setDefaults(Properties p) {
		p.put("optIBUCalcMethod", "Tinseth");
		p.put("optAlcCalcMethod", "Volume");
		p.put("optEvapCalcMethod", "Constant");
		p.put("optSizeUnits", "gallons US");
		p.put("optMaltUnits", "pounds");
		p.put("optHopsUnits", "ounces");
		p.put("optMashVolUnits", "gallons US");
		p.put("optMashTempUnits", "F");
		p.put("optPrimingSugar", "dextrose");
		p.put("optSugarUnits", "grams");
		p.put("optBottleUnits", "ml");
		p.put("optCarbTempU", "F");
		p.put("optMashRatioUnits", "qt/lb");
		p.put("optSourceWater", "");
		p.put("optTargetWater", "");
		p.put("optFirstScreen", "");
		p.put("optColourMethod", "SRM");
		p.put("optBrewer", "Your Name");
		p.put("optStreet", "Your Street");
		p.put("optCity", "Your City");
		p.put("optProv", "Your State/Prov");
		p.put("optCode", "Your zip/postal code");
		p.put("optPhone", "Your Phone");
		p.put("optClub", "Your Club");
		p.put("optCountry", "Your Country");
		p.put("optEmail", "Your Email");
		p.put("optMaltSortOrder", "By Name");
		p.put("optBrewDayStart", "10:00");

		// numerical
		p.put("optEfficiency", "75");
		p.put("optAttenuation", "75");
		p.put("optEvaporation", "1.5");
		p.put("optSize", "5");
		p.put("optMashRatio", "1.25");
		p.put("optGrainTemp", "68");
		p.put("optPelletHopsPct", "6");
		p.put("optMiscCost", "5.0");
		p.put("optBottleSize", "341");
		p.put("optBottleTemp", "68");
		p.put("optServeTemp", "45");
		p.put("optVolsCO2", "2.4");
		p.put("optBoilTime", "60");
		p.put("optTunLossF", "3");
		p.put("optKettleLoss", "1");
		p.put("optTrubLoss", "1");
		p.put("optMiscLoss", "1");
		p.put("optDryHopTime", "");
		p.put("optFWHTime", "1");
		p.put("optMashHopTime", "2");
		p.put("optHopsUtil", "4.15");
		p.put("optBoilTempF", "212");

		// ints
		p.put("optRed", "8");
		p.put("optGreen", "10");
		p.put("optBlue", "20");
		p.put("optPrepTime", "31");
		p.put("optSpargeTime", "60");
		p.put("optGetToBoilTime", "45");
		p.put("optChillTime", "45");
		p.put("optCleanTime", "120");

		// boleans:
		p.put("optMash", "true");
		p.put("optKegged", "true");
		p.put("optColourEfficiency", "false");

	}
	
	public String getProperty(String key){
			return p.getProperty(key);
	}

}
