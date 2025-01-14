package com.ronaldophc.command;

import com.ronaldophc.helper.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class PingCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("ping")) {
            if (commandSender == null)
                return true;
            if (strings.length == 0) {
                if (!(commandSender instanceof Player)) {
                    commandSender.sendMessage(Util.noConsole);
                    return true;
                }
                Player player = (Player) commandSender;
                int ping = getPing(player);
                player.sendMessage(Util.color1 + "Seu ping é: " + Util.color3 + ping + Util.color1 + "ms");
                return true;
            }
            if (strings.length == 1) {
                if (!commandSender.isOp() && !commandSender.hasPermission("legendhg.admin.ping")) {
                    commandSender.sendMessage(Util.noPermission);
                    return true;
                }
                try {
                    Player target = commandSender.getServer().getPlayer(strings[0]);
                    int ping = getPing(target);
                    commandSender.sendMessage(Util.color1 + "Ping de " + Util.color3 + target.getName() + Util.color1 + " é: " + Util.color3 + ping + Util.color1 + "ms");
                } catch (Exception e) {
                    commandSender.sendMessage(Util.error + "Algo aconteceu errado.");
                    Util.errorCommand("ping", e);
                }
                return true;
            }
        }
        return false;
    }

    public static int getPing(Player player) {
        try {
            Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
            Field pingField = entityPlayer.getClass().getDeclaredField("ping");
            return pingField.getInt(entityPlayer);
        } catch (Exception e) {
            return -1;
        }
    }
}
