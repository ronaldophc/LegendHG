package com.ronaldophc.feature.report;

import com.ronaldophc.helper.Util;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;

public class ReportMenus implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(command.getName().equalsIgnoreCase("reports")) {
            if(!(commandSender instanceof Player)) {
                commandSender.sendMessage(Util.noConsole);
                return true;
            }
            Player player = (Player) commandSender;
            if(!player.hasPermission("legendhg.admin.reports")) {
                player.sendMessage(Util.noPermission);
                return true;
            }
            guireports(player);
            return true;
        }
        return false;
    }

    private void guireports(Player player) {
        Inventory gui = Bukkit.createInventory(player, 54, "§bReports recebidos");
        for (Player target : Report.reports.keySet()) {
            String playerName = target.getName();
            ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
            SkullMeta meta = (SkullMeta) item.getItemMeta();
            meta.setDisplayName(Util.color1 + playerName);
            meta.setLore(Arrays.asList(Util.color1 + "Report: " + Report.reports.get(target).toString()));
            meta.setOwner(playerName);
            item.setItemMeta(meta);
            gui.addItem(item);
        }
        gui.setItem(49, new ItemStack(Material.BARRIER, 1));
        player.openInventory(gui);
    }

}
