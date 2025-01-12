package com.ronaldophc.listener;

import com.ronaldophc.LegendHG;
import com.ronaldophc.helper.Util;
import com.ronaldophc.setting.Settings;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class Motd implements Listener {

    @EventHandler
    public void onMotd(ServerListPingEvent e) {
        int max = Settings.getInstance().getInt("MaxPlayers");
        String msg1 = Settings.getInstance().getString("Msg1");
        String msg2 = Settings.getInstance().getString("Msg2");
        String suffix = Settings.getInstance().getString("Suffix");
        e.setMaxPlayers(max);
        if (!LegendHG.getInstance().started) {
            msg2 = "                  §cServidor iniciando...";
        }
        e.setMotd(suffix + Util.title + " " + msg1 + "\n" + msg2);
    }
}