package com.ronaldophc.kits.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

import com.ronaldophc.LegendHG;
import com.ronaldophc.constant.Kits;

public class Fisherman implements Listener {

    @EventHandler
    public void onFish(PlayerFishEvent event) {
        if(!(event.getCaught() instanceof Player)) return;
        if (!LegendHG.getGameStateManager().getGameState().canUseKit()) return;

        Player player = event.getPlayer();
        if (!LegendHG.getKitManager().isThePlayerKit(player, Kits.FISHERMAN)) return;

        Player caught = (Player) event.getCaught();
        caught.teleport(player.getLocation());
    }
}
