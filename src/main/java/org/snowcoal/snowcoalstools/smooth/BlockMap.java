package org.snowcoal.snowcoalstools.smooth;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import com.sk89q.worldedit.world.block.BlockType;
import com.sk89q.worldedit.world.block.BlockTypes;
import org.bukkit.Bukkit;
import org.snowcoal.snowcoalstools.SnowcoalsTools;

public class BlockMap {
    private Map<BlockInput, BlockOutput> blockMap;

    public BlockMap(SnowcoalsTools instance) {
        this.blockMap = new HashMap<>();

        // load specific values based on current version
        String version = Bukkit.getMinecraftVersion();
        switch (version) {
            case "1.18.1":
                addAllBlocks_1_18_1();
                break;
            case "1.19.3":
                addAllBlocks_1_19_3();
                break;
            case "1.20.1":
                addAllBlocks_1_20_1();
                break;
            default:
                instance.getLogger().log(Level.WARNING, "//smoothstairslab command is not supported for this Minecraft version: " + version);
                break;
        }
    }


    /**
     * Check if SSMS command enabled
     *
     */
    public boolean isSMSSEnabled() {
        return blockMap.size() != 0;
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
     * Adds all the known 1.18.1 constant values to the hashmap
     *
     */
    private void addAllBlocks_1_18_1() {
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


    /**
     * Adds all the known 1.19.3 constant values to the hashmap
     *
     */
    private void addAllBlocks_1_19_3() {
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


    /**
     * Adds all the known 1.20.1 constant values to the hashmap
     *
     */
    private void addAllBlocks_1_20_1() {
        // SCRIPT GENERATED CODE BASED ON KNOWN VALUES
        blockMap.put(new BlockInput(Blocks.STONE, xzDirections.NORTH, yDirections.UP), new BlockOutput(270733, 10671, BlockTypes.STONE));
        blockMap.put(new BlockInput(Blocks.GRANITE, xzDirections.NORTH, yDirections.UP), new BlockOutput(270363, 1373, BlockTypes.GRANITE));
        blockMap.put(new BlockInput(Blocks.DIORITE, xzDirections.NORTH, yDirections.UP), new BlockOutput(270377, 1494, BlockTypes.DIORITE));
        blockMap.put(new BlockInput(Blocks.ANDESITE, xzDirections.NORTH, yDirections.UP), new BlockOutput(271294, 23313, BlockTypes.ANDESITE));
        blockMap.put(new BlockInput(Blocks.COBBLESTONE, xzDirections.NORTH, yDirections.UP), new BlockOutput(270857, 13253, BlockTypes.COBBLESTONE));
        blockMap.put(new BlockInput(Blocks.MOSSY_COBBLESTONE, xzDirections.NORTH, yDirections.UP), new BlockOutput(271136, 19172, BlockTypes.MOSSY_COBBLESTONE));
        blockMap.put(new BlockInput(Blocks.BLACKSTONE, xzDirections.NORTH, yDirections.UP), new BlockOutput(270351, 1088, BlockTypes.BLACKSTONE));
        blockMap.put(new BlockInput(Blocks.COBBLED_DEEPSLATE, xzDirections.NORTH, yDirections.UP), new BlockOutput(270826, 12974, BlockTypes.COBBLED_DEEPSLATE));
        blockMap.put(new BlockInput(Blocks.PRISMARINE, xzDirections.NORTH, yDirections.UP), new BlockOutput(271008, 16389, BlockTypes.PRISMARINE));
        blockMap.put(new BlockInput(Blocks.RED_SANDSTONE, xzDirections.NORTH, yDirections.UP), new BlockOutput(270447, 3151, BlockTypes.RED_SANDSTONE));
        blockMap.put(new BlockInput(Blocks.SANDSTONE, xzDirections.NORTH, yDirections.UP), new BlockOutput(270715, 9280, BlockTypes.SANDSTONE));
        blockMap.put(new BlockInput(Blocks.STONE, xzDirections.EAST, yDirections.UP), new BlockOutput(273805, 10674, BlockTypes.STONE));
        blockMap.put(new BlockInput(Blocks.GRANITE, xzDirections.EAST, yDirections.UP), new BlockOutput(273435, 1376, BlockTypes.GRANITE));
        blockMap.put(new BlockInput(Blocks.DIORITE, xzDirections.EAST, yDirections.UP), new BlockOutput(273449, 1497, BlockTypes.DIORITE));
        blockMap.put(new BlockInput(Blocks.ANDESITE, xzDirections.EAST, yDirections.UP), new BlockOutput(274366, 23316, BlockTypes.ANDESITE));
        blockMap.put(new BlockInput(Blocks.COBBLESTONE, xzDirections.EAST, yDirections.UP), new BlockOutput(273929, 13256, BlockTypes.COBBLESTONE));
        blockMap.put(new BlockInput(Blocks.MOSSY_COBBLESTONE, xzDirections.EAST, yDirections.UP), new BlockOutput(274208, 19175, BlockTypes.MOSSY_COBBLESTONE));
        blockMap.put(new BlockInput(Blocks.BLACKSTONE, xzDirections.EAST, yDirections.UP), new BlockOutput(273423, 1091, BlockTypes.BLACKSTONE));
        blockMap.put(new BlockInput(Blocks.COBBLED_DEEPSLATE, xzDirections.EAST, yDirections.UP), new BlockOutput(273898, 12977, BlockTypes.COBBLED_DEEPSLATE));
        blockMap.put(new BlockInput(Blocks.PRISMARINE, xzDirections.EAST, yDirections.UP), new BlockOutput(274080, 16392, BlockTypes.PRISMARINE));
        blockMap.put(new BlockInput(Blocks.RED_SANDSTONE, xzDirections.EAST, yDirections.UP), new BlockOutput(273519, 3154, BlockTypes.RED_SANDSTONE));
        blockMap.put(new BlockInput(Blocks.SANDSTONE, xzDirections.EAST, yDirections.UP), new BlockOutput(273787, 9283, BlockTypes.SANDSTONE));
        blockMap.put(new BlockInput(Blocks.STONE, xzDirections.SOUTH, yDirections.UP), new BlockOutput(271757, 10672, BlockTypes.STONE));
        blockMap.put(new BlockInput(Blocks.GRANITE, xzDirections.SOUTH, yDirections.UP), new BlockOutput(271387, 1374, BlockTypes.GRANITE));
        blockMap.put(new BlockInput(Blocks.DIORITE, xzDirections.SOUTH, yDirections.UP), new BlockOutput(271401, 1495, BlockTypes.DIORITE));
        blockMap.put(new BlockInput(Blocks.ANDESITE, xzDirections.SOUTH, yDirections.UP), new BlockOutput(272318, 23314, BlockTypes.ANDESITE));
        blockMap.put(new BlockInput(Blocks.COBBLESTONE, xzDirections.SOUTH, yDirections.UP), new BlockOutput(271881, 13254, BlockTypes.COBBLESTONE));
        blockMap.put(new BlockInput(Blocks.MOSSY_COBBLESTONE, xzDirections.SOUTH, yDirections.UP), new BlockOutput(272160, 19173, BlockTypes.MOSSY_COBBLESTONE));
        blockMap.put(new BlockInput(Blocks.BLACKSTONE, xzDirections.SOUTH, yDirections.UP), new BlockOutput(271375, 1089, BlockTypes.BLACKSTONE));
        blockMap.put(new BlockInput(Blocks.COBBLED_DEEPSLATE, xzDirections.SOUTH, yDirections.UP), new BlockOutput(271850, 12975, BlockTypes.COBBLED_DEEPSLATE));
        blockMap.put(new BlockInput(Blocks.PRISMARINE, xzDirections.SOUTH, yDirections.UP), new BlockOutput(272032, 16390, BlockTypes.PRISMARINE));
        blockMap.put(new BlockInput(Blocks.RED_SANDSTONE, xzDirections.SOUTH, yDirections.UP), new BlockOutput(271471, 3152, BlockTypes.RED_SANDSTONE));
        blockMap.put(new BlockInput(Blocks.SANDSTONE, xzDirections.SOUTH, yDirections.UP), new BlockOutput(271739, 9281, BlockTypes.SANDSTONE));
        blockMap.put(new BlockInput(Blocks.STONE, xzDirections.WEST, yDirections.UP), new BlockOutput(272781, 10673, BlockTypes.STONE));
        blockMap.put(new BlockInput(Blocks.GRANITE, xzDirections.WEST, yDirections.UP), new BlockOutput(272411, 1375, BlockTypes.GRANITE));
        blockMap.put(new BlockInput(Blocks.DIORITE, xzDirections.WEST, yDirections.UP), new BlockOutput(272425, 1496, BlockTypes.DIORITE));
        blockMap.put(new BlockInput(Blocks.ANDESITE, xzDirections.WEST, yDirections.UP), new BlockOutput(273342, 23315, BlockTypes.ANDESITE));
        blockMap.put(new BlockInput(Blocks.COBBLESTONE, xzDirections.WEST, yDirections.UP), new BlockOutput(272905, 13255, BlockTypes.COBBLESTONE));
        blockMap.put(new BlockInput(Blocks.MOSSY_COBBLESTONE, xzDirections.WEST, yDirections.UP), new BlockOutput(273184, 19174, BlockTypes.MOSSY_COBBLESTONE));
        blockMap.put(new BlockInput(Blocks.BLACKSTONE, xzDirections.WEST, yDirections.UP), new BlockOutput(272399, 1090, BlockTypes.BLACKSTONE));
        blockMap.put(new BlockInput(Blocks.COBBLED_DEEPSLATE, xzDirections.WEST, yDirections.UP), new BlockOutput(272874, 12976, BlockTypes.COBBLED_DEEPSLATE));
        blockMap.put(new BlockInput(Blocks.PRISMARINE, xzDirections.WEST, yDirections.UP), new BlockOutput(273056, 16391, BlockTypes.PRISMARINE));
        blockMap.put(new BlockInput(Blocks.RED_SANDSTONE, xzDirections.WEST, yDirections.UP), new BlockOutput(272495, 3153, BlockTypes.RED_SANDSTONE));
        blockMap.put(new BlockInput(Blocks.SANDSTONE, xzDirections.WEST, yDirections.UP), new BlockOutput(272763, 9282, BlockTypes.SANDSTONE));
        blockMap.put(new BlockInput(Blocks.STONE, xzDirections.NORTH, yDirections.DOWN), new BlockOutput(262541, 10667, BlockTypes.STONE));
        blockMap.put(new BlockInput(Blocks.GRANITE, xzDirections.NORTH, yDirections.DOWN), new BlockOutput(262171, 1369, BlockTypes.GRANITE));
        blockMap.put(new BlockInput(Blocks.DIORITE, xzDirections.NORTH, yDirections.DOWN), new BlockOutput(262185, 1490, BlockTypes.DIORITE));
        blockMap.put(new BlockInput(Blocks.ANDESITE, xzDirections.NORTH, yDirections.DOWN), new BlockOutput(263102, 23309, BlockTypes.ANDESITE));
        blockMap.put(new BlockInput(Blocks.COBBLESTONE, xzDirections.NORTH, yDirections.DOWN), new BlockOutput(262665, 13249, BlockTypes.COBBLESTONE));
        blockMap.put(new BlockInput(Blocks.MOSSY_COBBLESTONE, xzDirections.NORTH, yDirections.DOWN), new BlockOutput(262944, 19168, BlockTypes.MOSSY_COBBLESTONE));
        blockMap.put(new BlockInput(Blocks.BLACKSTONE, xzDirections.NORTH, yDirections.DOWN), new BlockOutput(262159, 1084, BlockTypes.BLACKSTONE));
        blockMap.put(new BlockInput(Blocks.COBBLED_DEEPSLATE, xzDirections.NORTH, yDirections.DOWN), new BlockOutput(262634, 12970, BlockTypes.COBBLED_DEEPSLATE));
        blockMap.put(new BlockInput(Blocks.PRISMARINE, xzDirections.NORTH, yDirections.DOWN), new BlockOutput(262816, 16385, BlockTypes.PRISMARINE));
        blockMap.put(new BlockInput(Blocks.RED_SANDSTONE, xzDirections.NORTH, yDirections.DOWN), new BlockOutput(262255, 3147, BlockTypes.RED_SANDSTONE));
        blockMap.put(new BlockInput(Blocks.SANDSTONE, xzDirections.NORTH, yDirections.DOWN), new BlockOutput(262523, 9276, BlockTypes.SANDSTONE));
        blockMap.put(new BlockInput(Blocks.STONE, xzDirections.WEST, yDirections.DOWN), new BlockOutput(264589, 10669, BlockTypes.STONE));
        blockMap.put(new BlockInput(Blocks.GRANITE, xzDirections.WEST, yDirections.DOWN), new BlockOutput(264219, 1371, BlockTypes.GRANITE));
        blockMap.put(new BlockInput(Blocks.DIORITE, xzDirections.WEST, yDirections.DOWN), new BlockOutput(264233, 1492, BlockTypes.DIORITE));
        blockMap.put(new BlockInput(Blocks.ANDESITE, xzDirections.WEST, yDirections.DOWN), new BlockOutput(265150, 23311, BlockTypes.ANDESITE));
        blockMap.put(new BlockInput(Blocks.COBBLESTONE, xzDirections.WEST, yDirections.DOWN), new BlockOutput(264713, 13251, BlockTypes.COBBLESTONE));
        blockMap.put(new BlockInput(Blocks.MOSSY_COBBLESTONE, xzDirections.WEST, yDirections.DOWN), new BlockOutput(264992, 19170, BlockTypes.MOSSY_COBBLESTONE));
        blockMap.put(new BlockInput(Blocks.BLACKSTONE, xzDirections.WEST, yDirections.DOWN), new BlockOutput(264207, 1086, BlockTypes.BLACKSTONE));
        blockMap.put(new BlockInput(Blocks.COBBLED_DEEPSLATE, xzDirections.WEST, yDirections.DOWN), new BlockOutput(264682, 12972, BlockTypes.COBBLED_DEEPSLATE));
        blockMap.put(new BlockInput(Blocks.PRISMARINE, xzDirections.WEST, yDirections.DOWN), new BlockOutput(264864, 16387, BlockTypes.PRISMARINE));
        blockMap.put(new BlockInput(Blocks.RED_SANDSTONE, xzDirections.WEST, yDirections.DOWN), new BlockOutput(264303, 3149, BlockTypes.RED_SANDSTONE));
        blockMap.put(new BlockInput(Blocks.SANDSTONE, xzDirections.WEST, yDirections.DOWN), new BlockOutput(264571, 9278, BlockTypes.SANDSTONE));
        blockMap.put(new BlockInput(Blocks.STONE, xzDirections.SOUTH, yDirections.DOWN), new BlockOutput(263565, 10668, BlockTypes.STONE));
        blockMap.put(new BlockInput(Blocks.GRANITE, xzDirections.SOUTH, yDirections.DOWN), new BlockOutput(263195, 1370, BlockTypes.GRANITE));
        blockMap.put(new BlockInput(Blocks.DIORITE, xzDirections.SOUTH, yDirections.DOWN), new BlockOutput(263209, 1491, BlockTypes.DIORITE));
        blockMap.put(new BlockInput(Blocks.ANDESITE, xzDirections.SOUTH, yDirections.DOWN), new BlockOutput(264126, 23310, BlockTypes.ANDESITE));
        blockMap.put(new BlockInput(Blocks.COBBLESTONE, xzDirections.SOUTH, yDirections.DOWN), new BlockOutput(263689, 13250, BlockTypes.COBBLESTONE));
        blockMap.put(new BlockInput(Blocks.MOSSY_COBBLESTONE, xzDirections.SOUTH, yDirections.DOWN), new BlockOutput(263968, 19169, BlockTypes.MOSSY_COBBLESTONE));
        blockMap.put(new BlockInput(Blocks.BLACKSTONE, xzDirections.SOUTH, yDirections.DOWN), new BlockOutput(263183, 1085, BlockTypes.BLACKSTONE));
        blockMap.put(new BlockInput(Blocks.COBBLED_DEEPSLATE, xzDirections.SOUTH, yDirections.DOWN), new BlockOutput(263658, 12971, BlockTypes.COBBLED_DEEPSLATE));
        blockMap.put(new BlockInput(Blocks.PRISMARINE, xzDirections.SOUTH, yDirections.DOWN), new BlockOutput(263840, 16386, BlockTypes.PRISMARINE));
        blockMap.put(new BlockInput(Blocks.RED_SANDSTONE, xzDirections.SOUTH, yDirections.DOWN), new BlockOutput(263279, 3148, BlockTypes.RED_SANDSTONE));
        blockMap.put(new BlockInput(Blocks.SANDSTONE, xzDirections.SOUTH, yDirections.DOWN), new BlockOutput(263547, 9277, BlockTypes.SANDSTONE));
        blockMap.put(new BlockInput(Blocks.STONE, xzDirections.EAST, yDirections.DOWN), new BlockOutput(265613, 10670, BlockTypes.STONE));
        blockMap.put(new BlockInput(Blocks.GRANITE, xzDirections.EAST, yDirections.DOWN), new BlockOutput(265243, 1372, BlockTypes.GRANITE));
        blockMap.put(new BlockInput(Blocks.DIORITE, xzDirections.EAST, yDirections.DOWN), new BlockOutput(265257, 1493, BlockTypes.DIORITE));
        blockMap.put(new BlockInput(Blocks.ANDESITE, xzDirections.EAST, yDirections.DOWN), new BlockOutput(266174, 23312, BlockTypes.ANDESITE));
        blockMap.put(new BlockInput(Blocks.COBBLESTONE, xzDirections.EAST, yDirections.DOWN), new BlockOutput(265737, 13252, BlockTypes.COBBLESTONE));
        blockMap.put(new BlockInput(Blocks.MOSSY_COBBLESTONE, xzDirections.EAST, yDirections.DOWN), new BlockOutput(266016, 19171, BlockTypes.MOSSY_COBBLESTONE));
        blockMap.put(new BlockInput(Blocks.BLACKSTONE, xzDirections.EAST, yDirections.DOWN), new BlockOutput(265231, 1087, BlockTypes.BLACKSTONE));
        blockMap.put(new BlockInput(Blocks.COBBLED_DEEPSLATE, xzDirections.EAST, yDirections.DOWN), new BlockOutput(265706, 12973, BlockTypes.COBBLED_DEEPSLATE));
        blockMap.put(new BlockInput(Blocks.PRISMARINE, xzDirections.EAST, yDirections.DOWN), new BlockOutput(265888, 16388, BlockTypes.PRISMARINE));
        blockMap.put(new BlockInput(Blocks.RED_SANDSTONE, xzDirections.EAST, yDirections.DOWN), new BlockOutput(265327, 3150, BlockTypes.RED_SANDSTONE));
        blockMap.put(new BlockInput(Blocks.SANDSTONE, xzDirections.EAST, yDirections.DOWN), new BlockOutput(265595, 9279, BlockTypes.SANDSTONE));
        blockMap.put(new BlockInput(Blocks.STONE, xzDirections.SLAB, yDirections.UP), new BlockOutput(5533, 11301, BlockTypes.STONE));
        blockMap.put(new BlockInput(Blocks.GRANITE, xzDirections.SLAB, yDirections.UP), new BlockOutput(5831, 17314, BlockTypes.GRANITE));
        blockMap.put(new BlockInput(Blocks.DIORITE, xzDirections.SLAB, yDirections.UP), new BlockOutput(5985, 21699, BlockTypes.DIORITE));
        blockMap.put(new BlockInput(Blocks.ANDESITE, xzDirections.SLAB, yDirections.UP), new BlockOutput(5425, 7199, BlockTypes.ANDESITE));
        blockMap.put(new BlockInput(Blocks.COBBLESTONE, xzDirections.SLAB, yDirections.UP), new BlockOutput(5207, 2960, BlockTypes.COBBLESTONE));
        blockMap.put(new BlockInput(Blocks.MOSSY_COBBLESTONE, xzDirections.SLAB, yDirections.UP), new BlockOutput(5873, 17866, BlockTypes.MOSSY_COBBLESTONE));
        blockMap.put(new BlockInput(Blocks.BLACKSTONE, xzDirections.SLAB, yDirections.UP), new BlockOutput(6043, 22550, BlockTypes.BLACKSTONE));
        blockMap.put(new BlockInput(Blocks.COBBLED_DEEPSLATE, xzDirections.SLAB, yDirections.UP), new BlockOutput(6123, 24134, BlockTypes.COBBLED_DEEPSLATE));
        blockMap.put(new BlockInput(Blocks.PRISMARINE, xzDirections.SLAB, yDirections.UP), new BlockOutput(5852, 17599, BlockTypes.PRISMARINE));
        blockMap.put(new BlockInput(Blocks.RED_SANDSTONE, xzDirections.SLAB, yDirections.UP), new BlockOutput(5774, 15785, BlockTypes.RED_SANDSTONE));
        blockMap.put(new BlockInput(Blocks.SANDSTONE, xzDirections.SLAB, yDirections.UP), new BlockOutput(5182, 2439, BlockTypes.SANDSTONE));
        blockMap.put(new BlockInput(Blocks.STONE, xzDirections.SLAB, yDirections.DOWN), new BlockOutput(4509, 11300, BlockTypes.STONE));
        blockMap.put(new BlockInput(Blocks.GRANITE, xzDirections.SLAB, yDirections.DOWN), new BlockOutput(4807, 17313, BlockTypes.GRANITE));
        blockMap.put(new BlockInput(Blocks.DIORITE, xzDirections.SLAB, yDirections.DOWN), new BlockOutput(4961, 21698, BlockTypes.DIORITE));
        blockMap.put(new BlockInput(Blocks.ANDESITE, xzDirections.SLAB, yDirections.DOWN), new BlockOutput(4401, 7198, BlockTypes.ANDESITE));
        blockMap.put(new BlockInput(Blocks.COBBLESTONE, xzDirections.SLAB, yDirections.DOWN), new BlockOutput(4183, 2959, BlockTypes.COBBLESTONE));
        blockMap.put(new BlockInput(Blocks.MOSSY_COBBLESTONE, xzDirections.SLAB, yDirections.DOWN), new BlockOutput(4849, 17865, BlockTypes.MOSSY_COBBLESTONE));
        blockMap.put(new BlockInput(Blocks.BLACKSTONE, xzDirections.SLAB, yDirections.DOWN), new BlockOutput(5019, 22549, BlockTypes.BLACKSTONE));
        blockMap.put(new BlockInput(Blocks.COBBLED_DEEPSLATE, xzDirections.SLAB, yDirections.DOWN), new BlockOutput(5099, 24133, BlockTypes.COBBLED_DEEPSLATE));
        blockMap.put(new BlockInput(Blocks.PRISMARINE, xzDirections.SLAB, yDirections.DOWN), new BlockOutput(4828, 17598, BlockTypes.PRISMARINE));
        blockMap.put(new BlockInput(Blocks.RED_SANDSTONE, xzDirections.SLAB, yDirections.DOWN), new BlockOutput(4750, 15784, BlockTypes.RED_SANDSTONE));
        blockMap.put(new BlockInput(Blocks.SANDSTONE, xzDirections.SLAB, yDirections.DOWN), new BlockOutput(4158, 2438, BlockTypes.SANDSTONE));
    }
}
