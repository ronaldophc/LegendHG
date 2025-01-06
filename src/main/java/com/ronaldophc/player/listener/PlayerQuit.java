package com.ronaldophc.player.listener;

import com.ronaldophc.LegendHG;
import com.ronaldophc.constant.GameState;
import com.ronaldophc.helper.Util;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);

        Player player = event.getPlayer();
        Account account = AccountManager.getInstance().getOrCreateAccount(player);

        if (account.isSpectator()) {
            quitPlayer(player);
            return;
        }

        if (!account.isAlive()) {
            return;
        }

        if (LegendHG.getGladiatorController().isPlayerInFight(player)) {
            removePlayer(player);
            event.setQuitMessage(Util.color3 + "O jogador " + Util.color1 + player.getName() + Util.color3 + " saiu da partida durante um duelo no Gladiator!");
            return;
        }

        event.setQuitMessage(Util.color3 + event.getPlayer().getName() + " saiu do jogo");

        GameState gameState = LegendHG.getGameStateManager().getGameState();

        if (gameState == GameState.COUNTDOWN) {
            quitPlayer(player);
            return;
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(LegendHG.getInstance(), () -> removePlayer(player), 20L * 20);
    }

    private void removePlayer(Player player) {
        if (player.isOnline()) return;
        quitPlayer(player);
        Bukkit.broadcastMessage(Util.color3 + player.getName() + " saiu do jogo e foi eliminado!");
    }

    private void quitPlayer(Player player) {
        Account account = AccountManager.getInstance().getOrCreateAccount(player);
        account.logout();
        account.setAlive(false);
    }

}
