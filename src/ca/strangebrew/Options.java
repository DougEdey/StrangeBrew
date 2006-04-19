package ca.strangebrew;

/**
 * $Id: Options.java,v 1.4 2006/04/19 20:03:58 andrew_avis Exp $
 * Created on Oct 6, 2004
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;


public class Options {
	//	declare global variables, set hard defaults		
	
	private Properties props;
	private String type;
	
	private String optionString[][] = { 
			// calculations:
			{"optIBUCalcMethod", "Tinseth"},
			{"optAlcCalcMethod", "Volume"},
			{"optEvapCalcMethod", "Constant"},
			{"optColourMethod", "SRM"},
			{"optEfficiency", "75"},
			{"optAttenuation", "75"},
			{"optEvaporation", "1.5"},
			{"optPelletHopsPct", "6"},
			{"optDryHopTime", "0"},
			{"optFWHTime", "1"},
			{"optMashHopTime", "2"},
			{"optHopsUtil", "4.15"},
			{"optColourEfficiency", "false"},
			
			// recipe basics:
			{"optSizeU", "gallons US"},
			{"optMaltU", "pounds"},
			{"optHopsU", "ounces"},
			{"optPostBoilVol", "5"},
			{"optPreBoilVol", "6"},
			{"optMash", "true"},

			// carbonation:
			{"optPrimingSugar", "dextrose"},
			{"optSugarU", "grams"},
			{"optBottleU", "ml"},
			{"optCarbTempU", "F"},
			{"optBottleSize", "341"},
			{"optBottleTemp", "68"},
			{"optServeTemp", "45"},
			{"optVolsCO2", "2.4"},
			{"optKegged", "true"},

			// water:
			{"optSourceWater", ""},
			{"optTargetWater", ""},
			
			
			// labels:
			{"optBrewer", "Your Name"},
			{"optStreet", "Your Street"},
			{"optCity", "Your City"},
			{"optProv", "Your State/Prov"},
			{"optCode", "Your zip/postal code"},
			{"optPhone", "Your Phone"},
			{"optClub", "Your Club"},
			{"optCountry", "Your Country"},
			{"optEmail", "Your Email"},
			
			// appearance:
			{"optMaltSortOrder", "By Name"},
			{"optFirstScreen", ""},
			
			// time:
			{"optBrewDayStart", "10:00"},
			{"optPrepTime", "31"},
			{"optSpargeTime", "60"},
			{"optGetToBoilTime", "45"},
			{"optChillTime", "45"},
			{"optCleanTime", "120"},
			{"optBoilTime", "60"},

			// water use:
			{"optKettleLoss", "1"},
			{"optTrubLoss", "1"},
			{"optMiscLoss", "1"},
			
			// cost:
			{"optMiscCost", "5.0"},
	};
	
	// default constructor, create options for a recipe
	public Options(){
		this("strangebrew");
	}
	
	// constructor for other option types
	public Options(String t){
		type = t;
		Properties d = new Properties();
		setDefaults(d);
		props = new Properties(d);		
		loadProperties();
	}
	
	private String getPath(){
		
		
		String path = "";
		String slash = System.getProperty("file.separator");
		try {
			path = new File(".").getCanonicalPath();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Debug.print(path);
		
		return path;
	}
	
	private void loadProperties() {		
		try {	
			
			String path = getPath();				
			File inputFile = new File(path + "\\" + type + ".ini");
			if (inputFile.exists()){			
				props.load(new FileInputStream(inputFile));
				Debug.print(type + ".ini file read: " + inputFile.getAbsolutePath() +". Contents:");

				}
			
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public void saveProperties() {

		try{
			String path = getPath();

			Debug.print("Storing props: " + path);			  

			FileOutputStream out = new FileOutputStream(path + "\\" + type +".ini");


			props.store(out, "/* properties updated */");
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}

	private void setDefaults(Properties d) {

		if (type.equals("strangebrew")) {
			
			for (int i = 0; i<optionString.length; i++) {
				d.put(optionString[i][0], optionString[i][1]);
			}
			
/*			// calculations:
			d.put("optIBUCalcMethod", "Tinseth");
			d.put("optAlcCalcMethod", "Volume");
			d.put("optEvapCalcMethod", "Constant");
			d.put("optColourMethod", "SRM");
			d.put("optEfficiency", "75");
			d.put("optAttenuation", "75");
			d.put("optEvaporation", "1.5");
			d.put("optPelletHopsPct", "6");
			d.put("optDryHopTime", "0");
			d.put("optFWHTime", "1");
			d.put("optMashHopTime", "2");
			d.put("optHopsUtil", "4.15");
			d.put("optBoilTempF", "212");
			d.put("optColourEfficiency", "false");
			
			// recipe basics:
			d.put("optSizeU", "gallons US");
			d.put("optMaltU", "pounds");
			d.put("optHopsU", "ounces");
			d.put("optPostBoilVol", "5");
			d.put("optPreBoilVol", "6");
			d.put("optMash", "true");

			// carbonation:
			d.put("optPrimingSugar", "dextrose");
			d.put("optSugarU", "grams");
			d.put("optBottleU", "ml");
			d.put("optCarbTempU", "F");
			d.put("optBottleSize", "341");
			d.put("optBottleTemp", "68");
			d.put("optServeTemp", "45");
			d.put("optVolsCO2", "2.4");
			d.put("optKegged", "true");

			// water:
			d.put("optSourceWater", "");
			d.put("optTargetWater", "");
			
			
			// labels:
			d.put("optBrewer", "Your Name");
			d.put("optStreet", "Your Street");
			d.put("optCity", "Your City");
			d.put("optProv", "Your State/Prov");
			d.put("optCode", "Your zip/postal code");
			d.put("optPhone", "Your Phone");
			d.put("optClub", "Your Club");
			d.put("optCountry", "Your Country");
			d.put("optEmail", "Your Email");
			
			// appearance:
			d.put("optMaltSortOrder", "By Name");
			d.put("optFirstScreen", "");
			
			// time:
			d.put("optBrewDayStart", "10:00");
			d.put("optPrepTime", "31");
			d.put("optSpargeTime", "60");
			d.put("optGetToBoilTime", "45");
			d.put("optChillTime", "45");
			d.put("optCleanTime", "120");
			d.put("optBoilTime", "60");

			// water use:
			d.put("optKettleLoss", "1");
			d.put("optTrubLoss", "1");
			d.put("optMiscLoss", "1");
			
			// cost:
			d.put("optMiscCost", "5.0");
			
			
			*/
			
		} 
		
		else if (type.equals("mash")){
			d.put("optMashVolU", "gallons US");
			d.put("optMashTempU", "F");
			d.put("optMashRatioU", "qt/lb");
			d.put("optMashRatio", "1.25");
			d.put("optGrainTemp", "68");
			d.put("optTunLossF", "3");
			d.put("optBoilTempF", "212");
		}

	}
	
	// get methods:
	public String getProperty(String key){
			return props.getProperty(key);
	}
	
	public double getDProperty(String key){
		return Double.parseDouble(props.getProperty(key));
	}
			
	public int getIProperty(String key){
		return Integer.parseInt(props.getProperty(key));
	}
	
	// set methods:	
	public void setProperty(String key, String value){
		props.setProperty(key, value);
	}
	
	public void setDProperty(String key, double value){
		props.setProperty(key, String.valueOf(value));
	}
	
	public void setIProperty(String key, int value){
		props.setProperty(key, String.valueOf(value));
	}

}
