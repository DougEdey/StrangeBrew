/**
 *    Filename: WebServer.java
 *     Version: 0.9.0
 * Description: Web Server
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

import java.io.*;

import java.net.URLEncoder;

import java.util.*;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.JOptionPane;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamSource;

import com.homebrewware.NanoHTTPD.Response.Status;

public class WebServer extends NanoHTTPD {


    private File rootDir;
    private FileHandler fileHandler;
    public final static Logger log = Logger.getLogger("com.strangebrew.WevServer");
    public String lineSep = System.getProperty("line.separator");

    private List<Recipe> recipes = new ArrayList<Recipe>();
    private List<File> files = new ArrayList<File>();

    /**
     * Hashtable mapping (String)FILENAME_EXTENSION -> (String)MIME_TYPE
     */
    private static final Map<String, String> MIME_TYPES = new HashMap<String, String>() {/**
         *
         */
        private static final long serialVersionUID = 1L;

    {
        put("css", "text/css");
        put("htm", "text/html");
        put("html", "text/html");
        put("xml", "text/xml");
        put("txt", "text/plain");
        put("asc", "text/plain");
        put("gif", "image/gif");
        put("jpg", "image/jpeg");
        put("jpeg", "image/jpeg");
        put("png", "image/png");
        put("mp3", "audio/mpeg");
        put("m3u", "audio/mpeg-url");
        put("mp4", "video/mp4");
        put("ogv", "video/ogg");
        put("flv", "video/x-flv");
        put("mov", "video/quicktime");
        put("swf", "application/x-shockwave-flash");
        put("js", "application/javascript");
        put("pdf", "application/pdf");
        put("doc", "application/msword");
        put("ogg", "application/x-ogg");
        put("zip", "application/octet-stream");
        put("exe", "application/octet-stream");
        put("class", "application/octet-stream");
    }};



    public WebServer(int port) throws IOException {

        // just serve up on port 8080 for now
        super(port);

        //setup the logging handlers
        Handler[] lH = log.getHandlers();
        for (Handler h : lH) {
                h.setLevel(Level.INFO);
        }

        if(lH.length == 0) {
                log.addHandler(new ConsoleHandler());

                // default level, this can be changed
                try {
                    log.info("System property: "+System.getProperty("debug"));
                    if (System.getProperty("debug").equalsIgnoreCase("INFO")) {

                        log.setLevel(Level.INFO);
                        log.info("Enabled logging at an info level");
                    }
                } catch (NullPointerException e) {
                    log.setLevel(Level.WARNING);
                }

        }


    }

    public WebServer() throws IOException{
        this(8080);
    }

    public Response serve( String uri, Method method,  Map<String, String> header, Map<String, String> parms, Map<String, String> files)
    {

        // Are we looking for recipe details?
        if(uri.contains("/recipe_")) {
            try {
                int i = Integer.parseInt(uri.substring(uri.indexOf("/recipe_") + 8));
                recipes.get(i).calcFermentTotals();
                recipes.get(i).calcHopsTotals();
                recipes.get(i).calcMaltTotals();
                recipes.get(i).calcPrimeSugar();

                String xml = recipes.get(i).toXML(null);
                StreamSource xmlSource = new StreamSource(new StringReader(xml));
                String path = Product.getInstance().getAppPath(Product.Path.DATA);
                Debug.print("Using " + path);

                File xsltFile = new File(path, "recipeToHtml.xslt");

                try {
                    return new Response(XmlTransformer.getString(xmlSource, xsltFile));
                } catch (TransformerConfigurationException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (TransformerException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } catch (Exception e) {
                System.out.println("Invalid recipe ID");
                e.printStackTrace();
            }


        }

        if(rootDir == null ) {
            rootDir = new File(Product.getInstance().getAppPath(Product.Path.ROOT, ""));
        }

        if (uri.equals("/")) {
            return listRecipes(uri);
        }

        if(new File(rootDir, uri).exists()) {
            return serveFile(uri, header, rootDir);
        }
        return listRecipes(uri);
    }

    /**
     * Serves file from homeDir and its' subdirectories (only). Uses only URI, ignores all headers and HTTP parameters.
     */
    public Response serveFile(String uri, Map<String, String> header, File homeDir) {
        Response res = null;

        // Make sure we won't die of an exception later
        if (!homeDir.isDirectory())
            res = new Response(Response.Status.INTERNAL_ERROR, NanoHTTPD.MIME_PLAINTEXT, "INTERNAL ERRROR: serveFile(): given homeDir is not a directory.");

        if (res == null) {
            // Remove URL arguments
            uri = uri.trim().replace(File.separatorChar, '/');
            if (uri.indexOf('?') >= 0)
                uri = uri.substring(0, uri.indexOf('?'));

            // Prohibit getting out of current directory
            if (uri.startsWith("src/main") || uri.endsWith("src/main") || uri.contains("../"))
                res = new Response(Response.Status.FORBIDDEN, NanoHTTPD.MIME_PLAINTEXT, "FORBIDDEN: Won't serve ../ for security reasons.");
        }

        File f = new File(homeDir, uri);
        if (res == null && !f.exists())
            res = new Response(Response.Status.NOT_FOUND, NanoHTTPD.MIME_PLAINTEXT, "Error 404, file not found.");

        // List the directory, if necessary
        if (res == null && f.isDirectory()) {
            // Browsers get confused without '/' after the
            // directory, send a redirect.
            if (!uri.endsWith("/")) {
                uri += "/";
                res = new Response(Response.Status.REDIRECT, NanoHTTPD.MIME_HTML, "<html><body>Redirected: <a href=\"" + uri + "\">" + uri
                        + "</a></body></html>");
                res.addHeader("Location", uri);
            }

            if (res == null) {
                // First try index.html and index.htm
                if (new File(f, "index.html").exists())
                    f = new File(homeDir, uri + "/index.html");
                else if (new File(f, "index.htm").exists())
                    f = new File(homeDir, uri + "/index.htm");
                    // No index file, list the directory if it is readable
                else if (f.canRead()) {
                    String[] files = f.list();
                    String msg = "<html><body><h1>Directory " + uri + "</h1><br/>";

                    if (uri.length() > 1) {
                        String u = uri.substring(0, uri.length() - 1);
                        int slash = u.lastIndexOf('/');
                        if (slash >= 0 && slash < u.length())
                            msg += "<b><a href=\"" + uri.substring(0, slash + 1) + "\">..</a></b><br/>";
                    }

                    if (files != null) {
                        for (int i = 0; i < files.length; ++i) {
                            File curFile = new File(f, files[i]);
                            boolean dir = curFile.isDirectory();
                            if (dir) {
                                msg += "<b>";
                                files[i] += "/";
                            }

                            msg += "<a href=\"" + encodeUri(uri + files[i]) + "\">" + files[i] + "</a>";

                            // Show file size
                            if (curFile.isFile()) {
                                long len = curFile.length();
                                msg += " &nbsp;<font size=2>(";
                                if (len < 1024)
                                    msg += len + " bytes";
                                else if (len < 1024 * 1024)
                                    msg += len / 1024 + "." + (len % 1024 / 10 % 100) + " KB";
                                else
                                    msg += len / (1024 * 1024) + "." + len % (1024 * 1024) / 10 % 100 + " MB";

                                msg += ")</font>";
                            }
                            msg += "<br/>";
                            if (dir)
                                msg += "</b>";
                        }
                    }
                    msg += "</body></html>";
                    res = new Response(msg);
                } else {
                    res = new Response(Response.Status.FORBIDDEN, NanoHTTPD.MIME_PLAINTEXT, "FORBIDDEN: No directory listing.");
                }
            }
        }
        try {
            if (res == null) {
                // Get MIME type from file name extension, if possible
                String mime = null;
                int dot = f.getCanonicalPath().lastIndexOf('.');
                if (dot >= 0)
                    mime = MIME_TYPES.get(f.getCanonicalPath().substring(dot + 1).toLowerCase());
                if (mime == null)
                    mime = NanoHTTPD.MIME_DEFAULT_BINARY;

                // Calculate etag
                String etag = Integer.toHexString((f.getAbsolutePath() + f.lastModified() + "" + f.length()).hashCode());

                // Support (simple) skipping:
                long startFrom = 0;
                long endAt = -1;
                String range = header.get("range");
                if (range != null) {
                    if (range.startsWith("bytes=")) {
                        range = range.substring("bytes=".length());
                        int minus = range.indexOf('-');
                        try {
                            if (minus > 0) {
                                startFrom = Long.parseLong(range.substring(0, minus));
                                endAt = Long.parseLong(range.substring(minus + 1));
                            }
                        } catch (NumberFormatException ignored) {
                        }
                    }
                }

                // Change return code and add Content-Range header when skipping is requested
                long fileLen = f.length();
                if (range != null && startFrom >= 0) {
                    if (startFrom >= fileLen) {
                        res = new Response(Response.Status.RANGE_NOT_SATISFIABLE, NanoHTTPD.MIME_PLAINTEXT, "");
                        res.addHeader("Content-Range", "bytes 0-0/" + fileLen);
                        res.addHeader("ETag", etag);
                    } else {
                        if (endAt < 0)
                            endAt = fileLen - 1;
                        long newLen = endAt - startFrom + 1;
                        if (newLen < 0)
                            newLen = 0;

                        final long dataLen = newLen;
                        FileInputStream fis = new FileInputStream(f) {
                            @Override
                            public int available() throws IOException {
                                return (int) dataLen;
                            }
                        };
                        fis.skip(startFrom);

                        res = new Response(Response.Status.PARTIAL_CONTENT, mime, fis);
                        res.addHeader("Content-Length", "" + dataLen);
                        res.addHeader("Content-Range", "bytes " + startFrom + "-" + endAt + "/" + fileLen);
                        res.addHeader("ETag", etag);
                    }
                } else {
                    if (etag.equals(header.get("if-none-match")))
                        res = new Response(Response.Status.NOT_MODIFIED, mime, "");
                    else {
                        res = new Response(Response.Status.OK, mime, new FileInputStream(f));
                        res.addHeader("Content-Length", "" + fileLen);
                        res.addHeader("ETag", etag);
                    }
                }
            }
        } catch (IOException ioe) {
            res = new Response(Response.Status.FORBIDDEN, NanoHTTPD.MIME_PLAINTEXT, "FORBIDDEN: Reading file failed.");
        }

        res.addHeader("Accept-Ranges", "bytes"); // Announce that the file server accepts partial content requestes
        return res;
    }

    /**
     * URL-encodes everything between "/"-characters. Encodes spaces as '%20' instead of '+'.
     */
    private String encodeUri(String uri) {
        String newUri = "";
        StringTokenizer st = new StringTokenizer(uri, "/ ", true);
        while (st.hasMoreTokens()) {
            String tok = st.nextToken();
            if (tok.equals("/"))
                newUri += "/";
            else if (tok.equals(" "))
                newUri += "%20";
            else {
                try {
                    newUri += URLEncoder.encode(tok, "UTF-8");
                } catch (UnsupportedEncodingException ignored) {
                }
            }
        }
        return newUri;
    }

    private void loadRecipes() {
        recipes.clear();
        files.clear();

        String recipeDir = "";
        Options opt = Options.getInstance();
        if(opt.getProperty("optRecipe") != null)
            recipeDir = opt.getProperty("optRecipe");

        if(recipeDir.equalsIgnoreCase("") ) {
            recipeDir = Product.getInstance().getAppPath(Product.Path.RECIPE);
        }
        File dir = new File(recipeDir);

        for (int i = 0; i < dir.list().length; i++) {
            File file = new File(dir.list()[i]);

            if (file.getPath().endsWith(".rec") || file.getPath().endsWith(".qbrew")
                    || file.getPath().endsWith(".xml")) {

                Debug.print("Opening: " + file.getName() + ".\n");
                // file.getAbsolutePath doesn't work here for some reason,
                // so we have to build it ourselves
                String fileName = dir.getAbsolutePath() + System.getProperty("file.separator")
                        + file.getName();
                file = new File(fileName);
                OpenImport openImport = new OpenImport();
                Recipe r = openImport.openFile(file);
                if (openImport.getFileType().equals("sb")
                        || openImport.getFileType().equals("qbrew")
                        || openImport.getFileType().equals("promash")) {
                    recipes.add(r);
                    files.add(file);

                } else if (openImport.getFileType().equals("beerxml")) {
                    List<Recipe> rs = openImport.getRecipes();
                    for (int j=0; j<rs.size(); j++){
                        recipes.add(rs.get(j));
                        files.add(file);
                    }
                }
            }
        }

    }

    public String getHeader() {
        String header = "";
        return header;
    }

    public String addJS() {
        String javascript =  "<link rel=\"stylesheet\" type=\"text/css\" href=\"/templates/static/raspibrew.css\" />" + lineSep +
                 "<!-- Bootstrap -->" + lineSep +
                "<link href=\"/templates/css/bootstrap.min.css\" rel=\"stylesheet\" media=\"screen\">" + lineSep +

            "<script type=\"text/javascript\" src=\"/templates/js/bootstrap.min.js\"></script>";

        return javascript;

    }

    private Response listRecipes(String uri) {


        String msg = "<html>";
        msg += "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" + lineSep +
            getHeader() + lineSep +
            "</head><body>" + lineSep +
            addJS() + lineSep;

        Response res = null;

        if (recipes == null || recipes.size() == 0 ) {
            loadRecipes();
        }

        Recipe curRecipe = null;
        msg += "<div class=\"panel panel-primary\">";

        for (int i = 1; i < recipes.size(); ++i) {
            curRecipe = recipes.get(i);
            msg += "<a href=\"" + encodeUri("recipe_" + i) + "\" class=\"btn btn-primary btn-lg active\" role=\"button\">"
                    + curRecipe.getName() + " - " + curRecipe.getStyle() + " by " + curRecipe.getBrewer()+
                    "</a>" + lineSep;
            msg += "<br/>" + lineSep;
        }

        msg += "</div>";
        msg += "</body></html>";
        res = new Response(msg);


        return res;
    }


}
