package com.ronaldophc.kits.listeners;

import com.ronaldophc.LegendHG;
import com.ronaldophc.constant.Kits;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Anchor implements Listener {

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        if (!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player)) return;
        if (!LegendHG.getGameStateManager().getGameState().canUseKit()) return;
        Player damaged = (Player) event.getEntity();
        Player damager = (Player) event.getDamager();
        if (!LegendHG.getKitManager().isThePlayerKit(damaged, Kits.ANCHOR) && !LegendHG.getKitManager().isThePlayerKit(damager, Kits.ANCHOR))
            return;
        damaged.damage(event.getDamage());
        damaged.getWorld().playSound(damaged.getLocation(), Sound.ANVIL_USE, 1.0f, 1.0f);
        event.setCancelled(true);
    }
}
