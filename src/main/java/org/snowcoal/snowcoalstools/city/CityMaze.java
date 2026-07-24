package org.snowcoal.snowcoalstools.city;

import java.util.Stack;

import org.snowcoal.snowcoalstools.utils.RandUtils;


public class CityMaze {
    private City city = null;
    private RandUtils utils = null;

    private int[] ORDER_ARY;

    public CityMaze(City city){
        this.city = city;
        this.utils = city.utils;
        ORDER_ARY = new int[]{0, 1, 2, 3, 1, 0, 3, 2, 2, 3, 0, 1, 3, 2, 1, 0};
    }

    /**
     * generateMaze
     *
     * generates maze of roads inside city
     * returns 0 on success, -1 on failure
     *
     */
    public int generateMaze(){
        // choose initial cell
        GridCell initial = null;
        for(GridCell cell: city.cityCells){
            // find first road cell and break
            if(cell.type == 1){
                initial = cell;
                break;
            }
        }
        // if no initial cell is found, return -1
        if(initial == null){
            return -1;
        }

        // mark inital as visited and push to stack
        Stack<GridCell> stack = new Stack<GridCell>();
        stack.push(initial);
        initial.visited = true;

        while(!(stack.empty())){
            // current cell is top of stack
            GridCell cur = stack.pop();

            // if current has a neighbor
            GridCell n = pickRandNeighbor(cur, city.lrbias);
            if(n != null){
                // push current to stack
                stack.push(cur);

                // set wall between them to a road
                int i_diff = (n.i_index - cur.i_index) >> 1;
                int j_diff = (n.j_index - cur.j_index) >> 1;
                city.grid[cur.i_index + i_diff][cur.j_index + j_diff].type = 1;

                // mark neighbor as visited and push it to stack
                n.visited = true;
                stack.push(n);
            }
        }
        // return success
        return 0;
    }

    /**
     * pickRandNeighbor
     *
     * given input cell, picks random neighboring adjacent cell
     * that has not been visited. Used by maze generator.
     * neighboring cell HAS to be 2 spaces away
     *
     * input lrbias is left/right bias as opposed to up/down
     *
     * returns null if cell has no unvisited neighbors
     */
    public GridCell pickRandNeighbor(GridCell cell, int lrbias){
        GridCell[] cells = new GridCell[4];
        int row;
        GridCell output = null;
        int i = cell.i_index;
        int j = cell.j_index;
        // right/left
        cells[0] = city.grid[i][j+2];
        cells[1] = city.grid[i][j-2];
        // down/up
        cells[2] = city.grid[i+2][j];
        cells[3] = city.grid[i-2][j];

        // if true then left/right comes first
        boolean lr = utils.randTF(lrbias);
        // if true then plus comes first
        boolean plus = utils.randTF(50);

        // find which row of the order table to use
        if(!lr && !plus) row = 0;
        else if(!lr && plus) row = 1;
        else if(lr && !plus) row = 2;
        else row = 3;

        for(int n = 0; n < 4; n++){
            int index = ORDER_ARY[row*4 + n];
            // do only if cell is a road and is not visited
            if(!(cells[index].visited) && cells[index].type == 1){
                // update output
                output = cells[index];
            }
        }

        // assertion check
        if(output != null){
            assert(output.type == 1 && !(output.visited));
        }

        return(output);
    }


}
