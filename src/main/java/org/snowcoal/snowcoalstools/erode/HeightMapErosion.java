package org.snowcoal.snowcoalstools.erode;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.world.block.BlockState;
import org.snowcoal.snowcoalstools.SnowcoalsTools;
import org.snowcoal.snowcoalstools.utils.HeightMap;
import org.snowcoal.snowcoalstools.utils.XZBlock;

import java.util.*;


abstract class HeightMapErosion {
    // private vars
    private Region sel;
    private Player player;
    private SnowcoalsTools instance;
    private int changedBlocks = 0;
    private HeightMap heightMap;
    private int time;
    private int intensity;
    private EditSession editSession;
    private Map<XZBlock, BlockState> highestBlocks;

    public HeightMapErosion(Region sel, Player player, SnowcoalsTools instance, int time, int intensity){
        this.sel = sel;
        this.player = player;
        this.instance = instance;
        this.time = time;
        this.intensity = intensity;

        // create EditSession for edit
        editSession = WorldEdit.getInstance().newEditSessionBuilder()
                .world(this.player.getWorld())
                .actor((Actor)this.player)
                .allowedRegionsEverywhere()
                .limitUnlimited()
                .compile()
                .build();

        // call all operation methods
        populateHeightmap();
        doErosion();
        placeHeightmap();
    }

    private void populateHeightmap(){
        BlockVector3 min = this.sel.getMinimumPoint();
        BlockVector3 max = this.sel.getMaximumPoint();

        // instantiate heightmap
        heightMap = new HeightMap(this.sel.getWidth(), this.sel.getLength(), 1.0);

        // instantiate set of saved blocks
        highestBlocks = new HashMap<>();

        // loop through each x/z position
        for (int x = min.getX(); x <= max.getX(); x++) {
            for (int z = min.getZ(); z <= max.getZ(); z++) {
                // for each x/z point, search down to find highest not air block
                for (int y = max.getY(); y >= min.getY(); y--) {
                    BlockState block = editSession.getBlock(x, y, z);
                    // skip if air
                    if (block.isAir()) {
                        continue;
                    }
                    else{
                        // save the block for later
                        highestBlocks.put(new XZBlock(x, z), block);
                        // set heightmap height
                        heightMap.setHeight(x, z, (double) y);

                        break;
                    }
                }
            }
        }
    }

    abstract void doErosion();

    private void placeHeightmap() {
        // replace selection area with heightmap
        BlockVector3 min = this.sel.getMinimumPoint();
        BlockVector3 max = this.sel.getMaximumPoint();

        for (int x = min.getX(); x <= max.getX(); x++) {
            for (int z = min.getZ(); z <= max.getZ(); z++) {
                // get the xz block
                BlockState block = highestBlocks.get(new XZBlock(x,z));
                // get max height
                int maxHeight = (int) Math.round(heightMap.getHeight(x, z));
                // set all y values under max to the block
                for (int y = min.getY(); y <= max.getY(); y++) {
                    if(y <= maxHeight){
                        editSession.setBlock(x, y, z, block);
                        changedBlocks ++;
                    }
                    else break;
                }
            }
        }
    }
}
