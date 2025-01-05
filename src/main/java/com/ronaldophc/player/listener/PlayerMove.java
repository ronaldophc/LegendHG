package com.ronaldophc.player.listener;

import com.ronaldophc.LegendHG;
import com.ronaldophc.player.account.Account;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMove implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Account account = LegendHG.getAccountManager().getOrCreateAccount(event.getPlayer());
        if (!account.isLoggedIn()) {
            event.setCancelled(true);
        }
    }

}
