package com.ronaldophc.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.ronaldophc.helper.Util;

public class Ping implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(command.getName().equalsIgnoreCase("ping")) {
            if (commandSender == null)
                return true;
            if(strings.length == 0) {
                if (!(commandSender instanceof Player)) {
                    commandSender.sendMessage(Util.noConsole);
                    return true;
                }
                Player player = (Player) commandSender;
                int ping = ((CraftPlayer) player).getHandle().ping;
                player.sendMessage(Util.color1 + "Seu ping é: " + Util.color3 + ping + Util.color1 + "ms");
                return true;
            }
            if(strings.length == 1) {
                if (!commandSender.isOp() && !commandSender.hasPermission("legendhg.admin.ping")) {
                    commandSender.sendMessage(Util.noPermission);
                    return true;
                }
                try {
                    Player target = commandSender.getServer().getPlayer(strings[0]);
                    int ping = ((CraftPlayer) target).getHandle().ping;
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
}
