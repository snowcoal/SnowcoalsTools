package org.snowcoal.citygenerator.houseset;
import com.sk89q.worldedit.math.BlockVector3;

public class House {
    // ID of house (for mapping it in game)
    public int ID;
    // -1 = unassigned, -2 = house between corners, 0 = no neighbors, 1 = "i", 2 = "L", 3 = "T", 4 = "+"
    public int type;
    // width of house. -1 means dont care and will be equal to city grid width when added to a city
    public int width;

    // copy from location positional data
    public int cpy_pt_x;
    public int cpy_pt_y;
    public int cpy_pt_z;

    // pos1 data (one point of the bounding box)
    public int pos1_x;
    public int pos1_y;
    public int pos1_z;

    // pos2 data (2nd point of the bounding box)
    public int pos2_x;
    public int pos2_y;
    public int pos2_z;
}
