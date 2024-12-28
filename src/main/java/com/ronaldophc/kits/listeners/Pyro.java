package com.ronaldophc.kits.listeners;

import com.ronaldophc.LegendHG;
import com.ronaldophc.kits.manager.KitManager;
import com.ronaldophc.constant.Kits;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class Pyro implements Listener {

    Kits PYRO = Kits.PYRO;

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        KitManager kitManager = LegendHG.getKitManager();
        if (!kitManager.isThePlayerKit(player, PYRO)) return;
        if (!kitManager.isItemKit(event.getItem(), PYRO)) return;
        if (!LegendHG.getGameStateManager().getGameState().canTakeDamage()) {
            event.setCancelled(true);
            return;
        }
        if (!event.getAction().name().contains("RIGHT")) return;
        if (kitManager.isOnCooldown(player, PYRO)) {
            event.setCancelled(true);
            return;
        }
        kitManager.setCooldown(player, 10, PYRO);
        player.launchProjectile(Fireball.class);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Fireball)) return;
        if (!(event.getEntity() instanceof Player)) return;

        Fireball fireball = (Fireball) event.getDamager();
        if (!(fireball.getShooter() instanceof Player)) return;

        Player player = (Player) fireball.getShooter();
        Player target = (Player) event.getEntity();

        KitManager kitManager = LegendHG.getKitManager();

        if (!kitManager.isThePlayerKit(player, PYRO)) return;
        if (player == target) {
            event.setCancelled(true);
            return;
        }

    }
}
