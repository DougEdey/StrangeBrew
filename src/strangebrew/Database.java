package strangebrew;
import java.io.EOFException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import com.mindprod.csv.*;

/**
 * $Id: Database.java,v 1.7 2004/10/25 17:08:21 andrew_avis Exp $
 * @author aavis
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Database {

	// this class is just a list of various ingredients
	// read from the csv files.
	// I suspect that binary files will be faster, and
	// we might want to move that way in the future.
	
	private final static boolean DEBUG = true;

	public ArrayList fermDB = new ArrayList();
	public ArrayList hopsDB = new ArrayList();
	public ArrayList yeastDB = new ArrayList();
	public ArrayList styleDB = new ArrayList();

	public void readFermentables() {
		// read the fermentables from the csv file
		try {
			CSVReader reader = new CSVReader(new FileReader(
					"src/strangebrew/data/malts.csv"), ',', '\"', true, false);

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
					f.setMash(Boolean.valueOf(fields[mashIdx]).booleanValue());
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

	}

	public void readHops() {
		// read the hops from the csv file
		try {
			CSVReader reader = new CSVReader(new FileReader(
					"src/strangebrew/data/hops.csv"), ',', '\"', true, false);

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

	}

	public void readYeast() {
		// read the yeast from the csv file
		try {
			CSVReader reader = new CSVReader(new FileReader(
					"src/strangebrew/data/yeast.csv"), ',', '\"', true, false);

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

	}

	public void readStyles(){
			// read the styles from the csv file
			try {
				CSVReader reader = new CSVReader(new FileReader(
						"src/strangebrew/data/bjcp_styles.csv"), ',', '\"', true, false);

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
				if (true){
					for (int i=0; i<styleDB.size(); i++)
						System.out.print(((Style) styleDB.get(i)).toXML());
				}

			} catch (IOException e) {
				e.printStackTrace();
				System.out.println(e.getMessage());
			}
		
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
}