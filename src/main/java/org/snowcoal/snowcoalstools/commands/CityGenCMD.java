package org.snowcoal.snowcoalstools.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import org.snowcoal.snowcoalstools.SnowcoalsTools;
import org.snowcoal.snowcoalstools.city.City;
import org.snowcoal.snowcoalstools.city.CityStats;
import org.snowcoal.snowcoalstools.MessageSender;
import org.snowcoal.snowcoalstools.city.CityRoads;

import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.regions.Polygonal2DRegion;

import java.util.logging.Level;


public class CityGenCMD implements CommandExecutor {

    private SnowcoalsTools instance;
    private City city = null;
    private MessageSender msgSender = null;
    private CityRoads cityRoads = null;

    public CityGenCMD(SnowcoalsTools arg, MessageSender ms) {
        instance = arg;
        msgSender = ms;
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
        //System.out.println(bplayer.getWorld().getWorldFolder());
        Player player = BukkitAdapter.adapt(bplayer);
        //System.out.println(player.getWorld().getStoragePath());

        String base = args[0];

        if(base.equals("genbase") || base.equals("gb")){
            int grid_box_width = 0;
            int lr_bias = 0;
            int dist = 0;
            Region sel = null;

            // check args for validity
            if(args.length < 3) {
                msgSender.sendMessage(sender, incorrectUsageSetBase);
                return true;
            }
            try{
                grid_box_width = Integer.parseInt(args[1]);
                lr_bias = Integer.parseInt(args[2]);
            }
            catch(NumberFormatException e){
                msgSender.sendMessage(sender, invalidBaseArguments);
                return true;
            }
            if(grid_box_width %2 == 0 || lr_bias < 0 || lr_bias > 100){
                msgSender.sendMessage(sender, invalidBaseArguments);
                return true;
            }

            // check for poly selection
            try{
                sel = player.getSelection();
                if(!(sel instanceof Polygonal2DRegion)){
                    msgSender.sendMessage(sender, noPolygonSelected);
                    return true;
                }
            }
            catch(IncompleteRegionException e){
                msgSender.sendMessage(sender, noPolygonSelected);
                return true;
            }

            // make city
            city = new City(grid_box_width, lr_bias, (Polygonal2DRegion) sel);
            // check if it failed
            if(city.checkFailed()){
                msgSender.sendMessage(sender, generationFailed);
                return true;
            }

            // add random roads if needed
            if(args.length == 4){
                try{
                    dist = Integer.parseInt(args[3]);
                }
                catch(NumberFormatException e){
                    msgSender.sendMessage(sender, invalidRoadArguments);
                    return true;
                }
                if(dist < 0 || dist > 100){
                    msgSender.sendMessage(sender, invalidRoadArguments);
                    return true;
                }

                // add roads
                cityRoads = new CityRoads(city);
                cityRoads.addRandomRoads(dist);
            }

            // get city stats
            CityStats stats = city.getCityStats();
            sendStats(sender, stats);

            return true;
        }
        else if(base.equals("gencity") || base.equals("gc")){
            // check to see if it can even be done
            if(city == null){
                msgSender.sendMessage(sender, setCityFirst);
                return true;
            }
            // ensure house set is not empty
            if(instance.houseSet.isEmpty()){
                msgSender.sendMessage(sender, houseSetEmpty);
                return true;
            }

            // attempt to place all houses in city
            if(!city.placer.placeHouses(instance.houseSet)){
                msgSender.sendMessage(sender, cityGenFailed);
                return true;
            }

            msgSender.sendMessage(sender, genSuccess);
            // attempt to generate city in world
            try {
                city.paster.pasteCity(player);
            } catch(Exception e){
                instance.getLogger().log(Level.SEVERE, String.valueOf(e.getStackTrace()));
                msgSender.sendMessage(sender, pasteFail);
                return true;
            }

            msgSender.sendMessage(sender, pasteSuccess);
            return true;
        }

        // by default command is wrong
        msgSender.sendMessage(sender, incorrectUsage);
        return true;
    }

    // chat messages:
    private final String logo = "(&3&lSNOWCOAL&r)&d ";
    private final String playersOnly = "&cERROR: Only players can use this command.";
    private final String incorrectUsage = "&eIncorrect command usage. Correct: ./citygen <genbase OR gencity>";
    private final String incorrectUsageSetBase = "&eIncorrect command usage. Correct: ./citygen genbase <gridBoxWidth> <leftRightBias> <roadDist> (optional)";
    private final String noPolygonSelected = "&cERROR: Please select a 2D polygon region";
    private final String setCityFirst = "&cERROR: set the city base first using ./citygen genbase";
    private final String invalidBaseArguments = "&cERROR: Invalid arguments to setbase. Grid box width must be an odd integer and LRbias must be from 0-100";
    private final String invalidRoadArguments = "&cERROR: Invalid arguments to addroads. roadDist must be from 0-100 or empty";
    private final String generationFailed = "&cERROR: City Failed to generate, due to area not being large enough. Please select a larger area.";
    private final String houseSetEmpty = "&cERROR: City requires a house set to place houses. Please create one first.";
    private final String cityGenFailed = "&cERROR: City generation failed. It is likely you do not have enough houses. See console for more info.";
    private final String genSuccess = logo + "House Placement Succeeded";
    private final String pasteFail = "&cERROR: Could not paste city in world";
    private final String pasteSuccess = logo + "City pasted in world";


    private void sendStats(CommandSender sender, CityStats stats){
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', logo + "Base City Generation Succeeded. Stats: "));
        String area = "&dTotal Area: " + stats.area;
        String numCells = "&dTotal Cells: " + stats.numCells;
        String numHouses = "&dTotal Houses: " + stats.numHouses;
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', area));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', numCells));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', numHouses));
    }
}
