package com.ronaldophc.feature.scoreboard;

import com.ronaldophc.LegendHG;
import com.ronaldophc.constant.GameState;
import com.ronaldophc.gamestate.CountDown;
import com.ronaldophc.helper.GameHelper;
import com.ronaldophc.helper.Util;
import com.ronaldophc.player.PlayerAliveManager;
import com.ronaldophc.setting.Settings;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.sql.SQLException;

public class ScoreComplete extends Board {

    public static void createNewScoreboardOneKit(Player player, Objective objective, Scoreboard scoreboard) {
        objective.setDisplayName(Util.title);
        addTeam(scoreboard, "team9", "§9", Util.color2 + "Players: ", Util.color1 + PlayerAliveManager.getInstance().getPlayersAlive().size(), 9);
        addScore(objective, "§8 ", 8);
        addTeam(scoreboard, "team7", "§7", Util.color2 + "Kit: ", Util.color1 + LegendHG.getKitManager().getPlayerKitName(player), 7);
        addScore(objective, "§5", 5);
        addTeam(scoreboard, "team4", "§4", "§f", Util.color1 + "Iniciando", 4);
        addScore(objective, "§3 ", 3);
        addTeam(scoreboard, "team2", "§2", Util.color2 + "Começa em: ", Util.color1 + Settings.getInstance().getInt("Countdown"), 2);
        addScore(objective, "§1 ", 1);
        addScore(objective, Util.color1 + "Legendmc.com.br", 0);

        player.setScoreboard(scoreboard);
    }

    public static void createNewScoreboardTwoKits(Player player, Objective objective, Scoreboard scoreboard) {
        objective.setDisplayName(Util.title);
        addTeam(scoreboard, "team9", "§9", Util.color2 + "Players: ", Util.color1 + PlayerAliveManager.getInstance().getPlayersAlive().size(), 9);
        addScore(objective, "§8 ", 8);
        addTeam(scoreboard, "team7", "§7", Util.color2 + "Kit: ", Util.color1 + LegendHG.getKitManager().getPlayerKitName(player), 7);
        addTeam(scoreboard, "team6", "§6", Util.color2 + "Kit 2: ", Util.color1 + LegendHG.getKitManager().getPlayerKitName2(player), 6);
        addScore(objective, "§5", 5);
        addTeam(scoreboard, "team4", "§4", "§f", Util.color1 + "Iniciando", 4);
        addScore(objective, "§3 ", 3);
        addTeam(scoreboard, "team2", "§2", Util.color2 + "Começa em: ", Util.color1 + Settings.getInstance().getInt("Countdown"), 2);
        addScore(objective, "§1 ", 1);
        addScore(objective, Util.color1 + "Legendmc.com.br", 0);

        player.setScoreboard(scoreboard);
    }

    public static void updateScoreboard(Player player) throws SQLException {
        GameState gameState = LegendHG.getGameStateManager().getGameState();
        Scoreboard scoreboard = player.getScoreboard();

        Team team2 = scoreboard.getTeam("team2");
        Team team4 = scoreboard.getTeam("team4");
        Team team7 = scoreboard.getTeam("team7");
        Team team9 = scoreboard.getTeam("team9");

        if (team9 != null) {
            team9.setSuffix(Util.color1 + PlayerAliveManager.getInstance().getPlayersAlive().size());
        }

        if (team7 != null) {
            team7.setPrefix(Util.color2 + "Kit: ");
            team7.setSuffix(Util.color1 + LegendHG.getKitManager().getPlayerKitName(player));
        }

        if (GameHelper.getInstance().getKits() == 2) {
            Team team6 = scoreboard.getTeam("team6");
            if (team6 != null) {
                team6.setPrefix(Util.color2 + "Kit 2: ");
                team6.setSuffix(Util.color1 + LegendHG.getKitManager().getPlayerKitName2(player));
            }
        }

        if (team2 != null) {
            String time = Util.formatSeconds(CountDown.getInstance().getRemainingTime());
            team2.setSuffix(Util.color1 + time);
        }

        switch (gameState) {
            case COUNTDOWN:
                if (team2 != null) {
                    team2.setPrefix(Util.color2 + "Começa em: ");
                }
                break;
            case INVINCIBILITY:
                if (team4 != null) {
                    team4.setPrefix(Util.color1 + "Invenci");
                    team4.setSuffix(Util.color1 + "bilidade");
                }
                break;
            case RUNNING:
                if (team4 != null) {
                    team4.setPrefix(Util.color2 + "Feast em: ");
                    team4.setSuffix(Util.color1 + Util.formatSeconds(Settings.getInstance().getInt("Feast")));
                }
                if (team2 != null) {
                    team2.setPrefix(Util.color2 + "Tempo: ");
                }
                break;
            default:
                if (team2 != null) {
                    team2.setPrefix(Util.color2 + "Padrao demais: ");
                }
                break;
        }

        player.setScoreboard(scoreboard);
    }
}