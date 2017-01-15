/**
 *    Filename: WaterProfile.java
 *     Version: 0.9.0
 * Description: Water Profile
 *     License: GPLv2
 *        Date: 2017-01-14
 *
 * @author unknown
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

import java.util.List;

public class WaterProfile implements Comparable<WaterProfile> {
	private String name;
	private String description;
	private double ca;
	private double mg;
	private double na;
	private double so4;
	private double hco3;
	private double cl;
	private double hardness;
	private double tds;
	private double ph;
	private double alkalinity;

	public WaterProfile() {
		name = "Distilled/RO";
		ph = 5.80000019073486;
	}

	public WaterProfile(String name) {
		// Creates a new water profile based on the name from the db
		List<WaterProfile> profiles = Database.getInstance().waterDB;

		for (int i = 0; i < profiles.size(); i++) {
			WaterProfile p = profiles.get(i);
			if (p.getName().equals(name)) {
				this.name = p.getName();
				this.description = p.getDescription();
				this.ca = p.getCa();
				this.mg = p.getMg();
				this.na = p.getNa();
				this.so4 = p.getSo4();
				this.hco3 = p.getHco3();
				this.cl = p.getCl();
				this.hardness = p.getHardness();
				this.tds = p.getTds();
				this.ph = p.getPh();
				this.alkalinity = p.getAlkalinity();

				return;
			}
		}

		this.name = "Distilled/RO";
		this.ph = 5.80000019073486;
	}

	public double getAlkalinity() {
		return alkalinity;
	}

	public double getCa() {
		return ca;
	}

	public double getCl() {
		return cl;
	}

	public String getDescription() {
		return description;
	}

	public double getHardness() {
		return hardness;
	}

	public double getHco3() {
		return hco3;
	}

	public double getMg() {
		return mg;
	}

	public double getNa() {
		return na;
	}

	public String getName() {
		return name;
	}

	public double getPh() {
		return ph;
	}

	public double getSo4() {
		return so4;
	}

	public double getTds() {
		return tds;
	}

	public void setAlkalinity(double alkalinity) {
		this.alkalinity = alkalinity;
	}

	public void setCa(double ca) {
		this.ca = ca;
	}

	public void setCl(double cl) {
		this.cl = cl;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setHardness(double hardness) {
		this.hardness = hardness;
	}

	public void setHco3(double hco3) {
		this.hco3 = hco3;
	}

	public void setMg(double mg) {
		this.mg = mg;
	}

	public void setNa(double na) {
		this.na = na;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPh(double ph) {
		this.ph = ph;
	}

	public void setSo4(double so4) {
		this.so4 = so4;
	}

	public void setTds(double tds) {
		this.tds = tds;
	}

	public String toString() {

		String str = String.format("%s => %3.1fCa %3.1fMg %3.1fNa %3.1fSo4 %3.1fHCO3 %3.1fCl %3.1fHardness %3.1fTDS %3.1fpH %3.1fAlk",
				new Object[] {name,
				new Double(ca),
				new Double(mg),
				new Double(na),
				new Double(so4),
				new Double(hco3),
				new Double(cl),
				new Double(hardness),
				new Double(tds),
				new Double(ph),
				new Double(alkalinity)
				});


		return str;
	}

	public String toXML(int indent) {
		String xml = "";

		xml += StringUtils.xmlElement("NAME", name, indent);
		xml += StringUtils.xmlElement("CA", Double.toString(ca), indent);
		xml += StringUtils.xmlElement("MG", Double.toString(mg), indent);
		xml += StringUtils.xmlElement("NA", Double.toString(na), indent);
		xml += StringUtils.xmlElement("SO4", Double.toString(so4), indent);
		xml += StringUtils.xmlElement("HCO3", Double.toString(hco3), indent);
		xml += StringUtils.xmlElement("CL", Double.toString(cl), indent);
		xml += StringUtils.xmlElement("HARDNESS", Double.toString(hardness), indent);
		xml += StringUtils.xmlElement("TDS", Double.toString(tds), indent);
		xml += StringUtils.xmlElement("PH", Double.toString(ph), indent);
		xml += StringUtils.xmlElement("ALKALINITY", Double.toString(alkalinity), indent);

		return xml;
	}

	public int compareTo(WaterProfile w) {
		int result = this.getName().compareToIgnoreCase(w.getName());
		return (result == 0 ? -1 : result);
	}
}
