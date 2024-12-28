package com.ronaldophc.kits.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import com.ronaldophc.LegendHG;
import com.ronaldophc.helper.Util;
import com.ronaldophc.kits.manager.KitManager;
import com.ronaldophc.constant.Kits;
import com.ronaldophc.kits.manager.kits.NinjaManager;

public class Ninja implements Listener {

    Kits NINJA = Kits.NINJA;

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        if (!LegendHG.getGameStateManager().getGameState().canTakeDamage()) return;

        if (!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof Player)) return;

        Player ninja = (Player) event.getDamager();

        if (!LegendHG.getKitManager().isThePlayerKit(ninja, Kits.NINJA)) return;

        Player target = (Player) event.getEntity();

        NinjaManager ninjaManager = NinjaManager.getInstance();

        ninjaManager.pull(ninja, target);

    }

    @EventHandler
    public void onShift(PlayerToggleSneakEvent event) {
        if (!LegendHG.getGameStateManager().getGameState().canUseKit()) return;

        Player ninja = event.getPlayer();
        if (!LegendHG.getKitManager().isThePlayerKit(ninja, Kits.NINJA)) return;

        NinjaManager ninjaManager = NinjaManager.getInstance();

        KitManager kitManager = LegendHG.getKitManager();
        if (kitManager.isOnCooldown(ninja, NINJA)) return;

        if (!ninjaManager.hasTarget(ninja)) return;
        if (!ninjaManager.isTargetOnline(ninja)) return;
        Player target = ninjaManager.getTarget(ninja);

        if (target.getLocation().distance(ninja.getLocation()) > 15D) {
            ninja.sendMessage(Util.errorServer + "Você está muito longe do seu alvo.");
            return;
        }

        ninja.teleport(target.getLocation());
        kitManager.setCooldown(ninja, 5, Kits.NINJA);
        ninjaManager.removeTarget(ninja);
    }
}
