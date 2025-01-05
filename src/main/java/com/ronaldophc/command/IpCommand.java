package com.ronaldophc.command;

import com.ronaldophc.database.PlayerSQL;
import com.ronaldophc.helper.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.sql.SQLException;

public class IpCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("ip")) {

            if (!commandSender.hasPermission("legendhg.admin.ip")) {
                commandSender.sendMessage(Util.noPermission);
                return true;
            }

            if (strings.length != 1) {
                commandSender.sendMessage(Util.usage("/ip <player>"));
                return true;
            }

            String name = strings[0];

            try {
                String ip = PlayerSQL.getPlayerIpAddress(name);
                if (ip == null) {
                    commandSender.sendMessage(Util.noPlayer);
                    return true;
                }

                commandSender.sendMessage(Util.color1 + "Ultimo IP usado por " + Util.color2 + name + Util.color1 + ": " + Util.color2 + ip);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return true;
        }
        return true;
    }
}