package org.snowcoal.snowcoalstools.city;

public class GridCell {
    // positions of node in grid
    public int i_index = -1;
    public int j_index = -1;

    // positions of node (0-based from top left corner of input image)
    // x is left/right, z is up/down
    public int pos_x = 0;
    public int pos_z = 0;

    // vertical height - top left corner has y=0
    public int pos_y = 0;

    // tracks the type of the cell
    // 0 = none, 1 = road, 2 = house, 3 = border, 4 = debug purposes,
    public int type = 0;

    // -1 = unassigned, -2 = house between corners, 0 = no neighbors, 1 = "i", 2 = "L", 3 = "T", 4 = "+"
    public int corner_type = -1;

    // 0 = "i L T +", 1 = 90 clockwise, 2 = 180 clockwise, 3 = 270 clockwise, -1 unassigned
    // for non-corner houses: 0 rot = H, 1 rot = "I"
    // "+" gets assigned random rotation value
    public int corner_rotation = -1;

    // average luminance of cell
    public double avg_lum = 0;

    // Flags used for various purposes

    // tracks whether the current node is in the city or not
    public boolean inCity = false;

    // marks if visited for maze generator
    public boolean visited = false;

    // tracks if its a cliff or not
    public boolean isCliff = false;

    // tracks if visted for line checker
    public boolean visitedLine = false;

    // tracks if visted by BFS
    public boolean visitedBFS = false;
}
