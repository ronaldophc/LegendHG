package com.ronaldophc.command;

import com.ronaldophc.helper.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import org.bukkit.entity.Player;

public class TestCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("test")) {
            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage(Util.noConsole);
                return true;
            }

            Player player = (Player) commandSender;
            if (!commandSender.isOp() && !commandSender.hasPermission("legendhg.")) {
                commandSender.sendMessage(Util.noPermission);
                return true;
            }

            return true;
        }
        return false;
    }
}
