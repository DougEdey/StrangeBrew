/*
 * $Id: XmlBeerXmlHandler.java,v 1.1 2006/05/02 16:49:26 andrew_avis Exp $
 * Created on Oct 14, 2004
 */
package ca.strangebrew;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author aavis
 *
 * This class handles XML import of BeerXML recipes.
 */
public class XmlBeerXmlHandler extends DefaultHandler {
	private Recipe myRecipe = null;
	private Hop h;
	private Fermentable f;
	private ArrayList recipes = new ArrayList();

	private Attributes currentAttributes = null;
	private String currentList = null; //current List name
	private String currentElement = null; // current element name	
	private String descrBuf = ""; // buffer to hold long descriptions
 

	private boolean newRecipe = false;
	private int numRecipes = 0;



	public Recipe getRecipe() {
		return myRecipe;
	}

	//===========================================================
	// SAX DocumentHandler methods
	//===========================================================

	public void setDocumentLocator(Locator l) {
		// Save this to resolve relative URIs or to give diagnostics.
		System.out.print("LOCATOR");
		System.out.print("\n SYS ID: " + l.getSystemId());
		System.out.flush();

	}

	// we don't do anything with the start of a document
	public void startDocument() throws SAXException {
	}

	// this is debug stuff, delete later
	public void endDocument() throws SAXException {

		// nl();
		System.out.flush();
	}

	/**
	 * This method is called every time we encounter a new element
	 * To handle other xml import types, we check for 
	 * a "signpost" element that indicates the file type, then
	 * call out to the appropriate element handler for that file type.
	 * 
	 */
	public void startElement(String namespaceURI, String lName, // local unit
			String qName, // qualified unit
			Attributes attrs) throws SAXException {
		String eName = lName; // element unit

		if ("".equals(eName))
			eName = qName; // namespaceAware = false

		currentElement = eName;
		currentAttributes = attrs;
		
		if (eName.equalsIgnoreCase("recipe")){
			newRecipe = true;
			myRecipe = new Recipe();
			currentList = eName;
			
		}
		if (eName.equalsIgnoreCase("hops") || 
				eName.equalsIgnoreCase("FERMENTABLES") ||
				eName.equalsIgnoreCase("MISCS") ||
				qName.equalsIgnoreCase("yeasts") ||
				qName.equalsIgnoreCase("waters") ||
				qName.equalsIgnoreCase("style") ||
				qName.equalsIgnoreCase("equipment") ||
				qName.equalsIgnoreCase("mash")){
			currentList = eName;			
		}
		if (eName.equalsIgnoreCase("hop")){
			h = new Hop();
		}
		if (eName.equalsIgnoreCase("FERMENTABLE")){
			f = new Fermentable();
		}		
		if (eName.equalsIgnoreCase("notes")){
			descrBuf = "";
		}
	}

	
	public void endElement(String namespaceURI, String sName, // simple name
			String qName // qualified name
	) throws SAXException {


		if (qName.equalsIgnoreCase("recipe")){
			newRecipe = false;
			numRecipes ++;
			recipes.add(myRecipe);
			
		}
		if (qName.equalsIgnoreCase("hops") ||
				qName.equalsIgnoreCase("fermentables") ||
				qName.equalsIgnoreCase("miscs") ||
				qName.equalsIgnoreCase("yeasts") ||
				qName.equalsIgnoreCase("waters") ||
				qName.equalsIgnoreCase("style") ||
				qName.equalsIgnoreCase("equipment") ||
				qName.equalsIgnoreCase("mash"))  {
			currentList = "recipe";			
		}
		if (qName.equalsIgnoreCase("hop")){			
			myRecipe.addHop(h);
		}
		if (qName.equalsIgnoreCase("fermentable")){			
			myRecipe.addMalt(f);
		}
		if (qName.equalsIgnoreCase("notes")){
			if (currentList.equalsIgnoreCase("hops"))
				h.setDescription(descrBuf);
			if (currentList.equalsIgnoreCase("fermentables"))
				f.setDescription(descrBuf);
		}
		




	}

	/**
	 * This is the "meat" of the handler.  We figure out where in the
	 * document we are, based on the current list, and then start puting
	 * the data into the recipe fields.
	 */
	public void characters(char buf[], int offset, int len) throws SAXException {
		String s = new String(buf, offset, len);

		// we're inside a style:
		if (!s.trim().equals("")) {
			if (currentElement.equalsIgnoreCase("notes"))
					descrBuf += s;
			
			if (currentList.equalsIgnoreCase("hops")){
				if (currentElement.equalsIgnoreCase("name"))
					h.setName(s.trim());
				if (currentElement.equalsIgnoreCase("amount"))
					h.setAmountAs(Double.parseDouble(s.trim()), "kg");				
				if (currentElement.equalsIgnoreCase("form"))
					h.setType(s.trim());
				if (currentElement.equalsIgnoreCase("hsi"))
					h.setStorage(Double.parseDouble(s.trim()));
				if (currentElement.equalsIgnoreCase("alpha"))
					h.setAlpha(Double.parseDouble(s.trim()));
				if (currentElement.equalsIgnoreCase("time"))
					h.setMinutes(new Double(s.trim()).intValue());
				if (currentElement.equalsIgnoreCase("use")){
					if (s.trim().equalsIgnoreCase("boil") || s.trim().equalsIgnoreCase("aroma"))
						h.setAdd("Boil");
					if (s.trim().equalsIgnoreCase("dry hop"))
						h.setAdd("Dry");
					if (s.trim().equalsIgnoreCase("first wort"))
						h.setAdd("FWH");
					
				}
					
			}
			else if (currentList.equalsIgnoreCase("fermentables")){
				if (currentElement.equalsIgnoreCase("name"))
					f.setName(s.trim());
				if (currentElement.equalsIgnoreCase("amount"))
					f.setAmountAs(Double.parseDouble(s.trim()), "kg");				
				if (currentElement.equalsIgnoreCase("potential"))
					f.setPppg(Double.parseDouble(s.trim()));
				if (currentElement.equalsIgnoreCase("color"))
					f.setLov(Double.parseDouble(s.trim()));
				if (currentElement.equalsIgnoreCase("RECOMMEND_MASH"))
					f.setMashed(s.trim().equalsIgnoreCase("true"));
				
			}
			else if (currentList.equalsIgnoreCase("style")){
				if (currentElement.equalsIgnoreCase("name"))
					myRecipe.setStyle(s.trim());
			}
			else if (currentList.equalsIgnoreCase("recipe")){
				if (currentElement.equalsIgnoreCase("name"))
					myRecipe.setName(s.trim());
				if (currentElement.equalsIgnoreCase("brewer"))
					myRecipe.setBrewer(s.trim());
				if (currentElement.equalsIgnoreCase("BATCH_SIZE")){
					myRecipe.setPostBoil(Double.parseDouble(s.trim()));
					myRecipe.setVolUnits("l");
				}
				if (currentElement.equalsIgnoreCase("BOIL_TIME"))
					myRecipe.setBoilMinutes(new Double(s.trim()).intValue());
				if (currentElement.equalsIgnoreCase("EFFICIENCY"))
					myRecipe.setEfficiency(Double.parseDouble(s.trim()));
				if (currentElement.equalsIgnoreCase("EFFICIENCY"))
					myRecipe.setEfficiency(Double.parseDouble(s.trim()));
				if (currentElement.equalsIgnoreCase("IBU_METHOD"))
					myRecipe.setIBUMethod(s.trim()); 
				
			}

			
		}
		


	}

	public void ignorableWhitespace(char buf[], int offset, int len) throws SAXException {
		// nl();
		// emit("IGNORABLE");
	}

	public void processingInstruction(String target, String data) throws SAXException {
		// nl();
		emit("PROCESS: ");
		emit("<?" + target + " " + data + "?>");
	}

	//===========================================================
	// SAX ErrorHandler methods
	//===========================================================

	// treat validation errors as fatal
	public void error(SAXParseException e) throws SAXParseException {
		throw e;
	}

	// dump warnings too
	public void warning(SAXParseException err) throws SAXParseException {
		System.out.println("** Warning" + ", line " + err.getLineNumber() + ", uri "
				+ err.getSystemId());
		System.out.println("   " + err.getMessage());
	}

	//===========================================================
	// Utility Methods ...
	//===========================================================

	// Wrap I/O exceptions in SAX exceptions, to
	// suit handler signature requirements
	private void emit(String s) throws SAXException {
		// System.out.print(s);
		// System.out.flush();
	}

	// Start a new line
	// and indent the next line appropriately
	private void nl() throws SAXException {
		String indentString = "    "; // Amount to indent
		int indentLevel = 0;
		String lineEnd = System.getProperty("line.separator");
		System.out.print(lineEnd);
		for (int i = 0; i < indentLevel; i++)
			System.out.print(indentString);

	}

}
