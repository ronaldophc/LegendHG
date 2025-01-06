package com.ronaldophc.hook;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedSignedProperty;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.ronaldophc.LegendHG;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

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

                        if (player == null)
                            continue;

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

    }

}
