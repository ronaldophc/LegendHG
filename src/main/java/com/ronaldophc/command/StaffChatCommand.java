package com.ronaldophc.command;

import com.ronaldophc.helper.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StaffChatCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("staffchat")) {
            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage(Util.noConsole);
                return true;
            }

            Player player = (Player) commandSender;
            if (!commandSender.isOp() && !commandSender.hasPermission("legendhg.admin.staffchat")) {
                commandSender.sendMessage(Util.noPermission);
                return true;
            }

            if (strings.length == 0) {
                player.sendMessage(Util.usage("/staffchat <mesage>"));
                return true;
            }

            StringBuilder message = new StringBuilder();
            for (String string : strings) {
                message.append(string).append(" ");
            }

            for (Player online : player.getServer().getOnlinePlayers()) {
                if (online.hasPermission("legendhg.admin.staffchat")) {
                    online.sendMessage(("§8[§cStaff§8] §7" + player.getName() + " §8» §f" + message));
                }
            }

            return true;
        }
        return false;
    }
}
