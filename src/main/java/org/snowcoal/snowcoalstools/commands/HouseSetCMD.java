package org.snowcoal.snowcoalstools.commands;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import org.snowcoal.snowcoalstools.SnowcoalsTools;
import com.sk89q.worldedit.entity.Player;
import org.snowcoal.snowcoalstools.MessageSender;

import java.io.*;


public class HouseSetCMD implements CommandExecutor {
    private SnowcoalsTools instance;
    private MessageSender msgSender = null;

    public HouseSetCMD(SnowcoalsTools arg, MessageSender ms) {
        instance = arg;
        msgSender =  ms;

        // init housesets list
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof org.bukkit.entity.Player)){
            msgSender.sendMessage(sender, playersOnly);
            return true;
        }
        if(args.length == 0){
            msgSender.sendMessage(sender, incorrectUsage);
            return true;
        }

        // cast from command sender to bukkit player to worldedit player (bruh lmao)
        org.bukkit.entity.Player bplayer = (org.bukkit.entity.Player) sender;
        Player player = BukkitAdapter.adapt(bplayer);

        String base = args[0];
        switch (base) {
            // add new house to HouseSet
            case "addhouse":
            case "ah":
                int type = -1;
                // check args validity
                if(args.length != 2){
                    msgSender.sendMessage(sender, incorrectUsageAH);
                    return true;
                }
                try{
                    type = Integer.parseInt(args[1]);
                }
                catch(NumberFormatException e){
                    msgSender.sendMessage(sender, invalidAHArguments);
                    return true;
                }
                if(type < -2 || type == -1 || type > 4){
                    msgSender.sendMessage(sender, invalidAHArguments);
                    return true;
                }

                Region sel = null;
                // check for poly selection
                try {
                    sel = player.getSelection();
                    if (!(sel instanceof CuboidRegion)) {
                        msgSender.sendMessage(sender, noRegionSelected);
                        return true;
                    }
                } catch (IncompleteRegionException e) {
                    msgSender.sendMessage(sender, noRegionSelected);
                    return true;
                }
                // create new house and add it to house set
                if(!instance.houseSet.addHouse((CuboidRegion) sel, player.getLocation(), type)){
                    msgSender.sendMessage(sender, invalidSelection);
                    return true;
                }
                // send success messaage
                msgSender.sendMessage(sender, addHouseSuccess);
                break;

            // save HouseSet data as csv
            case "save":
            case "s":
                // check args validity
                if(args.length != 2){
                    msgSender.sendMessage(sender, nameNeededS);
                    return true;
                }
                writeHouseSet(sender, args[1]);
                break;

            // load csv with HouseSet data
            case "load":
            case "l":
                // check args validity
                if(args.length != 2){
                    msgSender.sendMessage(sender, nameNeeded);
                    return true;
                }
                readHouseSet(sender, args[1]);
                break;

            // list all saved HouseSets
            case "list":
            case "ls":
                listHouseSets(sender);
                break;

            // clear current HouseSet
            case "clear":
                instance.houseSet.clear();
                break;

            // print basic info about current HouseSet to console
            case "debug":
                instance.houseSet.printTree(false);
                break;

            // print advanced info about current HouseSet to console
            case "fulldebug":
                instance.houseSet.printTree(true);
                break;
        }


        return true;
    }

    /**
     * readHouseSet()
     *
     * attempts to reads the HouseSet object from a csv
     *
     * sender- player that sent command
     *
     */
    private void readHouseSet(CommandSender sender, String name){
        File folder = instance.getDataFolder();
        String filepath = folder.getPath() + "\\HouseSets" + "\\" + name + ".csv";

        if(!instance.houseSet.readFromCSV(filepath)){
            msgSender.sendMessage(sender, houseSetDNE);
            return;
        }
        msgSender.sendMessage(sender, houseSetLoaded);

    }

    /**
     * writeHouseSet()
     *
     * writes the HouseSet object to a csv
     *
     * sender- player that sent command
     *
     */
    private void writeHouseSet(CommandSender sender, String name){
        File folder = instance.getDataFolder();
        String filepath = folder.getPath() + "\\HouseSets" + "\\" + name + ".csv";

        if(!instance.houseSet.writeToCSV(filepath)){
            msgSender.sendMessage(sender, houseSetDNE);
            return;
        }
        msgSender.sendMessage(sender, houseSetSaved + name + ".csv");

    }

    /**
     * listHouseSets()
     *
     * lists all housesets saved in the housesets directory
     *
     * sender- player that sent command
     *
     */
    private void listHouseSets(CommandSender sender){
        File folder = instance.getDataFolder();
        String filepath = folder.getPath() + "\\HouseSets";
        folder = new File(filepath);

        File[] files = folder.listFiles();
        // return if folder empty
        if(files.length == 0){
            msgSender.sendMessage(sender, noHouseSets);
            return;
        }

        msgSender.sendMessage(sender, curHouseSets);
        // send msg for each file
        for(int i = 0;i < files.length; i++){
            File file = files[i];
            msgSender.sendMessage(sender, "&f" + file.getName());
        }
    }

    // chat messages
    private final String logo = "(&3&lSNOWCOAL&r)&d ";
    private final String playersOnly = "&cERROR: Only players can use this command.";
    private final String incorrectUsage = "&eIncorrect command usage. Correct: ./houseset <addhouse OR save OR load OR list OR clear>";
    private final String noRegionSelected = "&cERROR: Please make a selection first.";
    private final String houseSetDNE = "&cERROR: House Set does not exist";
    private final String houseSetLoaded = logo + "House Set successfully loaded!";
    private final String houseSetSaved = logo + "House Set successfully saved as ";
    private final String nameNeeded = "&eIncorrect command usage. Correct: ./houseset load <name>";
    private final String nameNeededS = "&eIncorrect command usage. Correct: ./houseset save <name>";
    private final String noHouseSets = "&eThere are no HouseSets to be loaded";
    private final String curHouseSets = logo + "House Sets currently saved:";
    private final String incorrectUsageAH = "&eIncorrect command usage. Correct: ./houseset addhouse <type>";
    private final String invalidAHArguments = "&eInvalid Arguments for <type>. Correct: <type> is the number of neighbors and should be 0-4, or -2 if the house has exactly 2 neighbors directly across";
    private final String invalidSelection = "&cERROR: Selection must have a odd-numbered width";
    private final String addHouseSuccess = logo + "selection successfully added to house set!";
}
