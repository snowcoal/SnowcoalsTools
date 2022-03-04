package org.snowcoal.citygenerator.smooth;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.block.BlockState;
import com.sk89q.worldedit.world.block.BlockStateHolder;
import com.sk89q.worldedit.world.block.BlockType;
import com.sk89q.worldedit.world.block.BlockTypes;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import org.snowcoal.citygenerator.CityGenerator;
import org.snowcoal.citygenerator.houseset.House;

import javax.print.DocFlavor;

import static java.lang.Math.abs;

public class SmoothStairSlab {
    private Region sel;
    private Player player;
    private CityGenerator instance;
    private Map<XZBlock, ArrayList<Integer>> airAboveMap;
    private Map<XZBlock, ArrayList<Integer>> airBelowMap;
    private final int MISSING_Y_OFFSET = 2;
    private Random random;

    public SmoothStairSlab(Region sel, Player player, CityGenerator instance) {
        this.sel = sel;
        this.player = player;
        this.instance = instance;
        this.airAboveMap = new HashMap<>();
        this.airBelowMap = new HashMap<>();
        this.random = new Random();
        smooth();
    }

    /**
     * smooth
     *
     * base method that does the smoothing
     *
     * returns true on success, false on failure
     */
    public boolean smooth() {
        BlockVector3 min = this.sel.getMinimumPoint();
        BlockVector3 max = this.sel.getMaximumPoint();

        // set up new editsession for edit
        EditSession editSession = WorldEdit.getInstance().newEditSessionBuilder()
                .world(this.player.getWorld())
                .actor((Actor)this.player)
                .allowedRegionsEverywhere()
                .limitUnlimited()
                .compile()
                .build();

        // set up lists to store 2 types of blocks
        List<Block> airAboveBlocks = new ArrayList<>();
        List<Block> airBelowBlocks = new ArrayList<>();
        // System.out.println("min: " + min + " max: " + max);

        // loop through all blocks in selection
        for (int x = min.getX(); x <= max.getX(); x++) {
            for (int z = min.getZ(); z <= max.getZ(); z++) {
                for (int y = min.getY(); y <= max.getY(); y++) {
                    // ensure the selection contains the block
                    if (this.sel.contains(x, y, z)) {
                        BlockState block = editSession.getBlock(x, y, z);
                        // check if its not air
                        if (!block.isAir()) {
                            // check if its a changeable block
                            BlockMap.Blocks blockType = isChangeable(block);
                            if (blockType != null) {
                                // check the blocks above and below
                                BlockState above = editSession.getBlock(x, y + 1, z);
                                BlockState below = editSession.getBlock(x, y - 1, z);
                                boolean airAbove = above.isAir();
                                boolean airBelow = below.isAir();

                                // determine which set it needs to go in
                                if (!airAbove || !airBelow)
                                    if (airAbove) {
                                        airAboveBlocks.add(new Block(blockType, x, y, z));
                                        XZBlock xzBlock = new XZBlock(x, z);
                                        // attempt to get a list from the hashmap
                                        ArrayList<Integer> yList = airAboveMap.get(xzBlock);
                                        // if no list exists yet make one
                                        if(yList == null){
                                            yList = new ArrayList<>();
                                            airAboveMap.put(xzBlock, yList);
                                        }
                                        // list will be sorted since y from min to max
                                        yList.add(y);
                                    }
                                    else if (airBelow) {
                                        airBelowBlocks.add(new Block(blockType, x, y, z));
                                        XZBlock xzBlock = new XZBlock(x, z);
                                        // attempt to get a list from the hashmap
                                        ArrayList<Integer> yList = airBelowMap.get(xzBlock);
                                        // if no list exists yet make one
                                        if(yList == null){
                                            yList = new ArrayList<>();
                                            airBelowMap.put(xzBlock, yList);
                                        }
                                        // list will be sorted since y from min to max
                                        yList.add(y);
                                    }
                            }
                        }
                    }
                }
            }
        }

        // make list for all blocks that need to be changed at the end
        List<ChangeBlock> changeBlocks = new ArrayList<>();

        // go through all blocks with air above
        for (Block block : airAboveBlocks) {
            int pY = block.posY;

            // get 4 neighbors of block
            int negX_Y = getNeighborYAbove(editSession, block, -1, 0);
            int posX_Y = getNeighborYAbove(editSession, block, 1, 0);
            int negZ_Y = getNeighborYAbove(editSession, block, 0, -1);
            int posZ_Y = getNeighborYAbove(editSession, block, 0, 1);

            // calculate 2* x slope
            int xSlope = posX_Y - negX_Y;
            int absXSlope = abs(xSlope);
            // calculate 2* z slope
            int zSlope = posZ_Y - negZ_Y;
            int absZSlope = abs(zSlope);

            // skip block if its on the same y lvl as the lower block
            if((xSlope > 0 && pY == negX_Y) || (xSlope < 0 && pY == posX_Y) || (zSlope > 0 && pY == negZ_Y) || (zSlope < 0 && pY == posZ_Y)){
                continue;
            }

            // if 1 slope is 0 and the other is 1 or both are 1 its a slab
            if(absXSlope == 1 || absZSlope == 1) {
                // add block to list of changed blocks and move to next one
                changeBlocks.add(new ChangeBlock(block, instance.getBlockMap().getBlock(block.type, BlockMap.xzDirections.SLAB, BlockMap.yDirections.UP)));
                continue;
            }

            // pick random number in case they are equal
            int rand = random.nextInt() % 2;

            // x smaller than z OR they are equal and random = 0
            if((absXSlope < absZSlope) || (absXSlope == absZSlope && rand == 0)){
                // negative x slope
                if(xSlope >= -3 && xSlope < 0){
                    changeBlocks.add(new ChangeBlock(block, instance.getBlockMap().getBlock(block.type, BlockMap.xzDirections.WEST, BlockMap.yDirections.UP)));
                    continue;
                }
                // positive x slope
                if(xSlope <= 3 && xSlope > 0) {
                    changeBlocks.add(new ChangeBlock(block, instance.getBlockMap().getBlock(block.type, BlockMap.xzDirections.EAST, BlockMap.yDirections.UP)));
                    continue;
                }
                // if none true skip block
                continue;
            }
            // z smaller than x OR they are equal and random = 1
            if(absZSlope < absXSlope || (absXSlope == absZSlope && rand == 1)){
                // negative z slope
                if(zSlope >= -3 && zSlope < 0){
                    changeBlocks.add(new ChangeBlock(block, instance.getBlockMap().getBlock(block.type, BlockMap.xzDirections.NORTH, BlockMap.yDirections.UP)));
                    continue;
                }
                // positive x slope
                if(zSlope <= 3 && zSlope > 0) {
                    changeBlocks.add(new ChangeBlock(block, instance.getBlockMap().getBlock(block.type, BlockMap.xzDirections.SOUTH, BlockMap.yDirections.UP)));
                    continue;
                }
                // if none true skip block
                continue;
            }
        }

        // go through all blocks with air below
        for (Block block : airBelowBlocks) {
            int pY = block.posY;

            // get 4 neighbors of block
            int negX_Y = getNeighborYBelow(editSession, block, -1, 0);
            int posX_Y = getNeighborYBelow(editSession, block, 1, 0);
            int negZ_Y = getNeighborYBelow(editSession, block, 0, -1);
            int posZ_Y = getNeighborYBelow(editSession, block, 0, 1);

            // calculate 2* x slope
            int xSlope = posX_Y - negX_Y;
            int absXSlope = abs(xSlope);
            // calculate 2* z slope
            int zSlope = posZ_Y - negZ_Y;
            int absZSlope = abs(zSlope);

            // skip block if its on the same y lvl as the higher block
            if((xSlope > 0 && pY == posX_Y) || (xSlope < 0 && pY == negX_Y) || (zSlope > 0 && pY == posZ_Y) || (zSlope < 0 && pY == negZ_Y)){
                continue;
            }

            // if 1 slope is 0 and the other is 1 or both are 1 its a slab
            if(absXSlope == 1 || absZSlope == 1){
                // add block to list of changed blocks and move to next one
                changeBlocks.add(new ChangeBlock(block, instance.getBlockMap().getBlock(block.type, BlockMap.xzDirections.SLAB, BlockMap.yDirections.DOWN)));
                continue;
            }

            // pick random number in case they are equal
            int rand = random.nextInt() % 2;

            // x smaller than z OR they are equal and random = 0
            if((absXSlope < absZSlope) || (absXSlope == absZSlope && rand == 0)){
                // negative x slope
                if(xSlope >= -3 && xSlope < 0){
                    changeBlocks.add(new ChangeBlock(block, instance.getBlockMap().getBlock(block.type, BlockMap.xzDirections.EAST, BlockMap.yDirections.DOWN)));
                    continue;
                }
                // positive x slope
                if(xSlope <= 3 && xSlope > 0) {
                    changeBlocks.add(new ChangeBlock(block, instance.getBlockMap().getBlock(block.type, BlockMap.xzDirections.WEST, BlockMap.yDirections.DOWN)));
                    continue;
                }
                // if none true skip block
                continue;
            }
            // z smaller than x OR they are equal and random = 1
            if(absZSlope < absXSlope || (absXSlope == absZSlope && rand == 1)){
                // negative z slope
                if(zSlope >= -3 && zSlope < 0){
                    changeBlocks.add(new ChangeBlock(block, instance.getBlockMap().getBlock(block.type, BlockMap.xzDirections.SOUTH, BlockMap.yDirections.DOWN)));
                    continue;
                }
                // positive x slope
                if(zSlope <= 3 && zSlope > 0) {
                    changeBlocks.add(new ChangeBlock(block, instance.getBlockMap().getBlock(block.type, BlockMap.xzDirections.NORTH, BlockMap.yDirections.DOWN)));
                    continue;
                }
                // if none true skip block
                continue;
            }
        }

        // change all required blocks
        for (ChangeBlock cBlock: changeBlocks){
            editSession.setBlock(cBlock.blockInput.posX, cBlock.blockInput.posY, cBlock.blockInput.posZ,
                    (BlockStateHolder)new BlockState(cBlock.blockOutput.blockType, cBlock.blockOutput.IID, cBlock.blockOutput.ordinal));
        }


        editSession.close();
        return true;
    }


    /**
     * assignYValueAbove
     *
     * gets the y value of the neighboring block. Performs a null check and if it doesnt exist, and then
     * guesses an appropriate value to assign to it
     *
     * used for blocks with air above them
     *
     */
    private int getNeighborYAbove(EditSession editSession, Block block, int xOffset, int zOffset){
        int pX = block.posX;
        int pY = block.posY;
        int pZ = block.posZ;

        ArrayList<Integer> yList = airAboveMap.get(new XZBlock(pX + xOffset, pZ + zOffset));
        // null check and assign appropriate value
        if(yList == null){
            // if neighbor is air, then it goes down
            if(editSession.getBlock(pX + xOffset, pY, pZ + zOffset).isAir()) return(pY - MISSING_Y_OFFSET);
                // if neighbor is block, then it goes up
            else return(pY + MISSING_Y_OFFSET);
        }

        int prevDist = Integer.MAX_VALUE;
        int nY = 0;
        // get the nearest y value neighbor
        for(Integer y: yList){
            int curDist = abs(y - pY);
            // break if an element found with an abs distance larger than previous
            if(curDist > prevDist) break;
            // set prev values
            nY = y;
            prevDist = curDist;
        }

        return nY;
    }


    /**
     * getNeighborYBelow
     *
     * gets the y value of the neighboring block. Performs a null check and if it doesnt exist, and then
     * guesses an appropriate value to assign to it
     *
     * used for blocks with air below them
     *
     */
    private int getNeighborYBelow(EditSession editSession, Block block, int xOffset, int zOffset){
        int pX = block.posX;
        int pY = block.posY;
        int pZ = block.posZ;

        ArrayList<Integer> yList = airBelowMap.get(new XZBlock(pX + xOffset, pZ + zOffset));
        // null check and assign appropriate value
        if(yList == null){
            // if neighbor is air, then it goes up
            if(editSession.getBlock(pX + xOffset, pY, pZ + zOffset).isAir()) return (pY + MISSING_Y_OFFSET);
                // if neighbor is block, then it goes down
            else return(pY - MISSING_Y_OFFSET);
        }

        int prevDist = Integer.MAX_VALUE;
        int nY = 0;
        // get the nearest y value neighbor
        for(Integer y: yList){
            int curDist = abs(y - pY);
            // break if an element found with an abs distance larger than previous
            if(curDist > prevDist) break;
            // set prev values
            nY = y;
            prevDist = curDist;
        }

        return nY;
    }


    /**
     * isChangeable
     *
     * cross checks the block accross a known list of changeable blocks to see if its changeable
     *
     * returns the block if its a changeable block, null otherwise
     */
    private BlockMap.Blocks isChangeable(BlockState block) {
        switch (block.getBlockType().getId()) {
            //STONE, ANDESITE, DIORITE, GRANITE, COBBLESTONE, MOSSY_COBBLESTONE, BLACKSTONE, COBBLED_DEEPSLATE, SANDSTONE, SMOOTH_SANDSTONE, RED_SANDSTONE, SMOOTH_RED_SANDSTONE
            case "minecraft:stone": return BlockMap.Blocks.STONE;
            case "minecraft:andesite": return BlockMap.Blocks.ANDESITE;
            case "minecraft:diorite": return BlockMap.Blocks.DIORITE;
            case "minecraft:granite": return BlockMap.Blocks.GRANITE;
            case "minecraft:cobblestone": return BlockMap.Blocks.COBBLESTONE;
            case "minecraft:mossy_cobblestone": return BlockMap.Blocks.MOSSY_COBBLESTONE;
            case "minecraft:blackstone": return BlockMap.Blocks.BLACKSTONE;
            case "minecraft:cobbled_deepslate": return BlockMap.Blocks.COBBLED_DEEPSLATE;
            case "minecraft:sandstone": return BlockMap.Blocks.SANDSTONE;
            case "minecraft:smooth_sandstone": return BlockMap.Blocks.SMOOTH_SANDSTONE;
            case "minecraft:red_sandstone": return BlockMap.Blocks.RED_SANDSTONE;
            case "minecraft:smooth_red_sandstone": return BlockMap.Blocks.SMOOTH_RED_SANDSTONE;

        }
        return null;
    }


    /**
     * Block
     *
     * stores important block info
     */
    private class Block {
        public BlockMap.Blocks type;
        public int posX;
        public int posY;
        public int posZ;

        public Block(BlockMap.Blocks type, int posX, int posY, int posZ) {
            this.type = type;
            this.posX = posX;
            this.posY = posY;
            this.posZ = posZ;
        }
    }


    /**
     * XZBlock
     *
     * gets a blocks y-position based on its x/z coords
     */
    private class XZBlock {
        public int posX;
        public int posZ;

        public XZBlock(int posX, int posZ) {
            this.posX = posX;
            this.posZ = posZ;
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) return true;
            if (getClass() != obj.getClass()) return false;
            final XZBlock other = (XZBlock) obj;
            if ((!(posX == other.posX)) || (!(posZ == other.posZ)))
                return false;
            return true;
        }

        // COLLISIONS WILL HAPPEN PAST X/Z +32767 -32768
        @Override
        public int hashCode() {
            // shift x left 16 times to make it 0xabcd0000
            int x = posX << 16;
            // clear high bits of z so its 0x00001234
            int z = posZ & 0x0000ffff;
            // ORing together yields 0xabcd1234
            return x | z;
        }
    }


    /**
     * ChangeBlock
     *
     * stores all required data to change the input block into the output block
     */
    private class ChangeBlock {
        public Block blockInput;
        public BlockMap.BlockOutput blockOutput;

        public ChangeBlock(Block block, BlockMap.BlockOutput blockOutput){
            this.blockInput = block;
            this.blockOutput = blockOutput;
        }
    }
}
