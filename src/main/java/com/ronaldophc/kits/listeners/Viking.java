package com.ronaldophc.kits.listeners;

import com.ronaldophc.LegendHG;
import com.ronaldophc.constant.Kits;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Viking implements Listener {

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            if (!LegendHG.getGameStateManager().getGameState().canUseKit()) return;
            Player damager = (Player) event.getDamager();
            Player damaged = (Player) event.getEntity();
            if (!LegendHG.getKitManager().isThePlayerKit(damager, Kits.VIKING)) return;
            if (damager.getInventory().getItemInHand().getType().name().contains("AXE")) {
                event.setDamage(event.getDamage() + 2);
            }
        }
    }

}
