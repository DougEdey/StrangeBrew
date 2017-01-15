/**
 *    Filename: Fermentable.java
 *     Version: 0.9.0
 * Description: Fermentable
 *     License: GPLv2
 *        Date: 2017-01-14
 *
 * Copyright (c) 2004 Drew Avis
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

import java.util.Comparator;

public class Fermentable extends Ingredient  {

	// base data
	private double pppg;
	private double lov;
	private boolean mashed;
	private boolean steeped;
	private boolean fermentable = true;
	private double percent;
	private boolean prime;

	final static private double basePppg = 1.047;

	// constructors:
	public Fermentable(String n, double p, double l, double a, String u) {
		setName(n);
		pppg = p;
		lov = l;
		setAmount(a);
		setUnits(u);
		mashed = true;
		prime = false;
	}

	public Fermentable(Fermentable f) {
		setName(f.getName());
		pppg = f.getPppg();
		lov = f.getLov();
		setAmount(f.getAmountAs(f.getUnits()));
		setUnits(f.getUnits());
		mashed = f.getMashed();
		prime = f.getPrime();
		percent = f.getPercent();
	}

	public Fermentable(String u) {
		setName("");
		pppg = 1.000;
		setUnits(u);
	}

	public Fermentable(){
		// default constructor
		setName("");
		pppg = 0;
		lov = 0;
		setAmount(1.0);
		setUnits(Options.getInstance().getProperty("optMaltU"));
		mashed = true;
		prime = false;
	}

	static public double getBasePppg() {
		return basePppg;
	}

	// getter methods:
	public double getLov(){ return lov; }
	public boolean getMashed(){ return mashed; }
	public double getPercent() { return percent; }
	public double getPppg(){ return pppg; }
	public boolean getSteep(){return steeped; }
	public boolean getPrime() { return prime; }
	public boolean ferments() { return fermentable; }

	// setter methods:
	public void setLov(double l){

		lov = l;
	}
	public void setMashed(boolean m){ mashed = m; }
	public void setPercent(double p){ percent = p; }
	public void setPppg(double p){ pppg = p; }
	public void setSteep(boolean s){ steeped = s; }
	public void setPrime(boolean b) { prime = b; }
	public void ferments(boolean f) { fermentable = f; }

	// Need to add the spaces and type attributes to make this
	// backwards-compatible with SB1.8:
	public String toXML(){
	    StringBuffer sb = new StringBuffer();
	    sb.append( "    <ITEM>\n" );
	    sb.append( "      <MALT>"+getName()+"</MALT>\n" );
	    sb.append( "      <AMOUNT>"+getAmountAs(getUnits())+"</AMOUNT>\n" );
	    sb.append( "      <PERCENT>"+StringUtils.format(percent, 1)+"</PERCENT>\n" );
	    sb.append( "      <UNITS>"+getUnitsAbrv()+"</UNITS>\n" );
	    sb.append( "      <POINTS>"+pppg+"</POINTS>\n" );
	    sb.append( "      <LOV>"+lov+"</LOV>\n" );
	    sb.append( "      <MASHED>"+mashed+"</MASHED>\n" );
	    sb.append( "      <STEEPED>"+steeped+"</STEEPED>\n" );
	    sb.append( "      <FERMENTS>"+fermentable+"</FERMENTS>\n" );
	    sb.append( "      <COSTLB>"+getCostPerU()+"</COSTLB>\n" );
	    sb.append( "      <DESCRIPTION>"+StringUtils.subEntities(getDescription())+"</DESCRIPTION>\n" );
	    sb.append( "    </ITEM>\n" );
	    return sb.toString();
	}

}
