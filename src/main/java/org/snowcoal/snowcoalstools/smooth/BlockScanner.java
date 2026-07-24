package org.snowcoal.snowcoalstools.smooth;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.block.BlockState;
import org.snowcoal.snowcoalstools.SnowcoalsTools;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.entity.Player;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * BlockScanner
 *
 * helper class used along with python script to get each required blocks ordinal and IID
 * unused except for development purposes: Once I have the block info, I just use my script
 * to generate the required hashmap automatically.
 *
 */
public class BlockScanner {

    private List<String> blocks;
    private SnowcoalsTools instance;
    private Region sel;
    private Player player;

    public BlockScanner(Region sel, Player player, SnowcoalsTools instance){
        this.instance = instance;
        this.sel = sel;
        this.player = player;
        blocks = new ArrayList<>();
    }


    /**
     * scanBlock
     *
     * scans a specific block to add it to the list
     *
     */
    public void scanBlock(BlockState block, int x, int y, int z){
        System.out.println("printing values for block: " + x + " " + y + " " + z);
        System.out.println("states: " + block.getStates());
        System.out.println("ordinal: " + block.getOrdinal());
        System.out.println("IID: " + block.getInternalId());
        System.out.println("ID: " + block.getBlockType());
        String output = block.getBlockType() + "," + block.getInternalId() + "," + block.getOrdinal() + "," + block.getStates() + "\n";

        this.blocks.add(output);
    }


    /**
     * outputScan
     *
     * outputs the scan to a file
     *
     */
    public void outputScan() {
        File folder = this.instance.getDataFolder();
        String filepath = folder.getPath() + "\\BlockScanner\\blocks.csv";
        try {
            FileWriter fw = new FileWriter(filepath);
            for (String s : this.blocks)
                fw.write(s);
            fw.close();
        } catch (IOException iOException) {}
    }

    /**
     * scanBlocks
     *
     * used for gathering each stairs ordinal/internal ID
     * unused and should not be used unless more blocks need to be added to the set
     * outputs the set of blocks to a folder
     *
     */
    public void scanBlocks(){
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

        // loop through all blocks in selection
        for (int x = min.getX(); x <= max.getX(); x++) {
            for (int z = min.getZ(); z <= max.getZ(); z++) {
                for (int y = min.getY(); y <= max.getY(); y++) {
                    BlockState block = editSession.getBlock(x, y, z);
                    if (!block.isAir()) {
                        scanBlock(block, x, y, z);
                    }
                }
            }
        }

        editSession.close();

        outputScan();
    }
}
