package com.ronaldophc.player.listener;

import com.ronaldophc.LegendHG;
import com.ronaldophc.constant.Scores;
import com.ronaldophc.database.CurrentGameSQL;
import com.ronaldophc.database.PlayerSQL;
import com.ronaldophc.feature.scoreboard.Board;
import com.ronaldophc.helper.GameHelper;
import com.ronaldophc.helper.Util;
import com.ronaldophc.kits.Kit;
import com.ronaldophc.player.PlayerAliveManager;
import com.ronaldophc.player.PlayerHelper;
import com.ronaldophc.player.PlayerSpectatorManager;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.SQLException;

public class PlayerDeath implements Listener {


    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Bukkit.broadcastMessage("PlayerDeath");
        event.setDeathMessage(null);

        Player player = event.getEntity();
        Account account = AccountManager.getOrCreateAccount(player);

        Kit diedKit = account.getKits().getPrimary();
        Kit diedKit2 = account.getKits().getSecondary();

        event.getDrops().removeIf(diedKit::isItemKit);
        event.getDrops().removeIf(diedKit2::isItemKit);

        String message = "O player " + Util.color3 + player.getName() + " morreu";
        Player killer = event.getEntity().getKiller();

        if (event.getEntity().getKiller() != null) {

            Account killerAccount = AccountManager.getOrCreateAccount(killer);

            Kit killerKit = killerAccount.getKits().getPrimary();
            Kit killerKit2 = killerAccount.getKits().getSecondary();
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