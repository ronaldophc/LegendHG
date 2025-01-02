package com.ronaldophc.player.listener;

import com.ronaldophc.database.PlayerSQL;
import com.ronaldophc.helper.Util;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.sql.SQLException;

public class PlayerCommandPreprocess implements Listener {

    @EventHandler
    public void PlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) throws SQLException {
        String command = event.getMessage().split(" ")[0];
        if (!PlayerSQL.isPlayerLoggedIn(event.getPlayer())) {
            if (!command.startsWith("/login") && !command.startsWith("/register")) {
                Player player = event.getPlayer();
                player.sendMessage(Util.title + " > " + Util.error + "Você precisa logar para usar comandos.");
                event.setCancelled(true);
                return;
            }
        }
    }
}
