package com.ronaldophc.kits.listeners;

import com.ronaldophc.LegendHG;
import com.ronaldophc.constant.Kits;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Anchor implements Listener {

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player)) return;
        if (!LegendHG.getGameStateManager().getGameState().canUseKit()) return;
        Player damaged = (Player) event.getEntity();
        if (!LegendHG.getKitManager().isThePlayerKit(damaged, Kits.ANCHOR)) return;
        new BukkitRunnable() {
            @Override
            public void run() {
                damaged.setVelocity(new Vector(0, 0, 0));
            }
        }.runTaskLater(LegendHG.getInstance(), 1L);
    }
}
