package com.ronaldophc.listener;

import com.ronaldophc.api.tablist.TabListAPI;
import com.ronaldophc.helper.Util;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class TabListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
        build(event.getPlayer());
    }

    public void build(Player player) {
        TabListAPI.setHeaderAndFooter(player,
                Util.bold + Util.color1 + "LegendHG\n" + Util.color1 + "legendhg.com.br",
                Util.color2 + "Discord: " + Util.color1 + "Discord.gg\n" + Util.color2 + "Site: " + Util.color1 + "§bLegendhg.com.br");
    }
}
