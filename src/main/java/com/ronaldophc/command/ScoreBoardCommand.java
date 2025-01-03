package com.ronaldophc.command;

import com.ronaldophc.helper.Logger;
import com.ronaldophc.helper.Util;
import com.ronaldophc.player.PlayerSpectatorManager;
import com.ronaldophc.feature.scoreboard.Board;
import com.ronaldophc.constant.Scores;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class ScoreBoardCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("scoreboard")) {

            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage(Util.noConsole);
                return true;
            }

            Player player = (Player) commandSender;

            Board.getInstance().removeScoreboard(player);

            if (Board.playerScore.get(player.getUniqueId()) == null) {
                try {
                    Board.playerScore.put(player.getUniqueId(), Board.getScoreType(player));
                } catch (SQLException e) {
                    Logger.logError("Failed to get ScoreType: " + e.getMessage());
                    throw new RuntimeException(e);
                }
            }

            if (PlayerSpectatorManager.getInstance().isPlayerSpectating(player)) {
                if (Board.playerScore.get(player.getUniqueId()) != Scores.SPEC) {
                    try {
                        Board.setPlayerScore(player, Scores.SPEC);
                        player.sendMessage(Util.title + " > " + Util.color3 + "Alterado para " + Util.bold + Util.success + "scoreboard visivel");
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    return true;
                }
                try {
                    Board.setPlayerScore(player, Scores.NONE);
                    player.sendMessage(Util.title + " > " + Util.color3 + "Alterado para " + Util.bold + Util.success + "scoreboard invisivel");
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                return true;
            }

            switch (Board.playerScore.get(player.getUniqueId())) {
                case SIMPLE:
                    try {
                        Board.setPlayerScore(player, Scores.COMPLETE);
                        player.sendMessage(Util.title + " > " + Util.color3 + "Alterado para " + Util.bold + Util.success + "scoreboard completa");
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case COMPLETE:
                    try {
                        Board.setPlayerScore(player, Scores.NONE);
                        player.sendMessage(Util.title + " > " + Util.color3 + "Alterado para " + Util.bold + Util.success + "scoreboard invisivel");
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                default:
                    try {
                        Board.setPlayerScore(player, Scores.SIMPLE);
                        player.sendMessage(Util.title + " > " + Util.color3 + "Alterado para " + Util.bold + Util.success + "scoreboard simples");
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    break;
            }
            return true;
        }
        return false;
    }
}