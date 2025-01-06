package com.ronaldophc.player.listener;

import com.ronaldophc.LegendHG;
import com.ronaldophc.kits.Kit;
import com.ronaldophc.kits.manager.KitManager;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PlayerDamages implements Listener {

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;

        Player damager = (Player) event.getDamager();
        Account damagerAccount = AccountManager.getInstance().getOrCreateAccount(damager);
        if (damagerAccount.isSpectator()) {
            event.setCancelled(true);
            return;
        }

        if (!(event.getEntity() instanceof Player)) return;

        if (!LegendHG.getGameStateManager().getGameState().canTakeDamage()) {
            event.setCancelled(true);
            return;
        }

        Player player = (Player) event.getEntity();

        Account playerAccount = AccountManager.getInstance().getOrCreateAccount(player);

        if (playerAccount.isSpectator()) {
            event.setCancelled(true);
            return;
        }

        KitManager kitManager = LegendHG.getKitManager();

//        String title = Util.success + player.getName() + Util.color2 + " - " + Util.color1 + kit.getName();
//
//        if (GameHelper.getInstance().getKits() == 2) {
//            title = Util.success + player.getName() + Util.color2 + " - " + Util.color1 + kit.getName() + Util.color2 + " - " + Util.color1 + kit2.getName();
//        }

        kitManager.setCombatLogCooldown(player, damager);

        double damage = event.getDamage() * 0.9;
        if (damage > 8) {
            damage = event.getDamage() * 0.8;
        }

        event.setDamage(damage);
    }

}


