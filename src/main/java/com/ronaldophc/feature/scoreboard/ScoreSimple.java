package com.ronaldophc.feature.scoreboard;

import com.ronaldophc.LegendHG;
import com.ronaldophc.constant.GameState;
import com.ronaldophc.gamestate.CountDown;
import com.ronaldophc.helper.GameHelper;
import com.ronaldophc.helper.Util;
import com.ronaldophc.player.PlayerAliveManager;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class ScoreSimple extends Board {

    public static void createNewScoreboardOneKit(Player player, Objective objective, Scoreboard scoreboard) {
        objective.setDisplayName(Util.bold + Util.color1 + "Começo " + Util.formatSeconds(CountDown.getInstance().getRemainingTime()));
        addTeam(scoreboard, "team1", "§1", Util.color2 + "", Util.color1 + "", 1);
        addTeam(scoreboard, "team0", "§0", Util.color2 + "Players ", Util.color1 + PlayerAliveManager.getInstance().getPlayersAlive().size(), 0);

        player.setScoreboard(scoreboard);
    }

    public static void createNewScoreboardTwoKits(Player player, Objective objective, Scoreboard scoreboard) {
        objective.setDisplayName(Util.bold + Util.color1 + "Começo " + Util.formatSeconds(CountDown.getInstance().getRemainingTime()));
        addTeam(scoreboard, "team2", "§2", Util.color2 + "", Util.color1 + "", 2);
        addTeam(scoreboard, "team1", "§1", Util.color2 + "", Util.color1 + "", 1);
        addTeam(scoreboard, "team0", "§0", Util.color2 + "Players ", Util.color1 + PlayerAliveManager.getInstance().getPlayersAlive().size(), 0);

        player.setScoreboard(scoreboard);
    }

    public static void updateScoreboard(Player player, Objective objective) {
        GameState gameState = LegendHG.getGameStateManager().getGameState();
        Scoreboard scoreboard = player.getScoreboard();
        Account account = AccountManager.getOrCreateAccount(player);

        if (GameHelper.getInstance().getKits() == 2) {
            Team team2 = scoreboard.getTeam("team2");
            team2.setSuffix(Util.color1 + account.getKits().getSecondary().getName());
        }

        Team team1 = scoreboard.getTeam("team1");
        team1.setSuffix(Util.color1 + account.getKits().getPrimary().getName());

        Team team0 = scoreboard.getTeam("team0");
        team0.setSuffix(Util.color1 + PlayerAliveManager.getInstance().getPlayersAlive().size());

        String time = Util.formatSeconds(CountDown.getInstance().getRemainingTime());

        switch (gameState) {
            case COUNTDOWN:
                objective.setDisplayName(Util.bold + Util.color1 + "Começo " + time);
                break;
            case INVINCIBILITY:
            case RUNNING:
                objective.setDisplayName(Util.bold + Util.color1 + time);
                team0.setPrefix(Util.color2 + "Vivos: ");
                break;
            default:
                objective.setDisplayName(Util.color1 + "Padrao demais " + time);
                team0.setPrefix(Util.color2 + "Vivos: ");
                break;
        }
    }
}
