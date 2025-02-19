package com.ronaldophc.command.admin;

import com.ronaldophc.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class PullCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("pull")) {

            if (commandSender == null)
                return true;

            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage(Util.noConsole);
                return true;
            }

            Player player = (Player) commandSender;

            if (!commandSender.isOp() && !commandSender.hasPermission("legendhg.admin.pull")) {
                commandSender.sendMessage(Util.noPermission);
                return true;
            }

            if (strings.length != 1) {
                commandSender.sendMessage(Util.usage("/pull <player>"));
                return true;
            }

            if (strings[0].equalsIgnoreCase("all")) {
                for (Entity entity : Bukkit.getWorld("world").getEntities()) {
                    entity.teleport(player.getLocation());
                }
                return true;
            }

            try {
                Player target = player.getServer().getPlayer(strings[0]);

                if (target == null) {
                    player.sendMessage(Util.noPlayer);
                    return true;
                }

                target.teleport(player);
                target.sendMessage(Util.admin + "Você foi puxado por " + player.getName());
                player.sendMessage(Util.admin + "Jogador puxado");

            } catch (NumberFormatException e) {
                player.sendMessage(Util.error + "Jogador não encontrado");
            }

            return true;
        }
        return true;
    }
}
