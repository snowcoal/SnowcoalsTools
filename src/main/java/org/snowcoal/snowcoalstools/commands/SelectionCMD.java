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

abstract class SelectionCMD implements CommandExecutor, Runnable {
    protected SnowcoalsTools instance;
    protected MessageSender msgSender;
    protected CommandSender sender;
    protected Region sel;
    protected Player player;
    protected String[] args;

    private Thread thread;

    public SelectionCMD(SnowcoalsTools cg, MessageSender ms){
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

        // save for later
        this.sender = sender;
        this.sel = sel;
        this.player = player;
        this.args = args;

        // run new thread for command
        this.start();
        return true;
    }

    @Override
    public void run() {
        runCommand();
        thread = null;
    }

    public void start(){
        if(thread == null){
            thread = new Thread(this);
            thread.start();
        }
    }

    abstract boolean runCommand();


    protected final String playersOnly = "&cERROR: Only players can use this command.";
    protected final String noSelection = "&cERROR: Please make a selection first";
    protected final String logo = "(&3&lSNOWCOAL&r)&d ";
    protected final String error = "&cERROR: An error occurred while attempting to run this command";
}
