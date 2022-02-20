package org.snowcoal.citygenerator;

import java.util.Random;

/*
 * CityUtils
 *
 * contains basic methods that are used for the city generator
 *
 */
public class CityUtils {
    public Random random;

    // City Utilities constructor
    public CityUtils(){
        random = new Random();
    }

    /*
     * randTF
     *
     * generates a true/false random output with given dist
     * dist is % chance it will be true
     * only does integer percentages
     */
    public boolean randTF(int dist){
        int x = random.nextInt(100);
        return(x <= dist);
    }
}
