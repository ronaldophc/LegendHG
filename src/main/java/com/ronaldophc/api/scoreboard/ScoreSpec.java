package com.ronaldophc.api.scoreboard;

import com.ronaldophc.game.CountDown;
import com.ronaldophc.helper.Util;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.sql.SQLException;

public class ScoreSpec extends Board {

    public static void createNewScoreboard(Player player, Objective objective, Scoreboard scoreboard) throws SQLException {
        objective.setDisplayName(Util.title);
        Account account = AccountManager.getInstance().getOrCreateAccount(player);
        addTeam(scoreboard, "team2", "§2", Util.color2 + "Tempo: ", Util.color1 + Util.formatSeconds(CountDown.getInstance().getRemainingTime()), 2);
        addTeam(scoreboard, "team1", "§1", Util.color2 + "Kills: ", Util.color1 + account.getKills(), 1);
        addTeam(scoreboard, "team0", "§0", Util.color2 + "Vivos: ", Util.color1 + AccountManager.getInstance().getPlayersAlive().size(), 0);

        player.setScoreboard(scoreboard);
    }

    public static void updateScoreboard(Player player) throws SQLException {
        Scoreboard scoreboard = player.getScoreboard();
        Account account = AccountManager.getInstance().getOrCreateAccount(player);

        Team team1 = scoreboard.getTeam("team1");
        team1.setSuffix(Util.color1 + account.getKills());

        Team team0 = scoreboard.getTeam("team0");
        team0.setSuffix(Util.color1 + AccountManager.getInstance().getPlayersAlive().size());

        Team team2 = scoreboard.getTeam("team2");
        team2.setSuffix(Util.color1 + Util.formatSeconds(CountDown.getInstance().getRemainingTime()));
    }
}
