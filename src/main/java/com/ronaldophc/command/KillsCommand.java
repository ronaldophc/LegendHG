package com.ronaldophc.command;

import com.ronaldophc.util.Util;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KillsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("kills")) {
            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage(Util.noConsole);
                return true;
            }

            Player player = (Player) commandSender;
            if (strings.length == 0) {
                Account account = AccountManager.getInstance().getOrCreateAccount(player);

                player.sendMessage(Util.color3 + "Kills: §c" + account.getKills());
                return true;
            }

            if (strings.length == 1) {
                Account account = AccountManager.getInstance().getAccountByName(strings[0]);
                if (account == null) {
                    player.sendMessage(Util.noPlayer);
                    return true;
                }

                player.sendMessage(Util.color3 + "Kills do jogador " + Util.color1 + strings[0] + Util.color3 + ": §c" + account.getKills());
                return true;
            }

            return false;
        }
        return true;
    }
}
