/*
 * $Id: XmlStyleHandler.java,v 1.1 2006/04/27 17:29:52 andrew_avis Exp $
 * Created on Oct 14, 2004
 */
package ca.strangebrew;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author aavis
 *
 * This class handles XML import of BJCP Style information.
 */
public class XmlStyleHandler extends DefaultHandler {
	private Style style = null;
	private ArrayList styles = new ArrayList();

	private Attributes currentAttributes = null;
	private String currentList = null; //current List name
	private String currentElement = null; // current element name
	private String descrBuf = ""; // buffer to hold long descriptions
	private String buffer = ""; // buffer 

	private boolean newStyle = false;

	private String category = "";

	public ArrayList getStyles() {
		return styles;
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
		
		if (eName.equalsIgnoreCase("og") ||
				eName.equalsIgnoreCase("fg") ||
				eName.equalsIgnoreCase("ibu") ||
				eName.equalsIgnoreCase("srm") ||
				eName.equalsIgnoreCase("abv")) {
			currentList = eName;
		}

		if (eName.equalsIgnoreCase("subcategory")) {
			style = new Style();
			style.setCategory(category);
			newStyle = true;
			

			if (attrs != null) {
				for (int i = 0; i < attrs.getLength(); i++) {
					String s = attrs.getLocalName(i); // Attr name
					if ("".equalsIgnoreCase(s))
						s = attrs.getQName(i);
					if (s.equalsIgnoreCase("id")) {
						style.setCatNum(attrs.getValue(i));
					}
				}
			}
		}
	}

	/**
	 * At the end of each element, we should check if we're looking at a list.
	 * If we are, set the list to null. This way, we can tell if we're looking
	 * at an element that has (stupidly) the same unit as a list... eg <MASH>is
	 * in the recipe (indicating whether it's mashed or not), and the unit of
	 * the mash list!
	 */
	public void endElement(String namespaceURI, String sName, // simple name
			String qName // qualified name
	) throws SAXException {

		if (qName.equalsIgnoreCase("subcategory") && newStyle) {
			styles.add(style);
			style = null;
			newStyle = false;
		}
		
		if (qName.equalsIgnoreCase("appearance")){			
			style.appearance = descrBuf;
			descrBuf = "";
		}
		if (qName.equalsIgnoreCase("aroma")){			
			style.aroma = descrBuf;
			descrBuf = "";
		}
		if (qName.equalsIgnoreCase("flavor")){
			style.flavour = descrBuf;
			descrBuf = "";
		}
		if (qName.equalsIgnoreCase("mouthfeel")){
			style.mouthfeel = descrBuf;
			descrBuf = "";
		}
		if (qName.equalsIgnoreCase("impression")){
			style.impression = descrBuf;
			descrBuf = "";
		}
		if (qName.equalsIgnoreCase("comments")){
			style.comments = descrBuf;
			descrBuf = "";
		}
		if (qName.equalsIgnoreCase("ingredients")){
			style.ingredients = descrBuf;
			descrBuf = "";
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
		if (!s.trim().equals("") && newStyle) {
			if (currentElement.equalsIgnoreCase("name")){
				style.setName(s.trim());
			}
			if (currentElement.equalsIgnoreCase("aroma")){
				descrBuf = descrBuf + s;
			}
			if (currentElement.equalsIgnoreCase("abbr")){
				descrBuf = descrBuf + " " +  s.trim();				
			}
			if (currentElement.equalsIgnoreCase("appearance")){
				descrBuf = descrBuf + s;
			}
			if (currentElement.equalsIgnoreCase("flavor")){
				descrBuf = descrBuf + s;
			}
			if (currentElement.equalsIgnoreCase("mouthfeel")){
				descrBuf = descrBuf + s;
			}
			if (currentElement.equalsIgnoreCase("impression")){
				descrBuf = descrBuf + s;
			}
			if (currentElement.equalsIgnoreCase("comments")){
				descrBuf = descrBuf + s;
			}
			if (currentElement.equalsIgnoreCase("ingredients")){
				descrBuf = descrBuf + s;
			}
			if (currentElement.equalsIgnoreCase("low")){
				if (currentList.equalsIgnoreCase("og"))
					style.ogLow = Double.parseDouble(s);
				if (currentList.equalsIgnoreCase("ibu"))
					style.ibuLow = Double.parseDouble(s);
				if (currentList.equalsIgnoreCase("srm"))
					style.lovLow = Double.parseDouble(s);
				if (currentList.equalsIgnoreCase("abv"))
					style.alcLow = Double.parseDouble(s);
			}
			if (currentElement.equalsIgnoreCase("high")){
				if (currentList.equalsIgnoreCase("og"))
					style.ogHigh = Double.parseDouble(s);
				if (currentList.equalsIgnoreCase("ibu"))
					style.ibuHigh = Double.parseDouble(s);
				if (currentList.equalsIgnoreCase("srm"))
					style.lovHigh = Double.parseDouble(s);
				if (currentList.equalsIgnoreCase("abv"))
					style.alcHigh = Double.parseDouble(s);
			}
			
		}
		
		if (!s.trim().equals("") && currentElement.equalsIgnoreCase("name") && 
				!newStyle ) {
			category = s.trim();
		}

	}

	public void ignorableWhitespace(char buf[], int offset, int len) throws SAXException {
		// nl();
		emit("IGNORABLE");
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
