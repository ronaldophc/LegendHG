package com.ronaldophc.listener.states;

import com.ronaldophc.LegendHG;
import com.ronaldophc.constant.GameState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;

public class CountdownListener implements Listener {

    @EventHandler
    public void onItemSpawn(ItemSpawnEvent event) {
        if (LegendHG.getGameStateManager().getGameState() == GameState.COUNTDOWN) {
            event.getEntity().remove();
            event.setCancelled(true);
            return;
        }
    }
}
