package com.ronaldophc.player.listener;

import com.ronaldophc.database.PlayerSQL;
import com.ronaldophc.helper.Logger;
import com.ronaldophc.kits.registry.gladiator.GladiatorController;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.ronaldophc.LegendHG;
import com.ronaldophc.constant.GameState;
import com.ronaldophc.helper.Util;
import com.ronaldophc.player.PlayerAliveManager;
import com.ronaldophc.player.PlayerSpectatorManager;

import java.sql.SQLException;

public class PlayerQuit implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) throws SQLException {
        event.setQuitMessage(null);

        Player player = event.getPlayer();

        if (PlayerSpectatorManager.getInstance().isPlayerSpectating(player)) {
            quitPlayer(player);
            return;
        }

        if (!PlayerAliveManager.getInstance().isPlayerAlive(player.getUniqueId())) {
            return;
        }

        GameState gameState = LegendHG.getGameStateManager().getGameState();
        if (gameState == GameState.COUNTDOWN) {
            quitPlayer(player);
            event.setQuitMessage(Util.color3 + event.getPlayer().getName() + " saiu do jogo");
            return;
        }

        if (GladiatorController.getInstance().isPlayerInFight(player)) {
            removePlayer(player);
            event.setQuitMessage(Util.color3 + "O jogador " + Util.color1 + player.getName() + Util.color3 + " saiu da partida durante um duelo no Gladiator!");
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(LegendHG.getInstance(), () -> removePlayer(player), 20L * 60);
    }

    private void removePlayer(Player player) {
        if (player.isOnline()) return;
        quitPlayer(player);
        Bukkit.broadcastMessage(Util.color3 + player.getName() + " saiu do jogo e foi eliminado!");
    }

    private void quitPlayer(Player player) {
        Account account = AccountManager.getOrCreateAccount(player);
        account.setLoggedIn(false);
        PlayerAliveManager.getInstance().removePlayer(player);
        try {
            PlayerSQL.logoutPlayer(player);
        } catch (SQLException e) {
            Logger.logError("Erro ao deslogar jogador(PlayerQuit, quitPlayer): " + e.getMessage());
        }
    }

}
