package com.ronaldophc.command.admin;

import com.ronaldophc.constant.MySQL.PlayerField;
import com.ronaldophc.constant.MySQL.Tables;
import com.ronaldophc.database.MySQLManager;
import com.ronaldophc.util.Util;
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
                String ip = MySQLManager.getStringByName(name, Tables.PLAYER.getTableName(), PlayerField.IP.getFieldName());
                if (ip == null) {
                    commandSender.sendMessage(Util.noPlayer);
                    return true;
                }

                commandSender.sendMessage(Util.admin + "Ultimo IP usado por " + Util.color2 + name + Util.admin + ": " + Util.color2 + ip);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return true;
        }
        return true;
    }
}