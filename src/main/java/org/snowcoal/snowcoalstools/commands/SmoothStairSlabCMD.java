package org.snowcoal.snowcoalstools.commands;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.regions.Region;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.snowcoal.snowcoalstools.SnowcoalsTools;
import org.snowcoal.snowcoalstools.MessageSender;
import org.snowcoal.snowcoalstools.city.City;
import org.snowcoal.snowcoalstools.city.CityRoads;
import org.snowcoal.snowcoalstools.smooth.SmoothStairSlab;

import java.util.logging.Level;

public class SmoothStairSlabCMD implements CommandExecutor {
    private SnowcoalsTools instance;

    private City city = null;

    private MessageSender msgSender = null;

    private CityRoads cityRoads = null;

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof org.bukkit.entity.Player)) {
            this.msgSender.sendMessage(sender, playersOnly);
            return true;
        }
        org.bukkit.entity.Player bplayer = (org.bukkit.entity.Player) sender;
        Player player = BukkitAdapter.adapt(bplayer);
        Region sel = null;

        // attempt to get player's selection
        try {
            sel = player.getSelection();
        } catch (IncompleteRegionException e) {
            this.msgSender.sendMessage(sender, noSelection);
            return true;
        }

        SmoothStairSlab smoothStairSlab = null;
        // attempt to run command
        try {
            smoothStairSlab = new SmoothStairSlab(sel, player, this.instance);
        } catch(Exception e){
            this.msgSender.sendMessage(sender, error);
            instance.getLogger().log(Level.SEVERE, String.valueOf(e.getStackTrace()));
            return true;
        }

        // success message
        this.msgSender.sendMessage(sender, logo + smoothStairSlab.getChangedBlocks() + " Blocks were changed.");
        return true;
    }

    public SmoothStairSlabCMD(SnowcoalsTools cg, MessageSender ms) {
        this.instance = cg;
        this.msgSender = ms;
    }

    private final String logo = "(&3&lSNOWCOAL&r)&d ";
    private final String playersOnly = "&cERROR: Only players can use this command.";
    private final String noSelection = "&cERROR: Please make a selection first";
    private final String error = "&cERROR: An error occurred while attempting to run this command";
}
