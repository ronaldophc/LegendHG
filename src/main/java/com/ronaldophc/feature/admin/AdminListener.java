package com.ronaldophc.feature.admin;

import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import com.ronaldophc.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

public class AdminListener implements Listener {

    @EventHandler
    public static void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Account account = AccountManager.getInstance().getOrCreateAccount(player);
        if (player.getItemInHand() != null && player.getItemInHand().isSimilar(AdminManager.playersCompass)) {
            AdminManager.openInventory(player);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public static void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        Account account = AccountManager.getInstance().getOrCreateAccount(player);
        if (event.getCurrentItem() == null) return;

        Inventory inventory = event.getInventory();
        if (inventory.getName().equals(AdminManager.inventoryName)) {
            event.setCancelled(true);
            if (event.getCurrentItem().getType() != Material.SKULL_ITEM) {
               return;
            }

            String targetName = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
            Player target = Bukkit.getPlayer(targetName);

            if (target == null) {
                player.sendMessage(Util.error + "Jogador não encontrado.");
                player.closeInventory();
                return;
            }

            player.teleport(target);
            player.closeInventory();
            player.sendMessage(Util.admin + "Teleportado para " + target.getName() + ".");
        }
    }
}
