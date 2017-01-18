/**
 *    Filename: Product.java
 *     Version: 0.9.0
 * Description: Product
 *     License: GPLv2
 *        Date: 2017-01-14
 *
 * Location for product information.
 *
 * Copyright (c) 2017 Dallas Fletchall
 * @author Dallas Fletchall
 */
package com.homebrewware;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import java.net.URL;
import java.net.URLDecoder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import generated.Build;

public class Product
{
    public static enum Path {
        ROOT,
        DATA,
        HELP,
        RECIPE,
        IMAGES,
        RESOURCES,
    };

    public boolean isNewSoftwareAvailable() {
        return (isNewBuildAvailable() || isNewVersionAvailable());
    }

        //BufferedReader in;
        //try {
        //    in = new BufferedReader(new InputStreamReader(Build.openStream()));

        //    String inputLine;
        //    while ((inputLine = in.readLine()) != null)


        //        if(inputLine.contains("BUILDNUMBER")) {
        //            //got the build ID line
        //            String [] splitLine = inputLine.split(" ");
        //            String vTemp = splitLine[splitLine.length-1];
        //            vTemp = vTemp.substring(1, vTemp.length()-2);
        //            int newBuildID = Integer.parseInt(vTemp);

        //            if( newBuildID != Integer.parseInt(Product.getInstance().getBuildNumber())) {
        //                // newest Build ID means there's a new download avalable!
        //                Object[] options = {"Yes, please",
        //                        "No, thanks"};

        //                int n = JOptionPane.showOptionDialog(this.getContentPane(),
        //                        "New download available (Build ID " + newBuildID + ")"
        //                        + ". Would you like to download it?",

        //                        "New Version Available!",
        //                        JOptionPane.YES_NO_OPTION,
        //                        JOptionPane.QUESTION_MESSAGE,
        //                        null,
        //                        options,
        //                        options[0]);

        //                if (n == 0) {
        //                    // User Selected Yes to download
        //                    Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        //                    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
        //                        try {
        //                            URL DLURL = new URL("https://github.com/DougEdey/StrangeBrew/blob/master/StrangeBrew-2.1.0-b"+newBuildID+".zip?raw=true");

        //                            desktop.browse(DLURL.toURI());
        //                        } catch (Exception e) {
        //                            e.printStackTrace();
        //                        }
        //                    }
        //                }
        //            } else {
        //                JOptionPane.showMessageDialog(this.getContentPane(),
        //                        "No updates available!");
        //            }
        //        }
        //    in.close();
        //} catch (IOException e) {
        //    e.printStackTrace();
        //}

        //return;

    public String getAppPath(Product.Path dir) {

        String returnPath = rootDir;
        switch(dir) {

            case ROOT:
                break;

            case DATA:
                returnPath = dataDir;
                break;

            case IMAGES:
                returnPath = imagesDir;
                break;

            case RECIPE:
                returnPath = recipesDir;
                break;

            case HELP:
                returnPath = helpDir;
                break;

            case RESOURCES:
                returnPath = resourceDir;
                break;

            default:
                /**
                 * No good way to handle this, previous implementation
                 * allowed the default case to return the rootDir
                 */
                System.err.println("Unknown Directory: " + dir);
        }
        return returnPath;
    }

    public String getAppPath(Product.Path dir, String mRelativePath) {
        return getAppPath(dir) + System.getProperty("file.separator") + mRelativePath;
    }

    public String getBuildDate() {
        return Build.DATE;
    }

    public int getBuildNumber() {
        return Build.Version.NUMBER;
    }

    public static Product getInstance() {

        if(uniqueInstance == null) {
            synchronized (Product.class) {
                if(uniqueInstance == null) {
                    uniqueInstance = new Product();
                }
            }
        }

        return uniqueInstance;
    }

    public String getName() {
        return Build.NAME;
    }

    public String getVersion() {
        return Build.Version.MAJOR + "." + Build.Version.MINOR + "." + Build.Version.REV;
    }

    private String rootDir;
    private String dataDir;
    private String emptyPath;
    private String helpDir;
    private String imagesDir;
    private String recipesDir;
    private String resourceDir;
    private Pattern buildNumberPattern;
    private Pattern majorRevPattern;
    private Pattern minorRevPattern;
    private Pattern revPattern;

    private volatile static Product uniqueInstance;

    private Product() {

        try {
            String jpath = new File(Product.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile().getPath();
            rootDir = URLDecoder.decode(jpath, "UTF-8");
        } catch(UnsupportedEncodingException e) {
            System.err.println("Unable to determine application path");
        }

        /* Cache the directory names */
        String separator = System.getProperty("file.separator");
        resourceDir = rootDir + separator + Build.Dir.RESOURCES;
        imagesDir = resourceDir + separator + Build.Dir.IMAGES;
        dataDir = resourceDir + separator + Build.Dir.DATA;
        recipesDir = resourceDir + separator + Build.Dir.RECIPES;
        helpDir = "file://" + rootDir + separator + Build.Dir.HELP;

        /* Cache each regex */
        buildNumberPattern = Pattern.compile("build\\.number=\\d+");
        majorRevPattern = Pattern.compile("ver\\.major=\\d+");
        minorRevPattern = Pattern.compile("ver\\.minor=\\d+");
        revPattern = Pattern.compile("ver\\.rev=\\d+");
    }

    private boolean isNewBuildAvailable() {

        Matcher m;
        boolean rval = false;

        try {
            URL url = new URL(Build.Domain.SRC_URL + "/build.number");
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

            String inputLine;
            while((inputLine = in.readLine()) != null) {
                 m = buildNumberPattern.matcher(inputLine);
                 if(m.find()) {
                     rval = (Integer.parseInt(m.group(0).split("=")[1]) > getBuildNumber());
                     break;
                 }
            }
            in.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return rval;
    }

    private boolean isNewVersionAvailable() {

        Matcher m;
        boolean rval = false;

        try {
            URL url = new URL(Build.Domain.SRC_URL + "/build.properties");
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

            String inputLine;
            Pattern[] patterns = { majorRevPattern, minorRevPattern, revPattern };
            int[] revisions = { Build.Version.MAJOR, Build.Version.MINOR, Build.Version.REV };

            while((rval == false) && (inputLine = in.readLine()) != null) {

                for(int i = 0; i < patterns.length; i++) {

                    m = patterns[i].matcher(inputLine);
                    if(m.find()) {
                        rval = (Integer.parseInt(m.group(0).split("=")[1]) > revisions[i]);
                    }
                 }
            }
            in.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return rval;
    }
}
