package com.ronaldophc.api.scoreboard;

import com.ronaldophc.LegendHG;
import com.ronaldophc.constant.GameState;
import com.ronaldophc.game.CountDown;
import com.ronaldophc.util.Util;
import com.ronaldophc.player.account.AccountManager;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class ScoreSimple extends Board {

    public static void createNewScoreboard(Player player, Objective objective, Scoreboard scoreboard) {
        objective.setDisplayName(Util.bold + Util.color1 + "Começo " + Util.formatSeconds(CountDown.getInstance().getRemainingTime()));
        addTeam(scoreboard, "team0", "§0", Util.color2 + "Players: ", Util.color1 + AccountManager.getInstance().getPlayersAlive().size(), 0);

        player.setScoreboard(scoreboard);
    }

    public static void updateScoreboard(Player player) {
        GameState gameState = LegendHG.getGameStateManager().getGameState();
        Scoreboard scoreboard = player.getScoreboard();
        Objective objective = player.getScoreboard().getObjective(DisplaySlot.SIDEBAR);

        Team team0 = scoreboard.getTeam("team0");

        if (team0 != null) {
            team0.setSuffix(Util.color1 + AccountManager.getInstance().getPlayersAlive().size());
        }

        String time = Util.formatSeconds(CountDown.getInstance().getRemainingTime());

        switch (gameState) {
            case COUNTDOWN:
                objective.setDisplayName(Util.bold + Util.color1 + "Começo " + time);
                break;
            case INVINCIBILITY:
            case RUNNING:
            default:
                objective.setDisplayName(Util.bold + Util.color1 + time);
                if (team0 != null) {
                    team0.setPrefix(Util.color2 + "Vivos: ");
                }
        }
    }
}
