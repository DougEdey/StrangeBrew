package ca.strangebrew;

/**
 * $Id $
 * @author aavis
 *
 */
public class Misc extends Ingredient {

	private String comments;
	private String stage;
	private int time;
	
	// default constructor
	public  Misc() {
		setName("");;
		setCost(0);
		setDescription("");
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
	    sb.append( "      <COMMENTS>"+comments+"</COMMENTS>\n" );
	    sb.append( "    </ITEM>\n" );
	    return sb.toString();
	}
}
