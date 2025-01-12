package com.ronaldophc.game;

import com.ronaldophc.LegendHG;
import com.ronaldophc.feature.MiniFeastManager;
import com.ronaldophc.helper.Util;
import com.ronaldophc.player.PlayerHelper;
import com.ronaldophc.player.account.AccountManager;
import com.ronaldophc.setting.Settings;
import org.bukkit.Bukkit;
import org.bukkit.Sound;

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
        LegendHG.getBoard().run();
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
        switch (countdownRemaining) {
            case 61:
                Util.playSoundForAll(Sound.CLICK);
                break;
            case 31:
            case 16:
            case 11:
            case 6:
            case 5:
            case 4:
            case 3:
            case 2:
            case 1:
                Util.playSoundForAll(Sound.CLICK);
                if (!PlayerHelper.verifyMinPlayers()) {
                    countdownRemaining = 30;
                }
                break;
            case 0:
            default:
                if (countdownRemaining <= 0) {
                    Util.playSoundForAll(Sound.ENDERDRAGON_GROWL);
                    plugin.gameStateManager.startInvincibility();
                }
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
        if(AccountManager.getInstance().getPlayersAlive().size() == 1) {
            plugin.gameStateManager.startFinished();
        }
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