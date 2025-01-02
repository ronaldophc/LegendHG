package com.ronaldophc.command;

import com.ronaldophc.LegendHG;
import com.ronaldophc.helper.Util;
import com.ronaldophc.player.account.Account;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TellCommand implements CommandExecutor, TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("tell")) {
            List<String> playerNames = new ArrayList<>();
            for (Account account : LegendHG.getAccountManager().getAccounts()) {
                Player player = account.getPlayer();
                if (player.isOnline() && player != commandSender) {
                    playerNames.add(account.getActualName());
                }
            }
            return playerNames;
        }
        return Collections.emptyList();
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("tell")) {
            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage(Util.noConsole);
                return true;
            }

            Player player = (Player) commandSender;

            if (strings.length < 2) {
                player.sendMessage(Util.usage("/tell <player> <message>"));
                return true;
            }

            Account account = LegendHG.getAccountManager().getAccountByActualName(strings[0]);

            if (account == null) {
                player.sendMessage(Util.noPlayer);
                return true;
            }

            Player target = account.getPlayer();
            if (target == null || !(target.isOnline())) {
                player.sendMessage(Util.error + "Player não encontrado ou offline.");
                return true;
            }

            StringBuilder message = new StringBuilder();
            for (int i = 1; i < strings.length; i++) {
                message.append(strings[i]).append(" ");
            }

            player.sendMessage("§8[§7Você §8» §7" + account.getActualName() + "§8] §f" + message);
            target.sendMessage("§8[§7" + LegendHG.getAccountManager().getOrCreateAccount(player).getActualName() + " §8» §7Você§8] §f" + message);

            return true;
        }
        return false;
    }
}
