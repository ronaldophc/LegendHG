package com.ronaldophc.command;

import com.ronaldophc.helper.Util;
import com.ronaldophc.feature.MiniFeast;
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
            new MiniFeast();
            return true;
        }
        return false;
    }
}