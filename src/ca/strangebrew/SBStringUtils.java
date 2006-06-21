/*
 * Created on Jun 13, 2005
 * by aavis
 *
 */
package ca.strangebrew;

import java.io.File;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * @author aavis
 *
 */
public class SBStringUtils {

	/**
	 * Returns a multi-line tooltip by wrapping
	 * the String input in <html> and inserting breaks <br>.
	 * @param len	the maximum line-length.  The method will insert
	 * a break at the space closest to this number.
	 * @param input	the input String.
	 * @return the new multi-lined string.
	 */
	public static String multiLineToolTip(int len, String input) {
		String s = "";
		int length = len;
		if (input == null)
			return "";
		if (input.length() < length)
			return input;

		int i = 0;
		int lastSpace = 0;

		while (i + length < input.length()) {
			String temp = input.substring(i, i + length);
			lastSpace = temp.lastIndexOf(" ");
			s += temp.substring(0, lastSpace) + "<br>";
			i += lastSpace + 1;
		}
		s += input.substring(i, input.length());

		s = "<html>" + s + "</html>";

		return s;
	}

	/**
	 * Returns a string with all ocurrences of special characters 
	 * replaced by their corresponding xml entities.
	 * @param input	the string you want to replace all special characters
	 * in
	 * @return a string with valid xml entites
	 */
	public static String subEntities(String input) {

		String sub[][] = {{"&", "&amp;"}, {"<", "&lt;"}, {">", "&gt;"}, {"'", "&apos;"},
				{"\"", "&quot;"}};
		String output = input;
		int index = 0;
		if (input == null)
			return "";
		for (int i = 0; i < sub.length; i++) {
			while (output.indexOf(sub[i][0], index) != -1) {
				index = output.indexOf(sub[i][0], index);
				output = output.substring(0, index) + sub[i][1]
						+ output.substring(index + sub[i][0].length());
				index = index + sub[i][0].length();
			}
		}

		return output;
	}
	
	
	public static NumberFormat nf = NumberFormat.getNumberInstance();
	public static DecimalFormat df = (DecimalFormat)nf; 
	public static NumberFormat myNF = NumberFormat.getCurrencyInstance(); // Use the country currency
	public static DateFormat dateFormatShort = DateFormat.getDateInstance(DateFormat.SHORT);
	public static DateFormat dateFormatFull = DateFormat.getDateInstance(DateFormat.FULL);
	
	public static String format(double value, int decimal){
		
		String pattern = "0";
		switch (decimal){
			case 0: pattern = "0";
			break;
			case 1: pattern = "0.0";
			break;
			case 2: pattern = "0.00";
			break;
			case 3: pattern = "0.000";
			break;
			
		}
		df.applyPattern(pattern);
		return df.format(value);		
	}
	
	public static String xmlElement(String elem, String content, int i) {
		String s = "";
		for (int j = 0; j<i;j++)
			s += " ";		
		s += "<" + elem + ">" + content + "</" + elem + ">\n";		
		return s;
	}
	
	public static String getAppPath(String type){
		String appRoot = "";
		String path = "";
		String slash = System.getProperty("file.separator");
		try {
			appRoot = new File(".").getCanonicalPath();
		} catch (Exception e){
			e.printStackTrace();
		}
		if (type.equals("data"))
			path = appRoot + slash + "src" + slash + "ca" 
				+ slash + "strangebrew" + slash + "data";
		else if (type.equals("icons"))
			path = appRoot + slash + "src" + slash + "ca" 
			+ slash + "strangebrew" + slash + "icons";
		else if (type.equals("recipes"))
			path = appRoot + slash + "recipes";
		else if (type.equals("help"))
			path = "file://" + appRoot + slash + "help" + slash;
		else if (type.equals("ini"))
			path = appRoot + slash;
		else
			path = appRoot;
		
		return path;
	}
	
	
	
	
	
	
	

}
