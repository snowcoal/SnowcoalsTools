package org.snowcoal.snowcoalstools.smooth;

import java.util.HashMap;
import java.util.Map;
import com.sk89q.worldedit.world.block.BlockType;
import com.sk89q.worldedit.world.block.BlockTypes;

public class BlockMap {
    private Map<BlockInput, BlockOutput> blockMap;

    public BlockMap() {
        this.blockMap = new HashMap<>();
        addAllBlocks();
    }

    /**
     * Input into the hash table
     *
     */
    private class BlockInput {
        private BlockMap.Blocks block;
        private BlockMap.xzDirections xzDir;
        private BlockMap.yDirections yDir;

        public BlockInput(BlockMap.Blocks block, BlockMap.xzDirections xzDir, BlockMap.yDirections yDir) {
            this.block = block;
            this.xzDir = xzDir;
            this.yDir = yDir;
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) return true;
            if (getClass() != obj.getClass()) return false;
            final BlockInput other = (BlockInput) obj;
            if ((!block.equals(other.block)) || (!xzDir.equals(other.xzDir)) || (!yDir.equals(other.yDir)))
                return false;
            return true;
        }

        @Override
        public int hashCode() {
            // make last 4 bits 0
            int blockOr = this.block.ordinal() << 6;
            // make last bit 0
            int xzDirOr = this.xzDir.ordinal() << 2;
            int yDirOr = this.yDir.ordinal();

            // xzDir has max size of 5, so it requires 3 bits
            // yDir requires 1 bit

            // so shift them in such a way so there wont be collisions, plus extra padding bits:
            // [31:6]  [5]    [4:2]    [1]    [0]
            // bOr   unused   xzOr   unused   yOr
            return blockOr | xzDirOr | yDirOr;
        }
    }

    /**
     * Output of hash table
     *
     */
    public class BlockOutput {
        public int IID;
        public BlockType blockType;
        public int ordinal;

        public BlockOutput(int IID, int ordinal, BlockType blockType) {
            this.IID = IID;
            this.ordinal = ordinal;
            this.blockType = blockType;
        }
    }

    /**
     * gets a block from the hash table
     *
     */
    public BlockOutput getBlock(Blocks block, xzDirections xzDir, yDirections yDir){
        return blockMap.get(new BlockInput(block, xzDir, yDir));
    }

    public enum Blocks {
        STONE, ANDESITE, DIORITE, GRANITE, COBBLESTONE, MOSSY_COBBLESTONE, BLACKSTONE, COBBLED_DEEPSLATE, SANDSTONE, SMOOTH_SANDSTONE, RED_SANDSTONE, SMOOTH_RED_SANDSTONE, PRISMARINE;
    }

    public enum xzDirections {
        NORTH, SOUTH, WEST, EAST, SLAB;
    }

    public enum yDirections {
        UP, DOWN;
    }


    /**
     * Adds all the known constant values to the hashmap
     *
     */
    private void addAllBlocks() {
        // SCRIPT GENERATED CODE BASED ON KNOWN VALUES
        blockMap.put(new BlockInput(Blocks.COBBLED_DEEPSLATE, xzDirections.EAST, yDirections.UP), new BlockOutput(273884, 12385 , BlockTypes.COBBLED_DEEPSLATE_STAIRS));
        blockMap.put(new BlockInput(Blocks.COBBLED_DEEPSLATE, xzDirections.SOUTH, yDirections.UP), new BlockOutput(271836, 12383 , BlockTypes.COBBLED_DEEPSLATE_STAIRS));
        blockMap.put(new BlockInput(Blocks.COBBLED_DEEPSLATE, xzDirections.WEST, yDirections.UP), new BlockOutput(272860, 12384 , BlockTypes.COBBLED_DEEPSLATE_STAIRS));
        blockMap.put(new BlockInput(Blocks.COBBLED_DEEPSLATE, xzDirections.NORTH, yDirections.UP), new BlockOutput(270812, 12382 , BlockTypes.COBBLED_DEEPSLATE_STAIRS));
        blockMap.put(new BlockInput(Blocks.COBBLED_DEEPSLATE, xzDirections.EAST, yDirections.DOWN), new BlockOutput(265692, 12381 , BlockTypes.COBBLED_DEEPSLATE_STAIRS));
        blockMap.put(new BlockInput(Blocks.COBBLED_DEEPSLATE, xzDirections.SOUTH, yDirections.DOWN), new BlockOutput(263644, 12379 , BlockTypes.COBBLED_DEEPSLATE_STAIRS));
        blockMap.put(new BlockInput(Blocks.COBBLED_DEEPSLATE, xzDirections.WEST, yDirections.DOWN), new BlockOutput(264668, 12380 , BlockTypes.COBBLED_DEEPSLATE_STAIRS));
        blockMap.put(new BlockInput(Blocks.COBBLED_DEEPSLATE, xzDirections.NORTH, yDirections.DOWN), new BlockOutput(262620, 12378 , BlockTypes.COBBLED_DEEPSLATE_STAIRS));
        blockMap.put(new BlockInput(Blocks.COBBLED_DEEPSLATE, xzDirections.SLAB, yDirections.UP), new BlockOutput(6092, 23231 , BlockTypes.COBBLED_DEEPSLATE_SLAB));
        blockMap.put(new BlockInput(Blocks.COBBLED_DEEPSLATE, xzDirections.SLAB, yDirections.DOWN), new BlockOutput(5068, 23230 , BlockTypes.COBBLED_DEEPSLATE_SLAB));
        blockMap.put(new BlockInput(Blocks.BLACKSTONE, xzDirections.EAST, yDirections.UP), new BlockOutput(273423, 1091 , BlockTypes.BLACKSTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.BLACKSTONE, xzDirections.SOUTH, yDirections.UP), new BlockOutput(271375, 1089 , BlockTypes.BLACKSTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.BLACKSTONE, xzDirections.WEST, yDirections.UP), new BlockOutput(272399, 1090 , BlockTypes.BLACKSTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.BLACKSTONE, xzDirections.NORTH, yDirections.UP), new BlockOutput(270351, 1088 , BlockTypes.BLACKSTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.BLACKSTONE, xzDirections.EAST, yDirections.DOWN), new BlockOutput(265231, 1087 , BlockTypes.BLACKSTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.BLACKSTONE, xzDirections.SOUTH, yDirections.DOWN), new BlockOutput(263183, 1085 , BlockTypes.BLACKSTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.BLACKSTONE, xzDirections.WEST, yDirections.DOWN), new BlockOutput(264207, 1086 , BlockTypes.BLACKSTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.BLACKSTONE, xzDirections.NORTH, yDirections.DOWN), new BlockOutput(262159, 1084 , BlockTypes.BLACKSTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.BLACKSTONE, xzDirections.SLAB, yDirections.UP), new BlockOutput(6016, 21719 , BlockTypes.BLACKSTONE_SLAB));
        blockMap.put(new BlockInput(Blocks.BLACKSTONE, xzDirections.SLAB, yDirections.DOWN), new BlockOutput(4992, 21718 , BlockTypes.BLACKSTONE_SLAB));
        blockMap.put(new BlockInput(Blocks.PRISMARINE, xzDirections.EAST, yDirections.UP), new BlockOutput(274060, 15700 , BlockTypes.PRISMARINE_STAIRS));
        blockMap.put(new BlockInput(Blocks.PRISMARINE, xzDirections.SOUTH, yDirections.UP), new BlockOutput(272012, 15698 , BlockTypes.PRISMARINE_STAIRS));
        blockMap.put(new BlockInput(Blocks.PRISMARINE, xzDirections.WEST, yDirections.UP), new BlockOutput(273036, 15699 , BlockTypes.PRISMARINE_STAIRS));
        blockMap.put(new BlockInput(Blocks.PRISMARINE, xzDirections.NORTH, yDirections.UP), new BlockOutput(270988, 15697 , BlockTypes.PRISMARINE_STAIRS));
        blockMap.put(new BlockInput(Blocks.PRISMARINE, xzDirections.EAST, yDirections.DOWN), new BlockOutput(265868, 15696 , BlockTypes.PRISMARINE_STAIRS));
        blockMap.put(new BlockInput(Blocks.PRISMARINE, xzDirections.SOUTH, yDirections.DOWN), new BlockOutput(263820, 15694 , BlockTypes.PRISMARINE_STAIRS));
        blockMap.put(new BlockInput(Blocks.PRISMARINE, xzDirections.WEST, yDirections.DOWN), new BlockOutput(264844, 15695 , BlockTypes.PRISMARINE_STAIRS));
        blockMap.put(new BlockInput(Blocks.PRISMARINE, xzDirections.NORTH, yDirections.DOWN), new BlockOutput(262796, 15693 , BlockTypes.PRISMARINE_STAIRS));
        blockMap.put(new BlockInput(Blocks.PRISMARINE, xzDirections.SLAB, yDirections.UP), new BlockOutput(5831, 16904 , BlockTypes.PRISMARINE_SLAB));
        blockMap.put(new BlockInput(Blocks.PRISMARINE, xzDirections.SLAB, yDirections.DOWN), new BlockOutput(4807, 16903 , BlockTypes.PRISMARINE_SLAB));
        blockMap.put(new BlockInput(Blocks.RED_SANDSTONE, xzDirections.EAST, yDirections.UP), new BlockOutput(273514, 2759 , BlockTypes.RED_SANDSTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.RED_SANDSTONE, xzDirections.SOUTH, yDirections.UP), new BlockOutput(271466, 2757 , BlockTypes.RED_SANDSTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.RED_SANDSTONE, xzDirections.WEST, yDirections.UP), new BlockOutput(272490, 2758 , BlockTypes.RED_SANDSTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.RED_SANDSTONE, xzDirections.NORTH, yDirections.UP), new BlockOutput(270442, 2756 , BlockTypes.RED_SANDSTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.RED_SANDSTONE, xzDirections.EAST, yDirections.DOWN), new BlockOutput(265322, 2755 , BlockTypes.RED_SANDSTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.RED_SANDSTONE, xzDirections.SOUTH, yDirections.DOWN), new BlockOutput(263274, 2753 , BlockTypes.RED_SANDSTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.RED_SANDSTONE, xzDirections.WEST, yDirections.DOWN), new BlockOutput(264298, 2754 , BlockTypes.RED_SANDSTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.RED_SANDSTONE, xzDirections.NORTH, yDirections.DOWN), new BlockOutput(262250, 2752 , BlockTypes.RED_SANDSTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.RED_SANDSTONE, xzDirections.SLAB, yDirections.UP), new BlockOutput(5754, 15093 , BlockTypes.RED_SANDSTONE_SLAB));
        blockMap.put(new BlockInput(Blocks.RED_SANDSTONE, xzDirections.SLAB, yDirections.DOWN), new BlockOutput(4730, 15092 , BlockTypes.RED_SANDSTONE_SLAB));
        blockMap.put(new BlockInput(Blocks.SANDSTONE, xzDirections.EAST, yDirections.UP), new BlockOutput(273774, 8693 , BlockTypes.SANDSTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.SANDSTONE, xzDirections.SOUTH, yDirections.UP), new BlockOutput(271726, 8691 , BlockTypes.SANDSTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.SANDSTONE, xzDirections.WEST, yDirections.UP), new BlockOutput(272750, 8692 , BlockTypes.SANDSTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.SANDSTONE, xzDirections.NORTH, yDirections.UP), new BlockOutput(270702, 8690 , BlockTypes.SANDSTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.SANDSTONE, xzDirections.EAST, yDirections.DOWN), new BlockOutput(265582, 8689 , BlockTypes.SANDSTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.SANDSTONE, xzDirections.SOUTH, yDirections.DOWN), new BlockOutput(263534, 8687 , BlockTypes.SANDSTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.SANDSTONE, xzDirections.WEST, yDirections.DOWN), new BlockOutput(264558, 8688 , BlockTypes.SANDSTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.SANDSTONE, xzDirections.NORTH, yDirections.DOWN), new BlockOutput(262510, 8686 , BlockTypes.SANDSTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.SANDSTONE, xzDirections.SLAB, yDirections.UP), new BlockOutput(5180, 2054 , BlockTypes.SANDSTONE_SLAB));
        blockMap.put(new BlockInput(Blocks.SANDSTONE, xzDirections.SLAB, yDirections.DOWN), new BlockOutput(4156, 2053 , BlockTypes.SANDSTONE_SLAB));
        blockMap.put(new BlockInput(Blocks.MOSSY_COBBLESTONE, xzDirections.EAST, yDirections.UP), new BlockOutput(274184, 18383 , BlockTypes.MOSSY_COBBLESTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.MOSSY_COBBLESTONE, xzDirections.SOUTH, yDirections.UP), new BlockOutput(272136, 18381 , BlockTypes.MOSSY_COBBLESTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.MOSSY_COBBLESTONE, xzDirections.WEST, yDirections.UP), new BlockOutput(273160, 18382 , BlockTypes.MOSSY_COBBLESTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.MOSSY_COBBLESTONE, xzDirections.NORTH, yDirections.UP), new BlockOutput(271112, 18380 , BlockTypes.MOSSY_COBBLESTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.MOSSY_COBBLESTONE, xzDirections.EAST, yDirections.DOWN), new BlockOutput(265992, 18379 , BlockTypes.MOSSY_COBBLESTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.MOSSY_COBBLESTONE, xzDirections.SOUTH, yDirections.DOWN), new BlockOutput(263944, 18377 , BlockTypes.MOSSY_COBBLESTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.MOSSY_COBBLESTONE, xzDirections.WEST, yDirections.DOWN), new BlockOutput(264968, 18378 , BlockTypes.MOSSY_COBBLESTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.MOSSY_COBBLESTONE, xzDirections.NORTH, yDirections.DOWN), new BlockOutput(262920, 18376 , BlockTypes.MOSSY_COBBLESTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.MOSSY_COBBLESTONE, xzDirections.SLAB, yDirections.UP), new BlockOutput(5851, 17170 , BlockTypes.MOSSY_COBBLESTONE_SLAB));
        blockMap.put(new BlockInput(Blocks.MOSSY_COBBLESTONE, xzDirections.SLAB, yDirections.DOWN), new BlockOutput(4827, 17169 , BlockTypes.MOSSY_COBBLESTONE_SLAB));
        blockMap.put(new BlockInput(Blocks.COBBLESTONE, xzDirections.EAST, yDirections.UP), new BlockOutput(273914, 12640 , BlockTypes.COBBLESTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.COBBLESTONE, xzDirections.SOUTH, yDirections.UP), new BlockOutput(271866, 12638 , BlockTypes.COBBLESTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.COBBLESTONE, xzDirections.WEST, yDirections.UP), new BlockOutput(272890, 12639 , BlockTypes.COBBLESTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.COBBLESTONE, xzDirections.NORTH, yDirections.UP), new BlockOutput(270842, 12637 , BlockTypes.COBBLESTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.COBBLESTONE, xzDirections.EAST, yDirections.DOWN), new BlockOutput(265722, 12636 , BlockTypes.COBBLESTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.COBBLESTONE, xzDirections.SOUTH, yDirections.DOWN), new BlockOutput(263674, 12634 , BlockTypes.COBBLESTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.COBBLESTONE, xzDirections.WEST, yDirections.DOWN), new BlockOutput(264698, 12635 , BlockTypes.COBBLESTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.COBBLESTONE, xzDirections.NORTH, yDirections.DOWN), new BlockOutput(262650, 12633 , BlockTypes.COBBLESTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.COBBLESTONE, xzDirections.SLAB, yDirections.UP), new BlockOutput(5204, 2571 , BlockTypes.COBBLESTONE_SLAB));
        blockMap.put(new BlockInput(Blocks.COBBLESTONE, xzDirections.SLAB, yDirections.DOWN), new BlockOutput(4180, 2570 , BlockTypes.COBBLESTONE_SLAB));
        blockMap.put(new BlockInput(Blocks.ANDESITE, xzDirections.EAST, yDirections.UP), new BlockOutput(274337, 22419 , BlockTypes.ANDESITE_STAIRS));
        blockMap.put(new BlockInput(Blocks.ANDESITE, xzDirections.SOUTH, yDirections.UP), new BlockOutput(272289, 22417 , BlockTypes.ANDESITE_STAIRS));
        blockMap.put(new BlockInput(Blocks.ANDESITE, xzDirections.WEST, yDirections.UP), new BlockOutput(273313, 22418 , BlockTypes.ANDESITE_STAIRS));
        blockMap.put(new BlockInput(Blocks.ANDESITE, xzDirections.NORTH, yDirections.UP), new BlockOutput(271265, 22416 , BlockTypes.ANDESITE_STAIRS));
        blockMap.put(new BlockInput(Blocks.ANDESITE, xzDirections.EAST, yDirections.DOWN), new BlockOutput(266145, 22415 , BlockTypes.ANDESITE_STAIRS));
        blockMap.put(new BlockInput(Blocks.ANDESITE, xzDirections.SOUTH, yDirections.DOWN), new BlockOutput(264097, 22413 , BlockTypes.ANDESITE_STAIRS));
        blockMap.put(new BlockInput(Blocks.ANDESITE, xzDirections.WEST, yDirections.DOWN), new BlockOutput(265121, 22414 , BlockTypes.ANDESITE_STAIRS));
        blockMap.put(new BlockInput(Blocks.ANDESITE, xzDirections.NORTH, yDirections.DOWN), new BlockOutput(263073, 22412 , BlockTypes.ANDESITE_STAIRS));
        blockMap.put(new BlockInput(Blocks.ANDESITE, xzDirections.SLAB, yDirections.UP), new BlockOutput(5416, 6700 , BlockTypes.ANDESITE_SLAB));
        blockMap.put(new BlockInput(Blocks.ANDESITE, xzDirections.SLAB, yDirections.DOWN), new BlockOutput(4392, 6699 , BlockTypes.ANDESITE_SLAB));
        blockMap.put(new BlockInput(Blocks.DIORITE, xzDirections.EAST, yDirections.UP), new BlockOutput(273448, 1496 , BlockTypes.DIORITE_STAIRS));
        blockMap.put(new BlockInput(Blocks.DIORITE, xzDirections.SOUTH, yDirections.UP), new BlockOutput(271400, 1494 , BlockTypes.DIORITE_STAIRS));
        blockMap.put(new BlockInput(Blocks.DIORITE, xzDirections.WEST, yDirections.UP), new BlockOutput(272424, 1495 , BlockTypes.DIORITE_STAIRS));
        blockMap.put(new BlockInput(Blocks.DIORITE, xzDirections.NORTH, yDirections.UP), new BlockOutput(270376, 1493 , BlockTypes.DIORITE_STAIRS));
        blockMap.put(new BlockInput(Blocks.DIORITE, xzDirections.EAST, yDirections.DOWN), new BlockOutput(265256, 1492 , BlockTypes.DIORITE_STAIRS));
        blockMap.put(new BlockInput(Blocks.DIORITE, xzDirections.SOUTH, yDirections.DOWN), new BlockOutput(263208, 1490 , BlockTypes.DIORITE_STAIRS));
        blockMap.put(new BlockInput(Blocks.DIORITE, xzDirections.WEST, yDirections.DOWN), new BlockOutput(264232, 1491 , BlockTypes.DIORITE_STAIRS));
        blockMap.put(new BlockInput(Blocks.DIORITE, xzDirections.NORTH, yDirections.DOWN), new BlockOutput(262184, 1489 , BlockTypes.DIORITE_STAIRS));
        blockMap.put(new BlockInput(Blocks.DIORITE, xzDirections.SLAB, yDirections.UP), new BlockOutput(5959, 20896 , BlockTypes.DIORITE_SLAB));
        blockMap.put(new BlockInput(Blocks.DIORITE, xzDirections.SLAB, yDirections.DOWN), new BlockOutput(4935, 20895 , BlockTypes.DIORITE_SLAB));
        blockMap.put(new BlockInput(Blocks.GRANITE, xzDirections.EAST, yDirections.UP), new BlockOutput(273435, 1376 , BlockTypes.GRANITE_STAIRS));
        blockMap.put(new BlockInput(Blocks.GRANITE, xzDirections.SOUTH, yDirections.UP), new BlockOutput(271387, 1374 , BlockTypes.GRANITE_STAIRS));
        blockMap.put(new BlockInput(Blocks.GRANITE, xzDirections.WEST, yDirections.UP), new BlockOutput(272411, 1375 , BlockTypes.GRANITE_STAIRS));
        blockMap.put(new BlockInput(Blocks.GRANITE, xzDirections.NORTH, yDirections.UP), new BlockOutput(270363, 1373 , BlockTypes.GRANITE_STAIRS));
        blockMap.put(new BlockInput(Blocks.GRANITE, xzDirections.EAST, yDirections.DOWN), new BlockOutput(265243, 1372 , BlockTypes.GRANITE_STAIRS));
        blockMap.put(new BlockInput(Blocks.GRANITE, xzDirections.SOUTH, yDirections.DOWN), new BlockOutput(263195, 1370 , BlockTypes.GRANITE_STAIRS));
        blockMap.put(new BlockInput(Blocks.GRANITE, xzDirections.WEST, yDirections.DOWN), new BlockOutput(264219, 1371 , BlockTypes.GRANITE_STAIRS));
        blockMap.put(new BlockInput(Blocks.GRANITE, xzDirections.NORTH, yDirections.DOWN), new BlockOutput(262171, 1369 , BlockTypes.GRANITE_STAIRS));
        blockMap.put(new BlockInput(Blocks.GRANITE, xzDirections.SLAB, yDirections.UP), new BlockOutput(5810, 16619 , BlockTypes.GRANITE_SLAB));
        blockMap.put(new BlockInput(Blocks.GRANITE, xzDirections.SLAB, yDirections.DOWN), new BlockOutput(4786, 16618 , BlockTypes.GRANITE_SLAB));
        blockMap.put(new BlockInput(Blocks.STONE, xzDirections.EAST, yDirections.UP), new BlockOutput(273792, 10084 , BlockTypes.STONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.STONE, xzDirections.SOUTH, yDirections.UP), new BlockOutput(271744, 10082 , BlockTypes.STONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.STONE, xzDirections.WEST, yDirections.UP), new BlockOutput(272768, 10083 , BlockTypes.STONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.STONE, xzDirections.NORTH, yDirections.UP), new BlockOutput(270720, 10081 , BlockTypes.STONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.STONE, xzDirections.EAST, yDirections.DOWN), new BlockOutput(265600, 10080 , BlockTypes.STONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.STONE, xzDirections.SOUTH, yDirections.DOWN), new BlockOutput(263552, 10078 , BlockTypes.STONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.STONE, xzDirections.WEST, yDirections.DOWN), new BlockOutput(264576, 10079 , BlockTypes.STONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.STONE, xzDirections.NORTH, yDirections.DOWN), new BlockOutput(262528, 10077 , BlockTypes.STONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.STONE, xzDirections.SLAB, yDirections.UP), new BlockOutput(5520, 10711 , BlockTypes.STONE_SLAB));
        blockMap.put(new BlockInput(Blocks.STONE, xzDirections.SLAB, yDirections.DOWN), new BlockOutput(4496, 10710 , BlockTypes.STONE_SLAB));

    }
}
