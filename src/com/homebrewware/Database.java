/**
 *    Filename: Database.java
 *     Version: 0.9.0
 * Description: Database
 *     License: GPLv2
 *        Date: 2017-01-14
 *
 * @author aavis
 *
 * Copyright (c) 2017 Dallas Fletchall
 * @author Dallas Fletchall
 */

/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as
 * published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package com.homebrewware;

import java.awt.Dialog;
import java.awt.Frame;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import java.net.URI;
import java.net.URL;

import java.nio.channels.FileChannel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.mindprod.csv.CSVReader;

public class Database {

	// this class is just some lists of various ingredients
	// read from the csv files.

	private static Database instance = null;
	private Options preferences = Options.getInstance();
	public List<Fermentable> fermDB = new ArrayList<Fermentable>();
	public List<Fermentable> stockFermDB = new ArrayList<Fermentable>();
	public List<Hop> hopsDB = new ArrayList<Hop>();
	public List<Hop> stockHopsDB = new ArrayList<Hop>();
	final public List<Yeast> yeastDB = new ArrayList<Yeast>();
	public List<Style> styleDB = new ArrayList<Style>();
	final public List<Misc> miscDB = new ArrayList<Misc>();
	final public List<PrimeSugar> primeSugarDB = new ArrayList<PrimeSugar>();
	final public List<WaterProfile> waterDB = new ArrayList<WaterProfile>();
	public String dbPath;
	private String styleFileName;
	private Connection conn = null;

	// This is now a singleton
	private Database() throws UnsupportedEncodingException {

		dbPath = Product.getAppPath(Product.Path.DATA);
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			Debug.print("Couldn't find the SQLIte JDBC Driver");
			//return;
		}
        try {


        	Debug.print("Trying to open database: "+ dbPath);
			conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath + File.separator + "ingredients.db");


			Debug.print("Checking for tables");
			DatabaseMetaData meta = conn.getMetaData();
			ResultSet res = meta.getTables(null, null, null,
					new String[] {"TABLE"});

			ArrayList tables = new ArrayList<String>();

			while (res.next()) {
				tables.add(res.getString("TABLE_NAME"));
			}

			Statement stat = conn.createStatement();

			if (!tables.contains("styleguide")) {
				// no Style guide
				Debug.print("Creating styleguide table");
				stat.executeUpdate("create table styleguide (Item INT AUTO_INCREMENT,Name, Category, OG_Low, OG_High, Alc_Low, Alc_High, IBU_Low, IBU_High, Lov_Low, Lov_High," +
						"Appearance, Aroma, Flavor, Mouthfeel, Impression, Comments, Ingredients, Comm_examples, Year );");
			}

			if (!tables.contains("fermentables")) {
				// no fermentables
				Debug.print("Creating fermentables table");
				stat.executeUpdate("create table fermentables (Item INT AUTO_INCREMENT,Name UNIQUE,Yield,Lov,Cost,Stock,Units,Mash,Descr,Steep,Modified, Ferments );");
			} else {
				Debug.print("Checking for fermentables updates");

				// This will cover us if we need to add new rows in the future, to enable people to update the DB
				Map<String, String> newColumns = new HashMap<String, String>();

				newColumns.put("Ferments", "True");

				ResultSet fermentColumns = meta.getColumns(null, null, "fermentables", null);

				// Iterate the Map
				Iterator <Entry<String, String>> it = newColumns.entrySet().iterator();

				while (it.hasNext()) {
					Entry<String, String> e = it.next();
					try {
						fermentColumns.getBoolean(e.getKey());
					} catch (SQLException noColumn) {
						// Column doesn't Exist
						try {
							stat.executeUpdate("ALTER TABLE fermentables ADD " + e.getKey() + " DEFAULT " + e.getValue());
						} catch (SQLException columnExists) {
							// Ignore it
						}
					}
				}

			}

			if (!tables.contains("hops")) {
				// no hops
				Debug.print("Creating hops table");
				stat.executeUpdate("create table hops (Item INT AUTO_INCREMENT,Name,Alpha,Cost,Stock,Units,Descr,Storage,Date,Modified,Type);");
			} else {

				// Add a new column to store the type
				ResultSet testColumns = stat.executeQuery("SELECT * FROM hops LIMIT 1;");
				ResultSetMetaData cmd = testColumns.getMetaData();
				int colCount = cmd.getColumnCount();
				boolean hopColumn = false;

				for (int i = 1; i <= colCount; i++) {
					if (cmd.getColumnName(i).equalsIgnoreCase("Type")) {
						hopColumn = true;
					}
				}

				if (hopColumn == false) {
					// add the column
					stat.executeUpdate("ALTER TABLE hops ADD Type");
				}
			}

			if(!tables.contains("misc")) {
				// no misc
				Debug.print("Creating misc table");
				stat.executeUpdate("create table misc (Item INT AUTO_INCREMENT,Name,Descr,Stock,Units,Cost,Stage,Modified);");
			}

			if(!tables.contains("prime")) {
				// no prime
				Debug.print("Creating prime table");
				stat.executeUpdate("create table prime  ( Item INT AUTO_INCREMENT,Name,Yield,Units,Descr);");
			}

			if(!tables.contains("yeast")) {
				// no yeast
				Debug.print("Creating yeast table");
				stat.executeUpdate("create table yeast (Item INT AUTO_INCREMENT,Name,Cost,Descr,Modified);");
			}
			if(!tables.contains("water_profiles")) {
				// no water_profiles
				Debug.print("Creating water_profiles table");
				stat.executeUpdate("create table water_profiles ( Item INT AUTO_INCREMENT, Name,Descr,Ca,Mg,Na,SO4,HCO3,Cl,Hardness,TDS,PH,Alk);");
			}


	        Debug.print("Created Database succesfully!");
        } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static Database getInstance() {
		if (instance == null) {
			try {
				instance = new Database();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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

	public void readDB(String path, String styleYear){

		// check to see if the style guide is in the DB already

		dbPath = path;
		styleFileName = "styleguide" + styleYear + ".xml";

		File styleFile = new File(path, styleFileName);
		if(styleFile != null && !styleFile.exists()){
			Frame window = new Frame();
			// style file doesn't exist!
			new Dialog(window, "Can not open style guide: "+path+"/"+styleFileName+"\n Please check your install!");
			//try reverting to the base styleguide
			styleFileName = "styleguide.xml";

			styleFile = new File(path, styleFileName);
		}

		try {
			styleDB.clear();
			stockFermDB.clear();
			fermDB.clear();
			stockHopsDB.clear();
			hopsDB.clear();
			yeastDB.clear();
			waterDB.clear();

			readFermentables(dbPath);
			readPrimeSugar(dbPath);
			readHops(dbPath);
			readYeast(dbPath);
			readMisc(dbPath);
			importStyles(dbPath, styleYear);
			readWater(dbPath);


			// sort
			Debug.print(styleDB.size());
			Collections.sort(styleDB);
			Collections.sort(fermDB);
			Collections.sort(hopsDB);
			Collections.sort(yeastDB);
			Collections.sort(waterDB);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	public void readFermentables(String path) {
		// read the fermentables from the csv file
		// get the current date just because
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
		Date date = new Date();

		try {
			Statement statement = conn.createStatement();
			File maltFile = new File(path, "malts.csv");
			if(maltFile != null && maltFile.exists()) {
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
					int fermIdx = getIndex(fields, "Ferments");

					String sql = new String();
					PreparedStatement pStatement;
					while (true) {

						fields = reader.getAllFieldsInLine();

						//Item,Name,Yield,Lov,Cost,Stock,Units,Mash,Descr,Steep,Modified
						Debug.print("SELECT COUNT(*) FROM fermentables WHERE name='"+fields[nameIdx]+"';");
						pStatement = conn.prepareStatement("SELECT COUNT(*) FROM fermentables WHERE name=?;");

						pStatement.setString(1, fields[nameIdx]);
						ResultSet res = pStatement.executeQuery();

						res.next();
						if(res.getInt(1) == 0 ){

							sql = "insert into fermentables (Name,Yield,Lov,Cost,Stock,Units,Mash,Descr,Steep,Modified,Ferments) " +
														"values(?, ?,    ?,  ? ,  ? ,   ?,    ?,    ?,   ?,     ?,       ?)";

							pStatement = conn.prepareStatement(sql);
							 pStatement.setString(1, fields[nameIdx]);
							 pStatement.setString(2, fields[pppgIdx]);
							 pStatement.setString(3, fields[lovIdx]);
							 pStatement.setString(4, fields[costIdx]);
							 pStatement.setString(5, fields[stockIdx]);
							 pStatement.setString(6, fields[unitsIdx]);
							 pStatement.setString(7, fields[mashIdx]);
							 pStatement.setString(8, fields[descrIdx]);

							 pStatement.setString(9, fields[steepIdx]);
							 pStatement.setString(10, fields[modIdx]);
							 pStatement.setString(11, fields[fermIdx]);

							pStatement.executeUpdate();
							fields = null;
						}

					} // end of while loop



				} catch (EOFException e) {
				}
				reader.close();

				// Rename the file

				File newFile = new File(path, "malts.csv" + dateFormat.format(date));

				if (!newFile.exists()) {
					maltFile.renameTo(newFile);
				}else {
					System.out.print("Could not rename malt file");
				}


				Debug.print("All fermentables in DB");
			}

			Fermentable f = new Fermentable();
			ResultSet res = statement.executeQuery("SELECT * FROM fermentables");
			// we weren't clearing this when updating
			stockFermDB.clear();


			while (res.next()) {
				f = new Fermentable();
				//Item,Name,Yield,Lov,Cost,Stock,Units,Mash,Descr,Steep,Modified

				f.setName(res.getString("Name"));
				f.setPppg(Double.parseDouble(res.getString("Yield")));
				f.setLov(Double.parseDouble(res.getString("Lov")));
				f.setCost(Double.parseDouble(res.getString("Cost")));
				f.setUnits(res.getString("Units"));
				f.setMashed(Boolean.valueOf(res.getString("Mash")).booleanValue());
				f.setSteep(Boolean.valueOf(res.getString("Steep")).booleanValue());
				f.ferments(Boolean.valueOf(res.getString("Ferments")).booleanValue());

				if (!res.getString("Stock").equals("")) {
					f.setStock(Double.parseDouble(res.getString("Stock")));
				}
				else
					f.setStock(0);


				f.setDescription(res.getString("Descr"));
				f.setModified(Boolean.valueOf(res.getString("Modified")).booleanValue());
				fermDB.add(f);

				// check to see if we have nonStock set

				if(f.getStock() > 0.00 ) {
					Debug.print("Adding to Stock DB");
					stockFermDB.add(f);
				}
			}
			Debug.print("Ferm Found " + res.getRow());

			Collections.sort(fermDB);

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			System.exit(-1);
		}

	}

	public void writeFermentables(String item, String name, String yield, String lov, String cost, String units,
				String mash, String descr, String steep, String modified) {
		Debug.print("Write fermentable specific");
	/*	File maltsFile = new File(dbPath, "malts.csv");
		backupFile(maltsFile);

		try {
			CSVWriter append = new CSVWriter(new FileWriter(maltsFile, true));
			// we should now be appending
			append.put(item);	// count, no idea why
			append.put(name);
			append.put(yield);
			append.put(lov);
			append.put(cost);
			append.put(units);
			append.put(mash);
			append.put(descr);
			append.put(steep);
			append.put(modified);
			append.nl();

			append.close();
		} catch (IOException e) {
			e.printStackTrace();

		}
		*/


	}



	public void writeFermentables() {
		// Don't need to do this anymore, we store as soon as we add anything to the DB!
		Debug.print("Saving fermentables to file");

		try {
			PreparedStatement pStatement = conn.prepareStatement("SELECT COUNT(*) FROM fermentables WHERE name = ?;");

			PreparedStatement rStatement = conn.prepareStatement("SELECT COUNT(*) FROM fermentables " +
					"WHERE name = ? AND Yield =? AND Lov=? AND Cost=? AND Stock=? AND" +
					" Units=? AND Mash=? AND Descr=? AND Steep=? AND Ferments=?;");


			String sql = "insert into fermentables (Name,Yield,Lov,Cost,Stock,Units,Mash,Descr,Steep, Ferments) " +
					"values(?, ?,  ? , ?, ?, ?, ?, ?, ?, ? )";

			PreparedStatement insertFerm = conn.prepareStatement(sql);

			sql = "UPDATE fermentables SET Yield =?, Lov=?, Cost=?, Stock=?," +
					" Units=?, Mash=?, Descr=?, Steep=?, Ferments=? " +
					" WHERE name = ?";

			PreparedStatement updateFerm = conn.prepareStatement(sql);

			ResultSet res = null;

			int i = 0;


			// sort the list first
			Collections.sort(fermDB);

			while (i < fermDB.size()) {
				Fermentable f = fermDB.get(i);
				i++;

				pStatement.setString(1, f.getName());
				res = pStatement.executeQuery();
				res.next();

				if(res.getInt(1) == 0){
					insertFerm.setString(1, f.getName());
					insertFerm.setString(2, Double.toString(f.getPppg()));
					insertFerm.setString(3, Double.toString(f.getLov()));
					insertFerm.setString(4, Double.toString(f.getCostPerU()));
					insertFerm.setString(5, Double.toString(f.getStock()));
					insertFerm.setString(6, f.getUnitsAbrv());
					insertFerm.setString(7, Boolean.toString(f.getMashed()));
					insertFerm.setString(8, f.getDescription());
					insertFerm.setString(9, Boolean.toString(f.getSteep()));
					insertFerm.setString(10, Boolean.toString(f.ferments()));
					insertFerm.execute();
				} else {
					res.close();
					rStatement.setString(1, f.getName());
					rStatement.setString(2, Double.toString(f.getPppg()));
					rStatement.setString(3, Double.toString(f.getLov()));
					rStatement.setString(4, Double.toString(f.getCostPerU()));
					rStatement.setString(5, Double.toString(f.getStock()));
					rStatement.setString(6, f.getUnitsAbrv());
					rStatement.setString(7, Boolean.toString(f.getMashed()));
					rStatement.setString(8, f.getDescription());
					rStatement.setString(9, Boolean.toString(f.getSteep()));
					rStatement.setString(10, Boolean.toString(f.ferments()));
					res = rStatement.executeQuery();

					// we have a name match, see if anything has changed
					if(res.getInt(1) == 0) {
						res.close();
						Debug.print("Fermentables Update");
						updateFerm.setString(1, Double.toString(f.getPppg()));
						updateFerm.setString(2, Double.toString(f.getLov()));
						updateFerm.setString(3, Double.toString(f.getCostPerU()));
						updateFerm.setString(4, Double.toString(f.getStock()));
						updateFerm.setString(5, f.getUnitsAbrv());
						updateFerm.setString(6, Boolean.toString(f.getMashed()));
						updateFerm.setString(7, f.getDescription());
						updateFerm.setString(8, Boolean.toString(f.getSteep()));
						updateFerm.setString(9, Boolean.toString(f.ferments()));
						// where cause
						updateFerm.setString(10, f.getName());
						updateFerm.executeUpdate();

					}
				}
			}
			//clear the list
			fermDB = new ArrayList<Fermentable>();
			stockFermDB = new ArrayList<Fermentable>();

			Debug.print("Trying to update DB at: "+dbPath);
			readFermentables(dbPath);


			//this.readFermentables(dbPath);


		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void readHops(String path) {

		// Open the database and save the hops in
		// get the current date just because
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
		Date date = new Date();
		Statement statement = null;
		try {
			statement = conn.createStatement();
			// read the hops from the csv file

			File hopsFile = new File(path, "hops.csv");
			if(hopsFile != null && hopsFile.exists()) {

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

					String sql = new String();
					PreparedStatement pStatement;
					while (true) {

						fields = reader.getAllFieldsInLine();

						//Item,Name,Alpha,Cost,Stock,Units,Descr,Storage,Date,Modified

						pStatement = conn.prepareStatement("SELECT COUNT(*) FROM hops WHERE name=?;");

						pStatement.setString(1, fields[nameIdx]);
						ResultSet res = pStatement.executeQuery();

						res.next();
						if(res.getInt(1) == 0 ){

						sql = "insert into hops (Name,Alpha,Cost,Stock,Units,Descr,Storage,Date,Modified) " +
													"values(?,  ?,  ? ,  ? ,   ?,    ?,    ?,   ?,   ?)";

						pStatement = conn.prepareStatement(sql);
							 pStatement.setString(1, fields[nameIdx]);
							 pStatement.setString(2, fields[alphaIdx]);
							 pStatement.setString(3, fields[costIdx]);
							 pStatement.setString(4, fields[stockIdx]);
							 pStatement.setString(5, fields[unitsIdx]);
							 pStatement.setString(6, fields[descrIdx]);
							 pStatement.setString(7, fields[storeIdx]);
							 pStatement.setString(8, fields[dateIdx]);
							 pStatement.setString(9, fields[modIdx]);
							 //pStatement.setString(10, fields[modIdx]);

							pStatement.executeUpdate();
							fields = null;
						}

					}
				} catch (EOFException e) {
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				reader.close();

				// Rename the file
				File newFile = new File(path, "hops.csv" + dateFormat.format(date));

				if (!newFile.exists()) {
					hopsFile.renameTo(newFile);
				}else {
					System.out.print("Could not rename hops file");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		ResultSet res = null;
		try {
			if(statement == null)
				statement = conn.createStatement();
			// read the hops from the csv file
			res = statement.executeQuery("SELECT * FROM hops");

			hopsDB.clear();

		while (res.next()) {
			Hop h = new Hop();
			//Item,Name,Alpha,Cost,Stock,Units,Descr,Storage,Date,Modified
			h.setName(res.getString("Name"));
			h.setAlpha(Double.parseDouble(res.getString("Alpha")));

			if (!res.getString("Cost").equals(""))
				h.setCost(Double.parseDouble(res.getString("Cost")));

			h.setUnits(res.getString("Units"));

			if (!res.getString("Stock").equals(""))
				h.setStock(Double.parseDouble(res.getString("Stock")));

			h.setDescription(res.getString("Descr"));

			if (!res.getString("Storage").equals(""))
				h.setStorage(Double.parseDouble(res.getString("Storage")));

			h.setDate(res.getString("Date"));

			h.setModified(Boolean.valueOf(res.getString("Modified")).booleanValue());

			{ // test for the type
				String tempType = res.getString("Type");
				if (tempType == null || tempType.equalsIgnoreCase("false")) {
					tempType = preferences.getProperty("optHopsType");
				}

				h.setType(tempType);
			}

			hopsDB.add(h);

			if (h.getStock() > 0) {
				stockHopsDB.add(h);
			}

			//Item,Name,Yield,Lov,Cost,Stock,Units,Mash,Descr,Steep,Modified


		}
		Debug.print("Hops Found " + res.getRow());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Collections.sort(hopsDB);


	}

	public void writeHops() {
			// Name,Alpha,Cost,Stock,Units,Descr,Storage,Date,Modified
		Debug.print("Write Hops to DB");
		try {
			PreparedStatement pStatement = conn.prepareStatement("SELECT COUNT(*) FROM hops WHERE name = ?;");
			PreparedStatement rStatement = conn.prepareStatement("SELECT COUNT(*) FROM hops WHERE name = ?" +
					" AND Alpha=? AND Cost=? AND Stock=? AND " +
					"Units=? AND Descr=? AND Storage=? AND Type = ?;");

			String sql = "insert into hops (Name,Alpha,Cost,Stock,Units,Descr,Storage,Date, Type) " +
					"values(?, ?,  ? , ?, ?, ?, ?, ?, ? )";

			PreparedStatement insertMisc = conn.prepareStatement(sql);

			sql = "UPDATE hops SET Alpha=?,Cost=?,Stock=?," +
					"Units=?,Descr=?,Storage=?,Date=?,Type=? " +
					"WHERE name = ?";

			PreparedStatement updateMisc = conn.prepareStatement(sql);

			ResultSet res = null;

			int i = 0;

			//Sort the list
			Collections.sort(hopsDB);

			while (i < hopsDB.size()) {
				Hop h = hopsDB.get(i);
				i++;
				pStatement.setString(1, h.getName());
				res = pStatement.executeQuery();
				res.next();
				// Does this hop exist?
				if(res.getInt(1) == 0){
					insertMisc.setString(1, h.getName());
					insertMisc.setString(2, Double.toString(h.getAlpha()));
					insertMisc.setString(3, Double.toString(h.getCostPerU()));
					insertMisc.setString(4, Double.toString(h.getStock()));
					insertMisc.setString(5, h.getUnitsAbrv());
					insertMisc.setString(6, h.getDescription());
					insertMisc.setString(7, Double.toString(h.getStorage()));
					insertMisc.setString(8, h.getDate().toString());
					insertMisc.setString(9, h.getType());

					insertMisc.executeUpdate();
				} else { // check to see if we need to update this hop

					rStatement.setString(1, h.getName());
					rStatement.setString(2, Double.toString(h.getAlpha()));
					rStatement.setString(3, Double.toString(h.getCostPerU()));
					rStatement.setString(4, Double.toString(h.getStock()));
					rStatement.setString(5, h.getUnitsAbrv());
					rStatement.setString(6, h.getDescription());
					rStatement.setString(7, Double.toString(h.getStorage()));
					rStatement.setString(8, h.getType());

					System.out.println(h.getName() + " - " + h.getType());
					res = rStatement.executeQuery();

					res.next();

					if(res.getInt(1) == 0) {
						// update required
						updateMisc.setString(1, Double.toString(h.getAlpha()));
						updateMisc.setString(2, Double.toString(h.getCostPerU()));
						updateMisc.setString(3, Double.toString(h.getStock()));
						updateMisc.setString(4, h.getUnitsAbrv());
						updateMisc.setString(5, h.getDescription());
						updateMisc.setString(6, Double.toString(h.getStorage()));
						updateMisc.setString(7, h.getDate().toString());
						updateMisc.setString(8, h.getType());
						// where
						updateMisc.setString(9, h.getName());
						System.out.println(updateMisc.toString());
						updateMisc.executeUpdate();
					}

				}
			}

			//clear the list
			hopsDB = new ArrayList<Hop>();
			stockHopsDB = new ArrayList<Hop>();

			readHops(dbPath);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	public void readYeast(String path) {
		// read the yeast from the csv file

		// Open the database and save the hops in
				// get the current date just because
				DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
				Date date = new Date();
				Statement statement = null;
				try {
					statement = conn.createStatement();
					// read the hops from the csv file

					File yeastFile = new File(path, "yeast.csv");
					if(yeastFile != null && yeastFile.exists()) {
						CSVReader reader = new CSVReader(new FileReader(
								yeastFile), ',', '\"', true, false);

						try {
							// get the first line and set up the index:
							String[] fields = reader.getAllFieldsInLine();

							int nameIdx = getIndex(fields, "Name");
							int costIdx = getIndex(fields, "Cost");
							int descrIdx = getIndex(fields, "Descr");
							int modIdx = getIndex(fields, "Modified");

							String sql = new String();
							PreparedStatement pStatement;
							while (true) {

								fields = reader.getAllFieldsInLine();

								//Item,Name,Alpha,Cost,Stock,Units,Descr,Storage,Date,Modified
								Debug.print("SELECT COUNT(*) FROM yeast WHERE name='"+fields[nameIdx]+"';");
								pStatement = conn.prepareStatement("SELECT COUNT(*) FROM yeast WHERE name=?;");

								pStatement.setString(1, fields[nameIdx]);
								ResultSet res = pStatement.executeQuery();

								res.next();
								if(res.getInt(1) == 0 ){

									sql = "insert into yeast (Name,Cost,Descr,Modified) " +
																"values(?,  ?,  ? ,  ? )";

									pStatement = conn.prepareStatement(sql);
									 pStatement.setString(1, fields[nameIdx]);
									 pStatement.setString(2, fields[costIdx]);
									 pStatement.setString(3, fields[descrIdx]);
									 pStatement.setString(4, fields[modIdx]);
									 //pStatement.setString(10, fields[modIdx]);

									pStatement.executeUpdate();
									fields = null;
								}

							}
						} catch (EOFException e) {
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						reader.close();

						// Rename the file
						File newFile = new File(path, "yeast.csv" + dateFormat.format(date));

						if (!newFile.exists()) {
							yeastFile.renameTo(newFile);
						}else {
							System.out.print("Could not rename hops file");
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println(e.getMessage());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// load from the database
				ResultSet res = null;
				try {
					if(statement == null)
						statement = conn.createStatement();
					// read the hops from the csv file
					res = statement.executeQuery("SELECT * FROM yeast");

					yeastDB.clear();

					while (res.next()) {
						Yeast y = new Yeast();

						y.setName(res.getString("Name"));

						if (!res.getString("Cost").equals(""))
							y.setCost(Double.parseDouble(res.getString("Cost")));

						y.setDescription(res.getString("Descr"));
						y.setModified(Boolean.valueOf(res.getString("Modified")).booleanValue());
						yeastDB.add(y);




					}

				Debug.print("Yeast" + res.getRow());

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Collections.sort(hopsDB);
				Collections.sort(stockHopsDB);




	}

	public void writeYeast() {

		//No need to do this now
		Debug.print("Write yeast called");


		try {
			PreparedStatement pStatement = conn.prepareStatement("SELECT COUNT(*) FROM Yeast WHERE name = ?;");

			String sql = "insert into yeast (Name,Cost,Descr) " +
					"values(?, ?,  ?  )";

			PreparedStatement insertMisc = conn.prepareStatement(sql);

			ResultSet res = null;


			// Sort the Yeast
			Collections.sort(yeastDB);
			int i = 0;
			while (i < yeastDB.size()) {
				//Item,Name,Cost,Descr,Modified
				Yeast y = yeastDB.get(i);
				i++;
				pStatement.setString(1, y.getName());
				res = pStatement.executeQuery();
				res.next();
				if(res.getInt(1) == 0) {

					insertMisc.setString(1, y.getName());
					insertMisc.setString(2, Double.toString(y.getCostPerU()));
					insertMisc.setString(3, y.getDescription());

					insertMisc.execute();
				}
			}



		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void readStyles(String path){
		// ead the styles from the csv file
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
		Date date = new Date();
		Statement statement = null;
		try {
			File styleFile = new File(path, "bjcp_styles.csv");
			if(styleFile != null && styleFile.exists()) {
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

					String sql = new String();
					PreparedStatement pStatement;
					while (true) {

						fields = reader.getAllFieldsInLine();

						//Item,Name, Category,OG_Low,OG_High,Alc_Low,Alc_High,IBU_Low,IBU_High,Lov_Low,Lov_High,Comm_examples,Descr
						//Debug.print("SELECT COUNT(*) FROM styleguide WHERE name='"+fields[nameIdx]+"';");
						pStatement = conn.prepareStatement("SELECT COUNT(*) FROM styleguide WHERE name=?;");

						pStatement.setString(1, fields[nameIdx]);
						ResultSet res = pStatement.executeQuery();

						res.next();
						if(res.getInt(1) == 0 ){

							sql = "insert into styleguide (Name, Category,OG_Low,OG_High,Alc_Low,Alc_High,IBU_Low,IBU_High,Lov_Low,Lov_High,Comm_examples,Descr, Year) " +
														"values(?, ?,  ?,  ? ,  ?,?,  ?,  ? ,  ?, ?,  ?,  ?, ?  )";

							pStatement = conn.prepareStatement(sql);
							 pStatement.setString(1, fields[nameIdx]);
							 pStatement.setString(2, fields[catIdx]);
							 pStatement.setString(3, fields[oglowIdx]);
							 pStatement.setString(4, fields[oghighIdx]);
							 pStatement.setString(5, fields[alclowIdx]);
							 pStatement.setString(6, fields[alchighIdx]);
							 pStatement.setString(7, fields[ibulowIdx]);
							 pStatement.setString(8, fields[ibuhighIdx]);
							 pStatement.setString(9, fields[lovlowIdx]);
							 pStatement.setString(10, fields[lovhighIdx]);
							 pStatement.setString(11, fields[commexIdx]);
							 pStatement.setString(12, fields[descrIdx]);
							 pStatement.setString(13, "BJCP");
							 //pStatement.setString(10, fields[modIdx]);

							 pStatement.executeUpdate();
							 fields = null;
						}

					}
				} catch (EOFException e) {
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				reader.close();

			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

		ResultSet res = null;
		try {
			if(statement == null)
				statement = conn.createStatement();
			// read the hops from the csv file
			res = statement.executeQuery("SELECT * FROM styleguide");



			styleDB.clear();

			while (res.next()) {
			Style s = new Style();
			//Item,Name, Category,OG_Low,OG_High,Alc_Low,Alc_High,IBU_Low,IBU_High,Lov_Low,Lov_High,Comm_examples,Descr
			s.setName(res.getString("Name"));
			s.setCategory(res.getString("Category"));
			s.setOgLow(Double.parseDouble(res.getString("OG_Low")));
			s.setOgHigh(Double.parseDouble(res.getString("OG_High")));
			s.setAlcLow(Double.parseDouble(res.getString("Alc_Low")));
			s.setAlcHigh(Double.parseDouble(res.getString("Alc_High")));
			s.setIbuLow(Double.parseDouble(res.getString("IBU_Low")));
			s.setIbuHigh(Double.parseDouble(res.getString("IBU_High")));
			s.setSrmLow(Double.parseDouble(res.getString("Lov_Low")));
			s.setSrmHigh(Double.parseDouble(res.getString("Lov_High")));
			s.setExamples(res.getString("Comm_examples"));
			s.setYear(res.getString("Year"));
			s.comments = res.getString("Descr");
			s.setComplete();
			styleDB.add(s);
			}

			Debug.print("Style Found " + res.getRow());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void importStyles(String path, String year){

		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
		Date date = new Date();
		PreparedStatement pStatement = null;
		ResultSet res = null;
		String sql = new String();

		File styleFile = null;
		Debug.print("Opening "+ path + styleFileName);
		try {

			 styleFile = new File(path, styleFileName);

		} catch (NullPointerException e) {
			// do nothing
			e.printStackTrace();
		}

		if(styleFile != null && styleFile.exists()) {
			Debug.print("Opening: " + path + styleFile.toString() + ".\n");

			ImportXml imp = new ImportXml(styleFile.toString(), "style");

			styleDB = imp.styleHandler.getStyles();

			for(Style style : styleDB) {
				sql = "SELECT COUNT(*) FROM styleguide WHERE name = ? AND year = ?";

				try {
					pStatement = conn.prepareStatement(sql);
					pStatement.setString(1, style.getName());
					pStatement.setString(2, year);

					res = pStatement.executeQuery();

					res.next();

					if(res.getInt(1) == 0 && !style.name.equals("")) {
						// Add this to the style db
						//Item,Name, Category, OG_Low, OG_High, IBU_Low, IBU_High, Lov_Low, Lov_High,"
						//Appearance, Aroma, Flavor, Mouthfeel, Impression, Comments, Ingredients, Year
						sql = "INSERT INTO styleguide (Name, Category,OG_Low,OG_High,Alc_Low,Alc_High,IBU_Low,IBU_High,Lov_Low,Lov_High," +
								"Comm_examples,Appearance, Aroma, Flavor, Mouthfeel, Impression, Comments, Ingredients, Year) " +
								"values(?, ?,?,?,?,?,?,?,?,?,?,?, ?, ?, ?, ?, ?, ?, ?)";

						// we have the year already
						pStatement = conn.prepareStatement(sql);

						pStatement.setString(1, style.name);
						pStatement.setString(2, style.category);
						pStatement.setString(3, Double.toString(style.ogLow));
						pStatement.setString(4, Double.toString(style.ogHigh));
						pStatement.setString(5, Double.toString(style.alcHigh));
						pStatement.setString(6, Double.toString(style.alcLow));
						pStatement.setString(7, Double.toString(style.ibuLow));
						pStatement.setString(8, Double.toString(style.ibuHigh));
						pStatement.setString(9, Double.toString(style.srmLow));
						pStatement.setString(10, Double.toString(style.srmHigh));
						pStatement.setString(11, style.examples);
						pStatement.setString(12, style.appearance);
						pStatement.setString(13, style.aroma);
						pStatement.setString(14, style.flavour);
						pStatement.setString(15, style.mouthfeel);
						pStatement.setString(16, style.impression);
						pStatement.setString(17, style.comments);
						pStatement.setString(18, style.ingredients);
						pStatement.setString(19, year);

						pStatement.execute();
					}

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


			}

			File newStyle = new File(path, styleFileName+"."+dateFormat.format(date));

			if(!newStyle.exists()) {
				styleFile.renameTo(newStyle);
			}


		} // File existed, we can now move on and read from the DB

		try {
			sql = "SELECT * FROM styleguide WHERE year = ?";
			pStatement = conn.prepareStatement(sql);
			pStatement.setString(1, year);

			res = pStatement.executeQuery();
			//Item,Name, Category, OG_Low, OG_High, IBU_Low, IBU_High, Lov_Low, Lov_High,"
			//Appearance, Aroma, Flavor, Mouthfeel, Impression, Comments, Ingredients, Year

			while(res.next()) {
				Style s = new Style();

				s.setName(res.getString("Name"));
				s.setCategory(res.getString("Category"));
				s.setOgHigh(Double.parseDouble(res.getString("OG_High")));
				s.setOgLow(Double.parseDouble(res.getString("OG_Low")));
				s.setAlcLow(Double.parseDouble(res.getString("Alc_Low")));
				s.setAlcHigh(Double.parseDouble(res.getString("Alc_High")));
				s.setIbuLow(Double.parseDouble(res.getString("IBU_Low")));
				s.setIbuHigh(Double.parseDouble(res.getString("IBU_High")));
				s.setSrmLow(Double.parseDouble(res.getString("Lov_Low")));
				s.setSrmHigh(Double.parseDouble(res.getString("Lov_High")));
				if(res.getString("Comm_examples") != null)
					s.setExamples(res.getString("Comm_examples"));
				if(res.getString("Appearance") != null)
					s.appearance = res.getString("Appearance");
				if(res.getString("Aroma") != null)
					s.aroma = res.getString("Aroma");
				if(res.getString("Flavor") != null)
					s.flavour = res.getString("Flavor");
				if(res.getString("Mouthfeel") != null)
					s.mouthfeel = res.getString("Mouthfeel");
				if(res.getString("Impression") != null)
					s.impression = res.getString("Impression");
				if(res.getString("Comments") != null)
					s.comments = res.getString("Comments");
				if(res.getString("Ingredients") != null)
					s.ingredients = res.getString("Ingredients");
		//		Debug.print("Adding style " + s.getName() + s.toText());
				s.setComplete();
				styleDB.add(s);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		Debug.print("Loaded Styles");


	}

	public void readMisc(String path) {
		// read the yeast from the csv file
		PreparedStatement pStatement = null;

		try {
			File miscFile = new File(path, "misc_ingr.csv");
			CSVReader reader = null;
			if(miscFile != null && miscFile.exists()) {
				reader = new CSVReader(new FileReader(
						miscFile), ',', '\"', true, false);
				String sql = new String();

				while (true) {
					String[] fields = reader.getAllFieldsInLine();

					int nameIdx = getIndex(fields, "NAME");
					int costIdx = getIndex(fields, "COST");
					int descrIdx = getIndex(fields, "DESCR");
					int unitsIdx = getIndex(fields, "UNITS");
					int stockIdx = getIndex(fields, "STOCK");
					int stageIdx = getIndex(fields, "STAGE");

					fields = null;
					while (true) {
						fields = reader.getAllFieldsInLine();
						//Item,Name,Descr,Stock,Units,Cost,Stage,Modified

						pStatement = conn.prepareStatement("SELECT COUNT(*) FROM misc WHERE name=?;");



						pStatement.setString(1, fields[nameIdx]);
						ResultSet res = pStatement.executeQuery();

						if(res != null)	res.next();
						if(res == null || res.getInt(1) == 0 ){



						sql = "insert into misc (Name,Descr,Stock,Units,Cost,Stage) " +
													"values(?, ?,  ?,  ? ,  ?,?  )";


						pStatement = conn.prepareStatement(sql);

						pStatement.setString(1, fields[nameIdx]);
						pStatement.setString(2, fields[descrIdx]);
						pStatement.setString(3, fields[stockIdx]);
						pStatement.setString(4, fields[unitsIdx]);
						pStatement.setString(5, fields[costIdx]);
						pStatement.setString(6, fields[stageIdx]);


						pStatement.executeUpdate();
						fields = null;
						}

					}

				}

			}
			//if(reader != null) {reader.close();};
		} catch (EOFException e) {
			//reader.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}


		// Get the fields from the database
		try {
			Debug.print("Loading misc ingredients");
			pStatement = conn.prepareStatement("SELECT * FROM misc;");

			miscDB.clear();
			ResultSet res = pStatement.executeQuery();
			// get the first line and set up the index:
			while(res.next()) {
				Misc m = new Misc();

				m.setName(res.getString("Name"));

				if ((res.getString("Cost") != null) && !res.getString("Cost").equals(""))
					m.setCost(Double.parseDouble(res.getString("Cost")));

				m.setDescription(res.getString("Descr"));
				m.setUnits(res.getString("Units"));
				m.setStage(res.getString("Stage"));
				m.setModified(Boolean.valueOf(res.getString("Modified")).booleanValue());
				miscDB.add(m);
			}
			Debug.print("Found " + res.getRow());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}


	}

	public void writeMisc() {
		Debug.print("Write misc called");
		try {
			PreparedStatement pStatement = conn.prepareStatement("SELECT COUNT(*) FROM misc WHERE name = ?;");

			String sql = "insert into misc (Name,Descr,Stock,Units,Cost,Stage) " +
					"values(?, ?,  ?,  ? ,  ?,?  )";

			PreparedStatement insertMisc = conn.prepareStatement(sql);

			ResultSet res = null;


			int i = 0;

			while (i < miscDB.size()) {
				Misc m = miscDB.get(i);
				pStatement.setString(1, m.getName());
				res = pStatement.executeQuery();

				res.next();
				if(res.getInt(1) == 0) {
					//Name,Descr,Stock,Units,Cost,Stage

					insertMisc.setString(1, m.getName());
					insertMisc.setString(3, Double.toString(m.getCostPerU()));

					insertMisc.setString(2, m.getDescription());
					insertMisc.setString(4, m.getUnitsAbrv());
					insertMisc.setString(6, m.getStage());
					insertMisc.setString(5, Double.toString(m.getCostPerU()));

					pStatement.execute();

				}

				i++;
			}



		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void readPrimeSugar(String path) {
		// read the fermentables from the csv file
		PreparedStatement pStatement = null;
		String sql = new String();
		ResultSet res;
		Debug.print("Opening Priming Sugar");
		try {
			File primeFile = new File(path, "prime_sugar.csv");
			if(primeFile != null && primeFile.exists()) {


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
						fields = reader.getAllFieldsInLine();
						sql = "SELECT COUNT(*) FROM prime WHERE Name = ?";

						pStatement = conn.prepareStatement(sql);
						pStatement.setString(1, fields[nameIdx]);
						res = pStatement.executeQuery();
						res.next();
						if(res == null || res.getInt(1) == 0) {
							// No results from the DB, add it in
							sql = "INSERT INTO prime (Name,Yield,Units,Descr) VALUES ("+
														"?, ?, ?, ?);";
							pStatement = conn.prepareStatement(sql);

							pStatement.setString(1, fields[nameIdx]);
							pStatement.setString(2, fields[yieldIdx]);
							pStatement.setString(3, fields[unitsIdx]);
							pStatement.setString(4, fields[descrIdx]);

							pStatement.execute();
							fields = null;
						}
						//Item,Name,Yield,Units,Descr
					}
				} catch (NullPointerException e) {
					e.printStackTrace();
				} catch (EOFException e) {

				} catch (SQLException e) {
					e.printStackTrace();
				}
				reader.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

		try {
			pStatement = conn.prepareStatement("SELECT * FROM prime;");
			res = pStatement.executeQuery();

			primeSugarDB.clear();
			// loop the results
			while(res.next()) {
				PrimeSugar p = new PrimeSugar();

				p.setName(res.getString("Name"));
				p.setYield(Double.parseDouble(res.getString("Yield")));
				p.setUnits(res.getString("Units"));
				p.setAmount(0);
				p.setDescription(res.getString("Descr"));
				primeSugarDB.add(p);
			}
			Debug.print("Prime Found " + res.getRow());

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void readWater(String path) {
		PreparedStatement pStatement = null;
		String sql = new String();
		ResultSet res = null;
		Debug.print("Reading water");
		// read the fermentables from the csv file
		try {
			File waterFile = new File(path, "water_profiles.csv");

			if(waterFile != null && waterFile.exists()) {
				CSVReader reader = new CSVReader(new FileReader(
						waterFile), ',', '\"', true, false);

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
					fields = null;

					while (true) {
						//Name,Descr,Ca,Mg,Na,SO4,HCO3,Cl,Hardness,TDS,PH,Alk
						fields = reader.getAllFieldsInLine();

						sql = "SELECT COUNT(*) FROM water_profiles WHERE name = ?";
						pStatement = conn.prepareStatement(sql);
						pStatement.setString(1, fields[nameIdx]);
						res = pStatement.executeQuery();

						res.next();
						if(res.getInt(1) == 0) {
							// Add this to the DB
							//Name,Descr,Ca,Mg,Na,SO4,HCO3,Cl,Hardness,TDS,PH,Alk
							sql = "INSERT INTO water_profiles (Name,Descr,Ca,Mg,Na,SO4,HCO3,Cl,Hardness,TDS,PH,Alk)"+
														" VALUES (?, ?,    ?, ?, ?, ?, ?,   ?,  ?,       ?,  ?, ?);";

							pStatement = conn.prepareStatement(sql);
							pStatement.setString(1, fields[nameIdx]);
							pStatement.setString(2, fields[descrIdx]);
							pStatement.setString(3, fields[caIdx]);
							pStatement.setString(4, fields[mgIdx]);
							pStatement.setString(5, fields[naIdx]);
							pStatement.setString(6, fields[so4Idx]);
							pStatement.setString(7, fields[hco3Idx]);
							pStatement.setString(8, fields[clIdx]);
							pStatement.setString(9, fields[hardnessIdx]);
							pStatement.setString(10, fields[tdsIdx]);
							pStatement.setString(11, fields[phIdx]);
							pStatement.setString(12, fields[alkIdx]);

							//Dump it in
							pStatement.execute();
							fields = null;
						}


					}
				} catch (EOFException e) {
				} catch (NullPointerException e) {
					e.printStackTrace();
				}

				reader.close();

				DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
				Date date = new Date();
				File newFile = new File(path, "water_profiles.csv" + dateFormat.format(date));

				if (!newFile.exists()) {
					waterFile.renameTo(newFile);
				}else {
					System.out.print("Could not rename Water file");
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		try {
			// Now read the Database out

			pStatement = conn.prepareStatement("SELECT * FROM water_profiles;");
			res = pStatement.executeQuery();
			waterDB.clear();
			while(res.next() ) {

				//Name,Descr,Ca,Mg,Na,SO4,HCO3,Cl,Hardness,TDS,PH,Alk
				WaterProfile w = new WaterProfile();
				w.setName(res.getString("Name"));
				w.setDescription(res.getString("Descr"));
				w.setCa(Double.parseDouble(res.getString("Ca")));
				w.setMg(Double.parseDouble(res.getString("Mg")));
				w.setNa(Double.parseDouble(res.getString("Na")));
				w.setSo4(Double.parseDouble(res.getString("SO4")));
				w.setHco3(Double.parseDouble(res.getString("HCO3")));
				w.setCl(Double.parseDouble(res.getString("Cl")));
				w.setHardness(Double.parseDouble(res.getString("Hardness")));
				w.setTds(Double.parseDouble(res.getString("TDS")));
				w.setPh(Double.parseDouble(res.getString("PH")));
				w.setAlkalinity(Double.parseDouble(res.getString("Alk")));

				waterDB.add(w);
			}

			Debug.print("Water Found " + res.getRow());
		}catch (SQLException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		Debug.print("Read all water");
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

	public int find(Object seek) {
		// check the objects and seek specifically
		int index = -1;
		if(seek instanceof Fermentable) {
			index = Collections.binarySearch(fermDB, (Fermentable)seek);
		} else if(seek instanceof Hop) {
			Comparator<Hop> c = new Comparator<Hop>()  {
				public int compare(Hop h1, Hop h2){

					int result = h1.getName().compareToIgnoreCase(h2.getName());

					return result ;
				}

			};
			index = Collections.binarySearch(hopsDB, (Hop)seek, c);
		} else if(seek instanceof Yeast) {
			index = Collections.binarySearch(yeastDB, (Yeast)seek);
		}
		// we can't find the object, lets return
		return index;
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

	public boolean loadRRecipes() {

		// Reads the list of recipes on the remote Database

	    try
	    {

	    	String baseURL = Options.getInstance().getProperty("cloudURL");
	    	URI rURI = new URI("http", null, baseURL, 80, "/recipes/", null, null);
	    	Debug.print("Trying to access: " + rURI.toString());
	    	URL url = rURI.toURL();
	    	InputStream response = url.openStream();
	    	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

	        dbf.setValidating(false);
	        dbf.setIgnoringComments(false);
	        dbf.setIgnoringElementContentWhitespace(true);
	        dbf.setNamespaceAware(true);
	        // dbf.setCoalescing(true);
	        // dbf.setExpandEntityReferences(true);

	        DocumentBuilder db = null;
	        db = dbf.newDocumentBuilder();
	        //db.setEntityResolver(new NullResolver());

	        // db.setErrorHandler( new MyErrorHandler());

	        Document readXML = db.parse(response);


	        NodeList childNodes = readXML.getChildNodes();

	        Debug.print("Found: " + childNodes.getLength() + "Recipes " + childNodes.item(0).getNodeName());
	        // check that we have a valid first node recipe
	        if(childNodes.item(0).getNodeName().equals("recipes")){
	        	return true;
	        }

	        Debug.print("False");
	        return false;
	        /*
	        for(int x = 0; x < childNodes.getLength(); x++ ) {


	        	Node child = childNodes.item(x);

	        	NamedNodeMap childAttr = child.getAttributes();

	        	childAttr.getNamedItem("id");



	        }*/

	    } catch (Exception e) {
	    	e.printStackTrace();
	    	return false;
	    }


	}
}
