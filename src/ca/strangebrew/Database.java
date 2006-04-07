package ca.strangebrew;
import java.io.EOFException;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.util.*;

import com.mindprod.csv.*;

/**
 * $Id: Database.java,v 1.1 2006/04/07 13:59:14 andrew_avis Exp $
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
	
	private final static boolean DEBUG = false;

	public ArrayList fermDB = new ArrayList();
	public ArrayList hopsDB = new ArrayList();
	public ArrayList yeastDB = new ArrayList();
	public ArrayList styleDB = new ArrayList();
	public ArrayList miscDB = new ArrayList();

	public void readDB(String dbPath){
		readFermentables(dbPath);
		readHops(dbPath);
		readYeast(dbPath);
		readStyles(dbPath);
		readMisc(dbPath);
	}
	public void readFermentables(String dbPath) {
		// read the fermentables from the csv file
		try {
			File maltFile = new File(dbPath, "malts.csv");
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
					fermDB.add(f);
				}
			} catch (EOFException e) {
			}
			reader.close();


		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		ObjComparator sc = new ObjComparator();
		Collections.sort(styleDB,  sc);

	}

	public void readHops(String dbPath) {
		// read the hops from the csv file
		try {
			File hopsFile = new File(dbPath, "hops.csv");
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
					hopsDB.add(h);
				}
			} catch (EOFException e) {
			}
			reader.close();

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		// sort:
		ObjComparator sc = new ObjComparator();
		Collections.sort(styleDB,  sc);

	}

	public void readYeast(String dbPath) {
		// read the yeast from the csv file
		try {
			File yeastFile = new File(dbPath, "yeast.csv");
			CSVReader reader = new CSVReader(new FileReader(
					yeastFile), ',', '\"', true, false);

			try {
				// get the first line and set up the index:
				String[] fields = reader.getAllFieldsInLine();

				int nameIdx = getIndex(fields, "Name");
				int costIdx = getIndex(fields, "Cost");
				int descrIdx = getIndex(fields, "Descr");

				while (true) {
					Yeast y = new Yeast();
					fields = reader.getAllFieldsInLine();
					y.setName(fields[nameIdx]);

					if (!fields[costIdx].equals(""))
						y.setCost(Double.parseDouble(fields[costIdx]));
					y.setDescription(fields[descrIdx]);
					yeastDB.add(y);
				}
			} catch (EOFException e) {
			}
			reader.close();
			if (DEBUG){
				for (int i=0; i<yeastDB.size(); i++)
					System.out.print(((Yeast) yeastDB.get(i)).toXML());
			}

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		// sort:
		ObjComparator sc = new ObjComparator();
		Collections.sort(styleDB,  sc);

	}

	public void readStyles(String dbPath){
			// read the styles from the csv file
			try {
				File styleFile = new File(dbPath, "bjcp_styles.csv");				
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
						s.setLovLow(Double.parseDouble(fields[lovlowIdx]));
						s.setLovHigh(Double.parseDouble(fields[lovhighIdx]));
						s.setCommercialEx(fields[commexIdx]);
						s.setDescription(fields[descrIdx]);
						styleDB.add(s);
					}
				} catch (EOFException e) {
				}
				reader.close();
				if (DEBUG){
					for (int i=0; i<styleDB.size(); i++)
						System.out.print(((Style) styleDB.get(i)).toXML());
				}

			} catch (IOException e) {
				e.printStackTrace();
				System.out.println(e.getMessage());
			}
			// sort:
			ObjComparator sc = new ObjComparator();
			Collections.sort(styleDB,  sc);
		
	}
	
	public void readMisc(String dbPath) {
		// read the yeast from the csv file
		try {
			File miscFile = new File(dbPath, "misc_ingr.csv");
			CSVReader reader = new CSVReader(new FileReader(
					miscFile), ',', '\"', true, false);

			try {
				// get the first line and set up the index:
				String[] fields = reader.getAllFieldsInLine();

				int nameIdx = getIndex(fields, "NAME");
				int costIdx = getIndex(fields, "COST");
				int descrIdx = getIndex(fields, "DESCR");
				int unitsIdx = getIndex(fields, "UNITS");
				int stockIdx = getIndex(fields, "STOCK");
				int stageIdx = getIndex(fields, "STAGE");

				while (true) {
					Misc m = new Misc();
					fields = reader.getAllFieldsInLine();
					m.setName(fields[nameIdx]);

					if (!fields[costIdx].equals(""))
						m.setCost(Double.parseDouble(fields[costIdx]));
					m.setDescription(fields[descrIdx]);
					m.setUnits(fields[unitsIdx]);
					m.setStage(fields[stageIdx]);					
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
		ObjComparator sc = new ObjComparator();
		Collections.sort(styleDB,  sc);

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

	public class ObjComparator implements Comparator {

		/* (non-Javadoc)
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare(Object a, Object b) {
			if (a.getClass().getName().equalsIgnoreCase("strangebrew.Style")) {
				Style a1 = (Style) a;
				Style b1 = (Style) b;
				int result = a1.getName().compareTo(b1.getName());
				return (result == 0 ? -1 : result);
			} else if (a.getClass().getName().equalsIgnoreCase("strangebrew.Yeast")) {
				Yeast a1 = (Yeast) a;
				Yeast b1 = (Yeast) b;
				int result = a1.getName().compareTo(b1.getName());
				return (result == 0 ? -1 : result);
			} else if (a.getClass().getName().equalsIgnoreCase("strangebrew.Misc")) {
				Misc a1 = (Misc) a;
				Misc b1 = (Misc) b;
				int result = a1.getName().compareTo(b1.getName());
				return (result == 0 ? -1 : result);
			}
			
			else return 0;
		}

	}
}