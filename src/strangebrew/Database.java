package strangebrew;
import java.io.EOFException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import com.mindprod.csv.*;

/**
 * $Id: Database.java,v 1.2 2004/10/20 19:34:22 andrew_avis Exp $
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

	private ArrayList fermDB = new ArrayList();
	private ArrayList hopsDB = new ArrayList();

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
					f.setMash(Boolean.parseBoolean(fields[mashIdx]));
					f.setSteep(Boolean.parseBoolean(fields[steepIdx]));
					if (!fields[stockIdx].equals(""))
						f.setAmount(Double.parseDouble(fields[stockIdx]));
					else
						f.setAmount(0);
					f.setDesc(fields[descrIdx]);
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
					h.setDesc(fields[descrIdx]);
					if (!fields[storeIdx].equals(""))
						h.setStorage(Double.parseDouble(fields[storeIdx]));
					h.setDate(fields[dateIdx]);
					hopsDB.add(h);
				}
			} catch (EOFException e) {
			}
			reader.close();
			for (int i=0;i<hopsDB.size(); i++){
				Hop h = (Hop)(hopsDB.get(i));
				System.out.print(h.toXML());
			}

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

	}
	
	private int getIndex(String[] fields, String key){
		int i = 0;
		while (i<fields.length && !fields[i].equalsIgnoreCase(key)){
			i++;
		}
		if (i >= fields.length)  // wasn't found
			return -1;
		else
			return i;
	}
}