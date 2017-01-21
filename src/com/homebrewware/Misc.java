/**
 *    Filename: Misc.java
 *     Version: 0.9.0
 * Description: Miscellaneous
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

public class Misc extends Ingredient {

    private String comments;
    private String stage;
    private int time;
    static final public String MASH = "Mash";
    static final public String BOIL = "Boil";
    static final public String PRIMARY = "Primary";
    static final public String SECONDARY = "Secondary";
    static final public String BOTTLE = "Bottle";
    static final public String KEG = "Keg";
    static final public String[] stages = {MASH, BOIL, PRIMARY, SECONDARY, BOTTLE, KEG};

    // default constructor
    public  Misc() {
        setName("");
        setCost(0);
        setDescription("");
        setUnits(Quantity.G);
    }

    // get methods
    public String getComments(){ return comments; }
    public String getStage(){ return stage; }
    public int getTime(){ return time; }

    // set methods
    public void setComments(String c){ comments = c; }
    public void setStage(String s){ stage = s; }
    public void setTime(int t){ time = t; }

    public String toXML(){
        StringBuffer sb = new StringBuffer();
        sb.append( "    <ITEM>\n" );
        sb.append( "      <NAME>"+getName()+"</NAME>\n" );
        sb.append( "      <AMOUNT>"+getAmountAs(getUnits())+"</AMOUNT>\n" );
        sb.append( "      <UNITS>"+getUnits()+"</UNITS>\n" );
        sb.append( "      <STAGE>"+stage+"</STAGE>\n" );
        sb.append( "      <TIME>"+time+"</TIME>\n" );
        sb.append( "      <COMMENTS>"+StringUtils.subEntities(comments)+"</COMMENTS>\n" );
        sb.append( "    </ITEM>\n" );
        return sb.toString();
    }
}
