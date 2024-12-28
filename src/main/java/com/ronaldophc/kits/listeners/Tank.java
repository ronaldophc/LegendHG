package com.ronaldophc.kits.listeners;

import com.ronaldophc.LegendHG;
import com.ronaldophc.kits.manager.KitManager;
import com.ronaldophc.constant.Kits;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class Tank implements Listener {

    Kits TANK = Kits.TANK;

    @EventHandler
    public void onExplode(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player tank = (Player) event.getEntity();
        KitManager kitManager = LegendHG.getKitManager();
        if (!kitManager.isThePlayerKit(tank, TANK)) return;
        if (event.getCause().name().contains("EXPLOSION")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (event.getEntity().getKiller() == null) {
            return;
        }
        Player player = event.getEntity();
        Player tank = event.getEntity().getKiller();
        KitManager kitManager = LegendHG.getKitManager();
        if (!kitManager.isThePlayerKit(tank, TANK)) return;
        event.getEntity().getWorld().createExplosion(player.getLocation(), 4.0F);
    }
}
