package com.ronaldophc.listener;

import com.ronaldophc.LegendHG;
import com.ronaldophc.feature.battleonthesummit.SummitManager;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityEvents implements Listener {

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.isCancelled()) return;

        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();
        Account playerAccount = AccountManager.getInstance().getOrCreateAccount(player);

        if (event.getCause() == EntityDamageEvent.DamageCause.SUFFOCATION) {
            event.setCancelled(true);
            return;
        }

        if (SummitManager.getInstance().getAccounts().contains(playerAccount)) {
            return;
        }

        if (!LegendHG.getGameStateManager().getGameState().canTakeDamage()) {
            event.setCancelled(true);
            return;
        }

    }
}
