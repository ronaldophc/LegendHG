package com.ronaldophc.player.listener;

import com.ronaldophc.LegendHG;
import com.ronaldophc.constant.Scores;
import com.ronaldophc.database.PlayerSQL;
import com.ronaldophc.api.scoreboard.Board;
import com.ronaldophc.helper.GameHelper;
import com.ronaldophc.helper.Util;
import com.ronaldophc.kits.Kit;
import com.ronaldophc.kits.manager.KitManager;
import com.ronaldophc.player.PlayerHelper;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
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

        Player died = event.getEntity();
        Account account = AccountManager.getInstance().getOrCreateAccount(died);

        died.getWorld().strikeLightningEffect(died.getLocation());

        Kit diedKit = account.getKits().getPrimary();
        Kit diedKit2 = account.getKits().getSecondary();

        event.getDrops().removeIf(diedKit::isItemKit);
        event.getDrops().removeIf(diedKit2::isItemKit);

        String message = "O player " + Util.color3 + died.getName() + " morreu";
        Player killer = event.getEntity().getKiller();

        if (killer == null) {
            KitManager kitManager = LegendHG.getKitManager();
            killer = kitManager.getCombatLogHitterPlayer(died);
        }

        if (killer != null) {

            killer.playSound(killer.getLocation(), Sound.LEVEL_UP, 1, 1);
            Account killerAccount = AccountManager.getInstance().getOrCreateAccount(killer);

            Kit killerKit = killerAccount.getKits().getPrimary();
            Kit killerKit2 = killerAccount.getKits().getSecondary();

            if (GameHelper.getInstance().isTwoKits()) {
                message = Util.color1 + died.getName() + "(" + diedKit.getName() + " e " + diedKit2.getName() + ") foi para a próxima vida, cortesia de " + killer.getName() + "(" + killerKit.getName() + " e " + killerKit2.getName() + ")";
            } else {
                message = Util.color1 + died.getName() + "(" + diedKit.getName() + ") foi para a próxima vida, cortesia de " + killer.getName() + "(" + killerKit.getName() + ")";
            }

            try {
                killerAccount.addKill();
                PlayerSQL.addPlayerDeath(died);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        if (account.isAlive()) {
            account.setAlive(false);
        }

        Bukkit.broadcastMessage(message);
        Bukkit.broadcastMessage(Util.color3 + AccountManager.getInstance().getPlayersAlive().size() + " jogadores restantes");

        if (!died.hasPermission("legendhg.spec")) {
            died.kickPlayer(Util.color1 + "Voce morreu e não tem permissão para assistir a partida!");
            return;
        }

        Player finalKiller = killer;
        new BukkitRunnable() {

            @Override
            public void run() {
                died.spigot().respawn();
                if (finalKiller != null) {
                    died.teleport(finalKiller.getLocation());
                } else {
                    PlayerHelper.teleportPlayerToSpawnLocation(died);
                }
                PlayerHelper.preparePlayerToSpec(died);
                account.setSpectator(true);
                try {
                    Board.getInstance().removeScoreboard(died);
                    Board.setPlayerScore(died, Scores.SPEC);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

        }.runTaskLater(LegendHG.getInstance(), 1);
    }
}