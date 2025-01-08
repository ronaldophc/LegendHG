package com.ronaldophc.helper;

import com.ronaldophc.LegendHG;
import com.ronaldophc.player.PlayerHelper;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Helper {

    public static void refreshPlayer(Player player) {
        Bukkit.getScheduler().runTask(LegendHG.getInstance(), () -> {
            try {
                EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();

                for (Player online : Bukkit.getOnlinePlayers()) {
                    PlayerConnection connection = ((CraftPlayer) online).getHandle().playerConnection;

                    connection.sendPacket(new PacketPlayOutPlayerInfo(
                            PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, entityPlayer));

                    connection.sendPacket(new PacketPlayOutPlayerInfo(
                            PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, entityPlayer));

                    if (!online.equals(player)) {
                        connection.sendPacket(new PacketPlayOutEntityDestroy(entityPlayer.getId()));
                        connection.sendPacket(new PacketPlayOutNamedEntitySpawn(entityPlayer));
                    }
                }

                PacketPlayOutRespawn respawn = new PacketPlayOutRespawn(
                        entityPlayer.dimension,
                        entityPlayer.getWorld().getDifficulty(),
                        entityPlayer.getWorld().getWorldData().getType(),
                        entityPlayer.playerInteractManager.getGameMode()
                );
                CraftPlayer craftOnlinePlayer = (CraftPlayer) player;
                craftOnlinePlayer.getHandle().playerConnection.sendPacket(respawn);
                player.teleport(player.getLocation());
                player.updateInventory();

                // Atualizar a aparência para todos
                Bukkit.getScheduler().runTaskLater(LegendHG.getInstance(), () -> {
                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        if (!onlinePlayer.equals(player)) {
                            onlinePlayer.hidePlayer(player);
                            onlinePlayer.showPlayer(player);
                        }
                    }
                }, 5L);

                Account account = AccountManager.getInstance().getOrCreateAccount(player);
                if (account.isSpectator()) {
                    PlayerHelper.preparePlayerToSpec(player);
                }
            } catch (Exception e) {
                Logger.logError("Erro ao atualizar player (SkinFix, refreshPlayer): " + e.getMessage());
                throw new RuntimeException(e);
            }
        });
    }

    public static void injectPlayerNotTabComplete(Player player) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        Channel channel = craftPlayer.getHandle().playerConnection.networkManager.channel;

        channel.pipeline().addBefore("packet_handler", "tab_complete_blocker", new ChannelDuplexHandler() {
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                if (msg instanceof PacketPlayInTabComplete) {
                    // Block incoming tab-completion requests
                    return;
                }
                super.channelRead(ctx, msg);
            }

            @Override
            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                if (msg instanceof PacketPlayOutTabComplete) {
                    // Block outgoing tab-completion results
                    return;
                }
                super.write(ctx, msg, promise);
            }
        });
    }

    public static void loadAllChunks() {
        LegendHG.logger.info("Loading chunks where 400 & -400");
        World world = Bukkit.getWorld("world");
        int minX = -400;
        int maxX = 400;
        int minZ = -400;
        int maxZ = 400;

        for (int x = minX; x <= maxX; x += 16) {
            for (int z = minZ; z <= maxZ; z += 16) {
                Chunk chunk = world.getChunkAt(x >> 4, z >> 4);
                world.loadChunk(chunk);
            }
        }
        LegendHG.logger.info("Chunks loaded");
    }
}