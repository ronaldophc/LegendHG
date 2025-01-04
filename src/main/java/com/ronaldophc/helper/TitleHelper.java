package com.ronaldophc.helper;

import com.ronaldophc.LegendHG;
import com.ronaldophc.hook.ProtocolLibHook;
import com.ronaldophc.kits.Kit;
import com.ronaldophc.kits.manager.KitManager;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class TitleHelper {

    public static void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        CraftPlayer craftplayer = (CraftPlayer) player;
        PlayerConnection connection = craftplayer.getHandle().playerConnection;
        IChatBaseComponent titleJSON = IChatBaseComponent.ChatSerializer.a("{'text': '" + title + "'}");
        IChatBaseComponent subtitleJSON = IChatBaseComponent.ChatSerializer.a("{'text': '" + subtitle + "'}");
        PacketPlayOutTitle titlePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, titleJSON, fadeIn, stay,
                fadeOut);
        PacketPlayOutTitle subtitlePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, subtitleJSON);
        connection.sendPacket(titlePacket);
        connection.sendPacket(subtitlePacket);
    }

    public static void sendActionBar(Player player, String message) {
        ProtocolLibHook.sendActionBar(player, message);
    }

    public static void sendCooldownBar(Player player, Kit kit) {
        KitManager kitManager = LegendHG.getKitManager();
        sendActionBar(player, Util.bold + Util.color1 + kit.getName() + ": " + Util.error + Util.bold + (kitManager.getCooldown(player, kit) + 1) + "s" + Util.bold + Util.color2 + " para usar novamente.");
    }

    public static void sendCombatLogCooldownBar(Player player) {
        KitManager kitManager = LegendHG.getKitManager();
        sendActionBar(player, Util.error + Util.bold + (kitManager.getCombatLogCooldown(player) + 1) + "s" + Util.bold + Util.color2 + " para sair do combate.");
    }
}
