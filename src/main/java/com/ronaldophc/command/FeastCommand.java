package com.ronaldophc.command;

import com.ronaldophc.LegendHG;
import com.ronaldophc.feature.FeastManager;
import com.ronaldophc.helper.Util;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FeastCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(Util.noConsole);
            return true;
        }

        Player player = (Player) commandSender;
        FeastManager feast = LegendHG.getFeast();
        if (command.getName().equalsIgnoreCase("feast")) {
            if (feast.getState() == FeastManager.states.NOT_SPAWNED) {
                player.sendMessage(Util.color1 + "O feast ainda não foi spawnado");
                return true;
            }

            Location location = feast.getLocation();
            player.sendMessage(Util.color1 + "Apontando para o feast");
            player.setCompassTarget(location);
            return true;
        }

        if (command.getName().equalsIgnoreCase("lfeast")) {
            if (!commandSender.isOp() && !commandSender.hasPermission("legendhg.admin.feast")) {
                commandSender.sendMessage(Util.noPermission);
                return true;
            }

            if (strings.length == 0) {
                if (feast.getState() == FeastManager.states.NOT_SPAWNED) {
                    feast.start();
                    return true;
                }
                if (feast.getState() == FeastManager.states.SPAWNED) {
                    feast.spawnChests();
                    return true;
                }
                return true;
            }

            if (strings.length == 1) {
                if (strings[0].equalsIgnoreCase("redo")) {
                    feast.createBlocks();
                    return true;
                }
            }
        }
        return true;
    }
}
