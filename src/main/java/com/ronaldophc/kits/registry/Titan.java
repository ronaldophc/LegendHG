package com.ronaldophc.kits.registry;

import com.ronaldophc.LegendHG;
import com.ronaldophc.kits.Kit;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import com.ronaldophc.util.ItemManager;
import com.ronaldophc.util.Util;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Titan extends Kit {

    private final Set<Player> invinciblePlayers = new HashSet<>();

    public Titan() {
        super("Titan",
                "legendhg.kits.titan",
                new ItemManager(Material.BEDROCK, Util.color3 + "Titan")
                        .setLore(Arrays.asList(Util.success + "Fique invencivel", Util.success + "por 10 segundos."))
                        .build(),
                new ItemManager(Material.BEDROCK, Util.color3 + "Titan")
                        .build(),
                false);
    }

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

        Account account = AccountManager.getInstance().getOrCreateAccount(titan);
        if (!account.getKits().contains(this)) return;

        ItemStack item = event.getItem();

        if (!isItemKit(item)) return;

        if (kitManager.isOnCooldown(titan, this)) return;
        kitManager.setCooldown(titan, 40, this);

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