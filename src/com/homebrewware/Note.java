/**
 *    Filename: Note.java
 *     Version: 0.9.0
 * Description: Note
 *     License: GPLv2
 *        Date: 2017-01-14
 *
 * Copyright (c) 2005 Drew Avis
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

import java.text.SimpleDateFormat;
import java.util.Date;

public class Note {

	private Date date;
	private String type;
	private String note;
	static final public String PLANNING = "Planning";
	static final public String BREWED = "Brewed";
	static final public String FERMENTATION = "Fermentation";
	static final public String RACKED = "Racked";
	static final public String CONDITIONED = "Conditioned";
	static final public String KEGGED = "Kegged";
	static final public String BOTTLED = "Bottled";
	static final public String TASTING = "Tasting";
	static final public String CONTEST = "Contest";
	static final public String[] types = {PLANNING, BREWED, FERMENTATION, RACKED, CONDITIONED, KEGGED, BOTTLED, TASTING, CONTEST};

	public Note() {
		date = new Date();
		type = PLANNING;
		note = "";
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
		return this.note;
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

	public String toString(){
		StringBuffer sb = new StringBuffer();
	    sb.append( getType()+": " );
	    sb.append( getNote() );
	    SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
	    sb.append( " ("+df.format(getDate())+")\n" );
	    return sb.toString();
	}



}
