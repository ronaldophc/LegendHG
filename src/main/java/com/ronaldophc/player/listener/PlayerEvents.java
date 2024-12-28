package com.ronaldophc.player.listener;

import com.ronaldophc.LegendHG;
import com.ronaldophc.constant.Kits;
import com.ronaldophc.player.PlayerSpectatorManager;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

public class PlayerEvents implements Listener {


    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (PlayerSpectatorManager.getInstance().isPlayerSpectating(player)) {
            PlayerSpectatorManager.getInstance().removePlayer(player);
            event.setCancelled(true);
            return;
        }

        ItemStack item = event.getItemDrop().getItemStack();
        if (item == null) return;

        for (Kits kit : Kits.values()) {
            if (LegendHG.getKitManager().isItemKit(item, kit)) {
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
        if (PlayerSpectatorManager.getInstance().isPlayerSpectating(player)) {
            event.setCancelled(true);
            return;
        }
        ItemStack item = player.getItemInHand();
        if (item == null) return;
        for (Kits kit : Kits.values()) {
            if (LegendHG.getKitManager().isItemKit(item, kit)) {
                if(kit == Kits.LAUNCHER) continue;
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
