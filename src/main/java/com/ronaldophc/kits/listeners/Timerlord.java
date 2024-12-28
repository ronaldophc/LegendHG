package com.ronaldophc.kits.listeners;

import com.ronaldophc.LegendHG;
import com.ronaldophc.kits.manager.KitManager;
import com.ronaldophc.constant.Kits;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;

public class Timerlord implements Listener {

    Kits TIMERLORD = Kits.TIMERLORD;
    private final Set<Player> blockedPlayers = new HashSet<>();

    @EventHandler
    public void onEat(PlayerInteractEvent event) {
        if (!LegendHG.getGameStateManager().getGameState().canTakeDamage()) return;

        Player timerlord = event.getPlayer();
        KitManager kitManager = LegendHG.getKitManager();

        if (!kitManager.isThePlayerKit(timerlord, TIMERLORD)) return;

        if (event.getItem() == null) return;
        if (!kitManager.isItemKit(event.getItem(), TIMERLORD)) return;
        if (!(event.getAction().name().contains("RIGHT"))) return;
        if (kitManager.isOnCooldown(timerlord, TIMERLORD)) return;
        kitManager.setCooldown(timerlord, 30, TIMERLORD);
        for (Entity entity : timerlord.getNearbyEntities(5, 5, 5)) {
            if (!(entity instanceof Player)) continue;

            Player player = (Player) entity;
            blockedPlayers.add(player);
            new BukkitRunnable()
            {
                public void run()
                {
                    blockedPlayers.remove(player);
                }
            }.runTaskLater(LegendHG.getInstance(), 20 * 5);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (blockedPlayers.contains(event.getPlayer())) {
            event.setCancelled(true);
        }
    }
}
