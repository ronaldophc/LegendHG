package com.ronaldophc.player.listener;

import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMove implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Account account = AccountManager.getInstance().getOrCreateAccount(event.getPlayer());
        if (!account.isLoggedIn()) {
            if (event.getPlayer().getLocation().subtract(0, 1, 0).getBlock().getType() == Material.AIR || event.getPlayer().isFlying()) {
                return;
            }
            event.setCancelled(true);
        }
    }

}
