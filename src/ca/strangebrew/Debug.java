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
	private static final boolean DEBUG = true;
	
	public static void print(Object msg){
		if (DEBUG){
			System.out.println(msg.toString());
		}
	}

}
