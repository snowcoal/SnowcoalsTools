package org.snowcoal.snowcoalstools.commands;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.regions.Region;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.snowcoal.snowcoalstools.MessageSender;
import org.snowcoal.snowcoalstools.SnowcoalsTools;
import org.snowcoal.snowcoalstools.erode.WaterErosion;

import java.util.logging.Level;

abstract class ErosionCMD implements CommandExecutor {
    private SnowcoalsTools instance;
    private MessageSender msgSender;

    public ErosionCMD(SnowcoalsTools cg, MessageSender ms){
        this.instance = cg;
        this.msgSender = ms;
    }

    @Override
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

        // attempt to run command
        try {
            runCommand(sel, player, this.instance);
        } catch(Exception e){
            this.msgSender.sendMessage(sender, error);
            instance.getLogger().log(Level.SEVERE, String.valueOf(e.getStackTrace()));
            return true;
        }

        // success message
        this.msgSender.sendMessage(sender, logo + getChangedBlocks() + " Blocks were changed.");
        return true;
    }

    abstract void runCommand(Region sel, Player player, SnowcoalsTools instance);

    abstract int getChangedBlocks();

    private final String logo = "(&3&lSNOWCOAL&r)&d ";
    private final String playersOnly = "&cERROR: Only players can use this command.";
    private final String noSelection = "&cERROR: Please make a selection first";
    private final String error = "&cERROR: An error occurred while attempting to run this command";
}
