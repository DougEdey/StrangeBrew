/*
 * Created on Nov 25, 2004
 * @author aavis
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package strangebrew.server;

import java.net.*;
import java.io.*;

public class SBServer {

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(4444);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 4444.");
            System.exit(1);
        }

        Socket clientSocket = null;
        try {
            clientSocket = serverSocket.accept();
        } catch (IOException e) {
            System.err.println("Accept failed.");
            System.exit(1);
        }
        System.out.println("Connected.\n");
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(
				new InputStreamReader(
				clientSocket.getInputStream()));
        String inputLine, outputLine;
        SBProtocol sbp = new SBProtocol();

        /*
        outputLine = sbp.processInput(null);
        out.println(outputLine);
        */
        while ((inputLine = in.readLine()) != null && !inputLine.equals("bye")){
        	System.out.println("processing: "+inputLine);
             outputLine = sbp.processInput(inputLine);
             out.println(outputLine);
             if (outputLine == null)
                break;
        }
        out.close();
        in.close();
        clientSocket.close();
        serverSocket.close();
    }
	
}

