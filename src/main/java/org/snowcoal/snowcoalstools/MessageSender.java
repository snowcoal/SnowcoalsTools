package org.snowcoal.snowcoalstools;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class MessageSender {
    public void  sendMessage(CommandSender sender, String msg){
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
    }
}
