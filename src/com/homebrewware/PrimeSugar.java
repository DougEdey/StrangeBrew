/**
 *    Filename: PrimeSugar.java
 *     Version: 0.9.0
 * Description: Prime Sugar
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

public class PrimeSugar extends Ingredient {

    // base data
    private double yield;

    public PrimeSugar(){
        yield = 1.0;
    }

    public double getYield() {
        return yield;
    }

    public void setYield(double attenuation) {
        this.yield = attenuation;
    }
}
