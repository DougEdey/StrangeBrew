/*
 * $Id: PromashImport.java,v 1.3 2006/05/01 20:02:06 andrew_avis Exp $ Created on Jun 15, 2005
 * by aavis
 * A Promash .rec file importer, based on specs provided by Don Kelly
 */
package ca.strangebrew;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author aavis
 *
 */
public class PromashImport {

	private Recipe myRecipe;

	public static void main(String[] args) throws Exception {
		
	}

	public Recipe readRecipe(File input) {
		myRecipe = new Recipe();

		// File f = new File(path);
		FileInputStream in = null;
		try {
			in = new FileInputStream(input);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String s = "";		
		long l, l2;
		float fl;
		int x;

		try {
			// read in the name - 82 bytes
			s = readString(in, 82);
			Debug.print("Recipe name: " + s);
			myRecipe.setName(s);

			// get the various sizes
			long hopCount = readLong(in);
			Debug.print("Hop count: " + hopCount);
			long maltCount = readLong(in);
			Debug.print("Malt count: " + maltCount);
			long extraCount = readLong(in);
			Debug.print("Extra count: " + extraCount);
			fl = readFloat(in);
			in.skip(3);
			Debug.print("Batch size: " + fl);			
			fl = readFloat(in);
			Debug.print("Wort size: " + fl);
			myRecipe.setVolUnits("gal");
			myRecipe.setPostBoil(fl);
			in.skip(8);
			fl = readFloat(in);
			Debug.print("%effic: " + fl*100);
			myRecipe.setEfficiency(fl*100);
			x = readInt(in);
			Debug.print("Boil time: " + x);
			myRecipe.setBoilMinutes(x);
			in.skip(8);
			
			// Style
			s = readString(in, 55);
			s = readString(in, 55);
			Debug.print("Style: " + s );
			myRecipe.setStyle(s.trim());
			
			
			
			// let's skip to start of hops
			
			in.skip(921);

			// lets try to read the hops : 635 bytes
			for (int i = 0; i < hopCount; i++) {
				Hop h = new Hop();
				s = readString(in, 55);
				Debug.print("");
				Debug.print("Hop name: " + s);
				h.setName(s);

				fl = readFloat(in);
				Debug.print("Alpha: " + fl);
				h.setAlpha(fl);
								
				in.skip(22);
				x = readInt(in);
				Debug.print("Form: " + x);
				in.skip(4);
				
				String descr = "";
				s = readString(in, 155);
				Debug.print("Descr: " + s);
				descr = "Description: " + s.trim() + "\n";				
				s = readString(in, 55);
				Debug.print("Origin: " + s);
				descr += "Origin: " + s.trim() + "\n";	
				s = readString(in, 155);
				Debug.print("Use: " + s);
				descr += "Use: " + s.trim() + "\n";	
				s = readString(in, 165);
				Debug.print("Substitutes: " + s);
				descr += "Substitutes: " + s.trim() + "\n";	
				h.setDescription(descr);
				
				in.skip(9);				

				fl = readFloat(in);
				Debug.print("Amount: " + fl);
				h.setAmount(fl);
				h.setUnits("oz");
				
				x = readInt(in);
				Debug.print("Min:" + x);
				h.setMinutes(x);
				myRecipe.addHop(h);			
				
				in.skip(5);

			}

			// lets try to read the malts
			for (int i = 0; i < maltCount; i++) {
				Fermentable m = new Fermentable();
				s = readString(in, 55);
				Debug.print("");
				Debug.print("Malt name: " + s);
				m.setName(s.trim());
				s = readString(in, 55);
				Debug.print("Mfg: " + s);
				s = readString(in, 55);
				Debug.print("Origin: " + s);
				x = readInt(in);
				String type = "";
				switch (x) {
					case 1 :
						type = "grain";
						break;
					case 2 :
						type = "extract";
						m.setMashed(false);
						break;
					case 3 :
						type = "sugar";
						m.setMashed(false);
						break;
					case 4 :
						type = "other";
				}
				Debug.print("Type: " + type);

				String type2 = "";
				x = readInt(in);
				switch (x) {
					case 0 : {
						if (type.equals("extract"))
							type2 = "LME";
						else if (type.equals("grain") || type.equals("sugar"))
							type2 = "Mustmash = no";
							m.setMashed(false);
						break;

					}
					case 1 : {
						if (type.equals("extract"))
							type2 = "DME";
						else if (type.equals("grain") || type.equals("sugar"))
							type2 = "Mustmash = yes";
							m.setMashed(true);
						break;
					}
				}
				Debug.print("Type2: " + type2);

				fl = readFloat(in);
				Debug.print("pppg: " + fl);
				m.setPppg(fl);
				fl = readFloat(in);
				Debug.print("srm: " + fl);
				m.setLov(fl);

				in.skip(20);
				String descr = "";
				s = readString(in, 155);
				Debug.print("uses: " + s);
				descr = "Uses: " + s + "\n";
				s = readString(in, 159);
				Debug.print("comments: " + s);
				descr += "Comments: " + s + "\n";
				m.setDescription(descr);

				in.skip(12);
				fl = readFloat(in);
				Debug.print("amount: " + fl);
				m.setAmount(fl);
				m.setUnits("lb");
				
				myRecipe.addMalt(m);

				in.skip(4);
			}

			for (int i = 0; i < extraCount; i++) {
				s = readString(in, 155);
				Debug.print("Extra: " + s);

				String type = "";
				x = readInt(in);
				switch (x) {
					case 0 :
						type = "spice";
						break;
					case 1 :
						type = "fruit";
						break;
					case 2 :
						type = "coffee";
						break;
					case 3 :
						type = "other";
						break;
					case 4 :
						type = "fining";
						break;
					case 5 :
						type = "herb";
						break;
				}
				Debug.print("Type: " + type);

				in.skip(433);
			}

			// yeast: 473
			Yeast y = new Yeast();
			
			String descr = "";
			s = readString(in, 55);
			Debug.print("Yeast: " + s);
			descr = s.trim();			
			s = readString(in, 55);
			Debug.print("Mfg: " + s);
			descr = s.trim() + " " + descr;						
			String s2 = readString(in, 25);
			Debug.print("Num: " + s2);
			descr = s.trim() + " " + s2.trim() + " " + descr;
			y.setName(descr);
			in.skip(2);
			descr = "";
			s = readString(in, 155);
			Debug.print("Flavour: " + s);
			descr = "Flavour: " + s.trim() + "\n";
			s = readString(in, 159);
			Debug.print("Comment: " + s);
			descr += "Comments: " + s.trim() + "\n";
			y.setDescription(descr);
			
			myRecipe.setYeast(y);
			
			in.skip(22);

			// skip water
			in.skip(106);

			// skip ?
			in.skip(113);

			// regular mash			
			in.skip(9);
			
			for (int j=0; j<5; j++){
				l = readLong(in);
				Debug.print(j + " rest Temp: " + l);
				l2 = readLong(in);
				Debug.print(j + "rest Min: " + l2);
				String stepType="alpha";
				switch (j){
					case 0 : stepType = "acid";
					break;
					case 1: stepType = "protein";
					break;
					case 2: stepType = "beta";
					break;
					case 3: stepType = "alpha";
					break;
					case 4: stepType = "mashout";
					break;					
				}
				if (l > 0){
					myRecipe.mash.addStep(stepType, Double.valueOf(l).doubleValue(), 
							Double.valueOf(l).doubleValue(), "f", "infusion",  Double.valueOf(l2).intValue(), 0);					
				}
			}		

			
			l = readLong(in);
			Debug.print("Sparge rest Temp: " + l);
			l2 = readLong(in);
			Debug.print("Sparge rest Min: " + l2);

			// TODO: set sparge time & temp
			
			in.skip(5);

			// notes:
			s = readString(in, 4028);
			Debug.print("Notes: " + s);
			s = readString(in, 4283);
			Debug.print("Awards: " + s);

			// skip other stuff
			in.skip(175);

			// Custom Mash
			// in.skip(62);
			long mashCount = readLong(in);
			Debug.print("Mash steps: " + mashCount);
			in.skip(11);
			for (int i = 0; i < mashCount; i++) {
				s = readString(in, 253);
				Debug.print("step: " + s);
				l = readLong(in);
				Debug.print("start: " + l);
				l = readLong(in);
				Debug.print("end: " + l);
				l = readLong(in);
				Debug.print("infuse temp: " + l);
				l = readLong(in);
				Debug.print("min: " + l);
				l = readLong(in);
				Debug.print("stop time: " + l);

				in.skip(19);
			}
		} catch (Exception e){
			e.printStackTrace();
		}

		try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return myRecipe;
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

	static public float makeFloat(byte[] b) {
		int i = (b[0] & 0xff) + ((b[1] & 0xff) << 8) + ((b[2] & 0xff) << 16) + (b[3] << 24);
		return Float.intBitsToFloat(i);
	}

	public static String unsignedIntToString(byte[] b) {
		String s = "";
		int i = 0;
		while (i < b.length && b[i] != 0x00) {
			s = s + new Character((char) (b[i] & 0xff));
			i++;
		}
		return s;
	}
	/**
	 * Converts a 4 byte array of unsigned bytes to an long
	 * @param b an array of 4 unsigned bytes
	 * @return a long representing the unsigned int
	 */
	public static final long unsignedIntToLong(byte[] b) {
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
	public static final int unsignedShortToInt(byte[] b) {
		int i = 0;
		i |= b[0] & 0xFF;
		i <<= 8;
		i |= b[1] & 0xFF;
		return i;
	}
}
