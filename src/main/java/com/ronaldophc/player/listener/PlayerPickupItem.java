package com.ronaldophc.player.listener;

import com.ronaldophc.player.PlayerSpectatorManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class PlayerPickupItem implements Listener {

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        if (PlayerSpectatorManager.getInstance().isPlayerSpectating(event.getPlayer())) {
            event.setCancelled(true);
        }
    }
}
