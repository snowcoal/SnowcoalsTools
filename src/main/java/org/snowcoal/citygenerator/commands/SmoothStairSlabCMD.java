package org.snowcoal.citygenerator.commands;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.BukkitPlayer;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.regions.Region;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.snowcoal.citygenerator.CityGenerator;
import org.snowcoal.citygenerator.MessageSender;
import org.snowcoal.citygenerator.city.City;
import org.snowcoal.citygenerator.city.CityRoads;
import org.snowcoal.citygenerator.smooth.SmoothStairSlab;

public class SmoothStairSlabCMD implements CommandExecutor {
    private CityGenerator instance;

    private City city = null;

    private MessageSender msgSender = null;

    private CityRoads cityRoads = null;

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof org.bukkit.entity.Player)) {
            this.msgSender.sendMessage(sender, "&cERROR: Only players can use this command.");
            return true;
        }
        org.bukkit.entity.Player bplayer = (org.bukkit.entity.Player) sender;
        Player player = BukkitAdapter.adapt(bplayer);
        Region sel = null;

        try {
            sel = player.getSelection();
        } catch (IncompleteRegionException e) {
            this.msgSender.sendMessage(sender, "&cERROR: Please make a selection first");
            return true;
        }
        SmoothStairSlab smoothStairSlab = new SmoothStairSlab(sel, player, this.instance);
        return true;
    }

    public SmoothStairSlabCMD(CityGenerator cg, MessageSender ms) {
        this.instance = cg;
        this.msgSender = ms;
    }

    private final String playersOnly = "&cERROR: Only players can use this command.";
    private final String incorrectUsage = "&eIncorrect command usage. Correct: ./citygen <genbase OR gencity>";
    private final String noSelection = "&cERROR: Please make a selection first";
}
