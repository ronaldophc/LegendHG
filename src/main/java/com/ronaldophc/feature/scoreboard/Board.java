package com.ronaldophc.feature.scoreboard;

import com.ronaldophc.LegendHG;
import com.ronaldophc.constant.Scores;
import com.ronaldophc.constant.Tags;
import com.ronaldophc.database.PlayerSQL;
import com.ronaldophc.feature.Tag;
import com.ronaldophc.helper.GameHelper;
import com.ronaldophc.constant.GameState;
import com.ronaldophc.helper.Logger;
import com.ronaldophc.helper.Util;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public class Board implements Runnable {

    private static final Board instance = new Board();
    public static HashMap<UUID, Scores> playerScore = new HashMap<>();

    public Board() {
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player == null || !player.isOnline()) {
                continue;
            }

            try {
                if (!PlayerSQL.isPlayerLoggedIn(player)) {
                    continue;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            if (LegendHG.getGameStateManager().getGameState() == GameState.FINISHED) {
                removeScoreboard(player);
                continue;
            }

            if (player.getScoreboard().getObjective(LegendHG.getInstance().getName()) == null) {
                setupScoreboard(player);
            }

            try {
                update(player);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void setupScoreboard(Player player) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective(LegendHG.getInstance().getName(), "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        if (playerScore.get(player.getUniqueId()) == null) {
            try {
                playerScore.put(player.getUniqueId(), getScoreType(player));
            } catch (SQLException e) {
                Logger.logError(e.getMessage());
                throw new RuntimeException(e);
            }
        }

        Scores scoreType = playerScore.get(player.getUniqueId());
        switch (scoreType) {
            case COMPLETE:
                if (GameHelper.getInstance().getKits() == 2) {
                    ScoreComplete.createNewScoreboardTwoKits(player, objective, scoreboard);
                    break;
                }
                ScoreComplete.createNewScoreboardOneKit(player, objective, scoreboard);
                break;
            case SIMPLE:
                if (GameHelper.getInstance().getKits() == 2) {
                    ScoreSimple.createNewScoreboardTwoKits(player, objective, scoreboard);
                    break;
                }
                ScoreSimple.createNewScoreboardOneKit(player, objective, scoreboard);
                break;
            case SPEC:
                try {
                    ScoreSpec.createNewScoreboard(player, objective, scoreboard);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                break;
            default:
                removeScoreboard(player);
                break;
        }
    }

    private void update(Player player) throws SQLException {
        Scoreboard scoreboard = player.getScoreboard();
        String name = player.getCustomName();
        Team team = scoreboard.getTeam(name);
        Tags tag = Tag.getTag(player);

        player.setPlayerListName(tag.getColor() + tag.name() + " " + Util.color1 + name + " " + Util.color2 + player.getName());
        if (team == null) {
            setPlayerNameTag(player, tag.getColor() + tag.name() + " ", " " + Util.color2 + player.getName(), scoreboard);
        }

        Scores scoreType = playerScore.get(player.getUniqueId());
        switch (scoreType) {
            case COMPLETE:
                ScoreComplete.updateScoreboard(player);
                break;
            case SIMPLE:
                ScoreSimple.updateScoreboard(player, player.getScoreboard().getObjective(DisplaySlot.SIDEBAR));
                break;
            case SPEC:
                ScoreSpec.updateScoreboard(player);
                break;
            default:
                removeScoreboard(player);
                break;
        }
    }

    static void addScore(Objective objective, String entry, int score) {
        objective.getScore(entry).setScore(score);
    }

    static void addTeam(Scoreboard scoreboard, String teamName, String entry, String prefix, String suffix, int score) {
        addTeam(scoreboard, teamName, entry, prefix, suffix);
        scoreboard.getObjective(DisplaySlot.SIDEBAR).getScore(entry).setScore(score);
    }

    static void addTeam(Scoreboard scoreboard, String teamName, String entry, String prefix, String suffix) {
        Team team = scoreboard.registerNewTeam(teamName);
        team.addEntry(entry);
        team.setPrefix(prefix);
        team.setSuffix(suffix);
    }

    public void removeScoreboard(Player player) {
        player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
    }

    public static Scores getScoreType(Player player) throws SQLException {
        return PlayerSQL.getPlayerScore(player);
    }

    public static void setPlayerScore(Player player, Scores scoreType) throws SQLException {
        playerScore.replace(player.getUniqueId(), scoreType);
        if (scoreType != Scores.SPEC) {
            PlayerSQL.setPlayerScore(player, scoreType);
        }
    }

    private static void setPlayerNameTag(Player player, String prefix, String suffix, Scoreboard scoreboard) {
        String name = player.getCustomName();
        Team team = scoreboard.getTeam(name);

        if (team == null) {
            team = scoreboard.registerNewTeam(name);
        }

        team.setPrefix(prefix);
        team.setSuffix(suffix);
        team.addEntry(name);
    }

    public static Board getInstance() {
        return instance;
    }
}