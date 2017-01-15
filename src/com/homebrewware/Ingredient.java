/**
 *    Filename: Ingredient.java
 *     Version: 0.9.0
 * Description: Ingredient
 *     License: GPLv2
 *        Date: 2017-01-14
 *
 * Copyright (c) 2004 Drew Avis
 * @author Drew Avis
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

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.homebrewware.Quantity.Converter;

public class Ingredient implements Comparable<Ingredient> {
	private Quantity amount = new Quantity();
	private double costPerU;
	private Date dateBought = new Date();
	private String description="";
	private String name="";
	private Quantity stock = new Quantity();
	private String type;
	private boolean modified;


	public Ingredient() {
		modified = true;
	}
	// override the equals so we can compare:
	public boolean equals(Object obj)
	  {
	    if(obj == this)
	    return true;

	    /* is obj reference null */
	    if(obj == null)
	    return false;

	    /* Make sure references are of same type */

	    if(!(getClass() == obj.getClass()))
	    return false;
	    else
	    {
	      Ingredient tmp = (Ingredient)obj;
	      if(tmp.name.equalsIgnoreCase(this.name)){
	       return true;
	      }
	      else
	       return false;
	    }
	  }
	public double getAmountAs(String s){ return amount.getValueAs(s); }
	public double getStockAs(String s) { return stock.getValueAs(s); }
	public double getStock() { return stock.getValue(); }
	// Get methods:
	public double getCostPerU(){ return costPerU; }
	public Date getDate(){ return dateBought; }
	public String getDescription(){ return description; }
	public boolean getModified(){ return modified; }
	public String getName(){ return name; }
	public String getType(){ return type; }
	public String getUnits(){ return amount.getUnits(); }

	public double getCostPerUAs(String to){
		// current value / new value * cost
		return costPerU;
	}

	public String getUnitsAbrv(){ return amount.getAbrv(); }

	public void setAmount(double a){ amount.setAmount(a); }
	public void setStock(double a){
		if(a < 0.00)
			stock.setAmount(0);
		else
			stock.setAmount(a);
	}
	/**
	 * Handles a string of the form "d u", where d is a double
	 * amount, and u is a string of units.  For importing the
	 * quantity attribute from QBrew xml.
	 * @param a
	 */

	public void setAmountAndUnits(String a){
		int i = a.indexOf(" ");
		String d = a.substring(0,i);
		String u = a.substring(i);
		Double dAmount = 0.0;
		try {
			NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
			Number number = format.parse(d.trim());
		    dAmount = number.doubleValue();

		} catch (NumberFormatException m) {

			Debug.print("Could not read Amount: "+ d + " as a valid size");
			return;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			Debug.print("Could not read Amount: "+ d + " as a valid size");
			return;
		}
		amount.setAmount(dAmount);
		amount.setUnits(u.trim());
	}
	public void setAmountAs(double a, String u) {
		double converted = Quantity.convertUnit(u, amount.getUnits(), a);
		amount.setAmount(converted);
	}
	public void setCost(double c){ costPerU = c; }
	public void setCost(String c){
		if (c.substring(0,1).equals("$")) {
			c = c.substring(1, c.length()); // trim leading "$"
		}
		try {
			NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
			Number number = format.parse(c.trim());
		    costPerU = number.doubleValue();

		} catch (NumberFormatException m) {
			Debug.print("Number format Exception setting cost to " + c);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			Debug.print("Parse Exception setting cost to " + c);
		}
	}
	public void setDate(String d){
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		try{
		dateBought = df.parse(d);
		}catch (ParseException p){

		}

	}

	public void setDescription(String d){ description = d; }

	// Setter methods:
	public void setModified(boolean b){ modified = b; }
	public void setName(String n){ name = n; }
	public void setType(String t){ type = t; }
	public void setUnits(String a){ amount.setUnits(a); }

	public void convertTo(String newUnits){
		setCost(Quantity.convertUnit(newUnits, getUnits(), getCostPerU()));

		amount.convertTo(newUnits);
	}

	// implement to support comboboxes in Swing:
	public String toString(){
		return name;
	}

	public int compareTo(Ingredient i) {
		int result = this.getName().compareToIgnoreCase(i.getName());
		return result;
	}
}
