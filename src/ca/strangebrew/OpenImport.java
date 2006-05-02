/*
 * Created on May 2, 2006
 * by aavis
 *
 */
package ca.strangebrew;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JOptionPane;

public class OpenImport {

	private String fileType;
	private Recipe myRecipe;
	
	private String checkFileType(File f){
		
		String suff = f.getPath().substring(f.getPath().length()-3, f.getPath().length());
		
		Debug.print(suff);
		if (suff.equalsIgnoreCase(".rec"))
			return "promash";
		
		// let's open it up and have a peek
		// we'll only read 10 lines
		try {
			FileReader in = new FileReader(f);
			BufferedReader inb = new BufferedReader(in);
			String c;			
			int i = 0;			
			while ((c = inb.readLine()) != null && i<10){
				if (c.indexOf("BeerXML Format") > -1)
					return "beerxml";
				if (c.indexOf("STRANGEBREWRECIPE") > -1)
					return "sb";
				if (c.indexOf("generator=\"qbrew\"") > -1)
					return "qbrew";
				i++;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}				
		
		return "";
	}
	
	public Recipe openFile(File f){
		
		String type = checkFileType(f);
		Debug.print("File type: " + type);
		
		if (type.equalsIgnoreCase("")){
			Debug.print("Unrecognized file");
			JOptionPane.showMessageDialog(
					null, 
					"The file you've tried to open isn't a recognized format. \n" +
					"You can open: \n" +
					"StrangeBrew 1.x and Java files (.xml)\n" +
					"QBrew files (.qbrew)\n" +
					"BeerXML files (.xml)\n" +
					"Promash files (.rec)",
					"Unrecognized Format!", 
					JOptionPane.INFORMATION_MESSAGE);
			myRecipe = new Recipe();			
		}
		
		if (type.equals("promash")){
			
			PromashImport imp = new PromashImport();										
			myRecipe = imp.readRecipe(f);
			
		}
		if (type.equals("sb") || type.equals("qbrew")){			
			ImportXml imp = new ImportXml(f.toString(), "recipe");
			myRecipe = imp.handler.getRecipe();
		}
		if (type.equals("beerxml")){
			ImportXml imp = new ImportXml(f.toString(), "beerXML");
			myRecipe = imp.beerXmlHandler.getRecipe();
		}
		
		
		
		return myRecipe;
		
		
	}
}
