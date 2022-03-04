package org.snowcoal.citygenerator;


import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import java.io.File;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.snowcoal.citygenerator.commands.CityGenCMD;
import org.snowcoal.citygenerator.commands.HouseSetCMD;
import org.snowcoal.citygenerator.commands.SmoothStairSlabCMD;
import org.snowcoal.citygenerator.houseset.HouseSet;
import org.snowcoal.citygenerator.smooth.BlockMap;

import java.io.File;

public final class CityGenerator extends JavaPlugin {

    private WorldEditPlugin fawe;
    public HouseSet houseSet = null;
    private BlockMap blockMap = null;

    @Override
    public void onEnable() {
        // Plugin startup logic

        // check for worldedit
        Plugin fawe = getServer().getPluginManager().getPlugin("FastAsyncWorldEdit");
        if(fawe.equals(null) || !(fawe instanceof WorldEditPlugin)){
            System.out.println("FAWE required for CityGenerator, please install it");
            return;
        }
        this.fawe = (WorldEditPlugin) fawe;

        // make main plugin directory and sub directories
        File folder = getDataFolder();
        if (!folder.exists()){
            folder.mkdir();
        }
        String filepath = folder.getPath();
        File houseSetFolder = new File(filepath + "\\HouseSets");
        if(!houseSetFolder.exists()){
            houseSetFolder.mkdir();
        }
        File schematicFolder = new File(filepath + "\\BlockScanner");
        if (!schematicFolder.exists()) {
            schematicFolder.mkdir();
        }

        this.houseSet = new HouseSet();
        this.blockMap = new BlockMap();
        MessageSender ms = new MessageSender();

        // register commands
        getCommand("citygen").setExecutor(new CityGenCMD(this, ms));
        getCommand("houseset").setExecutor(new HouseSetCMD(this, ms));
        getCommand("smoothstairslab").setExecutor(new SmoothStairSlabCMD(this, ms));

        System.out.println("CityGenerator Successfully loaded!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public BlockMap getBlockMap(){
        return blockMap;
    }


    public WorldEditPlugin getFAWE(){
        return fawe;
    }
}
