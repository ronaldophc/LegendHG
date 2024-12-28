package com.ronaldophc.feature.scoreboard;

import com.ronaldophc.LegendHG;
import com.ronaldophc.database.CurrentGameSQL;
import com.ronaldophc.gamestate.CountDown;
import com.ronaldophc.helper.Util;
import com.ronaldophc.player.PlayerAliveManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.sql.SQLException;

public class ScoreSpec extends Board {

    public static void createNewScoreboard(Player player, Objective objective, Scoreboard scoreboard) throws SQLException {
        objective.setDisplayName(Util.title);
        addTeam(scoreboard, "team2", "§2", Util.color2 + "Tempo: ", Util.color1 + Util.formatSeconds(CountDown.getInstance().getRemainingTime()), 2);
        addTeam(scoreboard, "team1", "§1", Util.color2 + "Kills: ", Util.color1 + CurrentGameSQL.getCurrentGameKills(player, LegendHG.getGameId()), 1);
        addTeam(scoreboard, "team0", "§0", Util.color2 + "Vivos: ", Util.color1 + PlayerAliveManager.getInstance().getPlayersAlive().size(), 0);

        player.setScoreboard(scoreboard);
    }

    public static void updateScoreboard(Player player) throws SQLException {
        Scoreboard scoreboard = player.getScoreboard();

        Team team = scoreboard.getTeam(player.getName());
        team.setSuffix(ChatColor.GREEN + " [Suffix]");
        team.setPrefix(ChatColor.RED + "[Prefix] ");

        Team team1 = scoreboard.getTeam("team1");
        team1.setSuffix(Util.color1 + CurrentGameSQL.getCurrentGameKills(player, LegendHG.getGameId()));

        Team team0 = scoreboard.getTeam("team0");
        team0.setSuffix(Util.color1 + PlayerAliveManager.getInstance().getPlayersAlive().size());

        Team team2 = scoreboard.getTeam("team2");
        team2.setSuffix(Util.color1 + Util.formatSeconds(CountDown.getInstance().getRemainingTime()));
    }
}
