package com.ronaldophc.player.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.ronaldophc.LegendHG;
import com.ronaldophc.kits.manager.KitManager;
import com.ronaldophc.constant.Kits;
import com.ronaldophc.player.PlayerSpectatorManager;

public class PlayerDamages implements Listener {

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {

        if (!(event.getEntity() instanceof Player)) return;
        if (!(event.getDamager() instanceof Player)) return;
        if (!LegendHG.getGameStateManager().getGameState().canTakeDamage()) {
            event.setCancelled(true);
            return;
        }

        Player player = (Player) event.getEntity();
        Player damager = (Player) event.getDamager();

        if (PlayerSpectatorManager.getInstance().isPlayerSpectating(player)) {
            event.setCancelled(true);
            return;
        }

        if (PlayerSpectatorManager.getInstance().isPlayerSpectating(damager)) {
            event.setCancelled(true);
            return;
        }

        KitManager kitManager = LegendHG.getKitManager();

        Kits kit = kitManager.getPlayerKit(player);
        Kits kit2 = kitManager.getPlayerKit2(player);
        /* To do: BossBar to show the kits
        String title = Util.success + player.getName() + Util.color2 + " - " + Util.color1 + kit.getName();

        if (GameHelper.getInstance().getKits() == 2) {
            title = Util.success + player.getName() + Util.color2 + " - " + Util.color1 + kit.getName() + Util.color2 + " - " + Util.color1 + kit2.getName();
        }
        */
        if (kit.getCombatLog()) {
            kitManager.setCooldown(player, 5, kit);
        }

        if (kit2.getCombatLog()) {
            kitManager.setCooldown(player, 5, kit2);
        }

        double damage = event.getDamage() * 0.9;
        if (damage > 8) {
            damage = event.getDamage() * 0.8;
        }

        event.setDamage(damage);
    }

}


