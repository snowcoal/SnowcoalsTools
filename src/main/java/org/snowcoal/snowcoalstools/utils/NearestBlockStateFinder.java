package org.snowcoal.snowcoalstools.utils;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.block.BlockState;

import java.util.*;

public class NearestBlockStateFinder {
    private Region sel;
    private EditSession editSession;
    private int maxSearch;
    private List<XYZBlock> visited;

    public NearestBlockStateFinder(Region sel, EditSession editSession, int maxSearch) {
        this.sel = sel;
        this.editSession = editSession;
        this.maxSearch = maxSearch;
        visited = null;
    }

    /**
     * findNearestBlockState
     *
     * gets the nearest (BFS search) BlockState that isnt air from given position (output can be same as position)
     *
     * returns the BlockState or null if none found
     */
    public BlockState findNearestBlockState(int x, int y, int z){
        // check if initial block is not air, if so then use its material
        BlockState blockState = editSession.getBlock(x, y, z);
        if(!blockState.isAir()){
            return blockState;
        }

        // set up BFS
        LinkedList<XYZBlock> queue = new LinkedList<>();
        visited = new ArrayList<>();

        XYZBlock start = new XYZBlock(x, y, z);
        queue.add(start);
        visited.add(start);

        int numSearched = 0;

        while (queue.size() != 0){
            // get front of queue
            XYZBlock front = queue.poll();

            ArrayList<XYZBlock> neighbors = getNeighbors(front);

            for (XYZBlock nBlock: neighbors){
                // ensure the block is not visited and the selection contains it
                if(!isVisited(nBlock) && this.sel.contains(nBlock.posX, nBlock.posY, nBlock.posZ)){
                    // if its not air, choose it
                    blockState = editSession.getBlock(nBlock.posX, nBlock.posY, nBlock.posZ);
                    if(!blockState.isAir()){
                        return blockState;
                    }

                    // otherwise continue with BFS
                    visited.add(nBlock);
                    queue.add(nBlock);
                    numSearched++;

                    if(numSearched > maxSearch){
                        return null;
                    }
                }
            }
        }

        return null;
    }

    /**
     * getNeighbors
     *
     * gets the neighbors from the block that have not been visited
     *
     */
    private ArrayList<XYZBlock> getNeighbors(XYZBlock block){
        ArrayList<XYZBlock> neighbors = new ArrayList<>();

        int x = block.posX;
        int y = block.posY;
        int z = block.posZ;

        // 6 neighbors
        neighbors.add(new XYZBlock(x+1, y, z));
        neighbors.add(new XYZBlock(x-1, y, z));
        neighbors.add(new XYZBlock(x, y+1, z));
        neighbors.add(new XYZBlock(x, y-1, z));
        neighbors.add(new XYZBlock(x, y, z+1));
        neighbors.add(new XYZBlock(x, y, z-1));

        return neighbors;
    }

    /**
     * isVisited
     *
     * checks if a block is visited by the search
     *
     */
    private boolean isVisited(XYZBlock block){
        for (XYZBlock b: visited){
            if(block.equals(b)){
                return true;
            }
        }
        return false;
    }
}
