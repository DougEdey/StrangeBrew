/*
 * $Id: PromashImport.java,v 1.2 2005/06/16 16:07:23 andrew_avis Exp $ Created on Jun 15, 2005
 * by aavis
 * A Promash .rec file importer, based on specs provided by Don Kelly
 */
package strangebrew;

import java.io.File;
import java.io.FileInputStream;

import ca.strangebrew.Debug;


/**
 * @author aavis
 *
 */
public class PromashImport {
	

	public static void main(String[] args) throws Exception {
		
//		if (args.length != 1) {
//			System.err.println("Usage: filename");
//			System.exit(1);
//		}		
		String path = new String("C:\\QNX630\\workspace2\\StrangeBrew\\src\\strangebrew\\data\\");
		String angry = new String("Angry American (911) Ale.rec");
		String mule = new String("Mule Kick Bitter.rec");
		String filen = new String("Bass Ale.rec");
		File f = new File (path + angry);
		FileInputStream in = new FileInputStream(f); 
        		                
        String s = "";
        byte b[] = new byte[82];        
        
        // read in the name - 82 bytes
        s = readString(in,82);               
        Debug.print("Recipe name: " + s);  
        
        // get the various sizes
        long hopCount = readLong(in);        
        Debug.print("Hop count: " + hopCount);         
        long l = readLong(in);       
        Debug.print("Malt count: " + l);
        l = readLong(in);    
        Debug.print("Extra count: " + l);
        
        // let's skip to start of hops
        // we're at 94, want to go to 1158
        in.skip(1063);
        
        // lets try to read the hops
        for (int i=0; i<hopCount; i++){
        	s = readString(in, 55);
        	Debug.print("");
            Debug.print("Hop name: " + s);
            
            float fl = readFloat(in);
            Debug.print("Alpha: " + fl);
            fl = readFloat(in);
            Debug.print("Beta: " + fl);
            int k = readInt(in);
            Debug.print("IsNobel: " + k);
            fl = readFloat(in);
            Debug.print("Cohomulone: " + fl);
            fl = readFloat(in);
            Debug.print("Myrcene: " + fl);
            fl = readFloat(in);
            Debug.print("Humulene: " + fl);
            fl = readFloat(in);
            Debug.print("Carophyllene: " + fl);
                    
            
            in.skip(6);
            
            
            
            s = readString(in, 155);
            Debug.print("Descr: " + s);
            s = readString(in, 55);
            Debug.print("Origin: " + s);
            s = readString(in, 155);
            Debug.print("Use: " + s);
            s = readString(in, 165);
            Debug.print("Substitutes: " + s);
            fl = readFloat(in);
            Debug.print("Always 20: " + fl);
            fl = readFloat(in);
            Debug.print("Next fl, probably alpha again: " + fl);
            in.skip(11);           
        	
        }       

        
        in.close();
        
	}
	
	public static String readString(FileInputStream fs, int b) throws Exception {
		String s = null;
		byte buffer[] = new byte[b];
		fs.read(buffer, 0, b);
        s = unsignedIntToString(buffer);
		
		return s;
	}
	
	public static int readInt(FileInputStream fs) throws Exception {
		int i = fs.read();
        	
		return i;
	}
	
	public static long readLong(FileInputStream fs) throws Exception {
		long l = 0;
		byte buffer[] = new byte[4];
		fs.read(buffer, 0, 4);
        l = unsignedIntToLong(buffer);
		
		return l;
	}
	
	public static float readFloat(FileInputStream fs) throws Exception {
		float f = 0;
		byte buffer[] = new byte[4];
		fs.read(buffer, 0, 4);
        f = makeFloat(buffer);
		
		return f;
	}
	
	static public float makeFloat( byte[] b )
	 {
	      int i = (b[0]&0xff) + ((b[1]&0xff) << 8) + ((b[2]&0xff) << 16) + (b[3] << 24);
	      return Float.intBitsToFloat(i);
	 } 
	
	public static String unsignedIntToString (byte[] b){
		String s = "";
		int i = 0;
		while (i<b.length && b[i]!=0x00){
			s = s +  new Character((char)(b[i] & 0xff));
			i++;
		}
		return s;
	}
	/**
	 * Converts a 4 byte array of unsigned bytes to an long
	 * @param b an array of 4 unsigned bytes
	 * @return a long representing the unsigned int
	 */
	public static final long unsignedIntToLong(byte[] b) 
	{
	    long l = 0;
	    l |= b[0] & 0xFF;
	    l <<= 8;
	    l |= b[1] & 0xFF;
	    l <<= 8;
	    l |= b[2] & 0xFF;
	    l <<= 8;
	    l |= b[3] & 0xFF;
	    return l;
	}

	    
	/**
	 * Converts a two byte array to an integer
	 * @param b a byte array of length 2
	 * @return an int representing the unsigned short
	 */
	public static final int unsignedShortToInt(byte[] b) 
	{
	    int i = 0;
	    i |= b[0] & 0xFF;
	    i <<= 8;
	    i |= b[1] & 0xFF;
	    return i;
	}
}
