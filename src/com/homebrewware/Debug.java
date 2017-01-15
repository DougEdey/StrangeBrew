/**
 *    Filename: Debug.java
 *     Version: 0.9.0
 * Description: Debug
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

public class Debug {
	// private static final boolean DEBUG = true;
	public static boolean DEBUG = false;

	public static void set(boolean s){
		DEBUG = s;
		Debug.print("Debugging is on.\n");
	}


	public static void print(Object msg){
		if (DEBUG){
			System.out.println(msg.toString());
		}
	}

}
