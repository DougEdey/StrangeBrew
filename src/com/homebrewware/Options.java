/**
 *    Filename: Options.java
 *     Version: 0.9.0
 * Description: Options
 *     License: GPLv2
 *        Date: 2017-01-14
 *
 * This is the class that holds the program options, and
 * knows how to save and read them.
 * Theoretically you could have options apply to each individual
 * recipe, which SBWin only half implements.  I think it's a good
 * idea, and would make every recipe tweakable.
 *
 * Now we create defaults, and we can load a file.
 *
 * Copyright (c) 2004 Drew Avis
 * @author aavis
 *
 * Copyright (c) 2017 Dallas Fletchall
 * @author Dallas Fletchall
 */

/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as
 * published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package com.homebrewware;

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
    //  declare global variables, set hard defaults
    private Properties props;

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
            Properties d = new Properties();
            instance.setDefaults(d);
            instance.props = new Properties(d);
            instance.loadProperties();
        }

        return instance;
    }

    private void loadProperties() {

        String propFname = Product.getInstance().getName() + ".ini";
        String fname = Product.getInstance().getAppPath(Product.Path.ROOT, propFname);
        try {
            props.load(new FileInputStream(new File(fname)));
            Debug.print(propFname + "file read: " + fname + ". Contents:");
            Debug.print(props.toString());
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public void saveProperties() {

        String propFname = Product.getInstance().getName() + ".ini";
        String fname = Product.getInstance().getAppPath(Product.Path.ROOT, propFname);
        try{
            Debug.print("Storing props: " + fname);
            FileOutputStream out = new FileOutputStream(fname);
            Debug.print(props.toString());

            props.store(out, "/* properties updated */");
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    private void setDefaults(Properties d) {

        for (int i = 0; i<optionString.length; i++) {
            d.put(optionString[i][0], optionString[i][1]);
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
            System.out.println("Langauge: " + lang);
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
