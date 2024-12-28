package com.ronaldophc.gamestate;

import com.ronaldophc.LegendHG;
import com.ronaldophc.constant.GameState;
import com.ronaldophc.helper.Util;
import com.ronaldophc.setting.Settings;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class GameStateManager {
    private GameState gameState;

    public GameStateManager(LegendHG plugin) {
        startCountdown();
    }

    public void startCountdown() {
        setGameState(GameState.COUNTDOWN, Settings.getInstance().getInt("Countdown"));
    }

    public void startInvincibility() {
        setGameState(GameState.INVINCIBILITY, Settings.getInstance().getInt("Invincibility"));
        Bukkit.broadcastMessage(Util.color1 + "Iniciando a invencibilidade");
        GameStateHelper.preparePlayerToStart();
    }

    public void startInvincibilityForced() {
        Bukkit.broadcastMessage(Util.color1 + "Inicio do jogo forcado por um §cADMIN");
        Bukkit.broadcastMessage(Util.color1 + "Comeca em 3 segundos");
        CountDown.getInstance().setTime(3);
    }

    public void startRunning() {
        Bukkit.broadcastMessage(Util.color1 + "A invencibilidade acabou!");
        setGameState(GameState.RUNNING, 1);
    }

    public void startFinished() {
        GameStateHelper.finishGame();
        setGameState(GameState.FINISHED, 20);
    }

    public void startRestart() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            player.kickPlayer(Util.color1 + "O jogo acabou, reiniciando o servidor!");
        }
        setGameState(GameState.RESTARTING, 2);
    }

    public GameState getGameState() {
        return gameState;
    }

    private void setGameState(GameState state, int time) {
        this.gameState = state;
        CountDown.getInstance().setTime(time);
    }
}