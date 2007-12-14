/*
 * Created on Jun 8, 2005
 * by aavis
 *
 */
package ca.strangebrew;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 
 * @author aavis
 *
 * Note class to store notes
 * Eventually this will all go into a much larger "Brew Session" class
 */

public class Note {
	
	private Date date;
	private String type;
	private String note;
	static final private String[] types = {"Planning", "Brewed", "Fermentation", "Racked",
		"Conditioned", "Kegged", "Bottled", "Tasting", "Contest"};
	
	public Note() {
		date = new Date();
		type = types[0];
		note = "";
	}
	
	static public String[] getTypes() {
		return types;
	}
	
	/**
	 * @return Returns the date.
	 */
	public Date getDate() {
		return date;
	}
	/**
	 * @param date The date to set.
	 */
	public void setDate(Date date) {
		this.date = date;
	}
	/**
	 * @return Returns the note.
	 */
	public String getNote() {
		return note;
	}
	/**
	 * @param note The note to set.
	 */
	public void setNote(String note) {
		this.note = note;
	}
	/**
	 * @return Returns the type.
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type The type to set.
	 */
	public void setType(String type) {
		this.type = type;
	}		
	
	public String toXML(){
		    StringBuffer sb = new StringBuffer();
		    sb.append( "    <ITEM>\n" );
		    SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");		
		    sb.append( "      <DATE>"+df.format(getDate())+"</DATE>\n" );
		    sb.append( "      <TYPE>"+getType()+"</TYPE>\n" );
		    sb.append( "      <NOTE>"+getNote()+"</NOTE>\n" );
		    sb.append( "    </ITEM>\n" );
		    return sb.toString();
	}
	
	

}