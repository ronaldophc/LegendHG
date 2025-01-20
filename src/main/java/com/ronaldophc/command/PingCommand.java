package com.ronaldophc.command;

import com.ronaldophc.Reflection;
import com.ronaldophc.util.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

public class PingCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("ping")) {
            if (commandSender == null) {
                return true;
            }
            if (strings.length == 0) {
                if (!(commandSender instanceof Player)) {
                    commandSender.sendMessage(Util.noConsole);
                    return true;
                }

                Player target = (Player) commandSender;
                sendMessage(commandSender, target);
                return true;
            }
            if (strings.length == 1) {
                if (!commandSender.isOp() && !commandSender.hasPermission("legendhg.admin.ping")) {
                    commandSender.sendMessage(Util.noPermission);
                    return true;
                }

                Player target = commandSender.getServer().getPlayer(strings[0]);
                if (target == null) {
                    commandSender.sendMessage(Util.error + "Jogador não encontrado.");
                    return true;
                }

                sendMessage(commandSender, target);
                return true;
            }
        }
        return true;
    }

    private void sendMessage(CommandSender commandSender, Player target) {
        int ping = getPing(target);
        String message = Util.color3 + ping + Util.color1 + "ms";
        if (commandSender != target) {
            message = Util.color3 + target.getName() + Util.color1 + ": " + message;
        }
        commandSender.sendMessage(Util.color1 + message);
    }

    private int getPing(Player player) {
        try {
            return (int) Reflection.getField(player, "ping");
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException e) {
            Util.errorCommand("ping", e);
        }
        return 0;
    }
}
