/*
 * Created on Oct 14, 2004
 *
 * This class is the "content handler" for xml input.
 * Each start and end of an element, document, content, etc. is
 * captured by the event handlers, and we use them to "build" a
 * recipe.
 */
package strangebrew;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.*;

/**
 * @author aavis
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class XmlHandler extends DefaultHandler{
	private Recipe r = null;
	private Malt m = null; 
	private Hop h = null;

	private String currentList = null; //current List name
	private String currentElement = null; // current element name
	private String importType = null; // the type of recipe we're importing

	public Recipe getRecipe() {return r;}
	
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
		emit(r.toXML());
		r.testRecipe();
		nl();
		System.out.flush();
	}

	/**
	 * This method is called every time we encounter a new element
	 * TODO: To handle other xml import types, we should check only for 
	 * a "signpost" element that indicates the file type, then
	 * call out to the appropriate element handler for that file type.
	 * Right now we only look for STRANGEBREWRECIPE.
	 */
	public void startElement(String namespaceURI, String lName, // local name
			String qName, // qualified name
			Attributes attrs) throws SAXException {
		String eName = lName; // element name

		if ("".equals(eName))
			eName = qName; // namespaceAware = false
		
		currentElement = eName;

		if (eName.equalsIgnoreCase("STRANGEBREWRECIPE")) {
			importType = "STRANGEBREW";
			r = new Recipe();
			emit("StrangeBrew recipe detected.");
		} else if (eName.equalsIgnoreCase("RECIPE")) {
			if (attrs != null) {
				for (int i = 0; i < attrs.getLength(); i++) {
					String s = attrs.getLocalName(i); // Attr name
					if ("".equalsIgnoreCase(s))
						s = attrs.getQName(i);
					if (s.equalsIgnoreCase("generator")&& 
							"qbrew".equalsIgnoreCase(attrs.getValue(i))){
						importType = "QBREW";
						r = new Recipe();
						emit("QBrew recipe detected.");
					}						
				}
			}

		} else if (importType == "STRANGEBREW") {
			// call SB start element
			sbStartElement(eName);
		}

	}
	
	/**
	 * Start of an element handler when we know this is a StrangeBrew recipe
	 * 
	 * @param eName
	 */
	void sbStartElement(String eName) {
		if (eName.equalsIgnoreCase("DETAILS")) {
			currentList = "DETAILS";
		} else if (eName.equalsIgnoreCase("FERMENTABLES")) {
			currentList = "FERMENTABLES";
		} else if (eName.equalsIgnoreCase("HOPS")) {
			currentList = "HOPS";
		} else if (eName.equalsIgnoreCase("MASH") && currentList == null) {
			currentList = "MASH";
		} else if (eName.equalsIgnoreCase("MISC")) {
			currentList = "MISC";
		} else if (eName.equalsIgnoreCase("ITEM")) { // this is an item in a
			// list
			if (currentList.equals("FERMENTABLES")) {
				m = new Malt();
			} else if (currentList.equals("HOPS")) {
				h = new Hop();
			} else if (currentList.equals("MISC")) {
				// TODO: new misc object
			}

		} 
	}

	/**
	 * At the end of each element, we should check if we're looking at a list.
	 * If we are, set the list to null. This way, we can tell if we're looking
	 * at an element that has (stupidly) the same name as a list... eg <MASH>is
	 * in the recipe (indicating whether it's mashed or not), and the name of
	 * the mash list!
	 */
	public void endElement(String namespaceURI, String sName, // simple name
			String qName // qualified name
	) throws SAXException {
		if (importType == "STRANGEBREW") {
			if (qName.equalsIgnoreCase("ITEM")
					&& currentList.equalsIgnoreCase("FERMENTABLES")) {
				r.addMalt(m);
				m = null;
			} else if (qName.equalsIgnoreCase("ITEM")
					&& currentList.equalsIgnoreCase("HOPS")) {
				r.addHop(h);
				h = null;
			} else if (qName.equalsIgnoreCase("FERMENTABLS")
					|| qName.equalsIgnoreCase("HOPS")
					|| qName.equalsIgnoreCase("DETAILS")) {
				currentList = null;
			}
		}
	}

	/**
	 * This is the "meat" of the handler.  We figure out where in the
	 * document we are, based on the current list, and then start puting
	 * the data into the recipe fields.
	 */
	public void characters(char buf[], int offset, int len) throws SAXException {
		String s = new String(buf, offset, len);

		if (!s.trim().equals("") && importType == "STRANGEBREW") {
			sbCharacters(s.trim());	
		}
	}
	
	void sbCharacters(String s){
		if (currentList.equals("FERMENTABLES")) {
			if (currentElement.equalsIgnoreCase("MALT")) {
				m.setName(s);
			} else if (currentElement.equalsIgnoreCase("AMOUNT")) {
				m.setAmount(Double.parseDouble(s));
			} else if (currentElement.equalsIgnoreCase("POINTS")) {
				m.setPppg(Double.parseDouble(s));
			} else if (currentElement.equalsIgnoreCase("COSTLB")) {
				m.setCost( s );
			} else if (currentElement.equalsIgnoreCase("UNITS")) {
				m.setUnits(s);
			} else if (currentElement.equalsIgnoreCase("LOV")) {
				m.setLov(Double.parseDouble(s));
			} else if (currentElement.equalsIgnoreCase("DescrLookup")) {
				m.setDesc(s);
			}
		}
		else if (currentList.equalsIgnoreCase("HOPS")) {
			if (currentElement.equalsIgnoreCase("HOP")) {
				h.setName(s);
			} else if (currentElement.equalsIgnoreCase("AMOUNT")) {
				h.setAmount(Double.parseDouble(s));
			} else if (currentElement.equalsIgnoreCase("ALPHA")) {
				h.setAlpha(Double.parseDouble(s));
			} else if (currentElement.equalsIgnoreCase("UNITS")) {
				h.setUnits(s);
			} else if (currentElement.equalsIgnoreCase("FORM")) {
				h.setForm(s);
			} else if (currentElement.equalsIgnoreCase("COSTOZ")) {
				// h.setCost( Double.parseDouble(s) );
			} else if (currentElement.equalsIgnoreCase("ADD")) {
				h.setAdd(s);
			} else if (currentElement.equalsIgnoreCase("DescrLookup")) {
				h.setDesc(s);
			} else if (currentElement.equalsIgnoreCase("TIME")) {
				h.setMinutes(Integer.parseInt(s));
			}
		}

		else if (currentList.equalsIgnoreCase("DETAILS")) {
			if (currentElement.equalsIgnoreCase("NAME")) {
				r.setName(s);
			} else if (currentElement.equalsIgnoreCase("EFFICIENCY")) {
				r.setEfficiency(Double.parseDouble(s));
			} else if (currentElement.equalsIgnoreCase("ATTENUATION")) {
				r.setAttenuation(Double.parseDouble(s));
			} else if (currentElement.equalsIgnoreCase("PRESIZE")) {
				r.setPreBoil(Double.parseDouble(s));
			} else if (currentElement.equalsIgnoreCase("SIZE")) {
				r.setPostBoil(Double.parseDouble(s));
			} else if (currentElement.equalsIgnoreCase("SIZE_UNITS")) {
				r.setVolUnits(s);
			} else if (currentElement.equalsIgnoreCase("STYLE")) {
				r.setStyle(s);
			} else if (currentElement.equalsIgnoreCase("BOIL_TIME")) {
				r.setBoilMinutes(Integer.parseInt(s));
			} else if (currentElement.equalsIgnoreCase("HOPS_UNITS")) {
				r.setHopsUnits(s);
			} else if (currentElement.equalsIgnoreCase("MALT_UNITS")) {
				r.setMaltUnits(s);
			} else if (currentElement.equalsIgnoreCase("MASH_RATIO")) {
				r.setMashRatio(Double.parseDouble(s));
			} else if (currentElement.equalsIgnoreCase("MASH_RATIO_U")) {
				r.setMashRatioU(s);
			} else if (currentElement.equalsIgnoreCase("BREWER")) {
				r.setBrewer(s);
			}

		}
	}

	public void ignorableWhitespace(char buf[], int offset, int len)
			throws SAXException {
		nl();
		emit("IGNORABLE");
	}

	public void processingInstruction(String target, String data)
			throws SAXException {
		nl();
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
		System.out.println("** Warning" + ", line " + err.getLineNumber()
				+ ", uri " + err.getSystemId());
		System.out.println("   " + err.getMessage());
	}

	//===========================================================
	// Utility Methods ...
	//===========================================================

	// Wrap I/O exceptions in SAX exceptions, to
	// suit handler signature requirements
	private void emit(String s) throws SAXException {
			System.out.print(s);
			System.out.flush();
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
