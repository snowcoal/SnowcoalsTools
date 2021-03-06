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
        STONE, ANDESITE, DIORITE, GRANITE, COBBLESTONE, MOSSY_COBBLESTONE, BLACKSTONE, COBBLED_DEEPSLATE, SANDSTONE, SMOOTH_SANDSTONE, RED_SANDSTONE, SMOOTH_RED_SANDSTONE;
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
        blockMap.put(new BlockInput(Blocks.STONE, xzDirections.NORTH, yDirections.UP), new BlockOutput(270688, 8968, BlockTypes.STONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.STONE, xzDirections.SOUTH, yDirections.UP), new BlockOutput(271712, 8969, BlockTypes.STONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.STONE, xzDirections.WEST, yDirections.UP), new BlockOutput(272736, 8970, BlockTypes.STONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.STONE, xzDirections.EAST, yDirections.UP), new BlockOutput(273760, 8971, BlockTypes.STONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.STONE, xzDirections.NORTH, yDirections.DOWN), new BlockOutput(262496, 8964, BlockTypes.STONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.STONE, xzDirections.SOUTH, yDirections.DOWN), new BlockOutput(263520, 8965, BlockTypes.STONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.STONE, xzDirections.WEST, yDirections.DOWN), new BlockOutput(264544, 8966, BlockTypes.STONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.STONE, xzDirections.EAST, yDirections.DOWN), new BlockOutput(265568, 8967, BlockTypes.STONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.STONE, xzDirections.SLAB, yDirections.UP), new BlockOutput(5488, 9598, BlockTypes.STONE_SLAB));
        blockMap.put(new BlockInput(Blocks.STONE, xzDirections.SLAB, yDirections.DOWN), new BlockOutput(4464, 9597, BlockTypes.STONE_SLAB));
        blockMap.put(new BlockInput(Blocks.ANDESITE, xzDirections.NORTH, yDirections.UP), new BlockOutput(271195, 19753, BlockTypes.ANDESITE_STAIRS));
        blockMap.put(new BlockInput(Blocks.ANDESITE, xzDirections.SOUTH, yDirections.UP), new BlockOutput(272219, 19754, BlockTypes.ANDESITE_STAIRS));
        blockMap.put(new BlockInput(Blocks.ANDESITE, xzDirections.WEST, yDirections.UP), new BlockOutput(273243, 19755, BlockTypes.ANDESITE_STAIRS));
        blockMap.put(new BlockInput(Blocks.ANDESITE, xzDirections.EAST, yDirections.UP), new BlockOutput(274267, 19756, BlockTypes.ANDESITE_STAIRS));
        blockMap.put(new BlockInput(Blocks.ANDESITE, xzDirections.NORTH, yDirections.DOWN), new BlockOutput(263003, 19749, BlockTypes.ANDESITE_STAIRS));
        blockMap.put(new BlockInput(Blocks.ANDESITE, xzDirections.SOUTH, yDirections.DOWN), new BlockOutput(264027, 19750, BlockTypes.ANDESITE_STAIRS));
        blockMap.put(new BlockInput(Blocks.ANDESITE, xzDirections.WEST, yDirections.DOWN), new BlockOutput(265051, 19751, BlockTypes.ANDESITE_STAIRS));
        blockMap.put(new BlockInput(Blocks.ANDESITE, xzDirections.EAST, yDirections.DOWN), new BlockOutput(266075, 19752, BlockTypes.ANDESITE_STAIRS));
        blockMap.put(new BlockInput(Blocks.ANDESITE, xzDirections.SLAB, yDirections.UP), new BlockOutput(5391, 6063, BlockTypes.ANDESITE_SLAB));
        blockMap.put(new BlockInput(Blocks.ANDESITE, xzDirections.SLAB, yDirections.DOWN), new BlockOutput(4367, 6062, BlockTypes.ANDESITE_SLAB));
        blockMap.put(new BlockInput(Blocks.DIORITE, xzDirections.NORTH, yDirections.UP), new BlockOutput(270372, 1442, BlockTypes.DIORITE_STAIRS));
        blockMap.put(new BlockInput(Blocks.DIORITE, xzDirections.SOUTH, yDirections.UP), new BlockOutput(271396, 1443, BlockTypes.DIORITE_STAIRS));
        blockMap.put(new BlockInput(Blocks.DIORITE, xzDirections.WEST, yDirections.UP), new BlockOutput(272420, 1444, BlockTypes.DIORITE_STAIRS));
        blockMap.put(new BlockInput(Blocks.DIORITE, xzDirections.EAST, yDirections.UP), new BlockOutput(273444, 1445, BlockTypes.DIORITE_STAIRS));
        blockMap.put(new BlockInput(Blocks.DIORITE, xzDirections.NORTH, yDirections.DOWN), new BlockOutput(262180, 1438, BlockTypes.DIORITE_STAIRS));
        blockMap.put(new BlockInput(Blocks.DIORITE, xzDirections.SOUTH, yDirections.DOWN), new BlockOutput(263204, 1439, BlockTypes.DIORITE_STAIRS));
        blockMap.put(new BlockInput(Blocks.DIORITE, xzDirections.WEST, yDirections.DOWN), new BlockOutput(264228, 1440, BlockTypes.DIORITE_STAIRS));
        blockMap.put(new BlockInput(Blocks.DIORITE, xzDirections.EAST, yDirections.DOWN), new BlockOutput(265252, 1441, BlockTypes.DIORITE_STAIRS));
        blockMap.put(new BlockInput(Blocks.DIORITE, xzDirections.SLAB, yDirections.UP), new BlockOutput(5898, 18463, BlockTypes.DIORITE_SLAB));
        blockMap.put(new BlockInput(Blocks.DIORITE, xzDirections.SLAB, yDirections.DOWN), new BlockOutput(4874, 18462, BlockTypes.DIORITE_SLAB));
        blockMap.put(new BlockInput(Blocks.GRANITE, xzDirections.NORTH, yDirections.UP), new BlockOutput(270360, 1338, BlockTypes.GRANITE_STAIRS));
        blockMap.put(new BlockInput(Blocks.GRANITE, xzDirections.SOUTH, yDirections.UP), new BlockOutput(271384, 1339, BlockTypes.GRANITE_STAIRS));
        blockMap.put(new BlockInput(Blocks.GRANITE, xzDirections.WEST, yDirections.UP), new BlockOutput(272408, 1340, BlockTypes.GRANITE_STAIRS));
        blockMap.put(new BlockInput(Blocks.GRANITE, xzDirections.EAST, yDirections.UP), new BlockOutput(273432, 1341, BlockTypes.GRANITE_STAIRS));
        blockMap.put(new BlockInput(Blocks.GRANITE, xzDirections.NORTH, yDirections.DOWN), new BlockOutput(262168, 1334, BlockTypes.GRANITE_STAIRS));
        blockMap.put(new BlockInput(Blocks.GRANITE, xzDirections.SOUTH, yDirections.DOWN), new BlockOutput(263192, 1335, BlockTypes.GRANITE_STAIRS));
        blockMap.put(new BlockInput(Blocks.GRANITE, xzDirections.WEST, yDirections.DOWN), new BlockOutput(264216, 1336, BlockTypes.GRANITE_STAIRS));
        blockMap.put(new BlockInput(Blocks.GRANITE, xzDirections.EAST, yDirections.DOWN), new BlockOutput(265240, 1337, BlockTypes.GRANITE_STAIRS));
        blockMap.put(new BlockInput(Blocks.GRANITE, xzDirections.SLAB, yDirections.UP), new BlockOutput(5758, 14639, BlockTypes.GRANITE_SLAB));
        blockMap.put(new BlockInput(Blocks.GRANITE, xzDirections.SLAB, yDirections.DOWN), new BlockOutput(4734, 14638, BlockTypes.GRANITE_SLAB));
        blockMap.put(new BlockInput(Blocks.COBBLESTONE, xzDirections.NORTH, yDirections.UP), new BlockOutput(270804, 11471, BlockTypes.COBBLESTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.COBBLESTONE, xzDirections.SOUTH, yDirections.UP), new BlockOutput(271828, 11472, BlockTypes.COBBLESTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.COBBLESTONE, xzDirections.WEST, yDirections.UP), new BlockOutput(272852, 11473, BlockTypes.COBBLESTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.COBBLESTONE, xzDirections.EAST, yDirections.UP), new BlockOutput(273876, 11474, BlockTypes.COBBLESTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.COBBLESTONE, xzDirections.NORTH, yDirections.DOWN), new BlockOutput(262612, 11467, BlockTypes.COBBLESTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.COBBLESTONE, xzDirections.SOUTH, yDirections.DOWN), new BlockOutput(263636, 11468, BlockTypes.COBBLESTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.COBBLESTONE, xzDirections.WEST, yDirections.DOWN), new BlockOutput(264660, 11469, BlockTypes.COBBLESTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.COBBLESTONE, xzDirections.EAST, yDirections.DOWN), new BlockOutput(265684, 11470, BlockTypes.COBBLESTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.COBBLESTONE, xzDirections.SLAB, yDirections.UP), new BlockOutput(5194, 2275, BlockTypes.COBBLESTONE_SLAB));
        blockMap.put(new BlockInput(Blocks.COBBLESTONE, xzDirections.SLAB, yDirections.DOWN), new BlockOutput(4170, 2274, BlockTypes.COBBLESTONE_SLAB));
        blockMap.put(new BlockInput(Blocks.MOSSY_COBBLESTONE, xzDirections.NORTH, yDirections.UP), new BlockOutput(271055, 16246, BlockTypes.MOSSY_COBBLESTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.MOSSY_COBBLESTONE, xzDirections.SOUTH, yDirections.UP), new BlockOutput(272079, 16247, BlockTypes.MOSSY_COBBLESTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.MOSSY_COBBLESTONE, xzDirections.WEST, yDirections.UP), new BlockOutput(273103, 16248, BlockTypes.MOSSY_COBBLESTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.MOSSY_COBBLESTONE, xzDirections.EAST, yDirections.UP), new BlockOutput(274127, 16249, BlockTypes.MOSSY_COBBLESTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.MOSSY_COBBLESTONE, xzDirections.NORTH, yDirections.DOWN), new BlockOutput(262863, 16242, BlockTypes.MOSSY_COBBLESTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.MOSSY_COBBLESTONE, xzDirections.SOUTH, yDirections.DOWN), new BlockOutput(263887, 16243, BlockTypes.MOSSY_COBBLESTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.MOSSY_COBBLESTONE, xzDirections.WEST, yDirections.DOWN), new BlockOutput(264911, 16244, BlockTypes.MOSSY_COBBLESTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.MOSSY_COBBLESTONE, xzDirections.EAST, yDirections.DOWN), new BlockOutput(265935, 16245, BlockTypes.MOSSY_COBBLESTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.MOSSY_COBBLESTONE, xzDirections.SLAB, yDirections.UP), new BlockOutput(5796, 15040, BlockTypes.MOSSY_COBBLESTONE_SLAB));
        blockMap.put(new BlockInput(Blocks.MOSSY_COBBLESTONE, xzDirections.SLAB, yDirections.DOWN), new BlockOutput(4772, 15039, BlockTypes.MOSSY_COBBLESTONE_SLAB));
        blockMap.put(new BlockInput(Blocks.BLACKSTONE, xzDirections.NORTH, yDirections.UP), new BlockOutput(270351, 1088, BlockTypes.BLACKSTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.BLACKSTONE, xzDirections.SOUTH, yDirections.UP), new BlockOutput(271375, 1089, BlockTypes.BLACKSTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.BLACKSTONE, xzDirections.WEST, yDirections.UP), new BlockOutput(272399, 1090, BlockTypes.BLACKSTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.BLACKSTONE, xzDirections.EAST, yDirections.UP), new BlockOutput(273423, 1091, BlockTypes.BLACKSTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.BLACKSTONE, xzDirections.NORTH, yDirections.DOWN), new BlockOutput(262159, 1084, BlockTypes.BLACKSTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.BLACKSTONE, xzDirections.SOUTH, yDirections.DOWN), new BlockOutput(263183, 1085, BlockTypes.BLACKSTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.BLACKSTONE, xzDirections.WEST, yDirections.DOWN), new BlockOutput(264207, 1086, BlockTypes.BLACKSTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.BLACKSTONE, xzDirections.EAST, yDirections.DOWN), new BlockOutput(265231, 1087, BlockTypes.BLACKSTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.BLACKSTONE, xzDirections.SLAB, yDirections.UP), new BlockOutput(5949, 19078, BlockTypes.BLACKSTONE_SLAB));
        blockMap.put(new BlockInput(Blocks.BLACKSTONE, xzDirections.SLAB, yDirections.DOWN), new BlockOutput(4925, 19077, BlockTypes.BLACKSTONE_SLAB));
        blockMap.put(new BlockInput(Blocks.COBBLED_DEEPSLATE, xzDirections.NORTH, yDirections.UP), new BlockOutput(270778, 11248, BlockTypes.COBBLED_DEEPSLATE_STAIRS));
        blockMap.put(new BlockInput(Blocks.COBBLED_DEEPSLATE, xzDirections.SOUTH, yDirections.UP), new BlockOutput(271802, 11249, BlockTypes.COBBLED_DEEPSLATE_STAIRS));
        blockMap.put(new BlockInput(Blocks.COBBLED_DEEPSLATE, xzDirections.WEST, yDirections.UP), new BlockOutput(272826, 11250, BlockTypes.COBBLED_DEEPSLATE_STAIRS));
        blockMap.put(new BlockInput(Blocks.COBBLED_DEEPSLATE, xzDirections.EAST, yDirections.UP), new BlockOutput(273850, 11251, BlockTypes.COBBLED_DEEPSLATE_STAIRS));
        blockMap.put(new BlockInput(Blocks.COBBLED_DEEPSLATE, xzDirections.NORTH, yDirections.DOWN), new BlockOutput(262586, 11244, BlockTypes.COBBLED_DEEPSLATE_STAIRS));
        blockMap.put(new BlockInput(Blocks.COBBLED_DEEPSLATE, xzDirections.SOUTH, yDirections.DOWN), new BlockOutput(263610, 11245, BlockTypes.COBBLED_DEEPSLATE_STAIRS));
        blockMap.put(new BlockInput(Blocks.COBBLED_DEEPSLATE, xzDirections.WEST, yDirections.DOWN), new BlockOutput(264634, 11246, BlockTypes.COBBLED_DEEPSLATE_STAIRS));
        blockMap.put(new BlockInput(Blocks.COBBLED_DEEPSLATE, xzDirections.EAST, yDirections.DOWN), new BlockOutput(265658, 11247, BlockTypes.COBBLED_DEEPSLATE_STAIRS));
        blockMap.put(new BlockInput(Blocks.COBBLED_DEEPSLATE, xzDirections.SLAB, yDirections.UP), new BlockOutput(6018, 20341, BlockTypes.COBBLED_DEEPSLATE_SLAB));
        blockMap.put(new BlockInput(Blocks.COBBLED_DEEPSLATE, xzDirections.SLAB, yDirections.DOWN), new BlockOutput(4994, 20340, BlockTypes.COBBLED_DEEPSLATE_SLAB));
        blockMap.put(new BlockInput(Blocks.SANDSTONE, xzDirections.NORTH, yDirections.UP), new BlockOutput(270673, 8005, BlockTypes.SANDSTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.SANDSTONE, xzDirections.SOUTH, yDirections.UP), new BlockOutput(271697, 8006, BlockTypes.SANDSTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.SANDSTONE, xzDirections.WEST, yDirections.UP), new BlockOutput(272721, 8007, BlockTypes.SANDSTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.SANDSTONE, xzDirections.EAST, yDirections.UP), new BlockOutput(273745, 8008, BlockTypes.SANDSTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.SANDSTONE, xzDirections.NORTH, yDirections.DOWN), new BlockOutput(262481, 8001, BlockTypes.SANDSTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.SANDSTONE, xzDirections.SOUTH, yDirections.DOWN), new BlockOutput(263505, 8002, BlockTypes.SANDSTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.SANDSTONE, xzDirections.WEST, yDirections.DOWN), new BlockOutput(264529, 8003, BlockTypes.SANDSTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.SANDSTONE, xzDirections.EAST, yDirections.DOWN), new BlockOutput(265553, 8004, BlockTypes.SANDSTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.SANDSTONE, xzDirections.SLAB, yDirections.UP), new BlockOutput(5173, 1928, BlockTypes.SANDSTONE_SLAB));
        blockMap.put(new BlockInput(Blocks.SANDSTONE, xzDirections.SLAB, yDirections.DOWN), new BlockOutput(4149, 1927, BlockTypes.SANDSTONE_SLAB));
        blockMap.put(new BlockInput(Blocks.SMOOTH_SANDSTONE, xzDirections.NORTH, yDirections.UP), new BlockOutput(270900, 13338, BlockTypes.SMOOTH_SANDSTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.SMOOTH_SANDSTONE, xzDirections.SOUTH, yDirections.UP), new BlockOutput(271924, 13339, BlockTypes.SMOOTH_SANDSTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.SMOOTH_SANDSTONE, xzDirections.WEST, yDirections.UP), new BlockOutput(272948, 13340, BlockTypes.SMOOTH_SANDSTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.SMOOTH_SANDSTONE, xzDirections.EAST, yDirections.UP), new BlockOutput(273972, 13341, BlockTypes.SMOOTH_SANDSTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.SMOOTH_SANDSTONE, xzDirections.NORTH, yDirections.DOWN), new BlockOutput(262708, 13334, BlockTypes.SMOOTH_SANDSTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.SMOOTH_SANDSTONE, xzDirections.SOUTH, yDirections.DOWN), new BlockOutput(263732, 13335, BlockTypes.SMOOTH_SANDSTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.SMOOTH_SANDSTONE, xzDirections.WEST, yDirections.DOWN), new BlockOutput(264756, 13336, BlockTypes.SMOOTH_SANDSTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.SMOOTH_SANDSTONE, xzDirections.EAST, yDirections.DOWN), new BlockOutput(265780, 13337, BlockTypes.SMOOTH_SANDSTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.SMOOTH_SANDSTONE, xzDirections.SLAB, yDirections.UP), new BlockOutput(5547, 10797, BlockTypes.SMOOTH_SANDSTONE_SLAB));
        blockMap.put(new BlockInput(Blocks.SMOOTH_SANDSTONE, xzDirections.SLAB, yDirections.DOWN), new BlockOutput(4523, 10796, BlockTypes.SMOOTH_SANDSTONE_SLAB));
        blockMap.put(new BlockInput(Blocks.RED_SANDSTONE, xzDirections.NORTH, yDirections.UP), new BlockOutput(270429, 2373, BlockTypes.RED_SANDSTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.RED_SANDSTONE, xzDirections.SOUTH, yDirections.UP), new BlockOutput(271453, 2374, BlockTypes.RED_SANDSTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.RED_SANDSTONE, xzDirections.WEST, yDirections.UP), new BlockOutput(272477, 2375, BlockTypes.RED_SANDSTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.RED_SANDSTONE, xzDirections.EAST, yDirections.UP), new BlockOutput(273501, 2376, BlockTypes.RED_SANDSTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.RED_SANDSTONE, xzDirections.NORTH, yDirections.DOWN), new BlockOutput(262237, 2369, BlockTypes.RED_SANDSTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.RED_SANDSTONE, xzDirections.SOUTH, yDirections.DOWN), new BlockOutput(263261, 2370, BlockTypes.RED_SANDSTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.RED_SANDSTONE, xzDirections.WEST, yDirections.DOWN), new BlockOutput(264285, 2371, BlockTypes.RED_SANDSTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.RED_SANDSTONE, xzDirections.EAST, yDirections.DOWN), new BlockOutput(265309, 2372, BlockTypes.RED_SANDSTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.RED_SANDSTONE, xzDirections.SLAB, yDirections.UP), new BlockOutput(5707, 13608, BlockTypes.RED_SANDSTONE_SLAB));
        blockMap.put(new BlockInput(Blocks.RED_SANDSTONE, xzDirections.SLAB, yDirections.DOWN), new BlockOutput(4683, 13607, BlockTypes.RED_SANDSTONE_SLAB));
        blockMap.put(new BlockInput(Blocks.SMOOTH_RED_SANDSTONE, xzDirections.NORTH, yDirections.UP), new BlockOutput(270505, 3641, BlockTypes.SMOOTH_RED_SANDSTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.SMOOTH_RED_SANDSTONE, xzDirections.SOUTH, yDirections.UP), new BlockOutput(271529, 3642, BlockTypes.SMOOTH_RED_SANDSTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.SMOOTH_RED_SANDSTONE, xzDirections.WEST, yDirections.UP), new BlockOutput(272553, 3643, BlockTypes.SMOOTH_RED_SANDSTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.SMOOTH_RED_SANDSTONE, xzDirections.EAST, yDirections.UP), new BlockOutput(273577, 3644, BlockTypes.SMOOTH_RED_SANDSTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.SMOOTH_RED_SANDSTONE, xzDirections.NORTH, yDirections.DOWN), new BlockOutput(262313, 3637, BlockTypes.SMOOTH_RED_SANDSTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.SMOOTH_RED_SANDSTONE, xzDirections.SOUTH, yDirections.DOWN), new BlockOutput(263337, 3638, BlockTypes.SMOOTH_RED_SANDSTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.SMOOTH_RED_SANDSTONE, xzDirections.WEST, yDirections.DOWN), new BlockOutput(264361, 3639, BlockTypes.SMOOTH_RED_SANDSTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.SMOOTH_RED_SANDSTONE, xzDirections.EAST, yDirections.DOWN), new BlockOutput(265385, 3640, BlockTypes.SMOOTH_RED_SANDSTONE_STAIRS));
        blockMap.put(new BlockInput(Blocks.SMOOTH_RED_SANDSTONE, xzDirections.SLAB, yDirections.UP), new BlockOutput(5818, 15511, BlockTypes.SMOOTH_RED_SANDSTONE_SLAB));
        blockMap.put(new BlockInput(Blocks.SMOOTH_RED_SANDSTONE, xzDirections.SLAB, yDirections.DOWN), new BlockOutput(4794, 15510, BlockTypes.SMOOTH_RED_SANDSTONE_SLAB));
    }
}
