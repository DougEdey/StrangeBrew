/**
 *    Filename: Style.java
 *     Version: 0.9.0
 * Description: Style
 *     License: GPLv2
 *        Date: 2017-01-14
 *
 * Copyright (c) 2004 Drew Avis
 * @author aavis
 *
 * Copyright (c) 2008 jimcdiver
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

public class Style implements Comparable<Style> {

    public String name = "";
    public String category;
    public String catNum;
    public double ogLow;
    public double ogHigh;
    public boolean ogFlexible;
    public double fgLow;
    public double fgHigh;
    public boolean fgFlexible;
    public double alcLow;
    public double alcHigh;
    public boolean alcFlexible;
    public double ibuLow;
    public double ibuHigh;
    public boolean ibuFlexible;
    public double srmLow;
    public double srmHigh;
    public boolean srmFlexible;
    public String examples;
    // public String description;

    public String aroma;
    public String appearance;
    public String flavour;
    public String mouthfeel;
    public String impression;
    public String comments;
    public String ingredients;
    public String year;
    public String type;

    // override the equals so we can compare:
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        /* is obj reference null */
        if (obj == null)
            return false;

        /* Make sure references are of same type */
        if (!(getClass() == obj.getClass()))
            return false;

        else {
            Style tmp = (Style) obj;

            if (tmp.name.equalsIgnoreCase(this.name)) {
                return true;
            } else
                return false;
        }
    }

    // get methods:
    public String getName() {
        if (name != null)
            return name;
        else
            return "";
    }

    public String getDescription() {
        String s = "";
        if (impression != null)
            s = "<b>Impression:</b> " + impression + "\n";
        if (comments != null)
            s = s + "<b>Comments:</b> " + comments;

        return s;
    }

    /**
     * @return Returns the alcHigh.
     */
    public double getAlcHigh() {
        return alcHigh;
    }
    /**
     * @return Returns the alcLow.
     */
    public double getAlcLow() {
        return alcLow;
    }
    /**
     * @return Returns the category.
     */
    public String getCategory() {
        return category;
    }
    /**
     * @return Returns the catNum.
     */
    public String getCatNum() {
        return catNum;
    }
    /**
     * @return Returns the examples.
     */
    public String getExamples() {
        return examples;
    }

    /**
     * @return Returns the ibuHigh.
     */
    public double getIbuHigh() {
        return ibuHigh;
    }
    /**
     * @return Returns the ibuLow.
     */
    public double getIbuLow() {
        return ibuLow;
    }
    /**
     * @return Returns the srmHigh.
     */
    public double getSrmHigh() {
        return srmHigh;
    }
    /**
     * @return Returns the srmLow.
     */
    public double getSrmLow() {
        return srmLow;
    }
    /**
     * @return Returns the ogHigh.
     */
    public double getOgHigh() {
        return ogHigh;
    }
    /**
     * @return Returns the ogLow.
     */
    public double getOgLow() {
        return ogLow;
    }

    public String getYear() {
        return year;
    }
    // set methods:
    public void setName(String n) {
        name = n;
    }

    /**
     * @param alcHigh The alcHigh to set.
     */
    public void setAlcHigh(double alcHigh) {
        this.alcHigh = alcHigh;
    }
    /**
     * @param alcLow The alcLow to set.
     */
    public void setAlcLow(double alcLow) {
        this.alcLow = alcLow;
    }
    /**
     * @param category The category to set.
     */
    public void setCategory(String category) {
        this.category = category;
    }
    /**
     * @param catNum The catNum to set.
     */
    public void setCatNum(String catNum) {
        this.catNum = catNum;
    }
    /**
     * @param examples The examples to set.
     */
    public void setExamples(String commercialEx) {
        this.examples = commercialEx;
    }
    /**
     * @param ibuHigh The ibuHigh to set.
     */
    public void setIbuHigh(double ibuHigh) {
        this.ibuHigh = ibuHigh;
    }
    /**
     * @param ibuLow The ibuLow to set.
     */
    public void setIbuLow(double ibuLow) {
        this.ibuLow = ibuLow;
    }
    /**
     * @param srmHigh The srmHigh to set.
     */
    public void setSrmHigh(double lovHigh) {
        this.srmHigh = lovHigh;
    }
    /**
     * @param srmLow The srmLow to set.
     */
    public void setSrmLow(double lovLow) {
        this.srmLow = lovLow;
    }
    /**
     * @param ogHigh The ogHigh to set.
     */
    public void setOgHigh(double ogHigh) {
        this.ogHigh = ogHigh;
    }
    /**
     * @param ogLow The ogLow to set.
     */
    public void setOgLow(double ogLow) {
        this.ogLow = ogLow;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String toXML() {
        StringBuffer sb = new StringBuffer();
        int indent = 0;
        sb.append(" <style>\n");
        // this is the BJCP style dtd:
        sb.append("  <subcategory id=\"" + catNum + "\">");
        indent = 4;
        sb.append(StringUtils.xmlElement("name", name, indent));
        sb.append(StringUtils.xmlElement("aroma", aroma, indent));
        sb.append(StringUtils.xmlElement("appearance", appearance, indent));
        sb.append(StringUtils.xmlElement("flavor", flavour, indent));
        sb.append(StringUtils.xmlElement("mouthfeel", mouthfeel, indent));
        sb.append(StringUtils.xmlElement("impression", impression, indent));
        sb.append(StringUtils.xmlElement("comments", comments, indent));
        sb.append(StringUtils.xmlElement("ingredients", ingredients, indent));
        sb.append(" <stats>\n");
        sb.append("  <og flexible=\"" + ogFlexible + "\">\n");
        indent = 6;
        sb.append(StringUtils.xmlElement("low", "" + ogLow, indent));
        sb.append(StringUtils.xmlElement("high", "" + ogHigh, indent));
        sb.append("  </og>\n");
        sb.append("  <fg flexible=\"" + fgFlexible + "\">\n");
        sb.append(StringUtils.xmlElement("low", "" + fgLow, indent));
        sb.append(StringUtils.xmlElement("high", "" + fgHigh, indent));
        sb.append("  </fg>\n");
        sb.append("  <ibu flexible=\"" + ibuFlexible + "\">\n");
        sb.append(StringUtils.xmlElement("low", "" + ibuLow, indent));
        sb.append(StringUtils.xmlElement("high", "" + ibuHigh, indent));
        sb.append("  </ibu>\n");
        sb.append("  <srm flexible=\"" + srmFlexible + "\">\n");
        sb.append(StringUtils.xmlElement("low", "" + srmLow, indent));
        sb.append(StringUtils.xmlElement("high", "" + srmHigh, indent));
        sb.append("  </srm>\n");
        sb.append("  <abv flexible=\"" + alcFlexible + "\">\n");
        sb.append(StringUtils.xmlElement("low", "" + alcLow, indent));
        sb.append(StringUtils.xmlElement("high", "" + alcHigh, indent));
        sb.append("  </abv>\n");
        sb.append("</stats>\n");
        indent = 4;
        sb.append(StringUtils.xmlElement("examples", examples, indent));
        sb.append("  </subcategory>\n");
        sb.append(" </style>\n");
        return sb.toString();
    }

    public String toText() {
        StringBuffer sb = new StringBuffer();
        sb.append("Name: " + catNum + ":" + getName() + "\n");
        sb.append("Category: " + category + "\n");
        sb.append("Class: " + type + "\n");
        sb.append("OG: " + ogLow + "-" + ogHigh + "\n");
        sb.append("IBU: " + ibuLow + "-" + ibuHigh + "\n");
        sb.append("SRM: " + srmLow + "-" + srmHigh + "\n");
        sb.append("Alc: " + alcLow + "-" + alcHigh + "\n");
        sb.append("Aroma: " + aroma + "\n");
        sb.append("Appearance: " + appearance + "\n");
        sb.append("Flavour: " + flavour + "\n");
        sb.append("Mouthfeel: " + mouthfeel + "\n");
        sb.append("Impression: " + impression + "\n");
        sb.append("Comments: " + comments + "\n");
        sb.append("Ingredients: " + ingredients + "\n");
        sb.append("Examples: " + examples + "\n");
        return sb.toString();
    }

    public String toString() {
        return getName();
    }

    public int compareTo(Style s) {
        int result = this.getName().compareTo(s.getName());
        return (result == 0 ? -1 : result);
    }

    /*********
     * Set the style as complete so any values can be switched
     */
    public void setComplete() {
        double temp = 0.0;

        // Check the IBU
        if (ibuHigh < ibuLow) {
            temp = ibuHigh;
            ibuHigh = ibuLow;
            ibuLow = temp;
        }

        // check the SRM
        if (srmHigh < srmLow) {
            temp = srmHigh;
            srmHigh = srmLow;
            srmLow = temp;
        }

        // check the OG
        if (ogHigh < ogLow) {
            temp = ogHigh;
            ogHigh = ogLow;
            ogLow = temp;
        }

        // check the ALC
        if (alcHigh < alcLow) {
            temp = alcHigh;
            alcHigh = alcLow;
            alcLow = temp;
        }

    }


}
