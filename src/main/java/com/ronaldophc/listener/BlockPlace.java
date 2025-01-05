package com.ronaldophc.listener;

import com.ronaldophc.player.account.AccountManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import com.ronaldophc.LegendHG;

public class BlockPlace implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!LegendHG.getGameStateManager().getGameState().canPlaceBlocks()) {
            event.setCancelled(true);
        }
        Player player = event.getPlayer();
        if (LegendHG.getAccountManager().getOrCreateAccount(player).isSpectator()) {
            event.setCancelled(true);
        }
    }
}
