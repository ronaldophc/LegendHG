package com.ronaldophc.command;

import com.ronaldophc.LegendHG;
import com.ronaldophc.constant.Scores;
import com.ronaldophc.feature.scoreboard.Board;
import com.ronaldophc.helper.Util;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
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

            Account account = AccountManager.getInstance().getOrCreateAccount(player);

            Board.getInstance().removeScoreboard(player);

            if (Board.playerScore.get(player.getUniqueId()) == null) {
                Board.playerScore.put(player.getUniqueId(), account.getScore());
            }

            if (account.isSpectator()) {
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