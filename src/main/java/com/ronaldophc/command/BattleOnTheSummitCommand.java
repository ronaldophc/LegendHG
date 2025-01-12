package com.ronaldophc.command;

import com.ronaldophc.LegendHG;
import com.ronaldophc.constant.GameState;
import com.ronaldophc.feature.battleonthesummit.SummitManager;
import com.ronaldophc.helper.Util;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BattleOnTheSummitCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("rdc")) {
            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage(Util.noConsole);
                return true;
            }

            Player player = (Player) commandSender;
            GameState gameState = LegendHG.getGameStateManager().getGameState();
            if (gameState != GameState.COUNTDOWN) {
                player.sendMessage("§cMinigame disponível apenas antes da partida iniciar!");
                return true;
            }

            SummitManager summitManager = SummitManager.getInstance();

            Account account = AccountManager.getInstance().getOrCreateAccount(player);
            if (summitManager.getAccounts().contains(account)) {
                summitManager.playerLose(player);
                player.sendMessage("§aVocê saiu do minigame " + Util.color1 + "Rei do Cume§a!");
                return true;
            }

            summitManager.playerJoin(player);
            player.sendMessage("§aVocê entrou no minigame " + Util.color1 + "Rei do Cume§a!");

            return true;
        }
        return false;
    }
}
