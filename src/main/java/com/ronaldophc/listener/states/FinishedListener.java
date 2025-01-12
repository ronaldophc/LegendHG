package com.ronaldophc.listener.states;

import com.ronaldophc.LegendHG;
import com.ronaldophc.constant.GameState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class FinishedListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDeath(EntityDamageEvent event) {
        if (LegendHG.getGameStateManager().getGameState() != GameState.FINISHED) {
            return;
        }

        event.setDamage(0);
    }
}
