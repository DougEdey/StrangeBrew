package strangebrew;

/**
 * $Id $
 * @author aavis
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Misc {
	private String name;
	private double amount;
	private String units;
	private double costPerU;
	private String description;
	private String comments;
	private String stage;
	private int time;
	
	// default constructor
	public  Misc() {
		name = "";
		amount = 0;
		units = "";
		costPerU = 0;
		description = "";
	}
	
	// get methods
	public String getName(){ return name; }
	public double getAmount(){ return amount; }
	public String getUnits(){ return units; }
	public double getCostPerU(){ return costPerU; }
	public String getDescription(){ return description; }
	public String getComments(){ return comments; }
	public String getStage(){ return stage; }
	public int getTime(){ return time; }
	
	// set methods
	public void setName(String s){ name = s; }
	public void setAmount(double a){ amount = a; }
	public void setUnits(String u){ units = u; }
	public void setCostPerU(double c){ costPerU = c; }
	public void setDescription(String d){ description = d; }
	public void setComments(String c){ comments = c; }
	public void setStage(String s){ stage = s; }
	public void setTime(int t){ time = t; }

	public String toXML(){
	    StringBuffer sb = new StringBuffer();
	    sb.append( "    <ITEM>\n" );
	    sb.append( "      <NAME>"+name+"</NAME>\n" );
	    sb.append( "      <AMOUNT>"+amount+"</AMOUNT>\n" );
	    sb.append( "      <UNITS>"+units+"</UNITS>\n" );
	    sb.append( "      <STAGE>"+stage+"</STAGE>\n" );
	    sb.append( "      <TIME>"+time+"</TIME>\n" );
	    sb.append( "    </ITEM>\n" );
	    return sb.toString();
	}
}
