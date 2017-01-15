/**
 *    Filename: ServerRunner.java
 *     Version: 0.9.0
 * Description: Server Runner
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

import java.io.IOException;

public class ServerRunner implements Runnable {

	Class serverClass = null;
	int port = 8080;

	public ServerRunner(Class serverClass, int port) {
    	this.serverClass = serverClass;
    	this.port = port;
    }

    public static void executeInstance(NanoHTTPD server) {
        try {

            server.start();
        } catch (IOException ioe) {
            System.err.println("Couldn't start server:\n" + ioe);
            System.exit(-1);
        }

        System.out.println("Server started, Hit Enter to stop.\n");

        try {
            System.in.read();
        } catch (Throwable ignored) {
        }

        server.stop();
        System.out.println("Server stopped.\n");
    }

	@Override
	public void run() {
		 try {
			executeInstance((NanoHTTPD) serverClass.getDeclaredConstructor(int.class).newInstance(port));
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}
