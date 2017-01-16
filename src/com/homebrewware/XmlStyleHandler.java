/**
 *    Filename: XmlStyleHandler.java
 *     Version: 0.9.0
 * Description: XML Style Handler
 *     License: GPLv2
 *        Date: 2017-01-14
 *
 * @author aavis
 *
 * Copyright (c) 2004-2008 jimcdiver
 * @author jimcdiver
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

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class XmlStyleHandler extends DefaultHandler {
    private Style style = null;
    private ArrayList<Style> styles = new ArrayList<Style>();

    // private Attributes currentAttributes = null;
    private String currentList = null; //current List name
    private String currentElement = null; // current element name
    private String descrBuf = ""; // buffer to hold long descriptions
    private String type = ""; // beer class
    private boolean flexible = false; // flexible attr

    private boolean newStyle = false;

    private String category = "";

    public ArrayList<Style> getStyles() {
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

    public void startDocument() throws SAXException {
        // let's add one blank style to the style list, so when a new, empty
        // recipe is viewed, the top of the list is seen first
        // causes a problem w/ combomodel, style update:
        style = new Style();
        styles.add(style);
        style = null;
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
        // currentAttributes = attrs;

        if (eName.equalsIgnoreCase("class")){
            if (attrs != null) {
                for (int i = 0; i < attrs.getLength(); i++) {
                    String s = attrs.getLocalName(i); // Attr name
                    if ("".equalsIgnoreCase(s))
                        s = attrs.getQName(i);
                    if (s.equalsIgnoreCase("type")) {
                        type = attrs.getValue(i);
                    }
                }
            }
        }

        if (eName.equalsIgnoreCase("og") ||
                eName.equalsIgnoreCase("fg") ||
                eName.equalsIgnoreCase("ibu") ||
                eName.equalsIgnoreCase("srm") ||
                eName.equalsIgnoreCase("abv")) {
            currentList = eName;
            if (attrs != null) {
                for (int i = 0; i < attrs.getLength(); i++) {
                    String s = attrs.getLocalName(i); // Attr name
                    if ("".equalsIgnoreCase(s))
                        s = attrs.getQName(i);
                    if (s.equalsIgnoreCase("flexible")) {
                        flexible = new Boolean(attrs.getValue(i)).booleanValue();
                    }
                }
                if (eName.equalsIgnoreCase("og"))
                    style.ogFlexible = flexible;
                if (eName.equalsIgnoreCase("fg"))
                    style.fgFlexible = flexible;
                if (eName.equalsIgnoreCase("ibu"))
                    style.ibuFlexible = flexible;
                if (eName.equalsIgnoreCase("srm"))
                    style.srmFlexible = flexible;
                if (eName.equalsIgnoreCase("abv"))
                    style.alcFlexible = flexible;

            }
        }



        if (eName.equalsIgnoreCase("subcategory") ) {
            style = new Style();
            style.setCategory(category);
            style.type = type;
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
            // only add if it's beer class for now:
            if (type.equalsIgnoreCase("beer"))
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
        if (qName.equalsIgnoreCase("examples")){
            style.examples = descrBuf;
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
            if (currentElement.equalsIgnoreCase("abbr")){
                descrBuf = descrBuf + " " +  s.trim();
            }
            if (currentElement.equalsIgnoreCase("aroma") ||
                    currentElement.equalsIgnoreCase("appearance") ||
                    currentElement.equalsIgnoreCase("flavor") ||
                    currentElement.equalsIgnoreCase("mouthfeel") ||
                    currentElement.equalsIgnoreCase("impression") ||
                    currentElement.equalsIgnoreCase("comments") ||
                    currentElement.equalsIgnoreCase("ingredients") ||
                    currentElement.equalsIgnoreCase("examples") ){
                descrBuf = descrBuf + s;
            }


            if (currentElement.equalsIgnoreCase("low")){
                if (currentList.equalsIgnoreCase("og"))
                    style.ogLow = Double.parseDouble(s);
                if (currentList.equalsIgnoreCase("ibu"))
                    style.ibuLow = Double.parseDouble(s);
                if (currentList.equalsIgnoreCase("srm"))
                    style.srmLow = Double.parseDouble(s);
                if (currentList.equalsIgnoreCase("abv"))
                    style.alcLow = Double.parseDouble(s);
            }
            if (currentElement.equalsIgnoreCase("high")){
                if (currentList.equalsIgnoreCase("og"))
                    style.ogHigh = Double.parseDouble(s);
                if (currentList.equalsIgnoreCase("ibu"))
                    style.ibuHigh = Double.parseDouble(s);
                if (currentList.equalsIgnoreCase("srm"))
                    style.srmHigh = Double.parseDouble(s);
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



}
