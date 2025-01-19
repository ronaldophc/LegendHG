package com.ronaldophc.player;

import com.ronaldophc.LegendHG;
import com.ronaldophc.util.Util;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class PlayerPreLogin implements Listener {

    @EventHandler
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        for (Player player : LegendHG.getInstance().getServer().getOnlinePlayers()) {
            if (player.getAddress().getAddress().equals(event.getAddress())) {
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, Util.title + "\n\n" + Util.errorServer + Util.bold + "Você já está conectado com este IP.");
            }
        }
    }
}
