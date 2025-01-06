package com.ronaldophc.hook;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.*;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.ronaldophc.LegendHG;
import com.ronaldophc.helper.Util;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.UUID;

public class ProtocolLibHook {

    public static void register() {
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();

        // ----------------- Tag --------------------- //

        protocolManager.addPacketListener(new PacketAdapter(LegendHG.getInstance(), PacketType.Play.Server.PLAYER_INFO) {

            @Override
            public void onPacketSending(PacketEvent event) {
                PacketContainer packet = event.getPacket();

                if (packet.getPlayerInfoAction().read(0) == EnumWrappers.PlayerInfoAction.ADD_PLAYER) {
                    List<PlayerInfoData> list = packet.getPlayerInfoDataLists().read(0);

                    for (int i = 0; i < list.size(); i++) {
                        PlayerInfoData data = list.get(i);

                        if (data == null)
                            continue;

                        UUID uniqueId = data.getProfile().getUUID();
                        Player player = Bukkit.getPlayer(uniqueId);

                        if (player == null) {
                            continue;
                        }

                        GameProfile gameProfile = ((CraftPlayer) player).getProfile();

                        Account account = AccountManager.getInstance().getOrCreateAccount(player);
                        WrappedGameProfile wrappedProfile = new WrappedGameProfile(uniqueId, account.getActualName());

                        if (gameProfile.getProperties().containsKey("textures")) {
                            Property textures = gameProfile.getProperties().get("textures").iterator().next();
                            wrappedProfile.getProperties().put("textures", new WrappedSignedProperty(textures.getName(), textures.getValue(), textures.getSignature()));
                        }

                        PlayerInfoData playerInfoData = new PlayerInfoData(wrappedProfile, data.getLatency(), data.getGameMode(), data.getDisplayName());

                        list.set(i, playerInfoData);

                    }

                    packet.getPlayerInfoDataLists().write(0, list);
                }
            }
        });

        // ----------------- Tablist ----------------- //
        new BukkitRunnable() {

            @Override
            public void run() {
                PacketContainer tabPacket = protocolManager.createPacket(PacketType.Play.Server.PLAYER_LIST_HEADER_FOOTER);
                tabPacket.getChatComponents().write(0, WrappedChatComponent.fromText(Util.bold + Util.color1 + "LegendHG\n" + Util.color1 + "legendhg.com.br"));
                tabPacket.getChatComponents().write(1, WrappedChatComponent.fromText(Util.color2 + "Discord: " + Util.color1 + "Discord.gg\n" + Util.color2 + "Site: " + Util.color1 + "§bLegendhg.com.br"));

                try {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        protocolManager.sendServerPacket(player, tabPacket);
                    }
                } catch (Exception e) {
                    throw new RuntimeException("Cannot send Tab List packet.", e);
                }
            }
        }.runTaskTimer(LegendHG.getInstance(), 0, 10);
    }

    // ----------------- ActionBar ----------------- //

    public static void sendActionBar(Player player, String message) {
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        PacketContainer actionPacket = protocolManager.createPacket(PacketType.Play.Server.CHAT);

        actionPacket.getChatComponents().write(0, WrappedChatComponent.fromText(message));
        actionPacket.getBytes().write(0, (byte) 2); // ActionBar

        try {
            protocolManager.sendServerPacket(player, actionPacket);
        } catch (Exception e) {
            throw new RuntimeException("Cannot send Action Bar packet.", e);
        }
    }
}
