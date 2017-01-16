/**
 *    Filename: Acid.java
 *     Version: 0.9.0
 * Description: Acid
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

public class Acid {
    final private String name;
    final private double pK1;
    final private double pK2;
    final private double pK3;
    final private double molWt;
    final private double mgPerML;

    static final public String ACETIC = "Acetic";
    static final public String CITRIC = "Citric";
    static final public String HYDROCHLORIC = "Hydrochloric";
    static final public String LACTIC = "Lactic";
    static final public String PHOSPHORIC = "Phosphoric";
    static final public String SULFURIC = "Sulfuric";
    static final public String TARTRIC = "Tartric";
    static final public String[] acidNames = {ACETIC, CITRIC, HYDROCHLORIC, LACTIC, PHOSPHORIC, SULFURIC, TARTRIC};

    static final public Acid[] acids = {
        new Acid(ACETIC, 4.75, 20, 20, 60.05, 0),
        new Acid(CITRIC, 3.14, 4.77, 6.39, 192.13, 0),
        new Acid(HYDROCHLORIC, -10, 20, 20, 36.46, 319.8), // 28% hcl
        new Acid(LACTIC, 3.08, 20, 20, 90.08, 1068), // 88% lactic
        new Acid(PHOSPHORIC, 2.12, 7.20, 12.44, 98.00, 292.5), // 25% phosphoric
        new Acid(SULFURIC, -10, 1.92, 20, 98.07, 0),
        new Acid(TARTRIC, 2.98, 4.34, 20, 150.09, 0)
    };

    public Acid(String name, double pK1, double pK2, double pK3, double molWt, double mgPerML) {
        this.name = name;
        this.pK1 = pK1;
        this.pK2 = pK2;
        this.pK3 = pK3;
        this.molWt = molWt;
        this.mgPerML = mgPerML;
    }

    public double getMolWt() {
        return molWt;
    }

    public String getName() {
        return name;
    }

    public double getPK1() {
        return pK1;
    }

    public double getPK2() {
        return pK2;
    }

    public double getPK3() {
        return pK3;
    }

    public double getMgPerL() {
        return mgPerML;
    }

    public boolean isLiquid() {
        if (mgPerML == 0) {
            return false;
        } else {
            return true;
        }
    }

    public String getAcidUnit() {
        if (isLiquid()) {
            return Quantity.ML;
        } else {
            return Quantity.MG;
        }
    }

    public static Acid getAcidByName(String name) {
        for (int i = 0; i < Acid.acids.length; i++) {
            if (Acid.acids[i].getName().equals(name)) {
                return Acid.acids[i];
            }
        }

        return null;
    }
}
