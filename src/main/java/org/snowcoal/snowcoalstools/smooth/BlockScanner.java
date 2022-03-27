package org.snowcoal.snowcoalstools.smooth;

import com.sk89q.worldedit.world.block.BlockState;
import org.snowcoal.snowcoalstools.SnowcoalsTools;

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

    public BlockScanner(SnowcoalsTools instance){
        this.instance = instance;
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
        System.out.println("ID: " + block.getMaterial());
        String output = block.getMaterial() + "," + block.getInternalId() + "," + block.getOrdinal() + "," + block.getStates() + "\n";

        this.blocks.add(output);
    }


    /**
     * scanBlocks
     *
     * used for gathering each stairs ordinal/internal ID
     * unused and should not be used unless more blocks need to be added to the set
     * outputs the set of blocks to a folder
     *
     */
    public void scanBlocks() {
        File folder = this.instance.getDataFolder();
        String filepath = folder.getPath() + "\\BlockScanner\\blocks.csv";
        try {
            FileWriter fw = new FileWriter(filepath);
            for (String s : this.blocks)
                fw.write(s);
            fw.close();
        } catch (IOException iOException) {}
    }
}
