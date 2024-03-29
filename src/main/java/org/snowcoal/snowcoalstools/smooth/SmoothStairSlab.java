package org.snowcoal.snowcoalstools.smooth;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.block.BlockState;
import com.sk89q.worldedit.world.block.BlockStateHolder;
import org.snowcoal.snowcoalstools.utils.GuassConvKernel;
import org.snowcoal.snowcoalstools.utils.XZBlock;

import java.util.*;

import org.snowcoal.snowcoalstools.SnowcoalsTools;

import static java.lang.Math.abs;

public class SmoothStairSlab {
    private Region sel;
    private Player player;
    private SnowcoalsTools instance;
    private Map<XZBlock, ArrayList<Integer>> airAboveMap;
    private Map<XZBlock, ArrayList<Integer>> airBelowMap;
    private final int MISSING_Y_OFFSET = 2;
    private Random random;
    private int changedBlocks = 0;
    private final int convK = 5;
    private final int convPad = convK/2;
    // constants derived from normal distribution ~(mu=0.5, sigma=1)
    // the constants are where the distribution has area=0.25 in ranges [thresholdMin - 0.5, 0.5] and [0.5, 0.5 + thresholdMax]
    // theoretically, they should be this:
    // min = 0.325510249804
    // max = 0.674489750196
    // but these values seem to work better:
    private final double convThresholdMin = 0.305;
    private final double convThresholdMax = 0.695;

    public SmoothStairSlab(Region sel, Player player, SnowcoalsTools instance) {
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

        // loop through all blocks in selection
        for (int x = min.getX(); x <= max.getX(); x++) {
            for (int z = min.getZ(); z <= max.getZ(); z++) {
                for (int y = min.getY(); y <= max.getY(); y++) {
                    // ensure the selection contains the block
                    if (!this.sel.contains(x, y, z)) {
                        continue;
                    }
                    // check if its not air
                    BlockState block = editSession.getBlock(x, y, z);
                    if (block.isAir()) {
                        continue;
                    }
                    // check if its a changeable block
                    BlockMap.Blocks blockType = isChangeable(block);
                    if (blockType == null) {
                        continue;
                    }

                    // check the blocks above and below
                    BlockState above = editSession.getBlock(x, y + 1, z);
                    BlockState below = editSession.getBlock(x, y - 1, z);
                    boolean airAbove = above.isAir();
                    boolean airBelow = below.isAir();

                    // make sure the block doesnt have both air above and below it
                    if (airAbove && airBelow){
                        continue;
                    }
                    if (airAbove) {
                        airAboveBlocks.add(new Block(blockType, x, y, z));
                        XZBlock xzBlock = new XZBlock(x, z);
                        // attempt to get a list from the hashmap
                        ArrayList<Integer> yList = airAboveMap.get(xzBlock);
                        // if no list exists yet make one
                        if (yList == null) {
                            yList = new ArrayList<>();
                            airAboveMap.put(xzBlock, yList);
                        }
                        // list will be sorted since y from min to max
                        yList.add(y);
                    }
                    if (airBelow) {
                        airBelowBlocks.add(new Block(blockType, x, y, z));
                        XZBlock xzBlock = new XZBlock(x, z);
                        // attempt to get a list from the hashmap
                        ArrayList<Integer> yList = airBelowMap.get(xzBlock);
                        // if no list exists yet make one
                        if (yList == null) {
                            yList = new ArrayList<>();
                            airBelowMap.put(xzBlock, yList);
                        }
                        // list will be sorted since y from min to max
                        yList.add(y);
                    }
                }
            }
        }

        // make list for all blocks that need to be changed at the end
        List<ChangeBlock> changeBlocks = new ArrayList<>();

        // setup convolution kernel for slabs
        GuassConvKernel guassConvKernel = new GuassConvKernel(editSession, convK);

        // go through all blocks with air above
        for (Block block : airAboveBlocks) {
            int pX = block.posX;
            int pY = block.posY;
            int pZ = block.posZ;

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
                changeBlocks.add(new ChangeBlock(block, instance.getBlockMap().getBlock(block.type, BlockMap.xzDirections.SLAB, BlockMap.yDirections.UP)));
                continue;
            }

            // if both slopes are zero, try convolution to determine if its a slab
            if(absXSlope == 0 && absZSlope == 0) {
                // bounds check
                if(!guassConvKernel.checkKernelBounds(sel, pX, pY, pZ)){
                    continue;
                }

                // perform convolution, if sum is within threshold, add block
                double sum = guassConvKernel.guassConvolve(pX, pY, pZ);
                if(sum > convThresholdMin && sum < convThresholdMax) {
                    changeBlocks.add(new ChangeBlock(block, instance.getBlockMap().getBlock(block.type, BlockMap.xzDirections.SLAB, BlockMap.yDirections.UP)));
                }
                continue;
            }

            // pick random numbers for determining rotation and whether to place blocks or not
            int randRot = random.nextInt() % 2;
            int randLarge = random.nextInt() % 4;

            // dist: slope +- 3: always places
            //       slope +- 4: 1/2 chance of placing
            //       slope +- 5: 1/4 chance of placing

            // x smaller than z OR they are equal and random = 0
            if((absXSlope < absZSlope) || (absXSlope == absZSlope && randRot == 0)){
                // negative x slope
                if((xSlope >= -3 || (xSlope == -4 && randLarge < 2) || (xSlope == -5 && randLarge == 0)) && xSlope < 0){
                    changeBlocks.add(new ChangeBlock(block, instance.getBlockMap().getBlock(block.type, BlockMap.xzDirections.WEST, BlockMap.yDirections.UP)));
                    continue;
                }
                // positive x slope
                if((xSlope <= 3 || (xSlope == 4 && randLarge < 2) || (xSlope == 5 && randLarge == 0)) && xSlope > 0) {
                    changeBlocks.add(new ChangeBlock(block, instance.getBlockMap().getBlock(block.type, BlockMap.xzDirections.EAST, BlockMap.yDirections.UP)));
                    continue;
                }
                // if none true skip block
                continue;
            }
            // z smaller than x OR they are equal and random = 1
            if(absZSlope < absXSlope || (absXSlope == absZSlope && randRot == 1)){
                // negative z slope
                if((zSlope >= -3 || (zSlope == -4 && randLarge < 2) || (zSlope == -5 && randLarge == 0)) && zSlope < 0){
                    changeBlocks.add(new ChangeBlock(block, instance.getBlockMap().getBlock(block.type, BlockMap.xzDirections.NORTH, BlockMap.yDirections.UP)));
                    continue;
                }
                // positive z slope
                if((zSlope <= 3 || (zSlope == 4 && randLarge < 2) || (zSlope == 5 && randLarge == 0)) && zSlope > 0) {
                    changeBlocks.add(new ChangeBlock(block, instance.getBlockMap().getBlock(block.type, BlockMap.xzDirections.SOUTH, BlockMap.yDirections.UP)));
                    continue;
                }
                // if none true skip block
                continue;
            }
        }

        // go through all blocks with air below
        for (Block block : airBelowBlocks) {
            int pX = block.posX;
            int pY = block.posY;
            int pZ = block.posZ;

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
                changeBlocks.add(new ChangeBlock(block, instance.getBlockMap().getBlock(block.type, BlockMap.xzDirections.SLAB, BlockMap.yDirections.DOWN)));
                continue;
            }

            // if both slopes are zero, try convolution to determine if its a slab
            if(absXSlope == 0 && absZSlope == 0) {
                // bounds check
                if(!guassConvKernel.checkKernelBounds(sel, pX, pY, pZ)){
                    continue;
                }

                // perform convolution, if sum is within threshold, add block
                double sum = guassConvKernel.guassConvolve(pX, pY, pZ);
                if(sum > convThresholdMin && sum < convThresholdMax) {
                    changeBlocks.add(new ChangeBlock(block, instance.getBlockMap().getBlock(block.type, BlockMap.xzDirections.SLAB, BlockMap.yDirections.DOWN)));
                }
                continue;
            }

            // pick random numbers for determining rotation and whether to place blocks or not
            int randRot = random.nextInt() % 2;
            int randLarge = random.nextInt() % 4;

            // dist: slope +- 3: always places
            //       slope +- 4: 1/2 chance of placing
            //       slope +- 5: 1/4 chance of placing

            // x smaller than z OR they are equal and random = 0
            if((absXSlope < absZSlope) || (absXSlope == absZSlope && randRot == 0)){
                // negative x slope
                if((xSlope >= -3 || (xSlope == -4 && randLarge < 2) || (xSlope == -5 && randLarge == 0)) && xSlope < 0){
                    changeBlocks.add(new ChangeBlock(block, instance.getBlockMap().getBlock(block.type, BlockMap.xzDirections.EAST, BlockMap.yDirections.DOWN)));
                    continue;
                }
                // positive x slope
                if((xSlope <= 3 || (xSlope == 4 && randLarge < 2) || (xSlope == 5 && randLarge == 0)) && xSlope > 0) {
                    changeBlocks.add(new ChangeBlock(block, instance.getBlockMap().getBlock(block.type, BlockMap.xzDirections.WEST, BlockMap.yDirections.DOWN)));
                    continue;
                }
                // if none true skip block
                continue;
            }
            // z smaller than x OR they are equal and random = 1
            if(absZSlope < absXSlope || (absXSlope == absZSlope && randRot == 1)){
                // negative z slope
                if((zSlope >= -3 || (zSlope == -4 && randLarge < 2) || (zSlope == -5 && randLarge == 0)) && zSlope < 0){
                    changeBlocks.add(new ChangeBlock(block, instance.getBlockMap().getBlock(block.type, BlockMap.xzDirections.SOUTH, BlockMap.yDirections.DOWN)));
                    continue;
                }
                // positive z slope
                if((zSlope <= 3 || (zSlope == 4 && randLarge < 2) || (zSlope == 5 && randLarge == 0)) && zSlope > 0) {
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

            // count total amount of blocks changed
            this.changedBlocks++;
        }

        LocalSession localSession = WorldEdit.getInstance().getSessionManager().get(player);
        localSession.remember(editSession);
        editSession.close();
        return true;
    }

    /**
     * getChangedBlocks
     *
     * gets the amount of blocks that were changed by the edit
     *
     */
    public int getChangedBlocks(){
        return this.changedBlocks;
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
            case "minecraft:cobbled_deepslate":
            case "minecraft:deepslate": return BlockMap.Blocks.COBBLED_DEEPSLATE;
            case "minecraft:sandstone": return BlockMap.Blocks.SANDSTONE;
            case "minecraft:smooth_sandstone": return BlockMap.Blocks.SMOOTH_SANDSTONE;
            case "minecraft:red_sandstone": return BlockMap.Blocks.RED_SANDSTONE;
            case "minecraft:smooth_red_sandstone": return BlockMap.Blocks.SMOOTH_RED_SANDSTONE;
            case "minecraft:prismarine": return BlockMap.Blocks.PRISMARINE;

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
