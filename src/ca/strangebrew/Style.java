package ca.strangebrew;


/**
 * $Id: Style.java,v 1.4 2006/05/19 16:57:30 andrew_avis Exp $
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
	
	public String type;
	
	
	
	
	// get methods:
	public String getName(){ 
		if (name != null)
			return name;
		else
			return "";}
	
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
	
	// set methods:
	public void setName(String n){ name = n; }
		
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
	
	private String xmlElement(String elem, String content, int i) {
		String s = "";
		for (int j = 0; j<i;j++)
			s += " ";		
		s += "<" + elem + ">" + content + "</" + elem + ">\n";		
		return s;
	}

	
	public String toXML(){
		StringBuffer sb = new StringBuffer();
		int indent = 0;
	    sb.append(" <style>\n");    
	    // this is the BJCP style dtd:
	    sb.append("  <subcategory id=\"" + catNum + "\">");
	    indent = 4;
	    sb.append(xmlElement("name", name, indent));
	    sb.append(xmlElement("aroma", aroma, indent)); 
	    sb.append(xmlElement("appearance", appearance, indent));
	    sb.append(xmlElement("flavor", flavour, indent));
	    sb.append(xmlElement("mouthfeel", mouthfeel, indent));
	    sb.append(xmlElement("impression", impression, indent));
	    sb.append(xmlElement("comments", comments, indent));
	    sb.append(xmlElement("ingredients", ingredients, indent));
	    sb.append(" <stats>\n");
	    sb.append("  <og flexible=\"" + ogFlexible + "\">\n");
	    indent = 6;
	    sb.append(xmlElement("low", ""+ogLow, indent));
	    sb.append(xmlElement("high", ""+ogHigh, indent));
	    sb.append("  </og>\n"); 
	    sb.append("  <fg flexible=\"" + fgFlexible + "\">\n");
	    sb.append(xmlElement("low", ""+fgLow, indent));
	    sb.append(xmlElement("high", ""+fgHigh, indent));
	    sb.append("  </fg>\n");
	    sb.append("  <ibu flexible=\"" + ibuFlexible + "\">\n");
	    sb.append(xmlElement("low", ""+ibuLow, indent));
	    sb.append(xmlElement("high", ""+ibuHigh, indent));
	    sb.append("  </ibu>\n");
	    sb.append("  <srm flexible=\"" + srmFlexible + "\">\n");
	    sb.append(xmlElement("low", ""+srmLow, indent));
	    sb.append(xmlElement("high", ""+srmHigh, indent));
	    sb.append("  </srm>\n");
	    sb.append("  <abv flexible=\"" + alcFlexible + "\">\n");
	    sb.append(xmlElement("low", ""+alcLow, indent));
	    sb.append(xmlElement("high", ""+alcHigh, indent));
	    sb.append("  </abv>\n");	    
	    sb.append("</stats>\n");
	    indent = 4;
	    sb.append(xmlElement("examples", examples, indent));	    
	    sb.append("  </subcategory>\n");
	    sb.append(" </style>\n");	    
	    return sb.toString();
	}
	
	public String toText(){
		StringBuffer sb = new StringBuffer();
	    sb.append( "Name: "+catNum + ":"+getName()+"\n" );
	    sb.append( "Category: "+category+"\n" );
	    sb.append( "Class: "+type+"\n" );
	    sb.append( "OG: "+ogLow + "-"+ogHigh+"\n" );
	    sb.append( "IBU: "+ibuLow + "-"+ibuHigh+"\n" );
	    sb.append( "SRM: "+srmLow + "-"+srmHigh+"\n" );
	    sb.append( "Alc: "+alcLow + "-"+alcHigh+"\n" );
	    sb.append( "Aroma: "+aroma+"\n" );
	    sb.append( "Appearance: "+appearance+"\n" );
	    sb.append( "Flavour: "+flavour+"\n" );
	    sb.append( "Mouthfeel: "+mouthfeel+"\n" );
	    sb.append( "Impression: "+impression+"\n" );
	    sb.append( "Comments: "+comments+"\n" );
	    sb.append( "Ingredients: "+ingredients+"\n" );
	    sb.append( "Examples: "+examples+"\n" );
	    return sb.toString();
	}
	
	public String toString(){
		return getName();
	}

	
	
	
}


