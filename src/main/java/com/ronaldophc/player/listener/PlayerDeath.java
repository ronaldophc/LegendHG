package com.ronaldophc.player.listener;

import com.ronaldophc.database.CurrentGameSQL;
import com.ronaldophc.feature.scoreboard.Board;
import com.ronaldophc.constant.Scores;
import com.ronaldophc.helper.GameHelper;
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
        Bukkit.broadcastMessage("PlayerDeath");
        event.setDeathMessage(null);

        Player player = event.getEntity();
        KitManager kitManager = LegendHG.getKitManager();
        Kits diedKit = kitManager.getPlayerKit(player);
        Kits diedKit2 = kitManager.getPlayerKit2(player);

        event.getDrops().removeIf(item -> kitManager.isItemKit(item, diedKit));
        event.getDrops().removeIf(item -> kitManager.isItemKit(item, diedKit2));

        String message = "O player " + Util.color3 + player.getName() + " morreu";
        Player killer = event.getEntity().getKiller();

        if (event.getEntity().getKiller() != null) {

            Kits killerKit = kitManager.getPlayerKit(player);
            Kits killerKit2 = kitManager.getPlayerKit2(player);
            if (GameHelper.getInstance().isTwoKits()) {
                message = Util.color1 + player.getName() + "(" + killerKit.getName() + " e " + killerKit2.getName() + ") foi para a próxima vida, cortesia de " + killer.getName();
            } else {
                message = Util.color1 + player.getName() + "(" + killerKit.getName() + ") foi para a próxima vida, cortesia de " + killer.getName();
            }
            try {
                CurrentGameSQL.addCurrentGameKill(killer, LegendHG.getGameId());
                CurrentGameSQL.addCurrentGameDeath(player, LegendHG.getGameId());
                PlayerSQL.addPlayerKill(killer);
                PlayerSQL.addPlayerDeath(player);
            } catch (Exception ignored) {
            }
        }



        if (PlayerAliveManager.getInstance().isPlayerAlive(player.getUniqueId())) {
            PlayerAliveManager.getInstance().removePlayer(player);
        }

        Bukkit.broadcastMessage(message);
        Bukkit.broadcastMessage(Util.color3 + PlayerAliveManager.getInstance().getPlayersAlive().size() + " jogadores restantes");

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