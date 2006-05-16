package ca.strangebrew;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * $Id: Ingredient.java,v 1.4 2006/05/16 14:36:52 andrew_avis Exp $
 * Created on Oct 21, 2004
 * @author aavis
 *
 * Base class for all ingredients.  Dunno why I didn't do this
 * in the first place.
 */
public class Ingredient {
	private Quantity amount = new Quantity();
	private double costPerU;
	private Date dateBought;
	private String description;
	private String name="";
	private double stock;
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
	// Get methods:
	public double getCostPerU(){ return costPerU; }
	public Date getDate(){ return dateBought; }
	public String getDescription(){ return description; }
	public boolean getModified(){ return modified; }
	public String getName(){ return name; }
	public double getStock(){ return stock; }	
	public String getType(){ return type; }
	public String getUnits(){ return amount.getUnits(); }
	
	
	public String getUnitsAbrv(){ return amount.getAbrv(); }
	public void setAmount(double a){ amount.setAmount(a); }
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
		
		amount.setAmount(Double.parseDouble(d.trim()));
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
		costPerU = Double.parseDouble(c);
	}
	public void setDate(String d){ 
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		try{
		dateBought = df.parse(d);
		}catch (ParseException p){}
		
	}
	
	public void setDescription(String d){ description = d; }
	
	// Setter methods:
	public void setModified(boolean b){ modified = b; }
	public void setName(String n){ name = n; }
	public void setStock (double d) { stock = d; }
	public void setType(String t){ type = t; }
	public void setUnits(String a){ amount.setUnits(a); }
	
	public void setUnitsFull(String u){ amount.setUnits(u); }
	
	// implement to support comboboxes in Swing:
	public String toString(){
		return name;
	}

	
	

}
