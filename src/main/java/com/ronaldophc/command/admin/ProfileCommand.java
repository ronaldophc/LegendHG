package com.ronaldophc.command.admin;

import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import com.ronaldophc.util.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ProfileCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("profile")) {
            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage(Util.noConsole);
                return true;
            }

            Player player = (Player) commandSender;
            if (!player.isOp() && !player.hasPermission("legendhg.admin.profile")) {
                commandSender.sendMessage(Util.noPermission);
                return true;
            }

            Account account = AccountManager.getInstance().getOrCreateAccount(player);
            player.sendMessage("Profile Infomation:");
            player.sendMessage("§aActual Name: §f" + account.getActualName());
            player.sendMessage("§aUUID: §f" + account.getUUID());
            player.sendMessage("§aisAlive: §f" + account.isAlive());
            player.sendMessage("§aisSpec: §f" + account.isSpectator());
            player.sendMessage("§aVersion: §f" + account.getVersion());
            player.sendMessage("§aScore: §f" + account.getScore());
            player.sendMessage("§aKit 1: §f" + account.getKits().getPrimary().getName());
            player.sendMessage("§aKit 2: §f" + account.getKits().getSecondary().getName());
            player.sendMessage("§aChat: §f" + account.isChat());
            player.sendMessage("§aTell: §f" + account.isTell());
            player.sendMessage("§aVanish: §f" + account.isVanish());
            player.sendMessage("§aBuild: §f" + account.isBuild());
            player.sendMessage("§aSeeSpecs: §f" + account.isSeeSpecs());
            player.sendMessage(AccountManager.getInstance().getPlayersAlive().toString());

            return true;
        }
        return true;
    }
}
