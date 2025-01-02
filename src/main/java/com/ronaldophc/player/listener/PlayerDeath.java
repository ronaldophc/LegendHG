package com.ronaldophc.player.listener;

import com.ronaldophc.LegendHG;
import com.ronaldophc.constant.Scores;
import com.ronaldophc.database.PlayerSQL;
import com.ronaldophc.feature.scoreboard.Board;
import com.ronaldophc.helper.GameHelper;
import com.ronaldophc.helper.Util;
import com.ronaldophc.kits.Kit;
import com.ronaldophc.player.PlayerHelper;
import com.ronaldophc.player.account.Account;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.SQLException;

public class PlayerDeath implements Listener {


    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.setDeathMessage(null);

        Player player = event.getEntity();
        Account account = LegendHG.getAccountManager().getOrCreateAccount(player);

        Kit diedKit = account.getKits().getPrimary();
        Kit diedKit2 = account.getKits().getSecondary();

        event.getDrops().removeIf(diedKit::isItemKit);
        event.getDrops().removeIf(diedKit2::isItemKit);

        String message = "O player " + Util.color3 + player.getName() + " morreu";
        Player killer = event.getEntity().getKiller();

        if (event.getEntity().getKiller() != null) {

            Account killerAccount = LegendHG.getAccountManager().getOrCreateAccount(killer);

            Kit killerKit = killerAccount.getKits().getPrimary();
            Kit killerKit2 = killerAccount.getKits().getSecondary();

            if (GameHelper.getInstance().isTwoKits()) {
                message = Util.color1 + player.getName() + "(" + killerKit.getName() + " e " + killerKit2.getName() + ") foi para a próxima vida, cortesia de " + killer.getName();
            } else {
                message = Util.color1 + player.getName() + "(" + killerKit.getName() + ") foi para a próxima vida, cortesia de " + killer.getName();
            }

            try {
                killerAccount.addKill();
                PlayerSQL.addPlayerKill(killer);
                PlayerSQL.addPlayerDeath(player);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        if (account.isAlive()) {
            account.setAlive(false);
        }

        Bukkit.broadcastMessage(message);
        Bukkit.broadcastMessage(Util.color3 + LegendHG.getAccountManager().getPlayersAlive().size() + " jogadores restantes");

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
                account.setSpectator(true);
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