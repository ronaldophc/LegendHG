package com.ronaldophc.command.admin;

import com.ronaldophc.feature.MiniFeastManager;
import com.ronaldophc.util.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MiniFeastCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("minifeast")) {
            if (!commandSender.isOp() && !commandSender.hasPermission("legendhg.admin.minifeast")) {
                commandSender.sendMessage(Util.noPermission);
                return true;
            }
            new MiniFeastManager();
            commandSender.sendMessage(Util.admin + "MiniFeast criado com sucesso.");
            return true;
        }
        return false;
    }
}