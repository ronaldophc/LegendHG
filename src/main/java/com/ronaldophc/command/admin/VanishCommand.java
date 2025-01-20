package com.ronaldophc.command.admin;

import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import com.ronaldophc.util.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VanishCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("vanish")) {
            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage(Util.noConsole);
                return true;
            }

            Player player = (Player) commandSender;
            if (!player.isOp() && !commandSender.hasPermission("legendhg.admin.vanish")) {
                player.sendMessage(Util.noPermission);
                return true;
            }

            Account account = AccountManager.getInstance().getOrCreateAccount(player);
            account.setVanish(!account.isVanish());

            if (account.isVanish()) {
                player.sendMessage(Util.admin + "Você ativou a invisibilidade");
            } else {
                player.sendMessage(Util.admin + "Você desativou a invisibilidade");
            }

            return true;
        }
        return true;
    }
}
