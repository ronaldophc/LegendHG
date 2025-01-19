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
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Timerlord extends Kit {

    private final Set<Player> blockedPlayers = new HashSet<>();

    public Timerlord() {
        super("Timerlord",
                "legendhg.kits.timerlord",
                new ItemManager(Material.WATCH, Util.color3 + "Timerlord")
                        .setLore(Arrays.asList(Util.success + "Ao clicar com o relogio", Util.success + "congele os jogadores em sua volta."))
                        .build(), new ItemManager(Material.WATCH, Util.color3 + "Timerlord")
                        .setUnbreakable()
                        .build(),
                false);
    }

    @EventHandler
    public void onEat(PlayerInteractEvent event) {
        if (!LegendHG.getGameStateManager().getGameState().canTakeDamage()) return;

        Player timerlord = event.getPlayer();

        Account account = AccountManager.getInstance().getOrCreateAccount(timerlord);
        if (!account.getKits().contains(this)) return;

        if (event.getItem() == null) return;
        if (!isItemKit(event.getItem())) return;
        if (!(event.getAction().name().contains("RIGHT"))) return;
        if (kitManager.isOnCooldown(timerlord, this)) return;
        kitManager.setCooldown(timerlord, 30, this);
        spawnParticles(timerlord.getLocation());
        for (Entity entity : timerlord.getNearbyEntities(5, 5, 5)) {
            if (!(entity instanceof Player)) continue;

            Player player = (Player) entity;
            if (AccountManager.getInstance().getOrCreateAccount(player).isAlive()) {
                blockedPlayers.add(player);
                new BukkitRunnable() {
                    public void run() {
                        blockedPlayers.remove(player);
                    }
                }.runTaskLater(LegendHG.getInstance(), 20 * 4);
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (blockedPlayers.contains(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    private void spawnParticles(Location location) {
        int radius = 5;
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                Location particleLocation = location.clone().add(x, 1, z);
                particleLocation.getWorld().playEffect(particleLocation, Effect.SMOKE, 1, radius);
            }
        }
    }
}
