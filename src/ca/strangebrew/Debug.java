/*
 * Created on Jun 15, 2005
 * by aavis
 *
 */
package ca.strangebrew;

/**
 * @author aavis
 *
 */
public class Debug {
	// private static final boolean DEBUG = true;
	public static boolean DEBUG = false;
	
	public static void set(boolean s){
		DEBUG = s;
		Debug.print("Debugging is on.\n");
	}
	
	
	public static void print(Object msg){
		if (DEBUG){
			System.out.println(msg.toString());
		}
	}

}
