package org.snowcoal.snowcoalstools;


import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.snowcoal.snowcoalstools.commands.*;
import org.snowcoal.snowcoalstools.houseset.HouseSet;
import org.snowcoal.snowcoalstools.smooth.BlockMap;

public final class SnowcoalsTools extends JavaPlugin {

    private WorldEditPlugin fawe;
    public HouseSet houseSet = null;
    private BlockMap blockMap = null;

    @Override
    public void onEnable() {
        // Plugin startup logic

        // check for worldedit
        Plugin fawe = getServer().getPluginManager().getPlugin("FastAsyncWorldEdit");
        if(fawe.equals(null) || !(fawe instanceof WorldEditPlugin)){
            this.getLogger().log(Level.SEVERE, "FAWE required for snowcoalsTools, please install it");
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
        this.blockMap = new BlockMap(this);
        MessageSender ms = new MessageSender();

        // register commands
        getCommand("citygen").setExecutor(new CityGenCMD(this, ms));
        getCommand("houseset").setExecutor(new HouseSetCMD(this, ms));
        getCommand("/smoothstairslab").setExecutor(new SmoothStairSlabCMD(this, ms));
        getCommand("/watererode").setExecutor(new WaterErosionCMD(this, ms));
        getCommand("scanblocks").setExecutor(new BlockScannerCMD(this, ms));
        getCommand("/dim3smooth").setExecutor(new Dim3SmoothCMD(this, ms));

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public BlockMap getBlockMap(){
        return blockMap;
    }

    public void printException(Exception e){
        this.getLogger().log(Level.SEVERE, String.valueOf(e));
        for(StackTraceElement st:e.getStackTrace()) {
            this.getLogger().log(Level.SEVERE, String.valueOf(st));
        }
    }


    public WorldEditPlugin getFAWE(){
        return fawe;
    }
}
