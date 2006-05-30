package ca.strangebrew;

/**
 * $Id: Options.java,v 1.10 2006/05/30 17:08:06 andrew_avis Exp $
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
			{"optHopForm", "Leaf"},
			
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
			{"optRed", "8"},
			{"optGreen", "30"},
			{"optBlue", "20"},
			{"optAlpha", "255"},
			{"optRGBMethod", "1"},
			
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
			
			// application window:
			{"winX", "0"},
			{"winY", "0"},
			{"winWidth", "600"},
			{"winHeight", "650"},
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
	
	private String getSep(){
		String slash = System.getProperty("file.separator");
		return slash;
	}
	
	private String getPath(){		
		String path = "";		
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
			File inputFile = new File(path + getSep() + type + ".ini");
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
			
			
		} 
		
		else if (type.equals("mash")){
			d.put("optMashVolU", "gallons US");
			d.put("optMashTempU", "F");
			d.put("optMashRatioU", "qt/lb");
			d.put("optMashRatio", "1.25");
			d.put("optGrainTemp", "68");
			d.put("optTunLossF", "3");
			d.put("optBoilTempF", "212");

			// default ranges for mash steps - indicates the bottom of the range
			d.put("optAcidTmpF", "85");
			d.put("optGlucanTmpF", "95");
			d.put("optProteinTmpF", "113");
			d.put("optBetaTmpF", "131");
			d.put("optAlphaTmpF", "151");
			d.put("optMashoutTmpF", "161");
			d.put("optSpargeTmpF", "170");

		}

	}
	
	// get methods:
	public String getProperty(String key){
			return props.getProperty(key);
	}
	
	public double getDProperty(String key){
		return Double.parseDouble(props.getProperty(key));
	}
	
	public float getFProperty(String key){
		return Float.parseFloat(props.getProperty(key));
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
