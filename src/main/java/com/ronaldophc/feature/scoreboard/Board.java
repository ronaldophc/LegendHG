package com.ronaldophc.feature.scoreboard;

import com.ronaldophc.LegendHG;
import com.ronaldophc.constant.GameState;
import com.ronaldophc.constant.Scores;
import com.ronaldophc.constant.Tags;
import com.ronaldophc.helper.GameHelper;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public class Board {

    @Getter
    private static final Board instance = new Board();
    public static HashMap<UUID, Scores> playerScore = new HashMap<>();

    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player == null || !player.isOnline()) {
                continue;
            }

            Account account = AccountManager.getInstance().getOrCreateAccount(player);

            if (!account.isLoggedIn()) {
                continue;
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

        Account account = AccountManager.getInstance().getOrCreateAccount(player);

        if (playerScore.get(player.getUniqueId()) == null) {
            playerScore.put(player.getUniqueId(), account.getScore());
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
                ScoreSimple.createNewScoreboard(player, objective, scoreboard);
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
        setPlayerNameTag(player);

        Scores scoreType = playerScore.get(player.getUniqueId());
        switch (scoreType) {
            case COMPLETE:
                ScoreComplete.updateScoreboard(player);
                break;
            case SIMPLE:
                ScoreSimple.updateScoreboard(player);
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

    public static void setPlayerScore(Player player, Scores scoreType) throws SQLException {
        playerScore.replace(player.getUniqueId(), scoreType);
        if (scoreType != Scores.SPEC) {
            Account account = AccountManager.getInstance().getOrCreateAccount(player);
            account.setScore(scoreType);
        }
    }

    private static void setPlayerNameTag(Player player) {
        for (Player player1 : Bukkit.getOnlinePlayers()) {
            Scoreboard scoreboard1 = player1.getScoreboard();

            Account account = AccountManager.getInstance().getOrCreateAccount(player);

            String name = player.getName();
            Tags tag = account.getTag();
            Team team = scoreboard1.getTeam(name);

            if (team == null) {
                team = scoreboard1.registerNewTeam(name);
            }

            team.setPrefix(tag.getColor() + tag.name() + ChatColor.RESET + " ");
//        team.setSuffix(suffix);
            team.addEntry(account.getActualName());
        }
    }

}