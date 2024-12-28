package com.ronaldophc.command;

import com.ronaldophc.helper.Util;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class PrefsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("prefs")) {

            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage(Util.noConsole);
                return true;
            }

            Player player = (Player) commandSender;

            openMenu(player);

            return true;
        }
        return false;
    }

    private static void openMenu(Player player) {
        // Score, tell, chat
        Inventory prefs = Bukkit.createInventory(null, 6 * 9, Util.title + " " + Util.color2 +"Ajustes");

        player.openInventory(prefs);
    }
}
