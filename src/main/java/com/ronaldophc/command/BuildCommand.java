package com.ronaldophc.command;

import com.ronaldophc.util.Util;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BuildCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("build")) {
            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage(Util.noConsole);
                return true;
            }

            Player player = (Player) commandSender;
            if (!commandSender.isOp() && !commandSender.hasPermission("legendhg.admin.build")) {
                commandSender.sendMessage(Util.noPermission);
                return true;
            }

            Account account = AccountManager.getInstance().getOrCreateAccount(player);

            boolean isBuilding = account.isBuild();
            account.setBuild(!isBuilding);

            String active = isBuilding ? "desativou" : "ativou";
            player.sendMessage(Util.admin + "Você " + active + " a construção.");

            return true;
        }
        return true;
    }
}
