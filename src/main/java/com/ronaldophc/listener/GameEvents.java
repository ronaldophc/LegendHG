package com.ronaldophc.listener;

import com.ronaldophc.LegendHG;
import com.ronaldophc.constant.GameState;
import com.ronaldophc.player.account.AccountManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class GameEvents implements Listener {

    @EventHandler
    public void weather(WeatherChangeEvent e) {
        e.setCancelled(true);
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
        if (LegendHG.getGameStateManager().getGameState() == GameState.COUNTDOWN
                || AccountManager.getInstance().getOrCreateAccount((Player) event.getPlayer()).isSpectator()) {
            if (event.getInventory().getType() == InventoryType.CHEST) {
                String[] titles = {"Kit", "Ajustes", "Status", "Players", "BanIP", "Mute", "Ban"};
                for (String title : titles) {
                    if (event.getInventory().getTitle().contains(title)) {
                        return;
                    }
                }
                if (LegendHG.settings.getString("Environment").equalsIgnoreCase("dev")) {
                    event.getPlayer().sendMessage("§c§l[!] Dev mode on: §cVocê não pode abrir este inventário agora.");
                }
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


