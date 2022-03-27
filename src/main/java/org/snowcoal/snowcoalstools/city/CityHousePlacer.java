package org.snowcoal.snowcoalstools.city;

import org.snowcoal.snowcoalstools.houseset.HouseSet;
import org.snowcoal.snowcoalstools.houseset.House;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.*;

public class CityHousePlacer {
    private City city;
    public HouseSet houseSet;

    // list of pointers of lines of gridcells
    private List<LinkedList<GridCell>> lineList = null;

    public CityHousePlacer(City city){
        this.city = city;
        lineList = new ArrayList<LinkedList<GridCell>>();
    }

    /**
     * placeHouses
     *
     * loads and places houses into city
     *
     * returns true on success, false on failure
     */
    public boolean placeHouses(HouseSet houseSet){
        this.houseSet = houseSet;

        // first ensure cliffs are correct
        for(GridCell cell: city.cityCells){
            if(cell.isCliff){
                cell.type = 5;
            }
        }

        List<GridCell> cornerCells = new ArrayList<GridCell>();
        List<GridCell> lineCells = new ArrayList<GridCell>();

        // first sort out all corner gridcells
        for(GridCell cell: city.cityCells){
            // first check if its a house
            if(cell.type == 2){
                // for each of the 4 direct neighbors, check if house (could error if house right next to edge)
                int n = 0x00;
                // shift n then OR with left neighbor
                n = ((n << 1) | (((city.grid[cell.i_index][cell.j_index - 1]).type == 2) ? 1 : 0));
                // do same thing with right neighbor
                n = ((n << 1) | (((city.grid[cell.i_index][cell.j_index + 1]).type == 2) ? 1 : 0));
                // do same thing with top neighbor
                n = ((n << 1) | (((city.grid[cell.i_index - 1][cell.j_index]).type == 2) ? 1 : 0));
                // do same thing with bottom neighbor
                n = ((n << 1) | (((city.grid[cell.i_index + 1][cell.j_index]).type == 2) ? 1 : 0));

                // use giant fucking switch statement to find rotation and type of every single house

                // left|right|top|bottom
                // 0 rot = "i, L, T, +", 1 rot = 90, 2 rot = 180, 3 rot = 270
                // for non-corner houses: 0 rot = H, 1 rot = "I"
                switch (n){
                    // no neighbors
                    case 0b00000000:
                        cell.corner_type = 0;
                        cell.corner_rotation = city.utils.random.nextInt() % 3;
                        cornerCells.add(cell);
                        break;
                    // 1 neighbor
                    case 0b00000001:
                        cell.corner_type = 1;
                        cell.corner_rotation = 2;
                        cornerCells.add(cell);
                        break;
                    case 0b00000010:
                        cell.corner_type = 1;
                        cell.corner_rotation = 0;
                        cornerCells.add(cell);
                        break;
                    case 0b00000100:
                        cell.corner_type = 1;
                        cell.corner_rotation = 1;
                        cornerCells.add(cell);
                        break;
                    case 0b00001000:
                        cell.corner_type = 1;
                        cell.corner_rotation = 3;
                        cornerCells.add(cell);
                        break;
                    // 2 neighbors no corner
                    case 0b00000011:
                        cell.corner_type = -2;
                        cell.corner_rotation = 1;
                        lineCells.add(cell);
                        break;
                    case 0b00001100:
                        cell.corner_type = -2;
                        cell.corner_rotation = 0;
                        lineCells.add(cell);
                        break;
                    // 2 neighbors yes corner
                    case 0b00000101:
                        cell.corner_type = 2;
                        cell.corner_rotation = 1;
                        cornerCells.add(cell);
                        break;
                    case 0b00000110:
                        cell.corner_type = 2;
                        cell.corner_rotation = 0;
                        cornerCells.add(cell);
                        break;
                    case 0b00001001:
                        cell.corner_type = 2;
                        cell.corner_rotation = 2;
                        cornerCells.add(cell);
                        break;
                    case 0b00001010:
                        cell.corner_type = 2;
                        cell.corner_rotation = 3;
                        cornerCells.add(cell);
                        break;
                    // 3 neighbors
                    case 0b00000111:
                        cell.corner_type = 3;
                        cell.corner_rotation = 3;
                        cornerCells.add(cell);
                        break;
                    case 0b00001011:
                        cell.corner_type = 3;
                        cell.corner_rotation = 1;
                        cornerCells.add(cell);
                        break;
                    case 0b00001101:
                        cell.corner_type = 3;
                        cell.corner_rotation = 0;
                        cornerCells.add(cell);
                        break;
                    case 0b00001110:
                        cell.corner_type = 3;
                        cell.corner_rotation = 2;
                        cornerCells.add(cell);
                        break;
                    // 4 neighbors
                    case 0b00001111:
                        cell.corner_type = 4;
                        cell.corner_rotation = city.utils.random.nextInt() % 3;
                        cornerCells.add(cell);
                        break;
                }
            }
        }

        // find all lines between corners
        for(GridCell cell:lineCells){
            // first check if its not been visited
            if(!cell.visitedLine){
                // mark it as visited
                cell.visitedLine = true;
                // allocate a new line
                LinkedList<GridCell> cellLine = new LinkedList<GridCell>();
                // add the line to the line list
                lineList.add(cellLine);
                // add the cell to the line
                cellLine.add(cell);

                // if rotation is "H", search right
                if(cell.corner_rotation == 0){
                    GridCell rightCell = cell;
                    GridCell cur_cell = cell;
                    while(true){
                        // set current to the cell to the right of the initial one
                        rightCell = city.grid[cur_cell.i_index][cur_cell.j_index + 1];
                        // if it hasent been visited and its a house between corners, then add it to list
                        if(rightCell.corner_type == -2){
                            cellLine.add(rightCell);
                            rightCell.visitedLine = true;
                            cur_cell = rightCell;
                        }
                        // end loop if it hits a cell that isnt a house between corners
                        else{
                            break;
                        }
                    }
                }
                // if rotation is "I", search down
                else if(cell.corner_rotation == 1){
                    GridCell downCell = cell;
                    GridCell cur_cell = cell;
                    while(true){
                        // set current to the cell under the initial one
                        downCell = city.grid[cur_cell.i_index + 1][cur_cell.j_index];
                        // if it hasent been visited and its a house between corners, then add it to list
                        if(downCell.corner_type == -2){
                            cellLine.add(downCell);
                            downCell.visitedLine = true;
                            cur_cell = downCell;
                        }
                        // end loop if it hits a cell that isnt a house between corners
                        else{
                            break;
                        }
                    }
                }
            }
        }

        // assign houses to all corners
        if(!assignHousesToCorners(cornerCells)){
            return false;
        }

        // assign houses to all lines
        if(!assignHousesToLines()){
            return false;
        }

        // assign remaining splits
        for(GridCell cell: city.cityCells){
            int i = cell.i_index;
            int j = cell.j_index;
            // check if cell has even position and if its a corner
            if((i%2 == 0 || j%2 == 0) && cell.corner_type >= 1){
                // check surrounding cells to find if any of them are corners
                GridCell[] neighbors = {city.grid[i][j+1], city.grid[i][j-1], city.grid[i+1][j], city.grid[i-1][j]};
                for(int n = 0; n < 4; n++){
                    GridCell ncell = neighbors[n];
                    if(ncell.corner_type >= 1){
                        // calculate constants
                        int z_diff = (ncell.i_index - i);
                        int x_diff = (ncell.j_index - j);
                        int offset = ((city.grid_cell_size - 1) >> 1) + 1;
                        int posx = cell.pos_x + offset;
                        int posz = cell.pos_z + offset;
                        int rot = -1;
                        // calculate rotation
                        if(z_diff == 0) rot = 0;
                        else if(x_diff == 0) rot = 1;

                        // add a new split (idk why this works but it does via trial/error)
                        if(!addSplit(posx + x_diff*(offset) - Math.abs(x_diff), cell.pos_y, posz + z_diff*(offset) - Math.abs(z_diff), rot)){
                            return false;
                        }
                    }
                }
            }
        }


        return true;
    }


    /**
     * assignHousesToCorners
     *
     * assigns houses to all corners
     *
     * cornerCells - list of cells that are corners
     *
     * returns true on success, false on failure
     */
    boolean assignHousesToCorners(List<GridCell> cornerCells){
        for(GridCell cell: cornerCells){
            // get a random house with same type
            House houseID = houseSet.pickRandHouseByWidth(cell.corner_type, -1, -1);
            if(houseID == null) return false;

            // make new city house and set its data
            CityHouse city_house = new CityHouse();

            // location is at CENTER of house
            city_house.pos_x = cell.pos_x + ((city.grid_cell_size - 1) >> 1) + 1;
            city_house.pos_y = cell.pos_y;
            city_house.pos_z = cell.pos_z + ((city.grid_cell_size - 1) >> 1) + 1;

            city_house.rotation = cell.corner_rotation;
            city_house.house_ptr = houseID;

            city.houseList.add(city_house);
        }
        return true;
    }


    /**
     * assignHousesToLines
     *
     * assigns houses to all corners.
     *
     * returns true on success, false on failure
     */
    boolean assignHousesToLines(){
        for(LinkedList<GridCell> line: lineList){
            // calculate various needed values
            int line_cell_width = line.size();
            int line_rotation = line.getFirst().corner_rotation;
            // used for assertion check
            int line_block_width = city.GRID_SPACE + city.GRID_SPACE * line_cell_width + city.grid_cell_size * line_cell_width;
            // store beginning and end of line
            GridCell line_begin = line.getFirst();
            // store y val of line
            int line_y_val = line_begin.pos_y;


            List<CityHouse> lineHouses = new ArrayList<CityHouse>();

            // keep track of how many houses have been placed
            int count = 0;
            while(true){
                int houses_left = line_cell_width - count;

                if(houses_left > 1){
                    // pick a random house of width >= grid_cell_size
                    House houseID = houseSet.pickRandHouseByWidth(-2, city.grid_cell_size + 1, Integer.MAX_VALUE);
                    if(houseID == null) return false;

                    // add the first house
                    CityHouse city_house = new CityHouse();
                    // assign its rotation and ID pointer
                    city_house.house_ptr = houseID;
                    city_house.rotation = line_rotation;
                    lineHouses.add(city_house);
                    count++;

                    int width = city_house.getCityHouseWidth();
                    // if the first house was a grid_cell_size house, dont add another
                    if(width == city.grid_cell_size){
                        continue;
                    }
                    // if the first house was grid_cell_size * 2 + 1, update count and dont add another
                    else if(width == city.grid_cell_size*2 + 1){
                        count++;
                        continue;
                    }
                    // otherwise add a smaller house of corresponding width to add 2 houses total during current pass
                    else{
                        int width2 = 2*city.grid_cell_size - width;
                        // pick a random house of corresponding width to add to 2 houses
                        House houseID2 = houseSet.pickRandHouseByWidth(-2, width2, width2);
                        if(houseID2 == null) return false;

                        // add the 2nd house
                        CityHouse city_house2 = new CityHouse();
                        city_house2.house_ptr = houseID2;
                        city_house2.rotation = line_rotation;
                        lineHouses.add(city_house2);
                        count++;
                    }
                }
                else if(houses_left == 1){
                    // add a house of width equal to grid cell size
                    House houseID = houseSet.pickRandHouseByWidth(-2, city.grid_cell_size, city.grid_cell_size);
                    CityHouse city_house = new CityHouse();
                    // assign its rotation and ID pointer
                    city_house.house_ptr = houseID;
                    city_house.rotation = line_rotation;
                    lineHouses.add(city_house);
                    count++;
                }
                // break if theres no more houses to add
                else if(houses_left == 0){
                    break;
                }
            }

             // assertion sanity check
             int total_width = 0;
             for(CityHouse house:lineHouses){
                 total_width += house.getCityHouseWidth();
             }
             total_width += (lineHouses.size() + 1);
             if(!(total_width == line_block_width)){
                 System.out.println("SANITY CHECK ERROR: Line not equal to expected width. Expected: " + line_block_width + ", got: " + total_width);
                 return false;
             }

            // randomly shuffle list
            Collections.shuffle(lineHouses);

            int line_z_val = 0;
            int line_x_val = 0;

            // do if line goes from left to right ("H")
            if(line_rotation == 0){
                // if line goes from left to right, its Z val is constant and is the centered line in the middle of the cells
                line_z_val = line_begin.pos_z + ((city.grid_cell_size - 1) >> 1) + 1;
                line_x_val = line_begin.pos_x - city.half_grid_space;

                for(CityHouse house: lineHouses){
                    // add a split
                    if(!addSplit(line_x_val, line_y_val, line_z_val, line_rotation)){
                        return false;
                    }
                    line_x_val += city.GRID_SPACE;
                    // set the houses positional data and add it to the list
                    int width = house.getCityHouseWidth();
                    house.pos_z = line_z_val;
                    house.pos_y = line_y_val;
                    house.pos_x = line_x_val + ((width - 1) >> 1) + 1;

                    city.houseList.add(house);

                    line_x_val += width;
                }
                // add last split
                if(!addSplit(line_x_val, line_y_val, line_z_val, line_rotation)){
                    return false;
                }
            }
            // do if line goes from top to bottom ("I")
            else if(line_rotation == 1){
                // if line goes from top to bottom, its X val is constant and is the centered line in the middle of the cells
                line_x_val = line_begin.pos_x + ((city.grid_cell_size - 1) >> 1) + 1;
                line_z_val = line_begin.pos_z - city.half_grid_space;

                for(CityHouse house: lineHouses){
                    // add a split
                    if(!addSplit(line_x_val, line_y_val, line_z_val, line_rotation)){
                        return false;
                    }
                    line_z_val += city.GRID_SPACE;
                    // set the houses positional data and add it to the list
                    int width = house.getCityHouseWidth();
                    house.pos_z = line_z_val + ((width - 1) >> 1) + 1;
                    house.pos_y = line_y_val;
                    house.pos_x = line_x_val;

                    city.houseList.add(house);

                    line_z_val += width;
                }
                // add last split
                if(!addSplit(line_x_val, line_y_val, line_z_val, line_rotation)){
                    return false;
                }
            }
        }

        return true;
    }


    /**
     * addSplit
     *
     * adds a new split type house to the house set
     *
     * returns true on success, false on failure
     *
     */
    boolean addSplit(int posx, int posy, int posz, int rot)
    {
        // add a house of width 1
        House houseID = houseSet.pickRandHouseByWidth(-2, 1, 1);
        if(houseID == null) return false;

        CityHouse city_house = new CityHouse();

        if(rot == 0){
            city_house.pos_x = posx + ((city.GRID_SPACE - 1) >> 1) + 1;
            city_house.pos_z = posz;
        }
        else if(rot == 1){
            city_house.pos_x = posx;
            city_house.pos_z = posz + ((city.GRID_SPACE - 1) >> 1) + 1;
        }

        // set its information
        city_house.pos_y = posy;

        city_house.rotation = rot;
        city_house.house_ptr = houseID;

        // add it to the list
        city.houseList.add(city_house);

        return true;
    }


}
