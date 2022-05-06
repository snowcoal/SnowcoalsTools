package org.snowcoal.snowcoalstools.commands;

import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.regions.Region;
import org.bukkit.command.CommandSender;
import org.snowcoal.snowcoalstools.MessageSender;
import org.snowcoal.snowcoalstools.SnowcoalsTools;

public class SenderData {
    public SnowcoalsTools instance;
    public MessageSender msgSender;
    public CommandSender sender;
    public Region sel;
    public Player player;
    public String[] args;

    public SenderData(SnowcoalsTools instance, MessageSender msgSender, CommandSender sender, Region sel, Player player, String[] args){
        this.instance = instance;
        this.msgSender = msgSender;
        this.sender = sender;
        this.sel = sel;
        this.player = player;
        this.args = args;
    }
}
