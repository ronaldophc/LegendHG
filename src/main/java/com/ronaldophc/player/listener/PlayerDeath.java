package com.ronaldophc.player.listener;

import com.ronaldophc.feature.scoreboard.Board;
import com.ronaldophc.constant.Scores;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.ronaldophc.LegendHG;
import com.ronaldophc.database.PlayerSQL;
import com.ronaldophc.helper.Util;
import com.ronaldophc.kits.manager.KitManager;
import com.ronaldophc.constant.Kits;
import com.ronaldophc.player.PlayerAliveManager;
import com.ronaldophc.player.PlayerHelper;
import com.ronaldophc.player.PlayerSpectatorManager;

import java.sql.SQLException;

public class PlayerDeath implements Listener {


    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.setDeathMessage(null);

        Player player = event.getEntity();
        KitManager kitManager = LegendHG.getKitManager();
        Kits kit = kitManager.getPlayerKit(player);
        Kits kit2 = kitManager.getPlayerKit2(player);

        event.getDrops().removeIf(item -> kitManager.isItemKit(item, kit));
        event.getDrops().removeIf(item -> kitManager.isItemKit(item, kit2));

        String message = "O player " + Util.color3 + player.getName() + " morreu";
        Player killer = event.getEntity().getKiller();

        if (event.getEntity().getKiller() != null) {

            message = Util.color1 + "O player " + Util.color3 + player.getName() + Util.color1 + " morreu pelo player " + Util.color3 + killer.getName();
            try {
                PlayerSQL.addPlayerKill(killer);
                PlayerSQL.addPlayerDeath(player);
            } catch (Exception ignored) {
            }
        }

        Bukkit.broadcastMessage(message);

        if (PlayerAliveManager.getInstance().isPlayerAlive(player.getUniqueId())) {
            PlayerAliveManager.getInstance().removePlayer(player);
        }

        if (!player.hasPermission("legendhg.spec")) {
            player.kickPlayer(Util.color1 + "Voce morreu e não tem permissão para assistir a partida!");
            return;
        }

        new BukkitRunnable() {

            @Override
            public void run() {
                player.spigot().respawn();
                if (killer != null) {
                    player.teleport(killer.getLocation());
                } else {
                    PlayerHelper.teleportPlayerToSpawnLocation(player);
                }
                PlayerHelper.preparePlayerToSpec(player);
                PlayerSpectatorManager.getInstance().addPlayer(player);
                try {
                    Board.getInstance().removeScoreboard(player);
                    Board.setPlayerScore(player, Scores.SPEC);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

        }.runTaskLater(LegendHG.getInstance(), 1);

    }
}