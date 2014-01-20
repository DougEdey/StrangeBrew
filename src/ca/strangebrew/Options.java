package ca.strangebrew;

/**
 * $Id: Options.java,v 1.32 2008/07/07 17:51:14 andrew_avis Exp $
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
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.Properties;


public class Options {
	// Singelton Instance
	private static Options instance = null;
	//	declare global variables, set hard defaults			
	private Properties props;
	private String type;
	
	private String optionString[][] = { 
			// calculations:
			{"optIBUCalcMethod", BrewCalcs.TINSETH},
			// TODO
			{"optAlcCalcMethod", BrewCalcs.ALC_BY_VOLUME},
			{"optEvapCalcMethod", "Constant"},
			{"optColourMethod", BrewCalcs.SRM},
			{"optEfficiency", "75"},
			{"optAttenuation", "75"},
			{"optEvaporation", "1.5"},
			{"optPelletHopsPct", "6"},
			{"optDryHopTime", "0"},
			{"optFWHTime", "1"},
			{"optMashHopTime", "2"},
			{"optHopsUtil", "4.15"},
			{"optColourEfficiency", "false"},
			{"optHopForm", Hop.LEAF},
			
			// recipe basics:
			{"optSizeU", Quantity.GALLONS_US},
			{"optMaltU", Quantity.POUNDS},
			{"optHopsU", Quantity.OUNCES},
			{"optPostBoilVol", "5"},
			{"optPreBoilVol", "6"},
			{"optMash", "true"},
			{"optHopsType", Hop.LEAF},
			{"optWaterProfile", "Distilled/RO"},

			// carbonation:
			{"optPrimingSugar", "Corn Sugar"},
			{"optSugarU", Quantity.GRAMS},
			{"optBottleU", Quantity.FL_OUNCES},
			{"optCarbTempU", "F"},
			{"optBottleSize", "12"},
			{"optBottleTemp", "68"},
			{"optServTemp", "45"},
			{"optVolsCO2", "2.4"},
			{"optKegged", "true"},
			{"optTubingID", "3/16"},
			{"optHeightAboveKeg", "1"},

			// water:
			{"optSourceWater", ""},
			{"optTargetWater", ""},
			
			// Fermentation
			{"optFermentType", FermentStep.PRIMARY},
			{"optFermentTime", "7"},
			{"optFermentTemp", "68.0"},
			{"optFermentTempU", "F"},
			
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
			{"optLocaleLang", ""},
			{"optLocaleCountry", ""},
			{"optLocaleVariant", ""},		
			
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
			{"optKettleLoss", "0"},
			{"optTrubLoss", "0"},
			{"optMiscLoss", "0"},
			
			// cost:
			{"optMiscCost", "5.0"},
			
			// mash:
			{"optMashVolU", Quantity.GALLONS_US},
			{"optMashTempU", "F"},
			{"optMashRatioU", Mash.QT_PER_LB},
			{"optMashRatio", "1.25"},
			{"optGrainTemp", "68"},
			{"optTunLossF", "3"},
			{"optDeadSpace", "0"},
			{"optBoilTempF", "212"},
			{"optThickDecoctRatio", "0.6"},
			{"optThinDecoctRatio", "0.9"},

			// default ranges for mash steps - indicates the bottom of the range
			{"optAcidTmpF", "85"},
			{"optGlucanTmpF", "95"},
			{"optProteinTmpF", "113"},
			{"optBetaTmpF", "131"},
			{"optAlphaTmpF", "151"},
			{"optMashoutTmpF", "161"},
			{"optSpargeTmpF", "170"},			
			{"optCerealMashTmpF", "155"},
			
			// style DB
			{"optStyleYear", "2004"},
			
			// application window:
			{"winX", "0"},
			{"winY", "0"},
			{"winWidth", "600"},
			{"winHeight", "650"},
			{"maltTable0", "10"},
			{"maltTable1", "10"},
			{"maltTable2", "200"},
			{"hopsTable0", "200"},
			
			{"cloudURL", "strangebrewcloud.appspot.com"}
	};
	
	// default constructor, create options for a recipe
	private Options() {}
	
	public static Options getInstance() {
		if (instance == null)
		{
			instance = new Options();

			instance.type = "strangebrew";
			Properties d = new Properties();
			instance.setDefaults(d);
			instance.props = new Properties(d);		
			instance.loadProperties();
		}
		
		return instance;			
	}
	
	// constructor for other option types
//	private Options(String t) {
//		if (instance == null)
//			instance = new
//		type = t;
//		Properties d = new Properties();
//		setDefaults(d);
//		props = new Properties(d);		
//		loadProperties();
//	}
	

	private void loadProperties() {		
		try {	
			
			String path = SBStringUtils.getAppPath("ini");				
			File inputFile = new File(path + type + ".ini");
			if (inputFile.exists()){			
				props.load(new FileInputStream(inputFile));
				Debug.print(type + ".ini file read: " + inputFile.getAbsolutePath() +". Contents:");
				Debug.print(props.toString());
			}
			
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public void saveProperties() {

		try{
			String path = SBStringUtils.getAppPath("ini");
			Debug.print("Storing props: " + path);
			FileOutputStream out = new FileOutputStream(path + type +".ini");
			Debug.print(props.toString());

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

	}
	
	// get methods:
	public String getProperty(String key){
			return props.getProperty(key);
	}
	
	public double getDProperty(String key){
		try {
			NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
			Number number = format.parse(props.getProperty(key));
		    
			return number.doubleValue();
		} catch (NumberFormatException m) {
			Debug.print("Could not read a double value from property file for key: "+key + "." +
					" Value read in: " + props.getProperty(key));
		} catch (ParseException e) {
			Debug.print("Could not read a double value from property file for key: "+key + "." +
					" Value read in: " + props.getProperty(key));
		}
		return 0.0;
	}
	
	public float getFProperty(String key){
		try {
			NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
			Number number = format.parse(props.getProperty(key));
			return number.floatValue();
		} catch (NumberFormatException m) {
			Debug.print("Could not read a float value from property file for key: "+key + "." +
					" Value read in: " + props.getProperty(key));
		} catch (ParseException e) {
			Debug.print("Could not read a float value from property file for key: "+key + "." +
					" Value read in: " + props.getProperty(key));
		}
		return (float) 0.0;
	}
			
	public int getIProperty(String key){	
		// does this property exist, either saved or in the defaults?
		if (props.getProperty(key) != null) {
			try {
				NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
				Number number = format.parse(props.getProperty(key));
				return number.intValue();
			} catch (NumberFormatException m) {
				Debug.print( key + " property is corrupted read from file: " + props.getProperty(key));
			} catch (ParseException e) {
				Debug.print( key + " property is corrupted read from file: " + props.getProperty(key));
			}
		} else {
			Debug.print("Could not find property " + key);
		}
		
		
		return 0;

	}
	
	public boolean getBProperty(String key) {
		String s = props.getProperty(key);
		if (s != null && s.equalsIgnoreCase("true")) {
			return true;
		}
		return false;
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
	
	public void setBProperty(String key, boolean value) {
		if (value) {
			props.setProperty(key, "true");
		} else {
			props.setProperty(key, "false");
		}
	}
	
	public Locale getLocale() {
		Locale l = null;
		String lang = getProperty("optLocaleLang");
		
		if (lang == null || lang == "") {
			l = Locale.getDefault();
		} else {
			l =  new Locale(lang,
						getProperty("optLocaleCountry"),
						getProperty("optLocaleVariant"));			
		}
		
		return l;
	}

	public void setLocale(Locale l) {
		if (l == null || l == Locale.getDefault()) {
			props.setProperty("optLocaleLang", "");
			props.setProperty("optLocaleCountry", "");
			props.setProperty("optLocaleVariant", "");					
		} else {
			props.setProperty("optLocaleLang", l.getLanguage());
			props.setProperty("optLocaleCountry", l.getCountry());
			props.setProperty("opsLocaleVariant", l.getVariant());
		}
	}	
}
