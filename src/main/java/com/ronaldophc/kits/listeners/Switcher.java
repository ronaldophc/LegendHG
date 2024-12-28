package com.ronaldophc.kits.listeners;

import com.ronaldophc.LegendHG;
import com.ronaldophc.kits.manager.KitManager;
import com.ronaldophc.constant.Kits;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class Switcher implements Listener {

    Kits SWITCHER = Kits.SWITCHER;

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        KitManager kitManager = LegendHG.getKitManager();
        if (!kitManager.isThePlayerKit(player, SWITCHER)) return;
        if (!kitManager.isItemKit(event.getItem(), SWITCHER)) return;
        if (!LegendHG.getGameStateManager().getGameState().canTakeDamage()) {
            event.setCancelled(true);
            return;
        }
        if (!event.getAction().name().contains("RIGHT")) return;
        if (kitManager.isOnCooldown(player, SWITCHER)) {
            event.setCancelled(true);
            return;
        }
        kitManager.setCooldown(player, 1, SWITCHER);
        for (ItemStack item : SWITCHER.getKitItem()) {
            player.getInventory().addItem(item);
        }
        player.updateInventory();
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Snowball)) return;
        if (!(event.getEntity() instanceof Player)) return;

        Snowball snowball = (Snowball) event.getDamager();
        if (!(snowball.getShooter() instanceof Player)) return;

        Player player = (Player) snowball.getShooter();
        Player target = (Player) event.getEntity();

        KitManager kitManager = LegendHG.getKitManager();

        if (!kitManager.isThePlayerKit(player, SWITCHER)) return;
        if (player == target) return;

        Location playerLocation = player.getLocation();
        Location targetLocation = target.getLocation();
        player.teleport(targetLocation);
        target.teleport(playerLocation);
        kitManager.setCooldown(player, 3, SWITCHER);
    }
}
