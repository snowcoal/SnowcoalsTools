package org.snowcoal.citygenerator;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.Plugin;
import org.snowcoal.citygenerator.commands.CityGenCMD;
import org.snowcoal.citygenerator.commands.HouseSetCMD;
import org.snowcoal.citygenerator.houseset.HouseSet;

import java.io.File;

public final class CityGenerator extends JavaPlugin {

    private WorldEditPlugin fawe;
    public HouseSet houseSet = null;

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
        File schematicFolder = new File(filepath + "\\schematics");
        if(!schematicFolder.exists()){
            schematicFolder.mkdir();
        }

        this.houseSet = new HouseSet();

        // register commands
        getCommand("citygen").setExecutor(new CityGenCMD(this));
        getCommand("houseset").setExecutor(new HouseSetCMD(this));


        System.out.println("CityGenerator Successfully loaded!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    public WorldEditPlugin getFAWE(){
        return fawe;
    }
}
