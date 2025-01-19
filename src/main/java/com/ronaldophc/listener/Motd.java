package com.ronaldophc.listener;

import com.ronaldophc.LegendHG;
import com.ronaldophc.setting.Settings;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import java.util.ArrayList;
import java.util.List;

public class Motd implements Listener {

    @EventHandler
    public void onMotd(ServerListPingEvent event) {
        List<String> motd = new ArrayList<>();
        motd.add(Settings.getInstance().getString("Msg1"));
        motd.add(Settings.getInstance().getString("Msg2"));
        StringBuilder message = new StringBuilder();
        for (String line : motd)
            message.append(centerText(line)).append("\n");
        if (!LegendHG.getInstance().started) {
            message = new StringBuilder(centerText(Settings.getInstance().getString("Msg1")) + "\n" + centerText("§cServidor iniciando"));
        }
        event.setMotd(message.toString());
    }

    public String centerText(String text) {
        int lineLength = 45;
        char[] chars = text.toCharArray();
        boolean isBold = false;
        double length = 0;
        ChatColor pholder = null;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '§' && chars.length != (i + 1) && (pholder = ChatColor.getByChar(chars[i + 1])) != null) {
                if (pholder != ChatColor.UNDERLINE && pholder != ChatColor.ITALIC
                        && pholder != ChatColor.STRIKETHROUGH && pholder != ChatColor.MAGIC) {
                    isBold = (chars[i + 1] == 'l');
                    length--;
                    i++;
                }
            } else {
                length++;
                length += (isBold ? (chars[i] != ' ' ? 0.1555555555555556 : 0) : 0);
            }
        }

        double spaces = (lineLength - length) / 2;

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < spaces; i++) {
            builder.append(' ');
        }
        builder.append(text);

        return builder.toString();
    }
}