package com.ronaldophc.command;

import com.ronaldophc.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SayCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("say")) {

            if (!commandSender.isOp() && !commandSender.hasPermission("legendhg.admin.say")) {
                commandSender.sendMessage(Util.noPermission);
                return true;
            }

            String message = String.join(" ", strings).replaceAll("&", "§");

            Bukkit.broadcastMessage(Util.title + " §8§l-> " + ChatColor.RESET + message);
            return true;
        }
        return false;
    }
}
