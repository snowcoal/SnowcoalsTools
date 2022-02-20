package org.snowcoal.citygenerator.city;

import org.jetbrains.annotations.NotNull;
import org.snowcoal.citygenerator.houseset.House;

public class CityHouse implements Comparable<CityHouse>{
    // positional data (center point based)
    public int pos_x = 0;
    public int pos_z = 0;
    public int pos_y = 0;
    // rotation same as corner_rotation
    public int rotation;
    // pointer (not rlly just copied var name from c++) to houseType of house its assigned to
    public House house_ptr;

    /**
     * getCityHouseWidth
     *
     * returns the width of input cityHouse
     *
     */
    public int getCityHouseWidth()
    {
        return(this.house_ptr.width);
    }

    /**
     * getCityHouseType
     *
     * returns the type of input cityHouse
     *
     */
    public int getCityHouseType()
    {
        return(this.house_ptr.type);
    }

    @Override
    public int compareTo(@NotNull CityHouse b) {
        int a_ID = this.house_ptr.ID;
        int b_ID = b.house_ptr.ID;
        int a_w = this.house_ptr.width;
        int b_w = b.house_ptr.width;
        int a_r = this.rotation;
        int b_r = b.rotation;
        // try to sort by widths first
        if(a_w != b_w){
            return a_w - b_w;
        }
        // if widths are equal sort by ID
        else if(a_ID != b_ID){
            return a_ID - b_ID;
        }
        // otherwise sort by rotation
        else{
            return a_r - b_r;
        }
    }
}
