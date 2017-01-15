/**
 *    Filename: Hop.java
 *     Version: 0.9.0
 * Description: Hop
 *     License: GPLv2
 *        Date: 2017-01-14
 *
 * Copyright (c) 2004 dougedey
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

public class Hop extends Ingredient implements Comparable<Ingredient> {
	private double alpha;
	private String add;
	private int minutes;
	private double storage;
	private double IBU;

	// Hops should know about hop types
	static final public String LEAF = "Leaf";
	static final public String PELLET = "Pellet";
	static final public String PLUG = "Plug";
	static final public String BOIL = "Boil";
	static final public String FWH = "FWH";
	static final public String DRY = "Dry";
	static final public String MASH = "Mash";

	static final public String[] forms = { LEAF, PELLET, PLUG };
	static final public String[] addTypes = { BOIL, FWH, DRY, MASH };

	// Constructors:

	public Hop() {
		// default constructor
		setName("New Hop");
		setType(LEAF);
		setAdd(BOIL);
		setUnits(Quantity.OZ); // oz
	}

	public Hop(String u, String t) {
		setUnits(u);
		setType(t);
		setAdd(BOIL);
	}

	// get methods:
	public String getAdd() {
		return add;
	}

	public double getAlpha() {
		return alpha;
	}

	public double getIBU() {
		return IBU;
	}

	public int getMinutes() {
		return minutes;
	}

	public double getStorage() {
		return storage;
	}

	// Setter methods:
	public void setAdd(String a) {
		add = a;
	}

	public void setAlpha(double a) {
		alpha = a;
	}

	// public void setForm(String f){ form = f; }
	public void setIBU(double i) {
		IBU = i;
	}

	public void setMinutes(int m) {
		minutes = m;
	}

	public void setStorage(double s) {
		storage = s;
	}

	public String toXML() {
		StringBuffer sb = new StringBuffer();
		sb.append("    <ITEM>\n");
		sb.append("      <HOP>" + getName() + "</HOP>\n");
		sb.append("      <AMOUNT>" + getAmountAs(getUnits()) + "</AMOUNT>\n");
		sb.append("      <TIME>" + getMinutes() + "</TIME>\n");
		sb.append("      <UNITS>" + getUnitsAbrv() + "</UNITS>\n");
		sb.append("      <FORM>" + getType() + "</FORM>\n");
		sb.append("      <ALPHA>" + alpha + "</ALPHA>\n");
		sb.append("      <COSTOZ>" + getCostPerU() + "</COSTOZ>\n");
		sb.append("      <ADD>" + add + "</ADD>\n");
		sb.append("      <DESCRIPTION>"
				+ StringUtils.subEntities(getDescription())
				+ "</DESCRIPTION>\n");
		sb.append("      <DATE>" + getDate() + "</DATE>\n");
		sb.append("    </ITEM>\n");
		return sb.toString();
	}

	public int compareTo(Ingredient i) {
		if (i instanceof Hop) {
			return compareTo((Hop) i);
		} else {
			return super.compareTo(i);
		}
	}


	public int compareTo(Hop h) {


		// Check to see if the additions are at the same time
		if (this.getMinutes() == 0 && h.getMinutes() == 0) {
			// Check to see if we have dry hopping
			if (this.getAdd() == h.getAdd()) {
				// Same addition, continue the compare
				return super.compareTo(h);
			} else {
				// Different addition type, so compare that. Boil is luckily
				// prior to Dry
				return this.getAdd().compareToIgnoreCase(h.getAdd());

			}
		} else {
			// Times are not the same, straightforward comparrison
			int result = ((Integer) h.getMinutes()).compareTo((Integer) this
					.getMinutes());
			return (result == 0 ? -1 : result);
		}
	}



}
