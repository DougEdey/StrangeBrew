/**
 *    Filename: MashDefaults.java
 *     Version: 0.9.0
 * Description: Mash Defaults
 *     License: GPLv2
 *        Date: 2017-01-14
 *
 * Copyright (c) 2006 Drew Avis
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

import java.io.EOFException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

import com.mindprod.csv.CSVReader;
import com.mindprod.csv.CSVWriter;

public class MashDefaults {
    String fileName = "mashdefaults.csv";

    // an array of arrays:
    public ArrayList<ArrayList<String>> defaults = new ArrayList<ArrayList<String>>();

    public MashDefaults(){
        load();
    }

    public void add(Mash m, String name){
        ArrayList<String> def = new ArrayList<String>();
        def.add(name);
        def.add("" + m.getStepSize());
        for (int i=0;i<m.getStepSize(); i++){
            def.add("" + m.getStepStartTemp(i));
            def.add("" + m.getStepEndTemp(i));
            def.add("" + m.getStepMin(i));
            def.add("" + m.getStepMethod(i));
        }
        defaults.add(def);
        save();
    }

    public void set(String name, Recipe r){
        Mash m = new Mash(r);
        m.setName(name);
        r.mash = m;

        r.allowRecalcs = false;
        for (int x=0;x<defaults.size();x++){
            ArrayList<String> d = defaults.get(x);
            if (d.contains(name)){
                int size = Integer.parseInt(d.get(1).toString());
                int j = 2;
                while (j < (size * 4) + 2){
                    int k = m.addStep();
                    m.setStepStartTemp(k, Double.parseDouble(d.get(j++).toString()));
                    m.setStepEndTemp(k, Double.parseDouble(d.get(j++).toString()));
                    m.setStepMin(k, Integer.parseInt(d.get(j++).toString()));
                    m.setStepMethod(k, d.get(j++).toString());
                }
            }
        }
        r.allowRecalcs = true;
        r.mash.calcMashSchedule();

    }

    public String[] getNames() {
        String[] names = new String[defaults.size()];
        for (int i=0;i<defaults.size();i++){
            ArrayList<String> d = defaults.get(i);
            names[i] = d.get(0).toString();
        }

        return names;

    }

    private void save() {

        String path = Product.getInstance().getAppPath(Product.Path.DATA);
        try {
            File file = new File(path, fileName);
            CSVWriter writer = new CSVWriter(new FileWriter(file));
            for (int i=0;i<defaults.size();i++){
                ArrayList<String> d = defaults.get(i);
                for (int j=0;j<d.size();j++)
                    writer.put(d.get(j).toString());
                writer.nl();
            }
            writer.close();
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    private void load(){
        String path = Product.getInstance().getAppPath(Product.Path.DATA);
        defaults = new ArrayList<ArrayList<String>>();
        try {
            File file = new File(path, fileName);
            if (file.exists()){
                CSVReader reader = new CSVReader(new FileReader(file));
                try {
                    while (true){
                        String[] fields = reader.getAllFieldsInLine();
                        ArrayList<String> d = new ArrayList<String>();
                        for (int i=0;i<fields.length;i++){
                            d.add(fields[i]);
                        }
                        defaults.add(d);
                    }
                } catch (EOFException e) {
                }
                reader.close();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
