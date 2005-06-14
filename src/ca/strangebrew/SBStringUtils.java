/*
 * Created on Jun 13, 2005
 * by aavis
 *
 */
package ca.strangebrew;

/**
 * @author aavis
 *
 */
public class SBStringUtils  {
	
	/**
	 * Returns a multi-line tooltip by wrapping
	 * the String input in <html> and inserting breaks <br>.
	 * @param len	the maximum line-length.  The method will insert
	 * a break at the space closest to this number.
	 * @param input	the input String.
	 * @return the new multi-lined string.
	 */
	public static String multiLineToolTip(int len, String input){
		String s = "";
		int length = len;
		if (input == null || input.length() < length)
			return "";
		
		int i = 0;
		int lastSpace = 0;
		
		while (i+length < input.length()){		
			String temp = input.substring(i, i+length);
			lastSpace = temp.lastIndexOf(" ");
			s += temp.substring(0,lastSpace) + "<br>";
			i+=lastSpace+1;
		}
		s += input.substring(i,input.length());

		s = "<html>"+s+"</html>";
		
		return s;
	}
	
	/**
	 * Returns a string with all ocurrences of special characters 
	 * replaced by their corresponding xml entities.
	 * @param input	the string you want to replace all special characters
	 * in
	 * @return a string with valid xml entites
	 */
	public static String subEntities(String input){
		
	String sub [][] = { {"&", "&amp;"},
						{"<", "&lt;"},
						{">", "&gt;"},
						{"'", "&apos;"},
						{"\"", "&quot;"}
						};
	String output = input;
	int index = 0;		
	if (input == null)
		return "";
	for (int i = 0; i<sub.length; i++){
		while (output.indexOf(sub[i][0],index) != -1){
			index = output.indexOf(sub[i][0],index);
			output = output.substring(0, index) + sub[i][1] +
	           output.substring(index+sub[i][0].length());		
			index=index+sub[i][0].length();			
		}
	}
	
	return output;
}

}
