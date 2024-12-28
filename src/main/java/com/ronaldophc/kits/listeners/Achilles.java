package com.ronaldophc.kits.listeners;

import com.ronaldophc.LegendHG;
import com.ronaldophc.constant.Kits;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Achilles implements Listener {

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            if (!LegendHG.getGameStateManager().getGameState().canUseKit()) return;
            Player damager = (Player) event.getDamager();
            Player player = (Player) event.getEntity();
            if (!LegendHG.getKitManager().isThePlayerKit(player, Kits.ACHILLES)) return;
            if (damager.getItemInHand().getType().name().contains("WOOD_")) {
                event.setDamage(event.getDamage() * 1.5);
                return;
            }
            event.setDamage(event.getDamage() * 0.5);
        }
    }
}
