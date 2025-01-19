package com.ronaldophc.kits.registry;

import com.ronaldophc.LegendHG;
import com.ronaldophc.util.ItemManager;
import com.ronaldophc.util.Util;
import com.ronaldophc.kits.Kit;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;

public class Viper extends Kit {

    public Viper() {
        super("Viper",
                "legendhg.kits.viper",
                new ItemManager(Material.SPIDER_EYE, Util.color3 + "Viper")
                        .setLore(Arrays.asList(Util.success + "Crie uma area envenenada", Util.success + "em sua volta."))
                        .build(),
                new ItemManager(Material.SPIDER_EYE, Util.color3 + "Viper")
                        .build(),
                false);
    }

    @EventHandler
    public void onActiveViper(PlayerInteractEvent event) {
        if (!(LegendHG.getGameStateManager().getGameState().canTakeDamage())) return;
        if (!(LegendHG.getGameStateManager().getGameState().canUseKit())) return;

        Player player = event.getPlayer();

        Account account = AccountManager.getInstance().getOrCreateAccount(player);
        if (!account.getKits().contains(this)) return;

        if (!isItemKit(player.getItemInHand())) return;

        if (kitManager.isOnCooldown(player, this)) return;
        kitManager.setCooldown(player, 12, this);

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
        for (Player player : AccountManager.getInstance().getPlayersAlive()) {

            Account account = AccountManager.getInstance().getOrCreateAccount(player);
            if (account.getKits().contains(this)) continue;

            if (player.getLocation().distance(location) <= 5) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 40, 0)); // Poison for 2 seconds
                player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20, 0)); // Poison for 2 seconds
            }
        }
    }

}

