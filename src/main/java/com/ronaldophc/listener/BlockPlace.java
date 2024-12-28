package com.ronaldophc.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import com.ronaldophc.LegendHG;
import com.ronaldophc.player.PlayerSpectatorManager;

public class BlockPlace implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!LegendHG.getGameStateManager().getGameState().canPlaceBlocks()) {
            event.setCancelled(true);
        }
        Player player = event.getPlayer();
        if (PlayerSpectatorManager.getInstance().isPlayerSpectating(player)) {
            event.setCancelled(true);
        }
    }
}
