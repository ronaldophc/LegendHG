package com.ronaldophc.kits.listeners;

import com.ronaldophc.LegendHG;
import com.ronaldophc.helper.Util;
import com.ronaldophc.kits.manager.KitManager;
import com.ronaldophc.constant.Kits;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;

public class Titan implements Listener {

    Kits TITAN = Kits.TITAN;
    private final Set<Player> invinciblePlayers = new HashSet<>();

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();
        if (invinciblePlayers.contains(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (!LegendHG.getGameStateManager().getGameState().canUseKit()) return;
        Player titan = event.getPlayer();

        KitManager kitManager = LegendHG.getKitManager();
        ItemStack item = event.getItem();

        if (!kitManager.isThePlayerKit(titan, TITAN)) return;
        if (!kitManager.isItemKit(item, TITAN)) return;

        if (kitManager.isOnCooldown(titan, TITAN)) return;
        kitManager.setCooldown(titan, 40, TITAN);

        invinciblePlayers.add(titan);
        titan.sendMessage(Util.success + "Você esta invencível por 10 segundos.");
        (new BukkitRunnable() {
            public void run() {
                titan.sendMessage(Util.success + "Você esta invencível por mais 5 segundos.");
            }
        }).runTaskLater(LegendHG.getInstance(), 5 * 20L);
        (new BukkitRunnable() {
            public void run() {
                titan.sendMessage(Util.success + "Você esta invencível por mais 3 segundos.");
            }
        }).runTaskLater(LegendHG.getInstance(), 7 * 20L);
        (new BukkitRunnable() {
            public void run() {
                titan.sendMessage(Util.success + "Você esta invencível por mais 1 segundo.");
            }
        }).runTaskLater(LegendHG.getInstance(), 9 * 20L);
        new BukkitRunnable() {
            @Override
            public void run() {
                titan.sendMessage(Util.success + "Você não esta mais invencível.");
                invinciblePlayers.remove(titan);
            }
        }.runTaskLater(LegendHG.getInstance(), 200L);
    }
}