package com.ronaldophc.listener;

import com.ronaldophc.LegendHG;
import com.ronaldophc.task.NormalServerTickEvent;
import com.ronaldophc.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class NormalTaskListener implements Listener {

    private int id = 0;

    @EventHandler
    public void onTick(NormalServerTickEvent event) {
        int seconds = event.getSecondsOnline();
        int interval = 30;
        if (seconds % interval == 0) {
            String message = LegendHG.messages.getAutoMessage(id);

            if (message == null) {
                id = 0;
                return;
            }

            id++;
            sendAutoMessage(message);
        }
    }

    private void sendAutoMessage(String message) {
        message = message.replaceAll("title", Util.title).replaceAll("&", "§");

        Bukkit.broadcastMessage(message);
    }
}
