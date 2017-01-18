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

import java.io.File;
import java.io.UnsupportedEncodingException;

import java.net.URLDecoder;

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

    public String getBuildNumber() {
        return Build.NUMBER;
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

    private volatile static Product uniqueInstance;

    private Product() {

        try {
            String jpath = new File(Product.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile().getPath();
            rootDir = URLDecoder.decode(jpath, "UTF-8");
        } catch(UnsupportedEncodingException e) {
            System.err.println("Unable to determine application path");
        }

        String separator = System.getProperty("file.separator");

        resourceDir = rootDir + separator + Build.Dir.RESOURCES;
        imagesDir = resourceDir + separator + Build.Dir.IMAGES;
        dataDir = resourceDir + separator + Build.Dir.DATA;
        recipesDir = resourceDir + separator + Build.Dir.RECIPES;

        helpDir = "file://" + rootDir + separator + Build.Dir.HELP;
    }
}
