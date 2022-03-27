package org.snowcoal.snowcoalstools.city;

import org.snowcoal.snowcoalstools.CityUtils;
import java.util.ArrayList;
import java.util.List;

public class CityRoads {
    private City city = null;
    private CityUtils utils = null;

    // constants
    private final int NUM_LOOP_PASSES = 2;

    public CityRoads(City city){
        this.city = city;
        this.utils = city.utils;
    }

    /**
     * addRandomRoads
     *
     * adds random vertical/horizontal roads along the maze according to lrbias of city
     * makes house loops
     * connects extraneous houses together
     *
     * dist controls how many new roads get made
     *
     */
    public void addRandomRoads(int dist){
        // add vertical roads if most of the roads are horizontal so far
        if(city.lrbias > 50){
            for(int j = 2; j < city.gridWidth - 2; j++){
                // possibly skip entire column
                // the closer lrbias is to 50, the more columns are skipped
                if(!utils.randTF((city.lrbias - 50)*2) || j%2 == 1){
                    continue;
                }
                for(int i = 2; i < city.gridLength - 2; i++){
                    GridCell cell = city.grid[i][j];
                    if(cell.inCity && cell.type == 2 && utils.randTF(dist)){
                        cell.type = 1;
                    }
                }
            }
        }

        // add horizontal roads if most of the roads are vertical so far
        else if(city.lrbias <= 50){
            for(int i = 2; i < city.gridLength - 2; i++){
                // possibly skip entire row
                // the closer lrbias is to 50, the more rows are skipped
                if(!utils.randTF((50 - city.lrbias)*2) || i%2 == 1){
                    continue;
                }
                for(int j = 2; j < city.gridWidth - 2; j++){
                    GridCell cell = city.grid[i][j];
                    if(cell.inCity && cell.type == 2 && utils.randTF(dist)){
                        cell.type = 1;
                    }
                }
            }
        }

        // list to hold new house cells
        List<GridCell> newHouses = new ArrayList<GridCell>();
        // list to hold new road cells
        List<GridCell> roads = new ArrayList<GridCell>();
        // list to hold new road cells
        List<GridCell> houses = new ArrayList<GridCell>();

        // add house loops 2x
        for(int n = 0; n < NUM_LOOP_PASSES; n++){
            for(GridCell cell: city.cityCells){
                int house_cnt = 0;
                int road_cnt = 0;

                // check if the cell is a road and hasnt been visted yet
                if(cell.type == 1 || cell.type == 2){
                    CellCount cnt = getNumNeighbors(cell);
                    house_cnt = cnt.houseCount;
                    road_cnt = cnt.roadCount;
                }
                // otherwise skip it
                else continue;

                // only edit if it has exactly 7 houses around it
                if(house_cnt == 7){
                    int i = cell.i_index;
                    int j = cell.j_index;

                    // add it to the newRoads list to check later
                    roads.add(cell);

                    // find extra road
                    outer:
                    for(int k = -1; k <= 1; k++){
                        for(int l = -1; l <= 1; l++){
                            // the following should only happen one time per double loop
                            if(city.grid[i+k][j+l].type == 1 && !(k == 0 && l == 0)){
                                // set the road to a house
                                GridCell road = city.grid[i+k][j+l];
                                // road->type = 2;
                                newHouses.add(road);
                                break outer;
                            }
                        }
                    }
                    continue;
                }
                // only add to the list once
                if(road_cnt == 8 && n == 0){
                    houses.add(cell);
                }
            }

            for(GridCell road: newHouses){
                road.type = 2;
            }
            newHouses.clear();
        }

        // add a few more house cells
        combineAdjacentCells(roads, 1, 2);

        // add a few more road cells
        combineAdjacentCells(houses, 2, 1);
    }

    /**
     * getNumNeighbors
     *
     * gets the number of neighboring cells that have type input
     *
     */
    public CellCount getNumNeighbors(GridCell cell){
        CellCount retVal = new CellCount();
        retVal.roadCount = 0;
        retVal.houseCount = 0;

        int i = cell.i_index;
        int j = cell.j_index;

        for(int k = -1; k <= 1; k++){
            for(int l = -1; l <= 1; l++){
                if(!(k == 0 && l == 0)){
                    GridCell ncell = city.grid[i+k][j+l];
                    // update road counter
                    if((ncell.type == 1 || ncell.isCliff) && ncell.type != 2) retVal.roadCount++;
                        // update house counter
                    else if((ncell.type == 2 || ncell.isCliff) && ncell.type != 1) retVal.houseCount++;
                }
            }
        }
        return retVal;
    }

    /**
     * combineAdjacentCells
     *
     * looks through cellList for cells that have same type 2 cells apart and both surrounded
     * by cells of a different type. It then removes the cell between these two
     *
     *
     * cellList - list of cells to check over
     * baseType - type to set the cells to if needed, and type of center cell
     * checkType - type to check the surrounding cells for
     */
    public void combineAdjacentCells(List<GridCell> cellList, int baseType, int checkType){
        List<GridCell> newCellList = new ArrayList<GridCell>();
        // loop through all potential cells that could have a new connecting road
        for(GridCell cell: cellList){
            int i = cell.i_index;
            int j = cell.j_index;
            // get 4 neighbors
            GridCell[] neighbors = {city.grid[i][j+2], city.grid[i][j-2], city.grid[i+2][j], city.grid[i-2][j]};
            for(int k = 0; k < 4; k++){
                GridCell ncell = neighbors[k];
                // check if the neighbors is a road
                if(ncell.type == baseType){
                    CellCount cnt = getNumNeighbors(ncell);
                    int x = 0;
                    if(checkType == 1){
                        x = cnt.roadCount;
                    }
                    else if(checkType == 2){
                        x = cnt.houseCount;
                    }
                    if(x == 8){
                        int i_diff = (ncell.i_index - i) >> 1;
                        int j_diff = (ncell.j_index - j) >> 1;
                        // set cell between them to house
                        GridCell new_cell = city.grid[i + i_diff][j + j_diff];
                        if(new_cell.type == checkType)
                            newCellList.add(new_cell);
                    }
                }
            }
        }
        for(GridCell house: newCellList){
            house.type = baseType;
        }
    }
}
