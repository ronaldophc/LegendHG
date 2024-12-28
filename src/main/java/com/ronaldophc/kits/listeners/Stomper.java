package com.ronaldophc.kits.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import com.ronaldophc.LegendHG;
import com.ronaldophc.constant.Kits;

public class Stomper implements Listener {

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (!(LegendHG.getGameStateManager().getGameState().canTakeDamage())) return;
        if (!(LegendHG.getGameStateManager().getGameState().canUseKit())) return;
        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();

        if (!LegendHG.getKitManager().isThePlayerKit(player, Kits.STOMPER)) return;

        EntityDamageEvent.DamageCause cause = event.getCause();
        if (cause != EntityDamageEvent.DamageCause.FALL) return;

        if (event.getFinalDamage() <= 6) return;

        for (Entity entity : player.getNearbyEntities(5, 3, 5)) {
            if (entity instanceof Player) {
                Player target = (Player) entity;
                if (!target.isSneaking()) {
                    target.damage(event.getFinalDamage(), player);
                }
            }
        }

        event.setDamage(6);
    }
}
