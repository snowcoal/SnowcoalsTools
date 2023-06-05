package org.snowcoal.snowcoalstools.city;

import java.util.ArrayList;
import java.util.List;

import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Polygonal2DRegion;
import org.snowcoal.snowcoalstools.utils.RandUtils;
import org.snowcoal.snowcoalstools.houseset.HouseSet;

public class City {
    // member vars

    public Player player;

    // position of corner of city
//    private int start_x = Integer.MAX_VALUE;
//    private int start_z = Integer.MAX_VALUE;
    public int start_x;
    public int start_z;

    // number of steps on city from heightmap
    private double num_steps;

    // length and width of the city
    public int cityWidth;
    public int cityLength;

    // length and width of grid
    public int gridWidth;
    public int gridLength;

    // width of each square (does NOT include 1 block padding on each side)
    public int grid_cell_size;

    // half the size of the grid space
    public int half_grid_space;

    // bias of left/right vs up/down
    public int lrbias;

    // check if generation failed or succeeded
    public boolean failFlag = false;

    // constants

    // size of space
    public final int GRID_SPACE = 1;
    private final double PX_PCT = 0.5;
    private final double OUTER_RD_PCT = 0.9;
    private final double HEIGHT_PCT = 0.17;
    private final double LRBIAS = 90;
    private final double NUM_ADJ = 8.0;
    private final int BITMASK = 0x03;
    private final int BITMASK2 = 0x01;
    private final int NUM_LOOP_PASSES = 2;

    // selection
    Polygonal2DRegion citySelection;
    // maze generator
    private CityMaze mazeGen = null;
    // utilities object
    public RandUtils utils = null;
    // city house placer object
    public CityHousePlacer placer = null;
    // city paster object
    public CityPaster paster = null;

    // grid of city. Made up of grid nodes (for now might use houses later)
    // grid is made up of nxn squares with a border of 1 square around the entire image
    public GridCell[][] grid = null;
    // list of all gridcell pointers in the city
    public List<GridCell> cityCells = null;
    // list of houses
    public List<CityHouse> houseList = null;


    public City(Player player, int grid_box_width, int lr_bias, Polygonal2DRegion sel){
        this.player = player;

        // find minimum corner bounding box of region
        BlockVector3 min_point = sel.getMinimumPoint();
        start_x = min_point.getX();
        start_z = min_point.getZ();

//        List<BlockVector2> blocks = sel.polygonize(-1);
//        for(BlockVector2 b: blocks){
//            if(b.getX() < start_x) start_x = b.getX();
//            if(b.getZ() < start_z) start_z = b.getZ();
//        }

        // init member vars
        citySelection = sel;
        lrbias = lr_bias;
        grid_cell_size = grid_box_width;
        cityWidth = sel.getWidth();
        cityLength = sel.getLength();

        // init member objects
        utils = new RandUtils();
        mazeGen = new CityMaze(this);
        placer = new CityHousePlacer(this);
        paster = new CityPaster(this);

        // add outer edge of grid cells around original image
        gridWidth = cityWidth/(grid_cell_size + GRID_SPACE) + 4;
        gridLength = cityLength/(grid_cell_size + GRID_SPACE) + 4;
        // get area of cell and space
        int grid_cell_area = (int) Math.pow((double)grid_cell_size,2.0);
        half_grid_space = ((GRID_SPACE - 1) >> 2) + 1;

        // allocate memory for lists
        grid = new GridCell[gridLength][gridWidth];
        cityCells = new ArrayList<GridCell>();
        houseList = new ArrayList<CityHouse>();

        // init grid cells
        for(int i = 0; i < gridLength; i++){
            for(int j = 0; j < gridWidth; j++){
                GridCell cell = new GridCell();

                // the corner position needs to be -squarew,-squarew
                cell.pos_x = (j * (grid_cell_size + GRID_SPACE) - (grid_cell_size)) + start_x;
                cell.pos_z = (i * (grid_cell_size + GRID_SPACE) - (grid_cell_size)) + start_z;
                cell.pos_y = min_point.getY();

                cell.i_index = i;
                cell.j_index = j;

                // find if the cell needs to be in city or not
                int count = 0;
                // loop for each pixel within the current grid cell
                for(int k = cell.pos_x; k < cell.pos_x + grid_cell_size; k++){
                    for(int l = cell.pos_z; l < cell.pos_z + grid_cell_size; l++){
                        // check to see if block is in region
                        if(sel.contains(k, l)){
                            count++;
                        }
                    }
                }

                // check whether enough pixels are white
                if((double)count / (double)grid_cell_area > PX_PCT){
                    cell.inCity = true;
                    // add cell to list
                    cityCells.add(cell);
                }
                else{
                    cell.inCity = false;
                }

                // add cell to grid
                grid[i][j] = cell;
            }
        }

        // set height here

        // set the type of each cell prior to maze generation
        // uses "shitty edge detection"
        for(int i = 2; i < gridLength - 2; i++){
            for(int j = 2; j < gridWidth - 2; j++){
                GridCell cell = grid[i][j];
                if(cell.inCity){
                    // count 8 surrounding cells
                    int edge_cnt = 0;
                    int height_cnt = 0;
                    for(int k = -1; k <= 1; k++){
                        for(int l = -1; l <= 1; l++){
                            // count city cells in surrounding cells
                            if(grid[i+k][j+l].inCity){
                                edge_cnt++;
                            }
                            // count lower height surrounding cells
                            if(grid[i+k][j+l].pos_y < cell.pos_y){
                                height_cnt++;
                            }
                        }
                    }
                    // set it as a border if less than some percentage of its surrounding cells are in the city
                    if((double)(edge_cnt-1) / NUM_ADJ < OUTER_RD_PCT){
                        cell.type = 3;
                        cell.visited = true;
                    }
                    // set it as a house if it has an odd position
                    else if(!(i%2 == 0 && j%2 == 0)){
                        cell.type = 2;
                    }
                    // finally set it as a road
                    else{
                        cell.type = 1;
                    }
                }
            }
        }

        // if maze gen fails or size is 0, set the fail flag to true
        if(mazeGen.generateMaze() == -1 || cityCells.size() == 0){
            failFlag = true;
        }
    }

    // checks if generation succeeded or failed
    public boolean checkFailed(){
        return failFlag;
    }

    // returns stats of city
    public CityStats getCityStats(){
        int count = 0;
        // count all houses (could be done during generation but this is better
        // since this ensures the output is always accurate, and its not called often)
        for(GridCell cell: cityCells){
            if(cell.type == 2){
                count++;
            }
        }
        CityStats stats = new CityStats(cityCells.size(), count, citySelection.getVolume());
        return stats;
    }

    // gets houseSet
    public HouseSet getHouseSet(){
        return this.placer.houseSet;
    }


}
