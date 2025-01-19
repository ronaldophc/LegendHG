package com.ronaldophc.player.listener;

import com.ronaldophc.LegendHG;
import com.ronaldophc.api.bossbar.BossBarAPI;
import com.ronaldophc.feature.battleonthesummit.SummitManager;
import com.ronaldophc.game.GameHelper;
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
        if (event.isCancelled()) return;
        if (!(event.getDamager() instanceof Player)) return;

        Player damager = (Player) event.getDamager();
        Account damagerAccount = AccountManager.getInstance().getOrCreateAccount(damager);

        if (!(event.getEntity() instanceof Player)) return;

        if (SummitManager.getInstance().getAccounts().contains(damagerAccount)) {
            return;
        }

        if (!LegendHG.getGameStateManager().getGameState().canTakeDamage()) {
            event.setCancelled(true);
            return;
        }

        Player player = (Player) event.getEntity();

        Account playerAccount = AccountManager.getInstance().getOrCreateAccount(player);

        KitManager kitManager = LegendHG.getKitManager();

        Kit kit = playerAccount.getKits().getPrimary();

        String title = player.getName() + " - " + kit.getName();

        if (GameHelper.getInstance().getKits() == 2) {
            Kit kit2 = playerAccount.getKits().getSecondary();
            title = player.getName() + " - " + kit.getName() + " e " + kit2.getName();
        }

        if (BossBarAPI.hasBar(player)) {
            BossBarAPI.removeBar(player);
        }
        BossBarAPI.setBar(damager, title, 2);

        kitManager.setCombatLogCooldown(player, damager);

        double damage = event.getDamage() * 0.9;
        if (damage > 8) {
            damage = event.getDamage() * 0.8;
        }

        event.setDamage(damage);
    }

}


