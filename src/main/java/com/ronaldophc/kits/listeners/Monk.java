package com.ronaldophc.kits.listeners;

import com.ronaldophc.LegendHG;
import com.ronaldophc.kits.manager.KitManager;
import com.ronaldophc.constant.Kits;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class Monk implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();

        KitManager kitManager = LegendHG.getKitManager();

        if (!(entity instanceof Player)) return;
        if (!LegendHG.getGameStateManager().getGameState().canTakeDamage()) return;
        if (!kitManager.isThePlayerKit(player, Kits.MONK)) return;
        if (!kitManager.isItemKit(player.getItemInHand(), Kits.MONK)) return;

        Player target = (Player) event.getRightClicked();

        if (kitManager.isOnCooldown(player, Kits.MONK)) return;
        kitManager.setCooldown(player, 15, Kits.MONK);

        int playerInvSize = player.getInventory().getSize();
        int randomSlot = new Random().nextInt(playerInvSize);

        ItemStack inHand = target.getItemInHand();
        ItemStack randomItem = target.getInventory().getItem(randomSlot);

        target.getInventory().setItem(randomSlot, inHand);
        target.getInventory().setItemInHand(randomItem);
    }
}
