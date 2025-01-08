package com.ronaldophc.command;

import com.ronaldophc.feature.Schematic;
import com.ronaldophc.helper.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SchematicCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("schematic")) {
            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage(Util.noConsole);
                return true;
            }

            if (strings.length != 1) {
                commandSender.sendMessage("§cUse: /schematic <name>");
                return true;
            }

            Player player = (Player) commandSender;
            Schematic.getInstance().createSchematic(player.getWorld(), player.getLocation(), strings[0]);
            player.sendMessage("§aSchematic loaded!");


            return true;
        }
        return false;
    }
}
