package com.ronaldophc.listener.states;

import com.ronaldophc.LegendHG;
import com.ronaldophc.constant.GameState;
import com.ronaldophc.feature.prefs.PrefsService;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class CountdownListener implements Listener {

    @EventHandler
    public void onItemSpawn(ItemSpawnEvent event) {
        if (LegendHG.getGameStateManager().getGameState() == GameState.COUNTDOWN) {
            event.getEntity().remove();
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction().name().contains("RIGHT")) {
            ItemStack item = event.getItem();
            if (item == null || item.getType() == Material.AIR) return;
            if (item.getType() == Material.NAME_TAG && item.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.YELLOW + "Ajustes")) {
                PrefsService.openPrefsMenu(player);
            }
        }
    }
}
