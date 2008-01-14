package ca.strangebrew;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;

import com.mindprod.csv.CSVReader;
import com.mindprod.csv.CSVWriter;

/**
 * $Id: Database.java,v 1.22 2008/01/14 19:18:47 jimcdiver Exp $
 * @author aavis
 *
 * This is the Database class that reads in the .csv files and 
 * creates ArrayLists of ingredient objects.  It uses the 
 * csv reader from the com.mindprod.csv package.
 * 
 * TODO: create methods to add and delete items, detect if the
 * list had changed, and save the DB to csv.
 */
public class Database {

	// this class is just some lists of various ingredients
	// read from the csv files.
	// I suspect that binary files will be faster, and
	// we might want to move that way in the future.
	private static Database instance = null;
	
	final public ArrayList<Fermentable> fermDB = new ArrayList<Fermentable>();
	final public ArrayList<Hop> hopsDB = new ArrayList<Hop>();
	final public ArrayList<Yeast> yeastDB = new ArrayList<Yeast>();
	public ArrayList<Style> styleDB = new ArrayList<Style>();
	final public ArrayList<Misc> miscDB = new ArrayList<Misc>();
	final public ArrayList<PrimeSugar> primeSugarDB = new ArrayList<PrimeSugar>();
	final public ArrayList<WaterProfile> waterDB = new ArrayList<WaterProfile>();
	public String dbPath;

	// This is now a singleton
	private Database() {}
	
	public static Database getInstance() {
		if (instance == null) {
			instance = new Database();
		}
		
		return instance;
	}
	
	public int inDB(Object o){		
		if (o instanceof Yeast) {
			for (int i=0; i<yeastDB.size(); i++){
				Yeast y1 = (Yeast)o;
				Yeast y2 = (Yeast)yeastDB.get(i);
				if (y1.equals(y2)) {					
					return i;			
				}
			}			
		} else if (o instanceof Fermentable) {
			for (int i=0; i<fermDB.size(); i++){
				Fermentable y1 = (Fermentable)o;
				Fermentable y2 = (Fermentable)fermDB.get(i);
				if (y1.equals(y2)) {					
					return i;			
				}
			}
		} else if (o instanceof Hop) {
			for (int i=0; i<hopsDB.size(); i++){
				Hop y1 = (Hop)o;
				Hop y2 = (Hop)hopsDB.get(i);
				if (y1.equals(y2)) {					
					return i;			
				}
			}
		} else if (o instanceof Misc) {
			for (int i=0; i<miscDB.size(); i++){
				Misc y1 = (Misc)o;
				Misc y2 = (Misc)miscDB.get(i);
				if (y1.equals(y2)) {					
					return i;			
				}
			}
		} else if (o instanceof Style) {
			for (int i=0; i<styleDB.size(); i++){
				Style y1 = (Style)o;
				Style y2 = (Style)styleDB.get(i);
				if (y1.equals(y2)) {					
					return i;			
				}
			}
		} else if (o instanceof PrimeSugar) {
			for (int i=0; i<primeSugarDB.size(); i++){
				PrimeSugar y1 = (PrimeSugar)o;
				PrimeSugar y2 = (PrimeSugar)primeSugarDB.get(i);
				if (y1.equals(y2)) {					
					return i;			
				}
			}
		} else if (o instanceof WaterProfile) {
			for (int i=0; i<waterDB.size(); i++){
				WaterProfile y1 = (WaterProfile)o;
				WaterProfile y2 = (WaterProfile)waterDB.get(i);
				if (y1.equals(y2)) {					
					return i;			
				}
			}
		}
		
		return -1;
	}
	
	public void readDB(String path){
		dbPath = path;
		readFermentables(dbPath);
		readPrimeSugar(dbPath);
		readHops(dbPath);
		readYeast(dbPath);
		readMisc(dbPath);
		importStyles(dbPath);
		readWater(dbPath);
		
		// sort
		Collections.sort(styleDB);
		Collections.sort(fermDB);
		Collections.sort(hopsDB);
		Collections.sort(yeastDB);
		Collections.sort(waterDB);
	}
	
	public void readFermentables(String path) {
		// read the fermentables from the csv file
		try {
			File maltFile = new File(path, "malts.csv");
			CSVReader reader = new CSVReader(new FileReader(
					maltFile), ',', '\"', true, false);

			try {
				// get the first line and set up the index:
				String[] fields = reader.getAllFieldsInLine();
				int nameIdx = getIndex(fields, "Name");
				int pppgIdx = getIndex(fields, "Yield");
				int lovIdx = getIndex(fields, "Lov");
				int costIdx = getIndex(fields, "Cost");
				int stockIdx = getIndex(fields, "Stock");
				int unitsIdx = getIndex(fields, "Units");
				int mashIdx = getIndex(fields, "Mash");
				int descrIdx = getIndex(fields, "Descr");
				int steepIdx = getIndex(fields, "Steep");
				int modIdx = getIndex(fields, "Modified");

				while (true) {
					Fermentable f = new Fermentable();
					fields = reader.getAllFieldsInLine();
					f.setName(fields[nameIdx]);
					f.setPppg(Double.parseDouble(fields[pppgIdx]));
					f.setLov(Double.parseDouble(fields[lovIdx]));
					f.setCost(Double.parseDouble(fields[costIdx]));
					f.setUnits(fields[unitsIdx]);
					f.setMashed(Boolean.valueOf(fields[mashIdx]).booleanValue());
					f.setSteep(Boolean.valueOf(fields[steepIdx]).booleanValue());
					if (!fields[stockIdx].equals(""))
						f.setAmount(Double.parseDouble(fields[stockIdx]));
					else
						f.setAmount(0);
					f.setDescription(fields[descrIdx]);
					f.setModified(Boolean.valueOf(fields[modIdx]).booleanValue());
					fermDB.add(f);		
				}
			} catch (EOFException e) {
			}
			reader.close();	
			


		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

	}
	
	public void writeFermentables(ArrayList<Ingredient> newIngr, boolean[] add) {		
		
		File maltsFile = new File(dbPath, "malts.csv");
		backupFile(maltsFile);

		try {
			CSVWriter writer = new CSVWriter(new FileWriter(maltsFile));
			writer.put("Item");
			writer.put("Name");
			writer.put("Yield");
			writer.put("Lov");
			writer.put("Cost");
			writer.put("Stock");
			writer.put("Units");
			writer.put("Mash");
			writer.put("Descr");			
			writer.put("Steep");
			writer.put("Modified");
			writer.nl();
			int i = 0, j = 0, k = 0;
			while (i < fermDB.size() || j < newIngr.size()) {
				Fermentable f = null;				
				if (i < fermDB.size()) {
					f = (Fermentable) fermDB.get(i);
					i++;					
				} else if (j < newIngr.size()) {
					if (newIngr.get(j) instanceof Fermentable && add[j]){
						f = (Fermentable) newIngr.get(j);						
					}
					j++;
				}
				if (f != null) {
					k++;
					writer.put("" + (k));
					writer.put(f.getName());
					writer.put("" + f.getPppg());
					writer.put("" + f.getLov());
					writer.put("" + f.getCostPerU());
					writer.put("" + f.getStock());
					writer.put(f.getUnitsAbrv());
					writer.put("" + f.getMashed());
					writer.put(f.getDescription());
					writer.put("" + f.getSteep());
					writer.put("" + f.getModified());
					writer.nl();
				}

			}

			writer.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void readHops(String path) {
		// read the hops from the csv file
		try {
			File hopsFile = new File(path, "hops.csv");
			CSVReader reader = new CSVReader(new FileReader(
					hopsFile), ',', '\"', true, false);

			try {
				// get the first line and set up the index:
				String[] fields = reader.getAllFieldsInLine();
				int nameIdx = getIndex(fields, "Name");
				int alphaIdx = getIndex(fields, "Alpha");
				int costIdx = getIndex(fields, "Cost");
				int stockIdx = getIndex(fields, "Stock");
				int unitsIdx = getIndex(fields, "Units");
				int descrIdx = getIndex(fields, "Descr");
				int storeIdx = getIndex(fields, "Storage");
				int dateIdx = getIndex(fields, "Date");
				int modIdx = getIndex(fields, "Modified");

				while (true) {
					Hop h = new Hop();
					fields = reader.getAllFieldsInLine();
					h.setName(fields[nameIdx]);
					h.setAlpha(Double.parseDouble(fields[alphaIdx]));
					if (!fields[costIdx].equals(""))
						h.setCost(Double.parseDouble(fields[costIdx]));
					h.setUnits(fields[unitsIdx]);
					if (!fields[stockIdx].equals(""))
						h.setAmount(Double.parseDouble(fields[stockIdx]));
					h.setDescription(fields[descrIdx]);
					if (!fields[storeIdx].equals(""))
						h.setStorage(Double.parseDouble(fields[storeIdx]));
					h.setDate(fields[dateIdx]);
					h.setModified(Boolean.valueOf(fields[modIdx]).booleanValue());
					hopsDB.add(h);
				}
			} catch (EOFException e) {
			}
			reader.close();

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}


	}
	
	public void writeHops(ArrayList<Ingredient> newIngr, boolean[] add) {		
			
		if (!(newIngr.size() > 0))
			return;
		File hopsFile = new File(dbPath, "hops.csv");
		backupFile(hopsFile);

		try {
			CSVWriter writer = new CSVWriter(new FileWriter(hopsFile));
			writer.put("Item");
			writer.put("Name");
			writer.put("Alpha");
			writer.put("Cost");
			writer.put("Stock");
			writer.put("Units");
			writer.put("Descr");
			writer.put("Storage");
			writer.put("Date");
			writer.put("Modified");
			writer.nl();
			int i = 0, j = 0, k = 0;
			while (i < hopsDB.size() || j < newIngr.size()) {
				Hop h = null;				
				if (i < hopsDB.size()) {
					h = (Hop) hopsDB.get(i);
					i++;					
				} else if (j < newIngr.size()) {
					if (newIngr.get(j) instanceof Hop && add[j]){
						h = (Hop) newIngr.get(j);						
					}
					j++;
				}
				if (h != null) {
					k++;
					writer.put("" + (k));
					writer.put(h.getName());
					writer.put("" + h.getAlpha());
					writer.put("" + h.getCostPerU());
					writer.put("" + h.getStock());
					writer.put(h.getUnitsAbrv());
					writer.put(h.getDescription());
					writer.put("" + h.getStorage());
					writer.put("" + h.getDate());
					writer.put("" + h.getModified());
					writer.nl();
				}

			}

			writer.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	public void readYeast(String path) {
		// read the yeast from the csv file
		try {
			File yeastFile = new File(path, "yeast.csv");
			CSVReader reader = new CSVReader(new FileReader(
					yeastFile), ',', '\"', true, false);

			try {
				// get the first line and set up the index:
				String[] fields = reader.getAllFieldsInLine();

				int nameIdx = getIndex(fields, "Name");
				int costIdx = getIndex(fields, "Cost");
				int descrIdx = getIndex(fields, "Descr");
				int modIdx = getIndex(fields, "Modified");

				while (true) {
					Yeast y = new Yeast();
					fields = reader.getAllFieldsInLine();
					y.setName(fields[nameIdx]);

					if (!fields[costIdx].equals(""))
						y.setCost(Double.parseDouble(fields[costIdx]));
					y.setDescription(fields[descrIdx]);
					y.setModified(Boolean.valueOf(fields[modIdx]).booleanValue());
					yeastDB.add(y);
				}
			} catch (EOFException e) {
			}
			reader.close();
			

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}


	}
	
	public void writeYeast(ArrayList<Ingredient> newIngr, boolean[] add) {		
		
		if (!(newIngr.size() > 0))
			return;
		File yeastFile = new File(dbPath, "yeast.csv");
		backupFile(yeastFile);

		try {
			CSVWriter writer = new CSVWriter(new FileWriter(yeastFile));
			writer.put("Item");
			writer.put("Name");
			writer.put("Cost");
			writer.put("Descr");
			writer.put("Modified");
			writer.nl();
			int i = 0, j = 0, k = 0;
			while (i < yeastDB.size() || j < newIngr.size()) {
				Yeast y = null;				
				if (i < yeastDB.size()) {
					y = (Yeast) yeastDB.get(i);
					i++;					
				} else if (j < newIngr.size()) {
					if (newIngr.get(j) instanceof Yeast && add[j]){
						y = (Yeast) newIngr.get(j);						
					}
					j++;
				}
				if (y != null) {
					k++;
					writer.put("" + (k));
					writer.put(y.getName());
					writer.put("" + y.getCostPerU());
					writer.put(y.getDescription());
					writer.put("" + y.getModified());
					writer.nl();
				}

			}

			writer.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void readStyles(String path){
			// read the styles from the csv file
			try {
				File styleFile = new File(path, "bjcp_styles.csv");				
				CSVReader reader = new CSVReader(new FileReader(
						styleFile), ',', '\"', true, false);

				try {
					// get the first line and set up the index:
					String[] fields = reader.getAllFieldsInLine();

					int nameIdx = getIndex(fields, "Name");
					int catIdx = getIndex(fields, "Category");
					int oglowIdx = getIndex(fields, "OG_Low");
					int oghighIdx = getIndex(fields, "OG_High");
					int alclowIdx = getIndex(fields, "Alc_Low");
					int alchighIdx = getIndex(fields, "Alc_High");
					int ibulowIdx = getIndex(fields, "IBU_Low");
					int ibuhighIdx = getIndex(fields, "IBU_High");
					int lovlowIdx = getIndex(fields, "Lov_Low");
					int lovhighIdx = getIndex(fields, "Lov_High");
					int commexIdx = getIndex(fields, "Comm_Examples");
					int descrIdx = getIndex(fields, "Descr");

					while (true) {
						Style s = new Style();
						fields = reader.getAllFieldsInLine();
						s.setName(fields[nameIdx]);
						s.setCategory(fields[catIdx]);
						s.setOgLow(Double.parseDouble(fields[oglowIdx]));
						s.setOgHigh(Double.parseDouble(fields[oghighIdx]));
						s.setAlcLow(Double.parseDouble(fields[alclowIdx]));
						s.setAlcHigh(Double.parseDouble(fields[alchighIdx]));
						s.setIbuLow(Double.parseDouble(fields[ibulowIdx]));
						s.setIbuHigh(Double.parseDouble(fields[ibuhighIdx]));
						s.setSrmLow(Double.parseDouble(fields[lovlowIdx]));
						s.setSrmHigh(Double.parseDouble(fields[lovhighIdx]));
						s.setExamples(fields[commexIdx]);
						s.comments = fields[descrIdx];
						styleDB.add(s);
					}
				} catch (EOFException e) {
				}
				reader.close();
			

			} catch (IOException e) {
				e.printStackTrace();
				System.out.println(e.getMessage());
			}
			// sort:
			/*ObjComparator sc = new ObjComparator();
			Collections.sort(styleDB,  sc);*/
		
	}
	
	public void importStyles(String path){
		// import a yeast array from an xml file:
		File yeastFile = new File(path, "styleguide.xml");
		Debug.print("Opening: " + yeastFile.getName() + ".\n");
		ImportXml imp = new ImportXml(yeastFile.toString(), "style");
		styleDB = imp.styleHandler.getStyles();
	}
	
	public void readMisc(String path) {
		// read the yeast from the csv file
		try {
			File miscFile = new File(path, "misc_ingr.csv");
			CSVReader reader = new CSVReader(new FileReader(
					miscFile), ',', '\"', true, false);

			try {
				// get the first line and set up the index:
				String[] fields = reader.getAllFieldsInLine();

				int nameIdx = getIndex(fields, "NAME");
				int costIdx = getIndex(fields, "COST");
				int descrIdx = getIndex(fields, "DESCR");
				int unitsIdx = getIndex(fields, "UNITS");
				// int stockIdx = getIndex(fields, "STOCK");
				int stageIdx = getIndex(fields, "STAGE");
				int modIdx = getIndex(fields, "Modified");

				while (true) {
					Misc m = new Misc();
					fields = reader.getAllFieldsInLine();
					m.setName(fields[nameIdx]);

					if (!fields[costIdx].equals(""))
						m.setCost(Double.parseDouble(fields[costIdx]));
					m.setDescription(fields[descrIdx]);
					m.setUnits(fields[unitsIdx]);
					m.setStage(fields[stageIdx]);	
					m.setModified(Boolean.valueOf(fields[modIdx]).booleanValue());
					miscDB.add(m);
				}
			} catch (EOFException e) {
			}
			reader.close();			

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		// sort:
		/*ObjComparator sc = new ObjComparator();
		Collections.sort(miscDB,  sc);*/

	}
	
	public void writeMisc(ArrayList<Ingredient> newIngr, boolean[] add) {		
		
		if (!(newIngr.size() > 0))
			return;
		File miscFile = new File(dbPath, "misc_ingr.csv");
		backupFile(miscFile);

		try {
			CSVWriter writer = new CSVWriter(new FileWriter(miscFile));
			writer.put("ITEM");
			writer.put("NAME");
			writer.put("COST");			
			writer.put("DESCR");
			writer.put("UNITS");
			writer.put("STAGE");
			writer.put("Modified");
			writer.nl();
			int i = 0, j = 0, k = 0;
			while (i < miscDB.size() || j < newIngr.size()) {
				Misc m = null;				
				if (i < miscDB.size()) {
					m = (Misc) miscDB.get(i);
					i++;					
				} else if (j < newIngr.size()) {
					if (newIngr.get(j) instanceof Misc
							&& add[j]){
						m = (Misc) newIngr.get(j);						
					}
					j++;
				}
				if (m != null) {
					k++;
					writer.put("" + (k));
					writer.put(m.getName());
					writer.put("" + m.getCostPerU());
					writer.put(m.getDescription());
					writer.put(m.getUnitsAbrv());
					writer.put(m.getStage());
					writer.put("" + m.getModified());
					writer.nl();
				}

			}

			writer.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void readPrimeSugar(String path) {
		// read the fermentables from the csv file
		try {
			File primeFile = new File(path, "prime_sugar.csv");
			CSVReader reader = new CSVReader(new FileReader(
					primeFile), ',', '\"', true, false);

			try {
				// get the first line and set up the index:
				String[] fields = reader.getAllFieldsInLine();
				int nameIdx = getIndex(fields, "Name");
				int yieldIdx = getIndex(fields, "Yield");
				int unitsIdx = getIndex(fields, "Units");
				int descrIdx = getIndex(fields, "Descr");
				
				while (true) {
					PrimeSugar p = new PrimeSugar();
					fields = reader.getAllFieldsInLine();
					p.setName(fields[nameIdx]);
					p.setYield(Double.parseDouble(fields[yieldIdx]));
					p.setUnits(fields[unitsIdx]);
					p.setAmount(0);
					p.setDescription(fields[descrIdx]);
					primeSugarDB.add(p);		
				}
			} catch (EOFException e) {
			}
			reader.close();	
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
	
	public void readWater(String path) {
		// read the fermentables from the csv file
		try {
			File primeFile = new File(path, "water_profiles.csv");
			CSVReader reader = new CSVReader(new FileReader(
					primeFile), ',', '\"', true, false);

			try {
				// get the first line and set up the index:
				String[] fields = reader.getAllFieldsInLine();
				int nameIdx = getIndex(fields, "NAME");
				int descrIdx = getIndex(fields, "DESCR");
				int caIdx = getIndex(fields, "Ca");
				int mgIdx = getIndex(fields, "Mg");
				int naIdx = getIndex(fields, "Na");
				int so4Idx = getIndex(fields, "SO4");
				int hco3Idx = getIndex(fields, "HCO3");
				int clIdx = getIndex(fields, "Cl");
				int hardnessIdx = getIndex(fields, "Hardness");
				int tdsIdx = getIndex(fields, "TDS");
				int phIdx = getIndex(fields, "PH");
				int alkIdx = getIndex(fields, "Alk");
				
				while (true) {
					WaterProfile w = new WaterProfile();
					fields = reader.getAllFieldsInLine();
					w.setName(fields[nameIdx]);
					w.setDescription(fields[descrIdx]);
					w.setCa(Double.parseDouble(fields[caIdx]));
					w.setMg(Double.parseDouble(fields[mgIdx]));
					w.setNa(Double.parseDouble(fields[naIdx]));
					w.setSo4(Double.parseDouble(fields[so4Idx]));
					w.setHco3(Double.parseDouble(fields[hco3Idx]));
					w.setCl(Double.parseDouble(fields[clIdx]));
					w.setHardness(Double.parseDouble(fields[hardnessIdx]));
					w.setTds(Double.parseDouble(fields[tdsIdx]));
					w.setPh(Double.parseDouble(fields[phIdx]));
					w.setAlkalinity(Double.parseDouble(fields[alkIdx]));
									
					waterDB.add(w);		
				}
			} catch (EOFException e) {
			}
			reader.close();	
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}	
	
	public String[] getPrimeSugarNameList() {
		String[] names = new String[primeSugarDB.size()];
		
		for (int i = 0; i < primeSugarDB.size(); i++) {
			names[i] = ((PrimeSugar)primeSugarDB.get(i)).getName();
		}
	
		return names;
	}
	
	private int getIndex(String[] fields, String key) {
		int i = 0;
		while (i < fields.length && !fields[i].equalsIgnoreCase(key)) {
			i++;
		}
		if (i >= fields.length) // wasn't found
			return -1;
		else
			return i;
	}
	
	public void backupFile(File in){
		try {
		File out = new File (in.getAbsolutePath() + ".bak");
		FileChannel sourceChannel = new FileInputStream(in).getChannel();
		FileChannel destinationChannel = new FileOutputStream(out).getChannel();
		sourceChannel.transferTo(0, sourceChannel.size(), destinationChannel);
		// or
		//  destinationChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
		sourceChannel.close();
		destinationChannel.close();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
}