package com.ronaldophc.kits.listeners;

import com.ronaldophc.LegendHG;
import com.ronaldophc.kits.manager.KitManager;
import com.ronaldophc.constant.Kits;
import com.ronaldophc.player.PlayerAliveManager;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class Viper implements Listener {

    @EventHandler
    public void onActiveViper(PlayerInteractEvent event) {
        if (!(LegendHG.getGameStateManager().getGameState().canTakeDamage())) return;
        if (!(LegendHG.getGameStateManager().getGameState().canUseKit())) return;

        Player player = event.getPlayer();

        if (!LegendHG.getKitManager().isThePlayerKit(player, Kits.VIPER)) return;
        if (!LegendHG.getKitManager().isItemKit(player.getItemInHand(), Kits.VIPER)) return;

        KitManager kitManager = LegendHG.getKitManager();
        if (kitManager.isOnCooldown(player, Kits.VIPER)) return;
        kitManager.setCooldown(player, 12, Kits.VIPER);

        startPoisonEffect(player.getLocation());
    }

    private void startPoisonEffect(Location location) {
        new BukkitRunnable() {
            int duration = 5; // Duration in ticks (5 seconds)

            @Override
            public void run() {
                if (duration <= 0) {
                    cancel();
                    return;
                }

                spawnParticles(location);
                applyPoisonDamage(location);

                duration--; // Decrease duration by 1 second (20 ticks)
            }
        }.runTaskTimer(LegendHG.getInstance(), 0, 20); // Run every second
    }

    private void spawnParticles(Location location) {
        int radius = 4;
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                Location particleLocation = location.clone().add(x, 1, z);
                particleLocation.getWorld().playEffect(particleLocation, Effect.HAPPY_VILLAGER, 1, radius);
                particleLocation.getWorld().playEffect(particleLocation, Effect.SMALL_SMOKE, 1, radius);
            }
        }
    }

    private void applyPoisonDamage(Location location) {
        for (UUID uuid : PlayerAliveManager.getInstance().getPlayersAlive()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player.getLocation().distance(location) <= 5 && !LegendHG.getKitManager().isThePlayerKit(player, Kits.VIPER)) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 40, 0)); // Poison for 2 seconds
                player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20, 0)); // Poison for 2 seconds
            }
        }
    }


}

