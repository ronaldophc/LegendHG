package com.ronaldophc.kits.listeners;

import com.ronaldophc.LegendHG;
import com.ronaldophc.constant.Kits;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Random;

public class Magma implements Listener {

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player)) return;
        if (!LegendHG.getGameStateManager().getGameState().canUseKit()) return;

        Player damager = (Player) event.getDamager();
        if (!LegendHG.getKitManager().isThePlayerKit(damager, Kits.MAGMA)) return;

        Player damaged = (Player) event.getEntity();
        Random random = new Random();
        int chance = random.nextInt(100);
        if (chance < 33) {
            damaged.setFireTicks(100);
        }
    }

    @EventHandler
    public void onFireTick(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (!LegendHG.getGameStateManager().getGameState().canUseKit()) return;

        Player damaged = (Player) event.getEntity();
        if (!LegendHG.getKitManager().isThePlayerKit(damaged, Kits.MAGMA)) return;

        if (event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK || event.getCause() == EntityDamageEvent.DamageCause.FIRE || event.getCause() == EntityDamageEvent.DamageCause.LAVA) {
            event.setCancelled(true);
        }
    }
}
