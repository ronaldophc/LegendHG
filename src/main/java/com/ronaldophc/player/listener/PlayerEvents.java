package com.ronaldophc.player.listener;

import com.ronaldophc.LegendHG;
import com.ronaldophc.kits.Kit;
import com.ronaldophc.kits.manager.KitManager;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerAchievementAwardedEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerStatisticIncrementEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerEvents implements Listener {


    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        Account account = LegendHG.getAccountManager().getOrCreateAccount(player);
        if (account.isSpectator()) {
            event.setCancelled(true);
            return;
        }

        ItemStack item = event.getItemDrop().getItemStack();
        if (item == null) return;

        KitManager kitManager = LegendHG.getKitManager();
        for (Kit kit : kitManager.getKits()) {
            if (kit.isItemKit(item)) {
                event.setCancelled(true);
                player.updateInventory();
                return;
            }
        }

        if (item.getType() == Material.CHEST && item.getEnchantments().containsKey(Enchantment.DURABILITY)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Account account = LegendHG.getAccountManager().getOrCreateAccount(player);
        if (account.isSpectator()) {
            event.setCancelled(true);
            return;
        }
        ItemStack item = player.getItemInHand();
        if (item == null) return;

        KitManager kitManager = LegendHG.getKitManager();
        for (Kit kit : kitManager.getKits()) {
            if (kit.isItemKit(item)) {
                if (kit.getName().equalsIgnoreCase("Launcher")) continue;
                if (kit.getName().equalsIgnoreCase("Brand")) continue;
                event.setCancelled(true);
                player.updateInventory();
                return;
            }
        }
    }

    @EventHandler
    public void onAchivements(PlayerAchievementAwardedEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onTest(PlayerStatisticIncrementEvent event) {
        event.setCancelled(true);
    }

}
