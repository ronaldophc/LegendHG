package com.ronaldophc.listener;

import com.ronaldophc.constant.CooldownType;
import com.ronaldophc.feature.prefs.PrefsService;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class PrefsInventoryListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        if (inventory.getName().contains("Ajustes")) {
            event.setCancelled(true);

            Player player = (Player) event.getWhoClicked();
            Account account = AccountManager.getInstance().getOrCreateAccount(player);

            ItemStack item = event.getCurrentItem();
            if (item == null) return;

            if (item.equals(PrefsService.tellActive)) {
                account.setTell(true);
                inventory.setItem(19, PrefsService.tellDisable);
            }

            if (item.equals(PrefsService.tellDisable)) {
                account.setTell(false);
                inventory.setItem(19, PrefsService.tellActive);
            }

            if (item.equals(PrefsService.chatActive)) {
                account.setChat(true);
                inventory.setItem(21, PrefsService.chatDisable);
            }

            if (item.equals(PrefsService.chatDisable)) {
                account.setChat(false);
                inventory.setItem(21, PrefsService.chatActive);
            }

            if (item.equals(PrefsService.cooldownChat)) {
                account.setCooldownType(CooldownType.CHAT);
                inventory.setItem(23, PrefsService.cooldownActionBar);
            }

            if (item.equals(PrefsService.cooldownActionBar)) {
                account.setCooldownType(CooldownType.ACTION_BAR);
                inventory.setItem(23, PrefsService.cooldownChat);
            }

        }
    }
}
