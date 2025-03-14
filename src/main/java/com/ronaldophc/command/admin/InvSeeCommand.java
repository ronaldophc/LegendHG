package com.ronaldophc.command.admin;

import com.ronaldophc.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InvSeeCommand implements CommandExecutor, TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("invsee")) {
            if (strings.length == 1) {
                String prefix = strings[0].toLowerCase();
                List<String> suggestions = new ArrayList<>();
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.getName().toLowerCase().startsWith(prefix)) {
                        suggestions.add(player.getName());
                    }
                }
                return suggestions;
            }
        }
        return Collections.emptyList();
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("invsee")) {
            if (commandSender == null)
                return true;
            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage(Util.noConsole);
                return true;
            }
            Player player = (Player) commandSender;
            if (!commandSender.isOp() && !commandSender.hasPermission("legendhg.admin.invsee")) {
                commandSender.sendMessage(Util.noPermission);
                return true;
            }
            if (strings.length == 0) {
                player.sendMessage(Util.usage("/invsee <player>"));
                return true;
            }
            if (strings.length == 1) {
                try {
                    Player target = player.getServer().getPlayer(strings[0]);
                    player.sendMessage(Util.admin + "Abrindo inventario de " + Util.color2 + target.getName());
                    player.openInventory(target.getInventory());
                } catch (Exception e) {
                    player.sendMessage(Util.noPlayer);
                    Util.errorCommand("invsee", e);
                }
            }
        }
        return false;
    }
}
