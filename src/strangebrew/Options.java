/*
 * $Id $
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class Options {
	//	declare global variables, set hard defaults		
	final boolean DEBUG = true;
	private Properties props;
	private String type;
	
	// default constructor, create options for a recipe
	public Options(){
		type = "strangebrew";
		Properties d = new Properties();
		setDefaults(d);
		props = new Properties(d);
		loadProperties();
	}
	
	// constructor for other option types
	public Options(String t){
		type = t;
		Properties d = new Properties();
		setDefaults(d);
		props = new Properties(d);
		loadProperties();
	}
	
	private void loadProperties() {		
		try {	
			
			String path =
				getClass().getProtectionDomain().getCodeSource()
					.getLocation().toString().substring(6);
			File inputFile = new File(path + "\\" + type + ".ini");
			if (inputFile.exists()){			
				props.load(new FileInputStream(inputFile));
				if (DEBUG) {
					System.out.println(type + ".ini file read. Contents: \n");
					props.list(System.out);
				}
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
		  FileOutputStream out = new FileOutputStream(path + "\\" + type +".ini");
		  props.store(out, "/* properties updated */");
		  }
		catch (Exception e) {
		  System.out.println(e);
		  }
	}

	private void setDefaults(Properties d) {

		if (type.equals("strangebrew")) {
			
			// calculations:
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
			
		}
		
		else if (type.equals("mash")){
			d.put("optMashVolU", "gallons US");
			d.put("optMashTempU", "F");
			d.put("optMashRatioU", "qt/lb");
			d.put("optMashRatio", "1.25");
			d.put("optGrainTemp", "68");
			d.put("optTunLossF", "3");
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
