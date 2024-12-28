package com.ronaldophc.kits.listeners;

import com.ronaldophc.LegendHG;
import com.ronaldophc.constant.Kits;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class Barbarian implements Listener {

    @EventHandler
    public void onKill(PlayerDeathEvent event) {
        if (event.getEntity().getKiller() == null) return;
        if (!LegendHG.getGameStateManager().getGameState().canUseKit()) return;
        Player killer = event.getEntity().getKiller();
        if (LegendHG.getKitManager().isThePlayerKit(killer, Kits.BARBARIAN)) {
            // To do: system to upgrade the play sword, before i do the kill system
        }
    }
}
