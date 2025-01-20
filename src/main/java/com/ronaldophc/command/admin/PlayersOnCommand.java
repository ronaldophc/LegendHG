package com.ronaldophc.command.admin;

import com.ronaldophc.player.account.AccountManager;
import com.ronaldophc.util.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class PlayersOnCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("playerson")) {
            commandSender.sendMessage(Util.admin + "Players online: " + AccountManager.getInstance().getPlayersAlive().size());
            AccountManager.getInstance().getPlayersAlive().forEach(player -> commandSender.sendMessage(player.getName()));
            return true;
        }
        return false;
    }
}
