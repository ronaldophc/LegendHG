package com.ronaldophc.command;

import com.ronaldophc.LegendHG;
import com.ronaldophc.helper.Util;
import com.ronaldophc.player.account.Account;
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

            Account account = LegendHG.getAccountManager().getOrCreateAccount(player);
            player.sendMessage("Profile Infomation:");
            player.sendMessage("§aName: §f" + account.getActualName());
            player.sendMessage("§aUUID: §f" + account.getUUID());
            player.sendMessage("§aKits: §f" + account.getKits().toString());
            player.sendMessage("§aisAlive: §f" + account.isAlive());
            player.sendMessage("§aisSpec: §f" + account.isSpectator());

            return true;
        }
        return false;
    }
}
