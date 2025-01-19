package com.ronaldophc.command.admin;

import com.ronaldophc.database.BanManager;
import com.ronaldophc.util.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BanIpCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("banip")) {
            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage(Util.noConsole);
                return true;
            }

            Player player = (Player) commandSender;
            if (!player.isOp() && !player.hasPermission("legendhg.admin.banip")) {
                player.sendMessage(Util.noPermission);
                return true;
            }

            if (strings.length < 2) {
                player.sendMessage(Util.usage("/banip <ipAddress> <duration> <reason>"));
                return false;
            }

//            String target = strings[0];
//            long duration = Long.parseLong(strings[1]) * 1000;
//            BanManager.banIP(ipAddress, duration);
//            sender.sendMessage(ChatColor.GREEN + "IP banned successfully!");

            return true;
        }
        return false;
    }
}
