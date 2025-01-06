package com.ronaldophc.player.listener;

import com.ronaldophc.LegendHG;
import com.ronaldophc.helper.Util;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PlayerCommandPreprocess implements Listener {

    @EventHandler
    public void PlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
        String command = event.getMessage().split(" ")[0];
        Player player = event.getPlayer();
        Account account = AccountManager.getInstance().getOrCreateAccount(player);
        if (account.isLoggedIn()) {
            return;
        }
        if (command.toLowerCase().startsWith("/login") || command.toLowerCase().startsWith("/register")) {
            return;
        }
        player.sendMessage(Util.title + " > " + Util.error + "Você precisa logar para usar comandos.");
        event.setCancelled(true);
    }
}
