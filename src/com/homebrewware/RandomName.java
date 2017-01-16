/**
 *    Filename: RandomName.java
 *     Version: 0.9.0
 * Description: Random Name
 *     License: GPLv2
 *        Date: 2017-01-14
 *
 * We use this class to generate a Random name.
 * We'll store the existing one (just in case)
 * for the toString() and use generate()
 * to get a new one
 *
 * @author Doug Edey
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomName {

    private String name;

    List<String> colours = Arrays.asList(new String[] {"Amber", "Beige", "Black", "Brown", "Golden", "Red",
                                         "Ruby", "Yellow"});

    List<String> famous = Arrays.asList(new String[] {"Ace Ventura", "Barney", "Beazelbub", "Big Bird",
              "Beeker","Bo and Luke Duke", "Bob and Doug McKenzie", "Bubba",
              "Buddha", "Cletus", "Count Chocula", "Charlie Sheen","Dan Quayle",
              "Dubbya", "Elvis", "Finnigan", "Gary Coleman", "Gary Busey", "General Tsao",
              "Hamburgler", "Hannibal", "Jacques Cousteau", "Kermit",
              "Krusty", "L. Ron Hubbard", "Napolean", "Madonna", "Marcel Mersault",
              "Mikhail Baryshnikov", "Mothra", "Muammar Gaddafi", "Obi Wan Kenobe",
              "Pablo Picasso", "Paul Newman", "Phil McCrackin", "Satan", "Santa",
              "Spiderman", "Winston Churchill"});

    List<String> animals = Arrays.asList(new String[] {"Ant Farm", "Ardvaark", "Beaver", "Centipede", "Chicken",
              "Dragon", "Emu", "Ewok", "Flying Squirrel", "Gloworm", "Gnu", "Hippo",
              "Jellyfish", "Landshark", "Llama", "Lemur", "Loch Ness Monster",
              "Manatee", "Mantaray", "Moose", "Naked Mole Rat", "Ocelot", "Ookpik",
              "Polecat", "Porpoise", "Pufferfish", "Rat",
              "Sharks With Frickin Laser Beams Tied To Their Heads", "Sheep", "Shitszu", "Slimemold", "Sloth",
              "Smelly Cat", "Smurf", "Squid", "Tribles", "Vampire Bat", "Weasels",
              "Wombat"});

    List<String> adjectives = Arrays.asList(new String[] {"Achin\'", "Ancient", "Arrogant", "Bald", "Bionic",
              "Bitchin\'", "Blessed", "Burning", "Crackalicious", "Cranky",
              "Craptacular", "Crunk","Cunning", "Curious", "Curried",
              "Dope","Drunk", "El Dente", "Electric", "Evil", "Famous",
              "Faaaaantastick", "Fat", "Farty", "Ferrell", "Firkin", "Flacid",
              "Flaming", "Flatulent", "Flying", "Frickin", "Frisky", "Glutinous",
              "Gnarly", "Grunting", "Gurgling","Hairy", "Happy", "Heezy","Hoppy",
              "Insane", "Invisible", "Irish", "Joyous", "Limpid", "Lost", "Moronic",
              "Nefarious", "Nuclear", "Non-Euclidean", "Old", "Phat", "Poisonous",
              "Polyamorous", "Portentious", "Randy", "Retarded", "Salty",
              "Sizzling", "Skinny", "Sloshed", "Smutty", "Sneaky", "Spiced",
              "Stinky", "Transgendered", "Stumbling", "Subatomic", "Succulent",
              "Super Duper", "Tipsy", "Totally Radical", "Tubular", "Turgid",
              "Twizzy","Unbefreakinglievable", "Unspeakable", "Unholy", "Viscious",
              "Wicked", "Wobbly", "Yummy"});

    List<String> nouns = Arrays.asList(new String[]{"Barley", "Barrel", "Bastard", "Belch", "Black Hole",
              "Brain", "Breath", "Butt", "Carboy", "Chewbacca Defense",
              "Crackhouse", "Dram", "Egg", "Face", "Firkin", "Flaggon", "Forbidden Donut",
              "Gas Cloud", "Glove", "Halitosis", "Hippie", "Hoser", "Howling Savage",
              "Juice", "Kettle", "Klingon", "Kung Fu", "Liquid", "Love",
              "Mash", "Mojo", "Monk", "Moron", "Mushroom Cloud", "Ninja", "Nun",
              "Pinto", "Poison", "President", "Pyramid", "Rat Hole", "Road Rage",
              "Robot", "Schadenfreude", "Shlong", "Scourge", "Show", "Swill",
              "Tongue", "Tornado", "Tun", "Zen"});

    List<String> events = Arrays.asList(new String[]{"April Fools\' Day", "Autumn", "Bastille Day",
            "Chinese New Years", "Valentine Day", "Christmas", "Cinco de Mayo", "Easter", "Epiphany",
              "Fourth of July", "Full Moon", "Groundhog Day", "Haloween", "Kwanzaa",
              "National Hispanic Heritage Month", "Nurses Day", "Persian New Year",
              "Pulaski Day", "Simchat Torah", "Solstice", "Spring", "St. Paddy\'s Day",
              "St. Urho\'s Day", "Sukkot", "Summer", "Thursday", "Winter"});

    List<String> styles = Arrays.asList(new String[]{"Altbier", "American Light Lager", "Barley Wine",
            "Bavarian Dunkelweizen", "Biere de Garde ", "Bock", "Brown Ale", "Dopplebock",
              "Dubble", "Extra Special Bitter", "Gueuze", "Hefeweizen", "India Pale Ale",
              "Koelsch", "Lambic", "Maerzen", "Old Ale", "Oud Bruin", "Pale Ale",
              "Pilsner", "Porter", "Rauchbier", "Russian Imperial Stout",
              "Schwartzbier", "Stout", "Tripel", "Wee Heavy", "Wit"});

    List<String> templArray = Arrays.asList(new String[]{"ad", "adad", "adan", "adno", "adnm", "clev", "anno",
              "nmadan", "nmadclan", "adevcl", "cladno", "fun", "artist"});

    private List<String> getStyles(){
           return styles;
    }

    private String select(List<String> l) {
            Collections.shuffle(l);
            int i = new Random().nextInt(l.size());
           return l.get(i).toString();
    }

    public void generate(){
           name = "";

           String s = select(templArray);
           if(s.equals("ad")){
               //adjective
               name += select(adjectives);
           } else if (s.equals("adad")) {
               // adjective + adj
               name += select(adjectives) + " " + select(adjectives);
           } else if (s.equals("adan")) {
               // # adjective + animal
               name += select(adjectives) + " " + select(animals);
           } else if (s.equals("adno")) {
               // adjective + noun
               name += select(adjectives) + " " + select(nouns);
           } else if (s.equals("adnm")) {
               // adjective + famous person
               name += select(adjectives) + " " + select(famous);
           } else if (s.equals("clev")) { //color + event
               name += select(colours) + " " + select(events);
           } else if (s.equals("anno")) { // animal-noun
               name += select(animals) + "-" + select(nouns);
           } else if (s.equals("nmadan")) {// famous pers + adjective + animal
               name += select(famous) + "'s " + select(adjectives) + " " + select(animals);
           } else if (s.equals("nmadclan")) { // famous + adj + colour + animal
               name += select(famous) + "'s " + select(adjectives) + " " +
        select(colours) + " " + select(animals);
           } else if (s.equals("adevcl")) {// adj + event + color
               name += select(adjectives) + " " + select(events) + " " +
        select(colours);
           } else if (s.equals("cladno")) {// color + adj + noun
               name += select(colours) + " " + select(adjectives) + " " + select(nouns);
           } else if (s.equals("fun")){
               int j = new Random().nextInt(1);

               if (j == 1) {//: # where's my?
                   name += "Where's my " + select(animals) + "?";
               } else if (j == 0) {//: # name + name
                   name += select(famous) + " and " + select(famous) + "'s "
                           + select(adjectives);
               }
           } else if (s.equals("artist")) { //: # artist
               name += "The " + select(animals) +" Formerly Known As " +
        select(famous) + "'s " + select(adjectives);
           }
           name += ' ' + select(styles);
    }

    public String getName() {
        return name;
    }
}
