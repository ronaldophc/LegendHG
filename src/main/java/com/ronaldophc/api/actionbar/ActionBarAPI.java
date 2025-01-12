package com.ronaldophc.api.actionbar;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import org.bukkit.entity.Player;

public class ActionBarAPI {

    public static void send(Player player, String message) {
        PacketContainer chatPacket = new PacketContainer(PacketType.Play.Server.CHAT);

        chatPacket.getChatComponents().write(0, WrappedChatComponent.fromJson("{\"text\":\"" + message + " \"}"));
        chatPacket.getBytes().write(0, (byte) 2); // ActionBar

        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, chatPacket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
