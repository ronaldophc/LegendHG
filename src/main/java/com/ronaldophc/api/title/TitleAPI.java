package com.ronaldophc.api.title;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import org.bukkit.entity.Player;

public class TitleAPI {

    public static void setTitle(Player player, String title, String subtitle, int fadeInTime, int stayTime,
                                 int fadeOutTime) {
        if (fadeInTime != -1 && fadeOutTime != -1 && stayTime != -1)
            setTimes(player, fadeInTime, stayTime, fadeOutTime);
        setTitle(player, title, subtitle);
    }

    public static void setTitle(Player player, String title, String subtitle) {
        if (title != null && !title.isEmpty())
            setTitle(player, title);
        if (subtitle != null && !subtitle.isEmpty())
            setSubtitle(player, subtitle);
    }

    public static void setTitle(Player player, String string) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.TITLE);
        packet.getTitleActions().write(0, EnumWrappers.TitleAction.TITLE);
        packet.getChatComponents().write(0, WrappedChatComponent.fromText(string));
        ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
    }

    public static void setSubtitle(Player player, String string) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.TITLE);
        packet.getTitleActions().write(0, EnumWrappers.TitleAction.SUBTITLE);
        packet.getChatComponents().write(0, WrappedChatComponent.fromText(string));
        ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
    }

    public static void setTimes(Player player, int fadeInTime, int stayTime, int fadeOutTime) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.TITLE);
        packet.getTitleActions().write(0, EnumWrappers.TitleAction.TIMES);
        packet.getIntegers().write(0, fadeInTime);
        packet.getIntegers().write(1, stayTime);
        packet.getIntegers().write(2, fadeOutTime);
        ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);

    }

    public static void resetTitle(Player player) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.TITLE);
        packet.getTitleActions().write(0, EnumWrappers.TitleAction.RESET);
        ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);

    }

    public static void clearTitle(Player player) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.TITLE);
        packet.getTitleActions().write(0, EnumWrappers.TitleAction.CLEAR);
        ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
    }
}
