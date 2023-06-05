package org.snowcoal.snowcoalstools.erode;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.convolution.HeightMapFilter;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.block.BlockState;
import com.sk89q.worldedit.world.block.BlockStateHolder;
import com.sk89q.worldedit.world.block.BlockType;
import com.sk89q.worldedit.world.block.BlockTypes;
import org.snowcoal.snowcoalstools.SnowcoalsTools;
import org.snowcoal.snowcoalstools.utils.DoubleVec3;
import org.snowcoal.snowcoalstools.utils.HeightMap;
import org.snowcoal.snowcoalstools.utils.NearestBlockStateFinder;
import org.snowcoal.snowcoalstools.utils.XZBlock;

import java.util.*;


abstract class HeightMapErosion {
    // member vars
    public Region sel;
    public int time;
    public int intensity;
    private Player player;
    private SnowcoalsTools instance;
    private long changedBlocks = 0;
    private HeightMap heightMap;
    private EditSession editSession;
    private Map<XZBlock, BlockState> highestBlocks;
    public int width;
    public int length;

    /**
     * HeightMapErosion
     *
     * constructor creates an editSession
     */
    public HeightMapErosion(Region sel, Player player, SnowcoalsTools instance, int time, int intensity){
        this.sel = sel;
        this.player = player;
        this.instance = instance;
        this.time = time;
        this.intensity = intensity;
        this.width = sel.getWidth();
        this.length = sel.getLength();

        // create EditSession for edit
        editSession = WorldEdit.getInstance().newEditSessionBuilder()
                .world(this.player.getWorld())
                .actor((Actor)this.player)
                .allowedRegionsEverywhere()
                .limitUnlimited()
                .compile()
                .build();
    }

    /**
     * populateHeightMap
     *
     * generates the initial heightmap based on the terrain
     */
    public void populateHeightMap(){
        BlockVector3 min = sel.getMinimumPoint();
        BlockVector3 max = sel.getMaximumPoint();

        // instantiate heightmap
        heightMap = new HeightMap(width, length, min.getY(), max.getY(), 1.0);

        // instantiate set of saved blocks
        highestBlocks = new HashMap<>();

        // loop through each x/z position
        for (int x = min.getX(); x <= max.getX(); x++) {
            for (int z = min.getZ(); z <= max.getZ(); z++) {
                // initialize highest blocks to all be diamond blocks
                highestBlocks.put(new XZBlock(x, z), new BlockState(BlockTypes.DIAMOND_BLOCK, 0, 0));
                // for each x/z point, search down to find highest not air block
                for (int y = max.getY(); y >= min.getY(); y--) {
                    BlockState block = editSession.getBlock(x, y, z);
                    // create copy of block in case it gets edited later before its needed again
                    BlockType blockTypeCpy = BlockTypes.get(block.getBlockType().getId());
                    BlockState blockCpy = new BlockState(blockTypeCpy, block.getInternalId(), block.getOrdinal());
                    // skip if air
                    if (block.isAir()) {
                        continue;
                    }
                    else{
                        // save the block for later
                        highestBlocks.put(new XZBlock(x, z), blockCpy);
                        // set heightmap height
                        heightMap.setHeight(x - min.getX(), z - min.getZ(), y);

                        break;
                    }
                }
            }
        }
    }

    /**
     * doErosion
     *
     * method that does the erosion on the heightmap
     */
    abstract void doErosion();

    /**
     * placeHeightMap
     *
     * places the eroded heightmap into the terrain
     */
    public void placeHeightMap() {
        // replace selection area with heightmap
        BlockVector3 min = sel.getMinimumPoint();
        BlockVector3 max = sel.getMaximumPoint();
        // create new region and clipboard for paste
        CuboidRegion pasteRegion = new CuboidRegion(player.getWorld(), min, max);
        BlockArrayClipboard pasteClipboard = new BlockArrayClipboard(pasteRegion);
        pasteClipboard.setOrigin(min);

        // generate the new terrain
        for (int x = min.getX(); x <= max.getX(); x++) {
            for (int z = min.getZ(); z <= max.getZ(); z++) {
                // get the xz block
                BlockState block = highestBlocks.get(new XZBlock(x,z));
                // get max height
                int maxHeight = (int) Math.round(heightMap.getHeight(x - min.getX(), z - min.getZ()));
                // loop y
                for (int y = min.getY(); y <= max.getY(); y++) {
                    // set all y values under max that werent air in original the nearest block
                    if(y <= maxHeight){
                        // pasteClipboard.setBlock(x, y, z, (BlockStateHolder) block);
                        BlockState blockState = editSession.getBlock(x, y, z);
                        if(blockState.isAir()){
                            pasteClipboard.setBlock(x, y, z, (BlockStateHolder) new BlockState(BlockTypes.AIR, 0, 0));
                        }
                        else{
                            // find nearest BlockState (to make output look similar to input)
                            NearestBlockStateFinder finder = new NearestBlockStateFinder(this.sel, this.editSession, 100);
                            BlockState closest = finder.findNearestBlockState(x, y, z);

                            // if null default to air
                            if(closest == null){
                                pasteClipboard.setBlock(x, y, z, (BlockStateHolder) new BlockState(BlockTypes.AIR, 0, 0));
                            }
                            else{
                                pasteClipboard.setBlock(x, y, z, (BlockStateHolder) closest);
                            }

                            changedBlocks ++;
                        }
                    }
                    // put air everywhere else
                    else{
                        pasteClipboard.setBlock(x, y, z, (BlockStateHolder) new BlockState(BlockTypes.AIR, 0, 0));
                    }
                }
            }
        }

        // create clipboard holder
        ClipboardHolder pasteClipboardHolder = new ClipboardHolder(pasteClipboard);

        // paste final clipboard into world
        Operation operation = pasteClipboardHolder
                .createPaste(editSession)
                .to(min)
                .copyEntities(false)
                .copyBiomes(false)
                .ignoreAirBlocks(false)
                .build();

        // perform operation
        Operations.complete(operation);
    }

    /**
     * endOperation
     *
     * closes the editSession and MUST be called at the end of the command
     */
    public void endOperation(){
        LocalSession localSession = WorldEdit.getInstance().getSessionManager().get(player);
        localSession.remember(editSession);
        editSession.close();
    }
    
    /**
     * getChangedBlocks
     *
     * gets the number of changed blocks
     */
    public long getChangedBlocks(){
        return changedBlocks;
    }

    /**
     * getHeightMapNormal
     *
     * gets the normal of the heightmap
     */
    public DoubleVec3 getHeightMapNormal(double x, double z){
        return heightMap.getSurfaceNormal(fixX(x), fixZ(z));
        // return heightMap.getAveragedNormal(fixX(x), fixZ(z), 2.0);
    }

    /**
     * changeHeightMap
     *
     * adds to the heightmap
     */
    public boolean changeHeightMap(double x, double z, double offset){
        return heightMap.addHeight(fixX(x), fixZ(z), offset);
    }

    /**
     * applyFilter
     *
     * applies a FAWE gauassian filter to the heightmap
     *
     * filter - the filter
     * iterations - number of times to filter
     */
    public void applyHeightMapFilter(HeightMapFilter filter, int iterations) {
        heightMap.applyFilter(filter, iterations);
    }

    /**
     * fixX
     *
     * converts a double x input into an int and keeps it in bounds
     */
    private int fixX(double x){
        int x2 = (int) Math.round(x);
        if(x2 >= heightMap.getWidth()) x2 = heightMap.getWidth() - 1;
        else if (x2 < 0) x2 = 0;
        return x2;
    }

    /**
     * fixZ
     *
     * converts a double z input into an int and keeps it in bounds
     */
    private int fixZ(double z){
        int z2 = (int) Math.round(z);
        if(z2 >= heightMap.getLength()) z2 = heightMap.getLength() - 1;
        else if (z2 < 0) z2 = 0;
        return z2;
    }
}
