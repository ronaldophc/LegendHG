package com.ronaldophc.kits.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import com.ronaldophc.LegendHG;
import com.ronaldophc.constant.GameState;
import com.ronaldophc.kits.manager.KitManager;
import com.ronaldophc.constant.Kits;
import com.ronaldophc.kits.manager.kits.gladiator.GladiatorController;

public class Gladiator implements Listener {

    Kits GLADIATOR = Kits.GLADIATOR;

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent event) {
        Player gladiator = event.getPlayer();

        if (!LegendHG.getGameStateManager().getGameState().canUseKit()) return;
        if (LegendHG.getGameStateManager().getGameState() == GameState.INVINCIBILITY) return;

        KitManager kitManager = LegendHG.getKitManager();
        if (!kitManager.isThePlayerKit(gladiator, GLADIATOR)) return;

        if (!(event.getRightClicked() instanceof Player)) return;

        Player target = (Player) event.getRightClicked();
        if (!kitManager.isItemKit(gladiator.getItemInHand(), GLADIATOR)) return;

        event.setCancelled(true);

        if (kitManager.isOnCooldown(gladiator, GLADIATOR)) return;
        GladiatorController gladiatorController = LegendHG.getGladiatorController();

        gladiatorController.startGladiatorFight(gladiator, target);
    }

}