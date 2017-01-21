/**
 *    Filename: XmlHandler.java
 *     Version: 0.9.0
 * Description: XML Handler
 *     License: GPLv2
 *        Date: 2017-01-14
 *
 * This class is the "content handler" for xml input.
 * Each start and end of an element, document, content, etc. is
 * captured by the event handlers, and we use them to "build" a
 * recipe.
 *
 * @author aavis
 *
 * Copyright (c) 2004-2012 Doug Edey
 * @author dougedey
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class XmlHandler extends DefaultHandler{
    private Recipe r = null;
    private Fermentable m = null;
    private Hop h = null;
    private Misc misc = null;
    private Note note = new Note();
    private Attributes currentAttributes = null;
    private FermentStep ferm = null;
    private Salt salt = null;
    private WaterProfile water = null;

    private String currentList = null; //current List name
    private String currentSubList = null;
    private String currentElement = null; // current element name
    private String importType = null; // the type of recipe we're importing
    private String descrBuf = ""; // buffer to hold long descriptions
    private String buffer = ""; // buffer

    // mash step stuff:
    private String type;
    private String method = "infusion";
    private double startTemp;
    private double endTemp;
    private int minutes;
    private int rampMin;
    private double weightLbs;

    public Recipe getRecipe() {
        r.setAllowRecalcs(true);
        return r;}

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

    // we don't do anything with the end of a document
    public void endDocument() throws SAXException {

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


        if (eName.equalsIgnoreCase("STRANGEBREWRECIPE")) {
            importType = "STRANGEBREW";
            r = new Recipe();
            r.setAllowRecalcs(false);
            emit("StrangeBrew recipe detected.");
        } else if (eName.equalsIgnoreCase("RECIPE")) {
            if (attrs != null) {
                for (int i = 0; i < attrs.getLength(); i++) {
                    String s = attrs.getLocalName(i); // Attr name
                    if ("".equalsIgnoreCase(s))
                        s = attrs.getQName(i);
                    if ((s.equalsIgnoreCase("generator") || s.equalsIgnoreCase("application"))
                            && "qbrew".equalsIgnoreCase(attrs.getValue(i))){
                        importType = "QBREW";
                        r = new Recipe();
                        r.setAllowRecalcs(false);
                        emit("QBrew recipe detected.");
                    }
                }
            }

        } else if (importType == "STRANGEBREW") {
            // call SB start element
            sbStartElement(eName);
        } else if (importType == "QBREW") {
            // call SB start element
            qbStartElement(eName);
        }

    }

    void qbStartElement(String eName){
        if (eName.equalsIgnoreCase("HOPS")) {
            currentList = "HOPS";
        }
        else if (eName.equalsIgnoreCase("GRAINS")) {
            currentList = "GRAINS";
        }
        else if (eName.equalsIgnoreCase("HOP")) {
            // new hop
            h = new Hop();
            for (int i = 0; i < currentAttributes.getLength(); i++) {
                String str = currentAttributes.getLocalName(i); // Attr name
                if ("".equalsIgnoreCase(str))
                    str = currentAttributes.getQName(i);
                if (str.equalsIgnoreCase("quantity"))
                    h.setAmountAndUnits(currentAttributes.getValue(i));
                else if (str.equalsIgnoreCase("time"))
                    h.setMinutes(Integer.parseInt(currentAttributes.getValue(i)));
                else if (str.equalsIgnoreCase("alpha"))
                    h.setAlpha(Double.parseDouble(currentAttributes.getValue(i)));
            }
        }
        else if (eName.equalsIgnoreCase("GRAIN")) {
            // new malt
            m = new Fermentable();
            for (int i = 0; i < currentAttributes.getLength(); i++) {
                String str = currentAttributes.getLocalName(i); // Attr name
                if ("".equalsIgnoreCase(str))
                    str = currentAttributes.getQName(i);
                if (str.equalsIgnoreCase("quantity"))
                    m.setAmountAndUnits(currentAttributes.getValue(i));
                else if (str.equalsIgnoreCase("color"))
                    m.setLov(Double.parseDouble(currentAttributes.getValue(i)));
                else if (str.equalsIgnoreCase("extract"))
                    m.setPppg(Double.parseDouble(currentAttributes.getValue(i)));
                else if (str.equalsIgnoreCase("ferments"))
                    m.ferments(Boolean.parseBoolean(currentAttributes.getValue(i)));
            }
        }
        // we have to do this here, because <batch> is an empty element
        else if (eName.equalsIgnoreCase("batch")){
            for (int i = 0; i < currentAttributes.getLength(); i++) {
                String str = currentAttributes.getLocalName(i); // Attr name
                if ("".equalsIgnoreCase(str))
                    str = currentAttributes.getQName(i);
                if (str.equalsIgnoreCase("quantity"))
                    r.setAmountAndUnits(currentAttributes.getValue(i));
            }
        }
        // TODO: handle new misc ingredient
    }

    /**
     * Start of an element handler when we know this is a StrangeBrew recipe
     *
     * @param eName
     */
    void sbStartElement(String eName) {
        if (eName.equalsIgnoreCase("DETAILS")) {
            currentList = "DETAILS";
        } else if (eName.equalsIgnoreCase("CARB")) {
            currentList = "CARB";
        } else if (eName.equalsIgnoreCase("FERMENTABLES")) {
            currentList = "FERMENTABLES";
        } else if (eName.equalsIgnoreCase("HOPS")) {
            currentList = "HOPS";
        } else if (eName.equalsIgnoreCase("MASH") && currentList.equals("")) {
            currentList = "MASH";
        } else if (eName.equalsIgnoreCase("MISC")) {
            currentList = "MISC";
        } else if (eName.equalsIgnoreCase("FERMENTATION_SCHEDULE")) {
            currentList = "FERMENTATION_SCHEDULE";
        } else if (eName.equalsIgnoreCase("WATER_PROFILE")) {
            currentList = "WATER_PROFILE";
        } else if (eName.equalsIgnoreCase("NOTES") && !currentList.equals("DETAILS")) {
            // two freaking elments named NOTES - make sure we're not looking at the
            // recipe notes in <DETAILS>
            currentList = "NOTES";
        } else if (currentList.equals("WATER_PROFILE") && eName.equalsIgnoreCase("SOURCE_WATER")) {
            currentSubList = "SOURCE_WATER";
            water = new WaterProfile();
        } else if (currentList.equals("WATER_PROFILE") && eName.equalsIgnoreCase("TARGET_WATER")) {
            currentSubList = "TARGET_WATER";
            water = new WaterProfile();
        } else if (currentList.equals("WATER_PROFILE") && eName.equalsIgnoreCase("SALTS")) {
            currentSubList = "SALTS";
        } else if (currentList.equals("WATER_PROFILE") && eName.equalsIgnoreCase("ACID")) {
            currentSubList = "ACID";
        } else if (currentList.equals("WATER_PROFILE") &&
                currentSubList.equals("SALTS") && eName.equalsIgnoreCase("SALT")) {
            salt = new Salt();
        } else if (eName.equalsIgnoreCase("ITEM")) { // this is an item in a
            // list
            if (currentList.equals("FERMENTABLES")) {
                m = new Fermentable();
            } else if (currentList.equals("HOPS")) {
                h = new Hop();
            } else if (currentList.equals("MISC")) {
                misc = new Misc();
            } else if (currentList.equals("FERMENTATION_SCHEDUAL")) {
                ferm = new FermentStep();
            } else if (currentList.equals("NOTES")) {
                note = new Note();
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
        if (importType == "STRANGEBREW") {
            if (qName.equalsIgnoreCase("ITEM")
                    && currentList.equalsIgnoreCase("FERMENTABLES")) {
                m.setDescription(descrBuf);
                descrBuf = "";
                r.addMalt(m);
                m = null;
            } else if (qName.equalsIgnoreCase("MALT")) {
                m.setName(descrBuf);
                descrBuf = "";
            } else if (qName.equalsIgnoreCase("HOP")) {
                h.setName(descrBuf);
                descrBuf = "";
            } else if (qName.equalsIgnoreCase("ITEM")
                    && currentList.equalsIgnoreCase("HOPS")) {
                h.setDescription(descrBuf);
                descrBuf = "";
                r.addHop(h);
                h = null;
            } else if (qName.equalsIgnoreCase("ITEM")
                    && currentList.equalsIgnoreCase("MISC")) {
                misc.setDescription(descrBuf);
                descrBuf = "";
                r.addMisc(misc);
                misc = null;

            } else if (qName.equalsIgnoreCase("ITEM")
                    && currentList.equalsIgnoreCase("NOTES")) {
                r.addNote(note);
                note = null;

            } else if (qName.equalsIgnoreCase("ITEM")
                        && currentList.equalsIgnoreCase("MASH")) {
                    r.mash.addStep(type, startTemp, endTemp, method, minutes, rampMin, weightLbs);
            } else if (qName.equalsIgnoreCase("ITEM") &&
                    currentList.equalsIgnoreCase("FERMENTATION_SCHEDUAL")) {
                r.addFermentStep(ferm);
            } else if (qName.equalsIgnoreCase("SALT")) {
                // look up the effects of this salt from the DB
                Salt dbSalt = Salt.getSaltByName(salt.getName());
                if (dbSalt != null) {
                    salt.setChemicalEffects(dbSalt.getChemicalEffects());
                    salt.setGramsPerTsp(dbSalt.getGramsPerTsp());
                }
                r.addSalt(salt);
                salt = null;
            } else if (qName.equalsIgnoreCase("SOURCE_WATER")) {
                currentSubList = "";
                r.setSourceWater(water);
                water = null;
            } else if (qName.equalsIgnoreCase("TARGET_WATER")) {
                currentSubList = "";
                r.setTargetWater(water);
                water = null;
            } else if (qName.equalsIgnoreCase("SALTS")) {
                currentSubList = "";
            } else if (qName.equalsIgnoreCase("ACID")) {
                currentSubList = "";
            } // there's a problem with having two elements named "NOTES" :
              else if (qName.equalsIgnoreCase("FERMENTABLS")
                    || qName.equalsIgnoreCase("HOPS")
                    || qName.equalsIgnoreCase("DETAILS")
                    || qName.equalsIgnoreCase("CARB")
                    || qName.equalsIgnoreCase("MISC")
                    || qName.equalsIgnoreCase("FERMENTATION_SCHEDUAL")
                    || qName.equalsIgnoreCase("WATER_PROFILE")
                    || (qName.equalsIgnoreCase("NOTES") && !currentList.equalsIgnoreCase("DETAILS"))
                    ) {
                currentList = "";
            }

              else if (qName.equalsIgnoreCase("NAME")
                        && currentList.equalsIgnoreCase("DETAILS")) {
                    r.setName(buffer);
                    buffer="";

            }
        }
        else if (importType == "QBREW"){
            if (qName.equalsIgnoreCase("GRAIN")){
                r.addMalt(m);
                m = null;
            }
            else if (qName.equalsIgnoreCase("HOP")){
                r.addHop(h);
                h = null;
            }
            else if (qName.equalsIgnoreCase("title")){
                r.setName(buffer);
                buffer = "";
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
            // SBWin uses gr instead of g, fix it:
            if (s.trim().equalsIgnoreCase("gr"))
                s="g";
            sbCharacters(s.trim());
        }
        else if (!s.trim().equals("") && importType == "QBREW") {
                qbCharacters(s.trim());
            }
    }

    void qbCharacters(String s){
        if (currentElement.equalsIgnoreCase("GRAIN")){
            m.setName(s);

        }
        else if (currentElement.equalsIgnoreCase("HOP")){
            h.setName(s);

        }
        else if (currentElement.equalsIgnoreCase("miscingredient")){
            r.setYeastName(s);
            // TODO: there is more data in the yeast record, and there may be other misc ingredients
            // figure out how to get the yeast, and parse the other data.
        }
        else if (currentElement.equalsIgnoreCase("title")){
            buffer = buffer + s;
            // r.setName(s);
        }
        else if (currentElement.equalsIgnoreCase("brewer")){
            r.setBrewer(s);
        }
        else if (currentElement.equalsIgnoreCase("style")){
            r.setStyle(s);
        }
    }

    void sbCharacters(String s){
        if (currentList.equals("FERMENTABLES")) {
            if (currentElement.equalsIgnoreCase("MALT")) {
                //m.setName(s);
                descrBuf = descrBuf + s;
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
            } else if (currentElement.equalsIgnoreCase("MASHED")) {
                m.setMashed(Boolean.valueOf(s).booleanValue());
            } else if (currentElement.equalsIgnoreCase("STEEPED")) {
                m.setSteep(Boolean.valueOf(s).booleanValue());
            } else if (currentElement.equalsIgnoreCase("DescrLookup") ||
                    currentElement.equalsIgnoreCase("description")) {
                descrBuf = descrBuf + s;
            }
        }
        else if (currentList.equalsIgnoreCase("HOPS")) {
            if (currentElement.equalsIgnoreCase("HOP")) {
                // h.setName(s);
                descrBuf = descrBuf + s;
            } else if (currentElement.equalsIgnoreCase("AMOUNT")) {
                h.setAmount(Double.parseDouble(s));
            } else if (currentElement.equalsIgnoreCase("ALPHA")) {
                h.setAlpha(Double.parseDouble(s));
            } else if (currentElement.equalsIgnoreCase("UNITS")) {
                h.setUnits(s);
            } else if (currentElement.equalsIgnoreCase("FORM")) {
                h.setType(s);
            } else if (currentElement.equalsIgnoreCase("COSTOZ")) {
                h.setCost( s );
            } else if (currentElement.equalsIgnoreCase("ADD")) {
                h.setAdd(s);
            } else if (currentElement.equalsIgnoreCase("DescrLookup")||
                    currentElement.equalsIgnoreCase("description")) {
                descrBuf = descrBuf + s;
            } else if (currentElement.equalsIgnoreCase("TIME")) {
                h.setMinutes(Integer.parseInt(s));
            }
        }

        else if (currentList.equalsIgnoreCase("MISC")) {
            if (currentElement.equalsIgnoreCase("NAME")) {
                misc.setName(s);
            } else if (currentElement.equalsIgnoreCase("AMOUNT")) {
                misc.setAmount(Double.parseDouble(s));
            } else if (currentElement.equalsIgnoreCase("UNITS")) {
                misc.setUnits(s);
            } else if (currentElement.equalsIgnoreCase("COMMENTS")) {
                misc.setComments(s);
            } else if (currentElement.equalsIgnoreCase("COST_PER_U")) {
                // misc.setCost( Double.parseDouble(s) );
            } else if (currentElement.equalsIgnoreCase("ADD")) {
                h.setAdd(s);
            } else if (currentElement.equalsIgnoreCase("DescrLookup")||
                    currentElement.equalsIgnoreCase("description")) {
                descrBuf = descrBuf + s;
            } else if (currentElement.equalsIgnoreCase("TIME")) {
                misc.setTime(Integer.parseInt(s));
            } else if (currentElement.equalsIgnoreCase("STAGE")) {
                misc.setStage(s);
            }
        }
        else if (currentList.equalsIgnoreCase("NOTES")) {
            if (currentElement.equalsIgnoreCase("DATE")) {
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                  try {
                     Date d = df.parse(s);
                     note.setDate(d);
                  }
                  catch(ParseException e) {
                     System.out.println("Unable to parse " + s);
                  }
            } else if (currentElement.equalsIgnoreCase("TYPE")) {
                note.setType(s);
            } else if (currentElement.equalsIgnoreCase("NOTE")) {
                note.setNote(s);
            }
        }
        else if (currentList.equalsIgnoreCase("MASH")) {
            if (currentElement.equalsIgnoreCase("TYPE")) {
                type = s;
            } else if (currentElement.equalsIgnoreCase("TEMP")) {
                startTemp = Double.parseDouble(s);
            } else if (currentElement.equalsIgnoreCase("METHOD")) {
                method = s;
            } else if (currentElement.equalsIgnoreCase("MIN")) {
                minutes = Integer.parseInt(s);
            } else if (currentElement.equalsIgnoreCase("END_TEMP")) {
                endTemp = Double.parseDouble(s);
            } else if (currentElement.equalsIgnoreCase("RAMP_MIN")) {
                rampMin = Integer.parseInt(s);
            } else if (currentElement.equalsIgnoreCase("WEIGHT_LBS")) {
                weightLbs = Double.parseDouble(s);

                // these are not part of item, but general mash settings:
            } else if (currentElement.equalsIgnoreCase("THICK_DECOCT_RATIO")) {
                r.mash.setDecoctRatio("thick",Double.parseDouble(s));
            } else if (currentElement.equalsIgnoreCase("THIN_DECOCT_RATIO")) {
                r.mash.setDecoctRatio("thin",Double.parseDouble(s));
            } else if (currentElement.equalsIgnoreCase("GRAIN_TEMP")) {
                r.mash.setGrainTemp(Double.parseDouble(s));
            } else if (currentElement.equalsIgnoreCase("MASH_TUNLOSS_TEMP")) {
                r.mash.setTunLoss(Double.parseDouble(s));
            } else if (currentElement.equalsIgnoreCase("BOIL_TEMP")) {
                r.mash.setBoilTemp(Double.parseDouble(s));
            } else if (currentElement.equalsIgnoreCase("MASH_RATIO")) {
                r.setMashRatio(Double.parseDouble(s));
            } else if (currentElement.equalsIgnoreCase("MASH_RATIO_U")) {
                r.setMashRatioU(s);
            }

        } else if (currentList.equalsIgnoreCase("CARB")) {
            if (currentElement.equalsIgnoreCase("BOTTLETEMP")) {
                r.setBottleTemp(Double.parseDouble(s));
            } else if (currentElement.equalsIgnoreCase("SERVTEMP")) {
                r.setServTemp(Double.parseDouble(s));
            } else if (currentElement.equalsIgnoreCase("VOL")) {
                r.setTargetVol(Double.parseDouble(s));
            } else if (currentElement.equalsIgnoreCase("SUGAR")) {
                r.setPrimeSugarName(s);
            } else if (currentElement.equalsIgnoreCase("SUGARU")) {
                r.setPrimeSugarU(s);
            } else if (currentElement.equalsIgnoreCase("TEMPU")) {
                r.setCarbTempU(s);
            } else if (currentElement.equalsIgnoreCase("KEG")) {
                r.setKegged(Boolean.valueOf(s).booleanValue());
            }
        } else if (currentList.equalsIgnoreCase("DETAILS")) {
            if (currentElement.equalsIgnoreCase("NAME")) {
                // r.setName(s);
                buffer = buffer + s;
            } else if (currentElement.equalsIgnoreCase("EFFICIENCY")) {
                r.setEfficiency(Double.parseDouble(s));
            } else if (currentElement.equalsIgnoreCase("ATTENUATION")) {
                r.setAttenuation(Double.parseDouble(s));
            } else if (currentElement.equalsIgnoreCase("PRESIZE")) {
                r.setPreBoil(new Quantity(r.getVolUnits(), Double.parseDouble(s)));
            } else if (currentElement.equalsIgnoreCase("SIZE")) {
                r.setPostBoil(new Quantity(r.getVolUnits(), Double.parseDouble(s)));
            } else if (currentElement.equalsIgnoreCase("SIZE_UNITS")) {
                // also sets postboil:
                r.setReadVolUnits(s);
//          } else if (currentElement.equalsIgnoreCase("ADDED_VOLUME")) {
//              double d = Double.parseDouble(s);
//              if ( d != 0 ) {
//                  DilutedRecipe dr = new DilutedRecipe(r, new Quantity(r.getVolUnits(), d));
//                  r = dr;
//              }
            } else if (currentElement.equalsIgnoreCase("STYLE")) {
                r.setStyle(s);
            } else if (currentElement.equalsIgnoreCase("BOIL_TIME")) {
                r.setBoilMinutes(Integer.parseInt(s));
            } else if (currentElement.equalsIgnoreCase("HOPS_UNITS")) {
                r.setHopsUnits(s);
            } else if (currentElement.equalsIgnoreCase("MALT_UNITS")) {
                r.setMaltUnits(s);
            }
            // leave this clause here to support older versions.  SB2.x puts this
            // data in the mash section
            else if (currentElement.equalsIgnoreCase("MASH_RATIO")) {
                r.setMashRatio(Double.parseDouble(s));
            } else if (currentElement.equalsIgnoreCase("MASH_RATIO_U")) {
                r.setMashRatioU(s);
            } else if (currentElement.equalsIgnoreCase("BREWER")) {
                r.setBrewer(s);
            } else if (currentElement.equalsIgnoreCase("MASH")) {
                r.setMashed(Boolean.valueOf(s).booleanValue());
            } else if (currentElement.equalsIgnoreCase("YEAST")) {
                r.setYeastName(s);
            } else if (currentElement.equalsIgnoreCase("RECIPE_DATE")) {
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                  try {
                     Date d = df.parse(s);
                     r.setCreated(d);
                  }
                  catch(ParseException e) {
                     System.out.println("Unable to parse " + s);
                  }

            // These are SBJ1.0 Extensions:
            } else if (currentElement.equalsIgnoreCase("ALC_METHOD")) {
                r.setAlcMethod(s);
            } else if (currentElement.equalsIgnoreCase("IBU_METHOD")) {
                r.setIBUMethod(s);
            } else if (currentElement.equalsIgnoreCase("COLOUR_METHOD")) {
                r.setColourMethod(s);
            } else if (currentElement.equalsIgnoreCase("EVAP")) {
                if(currentAttributes.getLength() > 0) {
                    int i = 0;
                    while(i < currentAttributes.getLength()) {
                        String attrName = currentAttributes.getQName(i);
                        Debug.print("Attr name: " +attrName);
                        String attrValue = currentAttributes.getValue(i);
                        if(attrName.equalsIgnoreCase("type")) {
                            if(attrValue.equalsIgnoreCase("6")) {
                                r.setEvapMethod("Percent");
                            } else {
                                r.setEvapMethod("Constant");
                            }
                        }
                        i++;
                    }
                }

                r.setEvap(Double.parseDouble(s));
            } else if (currentElement.equalsIgnoreCase("EVAP_METHOD")) {
                r.setEvapMethod(s);
            }
            else if (currentElement.equalsIgnoreCase("KETTLE_LOSS")) {
                r.setKettleLoss(new Quantity(r.getVolUnits(), Double.parseDouble(s)));
            }
            else if (currentElement.equalsIgnoreCase("MISC_LOSS")) {
                r.setMiscLoss(new Quantity(r.getVolUnits(), Double.parseDouble(s)));
            }
            else if (currentElement.equalsIgnoreCase("TRUB_LOSS")) {
                r.setTrubLoss(new Quantity(r.getVolUnits(), Double.parseDouble(s)));
            }
            else if (currentElement.equalsIgnoreCase("PELLET_HOP_PCT")) {
                r.setPelletHopPct(Double.parseDouble(s));
            }
            else if (currentElement.equalsIgnoreCase("YEAST_COST")) {
                r.getYeastObj().setCost((s));
            }
            else if (currentElement.equalsIgnoreCase("OTHER_COST")) {
                r.setOtherCost(Double.parseDouble(s));
            }

        } else if (currentList.equalsIgnoreCase("FERMENTATION_SCHEDUAL")) {
            if (currentElement.equalsIgnoreCase("TYPE")) {
                ferm.setType(s);
            } else if (currentElement.equalsIgnoreCase("TEMP")) {
                ferm.setTemp(Double.parseDouble(s));
            } else if (currentElement.equalsIgnoreCase("TEMPU")) {
                ferm.setTempU(s);
            } else if (currentElement.equalsIgnoreCase("TIME")) {
                ferm.setTime(Integer.parseInt(s));
            }
        } else if (currentList.equalsIgnoreCase("WATER_PROFILE")) {
            if (currentSubList.equalsIgnoreCase("ACID")) {
                if (currentElement.equalsIgnoreCase("NAME")) {
                    r.setAcid(Acid.getAcidByName(s));
                }
            } else if (currentSubList.equalsIgnoreCase("SALTS")) {
                // Read in a salt
                if (currentElement.equalsIgnoreCase("NAME")) {
                    salt.setName(s);
                } else if (currentElement.equalsIgnoreCase("COMMONNAME")) {
                    salt.setCommonName(s);
                } else if (currentElement.equalsIgnoreCase("CHEM")) {
                    salt.setChemicalName(s);
                } else if (currentElement.equalsIgnoreCase("AMOUNT")) {
                    salt.setAmount(Double.parseDouble(s));
                }
                // TODO units!!!!
            } else {
                // Source or Target water
                if (currentElement.equalsIgnoreCase("NAME")) {
                    water.setName(s);
                } else if (currentElement.equalsIgnoreCase("CA")) {
                    water.setCa(Double.parseDouble(s));
                } else if (currentElement.equalsIgnoreCase("MG")) {
                    water.setMg(Double.parseDouble(s));
                } else if (currentElement.equalsIgnoreCase("NA")) {
                    water.setNa(Double.parseDouble(s));
                } else if (currentElement.equalsIgnoreCase("SO4")) {
                    water.setSo4(Double.parseDouble(s));
                } else if (currentElement.equalsIgnoreCase("HCO3")) {
                    water.setHco3(Double.parseDouble(s));
                } else if (currentElement.equalsIgnoreCase("CL")) {
                    water.setCl(Double.parseDouble(s));
                } else if (currentElement.equalsIgnoreCase("HARDNESS")) {
                    water.setHardness(Double.parseDouble(s));
                } else if (currentElement.equalsIgnoreCase("TDS")) {
                    water.setTds(Double.parseDouble(s));
                } else if (currentElement.equalsIgnoreCase("PH")) {
                    water.setPh(Double.parseDouble(s));
                } else if (currentElement.equalsIgnoreCase("ALKALINITY")) {
                    water.setAlkalinity(Double.parseDouble(s));
                }
            }
        }
        else
            s = "";
    }

    public void ignorableWhitespace(char buf[], int offset, int len)
            throws SAXException {

    }

    public void processingInstruction(String target, String data)
            throws SAXException {

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



}
