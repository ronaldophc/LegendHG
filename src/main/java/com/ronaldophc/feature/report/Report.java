package com.ronaldophc.feature.report;

import com.ronaldophc.constant.ReportEnum;
import com.ronaldophc.helper.Util;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class Report {

    public static HashMap<Player, ReportEnum> reports = new HashMap<>();

    public static void sendReportPacket(PlayerConnection connection, String text, String command, String hoverText) {
        String json = String.format("{\"text\":\"%s\",\"italic\":true,\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"%s\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":[{\"text\":\"%s\",\"bold\":true,\"color\":\"dark_red\"}]}}", text, command, hoverText);
        connection.sendPacket(new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a(json)));
    }

    public static void sendNotification() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            if(player.hasPermission("legendhg.admin.report")) {
                player.sendMessage(Util.success + "Chegou um novo Report, use /reports para ver!");
            }
        }
    }
}
