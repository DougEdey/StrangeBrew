/*
 * $Id: PromashImport.java,v 1.1 2006/04/07 13:59:14 andrew_avis Exp $ Created on Jun 15, 2005
 * by aavis
 * A Promash .rec file importer, based on specs provided by Don Kelly
 */
package ca.strangebrew;

import java.io.File;
import java.io.FileInputStream;



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
		String path2 = new String("C:\\Program Files\\ProMash\\recipes\\");
		String angry = new String("Angry American (911) Ale.rec");
		String mule = new String("Mule Kick Bitter.rec");
		String filen = new String("Company1_Red.rec");
		File f = new File (path2 + filen);
		FileInputStream in = new FileInputStream(f); 
        		                
        String s = "";
        byte b[] = new byte[1024];    
        long l;
        float fl;              
        
        // read in the name - 82 bytes
        s = readString(in,82);               
        Debug.print("Recipe name: " + s);  
        
        // get the various sizes
        long hopCount = readLong(in);        
        Debug.print("Hop count: " + hopCount);         
        l = readLong(in);       
        Debug.print("Malt count: " + l);
        l = readLong(in);    
        Debug.print("Extra count: " + l);
        fl = readFloat(in);
        in.skip(3);
        Debug.print("Batch size: " + fl);
        fl = readFloat(in);
        Debug.print("Wort size: " + fl);
        in.skip(8);
        fl = readFloat(in);
        Debug.print("%effic: " + fl);        
        Debug.print("Boil time: " + readInt(in));        
        
        // let's skip to start of hops
        // wwant to go to 1158
        in.skip(1039);
        
        // lets try to read the hops
        for (int i=0; i<hopCount; i++){
        	s = readString(in, 55);
        	Debug.print("");
            Debug.print("Hop name: " + s);
            
            fl = readFloat(in);
            Debug.print("Alpha: " + fl); 

            in.skip(27);
            
            s = readString(in, 155);
            Debug.print("Descr: " + s);
            s = readString(in, 55);
            Debug.print("Origin: " + s);
            s = readString(in, 155);
            Debug.print("Use: " + s);
            s = readString(in, 165);
            Debug.print("Substitutes: " + s);

            in.skip(9);
            
            fl = readFloat(in);
            Debug.print("Amount: " + fl);
            
            in.skip(6);           
        	
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
	
	public static int readShort(FileInputStream fs) throws Exception {
		int s = 0;
		byte buffer[] = new byte[2];
		fs.read(buffer, 0, 2);
        s = unsignedShortToInt(buffer);
		
		return s;
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
