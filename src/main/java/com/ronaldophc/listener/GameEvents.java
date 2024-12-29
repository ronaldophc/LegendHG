package com.ronaldophc.listener;

import com.ronaldophc.player.PlayerSpectatorManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

import com.ronaldophc.LegendHG;
import com.ronaldophc.constant.GameState;

public class GameEvents implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void weather(WeatherChangeEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onItemSpawn(ItemSpawnEvent event) {
        if (LegendHG.getGameStateManager().getGameState() == GameState.COUNTDOWN) {
            event.getEntity().remove();
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onBed(PlayerBedEnterEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (LegendHG.getGameStateManager().getGameState() == GameState.COUNTDOWN || PlayerSpectatorManager.getInstance().isPlayerSpectating((Player) event.getPlayer())) {
            if (event.getInventory().getType() == InventoryType.CHEST && !event.getInventory().getTitle().contains("Kit") && !event.getInventory().getTitle().contains("Ajustes")) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPortal(PlayerPortalEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        int entitiesInChunk = event.getLocation().getChunk().getEntities().length;

        if (entitiesInChunk >= 10) {
            event.setCancelled(true);
        }
    }
}
