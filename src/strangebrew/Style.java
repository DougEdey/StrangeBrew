package strangebrew;

/**
 * $Id: Style.java,v 1.3 2004/11/18 18:06:18 andrew_avis Exp $
 * Created on Oct 21, 2004
 * @author aavis
 * This is a class to create a style object
 * Yes, it would be better as a struct, but what can you do?
 */

public class Style {
	
	public String name;
	public String category;
	public String catNum;
	public double ogLow;
	public double ogHigh;
	public double alcLow;
	public double alcHigh;
	public double ibuLow;
	public double ibuHigh;
	public double lovLow;
	public double lovHigh;
	public String commercialEx;
	public String description;
	
	
	
	// get methods:
	public String getName(){ return name; }
		
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
	 * @return Returns the commercialEx.
	 */
	public String getCommercialEx() {
		return commercialEx;
	}
	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
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
	 * @return Returns the lovHigh.
	 */
	public double getLovHigh() {
		return lovHigh;
	}
	/**
	 * @return Returns the lovLow.
	 */
	public double getLovLow() {
		return lovLow;
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
	
	// set methods:
	public void setName(String n){ name = n; }
	public void setDescription(String d){ description = d; }
	
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
	 * @param commercialEx The commercialEx to set.
	 */
	public void setCommercialEx(String commercialEx) {
		this.commercialEx = commercialEx;
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
	 * @param lovHigh The lovHigh to set.
	 */
	public void setLovHigh(double lovHigh) {
		this.lovHigh = lovHigh;
	}
	/**
	 * @param lovLow The lovLow to set.
	 */
	public void setLovLow(double lovLow) {
		this.lovLow = lovLow;
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
	
	public String toXML(){
		StringBuffer sb = new StringBuffer();
	    sb.append( "<STYLE>"+getName()+"</STYLE>\n" );
	    sb.append( "<DESCR>"+getDescription()+"</DESCR>\n" );
	    return sb.toString();
	}
	
	public String toString(){
		return getName();
	}
	
	
	
}


