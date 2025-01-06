package com.ronaldophc.kits.registry;

import com.ronaldophc.LegendHG;
import com.ronaldophc.helper.ItemManager;
import com.ronaldophc.helper.Util;
import com.ronaldophc.kits.Kit;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;

public class Phantom extends Kit {

    public Phantom() {
        super("Phantom",
                "legendhg.kits.phantom",
                new ItemManager(Material.FEATHER, Util.color3 + "Phantom")
                        .setLore(Arrays.asList(Util.success + "Use seu phantom", Util.success + "para poder voar", Util.success + "por 5 segundos."))
                        .build(),
                new ItemManager(Material.FEATHER, Util.color3 + "Phantom")
                        .setUnbreakable()
                        .build(),
                true);
    }

    @EventHandler
    public void onInteractPhantom(PlayerInteractEvent event) {
        if (!LegendHG.getGameStateManager().getGameState().canUseKit()) return;

        Player phantom = event.getPlayer();

        Account account = AccountManager.getInstance().getOrCreateAccount(phantom);
        if (!account.getKits().contains(this)) return;
        if (event.getItem() == null) return;
        if (!isItemKit(event.getItem())) return;
        if (kitManager.isOnCooldown(phantom, this)) return;

        kitManager.setCooldown(phantom, 40, this);
        phantom.setAllowFlight(true);
        phantom.setFlying(true);
        phantom.updateInventory();
        (new BukkitRunnable() {
            public void run() {
                phantom.sendMessage(Util.success + "Você pode voar por mais 5 segundos.");
            }
        }).runTaskLater(LegendHG.getInstance(), 40L);
        (new BukkitRunnable() {
            public void run() {
                phantom.sendMessage(Util.success + "Você pode voar por mais 3 segundos.");
            }
        }).runTaskLater(LegendHG.getInstance(), 60L);
        (new BukkitRunnable() {
            public void run() {
                phantom.sendMessage(Util.success + "Você pode voar por mais 1 segundo.");
            }
        }).runTaskLater(LegendHG.getInstance(), 80L);
        (new BukkitRunnable() {
            public void run() {
                phantom.setFlying(false);
                phantom.setAllowFlight(false);
            }
        }).runTaskLater(LegendHG.getInstance(), 100L);
    }

}