/*
 * Created on May 2, 2006
 * by aavis
 *
 */
package ca.strangebrew;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

public class OpenImport {

	private String fileType = "";
	private Recipe myRecipe;
	private List<Recipe> recipes = null;

	private String checkFileType(File f) {

		if (f.getPath().endsWith(".rec"))
			return "promash";

		// let's open it up and have a peek
		// we'll only read 10 lines

		if (f.getPath().endsWith(".qbrew") || (f.getPath().endsWith(".xml"))) {
			try {
				FileReader in = new FileReader(f);
				BufferedReader inb = new BufferedReader(in);
				String c;
				int i = 0;
				while ((c = inb.readLine()) != null && i < 10) {
					// check for an opening tag of Recipes too
					if (c.indexOf("BeerXML Format") > -1 || c.indexOf("<RECIPES>") > -1 )
						return "beerxml";
					if (c.indexOf("STRANGEBREWRECIPE") > -1)
						return "sb";
					if ((c.indexOf("generator=\"qbrew\"") > -1) || (c.indexOf("application=\"qbrew\"") > -1))
						return "qbrew";
					i++;
				}
			} catch (Exception e) {				
				e.printStackTrace();
			} 
		}
		return "";
	}

	public String getFileType() {
		return fileType;
	}
	public List<Recipe> getRecipes() {
		return recipes;
	}

	public Recipe openFile(File f) {

		fileType = checkFileType(f);
		Debug.print("File type: " + fileType);

		if (fileType.equals("promash")) {
			PromashImport imp = new PromashImport();
			myRecipe = imp.readRecipe(f);
		} else if (fileType.equals("sb") || fileType.equals("qbrew")) {
			ImportXml imp = new ImportXml(f.toString(), "recipe");
			myRecipe = imp.handler.getRecipe();
		} else if (fileType.equals("beerxml")) {
			ImportXml imp = new ImportXml(f.toString(), "beerXML");
			myRecipe = imp.beerXmlHandler.getRecipe();
			recipes = imp.beerXmlHandler.getRecipes();
		} else {
			// unrecognized type, just return a new blank recipe
			myRecipe = new Recipe();
		}

		return myRecipe;

	}
}
