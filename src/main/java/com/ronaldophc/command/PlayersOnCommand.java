package com.ronaldophc.command;

import com.ronaldophc.LegendHG;
import com.ronaldophc.player.account.AccountManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class PlayersOnCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("playerson")) {
            commandSender.sendMessage("Players online: " + LegendHG.getAccountManager().getPlayersAlive().size());
            return true;
        }
        return false;
    }
}
