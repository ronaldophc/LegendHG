package com.ronaldophc.gamestate;

import com.ronaldophc.feature.MiniFeastManager;
import org.bukkit.Bukkit;

import com.ronaldophc.LegendHG;
import com.ronaldophc.player.PlayerHelper;
import com.ronaldophc.setting.Settings;

public class CountDown implements Runnable {

    private static final CountDown instance = new CountDown();
    private int countdownRemaining;
    private final int feast;
    private final LegendHG plugin;

    private CountDown() {
        this.plugin = LegendHG.getInstance();
        this.countdownRemaining = Settings.getInstance().getInt("Countdown");
        this.feast = Settings.getInstance().getInt("Feast");
    }

    @Override
    public void run() {
        switch (LegendHG.getGameStateManager().getGameState()) {
            case COUNTDOWN:
                handleCountDown();
                break;
            case INVINCIBILITY:
                handleInvincibility();
                break;
            case RUNNING:
                handleRunning();
                break;
            case FINISHED:
                handleFinished();
                break;
            case RESTARTING:
                handleRestarting();
                break;
            default:
                break;
        }

    }

    private void handleRestarting() {
        if (countdownRemaining <= 0) {
            Bukkit.shutdown();
        }
        countdownRemaining--;
    }

    private void handleFinished() {
        if (countdownRemaining <= 0) {
            plugin.gameStateManager.startRestart();
        }
        countdownRemaining--;
    }

    private void handleCountDown() {
        if (countdownRemaining <= 10 && !PlayerHelper.verifyMinPlayers()) {
            countdownRemaining = 30;
        }
        if (countdownRemaining <= 0) {
            plugin.gameStateManager.startInvincibility();
        }
        countdownRemaining--;
    }

    private void handleInvincibility() {
        if (countdownRemaining <= 0) {
            plugin.gameStateManager.startRunning();
        }
        countdownRemaining--;
    }

    private void handleRunning() {
//        if(!PlayerAliveManager.getInstance().hasMoreThanOnePlayerAlive()) {
//            plugin.gameStateManager.startFinished();
//        }
        if (countdownRemaining >= feast) {
            LegendHG.getFeast().start();
        }
        switch(countdownRemaining) {
            case 1260:
            case 1080:
            case 600:
                new MiniFeastManager();
        }
        countdownRemaining++;
    }

    public int getRemainingTime() {
        return countdownRemaining;
    }

    public void setTime(int time) {
        countdownRemaining = time;
    }

    public static CountDown getInstance() {
        return instance;
    }
}