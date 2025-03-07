package com.ronaldophc.listener;

import com.ronaldophc.LegendHG;
import com.ronaldophc.feature.CustomYaml;
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
        CustomYaml settings = LegendHG.settings;
        motd.add(settings.getString("Line1"));
        motd.add(settings.getString("Line2"));
        StringBuilder message = new StringBuilder();
        for (String line : motd)
            message.append(centerText(line)).append("\n");
        if (!LegendHG.getInstance().started) {
            message = new StringBuilder(centerText(settings.getString("Line1")) + "\n" + centerText("§cServidor iniciando"));
        }
        if (LegendHG.settings.getBoolean("Maintenance")) {
            message = new StringBuilder(centerText(settings.getString("Line1")) + "\n" + centerText("§c§lServidor em manutenção"));
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